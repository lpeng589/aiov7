package com.menyi.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;

/**
 * 
 * <p>
 * Title:������Ҫ�������ɵ������ڵ�HTML���
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2010-7-12
 * @Copyright: �������
 * @Author ��СǮ
 */
public class PopupGrid {
	private static final List<String> calls = Arrays.asList(new String[] { "update", "detail", "next", "addClass", "status", "check", "nextClass", "endClass",
			"hurryTrans" });

	public int pageNo;

	public int pageSize;
	
	public String detailPopup=null;
	public String topPopup=null;

	public boolean configScope;
	/**
	 * �ַ������飺�����ơ�����ȡ����ֶ���
	 */
	private ArrayList<String[]> cols = new ArrayList<String[]>();

	private ArrayList rows = new ArrayList();

	private ArrayList<String[]> hiddenField = new ArrayList<String[]>();

	private ArrayList<String> listValue = new ArrayList<String>();

	private LoginBean loginBean = null;

	private String nextClass;

	private String strRadio;

	private String mainPop;

	private String isRoot;

	private String selectName;

	private boolean saveParentFlag;
	public String moreLanguage;
	
	public String fieldName;
	
	public int lineHeight=24;

	public void setSelectName(String selectName) {
		this.selectName = selectName;
	}

	public PopupGrid(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public void setCols(ArrayList cols) {
		this.cols = cols;
	}

	public void setRows(ArrayList rows) {
		this.rows = rows;
	}

	public void setListValue(ArrayList<String> listValue) {
		this.listValue = listValue;
	}

	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}
	/**
	 * ���ݵ��������������ɱ��(table)
	 * ���̣������ɱ���ͷ�������壬�ٺϲ���һ�������ģԣ��£̣�
	 * Ȼ��𿪳��ĸ��ԣ��£̣ţ������Ӧ���ϣ��ϣ������£����ֱ�����div��,ʵ������ͷ����ͷ����
	 * dataHeaderΪ��ͷ���ַ���
	 * dataBodyΪ�������
	 * fixNumberColΪ���������������������Լ�ѡ������,�����й̶�������
	 * lockWidth��Ϊ���еĿ��
	 * widthΪ�������Ŀ�ȣ����м����ĵط����ӣ�Ϊ���ı߿���
	 * @param popup ������ʵ��
	 * @param rootdir �����ô����ճ��ģ�
	 * @param showNext  �����ô��ճ�
	 * @return
	 */
	public String toHTML(PopupSelectBean popup, String rootdir, String showNext) {
		// ���������ݱ��Ŀ��
		int width = 0;
		// ��������ı��Ŀ��
		int lockWidth = 0;
		// �������ݵ�header�ַ���
		StringBuilder dataHeader = new StringBuilder("<thead><tr height=\"24\"></tr><tr height=\"24\"></tr></thead>");
		// �����洢ÿ���������������
		StringBuilder dataBody = new StringBuilder("<tbody></tbody>");
		StringBuilder columnBody = new StringBuilder("<tbody></tbody>");

		HashMap<String, Integer> column = new HashMap<String, Integer>();

		// ��ʼ��������Լ�����������Ŀ��,
		// �Լ�����thead
		int fixNumberCol = 0;

		String selectBox = "";
		if ("checkBox".equals(popup.getType()) || "multiSeleToRow".equals(popup.getType())) {
			/** ��ѡ��* */
			selectBox = "checkbox";
		} else if (mainPop != null && mainPop.length() > 0) {
			/** ���õ���ʱ��ѡ��* */
			selectBox = "checkbox";
		} else {
			/** ��ѡ��* */
			selectBox = "radio";
		}

		dataHeader
				.insert(dataHeader.indexOf("</tr>"),
						"<td width=\"20\">"
								+ (selectBox.equals("checkbox") ? "<input type=\"checkbox\" onclick=\"jQuery('input[name=\\\'varKeyId\\\']').attr('checked',this.checked);reloadkeyIds();\"/>"
										: "") + "</td>");
		width += 20 + 11;
		lockWidth += 20 + 11;
		int seq = (pageNo - 1) * pageSize + 1;
		if (seq < 1)
			seq = 1;
		dataHeader.insert(dataHeader.indexOf("</tr>"), "<td width=\"45\"></td>");
		width += 45+11;
		lockWidth += 46+11;
		int fixed = 0;
		for (int i = 0; i < cols.size(); i++) {
			
			String[] col = cols.get(i);
			if (Integer.parseInt(col[1]) <= 0)
				continue;

			if("img".equals(col[2])){
				lineHeight = 85; //�����ͼƬ�ֶΣ��и��Զ���Ϊ100
				String picSize = GlobalsTool.getSysSetting("picSize");
				if(picSize != null && picSize.length() > 0){
					try{
						lineHeight = Integer.parseInt(picSize)+5;
					}catch(Exception e){
						BaseEnv.log.error("ListGrid.toString Error ",e);
					}
					if(lineHeight < 24){
						lineHeight =24;
					}
				}
			}
			
			if (!"hidden".equals(col[3])) {
				width += Integer.parseInt(col[1]) + 11;// �����������Ŀ��
				if (fixed < fixNumberCol) {
					lockWidth += Integer.parseInt(col[1]) + 11;
					fixed++;
				}

				dataHeader.insert(dataHeader.indexOf("</tr>"), "<td df=\"" + col[3] + "\" noHidden=\"" + calls.contains(col[3]) + "\" width=\"" + col[1]
						+ "\">" + col[0] + "</td>");
			}
		}
		/** �Ƿ��зּ�* */
		if (popup.getClassCode() != null && popup.getClassCode().length() > 0) {
			width += 60;
			dataHeader.insert(dataHeader.indexOf("</tr>"), "<td align=\"right\" width=\"60\">" + getNextClass() + "</td>");
		}

		// �������õģԣ�
		int tmpInt = dataHeader.indexOf("<tr height=\"24\"></tr>");
		if (tmpInt != -1) {
			dataHeader.delete(tmpInt, tmpInt + 21);
		}
		// ̧ͷ�������
		// ��ʼ�������ݱ��
		for (int i = 0; i < rows.size(); i++) {
			String[] row = (String[]) rows.get(i);
			dataBody.insert(dataBody.lastIndexOf("</tbody>"), getRow(row, seq + i, popup));
		}
		String hiddenField = "";
		String showField = "";//��ʾ���ֶε����ƣ�����Լ���ʾ���֣��ֶ�֮���У��ָ����ֶ������ã��ָ�
		if (configScope){
			for (int m = 0; this.hiddenField != null && m < this.hiddenField.size(); m++) {
				if(Integer.parseInt(this.hiddenField.get(m)[1])>0)
				hiddenField += this.hiddenField.get(m)[3] + ":" + this.hiddenField.get(m)[1] + ":" + this.hiddenField.get(m)[0] + "|";
			}
			for (String[] col : cols) {
				if (Integer.parseInt(col[1]) > 0)
				{
					showField += col[3]+":"+col[1]+":"+col[0]+"|";
				}
			}

		}
		StringBuilder sb = new StringBuilder("<table hiddenField=\"" + hiddenField + "\" showField=\""+showField+"\" configScope=\"" + configScope
				+ "\" id=\"k_listgrid\" style=\"table-layout:fixed\" border=\"1\" cellspacing=\"0\" width=\"" + width + "\">");
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
		int columnNum = fixNumberCol + 2;

		while ((pos = columnStr.indexOf("<tr", pos)) != -1) {
			for (int i = 0; i < columnNum; i++) {
				pos = columnStr.indexOf("<td", pos) + 1;
			}
			columnStr.delete(columnStr.indexOf("<td", pos), columnStr.indexOf("</tr>", pos));
			pos += 5;
		}
		pos = columnStr.indexOf("width=\"");
		columnStr.replace(pos + 7, columnStr.indexOf("\"", pos + 8), String.valueOf(lockWidth));

		return "<div style=\"z-index:55;width:" + lockWidth + "px;overflow:hidden;position:absolute;top:0;left:0\">" + header.toString()
				+ "</div><div id=\"k_head\" style=\"z-index:50;overflow:hidden;width:100%;position:absolute;top:0;left:0\">" + header.toString()
				+ "</div><div id=\"k_column\" style=\"z-index:45;width:" + lockWidth + "px;position:absolute;background:#fff;overflow:hidden;top:0;left:0\">"
				+ columnStr.toString() + "</div><div id=\"k_data\" style=\"z-index:40;width:100%;overflow:auto;position:absolute;top:0;left:0\">"
				+ sb.toString().replaceAll("<input[\\d\\D]*?/>", "") + "</div>";
	}

