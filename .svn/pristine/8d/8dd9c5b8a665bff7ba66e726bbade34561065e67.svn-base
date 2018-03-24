
function addCols(griddata, name, width, type) {
	griddata.cols[griddata.cols.length] = new Cols(name, width, type);
}
function addRows(griddata, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r37, r38, r39, r40) {
	var rowcols = new Array();
	rowcols[0] = r1;
	rowcols[1] = r2;
	rowcols[2] = r3;
	rowcols[3] = r4;
	rowcols[4] = r5;
	rowcols[5] = r6;
	rowcols[6] = r7;
	rowcols[7] = r8;
	rowcols[8] = r9;
	rowcols[9] = r10;
	rowcols[10] = r11;
	rowcols[11] = r12;
	rowcols[12] = r13;
	rowcols[13] = r14;
	rowcols[14] = r15;
	rowcols[15] = r16;
	rowcols[16] = r17;
	rowcols[17] = r18;
	rowcols[18] = r19;
	rowcols[19] = r20;
	rowcols[20] = r21;
	rowcols[21] = r22;
	rowcols[22] = r23;
	rowcols[23] = r24;
	rowcols[24] = r25;
	rowcols[25] = r26;
	rowcols[26] = r27;
	rowcols[27] = r28;
	rowcols[28] = r29;
	rowcols[29] = r30;
	rowcols[30] = r31;
	rowcols[31] = r32;
	rowcols[32] = r33;
	rowcols[33] = r34;
	rowcols[34] = r35;
	rowcols[35] = r36;
	rowcols[36] = r37;
	rowcols[37] = r38;
	rowcols[38] = r39;
	rowcols[39] = r40;
	griddata.rows[griddata.rows.length] = rowcols;
}
function addStats(griddata, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r37, r38, r39, r40) {
	var rowcols = new Array();
	rowcols[0] = r1;
	rowcols[1] = r2;
	rowcols[2] = r3;
	rowcols[3] = r4;
	rowcols[4] = r5;
	rowcols[5] = r6;
	rowcols[6] = r7;
	rowcols[7] = r8;
	rowcols[8] = r9;
	rowcols[9] = r10;
	rowcols[10] = r11;
	rowcols[11] = r12;
	rowcols[12] = r13;
	rowcols[13] = r14;
	rowcols[14] = r15;
	rowcols[15] = r16;
	rowcols[16] = r17;
	rowcols[17] = r18;
	rowcols[18] = r19;
	rowcols[19] = r20;
	rowcols[20] = r21;
	rowcols[21] = r22;
	rowcols[22] = r23;
	rowcols[23] = r24;
	rowcols[24] = r25;
	rowcols[25] = r26;
	rowcols[26] = r27;
	rowcols[27] = r28;
	rowcols[28] = r29;
	rowcols[29] = r30;
	rowcols[30] = r31;
	rowcols[31] = r32;
	rowcols[32] = r33;
	rowcols[33] = r34;
	rowcols[34] = r35;
	rowcols[35] = r36;
	rowcols[36] = r37;
	rowcols[37] = r38;
	rowcols[38] = r39;
	rowcols[39] = r40;
	griddata.stats = rowcols;
}
function gridData() {
	this.cols = new Array();
	this.rows = new Array();
	this.stats = null;
}

function TableList(tableId) {
	this.table = tableId; //
	this.table.createTHead();
	this.table.tHead.insertRow(0);
	this.table.sortCol = 0;
	this.table.isDesc = true;
	
	var row = this.table.tHead.rows[0];
	var newTh = row.insertCell(0);
	newTh.className = "listheade";
	newTh.innerHTML = "&nbsp;" + "<IMG src=\"/style/images/down.jpg\" border=0/>";
	newTh.width = "30px";
	newTh.align = "center";
	newTh.style.cursor = "pointer";
	newTh.table = this.table;
	newTh.colId = 0;
	newTh.type = "int";
	newTh.onclick = function () {
		sortTable(this.table, this.colId, this.type);
	};
}

