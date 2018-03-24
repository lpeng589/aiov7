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
 * Title: ���ڼ����������
 * <p>
 * Description:
 * </p>
 * 
 * @Date:
 * @Copyright: �������
 * @Author ë��
 * @preserve all
 */

public class SortCollection {
	/**
	 * ����
	 */
	public static final int ASC = 1;

	/**
	 * ����
	 */
	public static final int DESC = 2;

	private List list;

	private ComparatorChain myBeanComparator = new ComparatorChain();

	public SortCollection(List list) {
		this.list = list;
	}

	/**
	 * ���������ֶ�(����)
	 * 
	 * @param field
	 *            ����������ֶ���
	 */
	public void addSortField(String field) {
		addSortField(field, ASC);
	}

	/**
	 * ���������ֶ�
	 * 
	 * @param field
	 *            ����������ֶ���
	 * @param sort
	 *            �����ǽ���
	 */
	public void addSortField(String field, int sort) {
		Comparator factoryComparator = new BeanComparator(field,
				new CollatorComparator(sort));
		myBeanComparator.addComparator(factoryComparator);
	}

	/**
	 * ����
	 */
	public void sort() {
		Object o = myBeanComparator;
		Collections.sort(list, myBeanComparator);
	}

	/**
	 * ����������ڲ���
	 * 
	 * @author Administrator
	 * @createdate 2007-7-13 ����04:24:51
	 */
	private class CollatorComparator implements Comparator {
		/**
		 * �����ж��������ǽ���ı�־
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