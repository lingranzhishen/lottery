package com.dhu.common;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.dhu.framework.conf.GlobalConfig;


public class PropertyConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected Properties mergeProperties() throws IOException {

		Properties superProps = super.mergeProperties();
		superProps.put("env",GlobalConfig.getEnv());
		superProps.putAll(ResourceConfig.getAllMap());
		System.out.println(">>>>>当前环境 env:" + superProps.getProperty("env"));
		return superProps;
	}

}
