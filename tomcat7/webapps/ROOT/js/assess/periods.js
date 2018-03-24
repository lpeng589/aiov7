var empNo = 0,decNo = 0;
var dutyDates = new Array();
var squads = new Array();
var squadsName = new Array();

  function openSelectByEmp(urlstr){
  
  	if(!validate()){
  		return false;
  	}
  	var str  = window.showModalDialog(urlstr+"&MOID=0809fc77_0909100911555462068&MOOP=add","","dialogWidth=800px;dialogHeight=450px"); 
  	
  	if(undefined == str){
		return false;
	}
  	var dates = str.split(";");
  	
  	var startDates = $("startDate").value.split("-");
  	var endDates = $("endDate").value.split("-");
  	
  	var startDay = '0' == startDates[2].charAt(0)?startDates[2].charAt(1):startDates[2];
  	var endDay = '0' == endDates[2].charAt(0)?endDates[2].charAt(1):endDates[2];
  	
  	for(var i = startDay; i <= endDay; i++){
  		var tempDay = '0'+i;
  		tempDay = tempDay.length == 2?tempDay:i;
  		var dutyDate = startDates[0] + "-" + startDates[1] + "-" + tempDay;
  		var j = 0
  		for(; j < dutyDates.length; j ++){
  			if(dutyDate == dutyDates[j]){
  				squads[j] = dates[0];
  				squadsName[j] = dates[1];
  				break;
  			}
  		}
  		if(j == dutyDates.length){
  			dutyDates[j] = dutyDate;
  			squads[j] = dates[0];
  			squadsName[j] = dates[1];
  		}
  	}
  	
  	createPeriodsTR();
  }
  
  function validate(){
  
  	if("" == $("startDate").value){
  		alert("开始工作日不能为空!");
  		return false;
  	}
  	if("" == $("endDate").value){
  		alert("结束工作日不能为空!");
  		return false;
  	}
  	if(!formatTime($("startDate").value,$("endDate").value)){
		return false;
	}
	
  	var startDates = $("startDate").value.split("-");
  	var endDates = $("endDate").value.split("-");
  	
  	if(startDates[0] != endDates[0] || startDates[1] != endDates[1]){
  		alert("工作日区间设定不支持跨年跨月设定!请重新选择!");
  		$("startDate").value = "";
  		$("endDate").value = "";
  		return false;
  	}
  	return true;
  }

function formatTime(time1,time2){
	var times1 = time1.split("-");
	var times2 = time2.split("-");
	var date1 = new Date(times1[0],times1[1],times1[2]);
	var date2 = new Date(times2[0],times2[1],times2[2]);
	
	if(date1>date2){
		alert("结束时间不能在开始时间之前");
		return false;
	}
	return true;
}

  function openSelect(urlstr,field,date){ 

	var str  = window.showModalDialog
	(urlstr+"&MOID=0809fc77_0909100911555462068&MOOP=add","","dialogWidth=800px;dialogHeight=450px"); 
	
	if(undefined == str){
		return false;
	}
	if("employeeNo" == field){
		
		var employees = str.split("|");
		for(var i = 0;i < employees.length;i++){
			if("" == employees[i]){
				employees.pop();
				break;
			}
			var employee = employees[i].split(";");
			employees[i] = employee;
		}
		createEmpTR(employees);
		return true;
	}
	else if ("DepartmentNo" == field){
		var departments = str.split("|");
		for(var i = 0;i < departments.length;i++){
			if("" == departments[i]){
				departments.pop();
				break;
			}
			var department = departments[i].split(";");
			departments[i] = department;
		}
		createDecTR(departments);
		return true;
	}
	else if ("squadEnactmentNo" == field){
		fs=str.split(";");   	
		
		var tempDate = $("tempDate"+date).value;
		
		var j = 0
  		for(; j < dutyDates.length; j ++){
  			if(tempDate == dutyDates[j]){
  				squads[j] = fs[0];
  				squadsName[j] = fs[1];
  				break;
  			}
  		}
  		if(j == dutyDates.length){
  			dutyDates[j] = tempDate;
  			squads[j] = fs[0];
  			squadsName[j] = fs[1];
  		}
		
		createPeriodsTR();
		
		return true;		  		  		  			
	}
	return true;
}


