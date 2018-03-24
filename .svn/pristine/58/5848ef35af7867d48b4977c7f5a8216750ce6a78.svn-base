<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="/style/css/jquery.contextmenu.css" type="text/css"/>
<link rel="stylesheet" href="/js/ui/jquery-ui-1.8.18.custom.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript"> 
	//界面改造 时，1、先显示界面，2、隐藏无权限字段，3赋值（添加默认值，修改原值）

</script>
<style type="text/css">

.add_Ul {height:25px;}
.add_Ul li{margin:2px 0 0 0;}
.add_Ul li label{white-space: normal;width: 100px;float: left;padding-right:2px;text-align: right;margin:0px;}
.add_Ul li span{display: none;border:1px solid #ff00ff}



</style>

</head>
<body style="overflow:auto;">
<div id="conter" style="overflow:scroll;">	
<div id="listRange_id" >
<script type="text/javascript">
var oDiv2=$("#listRange_id");
var sHeight=document.documentElement.clientHeight-32;
var sWidth=document.documentElement.clientWidth-32;
if(sHeight>0){
	oDiv2.height(sHeight);
	oDiv2.width(sWidth);
}
</script>
<div id="topConter">
<div class="listRange_1"  >
<ul class="add_Ul" >
#foreach ($row in $mainFieldInfos )			
#if("$row.inputType" == "6" && $row.fieldName != "id" && "$row.fieldName"!="moduleType" )
#if("$row.inputTypeOld" == "2")	
	#if("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")	
	<li id="li_$dismh"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：<input id="$dismh" name="$dismh"  type="text"></span></div>
	</li>
	#elseif(!$row.getSelectBean().relationKey.hidden)
	<li id="li_$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" 
		name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)"  type="text"></span></div>	
	</li>	
	#end
	#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
		#foreach($srow in $row.getSelectBean().viewFields)#set($colName="")		
			#if($!srow.display!="")
			#if($!srow.display.indexOf("@TABLENAME")==0)
				#set($index=$srow.display.indexOf(".")+1)#set($tableField=$tableName+"."+$srow.display.substring($index))
			#else
				#set($tableField=$srow.display)
			#end
			#set($colName="$srow.display")
			#else#set($tableField="$srow.fieldName")#set($colName="$srow.fieldName")
			#end
			#set($dis = $globals.getFieldDisplay($tableField))	
			#if($dis == "")#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName))#end			
			#if("$srow.hiddenInput"=="true")
			#else
				#if("$row.fieldType" == "18")
				<li id="li_$globals.getTableField($srow.asName)" style="width:95%;"><div>
					<label>$dis </label>
					<span>：<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)"  type="text"     ></span>	</div>				
				</li>
				#else
				<li id="li_$globals.getTableField($srow.asName)"><div>
					<label>$dis </label>
					<span>：<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)"  type="text"     ></span>	</div>			
				</li>				
				#end 
				#set($tableField="")
			#end				
		#end				
	#end
#elseif("$row.inputTypeOld" == "1" || "$row.inputTypeOld" == "16")
	<li id="li_$row.fieldName"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：<select id="$row.fieldName" name="$row.fieldName"  type="text"     ></select></span>	</div>
	</li>
#elseif("$row.inputTypeOld" == "5" )
#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
	<li id="li_$row.fieldName"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：
		#foreach($erow in $enumList)
<input type="checkbox" class="cbox" name="$row.fieldName" id="c_${row.fieldName}_${erow.value}" value="$erow.value" /><label for="c_${row.fieldName}_${erow.value}" class="cbox_w">$erow.name</label>
#end</span></div>					
	</li>	
#elseif("$row.inputTypeOld" == "10" )
#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
	<li id="li_$row.fieldName"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：
#foreach($erow in $enumList)
<input type="radio" class="cbox"  name="$row.fieldName" id="c_${row.fieldName}_${erow.value}" value="$erow.value" /><label class="cbox_w" for="c_${row.fieldName}_${erow.value}" >$erow.name</label>
#end</span></div>					
	</li>	
#else	
	<li id="li_$row.fieldName"><div >
		<label >$row.display.get("$globals.getLocale()") </label>
		<span >：<input id="$row.fieldName" name="$row.fieldName"  type="text"     ></span>	</div>				
	</li>	
#end
#end
#end

</ul>

</div>
</div><!-- topConter -->


<!-- 以上代码 主表表头 -->


</div>

</div>
</body>
</html>
