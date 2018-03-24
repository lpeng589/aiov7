package com.menyi.aio.web.customize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dbfactory.Result;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.web.colconfig.ColConfigMgt;
import com.menyi.aio.web.userFunction.DynAjaxSelect;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfigParse;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.ReportField;

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
public class PopupSelectBean implements Serializable{
	/**
	 * 缺省规则
	 * @see DynAjaxSelect#popSelect(String, String, PopupSelectBean, java.util.Hashtable, ArrayList, HashMap, ArrayList, int, int, String, String, String, String, java.util.Locale, String, javax.servlet.http.HttpServletRequest, String, int)
	 */
	public static final int DEFAULTRULE = 8;
	/**
	 * 查询未级
	 * @see DynAjaxSelect#popSelect(String, String, PopupSelectBean, java.util.Hashtable, ArrayList, HashMap, ArrayList, int, int, String, String, String, String, java.util.Locale, String, javax.servlet.http.HttpServletRequest, String, int)
	 */
	public static final int LEAFRULE = 1;
	/**
	 * 查询非子级
	 * @see DynAjaxSelect#popSelect(String, String, PopupSelectBean, java.util.Hashtable, ArrayList, HashMap, ArrayList, int, int, String, String, String, String, java.util.Locale, String, javax.servlet.http.HttpServletRequest, String, int)
	 */
	public static final int BRANCHRULE = 2;
	/**
	 * 只查当前级
	 * @see DynAjaxSelect#popSelect(String, String, PopupSelectBean, java.util.Hashtable, ArrayList, HashMap, ArrayList, int, int, String, String, String, String, java.util.Locale, String, javax.servlet.http.HttpServletRequest, String, int)
	 */
	public static final int LEVELRULE = 4;
	
	
    private String name; //本bean的名称
    private String type; //弹出框的数据选择类型 1.（type="checkBox" 可以选择多项）     2.（默认单选）
    private String showType; //弹出框的显示类型 1.（showType="defNoShow" 默认没有条件不显示）2.（没有类型显示全部）
    private String selectType ;//tblEmployee=职员弹出窗口 tblDepartment=部门弹出窗口
    private ArrayList tables = new ArrayList(); //本bean对应的所有的表 ，非自定义弹出窗
    private String relation; //本bean对应的所有的关系
    private String condition; //查询条件
    private String classCode; //如果是分类，则指定分类代码
    private String classSysType;//如果分类弹出窗，在客户等时要指定标识，是客户，还是供应商
    private String stockTable ; //出库时，查询序列序列号的表名  --暂未发现有弹出窗在用
    private String hasChild; //如果有明细需要显示，则指定明细弹出框的名称
    
    private String topPopup; //多单引用时，先弹出一级主弹出窗，本身为明细弹出窗，这将替换之前的hasChild
    
    private ArrayList<PopField> displayField; //不包含隐藏可显示
    private ArrayList<PopField> displayField2; //所有弹出界面要显示的字段 已经过滤掉了系统配置未启用字段
    private ArrayList<PopField> displayField3; //弹出窗原始所有显示字段未过滤掉系统配置等,未处理变量字段
    private ArrayList<PopField> hiddenField ; //隐藏字段
    private ArrayList<PopField> saveFields = new ArrayList(); // 所有要保存的字段
    private ArrayList<PopField> saveFields3; //弹出窗原始所有保存字段未过滤掉系统配置等,未处理变量字段
    private String defParentCode;//默认父级

    private ArrayList<PopField> viewFields; //保存displayField中需要返回主表值的字段 这里要减去在saveField中存在的字段

    private ArrayList<PopField> allFields; //保存所有交集字段

    private ArrayList<PopField> returnFields;//保存所有需返回字段

    private PopField relationKey; //保存关联字段的信息,每个弹出窗只能一个relatioinKey字段，用于确定主表和弹出窗关联的字段而不是其它回填字段


    private String relationTable; // 关联的表名
    private String relationField; //关联的字段名

    private String popsql; //查询语句

    private ArrayList<String> tableParams; //引用主表的字段

    private ArrayList sysParams;  //引用系统参数

    private ArrayList sessParams;  //引用sess参数

    private ArrayList codeParams;  //引用code编码序列

    private boolean saveParentFlag=false; //如果弹出框是分类的数据，false 不可以保存父类数据，默认为false

    private String orderBy; //用于用户设置排序
    private String forwardModel;//页面要跳转的模块
    private String changeCond;//条件
    private boolean popEnter ;//是否在弹出窗口输入，然后Enter
    private boolean keySearch=false;//是否有关键字搜索
    private String defineSQL;//定制人员自己写SQL语句不通过列做拼接
   
    private int version;
    
    private String desc;//弹窗描述
    private String path;//弹窗所在路径
    
    
    
    /*
     * 所属表名
     * 用途：分级报表，在点击上下级中必须清除和这一行相关的查询条件，如库存查询分级报表，点商品下级时，条件中和商品相关的条件不能带到下级去。
     * 实现这个需求，在报表中查询条件DataType表明这个报表是和哪个表相关，再和弹出窗中对应的表名来决定哪些查询条件要清除
     */
    private String belongTableName;
    
    
    
    public ArrayList<PopField> getDisplayField3() {
		return displayField3;
	}


	public String getClassSysType() {
		return classSysType;
	}


	public void setClassSysType(String classSysType) {
		this.classSysType = classSysType;
	}


	public void setDisplayField3(ArrayList<PopField> displayField3) {
		this.displayField3 = displayField3;
	}


	public ArrayList<PopField> getSaveFields3() {
		return saveFields3;
	}


	public void setSaveFields3(ArrayList<PopField> saveFields3) {
		this.saveFields3 = saveFields3;
	}


