<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/crm/jquery.min.js" ></script>
<script type="text/javascript" src="/js/crm/jquery.icolor.min.js"></script> 
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/function.js"></script>

<style type="text/css">
.inputbox ul li{
	width: 350px;	
}
.icolor{position:absolute;}
.icolor_flat,.icolor_ft{position:relative;}
.icolor td{width: 15px;height: 15px;border: solid 1px #000000;	cursor:pointer;}
.icolor table{background-color: #FFFFFF;border: solid 1px #ccc;}
.icolor .icolor_tbx{width:180px;border-top:1px solid #999;border-left:1px solid #ccc;border-right:1px solid #ccc;border-bottom:1px solid #ccc;}
.icolor_ok img{border:none;}
.icolorC,h2{width:500px;}
.inputbox ul{margin-left: 30px;width: 350px;}
.inputbox ul li span{width: 100px;font-size: 13px;color: #717168;}
#labelName{width: 170px;height: 16px;}
#colorDiv {border: 1px solid #98989A; width: 145px; height: 20px;float: left;background-color: #F2F4F5}
.icolor4 span{width: 80px;}
</style>


<script type="text/javascript">
	$(document).ready(function(){
		$("#labelName").select();
		
	});
	function beforeSubmit(){
		if(!WidthCheck(jQuery("#labelName").val(),8)){
			alert("标签名称长度不能超过4个字符(一个汉字两个字符)");
			jQuery("#labelName").focus();
			return false;
		}
		if(jQuery("#labelName").val()==""){
			alert("模板名称不能为空，请重新填写");
			jQuery("#labelName").focus();
			return "false";
		}
		if(typeof(top.junblockUI)!="undefined"){
			top.junblockUI();
		}
		document.form.submit();
		return false;//不加return false;会执行两次action
	}	
	
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="type" id="type" value="labelBean"/>
<input type="hidden" name="labelId" id="labelId" value="$!labelBean.id"/>
<input type="hidden" name="createTime" id="createTime" value="$!labelBean.createTime"/>
<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId"/>
<div class="boxbg2 subbox_w700" style="width:350px;">
<div class="subbox cf" style="height: 30px;border: 0;">
  <div class="bd" style="height: 20px;">
      <div class="inputbox" style="margin-top: 10px;">
        <ul>
          <li><span style="margin-top: 4px;">标签名称：</span><input name="labelName" id="labelName" value="$!labelBean.labelName" type="text" class="inp_w" onKeyDown="if(event.keyCode==13) return beforeSubmit();" />&nbsp;&nbsp; </li>
          <li style="margin-top: 10px;"><span style="margin-top: 2px;">标签颜色：</span>
          	<input type="hidden" name="labelColor" id="labelColor" value="$!labelBean.labelColor"/>
         	<div class="icolorC">
				<div id="colorDiv"><span id="selColor" style="width:14px;height:14px;float:left;margin-top: 3px;background-color: $!labelBean.labelColor;margin-left: 5px;"></span><div id="icolor4" style="height:20px;width:100px;float:left;"><span style="font-size: 13px;width: 70px;float: left;color: #49494A;cursor: pointer;">选择颜色</span><img  src="/style/images/ico2.gif" width="15px" height="15px;" style="float: left;margin-left: -10px;margin-top: 3px;"/></div></div>
			</div>
		</ul>
      </div>
     
  </div>
</div>
</div>
</form>
</body>
<script type="text/javascript">
		$("#icolor4").icolor({
			colors:["b54143", "47a91c", "db8933", "3796bf", "df7ba6", "e5acae",
			"aedfa3", "f3d1a8", "a5daea", "f4c9df", "43bc97", "c7ad24", "cf69e2","9d9d9d", "d9d9d9", "abe7d9", "ece0a5", "efc0f6"],
			col:6,
			onSelect:function(c){this.$tb.css("background-color",c);this.$t.val(c);jQuery("#selColor").css("backgroundColor",c);jQuery("#labelColor").val(c)}
		});	
	</script>
</html>
