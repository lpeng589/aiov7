<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript"  src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
body{margin: 0px;width:100%;font-size:12px;background-color:#FFFFFF;font-family:"微软雅黑","Microsoft YaHei";}
img {border:0px}
li {list-style-type:none;}
em,i{font-style:normal;}
.oamainhead {float:left;width:100%;height:31px;padding-top:5px;border-bottom:1px solid #ECECEC;}
.scroll_function_small_a{margin:3px 0 0 3px;float:left;overflow:auto; height:auto;width:99%; overflow-x:auto;}
.scroll_function_small_bbs{margin-left:3px;float:left;margin-top:3px;height:auto;width:99%;background:#ffffff;overflow-x:hidden;}
#edittableDIV{max-height:400px;overflow:auto;width:420px;}
.listRange_list_function{table-layout:fixed;border-collapse:collapse;border-top:1px #d2d2d2 solid;border-right:1px #d2d2d2 solid;}
.listRange_list_function td{border-bottom:1px #d2d2d2 solid;border-left:1px #d2d2d2 solid;}
.listRange_list_function thead{background:#2e8cbc;}
.listRange_list_function thead td{padding:4px 4px 0 4px;height:28px;text-align:center;color:#fff;word-break:break-all;word-wrap :break-word;position: relative;}
.listRange_list_function tbody td{padding:0 4px;height:28px;white-space:nowrap;overflow: hidden;text-overflow: ellipsis;}
.listRange_list_function input{border:0;text-align:left;width:100%;}
.listRange_list_function select{width:100%;border:0px;padding:0px;text-align:left;}
.bDel{width:7px;height:7px;display:inline-block;background:url(/style/images/client/CloseBtn.png) no-repeat 0 0;cursor:pointer;float:left;margin:2px 0 0 6px;}
</style>
<script type="text/javascript">
var fieldNames;
var fieldNIds;
var flag;
var flay;
function deptPop(popname,fieldName,fieldNameIds,flag){
	var row = flag.parentNode.parentNode.rowIndex - 1;
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	var titles = "请选择部门";
	if(popname == "userGroup"){
		titles = "请选择个人"
	}
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	flag=flag;
	flay = row;
	asyncbox.open({
	id : 'Popdiv',
	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。





　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。




				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds,flay)
　　　　　	}
　　　	}
　	});
}
function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,flay);
	jQuery.close('Popdiv');
}

function newOpenSelect(employees,fieldName,fieldNameIds,flay){
 	var oldArr = [];	
	$("#applyTody input[name='MailinfoSettingUser_userId']",document).each(function(){ 
		var tr = []; 														   
     		tr.push($(this).val());      			             			   				
     		oldArr.push(tr);
			    		
	});
	var k=0;
	var employees = employees.split("|") ;	
	for(var j=0;j<employees.length-1;j++){
		var field = employees[j].split(";") ;
		var fy = "only";
		if(oldArr !=null && oldArr.length>0){
			for(var i=0;i<oldArr.length-1;i++){
				if(oldArr[i] == field[0]){
					fy="rep";
				}
			}
		}
		if(field!="" && fy == "only"){
			document.getElementsByName("formatFileName")[flay+k].value=field[1];
			document.getElementsByName("MailinfoSettingUser_userId")[flay+k].value=field[0];
			k++;
			addRowClick();
		}
	}
}


function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	//window.parent.frames['leftFrame'].location.reload() ;
	return true;
}	
	
function delRow(e,index){
	var len = $('#applyTody tr',document).length;
	if(len<2){
		return false;
	}
	var row = index.parentNode.parentNode.rowIndex - 1;
	var table = document.getElementById(e);
	table.deleteRow(row);
	loadOrder();
}
//添加异性



function addRowClick(obj){
	var row =0;	
	var e = document.getElementById("applyTody");
	var tr = document.createElement("tr");
	var len = $('#applyTody tr',document).length;
	if(obj !="" && obj!=undefined){
		row = obj.parentNode.parentNode.rowIndex;
		if(row!=len){
			return false;
		}
	}		
	for(var j=0;j<3;j++){   
		if(j==0){
			td = document.createElement("td");
			td.innerHTML="<input name=\"order\"  id=\"order\" type=\"text\" readonly=\"readonly\" value="+(len+1)+" />";
			tr.appendChild(td);			
		}else if(j==1){
			td = document.createElement("td");
			td.innerHTML="<span class='bDel' onclick=\"delRow('applyTody',this)\" title=\"删除\"></span>";
			tr.appendChild(td);
		}else{
			td = document.createElement("td");
			td.innerHTML="<input type=\"hidden\" name=\"MailinfoSettingUser_userId\" /><input type=\"text\" onclick=\"addRowClick(this);\" name=\"formatFileName\" id=\"formatFileName\" onDblClick=\"deptPop('userGroup','formatFileName','formatFileName',this);\" class=\"search\" />";
			tr.appendChild(td);
		}					
	}
	e.appendChild(tr);
}
function loadOrder(){
	var len = $('#applyTody tr',document).length;
    for(var i = 0;i<len;i++){
        $('#applyTody tr:eq('+i+') input[name=order]').val(i+1);
    }
        
}
</script>
</head>
<body >
<iframe name="formFrame" style="display:none"></iframe>
<form id="form" name="form" method="post" action="/EMailAccountAction.do" onSubmit="return beforSubmit(this)"  target="formFrame">
<input type="hidden" name="type" value="mailSet"/>
<input type="hidden" name="op" value="share" id="op"/>
<input type="hidden" name="accountId" value="$!accountId" />
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE_PREPARE")"/>

<div class="oamainhead">
	<div class="HeadingButton"></div>
</div>			

<div align="center">
<div class="scroll_function_small_a" >
<div style="width:600px;" align="center" >
<input type="hidden" name="MailinfoSettingUserPopupType" value="$globals.getTableInfoBean(${rowlist.tableName}).popupType"/> 
<br/>
<div  name="edittableDIV" id="edittableDIV"> 
	<table width="300" border="0" cellpadding="0" align="center" cellspacing="0" class="listRange_list_function" id="applyTbl" >
		<thead>
			<tr>		
				<td width="30">NO.</td>
				<td width="20">
		        	<img title="加一行" src="/style/images/add.gif" border="0" onclick="addRowClick();"></td>
				<td width="250">共享用户</td>	     		      
			</tr>
		</thead>		
		<tbody id="applyTody" name="applyTody">	
		#if("$!flayy" == "true")
		#foreach($log in $!result)		
			<tr>
				<td><input id="order" name="order" value="$velocityCount" readonly="readonly"></td>
				<td><span class="bDel" title="删除" onclick="delRow('applyTody',this)"></span></td>
				<td><input type="hidden" name="MailinfoSettingUser_userId" value="$!globals.getFromArray($log,0)"/>
				<input type="text" name="formatFileName" id="formatFileName" onclick="addRowClick(this);" 
				value="$!globals.getFromArray($log,1)" onDblClick="deptPop('userGroup','formatFileName','formatFileName',this);"class="search"/></td>					
			</tr>	
		#end
		#else	
		<tr>
				<td algin="center"><input id="order" name="order" value="1" readonly="readonly"></td>
				<td><span class="bDel" title="删除" onclick="delRow('applyTody',this)"></span></td>
				<td><input type="hidden" name="MailinfoSettingUser_userId" value="$!globals.getFromArray($log,0)"/>
				<input type="text" name="formatFileName" id="formatFileName" onclick="addRowClick(this);" 
				value="$!globals.getFromArray($log,1)" onDblClick="deptPop('userGroup','formatFileName','formatFileName',this);"class="search"/></td>					
			</tr>
			#end	
		 </tbody>
</table>
</div>
		<br/>
		<input class="btn btn-small" type="submit" name="Submit" value='$text.get("common.lb.save")' />
		<span class="btn btn-small" onClick="location.href='/EMailAccountQueryAction.do'">$text.get("common.lb.back")</span>

					
					
					
				</div>																		
	</div>			
 
</div>
</form>

</body>
</html>
