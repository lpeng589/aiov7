package com.menyi.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * <p>Title: </p>
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
public class SystemState {
	public static final int ACCOUNT_NORMAL = 0; //正常帐套
    public static final int ACCOUNT_QUERY = 1; //查询帐套
	
	public static final int NORMAL = 0; //系统正常
    public static final int DB_CONNECT = 1; //数据库连接错误

    public static final int EVALUATE_MAX = 100; //试用的最长时间定100

    
    public static final int DOG_FORMAL = 0 ; //加密狗正版正常状态
    public static final int DOG_EVALUATE = 1; //加密狗试用版正常状态
    public static final int DOG_ERROR_USER = 2; //用户超限异常
    public static final int DOG_ERROR_USER_DEFINE = 3; //自定义模块异常
    public static final int DOG_ERROR_FORMAL_REG = 4; //正式版未注册
    public static final int DOG_ERROR = 5; //加密狗或软加密错误
    public static final int DOG_ERROR_NODOG_NOENV = 6; //无狗未试用
    public static final int DOG_ERROR_ENV_DATE = 7; //试用超期
    public static final int DOG_ERROR_ENV_BIS = 8; //试用单据过量
    public static final int DOG_ERROR_LOCK = 9; //试用单据过量
    public static final int DOG_ERROR_NOMODULE = 10; //系统一个模块都没有，应直接引导进模块升级页面
    
    public static final int DOG_ERROR_PCNO = 11; //软加密的机器码和证书不一致
    public static final int DOG_ERROR_VERSIONNOMATCH = 12; //软件版本和授权不一致
    public static final int DOG_ERROR_LIMIT_DATE = 13; //超过授权时间
    public static final int DOG_ERROR_PROP_USED = 14; //未授权使用属性，但是用户启用属性，且已使用
    public static final int DOG_ERROR_MORE_CUR_USED = 15; //未授权使用外币，但是用户启用外币，且已开账
    public static final int DOG_ERROR_USER_OA = 16; //用户超限异常
    public static final int DOG_ERROR_USER_CRM = 17; //用户超限异常
    public static final int DOG_ERROR_USER_HR = 18; //用户超限异常
    public static final int DOG_ERROR_USER_ORDERS = 19; //用户超限异常
    public static final int DOG_ERROR_FINANCIAL_USED = 30; //未授权使用标准财务，但是用户启用，且已开账
    public static final int DOG_ERROR_LOSE_EFFEC = 31; //证书失败
    public static final int DOG_ERROR_USED_NODOG = 32; //正常使用后，未插狗
    
    public static final int DOG_RESTART = 20; //服务器正在重启，修改重要参数后
    
    public static final int ENCRYPTION_EVAL = 0; //无加密方式，试用状态
    public static final int ENCRYPTION_DOG1 = 1; //第一版老加密狗。
    public static final int ENCRYPTION_DOG2 = 2; //第二版新加密狗。
    public static final int ENCRYPTION_SOFT = 3; //软加密。
    
    
    public static int serverCount = 1; //狗支持最多的正常帐套数

    //系统当前状态，如正常，数据库连接错误，等致命问题，系统必须停止工作，等待人工干预，或系统恢复正常
    public int lastErrorCode = 0;
    public String lastErrorMessge = "";

    public String evaluate_date = ""; //试用日期
    public String evaluate_no = ""; //试用号
    private long evaluate_long = 0; //试用时间的格林威治时间
    private long date_long = 24*60*60000; //每天的毫秒数

    public String noRegUseDate=null; //未注册时，开始使用时间，如果超过10天，强制要求注册


    public int dogState = DOG_FORMAL; //加密狗状态
    public int userState = 0; //系统用户数量状态，与加密狗状态分开管理，在登陆时超过用户数量后只允许 admin登陆
    public String dogId = ""; //加密狗代号
    
    public static boolean develope = false ; //开发环境状态
    
    public int encryptionType=0; //加密方式。
    
