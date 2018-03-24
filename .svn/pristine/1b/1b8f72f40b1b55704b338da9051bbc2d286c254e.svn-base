package com.menyi.web.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.swetake.util.Qrcode;

/**
 * 
 * <p>Title:生成二维码</p> 
 * <p>Description: </p>
 *
 * @Date:Oct 23, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class QRCode {

	/**
	 * 生成二维码(QRCode)图片
	 * @param content 存储内容
	 * @param imgPath 图片路径
	 */
	public static void encoderQRCode(String content, String imgPath) {
		encoderQRCode(content, imgPath, "png", 7);
	}
	
	/**
	 * 生成二维码(QRCode)图片
	 * @param content 存储内容
	 * @param output 输出流
	 */
	public static void encoderQRCode(String content, OutputStream output) {
		encoderQRCode(content, output, "png", 5);
	}
	
	/**
	 * 生成二维码(QRCode)图片
	 * @param content 存储内容
	 * @param imgPath 图片路径
	 * @param imgType 图片类型
	 */
	public static void encoderQRCode(String content, String imgPath, String imgType) {
		encoderQRCode(content, imgPath, imgType, 7);
	}
	
	/**
	 * 生成二维码(QRCode)图片
	 * @param content 存储内容
	 * @param output 输出流
	 * @param imgType 图片类型
	 */
	public static void encoderQRCode(String content, OutputStream output, String imgType) {
		encoderQRCode(content, output, imgType, 7);
	}

	/**
	 * 生成二维码(QRCode)图片
	 * @param content 存储内容
	 * @param imgPath 图片路径
	 * @param imgType 图片类型
	 * @param size 二维码尺寸
	 */
	public static void encoderQRCode(String content, String imgPath, String imgType, int size) {
		try {
			BufferedImage bufImg = qRCodeCommon(content, imgType, size);
			
			File imgFile = new File(imgPath);
			// 生成二维码QRCode图片
			ImageIO.write(bufImg, imgType, imgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成二维码(QRCode)图片
	 * @param content 存储内容
	 * @param output 输出流
	 * @param imgType 图片类型
	 * @param size 二维码尺寸
	 */
	public static void encoderQRCode(String content, OutputStream output, String imgType, int size) {
		try {
			BufferedImage bufImg = qRCodeCommon(content, imgType, size);
			// 生成二维码QRCode图片
			ImageIO.write(bufImg, imgType, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成二维码(QRCode)图片的公共方法
	 * @param content 存储内容
	 * @param imgType 图片类型
	 * @param size 二维码尺寸
	 * @return
	 */
	private static BufferedImage qRCodeCommon(String content, String imgType, int size) {
		BufferedImage bufImg = null;
		try {
			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcodeHandler.setQrcodeVersion(size);
			// 获得内容的字节数组，设置编码格式
			byte[] contentBytes = content.getBytes("utf-8");
			// 图片尺寸
			int imgSize = 67 + 12 * (size - 1);
			bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			// 设置背景颜色
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, imgSize, imgSize);

			// 设定图像颜色> BLACK
			gs.setColor(Color.BLACK);
			// 设置偏移量，不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容> 二维码
			if (contentBytes.length > 0 && contentBytes.length < 800) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
			}
			gs.dispose();
			bufImg.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImg;
	}
	
	/**
	 * 解析二维码（QRCode）
	 * @param imgPath 图片路径
	 * @return
	 */
	public static String decoderQRCode(String imgPath) {
		// QRCode 二维码图片的文件
		File imageFile = new File(imgPath);
		BufferedImage bufImg = null;
		String content = null;
		try {
			bufImg = ImageIO.read(imageFile);
			QRCodeDecoder decoder = new QRCodeDecoder();
			content = new String(decoder.decode(new BarCodeImage(bufImg)), "utf-8"); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DecodingFailedException dfe) {
			dfe.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 解析二维码（QRCode）
	 * @param input 输入流
	 * @return
	 */
	public static String decoderQRCode(InputStream input) {
		BufferedImage bufImg = null;
		String content = null;
		try {
			bufImg = ImageIO.read(input);
			QRCodeDecoder decoder = new QRCodeDecoder();
			content = new String(decoder.decode(new BarCodeImage(bufImg)), "utf-8"); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DecodingFailedException dfe) {
			dfe.printStackTrace();
		}
		return content;
	}
	
	public static String getValue(String xml, String name) {
        try {
            return xml.substring(xml.indexOf("<" + name + ">") +("<" + name + ">").length(), xml.indexOf("</" + name + ">"));
        } catch (Exception ex) {
            return null;
        }
    }
	
	
	//添加新安全算法,如果用JCE就要把它添加进去
    private static byte[] keyBytes = {0x21, 0x12, 0xF, 0x58, (byte) 0x88, 0x10, 0x40, 0x38
                              , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                              , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2}; //24字节的密钥
	private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] src) {
        try { //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
}

class BarCodeImage implements QRCodeImage {

	BufferedImage bufImg;
	
	public BarCodeImage(BufferedImage bufImg) {
		this.bufImg = bufImg;
	}
	
	public int getHeight() {
		return bufImg.getHeight();
	}

	public int getPixel(int x, int y) {
		return bufImg.getRGB(x, y);
	}

	public int getWidth() {
		return bufImg.getWidth();
	}
}