	// ����һ������
	private String getRow(String[] data, int rowNo, PopupSelectBean popup) {

		String[] lastCol = data[data.length - 1].split("~");
		String urlSrc = "";
		if (data[3] != null) {
			/** ��ͨ�����¼�* */
			int childCount = Integer.parseInt(data[3]);
			if (childCount > 0) {
				urlSrc += "hasClassBill(true,'" + GlobalsTool.encodeHTML2(data[2]) + "',this)\" ";
			} else {
				urlSrc += "hasClassBill(false,'',this)\" ";
			}
		} else if (this.detailPopup != null
				&& this.detailPopup.length() > 0) {
			/** �൥����ʱ�����ǵ�һ����������Ҫ������¼�* */
			urlSrc += "clickGogoDetail('" + data[1].substring(0, data[1].indexOf("#;#"))+ "',detailPopup,true) \" " ;
		} else if (popup.getHasChild() != null && popup.getHasChild().length() > 0) {
			/** ���õ���ʱ ����һ��* */
			urlSrc += "hasChildBill('" + popup.getHasChild() + "','" + GlobalsTool.encodeHTML2(data[1].substring(0, data[1].length() - 3)) + "','"
					+ popup.getName() + "') \" ";
		} else if (mainPop.length() > 0) {
			/** ���õ���ʱ û����һ��* */
			urlSrc += " isChildBill('" + GlobalsTool.encodeHTML2(data[1].substring(0, data[1].length() - 3).replaceAll("&apos;", "\\\\&apos;").replaceAll("&#92;", "\\\\&#92;")) + "')\" ";
		} else {
			urlSrc += "hasClassBill(false,'',this) \" ";
		}

		String del = "";
		if (lastCol.length > 1) {
			del = lastCol[1];
		}

		String columnInfo = "";
		for (int i = 0; i < cols.size() && i < data.length; i++) {
			if (!cols.get(i)[3].toLowerCase().equals("hidden"))
				columnInfo += " " + cols.get(i)[3] + "=\"" + data[i] + "\"";
		}
		String clickEvent = "";
		clickEvent = " onClick=\"document.getElementsByName('varKeyId')[jQuery(this).index()].checked=!document.getElementsByName('varKeyId')[jQuery(this).index()].checked;reloadkeyIds();\" ";
		StringBuilder sb = new StringBuilder("<tr del=\"" + del + "\" " + columnInfo + " ondblclick=\"" + urlSrc + "\" height=\""+lineHeight+"px\"" + clickEvent + "></tr>");

		String chkValue = data[1];
		//ת������˫���ŵ������ַ�
		
		
		chkValue = chkValue.replaceAll("','", ";");
		if (chkValue.startsWith("\'"))
			chkValue = chkValue.substring(1);
		if (chkValue.endsWith("\'"))
			chkValue = chkValue.substring(0, chkValue.length() - 2);
		
		String compareValue = "";
		if("Seq".equals(fieldName)){
			compareValue = data[5];
			compareValue = compareValue.replaceAll("','", ";");
			if (compareValue.startsWith("\'"))
				compareValue = compareValue.substring(1);
			if (compareValue.endsWith("\'"))
				compareValue = compareValue.substring(0, compareValue.length() - 2);
			
			compareValue = "compV=\""+compareValue+"\"";
		}
		
		// ����Ƕ�ѡ
		String checked = "";
		String subKeyId = "";

		if ("checkBox".equals(popup.getType())) {
			if (listValue != null) {
				for (String str : listValue) {
					if (str.contains(data[1])) {
						checked = "checked";
						break;
					}
				}
			}
		}
		
		if(topPopup != null && topPopup.length() > 0){
			checked = "checked";
		}
		
		if (null != data[3] && !"0".equals(data[3])) {
			subKeyId = "@hasChild:";
		}
		String selectBox = "";
		if ("checkBox".equals(popup.getType()) || "multiSeleToRow".equals(popup.getType())) {
			/** ��ѡ��* */
			selectBox = "checkbox";
		} else if (mainPop != null && mainPop.length() > 0) {
			/** ���õ���ʱ��ѡ��* */
			selectBox = "checkbox";
		} else {
			/** ��ѡ��* */
			selectBox = "radio";
		}

		sb.insert(sb.lastIndexOf("</tr>"), "<td><input type=\"" + selectBox + "\" value=\"" + chkValue + subKeyId
				+ "\" "+compareValue+" name=\"varKeyId\" onclick=\"this.checked=!this.checked\" " + checked + "/></td>");

		sb.insert(sb.lastIndexOf("</tr>"), "<td width=\"45\">" + rowNo + "</td>");
		data[0] = data[0].substring(1, data[0].length());
		String[] varValue = data[0].split("','");
		if (varValue[varValue.length - 1].length() > 0) {
			varValue[varValue.length - 1] = varValue[varValue.length - 1].substring(0, varValue[varValue.length - 1].length() - 1);
		}

		String caption = "";
		String dbClickStr = "";
		for (int j = 0; j < cols.size(); j++) {
			String[] col = cols.get(j);

			if (Integer.parseInt(col[1]) <= 0)
				continue;

			caption = "";
			caption = GlobalsTool.encodeHTML2(varValue[j]);
			dbClickStr = "";

			String align = "left";
			if (col[2].indexOf("call") != -1 )// ��call���
			{
				align = "right";
				if (varValue[j].indexOf(";") != -1 && !varValue[j].endsWith(";"))
					dbClickStr = varValue[j].substring(varValue[j].indexOf(";") + 1).trim() ;
				if(!dbClickStr.toLowerCase().startsWith("javascript:"))
				{
					dbClickStr = "window.open('" + dbClickStr +"','_blank')";
				}
			}else if (col[2].equals("img") && caption != null &&caption.length() > 0)// ��ͼƬ,��ʾͼƬ
			{
				align = "center";	
				if(caption.toLowerCase().indexOf("http")== -1){
					caption = "ReadFile.jpg?tempFile=false&type=PIC&YS=true&tableName=" + col[3].substring(0, col[3].indexOf(".")) + "&fileName=" + caption;
				}
				dbClickStr = "showPIC('" + caption +"')";	
				if(caption.indexOf("%3B")>0){
					caption = caption.substring(0,caption.indexOf("%3B"));
				}				
				if(caption.length() > 0){
					caption = "<img style=\"height:"+(lineHeight-5)+"px;\" src=\""+caption +"\" />";
				}else{
					caption = "";
				}
					
				
			} else if (calls.contains(col[3])) {
				if (varValue[j].indexOf(";") != -1 && !varValue[j].endsWith(";"))
					dbClickStr = col[3] + "('" + varValue[j].substring(varValue[j].indexOf(";") + 1).trim() + "')";
				align = "right";
			}
			if ("float".equals(col[3])) {
				align = "right";
			}

			sb.insert(sb.lastIndexOf("</tr>"), "<td align=\"" + align + "\">"
					+ ((dbClickStr.equals("")) ? caption : ("<span style=\"color:blue;cursor:pointer\" onclick=\"" + dbClickStr + "\">" + caption + "</span>")) + "</td>");
		}
		// nextClass
		caption = varValue[varValue.length - 1];
		dbClickStr = "";
		if (caption.indexOf(";") != -1) {
			dbClickStr = caption.substring(caption.indexOf(";") + 1);
			caption = caption.substring(0, caption.indexOf(";"));
		}

		if (popup.getClassCode() != null && popup.getClassCode().length() > 0) {
			sb.insert(sb.lastIndexOf("</tr>"), "<td align=\"right\">"
					+ ((dbClickStr.equals("")) ? caption : "<span style=\"cursor:pointer;color:blue\" onClick=\"nextClass2('" + dbClickStr + "');\">" + caption
							+ "</span>") + "</td>");
		}

		return sb.toString();
	}


	public String getMainPop() {
		return mainPop;
	}

	public void setMainPop(String mainPop) {
		this.mainPop = mainPop;
	}

	public String getNextClass() {
		return nextClass;
	}

	public void setNextClass(String nextClass) {
		this.nextClass = nextClass;
	}

	public String getStrRadio() {
		return strRadio;
	}

	public void setStrRadio(String strRadio) {
		this.strRadio = strRadio;
	}

	public boolean isSaveParentFlag() {
		return saveParentFlag;
	}

	public void setSaveParentFlag(boolean saveParentFlag) {
		this.saveParentFlag = saveParentFlag;
	}

	public ArrayList getHiddenField() {
		return hiddenField;
	}

	public void setHiddenField(ArrayList hiddenField) {
		this.hiddenField = hiddenField;
	}
}
