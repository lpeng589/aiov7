// JavaScript Document
var path = "/js/";
//document.writeln("<style type=\"text/css\">");
//document.writeln(".up{");
//document.writeln("	background-image:url(\"" + path + "img/arrow_up.gif\");");
//document.writeln("	background-position:center center;");
//document.writeln("	background-repeat:no-repeat;");
//document.writeln("	border:none;");
//document.writeln("}");
//document.writeln(".down{");
//document.writeln("	background-image:url(\"" + path + "img/arrow_down.gif\");");
//document.writeln("	background-position:center center;");
//document.writeln("	background-repeat:no-repeat;");
//document.writeln("	border:none;");
//document.writeln("}");
//document.writeln(".time{");
//document.writeln("	border:1px solid #7F9DB9;");
//document.writeln("}");
//document.writeln("</style>");
document.writeln("<span id=\"CalendarAreaLayer\"></span>");
document.onclick = hiddenCalendar;

var todayDate = new Date();	//得到当前的日期	
var currentYear = todayDate.getFullYear();
var currentMonth = todayDate.getMonth()+1;
var currentDate = todayDate.getDate();
var browser = getObject();
/*
 * @ 根浏览器不同,取不同的Document对象
 */
function getObject(){
	var doc = null;
	if(document.all){//IE
		doc = document.all;
	}else if(document.layers){//NS4
		doc = document.layers;
	}else if (document.getElementById && !document.all){//NS6
		doc = document.getElementById;
	}else{
		doc = null;
	}
	return doc;
}

/*
 * @ 初始化日历控件
 */
function initCalendar(panelId,imgId,fieldId,isShowTime){
	var str = "<iframe id=\"CalendarPanel\" name=\"CalendarPanel\" style=\"display:none;position:absolute;z-index:1000;width:146px;height:200px;\" hspace=\"0\" vspace=\"0\" marginheight=\"0\" marginwidth=\"0\" frameborder=\"0\" scrolling=\"no\" src=\"" + path + "CalendarPanel.html?panelId=" + panelId + "&imgId=" + imgId + "&fieldId=" + fieldId + "&isShowTime=" + isShowTime + "\"></iframe>";
	window.CalendarAreaLayer.innerHTML = str;		
	position(panelId,imgId,fieldId,isShowTime);
}

/*
 * @ 定位日历控件
 */
function position(panelId,imgId,fieldId,isShowTime){
	var sTop = document.body.scrollTop;
	var sLeft = document.body.scrollLeft;
	var bTop = document.body.offsetTop;
	var bLeft = document.body.offsetLeft;
	var imgW = document.all(imgId).width;
	var imgH = document.all(imgId).height;	
	var divWStr = new String(browser("CalendarPanel").style.width);
	var divHStr = new String(browser("CalendarPanel").style.height);
	var divW = parseInt(divWStr.substring(0,divWStr.length - 2));
	var divH = parseInt(divHStr.substring(0,divHStr.length - 2));
	var cWidth = document.body.clientWidth;
	var cHeight = document.body.clientHeight;	
	var eTop = 0;
	var eLeft = 0;
	var posX = 0;
	var posY = 0;
	var oI = browser(imgId);
	while(oI && oI.tagName != "BODY"){
		eTop += oI.offsetTop;
		eLeft += oI.offsetLeft;
		oI = oI.offsetParent;
	}
	posY = (cHeight - (eTop - sTop) - imgH >= divH) ? eTop + imgH : eTop - divH;
	posX = (cWidth - (eLeft - sLeft) >= divW) ? eLeft : eLeft + imgW - divW;	
	posY = posY + bTop;
	posX = posX + bLeft;
	if (posY <=0){
		posY = eTop + bTop + imgH ;
	}
	browser("CalendarPanel").style.top = posY + "px";
	browser("CalendarPanel").style.left = posX + "px";
	browser("CalendarPanel").style.display = "block";
}

/*
 * @ 隐藏日历控件
 */
function hiddenCalendar(){
	try{
		browser("CalendarPanel").style.display = "none";
	}catch(e){
		//
	}
}

