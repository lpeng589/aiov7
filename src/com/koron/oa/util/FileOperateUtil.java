package com.koron.oa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * @Date:
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
public class FileOperateUtil {
	private static String message;

	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public String readTxt(String filePathAndName, String encoding)
			throws IOException {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data + " ");
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
		} catch (IOException es) {
			st = "";
		}
		return st;
	}

	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            目录
	 * @return 返回目录创建后的路径
	 */
	public String createFolder(String folderPath) {
		String txt = folderPath;
		try {
			java.io.File myFilePath = new java.io.File(txt);
			txt = folderPath;
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			message = "创建目录操作出错";
		}
		return txt;
	}

	/**
	 * 多级目录创建
	 * 
	 * @param folderPath
	 *            准备要在本级目录下创建新目录的目录路径 例如 c:myf
	 * @param paths
	 *            无限级目录参数，各级目录以单数线区分 例如 a|b|c
	 * @return 返回创建文件后的路径 例如 c:myfac
	 */
	public String createFolders(String folderPath, String paths) {
		String txts = folderPath;
		try {
			String txt;
			txts = folderPath;
			StringTokenizer st = new StringTokenizer(paths, "|");
			for (int i = 0; st.hasMoreTokens(); i++) {
				txt = st.nextToken().trim();
				if (txts.lastIndexOf("/") != -1) {
					txts = createFolder(txts + txt);
				} else {
					txts = createFolder(txts + txt + "/");
				}
			}
		} catch (Exception e) {
			message = "创建目录操作出错！";
		}
		return txts;
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @return
	 */
	public void createFile(String filePathAndName, String fileContent) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
			resultFile.close();
		} catch (Exception e) {
			message = "创建文件操作出错";
		}
	}

	/**
	 * 有编码方式的文件创建
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @param encoding
	 *            编码方式 例如 GBK 或者 UTF-8
	 * @return
	 */
	public void createFile(String filePathAndName, String fileContent,
			String encoding) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			PrintWriter myFile = new PrintWriter(myFilePath, encoding);
			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
		} catch (Exception e) {
			message = "创建文件操作出错";
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @return Boolean 成功删除返回true遭遇异常返回false
	 */
	public boolean delFile(String filePathAndName) {
		boolean bea = false;
		try {
			String filePath = filePathAndName;
			File myDelFile = new File(filePath);
			if (myDelFile.exists()) {
				myDelFile.delete();
				bea = true;
			} else {
				bea = false;
				message = (filePathAndName + "删除文件操作出错");
			}
		} catch (Exception e) {
			message = e.toString();
		}
		return bea;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 * @return
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			message = ("删除文件夹操作出错");
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean bea = false;
		File file = new File(path);
		if (!file.exists()) {
			return bea;
		}
		if (!file.isDirectory()) {
			return bea;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				bea = true;
			}
		}

		return bea;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPathFile
	 *            准备复制的文件源
	 * @param newPathFile
	 *            拷贝到新绝对路径带文件名
	 * @return
	 */
	public void copyFile(String oldPathFile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			message = ("复制单个文件操作出错");
		}
	}

	/**
	 * 复制整个文件夹的内容
	 * 
	 * @param oldPath
	 *            准备拷贝的目录
	 * @param newPath
	 *            指定绝对路径的新目录
	 * @return
	 */
	public void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			message = "复制整个文件夹内容操作出错";
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动目录
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	public String getMessage() {
		return this.message;
	}

	/**
	 * 根据目录路径和过滤规则（image,doc等）获得该目录下对应的file[]
	 * 
	 * @param path
	 * @param fmtType
	 * @param isGetAllFiles
	 *            true查询所有
	 * @return
	 */
	public static File[] getFilesByPath(String path, String fmtType,
			boolean isGetAllFiles) {
		long begTime = System.currentTimeMillis();
		File file = new File(path);
		String fmt = null;
		if (!isGetAllFiles) {
			fmt = ImgManagerUtil.getAllowFormatByType(fmtType == ""
					|| fmtType == null ? "image" : fmtType);

		}

		File[] files = file.listFiles();
		if (files != null) {
			File[] newFiles = new File[files.length];
			int j = 0;
			for (int i = 0, length = files.length; i < length; i++) {
				File f = files[i];
				if (f.isFile()) {
					if (!isGetAllFiles) {
						String fileName = f.getName();
						String fileExtend = fileName.substring(
								fileName.lastIndexOf(".") + 1).toLowerCase();
						if (Arrays.<String> asList(fmt.split(",")).contains(
								fileExtend)) {
							newFiles[j] = f;
							j++;
						}
					} else {
						newFiles[j] = f;
						j++;
					}
				}
			}
			long endTime = System.currentTimeMillis();
			System.out.println("过滤文件封装file[]耗时：" + (endTime - begTime));
			return newFiles;
		}

		return new File[0];

	}

	/**
	 * 根据文件名称获得该文件对应的类型描述
	 * 
	 * @param fileName
	 */
	public static String getFileDesc(String fileName) {
		HashMap<String, String> extMap = new HashMap<String, String>();

		// 文件扩展名
		String fileExtend = fileName.substring(fileName.lastIndexOf(".") + 1)
				.toLowerCase();
		// image
		extMap.put("gif", "GIF 图像");
		extMap.put("jpg", "JPEG 图象");
		extMap.put("jpeg", "JPEG 图象");
		extMap.put("png", "PNG 图象");
		extMap.put("bmp", "BMP 图象");

		// flash media
		extMap.put("swf", "Flash 动画文件");
		extMap.put("flv", "Flash 视频文件");
		extMap.put("mp3", "MPEG 音频文件");
		extMap.put("wav", "Windows波形声形");
		extMap.put("wma", "Windows Media 音频/视频文件");
		extMap.put("wmv", "Windows Media 音频/视频文件");
		extMap.put("avi", "Windows Media 音频/视频文件");
		extMap.put("mpg", "MPEG 音频/视频文件");
		extMap.put("asf", "Windows Media 音频/视频文件");
		extMap.put("rm", "Real 音频/视频文件");
		extMap.put("rmvb", "Real 音频/视频文件");
		extMap.put("mid", "MIDI 音乐");
		
		// file
		extMap.put("doc", "Word 文档");
		extMap.put("docx", "Word 文档");
		extMap.put("xls", "Excel 工作表");
		extMap.put("xlsx", "Excel 工作表");
		extMap.put("ppt", "Powerpoint 演示文稿");
		extMap.put("htm", "HTML 网页文件");
		extMap.put("html", "HTML 网页文件");
		extMap.put("mht", "单一格式网页文件");
		extMap.put("zip", "Zip 压缩文件");
		extMap.put("rar", "RAR 压缩文件");
		extMap.put("gz", "GZIP 压缩文件");
		extMap.put("bz2", "bz2 压缩文件");
		extMap.put("xml", "XML 文档");
		extMap.put("exe", "应用程序");
		extMap.put("dll", "动态链接库");
		extMap.put("txt", "文本文件");
		extMap.put("chm", "编译的 HTML 帮助文件");
		extMap.put("pdf", "PDF 文档");
		
		
		
		

		String desc = extMap.get(fileExtend);
		if (StringUtils.isBlank(desc)) {
			desc = fileExtend.toUpperCase()+" 文件";
		}
		
		
		return desc;

	}

	/**
	 * 根据文件名称获得该文件对应的类型图标
	 * 
	 * @param fileName
	 */
	public static String getFileIcon(String fileName) {
		HashMap<String, String> extMap = new HashMap<String, String>();

		// 文件扩展名
		String fileExtend = fileName.substring(fileName.lastIndexOf(".") + 1)
				.toLowerCase();
		// image
		extMap.put("gif", "/style/images/fileIcon/gif.gif");
		extMap.put("jpg", "/style/images/fileIcon/jpg.gif");
		extMap.put("jpeg", "/style/images/fileIcon/jpg.gif");
		extMap.put("png", "/style/images/fileIcon/png.gif");
		extMap.put("bmp", "/style/images/fileIcon/bmp.gif");
	
		// flash media
		extMap.put("swf", "/style/images/fileIcon/flash.gif");
		extMap.put("flv", "/style/images/fileIcon/flash.gif");
		extMap.put("mp3", "/style/images/fileIcon/mp3.gif");
		extMap.put("wav", "/style/images/fileIcon/wav.gif");
		extMap.put("wma", "/style/images/fileIcon/avi.gif");
		extMap.put("wmv", "/style/images/fileIcon/avi.gif");
		extMap.put("avi", "/style/images/fileIcon/avi.gif");
		extMap.put("mpg", "/style/images/fileIcon/avi.gif");
		extMap.put("mht", "/style/images/fileIcon/mht.gif");
		extMap.put("asf", "/style/images/fileIcon/avi.gif");
		extMap.put("rm", "/style/images/fileIcon/ram.gif");
		extMap.put("rmvb", "/style/images/fileIcon/ram.gif");
		extMap.put("mid", "/style/images/fileIcon/mid.gif");

		// file
		extMap.put("doc", "/style/images/fileIcon/doc.gif");
		extMap.put("docx", "/style/images/fileIcon/doc.gif");
		extMap.put("xls", "/style/images/fileIcon/xls.gif");
		extMap.put("xlsx", "/style/images/fileIcon/xlsx.gif");
		extMap.put("ppt", "/style/images/fileIcon/ppt.gif");
		extMap.put("htm", "/style/images/fileIcon/mht.gif");
		extMap.put("html", "/style/images/fileIcon/html.gif");
		extMap.put("zip", "/style/images/fileIcon/zip.gif");
		extMap.put("rar", "/style/images/fileIcon/rar.gif");
		extMap.put("gz", "/style/images/fileIcon/zip.gif");
		extMap.put("bz2", "/style/images/fileIcon/zip.gif");
		extMap.put("xml", "/style/images/fileIcon/xml.gif");
		extMap.put("exe", "/style/images/fileIcon/exe.gif");
		extMap.put("dll", "/style/images/fileIcon/dll.gif");
		extMap.put("txt", "/style/images/fileIcon/txt.gif");
		extMap.put("chm", "/style/images/fileIcon/chm.gif");
		extMap.put("pdf","/style/images/fileIcon/pdf.gif");
		
		String iconPath = extMap.get(fileExtend);
		if (StringUtils.isBlank(iconPath)) {
			iconPath = "/style/images/fileIcon/defaut.gif";
		}
	
		return iconPath;


	}

}