function createDecTR(departments){

	var my_body = $("decBody");
	var rowNo = my_body.rows.length -1;
	for(var i = 0;i < departments.length;i++){
		var my_row = my_body.insertRow(rowNo);
		var numberNo = my_row.insertCell();
		var decNameCell = my_row.insertCell();
		var del = my_row.insertCell();
		
		my_row.id = "decRow" + decNo;
		numberNo.className = "listheadonerow";
		numberNo.innerHTML = decNo + 1;
		numberNo.id = "decNo" + decNo;
		decNameCell.innerHTML = "<input type='hidden' style='text-align: center;' name='decNumber"+decNo+"' id='decNumber"+decNo+"' value='"+departments[i][0]+"' />"+
			"<input type='text' style='text-align: center;' name='decName"+decNo+"' id='decName"+decNo+"' value='"+departments[i][1]+"' />";
		decNameCell.id = "decNameCell" + decNo;
		del.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' onclick=\"delDecRow(this)\" />";
		decNo++;
		rowNo++;
	}
}

function delDecRow(obj){
	var tr = obj.parentNode.parentNode;
	var start = Number(tr.id.substring(6)) + 1;
	$("decBody").deleteRow(tr.rowIndex-1);
	
	for(;;start++){
	
		if(null == $("decRow"+start)){
			break;
		}else{		
			$("decRow"+start).id = "decRow"+(start -1);
			$("decNo"+start).innerHTML = start;
			$("decNo"+start).id = "decNo"+(start -1);
			$("decNameCell"+start).innerHTML = "<input type='hidden' style='text-align: center;' name='decNumber"+(start -1)+"' id='decNumber"+(start -1)+"' value='"+$("decNumber"+start).value+"' />"+
				"<input type='text' style='text-align: center;' name='decName"+(start -1)+"' id='decName"+(start -1)+"' value='"+$("decName"+start).value+"' />";
			$("decNameCell"+start).id = "decNameCell"+(start -1);
		}
	}
	decNo--;
}

function createEmpTR(employees){
	var my_body = $("empBody");
	var rowNo = my_body.rows.length -1;
	for(var i = 0;i < employees.length;i++){
		var my_row = my_body.insertRow(rowNo);
		var numberNo = my_row.insertCell();
		var empNameCell = my_row.insertCell();
		var del = my_row.insertCell();
		
		my_row.id = "empRow" + empNo;
		numberNo.className = "listheadonerow";
		numberNo.innerHTML = empNo + 1;
		numberNo.id = "empNo" + empNo;
		empNameCell.innerHTML = "<input type='hidden' style='text-align: center;' name='empNumber"+empNo+"' id='empNumber"+empNo+"' value='"+employees[i][0]+"' />"+
		"<input type='text' style='text-align: center;' name='empName"+empNo+"' id='empName"+empNo+"' value='"+employees[i][1]+"' />";
		empNameCell.id = "empNameCell" + empNo;
		del.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' onclick=\"delEmpRow(this)\" />";
		empNo++;
		rowNo++;
	}
}

function delEmpRow(obj){
	var tr = obj.parentNode.parentNode;
	var start = Number(tr.id.substring(6)) + 1;
	$("empBody").deleteRow(tr.rowIndex-1);
	
	for(;;start++){
		if(null == $("empRow"+start)){
			break;
		}else{		
			$("empRow"+start).id = "empRow"+(start -1);
			$("empNo"+start).innerHTML = start;
			$("empNo"+start).id = "empNo"+(start -1);
			$("empNameCell"+start).innerHTML = "<input type='hidden' style='text-align: center;' name='empNumber"+(start -1)+"' id='empNumber"+(start -1)+"' value='"+$("empNumber"+start).value+"' />"+
				"<input type='text' style='text-align: center;width: 110px;' name='empName"+(start -1)+"' id='empName"+(start -1)+"' value='"+$("empName"+start).value+"' />";
			$("empNameCell"+start).id = "empNameCell"+(start -1);
		}
	}
	empNo--;
}

