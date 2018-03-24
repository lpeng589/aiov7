package com.koron.oa.controlPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SaveImage {
	/**
	 * 保存图片
	 * 
	 * @param img
	 *            原图路径
	 * @param dest
	 *            目标图路径
	 * @param top
	 *            选择框的左边y坐标
	 * @param left
	 *            选择框的左边x坐标
	 * @param width
	 *            选择框宽度
	 * @param height
	 *            选择框高度
	 * @return
	 * @throws IOException
	 */
	public static boolean saveImage(File img, String dest, int top, int left,
			int width, int height) throws IOException {
		File fileDest = new File(dest);
		if (!fileDest.getParentFile().exists())
			fileDest.getParentFile().mkdirs();
		String ext = Utils.getExtension(dest).toLowerCase();
		BufferedImage bi = (BufferedImage) ImageIO.read(img);
		height = Math.min(height, bi.getHeight());
		width = Math.min(width, bi.getWidth());
		if (height <= 0)
			height = bi.getHeight();
		if (width <= 0)
			width = bi.getWidth();
		top = Math.min(Math.max(0, top), bi.getHeight() - height);
		left = Math.min(Math.max(0, left), bi.getWidth() - width);
		System.out.println("top:" + top);
		System.out.println("left:" + left);
		BufferedImage bi_cropper = bi.getSubimage(left, top, width, height);
		return ImageIO.write(bi_cropper, ext.equals("png") ? "png" : "jpeg",
				fileDest);
	}

	public static void main(String[] args) {
		try {
			System.out.println(saveImage(new File(
					"E:\\tt\\Tulips.jpg"),
					"E:\\tt\\asdas.jpg", 106, 87,
					289, 217));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
