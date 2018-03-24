
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>345</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
	</head>
	
	<script language="javascript" src="/js/setTime.js"></script>
	<script type="text/javascript">
function $(value){
	return document.getElementById(value) ;
}
var No = 0;
function addTr(){
	var my_body = $("my_body");
	
	var rowNo = my_body.rows.length -1;
	var my_row = my_body.insertRow(rowNo);
	var serial = my_row.insertCell();
	var onDutyTimeCell = my_row.insertCell();
	var offDutyTimeCell = my_row.insertCell();
	var onDutyAvailabilityTimeCell = my_row.insertCell();
	var offDutyAvailabilityTimeCell = my_row.insertCell();
	var squadSectTypeCell = my_row.insertCell();
	var del = my_row.insertCell();
	
	for(var i = 0;;i++){
	
		if($(i) == null){
		
			No = i;
			
			break;
		}
	}
	my_row.id = No;
	serial.id = "serial"+No;
	serial.className = "listheadonerow";
	serial.innerHTML = No+1;
	onDutyTimeCell.innerHTML = "<input type='text' name='onDutyTime"+No+"' id='onDutyTime"+No+"' style='text-align: center;width: 110px;' onfocus='addRowByfocus(this)' onclick='_SetTime(this)' />";
	onDutyTimeCell.id = "onDutyTimeCell"+No;
	offDutyTimeCell.innerHTML = "<input type='text' name='offDutyTime"+No+"' style='text-align: center;width: 110px;' id='offDutyTime"+No+"' onfocus='addRowByfocus(this)' onclick='_SetTime(this)'/>";
	offDutyTimeCell.id = "offDutyTimeCell"+No;
	onDutyAvailabilityTimeCell.innerHTML = "<input type='text' name='onDutyAvailabilityTime"+No+"' style='text-align: center;width: 110px;' onfocus='addRowByfocus(this)' id='onDutyAvailabilityTime"+No+"' value='0'/>";
	onDutyAvailabilityTimeCell.id = "onDutyAvailabilityTimeCell" + No;
	offDutyAvailabilityTimeCell.innerHTML = "<input type='text' name='offDutyAvailabilityTime"+No+"' style='text-align: center;width: 110px;' onfocus='addRowByfocus(this)' id='offDutyAvailabilityTime"+No+"' value='0'/>";
	offDutyAvailabilityTimeCell.id = "offDutyAvailabilityTimeCell" + No;
	squadSectTypeCell.innerHTML = getSquadSectType(No,'0');
	squadSectTypeCell.id = "squadSectTypeCell"+No;
	del.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' onclick='delRow(this)' />";
	No++;
}

function getSquadSectType(obj,value){
	
	var squadSectType =  "<select name='squadSectType"+obj+"'>";
	#foreach($erow in $globals.getEnumerationItems("squadSectType"))
		if(value == "$erow.value"){
			squadSectType = squadSectType +"<option value='$erow.value' selected='selected'>$erow.name</option>";
		}else{
			squadSectType = squadSectType +"<option value='$erow.value'>$erow.name</option>";
		}
	#end
	squadSectType = squadSectType + "</select>";
	return squadSectType;
}

function addStateRow(){
	var my_body = $("my_body");
	
	var rowInd = Number(my_body.rows.length);

	var stateRow = my_body.insertRow(rowInd);

	stateRow.style.background = "#FFFFEE";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	addTr();
}

function delRow(obj){
	var tr = obj.parentNode.parentNode;
	var start = Number(tr.id) + 1;
	$("my_body").deleteRow(tr.rowIndex-1);
	for(;;start++){
		if($(start) == null){
			break;
		}
		$(start).id = start -1;
		$("serial"+start).innerHTML = start;
		$("serial"+start).id = "serial"+(start -1);
		$("onDutyTimeCell"+start).innerHTML = "<input type='text' onfocus='addRowByfocus(this)'  name='onDutyTime"+(start-1)+"' style='text-align: center;width: 110px;' id='onDutyTime"+(start-1)+"' value='"+$("onDutyTime"+start).value+"'  onclick='_SetTime(this)'/>";
		$("onDutyTimeCell"+start).id = "onDutyTimeCell"+(start-1);
		$("offDutyTimeCell"+start).innerHTML = "<input type='text' onfocus='addRowByfocus(this)' name='offDutyTime"+(start-1)+"' style='text-align: center;width: 110px;' id='offDutyTime"+(start-1)+"' value='"+$("offDutyTime"+start).value+"'  onclick='_SetTime(this)'/>";
		$("offDutyTimeCell"+start).id = "offDutyTimeCell"+(start-1);
		$("onDutyAvailabilityTimeCell"+start).innerHTML = "<input type='text' onfocus='addRowByfocus(this)' name='onDutyAvailabilityTime"+(start-1)+"' style='text-align: center;width: 110px;' id='onDutyAvailabilityTime"+(start-1)+"' value='"+$("onDutyAvailabilityTime"+start).value+"'/>";
		$("onDutyAvailabilityTimeCell"+start).id = "onDutyAvailabilityTimeCell" + (start-1);
		$("offDutyAvailabilityTimeCell"+start).innerHTML = "<input type='text' onfocus='addRowByfocus(this)' name='offDutyAvailabilityTime"+(start-1)+"' style='text-align: center;width: 110px;' id='offDutyAvailabilityTime"+(start-1)+"' value='"+$("offDutyAvailabilityTime"+start).value+"'/>";
		$("offDutyAvailabilityTimeCell"+start).id = "offDutyAvailabilityTimeCell" + (start-1);
		$("squadSectTypeCell"+start).innerHTML = getSquadSectType((start-1),$("squadSectType"+start).value);
		$("squadSectTypeCell"+start).id = "squadSectTypeCell" + (start-1)
	}
	No--;
}

