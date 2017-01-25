package com.dhu.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhu.common.model.Column;
import com.dhu.common.model.SheetInfo;
import com.dhu.common.model.SheetObject;

public class ExcelUtil {

	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	//传入List<?>格式数据的接口-----------------------------------------------------------------
	/**
	 * 导出单个sheet的execl，不使用模板
	 * @param out
	 * @param c
	 * @param data
	 * @param sheetName
	 */
	public static void exportExcel(OutputStream out,Class<?> c,List<?> data,String sheetName){
		exportExcel(out, c, data, sheetName, null, 1);
	}
	
	/**
	 * 导出单个sheet的execl，使用模板
	 * @param out
	 * @param c
	 * @param data
	 * @param excelTempName 模板文件名，例如：temp.xls    模板文件要放在freemarker下
	 * @param headerNum 表头有几行
	 */
	public static void exportExcel(OutputStream out,Class<?> c,List<?> data, 
			String excelTempName, int headerNum){
		exportExcel(out, c, data, null, excelTempName, headerNum);
	}
	
	/**
	 * 导出一个有多个表格的工作薄，不使用模板
	 * @param out
	 * @param sheetList 表格数据列表
	 */
	public static void exportExcel(OutputStream out, List<SheetInfo> sheetList) {
		exportExcel(out, sheetList, null);
	}
	
	/**
	 * 导出多个sheet的execl，使用模板
	 * @param out
	 * @param sheetList
	 * @param excelTempName 模板文件名，例如：temp.xls    模板文件要放在freemarker下
	 */
	public static void exportExcel(OutputStream out, List<SheetInfo> sheetList
			, String excelTempName) {
		// 声明一个工作薄
		HSSFWorkbook workbook = null;
		Boolean havaTemp = false;
		if(excelTempName == null){
			workbook = new HSSFWorkbook();
		}else{
			havaTemp = true;
			try {
				FileInputStream excelTemp = FileUtil.getFileStream(excelTempName);
				workbook = new HSSFWorkbook(excelTemp);
			} catch (Exception e) {
				log.error("导出模板有问题：", e);
			}
		}
		int sheetIndex = 0;
		for (SheetInfo sheetInfo : sheetList) {
			createSheet(sheetInfo.getC(), sheetInfo.getData(),
					sheetInfo.getSheetName(), workbook, havaTemp, sheetIndex, sheetInfo.getHeaderNum());
			sheetIndex ++;
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("异常：",e);
		}
	}
	
	//传入List<Object>格式数据的接口-----------------------------------------------------
	/**
	 * 导出单个sheet的execl，不使用模板
	 * @param out
	 * @param data 查数据的sql使用LinkedHashMap为resultType才能保证顺序
	 * @param sheetName
	 */
	public static void exportExcelObject(OutputStream out,List<Object> data,String sheetName){
		exportExcelObject(out, data, sheetName, null, 1);
	}
	
	/**
	 * 导出单个sheet的execl，使用模板
	 * @param out
	 * @param data 查数据的sql使用LinkedHashMap为resultType才能保证顺序
	 * @param excelTempName 模板文件名，例如：temp.xls    模板文件要放在freemarker下
	 * @param headerNum 表头有几行
	 */
	public static void exportExcelObject(OutputStream out,List<Object> data, 
			String excelTempName, int headerNum){
		exportExcelObject(out, data, null, excelTempName, headerNum);
	}
	
	/**
	 * 导出多个sheet的execl，不使用模板
	 * @param sheetList 表格数据列表
	 */
	public static void exportExcelObject(OutputStream out, List<SheetObject> sheetList) {
		exportExcelObject(null, out, sheetList);
	}
	
	/**
	 * 导出多个sheet的execl，使用模板
	 * @param excelTempName 模板文件名，例如：temp.xls    模板文件要放在freemarker下
	 * @param out
	 * @param sheetList
	 */
	public static void exportExcelObject(String excelTempName, OutputStream out, 
			List<SheetObject> sheetList) {
		// 声明一个工作薄
		HSSFWorkbook workbook = null;
		Boolean havaTemp = false;
		if(excelTempName == null){
			workbook = new HSSFWorkbook();
		}else{
			havaTemp = true;
			try {
				FileInputStream excelTemp = FileUtil.getFileStream(excelTempName);
				workbook = new HSSFWorkbook(excelTemp);
			} catch (Exception e) {
				log.error("导出模板有问题：", e);
			}
		}
		int sheetIndex = 0;
		for (SheetObject sheetObject : sheetList) {
			createSheet(sheetObject.getData(), sheetObject.getSheetName(), 
					workbook, havaTemp, sheetIndex, sheetObject.getHeaderNum());
			sheetIndex ++;
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("异常：",e);
		}
	}
	
