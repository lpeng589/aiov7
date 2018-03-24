package com.menyi.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.web.login.LoginBean;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: 周新宇
 * </p>
 * 
 * @author 周新宇
 * @version 1.0
 */
public class ListGrid {
	public int pageNo;
	public int pageSize;
	private ArrayList<Cols> cols = new ArrayList<Cols>();
	private ArrayList<String[]> rows = new ArrayList<String[]>();
	private String[] stats = null; // 当前页面的总计
	private String[] statsAll = null; // 添加所有结果的总计
	public String currentRow;
	private String reportType;
	public boolean isSort = true;
	public int fixNumberCol; // 固定列数
	public String listType;
	public boolean fixTitle; // 固定表头
	public boolean configScope; // 是否有列配置权限
	public ArrayList<ReportField> hiddenList;// 列配置隐藏的列
	public String rowRemark;//用于记录数据表列表的rowRemark字段，如billNo

	public String moreLanguage;// 多语言
	public boolean noHead = false;
	public boolean autoNumber = true;
	DefineReportBean defBean = null;//报表设置，现在只用在交叉报表中
	boolean hasStatus= false; //是否有状态字段
	int statusWidth= 0; //是否有状态字段
	public String draftQuery;
	public int lineHeight=Integer.parseInt(GlobalsTool.getSysSetting("reportLineHeight"));
	boolean hasOppertion = false; //列表是否有操作按扭，列配置中可以隐藏
	int operwidth = 55;			
	
	public String curKeyId = "";//用来记录当前的keyId,如果keyId相同，则不显示操作按扭和，状态字段，数据表列表，如果主从表一起显示，则重复不显示
	public LoginBean loginBean;
	
	
	private static final List<String> calls = Arrays.asList(new String[] { "update", "detail", "next", "addClass","status",  "check", "nextClass", "endClass",
			"hurryTrans" });
	
	
	public void addCols(String name, String fieldName, int width, String type, String groupName) {
		cols.add(new Cols(name, fieldName, width, type, groupName));
	}
	
	public int getColSize(){
		return cols.size();
	}

	public ListGrid(LoginBean loginBean)
	{
		this(loginBean,null);
	}
	public ListGrid(LoginBean loginBean,DefineReportBean defBean) {
		this.loginBean = loginBean;
		this.defBean = defBean;		
	}

	/**
	 * 对应ListGrad.js 中的相应方法。 可以视情况改进方法，如适应目前的 'aa','bb'字符串的方式。
	 * 
	 * @param row
	 *            String[]
	 */
	public void addRows(String[] row) {
		rows.add(row);
	}

	public void addStats(String[] statrow) {
		this.stats = statrow;
	}

	public void addStatsAll(String[] statAll) {
		this.statsAll = statAll;
	}

	public int getHeight(int size) {
		switch (size) {
		case 0:
			return 0;
		case 1:
			return 150;
		case 2:
			return 150;
		case 3:
			return 120;
		case 4:
			return 110;
		case 5:
			return 100;
		case 6:
			return 80;
		case 7:
			return 70;
		case 8:
			return 60;
		case 9:
			return 50;
		case 10:
			return 40;
		default:
			return 30;
		}

	}
	
	//取锁定列数
	private void setLockNum(){
		if(defBean.userConfig.size()==0){
			ArrayList<ColConfigBean> colList = BaseEnv.colConfig.get(defBean.getReportNumber() + "list");
			if(colList==null || colList.size() ==0){
				
			}else{
				for(int i=0 ;i<colList.size();i++){
					ColConfigBean ccb = colList.get(i);
					if(ccb.getLock()==1){
						fixNumberCol = i+1;
						break;
					}
				}
			}
		}else{
			for(int i=0 ;i<defBean.userConfig.size();i++){
				String[] colB =defBean.userConfig.get(i);
				if(colB[2].equals("1")){
					fixNumberCol = i+1;
					break;
				}
			}
		}
	}
	
