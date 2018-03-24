//此对象功能是用来把原来TABLE转换成可锁定行列的TABLE
//使用了右键弹出框功能，并调用了服务器的favourstyle这个SERVLET
//锁定列数使用了lockColumn这个参数
//整个对象的line均从0开始计算
function FixTable(TableID, FixColumnNumber, width, height) { 
	this.TableID = TableID;//锁定的表的ID
	this.FixColumnNumber = FixColumnNumber;//锁定的列数，会从数据库加载。
	$.ajax({
	   type: "POST",
	   url: "favourstyle",
   data: "OPERATE=GET&formId="+this.TableID+"&property=lockColumn",
   context:this,
   async: false,
   dataType:"xml",
   success: function(data){
   			if($(data).find("lockColumn").length > 0)
			this.FixColumnNumber=$(data).find("lockColumn").text();
	   }
	});
	//加载的宽度
	this.width = width;
	//保留 
	this.lockWidth = true;
	//保留
	this.lockHeight = true;
	//窗体高度
	this.height = height;
	
	
	//定义changeFix方法指向
	this.changeFix = changeFix;
	this.deleteRow = deleteRow;
	this.addRow = addRow;
	this.addRow2 = addRow2;
	
	this.copyRow = copyRow;
	this.moveRow = moveRow;
	
	this.lockNum = 999;
	this.init = init;
	this.setCols = setCols;
	
	var cols;
	//左上角窗体
	var fixTable;
	//数据窗体
	var dataTable;
	//左边TABLE
	var columnTable;
	//台头TABLE
	var headTable;
	this.init();
}

function setCols(cols)
{
	this.cols = cols;
}
//新增一行
function addRow() {
	addRowClick2(this.dataTable.attr("id"),-1);
	
	var	pos = this.columnTable[0].rows.length - 1;
	var newTr = this.columnTable[0].insertRow(pos);
	$(newTr).css("height",$(this.dataTable[0].rows[pos]).css("height"));
	for (var i = 0; i < this.FixColumnNumber; i++) {
		var _unit = $(this.dataTable[0].rows[pos].cells[i]);
		var _cell = $("<td>");
		_cell.attr("width", _unit.attr("width"));
		_unit.after(_cell);
		$(newTr).append(_unit);
	}
	$(this.dataTable[0].rows[pos]).find("td").each(function(){$(this).css("height",$(this).height());}); 
	$(this.columnTable[0].rows[pos]).find("td").each(function(){$(this).css("height",$(this).height());}); 
}

