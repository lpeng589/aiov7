package com.koron.oa.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


/**
 * 
 * <p>
 * Title:对file进行排序
 * 
 * @Date:
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
public class FileUtils {
	/**
	 * 升序
	 */
	public static final int ASC = 1;

	/**
	 * 降序
	 */
	public static final int DESC = 2;

	/**
	 * 最后修改时间
	 */
	public static final int LASTMODIFIED = 1;

	/**
	 * 大小
	 */
	public static final int SIZE = 2;

	/**
	 * 名称
	 */
	public static final int NAME = 3;

	public static class CompratorBySortType implements Comparator {
		private int sort = ASC;

		private int sortType = LASTMODIFIED;// 默认修改时间

		public CompratorBySortType(int sort, int sortType) {
			this.sort = sort;
			this.sortType = sortType;
		}

		public int compare(Object o1, Object o2) {
			
			if (o1!=null && o2!=null) {
				File file1 = (File) o1;
				File file2 = (File) o2;
				if (sortType == LASTMODIFIED) {
					if (sort == ASC) {
						long diff = file1.lastModified() - file2.lastModified();
						return diff > 0 ? 1 : -1;
					} else {
						long diff = file2.lastModified() - file1.lastModified();
						return diff > 0 ? 1 : -1;
					}

				} else if (sortType == SIZE) {
					if (sort == ASC) {
						long diff = file1.length() - file2.length();
						return diff > 0 ? 1 : -1;
					} else {
						long diff = file2.length() - file1.length();
						return diff > 0 ? 1 : -1;
					}

				} else if (sortType ==  NAME) {
					if (sort == ASC) {
						//大小写有影响
						int i = file1.getName().toUpperCase().compareTo(file2.getName().toUpperCase());
						System.out.println("file1.getName()"+file1.getName()+"###file2.getName()"+file2.getName()+"result="+i);
						return i > 0 ?1:-1;

					} else {
						return file2.getName().compareTo(file1.getName());
					}

				} else {
					System.out.println("发生错误");
					return -1;
				}
			}else {
				System.out.println("排序的两个对象有为空对象！");
				return -1;
			}

		}

	}

	public static void main(String[] args) {
		File dir = new File("F:\\OA改造\\small");

		File[] files = dir.listFiles();
		
		
		System.out.print("before sort: ");
		
		for (int i = 0; i < files.length; i++)

			System.out.print(files[i] + " \n");
		/*
		Arrays.sort(files);

		System.out.print("sort by name: ");

		for (int i = 0; i < files.length; i++)

			System.out.print(files[i] + " \n");

		System.out.println();
		*/
		Long begTime = System.currentTimeMillis();
		Arrays.sort(files, new FileUtils.CompratorBySortType(FileUtils.ASC,
				FileUtils.NAME));
		System.out.print("sort by size: ");
		for (int i = 0; i < files.length; i++)
			System.out.print(files[i] + " \n");
		
		Long endTime = System.currentTimeMillis();
		//读取文件的时候 默认就是读取的按照名称分组的
		System.out.println(endTime-begTime);
		/*
		Long begTime2 = System.currentTimeMillis();
		List<TreeVo> list = new ArrayList<TreeVo>();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			TreeVo vo = new TreeVo();
			vo.setFileSize(f.length());
			vo.setLastUpdateTime(BaseDateFormat.format(new Date(f.lastModified()), BaseDateFormat.yyyyMMddHHmmss));
			vo.setTempName(f.getName());
			list.add(vo);
		}
		
		
		SortCollection sort= new SortCollection(list);
		sort.addSortField("tempName",SortCollection.DESC);//估计大小写也有影响 需要修改代码 
		sort.sort(); 
		System.out.println("sortcoll:");
		for (int i = 0; i < files.length; i++)
			System.out.print(files[i] + " \n");
		Long endTime2 = System.currentTimeMillis();
		//读取文件的时候 默认就是读取的按照名称分组的
		System.out.println(endTime2-begTime2);
		*/
	}

}
