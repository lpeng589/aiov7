package com.menyi.aio.web.importData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.koron.crm.bean.WorkProfessionBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.OnlineUserInfo;

import jxl.write.*;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.ImportDataBean;

import java.io.FileOutputStream;
import java.math.BigDecimal;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.favourstyle.UserStyleServlet;
import com.menyi.aio.web.finance.popupSelect.PopupMgt;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;

/**
 * @author Hay Vanpull
 *
 */

public class JXLTOOL {
    
	private Workbook workbook = null; // ����������
    public List<ExcelFieldInfoBean> headers = null; // ͷ��
    private Sheet sheet = null; // ������
    public int totalRows = 0; // ������
    public int totalCells = 0; // ������
    private List<String[]> topTitle = new ArrayList<String[]>(); //��һ��ͷ��Ϣ
    private String sheetName = "sheet 1";
    private boolean hasTitle=false; //�Ƿ��б���ͷ
    
    private String mainTableDisplay;
    
    ImportDataMgt importMgt = new ImportDataMgt();
    
    /**
     * ��һ��InputStreamΪ�����Ĺ�����
     *
     * @param inputStream
     * @throws IOException
     * @throws BiffException
     */
    public JXLTOOL(InputStream inputStream) throws BiffException, IOException {
        this.workbook = Workbook.getWorkbook(inputStream);
        this.sheet = this.workbook.getSheet(0);
        this.getRows();
        this.getCells();
    }

    /**
     * ��һ��Struts FormFileΪ�����Ĺ�����
     *
     * @param file
     * @throws IOException
     * @throws FileNotFoundException
     * @throws BiffException
     */
    public JXLTOOL(FormFile file) throws FileNotFoundException, IOException,
            BiffException {
        this(file.getInputStream());
    }

    /**
     * ��һ��FileΪ�����Ĺ�����
     *
     * @param file
     * @throws IOException
     * @throws BiffException
     */
    public JXLTOOL(File file) throws BiffException, IOException {
        this(new FileInputStream(file));
    }

    /**
     * ��һ���ļ�·��path�Ĺ�����
     *
     * @param filePath
     * @throws IOException
     * @throws BiffException
     */
    public JXLTOOL(String filePath) throws BiffException, IOException {
        this(new File(filePath));
    }




    /**
     * �ж�excel��ĳ���Ƿ�Ϊ����
     * @return
     */
    private boolean isRowNull(int row) {
        for (int j = 0; j < this.totalCells; j++) {
            String val = sheet.getCell(j, row).getContents();
            val = StringUtils.trimToEmpty(val);
            if (!StringUtils.isBlank(val))
                return false;
        }
        return true;
    }

    /**
     * ���������ݷ�װ��data��
     *
     * @return
     */
    public Result getExcelData(boolean blankTitle) {
    	return getExcelData("",blankTitle);
    }
    
    /**
     * ���������ݷ�װ��data��
     *
     * @return
     */
    public Result getExcelData(String mainTableDisplay,boolean blankTitle) {
    	this.mainTableDisplay=mainTableDisplay;
        Result rs = new Result();
        rs = this.getExcelStructInfo("excelTable",blankTitle);
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {

            List<HashMap<String,ExcelFieldInfoBean>> data =  new ArrayList<HashMap<String,ExcelFieldInfoBean>>();
            try {
            	//����б�ͷ��ȡ����ʱ�ӵ�2�п�ʼ������ӵ�һ�п�ʼ
            	int pos = 2;
            	if(!hasTitle) pos = 1;
                for (int i = pos; i < this.totalRows; i++) {
                    if (!isRowNull(i)) { //�����Ϊ����
                        HashMap<String,ExcelFieldInfoBean>
                                rowFields = new HashMap<String,ExcelFieldInfoBean>();
                        for (int j = 0; j < this.totalCells; j++) {
                            ExcelFieldInfoBean excelBean = this.getData(j, i);
                            if (excelBean != null) {
                                excelBean.setName(headers.get(j).getName());
                                rowFields.put(excelBean.getName(),excelBean);
                            }
                        }
                        data.add(rowFields);
                    }
                }
                rs.setRetVal(data);
            } catch (Exception e) {
                e.printStackTrace();
                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            }
        } else {
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        }
        return rs;
    }

    /**
     * �õ�excelÿ��ͷ��
     *
     * @param excel
     * @return
     */
    public Result getExcelStructInfo(String excel,boolean blankTitle) {
        Result rs = new Result();
        try {
            //��һ��
            sheetName = this.sheet.getName();

            headers = new ArrayList<ExcelFieldInfoBean>();

            String title = null;
            int num = 0;
            //����Ƿ��б���ͷ�����ķ�ʽ�ǿ���һ����û�пյ�Ԫ��
            hasTitle=false;
            for (int j = 0; j < this.totalCells; j++) {
                Cell cl = this.sheet.getCell(j, 0);
                if(cl.getType().toString().equals(cl.getType().EMPTY.toString())){
                	hasTitle = true;
                	break;
                }
            }
            
            for (int j = 0; j < this.totalCells; j++) {
                Cell cl = this.sheet.getCell(j, 0);
                if(hasTitle){
	                if(cl.getType().toString().equals(cl.getType().LABEL.toString())){
	                    if(title != null){
	                        topTitle.add(new String[]{title,num+""});
	                    }
	                    title = cl.getContents();
	                    num = 0;
	                }else if(cl.getType().toString().equals(cl.getType().EMPTY.toString())){
	                    num++;
	                }
                }

                ExcelFieldInfoBean fieldInfo = new ExcelFieldInfoBean();
                ExcelFieldInfoBean temp = this.getData(j, hasTitle?1:0);
                if (temp != null && !temp.getValue().toString().equals("")) {
                    fieldInfo.setName((blankTitle?"":(!hasTitle?mainTableDisplay : title)+":")+temp.getValue()
                                         .toString()); 
                } else {
                    fieldInfo.setName("");
                }
                headers.add(fieldInfo);
            }
            if(title!=null && title.length() != 0){
            	topTitle.add(new String[]{title,num+""});
            }

            rs.setRetVal(this.headers);
        } catch (Exception e) {
            e.printStackTrace();
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        }
        return rs;
    }

    /**
     * �õ�������
     */
    private void getRows() {
        this.totalRows = sheet.getRows();
    }

    /**
     * �õ�������
     */
    private void getCells() {
        this.totalCells = this.sheet.getColumns();
    }

    /**
     * �õ�����
     *
     * @param cell
     * @param row
     * @return
     */
    private ExcelFieldInfoBean getData(int cell, int row) {
        Cell rs = this.sheet.getCell(cell, row);
        ExcelFieldInfoBean excelFieldBean = new ExcelFieldInfoBean();
        if (rs.getType() == CellType.DATE) {
            DateCell dc = (DateCell) rs;
            excelFieldBean.setValue(BaseDateFormat.format(dc.getDate(),
                    BaseDateFormat.yyyyMMddHHmmss));
        }else if(rs.getType() == CellType.NUMBER){
        	if(rs.getContents()!=null && rs.getContents().contains(".")){
        		NumberCell number = (NumberCell) rs ;
        		excelFieldBean.setValue(String.valueOf(new  java.math.BigDecimal(String.valueOf(number.getValue())).setScale(8,java.math.BigDecimal.ROUND_HALF_UP).doubleValue()));
        	}else{
        		excelFieldBean.setValue(rs.getContents()) ;
        	}
    	} else {
    		String content = rs.getContents() ;
    		if(content!=null){
    			content = content.replaceAll("\\\\r\\\\n", "") ;
    			content = content.replaceAll("\\r", "") ;
    			content = content.replaceAll("\\n", "") ;
    			content = content.trim();
    		}
        	excelFieldBean.setValue(content);
        }
        return excelFieldBean;
    }

    /**
     * �ر�workbook���ͷ���Դ��
     *
     */
    public void close() {
        this.workbook.close();
    }