    public String registerDate="";
    public String companyName="";
    public String pcNo="";


    //软件功能
    public ArrayList moduleList = new ArrayList();
    public int languageNum = 3;
    public int userNum = 1; //进销存用户数，兼老版所有用户数
    public int pudUserNum = 1; //生产用户
    public int ecUserNum = 1; //电商用户
    public int oaUserNum = 1; //OA用户
    public int crmUserNum = 1; //CRM用户
    public int ordersUserNum = 1; //下单用户
    public int hrUserNum = 1; //HR用户
    public String grantDate = ""; //授权期限
    
    public boolean funUserDefine = false;
    public boolean funProduct = false;
    public boolean funZQYW = false; //增强业务，原分支机构
    public boolean funMoreCurrency = false;
    public boolean funAttribute = false;
    public boolean funEc = false;
    public boolean funOrders = false;
    public boolean funRetail=false;
    public boolean funZY =false;//专业版 原VIP
    public boolean funFinancial = false;
    public boolean funQJ = false;//现在用于旗舰版的标识
    
    public boolean isFree = false; //是否免费版

    public int relLanuageNum = 1; //异常时的实际语言数
    public int reluserNum = 1; //异常时的实际用户数
    
    public String remoteName = null;
    
    

    public static SystemState instance = new SystemState();

    public int accountType = 0;
    
    
    public static long uCheckTime = System.currentTimeMillis();
    
