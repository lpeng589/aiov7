package com.menyi.web.util;

import java.util.ArrayList;

import com.menyi.aio.web.login.LoginBean;

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
public class ListGridHtml extends ListGrid{

	public int pageNo;
	public int pageSize;
    private ArrayList cols = new ArrayList();
    private ArrayList rows = new ArrayList();
    private String[] stats = null; //当前页面的总计
    private String[] statsAll = null ; //添加所有结果的总计
    private String reportType;
    private LoginBean loginBean;
    public boolean isSort=true;
    public int fixNumberCol ; //固定列数
    public boolean fixTitle ; //固定表头
    
    public boolean configScope ;	//是否有列配置权限
    public ArrayList hiddenList ;//列配置隐藏的列
    
    public String moreLanguage ;//多语言
    public boolean noHead =false;
    public boolean autoNumber=true;

    public ListGridHtml(LoginBean loginBean) {
		super(loginBean);
		this.loginBean = loginBean ;
	}
    
    public void addCols(String name,String fieldName, int width, String type,String groupName) {
        cols.add(new Cols(name, fieldName, width, type,groupName));
    }
    
    /**
     * 对应ListGrad.js 中的相应方法。 可以视情况改进方法，如适应目前的 'aa','bb'字符串的方式。
     * @param row String[]
     */
    public void addRows(String[] row) {
        rows.add(row);
    }

    public void addStats(String[] statrow) {
        this.stats = statrow;
    }

    public void addStatsAll(String[] statAll){
    	this.statsAll = statAll ;
    }
    