    public void writeExcel(String fileName, List<HashMap<String,ExcelFieldInfoBean>>
            errorList, String errormsg) {
        try {
            WritableCellFormat wf = new WritableCellFormat();
            wf.setBorder(Border.ALL, BorderLineStyle.THIN);
            wf.setAlignment(Alignment.CENTRE);
            WritableCellFormat wfc = new WritableCellFormat();
            wfc.setBorder(Border.ALL, BorderLineStyle.THIN);
            wfc.setAlignment(Alignment.LEFT);

            WritableWorkbook wbook = Workbook.createWorkbook(new File(fileName));
            WritableSheet ws = wbook.createSheet(sheetName, 0);
            //д��һ��
            if(hasTitle){
	            int c = 0;
	            for (String[] ss : topTitle) {
	
	                Label cell = new Label(c,0,ss[0],wf);
	                int len = Integer.parseInt(ss[1]);
	                if(c==0){
	                	len++; //��һ��������ʾ������Ϣ
	                }
	
	                ws.addCell(cell);
	                ws.mergeCells(c,0,c+len,0);
	                c += len+1;
	            }
            }
            
            for (int row = 0; row < errorList.size(); row++) {
                HashMap<String,ExcelFieldInfoBean> rowList = errorList.get(row);
                if(row ==0){
                	 Label cell = new Label(0, hasTitle? 1:0,errormsg,wf);
                	 ws.addCell(cell) ;
                }
                ws.setColumnView(0, 20) ;
                Label cell2 = new Label(0, row + (hasTitle? 2:1),
                        rowList.get("Error") == null ? "" :
                        rowList.get("Error").getValue(),wfc);
           	 	ws.addCell(cell2);
                for (int col = 0; col < headers.size(); col++) {
                    ExcelFieldInfoBean bean = rowList.get(headers.get(col).getName());
                    if (row == 0) {
                    	String nl = bean.getName();
                    	if(nl==null){
                    		nl = "";
                    	}else{
                    		nl = nl.substring(nl.indexOf(":")+1);
                    	}
                        Label cell = new Label(col+1, hasTitle? 1:0,nl,wf);
                        ws.addCell(cell);
                    }

                    Label cell = new Label(col+1, row + (hasTitle? 2:1),
                                           bean.getValue() == null ? "" :
                                           bean.getValue().toString(),wfc);
                    ws.addCell(cell);
                }

            }
            wbook.write();
            wbook.close();
        } catch (Exception ex) {
            BaseEnv.log.error("TXLTOOL.writeExcel ", ex);
        }
    }


