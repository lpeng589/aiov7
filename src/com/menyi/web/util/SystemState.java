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
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class SystemState {
	public static final int ACCOUNT_NORMAL = 0; //��������
    public static final int ACCOUNT_QUERY = 1; //��ѯ����
	
	public static final int NORMAL = 0; //ϵͳ����
    public static final int DB_CONNECT = 1; //���ݿ����Ӵ���

    public static final int EVALUATE_MAX = 100; //���õ��ʱ�䶨100

    
    public static final int DOG_FORMAL = 0 ; //���ܹ���������״̬
    public static final int DOG_EVALUATE = 1; //���ܹ����ð�����״̬
    public static final int DOG_ERROR_USER = 2; //�û������쳣
    public static final int DOG_ERROR_USER_DEFINE = 3; //�Զ���ģ���쳣
    public static final int DOG_ERROR_FORMAL_REG = 4; //��ʽ��δע��
    public static final int DOG_ERROR = 5; //���ܹ�������ܴ���
    public static final int DOG_ERROR_NODOG_NOENV = 6; //�޹�δ����
    public static final int DOG_ERROR_ENV_DATE = 7; //���ó���
    public static final int DOG_ERROR_ENV_BIS = 8; //���õ��ݹ���
    public static final int DOG_ERROR_LOCK = 9; //���õ��ݹ���
    public static final int DOG_ERROR_NOMODULE = 10; //ϵͳһ��ģ�鶼û�У�Ӧֱ��������ģ������ҳ��
    
    public static final int DOG_ERROR_PCNO = 11; //����ܵĻ������֤�鲻һ��
    public static final int DOG_ERROR_VERSIONNOMATCH = 12; //����汾����Ȩ��һ��
    public static final int DOG_ERROR_LIMIT_DATE = 13; //������Ȩʱ��
    public static final int DOG_ERROR_PROP_USED = 14; //δ��Ȩʹ�����ԣ������û��������ԣ�����ʹ��
    public static final int DOG_ERROR_MORE_CUR_USED = 15; //δ��Ȩʹ����ң������û�������ң����ѿ���
    public static final int DOG_ERROR_USER_OA = 16; //�û������쳣
    public static final int DOG_ERROR_USER_CRM = 17; //�û������쳣
    public static final int DOG_ERROR_USER_HR = 18; //�û������쳣
    public static final int DOG_ERROR_USER_ORDERS = 19; //�û������쳣
    public static final int DOG_ERROR_FINANCIAL_USED = 30; //δ��Ȩʹ�ñ�׼���񣬵����û����ã����ѿ���
    public static final int DOG_ERROR_LOSE_EFFEC = 31; //֤��ʧ��
    public static final int DOG_ERROR_USED_NODOG = 32; //����ʹ�ú�δ�幷
    
    public static final int DOG_RESTART = 20; //�����������������޸���Ҫ������
    
    public static final int ENCRYPTION_EVAL = 0; //�޼��ܷ�ʽ������״̬
    public static final int ENCRYPTION_DOG1 = 1; //��һ���ϼ��ܹ���
    public static final int ENCRYPTION_DOG2 = 2; //�ڶ����¼��ܹ���
    public static final int ENCRYPTION_SOFT = 3; //����ܡ�
    
    
    public static int serverCount = 1; //��֧����������������

    //ϵͳ��ǰ״̬�������������ݿ����Ӵ��󣬵��������⣬ϵͳ����ֹͣ�������ȴ��˹���Ԥ����ϵͳ�ָ�����
    public int lastErrorCode = 0;
    public String lastErrorMessge = "";

    public String evaluate_date = ""; //��������
    public String evaluate_no = ""; //���ú�
    private long evaluate_long = 0; //����ʱ��ĸ�������ʱ��
    private long date_long = 24*60*60000; //ÿ��ĺ�����

    public String noRegUseDate=null; //δע��ʱ����ʼʹ��ʱ�䣬�������10�죬ǿ��Ҫ��ע��


    public int dogState = DOG_FORMAL; //���ܹ�״̬
    public int userState = 0; //ϵͳ�û�����״̬������ܹ�״̬�ֿ������ڵ�½ʱ�����û�������ֻ���� admin��½
    public String dogId = ""; //���ܹ�����
    
    public static boolean develope = false ; //��������״̬
    
    public int encryptionType=0; //���ܷ�ʽ��
    
    public String registerDate="";
    public String companyName="";
    public String pcNo="";


    //�������
    public ArrayList moduleList = new ArrayList();
    public int languageNum = 3;
    public int userNum = 1; //�������û��������ϰ������û���
    public int pudUserNum = 1; //�����û�
    public int ecUserNum = 1; //�����û�
    public int oaUserNum = 1; //OA�û�
    public int crmUserNum = 1; //CRM�û�
    public int ordersUserNum = 1; //�µ��û�
    public int hrUserNum = 1; //HR�û�
    public String grantDate = ""; //��Ȩ����
    
    public boolean funUserDefine = false;
    public boolean funProduct = false;
    public boolean funZQYW = false; //��ǿҵ��ԭ��֧����
    public boolean funMoreCurrency = false;
    public boolean funAttribute = false;
    public boolean funEc = false;
    public boolean funOrders = false;
    public boolean funRetail=false;
    public boolean funZY =false;//רҵ�� ԭVIP
    public boolean funFinancial = false;
    public boolean funQJ = false;//���������콢��ı�ʶ
    
    public boolean isFree = false; //�Ƿ���Ѱ�

    public int relLanuageNum = 1; //�쳣ʱ��ʵ��������
    public int reluserNum = 1; //�쳣ʱ��ʵ���û���
    
    public String remoteName = null;
    
    

    public static SystemState instance = new SystemState();

    public int accountType = 0;
    
    
    public static long uCheckTime = System.currentTimeMillis();
    
    public static int bakState=0; //ϵͳ�����Ƿ�����
    public static String bakStateStr= ""; //ϵͳ�����쳣��ʾ��

    
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
