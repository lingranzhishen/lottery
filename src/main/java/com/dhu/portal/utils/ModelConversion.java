package com.dhu.portal.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Zouyuan on 2016/3/7.
 * ModelToModel
 */
public class ModelConversion {
    public static enum ConvertType {
        string_to_int,
        int_to_string,
        long_to_int,
        int_to_long,
        long_to_string,
        string_to_long,
        date_to_string,
        string_to_date,
        auto
    }


    public static Object convertType(Object source,Class sourceType,Class targetType,ConvertParameter convertParameter){
        //System.out.println(sourceType+" to "+ targetType );
        if(sourceType.equals(targetType)) {
            return source;
        }else if(targetType.equals(String.class)){
            if(sourceType.equals(java.util.Date.class)){
                SimpleDateFormat dateFormat = new SimpleDateFormat(convertParameter.dateFormat());
                return dateFormat.format((java.util.Date)source);
            }
            else return String.valueOf(source);
        }else if((targetType.equals(Integer.class)||targetType.equals(int.class))){
        	if(sourceType.equals(String.class)){
        		return Integer.valueOf((String)source);
        	} else if(sourceType.equals(Double.class)){
        		return Integer.valueOf(((Double)source).intValue());
        	} else {
        		return null;
        	}
            
        }else if((targetType.equals(Long.class)||targetType.equals(long.class))&&sourceType.equals(String.class)){
            return Long.valueOf((String)source);
        }else if(targetType.equals(Long.class)&&sourceType.equals(Integer.class)){
            return source;
        }else if(targetType.equals(java.util.Date.class)&&sourceType.equals(String.class)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(convertParameter.dateFormat());
            try {
                return dateFormat.parse((String)source);
            } catch (ParseException e) {
                //e.printStackTrace();
                return null;
            }
        }else return null;

    }


	public static Object convert(Object source, Class targetClass) {
		Object target = null;
		try {
			target = targetClass.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Field f[] = targetClass.getDeclaredFields();
		for (Field convertField : f) {
			String fname = convertField.getName();
			ConvertParameter p = convertField
					.getAnnotation(ConvertParameter.class);
			if (p != null && p.isAdd())
				continue;
			if (p != null && !p.value().equals(""))
				fname = p.value();

			try {
				Field field = source.getClass().getDeclaredField(fname);
				convertField.setAccessible(true);
				field.setAccessible(true);
				if (field.get(source) == null)
					continue;
				Object obj = field.get(source);
				convertField.set(
						target,
						convertType(obj, field.getType(),
								convertField.getType(), p));
			} catch (Exception e) {
				
			}
		}
		return target;

	}

    public static  void show(Object obj){
        Field f[] = obj.getClass().getDeclaredFields();
        for (Field convertField : f) {
            String fname = convertField.getName();
            try {
                convertField.setAccessible(true);
                System.out.println(fname+" "+convertField.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