function createPeriodsTR(fs,tempDate){
	
	var my_body = $("my_body");
	
	delAllPeriods();
	for(var i = my_body.rows.length -1; i < dutyDates.length;i ++){

		var my_row = my_body.insertRow( i);
		var numberNo = my_row.insertCell(0);
		var dateNum = my_row.insertCell(1);
		var squadEnactmentNoCell = my_row.insertCell(2);
		var deleteMyRow = my_row.insertCell(3);
		
		my_row.id = "row"+i;
		numberNo.innerHTML = i + 1;
		numberNo.id = "numberNo"+i;
		numberNo.className = "listheadonerow";
		dateNum.innerHTML = "<input type='text' name='myDate"+i+"' id='myDate"+i+"' value='"+dutyDates[i]+"' />";
		dateNum.id = "dateNum"+i;
		squadEnactmentNoCell.innerHTML ="<input type='hidden' name='squadEnactmentNo"+i+"' id='squadEnactmentNo"+i+"' value='"+squads[i]+"' />"+
				"<input type='text' name='squadEnactmentName"+i+"' id='squadEnactmentName"+i+"' value='"+squadsName[i]+"' />";
		squadEnactmentNoCell.id = "squadEnactmentNoCell"+i;
		deleteMyRow.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' onclick=\"delRow(this)\" />";
	}
}

function delAllPeriods(){

	var my_body = $("my_body");
	for(var i = 0; i < my_body.rows.length -1;){
		my_body.deleteRow(i);
	}
	return true;
}

function delRow(obj){
	
	var tr = obj.parentNode.parentNode;
	var start = Number(tr.id.substring(3)) + 1;
	$("my_body").deleteRow(tr.rowIndex-1);
	for(;;start++){
		if($("row"+start) == null){
			break;
		}else{		
			dutyDates = removeElementByIndex(start,dutyDates);
			squads = removeElementByIndex(start,squads);
			squadsName = removeElementByIndex(start,squadsName);
			$("row"+start).id = "row"+(start -1);
			$("numberNo"+start).innerHTML = start;
			$("numberNo"+start).id = "numberNo"+(start -1);
			$("dateNum"+start).innerHTML = "<input type='text' name='myDate"+(start -1)+"' id='myDate"+(start -1)+"' value='"+$("myDate"+start).value+"'/>";
			$("dateNum"+start).id = "dateNum"+(start-1);
			$("squadEnactmentNoCell"+start).innerHTML = "<input type='hidden' name='squadEnactmentNo"+(start-1)+"' id='squadEnactmentNo"+(start-1)+"' value='"+$("squadEnactmentNo"+start).value+"'/>"+
					"<input type='text' name='squadEnactmentName"+(start -1)+"' id='squadEnactmentName"+(start -1)+"' value='"+$("squadEnactmentName"+start).value+"' />";
			$("squadEnactmentNoCell"+start).id = "squadEnactmentNoCell"+(start-1);
		}
	}
}

function removeElementByIndex(index,arrs){
	var tempArrs = new Array();
	if(arrs.length == 0){
		return false;
	}
	if(index > 0 && index < arrs.length-1){
		tempArrs = arrs.slice(0,index).concat(arrs.slice(index+1));
	}
	else if(index == arrs.length - 1){
		tempArrs = arrs.slice(0,index);
	}
	else if(index == 0){
		tempArrs = arrs.slice(index+1);
	}
	return tempArrs;
}

function $(obj){
	return document.getElementById(obj);
}


function subAdd(){
	var myBody = $("my_body");
	var empBody = $("empBody");
	var decBody = $("decBody");
	
	if($("periodsName").value == ""){
		alert("周期名称不能为空!请输入!");
		return false;
	}
	if(myBody.rows.length <= 1){
		alert("周期不能为空,请选择!");
		return false;
	}
	if(empBody.rows.length <= 1 && decBody.rows.length <= 1){
		alert("周期应用范围不能为空!请选择!");
		return false;
	}
	return true ;
}

function formatDate(obj){
	if(obj < 10){
		return "0"+obj;
	}
	return obj;
}

function addStateRow(body,cellsLength){
	var my_body = $(body);
	
	var rowInd = Number(my_body.rows.length);

	var stateRow = my_body.insertRow(rowInd);

	stateRow.style.background = "#FFFFEE";
	for(var i = 0;i < cellsLength;i++){
		stateRow.insertCell(i).innerHTML = "&nbsp;";
	}
}

function onloadByTable(){
	addStateRow("decBody",3);
	addStateRow("empBody",3);
	addStateRow("my_body",4);
}