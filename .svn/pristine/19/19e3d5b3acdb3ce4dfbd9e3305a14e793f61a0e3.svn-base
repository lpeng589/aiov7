package com.menyi.web.util;

import com.dbfactory.DBCanstant;


/**
 * <p>Title: 错误常量类 (注：-2222已被数据库使用)</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public interface ErrorCanst extends DBCanstant {
	
    /**
     * 数据库操作类错误1100开始
     */
    public static int MULTI_VALUE_ERROR = -1109; //值重复
    public static int DATA_ALREADY_USED = -1110; //已经被使用
    public static int NUMBER_COMPARE_ERROR = -1111; //数据比较错误
    public static int TIMER_COMPARE_ERROR = -1112; //时间比较错误
    public static int RET_NAME_PSW_ERROR = -1114; //用户名或者密码错误
    public static int RET_NO_RIGHT_ERROR = -1115; //没有权限错误
    public static int RET_PASSWORD_ERROR = -1117; //密码错误
    public static int RET_NUMERICAL_OVERFLOW = -1118; //数据库数据运算溢出的错误 
    public static int RET_PROCEDURE_ERROR = -1119; //数据库存储过程返回错误
    public static int USER_STOP = -1219; //用户停用

    /**
     * 表操作类错误 2000开始
     */
    public static int RET_ILLEGAL_ARGUMENT_ERROR = -2001; //参数错误 IllegalArgument 添加,修改
    public static int RET_DEFAULT_TYPE_ERROR = -2002; //默认值数值格式不正确 添加
    public static int RET_TABLENAME_EXIST_ERROR = -2003; //表名已存在 添加
    public static int RET_PERANT_TABLE_ERROR = -2004; //主表不存在 添加
    public static int RET_ASSOCIATE_NOT_EXIST_ERROR = -2005; //关联字段不存在 添加
    public static int RET_ASSOCIATE_FIELD_TYPE_ERROR = -2006; //关联字段类型不是整形 添加
    public static int RET_EXIST_CHILD_TABLE_ERROR = -2007; // 存在子表不能删除， 删除
    public static int RET_TABLE_NOT_EXIST_ERROR = -2008; // 删除的表不存在， 删除,修改
    public static int RET_FORBID_UPDATE_ERROR = -2009; // 用户修改了不允许修改的字段
    public static int RET_HAS_VALUE_FORBID_UPDATE = -2028; // 表中存在数据不可以修改
    public static int RET_FIELD_EXIST_ERROR = -2010; // 列名重复 添加，修改
    public static int RET_BEGINACC_END = -2011; // 已经开账
    public static int RET_SETTLEACC_END = -2012; // 已经月结
    public static int RET_NOTSETTLEACC_LAST = -2013; // 最后一个期间未月结
    public static int RET_SETTLE_END=-2014;     //反开账时有已经月结的会计期间
    public static int RET_NOTBEGINACC=-2015;     //反开账时有已经月结的会计期间
    public static int RET_NOTSETTLEPERIOD=-2016;     //没有已经月结的会计期间
    public static int RET_NOTGRANT_UPDATEACC=-2017;     //修改了不允许修改的会计科目期初
    public static int RET_NOTREC_RECORDEXCHANGE=-2018;     //未录入记账汇率
    public static int RET_NOTREC_ADJUSTEXCHANGE=-2019;     //未录入月末调整汇率
    public static int RET_EXISTS_ADJUSTEXCHANGE=-2020;     //未录入月末调整汇率
    public static int RET_ACCNOTEQUAL=-2021;              //会计科目不平衡
    public static int RET_NOTSETROWMARKER=-2022;        //没有设置行标识字段
    public static int RET_ACCMAIN_NOTAPPROVE=-2023 ;//还有凭证还没有审核，不能月结
    public static int RET_BILL_NOTAPPROVE=-2026 ;//还有单据还没有审核，不能月结
    public static int RET_BILL_NOTCREATEACC=-2030 ;//还有单据没有产生凭证，不能月结
    public static int RET_BEGINACC_NOBEGINPERIOD=-2024;//系统未设置开账期间，不能开账
    public static int RET_HAS_AUDITING = - 2025 ;//已经审核或在审核中
    public static int RET_NOTPROFITLOSS_ERROR = - 2030 ;//已经审核或在审核中
    public static int RET_NOTSETTLE_LASTNOTYEAR = - 2027 ;//上一年没有年结，不可以进行月结
    public static int RET_BILL_EXISTDRAFT=-2028 ;//还有草稿单据没有过账，不能月结
    public static int RET_ASSET_NODEPRECIATE=-2029 ;//还有资产未进行计提折旧，不能月结

    /**
     * 自定义数据，数据库操作 2100开始
     *
     */
    public static int RET_FIELD_VALIDATOR_ERROR = -2100; // 字段校验错误
    public static int RET_FIELD_CANNOTSPEC_ERROR = -2120; // 不能包含除字母、数字和下划线之外的字符
    public static int RET_ID_NO_VALUE_ERROR = -2101; // 数据不存在
    public static int RET_DEFINE_SQL_ERROR = -2102; //自定义sql语句返回此错误后。当在提示信息中添加返回的msg
    public static int RET_DEFINE_SQL_NAME = -2103; //自定义sql不存在
    public static int RET_DEFINE_SENTENCE_ERROR=-2104;//自定义文件中语句书写错误
    public static int RET_DEFINE_VALUEOFDB_FORMAT_ERROR=-2105;//使用@ValueOfDB:的格式不正确（格式：@ValueofDB:tableName_fieldName）
    public static int RET_DEFINE_SQL_CONFIRM=-2106;   //自定义配置文件需要弹出（是否）提示
    public static int RET_NO_DEFINE_SQL = -2107 ; // 表信息维护中 没有定义sql语句
    public static int RET_EXISTSRELATION_ERROR=-2109; //存在关联单据
    public static int CURRENT_ACC_BEFORE_BILL = -2110 ; /*不能录入会计前数据*/
    public static int BILL_DATE_NOT_EXIST_CURRENT_ACC = -2111 ;/*单据日期的期间在会计期间中不存在*/
    public static int ADD_LOG_FAILURE = -2112 ;//添加日志失败
    public static int ADD_AUDITING_LAST_NODE_FAILURE = -2113;//添加反审核需要的审核结点的失败
    public static int DO_WORKFLOW_EXCEPTION = -2114 ;//执行审核出现异常
    public static int RET_LIST_NOCOLUMN = -2115 ;//列表中没有列可以显示
    public static int RET_FIELD_IS_NULL = -2116 ; // 值为空
    public static int RET_INPRICE_IS_ZERO = -2117 ; // 入库单价为0
    public static int RET_RETURNPRICE_IS_ZERO = -2118 ; // 退货单价为0
    public static int MRP_ERROR_CODE = -2119 ;  /*mrp运算出错*/     
    public static int RET_DEFINE_SQL_ALERT = 2120 ;//执行完所有操作，提示
    public static int RET_BILL_HASCERTIFICATE = -2121 ;//当前单据之后的单据已经手工生成凭证，不允许更新的操作
    
    /**
     * 期初
     */
    public static int GOODS_INI_MULTI=-5001;
    public static int GOODS_INI_PROPDISPLAY=-5002;
    public static int GOODS_INI_MULTI_VAL=-5003;
    /**
     * 存储过程返回错误 2200开始
     *
     */
    public static int PROC_NEGATIVE_ERROR = -2200; // 产生负库存错误
    public static int SETTLE_LASTQTYNEGATIVE_ERROR = -2201; // 月结结存数负数错误
    
    /**
     * 报表设置，显示，打印错误3000开始
     */
    public static int  RET_REPORTSQL_BillREPORTNOTSETID=-3000;//单据报表SQL没有查询条件ID
    
    /**
     * 读取Excel单据错误 4000开始 
     */
    public static int EXCEL_READ_FIELDERROR=4000;//单据模板字段错误
    public static int EXCEL_READ_FIELDNOTEXIST=4001;//字段不存在
    public static int EXCELBILL_FIELDVALUE_NOTEXIST=4002;//单据字段值数据库中不存在报错
    public static int EXCEL_FIELDVALUE_PARSEERROR=4003;//单据字段字段值解析出错
    public static int EXCEL_ENUMFIELDVALUE_NOTEXIST=4004;//单据枚举字段值不存在
    public static int EXCEL_PERENTCLASSCODE_USED=4005;//父类已经被使用
    public static int EXCEL_REPORT_EXPORT=4006;
    /**
     * 狗类错误，9000开始
     *
     */
    public static int RET_USER_LIMIT_ERROR = -9000; // 用户数超最大限制。
    public static int RET_OAUSER_LIMIT_ERROR = -9011; // 用户数超最大限制。
    public static int RET_CRMUSER_LIMIT_ERROR = -9012; // 用户数超最大限制。
    public static int RET_HRUSER_LIMIT_ERROR = -9013; // 用户数超最大限制。
    public static int RET_ORDERSUSER_LIMIT_ERROR = -9014; // 用户数超最大限制。
    public static int RET_FUNCTION_LIMIT_ERROR = -9001; // 模块数超最大限制。
    public static int RET_EVALUATE_BIS_LIMIT_ERROR = -9002; // 试用版数据量超过限制。


    public static int RETURN_COL_CONFIG_SETTING = -1000010 ;//去列配置设置页面
    public static int NOT_SET_EMAIL_ACCOUNT = -1000011 ;//未设置邮件账户
    public static int SEND_EMAIL_ERROR = -1000012 ;//发送邮件失败
    
    public static int NO_ADD_AREA_CLIENT = -5000;	/*无权添加些区域的客户*/
    public static int OVER_WILL_CLIENT_QTY = -5001 ; /*超出了添加意向客户的数量*/
    public static int BACK_PUBLIC_CLIENT_TIME = -5002 ;/*回归客户池时长*/
    public static int COMPLETE_CLIENT_NO_CHANGE = -5003 ; /*成交客户不能为变其它的状态*/
    
    public static int QUORE_INTERNET_ORDER_FAILURE = -6001 ; /*销售出库引用网上订单 更新网上订单出错。*/
    public static int BILL_ADD_WORK_FLOW_ERROR = -6002 ; /*添加工作流时 数据报错*/
    public static int BILL_UPDATE_WORK_FLOW_ERROR = -6003 ;/*修改工作流时 数据报错*/
    public static int WORK_FLOW_NO_NEXT_NODE = -6004 ;
}
