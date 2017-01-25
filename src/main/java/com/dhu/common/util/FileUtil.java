package com.dhu.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.util.ResourceUtils;

public class FileUtil {
	
	/**
	 * 在指定的目录下保存文件，关闭InputStream
	 * @param dir
	 * @param fileName
	 * @param in
	 */
	public static void writeFile(String dir, String fileName, InputStream in) throws IOException {
		createDirs(dir,true);
		File file = createFile(dir,fileName,false);
		FileOutputStream outputFile = new FileOutputStream(file);
	    byte b[] = new byte[1024];
	    int n;
	    while ((n = in.read(b)) != -1)
	    {
	        outputFile.write(b, 0, n);
	    }
	    outputFile.close();
	    in.close();
	}
	
	/**
	 * 删除一个文件
	 * @param filename
	 */
	public static Boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return file.delete();
	}
	
	/**
	 * 获取文件类型
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileType(String filePath) {
		String suffixes = getFileSuffixes(filePath).toLowerCase();
		if (".bmp".equalsIgnoreCase(suffixes)
				|| ".jpg".equalsIgnoreCase(suffixes)
				|| ".gif".equalsIgnoreCase(suffixes)
				|| ".bmp".equalsIgnoreCase(suffixes)
				|| ".jfif".equalsIgnoreCase(suffixes)
				|| ".tiff".equalsIgnoreCase(suffixes)
				|| ".jpe".equalsIgnoreCase(suffixes)
				|| ".ico".equalsIgnoreCase(suffixes)
				|| ".jpeg".equalsIgnoreCase(suffixes)
				|| ".png".equalsIgnoreCase(suffixes)) {
			return "image";
		} else if (".pdf".equalsIgnoreCase(suffixes)) {
			return "pdf";
		} else if (".doc".equalsIgnoreCase(suffixes)
				|| ".docx".equalsIgnoreCase(suffixes)) {
			return "doc";
		} else if (".txt".equalsIgnoreCase(suffixes)) {
			return "txt";
		} else if (".xls".equalsIgnoreCase(suffixes)
				|| ".xlsx".equalsIgnoreCase(suffixes)) {
			return "xls";
		}
		return "";
	}
	
	/**
	 * 获取文件后缀
	 *
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffixes(String fileName) {
		int index = fileName.lastIndexOf(".");
        return fileName.substring(index, fileName.length());
	}
	
	/**
	 * 创建多个文件夹
	 *
	 * @param dir
	 * @param ignoreIfExitst
	 * @throws java.io.IOException
	 */
	private static void createDirs(String dir, boolean ignoreIfExitst)
			throws IOException {
		File file = new File(dir);
		file.setWritable(true, false);
		if (ignoreIfExitst && file.exists()) {
			return;
		}
		file.mkdirs();
	}
	
	/**
	 * 在已存在的目录下创建文件
	 * @param dir
	 * @param fileName
	 * @param ignoreIfExitst
	 * @throws IOException
	 */
	private static File createFile(String dir, String fileName,
			boolean ignoreIfExitst) throws IOException {
		File file = new File(dir + fileName);
		file.setWritable(true, false);
		if (!file.exists()) {
			file.createNewFile();
		}else if(!ignoreIfExitst){
			file.delete();
			file.createNewFile();
		}
		return file;
	}
	
	/**
	 * 获取xls导出模板对象
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileInputStream getFileStream(String fileName) throws FileNotFoundException{
		String filePath = "classpath:freemarker/" + fileName;
		URL url = ResourceUtils.getURL(filePath);
		return new FileInputStream(url.getFile());
	}
}
