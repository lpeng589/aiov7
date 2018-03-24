<html>
<head>
<meta name="renderer" content="webkit">
<title>图片管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="/vm/oa/album/ui/tongda/theme/11/style.css">
<!-- <script src="/vm/album/ui/tongda/ccorrect_btn.js"></script> -->
<script type="text/javascript" src="/js/jquery.js"></script>
<SCRIPT LANGUAGE=javascript>
		/*
		$(window).bind('load', function() {
			$('img').bind("contextmenu", function(e){ return false; })
		});
		*/
		function stop(){ 
			return false; 
		} 
		document.oncontextmenu=stop; 
</SCRIPT> 
<script language="javascript">
	//封装为数组
	var listPhos = "$!phos";
	var FILE_ATTR_ARRAY = new Array();
	#foreach ($pho in $!phos)
		var No = "$!pho.tNo";
		var tempName = "$!pho.tempName";
		var path = "$!pho.path";
		var size = "$!pho.FileSize";
		var width = "$!pho.width";
		var height = "$!pho.height";
		var lastUpdateTime = "$!pho.lastUpdateTime";
		var arr = new Array(No,tempName,lastUpdateTime,size,width,height,path);
		FILE_ATTR_ARRAY.push(arr);
	#end
</script>
<script language="javascript">
	//图片数组游标 
	var cur_pic_no=0;  
	//放大
	var up_width,up_height;  
	function blowup() { 
		mywidth=parent.open_main.image.width; 
		myheight=parent.open_main.image.height; 
		up_width=mywidth * 1.1; 
		up_height=myheight * 1.1;
		parent.open_main.image.width=up_width;
		parent.open_main.image.height=up_height; 
	}
	//缩小 
	function reduce() {  
		mywidth=parent.open_main.image.width; 
		myheight=parent.open_main.image.height;
		up_width=mywidth * 0.9;  
		up_height=myheight * 0.9;
		parent.open_main.image.width=up_width;
		parent.open_main.image.height=up_height; 
	}  
	//最适合  实际大小 
	function adapt(flag) {  
		parent.open_main.pictable.style.zoom="75%"; 
		true_width = FILE_ATTR_ARRAY[cur_pic_no][4]; 
		true_height = FILE_ATTR_ARRAY[cur_pic_no][5] 
		clientWidth = parent.open_main.document.body.clientWidth; 
		clientHeight = parent.open_main.document.body.clientHeight; 
	  
	  	if (flag == 1) { //实际大小
		    up_width=true_width;  
		    up_height=true_height; 
	  	} else if (flag == 2) { //最适合   
	   		padbottom = 50;   
	        if (true_width > clientWidth && true_height <= clientHeight) { 
	            up_width=clientWidth;    
	            up_height=true_height*clientHeight/true_width - padbottom; 
	     	}
	   		if (true_height > clientHeight && true_width <= clientWidth) { 
				up_height=clientHeight - padbottom;
				up_width=true_width*clientHeight/true_height; 
	   		} 
	   		if (true_width > clientWidth && true_height > clientHeight) {
				if (true_width >= true_height) {
				   	up_width=clientWidth; 
				  	up_height=true_height*clientWidth/true_width - padbottom;   
				} else { 
					up_height=clientHeight - padbottom;  
					up_width=true_width*clientHeight/true_height; 
				}
			}
			if (true_width < clientWidth && true_height < clientHeight) {
				up_height=true_height;     
				up_width=true_width;
			} 
		  	if (true_width < clientWidth && true_height > clientHeight) {  
		    	up_height=clientHeight - padbottom;  
		    	up_width=true_width*clientHeight/true_height;   
		    } 
	    }  
		parent.open_main.image.width =up_width;  
		parent.open_main.image.height =up_height; 
	 }
   
   function inionload() { 
   	 top.frames["open_control"].focus();//获得frame的名字,通过聚焦用于处理键盘左右能控制图片

   	 var tempName = decodeURI("$!tempName");
   	 for (var i = 0; i < FILE_ATTR_ARRAY.length; i++) { 
		if (FILE_ATTR_ARRAY[i][1] == tempName){
			cur_pic_no = FILE_ATTR_ARRAY[i][0];
			break;
		}
   	 }
   	 file_name = FILE_ATTR_ARRAY[cur_pic_no][1];   
     file_path = FILE_ATTR_ARRAY[cur_pic_no][6];
   	 if (typeof(parent.open_main.div_image) == "object") {
	 	 //方法1：但是因为没有图片的大小等参数导致显示出现了一点问题parent.open_main.div_image.innerHTML = "<img onload='parent.open_control.adapt(2);' src='/ReadFile?tempFile=path&path="+decodeURI("$pId")+"&fileName="+tempName+"&albumType=tree' alt='鼠标滚轮缩放，点击图片翻页' border=0 id='image' width=1 height=1>";  
		 parent.open_main.div_image.innerHTML = "<img onload='parent.open_control.adapt(2);' src='/ReadFile?tempFile=path&path="+encodeURIComponent(file_path)+"&fileName="+encodeURIComponent(file_name)+"&albumType=tree' alt='鼠标滚轮缩放，点击图片翻页' border=0 id='image' width=1 height=1>";  
		 parent.open_main.file_name.innerText = tempName;  
	 	 parent.open_main.pictable.style.zoom = "100%";  
   	 }
   	  
   }
   
   function open_pic(op) { 
		cur_pic_no = parseInt(cur_pic_no)+op; 
		if (parseInt(cur_pic_no) <= -1)
		  cur_pic_no = FILE_ATTR_ARRAY.length - 1;  
		else if (parseInt(cur_pic_no) >= FILE_ATTR_ARRAY.length)  
		  cur_pic_no = 0; 
		file_name = FILE_ATTR_ARRAY[cur_pic_no][1];   
		file_path = FILE_ATTR_ARRAY[cur_pic_no][6];
		parent.open_main.image.src = "/ReadFile?tempFile=path&path="+encodeURIComponent(file_path)+"&fileName="+encodeURIComponent(file_name)+"&albumType=tree";
		parent.open_main.file_name.innerText = file_name;   
		parent.open_main.pictable.style.zoom = "75%"; 
   }
   
   function down_pic() { 
   		var isNeedDebar = false;//后台是否需要排除包含该照片列表
        var file_path = FILE_ATTR_ARRAY[cur_pic_no][6];
        var file_name = FILE_ATTR_ARRAY[cur_pic_no][1]; 
   		var url1="/PublicServlet?operation=downLoadFiles&downName="+encodeURIComponent(file_name)+"&path=$globals.encode($!parpath)&pho="+encodeURIComponent(file_name)+"&isNeedDebar="+isNeedDebar;
   		$("#downLoadId").attr("href",url1);
   		
   }
   
   function exif_pic() {
     //window.open("展示图片信息的后台?PIC_ID=4&SUB_DIR=&FILE_NAME="+parent.open_main.file_name.innerText+"&VIEW_TYPE=NAME&ASC_DESC=4","图片信息","height=400, width=400, left=440, top=200, toolbar =no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"); 
   }
   
 </script>
