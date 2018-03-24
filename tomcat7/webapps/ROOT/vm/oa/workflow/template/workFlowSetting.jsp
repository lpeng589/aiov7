<!DOCTYPE html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>审核流类型列表</title>
<link type="text/css" rel="stylesheet" href="/style1/css/worlflow_design.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
var moduleType = "$!moduleType";
/*加载NO.和行*/
function loadOrder(){
	var len = $('#mybody tr',document).length;
	if(len < 10){
		for(var i=len+1;i<11;i++){
			var tr ='<tr><input type="hidden" name="classCode" value="0" />'											
			    +'<td ><input type="text" name="number" onclick="addRow(this);" /></td>'
	     	    +'<td ><div class="hc-del"><input type="text" name="types"  onclick="addRow(this);" /><b class="icons"></b></td></tr>'	
	       	$("#mybody").append(tr);
		}
		
	}    
}
/*删除行*/
$(".hc-del .icons").live('click', function() {
	var len = $('#mybody tr',document).length;
	if(len == 1){
		asyncbox.tips('至少保留一个。');		
	}else{
		$(this).parent().parent().parent().remove();
	}
});

/*添加,修改*/
function addNext(Id){
	form.action = "/OAWorkFlowTempAction.do?operation=2&updateType=updateSet"+"&moduleType="+moduleType;
	form.target = "formFrame";
	form.submit();
}

/*新增行，单击最后一个input默认加多一行*/
function addRow(obj){	
	var len = $('#mybody tr',document).length;
	var index = obj.parentNode.parentNode.rowIndex;
	if(index == undefined){
		index = obj.parentNode.parentNode.parentNode.rowIndex;
	}
	if(len == index){
	var lens = parseFloat(len)+1; 
		var tr ='<tr><input type="hidden" name="classCode" value="0" />'												
		    +'<td ><input type="text" name="number" onclick="addRow(this);" /></td>'
     	    +'<td ><div class="hc-del"><input type="text" name="types"  onclick="addRow(this);" /><b class="icons"></b></td></tr>'	
	   jQuery("#mybody").append(tr);
	}
}

</script>
<body  onload="loadOrder();">
<div id="scroll-wrap">
<div class="meeting-wrap" style="width: 100%;min-height:250px"> 
<iframe name="formFrame" style="display:none"></iframe>
<form action="/OAWorkFlowTempAction.do" method="post" name="form" >
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
<table width="80%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
		<thead>
			<tr>		
				<td width="25" class="oabbs_tc">编号</td>
			    <td width="120" class="oabbs_tc">类型</td>     	    	      	        
			</tr>
		</thead>
		<tbody id="mybody">	
		#foreach($log in $!flowList)	
			<tr>	
				<input type="hidden" name="classCode" value="$!globals.get($!log,4)" />											
			    <td>
			    	<input type="text" name="number" value="$!globals.get($!log,6)" onclick="addRow(this);" />
			    </td>
	     	    <td>
	     	    	<div class="hc-del">
	     	    		<input type="text" name="types" value="$!globals.get($!log,7)" onclick="addRow(this);"/>
	     	    		<b class="icons"></b>
	     	    	</div>
	     	    </td>
	     	    	     	      	   
	     	</tr>	
     	#end
		</tbody>
		</table>
		</form>
</div>		
</div>	
</body>
</html>