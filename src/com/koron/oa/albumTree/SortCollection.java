package com.koron.oa.albumTree;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * <p>
 * Title: 用于集合排序的类
 * <p>
 * Description:
 * </p>
 * 
 * @Date:
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */

public class SortCollection {
	/**
	 * 升序
	 */
	public static final int ASC = 1;

	/**
	 * 降序
	 */
	public static final int DESC = 2;

	private List list;

	private ComparatorChain myBeanComparator = new ComparatorChain();

	public SortCollection(List list) {
		this.list = list;
	}

	/**
	 * 增加排序字段(升序)
	 * 
	 * @param field
	 *            用于排序的字段名
	 */
	public void addSortField(String field) {
		addSortField(field, ASC);
	}

	/**
	 * 增加排序字段
	 * 
	 * @param field
	 *            用于排序的字段名
	 * @param sort
	 *            升序还是降序
	 */
	public void addSortField(String field, int sort) {
		Comparator factoryComparator = new BeanComparator(field,
				new CollatorComparator(sort));
		myBeanComparator.addComparator(factoryComparator);
	}

	/**
	 * 排序
	 */
	public void sort() {
		Object o = myBeanComparator;
		Collections.sort(list, myBeanComparator);
	}

	/**
	 * 用于排序的内部类
	 * 
	 * @author Administrator
	 * @createdate 2007-7-13 下午04:24:51
	 */
	private class CollatorComparator implements Comparator {
		/**
		 * 用于判断是升序还是降序的标志
		 */
		private int sort = ASC;

		private Collator collator = Collator.getInstance();

		public CollatorComparator(int sort) {
			this.sort = sort;
		}

		public int compare(Object element1, Object element2) {
			CollationKey key1 = collator.getCollationKey(element1.toString());
			CollationKey key2 = collator.getCollationKey(element2.toString());
			if (sort == ASC) {
				return key1.compareTo(key2);
			} else {
				return key2.compareTo(key1);
			}
			
		}
	}

	public static void main(String[] args) {

		SortCollection sort= new SortCollection(null);
		sort.addSortField("field1");
		sort.addSortField("field2",SortCollection.ASC);
		sort.addSortField("field3",SortCollection.DESC);
		sort.addSortField("field4");
		sort.sort(); 
	}
}