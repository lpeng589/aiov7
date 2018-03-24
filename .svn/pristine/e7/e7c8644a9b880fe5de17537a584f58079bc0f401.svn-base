package com.menyi.aio.web.sysAcc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.AccBalanceBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.*;

/**
 * 
 * <p>Title:业务月结</p> 
 * <p>Description: </p>
 *
 * @Date:2011-5-10
 * @Copyright: 科荣软件
 * @Author 张雪
 */
public abstract class ReCalcucateThreadPool implements Runnable{
	
	protected  ArrayList hashList = new ArrayList();
	protected  ArrayList seqhashList = new ArrayList();//存储所有有序列号的hash值，因为这部分往往是带序列号成品，计算上要尽量靠后，否则会因为成本变动在计算完生品出库后又引起成本变化而生重算
	protected int PeriodYear;
	protected int Period;
	
	final String DigitsAmount=BaseEnv.systemSet.get("DigitsAmount").getSetting();
	final String DigitsPrice=BaseEnv.systemSet.get("DigitsPrice").getSetting();

	/**
	 * 取其中一个hash值，注意多线程处理，此方法必须同步
	 * @return
	 */
	public  synchronized String  getHash() {
		if(hashList.size() > 0){
			return hashList.remove(0).toString();
		}else if(seqhashList.size() > 0){
			return seqhashList.remove(0).toString();
		}else{
			return null;
		}
	}
	
	public  synchronized void  putHash(String hash) {
		hashList.remove(hash); //如果这个hash还存在，则删除，并把hash加入最列尾。多个原料同时影响同一成品出库时这种方式可减少运算量
		hashList.add(hash);
	}
	public  synchronized void  putSeqHash(String hash) {
		seqhashList.remove(hash); //如果这个hash还存在，则删除，并把hash加入最列尾。多个原料同时影响同一成品出库时这种方式可减少运算量
		seqhashList.add(hash);
	}
	
	private HashMap<String,Integer> allotCount = new HashMap<String,Integer>(); //同价调拨死循环计数器
	/**
	 * 同价调拨时设置Hash
	 * @param hash
	 */
	public  void  putAllotHash(String outHash,String inhash) {
		putHash(inhash);
		int count = 0;
		if(allotCount.get(outHash+":"+inhash) != null){
			count = allotCount.get(outHash+":"+inhash);
		}
		System.out.println(outHash+":"+inhash+"-------------"+count);
		allotCount.put(outHash+":"+inhash, count+1);
	}
	/**
	 * 取计数器
	 * @param hash
	 * @return
	 */
	public  int  getAllotHashCount(String outHash,String inhash) {
		int count = 0;
		if(allotCount.get(outHash+":"+inhash) != null){
			count = allotCount.get(outHash+":"+inhash);
		}
		return count;
	}
	
	public int hashSize(){
		return hashList.size();
	}
	
	/**
	 * 设置所有要处理的hash列表
	 * @param list
	 */
	public ReCalcucateThreadPool(ArrayList hashList,ArrayList seqList,int PeriodYear,int Period) {
		this.hashList = hashList;
		this.seqhashList = seqList;
		this.Period = Period;
		this.PeriodYear = PeriodYear;
		allotCount.clear();
	}
	
	/**
	 * 把查询从run中剥离开来，且此方法以query开头，是无事物的。避免表锁带来的等待，
	 * @param goodPropHash
	 */
	public abstract Result queryLastAmt(final String goodPropHash);
	
	/**
	 * 把查询从run中剥离开来，且此方法以query开头，是无事物的。避免表锁带来的等待，
	 * @param goodPropHash
	 */
	public abstract Result queryRewriteId(final String goodPropHash);
	
	public abstract void run();
}
