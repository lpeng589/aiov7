<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery(':text:eq(0)').focus();
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI(); 
	}
});

function toPath(path){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}	
	$("#picPath",document).val(path);	
	document.form.submit();
	event.stopPropagation();
}
function sort(){
	document.form.submit();
}
function chSel(id){
	if($("#"+id,document).attr("checked")=="checked"){
		$("#"+id,document).attr("checked",false);
	}else{
		$("#"+id,document).attr("checked",true);
	}
}

function doSelected(obj){
	   str = $(obj).find("input[name=files]").val();
	   var rootp = rootPath;
	   if(rootp.substring(rootp.length-1)!="/"){
	   	rootp = rootp + "/";
	   }
	   str = "PICPATH:"+ str.substring(rootp.length);				   	   
	   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+parent.selectPicHiddenName+" value='"+str+"'>"+
   	   	"<div><a style=\"cursor:pointer;\" href='javascript:void(0);' onclick='deleteupload(\""+str+"\",\"false\",\""+parent.$("#tableName").val()+"\",\"PIC\");'>$text.get("common.lb.del")</a><em>"+str.substring(8)+"</em></div><div><a href=\"/ReadFile?type=PIC&fileName="+encodeURI(str)+"\" target=\"_blank\"><img src=\"/ReadFile?type=PIC&fileName="+encodeURI(str)+"\"  height=\"120\"  border=\"0\"></a></div></li>";
   	   parent.selectPicUl.append(imgstr);
   	   parent.jQuery.close("popSelectPic");
}


function openAttachUpload(){
	var filter = "Image";
	var type="PIC";
	var btnId="";
	var attachUpload = document.getElementById("attachUpload");
	if(attachUpload == null){
		uploadDiv = document.createElement("div"); 
		uploadDiv.id = "attachUpload" ;
		uploadDiv.style.cssText = "position:absolute;top:10px;left:30px;width:600px;height:300px;display:block;z-index:100";
		document.body.appendChild(uploadDiv);
		attachUpload = document.getElementById("attachUpload");
	}
	var clientHeight = document.documentElement.clientHeight;
	if(clientHeight==0) {
		clientHeight = document.body.clientHeight ;
	}
	attachUpload.style.left=  ((document.body.clientWidth - 500) /2) +"px";
	attachUpload.style.top=  ((clientHeight - 250) /2) +"px";
	attachUpload.style.display="block";
	attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
			' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="maxSize=$!globals.getSysSetting("defaultAttachSize")&uploadUrl=/UploadServlet;jsessionid=$session.id?path=$!globals.getSysSetting("picPath").replaceAll("\\","/")@amp;fieldName='+btnId+'@amp;fileType='+type+'&filter='+filter+'&btnId='+btnId+'" />'+
			' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7"	width="500" height="250" name="column" align="middle" play="true" loop="false"'+
			'	flashvars="maxSize=$!globals.getSysSetting("defaultAttachSize")&uploadUrl=/UploadServlet;jsessionid=$session.id?path=$!globals.getSysSetting("picPath").replaceAll("\\","/")@amp;fieldName='+btnId+'@amp;fileType='+type+'&filter='+filter+'&btnId='+btnId+'"	quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
} 
function onCompleteUpload(retstr,btnId){   
	window.location.href=window.location.href;
}

var rootPath = '$!rootPath.replaceAll("\\","/")';

</script>
<style type="text/css">
body {
  margin: 0px;
  width: 100%;
  font-size: 12px;
  background: #fff;
  font-family: "微软雅黑","Microsoft YaHei";
}
.showCm li{
float: left;
list-style-type: none;
text-align: center;
margin: 5px;
padding: 0px;
width: 200px;
height: 200px;
}
.showCm li img{
display: block;
width: 200px;
height: 180px;
border:none;
}
.showCm li span{
overflow: hidden;
text-overflow: ellipsis;
display: block;
}
.showCm li div label{
max-width: 170px;
overflow: hidden;
text-overflow: ellipsis;
display: inline-block;
}

