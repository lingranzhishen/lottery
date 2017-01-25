package com.dhu.framework.utils;   
   
import org.springframework.beans.BeansException;    
import org.springframework.context.ApplicationContext;    
import org.springframework.context.ApplicationContextAware;    

/**********
 * spring配置容器里,可用于动态获取spring中的Bean
 * @author guofei
 */
public class SpringContextHolder implements ApplicationContextAware {   
   
    // Spring应用上下文环境   
    private static ApplicationContext appContext;    

    public void setApplicationContext(ApplicationContext applicationContext) {   
    	SpringContextHolder.appContext = applicationContext;   
    }   
   
       
    public static ApplicationContext getAppContext() {   
        return appContext;   
    }   
   
       
    public static Object getBean(String name) throws BeansException {   
        return appContext.getBean(name);   
    }   
   
 
}   