//在第几行新加一行,line从0开始
function addRow2(line) {
	addRowClick2(this.dataTable.attr("id"),line);
	var pos = line;
	var newTr = this.columnTable[0].insertRow(pos);
	$(newTr).css("height",$(this.dataTable[0].rows[pos]).css("height"));
	for (var i = 0; i < this.FixColumnNumber; i++) {
		var _unit = $(this.dataTable[0].rows[pos].cells[i]);
		var _cell = $("<td>");
		_cell.attr("width", _unit.attr("width"));
		_unit.after(_cell);
		$(newTr).append(_unit);
	}
	$(this.dataTable[0].rows[pos]).find("td").each(function(){$(this).css("height",$(this).height());});
	$(this.columnTable[0].rows[pos]).find("td").each(function(){$(this).css("height",$(this).height());});
	var _unit = this.columnTable.find("tbody tr td:nth-child(1)");
	for (var i = 0; i < _unit.size() - 1; i++) {
		if(_unit.get(i).innerHTML.indexOf("<")!=-1)
				_unit.get(i).innerHTML = (i + 1) + _unit.get(i).innerHTML.substr(_unit.get(i).innerHTML.indexOf("<"));
		else
			_unit.get(i).innerHTML = (i + 1);
	}
}
//复制
function copyRow(lineFrom,lineTo)
{
	if(lineTo== -1){
		lineTo = this.columnTable[0].rows.length - 3;
	}
	var rowClone = this.columnTable.find("tbody tr:eq("+lineFrom+")").clone();
	this.columnTable.find("tbody tr:eq("+lineTo+")").before(rowClone);
	rowClone = this.dataTable.find("tbody tr:eq("+lineFrom+")").clone();
	this.dataTable.find("tbody tr:eq("+lineTo+")").before(rowClone);
	$(this.dataTable[0].rows[lineTo]).find("td").each(function(){$(this).css("height",$(this).height());});
	$(this.columnTable[0].rows[lineTo]).find("td").each(function(){$(this).css("height",$(this).height());});
	
	gridTable = document.getElementById(this.TableID);
	if(typeof(initSelect)!="undefined"){
		initSelect(parseInt(gridTable.rows.length-gridTable.tHead.rows.length));
	}
	if(typeof(initCalculate)!="undefined"){
		initCalculate();
	}
	if(typeof(initCountCal)!="undefined"){
		initCountCal();
	}

	if(typeof(execStat)!="undefined"){
		execStat();
	}
	
	var _unit = this.columnTable.find("tbody tr td:nth-child(1)");
	for (var i = 0; i < _unit.size() - 1; i++) {
		if(_unit.get(i).innerHTML.indexOf("<")!=-1)
				_unit.get(i).innerHTML = (i + 1) + _unit.get(i).innerHTML.substr(_unit.get(i).innerHTML.indexOf("<"));
		else
			_unit.get(i).innerHTML = (i + 1);
	}
}
//移动
function moveRow(line22From,isUp)
{	
	if(isUp){ 
		var rowClone = this.columnTable.find("tbody tr:eq("+(line22From+1)+")");
		this.columnTable.find("tbody tr:eq("+(line22From-1)+")").insertBefore(rowClone);
		var rowClone = this.dataTable.find("tbody tr:eq("+(line22From+1)+")");
		this.dataTable.find("tbody tr:eq("+(line22From-1)+")").insertBefore(rowClone);
	}else{
		var rowClone = this.columnTable.find("tbody tr:eq("+(line22From)+")");
		this.columnTable.find("tbody tr:eq("+(line22From+1)+")").insertBefore(rowClone);
		rowClone = this.dataTable.find("tbody tr:eq("+(line22From)+")");
		this.dataTable.find("tbody tr:eq("+(line22From+1)+")").insertBefore(rowClone);
	}
	/*
	var _unit = this.columnTable.find("tbody tr td:nth-child(1)");
	for (var i = 0; i < _unit.size() - 1; i++) {
		if(_unit.get(i).innerHTML.indexOf("<")!=-1)
				_unit.get(i).innerHTML = (i + 1) + _unit.get(i).innerHTML.substr(_unit.get(i).innerHTML.indexOf("<"));
		else
			_unit.get(i).innerHTML = (i + 1);
	}
	*/
	
}

