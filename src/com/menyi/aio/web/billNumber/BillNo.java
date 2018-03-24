package com.menyi.aio.web.billNumber;

import java.sql.Connection;
import java.util.*;

import com.dbfactory.Result;
import com.menyi.aio.bean.TblBillNoHistoryBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.aio.web.billNumber.BillNoMgt;

/**
 * 用来生成系列号，此类线程安全 用到的数据表 tblBillNo id int identity,key varchar(50),pattern
 * varchar(200),billNO int,start int,step int,isfillback bit,reset int,laststamp
 * bigint,isAddbeform bit tblBillNoHistory id int identity,key varchar(50),value
 * int,formatedString varchar(200),timestamp long,removed smallint(0已使用1空出来2已填空)
 * 索引：所有id都是主键. tblbillno key 索引 tblbillnohistory key,formatedstring key,removed
 * 
 * @author FANGZW
 * 
 */
public class BillNo {

	/**
	 * 系列号当前已使用数字
	 */
	private int billNO = 0;

	/**
	 * 系列号标识 现在定规则为表名加字段名用下划线连接。 例：tblrole_id
	 */
	private String key;

	/**
	 * 用来生成帐单号格式的字符串
	 */
	private String pattern;

	// /**<pre>
	// * 存储被删除的数字的值 每次被删除均会加入此数组中 当{@link #reset()}时此数组会变成空 当{@link
	// #remove(int)}
	// * 时此数组会记录下来，当此数组空间不足时会扩充{@link #RESIZENUMBER}个空间
	// * </pre>
	// */
	// private TreeSet<Integer> data = new TreeSet<Integer>();
	/**
	 * 是否实现连号。 注意，启用此功能，系统响应速度会降低
	 */
	private boolean isFillBack = false;

	/**
	 * 重置周期，可以设置为年，月，日等。可参看{@link Calendar}里的Field设置
	 * 
	 * @see {@link Calendar #YEAR}
	 * @see {@link Calendar #MONTH}
	 * @see {@link Calendar #DATE}
	 */
	private int reset = 0;

	/**
	 * 上次获取数值的时间
	 */
	private long lastStamp = -1;

	/**
	 * 起始流水号
	 */
	private int startValue = 1;

	/**
	 * 流水号步长
	 */
	private int stepValue = 1;

	/**
	 * 编号生成时情况： 1录单时生成 0保存时生成 变更意义：是否补全部号。否是只补最近几个连续的号
	 */
	private boolean isAddbeform = false;

	/**
	 * 
	 * @param key
	 *            标识符
	 * @param reset
	 *            重置的单位,参见{@linkCalendar}的Field
	 * @param lastStamp
	 *            上次成功获取流水号的时间
	 * @param billNO
	 *            帐单号
	 * @param data
	 *            遗漏数据
	 */
	protected BillNo(String key, int reset, long lastStamp, int billNO) {
		this.key = key;
		this.reset = reset;
		this.lastStamp = lastStamp;
		this.billNO = billNO;
	}

	/**
	 * <pre>
	 * 获取流水号。
	 * 先检查此次获取流水号是否已跨越时间段，如果跨越时间段则先调用方法重置 流水号。
	 * 查找是否有重新放置回的数据，如查有则把该队列的数值返回，否则把帐单号加+step，并返回。
	 * </pre>
	 * 
	 * @return 流水号
	 */
	public synchronized BillNoUnit getNumber(HashMap<String, String> input, Object login) {
		return getNumber(input, login, null);
	}

