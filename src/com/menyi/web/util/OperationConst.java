package com.menyi.web.util;

/**
 *
 * <p>Title: 操作方式常量</p>
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
public interface OperationConst
{
    int OP_ADD            = 1;  //增加操作
    int OP_UPDATE         = 2;  //修改操作
    int OP_DELETE         = 3;  //删除操作
    int OP_QUERY          = 4;  //查询操作
    int OP_DETAIL         = 5;  //明细操作
    int OP_ADD_PREPARE    = 6;  //增加前准备操作
    int OP_UPDATE_PREPARE = 7;  //修改前准备操作
    int OP_CHECK          = 8;  //审核
    int OP_CHECK_PREPARE  = 9;  //审核前的准备操作
    int OP_BACKUPDB       = 12;  //备份数据库操作
    int OP_DRAFT          = 13; //存为草稿
    int OP_CHECK_LIST     = 14; //审核流程显示
    int OP_QUOTE          = 15;  //引用操作
    int OP_COPY_PREPARE   = 16;  //复制前准备操作
    int OP_COPY           =17;   //复制操作
    int OP_PRINT          =18;   //打印操作
    int OP_UPDATE_ADD     =88;   //如果数据存在执行修改，如果数据不存在执行添加

    int OP_LOGIN=10;  //登陆操作
    int OP_LOGOUT=11; //退出操作

    int OP_READ_OVER = 19 ;
    int OP_TABLE_VIEW_PREPARE = 20; //自定义表显示配置
    int OP_TABLE_VIEW = 21; //自定义表显示配置
    int OP_POPUP_SELECT = 22; //自定义表显示配置
    int OP_TABLE_VIEW_PREPARE_MYD = 23; //选择框字段显示配置
    int OP_TABLE_VIEW_QUERY_MYD = 24;//弹出框查询操作
    int OP_EXTENDBUTTON_DEFINE = 25;//自定义拓展按钮操作

    int OP_BUTTON_AUDITING	   = 26 ; //模块中 按钮审核
    int OP_BUTTON_REV_AUDITING	   = 27 ; //模块中 按钮反审核
    int OP_LANGUAGE_SETTING_PREPARE = 28 ; //语言设置前
    int OP_LANGUAGE_SETTING = 29 ;		//语言设置

    int OP_DELETE_PREPARE        = 30;  //删除前准备操作

    int OP_SCOPE_RIGHT_QUERY         = 31; // 用户的范围权限控制前的操作
    int OP_SCOPE_RIGHT_ADD_PREPARE         = 33; // 用户的范围权限控制前的操作
    int OP_SCOPE_RIGHT_UPDATE_PREPARE         = 35; // 用户的范围权限控制前的操作
    int OP_SCOPE_RIGHT_ADD           = 37; // 用户的范围权限控制增加
    int OP_SCOPE_RIGHT_UPDATE        = 38; // 用户的范围权限控制修改
    int OP_SCOPE_RIGHT_DELETE            = 41; // 用户的显示权限控制增加
    
    int OP_SET_ALERT = 42 ;			//添加提醒设置
    int OP_CANCEL_ALERT = 43 ;		//取消提醒设置

    int OP_MODULE_RIGHT_PREPARE         = 51;  //模块权限准备
    int OP_MODULE_RIGHT    = 52;  //模块权限修改
    int OP_UPLOAD_PREPARE = 53 ;  //上传之前
    int OP_UPLOAD = 54 ;			//上传
    
    int OP_UPGRADE_PREPARE    = 60;  //升级准备
    int OP_UPGRADE    = 61;  //升级准备
    

    int OP_SEND_PREPARE     =70;//即时消息发送前准备操作
    int OP_SEND             =71;//即时消息发送操作
    int OP_REVERT_PREPARE   =72;//即时消息回复前操作
    int OP_REVERT           =73;//即时消息回复操作
    int OP_RECEIVE			=69;//接收消息

    int OP_BBS_MANAGE =74; //用户管理

    int OP_OA_JSON = 75;//生成动态树数据
    int OP_OA_GRID = 76;//生成动态表格数据
    int OP_OA_VIEW_SINLE = 77;//查看单条记录
    int OP_OA_VIEW_GROUOP_QUERY = 78;//根据组名查询
    int Op_AUDITING=79;//审核权限
    int Op_RETAUDITING=83;//反审核
    int OP_AUDITING_PREPARE=82;//审核前操作
    int OP_DOWNLOAD = 80 ;//下载
    int frameLeft = 81 ;//左框架页面的跳转
    
    int OP_SET_PREPARE = 84 ;
    int OP_SET = 85 ;
    
    int OP_IMPORT_PREPARE = 91; //数据导入准备
    int OP_IMPORT = 92;//数据导入
    int OP_READ = 93 ;//阅办
    int OP_NEXT_NODE = 94 ;//获取下个审核结点
    
    int OP_URL_TO = 96;//转跳
    int OP_ERROR_LIST = 97;//添加失败用户列表
    int OP_STOCKDISTRIBUTING = 98; //到库存分布表
    int OP_EXPORT = 99;//导出
    
    int BATCHDELETE = 100;  //批量删除
    int OP_UPDATE_PWD_PREPARE = 101 ;//修改密码
    int OP_UPDATE_PWD = 102 ;
    
    int OP_SELECT_PIC = 401;//选择图片
    
    String OP_GROUPID_ONE_NUM = "1";//收件箱
    String OP_GROUPID_TWO_NUM = "2";//草稿箱
    String OP_GROUPID_THREE_NUM = "3";//已发送邮件箱
    String OP_GROUPID_FOUR_NUM = "4";//垃圾邮件箱
    String OP_GROUPID_FIVE_NUM = "5";//废件箱
    
    String OP_GROUPID_ONE_STR = "收件箱";//1
    String OP_GROUPID_TWO_STR = "草稿箱";//2
    String OP_GROUPID_THREE_STR = "已发送邮件箱";//3
    String OP_GROUPID_FOUR_STR = "垃圾邮件箱";//4
    String OP_GROUPID_FIVE_STR = "废件箱";//5
    
    
    
}
