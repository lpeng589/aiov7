<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="/style/css/client.css" rel="stylesheet" type="text/css" />
<link href="/style/css/client_sub.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function selectPop(){
	asyncbox.open({
　　　	id : 'Popdiv',title : '请选择接收人',
		url : '/Accredit.do?popname=userGroup',
		cache : false,modal:true,width:750,height:440,
		btnsbar : jQuery.btn.OKCANCEL, 
　　　	callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var strValues = opener.strData.split(";") ;
				$("#newCreateBy",document).val(strValues[0]) ;
				$("#newCreateByName",document).val(strValues[1]) ;
　　　　　	}
　　　	}
	});
}

//处理双击个人部门函数
function fillData(dbData){
	var strValues = dbData.split(";") ;
	$("#newCreateBy",document).val(strValues[0]) ;
	$("#newCreateByName",document).val(strValues[1]) ;
	parent.jQuery.close('Popdiv');
}

function returnValue(){
	var createBy = $("#newCreateBy",document).val() ;
	var content = $("#content",document).val() ;
	if(createBy.length==0){
		alert("接收人不能为空!") ;
		return false;
	}
	return createBy + "@koron@" + content ;
}
</script>
</head>

<body class="body_f4">
<input type="hidden" id="newCreateBy" name="newCreateBy" value="">
<div class="boxbg2 subbox_w565">
<div class="subbox cf">
  <div class="operate operate2">
  <ul>
  <li class="sel">客户移交</li>
  </ul>
  <div class="closel"></div>
  </div>
  <div class="bd">
      <div class="inputbox inputbox2">
        <ul>
          <li><span><font style="color:#F18302;">*</font>接收人：</span>
            <input id="newCreateByName" name="newCreateByName" type="text" class="inp_w" value=""/><a href="javascript:selectPop();"></a></li>
          <li class="col"><span>移交内容提醒：</span>
            <textarea id="content" name="content" rows="7" class="inp_w4"></textarea>
          </li>
        </ul>
      </div>
  </div>
</div>
</div>
</body>
</html>