	//列不存在时，反回-999
	private int hasSepColumn(String field,int oldWidth){
		boolean hasColumn = false;
		int columnWidth= oldWidth;
		if(defBean.userConfig.size()==0){
			ArrayList<ColConfigBean> colList = BaseEnv.colConfig.get(defBean.getReportNumber() + "list");
			if(colList==null || colList.size() ==0){
				hasColumn = true;//没有用户列配置，也没公共列配置
			}else{
				for(ColConfigBean ccb :colList){
					if(ccb.getColName().equals(field)){
						hasColumn = true;//没有用户列配置，有公共列配置
						int defineWidth = GlobalsTool.getFieldWidth(defBean.getReportNumber(), ccb.getColName());
						if(defineWidth != 0){
							columnWidth = defineWidth;
						}
						break;
					}
				}
			}
		}else{
			for(String[] colB:defBean.userConfig){
				if(colB[0].equals(field)){
					hasColumn = true;//用户列配置
					try{
						int defineWidth = Integer.parseInt(colB[1]);
						if(defineWidth != 0){
							columnWidth = defineWidth;
						}
					}catch(Exception ee){}
					break;
				}
			}
		}
		
		if(hasColumn){
			return columnWidth;
		}else{
			return -999;
		}
	}
	
	
	/**
	 * 用来生成ＨＴＭＬ表格
	 * 表头用dataHeader存着，主体用dataBody存着。
	 * 表头和主体生成后，合并成一个ＴＡＢＬＥ
	 * 然后把这ＴＡＢＬＥ生成四个ＴＡＢＬＥ，和ＤＩＶ组合实现锁行锁列的功能
	 */
	public String toString() {
		/**
		 * 在生成ＨＴＭＬ时使用，用来存储每行的列数，以方便统计行补齐ＴＤ
		 */
		int tds = 0;
		
		// 完整的数据表格的宽度
		int width = 0;
		//左边锁定的表格的宽度
		int lockWidth = 0;
		// 完整数据的header字符串
		StringBuilder dataHeader = new StringBuilder("<thead><tr height=\"24\"></tr><tr height=\"24\"></tr></thead>");
		// 用来存储每个分组包含多少项 
		StringBuilder dataBody = new StringBuilder("<tbody></tbody>");
		StringBuilder columnBody = new StringBuilder("<tbody></tbody>");
		//记录每个分组包含多少列
		HashMap<String, Integer> column = new HashMap<String, Integer>();
		boolean isGroup = false;
		//以下两个参数用来处理数据分组时，同组字段不连续问题，把不连续的分组合在一起
		HashMap<String, Integer> columnPos = new HashMap<String, Integer>();
		ArrayList<Cols> colArr = new ArrayList<ListGrid.Cols>();
		int tmpC = 0;
		for (Cols col : cols) {
			col.pos = tmpC++;
			if (col.name != null && !col.name.equals("") && col.groupName != null && !col.groupName.equals("")){
				if( !col.name.equals(col.groupName)) isGroup = true; 
				if (column.containsKey(col.groupName)) {
					column.put(col.groupName, column.get(col.groupName) + 10000 + col.width);
					isGroup = true; 
					int pos = columnPos.get(col.groupName)+1;
					colArr.add(pos,col);
					for (Entry<String, Integer> ent : columnPos.entrySet()) {
						if(ent.getValue()>=pos)
						{
							ent.setValue(ent.getValue()+1);
						}
					}
					columnPos.put(col.groupName, pos);
				} else
				{
					column.put(col.groupName, 10000+col.width);
					colArr.add(col);
					columnPos.put(col.groupName, colArr.size()-1);
				}
			}
			else
				colArr.add(col);
		}
		cols = colArr;
		// 初始化表格宽度以及左边锁定表格的宽度,
		// 以及生成thead
		String groupName = "";
		
		dataHeader.insert(dataHeader.indexOf("</tr>"), "<td " + (isGroup ? "rowspan=\"2\"" : "") + " width=\"35\">No.</td>");
		
		if ("TABLELIST".equalsIgnoreCase(this.reportType)) {// 如果是数据表列表，
			if (!"0".equals(listType) && !"draftPop".equals(draftQuery) ) {
				dataHeader
						.insert(dataHeader.indexOf("</tr>"),
								"<td "
										+ (isGroup ? "rowspan=\"2\"" : "")
										+ " width=\"24\"><input type=\"checkbox\" onclick=\"jQuery('input[name=\\\'keyId\\\']').attr('checked',this.checked);reloadkeyIds();\"/></td>");
			} else {
				dataHeader
						.insert(dataHeader.indexOf("</tr>"),
								"<td "
										+ (isGroup ? "rowspan=\"2\"" : "")
										+ " width=\"24\"></td>");
			}
			width += 24 + 1;
			lockWidth += 24 + 1;
		}
		//增加序号列
		int seq = (pageNo - 1) * pageSize + 1;
		if (seq < 1)
			seq = 1;
		
		width += 35+1;
		lockWidth += 35+1;
		//计算锁定的列数，如果用户做过锁定动作
		setLockNum();
		
		
		int fixed = 0;
		if ("TABLELIST".equalsIgnoreCase(this.reportType) && !"draftPop".equals(draftQuery)) {// 如果是数据表列表，
			//数据表列表增加操作列，收纳所有的操作按扭
			int columnWidth = hasSepColumn("operationbt",55);
			if(columnWidth== -999){
				hasOppertion = false;
			}else{
				hasOppertion = true;
				operwidth = columnWidth;
			}
			if(hasOppertion){
				dataHeader.insert(dataHeader.indexOf("</tr>"),
						"<td "+ (isGroup ? "rowspan=\"2\"" : "")+ " df=\"operationbt\" noHidden=\"false\" width=\""+operwidth+"\">操作</td>");
				width += operwidth + 1;
				if (fixed < fixNumberCol) {
					lockWidth += operwidth + 1;
					fixed ++;
				}				
			}else{ //用来定位，状态字段插入位置
				dataHeader.insert(dataHeader.indexOf("</tr>"),
						"<!--操作-->");
			}
		}
		
		//取状态列
		for (int i = 0; i < cols.size(); i++) {
			Cols col = cols.get(i);
			if (col.width < 0)
				continue;
			if (!"hidden".equals(col.fieldName) && (! "TABLELIST".equalsIgnoreCase(this.reportType) ||  
					(!calls.contains(col.fieldName) || col.fieldName.equals("next") || col.fieldName.equals("status")  ) )) {
				if(col.fieldName.equals("status") && "TABLELIST".equalsIgnoreCase(this.reportType)) { //数据表列表有状态字段，在操作后插入
					hasStatus = false;
					statusWidth= col.width;
					int columnWidth = hasSepColumn("statusView",col.width);
					if(columnWidth== -999){
						hasStatus = false;
					}else{
						hasStatus = true;
						statusWidth = columnWidth;
					}					
					if(hasStatus){						
						width += statusWidth + 1;// 计算整个表格的宽度
						if (fixed < fixNumberCol) {
							lockWidth += statusWidth + 1;
							fixed++;
						}
					}
				}
			}
		}
		
		
		
		for (int i = 0; i < cols.size(); i++) {
			Cols col = cols.get(i);
			if (col.width < 0)
				continue;
			if("img".equals(col.type)){
				lineHeight = 85; //如果有图片字段，行高自动变为100
				String picSize = GlobalsTool.getSysSetting("picSize");
				if(picSize != null && picSize.length() > 0){
					try{
						lineHeight = Integer.parseInt(picSize)+5;
					}catch(Exception e){
						BaseEnv.log.error("ListGrid.toString Error ",e);
					}
					if(lineHeight < Integer.parseInt(GlobalsTool.getSysSetting("reportLineHeight"))){
						lineHeight =Integer.parseInt(GlobalsTool.getSysSetting("reportLineHeight"));
					}
				}
			}
			//隐藏列和操作列不增加表格
			if (!"hidden".equals(col.fieldName) && (! "TABLELIST".equalsIgnoreCase(this.reportType) ||  
					(!calls.contains(col.fieldName) || col.fieldName.equals("next") || col.fieldName.equals("status")  ) )) {
				tds ++;
				
				if (isGroup && column.containsKey(col.groupName) && column.get(col.groupName) > 0)// 如果同上一个的分组不同而且分组包含多个栏目
				{
					if (!groupName.equals(col.groupName))// 插入组名
					{
						dataHeader.insert(dataHeader.indexOf("</tr>"), "<td colspan=\"" + (column.get(col.groupName)/10000) + "\" width=\"" + (column.get(col.groupName)%10000) + "\" >" + col.groupName + "</td>");
					}
					dataHeader.insert(dataHeader.lastIndexOf("</tr>"), "<td df=\""+col.fieldName+"\" noHidden=\""+calls.contains(col.fieldName)+"\" width=\"" + col.width + "\">" + col.name + "</td>");
					width += col.width + 1;// 计算整个表格的宽度
					if (fixed < fixNumberCol) {
						lockWidth += col.width + 1;
						fixed++;
					}
				} else if(col.fieldName.equals("status") && "TABLELIST".equalsIgnoreCase(this.reportType) && !"draftPop".equals(draftQuery)) { //数据表列表有状态字段，在操作后插入
							
					if(hasStatus){			
						if(hasOppertion){
							dataHeader.insert(dataHeader.indexOf("操作</td>")+"操作</td>".length(), "<td " + (isGroup ? "rowspan=\"2\"" : "") + " df=\"statusView\" noHidden=\"false\" width=\"" + statusWidth + "\">" + col.name
									+ "</td>");
						}else{
							dataHeader.insert(dataHeader.indexOf("<!--操作-->"), "<td " + (isGroup ? "rowspan=\"2\"" : "") + " df=\"statusView\" noHidden=\"false\" width=\"" + statusWidth + "\">" + col.name
									+ "</td>");
						}
						tds --;
					}
				} else {
					dataHeader.insert(dataHeader.indexOf("</tr>"), "<td " + (isGroup ? "rowspan=\"2\"" : "") + " df=\""+col.fieldName+"\" noHidden=\""+calls.contains(col.fieldName)+"\" width=\"" + col.width + "\">" + col.name
							+ "</td>");
					
					width += col.width + 1;// 计算整个表格的宽度
					if (fixed < fixNumberCol) {
						lockWidth += col.width + 1;
						fixed++;
					}
				}					
				groupName = col.groupName;
			}
		}
		// 处理无用的ＴＲ
		int tmpInt = dataHeader.indexOf("<tr height=\"24\"></tr>");
		if (tmpInt != -1) {
			dataHeader.delete(tmpInt, tmpInt + 21);
		}
		// 抬头处理完毕
		// 开始处理数据表格
		for (int i = 0; i < rows.size(); i++) {
			String[] row = (String[]) rows.get(i);
			dataBody.insert(dataBody.lastIndexOf("</tbody>"), getRow(row, seq + i,tds));
		}
		dataBody.insert(dataBody.lastIndexOf("</tbody>"), getRow(statsAll,-1,tds));
		
		
		String hiddenField = "";//隐藏的字段的名称，宽度以及显示文字，字段之间有｜分隔，字段属性用：分隔
		String showField = "";//显示的字段的名称，宽度以及显示文字，字段之间有｜分隔，字段属性用：分隔
		if (configScope) {
			//不管如何，显示字段总是当前列显示字段
			List<String> al = new ArrayList<String>();
			boolean tmpView = false;//用来判断是否已加入了交叉字段
			for (Cols col : cols) {
				if(col.width>0 && !"hidden".equals(col.fieldName) && !calls.contains(col.fieldName))
				{
					if(col.fieldName.startsWith(col.groupName+"_"))//交叉的字段
					{
						if(!tmpView && defBean.getCrossField()!=null)
						{
							tmpView = true;
							ReportField field = defBean.getCrossField();
							showField += field.getAsFieldName()+":"+field.getWidth()+":"+field.getDisplay()+"|";
						}
						String fName = col.fieldName.substring(col.groupName.length()+1);
						if(!al.contains(fName))//如果是交叉出来的字段，则存进al中，后面分组出来的就不进行处理了
						{
							al.add(fName);
							showField += fName+":"+col.width+":"+col.name+"|";
						}
					}
					else
						showField += col.fieldName+":"+col.width+":"+col.name+"|";
				}
			}
			if(hasStatus){
				showField = "statusView:"+statusWidth+":审核状态|"+showField;
			}
			if(hasOppertion){
				showField = "operationbt:"+operwidth+":操作|"+showField;
			}
			
			//当超级用户，或者非超级用户但是没有设置列配置时，用户个性列配置显示按默认
			if(loginBean.getId().equals("1") || !BaseEnv.colConfig.containsKey(defBean.getReportNumber() + "list")){ 
				for (int m = 0; hiddenList != null && m < hiddenList.size(); m++) {
					if(Integer.parseInt(hiddenList.get(m).getWidth())>0)
					hiddenField += hiddenList.get(m).getAsFieldName() + ":" + hiddenList.get(m).getWidth() + ":" + hiddenList.get(m).getDisplay() + "|";
				}	
				if ("TABLELIST".equalsIgnoreCase(this.reportType) && !"draftPop".equals(draftQuery) && !hasStatus) {// 如果是数据表列表，
					hiddenField = "statusView:"+statusWidth+":审核状态|"+hiddenField;
				}
				if ("TABLELIST".equalsIgnoreCase(this.reportType) && !"draftPop".equals(draftQuery) && !hasOppertion) {// 如果是数据表列表，
					hiddenField = "operationbt:"+operwidth+":操作|"+hiddenField;
				}
			}else{
				//用户独立个性列配置显示字段按列配置后显示的结果
				if(defBean.userConfig.size()==0){
					//用户没有个性设置，
					hiddenField="";					
				}else{
					hiddenField="";			
					//从列配置中取未在显示字段中的列
					ArrayList<ColConfigBean> colList = BaseEnv.colConfig.get(defBean.getReportNumber() + "list");
					for(ColConfigBean ccb :colList){
						if(!showField.contains(ccb.getColName()+":")){
							String defineWidth = GlobalsTool.getFieldWidth(defBean.getReportNumber(), ccb.getColName())+"";
							ReportField currf = null;
							if(ccb.getColName().equals("operationbt")){
								hiddenField += "operationbt:"+operwidth+":操作|";
							}else if(ccb.getColName().equals("statusView")){
								hiddenField += "statusView:"+statusWidth+":审核状态|";
							}else{
								for(ReportField rf:defBean.getDisFields2()){
									if(rf.getAsFieldName().equals(ccb.getColName())){
										currf = rf;
										break;
									}
								}
								if(currf != null){
									if(defineWidth.equals("0")){
										defineWidth = currf.getWidth();
									}
									hiddenField += ccb.getColName()+ ":" + defineWidth + ":" + currf.getDisplay() + "|";
								}
							}
						}
					}
				}
			}
		}
		
		StringBuilder sb = new StringBuilder("<table hiddenField=\""+hiddenField+"\" showField=\""+showField+"\" configScope=\""+configScope+"\" id=\"k_listgrid\" style=\"table-layout:fixed\" border=\"0\" cellspacing=\"0\" width=\"" + width + "\">");
		sb.append(dataHeader);
		sb.append(dataBody);
		sb.append("</table>");

		StringBuilder header = new StringBuilder();
		StringBuilder fix = new StringBuilder();
		StringBuilder columnStr = new StringBuilder();

		header.append(sb.substring(0, sb.indexOf("</thead>") + 8)).append("</table>");
		int pos = 0;
		fix.append(header.toString());
		columnStr.append(sb.toString());
		int columnNum = fixNumberCol + 1;
		if ("TABLELIST".equalsIgnoreCase(this.reportType)) {
			columnNum+=1;
			if(hasOppertion && columnNum < 3){
				columnNum = 3;
			}
		}

		if (isGroup) {
			pos = columnStr.indexOf("<tr");
			columnStr.insert(pos + 3, " height=\"48\" ");
			pos = columnStr.indexOf("<tr", pos + 1);
			columnStr.delete(pos, columnStr.indexOf("</thead>"));
		}
		pos = 0;
		//锁定列头，截取列头的表格
		while ((pos = columnStr.indexOf("<tr", pos)) != -1) {
			for (int i = 0; i < columnNum; i++) {
				pos = columnStr.indexOf("<td", pos) + 1;
			}
			if(columnStr.indexOf("<td", pos)<0){
				break;
			}
			//有可能出现锁定列数比实际列数还多的情况
			if(columnStr.indexOf("<td", pos) < columnStr.indexOf("</tr>", pos)){
				columnStr.delete(columnStr.indexOf("<td", pos), columnStr.indexOf("</tr>", pos));
				pos += 5;
			}
		}
		pos = columnStr.indexOf("width=\"");
		columnStr.replace(pos + 7, columnStr.indexOf("\"", pos + 8), String.valueOf(lockWidth));

		return "<div id=\"kt_head\" style=\"z-index:55;width:" + lockWidth + "px;overflow:hidden;position:absolute;top:0;left:0\">" + header.toString()
				+ "</div><div id=\"k_head\" style=\"z-index:50;overflow:hidden;width:100%;position:absolute;top:0;left:0\">" + header.toString()
				+ "</div><div id=\"k_column\" style=\"z-index:45;width:" + (lockWidth) + "px;position:absolute;background:#fff;overflow:hidden;top:0;left:0\">"
				+ columnStr.toString() + "</div><div id=\"k_data\" style=\"z-index:40;width:100%;overflow:auto;position:absolute;top:0;left:0\">"
				+ sb.toString().replaceAll("<input[\\d\\D]*?/>", "") + "</div>";
	}

