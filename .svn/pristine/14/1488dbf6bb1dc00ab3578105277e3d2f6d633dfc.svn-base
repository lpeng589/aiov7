package com.menyi.aio.web.billNumber;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 把流水号转换成系列号
 * pattern的格式说明：
 * pattern为一系列字符串的组合，在流水号中固定的字符中直接输入，需要引进变量的地方用<b>{}</b>分开。
 * 例：
 * 大括号以空格分开，前面表示用来格式化的数据，后面为格式化的格式，中括号的数据可以不填，即使用缺省格式。
 * {date [yyyy-MM-dd]}为把当前时间按"yyyy-MM-dd"的格式进行格式化，具体参见{@link SimpleDateFormat},Date的缺省格式为"yyyyMMdd"
 * {serial [####]}为把当前流水号按数字进行格式化，具体参见：{@link DecimalFormat}，缺省值为：####
 * {login.[property]}为获取当登录人员的信息，缺省为登入人的姓名
 * {input.property}为获取当前输入框信息，无缺省值。
 * 
 * 具体例子：
 * 模板：{date yyyy-MM-dd}-{input.name}-{login.name}-{serial ####} 结果：2002-12-25-[表单输入框中name的值]-[登入人的名]-0009
 * </pre>
 * 
 * @author FANGZW
 * 
 */
public class BillFormat {
	String pattern = "yyyy";
	private ArrayList<String> al = new ArrayList<String>();
	/**
	 * 帐单号格式化模板生成
	 * @param pattern 
	 */
	public BillFormat(String pattern) {
		this.pattern = pattern;
		compile();
	}
	/**
	 * 编译模板，把模板拆分成多段，依次放入一个队列中
	 */
	private synchronized void compile() {
		String regex = "(\\{.*?\\})|([^\\{\\}]+)";
		Matcher m = Pattern.compile(regex).matcher(pattern);
		while (m.find()) {
			al.add(m.group());
		}
	}
	/**
	 * 把系列号解析成需要的格式，模板请参见模板定义
	 * @param serial 系列号
	 * @param input 录入的值
	 * @param login 用户登录信息
	 * @return 格式化后的字符串
	 */
	public synchronized String parseInt(int serial, HashMap<String, String> input, Object login)
	{
		return parseInt(serial, input, login,true);
	}
	
	public synchronized String parseInt(int serial, HashMap<String, String> input, Object login,boolean useInput) {
		StringBuffer sb = new StringBuffer();

		for (String part : al) {// 对各个节点进行循环
			if (part.startsWith("{")) {
				String regex = "\\{([^ ]*)[ ]*(.*)\\}";// 用来把{xxxx
														// xxxx}这种格式的字符串进行切分
				Matcher m = Pattern.compile(regex).matcher(part);
				Format f = null;
				if (m.find())
					if (m.group(1).equals("date"))// 如果是日期
					{
						if (!m.group(2).equals("")) {
							f = new SimpleDateFormat(m.group(2));
						} else {
							f = new SimpleDateFormat("yyyyMMdd");
						}
						sb.append(f.format(new Date()));
					} else if (m.group(1).equals("serial"))// 如果是流水号
					{
						if (!m.group(2).equals("")) {
							f = new DecimalFormat(m.group(2));
						} else {

							f = new DecimalFormat("####");
						}
						sb.append(f.format(new Integer(serial)));
					} else if (m.group(1).startsWith("input") && input != null) {
						if (m.group(1).indexOf('.') != -1)// 如果输入有.，则后面跟着属性
						{
							String tmpValue = input.get(m.group(1).substring(m.group(1).indexOf('.') + 1));
							if (tmpValue != null)
								sb.append(tmpValue);
							else
								if(useInput)
								sb.append(m.group(1).substring(m.group(1).indexOf('.') + 1));
						}
					} else if (m.group(1).startsWith("login") && login != null) {
						if (m.group(1).indexOf('.') != -1)// 如果输入有.，则后面跟着属性
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
		map.put("name", "李四");
		System.out.println(new BillFormat(pattern).parseInt(2345, map,new BillFormat("").new LoginInfo("李四", "0001", "研发部")));
	}
}