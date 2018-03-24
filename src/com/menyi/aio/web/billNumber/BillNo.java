package com.menyi.aio.web.billNumber;

import java.sql.Connection;
import java.util.*;

import com.dbfactory.Result;
import com.menyi.aio.bean.TblBillNoHistoryBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.aio.web.billNumber.BillNoMgt;

/**
 * ��������ϵ�кţ������̰߳�ȫ �õ������ݱ� tblBillNo id int identity,key varchar(50),pattern
 * varchar(200),billNO int,start int,step int,isfillback bit,reset int,laststamp
 * bigint,isAddbeform bit tblBillNoHistory id int identity,key varchar(50),value
 * int,formatedString varchar(200),timestamp long,removed smallint(0��ʹ��1�ճ���2�����)
 * ����������id��������. tblbillno key ���� tblbillnohistory key,formatedstring key,removed
 * 
 * @author FANGZW
 * 
 */
public class BillNo {

	/**
	 * ϵ�кŵ�ǰ��ʹ������
	 */
	private int billNO = 0;

	/**
	 * ϵ�кű�ʶ ���ڶ�����Ϊ�������ֶ������»������ӡ� ����tblrole_id
	 */
	private String key;

	/**
	 * ���������ʵ��Ÿ�ʽ���ַ���
	 */
	private String pattern;

	// /**<pre>
	// * �洢��ɾ�������ֵ�ֵ ÿ�α�ɾ���������������� ��{@link #reset()}ʱ��������ɿ� ��{@link
	// #remove(int)}
	// * ʱ��������¼��������������ռ䲻��ʱ������{@link #RESIZENUMBER}���ռ�
	// * </pre>
	// */
	// private TreeSet<Integer> data = new TreeSet<Integer>();
	/**
	 * �Ƿ�ʵ�����š� ע�⣬���ô˹��ܣ�ϵͳ��Ӧ�ٶȻή��
	 */
	private boolean isFillBack = false;

	/**
	 * �������ڣ���������Ϊ�꣬�£��յȡ��ɲο�{@link Calendar}���Field����
	 * 
	 * @see {@link Calendar #YEAR}
	 * @see {@link Calendar #MONTH}
	 * @see {@link Calendar #DATE}
	 */
	private int reset = 0;

	/**
	 * �ϴλ�ȡ��ֵ��ʱ��
	 */
	private long lastStamp = -1;

	/**
	 * ��ʼ��ˮ��
	 */
	private int startValue = 1;

	/**
	 * ��ˮ�Ų���
	 */
	private int stepValue = 1;

	/**
	 * �������ʱ����� 1¼��ʱ���� 0����ʱ���� ������壺�Ƿ�ȫ���š�����ֻ��������������ĺ�
	 */
	private boolean isAddbeform = false;

	/**
	 * 
	 * @param key
	 *            ��ʶ��
	 * @param reset
	 *            ���õĵ�λ,�μ�{@linkCalendar}��Field
	 * @param lastStamp
	 *            �ϴγɹ���ȡ��ˮ�ŵ�ʱ��
	 * @param billNO
	 *            �ʵ���
	 * @param data
	 *            ��©����
	 */
	protected BillNo(String key, int reset, long lastStamp, int billNO) {
		this.key = key;
		this.reset = reset;
		this.lastStamp = lastStamp;
		this.billNO = billNO;
	}

	/**
	 * <pre>
	 * ��ȡ��ˮ�š�
	 * �ȼ��˴λ�ȡ��ˮ���Ƿ��ѿ�Խʱ��Σ������Խʱ������ȵ��÷������� ��ˮ�š�
	 * �����Ƿ������·��ûص����ݣ��������Ѹö��е���ֵ���أ�������ʵ��ż�+step�������ء�
	 * </pre>
	 * 
	 * @return ��ˮ��
	 */
	public synchronized BillNoUnit getNumber(HashMap<String, String> input, Object login) {
		return getNumber(input, login, null);
	}

