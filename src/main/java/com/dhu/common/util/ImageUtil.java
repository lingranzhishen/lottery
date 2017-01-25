package com.dhu.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtil {
	private static ImageUtil imageHelper = new ImageUtil();

	private ImageUtil() {
	}

	/**
	 * @return 获取实例
	 */
	public static ImageUtil getInstance() {
		return imageHelper;
	}

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            需要缩小的倍数，缩小2倍为原来的1/2 ，这个数值越大，返回的图片越小
	 * @return 返回处理后的图像
	 */
	public BufferedImage resizeImage(BufferedImage im, float resizeTimes) {

		/* 原始图像的宽度和高度 */
		int width = im.getWidth();
		int height = im.getHeight();

		/* 调整后的图片的宽度和高度 */
		int toWidth = (int) (Float.parseFloat(String.valueOf(width)) / resizeTimes);
		int toHeight = (int) (Float.parseFloat(String.valueOf(height)) / resizeTimes);

		/* 新生成结果图片 */
		BufferedImage newImage = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);
		newImage.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return newImage;
	}

	/**
	 * @param in
	 * @param resizeTimes
	 * @return 传入图片流
	 */
	public BufferedImage resizeImage(InputStream in, float resizeTimes) {

		BufferedImage im;
		try {
			im = ImageIO.read(in);
			/* 原始图像的宽度和高度 */
			int width = im.getWidth();
			int height = im.getHeight();

			/* 调整后的图片的宽度和高度 */
			int toWidth = (int) (Float.parseFloat(String.valueOf(width)) / resizeTimes);
			int toHeight = (int) (Float.parseFloat(String.valueOf(height)) / resizeTimes);

			/* 新生成结果图片 */
			BufferedImage newImage = new BufferedImage(toWidth, toHeight,
					BufferedImage.TYPE_INT_RGB);
			newImage.getGraphics().drawImage(
					im.getScaledInstance(toWidth, toHeight,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);
			return newImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            倍数,比如0.5就是缩小一半,0.98等等double类型
	 * @return 返回处理后的图像
	 */
	public BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
		/* 原始图像的宽度和高度 */
		int width = im.getWidth();
		int height = im.getHeight();
		/* 调整后的图片的宽度和高度 */
		int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
		int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);

		/* 新生成结果图片 */
		BufferedImage newImage = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);
		newImage.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return newImage;
	}

	/**
	 * @param im
	 * @param toWidth
	 *            期望宽度
	 * @param toHeight
	 *            期望宽度
	 * @return 返回缩小到指定大小的图像
	 */
	public BufferedImage zoomImage(BufferedImage im, int toWidth, int toHeight) {
		/* 新生成结果图片 */
		BufferedImage newImage = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);
		newImage.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return newImage;
	}

	/**
	 * @param in
	 * @param toWidth
	 * @param toHeight
	 * @return 图片流返回新图片
	 */
	/**
	 * @param in
	 *            输入流
	 * @param toWidth
	 *            指定宽度
	 * @param toHeight
	 *            指定高度
	 * @param path
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @return 成功写入磁盘返回true
	 * @throws IOException 
	 */
	public boolean resizeImageThenWriteToDisk(InputStream in, int toWidth,
			int toHeight, String path, String fileName) throws IOException {
		BufferedImage im;

		im = ImageIO.read(in);
		/* 新生成结果图片 */
		BufferedImage newImage = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);
		newImage.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		File f = new File(path + "/" + fileName);
		String fileType = getExtension(fileName);
		if (fileType == null) {
			return false;
		}
		ImageIO.write(newImage, fileType, f);
		im.flush();
		return true;

	}

	/**
	 * 把图片写到本地磁盘上
	 * 
	 * @param im
	 * @param path
	 *            图片写入的文件夹地址
	 * @param fileName
	 *            写入图片的名字
	 * @return
	 */
	public boolean writeToDisk(BufferedImage im, String path, String fileName) {
		File f = new File(path + "/" + fileName);
		String fileType = getExtension(fileName);
		if (fileType == null)
			return false;
		try {
			ImageIO.write(im, fileType, f);
			im.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 返回文件的文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public String getExtension(String fileName) {
		try {
			return fileName.split("\\.")[fileName.split("\\.").length - 1];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws Exception {

		String path = "C:\\Documents and Settings\\Administrator\\桌面";
		String fileName = "0.jpg";
		ImageUtil helper = ImageUtil.getInstance();
		System.out.println(helper.resizeImageThenWriteToDisk(
				new FileInputStream(path + "/" + fileName), 100, 100, path
						+ "/", "test.jsp"));
	}
}