    public static int bakState=0; //系统备份是否正常
    public static String bakStateStr= ""; //系统备份异常提示语

    
    public int getAccountType() {
		return accountType;
	}



	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}



	public boolean isFunFinancial() {
		return funFinancial;
	}



	public void setFunFinancial(boolean funFinancial) {
		this.funFinancial = funFinancial;
	}



	public int getRemainEvalD(){
        try {
            if (evaluate_long == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                evaluate_long = sdf.parse(evaluate_date).getTime();
            }
            return EVALUATE_MAX - (int) ((System.currentTimeMillis() - evaluate_long) / date_long);
        } catch (ParseException ex) {
            return 0;
        }
    }
    
    

    public boolean isFunEc() {
		return funEc;
	}



	public void setFunEc(boolean funEc) {
		this.funEc = funEc;
	}



	public boolean isFunOrders() {
		return funOrders;
	}



	public void setFunOrders(boolean funOrders) {
		this.funOrders = funOrders;
	}



	public String getCompanyName() {
		return companyName;
	}



	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}



	public String getRegisterDate() {
		return registerDate;
	}



	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

    public int getDogState() {
        return dogState;
    }

    public String getDogId() {
		return dogId;
	}

	public void setDogId(String dogId) {
		this.dogId = dogId;
	}

	public int getLanguageNum() {
        return languageNum;
    }

    public int getLastErrorCode() {
        return lastErrorCode;
    }

    public String getLastErrorMessge() {
        return lastErrorMessge;
    }

    public ArrayList getModuleList() {
        return moduleList;
    }

    public int getUserNum() {
        return userNum;
    }

    public int getRelLanuageNum() {
        return relLanuageNum;
    }

    public int getReluserNum() {
        return reluserNum;
    }

    public String getNoRegUseDate() {
        return noRegUseDate;
    }

    public boolean isFunQJ() {
		return funQJ;
	}



	public void setFunQJ(boolean funQJ) {
		this.funQJ = funQJ;
	}



	public boolean isFunZQYW() {
		return funZQYW;
	}



	public void setFunZQYW(boolean funZQYW) {
		this.funZQYW = funZQYW;
	}



	public boolean isFunZY() {
		return funZY;
	}



	public void setFunZY(boolean funZY) {
		this.funZY = funZY;
	}



	public boolean isFunMoreCurrency() {
        return funMoreCurrency;
    }

    public boolean isFunProduct() {
        return funProduct;
    }

    public boolean isFunUserDefine() {
        return funUserDefine;
    }


    public boolean isFunAttribute() {
		return funAttribute;
	}



	public void setFunAttribute(boolean funAttribute) {
		this.funAttribute = funAttribute;
	}



	public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public void setModuleList(ArrayList moduleList) {
        this.moduleList = moduleList;
    }

    public void setLastErrorMessge(String lastErrorMessge) {
        this.lastErrorMessge = lastErrorMessge;
    }

    public void setLastErrorCode(int lastErrorCode) {
        this.lastErrorCode = lastErrorCode;
    }

    public void setLanguageNum(int languageNum) {
        this.languageNum = languageNum;
    }

    public void setDogState(int dogState) {
        this.dogState = dogState;
    }

    public void setReluserNum(int reluserNum) {
        this.reluserNum = reluserNum;
    }

    public void setRelLanuageNum(int relLanuageNum) {
        this.relLanuageNum = relLanuageNum;
    }

    public void setNoRegUseDate(String noRegUseDate) {
        this.noRegUseDate = noRegUseDate;
    }

    public void setFunUserDefine(boolean funUserDefine) {
        this.funUserDefine = funUserDefine;
    }

    public void setFunProduct(boolean funProduct) {
        this.funProduct = funProduct;
    }

    public void setFunMoreCurrency(boolean funMoreCurrency) {
        this.funMoreCurrency = funMoreCurrency;
    }

  

	public int getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(int encryptionType) {
		this.encryptionType = encryptionType;
	}



	public int getCrmUserNum() {
		return crmUserNum;
	}



	public void setCrmUserNum(int crmUserNum) {
		this.crmUserNum = crmUserNum;
	}



	public long getDate_long() {
		return date_long;
	}



	public void setDate_long(long date_long) {
		this.date_long = date_long;
	}



	public int getEcUserNum() {
		return ecUserNum;
	}



	public void setEcUserNum(int ecUserNum) {
		this.ecUserNum = ecUserNum;
	}



	public String getEvaluate_date() {
		return evaluate_date;
	}



	public void setEvaluate_date(String evaluate_date) {
		this.evaluate_date = evaluate_date;
	}



	public long getEvaluate_long() {
		return evaluate_long;
	}



	public void setEvaluate_long(long evaluate_long) {
		this.evaluate_long = evaluate_long;
	}



	public String getEvaluate_no() {
		return evaluate_no;
	}



	public void setEvaluate_no(String evaluate_no) {
		this.evaluate_no = evaluate_no;
	}



	public String getGrantDate() {
		return grantDate;
	}



	public void setGrantDate(String grantDate) {
		this.grantDate = grantDate;
	}



	public int getHrUserNum() {
		return hrUserNum;
	}



	public void setHrUserNum(int hrUserNum) {
		this.hrUserNum = hrUserNum;
	}



	public int getOaUserNum() {
		return oaUserNum;
	}



	public void setOaUserNum(int oaUserNum) {
		this.oaUserNum = oaUserNum;
	}



	public int getOrdersUserNum() {
		return ordersUserNum;
	}



	public void setOrdersUserNum(int ordersUserNum) {
		this.ordersUserNum = ordersUserNum;
	}



	public String getPcNo() {
		return pcNo;
	}



	public void setPcNo(String pcNo) {
		this.pcNo = pcNo;
	}



	public int getPudUserNum() {
		return pudUserNum;
	}



	public void setPudUserNum(int pudUserNum) {
		this.pudUserNum = pudUserNum;
	}



	public boolean isFunRetail() {
		return funRetail;
	}



	public void setFunRetail(boolean funRetail) {
		this.funRetail = funRetail;
	}

	public static int getServerCount() {
		return serverCount;
	}



	public static void setServerCount(int serverCount) {
		SystemState.serverCount = serverCount;
	}



	public boolean isFree() {
		return isFree;
	}



	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}



	public String getRemoteName() {
		return remoteName;
	}



	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}


}
