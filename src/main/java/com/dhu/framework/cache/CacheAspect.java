package com.dhu.framework.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.dhu.framework.aop.BaseAspect;
import com.dhu.framework.cache.annotation.CheckCache;
import com.dhu.framework.cache.annotation.EvictCache;
import com.dhu.framework.conf.GlobalConfig;
import com.dhu.framework.utils.SpringContextHolder;

@Aspect
public class CacheAspect extends BaseAspect {
	
	@Around("execution(public * *(..)) and @annotation(checkCache)")
	public Object get(ProceedingJoinPoint jp, CheckCache checkCache) throws Throwable {
		String namespace = checkCache.namespace();
		if(namespace == null || namespace.equals("")){
			namespace = GlobalConfig.getProjectCode();
		}
		String fullkey = getKey(jp,checkCache.key(),null,checkCache.autoKeyPre());
		fullkey = CacheUtil.getCacheKey(namespace, fullkey);
		
		CacheManager cache = (CacheManager) SpringContextHolder.getBean(checkCache.type());
		
       	Object rs = null;
		try {
			if(checkCache.timeToIdle() > 0){//通过GetAndTouch实现活跃时间
				rs = cache.getAndTouch(fullkey,checkCache.timeToIdle());
			}else{
				rs = cache.get(fullkey);
			}
		} catch (Exception e) {
			log.error("缓存获取失败["+e.getMessage()+"]");
		}
		if(rs == null || (!checkCache.cacheNull() && rs instanceof EmptyCacheObject)){
			rs = getResult(jp);
			if(rs == null && checkCache.cacheNull()){//缓存空值
				rs = new EmptyCacheObject();
			}
			if(rs != null){
				try {
					cache.put(fullkey,rs,checkCache.timeToIdle()>0?checkCache.timeToIdle():checkCache.timeToLive());
				} catch (Exception e) {
					log.error("缓存保存失败["+e.getMessage()+"]");
				}
			}
		}
      	if(rs == null || rs instanceof EmptyCacheObject){
       		return null;
       	}
		return rs;
	}

	@AfterReturning(pointcut = "@annotation(evictCache)", returning = "retval")
	public void remove(JoinPoint jp, EvictCache evictCache, Object retval) {
		String namespace = evictCache.namespace();
		if(namespace == null || namespace.equals("")){
			namespace = GlobalConfig.getProjectCode();
		}
		String keystr = evictCache.key();
		if(keystr == null || keystr.isEmpty()){
			log.error("EvictCache Key不能为空");
		}
		String[] keys = keystr.split(",");
		String fullkey = null;
		for(String key : keys){
			fullkey = getKey(jp,key,retval,false);
			fullkey = CacheUtil.getCacheKey(namespace, fullkey);
			try {
				CacheManager cache = (CacheManager) SpringContextHolder.getBean(evictCache.type());
				cache.delete(fullkey);
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
	}
	
	/**
	 * 计算缓存key
	 * @param jp
	 * @param namespace 命名空间
	 * @param annKey key表达式
	 * @param retval 返回结果(可为空)
	 * @return 缓存key
	 */
    private String getKey(JoinPoint jp,String annKey,Object retval,boolean autoKeyPre){
    	String finalKey;
        if (annKey == null || annKey.equals("")) {
        	String fullName = getAutoKeyPre(jp.getSignature());
        	Object[] arr = jp.getArgs();
        	if(arr != null && arr.length >0){
        		boolean canAppend = true;
        		StringBuffer app = new StringBuffer();
        		for(Object o : arr){//当参数都为常见类型时 直接进行拼接
        			if(o==null || o.getClass().isPrimitive() || (o instanceof String) 
        					|| (o instanceof Number) || (o instanceof Boolean) || (o instanceof Character) ){
        				app.append(o);
        			}else{
        				canAppend = false;
        				break;
        			}
        		}
        		if(canAppend){
        			finalKey = fullName+"_"+app;
        		}else{
        			finalKey = fullName+"_"+Arrays.hashCode(arr);
        		}
        	}else{
        		finalKey = fullName;
        	}
        } else {
        	Map<String, Object> context = buildContext(jp);
        	EvaluationContext evalContext = new StandardEvaluationContext();
        	for (String key : context.keySet()) {
        		evalContext.setVariable(key, context.get(key));
        	}
        	if(retval != null){
        		evalContext.setVariable("retval", retval);
        	}
        	ExpressionParser parser = new SpelExpressionParser();
            String newKey = parser.parseExpression(annKey).getValue(evalContext).toString();
            if(autoKeyPre){
            	finalKey = getAutoKeyPre(jp.getSignature())+"_"+newKey;
            }else{
            	finalKey = newKey;
            }
        }
        return finalKey;
    }
    
    private String getAutoKeyPre(Signature sig){
    	String className = sig.getDeclaringTypeName();
    	int index = className.lastIndexOf(".");
    	if(index > 0){
    		className = className.substring(index+1);
    	}
    	return className+"."+sig.getName();
    }
    
    private boolean warnNoDebugSymbolInformation;
    public ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public Map<String, Object> buildContext(JoinPoint jp) {
        Map<String, Object> context = new HashMap<String, Object>();
        Object[] args = jp.getArgs();
        String[] paramNames = getParameterNames(jp);
        if (paramNames == null) {
            if (!warnNoDebugSymbolInformation) {
                warnNoDebugSymbolInformation = true;
                System.out.println("Unable to resolve method parameter names for method: "
                        + jp.getStaticPart().getSignature()
                        + ". Debug symbol information is required if you are using parameter names in expressions.");
            }
        } else {
            for (int i = 0; i < args.length; i++)
                context.put(paramNames[i], args[i]);
        }
        if (!context.containsKey("_this"))
            context.put("_this", jp.getThis());
        if (!context.containsKey("target"))
            context.put("target", jp.getTarget());
        return context;
    }


    public String[] getParameterNames(JoinPoint jp) {
        if (!jp.getKind().equals(JoinPoint.METHOD_EXECUTION))
            return null;
        Class<?> clz = jp.getTarget().getClass();
        MethodSignature sig = (MethodSignature) jp.getSignature();
        Method method;
        try {
            method = clz.getDeclaredMethod(sig.getName(),
                    sig.getParameterTypes());
            if (method.isBridge())
                method = BridgeMethodResolver.findBridgedMethod(method);
            return getParameterNames(method,null);
        } catch (Exception e) {
            return null;
        }
    }
    private String[] getParameterNames(Method method, Constructor<?> ctor) {
        Annotation[][] annotations = method != null ? method
                .getParameterAnnotations() : ctor.getParameterAnnotations();
        String[] names = new String[annotations.length];
        boolean allbind = true;
        for (int i = 0; i < annotations.length; i++) {
            allbind = false;
        }
        if (!allbind) {
            String[] namesDiscovered = method != null ? parameterNameDiscoverer
                    .getParameterNames(method) : parameterNameDiscoverer
                    .getParameterNames(ctor);
            if (namesDiscovered == null)
                return null;
            for (int i = 0; i < names.length; i++)
                if (names[i] == null)
                    names[i] = namesDiscovered[i];
        }
        return names;
    }
}
