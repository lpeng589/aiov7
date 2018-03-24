function hasClassBill(child, returnValue,trobj) {
	returnValue = revertTextCode2(returnValue) ;
	if (child) {
		form.parentCode.value = returnValue;
		form.nextClass.value = "next" ;
		form.submit();
	} else {
		if(typeof(popupType)!="undefined" && popupType=="salesPopup"){
			selectItem(returnValue) ; //这是以前flash的方法
		}else{
			//清除所有行的选择项，设置本行为选择，
			jQuery("input[name='varKeyId']").attr("checked",false);
			$(document.getElementsByName('varKeyId')[jQuery(trobj).index()]).attr("checked",true);
			closeSubmit();
		}
	}
}

function isChildBill(returnValue) {
	exeQuotepopup(mainId + ";childKeyId;" + returnValue);
	window.parent.jQuery.close(pupupWin);
}

//目录树回调函数
function doChangeParent(parentCode,name,ttableName){
	form.pageNo.value=0 ;
	form.parentCode.value=parentCode;
	form.submit();
}

function hasChildBill(varChildPop, varMainId, varSelectName) {
	form.action = "/UserFunctionAction.do?operation=22&src=menu&selectName=" + varChildPop + "&mainId=" + varMainId + "&mainPop=" + varSelectName;
	
	form.submit();
}
//多单引用时，进入明细弹出窗
function hasDetailBill(varChildPop, varMainId) {
	form.action = "/UserFunctionAction.do?operation=22&src=menu&selectName=" + varChildPop + "&topId=" + varMainId + "&toDetail=true&autoCommit=false";
	form.submit();
}
//点击查看明细
function gotoDetail(varChildPop,autoCommit){ 
	form.action = "/UserFunctionAction.do?operation=22&src=menu&selectName=" + varChildPop + "&toDetail=true&autoCommit="+autoCommit;
	ids= "";
	$("input[name='varKeyId']:checked",document).each(function(){
		if($(this).val().length>0){
			ids += $(this).val().split("#;#")[0]+";";
		}
	});
	if(ids == ""){
		alert("请选择至少一行数据");
		return;
	}
	
	form.topId.value=ids;
	form.submit();
}
function backTop()
{
	form.toDetail.value="" ;	
	form.submit();
}
//点击查看明细
function clickGogoDetail(ids,varChildPop,autoCommit){ 
	form.action = "/UserFunctionAction.do?operation=22&src=menu&selectName=" + varChildPop + "&toDetail=true&autoCommit="+autoCommit;	
	form.topId.value=ids+";";
	form.submit();
}
function convert(sValue, sDataType) {
	if (sValue == null) {
		return "";
	}
	switch (sDataType) {
	  case "int":
		return parseInt(sValue);
	  case "float":
		return parseFloat(sValue);
	  case "date":	     
		return new Date(sValue.substring(0, 4), sValue.substring(5, 7), sValue.substring(8));
	  default:
		return sValue.toString();
	}
}

function generateCompareTRs(iCol, sDataType) {
	return function compareTRs(oTR1, oTR2) {
		var vValue1 = convert(oTR1.cells[iCol].firstChild.nodeValue, sDataType);
		var vValue2 = convert(oTR2.cells[iCol].firstChild.nodeValue, sDataType);
		if (vValue1 < vValue2) {
			return -1;
		} else {
			if (vValue1 > vValue2) {
				return 1;
			} else {
				return 0;
			}
		}
	};
}

function sortTable(iCol, sDataType) {
	var oTable = document.getElementById("tblSort");
	var oTBody = oTable.tBodies[0];
	var colDataRows = oTBody.rows;
	var aTRs = new Array;
	for (var i = 0; i < colDataRows.length; i++) {
		aTRs[i] = colDataRows[i];
	}
	if (oTable.sortCol == iCol) {
		aTRs.reverse();
		oTable.isDesc = !oTable.isDesc;
		var row = oTable.rows[0];
		var cel = row.cells[iCol];
		if (oTable.isDesc) {
			cel.innerHTML = cel.innerHTML.substring(0, cel.innerHTML.indexOf("<IMG")) + "<IMG src=\"/style/images/down.jpg\" width=5 height=9 border=0/>";
		} else {
			cel.innerHTML = cel.innerHTML.substring(0, cel.innerHTML.indexOf("<IMG")) + "<IMG src=\"/style/images/up.jpg\" width=5 height=9 border=0/>";
		}
	} else {
		aTRs.sort(generateCompareTRs(iCol, sDataType));
		var row = oTable.rows[0];
		var cel = row.cells[oTable.sortCol];
		cel.innerHTML = cel.innerHTML.substring(0, cel.innerHTML.indexOf("<IMG"));
		oTable.sortCol = iCol;
		oTable.isDesc = true;
		var cel = row.cells[oTable.sortCol];
		cel.innerHTML = cel.innerHTML + "<IMG src=\"/style/images/down.jpg\" border=0/>";
	}
	var oFragment = document.createDocumentFragment();
	for (var i = 0; i < aTRs.length; i++) {
		oFragment.appendChild(aTRs[i]);
	}
	oTBody.appendChild(oFragment);
}
function initTableList() {
	var oTable = document.getElementById("tblSort");
	oTable.sortCol = 0;
	oTable.isDesc = true;
}

function popupClick(obj){
	for (var i = 0; i < document.getElementById("tblSort").rows.length; i++) {
	  	var idd=document.getElementById("tblSort").rows(i);
    	if (idd.style.background.toUpperCase( ) == "#E7FCA9"){ 
     		idd.style.background = "";
     		break;
     	}
   	}
  	obj.style.background = "#E7FCA9";
}