function addRowByfocus(obj){
	var tr = obj.parentNode.parentNode;
	if($("my_body").rows.length == tr.rowIndex + 1){
		addTr();
	}
}

function getTime(){

	var fs = new Array();
	var my_body = $("my_body");
	for(var i = 0;i < my_body.rows.length-1;i++){
		if("" == $("onDutyTime"+i).value && "" == $("offDutyTime"+i).value){
			continue;
		}
		if("" == $("onDutyTime"+i).value){
			alert("$text.get('common.msg.dutyTimeNotNull')")
			return false;
		}
		if("" == $("offDutyTime"+i).value){
			alert("$text.get('common.msg.afterDutyTimeNotNull')")
			return false;
		}
		var times = new Array();
		times[0] = $("onDutyTime"+i).value;
		times[1] = $("offDutyTime"+i).value;
		fs[i] = times;
	}

	if(fs.length == 0){
		alert("$text.get('common.msg.enactmentDutyTime')");
		return false;
	}
	for(var i = 0;i < fs.length;i++){
		var time1 = fs[i];
		if(formatTime(time1[0],time1[1])){
			alert("$text.get('common.msg.dutyTime.afterDutyTimeNotSame')");
			return false;
		}
		for(var j = 0;j < fs.length;j++){
			if(j != i){
				var time2 = fs[j];
				if(formatTime(time2[0],time1[0]) && formatTime(time1[1],time2[0]) || 
					formatTime(time2[1],time1[0]) && formatTime(time1[1],time2[1])){
					alert("$text.get('common.msg.addDutyTime')");
					return false;
				}
			}
		}
	}
	var squadEnactmentName = $("squadEnactmentName").value;
	if(squadEnactmentName == ""){
		alert("$text.get('common.msg.squadNoNotNull')");
		return false;
	}
	return true;
}

function formatTime(time1,time2){
	var times1 = time1.split(":");
	var times2 = time2.split(":");
	var date1 = new Date("2009","11","10",times1[0],times1[1],times1[2]);
	var date2 = new Date("2009","11","10",times2[0],times2[1],times2[2]);
	
	if(date1>=date2){
		return true;
	}
	return false;
}

function $(obj){
	return document.getElementById(obj);
	
}


  </script>

	<body onLoad="addStateRow()">
	<form action="/DutyPeriodsAction.do?type=1" name="listReport" method="post">
		<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
			</div>
			<div class="HeadingTitle">
				$text.get("common.msg.addSquadNo")
			</div>
			<ul class="HeadingButton">
				
					<li>
						<button type="submit" name="saveList" title="Ctrl+S"
							onClick="return getTime()" class="b2">$text.get("common.lb.save")
						</button>
					</li>
									<li>
						<button type="button" name="backList" title="Ctrl+Z"
							onClick="window.history.go(-1);" class="b2">$text.get("common.lb.back")
						</button>
					</li>
			</ul>
		</div>
<div id="listRange_id" align="center">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
			<div class="scroll_function_big">
				<ul class="listRange_1">
					<li>
						<span>$text.get("squad.squadEnactmentName")：</span>
						<font color="red">*</font><input type="text" onKeyDown="if(event.keyCode == 32)event.returnValue = false;" name="squadEnactmentName"  id="squadEnactmentName" />
					</li>
				</ul>
				
			</div>
			<input type="hidden" name="operation" id="operation" value="1">
			
			<table border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="table"
				width="680">
				<THead>
					<tr class="listhead">
						<td class="listheade" width="30" align="center">
							<span style="vertical-align:middle;"><IMG
									src="/$globals.getStylePath()/images/down.jpg" border=0 />
							</span>
						</td>
						<td width="120" align="center">
							$text.get("squadSect.onDutyTime")
						</td>
						<td width="120" align="center">
							$text.get("squadSect.offDutyTime")
						</td>
						<td width="150" align="center">
							$text.get("squadSect.onDutyAvailabilityTime")
						</td>
						<td width="150" align="center">
							$text.get("squadSect.offDutyAvailabilityTime")
						</td>
						<td width="120" align="center">
							$text.get("squadSect.squadSectType")
						</td>
						<td width="50" align="center">
							<img src="/$globals.getStylePath()/images/add.gif" onClick="addTr()"/>
						</td>
					</tr>
				</THead>
				<TBody id="my_body" align="center">
					
				</TBody>
			</table>
</form>
	</body>
</html>