//删除一行，如果只有一行，则不执行删除操作
function deleteRow(rowId,isnottreven) {
	if (this.dataTable[0].tBodies[0].rows.length>1) {
		for(i=0;i<this.cols.length;i++){
			if(this.cols[i].fieldType==0||this.cols[i].fieldType==1){
				document.getElementsByName(this.cols[i].name)[rowId-1].value="0";
			}else if(this.cols[i].inputType!=5){				
				document.getElementsByName(this.cols[i].name)[rowId-1].value="";
			}				
		}
		if(isnottreven){ 
		}else{
			delFireEvent(rowId-1,this.TableID);
		}

		this.dataTable.find("tbody tr:eq(" + (rowId-1) + ")").remove();
		this.columnTable.find("tbody tr:eq(" + (rowId-1) + ")").remove();
		

		if(typeof(initCalculate)!="undefined"){
        	initCalculate();
        }

        if(typeof(execStat)!="undefined"){
			execStat();
		}
		
		var _unit = this.columnTable.find("tbody tr td:nth-child(1)");
		for (var i = 0; i < _unit.size() - 1; i++) {
			if(_unit.get(i).innerHTML.indexOf("<")!=-1)
					_unit.get(i).innerHTML = (i + 1) + _unit.get(i).innerHTML.substr(_unit.get(i).innerHTML.indexOf("<"));
			else
				_unit.get(i).innerHTML = (i + 1);
		}
	}
	if(this.dataTable[0].tBodies[0].rows.length==1){
		this.addRow();
	}
}
//初始化单个TABLE成可以锁行锁列的DIV，缺省锁两列，如果用户设置过这个FORM则按设置过的值进行锁定
function init() {
	var TableID = this.TableID;//锁表的名称
	var width = this.width;//表的宽度
	var height = this.height;//表的高度
	var FixColumnNumber = this.FixColumnNumber;//锁定的列数，会加载用户设置过的列数
	
	$("#" + TableID).after("<div id='" + TableID + "_tableLayout' style='position:relative;overflow:hidden;height:" + height + "px; width:" + width + "px;'></div>");
	$("<div id=\"" + TableID + "_tableData\"></div>" + "<div id=\"" + TableID + "_tableHead\"></div>" + "<div id=\"" + TableID + "_tableColumn\"><iframe style=\"position:absolute;z-index:-1;width:100%;height:100%\" frameborder=\"0\"></iframe></div>" + "<div id=\"" + TableID + "_tableFix\"></div>").appendTo("#" + TableID + "_tableLayout");
	var oldtable = $("#" + TableID);
	//以下初始化可被锁定的列数
	if(oldtable[0].tHead.rows.length>1)//如果台头不是一行
	{
		var _tmpCells = oldtable[0].tHead.rows[0].cells;
		for(i=0;i<_tmpCells.length;i++)
		{
			if(_tmpCells[i].rowSpan>1)
			continue
			else
			{
				this.lockNum = i;
				break;
			}
		}
	}
	
	if(FixColumnNumber > this.lockNum)//如果锁定的位置超过可以被锁的列数
	{
		FixColumnNumber = this.lockNum;
		this.FixColumnNumber = this.lockNum;
	}	
	this.dataTable = oldtable;
	this.dataTable.css("table-layout", "fixed");
	
	var tableHeadClone = $("<table>").append($(document.getElementById(TableID).tHead).clone(true));
	tableHeadClone.attr("id", TableID + "_tableHeadClone");
	tableHeadClone.attr("width", oldtable.innerWidth());
	tableHeadClone.attr("class", "listRange_list_function_b");
	tableHeadClone.attr("border", "0");
	tableHeadClone.attr("cellspacing", "0");
	tableHeadClone.attr("cellpadding", "0");
	tableHeadClone.css("table-layout", "fixed");
	$("#" + TableID + "_tableHead").append(tableHeadClone);
	this.headTable = tableHeadClone;
	var tableColumnClone = $("<table>");
//	_rows = oldtable[0].tHead.rows.length;
	var _row;
	var thead = $("<thead>");
//	for (var i = 0; i < _rows; i++) {
		_row = $("<tr>");
		var _rowCell = oldtable[0].rows[0];
		for (var j = 0; j < FixColumnNumber; j++) {
			_row.append($(_rowCell.cells[j]).clone());
		}
		thead.append(_row);
		 //设置COLUMN的HEAD的高
		 if(_rowCell.cells[0].rowSpan==2)
		 {
		 	thead.find("td").css("height","44");
		 	thead[0].rows[0].cells[0].rowSpan=1;
		 	thead.append("<tr height=\"0\"><td style=\"height:0\" height=\"1\" colspan=\""+this.FixColumnNumber+"\"></td></tr>");
		 }
//	}
	tableColumnClone.append(thead);
	var _tmpWidth = 0;
	thead.find("tr:eq(0) td").each(function () {
		_tmpWidth += $(this).innerWidth();
	});
	_rows = oldtable[0].tBodies[0].rows.length;
	for (var i = 0; i < _rows; i++) {
		var _rowCell = oldtable[0].tBodies[0].rows[i];
		_row = $("<tr height=\""+$(_rowCell).css("height")+"px\">");
		for (var j = 0; j < FixColumnNumber; j++) {
			_row.append($(_rowCell.cells[j]).clone());
			$(_rowCell.cells[j]).find("INPUT,TEXTAREA,SELECT").remove();
		}
		tableColumnClone.append(_row);
	}
	tableColumnClone.attr("id", TableID + "_tableColumnClone");
	tableColumnClone.attr("class", "listRange_list_function_b");
	tableColumnClone.attr("width", _tmpWidth);
	tableColumnClone.attr("border", "0");
	tableColumnClone.attr("cellspacing", "0");
	tableColumnClone.attr("cellpadding", "0");
	tableColumnClone.css("table-layout", "fixed");
	$("#" + TableID + "_tableColumn").append(tableColumnClone);
	this.columnTable = tableColumnClone;
	var tableFixClone = $("<table>").append(tableColumnClone.find("thead").clone(true));
	tableFixClone.attr("id", TableID + "_tableFixClone");
	tableFixClone.attr("class", "listRange_list_function_b");
	tableFixClone.attr("border", "0");
	tableFixClone.attr("cellspacing", "0");
	tableFixClone.attr("cellpadding", "0");
	tableFixClone.css("table-layout", "fixed");
	tableFixClone.attr("width", _tmpWidth);
	$("#" + TableID + "_tableFix").append(tableFixClone);
	this.fixTable = tableFixClone;
	$("#" + TableID + "_tableData").append(oldtable);
	$("#" + TableID + "_tableLayout table").each(function () {
		$(this).css("margin", "0");
	});
	var HeadHeight = $("#" + TableID + "_tableHead thead").height();
	$("#" + TableID + "_tableHead").css("height", HeadHeight);
	$("#" + TableID + "_tableFix").css("height", HeadHeight);
	var ColumnsWidth = 0;
	var ColumnsNumber = 0;
	$("#" + TableID + "_tableColumn tr:first td:lt(" + FixColumnNumber + ")").each(function () {
		ColumnsWidth += ($(this).innerWidth() + 1);
		ColumnsNumber++;
	});
	$("#" + TableID + "_tableColumn").css("width", ColumnsWidth + 1);
	$("#" + TableID + "_tableFix").css("width", ColumnsWidth + 1);
	$("#" + TableID + "_tableData").scroll(function () {
		$("#" + TableID + "_tableHead").scrollLeft($("#" + TableID + "_tableData").scrollLeft());
		$("#" + TableID + "_tableColumn").scrollTop($("#" + TableID + "_tableData").scrollTop());
	});

	$("#" + TableID + "_tableFix").css({"overflow":"hidden", "position":"absolute", "z-index":"50", "background-color":"white"});
	$("#" + TableID + "_tableHead").css({"overflow":"hidden", "width":width - 25, "position":"absolute", "z-index":"45", "background-color":"white"});
	$("#" + TableID + "_tableColumn").css({"overflow":"hidden", "height":height - 25, "position":"absolute", "z-index":"40", "background-color":"white"});
	$("#" + TableID + "_tableData").css({"overflow":"auto", "width":width, "height":height, "position":"absolute", "z-index":"35"});
	
	
	$("#" + TableID + "_tableData").css({"scrollbar-face-color":"#aed3f1"});

	$("#" + TableID + "_tableFix").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableHead").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableColumn").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableData").offset($("#" + TableID + "_tableLayout").offset());
	this.FixColumnNumber = FixColumnNumber;

	//点击台头的时候，会弹出菜单，让用户设置锁定到第几列，调用此对象的changFix方法进行锁定	
	this.headTable.find("thead tr:eq(0) td:lt("+this.lockNum+")").contextPopup({
       title: '',
       items: [
         {label:'锁定到此列',action:function(e) {
			e.target.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix.changeFix(e.target.cellIndex+1);
         }},
         {label:'取消锁定',action:function(e) {
			e.target.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix.changeFix(2);
         }}
         ]
         });

	this.fixTable.find("thead tr:eq(0) td:lt("+this.lockNum+")").contextPopup({
       title: '',
       items: [
         {label:'锁定到此列',action:function(e) {
			e.target.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix.changeFix(e.target.cellIndex+1);
         }},
         {label:'取消锁定',action:function(e) {
			e.target.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix.changeFix(2);
         }}
         ]
         });
   	this.fixTable.find("thead td").css("color","#0000ff");
}
//锁定此对象的列数，会保存到服务器
function changeFix(FixColumnNumber) {
	$.post("favourstyle",{OPERATE:"SET",formId:this.TableID,property:"lockColumn",value:FixColumnNumber},function(data){},"xml");
	
	if(FixColumnNumber > this.lockNum)//如果锁定的位置超过可以被锁的列数
	{
		FixColumnNumber = this.lockNum;
		this.FixColumnNumber = this.lockNum;
	}	
	
	var TableID = this.TableID;
	var width = this.width;
	var height = this.height;
	if (this.FixColumnNumber < FixColumnNumber) {//如果锁定更多的列，则要从数据表格中把数据拷贝到锁定表格中
		var _tmpRows = this.columnTable[0].rows;
		var _tmpRowCount = _tmpRows.length;
		var _tmpUnit;
		var _tmpDataUnit;
		var _tmpDataRows = this.dataTable[0].rows;
		for (var i = 0; i < _tmpRowCount; i++) {
			if(i==1 && this.columnTable[0].tHead.rows.length==2)//如果表头有两行，并且处理第二行则跳过
			{
			}else
			for (var j = this.FixColumnNumber; j < FixColumnNumber; j++) {
				_tmpUnit = $(_tmpDataRows[i]).find("td:eq(" + j + ")");
				var _cell = $("<td>");
				_cell.attr("width", _tmpUnit.attr("width"));
				_tmpUnit.after(_cell);
				$(_tmpRows[i]).append(_tmpUnit);
			}
		}
		if(this.columnTable[0].tHead.rows.length>1)//如查有多行锁定
		{
			this.columnTable[0].tHead.rows[1].cells[0].colSpan=FixColumnNumber;
		}
	} else {//如果锁定列数减少，则需要把锁定的表格数据拷贝到数据表格中
		if (this.FixColumnNumber > FixColumnNumber) {
			var _tmpUnit;
			var _tmpDataRows = this.dataTable[0].rows;
			var _tmpRows = this.columnTable[0].rows;
			var _tmpRowCount = _tmpRows.length;
			for (var i = 0; i < _tmpRowCount; i++) {
			if(this.dataTable[0].tHead.rows.length==2 && i == 1)
			{}else
				for (var j = FixColumnNumber; j < this.FixColumnNumber; j++) {
					$(_tmpDataRows[i]).find("td:eq(" + j + ")").replaceWith($(_tmpRows[i]).find("td:eq(" + FixColumnNumber + ")"));
				}
				
			}
			if(this.columnTable[0].tHead.rows.length>1)//如查有多行锁定
			{
				this.columnTable[0].tHead.rows[1].cells[0].colSpan=FixColumnNumber;
			}
		} else {//如果锁定没发生变化
			return;
		}
	}
	var _realWidth =0;
	this.columnTable.find("thead tr:eq(0) td").each(function(){_realWidth+=parseInt(this.width);});
	this.columnTable.attr("width", _realWidth);
	this.columnTable.parent().css("width", this.columnTable.outerWidth());
	this.fixTable.find("thead").replaceWith(this.columnTable.find("thead").clone(true));
	this.fixTable.find("thead td").css("color","#0000ff");
	this.fixTable.attr("width",_realWidth);
	this.fixTable.parent().css("width",this.fixTable.outerWidth());
	this.fixTable.find("thead tr:eq(0) td:lt("+this.lockNum+")").contextPopup({
       title: '',
       items: [
         {label:'锁定到此列',action:function(e) {
			e.target.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix.changeFix(e.target.cellIndex+1);
         }},
         {label:'取消锁定',action:function(e) {
			e.target.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix.changeFix(2);
         }},
         ]
         });

	this.FixColumnNumber = FixColumnNumber;
	//favourstyle?formId=test&OPERATE=RESTORE&property=lockpos&value=4
}