    private static String getFieldDisplay(Hashtable allTables, String table,
                                          String field, String locale) {
        DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
        if (tableInfo == null) {
            return table + " not Exist";
        }
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.
                                        getFieldInfos().get(i);
            if (fieldInfo.getFieldName().equals(field)) {
                try {
                    return fieldInfo.getDisplay().get(locale).toString();
                } catch (Exception ex) {
                    return fieldInfo.getFieldName();
                }
            }
        }
        return field;
    }
    
	private static void popupSelect(HashMap values,HttpServletRequest request,String tableName,String fieldName){
		
		
		Hashtable<String,DBTableInfoBean> allTables = (Hashtable<String,DBTableInfoBean>) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);

		Hashtable<?,EnumerateBean> enumeratemap = BaseEnv.enumerationMap;

		//��������Ӧ��Bean
		PopupSelectBean popSelectBean = DDLOperation.getFieldInfo(allTables,tableName,fieldName).getSelectBean();

		LoginBean login = (LoginBean) request.getSession().getAttribute("LoginBean");
		
		//����˳��Ϊ 0 ����.�ֶ��� 1 ֵ 2 �Ա�(= like >= <=)
		ArrayList<String[]> param = new ArrayList<String[]>();
				
		for (PopField popField : popSelectBean.getDisplayField2()) {
			if(popField.getSearchType()==PopField.SEARCH_MATCHR ||
					popField.getSearchType()==PopField.SEARCH_MATCHL ||
					popField.getSearchType()==PopField.SEARCH_MATCH ||
					popField.getSearchType()==PopField.SEARCH_EQUAL  ){
				String value = (String)values.get(popField.getAsName());
				if(value != null && value.length() > 0){
					param.add(new String[] { popField.getFieldName(), value , "=", "and" });				
				}
			}
		}

		ArrayList<String> tabParam = popSelectBean.getTableParams();
		//��ȡ���������������ϵĸ���������ֵ
		HashMap<String, String> mainParam = new HashMap<String, String>();
		for (int i = 0; i < tabParam.size(); i++) {
			String mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
			}
			String value = (String)values.get(mainField);
			mainParam.put(mainField,value);			
		}
		
		DynDBManager dyn = new DynDBManager();
		popSelectBean.setPopEnter(true);		
		//��ѯ���ݿ⣬������ǰ100������
		Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, new ArrayList(), 1, 100, "", "", login.getId(),
				login.getSunCmpClassCode(), GlobalsTool.getLocale(request), "",request, "",popSelectBean.isSaveParentFlag()?0:PopupSelectBean.LEAFRULE,"");

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//�洢�����ֶ���getAllFields�е�λ�ã������ѻ�õ����ݰ��������ɻ��������
			ArrayList<Integer> retFieldPos = new ArrayList<Integer>();
			for (PopField fv: popSelectBean.getReturnFields()) {
				for(int i = 0 ;i < popSelectBean.getAllFields().size();i++)
				{
					PopField fv2 = popSelectBean.getAllFields().get(i);
					if(fv.getFieldName().equals(fv2.getFieldName()))
					{
						retFieldPos.add(i);
						break;
					}
				}
			}
			
			
			for (Object[] os : (List<Object[]>)rs.retVal) {				
				// ���ҳ��践�ص�ֵ
				for(Integer k :retFieldPos){
					String osstr = os[k.intValue()] + "";
					PopField fv = popSelectBean.getAllFields().get(k.intValue());
					String display = fv.fieldName;
					DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					if (tempfib == null) {
						display = fv.parentName;
						if (tableName != null) {
							display = display.replace("@TABLENAME", tableName);
						}
						tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					}
					if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						if (osstr == null || osstr.equals("null") || osstr.length() == 0) {
							osstr = "0";
						}
						osstr = GlobalsTool.formatNumberS(new Double(osstr), false, false, "", display);
					}
					osstr = GlobalsTool.revertTextCode2(osstr);
					values.put(display, osstr);
				}
			}
		}
	}

    /**
     * �滻ö�ٺ͵�����ֵ
     * @param data
     * @param mainMap
     * @param locale
     * @return
     */
    public Result replaceData(ImportDataBean importDataBean,
    		HashMap<String,ExcelFieldInfoBean> excelMap,HashMap values,
            String locale,Hashtable<String,DBTableInfoBean> allTables,ArrayList sqls,HashMap impFields,String msgModuleNot,
            String msgDownload,MessageResources resources,
            LoginBean loginBean,String path,Hashtable props,MOperation mop,HashMap cashMap,int row,boolean isAutoGoods) {
        
    	//impFields.put(ifb.getTableName()+"_"+ifb.getFieldName(), excelMapObj.get(excelStr));  ���ڱ�����Щ�г�����excel�У���ʽ����ʱ��������Щ��
    	Result rs = new Result();
        HashMap childValueMap = new HashMap(); //���ڴ���ӱ����ݡ�
        
        //���÷�֧��������
        values.put("SCompanyID", loginBean.getSunCmpClassCode());

        /**
         * �ɣ��ȴ����������ݣ��ٴ���ǵ��������ݣ��������⣬�����������û���������
         * ���Ǿɷ������ᵼ�²��ֵ�����ȡ������Ӧ׼���õķǵ������ݡ�
         * ��Ϊ��˳��ִ�У�������������ļ���˳����ȷ������Ӱ�쵼��Ľ����
         */
        ArrayList<DBFieldInfoBean> fieldInfoList = new ArrayList<DBFieldInfoBean>();
        DBTableInfoBean mainTable = allTables.get(importDataBean.getTargetTable());
        ArrayList<DBTableInfoBean> chileTables = DDLOperation.getChildTables(importDataBean.getTargetTable(), allTables);
        
        fieldInfoList.addAll(mainTable.getFieldInfos());
        for(DBTableInfoBean fb:chileTables){
        	fieldInfoList.addAll(fb.getFieldInfos());
        }
        
        for (DBFieldInfoBean fieldInfoBean : fieldInfoList) {
        	if(fieldInfoBean.getFieldName().equals("createBy")){
        		//���������ֶ�
        		ExcelFieldInfoBean eb = excelMap.get(fieldInfoBean.getTableBean().getDisplay().get(locale)+":"+fieldInfoBean.getDisplay().get(locale));
        		if(eb != null && eb.getValue() != null && eb.getValue().length()> 0){
        			OnlineUser ou = OnlineUserInfo.getUserByEmpName(eb.getValue());
        			if(ou != null){
        				values.put("createBy", ou.id);
        			}else{
        				rs.setRetCode(ImportDataMgt.MAIN_TABLE_ERROR) ;
	            		rs.setRetVal(fieldInfoBean.getDisplay().get(locale)) ;
	            		return rs ;
        			}
        		}
        		continue;
        	}
        	if(fieldInfoBean.getInputType() == 100  || fieldInfoBean.getFieldName().equals("id") || fieldInfoBean.getFieldName().equals("f_brother") ){
        		continue;
        	}
        	
        	if(row==0){
        		//impFields.put(ifb.getTableName()+"_"+ifb.getFieldName(), excelMapObj.get(excelStr));  ���ڱ�����Щ�г�����excel�У���ʽ����ʱ��������Щ��
        		if(excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+fieldInfoBean.getDisplay().get(locale)) !=null){
        			impFields.put(fieldInfoBean.getTableBean().getTableName()+"_"+fieldInfoBean.getFieldName(), "");
        		}	            
        	}
        	
            GoodsPropInfoBean tempProp=GlobalsTool.getPropBean(fieldInfoBean.getFieldName());
            
            if(importDataBean.getTargetTable().equals(fieldInfoBean.getTableBean().getTableName())&& row>0&& !isAutoGoods){
            	//����ֻҪȡ��һ�����ݣ�
            	continue;
            }
            
           if((fieldInfoBean.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE 
            		||(fieldInfoBean.getInputType()== DBFieldInfoBean.INPUT_HIDDEN_INPUT 
            				&& fieldInfoBean.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)
            		|| (fieldInfoBean.getInputType()==DBFieldInfoBean.INPUT_HIDDEN 
            				&& fieldInfoBean.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE))
            			&& !(tempProp!=null && tempProp.getIsSequence()==1)/*��������кŲ�������*/
            			&& fieldInfoBean.getInputValue() != null
            			){
                
                if(tempProp!=null && tempProp.getIsSequence()==1 ){
                	String gg = (String)values.get("GoodsCode");
                	if(!importDataBean.getTargetTable().equals(fieldInfoBean.getTableBean().getTableName())){
                        //�ӱ�
                        HashMap hm = (HashMap)childValueMap.get(fieldInfoBean.getTableBean().getTableName());
                        if(hm != null){
                        	gg = (String)hm.get("GoodsCode");
                        }
                    }
                	if(gg != null &&gg.length() > 0){
                		//��������кţ���δ���ã��򲻵���
                		if(new GoodsPropMgt().isGoodsSeqSet(gg)){
                        	//ֵ
                            String displayName =  fieldInfoBean.getDisplay().get(locale); 
                            Object val = ((ExcelFieldInfoBean)excelMap.get(allTables.get(fieldInfoBean.getTableBean().getTableName()).getDisplay().get(locale)+":"+displayName)).getValue();
                            setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(), val,row);
                    	}
                	}
                	continue;
                }
            	
            	PopupSelectBean selectBean = (PopupSelectBean)BaseEnv.popupSelectMap.get(fieldInfoBean.getInputValue());
            	if(selectBean==null){
            		continue;
            	}
        		String sql = selectBean.getPopsql();
        		//���sql����group �������group�ŵ����
        		String groupStr = "";
        		if(sql.lastIndexOf("group")>0){
        			int ind = sql.lastIndexOf("group");
        			if(sql.indexOf(")",ind)==-1){
        				groupStr = sql.substring(ind);
        				sql = sql.substring(0,ind);
        			}
        		}
        		int wherePos = DynDBManager.pos(sql, "where");
        		if(wherePos > 0){
        			sql += (selectBean.getCondition()==null || selectBean.getCondition().trim().length()==0?"":" and "+selectBean.getCondition()); 
        		}else{
        			sql += " where 1=1 "+ (selectBean.getCondition()==null || selectBean.getCondition().trim().length()==0?"":" and "+selectBean.getCondition());
        		}
                
                boolean hasCondition = false;
                String conditionStr="";
                for(PopField pop:(ArrayList<PopField>)selectBean.getDisplayField2()){
                	if((selectBean.isKeySearch() && (pop.getSearchType()!=0 && pop.keySearch)) || 
                			( !selectBean.isKeySearch() && pop.getSearchType()!=0 )){
                		String displayField = pop.getDisplay();
                		if(displayField==null || displayField.length()==0){
                			displayField = pop.getFieldName();
                		}
                		//�����ѯ������ƴ���룬������ѯ��������������ͻ����������
                		if(displayField==null || displayField.length()==0 || displayField.endsWith("PYM")){
                			continue;
                		}
                		//����ֶ���saveField���Ƿ����,���Ƿ����أ�������ڲ���δ��������saveField�е�display
                		for(PopField spop:(ArrayList<PopField>)selectBean.getSaveFields()){
                			if(pop.getFieldName().equals(spop.getFieldName())&&spop.isHidden()==false){
                				displayField = spop.getParentName();
                			}
                		}
                		displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                		//zxy �ĸ��������������ع��򡣡�����
                		if(displayField.indexOf("tblCompany2")>-1){
                			displayField = displayField.replaceAll("tblCompany2", "tblCompany");
                		}
                		if(displayField.indexOf("tblCompany1")>-1){
                			displayField = displayField.replaceAll("tblCompany1", "tblCompany");
                		}
                		//ȡ���� �����бȽ�
                		String display = displayField;
                		if(display.indexOf(".")> 0){
                			display =GlobalsTool.getFieldDisplay(allTables, displayField, locale);
                		}
                		//����ֶ���������ֶε����ֶ�Ӧ����˵�����ֶ�Ϊ���ֶΣ��������ֶ�Ϊ�գ��򲻽��в�ѯ
                		//�磺�����˻����ж������Ϊ�գ����˵���������������Ʒ���ƣ����ִ�д˵��������ܸ�����Ʒ���ƣ����ҵ�һ�����൥�Ķ�������ʵ�������������Ϊ��
                		//���ԣ�������һ��Ҫ��һ�����������ֶΣ��������ֶ�Ϊ�գ����ܲ�ѯ���ݿ⣬���������������ݡ�
                		if(display.equals(fieldInfoBean.getDisplay().get(locale))){
                			if(excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display) ==null||
                					excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display).getValue().length()==0){
	                			BaseEnv.log.debug("���ֶ�"+display+"Ϊ�գ���ִ�е�����������ѯ����ȷ��Ҫִ�У����޸��ֶ�����"+fieldInfoBean.getFieldName()+":"+displayField);
                				
                				hasCondition = false;
	                			break;
                			}
                		}
                		if(excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display) !=null){
                			
                			String disVal = excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display).getValue();
                			disVal = disVal.replaceAll("'", "''");
                			if(disVal != null && disVal.length()>0){
                				hasCondition = true; //�������������λ�ã���Ϊand��һ������û�У���ִ�������ѯ���϶���������Ҫ������
	                			DBFieldInfoBean fb =GlobalsTool.getFieldBean(pop.getFieldName());
	                			if(fb==null || fb.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE || fb.getFieldType()==DBFieldInfoBean.FIELD_INT){
	                				//�����fbΪ�գ���˵�����Ǹ����ϲ�ѯ������ֶ���������Ҫ����ѯ����
	                				//������������ֶΣ��򲻲��������жϣ���Ϊһ���ܶ൯��������������Ĳ�ѯ������������ʱһ���Ӧ�����ֶ����ڣ����²�ѯ�����鲻������
	                				//�������ʽ���������ֶ���price��һ�㶼ֱ�ӵ�price��ֵ��û��Ҫȥ��һ�����ݿ�.
	                				continue;
	                			}
	                			conditionStr += display+":"+disVal+";";
	                			if(!sql.trim().endsWith("and")){
	                				sql +=" and ";
	                			}
	                			if(fb != null && (fb.getInputType()== DBFieldInfoBean.INPUT_LANGUAGE ||
	                					fb.getInputTypeOld()== DBFieldInfoBean.INPUT_LANGUAGE)){
	                				sql +=pop.getFieldName()+" in ( select id from tblLanguage where  "+locale+" ='"+disVal+"' )";
	                			}else if(fb != null && (
	                					fb.getInputType()== DBFieldInfoBean.INPUT_ENUMERATE  || fb.getInputType()== DBFieldInfoBean.INPUT_RADIO||
	                					fb.getInputTypeOld()== DBFieldInfoBean.INPUT_ENUMERATE  || fb.getInputTypeOld()== DBFieldInfoBean.INPUT_RADIO)){
	                				String eVal = GlobalsTool.getEnumerationValue(fb.getRefEnumerationName(), locale, disVal);
	                				sql +=pop.getFieldName()+"='"+eVal+"'";
	                			}else if(fb != null && (
	                					fb.getInputType()== DBFieldInfoBean.INPUT_CHECKBOX  || fb.getInputType()== DBFieldInfoBean.INPUT_CHECKBOX)){
	                				String eVal = "";
	                				for(String s:disVal.split(",|;")){
	                					eVal +="'"+GlobalsTool.getEnumerationValue(fb.getRefEnumerationName(), locale, s)+"',";
	                				}
	                				if(eVal.length() > 0){
	                					eVal = eVal.substring(0,eVal.length()-1);
	                					sql +=pop.getFieldName()+" in ("+eVal+")";
	                				}
	                			}else{
	                				sql +=pop.getFieldName()+"='"+disVal+"'";
	                			}
                			}
                		}
                	}
                }
                if(!hasCondition){
                	//һ��������û�У��򲻽������ݿ��ѯ,ֱ��ȡ��Ӧ��ֵ
                	for(int j=0;j<selectBean.getAllFields().size();j++){
                		PopField pop=selectBean.getAllFields().get(j);
                		if(pop.parentName != null && pop.parentName.length() > 0){
                			String pN = pop.parentName;
                			if(pN.indexOf(".")>0){
                				pN = pN.substring(pN.indexOf(".")+1);
                			}
                			if(fieldInfoBean.getFieldName().equals(pN) && pop.hidden==false){
                				//��������ֶκ��ֶα���һ�£���δ���أ���ֵʱ������ֵ
                				String displayField = pop.getDisplay();
                        		if(displayField==null || displayField.length()==0){
                        			displayField = pop.getFieldName();
                        		}
                        		//ȥdisplayField��ȥ�ң����ҵ�������е�׼��ȡ��ʾ����
                        		for(PopField dpop:selectBean.getDisplayField()){
                        			if(dpop.getFieldName().equals(pop.getFieldName())){
                        				displayField = dpop.getDisplay();
                                		if(displayField==null || displayField.length()==0){
                                			displayField = dpop.getFieldName();
                                		}
                        			}
                        		}
                        		displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                        		String display =GlobalsTool.getFieldDisplay(allTables, displayField, locale);
                        		if(excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display)!=null){
                        			String val = excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display).getValue();    	                        
                        			setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(),val,row);
                        		}else{
                        			setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(),"",row);
                        		}
                			}else{
                    			setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(),"",row);
                    		}
                		}else if(pop.parentDisplay){
                			String pN = pop.fieldName;
                			if(pop.asName!=null && pop.asName.length() > 0){
                				pN = pop.asName;
                			}
                			pN = pN.replaceAll("\\.", "_");
                			if((fieldInfoBean.getTableBean().getTableName()+"_"+fieldInfoBean.getFieldName()).equals(pN) && pop.hidden==false){
                				
                				String displayField = pop.getDisplay();
                        		if(displayField==null || displayField.length()==0){
                        			displayField = pop.getFieldName();
                        		}
                        		displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                        		String display =GlobalsTool.getFieldDisplay(allTables, displayField, locale);
                        		if(excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display)!=null){
                        			String val = excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+display).getValue();    	                        
                        			setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(),val,row);
                        		}else{
                        			setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(),"",row);
                        		}
                			}else{
                    			setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(),"",row);
                    		}
                		}
                	}
                	continue;
                }
                
                //ִ��@condsent_��ǩ����
                int rootCount=0;
                while(sql.indexOf("@condsent_")>-1){
                	rootCount++;
                	if(rootCount>200){
                		throw new RuntimeException("JXLTOOL ����@condsentʱ������ѭ��");
                	}
                	//@condsent_tblBuyInStockDet_BuyOrderID:
                	int pos = sql.indexOf("@condsent_");
                	String condFN = sql.substring(pos+"@condsent_".length(),sql.indexOf(":",pos));
                	String condFN1 = condFN;
                	String condFN2 = "";
                	if(condFN.indexOf("_")> 0){
                		condFN2= condFN.substring(0,condFN.indexOf("_"));
                		condFN1= condFN.substring(condFN.indexOf("_")+1);
                	}
                	boolean exists = false;
                	if(condFN1.equals(importDataBean.getTargetTable())){
                		//����
                		if(values.get(condFN2)==null || values.get(condFN2)==null || (values.get(condFN2)).toString().length()==0){
                			sql = sql.substring(0,pos)+sql.substring(sql.indexOf("]",pos)+1);
                		}else{
                			sql = sql.substring(0,pos)+ sql.substring(sql.indexOf("[",pos)+1,sql.indexOf("]",pos)) + sql.substring(sql.indexOf("]",pos)+1);
                		}
                	}else{
                		//�ӱ�
                		HashMap hm = (HashMap)childValueMap.get(fieldInfoBean.getTableBean().getTableName());
                		if(hm ==null || hm.get(condFN2)==null || hm.get(condFN2)==null || (hm.get(condFN2)).toString().length()==0){
                			String pre = sql.substring(0,pos).trim();
                			sql = (pre.endsWith("and")?pre.substring(0,pre.length() -3):pre)+" "+sql.substring(sql.indexOf("]",pos)+1);
                		}else{
                			sql = sql.substring(0,pos)+ sql.substring(sql.indexOf("[",pos)+1,sql.indexOf("]",pos)) + sql.substring(sql.indexOf("]",pos)+1);
                		}
                	}
                }

                rootCount=0;
                sql =sql.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                while(sql.indexOf("@Sess:")>0){
                	rootCount++;
                	if(rootCount>200){
                		throw new RuntimeException("JXLTOOL ����@Sessʱ������ѭ��");
                	}
                	Pattern pattern = Pattern.compile("@Sess:([\\w]+)");
            		Matcher matcher = pattern.matcher(sql);
            		String sName = "";
            		if(matcher.find()){
            			sName =matcher.group(1);
            		}else{
            			throw new RuntimeException("�������@Sessʧ��JXLTOOL");
            		}
                	Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId());
                	String sValue =sessionSet != null&&sessionSet.get(sName)!=null?sessionSet.get(sName).toString():"";
                	sql=sql.replaceFirst("@Sess:"+sName, sValue.replaceAll("'", "''"));
                }
                //����@MEM:
                rootCount=0;
                while(sql.indexOf("@MEM:")>0){
                	rootCount++;
                	if(rootCount>200){
                		throw new RuntimeException("JXLTOOL ����@MEM:ʱ������ѭ��");
                	}
                	Pattern pattern = Pattern.compile("@MEM:([\\w]+)");
            		Matcher matcher = pattern.matcher(sql);
            		String sName = "";
            		if(matcher.find()){
            			sName =matcher.group(1);
            		}else{
            			throw new RuntimeException("�������@Sessʧ��JXLTOOL");
            		}
            		String sValue =BaseEnv.systemSet.get(sName)==null?"":BaseEnv.systemSet.get(sName).getSetting();
                	sql=sql.replaceFirst("@MEM:"+sName, sValue.replaceAll("'", "''"));
                }
                rootCount=0;
                while(sql.indexOf("@ValueofDB:")>0){
                	rootCount++;
                	if(rootCount>200){
                		throw new RuntimeException("JXLTOOL ����@ValueofDBʱ������ѭ��");
                	}
                	Pattern pattern = Pattern.compile("@ValueofDB:([\\w]+)");
            		Matcher matcher = pattern.matcher(sql);
            		String sName = "";
            		if(matcher.find()){
            			sName =matcher.group(1);
            		}else{
            			throw new RuntimeException("�������@ValueofDBʧ��JXLTOOL");
            		}
                	String sValue ="";
                	String st=importDataBean.getTargetTable();
                	String sf=sName;
                	//�ȴ�������ȡֵ��ȡ�������ٴ���ϸ����ȡ����Ϊ����ȡֵ�ֶ�Ҳ�п�����tblDepartment_DeptFullName
                	//����
            		sValue = (String)values.get(sName);   
            		if(sValue == null){
            			//���sName���� @ValueofDB:tblMainUpD_DepartmentCode ��ʽ����ǰ���������������ȥ�����������ٶ�һ��
            			if(sName.startsWith(mainTable.getTableName()+"_")){
            				String sfn = sName.substring(sName.indexOf("_")+1);
            				sValue = (String)values.get(sfn);  
            			}
            		}
            		if(sValue==null){
            			//�ӱ�
            			if(sName.indexOf("_")>0){
	            			st = sName.substring(0,sName.indexOf("_"));
	                		sf = sName.substring(sName.indexOf("_")+1);
            			}
                		HashMap hm = (HashMap)childValueMap.get(fieldInfoBean.getTableBean().getTableName());
                        if(hm == null){
                        	sValue="";
                        }else{
                        	sValue = (String)hm.get(sName);
                        	if(sValue ==null){
                    			sValue = hm.get(sf)==null?"":hm.get(sf).toString();
                    		}
                        }
            		}
            		
                	//���sValue��Ϊ�գ�����������У����Ϊ�գ���ȥ���������
                	if(sValue==null||sValue.length() ==0){
                		//���Ϊ�գ���Ӧ�ֶ�Ϊ������豨��
                		DBFieldInfoBean sfb = null;
                		if(sName.indexOf("_")>0){
                			sfb = DDLOperation.getFieldInfo(allTables, sName.substring(0,sName.indexOf("_")), sName.substring(sName.indexOf("_")+1));
                		}else{
                			sfb = DDLOperation.getFieldInfo(allTables, mainTable.getTableName(), sName);                			
                		}
                		if(sfb != null && sfb.getIsNull() == DBFieldInfoBean.NOT_NULL){
                			BaseEnv.log.error("JXLTOOL.replaceData Error �������������Ҫ�ֶ�"+sfb.getDisplay().get(locale)+",�����ֶ�ֵΪ�� :�ֶ�="+fieldInfoBean.getFieldName()+";������="+fieldInfoBean.getInputValue()+";sql="+sql);
                			throw new RuntimeException(""+sfb.getDisplay().get(locale)+"����Ϊ��");
                		}
                		
                		//�����ֵ����һ��sql����У�������sql���Ҫȥ��
                		sql=sql.replaceAll("[\\w|\\.]+[\\s]+((in)|(IN)|(=))[\\s]+\\([^\\(\\)]*((select)|(SELECT))[^\\(\\)]*@ValueofDB:"+sName+"[^\\(\\)]*\\)", " ");
                		//�ҵ���ֵǰ��Ϊ1=1
                		sql=sql.replaceAll("((or)|(OR)|(and)|(AND))[\\s]+[\\w|\\.]+[\\s]*(=|!=|like|LIKE|>|>=|<|<=)[']{0,1}@ValueofDB:"+sName+"['\\s]{1}", " ");
                		sql=sql.replaceAll("[\\w|\\.]+[\\s]*(=|!=|like|LIKE|>|>=|<|<=)[']{0,1}@ValueofDB:"+sName+"[']{0,1}[\\s]+((or)|(OR)|(and)|(AND))", " ");
                		sql=sql.replaceAll("[\\w|\\.]+[\\s]*(=|!=|like|LIKE|>|>=|<|<=)[']{0,1}@ValueofDB:"+sName+"['\\s]{1}", " ");
                		
                		sql=sql.replaceAll("((or)|(OR)|(and)|(AND))[\\s]+[']{0,1}@ValueofDB:"+sName+"[']{0,1}[\\s]+[not|NOT]*[\\s]*(in|IN)[\\s]+\\([^\\)]+\\)", " ");
                		sql=sql.replaceAll("[']{0,1}@ValueofDB:"+sName+"[']{0,1}[\\s]+[not|NOT]*[\\s]*(in|IN)[\\s]+\\([^\\)]+\\)[\\s]+((or)|(OR)|(and)|(AND))", " ");
                		sql=sql.replaceAll("[']{0,1}@ValueofDB:"+sName+"[']{0,1}[\\s]+[not|NOT]*[\\s]*(in|IN)[\\s]+\\([^\\)]+\\)", " ");
                		
                		sql=sql.replaceAll("((or)|(OR)|(and)|(AND))[\\s]+[']{0,1}@ValueofDB:"+sName+"['\\s]*(=|!=|like|LIKE|>|>=|<|<=)[\\s]*[\\w|\\.]+", " ");
                		sql=sql.replaceAll("[']{0,1}@ValueofDB:"+sName+"['\\s]*(=|!=|like|LIKE|>|>=|<|<=)[\\s]*[\\w|\\.]+[\\s]+((or)|(OR)|(and)|(AND))", " ");
                		sql=sql.replaceAll("[']{0,1}@ValueofDB:"+sName+"['\\s]*(=|!=|like|LIKE|>|>=|<|<=)[\\s]*[\\w|\\.]+", " ");
                		
                		
                		sql=sql.replaceAll("''@ValueofDB:"+sName+"''", "");
                		sql=sql.replaceAll("@ValueofDB:"+sName+"", "");
                		
                	}else{
                		sql=sql.replaceAll("@ValueofDB:"+sName, sValue.replaceAll("'", "''"));
                	}
                }
                //ȥ������е� and () ��() and �� �������������
                sql=sql.replaceAll("((or)|(OR)|(and)|(AND))[\\s]*[\\(]{1}[\\s]*[\\)]{1}", "");
                //ȥ�� and and ���ʽ
                sql=sql.replaceAll("((and)|(AND))[\\s]*((and)|(AND))", " and ");
                sql=sql.replaceAll("((or)|(OR))[\\s]*((or)|(OR))", " or ");
                sql=sql.replaceAll("[\\(]{1}[\\s]*[\\)]{1}[\\s]*((or)|(OR)|(and)|(AND))", "");
                sql=sql.replaceAll("[\\(]{1}[\\s]*[\\)]{1}", "");
                
                sql +=" "+groupStr;

                //cashMap�����ڴ洢���Ѿ������ĵ��������ݣ����ⵯ�����ظ�ִ��
                if (cashMap.get(fieldInfoBean.getTableBean().getTableName() + fieldInfoBean.getFieldName() + sql) == null) {
                	cashMap.put("COND:"+fieldInfoBean.getTableBean().getTableName() +  fieldInfoBean.getFieldName() +sql, conditionStr);
                	
                    //ִ��sql                    
                    Result poprs = importMgt.queryPopSql(sql,selectBean,locale);
                    ArrayList popValList = new ArrayList();
                    cashMap.put(fieldInfoBean.getTableBean().getTableName() +  fieldInfoBean.getFieldName() +sql,popValList);
                    if(poprs.retVal !=null){
                    	Object[] os = (Object[])poprs.retVal;
                    	
                    	if(selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0 && !selectBean.isSaveParentFlag() && "1".equals(os[os.length-1].toString())){
	                    	rs.setRetCode(ImportDataMgt.SAVE_PARENT_ERROR);
	    	                rs.setRetVal(fieldInfoBean.getDisplay().get(locale)+"�����¼�����������");
	    	                return rs;
                    	}
                    	
                    	for(int j=0;j<selectBean.getAllFields().size();j++){
                    		PopField pop=selectBean.getAllFields().get(j);
                    		if(pop.parentName != null && pop.parentName.length() > 0){
                    			String pN = pop.parentName;
                    			if(pN.indexOf(".")>0){
                    				pN = pN.substring(pN.indexOf(".")+1);
                    			}
                    			if(fieldInfoBean.getFieldName().equals(pN) && pop.hidden==false){
                    				//��������ֶκ��ֶα���һ�£���δ���أ�����ΪCURVALUE����ֵʱ������ֵ
                    				String displayField = pop.getParentName();
                            		if(displayField==null || displayField.length()==0){
                            			displayField = pop.getFieldName();
                            		}
                            		//------------��ƥ��Ʒ�ڳ���ɫ������������Щ������ȥ��
//                            		//ȥdisplayField��ȥ�ң����ҵ�������е�׼��ȡ��ʾ����
//                            		for(PopField dpop:selectBean.getDisplayField()){
//                            			if(dpop.getFieldName().equals(pop.getFieldName())){
//                            				displayField = dpop.getDisplay();
//                                    		if(displayField==null || displayField.length()==0){
//                                    			displayField = dpop.getFieldName();
//                                    		}
//                            			}
//                            		}
                            		displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                            		String display =GlobalsTool.getFieldDisplay(allTables, displayField, locale);
                    				popValList.add(new String[]{pN,"CURVALUE:"+display});
                    			}else{
                    				String fieldName = pop.parentName; 
                    				fieldName = fieldName.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                    				
                    				String tableName =fieldInfoBean.getTableBean().getTableName();
                    				if(fieldName.indexOf(".") >-1){
                    					tableName = fieldName.substring(0,fieldName.indexOf("."));
                    				}
                    				fieldName = fieldName.substring(fieldName.indexOf(".")+1);
                    				DBFieldInfoBean fb= GlobalsTool.getFieldBean(tableName, fieldName);
                    				if(fb !=null && (fb.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || fb.getFieldType() == DBFieldInfoBean.FIELD_INT)){
	            						int dig =GlobalsTool.getDigitsOrSysSetting(tableName, fieldName);
	            						if(GlobalsTool.getFieldBean(tableName, fieldName).getFieldType()==DBFieldInfoBean.FIELD_INT){
	            							dig = 0;				
	            						}
	            						if(os[j].equals("")){
	            							popValList.add(new String[]{pN,"0"});
	            						} else if(dig==0){	            							
	            							popValList.add(new String[]{pN,os[j]==null?"":Math.round(new BigDecimal(os[j].toString()).doubleValue())+""});
	            						}else{
	            							popValList.add(new String[]{pN,os[j]==null?"":GlobalsTool.round(new BigDecimal(os[j].toString()).doubleValue(), dig)+""});
	            						}
                    				}else{            						
                    					popValList.add(new String[]{pN,os[j]==null?"":os[j].toString()});
                    				}
                    			}
                    		}else if(pop.parentDisplay){
                    			String pN = pop.fieldName;
                    			if(pop.asName!=null && pop.asName.length() > 0){
                    				pN = pop.asName;
                    			}
                    			pN = pN.replaceAll("\\.", "_");
                    			if((fieldInfoBean.getTableBean().getTableName()+"_"+fieldInfoBean.getFieldName()).equals(pN) && pop.hidden==false){
                    				//��������ֶκ��ֶα���һ�£���δ���أ�����ΪCURVALUE����ֵʱ������ֵ
                    				String displayField = pop.getDisplay();
                            		if(displayField==null || displayField.length()==0){
                            			displayField = pop.getFieldName();
                            		}
                            		displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                            		String display =GlobalsTool.getFieldDisplay(allTables, displayField, locale);
                    				popValList.add(new String[]{pN,"CURVALUE:"+display});
                    			}else{
                    				String fieldName = pN;
                    				fieldName = fieldName.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                    				String tableName = fieldName.substring(0,fieldName.indexOf("_"));
                    				fieldName = fieldName.substring(fieldName.indexOf("_")+1);
                    				DBFieldInfoBean fb= GlobalsTool.getFieldBean(tableName, fieldName);
                    				if(fb !=null && (fb.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || fb.getFieldType() == DBFieldInfoBean.FIELD_INT)){
	            						int dig =GlobalsTool.getDigitsOrSysSetting(tableName, fieldName);
	            						if(GlobalsTool.getFieldBean(tableName, fieldName).getFieldType()==DBFieldInfoBean.FIELD_INT){
	            							dig = 0;				
	            						}
	            						if(os[j].equals("")){
	            							popValList.add(new String[]{pN,"0"});
	            						}else if(dig==0){	            							
	            							popValList.add(new String[]{pN,os[j]==null?"":Math.round(new BigDecimal(os[j].toString()).doubleValue())+""});
	            						}else{
	            							popValList.add(new String[]{pN,os[j]==null?"":GlobalsTool.round(new BigDecimal(os[j].toString()).doubleValue(), dig)+""});
	            						}
                    				}else{            						
                    					popValList.add(new String[]{pN,os[j]==null?"":os[j].toString()});
                    				}                    				
                    			}
                    			
                    		}
                    	}
                    }else{
                    	if(fieldInfoBean.getFieldName().equals("GoodsCode")){
                    		//��Ʒ�ֶβ���Ϊ��
	                        rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                        rs.setRetVal("δ���ҵ�ƥ����������Ʒ:"+conditionStr);
	                        return rs;
                    		
                    	}
                    	
                    	BaseEnv.log.debug("JXLTOOL.replaceData ���뵯����δ�ҵ����ݣ�"+sql);
                    	//δ�ҵ�ֵʱҪ����ֵ                    	
                    	for(int j=0;j<selectBean.getAllFields().size();j++){
                    		PopField pop=selectBean.getAllFields().get(j);
                    		if(pop.parentName != null && pop.parentName.length() > 0){
                    			String pN = pop.parentName;
                    			if(pN.indexOf(".")>0){
                    				pN = pN.substring(pN.indexOf(".")+1);
                    			}
                    			if(fieldInfoBean.getFieldName().equals(pN) && pop.hidden==false){
                    				//��������ֶκ��ֶα���һ�£���δ���أ�����ΪCURVALUE����ֵʱ������ֵ
                    				String displayField = pop.getDisplay();
                            		if(displayField==null || displayField.length()==0){
                            			displayField = pop.getFieldName();
                            		}
//                            		------------��ƥ��Ʒ�ڳ���ɫ������������Щ������ȥ��
                            		//ȥdisplayField��ȥ�ң����ҵ�������е�׼��ȡ��ʾ����
//                            		for(PopField dpop:selectBean.getDisplayField()){
//                            			if(dpop.getFieldName().equals(pop.getFieldName())){
//                            				displayField = dpop.getDisplay();
//                                    		if(displayField==null || displayField.length()==0){
//                                    			displayField = dpop.getFieldName();
//                                    		}
//                            			}
//                            		}
                            		String display="";
                            		if(fieldInfoBean.getFieldType()==DBFieldInfoBean.FIELD_DOUBLE ||fieldInfoBean.getFieldType()==DBFieldInfoBean.FIELD_INT){
                            			//�����粼ƥ���ڳ�¼�뵥��Qty1����������ֶΣ���ʾ���ֻ�����1����������������ʱ�������������Ժ�᲻�������
                            			display = fieldInfoBean.getDisplay().get(locale);
                            		}else{
                            			displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                            			display =GlobalsTool.getFieldDisplay(allTables, displayField, locale);
                            		}                            		
                            		
                    				popValList.add(new String[]{pN,"CURVALUE:"+display});
                    			}else{
                    				//-------------zxy ��ƥ��Ʒ�ڳ�������ɫ���Ѹ�ֵ�ɹ��������������������ݣ��򱻸���ֵ�����´�����ʱ���δ˴��룬��������ʱ�ٽ��-----
                    				//popValList.add(new String[]{pN,""});
                    			}
                    		}else if(pop.parentDisplay){
                    			String pN = pop.fieldName;
                    			if(pop.asName!=null && pop.asName.length() > 0){
                    				pN = pop.asName;
                    			}
                    			pN = pN.replaceAll("\\.", "_");
                    			if((fieldInfoBean.getTableBean().getTableName()+"_"+fieldInfoBean.getFieldName()).equals(pN) && pop.hidden==false){
                    				//��������ֶκ��ֶα���һ�£���δ���أ�����ΪCURVALUE����ֵʱ������ֵ
                    				String displayField = pop.getDisplay();
                            		if(displayField==null || displayField.length()==0){
                            			displayField = pop.getFieldName();
                            		}
                            		displayField = displayField.replaceAll("@TABLENAME", fieldInfoBean.getTableBean().getTableName());
                            		String display =GlobalsTool.getFieldDisplay(allTables, displayField, locale);
                    				popValList.add(new String[]{pN,"CURVALUE:"+display});
                    			}else{
                    				//-------------zxy ��ƥ��Ʒ�ڳ�������ɫ���Ѹ�ֵ�ɹ��������������������ݣ��򱻸���ֵ�����´�����ʱ���δ˴��룬��������ʱ�ٽ��-----
                    				//popValList.add(new String[]{pN,""});
                    			}
                    			
                    		}
                    	}
                    	BaseEnv.log.debug("JXLTOOL.ReplaceData PoPup Sql :"+ fieldInfoBean.getTableBean().getTableName() +":"+ fieldInfoBean.getFieldName()+sql);
                    }
                }
                
                //����ֵ
                ArrayList<String[]> popValList = (ArrayList)cashMap.get(fieldInfoBean.getTableBean().getTableName() +  fieldInfoBean.getFieldName() +sql);
                if(popValList != null&&popValList.size()>0){
	                for(String[] v:popValList){
	                	if(v[1].startsWith("CURVALUE:")){
	                		//���������ֶε�ֵ�����ֵ������ֶ���¼��ֵ����Ҫ��¼��ֵΪ׼
	                		if(excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+v[1].substring("CURVALUE:".length()))!=null){
		                		String val = excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+v[1].substring("CURVALUE:".length())).getValue();
		                        setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,v[0],val,row);
	                		}else{
	                			BaseEnv.log.debug("JXLTOOL.ReplaceData PoPup value :"+ fieldInfoBean.getTableBean().getDisplay().get(locale)+":"+v[1].substring("CURVALUE:".length()) +" is NULL");
	                		}
	                	}else{
	                		setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,v[0],v[1],row);	
	                	}
	                }
                }else{
                	String cdstr = (String)cashMap.get("COND:"+fieldInfoBean.getTableBean().getTableName() +  fieldInfoBean.getFieldName() +sql);
                	if(cdstr.length() > 0){
                		//���������ֵ�����ǲ�ѯ���Ϊ�գ�˵��������
                		//��ʱ������
                	}
                }
	        }else{
	        	 //�ǵ������ֶ�	   
	        	//��Ӧ������
	            String displayName =  fieldInfoBean.getDisplay().get(locale); 
	            
	            
	            if(excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+displayName)==null){
	            	continue;
	            }
	            //ֵ
	            Object val = ((ExcelFieldInfoBean)excelMap.get((isAutoGoods?importDataBean.getName():fieldInfoBean.getTableBean().getDisplay().get(locale))+":"+displayName)).getValue();
	            
	            if(tempProp!=null && tempProp.getIsSequence()==1 ){
	            	String gg = (String)values.get("GoodsCode");
	            	if(!importDataBean.getTargetTable().equals(fieldInfoBean.getTableBean().getTableName())){
	                    //�ӱ�
	                    HashMap hm = (HashMap)childValueMap.get(fieldInfoBean.getTableBean().getTableName());
	                    if(hm != null){
	                    	gg = (String)hm.get("GoodsCode");
	                    }
	                }
	            	if(gg != null &&gg.length() > 0){
	            		//��������кţ���δ���ã��򲻵���
	            		if(!new GoodsPropMgt().isGoodsSeqSet(gg)){
	            			val = "";
	            			continue;
	            		}
	            	}
	            }
	            
	            if(fieldInfoBean.getFieldType() == DBFieldInfoBean.FIELD_DATE_SHORT && val.toString().length()>0){
	            	try {
						val = BaseDateFormat.format(BaseDateFormat.parse(val.toString(), BaseDateFormat.yyyyMMdd), BaseDateFormat.yyyyMMdd) ;
					} catch (Exception e) {
						//�����Ƿ���2012-2-5��ʽ
						try {
							SimpleDateFormat sd = new SimpleDateFormat("yyyy-M-d");
							val = BaseDateFormat.format(sd.parse(val.toString()), BaseDateFormat.yyyyMMdd) ;
						} catch (Exception e1) {
							rs.setRetCode(ImportDataMgt.DATE_FORMAT_ERROR);
			                rs.setRetVal(fieldInfoBean.getDisplay().get(locale));
			                return rs;
						}
					}
	            }
            	
	            if((fieldInfoBean.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE ||fieldInfoBean.getInputTypeOld() == DBFieldInfoBean.INPUT_ENUMERATE||
	            		fieldInfoBean.getInputType() == DBFieldInfoBean.INPUT_RADIO ||fieldInfoBean.getInputTypeOld() == DBFieldInfoBean.INPUT_RADIO)){
	            	if(fieldInfoBean.getRefEnumerationName()==null || BaseEnv.enumerationMap.get(fieldInfoBean.getRefEnumerationName()) == null){
	            		rs.setRetCode(ImportDataMgt.ENUM_VALUE_ERROR);
                        rs.setRetVal(displayName+":"+fieldInfoBean.getFieldName()+":ö�ٲ�����"+fieldInfoBean.getRefEnumerationName());
                        return rs;
	            	}
	            	//ö��
	                //���ֵΪ�գ���Ϊ�ӱ��򲻸�ֵ��������������һ��Ĭ��ֵ
	            	if((val!=null && val.toString().length() >0) || fieldInfoBean.getTableBean().getTableName().equals(importDataBean.getTargetTable())){
	                    EnumerateBean enumBean = (EnumerateBean) BaseEnv.enumerationMap
	                    						 .get(fieldInfoBean.getRefEnumerationName());
	                    boolean found = false;
	                    for (int i = 0; i < enumBean.getEnumItem().size(); i++) {
	                        EnumerateItemBean eitemBean = (EnumerateItemBean)enumBean.getEnumItem().get(i);
	                        if((val==null || val.toString().length() ==0) 
	                        		&& (fieldInfoBean.getDefaultValue()!=null && fieldInfoBean.getDefaultValue().length()>0)){
	                        	val = fieldInfoBean.getDefValue();
	                        	found = true;
	                            break;
	                        }
	                        if(val==null || val.toString().trim().length() ==0){
	                        	//Ϊ��ʱ��ֱ�Ӹ�һ��Ĭ��ֵ
	                        	val = eitemBean.getEnumValue();
	                        	found = true;
	                            break;
	                        }else if (eitemBean != null && eitemBean.getDisplay() != null && eitemBean.getDisplay().get(locale) != null && eitemBean.getDisplay().get(locale).equals(val)) {
	                            val = eitemBean.getEnumValue();
	                            found = true;
	                            break;
	                        }
	                    }
	                    if(!found){
	                        //����ö��ֵδ�ҵ�;
	                        rs.setRetCode(ImportDataMgt.ENUM_VALUE_ERROR);
	                        rs.setRetVal(displayName+":"+val);
	                        return rs;
	                    }
	            	}
	            }else if(val !=null && val.toString().length()>0 &&
	            		(fieldInfoBean.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX ||fieldInfoBean.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)){
	                //��ѡ��
	                
                    EnumerateBean enumBean = (EnumerateBean) BaseEnv.enumerationMap
                    						 .get(fieldInfoBean.getRefEnumerationName());
                    String newVal="";
                    for(String valStr:val.toString().split(";|,")){
                    	if(valStr==null || valStr.length()==0) continue;
	                    for (int i = 0; i < enumBean.getEnumItem().size(); i++) {
	                        EnumerateItemBean eitemBean = (EnumerateItemBean)enumBean.getEnumItem().get(i);
	                        if (eitemBean.getDisplay().get(locale).equals(valStr)) {	                            
	                        	newVal += eitemBean.getEnumValue()+",";
	                            break;
	                        }
	                    }	
                    }
                    val = newVal;
	            } else  if(val !=null && val.toString().length()>0 && fieldInfoBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE){
	            	if(val.toString().contains("zh_CN:") || val.toString().contains("en:") || val.toString().contains("zh_TW:")){
	            		val = KRLanguageQuery.create(GlobalsTool.getLocaleDisplay(val.toString(), "zh_CN"), 
	            								GlobalsTool.getLocaleDisplay(val.toString(), "en"), 
	            								GlobalsTool.getLocaleDisplay(val.toString(), "zh_TW")) ;
	            	}else{
	            		if("zh_CN".equals(locale)){
	            			val = KRLanguageQuery.create(val.toString(), "", "") ;
	            		}else if("en".equals(locale)){
	            			val = KRLanguageQuery.create("", val.toString(), "") ;
	            		}else{
	            			val = KRLanguageQuery.create("", "", val.toString());
	            		}
	            	}
	            }
	            
	            if(val !=null && val.toString().length()>0 && fieldInfoBean.getFieldType() == DBFieldInfoBean.FIELD_INT){
	            	try{
	            		Integer.parseInt(val.toString()) ;
	            	}catch (Exception e) {
	            		rs.setRetCode(ImportDataMgt.STRING_TO_NUMBER_ERROR) ;
	            		rs.setRetVal(fieldInfoBean.getDisplay().get(locale)) ;
	            		return rs ;
	            	}
	            }
	            /*����˫���� С��λ��*/
	            if(val !=null && val.toString().length()>0 && fieldInfoBean.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE){
	            	try{
	            		val = GlobalsTool.formatNumber(Double.valueOf(val.toString()), false, false, 
	            				GlobalsTool.getSysIntswitch(), fieldInfoBean.getTableBean().getTableName(), fieldInfoBean.getFieldName(), true) ;
	            	}catch (Exception e) {
	            		rs.setRetCode(ImportDataMgt.STRING_TO_NUMBER_ERROR) ;
	            		rs.setRetVal(fieldInfoBean.getDisplay().get(locale)) ;
	            		return rs ;
	            	}
	            }
	            
	            if(fieldInfoBean.getInputType() == 14){
	            	//���벿��
	            	val = importMgt.getEmpIdOrDeptCodeByName(String.valueOf(val), "dept");
	            }else if(fieldInfoBean.getInputType() == 15){
	            	//������Ա����
	            	val = importMgt.getEmpIdOrDeptCodeByName(String.valueOf(val), "empId");
	            }else if(fieldInfoBean.getTableBean().getTableName().indexOf("CRMClientInfo")>-1 && "Trade".equals(fieldInfoBean.getFieldName())){
	            	//CRM�ͻ��б���ҵ����
	            	rs = importMgt.findTradeByFullName((String)val);
	            	List<WorkProfessionBean> list = (List<WorkProfessionBean>)rs.retVal;
	            	if(list==null || list.size()==0){
	            		val = "";
	            	}else{
	            		WorkProfessionBean bean = list.get(0);
	            		val = bean.getId();
	            	}
	            }else if(fieldInfoBean.getTableBean().getTableName().indexOf("CRMClientInfo")>-1 && "District".equals(fieldInfoBean.getFieldName())){
	            	//CRM�ͻ��б��������
	            	rs = importMgt.findDistrictByFullName((String)val);
	            	List list = (List<Object>)rs.retVal;
	            	if(list==null || list.size()==0){
	            		val = "";
	            	}else{
	            		String disCode = ((Object[])list.get(0))[0].toString();
	            		val = disCode;
	            	}
	            }
	            
	            //����ֵ
	            setVale(importDataBean, fieldInfoBean, childValueMap, values,loginBean,fieldInfoBean.getFieldName(), val,row);
	            
	            
	        }
            
        }
                
        return rs;
    }
    private void setVale(ImportDataBean importDataBean,DBFieldInfoBean fieldInfoBean,HashMap childValueMap,HashMap values,LoginBean loginBean,String fName,Object val,int row){
    	if(importDataBean.getTargetTable().equals(fieldInfoBean.getTableBean().getTableName()) && row ==0){
            //����
    		if(val==null || val.toString().length() ==0){
    			if(values.get(fName)==null){
    				return;
    			}
    		}
    		BaseEnv.log.debug("JXLTOOL.setValue �����ֶ�"+fName+"="+val);
            values.put(fName,val);
        }else{
            //�ӱ�
            HashMap hm = (HashMap)childValueMap.get(fieldInfoBean.getTableBean().getTableName());
            if(val==null || val.toString().length() ==0){
    			if(hm==null ||hm.get(fName)==null){
    				return;
    			}
    		}
            if(hm == null){
                ArrayList childList = (ArrayList) values.get("TABLENAME_" + fieldInfoBean.getTableBean().getTableName());
                if (childList == null) {
                    childList = new ArrayList();
                    values.put("TABLENAME_" + fieldInfoBean.getTableBean().getTableName(), childList);
                }
                hm = new HashMap();
                //���÷�֧��������
                hm.put("SCompanyID", loginBean.getSunCmpClassCode());
                childList.add(hm);
                childValueMap.put(fieldInfoBean.getTableBean().getTableName(), hm) ;
            }
            BaseEnv.log.debug("JXLTOOL.setValue ��ϸ���ֶ�"+fName+"="+val);
            hm.put(fName,val);
        }
    }


    public static void writeTest() {
        try {
            WritableWorkbook wbook = Workbook.createWorkbook(new
                    FileOutputStream("d:\\ddd.xls"));
            WritableSheet ws = wbook.createSheet("test", 0);
            Label cell = new Label(0, 0, "��Ʒ��");
            ws.addCell(cell);
//                ws.addCell(new jxl.write.biff.MergedCells);

//                for (int row = 0; row < errorList.size(); row++) {
//                    List<ExcelFieldInfoBean> rowList = errorList.get(row);
//                    for (int col = 0; col < rowList.size(); col++) {
//                        ExcelFieldInfoBean bean = rowList.get(col);
//                        if (row == 0) {
//                            Label cell = new Label(col, 0, bean.getDisplay()==null?"":bean.getDisplay().toString());
//                            ws.addCell(cell);
//                        }
//                        Label cell = new Label(col, row + 1, bean.getValue()==null?"":bean.getValue().toString());
//                        ws.addCell(cell);
//                    }
//                }
            wbook.write();
            wbook.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
//            JXLTOOL.writeTest();
        try {
            JXLTOOL jxlt = new JXLTOOL("d:\\abc.xls");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	public List<String[]> getTopTitle() {
		return topTitle;
	}

	public void setTopTitle(List<String[]> topTitle) {
		this.topTitle = topTitle;
	}

	public String getSheetName() {
		return  this.sheet.getName();
	}

	public void setSheetName(String sheetName) {
		this.sheetName =  this.sheet.getName();
	}
	
	
}