function Cols(name, width, type) {
	this.name = name;
	this.width = width;
	this.type = type;
}

TableList.prototype.addCols = function (col, colId) {
	var row = this.table.tHead.rows[0];
	row.className = "listhead";
	var newTh = row.insertCell(colId);
	newTh.innerHTML = col.name;
	newTh.width = col.width + "px";
	newTh.align = "center";
	newTh.table = this.table;
	newTh.colId = colId;
	newTh.type = col.type;
	if (col.type != "nosort") {
		newTh.style.cursor = "pointer";
		newTh.onclick = function () {
			sortTable(this.table, this.colId, this.type);
		};
		if (colId == 1) {
			newTh.className = "listhead30";
		}
	}
};
/**
 * 
 * 
 */
TableList.prototype.addRows = function (cols, rowcols, rowId) {
	var newTr = this.table.insertRow(rowId);
	newTr.onmouseover = function () {
		if (this.style.background == "#E6F2FF") {
			return;
		}
		this.style.background = "#E7FCA9";
		this.style.color = "#000000";
	};
	newTr.onmouseout = function () {
		if (this.style.background == "#E6F2FF") {
			return;
		}
		this.style.background = (this.sectionRowIndex % 2) ? "#efefef" : "#ffffff";
		this.style.color = "#000000";
	};
		
	//newTr.onmouseover=function(){this.style.background="#E7FCA9";}//ԭɫֵ#ffffcc(2008/11/29 gongע)
        //newTr.onmouseout=function(){this.style.background="";}	
	if (this.dbClick) {
		newTr.ondblclick = function () {
			onDblClick();
		};
	}
	var sTd = newTr.insertCell(0);
	sTd.className = "listheadonerow";
	sTd.align = "center";
	sTd.width = "10px";
	sTd.innerHTML = rowId;
	for (i = 0; i < cols.length; i++) {
		var newTd = newTr.insertCell(i + 1);
	   
	   //if (i == 0){
		//newTd.className = "listheadonerow10";
		//}
		newTd.width = cols[i].width + "px";
		if ((cols[i].type == "int") || (cols[i].type == "float")) {
			newTd.align = "right";
		} else {
			if ((cols[i].type == "date") || (cols[i].type == "other") || (cols[i].type == "nosort")) {
				newTd.align = "center";
			} else {
				newTd.align = "left";
			}
		}
		newTd.innerHTML = rowcols[i]+'&nbsp;';
	}
	if (typeof (rowcols[cols.length]) != "undefined") {
		if (rowcols[cols.length].length > 0) {
			newTr.ondblclick = function () {
				location.href = rowcols[cols.length];
			};
		}
	}else{
	//主要针对分类权限的弹出框，双击显示下级或提交数据
		var endstr=rowcols[cols.length-1];
		
		if (endstr.indexOf("<a") >=0) {
			newTr.ondblclick = function () {
				form.action = endstr.substring(endstr.indexOf("'")+1,endstr.lastIndexOf("'"));
				form.submit();
			};
		}else{
			newTr.ondblclick = function () {
				var cb = document.getElementsByName("keyId")[rowId-1];	
				window.parent.returnValue = cb.value;
				window.close();
			};
		}
	}
};
TableList.prototype.addStats = function (cols, stats) {
	var newTr = this.table.insertRow();
	newTr.className = "listRange_list_statistic";
	var sTd = newTr.insertCell();
	sTd.align = "center";
	sTd.colSpan = "2";
	sTd.innerHTML = "TOTAL";
	for (i = 1; i < cols.length; i++) {
		var newTd = newTr.insertCell();
		if ((cols[i].type == "int") || (cols[i].type == "float")) {
			newTd.align = "right";
		}
		if (stats[i] != null && stats[i] != "") {
			newTd.innerHTML = stats[i];
		} else {
			newTd.innerHTML = "&nbsp;";
		}
	}
};


            //תеֶתΪͣString,int,float
