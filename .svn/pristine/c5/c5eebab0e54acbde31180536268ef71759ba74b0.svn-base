<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/style4/css/common.css" type="text/css" />
<link rel="stylesheet" href="/style4/css/jquery.Jcrop.css" type="text/css" />
<link rel="stylesheet" href="/style/css/controlPanel.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.Jcrop.js"></script>
<script language="javascript" src="/js/ajaxfileupload.js"> </script>
<style type="text/css">
.crop_preview{position:absolute; left:520px; top:0; width:150px; height:150px; overflow:hidden;}
.crop_preview2{position:absolute; left:514px; top:190px;; width:80px; height:90px; overflow:hidden;}
.crop_preview_a{position:absolute; left:230px; top:225px; width:150px; height:150px; overflow:hidden;}
.crop_preview_b{position:absolute; right:375px; top:240px; width:80px; height:90px; overflow:hidden;}
#headPic div{
	float: left;
	margin-top: 5px;
	margin-left: 150px;
}

</style>

<script type="text/javascript">
	var api
	var api2
	function upload(uploadInputName,flag) {
		jQuery.ajaxFileUpload({
			url:'/FileUpload.do?flag='+flag, //需要链接到服务器地址
			secureuri:false,
			fileElementId:uploadInputName,                         //文件选择框的id属性

            dataType: 'JSON',                                     //服务器返回的格式，可以是json
            success: function (data, status){   
                var data = "/temporarys/" + data;//data是从服务器返回来的图片名称 
				var showDiv = document.getElementById("showDiv");//获得截图的DIV
				if(flag==1){
	                $("#xuwanting").attr("src",data);
	                $("#crop_preview").attr("src",data);
					document.getElementById("xuwanting").style.display = "block";
					document.getElementById("xuwanting2").style.display = "none";
				}
				if(flag==2){
					$("#xuwanting2").attr("src",data);
					$("#crop_preview2").attr("src",data);
					document.getElementById("xuwanting2").style.display = "block";
					document.getElementById("xuwanting").style.display = "none";
				}
				showDiv.style.display = "block";//打开截图DIV
				$("#submitFlag").val(flag);
				$("div").remove(".jcrop-holder");//打开一个JRCOP默认会有增加一个class=jcrop-holder的div,所以要删除
				if(flag ==1){
					api = jQuery.Jcrop('#xuwanting',{
						   setSelect:[100,100,250,250],
						   allowSelect:false,
                           aspectRatio:1/1,
           			       onChange: showPreview,
                           onSelect: showPreview,
           				   });
				}
				if(flag ==2){
					api2 = jQuery.Jcrop('#xuwanting2',{
						   setSelect:[100,100,180,190],
						   allowSelect:false,
                           aspectRatio:8/9,
           			       onChange: showPreviewSamll,
                           onSelect: showPreviewSamll,
           				   });
				}
            },
            error: function (data, status, e){
               alert('上传图片失败');
            }
		});
	}
	
	
	
	//简单的事件处理程序，响应自onChange,onSelect事件，按照上面的Jcrop调用
	function showPreview(coords){
		if(parseInt(coords.w) > 0){
			//计算预览区域图片缩放的比例，通过计算显示区域的宽度(与高度)与剪裁的宽度(与高度)之比得到
			var rx = $("#preview_box").width() / coords.w; 
			var ry = $("#preview_box").height() / coords.h;
			//通过比例值控制图片的样式与显示

			$("#crop_preview").css({
				width:Math.round(rx * $("#xuwanting").width()) + "px",	//预览图片宽度为计算比例值与原图片宽度的乘积
				height:Math.round(rx * $("#xuwanting").height()) + "px",	//预览图片高度为计算比例值与原图片高度的乘积
				marginLeft:"-" + Math.round(rx * coords.x) + "px",
				marginTop:"-" + Math.round(ry * coords.y) + "px"
			});
			$("#x1").val(coords.x);
			$("#y1").val(coords.y);
			$("#x2").val(coords.x2);
			$("#y2").val(coords.y2);
			$("#w").val(coords.w);
			$("#h").val(coords.h);
		}
	}
	
	//简单的事件处理程序，响应自onChange,onSelect事件，按照上面的Jcrop调用
	function showPreviewSamll(coords){
		if(parseInt(coords.w) > 0){
			//计算预览区域图片缩放的比例，通过计算显示区域的宽度(与高度)与剪裁的宽度(与高度)之比得到
			var sx = $("#preview_box2").width() / coords.w; 
			var sy = $("#preview_box2").height() / coords.h;
			//通过比例值控制图片的样式与显示

			$("#crop_preview2").css({
				width:Math.round(sx * $("#xuwanting2").width()) + "px",	//预览图片宽度为计算比例值与原图片宽度的乘积
				height:Math.round(sx * $("#xuwanting2").height()) + "px",	//预览图片高度为计算比例值与原图片高度的乘积
				marginLeft:"-" + Math.round(sx * coords.x) + "px",
				marginTop:"-" + Math.round(sy * coords.y) + "px"
			});
			
			$("#x1").val(coords.x);
			$("#y1").val(coords.y);
			$("#x2").val(coords.x2);
			$("#y2").val(coords.y2);
			$("#w").val(coords.w);
			$("#h").val(coords.h);
		}
	}
	
	
	function cutCancel(){
		var showDiv = document.getElementById("showDiv");
		showDiv.style.display = "none";
		var cutPicSrc = $("#crop_preview").attr("src");
		jQuery.ajax({
		   type: "POST",
		   url: "/ControlPanelAction.do?operation=3&cutPicSrc=" + cutPicSrc,
		   data: "",
		   success: function(msg){
		   }
		});
		
	}
	
	function cutSubmit(){
		var cropCss;
		if($("#submitFlag").val() == 1){
			cropCss = "#crop_preview";
		}else{
			cropCss = "#crop_preview2";
			
		}		
		var width = $(cropCss).css("width");
		var height = $(cropCss).css("height");
		var marginLeft = $(cropCss).css("marginLeft");
		var marginTop = $(cropCss).css("marginTop");
	
		if($("#submitFlag").val() == 1){
			$("#cutBigPic").css({
				width:width,	//预览图片宽度为计算比例值与原图片宽度的乘积
				height:height,	//预览图片高度为计算比例值与原图片高度的乘积
				marginLeft:marginLeft,
				marginTop:marginTop
			});
			$("#cutBigPic").attr("src",$("#crop_preview").attr("src"));
			$("#cutPicSrc").val($("#crop_preview").attr("src"));
			$("#saveBigPicSrc").val($("#crop_preview").attr("src"));
		}
		
		if($("#submitFlag").val() == 2){
		//使头像图片与预览图片一样

			$("#cutSmallPic").css({
				width:width,	//预览图片宽度为计算比例值与原图片宽度的乘积
				height:height,	//预览图片高度为计算比例值与原图片高度的乘积
				marginLeft:marginLeft,
				marginTop:marginTop
			});
			$("#cutSmallPic").attr("src",$("#crop_preview2").attr("src"));
			$("#cutPicSrc").val($("#crop_preview2").attr("src"));
			$("#saveSamllSrc").val($("#crop_preview2").attr("src"));
		}
		var showDiv = document.getElementById("showDiv");
		showDiv.style.display = "none";
		$("#operation").val('1');
		form.submit();
		
	}
	
	function cropRelease(){
	
		var cropCss;
		if($("#submitFlag").val() == 1){
			api.release();
			cropCss = "#crop_preview";
			$(cropCss).css("width","150px");
			$(cropCss).css("height","120px");
		}else{
			api2.release();
			cropCss = "#crop_preview2";
			$(cropCss).css("width","80px");
			$(cropCss).css("height","64px");
		}
		$("#x1").val(0);
		$("#y1").val(0);
		$("#w").val(500);
		$("#h").val(400);
		
		$(cropCss).css("marginLeft","0px");
		$(cropCss).css("marginTop","0px");
	}
	
	function cropEnable(){
		if($("#submitFlag").val() == 1){
			api.setSelect([100,100,250,250]);
		}else{
			api2.setSelect([100,100,180,190]);
		}
	}
	
	
	function addSubmit(){
		$("#operation").val('2');
		form.submit();
	}
	
	function loadBigPic(){
		setTimeout("upload('uploadFileInputBig','1')",100);
	}
	
	function loadSmallPic(){
		setTimeout("upload('uploadFileInputSmall','2')",100);
	}
	
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/ControlPanel.do" method="post" enctype="multipart/form-data" target="formFrame">
<input type="hidden" value="1" name="operation" id="operation"/>
<input type="hidden" id="x1" name="x1"/>
<input type="hidden" id="y1" name="y1"/>
<input type="hidden" id="x2" name="x2"/>
<input type="hidden" name="isnickUpdate" value="1"/>
<input type="hidden" id="y2" name="y2"/>
<input type="hidden" id="w" name="w"/>
<input type="hidden" id="h" name="h"/>
<input type="hidden" name="cutPicSrc" id="cutPicSrc"/>
<input type="hidden" name="saveBigPicSrc" id="saveBigPicSrc" value="$!saveBigPicSrc"/>
<input type="hidden" name="saveSamllSrc" id="saveSamllSrc" value="$!saveSamllPicSrc"/>
<input type="hidden" name="submitFlag" id="submitFlag"/>
<table cellpadding="0" cellspacing="0" class="framework">
		<tr>
			<div style="float: right; margin: -2px;"><input  type="button" value="$text.get("mrp.lb.saveAdd")" onclick="addSubmit();"/></div>
		</tr>
		<tr>
			<!--右边列表开始-->
			<td>
				<div class="data" >
					<div class="data_title"><div class="control_list">昵称  
					</div></div>
					<ul class="column">
						<li><label>昵称:</label><span><input type="text" name="nickName" value="$!userBean.NickName"/></span></li>
						<li>&nbsp;</li>
						<li><label>签名:</label><span><textarea rows="" cols="50" name="signature">$!userBean.Signature   </textarea></span></li>
						
					</ul>
					<div class="column_title"><div class="control_list">头像</div></div>
					<ul class="column">
						<div style="width: 85%;height: 200px;margin: 0 auto;float: left;" id="headPic" >
							<div style="width: 300px;height: 200px;border: 1px solid gray;" >
								<span  class="crop_preview_a">	<img  src="/style4/images/selectCut.jpg" width="150px" height="150px" id="cutBigPic" /></span>
								<div style="width: 300px; margin-left: 30px;margin-top: 170px;">
									&nbsp;<input id="uploadFileInputBig" type="file" name="uploadFileInput" class="input" onclick="loadBigPic()"/> 
								</div>
							</div>
							
							
							<div style="border: 1px solid graytext;width: 300px;height: 200px;" >
								<span  class="crop_preview_b">	
									<img  src="/style4/images/selectCut.jpg" id="cutSmallPic" width="80px" height="90px;"/>
								</span>
								<div style="width: 300px; margin-left: 30px;margin-top: 170px;">
									&nbsp;<input id="uploadFileInputSmall" type="file" name="uploadFileInputSmall" class="input" onclick="loadSmallPic();"/> 
							</div>
							
							
							
							
							</div>
							
						</div>
					</ul>
				</div>
				
			</td><!--右边列表结束-->
		</tr>
		
	</table>

				<div class="zxx_out_box" style="display: none;position: absolute;top: 50px;left: 200px;height: 250px;" id="showDiv">
               		 <div class="rel mb20" style="background-color: graytext;width: 500px; height: 440px;">
	                	<img id="xuwanting"  width="500px" height="400px;" />
	                	<img id="xuwanting2"  width="500px" height="400px;" />
	                	
	                    <span id="preview_box" class="crop_preview" ><img id="crop_preview" src="xuwanting.jpg" style="display: none;"/></span>
	                    <span id="preview_box2" class="crop_preview2" ><img id="crop_preview2" src="xuwanting.jpg" style="display: none;"/></span>
	                    <div style="width: 300px;height: 50px;margin-left: 90px;margin-top: 10px;">
                    	<input type="button" value="启用选区" onclick="cropEnable()"/>&nbsp;&nbsp;
                    	<input type="button" value="选择全图" onclick="cropRelease()"/>&nbsp;&nbsp;
                    	<input type="button" value="确认" onclick="cutSubmit()"/>&nbsp;&nbsp;
                    	<input type="button" value="关闭" onclick="cutCancel()"/>
                    </div>
                </div>           
</div>


</form>

</body>
</html>
