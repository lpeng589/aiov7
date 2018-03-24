<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>标尺打印</title>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">

body{font-size: 12px; font-family: Microsoft Yahei; text-decoration: none;}

/*边框阴影*/

</style>
<script type="text/javascript"> 
	function sprint(){	
		$("#firstId").remove();
		$("#prsetup").remove();		
		window.print();
	}
</script>
</head>
<body >
  
	#if("$!flag_print" == "true")
	#foreach($log in $!paramRet)	
	#set( $row = $math.mul("$!globals.get($log.split(','),1)","8"))	
	#set( $pow = $math.mul("$!globals.get($log.split(','),2)","16.5"))
	#if("$!globals.get($log.split(','),3)" != "")
	#set( $wid = $math.mul("$!globals.get($log.split(','),3)","8"))
	#else
	#set( $wid = "")
	#end
	#if("$!globals.get($log.split(','),4)" != "")
	#set( $hgt = $math.mul("$!globals.get($log.split(','),4)","16.5"))
	#else
	#set( $hgt = "")
	#end	
	#set($font = $!globals.get($log.split(','),5))
	<div style="position:absolute;left:${row}px;top:${pow}px;font-size:${font}pt;word-break:break-all;#if("$!wid" != "")width:${wid}px;#end #if("$!hgt" != "")height:${hgt}px;#end">$!globals.get($log.split(','),6)</div>
	#end
	#else
	<!-- 标 -->
	#foreach($log in [0..80])	
	#set( $bar = $log *8 )
	#set( $ba = $log / 10 )	
	<div class="bar" style="position:absolute;left:${bar}px;top:0;">#if($log == "1" || $log == "11" || $log == "21" || $log == "31" || $log == "41" || $log == "51" || $log == "61" || $log == "71" ) $ba #end</div>
	#end
	
	<div class="bar" style="position:absolute;left:0;top:15px;">0</div>
	#foreach($log in [1..80])
	#set( $bar = $log *8 )	
	#set( $ba = $log % 10 - 1)		
	<div class="bar" style="position:absolute;left:${bar}px;top:15px;">#if($ba=="-1") 9 #else $ba #end</div>	
	#end	
	
	#foreach($log in [2..25])
	
	#set( $row = $log *15)	
	#set( $woe = ($log - 1) % 10)
	<div class="bar" style="position:absolute;left:0;top:${row}px;">#if($log < 11)#elseif($log < 21 && $log >= 11) 1 #elseif($log < 31 && $log >= 21)2#elseif($log < 41 && $log >= 31)3 #end</div>
	<div class="bar" style="position:absolute;left:8px;top:${row}px;">$woe</div>
	#foreach($log in [2..80])
	#set( $bar = $log *8 )				
	<div class="bar" style="position:absolute;left:${bar}px;top:${row}px;">#if($log=="6" || $log=="11" || $log=="16" || $log=="21" || $log=="26" || $log=="31" || $log=="36" || $log=="41" 
	|| $log=="46" || $log=="51" || $log=="56" || $log=="61" || $log=="66" || $log=="71" || $log=="76") ■ #else □ #end</div>	
	#end
	#end
	
	#end
	<div class="btn btn-danger btn-mini" style="position:absolute;left:245px;top:96px;z-index:101;" id="firstId" onclick="sprint();">打印</div>
 	<div id="prsetup"  class="sha" style="overflow:hidden; position:absolute; z-index:100;left:80px;top:30px;width:350px;height:450px;background:#FFF;border:3px #000 solid;">
 	<iframe width="100%" height="450px" src="/CRMPrintSetAction.do?operation=15" frameborder="0" scrolling="0"  ></iframe></div>
	     
</body>
</html>