function convert(sValue, sDataType) {
	if (sValue == null) {
		return "";
	}
	var re = /,/g;
	sValue = sValue.replace(re,'') ;
	switch (sDataType) {
	  case "int":
		return parseInt(sValue);
	  case "float":
		return parseFloat(sValue);
	  case "date":	     
         //return new Date(Date.parse(sValue));
		return new Date(sValue.substring(0, 4), sValue.substring(5, 7), sValue.substring(8));
	  default:
		return sValue.toString();
	}
}
            
            //iColʾsDataTypeʾе
function generateCompareTRs(iCol, sDataType) {
	return function compareTRs(oTR1, oTR2) {
		var vValue1;
		var vValue2;
		if (oTR1.cells[iCol].firstChild.nodeName == "A") {
			vValue1 = convert(oTR1.cells[iCol].firstChild.firstChild.nodeValue, sDataType);
			if(oTR2.cells[iCol].firstChild.firstChild==null){
				vValue2 = "&nbsp;" ;
			}else{
				vValue2 = convert(oTR2.cells[iCol].firstChild.firstChild.nodeValue, sDataType);
			}
		} else {
			vValue1 = convert(oTR1.cells[iCol].firstChild.nodeValue, sDataType);
			vValue2 = convert(oTR2.cells[iCol].firstChild.nodeValue, sDataType);
		}
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
            
            //򷽷
function sortTable(sTable, iCol, sDataType) {
		//	if(!dragFlag){
	var oTable = sTable;
	var oTBody = oTable.tBodies[0];
	var colDataRows = oTBody.rows;
	var aTRs = new Array;
                
                //з
	var rowCount = colDataRows.length;
	if (oTable.hasStat=="true") {
		rowCount = rowCount - 1;
	}
	if (oTable.isStatsAll=="true"){
		rowCount = rowCount - 1 ;
	}
	for (var i = 0; i < rowCount; i++) {
		aTRs[i] = colDataRows[i];
	}
                //жһǷҪͬǵĻֱʹreverse()
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
					//ȡԭеͼƬ
		var row = oTable.rows[0];
		var cel = row.cells[oTable.sortCol];
		if(cel != null)
		cel.innerHTML = cel.innerHTML.substring(0, cel.innerHTML.indexOf("<IMG")); 
					
					//¼һ
		oTable.sortCol = iCol;
		oTable.isDesc = true;
		var cel = row.cells[oTable.sortCol];
		cel.innerHTML = cel.innerHTML + "<IMG src=\"/style/images/down.jpg\" border=0/>";
	}
	var oFragment = document.createDocumentFragment();
	for (var i = 0; i < aTRs.length; i++) {
		oFragment.appendChild(aTRs[i]);
	}
	if(oTable.hasStat=="true" && oTable.isStatsAll=="true") {
		oFragment.appendChild(colDataRows[colDataRows.length - 2]);
		oFragment.appendChild(colDataRows[colDataRows.length - 1]);
	}else{
		if(oTable.hasStat=="true" || oTable.isStatsAll=="true"){
			oFragment.appendChild(colDataRows[colDataRows.length - 1]);
		}
	}
	oTBody.appendChild(oFragment);     
          //  }else{
         //   	dragFlag=false;
         //   }           
}
/**
 * ʼб
 */
function initTableList(gridName, gridData, dbClick) {
	if (gridData.cols.length == 0) {
		return;
	}
	tableList = new TableList(gridName);
	tableList.dbClick = dbClick;
	alllength = 0;
	for (i = 0; i < gridData.cols.length; i++) {
		tableList.addCols(gridData.cols[i], i + 1);
		alllength += Number(gridData.cols[i].width);
	}
	tableList.table.style.width = alllength;
	for (j = 0; j < gridData.rows.length; j++) {
		tableList.addRows(gridData.cols, gridData.rows[j], j + 1);
	}
	if (gridData.stats != null) {
		tableList.table.hasStat = true;
		tableList.addStats(gridData.cols, gridData.stats);
	}
}