	/**
	 * 重构getNumber方法
	 * 
	 * @param input
	 * @param login
	 * @return
	 */
	public synchronized BillNoUnit getNumber(HashMap<String, String> input, Object login, Connection conn) {
		if (willReset())
			reset(conn);
		int ret = -1;
		BillNoMgt mgt = new BillNoMgt();
		/**
		 * 1、新增时生成编号 ，直接从内存中取编号，并写入历史记录表。
		 * 2、补号不会在新增时执行这个方法，先从数据库中取已删除的编号，没有再从内存中取编号
		 * 3、保存时生成编号，不会在新增时执行这个方法，新增时只会产生一个为000的流水号，保存时才会执行这个方法
		 */
		if (isFillBack && isAddbeform()) {// 如果要回填，isFillBack 连号，isAddbeform是新增时生成编号，这两个条件都成立是启用补号的意思
			// TODO if tblBillNoHistory有记录 removed =1存在记录
			//启用补号时，从数据库历史记录表读删除的单号，并置标识为已填充
			
			TblBillNoHistoryBean bean = new TblBillNoHistoryBean();
			bean.setRemoved(1);
			bean.setKey(getKey());
			Result result = mgt.queryBillNoHis(bean, conn); // 查询数据
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 查询成功
				bean = (TblBillNoHistoryBean) result.getRetVal();
				if (bean != null) {
					// 存在记录
					ret = bean.getValue();
					mgt.updateRemoved(2, bean.getFormatedString(), key, conn); // 修改remove=2为已填充
				}
			}
		}
		if (ret < 0) {
			//这里如果是启用单据编号连续，直接从数据库中读取单据编号，利用数据库的事物锁机制保证编号的连续性，如果是从内存中读取，则在新增失败时要回手动回滚编号，
			//在多人同时操作时这可能造成断号，而数据库在执行失败时会自动回滚，而且事物锁保证了这期间其它线程拿不到最新编号。
			if(isFillBack){
				ret = mgt.queryBillNo(key, conn);
			}else{
				ret = billNO;
			}      
			billNO += stepValue;  
			
			BillNoUnit unit = new BillNoUnit(ret, new BillFormat(pattern).parseInt(ret, input, login));
			//只在更新BillNo时才更新数据库，且updateDB方法中修改了更新数据库时采用全局变量的做法，采用局部ret+步长的方式更新，避免billNo因多线程变动问题
			updateDb(unit, login, conn);
			return unit;
		}else{       
			//如果补号且查到有删除的号，则不更新数据库了，原来是有更新的，因为原来更新时采用BillNo这个全局变量，所以这种补号情况因没有变动故并不会真正更新BillNo,
			BillNoUnit unit = new BillNoUnit(ret, new BillFormat(pattern).parseInt(ret, input, login));
			return unit;
		}
		
		
	}

	/**
	 * 获取默认格式单据编号，用于单据编号连续时显示默认编号，保存时再修改
	 * 
	 * @param input
	 * @param login
	 * @return
	 */
	public BillNoUnit getInvers(HashMap<String, String> input, Object login) { 
		BillNoUnit unit = new BillNoUnit(000, new BillFormat(pattern).parseInt(000, new HashMap<String, String>(), login, false));
		return unit;
	}

	private void updateDb(BillNoUnit unit, Object login) {
		updateDb(unit, login, null);
	}

	/**
	 * 修改表tblBillNo和添加数据到tblBillNoHistory
	 * 
	 * @param unit
	 * @param login
	 */
	private void updateDb(BillNoUnit unit, Object login, Connection conn) {
		// 更新tblBillNo和tblBillNoHistory两个表
		/* 修改tblBillNo数据 billNo,lastStamp */
		BillNoMgt mgt = new BillNoMgt();
		mgt.updateBillNo(unit.getValue()+stepValue, unit.getStamp(), key, conn);
		TblBillNoHistoryBean bean = new TblBillNoHistoryBean();
		bean.setKey(key);
		bean.setFormatedString(unit.getValStr());
		bean.setRemoved(0);
		bean.setValue(unit.getValue());
		bean.setTimestamp(unit.getStamp());
		mgt.addBillNoHistory(bean, conn);

	}

	/**
	 * 进行是否要进行重置流水的判断
	 * 
	 * @return
	 */
	private boolean willReset() {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(System.currentTimeMillis());
		c2.setTimeInMillis(lastStamp);
		if (c1.get(reset) != c2.get(reset))// 如果跨时间段，则自动执行重置功能
			return true;
		return false;
	}

	/**
	 * 重置 帐单号
	 */
	public void reset() {
		reset(null);
	}

	/**
	 * 重置 帐单号
	 */
	private void reset(Connection conn) {
		billNO = startValue;
		clearDb(conn);// 清除已存储的帐单号
	}

	/**
	 * 根据key，清除数据库中已存储的需要补号的数据
	 */
	private void clearDb(Connection conn) {
		// 清除tblbillnohistory表里的此key相关的所有数据
		BillNoMgt mgt = new BillNoMgt();
		mgt.updateBillNoHistory(key, conn);

		// 并且把tblbillno表中的，billno,laststamp等更新
		lastStamp = System.currentTimeMillis();
		mgt.updateBillNo(billNO, lastStamp, key, conn);

	}

	/**
	 * 根据key，清除数据库中已存储的需要补号的数据
	 */
	private void clearDb() {
		clearDb(null);
	}

	/**
	 * <pre>
	 * 把系列号归还，此系列号可以重新使用
	 * 如果此系列号不自动连号，则不进行任何处理 {@link #isFillBack}
	 * 系列号必须大于0.如果系列号已存在则返回失败
	 * </pre>
	 * 
	 * @param serial
	 *            重新返回的系列号
	 */
	public synchronized void remove(String valStr, Connection conn) {
		if(isFillBack){
			BillNoMgt mgt = new BillNoMgt();
			BillNoUnit billunit = getFromValStr(valStr, conn);
			if (billunit == null) {
				return;
			}
			int serial = billunit.getValue();
			if (serial < 1)
				return;
			int removedStr = 1;
			if (serial == billNO) {
				// 相等
				billNO -= stepValue;
				removedStr = 2;
			}
			mgt.updateBillNoHistoryRemove(key, valStr, removedStr, conn); // 修改tblBillNoHistory表中相应的数据
			mgt.updateBillNo(billNO, System.currentTimeMillis(), key, conn); // 修改tblBillNo表中相应的数据
		}
	}

	/**
	 * <pre>
	 * 把系列号归还，此系列号可以重新使用
	 * 如果此系列号不自动连号，则不进行任何处理 {@link #isFillBack}
	 * 系列号必须大于0.如果系列号已存在则返回失败
	 * </pre>
	 * 
	 * @param serial
	 *            重新返回的系列号
	 */
	public synchronized void remove(String valStr) {
		remove(valStr, null);
	}

	/**
	 * 根据key,单据编号删除值
	 * 
	 * @param key
	 * @param valStr
	 */
	public static void billNoRemove(String key, String valStr) {
		billNoRemove(key, valStr, null);
	}

	/**
	 * 根据key,单据编号删除值
	 * 
	 * @param key
	 * @param valStr
	 */
	public static void billNoRemove(String key, String valStr, Connection conn) {
		BillNo billNo = BillNoManager.find(key, conn);
		if (billNo != null) {
			billNo.remove(valStr, conn);
		}
	}

	/**
	 * 通过帐单号查出流水单元
	 * 
	 * @param valStr
	 *            生成出来的帐单号
	 * @return
	 */
	private BillNoUnit getFromValStr(String valStr, Connection conn) {
		// TODO 从tblbillnohistory表中查出
		BillNoMgt mgt = new BillNoMgt();
		TblBillNoHistoryBean bean = new TblBillNoHistoryBean();
		bean.setFormatedString(valStr);
		bean.setRemoved(0);
		bean.setKey(key);
		Result result = new Result();
		if(conn != null){
			result = mgt.queryBillNoHis(bean, conn);
		}else{
			result = mgt.queryBillNoHistory(bean);
		}
		BillNoUnit unit = null;
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 查询成功
			bean = (TblBillNoHistoryBean) result.getRetVal();
			if (bean != null) {
				// 存在数据
				unit = new BillNoUnit(bean.getValue(), bean.getFormatedString());
			}
		}
		return unit;
	}

	/**
	 * 通过帐单号查出流水单元
	 * 
	 * @param valStr
	 *            生成出来的帐单号
	 * @return
	 */
	private BillNoUnit getFromValStr(String valStr) {
		return getFromValStr(valStr, null);
	}

	public int getBillNO() {
		return billNO;
	}

	public void setBillNO(int billNO) {
		this.billNO = billNO;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public boolean isFillBack() {
		return isFillBack;
	}

	public void setFillBack(boolean isFillBack) {
		this.isFillBack = isFillBack;
	}

	public int getReset() {
		return reset;
	}

	public void setReset(int reset) {
		this.reset = reset;
	}

	public long getLastStamp() {
		return lastStamp;
	}

	public void setLastStamp(long lastStamp) {
		this.lastStamp = lastStamp;
	}

	public int getStartValue() {
		return startValue;
	}

	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}

	public int getStepValue() {
		return stepValue;
	}

	public void setStepValue(int stepValue) {
		this.stepValue = stepValue;
	}

	public boolean isAddbeform() {
		return isAddbeform;
	}

	public void setAddbeform(boolean isAddbeform) {
		this.isAddbeform = isAddbeform;
	}

	public class BillNoUnit {
		int value;

		String valStr;

		long stamp;

		public BillNoUnit(int value, String valStr) {
			this.value = value;
			this.valStr = valStr;
			this.stamp = System.currentTimeMillis();
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getValStr() {
			return valStr;
		}

		public void setValStr(String valStr) {
			this.valStr = valStr;
		}

		public long getStamp() {
			return stamp;
		}

		public void setStamp(long stamp) {
			this.stamp = stamp;
		}

	}

	public static void main(String[] args) {
		String pattern = "{date yyyy-MM-dd}=={date yyyy/MM/dd}=={login.getname}=={serial 0000}";
		final HashMap<String, String> map = new HashMap<String, String>();
		// map.put("name", "方志文");
		final Object o = new BillFormat("").new LoginInfo("方志文", "0001", "研发部");
		final BillNo g = new BillNo("gfs", Calendar.MONTH, 0, 0);
		g.pattern = pattern;
		g.isFillBack = true;
		Thread[] t = new Thread[20];
		final Random r = new Random();
		for (int i = 0; i < t.length; i++) {
			t[i] = new Thread(new Runnable() {
				ArrayList<String> al = new ArrayList<String>();

				public void run() {
					int times = 1000;
					while (times-- > 0) {
						if (r.nextInt(10) > 4) {
							BillNoUnit unit = g.getNumber(map, o);
							al.add(unit.getValStr());
							System.out.println(Thread.currentThread().getName() + ":取数据:" + unit.getValStr());
						} else {
							String tmpI = al.get(r.nextInt(al.size()));
							System.out.println(Thread.currentThread().getName() + ":删除数据" + tmpI);
							g.remove(tmpI);
						}
						try {
							Thread.sleep(r.nextInt(300));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
		for (Thread thread : t) {
			thread.start();
		}
	}

}