    public String toString() {
        //生成表格的html 代码。可参考 ListGrid中的对象.
        StringBuffer table = new StringBuffer("");
        int tableLen = 0; //计算表格的长度
        boolean isGroup=false;
        String tempGroup="";
        
        for (int i = 0; i < cols.size(); i++) {
            Cols col = (Cols) cols.get(i);
            tableLen = tableLen + col.width;
            if(col.groupName!=null&&col.groupName.length()>0){
            	if(col.groupName.equals(tempGroup)){
            		Cols colTemp=(Cols) cols.get(i-1);            		
            		col.groupCount=colTemp.groupCount+1;
            		colTemp.groupCount=0;
            		isGroup=true;
            	}else{            		
            		col.groupCount=1;
            	}
            }
            tempGroup=col.groupName;
        }
        table.append("<table sortCol=\"0\" isDesc=\"true\" border=\"0\" isStatsAll="+(statsAll!=null?true:false)+" hasStat="+(this.stats==null?false:true)+" cellpadding=\"0\" cellspacing=\"0\" class=\"listRange_list_function\" id=\"tblSort\" name=\"table\" width=\"" +
                     tableLen + "\">\r\n");
        table.append("<THead>\r\n");    
        /*固定表头*/
        table.append("<tr>\r\n");
        String rowSpan="";
        if(isGroup){
        	rowSpan=" rowSpan=\"2\"";
        }
        
        String fixRow = "" ;
        if(fixNumberCol>0){
        	fixRow = "class=\"fixedTitleRow\"" ;
        }else if(fixTitle){
        	fixRow = "class=\"scrollColThead\"" ;
        }
        
        if(!isSort){
	        table.append(
	                "<td width=\"30\" align=\"center\" "+fixRow+" "+rowSpan+"><IMG src=\"/" +
	                loginBean.getDefStyle() +
	                "/images/down.jpg\" width=\"5\" height=\"9\" border=0/></td>\r\n");
        }else{
        	table.append(
	                "<td width=\"30\" align=\"center\" "+fixRow+"  "+rowSpan+" onclick=\"sortTable(tblSort,0,'int')\">&nbsp;<IMG src=\"/" +
	                loginBean.getDefStyle() +
	                "/images/down.jpg\" width=\"5\" height=\"9\" border=0/></td>\r\n");
        }
        for (int i = 0; i < cols.size(); i++) {
            Cols col = (Cols) cols.get(i);
            if(col.width<=0) continue ;
            String sort="";
            if(isSort){
            	sort="onclick=\"sortTable(tblSort,"+(i+1)+",'"+col.type+"')\"";
            }
            if(col.type.equals("nosort")){
            	sort="";
            }
            String fixCol = "" ;
            if(isGroup){
            	if(col.groupCount>1){    
            		if(fixTitle){
                		fixCol = "class=\"scrollColThead\"" ;
                	}
            		table.append("<td "+fixCol+" width=\"" + (col.width*col.groupCount) +
            					"\" align=\"center\" colSpan=\""+col.groupCount+"\">" + col.groupName + "</td>\r\n");
            	}else if((col.groupName.length()>0&&col.groupCount==1)||col.groupName.length()==0){
            		if(!fixTitle && fixNumberCol!=0 && i<=fixNumberCol){
            			fixCol = "class=\"scrollColThead\"" ;
            		}else if(fixNumberCol!=0 && i<=fixNumberCol){
            			fixCol = "class=\"fixedTitleRow\"" ;
            		}else if(fixTitle){
            			fixCol = "class=\"scrollColThead\"" ;
            		} 
            		table.append("<td "+fixCol+" width=\"" + col.width +
        					"\" align=\"center\" "+sort+rowSpan+"\">" + col.name + "</td>\r\n");
            	}
            }else{
            	if(fixTitle){
            		fixCol = "class=\"scrollColThead\"" ;
            	}
            	table.append("<td "+fixCol+" width=\"" + col.width +
    					"\" align=\"center\" "+sort+">" + col.name + "</td>\r\n");
            }
            
        }
        table.append("</tr>\r\n");
        if(isGroup){
        	/*固定表头*/
        	table.append("<tr>");
	       	for (int i = 0; i < cols.size(); i++) {
	                Cols col = (Cols) cols.get(i);
	                String sort="";
	                if(isSort){
	                	sort="onclick=\"sortTable(tblSort,"+(i+1)+",'"+col.type+"')\"";
	                }
	                if(col.type.equals("nosort")){
	                	sort="";
	                }
	                if(col.groupName.length()>0&&(col.groupCount==0||col.groupCount>1)){
	                	String fixCol = "" ;
	                	if(fixTitle){
	                		fixCol = "class=\"scrollColThead\"" ;
		                }
	               	 	table.append("<td "+fixCol+" width=\"" + col.width +
	        					"\" align=\"center\" "+sort+">" + col.name + "</td>\r\n");
	                }
	            }
	       	table.append("</tr>\r\n");
        }
        table.append("</THead>\r\n");

        table.append("<TBody>\r\n");
        
        for (int i = 0; i < rows.size(); i++) {
        	String trheight="";
            String[] row = (String[]) rows.get(i);
            for(int j=0;j<row.length;j++){
            	if(row[j].indexOf("<img src=")>=0){
            		trheight="height=\"80\"";
            		break;
            	}
            }
            
            if (i % 2 == 0) {
                //如果是数据表列表，则加上单击事件
                if ("TABLELIST".equalsIgnoreCase(this.reportType)) {
                    table.append("<tr name=\"listLine\" class=\"trbg\" "+trheight+" bk=\"#EEF7FF\" onClick=\"" +
                    		GlobalsTool.revertTextCode4(row[row.length - 1].replace("~true", "")) +
                                 "\" onDblClick=\"" +
                                 GlobalsTool.revertTextCode4(row[row.length - 1].replace("~true", "")) +
                    			 "\" onmouseover=\"setBackground(this,true);\" onmouseout=\"setBackground(this,false);\">" +
                    			 "<input name=\"keyIds\" type=\"hidden\"/>\r\n");

                } else {
                	if(row[row.length-1].length()>0){
                		table.append("<tr name=\"listLine\" class=\"trbg\" "+trheight+" bk=\"#EEF7FF\" onDblClick=\"location.href='" +
                                 row[row.length - 1] + "'\"  onmouseover=\"setBackground(this,true);\" onmouseout=\"setBackground(this,false);\">" +
                                 		"<input name=\"keyIds\" type=\"hidden\"/>\r\n");
                	}else{
                		table.append("<tr name=\"listLine\" class=\"trbg\" "+trheight+" bk=\"#EEF7FF\" onmouseover=\"setBackground(this,true);\" " +
                				"onmouseout=\"setBackground(this,false);\"><input name=\"keyIds\" type=\"hidden\"/>\r\n");
                	}
                }
            } else {
                //如果是数据表列表，则加上单击事件
                if ("TABLELIST".equalsIgnoreCase(this.reportType)) {
                    table.append("<tr name=\"listLine\" onClick=\"" +
                                 GlobalsTool.revertTextCode4(row[row.length - 1].replace("~true", "")) +
                                 "\" onDblClick=\"" +
                                 GlobalsTool.revertTextCode4(row[row.length - 1].replace("~true", "")) +
                    			 "\" "+trheight+" onmouseover=\"setBackground(this,true);\" onmouseout=\"setBackground(this,false);\">" +
                    			 "<input name=\"keyIds\" type=\"hidden\"/>\r\n");

                } else {
                	if(row[row.length-1].length()>0){
                		table.append("<tr name=\"listLine\" onDblClick=\"location.href='" +
                                 row[row.length - 1] + "'\" "+trheight+" onmouseover=\"setBackground(this,true);\" onmouseout=\"setBackground(this,false);\" >" +
                                 		"<input name=\"keyIds\" type=\"hidden\"/>\r\n");
                	}else{
                		table.append("<tr name=\"listLine\" "+trheight+" onmouseover=\"setBackground(this,true);\" onmouseout=\"setBackground(this,false);\">" +
                				"<input name=\"keyIds\" type=\"hidden\"/>\r\n");
                	}

                }

            }
            if(fixNumberCol>0){
            	table.append(
                        "<td class=\"scrollRowThead\" align=\"center\" width=\"30\">" +
                        ((pageNo-1)*pageSize+(i + 1)) + "</td>\r\n");
            }else{
            	table.append("<td align=\"center\" width=\"30\">" +
                        ((pageNo-1)*pageSize+(i + 1)) + "</td>\r\n");
            }
            
            for (int j = 0; j < cols.size(); j++) {
                Cols col = (Cols) cols.get(j);
                if(col.width<=0) continue ;
                if(fixNumberCol!=0 && j<fixNumberCol){
                	table.append("<td class=\"scrollRowThead\"");
                }else{
                	table.append("<td ");
                }
                table.append(" width=" + col.width);
                if (col.type.equals("int") || col.type.equals("float")) {
                    table.append(" align=right");
                } else if (col.type.equals("date") || col.type.equals("other") ||
                           col.type.equals("nosort")) {
                    table.append(" align=center");
                } else {
                    table.append(" align=left");
                }
                String strContent = row[j] ;
                if(strContent!=null){
                	strContent = strContent.replaceAll("\\\\r\\\\n", "") ;
                	if(strContent.contains(";/")){
                		strContent = strContent.substring(0, strContent.indexOf(";/")) ;
                	}
                	if(strContent.contains(";")){
                		strContent = strContent.substring(0, strContent.indexOf(";")) ;
                	}
                }
                if(strContent==null || strContent.trim().length()==0){
                	strContent = "&nbsp;" ;
                }
                table.append(">");
                table.append(strContent);
                table.append("</td>\r\n");
            }
            table.append("</tr>\r\n");
        }

        //添加每页统计
        if (stats != null) {
            table.append("<tr>\r\n");
            if(fixNumberCol>0){
            	table.append("<td align=\"center\" colspan=\"2\" class=\"listheadonerow10_statistic scrollRowThead\">" +
                    stats[0] + "</td>\r\n");
            }else{
            	table.append("<td align=\"center\" colspan=\"2\" class=\"listheadonerow10_statistic\">" +
                        stats[0] + "</td>\r\n");
            }
            for (int i = 1; i < cols.size(); i++) {
                Cols col = (Cols) cols.get(i);
                if(fixNumberCol!=0 && i<fixNumberCol){
                	table.append("<td class=\"scrollRowThead\"");
                }else{
                	table.append("<td ");
                }
                
                if (col.type.equals("int") || col.type.equals("float")) {
                    table.append("align=\"right\"");
                }
                table.append(">");

                if (i < stats.length && stats[i] != null &&
                    stats[i].length() > 0) {
                    table.append(stats[i]);
                } else {
                    table.append("&nbsp;");
                }
                table.append("</td>\r\n");
            }
            table.append("</tr>\r\n");
        }
        
        //添加所有统计
        if (statsAll != null) {
            table.append("<tr>\r\n");
            if(fixNumberCol>0){
            	table.append("<td align=\"center\" colspan=\"2\" class=\"listheadonerow10_statistic scrollRowThead\">" +
                    statsAll[0] + "</td>\r\n");
            }else{
            	table.append("<td align=\"center\" colspan=\"2\" class=\"listheadonerow10_statistic\">" +
                        statsAll[0] + "</td>\r\n");
            }
            for (int i = 1; i < cols.size(); i++) {
                Cols col = (Cols) cols.get(i);
                if(fixNumberCol!=0 && i<fixNumberCol){
                	table.append("<td class=\"scrollRowThead\"");
                }else{
                	table.append("<td ");
                }
                if (col.type.equals("int") || col.type.equals("float")) {
                    table.append("align=\"right\"");
                }
                table.append(">");

                if (i < statsAll.length && statsAll[i] != null &&
                    statsAll[i].length() > 0) {
                    table.append(statsAll[i]);
                } else {
                    table.append("&nbsp;");
                }
                table.append("</td>\r\n");
            }
            table.append("</tr>\r\n");
        }
        table.append("</TBody>\r\n");

        table.append("</table>\r\n");
        return table.toString();
    }

    private class Cols {
        public String name;
        public String fieldName;
        public int width;
        public String type;
        public String groupName;
        public int groupCount;
        public Cols(String name,String fieldName, int width, String type,String groupName) {
            this.name = name;
            this.fieldName = fieldName ;
            this.type = type;
            this.width = width;
            this.groupName=groupName;
        }
    }


    public ArrayList getCols() {
        return cols;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

	public void setSort(boolean isSort) {
		this.isSort = isSort;
	}

}
