package com.stardai.manage.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Component
@SuppressWarnings("all")
public class Base64Image {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(Base64Image.class);

	static {
		ImageIO.scanForPlugins();
	}

	// 阿里云API的内或外网域名
	private static String ENDPOINT;

	// 阿里云API的密钥Access Key ID
	private static String ACCESS_KEY_ID;

	// 阿里云API的密钥Access Key Secret
	private static String ACCESS_KEY_SECRET;

	// 阿里云API的bucket名称
	private static String BACKET_NAME;

	// 阿里云API的文件夹名称
	private static String FOLDER;

	private static String POSTBARURL;

	/*
	 * private static String imagePath; private static OSSClient ossClient;
	 * private static String bucketName;
	 */

	// 初始化属性
	static {
		ENDPOINT = OssClientConstants.ENDPOINT;
		ACCESS_KEY_ID = OssClientConstants.ACCESS_KEY_ID;
		ACCESS_KEY_SECRET = OssClientConstants.ACCESS_KEY_SECRET;
		BACKET_NAME = OssClientConstants.BACKET_NAME;
		// FOLDER = OSSClientConstants.FOLDER;
		POSTBARURL = OssClientConstants.POSTBARURL;
	}

	/*
	 * public String getImagePath() { return imagePath; }
	 * 
	 * public void setImagePath(String imagePath) { this.imagePath = imagePath;
	 * }
	 * 
	 * public String getBucketName() { return bucketName; }
	 * 
	 * public void setBucketName(String bucketName) { this.bucketName =
	 * bucketName; }
	 * 
	 * public OSSClient getOssClient() { return ossClient; }
	 * 
	 * public void setOssClient(OSSClient ossClient) { this.ossClient =
	 * ossClient; }
	 */

	/*
	 * protected OSSClient getRequiredOssClient() { OSSClient client =
	 * getOssClient(); return client; }
	 */
	/**
	 * 获取阿里云OSS客户端对象
	 * 
	 * @return ossClient
	 */
	/*
	 * public static OSSClient getRequiredOssClient() { return new
	 * OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET); }
	 */

	/*
	 * public static String GetImageStr(String filePath) { String imgFile =
	 * filePath; InputStream in = null; byte[] data = null;
	 * 
	 * try { in = new FileInputStream(imgFile); data = new byte[in.available()];
	 * in.read(data); in.close(); } catch (IOException e) { e.printStackTrace();
	 * }
	 * 
	 * BASE64Encoder encoder = new BASE64Encoder();
	 * 
	 * String base64Head = "data:" + GetMimeType(filePath) + ";base64,";
	 * 
	 * return base64Head + encoder.encode(data); }
	 */
	private static HashMap<String, String> mimeTypes;

	public static HashMap<String, String> getMimeTypes() {

		if (mimeTypes == null) {
			mimeTypes = new HashMap<>();
			mimeTypes.put("image/jpg", ".jpg");
			mimeTypes.put("image/jpeg", ".jpeg");
			mimeTypes.put("image/png", ".png");
			mimeTypes.put("image/gif", ".gif");
		}

		return mimeTypes;
	}

	private static String GetMimeType(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		HashMap<String, String> mimeTypes = getMimeTypes();
		Iterator iter = mimeTypes.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Object val = mimeTypes.get(key);
			if (val.equals(extension)) {
				return key.toString();
			}

		}

