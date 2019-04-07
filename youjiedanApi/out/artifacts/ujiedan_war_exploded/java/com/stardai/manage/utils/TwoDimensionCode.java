/**
 * 
 */
package com.stardai.manage.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import sun.misc.BASE64Encoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

/**
 * @author Tina 2018年5月29日
 */
public class TwoDimensionCode {

	private static final int black = 0xFF000000;

	private static final int white = 0xFFFFFFFF;

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? black : white);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		ImageIO.write(image, format, file);
	}

	public static void createQRImage(String content, int width, int height, String path, String fileName)
			throws Exception {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		if (StringUtils.isNotBlank(path)) {
			if (!path.endsWith("/")) {
				path = path + "/";
			}
		} else {
			path = "";
		}
		String suffix = "jpg";
		if (fileName.indexOf(".") <= -1) {
			fileName = fileName + "." + suffix;
		} else {
			suffix = fileName.split("[.]")[1];
		}
		fileName = path + fileName;
		File file = new File(fileName);
		writeToFile(bitMatrix, suffix, file);
	}

	public static String createQRImageBuffer(String content, int width, int height) throws Exception {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Hashtable hints = new Hashtable();
		 hints.put(EncodeHintType.MARGIN, 1);   //设置白边 
		
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage image = toBufferedImage(bitMatrix);
		// 把生成BufferedImage数据流进行Base64编码
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "png", os);
		
		 BASE64Encoder encoder = new BASE64Encoder();  
		 String base64Img = encoder.encode(os.toByteArray()); 
		
		
		//String base64Img = new String(Base64.encode(os.toByteArray(),1));
		//Base64.getEncoder().encodeToString(os.toByteArray())
		// os.toString("UTF-8");
		return "data:image/png;base64,"+base64Img;
	}

}
