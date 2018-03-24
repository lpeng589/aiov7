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
 * @Copyright: �������
 * @Author ë��
 * @preserve all
 */
public class FileOperateUtil {
	private static String message;

	/**
	 * ��ȡ�ı��ļ�����
	 * 
	 * @param filePathAndName
	 *            ������������·�����ļ���
	 * @param encoding
	 *            �ı��ļ��򿪵ı��뷽ʽ
	 * @return �����ı��ļ�������
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
	 * �½�Ŀ¼
	 * 
	 * @param folderPath
	 *            Ŀ¼
	 * @return ����Ŀ¼�������·��
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
			message = "����Ŀ¼��������";
		}
		return txt;
	}

	/**
	 * �༶Ŀ¼����
	 * 
	 * @param folderPath
	 *            ׼��Ҫ�ڱ���Ŀ¼�´�����Ŀ¼��Ŀ¼·�� ���� c:myf
	 * @param paths
	 *            ���޼�Ŀ¼����������Ŀ¼�Ե��������� ���� a|b|c
	 * @return ���ش����ļ����·�� ���� c:myfac
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
			message = "����Ŀ¼��������";
		}
		return txts;
	}

	/**
	 * �½��ļ�
	 * 
	 * @param filePathAndName
	 *            �ı��ļ���������·�����ļ���
	 * @param fileContent
	 *            �ı��ļ�����
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
			message = "�����ļ���������";
		}
	}

	/**
	 * �б��뷽ʽ���ļ�����
	 * 
	 * @param filePathAndName
	 *            �ı��ļ���������·�����ļ���
	 * @param fileContent
	 *            �ı��ļ�����
	 * @param encoding
	 *            ���뷽ʽ ���� GBK ���� UTF-8
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
			message = "�����ļ���������";
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param filePathAndName
	 *            �ı��ļ���������·�����ļ���
	 * @return Boolean �ɹ�ɾ������true�����쳣����false
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
				message = (filePathAndName + "ɾ���ļ���������");
			}
		} catch (Exception e) {
			message = e.toString();
		}
		return bea;
	}

	/**
	 * ɾ���ļ���
	 * 
	 * @param folderPath
	 *            �ļ�����������·��
	 * @return
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ɾ�����ļ���
		} catch (Exception e) {
			message = ("ɾ���ļ��в�������");
		}
	}

	/**
	 * ɾ��ָ���ļ����������ļ�
	 * 
	 * @param path
	 *            �ļ�����������·��
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
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
				bea = true;
			}
		}

		return bea;
	}

	/**
	 * ���Ƶ����ļ�
	 * 
	 * @param oldPathFile
	 *            ׼�����Ƶ��ļ�Դ
	 * @param newPathFile
	 *            �������¾���·�����ļ���
	 * @return
	 */
	public void copyFile(String oldPathFile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldPathFile); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			message = ("���Ƶ����ļ���������");
		}
	}

	/**
	 * ���������ļ��е�����
	 * 
	 * @param oldPath
	 *            ׼��������Ŀ¼
	 * @param newPath
	 *            ָ������·������Ŀ¼
	 * @return
	 */
	public void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs(); // ����ļ��в����� �������ļ���
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
				if (temp.isDirectory()) {// ��������ļ���
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			message = "���������ļ������ݲ�������";
		}
	}

	/**
	 * �ƶ��ļ�
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
	 * �ƶ�Ŀ¼
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
	 * ����Ŀ¼·���͹��˹���image,doc�ȣ���ø�Ŀ¼�¶�Ӧ��file[]
	 * 
	 * @param path
	 * @param fmtType
	 * @param isGetAllFiles
	 *            true��ѯ����
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
			System.out.println("�����ļ���װfile[]��ʱ��" + (endTime - begTime));
			return newFiles;
		}

		return new File[0];

	}

	/**
	 * �����ļ����ƻ�ø��ļ���Ӧ����������
	 * 
	 * @param fileName
	 */
	public static String getFileDesc(String fileName) {
		HashMap<String, String> extMap = new HashMap<String, String>();

		// �ļ���չ��
		String fileExtend = fileName.substring(fileName.lastIndexOf(".") + 1)
				.toLowerCase();
		// image
		extMap.put("gif", "GIF ͼ��");
		extMap.put("jpg", "JPEG ͼ��");
		extMap.put("jpeg", "JPEG ͼ��");
		extMap.put("png", "PNG ͼ��");
		extMap.put("bmp", "BMP ͼ��");

		// flash media
		extMap.put("swf", "Flash �����ļ�");
		extMap.put("flv", "Flash ��Ƶ�ļ�");
		extMap.put("mp3", "MPEG ��Ƶ�ļ�");
		extMap.put("wav", "Windows��������");
		extMap.put("wma", "Windows Media ��Ƶ/��Ƶ�ļ�");
		extMap.put("wmv", "Windows Media ��Ƶ/��Ƶ�ļ�");
		extMap.put("avi", "Windows Media ��Ƶ/��Ƶ�ļ�");
		extMap.put("mpg", "MPEG ��Ƶ/��Ƶ�ļ�");
		extMap.put("asf", "Windows Media ��Ƶ/��Ƶ�ļ�");
		extMap.put("rm", "Real ��Ƶ/��Ƶ�ļ�");
		extMap.put("rmvb", "Real ��Ƶ/��Ƶ�ļ�");
		extMap.put("mid", "MIDI ����");
		
		// file
		extMap.put("doc", "Word �ĵ�");
		extMap.put("docx", "Word �ĵ�");
		extMap.put("xls", "Excel ������");
		extMap.put("xlsx", "Excel ������");
		extMap.put("ppt", "Powerpoint ��ʾ�ĸ�");
		extMap.put("htm", "HTML ��ҳ�ļ�");
		extMap.put("html", "HTML ��ҳ�ļ�");
		extMap.put("mht", "��һ��ʽ��ҳ�ļ�");
		extMap.put("zip", "Zip ѹ���ļ�");
		extMap.put("rar", "RAR ѹ���ļ�");
		extMap.put("gz", "GZIP ѹ���ļ�");
		extMap.put("bz2", "bz2 ѹ���ļ�");
		extMap.put("xml", "XML �ĵ�");
		extMap.put("exe", "Ӧ�ó���");
		extMap.put("dll", "��̬���ӿ�");
		extMap.put("txt", "�ı��ļ�");
		extMap.put("chm", "����� HTML �����ļ�");
		extMap.put("pdf", "PDF �ĵ�");
		
		
		
		

		String desc = extMap.get(fileExtend);
		if (StringUtils.isBlank(desc)) {
			desc = fileExtend.toUpperCase()+" �ļ�";
		}
		
		
		return desc;

	}

	/**
	 * �����ļ����ƻ�ø��ļ���Ӧ������ͼ��
	 * 
	 * @param fileName
	 */
	public static String getFileIcon(String fileName) {
		HashMap<String, String> extMap = new HashMap<String, String>();

		// �ļ���չ��
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