	public String getBelongTableName() {
		return belongTableName;
	}


	public void setBelongTableName(String belongTableName) {
		this.belongTableName = belongTableName;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	/**
     * 从配置文件中分析出对象
     * @param node Node
     * @return InputValueBean
     */
    public static PopupSelectBean parse(Node node,HashMap<String,PopupSelectBean> map,HashMap pathMap,String path) throws Exception {
        PopupSelectBean bean = new PopupSelectBean();
        bean.path = path;
        
        ColConfigMgt configMgt = new ColConfigMgt() ;
        NamedNodeMap nnm = node.getAttributes();
        Node nodeName = nnm.getNamedItem("name");
        Node nodeDesc = nnm.getNamedItem("desc");
        Node nodeType = nnm.getNamedItem("type");
        Node nodeShowType=nnm.getNamedItem("showType");
        Node nodeSelect = nnm.getNamedItem("selectType") ;
        Node nodeVersion = nnm.getNamedItem("version");        
        bean.setName(nodeName.getNodeValue());
        
        if(nodeDesc!=null){
        	bean.setDesc(nodeDesc.getNodeValue());
        }
        if(bean.getDesc()== null || bean.getDesc().length() ==0){
        	bean.setDesc(bean.getName());
        }
        
        Node nodebelongTableName = nnm.getNamedItem("belongTableName");
        
        if(nodebelongTableName!=null){//所属表名 
        	bean.setBelongTableName(nodebelongTableName.getNodeValue());
        }else{
        	bean.setBelongTableName("");
        }

        if(nodeType!=null){//弹出框的类型 null：普通类型 multiSeleToRow：可以多选并且某些字段是填充一行数据 
        	bean.setType(nodeType.getNodeValue());
        }else{
        	bean.setType("");
        }
        if(nodeShowType!=null){
        	bean.setShowType(nodeShowType.getNodeValue());
        }else{
        	bean.setShowType("");
        }
        if(nodeSelect!=null){
        	bean.setSelectType(nodeSelect.getNodeValue());
        }else{
        	bean.setSelectType("");
        }
       
        bean.setVersion(1);
        if(nodeVersion!=null){
        	try {
        		if(nodeVersion!=null && !nodeVersion.equals(""))
				bean.setVersion(Integer.parseInt(nodeVersion.getNodeValue()));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        bean.viewFields = new ArrayList<PopField>();
        bean.allFields = new ArrayList<PopField>();
        bean.returnFields = new ArrayList<PopField>();
        bean.tableParams = new ArrayList();
        bean.sysParams = new ArrayList();
        bean.sessParams = new ArrayList();
        bean.codeParams = new ArrayList();

        String tablestr = "";
        String table1 = ""; //保存第一张表，用来生成ID
        Result popup_rs = configMgt.queryConfigExistByTableName(bean.getName(), "popup") ;
        ArrayList<ColConfigBean> colConfig = (ArrayList<ColConfigBean>) popup_rs.getRetVal() ; //列配置
        
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node temp = list.item(i);
            if (temp.getNodeName().equalsIgnoreCase("table")) {
                tablestr = temp.getFirstChild().getNodeValue();
                String[] tablestrs = tablestr.split(",");
                table1 = tablestrs[0];
                
                for (String table : tablestrs) {
                    bean.tables.add(table);
                }
            } else if (temp.getNodeName().equalsIgnoreCase("relation")) {
                String relationstr = temp.getFirstChild()!=null?temp.getFirstChild().getNodeValue():null;
                if (relationstr != null) {
                    bean.relation = relationstr.replaceAll(",", " and ");
                    //解析查询语句
                    ConfigParse.parseSentenceGetParam(bean.relation,bean.tableParams, bean.sysParams,bean.sessParams,bean.codeParams,new ArrayList(),null);
                }
            }else if (temp.getNodeName().equalsIgnoreCase("condition")) {
                    bean.condition = temp.getTextContent();
                    if(bean.condition!=null&&bean.condition.length()>0)
                    {
                        //解析查询语句
                        ConfigParse.parseSentenceGetParam(bean.condition,bean.tableParams, bean.sysParams,bean.sessParams,bean.codeParams,new ArrayList(),null);
                    }
            }else if (temp.getNodeName().equalsIgnoreCase("changeCond")) {
                bean.changeCond = temp.getTextContent();
                if(bean.changeCond!=null&&bean.changeCond.length()>0)
                {
                    //解析查询语句
                    ConfigParse.parseSentenceGetParam(bean.changeCond,bean.tableParams, bean.sysParams,bean.sessParams,bean.codeParams,new ArrayList(),null);
                }
            }else if(temp.getNodeName().equalsIgnoreCase("defineSQL")){
            	bean.defineSQL=temp.getTextContent();
                if(bean.defineSQL!=null&&bean.defineSQL.length()>0)
                {
                    //解析查询语句
                    ConfigParse.parseSentenceGetParam(bean.defineSQL,bean.tableParams, bean.sysParams,bean.sessParams,bean.codeParams,new ArrayList(),null);
                }
                
            }else if(temp.getNodeName().equalsIgnoreCase("classCode")){
                bean.classCode=temp.getTextContent();       
                if(bean.classCode != null && bean.classCode.length() > 0 && !bean.classCode.contains(("."))){
                	throw new Exception("弹出窗"+bean.getName()+"分级字段"+bean.classCode+"必须包括表名和字段名");
                }
                NamedNodeMap cnnm = temp.getAttributes();
                Node classSysTypeNode = cnnm.getNamedItem("classSysType");
                if(classSysTypeNode!=null){
                	bean.setClassSysType(classSysTypeNode.getNodeValue());
                }
            }else if(temp.getNodeName().equalsIgnoreCase("hasChild")){
                bean.hasChild=temp.getTextContent();
            }else if(temp.getNodeName().equalsIgnoreCase("topPopup")){
                bean.topPopup=temp.getTextContent();
            }else if(temp.getNodeName().equalsIgnoreCase("stockTable")){
            	bean.stockTable=temp.getTextContent() ;
            }else if(temp.getNodeName().equalsIgnoreCase("saveParentFlag")){
                bean.saveParentFlag=Boolean.parseBoolean(temp.getTextContent());
            }else if(temp.getNodeName().equalsIgnoreCase("orderBy")){
                bean.orderBy=temp.getTextContent();
            }else if(temp.getNodeName().equalsIgnoreCase("forwardModel")){
                bean.forwardModel=temp.getTextContent();
            }else if(temp.getNodeName().equalsIgnoreCase("defineSQL")){
                bean.defineSQL=temp.getTextContent();
            }else if(temp.getNodeName().equalsIgnoreCase("defParentCode")){
                bean.defParentCode =temp.getTextContent();
            }else if (temp.getNodeName().equalsIgnoreCase("displayFields")) {
                bean.displayField = new ArrayList<PopField>();
                bean.displayField2 = new ArrayList<PopField>() ;
                bean.displayField3 = new ArrayList<PopField>() ;
                bean.hiddenField = new ArrayList<PopField>() ;
                NodeList fieldList = temp.getChildNodes();
                for (int j = 0; j < fieldList.getLength(); j++) {
                    Node fieldNode = fieldList.item(j);
                    if (fieldNode.getNodeName().equalsIgnoreCase("field")) {
                        PopField df = new PopField();
                        df.type = PopField.TYPE_DISPLAY;
                        NamedNodeMap nnmfield = fieldNode.getAttributes();
                        df.fieldName = nnmfield.getNamedItem("name").getNodeValue();
                        Node displayNode = nnmfield.getNamedItem("display");
                        df.display = displayNode == null ? null : displayNode.getNodeValue();
                        Node inputTypeNode = nnmfield.getNamedItem("inputType");
                        df.inputType = inputTypeNode == null ? null : inputTypeNode.getNodeValue();
                        Node inputValueNode = nnmfield.getNamedItem("inputValue");
                        df.inputValue = inputValueNode == null ? null : inputValueNode.getNodeValue();                        
                        
                        Node isMobileNode = nnmfield.getNamedItem("isMobile");
                        df.isMobile = isMobileNode == null ? 2 : Byte.parseByte(inputValueNode.getNodeValue());    
                        
                        Node createSearchNode = nnmfield.getNamedItem("createSearch");
                        
                        boolean boolSysType = true ;
                        DBFieldInfoBean dbfield=null;                        
                        
                        if(df.fieldName.contains(".")){
	                      	String[] strFields =df.getDisplay()==null||df.getDisplay().indexOf(".")==-1 ?df.fieldName.split("\\."):df.getDisplay().split("\\.") ;
	                        dbfield= GlobalsTool.getFieldBean(strFields[0],strFields[1]) ;
	                        	                           
	                        if(dbfield!=null && dbfield.getFieldSysType()!=null && dbfield.getFieldSysType().length()>0){
	                        	if(BaseEnv.systemSet.get(dbfield.getFieldSysType())!=null){
	                        		boolSysType = Boolean.parseBoolean(BaseEnv.systemSet.get(dbfield.getFieldSysType()).getSetting());
	                        	}
	                        }
                        }
                        //系统类型节点，直接在字段中指定系统类型，系统类型启用时，此字段显示，否则就不显示
                        Node sysTypeNode = nnmfield.getNamedItem("sysType");
                        df.sysType = sysTypeNode == null ? null : sysTypeNode.getNodeValue();
                        if(df.sysType!=null&&df.sysType.length()>0){
                        	if(BaseEnv.systemSet.get(df.sysType)==null){
                        		BaseEnv.log.error("PopupSelectBean.parse Error file:"+path+" ;select="+bean.getName()+";SystemSet:"+df.sysType+"不存在");
                        		boolSysType = false;
                        	}else{
                        	boolSysType = Boolean.parseBoolean(BaseEnv.systemSet.get(df.sysType).getSetting());
                        	}
                        }
                        
                        Node asNameNode = nnmfield.getNamedItem("asName");
                        df.asName=asNameNode==null||asNameNode.getNodeValue()==null||asNameNode.getNodeValue().length()==0?df.fieldName:asNameNode.getNodeValue();
                        if(df.asName.indexOf("(")>-1 || df.asName.trim().indexOf(" ") > -1 || df.asName.trim().indexOf("+") > -1){
                        	throw new Exception("弹出窗"+bean.getName()+"复合字段："+df.getAsName()+"未设置asName!!!!!");
                        }
                        
                        Node reNotShowNode = nnmfield.getNamedItem("repeatNotShow");
                        df.repeatNotShow = reNotShowNode == null ? 0 : Integer.parseInt(reNotShowNode.getNodeValue());
                        if(nnmfield.getNamedItem("width") != null){
                        	df.width = Integer.parseInt(nnmfield.getNamedItem("width").getNodeValue());
                        }
                        Node keySearch = nnmfield.getNamedItem("keySearch");
                        df.keySearch=keySearch==null?false: Boolean.parseBoolean(keySearch.getNodeValue());
                        if(df.keySearch){
                        	bean.keySearch=true;
                        }
                        Node popSelect = nnmfield.getNamedItem("popSelect");
                        df.popSelect = popSelect == null ? null : popSelect.getNodeValue();
                        Node hiddenInput = nnmfield.getNamedItem("hiddenInput") ;
                        df.hiddenInput = hiddenInput==null?"": hiddenInput.getNodeValue() ;
                        Node searchNode = nnmfield.getNamedItem("searchType");
                        String ststr = searchNode == null ? null : searchNode.getNodeValue();                        
                        Node linkAddr = nnmfield.getNamedItem("linkAddr") ;
                        df.setLinkAddr(linkAddr==null?"":linkAddr.getNodeValue()) ;
                        
                        if("scope".equals(ststr)){
                            df.setSearchType(PopField.SEARCH_SCOPE);
                        }else if("match".equals(ststr)){
                            df.setSearchType(PopField.SEARCH_MATCH);
                        }else if("equal".equals(ststr)){
                            df.setSearchType(PopField.SEARCH_EQUAL);
                        }else if("matchl".equals(ststr)){
                        	df.setSearchType(PopField.SEARCH_MATCHL);
                        }else if("matchr".equals(ststr)){
                        	df.setSearchType(PopField.SEARCH_MATCHR);
                        }else if("no".equals(ststr)){
                        	df.setSearchType(PopField.SEARCH_NO) ;
                        }else if("more".equals(ststr)){
                        	df.setSearchType(PopField.SEARCH_MORE) ;
                        }else if("input".equals(ststr)){
                        	df.setSearchType(PopField.SEARCH_INPUT) ;
                        }
                        
                        Node defaultValueTemp = nnmfield.getNamedItem("defaultValue") ;
                        String dv = defaultValueTemp==null?"": defaultValueTemp.getNodeValue() ;
                        df.defaultValue=dv;
                        
                        //如果是多选要填充数据到一行，则此字段指定要填充的父窗口字段的类型
                        Node returnToIdentityNode = nnmfield.getNamedItem("returnToIdentity");
                        df.returnToIdentity = returnToIdentityNode == null ? "" : returnToIdentityNode.getNodeValue();
                        
                        df.parentDisplay = nnmfield.getNamedItem("parentDisplay") != null && 
                        	"true".equals(nnmfield.getNamedItem("parentDisplay").getNodeValue()) ? true : false;
                        
                        Node hasValNotFillNode = nnmfield.getNamedItem("hasValNotFill");
                        df.hasValNotFill=hasValNotFillNode==null?false:Boolean.parseBoolean(hasValNotFillNode.getNodeValue());
                        //如果属性字段未启用，并且字段系统类型为商品属性则界面不显示
                        boolean flag = true ; 
                        
                        
                        if (dbfield!=null && "GoodsField".equals(dbfield.getFieldSysType())) {
                        	String propFiledName = dbfield.getFieldName();
                        	//如果颜色名称，则按颜色来处理属性
                        	if("colorName".equals(propFiledName)||("ViewGoodsPropColor".equals(dbfield.getTableBean().getTableName())&&"PropEName".equals(propFiledName))){
                        		propFiledName = "color";
                        	}
                        	GoodsPropInfoBean gpBean=(GoodsPropInfoBean)BaseEnv.propIgnoreCaseMap.get(propFiledName.toLowerCase());
                            if(gpBean!=null&&gpBean.getIsUsed()==2){ 
                            	flag = false;								
							}										
						}
                        bean.displayField3.add(df) ; //记录原始字段信息，用于弹出窗的修改zxy--
                        
                        //处理商品弹出窗信息
                        if (df.fieldName.indexOf("[") > 0 && df.fieldName.indexOf("]") > 0) {
							String preFN = df.fieldName.substring(0, df.fieldName.indexOf("["));
							String showSet = df.fieldName.substring(df.fieldName.indexOf("[") + 1, df.fieldName.indexOf("]"));
							//tableName,fieldName,reportView,billView,popSel,keyword,popupView
	                        for (String[] shows : BaseEnv.reportShowSet) { 
								if (showSet.equals(shows[0]) && "1".equals(shows[6])) {
									PopField  rf = df.clone();
									rf.setFieldName(preFN + shows[1]);
									rf.setAsName(rf.getFieldName());
									rf.parentDisplay = true;
									if("GoodsFullName".equals(shows[1])){
										rf.setDisplay("@TABLENAME.GoodsCode");
									}else if("ComFullName".equals(shows[1])){
										if(showSet.equals("tblCompany_2")){
											rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_2")==null? "客户全称":BaseEnv.ModuleTable.get("tblCompany_2").get("zh_CN")+"全称");
										}else if(showSet.equals("tblCompany_1")){
											rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_1")==null? "供应商全称":BaseEnv.ModuleTable.get("tblCompany_1").get("zh_CN")+"全称");
										}else{
											rf.setDisplay("@TABLENAME.CompanyCode");
										}
									}else if("ComName".equals(shows[1])){
										if(showSet.equals("tblCompany_2")){
											rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_2")==null? "客户简称":BaseEnv.ModuleTable.get("tblCompany_2").get("zh_CN")+"简称");
										}else if(showSet.equals("tblCompany_1")){
											rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_1")==null? "供应商简称":BaseEnv.ModuleTable.get("tblCompany_1").get("zh_CN")+"简称");
										}else{
											rf.setDisplay("简称");
										}
									}else if("ComNumber".equals(shows[1])){
										if(showSet.equals("tblCompany_2")){
											rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_2")==null? "客户编号":BaseEnv.ModuleTable.get("tblCompany_2").get("zh_CN")+"编号");
										}else if(showSet.equals("tblCompany_1")){
											rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_1")==null? "供应商编号":BaseEnv.ModuleTable.get("tblCompany_1").get("zh_CN")+"编号");
										}else{
											rf.setDisplay("编号");
										}
									}else{
										rf.setDisplay(null);
									}
									DBFieldInfoBean fb = null;
									if(shows[0].indexOf("_")>0){
										fb = GlobalsTool.getFieldBean(shows[0].substring(0,shows[0].indexOf("_")) + "." + shows[1]);	
									}else{
										fb = GlobalsTool.getFieldBean(shows[0] + "." + shows[1]);	
									}
									if(fb==null){
										System.out.println("--");
									}
									rf.setFieldType(fb.getFieldType());	
									rf.setWidth(fb.getWidth());
									if(rf.getFieldName().endsWith("PYM")){
										rf.setWidth(0);
									}
									
									if((bean.getVersion()!=2 &&"1".equals(shows[3])) ||((bean.getVersion()==2 &&"1".equals(shows[2])))){ //单据显示或报表显示，版本为2的是报表
										rf.setParentDisplay(true);
									}else{
										rf.setParentDisplay(false);
									}
									if("1".equals(shows[4])){ //弹出窗条件
										if(fb != null && fb.getInputTypeOld()== DBFieldInfoBean.INPUT_ENUMERATE){
											rf.setSearchType(PopField.SEARCH_EQUAL);
										}else{
											rf.setSearchType(PopField.SEARCH_MATCH);
										}
									}else{
										rf.setSearchType(PopField.SEARCH_NO);
									}
									if("1".equals(shows[5])){ //关键字
										rf.keySearch = true;
									}else{
										rf.keySearch = false;
									}
									if(colConfig!=null && colConfig.size()>0){
			                        	boolean exist = false ;
			                        	for(int m=0;m<colConfig.size();m++){
			                        		ColConfigBean configBean = colConfig.get(m) ;
			                        		if(rf.getAsName().equals(configBean.getColName())){
			                        			bean.displayField.add(rf) ; //所有要显示字段
			                        			exist = true ;
			                        			break ;
			                        		}
			                        	}
			                        	if(!exist && boolSysType  && flag){
			                        		bean.hiddenField.add(rf) ; //所有隐藏可显示字段
			                        	}
			                        }else{
			                        	if(boolSysType  && flag){
				                            bean.displayField.add(rf);
			                        	}
			                        }                        
			                        if(boolSysType && flag){
			                        	bean.displayField2.add(rf) ; //所有显示字段包括隐藏字段
			                        }
			                        if (rf.parentDisplay && boolSysType  && flag) {
			                            bean.viewFields.add(rf); //所有要返回字段，不包括saveField
			                        }                        
			                        Node markNode = nnmfield.getNamedItem("marker");
			                        rf.marker = markNode == null ? "false" : markNode.getNodeValue();
								}
							}
                        }else{ //普通字段
                        	DBFieldInfoBean fb =  GlobalsTool.getFieldBean(df.getAsName());	
                        	if(fb != null){
                        		df.setFieldType(fb.getFieldType());
                        	}
                        	
	                        if(colConfig!=null && colConfig.size()>0){
	                        	boolean exist = false ;
	                        	for(int m=0;m<colConfig.size();m++){
	                        		ColConfigBean configBean = colConfig.get(m) ;
	                        		
	                        		if(df.getAsName().replace('+', ' ').equals(configBean.getColName())){
	                        			bean.displayField.add(df) ; //所有要显示字段
	                        			exist = true ;
	                        			break ;
	                        		}
	                        	}
	                        	if(!exist && boolSysType  && flag){
	                        		bean.hiddenField.add(df) ; //所有隐藏可显示字段
	                        	}
	                        }else{
	                        	if(boolSysType  && flag){
		                        	if(!"true".equals(df.hiddenInput)){
		                            	bean.displayField.add(df);
		                            }else{
		                        		bean.hiddenField.add(df) ;
		                        	}
	                        	}
	                        }                        
	                        if(boolSysType && flag){
	                        	bean.displayField2.add(df) ; //所有显示字段包括隐藏字段
	                        }
	                        if (df.parentDisplay && boolSysType  && flag) {
	                            bean.viewFields.add(df); //所有要返回字段，不包括saveField
	                        }                        
	                        Node markNode = nnmfield.getNamedItem("marker");
	                        df.marker = markNode == null ? "false" : markNode.getNodeValue();
                        }
                    }
                }
            } else if (temp.getNodeName().equalsIgnoreCase("saveFields")) {
                bean.saveFields = new ArrayList<PopField>();
                bean.saveFields3 = new ArrayList<PopField>();
                NodeList fieldList = temp.getChildNodes();
                for (int j = 0; j < fieldList.getLength(); j++) {
                    Node fieldNode = fieldList.item(j);
                    if (fieldNode.getNodeName().equalsIgnoreCase("field")) {
                        PopField sf = new PopField();
                        sf.type = PopField.TYPE_SAVE;
                        NamedNodeMap nnmfield = fieldNode.getAttributes();
                        sf.fieldName = nnmfield.getNamedItem("name").getNodeValue();
                        Node displayNode = nnmfield.getNamedItem("display");
                        sf.display=displayNode==null?sf.parentName:displayNode.getNodeValue();
                        Node hasValNotFillNode = nnmfield.getNamedItem("hasValNotFill");
                        sf.hasValNotFill=hasValNotFillNode==null?false:Boolean.parseBoolean(hasValNotFillNode.getNodeValue());
                        boolean boolSysType = true ;
                        DBFieldInfoBean dbfield = null ;
                        if(sf.fieldName.contains(".")){
                        	String[] strFields =sf.display==null?sf.fieldName.split("\\."): sf.display.split("\\.");
                            dbfield = GlobalsTool.getFieldBean(strFields[0],strFields[1]) ;
                           
                            if(dbfield!=null && dbfield.getFieldSysType()!=null && dbfield.getFieldSysType().length()>0){
                            	boolSysType = BaseEnv.systemSet.get(dbfield.getFieldSysType())==null?true:
                            		Boolean.parseBoolean(BaseEnv.systemSet.get(dbfield.getFieldSysType()).getSetting());
                            }
                        }
                        //系统类型节点，直接在字段中指定系统类型，系统类型启用时，此字段显示，否则就不显示
                        Node sysTypeNode = nnmfield.getNamedItem("sysType");
                        sf.sysType = sysTypeNode == null ? null : sysTypeNode.getNodeValue();
                        if(sf.sysType!=null&&sf.sysType.length()>0){
                        	if(BaseEnv.systemSet.get(sf.sysType)==null){
                        		BaseEnv.log.error("PopupSelectBean.parse Error file:"+path+" ;select="+bean.getName()+";SystemSet:"+sf.sysType+"不存在");
                        		boolSysType = false;
                        	}else{
                        		boolSysType = Boolean.parseBoolean(BaseEnv.systemSet.get(sf.sysType).getSetting());                        	
                        	}
                        }
                        Node asNameNode = nnmfield.getNamedItem("asName");
                        sf.asName=asNameNode==null?sf.fieldName:asNameNode.getNodeValue();
                        sf.parentName = nnmfield.getNamedItem("parentName").getNodeValue();
                        Node rNode = nnmfield.getNamedItem("relationKey");
                        sf.relationKey = rNode != null && "true".equals(rNode.getNodeValue()) ? true : false;
                        rNode = nnmfield.getNamedItem("hidden");
                        sf.hidden = rNode != null && "true".equals(rNode.getNodeValue()) ? true : false;
                       
                        Node reNotShowNode = nnmfield.getNamedItem("repeatNotShow");
                        sf.repeatNotShow = reNotShowNode == null ? 0 : Integer.parseInt(reNotShowNode.getNodeValue());
                        boolean flag = true ;
                        if (dbfield!=null && "GoodsField".equals(dbfield.getFieldSysType())) {
                        	String propFiledName = dbfield.getFieldName();
                        	//如果颜色名称，则按颜色来处理属性
                        	if("colorName".equals(propFiledName)||("ViewGoodsPropColor".equals(dbfield.getTableBean().getTableName())&&"PropEName".equals(propFiledName))){
                        		propFiledName = "color";
                        	}
                        	GoodsPropInfoBean gpBean=(GoodsPropInfoBean)BaseEnv.propMap.get(propFiledName);
                            if(gpBean!=null&&gpBean.getIsUsed()==2){ 
                            	flag = false;                            	
							}
						}
                        Node compareNode = nnmfield.getNamedItem("compare") ;
                        sf.compare = compareNode == null ? true : Boolean.parseBoolean(compareNode.getNodeValue()) ;
                        bean.saveFields3.add(sf);//记录原始保存字段信息，用于弹出窗修改
                        
                        //处理商品弹出窗信息
                        if (sf.fieldName.indexOf("[") > 0 && sf.fieldName.indexOf("]") > 0) {
							String preFN = sf.fieldName.substring(0, sf.fieldName.indexOf("["));
							String showSet = sf.fieldName.substring(sf.fieldName.indexOf("[") + 1, sf.fieldName.indexOf("]"));
							//tableName,fieldName,reportView,billView,popSel,keyword,popupView
	                        for (String[] shows : BaseEnv.reportShowSet) { 
								if (showSet.equals(shows[0]) && "1".equals(shows[6]) && ((bean.getVersion()!=2 &&"1".equals(shows[3])) ||((bean.getVersion()==2 &&"1".equals(shows[2]))))) {
									PopField  rf = sf.clone();
									rf.setFieldName(preFN + shows[1]);
									rf.setAsName(rf.getFieldName());
									rf.parentDisplay = false;
									if("GoodsFullName".equals(shows[1])){
										rf.setDisplay("@TABLENAME.GoodsCode");
									}else if("ComFullName".equals(shows[1])){
										rf.setDisplay("@TABLENAME.CompanyCode");
									}else{
										rf.setDisplay(null);
									}
									rf.setParentName("@TABLENAME."+(rf.getFieldName().replace('.', '_')));
									
									DBFieldInfoBean fb = null;
									if(shows[0].indexOf("_")>0){
										fb = GlobalsTool.getFieldBean(shows[0].substring(0,shows[0].indexOf("_")) + "." + shows[1]);	
									}else{
										fb = GlobalsTool.getFieldBean(shows[0] + "." + shows[1]);	
									}
									rf.setFieldType(fb.getFieldType());				        
									rf.setWidth(fb.getWidth());
									
									if(boolSysType && flag){
			                        	bean.saveFields.add(rf);
			                        }
			                        
			                        //如果是多选要填充数据到一行，则此字段指定要填充的父窗口字段的类型
			                        Node returnToIdentityNode = nnmfield.getNamedItem("returnToIdentity");
			                        rf.returnToIdentity = returnToIdentityNode == null ? "" : returnToIdentityNode.getNodeValue();
			                        Node markNode = nnmfield.getNamedItem("marker");
			                        rf.marker = markNode == null ?"false" : markNode.getNodeValue();
			                        
								}
							}
                        }else{ //普通字段                        
                        	DBFieldInfoBean fb =  GlobalsTool.getFieldBean(sf.getAsName());	
                        	if(fb != null){
                        		sf.setFieldType(fb.getFieldType());
                        	}
                        	if(boolSysType && flag){
	                        	bean.saveFields.add(sf);
	                        }
	                        if (sf.relationKey) {
	                        	if(bean.relationKey != null){
	//                        		BaseEnv.log.error("弹出窗"+bean.getName()+"存在多个relationKey字段");
	                        	}
	                            bean.relationKey = sf;
	                            if(sf.fieldName.contains(".")){
		                            bean.relationTable = sf.fieldName.substring(0, sf.fieldName.indexOf("."));
		                            bean.relationField = sf.fieldName.substring(sf.fieldName.indexOf(".") + 1);
	                            }else{
	                            	throw new Exception("弹出窗 "+bean.getName()+" relation字段"+sf.fieldName+"必须包括表名和字段名");
	                            }
	                        }
	                        //如果是多选要填充数据到一行，则此字段指定要填充的父窗口字段的类型
	                        Node returnToIdentityNode = nnmfield.getNamedItem("returnToIdentity");
	                        sf.returnToIdentity = returnToIdentityNode == null ? "" : returnToIdentityNode.getNodeValue();
	                        Node markNode = nnmfield.getNamedItem("marker");
	                        sf.marker = markNode == null ?"false" : markNode.getNodeValue();
                        }
                    }
                }

            }
        }
//        if(pathMap.get(bean.name) != null){
//        	BaseEnv.log.debug("PopupSelectBean.parse popup 重复加载 name:"+bean.name +"; oldFile="+pathMap.get(bean.name)+"; newFile="+path);
//        }
        map.put(bean.name, bean);
        pathMap.put(bean.name, path); 
       
        bean.popsql = " select ";
        int pos = 1;
        //找出要查询的列，取displayField和saveFields 的交集
        for (Object o : bean.saveFields) {
            PopField field = (PopField) o;
            bean.popsql += field.fieldName + " f"+pos+" ,";
            
            pos++;
            bean.allFields.add(field);
            bean.returnFields.add(field);

            //查找viewField中如果也存在，则删除viewField;
            for (int j=0;j<bean.viewFields.size();j++) {
                PopField field2 = (PopField) bean.viewFields.get(j);
                if (field.fieldName.equals(field2.fieldName)) {
                    bean.viewFields.remove(j);
                    break;
                }
            }
        }
        //找出在saveField中不存在，而DisplayField中存在的
        for (Object o : bean.displayField2) {
            boolean same = false;
            PopField field = (PopField) o;
            for (Object o2 : bean.saveFields) {
                PopField field2 = (PopField) o2;
                if (field.fieldName.equals(field2.fieldName)) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                bean.popsql += field.fieldName + " f" + pos + " ,";
                pos++;
                bean.allFields.add(field);
                if(field.isParentDisplay()||(!field.parentDisplay&&field.returnToIdentity.length()>0)){
                    bean.returnFields.add(field);
                }
            }
        }

        //分类数据显示
        if(bean.getClassCode()!=null&&bean.getClassCode().length()>0){
            //分类代码查询
            boolean same = false;
            for (Object o : bean.getAllFields()) {

                PopField field = (PopField) o;
                if (field.fieldName.equals(bean.getClassCode())) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                PopField field = new PopField();

                field.setFieldName(bean.getClassCode());
                field.setWidth(0);
                field.setParentDisplay(false);
                bean.allFields.add(field);

                bean.popsql += field.fieldName + " f" + pos;
            }
        }
        if(bean.defineSQL!=null&&bean.defineSQL.length()>0){
        	if (bean.popsql.endsWith(",")) {
	            bean.popsql = bean.popsql.substring(0, bean.popsql.length() - 1);
	        }
        	bean.popsql +="\n";
        	
    		int fpos = pos(bean.defineSQL,"from");
    		if(fpos == -1){
    			BaseEnv.log.error("PopupSelectBean.parse 弹出窗"+bean.name +"自定义语句找不到from"+bean.defineSQL);
    		}else{
    			bean.popsql += bean.defineSQL.substring(fpos);
    		}
//        	bean.popsql = bean.defineSQL;
        }else{
	        if (bean.popsql.endsWith(",")) {
	            bean.popsql = bean.popsql.substring(0, bean.popsql.length() - 1);
	        }
	
	        /*
	        将配置文件中输入的
	        tblGoods,tblStocks,tblUnit
	        tblGoods.classCode=tblStocks.GoodsCode and tblGoods.BaseUnit = tblUnit.ID
	        转换成如下格式
	        tblGoods  left join tblStocks on tblGoods.classCode=tblStocks.GoodsCode left join tblUnit on
	        tblGoods.BaseUnit = tblUnit.ID
	        */
	       ArrayList tabList=bean.getTables();
	       bean.popsql += tableTosql(tabList,bean.getRelation());
        }

        
        //解析查询语句
        ConfigParse.parseSentenceGetParam(bean.popsql,bean.tableParams, bean.sysParams,bean.sessParams,bean.codeParams,new ArrayList(),null);
        return bean;
    }
    
    public static String tableTosql(ArrayList tabList,String relation){    	
    	String ret =" from "+tabList.get(0);
        if(relation!=null&&relation.length()>0){
    	   if(relation.indexOf("on ")>=0){
    		   String[] relations = relation.split("on ");
	           for (int i = 1; i < tabList.size(); i++) {
	               ret += " left join " + tabList.get(i) + " on " +
	                       relations[i];
	           }
    	   }else{
	           String[] relations = relation.split("and");
	           for (int i = 1; i < tabList.size(); i++) {
	               ret += " left join " + tabList.get(i) + " on " +
	                       relations[i - 1];
	           }
    	   }
       } 
       return ret;
    }
    
    
	/**
	 * 本函数，用于在sql中查询指定字段串，中间要去掉（）子查询
	 * @param sql
	 * @param selStr
	 * @return
	 */
	public static int pos(String sql, String selStr) {
		sql = sql.toUpperCase();
		selStr = selStr.toUpperCase();
		int pos = -1;
		for (int i = 0; i < 100; i++) {
			pos = sql.indexOf(selStr, pos + 1);
			if (pos < 0) {
				return pos;
			}
			//查询本段中是否有匹配的()号，如果不匹配，找后一个selStr		
			if (ismatchkh(sql.substring(0, pos))) {
				return pos;
			}
		}

		return -1;
	}

	/**
	 * 计算字符串中是否是相匹配的(),如果()个数不想等为不匹配
	 * @param str
	 * @return
	 */
	private static boolean ismatchkh(String str) {
		boolean rs = false;
		Pattern pattern = Pattern.compile("(\\(|\\))", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		int kl = 0;
		int kr = 0;
		while (matcher.find()) {
			String s = matcher.group(1);
			if ("(".equals(s)) {
				kl++;
			} else if (")".equals(s)) {
				kr++;
			}
		}
		return kl == kr;
	}


    public String getTableName(String field) {
    	if(field != null && field.indexOf(".")>0){
    		return field.substring(0, field.indexOf("."));
    	}
        return "";
    }

    public String getFieldName(String field) {
    	if(field != null && field.indexOf(".")>0){
    		return field.substring(field.indexOf(".") + 1);
    	}
    	return "";
    }

    public ArrayList getTables() {
        return tables;
    }


    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    public ArrayList<PopField> getDisplayField() {
        return displayField;
    }
    
    public ArrayList<PopField> getDisplayField2() {
        return displayField2;
    }

    public String getPopsql() {
        return popsql;
    }

    public String getRelationField() {
        return relationField;
    }

    public String getRelationTable() {
        return relationTable;
    }

    public ArrayList<PopField> getSaveFields() {
        return saveFields;
    }

    public com.menyi.aio.web.customize.PopField getRelationKey() {
        return relationKey;
    }

    public ArrayList<PopField> getViewFields() {
        return viewFields;
    }

    public ArrayList<PopField> getAllFields() {
        return allFields;
    }
    /**<pre>
     * 弹出框所有需要回填的字段
     * 包括 {@link #getDisplayField()}中parentdisplay的字段和 {@link #getSaveFields()}
     * </pre>
     * @return
     */
    public ArrayList<PopField> getReturnFields() {
        return returnFields;
    }

    public String getCondition() {
        return condition;
    }

    public String getClassCode() {
        return classCode;
    }

    public ArrayList<String> getTableParams() {
        return tableParams;
    }

    public ArrayList getSysParams() {
        return sysParams;
    }

    public String getHasChild() {
        return hasChild;
    }

    public boolean isSaveParentFlag() {
        return saveParentFlag;
    }

    public ArrayList getSessParams() {
        return sessParams;
    }

    public ArrayList getCodeParams() {
        return codeParams;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setTables(ArrayList tables) {
        this.tables = tables;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setSaveFields(ArrayList saveFields) {
        this.saveFields = saveFields;
    }

    public void setRelationTable(String relationTable) {
        this.relationTable = relationTable;
    }

    public void setRelationField(String relationField) {
        this.relationField = relationField;
    }

    public void setPopsql(String popsql) {
        this.popsql = popsql;
    }

    public void setDisplayField(ArrayList<PopField> displayField) {
        this.displayField = displayField;
    }


    public void setRelationKey(com.menyi.aio.web.customize.PopField relationKey) {
        this.relationKey = relationKey;
    }

    public void setViewFields(ArrayList<PopField> viewFields) {
        this.viewFields = viewFields;
    }

    public void setAllFields(ArrayList<PopField> allFields) {
        this.allFields = allFields;
    }

    public void setReturnFields(ArrayList<PopField> returnFields) {
        this.returnFields = returnFields;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public void setTableParams(ArrayList<String> tableParams) {
        this.tableParams = tableParams;
    }

    public void setSysParams(ArrayList sysParams) {
        this.sysParams = sysParams;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public void setSaveParentFlag(boolean saveParentFlag) {
        this.saveParentFlag = saveParentFlag;
    }

    public void setSessParams(ArrayList sessParams) {
        this.sessParams = sessParams;
    }

    public void setCodeParams(ArrayList codeParams) {
        this.codeParams = codeParams;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getForwardModel() {
		return forwardModel;
	}
	public void setForwardModel(String forwardModel) {
		this.forwardModel = forwardModel;
	}
	public String getChangeCond() {
		return changeCond;
	}
	public void setChangeCond(String changeCond) {
		this.changeCond = changeCond;
	}
	public boolean isKeySearch() {
		return keySearch;
	}
	public void setKeySearch(boolean keySearch) {
		this.keySearch = keySearch;
	}
	public void setPopEnter(boolean popEnter) {
		this.popEnter = popEnter;
	}
	public boolean getPopEnter() {
		return popEnter;
	}
	public String getStockTable() {
		return stockTable;
	}
	public void setStockTable(String stockTable) {
		this.stockTable = stockTable;
	}
	public String getDefineSQL() {
		return defineSQL;
	}
	public void setDefineSQL(String defineSQL) {
		this.defineSQL = defineSQL;
	}
	public String getDefParentCode() {
		return defParentCode;
	}
	public void setDefParentCode(String defParentCode) {
		this.defParentCode = defParentCode;
	}
	public ArrayList getHiddenField() {
		return hiddenField;
	}
	public void setHiddenField(ArrayList hiddenField) {
		this.hiddenField = hiddenField;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getTopPopup() {
		return topPopup;
	}


	public void setTopPopup(String topPopup) {
		this.topPopup = topPopup;
	}
}
