package com.menyi.aio.web.billNumber;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * ����ˮ��ת����ϵ�к�
 * pattern�ĸ�ʽ˵����
 * patternΪһϵ���ַ�������ϣ�����ˮ���й̶����ַ���ֱ�����룬��Ҫ���������ĵط���<b>{}</b>�ֿ���
 * ����
 * �������Կո�ֿ���ǰ���ʾ������ʽ�������ݣ�����Ϊ��ʽ���ĸ�ʽ�������ŵ����ݿ��Բ����ʹ��ȱʡ��ʽ��
 * {date [yyyy-MM-dd]}Ϊ�ѵ�ǰʱ�䰴"yyyy-MM-dd"�ĸ�ʽ���и�ʽ��������μ�{@link SimpleDateFormat},Date��ȱʡ��ʽΪ"yyyyMMdd"
 * {serial [####]}Ϊ�ѵ�ǰ��ˮ�Ű����ֽ��и�ʽ��������μ���{@link DecimalFormat}��ȱʡֵΪ��####
 * {login.[property]}Ϊ��ȡ����¼��Ա����Ϣ��ȱʡΪ�����˵�����
 * {input.property}Ϊ��ȡ��ǰ�������Ϣ����ȱʡֵ��
 * 
 * �������ӣ�
 * ģ�壺{date yyyy-MM-dd}-{input.name}-{login.name}-{serial ####} �����2002-12-25-[���������name��ֵ]-[�����˵���]-0009
 * </pre>
 * 
 * @author FANGZW
 * 
 */
public class BillFormat {
	String pattern = "yyyy";
	private ArrayList<String> al = new ArrayList<String>();
	/**
	 * �ʵ��Ÿ�ʽ��ģ������
	 * @param pattern 
	 */
	public BillFormat(String pattern) {
		this.pattern = pattern;
		compile();
	}
	/**
	 * ����ģ�壬��ģ���ֳɶ�Σ����η���һ��������
	 */
	private synchronized void compile() {
		String regex = "(\\{.*?\\})|([^\\{\\}]+)";
		Matcher m = Pattern.compile(regex).matcher(pattern);
		while (m.find()) {
			al.add(m.group());
		}
	}
	/**
	 * ��ϵ�кŽ�������Ҫ�ĸ�ʽ��ģ����μ�ģ�嶨��
	 * @param serial ϵ�к�
	 * @param input ¼���ֵ
	 * @param login �û���¼��Ϣ
	 * @return ��ʽ������ַ���
	 */
	public synchronized String parseInt(int serial, HashMap<String, String> input, Object login)
	{
		return parseInt(serial, input, login,true);
	}
	
	public synchronized String parseInt(int serial, HashMap<String, String> input, Object login,boolean useInput) {
		StringBuffer sb = new StringBuffer();

		for (String part : al) {// �Ը����ڵ����ѭ��
			if (part.startsWith("{")) {
				String regex = "\\{([^ ]*)[ ]*(.*)\\}";// ������{xxxx
														// xxxx}���ָ�ʽ���ַ��������з�
				Matcher m = Pattern.compile(regex).matcher(part);
				Format f = null;
				if (m.find())
					if (m.group(1).equals("date"))// ���������
					{
						if (!m.group(2).equals("")) {
							f = new SimpleDateFormat(m.group(2));
						} else {
							f = new SimpleDateFormat("yyyyMMdd");
						}
						sb.append(f.format(new Date()));
					} else if (m.group(1).equals("serial"))// �������ˮ��
					{
						if (!m.group(2).equals("")) {
							f = new DecimalFormat(m.group(2));
						} else {

							f = new DecimalFormat("####");
						}
						sb.append(f.format(new Integer(serial)));
					} else if (m.group(1).startsWith("input") && input != null) {
						if (m.group(1).indexOf('.') != -1)// ���������.��������������
						{
							String tmpValue = input.get(m.group(1).substring(m.group(1).indexOf('.') + 1));
							if (tmpValue != null)
								sb.append(tmpValue);
							else
								if(useInput)
								sb.append(m.group(1).substring(m.group(1).indexOf('.') + 1));
						}
					} else if (m.group(1).startsWith("login") && login != null) {
						if (m.group(1).indexOf('.') != -1)// ���������.��������������
						{
							String tmpValue = m.group(1).substring(m.group(1).indexOf('.') + 1);
							for (Method method : login.getClass().getMethods()) {
								if(method.getName().equalsIgnoreCase(tmpValue))
									try {
										tmpValue = String.valueOf(method.invoke(login));
									} catch (IllegalAccessException e) {
										e.printStackTrace();
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									} catch (InvocationTargetException e) {
										e.printStackTrace();
									}
							}
							if (tmpValue != null)
								sb.append(tmpValue);
						}
					}
			} else {
				sb.append(part);
			}
		}
		return sb.toString();
	}

	class LoginInfo
	{
		String name,id,department;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public LoginInfo(String name, String id, String department) {
			super();
			this.name = name;
			this.id = id;
			this.department = department;
		}
	}
	
	public static void main(String[] args) {
		String pattern = "{date yyyy-MM-dd}-{input.name}-{login.getDepartment}-{serial ##}";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", "����");
		System.out.println(new BillFormat(pattern).parseInt(2345, map,new BillFormat("").new LoginInfo("����", "0001", "�з���")));
	}
}