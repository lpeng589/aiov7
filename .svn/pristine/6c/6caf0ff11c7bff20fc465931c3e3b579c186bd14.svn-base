package com.menyi.web.util;

import org.apache.log4j.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.bsf.BSFManager;

import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.AIOShopBean;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.GoodsAttributeBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopupSelectBean;

import java.util.*;

import javax.servlet.ServletContext;


/**
 *
 * <p>Title: 环境变量类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class BaseEnv {
    public static final String CUR_INDEX_URL = "CUR_INDEX_URL";
    public static final String WIN_MAP = "WIN_MAP";
    public static Logger log= Logger.getLogger("MYLog");
    public static final String CUR_HELP_URL="CUR_INDEX_URL";
    public static final String TABLE_INFO="TABLE_INFO";
    public static final String PROP_INFO="PROP_INFO";
    public static final String PROP_INFOL="PROP_INFOL";
    public static final String TABLE_VIEW_INFO="TABLE_VIEW_INFO";
    public static final String USER_INFO="USER_INFO";
    public static  String CLOSE_ACC="";
    

    //public static String bol88URL= "http://www.bol88.com"; 
    public static String bol88URL= "http://localhost:8080";
    
    public static String PicScanURL = "http://netocr.com/api/recog.do";
    
    public static Locale defaultLocale ;

    public static String FILESERVERPATH = "";
    public static String REPORTPATH = "" ;
    public static String DEFINEPATH = "" ;
    public static String USERREPORTPATH = "" ;
    
    public static AIOShopBean AIO_SHOP = null ;
    
    public static Hashtable propMap=new Hashtable();//保存属性信息
    
    public static ArrayList<GoodsPropInfoBean> propList=new ArrayList<GoodsPropInfoBean>();//保存属性信息的列表
    public static ArrayList<GoodsAttributeBean> attList=new ArrayList<GoodsAttributeBean>();//保存属性信息的列表

    public static Hashtable enumerationMap=new Hashtable();
    
    public static HashMap<String,KRLanguage> ModuleTable = new HashMap<String,KRLanguage>();

//    public static ArrayList<String[]> pinYinList=new ArrayList<String[]>();//存放拼音码信息
    
    public static HashMap workProfessionMap=new HashMap();//存放行业信息
    
    
    public static HashMap districtMap=new HashMap();//存放行业信息
    
    public static HashMap deptMap=new HashMap();//存放部门信息
    
    public static HashMap defineSqlMap=new HashMap(); //用来标识自定义sql
    
    public static HashMap pupCalculate=new HashMap(); //用来保存公式的计算公式

    public static HashMap functionInterface = new HashMap();

    public static  BSFManager  evalManager = new BSFManager();//计算字符串

    public static HashMap<String,PopupSelectBean> popupSelectMap=new HashMap<String,PopupSelectBean>(); //用来标识弹出选择框


    public static HashMap defineAlertMap=new HashMap(); //保存自定义预警信息

    public static Hashtable<String,SystemSettingBean> systemSet = new Hashtable();

    public static Hashtable sessionSet = new Hashtable();//保存登录时的一些session信息
    
    public static HashMap<String,String[]> barcodeMap = new HashMap<String,String[]>(); //记录二维码
    
    public static ArrayList<String[]> isStartFlowInfo;//ERP所有单据是否启用审核流信息

//    public static String versionNumber ;//保存版本号(配置文件中版本号，zxy 去掉)

    public static String sysVersionName ;//保存版本号（数据库中版本名字）
    
    public static int version ;//保存版本（数据库中版本号）

    public static ArrayList popSelectFiles =  new ArrayList();

    public static ArrayList defineSqlFiles = new ArrayList();

     
    public static ArrayList buttonFiles = new ArrayList();


    public static ArrayList<String> defineAlertFileNames = new ArrayList<String>();
    
    public static String defineSateConfig = "";	/*CRM统计*/

    public static HashMap moduleMap = new HashMap(); 
    

    public static AIOTelecomCenter telecomCenter= null; 

    public static ArrayList<String> defineNoteFileNames= new ArrayList<String>();


    public static HashMap defineNoteMap=new HashMap();//保存自定义的短信及邮件信息

    public static HashMap defineBillMsgMap=new HashMap();//保存单据模板define信息


    public static Hashtable<String,DBTableInfoBean> tableInfos; //表信息

    public static String DefaultStyle ; //版本默认样式

    public static Hashtable localeTable ; //多语言表

    public static HashMap adminOperationMap = new HashMap();
    public static HashMap adminOoperationMapKeyId = new HashMap();
    
    public static int dayStudyNum;//每日一学数量

    public static HashMap popDisSentence;//弹出框修改详情的语句
    
    public static HashMap ModuleField;//模块字段
    
    public static InitMenData initMenData;
    
    public static String delAlert ;/*最近一次预警删除时间*/
    
    public static HashMap mailPoolThreadMap = new HashMap();
    
    
    public static HashMap<String ,AccPeriodBean> accPerios=new HashMap<String ,AccPeriodBean>();//保存不同分支机构的当前期间信息
    public static HashMap<String, AccPeriodBean> beginPerios = new HashMap<String, AccPeriodBean>() ;//开帐 期间
    public static String beginDate ;//开帐日期
    
    public static HashMap<String,WorkFlowDesignBean> workFlowDesignBeans=new HashMap<String,WorkFlowDesignBean>();
    
    public static Hashtable<String,OAWorkFlowTemplate> workFlowInfo;
    
    public static boolean alertUsing=false;
    
    public static String systemRealPath = "";
    
    public static ArrayList<ModuleBean> allModule;
    
    public static ArrayList holidayInfo;
    public static HashMap<String,GoodsPropInfoBean> propIgnoreCaseMap; //为解决属性大小写不规范的问题，增加小写属性和标准属性名字映射关系
    
    public static ArrayList<String[]> reportShowSet=new ArrayList(); //用于缓存报表显示字段 tableName,fieldName,reportView,billView,popSel,keyword,popupView
    
    
    public static Hashtable<String,	ArrayList<ColConfigBean>> colConfig;
    
    public static ArrayList<String[]> fastQuery = new ArrayList(); //保存快速查询数据
    
    public static Hashtable<String, ColDisplayBean> userLanguage;
    
    public static Hashtable<String, ColDisplayBean> userSettingWidth;
    
    public static ServletContext servletContext;
}