/*
 * @ 检测数值范围
 *
function checkNumberLimit(number,maxNumber,fieldName,oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName,position){
	var pattern = /^[0-9]{1,2}$/;
	var isTrue = pattern.test(number);
	if (!isTrue){
		document.all(fieldName).value = 00;		
	}else{
		if (parseInt(number,10)<0){
			document.all(fieldName) = parseInt(maxNumber,10) - 1;
		}else if (parseInt(number,10) >= parseInt(maxNumber,10)){
			document.all(fieldName).value = 00;
		}
	}
	var ofnv = document.all(oFieldName).value;
	var ohnv = document.all(oHourFieldName).value;
	var omnv = document.all(oMinuteFieldName).value;
	var osnv = document.all(oSecondFieldName).value;
	var s = ofnv.split(" ");
	var pos = getClickPosition(position);
	var date;
	var hour = 0;
	var minute = 0;
	var second = 0;
	var datetimeArray;
	var timeStr;
	if (ofnv != "" || ofnv.length>0){
		date = s[0];		
		if (s[1].indexOf(":") != -1){
			timeStr = s[1].split(":");
			hour = timeStr[0];
			minute = timeStr[1];
			second = timeStr[2];			
		}
	}
	if (hour < 10)hour = "0" + parseInt(hour,10);
	if (minute < 10)minute = "0" + parseInt(minute,10);
	if (second < 10)second = "0" + parseInt(second,10);
	if (ohnv < 10)ohnv = "0" + parseInt(ohnv,10);
	if (omnv < 10)omnv = "0" + parseInt(omnv,10);
	if (osnv < 10)osnv = "0" + parseInt(osnv,10);
	if (currentMonth < 10)currentMonth = "0" + parseInt(currentMonth,10);
	if (currentDate < 10)currentDate = "0" + parseInt(currentDate,10);
	if (s[0] == "" || s[0] == null)s[0] = currentYear +""+ currentMonth +""+ currentDate;	
	if (pos == "H")document.all(oFieldName).value = s[0] + " " + ohnv +":"+ minute +":"+ second;
	if (pos == "M")document.all(oFieldName).value = s[0] + " " + hour +":"+ omnv +":"+ second;
	if (pos == "S")document.all(oFieldName).value = s[0] + " " + hour +":"+ minute +":"+ osnv;
	if (pos == null)document.all(oFieldName).value = s[0] + " " + ohnv +":"+ omnv +":"+ osnv;
	getTimeFromDatetime(oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName);
}
	
 *
 *相应的字段值加1
 *
function add(number,maxNumber,fieldName,oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName){	
	var val = 0;
	var sum = 0;
	sum = parseInt(number,10) + 1;

	if (parseInt(sum,10) <= 0){
		val = parseInt(maxNumber,10) - 1;
	}else if (parseInt(sum,10) >= parseInt(maxNumber,10)){
			val = 00;
	}else{
		val = parseInt(sum,10);
	}
	document.all(fieldName).value = val;
	var ofnv = document.all(oFieldName).value;
	var ohnv = document.all(oHourFieldName).value;
	var omnv = document.all(oMinuteFieldName).value;
	var osnv = document.all(oSecondFieldName).value;
	var s = ofnv.split(" ");	
	if (ohnv < 10)ohnv = "0" + parseInt(ohnv,10);
	if (omnv < 10)omnv = "0" + parseInt(omnv,10);
	if (osnv < 10)osnv = "0" + parseInt(osnv,10);
	if (currentMonth < 10)currentMonth = "0" + parseInt(currentMonth,10);
	if (currentDate < 10)currentDate = "0" + parseInt(currentDate,10);
	if (s[0] == "" || s[0] == null)s[0] = currentYear +""+ (currentMonth+1) +""+ currentDate;
	document.all(oFieldName).value = s[0] + " " + ohnv +":"+ omnv +":"+ osnv;
	getTimeFromDatetime(oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName);
}

 *
 *相应的字段值减1
 *
function minus(number,maxNumber,fieldName,oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName){
	var val = 0;
	var sum = 0;
	sum = parseInt(number,10) - 1;
	
	if (parseInt(sum,10) <= 0){
		val = parseInt(maxNumber,10) - 1;
	}else if (parseInt(sum,10) >= parseInt(maxNumber,10)){
			val = 00;
	}else{
		val = parseInt(sum,10);
	}	
	document.all(fieldName).value = val;
	var ofnv = document.all(oFieldName).value;
	var ohnv = document.all(oHourFieldName).value;
	var omnv = document.all(oMinuteFieldName).value;
	var osnv = document.all(oSecondFieldName).value;
	var s = ofnv.split(" ");
	if (ohnv < 10)ohnv = "0" + parseInt(ohnv,10);
	if (omnv < 10)omnv = "0" + parseInt(omnv,10);
	if (osnv < 10)osnv = "0" + parseInt(osnv,10);
	if (currentMonth < 10)currentMonth = "0" + parseInt(currentMonth,10);
	if (currentDate < 10)currentDate = "0" + parseInt(currentDate,10);
	if (s[0] == "" || s[0] == null)s[0] = currentYear +""+ (currentMonth+1) +""+ currentDate;
	document.all(oFieldName).value = s[0] + " " + ohnv +":"+ omnv +":"+ osnv;
	getTimeFromDatetime(oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName);
}

 *
 * @ 显示时间
 * @ 前三个参数为任意参数,最后一个参数是指日期显示的文本框
 * 
function time(oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName){
	document.writeln("<table width=\"100%\"  border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
	document.writeln("  <tr>");
	document.writeln("	<td rowspan=\"2\"><input name=\"" + oHourFieldName + "\" class=\"time\" type=\"text\" id=\"" + oHourFieldName + "\" style=\"width:25px;height:20px;\" value=\"00\" maxlength=\"2\" onBlur=\"javascript:checkNumberLimit(document.all('" + oHourFieldName + "').value,24,'" + oHourFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"',0);\"></td>");
	document.writeln("	<td align=\"center\" valign=\"bottom\"><input name=\"H_UP\" type=\"button\" class=\"up\" id=\"H_UP\" style=\"width:18px;height:8px;\" value=\"\" onClick=\"javascript:add(document.all('" + oHourFieldName + "').value,24,'" + oHourFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"');\" onMouseDown=\"javascript:oIntervalId=setInterval(\'add(document.all(\\'" + oHourFieldName + "\\').value,24,\\'" + oHourFieldName + "\\',\\'"+ oHourFieldName +"\\',\\'"+ oMinuteFieldName +"\\',\\'"+ oSecondFieldName +"\\',\\'"+ oFieldName +"\\')\',200);\" onMouseUp=\"javascript:clearInterval(oIntervalId);\" onMouseOut=\"javascript:try{clearInterval(oIntervalId);}catch(e){}\"></td>");
	document.writeln("	<td rowspan=\"2\"><input name=\"" + oMinuteFieldName + "\" class=\"time\" type=\"text\" id=\"" + oMinuteFieldName + "\" style=\"width:25px;height:20px;\" value=\"00\" size=\"3\" maxlength=\"2\" onBlur=\"javascript:checkNumberLimit(document.all('" + oMinuteFieldName + "').value,60,'" + oMinuteFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"',1);\"></td>");
	document.writeln("	<td align=\"center\" valign=\"bottom\"><input name=\"M_UP\" type=\"button\" class=\"up\" id=\"M_UP\" style=\"width:18px;height:8px;\" value=\"\" onClick=\"javascript:add(document.all('" + oMinuteFieldName + "').value,60,'" + oMinuteFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"');\" onMouseDown=\"javascript:oIntervalId=setInterval(\'add(document.all(\\'" + oMinuteFieldName + "\\').value,60,\\'" + oMinuteFieldName + "\\',\\'"+ oHourFieldName +"\\',\\'"+ oMinuteFieldName +"\\',\\'"+ oSecondFieldName +"\\',\\'"+ oFieldName +"\\')\',200);\" onMouseUp=\"javascript:clearInterval(oIntervalId);\" onMouseOut=\"javascript:try{clearInterval(oIntervalId);}catch(e){}\"></td>");
	document.writeln("	<td rowspan=\"2\"><input name=\"" + oSecondFieldName + "\" class=\"time\" type=\"text\" id=\"" + oSecondFieldName + "\" style=\"width:25px;height:20px;\" value=\"00\" size=\"3\" maxlength=\"2\" onBlur=\"javascript:checkNumberLimit(document.all('" + oSecondFieldName + "').value,60,'" + oSecondFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"',2);\"></td>");
	document.writeln("	<td align=\"center\" valign=\"bottom\"><input name=\"S_UP\" type=\"button\" class=\"up\" id=\"S_UP\" style=\"width:18px;height:8px;\" value=\"\" onClick=\"javascript:add(document.all('" + oSecondFieldName + "').value,60,'" + oSecondFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"');\" onMouseDown=\"javascript:oIntervalId=setInterval(\'add(document.all(\\'" + oSecondFieldName + "\\').value,60,\\'" + oSecondFieldName + "\\',\\'"+ oHourFieldName +"\\',\\'"+ oMinuteFieldName +"\\',\\'"+ oSecondFieldName +"\\',\\'"+ oFieldName +"\\')\',200);\" onMouseUp=\"javascript:clearInterval(oIntervalId);\" onMouseOut=\"javascript:try{clearInterval(oIntervalId);}catch(e){}\"></td>");
	document.writeln("  </tr>");
	document.writeln("  <tr>");
	document.writeln("	<td align=\"center\" valign=\"top\"><input name=\"H_DOWN\" type=\"button\" class=\"down\" id=\"H_DOWN\" style=\"width:18px;height:8px;\" onClick=\"javascript:minus(document.all('" + oHourFieldName + "').value,24,'" + oHourFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"');\" onMouseDown=\"javascript:oIntervalId=setInterval(\'minus(document.all(\\'" + oHourFieldName + "\\').value,24,\\'" + oHourFieldName + "\\',\\'"+ oHourFieldName +"\\',\\'"+ oMinuteFieldName +"\\',\\'"+ oSecondFieldName +"\\',\\'"+ oFieldName +"\\')\',200);\" onMouseUp=\"javascript:clearInterval(oIntervalId);\" onMouseOut=\"javascript:try{clearInterval(oIntervalId);}catch(e){}\"></td>");
	document.writeln("	<td align=\"center\" valign=\"top\"><input name=\"M_DOWN\" type=\"button\" class=\"down\" id=\"M_DOWN\" style=\"width:18px;height:8px;\" value=\"\" onClick=\"javascript:minus(document.all('" + oMinuteFieldName + "').value,60,'" + oMinuteFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"');\" onMouseDown=\"javascript:oIntervalId=setInterval(\'minus(document.all(\\'" + oMinuteFieldName + "\\').value,60,\\'" + oMinuteFieldName + "\\',\\'"+ oHourFieldName +"\\',\\'"+ oMinuteFieldName +"\\',\\'"+ oSecondFieldName +"\\',\\'"+ oFieldName +"\\')\',200);\" onMouseUp=\"javascript:clearInterval(oIntervalId);\" onMouseOut=\"javascript:try{clearInterval(oIntervalId);}catch(e){}\"></td>");
	document.writeln("	<td align=\"center\" valign=\"top\"><input name=\"S_DOWN\" type=\"button\" class=\"down\" id=\"S_DOWN\" style=\"width:18px;height:8px;\" value=\"\" onClick=\"javascript:minus(document.all('" + oSecondFieldName + "').value,60,'" + oSecondFieldName + "','"+ oHourFieldName +"','"+ oMinuteFieldName +"','"+ oSecondFieldName +"','"+ oFieldName +"');\" onMouseDown=\"javascript:oIntervalId=setInterval(\'minus(document.all(\\'" + oSecondFieldName + "\\').value,60,\\'" + oSecondFieldName + "\\',\\'"+ oHourFieldName +"\\',\\'"+ oMinuteFieldName +"\\',\\'"+ oSecondFieldName +"\\',\\'"+ oFieldName +"\\')\',200);\" onMouseUp=\"javascript:clearInterval(oIntervalId);\" onMouseOut=\"javascript:try{clearInterval(oIntervalId);}catch(e){}\"></td>");
	document.writeln("  </tr>");
	document.writeln("</table>");
	getTimeFromDatetime(oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName);
}

 *
 * @ 从Datetime字段里取出时间(时/分/秒)
 * 
function getTimeFromDatetime(oHourFieldName,oMinuteFieldName,oSecondFieldName,oFieldName){
	var datetime = document.all(oFieldName).value;
	var date,hour,minute,second;
	var datetimeArray;
	var timeStr;
	if (datetime != "" || datetime.length>0){
		datetimeArray = datetime.split(" ");
		date = datetimeArray[0];		
		if (datetimeArray[1].indexOf(":") != -1){
			timeStr = datetimeArray[1].split(":");
			hour = timeStr[0];
			minute = timeStr[1];
			second = timeStr[2];
			document.all(oHourFieldName).value = hour;
			document.all(oMinuteFieldName).value = minute;
			document.all(oSecondFieldName).value = second;
		}
	}
}

 *
 * @ 清空日期
 * 
function clean(target){
	document.all(target).value = "";
}

/*
 * @ 如果没有选择日期禁止提交
 */
function disableSubmit(str){	
	var pattern = new RegExp("^[0-9]{8}");
	if (pattern.test(str)){
		return true;
	}else{
		//alert("请选择日期");
		return false;
	}
}

/*
 * @ 得到鼠标操作焦点事件
 *
function getClickPosition(pos){
	switch(pos){
		case 0:
			return "H";
			break;
		case 1:
			return "M";
			break;
		case 2:
			return "S";
			break;
		default:
			return null;
			break;
	}
}
 */