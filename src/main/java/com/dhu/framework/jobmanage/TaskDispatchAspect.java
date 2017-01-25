package com.dhu.framework.jobmanage;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.dhu.framework.aop.BaseAspect;
import com.dhu.framework.conf.GlobalConfig;
import com.dhu.framework.jobmanage.dto.ResultDTO;
import com.dhu.framework.jobmanage.dto.TaskDispatchDTO;
import com.dhu.framework.utils.IPUtil;


/**
 * 用于控制调度任务
 * @author guofei
 */
@Aspect
public class TaskDispatchAspect extends BaseAspect{
	
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private DefaultListableBeanFactory configFatory;
    
    private static ThreadLocal<String> clientIds = new ThreadLocal<String>();
    
    @Resource(name="taskClientHik")
    private ITaskDispatchFacade taskFacade;
    
    /**
     * 自动拦截以
     */
    @Pointcut("@annotation(com.dhu.framework.jobmanage.TaskOnOff)")
    private void cutTaskOnOff() {}
    
    /**
     * 注解方式拦截
     */
    @Pointcut("@annotation(com.dhu.framework.jobmanage.TaskDispatch)")
    private void cutTaskDispatch() {}

	/**
     * Controller结尾或者注解都进行拦截
     * @param thisJoinPoint 切点
     * @return 返回结果
     * @throws Throwable
     */
    @Around("cutTaskOnOff() || cutTaskDispatch()")
    public Object around(ProceedingJoinPoint thisJoinPoint) throws Throwable {
    	
    	Signature sig = thisJoinPoint.getSignature();
    	Method m = ((MethodSignature)sig).getMethod();
    	
    	 //检查可运行开关
    	if(!checkCanRun(m.getAnnotation(TaskOnOff.class))){
    		logger.debug("未达到运行条件["+sig.getDeclaringTypeName()+"]["+sig.getName()+"]");
    		return null;
    	}
    	Object obj = null;
    	
    	TaskDispatch td = m.getAnnotation(TaskDispatch.class);
    	//不需要远程调度 直接执行
    	if(!canDispatch(td)){
    		logger.info("["+sig.getDeclaringTypeName()+"."+sig.getName()+"]开始执行......");
    		obj = getResult(thisJoinPoint);
    		logger.info("["+sig.getDeclaringTypeName()+"."+sig.getName()+"]执行结束!");
    		return obj;
    	}
    	TaskDispatchDTO dto = new TaskDispatchDTO();
    	dto.setEnv(GlobalConfig.getEnv());
    	dto.setLockTimes(td.lockTimes());
    	dto.setProjectCode(GlobalConfig.getProjectCode());
    	dto.setClassName(sig.getDeclaringTypeName());
    	dto.setMethodName(sig.getName());
    	
    	
    	boolean issuccess = getTaskLock(dto, td.repeatCount());
    	if(issuccess){//拿到锁
    		logger.info("["+dto.getClassName()+"."+dto.getMethodName()+"]开始执行......");
    		obj = getResult(thisJoinPoint);
    		logger.info("["+dto.getClassName()+"."+dto.getMethodName()+"]执行结束!");
    		//提交方法执行结束消息
    		try {
    			ResultDTO result = taskFacade.commitComlete(dto);
    			logger.debug("远程提交任务执行完成消息消息["+result.isSuccess()+"]");
    		} catch (Exception e) {
    			logger.error("远程提交任务执行完成消息异常,"+e.getMessage());
    		}
    	}else{	//没有拿到锁 不能执行
    		logger.debug("定时任务已被锁定["+sig.getDeclaringTypeName()+"]["+sig.getName()+"]");
    	}
    	return obj;
    }
    
    /**
     * 竞争锁  竞争规则:
     * 1.远程调用竞争锁,若正常返回,以远程结果为准
     * 2.若远程调用异常或没有获取到所 等待10秒重试 直到超过指定次数
     * @param dto
     * @return 成功/失败
     * @throws Exception
     */
    private boolean getTaskLock(TaskDispatchDTO dto,int repeatCount) throws Exception{
    	//发出远程调度请求
    	ResultDTO result = null;
    	boolean issuccess = false;
    	if(repeatCount <= 0) repeatCount = 1;
    	for(int i = 0; i < repeatCount; i++){
    		try {
    			result = taskFacade.getTaskLock(dto);
    		} catch (Exception e) {
    			logger.error("获取锁失败["+dto.getClassName()+"]["+dto.getMethodName()+"]["+i+"]");
    		}
    		if(result != null){
    			issuccess = result.isSuccess();
    			//定时任务已被其他客户端完成,本客户端不必再执行
    			if(result.getStatus() == 1) break;
    			
    			if(result.getClientId() != null){
    				dto.setClientId(result.getClientId());
    				clientIds.set(result.getClientId());
    			}
    		}
    		if(issuccess){
    			break;
    		}
    		if(i < repeatCount-1){
    			logger.debug("未获取锁,等待10秒重试.....");
    			Thread.sleep(10000);
    		}
    	}
    	//true表示拿到锁
		return issuccess;
    }
    
    /**
     * 检查定时任务是否满足执行要求
     * @param to 注解
     */
    private boolean checkCanRun(TaskOnOff to){
    	if(to == null) return true;
    	String env = to.canRunEnv();
    	//env不匹配不可执行
    	if(!env.isEmpty() && !env.contains(GlobalConfig.getEnv())){
    		return false;
    	}
    	String key = to.canRunIpProp();
    	if(key.isEmpty()) return true;
    	//获取key对应的参数值
    	String ips = configFatory.resolveEmbeddedValue(key);
    	if(ips == null || ips.isEmpty()){
    		return true;
    	}
    	if(ips.contains("${")){
    		logger.debug("参数["+key+"]获取失败~!,不进行IP判断控制");
    		return true;
    	}
    	String[] iparr = ips.split(",");
    	for(String p : iparr){
    		if(IPUtil.getLocalIpSet().contains(p)) return true;
    	}
    	return false;
    }
    /**
     * 检查是否需要远程调度
     * @param td 注解
     */
    private boolean canDispatch(TaskDispatch td){
    	if(td == null) return false;
    	String dispatchEnv = td.dispatchEnv();
    	//env不匹配不可调度
    	if(!dispatchEnv.isEmpty() && !dispatchEnv.contains(GlobalConfig.getEnv())){
    		return false;
    	}
    	return true;
    }
    
    /**
     * 定时任务抛出异常后执行
     * @param e
     */
    @AfterThrowing(pointcut = "cutTaskDispatch()", throwing = "e")
    public void doAfterThrowing(JoinPoint jp,Exception e) {
    	Signature sig = jp.getSignature();
    	Method m = ((MethodSignature)sig).getMethod();
    	if(!canDispatch(m.getAnnotation(TaskDispatch.class))) return;
      	try {
       		TaskDispatchDTO dto = new TaskDispatchDTO();
           	dto.setEnv(GlobalConfig.getEnv());
           	dto.setProjectCode(GlobalConfig.getProjectCode());
           	dto.setClassName(sig.getDeclaringTypeName());
           	dto.setMethodName(sig.getName());
           	dto.setClientId(clientIds.get());
   			taskFacade.actionError(dto,e.getMessage());
   			logger.debug("远程提交任务执行失败消息完成");
   		} catch (Exception ee) {
   			logger.error("远程提交任务执行失败消息异常:"+ee.getMessage());
   		}
    }
}
