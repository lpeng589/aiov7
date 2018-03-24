package com.koron.oa.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
public class ImgManagerUtil {
	
	
	public static void main(String args[]) {
		ImgManagerUtil.reduceImgGif("c:/testgif.gif", "c:/xufnew/test02.gif",150, 200);
	}

	/**
	 * 图像缩放 jpg格式
	 * 
	 * @param imgsrc :原图片文件路径
	 * @param imgdist :生成的缩略图片文件路径
	 * @param widthdist :生成图片的宽度
	 * @param heightdist :生成图片的高度
	 */
	public static void reduceImg(String imgsrc, String imgdist, int widthdist, int heightdist) {
		try {
			File srcfile = new File(imgsrc);
			if (!srcfile.exists()) {
				return;
			}
			Image src = ImageIO.read(srcfile);
			BufferedImage tag = new BufferedImage((int) widthdist,(int) heightdist, BufferedImage.TYPE_INT_RGB);
			/*
			 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
			 */
			tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist,Image.SCALE_SMOOTH), 0, 0, null);
			FileOutputStream out = new FileOutputStream(imgdist);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void reduceImgGif(String imgsrc, String imgdist, int widthdist, int heightdist) {
		try {
			File srcfile = new File(imgsrc);
			if (!srcfile.exists()) {
				return;
			}
			Image src = ImageIO.read(srcfile);
			BufferedImage tag = new BufferedImage((int) widthdist,(int) heightdist, BufferedImage.TYPE_INT_RGB);
			/*
			 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
			 */
			tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist,Image.SCALE_SMOOTH), 0, 0, null);
			// FileOutputStream out = new FileOutputStream(imgdist);
			// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			// encoder.encode(tag);
			// out.close();
			ImageIO.write(tag, "gif", new File(imgdist));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据图片路径 读取图片文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static BufferedImage readImage(String fileName) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(fileName));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return bi;
	}

	/**
	 * 生成新的图片文件
	 * 
	 * @param im
	 * @param formatName
	 * @param fileName
	 * @return
	 */
	public static boolean writeImage(RenderedImage im, String formatName,String fileName) {
		boolean result = false;
		try {
			result = ImageIO.write(im, formatName, new File(fileName));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return result;
	}

	/**
	 * 转换图片格式 到 jpg
	 * 
	 * @param im
	 * @param fileName
	 * @return
	 */
	public static boolean writeJPEGImage(RenderedImage im, String fileName) {
		return writeImage(im, "JPEG", fileName);
	}

	/**
	 * 转换图片格式 到 gif 不知到好用不
	 * 
	 * @param im
	 * @param fileName
	 * @return
	 */
	public static boolean writeGIFImage(RenderedImage im, String fileName) {
		return writeImage(im, "GIF", fileName);
	}

	public static boolean writePNGImage(RenderedImage im, String fileName) {
		return writeImage(im, "PNG", fileName);
	}

	public static boolean writeBMPImage(RenderedImage im, String fileName) {
		return writeImage(im, "BMP", fileName);
	}

	/**
	 * 以下是 缩小图片
	 * 
	 * @param url
	 * @param name
	 */

	public void img_change(String url, String name) {
		// 三种不同的压缩图片
		toSmallerpic(url, new File(url + name), "_middle", name, 188, 165,
				(float) 0.7);
		toSmallerpic(url, new File(url + name), "_small", name, 45, 45,
				(float) 0.7);
		toSmallerpic(url, new File(url + name), "_smaller", name, 116, 100,
				(float) 0.7);
	}

	public static void toSmallerpic(String f, File filelist, String ext,
			String n, int w, int h, float per) {
		Image src;
		try {
			String smallPicPath = f + "/small";
			File fSmall = new File(smallPicPath, n);
			if (!fSmall.exists()) {
				fSmall.getParentFile().mkdirs();// 返回此抽象路径名父目录的抽象路径名；如果此路径名没有指定父目录，则返回null
				// 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
				fSmall.createNewFile();// 当且仅当不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件。
				long begin = System.currentTimeMillis();
				src = javax.imageio.ImageIO.read(filelist); // 构造Image对象
				long end = System.currentTimeMillis();
				int old_w = src.getWidth(null); // 得到源图宽
				int old_h = src.getHeight(null);
				int new_w = 0;
				int new_h = 0; // 得到源图长

				double w2 = (old_w * 1.00) / (w * 1.00);
				double h2 = (old_h * 1.00) / (h * 1.00);

				// 图片跟据长宽留白，成一个正方形图。
				BufferedImage oldpic;
				if (old_w > old_h) {
					oldpic = new BufferedImage(old_w, old_w,BufferedImage.TYPE_INT_RGB);
				} else {
					if (old_w < old_h) {
						oldpic = new BufferedImage(old_h, old_h,BufferedImage.TYPE_INT_RGB);
					} else {
						oldpic = new BufferedImage(old_w, old_h,BufferedImage.TYPE_INT_RGB);
					}
				}
				Graphics2D g = oldpic.createGraphics();
				g.setColor(Color.white);
				if (old_w > old_h) {
					g.fillRect(0, 0, old_w, old_w);
					g.drawImage(src, 0, (old_w - old_h) / 2, old_w, old_h,Color.white, null);
				} else {
					if (old_w < old_h) {
						g.fillRect(0, 0, old_h, old_h);
						g.drawImage(src, (old_h - old_w) / 2, 0, old_w, old_h,Color.white, null);
					} else {
						// g.fillRect(0,0,old_h,old_h);
						g.drawImage(src.getScaledInstance(old_w, old_h,Image.SCALE_SMOOTH), 0, 0, null);
					}
				}
				g.dispose();
				src = oldpic;
				// 图片调整为方形结束
				if (old_w > w)
					new_w = (int) Math.round(old_w / w2);
				else
					new_w = old_w;
				if (old_h > h)
					new_h = (int) Math.round(old_h / h2);// 计算新图长宽
				else
					new_h = old_h;
				BufferedImage tag = new BufferedImage(new_w, new_h,BufferedImage.TYPE_INT_RGB);
				// tag.getGraphics().drawImage(src,0,0,new_w,new_h,null);
				// //绘制缩小后的图
				tag.getGraphics().drawImage(src.getScaledInstance(new_w, new_h,Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream newimage = new FileOutputStream(fSmall); // 输出到文件流
				// 可放字符串
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
				jep.setQuality(per, true);
				encoder.encode(tag, jep);
				// encoder.encode(tag); //近JPEG编码
				newimage.close();
			}

		} catch (IOException ex) {
			org.apache.log4j.Logger.getLogger(ImgManagerUtil.class.getName()).log(Level.DEBUG, null, ex);
		}
	}

	/**
	 * 压缩 效率稍微比上面高些 但是非高清等 压缩效果不是很好
	 * 
	 * @param f
	 * @param filelist
	 * @param ext
	 * @param n
	 * @param w
	 * @param h
	 * @param per
	 */
	public static void compressPicSim(HttpServletRequest req,String f, File filelist, String ext,
			String n, int w, int h, float per) {
		Image src = null;
		try {
			StringBuilder smallPicPath = new StringBuilder(f);
			smallPicPath.append("/small");
			File fSmall = new File(smallPicPath.toString(), n);
			if (!fSmall.exists()) {
				fSmall.getParentFile().mkdirs();// 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录
				fSmall.createNewFile();// 当且仅当不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件。
				src = javax.imageio.ImageIO.read(filelist); // 构造Image对象
				if (src == null) {
					String path = req.getSession().getServletContext().getRealPath("/style/images/unknown.gif");
					File fil = new File(path);
					if (fil.exists()) {
						src = javax.imageio.ImageIO.read(fil); // 构造Image对象
					}
				}

				int old_w = src.getWidth(null); // 得到源图宽
				int old_h = src.getHeight(null);
				int new_w = 0;
				int new_h = 0; // 得到源图长

				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) old_w) / (double) w + 0.1;
				double rate2 = ((double) old_h) / (double) h + 0.1;
				double rate = rate1 > rate2 ? rate1 : rate2;
				new_w = (int) (((double) old_w) / rate);
				new_h = (int) (((double) old_h) / rate);

				long begCreate = System.currentTimeMillis();
				BufferedImage tag = new BufferedImage(new_w, new_h,
						BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); // 绘制缩小后的图
				// tag.getGraphics().drawImage(
				// src.getScaledInstance(new_w, new_h, Image.SCALE_FAST), 0,
				// 0, null);
				FileOutputStream newimage = new FileOutputStream(fSmall); // 输出到文件流
				// 可放字符串
				JPEGImageEncoder encoder = JPEGCodec
						.createJPEGEncoder(newimage);
				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
				jep.setQuality(per, true);
				encoder.encode(tag, jep); // 近JPEG编码
				long endCreate = System.currentTimeMillis();
				System.out.println("createNewImg consute time :"
						+ (endCreate - begCreate));
				newimage.close();
			}

		} catch (IOException ex) {
			org.apache.log4j.Logger.getLogger(ImgManagerUtil.class.getName())
					.log(Level.DEBUG, null, ex);
		} finally {
			if (src != null)
				src.flush();
		}
	}

	/**
	 * 压缩图片方法3
	 * 
	 * @param oldFile
	 *            要压缩的图片路径
	 * @param newFile
	 *            压缩后添加的后缀名(在扩展名前添加,不会改变格式)
	 * @param width
	 *            压缩宽
	 * @param height
	 *            压缩高
	 * @param percentage
	 *            是否等比例压缩,true则宽高比自动调整
	 * @return
	 * @throws Exception
	 */
	public static void reduceImg(String oldFile, String newFile, int widthdist,
			int heightdist, boolean percentage) {
		try {
			File srcfile = new File(oldFile);
			if (!srcfile.exists()) {
				return;
			}
			Image src = javax.imageio.ImageIO.read(srcfile);

			if (percentage) {
				// 为等比压缩计算输出的宽高
				double rate1 = ((double) src.getWidth(null))
						/ (double) widthdist + 0.1;
				double rate2 = ((double) src.getHeight(null))
						/ (double) heightdist + 0.1;
				double rate = rate1 > rate2 ? rate1 : rate2;

				int new_w = (int) (((double) src.getWidth(null)) / rate);
				int new_h = (int) (((double) src.getHeight(null)) / rate);
				// 设定宽高
				BufferedImage tag = new BufferedImage(new_w, new_h,
						BufferedImage.TYPE_INT_RGB);

				// 设定文件扩展名
				String filePrex = oldFile
						.substring(0, oldFile.lastIndexOf('.'));
				newFile = filePrex + "SCALE_AREA_AVERAGING"
						+ oldFile.substring(filePrex.length());
				// 生成图片
				// 两种方法,效果与质量都相同,效率差不多
				// tag.getGraphics().drawImage(src.getScaledInstance(widthdist,heightdist,
				// Image.SCALE_SMOOTH), 0, 0, null);
				tag.getGraphics().drawImage(
						src.getScaledInstance(new_w, new_h,
								Image.SCALE_AREA_AVERAGING), 0, 0, null);
				FileOutputStream out = new FileOutputStream(newFile);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(tag);
				out.close();
			} else {
				// 设定宽高
				BufferedImage tag = new BufferedImage(widthdist, heightdist,
						BufferedImage.TYPE_INT_RGB);

				// 设定文件扩展名
				String filePrex = oldFile
						.substring(0, oldFile.lastIndexOf('.'));
				newFile = filePrex + "SCALE_AREA_AVERAGING" + newFile
						+ oldFile.substring(filePrex.length());
				// 生成图片
				// 两种方法,效果与质量都相同,第二种效率比第一种高,约一倍
				// tag.getGraphics().drawImage(src.getScaledInstance(widthdist,
				// heightdist, Image.SCALE_SMOOTH), 0, 0, null);
				tag.getGraphics().drawImage(
						src.getScaledInstance(widthdist, heightdist,
								Image.SCALE_AREA_AVERAGING), 0, 0, null);
				FileOutputStream out = new FileOutputStream(newFile);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(tag);
				out.close();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * 备注：ImageIO从JDK1.4 创建图片的方法
	 * 
	 * @param file
	 * @param outfile
	 */
	public static void createImageBySun(File file, File outfile) {
		try {

			// 源图片
			Image src = ImageIO.read(file);
			int w = src.getWidth(null);
			int h = src.getHeight(null);
			int new_w = (int) Math.round(w * 0.2);
			int new_h = (int) Math.round(h * 0.2);

			BufferedImage tag = new BufferedImage(new_w, new_h,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); // 绘制缩小后的图
			FileOutputStream newimage = new FileOutputStream(outfile);
			// 采用SUN公司的图片库读创建图片文件
			// 在maven中可能编译不通过，可以使用ImageIO写入文件
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			encoder.encode(tag); // 近JPEG编码
			newimage.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 备注：ImageIO从JDK1.4，JDK包含GIF，JPEG，PNG但是不支持GIF 创建图片的方法
	 * 
	 * @param file
	 * @param outfile
	 */
	public static void createImageByImageIo(File file, File outfile,
			String imageFormat) {
		try {
			// 源图片
			Image src = ImageIO.read(file);
			int w = src.getWidth(null);
			int h = src.getHeight(null);
			int new_w = (int) Math.round(w * 0.2);
			int new_h = (int) Math.round(h * 0.2);
			BufferedImage tag = new BufferedImage(new_w, new_h,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); // 绘制缩小后的图
			if (!outfile.exists()) {
				outfile.getParentFile().mkdirs();// 返回此抽象路径名父目录的抽象路径名；如果此路径名没有指定父目录，则返回
				// null。
				// 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
				outfile.createNewFile();// 当且仅当不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件。
			}
			FileOutputStream newimage = new FileOutputStream(outfile);
			// 在maven中可能编译不通过，可以使用ImageIO写入文件
			ImageIO.write(tag, imageFormat, newimage);
			newimage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据类型 返回该类型可以支持的格式（扩展名）
	 */
	public static String getAllowFormatByType(String type) {
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		return extMap.get(type);

	}
}
