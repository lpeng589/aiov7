<style type="text/css">

#a{
	font-size: 14px;
}
#a b{
	float: left;
	
}
</style>
<script type="text/javascript" >
function mailprint(){
	//parent.location.reload();
	//window.parent.parent.shu=1;
	window.parent.print();
}
window.onload=function(){
	//if(window.parent.parent.shu==1){
	//	window.setTimeout("xing()",500);
	//}
}

function xing(){
	//window.parent.print();
	//window.parent.parent.shu=0;
	//window.parent.parent.xinghou();
}
</script>

<div id="a">
$result.mailContent
</div>
	 #if("$!result.mailAttaches" != "")
	#foreach($att in $result.mailAttaches.split(",|;"))
	
  #if($att.toLowerCase().indexOf(".bmp")>0 ||$att.toLowerCase().indexOf(".jpg")>0 ||$att.toLowerCase().indexOf(".jpeg")>0||$att.toLowerCase().indexOf(".gif")>0||$att.toLowerCase().indexOf(".png")>0)
  	#if($att.indexOf("&fileName=")> 0)
  	<img src="/ReadFile?tempFile=email&$att.substring(0,$pp)$!globals.encode($att.substring($pp))"><br/>
  	#else
  	<img src="/ReadFile?tempFile=email&emlfile=$!result.emlfile&charset=$!result.mailcharset&attPath=$attPath&fileName=$!globals.encode($att)"><br/>
  	#end  
  #end
	#end
	#end