.filea { text-decoration:none; }
.filea :hover{ text-decoration:none;  }
</style>
</head>
<body>
<form  method="post"  scope="request" name="form"  action="/UserFunctionAction.do?&operation=$globals.getOP("OP_SELECT_PIC")" >
<div style="text-align:right ; border-bottom: 1px solid #DAD2DA;  padding-bottom: 5px;">
<input type="hidden" name="picPath"  id="picPath" value="$!picPath"/>
<span>
<input type="text" name="search" disableautocomplete="true" autocomplete="off"   id="search" value="$!search" onkeydown="if(event.keyCode==13){sort()}" />
<input type="checkbox" name="searchChild" id="searchChild" #if("$!searchChild" == "1") checked #end value="1"> 
<label for="searchChild">搜索子目录</label>
<input type="button" value="搜索" onclick="sort()"/>
</span>
<span>
	<label>排序</label>
	<select  name="sortName"  id="sortName" onchange="sort()">
		<option value="name" #if("$!sortName"=="name") selected #end>名称</option>
		<option value="date" #if("$!sortName"=="date") selected #end>日期</option>
		<option value="type" #if("$!sortName"=="type") selected #end>类型</option>
	</select>
	<select  name="asc"  id="asc" onchange="sort()">
		<option value="asc" #if("$!asc"=="asc") selected #end>升序</option>
		<option value="desc" #if("$!asc"=="desc") selected #end>降序</option>
	</select>
	<select  name="detail"  id="detail" onchange="sort()">
		<option value="snt" #if("$!detail"=="snt") selected #end>缩略图</option>
		<option value="detail" #if("$!detail"=="detail") selected #end>详情</option>
	</select>
</span>
#if("$!backPath" != "" )
<input type="button" value="返回上级" onclick="toPath('$!backPath.replaceAll("\\","/")')"/>
#end
</div>
</form>
#set($rootLength = $!rootPath.length())  
#if( !$!rootPath.endsWith("/")) #set($rootLength = $rootLength +1)  #end
#if("$!detail" != "detail")
	<ul class="showCm"> 
	#foreach($row in $files)  
		<li #if(!$row.isDirectory()) ondblclick="doSelected(this)" #end title="$row.getAbsolutePath().substring($rootLength).replaceAll("\\","/")">
		#if($row.isDirectory())
		<img src="/style/images/folder.png" width="100" height="100" onclick="toPath('$row.getAbsolutePath().replaceAll("\\","/")')" border=0/>
		<span>$row.name</span>
		#else
		<img src="/ReadFile.jpg?fileName=DISK$row.getAbsolutePath().replaceAll("\\","/")" width="100" height="100" border=0 onclick="chSel('files_$velocityCount')"/>
		<div><input type="checkbox" name="files" id="files_$velocityCount" value="$row.getAbsolutePath().replaceAll("\\","/")"> 
		<label for="files_$velocityCount"> $row.name  </label><div>
		#end
		
		</li>
	#end
	</ul>
#else	
	<table width="95%">
		<thead><tr style="border-bottom: 1px solid #ff00ff;"><td>名称</td><td width="200px">日期</td><td width="100px">大小</td></tr></thead>
		<tbody>
	#if("$!backPath" != "" )	
		<tr  style="border-bottom: 1px solid #ff00ff;">
		<td>
		<a  class="filea" href="javascript:toPath('$!backPath.replaceAll("\\","/")')"> 返回上级 </a>
		</td>
		<td></td>
		<td></td>
		</tr>
	#end
		
	#foreach($row in $files) 
	#set($s = $row.length()/1024)
		<tr  style="border-bottom: 1px solid #ff00ff;">
		<td  #if(!$row.isDirectory()) ondblclick="doSelected(this)" #end>
		#if($row.isDirectory())
		<a class="filea" href="javascript:toPath('$row.getAbsolutePath().replaceAll("\\","/")')"> $row.getAbsolutePath().substring($rootLength).replaceAll("\\","/") </a>
		#else  <input type="checkbox" name="files" id="files_$velocityCount" value="$row.getAbsolutePath().replaceAll("\\","/")"> 
		<label for="files_$velocityCount"> $row.getAbsolutePath().substring($rootLength).replaceAll("\\","/")  </label>
		#end</td>
		<td>$globals.formatDate( $row.lastModified() )</td>
		<td>$s K</td>
		</tr>
	#end		
		</tbody>
	</table>
#end	
</body>

</html>