	/**
	 * �ع�getNumber����
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
		 * 1������ʱ���ɱ�� ��ֱ�Ӵ��ڴ���ȡ��ţ���д����ʷ��¼��
		 * 2�����Ų���������ʱִ������������ȴ����ݿ���ȡ��ɾ���ı�ţ�û���ٴ��ڴ���ȡ���
		 * 3������ʱ���ɱ�ţ�����������ʱִ���������������ʱֻ�����һ��Ϊ000����ˮ�ţ�����ʱ�Ż�ִ���������
		 */
		if (isFillBack && isAddbeform()) {// ���Ҫ���isFillBack ���ţ�isAddbeform������ʱ���ɱ�ţ����������������������ò��ŵ���˼
			// TODO if tblBillNoHistory�м�¼ removed =1���ڼ�¼
			//���ò���ʱ�������ݿ���ʷ��¼���ɾ���ĵ��ţ����ñ�ʶΪ�����
			
			TblBillNoHistoryBean bean = new TblBillNoHistoryBean();
			bean.setRemoved(1);
			bean.setKey(getKey());
			Result result = mgt.queryBillNoHis(bean, conn); // ��ѯ����
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// ��ѯ�ɹ�
				bean = (TblBillNoHistoryBean) result.getRetVal();
				if (bean != null) {
					// ���ڼ�¼
					ret = bean.getValue();
					mgt.updateRemoved(2, bean.getFormatedString(), key, conn); // �޸�remove=2Ϊ�����
				}
			}
		}
		if (ret < 0) {
			//������������õ��ݱ��������ֱ�Ӵ����ݿ��ж�ȡ���ݱ�ţ��������ݿ�����������Ʊ�֤��ŵ������ԣ�����Ǵ��ڴ��ж�ȡ����������ʧ��ʱҪ���ֶ��ع���ţ�
			//�ڶ���ͬʱ����ʱ�������ɶϺţ������ݿ���ִ��ʧ��ʱ���Զ��ع���������������֤�����ڼ������߳��ò������±�š�
			if(isFillBack){
				ret = mgt.queryBillNo(key, conn);
			}else{
				ret = billNO;
			}      
			billNO += stepValue;  
			
			BillNoUnit unit = new BillNoUnit(ret, new BillFormat(pattern).parseInt(ret, input, login));
			//ֻ�ڸ���BillNoʱ�Ÿ������ݿ⣬��updateDB�������޸��˸������ݿ�ʱ����ȫ�ֱ��������������þֲ�ret+�����ķ�ʽ���£�����billNo����̱߳䶯����
			updateDb(unit, login, conn);
			return unit;
		}else{       
			//��������Ҳ鵽��ɾ���ĺţ��򲻸������ݿ��ˣ�ԭ�����и��µģ���Ϊԭ������ʱ����BillNo���ȫ�ֱ������������ֲ��������û�б䶯�ʲ�������������BillNo,
			BillNoUnit unit = new BillNoUnit(ret, new BillFormat(pattern).parseInt(ret, input, login));
			return unit;
		}
		
		
	}

	/**
	 * ��ȡĬ�ϸ�ʽ���ݱ�ţ����ڵ��ݱ������ʱ��ʾĬ�ϱ�ţ�����ʱ���޸�
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
	 * �޸ı�tblBillNo��������ݵ�tblBillNoHistory
	 * 
	 * @param unit
	 * @param login
	 */
	private void updateDb(BillNoUnit unit, Object login, Connection conn) {
		// ����tblBillNo��tblBillNoHistory������
		/* �޸�tblBillNo���� billNo,lastStamp */
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
	 * �����Ƿ�Ҫ����������ˮ���ж�
	 * 
	 * @return
	 */
	private boolean willReset() {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(System.currentTimeMillis());
		c2.setTimeInMillis(lastStamp);
		if (c1.get(reset) != c2.get(reset))// �����ʱ��Σ����Զ�ִ�����ù���
			return true;
		return false;
	}

	/**
	 * ���� �ʵ���
	 */
	public void reset() {
		reset(null);
	}

	/**
	 * ���� �ʵ���
	 */
	private void reset(Connection conn) {
		billNO = startValue;
		clearDb(conn);// ����Ѵ洢���ʵ���
	}

	/**
	 * ����key��������ݿ����Ѵ洢����Ҫ���ŵ�����
	 */
	private void clearDb(Connection conn) {
		// ���tblbillnohistory����Ĵ�key��ص���������
		BillNoMgt mgt = new BillNoMgt();
		mgt.updateBillNoHistory(key, conn);

		// ���Ұ�tblbillno���еģ�billno,laststamp�ȸ���
		lastStamp = System.currentTimeMillis();
		mgt.updateBillNo(billNO, lastStamp, key, conn);

	}

	/**
	 * ����key��������ݿ����Ѵ洢����Ҫ���ŵ�����
	 */
	private void clearDb() {
		clearDb(null);
	}

	/**
	 * <pre>
	 * ��ϵ�кŹ黹����ϵ�кſ�������ʹ��
	 * �����ϵ�кŲ��Զ����ţ��򲻽����κδ��� {@link #isFillBack}
	 * ϵ�кű������0.���ϵ�к��Ѵ����򷵻�ʧ��
	 * </pre>
	 * 
	 * @param serial
	 *            ���·��ص�ϵ�к�
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
				// ���
				billNO -= stepValue;
				removedStr = 2;
			}
			mgt.updateBillNoHistoryRemove(key, valStr, removedStr, conn); // �޸�tblBillNoHistory������Ӧ������
			mgt.updateBillNo(billNO, System.currentTimeMillis(), key, conn); // �޸�tblBillNo������Ӧ������
		}
	}

	/**
	 * <pre>
	 * ��ϵ�кŹ黹����ϵ�кſ�������ʹ��
	 * �����ϵ�кŲ��Զ����ţ��򲻽����κδ��� {@link #isFillBack}
	 * ϵ�кű������0.���ϵ�к��Ѵ����򷵻�ʧ��
	 * </pre>
	 * 
	 * @param serial
	 *            ���·��ص�ϵ�к�
	 */
	public synchronized void remove(String valStr) {
		remove(valStr, null);
	}

	/**
	 * ����key,���ݱ��ɾ��ֵ
	 * 
	 * @param key
	 * @param valStr
	 */
	public static void billNoRemove(String key, String valStr) {
		billNoRemove(key, valStr, null);
	}

	/**
	 * ����key,���ݱ��ɾ��ֵ
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
	 * ͨ���ʵ��Ų����ˮ��Ԫ
	 * 
	 * @param valStr
	 *            ���ɳ������ʵ���
	 * @return
	 */
	private BillNoUnit getFromValStr(String valStr, Connection conn) {
		// TODO ��tblbillnohistory���в��
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
			// ��ѯ�ɹ�
			bean = (TblBillNoHistoryBean) result.getRetVal();
			if (bean != null) {
				// ��������
				unit = new BillNoUnit(bean.getValue(), bean.getFormatedString());
			}
		}
		return unit;
	}

	/**
	 * ͨ���ʵ��Ų����ˮ��Ԫ
	 * 
	 * @param valStr
	 *            ���ɳ������ʵ���
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
		// map.put("name", "��־��");
		final Object o = new BillFormat("").new LoginInfo("��־��", "0001", "�з���");
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
							System.out.println(Thread.currentThread().getName() + ":ȡ����:" + unit.getValStr());
						} else {
							String tmpI = al.get(r.nextInt(al.size()));
							System.out.println(Thread.currentThread().getName() + ":ɾ������" + tmpI);
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