		return "";
	}

	private static String GetExtension(String base64) {
		String mimeType = base64.substring(base64.indexOf("data:"), base64.indexOf(";")).replace("data:", "")
				.toLowerCase();
		HashMap<String, String> mimeTypes = getMimeTypes();
		if (mimeTypes.containsKey(mimeType)) {
			return mimeTypes.get(mimeType);
		} else {
			return "";
		}
	}

	/*
	 * public Map<String, String> getBigPathAndSmall(String BigPath,
	 * HttpServletRequest request) { String savePath =
	 * request.getSession().getServletContext().getRealPath("/");//项目路径 String
	 * Path = null; String smallPath = null;
	 * 
	 * if (savePath.indexOf("usr") != -1) { Path = imagePath +
	 * BigPath.substring(BigPath.lastIndexOf("/")); String Suffix =
	 * BigPath.substring(BigPath.lastIndexOf(".") + 1, BigPath.length());
	 * smallPath = Path.substring(0, Path.lastIndexOf(".")) + "_s." + Suffix; }
	 * else { String Suffix = BigPath.substring(BigPath.lastIndexOf(".") + 1,
	 * BigPath.length()); smallPath = BigPath.substring(0,
	 * BigPath.lastIndexOf(".")) + "_s." + Suffix;
	 * 
	 * Path = request.getSession().getServletContext().getRealPath("WEB-INF/" +
	 * BigPath); smallPath =
	 * request.getSession().getServletContext().getRealPath("WEB-INF/" +
	 * smallPath); } Map<String, String> map = new HashMap<String, String>();
	 * map.put("bigPath", Path);//大图全路径 map.put("smallPath", smallPath);//小图全路径
	 * return map; }
	 */

	public String GenerateImage(String imgStr, HttpServletRequest request, String folder) {
		if (imgStr == null) {
			return null;
		}
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

		String base64Head = imgStr.substring(imgStr.indexOf("data:"), imgStr.indexOf(",") + 1);// 获取base64开头
		String extension = GetExtension(imgStr);// 获取图片后缀
		imgStr = imgStr.replace(base64Head, ""); // 获取base64 不包含开头
		UUID uuid = UUID.randomUUID();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = simpleDateFormat.format(new Date());
		String savePath = folder + "/" + uuid.toString() + dateString + extension;// 存入项目路径
		File uploadedFile = null;
		// BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
			ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);

			PutObjectResult result = ossClient.putObject(BACKET_NAME, savePath, inStream, new ObjectMetadata());

			/*
			 * PutObjectResult result =
			 * getRequiredOssClient().putObject(BACKET_NAME, savePath, inStream,
			 * new ObjectMetadata());
			 */
			return POSTBARURL + savePath;
			// return "storage/images/" + uuid.toString() +dateString+
			// extension;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ossClient.shutdown();
		}
	}

	public String GenerateImageForCompany(String imgStr, HttpServletRequest request, String folder) {
		if (imgStr == null) {
			return null;
		}
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

		String base64Head = imgStr.substring(imgStr.indexOf("data:"), imgStr.indexOf(",") + 1);// 获取base64开头
		String extension = GetExtension(imgStr);// 获取图片后缀
		imgStr = imgStr.replace(base64Head, ""); // 获取base64 不包含开头
		UUID uuid = UUID.randomUUID();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = simpleDateFormat.format(new Date());
		String savePath = folder + "/";
		String imageId = uuid.toString() + dateString + extension;// 存入项目路径
		savePath = savePath + imageId;
		File uploadedFile = null;
		// BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
			ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);

			PutObjectResult result = ossClient.putObject(BACKET_NAME, savePath, inStream, new ObjectMetadata());

			/*
			 * PutObjectResult result =
			 * getRequiredOssClient().putObject(BACKET_NAME, savePath, inStream,
			 * new ObjectMetadata());
			 */
			return imageId;
			// return "storage/images/" + uuid.toString() +dateString+
			// extension;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ossClient.shutdown();
		}
	}
	
	
	public String GenerateImageForTwoDimension(String imgStr, HttpServletRequest request, String folder,String userId) {
		if (imgStr == null) {
			return null;
		}
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

		String base64Head = imgStr.substring(imgStr.indexOf("data:"), imgStr.indexOf(",") + 1);// 获取base64开头
		String extension = GetExtension(imgStr);// 获取图片后缀
		imgStr = imgStr.replace(base64Head, ""); // 获取base64 不包含开头
		UUID uuid = UUID.randomUUID();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = simpleDateFormat.format(new Date());
		String savePath = folder + "/" + userId +"-"+ dateString+ extension ;// 存入项目路径
		File uploadedFile = null;
		// BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
			ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);

			PutObjectResult result = ossClient.putObject(BACKET_NAME, savePath, inStream, new ObjectMetadata());

			/*
			 * PutObjectResult result =
			 * getRequiredOssClient().putObject(BACKET_NAME, savePath, inStream,
			 * new ObjectMetadata());
			 */
			return POSTBARURL + savePath;
			// return "storage/images/" + uuid.toString() +dateString+
			// extension;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ossClient.shutdown();
		}
	}

}