</head>
<body topMargin="0" leftMargin="0" style="background-image: url(/vm/album/ui/tongda/images/topbar.gif);" onload="inionload()">
<table width="100%" align="center" class="small" border="0" cellSpacing="0" cellPadding="2">
	<tbody>
		<tr>
			<td align="center" vAlign="middle">
				<span style="padding-top: 2px;">
					#if($!phos.size()>1)
					<a href="javascript:open_pic(-1);"><img width="48" height="48" title="上一张" id = "a_idd" src="/vm/oa/album/ui/images/p_01.gif" border="0" complete="complete"/></a>
					<a href="javascript:open_pic(1);"><img width="48" height="48" title="下一张" id="a_id" src="/vm/oa/album/ui/images/p_02.gif" border="0" complete="complete"/></a>
					#end
					<a href="javascript:adapt(2);"><img width="48" height="48" title="最适合" src="/vm/oa/album/ui/images/adapt.png" border="0" complete="complete"/></a>
					<a href="javascript:adapt(1);"><img width="48" height="48" title="实际大小" src="/vm/oa/album/ui/images/original.png" border="0" complete="complete"/></a>
					<a href="javascript:blowup()"><img width="48" height="48" title="放大" src="/vm/oa/album/ui/images/p_06.gif" border="0" complete="complete"/></a>
					<a href="javascript:reduce();"> <img width="48" height="48" title="缩小" src="/vm/oa/album/ui/images/p_07.gif" border="0" complete="complete"/></a>
					#if("$!onDown"!="false")
					<a href="#" onclick="down_pic()" id="downLoadId"><img width="48" height="48" title="保存图片" src="/vm/oa/album/ui/images/p_05.gif" border="0" complete="complete"/></a>
					#end
					<!-- down_pic如果写在 href里面 不能下载 具体原因未知 -->
					<!--
						 <a href="javascript:exif_pic();"><img width="48" height="48" title="照片信息" src="/vm/album/ui/images/sms_type40.gif" border="0" complete="complete"/></a>
				 	-->
				</span>
			</td>
		</tr>
	</tbody>
</table>


<script type="text/javascript">
	function keyDown(e) { 
		var iekey=event.keyCode; 
		action(iekey);
		} 
	document.onkeydown = keyDown; 
	function action(iekey) { 
		if(iekey==37) {
			open_pic(-1);
		} 
		if(iekey==39) { 
			open_pic(1);
		} 
	}
</script> 
</body>
</html>