	// 生成一行数据
	private String getRow(String[] data, int rowNo,int tds) {
		if(data==null)
			return "";
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null)
				data[i] = "";
		}
		String[] lastCol = data[data.length - 1].split("~");
		
		String dblClick = "";
		if (!"TABLELIST".equalsIgnoreCase(this.reportType) && data[data.length - 1].length() == 0)
			;
		else
			dblClick = lastCol[0];

		dblClick.replaceAll("'", "\\'");
		
		boolean isSameId=false;//是否是相同keyId的数据，数据表列表，如果是相同keyId,则不显示操作按扭，级状态等字段
		if(data[0].equals(curKeyId)){
			isSameId = true;
		}else{
			curKeyId = data[0];
		}

		String del = "";
		String workflowNodeName = "";
		String draft="";
		if (lastCol.length > 1) {
			del = lastCol[1];
		}
		if (lastCol.length > 2) {
			workflowNodeName = lastCol[2];
			draft = "draft".equals(lastCol[2])?"true":"false";
		}

		String columnInfo = "";
		for (int i = 0; i < cols.size() && i < data.length; i++) {
			if(cols.get(i).fieldName.toLowerCase().equalsIgnoreCase(rowRemark)){ //只记录rowRemar字段就行
				if (!cols.get(i).fieldName.toLowerCase().equals("hidden"))
					columnInfo += " " + cols.get(i).fieldName + "=\"" + GlobalsTool.replaceSpecLitter(data[cols.get(i).pos]) + "\"";
				else
					columnInfo += " k_hidden=\"" + GlobalsTool.replaceSpecLitter(data[cols.get(i).pos]) + "\"";
			}
		}
		String clickEvent = "";
		if ("TABLELIST".equalsIgnoreCase(this.reportType) && rowNo != -1)//数据表列表且这行不是统计行，就加上单击事件
			clickEvent = " onClick=\"gridselectKeyId(this);reloadkeyIds();\" "; 
		
		StringBuilder sb = new StringBuilder("<tr del=\"" + del + "\" workflowNodeName=\"" + workflowNodeName + "\" " + columnInfo + " ondblclick=\"" + dblClick + "\" height=\""+lineHeight+"px\"" + clickEvent
				+ "></tr>\r\n");
		
		sb.insert(sb.lastIndexOf("</tr>"), "<td >" + (rowNo==-1?"":String.valueOf(rowNo)) + "</td>");
		
		if ("TABLELIST".equalsIgnoreCase(this.reportType)) {// 如果是数据表列表，
			if (rowNo == -1 || isSameId) { //相同id不显示复选框
				sb.insert(sb.lastIndexOf("</tr>"), "<td></td>");
			} else if (listType.equals("0") || "draftPop".equals(draftQuery)) {
				sb.insert(sb.lastIndexOf("</tr>"), "<td align=\"center\"><input type=\"radio\" value=\"" + data[0]
						+ "\" name=\"keyId\"  onclick=\"stopEvent()\"/></td>");
			} else {
				sb.insert(sb.lastIndexOf("</tr>"), "<td align=\"center\"><input type=\"checkbox\" value=\"" + data[0]
						+ "\" name=\"keyId\" onclick=\"this.checked=!this.checked\"/></td>");
			}
		}

		
		if( !"draftPop".equals(draftQuery) && "TABLELIST".equalsIgnoreCase(this.reportType)){
			if(hasOppertion){
				if (rowNo != -1 && !isSameId) {// 如果是数据表列表，插入操作按扭
					sb.insert(sb.lastIndexOf("</tr>"), "<td  class=\"opbt\" align=\"center\"  vid=\""+data[0]+"\">操作</td>");
				}else{
					sb.insert(sb.lastIndexOf("</tr>"), "<td  align=\"center\" cn=\"操作\">&nbsp;</td>");
				}
			}else{
				sb.insert(sb.lastIndexOf("</tr>"), "<!--操作-->");
			}
		}
		boolean hadAdd = false;
		String optstr=""; //用于记录所有操作按扭字符串
		String detailoptstr = "";
		String updateoptstr = "";
		int optCount=0;
		
		for (int j = 0; j < cols.size() && j < data.length; j++) {
			Cols col = cols.get(j);
			if (cols.get(j).width <= 0)
				continue;
			String cellData = data[cols.get(j).pos];
			String caption = "";
			if (cellData != null) {
				cellData = cellData.replaceAll("\\\\r\\\\n", "");
				caption = cellData;
				
				if (cellData.indexOf("#;#") != -1  && !"img".equals(col.type)) {
					caption = cellData.substring(0, cellData.indexOf("#;#"));
				}
				//caption = caption.replaceAll("\\|=", "");
				caption = GlobalsTool.replaceSpecLitter(caption);
				caption = caption.replaceAll("\\\\&apos", "&apos");
			}
			if(hasStatus && rowNo == -1 && !hadAdd){
				tds --;
				//如果有状态字段且是统计行，行插入一个空白td
				sb.insert(sb.lastIndexOf("</tr>"), "<td align=\"\">&nbsp;</td>");
			}
			if(rowNo==-1 && !hadAdd)
			{
				caption = data[0];
				hadAdd = true;
			}
			String dbClickStr = "";

			String align = "left";
			
			if ("TABLELIST".equalsIgnoreCase(this.reportType) &&col.fieldName.indexOf("BillNo") != -1  && !"draftPop".equals(draftQuery))// 有call这词,或者是图片,显示图片
			{
				align = "left";
				dbClickStr = "detail('"+data[0]+",')";
			}else if (col.type.indexOf("call") != -1   && !"draftPop".equals(draftQuery))// 有call这词,或者是图片,显示图片
			{
				align = "left";
				if (cellData.indexOf("#;#") != -1 && !cellData.endsWith("#;#"))
				{
					dbClickStr = cellData.substring(cellData.indexOf("#;#") + 3).trim();
				}
				if(!dbClickStr.equals("") && !dbClickStr.toLowerCase().startsWith("javascript:"))
				{
					dbClickStr = "openLink('" + dbClickStr +"')";
				}
			}else if (col.type.equals("img"))// 有call这词,或者是图片,显示图片 如果是图片则加上图片标签
			{
				align = "center";	
				dbClickStr = "showPIC('" + caption +"')";
				if(caption.indexOf(";") > 0){
					caption = caption.substring(0,caption.indexOf(";"));
				}
				String picSize = GlobalsTool.getSysSetting("picSize");
				if(picSize != null && picSize.length() > 0 && "0".equals(picSize)){
					if(caption.length() > 0){
						caption = ""+col.name;
					}else{
						caption ="";
					}
				}else{
					caption = "<img style=\"height:"+(lineHeight-5)+"px;\" src=\""+caption +"\" />";
				}
				
			}else if (col.fieldName.equals("next"))// 下级保持不变
			{
				align = "center";
				if (cellData.indexOf("#;#") != -1 && !cellData.endsWith("#;#"))
					dbClickStr = col.fieldName+"('" + cellData.substring(cellData.indexOf("#;#") + 3).trim() + "')";			
			}else if (calls.contains(col.fieldName) && "TABLELIST".equalsIgnoreCase(this.reportType) && !col.fieldName.equals("status")) { //数据表列表包含时
				if (rowNo != -1 && !isSameId && cellData.indexOf("#;#") != -1 && !cellData.endsWith("#;#") )
				{ //新的规则是有权限自动进修改界面，无权限自动进详情界面，所以这里去掉一个
					optCount ++;
					String cn = cellData.substring(0,cellData.indexOf("#;#")).trim();
					String cv = cellData.substring(cellData.indexOf("#;#")+3).trim();
					String ostr = " f"+optCount+"=\""+cn+":javascript:void(0);"+(col.fieldName.equals("endClass")?"k_endClass":col.fieldName) + "('" + cv  + "')\" ";
					if(col.fieldName.equals("detail")){
						detailoptstr=ostr;
					}else if(col.fieldName.equals("update")){
						updateoptstr = ostr;
					}else{
						optstr += ostr;
					}
				}
				//这里不能插入列
				continue;
			}else if (col.fieldName.equals("status") && "TABLELIST".equalsIgnoreCase(this.reportType))// 状态字段要插在前面
			{
				align = "left";
				if (rowNo!=-1 && cellData.indexOf("#;#") != -1 && !cellData.endsWith("#;#"))
					dbClickStr = col.fieldName+"('" + cellData.substring(cellData.indexOf("#;#") + 3).trim() + "')";
				if(hasStatus){	
					tds--;
					if (rowNo != -1 && !isSameId) {// 如果是数据表列表，插入操作按扭
						int ipos = 0;
						if(hasOppertion){
							ipos = sb.indexOf(">操作</td>")+">操作</td>".length();
						}else{
							ipos = sb.indexOf("<!--操作-->");
						}
						sb.insert(ipos, "<td align=\""
								+ align
								+ "\">"
								+ ((dbClickStr.equals("")) ? caption
										: ("<span style=\"color:blue;cursor:pointer\" onclick=\"" + dbClickStr + "\">" + caption + "</span>")) + "</td>");
					}else{
						int ipos = 0;
						if(hasOppertion){
							ipos = sb.indexOf("cn=\"操作\">&nbsp;</td>")+"cn=\"操作\">&nbsp;</td>".length();
						}else{
							ipos = sb.indexOf("<!--操作-->");
						}
						sb.insert(ipos, "<td align=\""
							+ align
							+ "\">&nbsp;</td>");
					}
				}
				continue;
			}else if (calls.contains(col.fieldName)  && !"TABLELIST".equalsIgnoreCase(this.reportType))// 非数据表列表
			{
				align = "left";
				if (cellData.indexOf("#;#") != -1 && cellData.indexOf("#;#")!=cellData.length()-1)
					dbClickStr = (col.fieldName.equals("endClass")?"k_endClass":col.fieldName)+"('" + cellData.substring(cellData.indexOf("#;#") + 3).trim() + "')";
				
			}
			if (col.type.startsWith("float") || col.type.startsWith("int")) {
				align = "right";
			}
			tds --;
			if(rowNo==-1 || data[0].indexOf("|=")!=-1)//如果是统计行，则不生成链接
				dbClickStr = "";
			sb.insert(sb.lastIndexOf("</tr>"), "<td align=\"" + align + "\">"
					+ ((dbClickStr.equals("")) ? caption : ("<span style=\"color:blue;cursor:pointer\" onclick=\"" + dbClickStr + "\">" + caption + "</span>")) + "</td>");
		}
//		if("true".equals(del)){ 
//			optCount ++;
//			optstr +=" f"+optCount+"=\"删除:javascript:void(0);deleteOne('" + data[0]  + "')\" ";
//		}
		
		if(updateoptstr.length() > 0){
			optstr = updateoptstr +optstr;
		}else if(detailoptstr.length() > 0){
			optstr = detailoptstr +optstr;
		}
		//插入操作按扭
		sb.insert(sb.lastIndexOf("class=\"opbt\"")+12, " "+optstr+" ");
		
		for (int i = 0; i < tds; i++) {
			sb.insert(sb.lastIndexOf("</tr>"),"<td></td>");
		}
		
		return sb.toString();
	}

	private class Cols {
		public String name;// 显示名
		public String fieldName;// 字段名
		public int width;// 宽度
		public String type;// 类型
		public String groupName;// 分组名
		public int pos;
		
		public Cols(String name, String fieldName, int width, String type, String groupName) {
			this.name = name;
			this.fieldName = fieldName.replaceAll(" ", "");
			this.type = type;
			this.width = width;
			this.groupName = groupName;
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
