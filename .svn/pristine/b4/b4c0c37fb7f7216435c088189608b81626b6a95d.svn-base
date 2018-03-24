<!DOCTYPE html>
<html>
<head>
<title>推荐客户</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/scrollbar.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/mobile.css">

<link rel="shortcut icon" href="/favicon.ico">
<script src="/mobile/js/jquery.min.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script src="/mobile/js/mobileapi.min.js"></script>
</head>
<body>
<div class="pull-demo-page"  data-role="page" id="pageUpdate">
  <div data-role="header" data-position="fixed" data-tap-toggle="false" data-disable-page-zoom="false" style="position: fixed;">
    <a href="#pageone" data-transition="flip" id="backbt" data-icon="back" data-role="button">返回</a>
    <h1 id="titleh1">推荐客户 </h1>
    <a href="javascript:saveBill(false)" data-role="button" id="updateOpbt"  class="ui-btn-right" data-icon="check">保存</a>
    <span id="detailBtDiv" class="dropOperations" style="display:none; position: absolute;  right: 5px;  top: 40px;  background-color: #fff;  padding: 5px;  border: 1px solid #C9CBC5;  border-radius: 0 0 8px 8px;"   >
    	<ul id="detailBt" >
    	</ul>
    </span>
  </div>

  <div data-iscroll="" data-role="content" id="pageUpdateContent"><!-- 修改的界面显示 -->
	<ul data-role="listview">
      <li data-role="list-divider"><span style="  padding-top: 15px;  display: inline-block;">客户信息 </span></li>
      <li><label class="ilabel" style="display:inline-block" for="tzhlclient_clientName">客户名称：</label><span id="clientNameSpan"></span><input type="hidden" id="clientId" /></li>
      <li><label class="ilabel" style="display:inline-block" for="district">所在城市：</label><span id="districtSpan"></span></li>
	  <li><label class="ilabel" style="display:inline-block" for="connect">联系人：</label><span id="connectSpan"></span></li>
	  <li><label class="ilabel" style="display:inline-block" for="tel">电话：</label><span id="telSpan"></span></li>	  
	  <li data-role="list-divider">处理信息</li>
	  <li><label for="statusId">状态</label>
      	<select id="statusId">
      		<option title="待接收" value="0">待接收</option>
      		<option title="拒绝" value="1">拒绝</option>
      		<option title="跟进中" value="2">跟进中</option>
         	<option title="已提成" value="3">已提成</option>
      	</select>
      </li>
	  <li><label class="ilabel" for="charges">佣金金额：</label><input type="text" id="charges"></li>      
	  <li><label class="ilabel" for="remark">备注</label><textarea id="remark"></textarea></li>
    </ul>
  </div>  
<script type="text/javascript">
//注意本script必须包括在主界面div之内，因为当从列表通过ajax取本文件代码时主界面div之外的代码将会丢弃
var keyId="";
//修改某条数据
function update(kId){
	keyId = kId;	
	jQuery.mobile.changePage($("#pageUpdate"),{ transition: "flip"} );			
	detail(keyId,'tzhlclient',detailCalback); //从服务器取数据	
}

//添加或修改的保存
function saveBill(isCheck){ 
	//$("#detailBtDiv").toggle() ;	
	
	for(i=0;i< curBillObj.fieldList.length;i++){
		item =curBillObj.fieldList[i];
		//元素的隐藏与不隐藏
		if(item.isNull==1 && item.fieldName != 'id'){
			if($("#"+item.fieldName).val()==''){
				malert(item.display+'不能为空');
				returnn;
			}
		}
	}
	
	saveValues = new Object();
	$("input").each(function(){
		saveValues[$(this).attr("id")] = $(this).val();
	});
	$("textarea").each(function(){
		saveValues[$(this).attr("id")] = $(this).val();
	});
	$("select").each(function(){
		saveValues[$(this).attr("id")] = $(this).val();
	});
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	//执行修改
	updateBill('tzhlclient','','',isCheck,postdata,function(){
		detail(keyId,'tzhlclient',detailCalback); //更新数据
	});
	
}

//从服务器取数据后的回调函数
var curBillObj;
function detailCalback(obj){
	//$("#detailBtDiv").hide();	
	curBillObj = obj;
	$("#pageUpdate").trigger('create');	
	$("span[id$=Span]").text('');
	
	//控制各字段的隐藏，只读，清空字段内容
	for(i=0;i< obj.fieldList.length;i++){
		item =obj.fieldList[i];
		name = item.fieldName.replace(".","_");
		$("#"+name).val(''); //清空，避免先修改后添加时，一些残留数据
		$("#"+name+"Span").text('');//有些字段不需要input而只是为了显示
				
		//元素的隐藏与不隐藏
		if(item.inputType==3){
			$("label[for="+item.fieldName+"]").hide();
			$("li[for="+item.fieldName+"]").hide();
		}else{
			$("label[for="+item.fieldName+"]").show();
			$("li[for="+item.fieldName+"]").show();
		}
		
	}
	
	//设置界面各字段的值	
	for (item in obj.values){
		name = item.replace(".","_");
		//如果对应字段不存在，则自动添加隐藏字段。
		if($("#"+name).size()==0 && name !='LANGUAGEQUERY'){
			$("#pageUpdate").append("<input type='hidden' id='"+name+"'>");
		}
		$("#"+name).val(obj.values[item]);
		$("#"+name+"Span").text(obj.values[item]);//有些字段不需要input而只是为了显示
		if(name=="statusId"){
			$("#"+name).selectmenu('refresh');
		}
	}

}


</script>  
  
</div>
</body>
</html>
			