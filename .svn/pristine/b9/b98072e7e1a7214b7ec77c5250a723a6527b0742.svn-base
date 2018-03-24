<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
		$("#labelName").focus();
	});
	
	function beforeSubmit(){
		if(!WidthCheck(jQuery("#labelName").val(),8)){
			alert("标签名称长度不能超过4个字符(一个汉字两个字符)");
			jQuery("#labelName").focus();
			return false;
		}
		if(jQuery.trim(jQuery("#labelName").val())==""){
			alert("标签名称不能为空，请重新填写");
			jQuery("#labelName").focus();
			return "false";
		}
		document.form.submit();
		return false;//不加return false;会执行两次action
	}	
	
	function dealAsyn(){
		var str ='<div class="nui-menu-item" onclick="selLabel(this,\''+jQuery("#labelId").val()+'\',\''+jQuery("#labelName").val()+'\',\''+jQuery("#labelColor").val()+'\')">'
        	str +='<div class="nui-menu-item-link">'
            str +='<span class="nui-menu-item-icon"><input id="'+jQuery("#labelId").val()+'_box" type="checkbox"/></span>'
            str +='<span class="nui-menu-item-text"><b class="nui-ico nui-ico-tag" style="background:'+jQuery("#labelColor").val()+';"></b>'+jQuery("#labelName").val()+'</span>'
            str +='</div></div>';
		parent.$("#labelContDiv").append(str);
		
		//回填数据,更新数据库

		var $showLabelLi = parent.$(".col input:checked").parent().find("li[id$='label']");
		var clientId = parent.$(".col input:checked").val();
		if(parent.$("#"+$("#labelShow").attr("liId") + " div").length < 2){
			var labelStr ='<div labelId="'+jQuery("#labelId").val()+'" tabindex="0" class="nui-tag nui-tag-1" style="margin-right:3px;">'
			+'<span onclick="searchLabel(\''+jQuery("#labelId").val()+'\')" onmouseover="showDel(this,\''+jQuery("#labelColor").val()+'\',\'name\')" onmouseout="hideDel(this,\'name\')" class="nui-tag-text" style="background-color:'+jQuery("#labelColor").val()+'">'+jQuery("#labelName").val()+'</span>'
			+'<span id="'+jQuery("#labelId").val()+'" onmouseover="showDel(this,\''+jQuery("#labelColor").val()+'\',\'del\')" onmouseout="hideDel(this,\'del\')" onclick="delLabel(this)" tabindex="0" class="nui-tag-close nui-close" style="display:none;background-color:'+jQuery("#labelColor").val()+'" title="取消标签"><b class="nui-ico" >x</b></span></div>';
			$showLabelLi.append(labelStr)	
			
			var labelIds = "";
			parent.$(".col input:checked").parent().find("li[id$='label']").find("div").each(function(){
				labelIds += $(this).attr("labelId") +",";
			})
			jQuery.ajax({
			   type: "POST",
			   url: "/CRMClientAction.do?operation=2&type=selLabel&labelId="+labelIds +"&clientId="+clientId,
			   success: function(msg){
			   }
			});	
		}
		parent.jQuery.close("addLabelId"); 
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" name="type" id="type" value="addLabel"/>
<input type="hidden" name="labelId" id="labelId" value="$labelId"/>
<input type="hidden" name="moduleId" id="moduleId" value="$moduleId"/>
<div class="boxbg2 subbox_w700" style="width:350px;">
<div class="subbox cf" style="height: 30px;border: 0;">
  <div class="bd" style="height: 20px;">
      <div class="inputbox" style="margin-top: 10px;">
        <ul>
          <li><span style="margin-top: 4px;">标签名称：</span><input name="labelName" id="labelName" type="text" class="inp_w" onKeyDown="if(event.keyCode==13) return beforeSubmit();" />&nbsp;&nbsp; </li>
          <li style="margin-top: 10px;"><span style="margin-top: 2px;">标签颜色：</span>
          	<input type="hidden" name="labelColor" id="labelColor" value="#3796bf"/>
         	<div class="icolorC">
				<div id="colorDiv"><span id="selColor" style="width:14px;height:14px;float:left;margin-top: 3px;background-color: #3796bf;margin-left: 5px;"></span><div id="icolor4" style="height:20px;width:100px;float:left;"><span style="font-size: 13px;width: 70px;float: left;color: #49494A;cursor: pointer;">选择颜色</span><img  src="/style/images/ico2.gif" width="15px" height="15px;" style="float: left;margin-left: -10px;margin-top: 3px;"/></div></div>
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