	//辅助方法--------------------------------------------------------------
	/**
	 * 导出单个sheet的execl，传入List<?>格式数据
	 * @param out
	 * @param c
	 * @param data
	 * @param sheetName
	 * @param excelTempName 模板文件名，例如：temp.xls    模板文件要放在freemarker下
	 * @param headerNum 表头有几行
	 */
	private static void exportExcel(OutputStream out,Class<?> c,List<?> data,
			String sheetName, String excelTempName, int headerNum){
		// 声明一个工作薄
		HSSFWorkbook workbook = null;
		Boolean havaTemp = false;
		if(excelTempName == null){
			workbook = new HSSFWorkbook();
		}else{
			havaTemp = true;
			try {
				FileInputStream excelTemp = FileUtil.getFileStream(excelTempName);
				workbook = new HSSFWorkbook(excelTemp);
			} catch (Exception e) {
				log.error("导出模板有问题：", e);
			}
		}
		// 生成一个表格
		createSheet(c,data,sheetName,workbook,havaTemp,0,headerNum);
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("异常：",e);
		}
	}
	
	/**
	 * 导出单个sheet的execl，传入List<Object>数据
	 * @param out
	 * @param data
	 * @param sheetName
	 * @param excelTempName 模板文件名，例如：temp.xls    模板文件要放在freemarker下
	 * @param headerNum 表头有几行
	 */
	private static void exportExcelObject(OutputStream out,List<Object> data,
			String sheetName, String excelTempName, int headerNum){
		// 声明一个工作薄
		HSSFWorkbook workbook = null;
		Boolean havaTemp = false;
		if(excelTempName == null){
			workbook = new HSSFWorkbook();
		}else{
			havaTemp = true;
			try {
				FileInputStream excelTemp = FileUtil.getFileStream(excelTempName);
				workbook = new HSSFWorkbook(excelTemp);
			} catch (Exception e) {
				log.error("导出模板有问题：", e);
			}
		}
		// 生成一个表格
		createSheet(data,sheetName,workbook,havaTemp,0,headerNum);
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("异常：",e);
		}
	}
	
	/**
	 * 生成一个表格
	 */
	private static void createSheet(Class<?> c,List<?> data,String sheetName,
			HSSFWorkbook workbook,Boolean havaTemp, int sheetIndex, int headerNum ){
		Map<String,String> fieldMap=new HashMap<String, String>();
		Map<String,Integer> fields=getField(c,fieldMap);
		// 生成一个表格
		HSSFSheet sheet = null;
		if(havaTemp){
			sheet = workbook.getSheetAt(sheetIndex);
		}else{
			sheet = workbook.createSheet(sheetName);
			createHeader(sheet,fields);//创建表头
		}
		HSSFRow row=null;
		Set<String> keySet=fields.keySet();
		try {
			for(int i=0;i<data.size();i++){//创建数据行
				row=sheet.createRow(i+headerNum);
				HSSFCell cell=null;
				for(String key:keySet){
					String valueString=(String) c.getMethod("get"+fieldMap.get(key).substring(0, 1).toUpperCase()+ fieldMap.get(key).substring(1)).invoke(data.get(i));
					cell=row.createCell(fields.get(key));
					cell.setCellValue(valueString);
				}
			}
		} catch (Exception e) {
			log.error("异常：",e);
		} 
	}
	
	/**
	 * 生成一个表格
	 */
	@SuppressWarnings("unchecked")
	private static void createSheet(List<Object> data,String sheetName,
			HSSFWorkbook workbook,Boolean havaTemp, int sheetIndex, int headerNum ){
		if(data != null && data.size() > 0){
			Map<String,Object> fristMap = (Map<String,Object>)data.get(0);
			Set<String> headerSet = fristMap.keySet();
			// 生成一个表格
			HSSFSheet sheet = null;
			if(havaTemp){
				sheet = workbook.getSheetAt(sheetIndex);
			}else{
				sheet = workbook.createSheet(sheetName);
				createHeader(sheet,headerSet);//创建表头
			}
			
			HSSFRow row=null;
			try {
				for(int i=0;i<data.size();i++){//创建数据行
					row=sheet.createRow(i+headerNum);
					Map<String,Object> thisMap = (Map<String,Object>)data.get(i);
					int cellNum = 0;
					for(String key:headerSet){
						Object valueObject = thisMap.get(key);
						if(valueObject != null){
							String valueString = valueObject.toString();
							HSSFCell cell=row.createCell(cellNum);
							cell.setCellValue(valueString);
						}
						cellNum++;
					}
				}
			} catch (Exception e) {
				log.error("写表单数据异常：", e);
			}
		}
	}
	
	/**
	 * 获取对象属性名
	 * @param c
	 * @return
	 */
	private static Map<String,Integer> getField(Class<?> c,Map<String, String> fieldMap){
		Field[] fields=c.getDeclaredFields();
		Map<String,Integer> reMap=new HashMap<String,Integer>();
		for(Field field:fields){
			Column column = field.getAnnotation(Column.class);
			if(column != null){
				reMap.put(column.label(),column.index());
				fieldMap.put(column.label(), field.getName());
			}
		}
		return reMap;
	}
	/**
	 * 创建表头
	 * @param sheet
	 * @param fields
	 */
	private static void createHeader(HSSFSheet sheet,Map<String,Integer> fields){
		HSSFRow header =sheet.createRow(0);
		HSSFCell cell=null;
		for(Map.Entry<String, Integer> entry:fields.entrySet()){
			cell=header.createCell(entry.getValue());
			cell.setCellValue(entry.getKey());
		}
	}
	/**
	 * 创建表头
	 * @param sheet
	 * @param fields
	 */
	private static void createHeader(HSSFSheet sheet,Set<String> fields){
		HSSFRow header =sheet.createRow(0);
		HSSFCell cell=null;
		int i = 0;
		for (String field : fields) {
			cell=header.createCell(i);
			cell.setCellValue(field);
			i++;
		}
	}
	
	//读execl代码-----------------------------------------------------------------
	/**
	 * 读2003execl
	 * @param in
	 * @param c
	 * @param list
	 * @throws Exception
	 */
	public static List<String> readExecl2003(InputStream in, Class<?> c, List<Object> list) throws Exception {
		POIFSFileSystem poiFileSystem = new POIFSFileSystem(in);
		HSSFWorkbook workbook = new HSSFWorkbook(poiFileSystem);
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		List<String> errorList = new ArrayList<>();

		Field[] fields=c.getDeclaredFields();
		int rowCount = 0;
		Iterator<Row> iterRow = sheet.rowIterator();
		while (iterRow.hasNext()) {
			HSSFRow row = (HSSFRow) iterRow.next();
			if (rowCount > 0) {
				Object object = c.newInstance();				
				for (int cellCount = 0; cellCount < fields.length; cellCount++) {
					HSSFCell cell = row.getCell(cellCount);
					String cellValue = getCell(cell).trim();
					//如果第一列数据为空，则认为此列为无效数据
					if(cellCount == 0 && "".equals(cellValue)){
						break;
					}
					String fieldName = fields[cellCount].getName();
					Class<?> fieldType = fields[cellCount].getType();
					String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
					Object thisValue = null;
					try {
						thisValue = castType(fieldType,cellValue);
					} catch (Exception e) {
						errorList.add("第"+(rowCount+1)+"行第"+(cellCount+1)+"列数据类型错误");
					}
					if(thisValue != null){
						c.getMethod(methodName,fieldType).invoke(object,thisValue);
					}
				}
				list.add(object);
			}
			rowCount++;
		}
		
		return errorList;
	}
	
	private static String getCell(Cell cell) {
	    if (cell == null)
	      return "";
	    switch (cell.getCellType()) {
	      case HSSFCell.CELL_TYPE_NUMERIC:
	        return cell.getNumericCellValue() + "";
	      case HSSFCell.CELL_TYPE_STRING:
	        return cell.getStringCellValue();
	      case HSSFCell.CELL_TYPE_FORMULA:
	        return cell.getCellFormula();
	      case HSSFCell.CELL_TYPE_BLANK:
	        return "";
	      case HSSFCell.CELL_TYPE_BOOLEAN:
	        return cell.getBooleanCellValue() + "";
	      case HSSFCell.CELL_TYPE_ERROR:
	        return cell.getErrorCellValue() + "";
	    }
	    return "";
	  }
	
	private static Object castType(Class<?> typeClass,String value) throws Exception{
		value = value.trim();
		Object returnValue = value;
		String typeName = typeClass.getSimpleName();
		switch (typeName) {
		case "int":
			if(value == null || value.equals("")){
				value = "0";
			}
			returnValue = Double.valueOf(value).intValue();
			break;
		case "Integer":
			if(value == null || value.equals("")){
				value = "0";
			}
			returnValue = Double.valueOf(value).intValue();
			break;
		case "Double":
			if(value == null || value.equals("")){
				value = "0";
			}
			returnValue = Double.valueOf(value);
			break;
		case "BigDecimal":
			if(value == null || value.equals("")){
				value = "0";
			}
			returnValue = new BigDecimal(value);
			break;
		case "Date":
			Date date = null;
			if(value.length() == 10){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
				date = dateFormat.parse(value);
			}else if(value.length() == 19){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = dateFormat.parse(value);
			}
			returnValue = date;
			break;
		default:
			break;
		}
		return returnValue;
	}
}
