<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.deskTop.myHeadportrait")</title>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.Jcrop.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="/js/oa/myDesk/ajaxfileupload.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="Javascript">
jQuery(window).load(function(){
    jQuery('#cropbox').Jcrop({
        onChange: showPreview,
        onSelect: showPreview,
        aspectRatio: 1,    
        keySupport:true,
        sideHandles:true, 
        setSelect:[30,45,110,125]            
    });
    var picWith=$("#cropbox").width();
    var picHeight=$("#cropbox").height();
    function showPreview(coords){
        jQuery("#x").val(coords.x);
        jQuery("#y").val(coords.y);
        jQuery("#x2").val(coords.x2);
        jQuery("#y2").val(coords.y2);
        jQuery("#w").val(coords.w);
        jQuery("#h").val(coords.h);
        if (parseInt(coords.w) > 0){
            var rxBig = 140 / coords.w;
            var ryBig = 140 / coords.h;

            var rxSmall = 48 / coords.w;
            var rySmall = 48 / coords.h;

            jQuery('#previewBig').css({
                width: Math.round(rxBig * picWith) + 'px',
                height: Math.round(ryBig * picHeight) + 'px',
                marginLeft: '-' + Math.round(rxBig * coords.x) + 'px',
                marginTop: '-' + Math.round(ryBig * coords.y) + 'px'
            });

            jQuery('#previewSmall').css({
                width: Math.round(rxSmall * picWith) + 'px',
                height: Math.round(rySmall * picHeight) + 'px',
                marginLeft: '-' + Math.round(rxSmall * coords.x) + 'px',
                marginTop: '-' + Math.round(rySmall * coords.y) + 'px'
            });
        }
    }
});

</script>
<style type="text/css">
body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,select,legend,button,input,textarea,th,td {margin:0;padding:0;}
body,button,input,select,textarea {font:12px/1.5 Microsoft Yahei;color:#666;outline:0;}
img{border:0;}
em,i{font-style:normal;}
b{font-weight:normal;}
li{list-style:none;}body{font:12px/1.5 Microsoft Yahei;}
#imageFile{font-family:Microsoft Yahei;width:150px;float:left;}
#upload{clear:both;overflow:hidden;padding:15px 30px;}
#upload>span{float:left;display:inline-block;color:red;}
.hBtns{padding:1px 12px;font-weight:bold;cursor:pointer;display:inline-block;height:21px;line-height:21px;color:#666;border:1px #bbb solid;border-radius:3px;}
.hBtns:hover{background:#f2f2f2;}
.d-btn{padding:0 30px;}
.d-btn>span{float:right;margin-left:5px}
.d-250{width:180px;height:180px;overflow:hidden;float:left;margin:30px 30px 0 30px;border:1px #bbb solid;text-align:center;}
.d-140{width:140px;height:140px;overflow:hidden;margin-bottom:20px;border: 1px #bbb solid;}
.d-48{width:48px;height:48px;overflow:hidden;border: 1px #bbb solid;}
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:10000;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}

</style>
</head> 
<body style="overflow:hidden;">
	
<div class="d-250">
	#if("$!eixtHere" == "NO")
		<img  style="width:180px;cursor: pointer;" src="/style/images/defult_1.jpg" onclick="openFile();"/>
	#else	
		#if("$!updateFlag" != "update")	   			
		<img  style="width:180px;cursor: pointer;" src="/style/images/defult_1.jpg" onclick="openFile();" />
		#else
		<img  #if("$!whFlag" == "yes")style="width:180px;"#else style="height:180px;" #end src="/ReadFile.jpg?fileName=$!myPhoto&tempFile=true&tableName=temp" id="cropbox" />
		#end
	#end
</div>
<div style="float:left;display:inline-block;margin-top:30px;">
   <div class="d-140">
   		#if("$!eixtHere" == "NO")
		 <img  style="width:180px;" src="" id="previewBig" />
		#else
		   #if("$!updateFlag" != "update")
		    <img  src="$!myPhoto_140" id="previewBig" />
		   #else
	   		<img  src="/ReadFile.jpg?fileName=$!myPhoto&tempFile=true&tableName=temp" id="previewBig" />
	   		#end
	   	#end
   </div>
   <div class="d-48">
   		#if("$!eixtHere" == "NO")
		 <img  style="width:180px;" src="" id="previewSmall" /> 
		#else
		   #if("$!updateFlag" != "update")
		    <img  src="$!myPhoto_48" id="previewSmall" />
		    #else
	   		<img  src="/ReadFile.jpg?fileName=$!myPhoto&tempFile=true&tableName=temp" id="previewSmall" />
	   		#end
	   	#end
   </div>
</div>
 <div id="hideBg" class="hideBg"></div> 
<form id="upload" name="upload" method="post" action="/MyDesktopAction.do?operation=53&uploadFlag=ajaxType&type=$!type&empId=$!empId" enctype="multipart/form-data">		
	<input id="imageFile" name="imageFile" type="file" onchange="FileUpload_onselect();"  style="width: 65px;" />
	<span>仅支持JPG,PNG,BMP图片</span>
</form>
   <form name="form" id="form" action="/MyDesktopAction.do">
   <input type="hidden"  id="operation" name="operation" value="54" /> 
   <input type="hidden"  id="loginId" name="loginId" value="$!loginId" />
   <input type="hidden" size="4" id="x" name="x" value="30"/>
   <input type="hidden" size="4" id="y" name="y" value="45" />
   <input type="hidden" size="4" id="x2" name="x2" value="110" />
   <input type="hidden" size="4" id="y2" name="y2" value="145" />
   <input type="hidden" size="4" id="w" name="w" value="80" />
   <input type="hidden" size="4" id="h" name="h" value="80" />
   <input type="hidden" id="type" name="type" value="$!type" /> 
   <input type="hidden" id="empId" name="empId" value="$!empId" /> 
   </form>
   <div class="d-btn">
   	   <span class="hBtns" onclick="resetAll();">取消</span>
   	   <span class="hBtns" onclick="allSubmit();">保存</span>
  </div>
   <div id="closeDiv" style='width:135px;height:102px;position:absolute;left:180px;top:120px;display:none;z-index:10001;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在上传，请稍后。。。</div></div>
<script type="text/javascript">
	
	function openFile(){	
		document.getElementById("imageFile").click();
	}

	function FileUpload_onselect(){
		upload.submit();
	}
	
	function beforSubmit(){   	
		parent.jQuery.close('deskTopId');	
	}
	
	function resetAll(){
		beforSubmit();
	}
	
	function allSubmit(){		
		if(jQuery("#x").val()==""){
			asyncbox.tips('还没开始截图','error');
			return false;
		}
		jQuery("#hideBg").show();
		jQuery("#closeDiv").show();
		jQuery.ajax({
			type:"post",
			url:"/MyDesktopAction.do",
			data:{
				operation:jQuery("#operation").val(),
				x:jQuery("#x").val(),
				y:jQuery("#y").val(),
				w:jQuery("#w").val(),
				h:jQuery("#h").val(),
				divH:jQuery("#cropbox").height(),
				divW:jQuery("#cropbox").width(),
				type:jQuery("#type").val(),
				empId:jQuery("#empId").val()
			},
			success:function(msg){
				if(msg=="1"){					
					
					parent.location.reload();
					asyncbox.tips('保存成功','success');
					beforSubmit();										
				}else{
					jQuery("#closeDiv").hide();
					jQuery("#hideBg").hide();
					asyncbox.tips('保存失败','error');
				}
			}
		})
		//form.submit();
	}
</script>    
</body>
</html>
