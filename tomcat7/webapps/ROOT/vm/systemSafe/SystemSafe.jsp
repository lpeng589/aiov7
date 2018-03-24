<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"  />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/tab.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">


function beforeSubmit(){
	backDay = $("input[name=backDay]").val();
	if(backDay==""){
		alert("备份保存时间不能为空");
		return;
	}
	if(!isInt(backDay)){
		alert("备份保存时间必须是整数");
		return;
	}
	if($("input[name=dayPiece]:checked").size()==0){
		alert("碎片整理至少一周执行一次");
		return;
	}
	document.form.submit();
}
#if($msg != '')
	alert('$msg');
#end
</script>
<style type="text/css">
/*主要控制参数*/
.title_font{font-size:16px;font-weight:bold;padding:10px 0 0 20px;display:block;}
.wp_ul {overflow:hidden;min-width:900px;width:100%;}
.wp_ul {margin:2px 0 0 0;padding:8px 0;}
.wp_ul .lt_lb{float:left;white-space: normal;width:300px;line-height:25px;display:inline-block;text-align: right;margin:0px;height:60px; }
.wp_ul .rt_sp{float:left;min-width:550px;width:65%;padding-left:8px;text-align:left;min-height:60px; overflow:hidden;}
.wp_ul .rt_sp input{margin:0;}
.wp_ul .rt_sp .con_input{margin-left:20px;}
.wp_ul .rt_sp .con_input input{width: 30px;}
.wp_ul .rt_sp .ck_box{margin:6px 0 0 0;}
.wp_ul .rt_sp .ck_box_alert{margin:6px 0 0 0;}
.wp_ul .rt_sp .txt_sp{display:block;margin:5px 0 0 0;color:#a1a1a1;}

.hr_link{height:1px;border-top:1px #ebebeb solid;}
.conSelected{color:#FF00CC; }
.curconSelected{ color:red;}

/*用于jquery选择*/
.conDiv{}
.txt_jx{padding-left: 30px;}
.ts_div{overflow: hidden;}
.del_div{padding: 20px 0 0 0}
.del_div img{padding-left: 10px;}
.ts_div select{width:120px;}
.div_name{float: left;}

#queryi{border:1px #ccc solid;border-right:0;width:105px;height:21px;border-radius:3px 0 0 3px;padding:0 5px;float:left;}
.s-span{display:inline-block;float:left;background:#eee;background-image:-webkit-linear-gradient(top,#f9f9f9,#f2f2f2);background-image:linear-gradient(top,#f9f9f9,#f2f2f2);}
.s-span b{width:21px;height:21px;background-image:url(/style/images/item/icons.png);background-repeat:no-repeat;display:inline-block;cursor:pointer;border-top:1px #ccc solid;border-bottom:1px #ccc solid;}
.s-down b{background-position:-294px -213px;border-left:1px #ccc solid;}
.s-up b{background-position:-326px -213px;border-right:1px #ccc solid;}
.savep{float:left;margin:0 0 0 5px;}
</style>
</head>

<body onLoad="showtable('tblSort');showStatus(); $('#queryi').focus(); #if("$!page" != "")goLocal('$!page');#end">

<form  method="post" scope="request" name="form" action="/SystemSafeAction.do" >
<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')" />
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
<input type="hidden" name="winCurIndex" id="winCurIndex" value="$!winCurIndex"/>
<input type="hidden" name="save"  value="true"/>
<div class="Heading" id="head">
	<div class="HeadingTitle">系统安全</div>
	
</div>
	<DIV id="con" style="min-width:960px;width:90%;margin:0 auto;margin-top:10px;overflow-y: auto;">

		
		<div id="sd_remoteSet" class="conDiv">
			<font class="title_font">数据备份	</font>
		 	<div class="wp_ul">
	 			<span class="rt_sp" >
		 			<span class="txt_sp">
		 				&nbsp;&nbsp;&nbsp;&nbsp;当您的系统遭遇断电或其它原因引起的系统崩溃，数据的丢失将是灾难性的。<br/>
		 				&nbsp;&nbsp;&nbsp;&nbsp;数据备份可最大限度的为您保障数据安全。<br/>
		 				&nbsp;&nbsp;&nbsp;&nbsp;本系统为保障安全，要求每天至少备份一次或多次。
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">第一备份时间点：</label>
	 			<span class="rt_sp">
		 			<select name="hour1">
		 				<option value="00" #if("$!result.hour1"=="00") selected #end>00</option>
		 				<option value="01" #if("$!result.hour1"=="01") selected #end>01</option>
		 				<option value="02" #if("$!result.hour1"=="02") selected #end>02</option>
		 				<option value="03" #if("$!result.hour1"=="03") selected #end>03</option>
		 				<option value="04" #if("$!result.hour1"=="04") selected #end>04</option>
		 				<option value="05" #if("$!result.hour1"=="05") selected #end>05</option>
		 				<option value="06" #if("$!result.hour1"=="06") selected #end>06</option>
		 				<option value="07" #if("$!result.hour1"=="07") selected #end>07</option>
		 				<option value="08" #if("$!result.hour1"=="08") selected #end>08</option>
		 				<option value="09" #if("$!result.hour1"=="09") selected #end>09</option>
		 				<option value="10" #if("$!result.hour1"=="10") selected #end>10</option>
		 				<option value="11" #if("$!result.hour1"=="11") selected #end>11</option>
		 				<option value="12" #if("$!result.hour1"=="12") selected #end>12</option>
		 				<option value="13" #if("$!result.hour1"=="13") selected #end>13</option>
		 				<option value="14" #if("$!result.hour1"=="14") selected #end>14</option>
		 				<option value="15" #if("$!result.hour1"=="15") selected #end>15</option>
		 				<option value="16" #if("$!result.hour1"=="16") selected #end>16</option>
		 				<option value="17" #if("$!result.hour1"=="17") selected #end>17</option>
		 				<option value="18" #if("$!result.hour1"=="18") selected #end>18</option>
		 				<option value="19" #if("$!result.hour1"=="19") selected #end>19</option>
		 				<option value="20" #if("$!result.hour1"=="20") selected #end>20</option>
		 				<option value="21" #if("$!result.hour1"=="21") selected #end>21</option>
		 				<option value="22" #if("$!result.hour1"=="22") selected #end>22</option>
		 				<option value="23" #if("$!result.hour1"=="23") selected #end>23</option>
		 			</select>时
		 			<select name="mult1">
		 				<option value="00" #if("$!result.mult1"=="00") selected #end>00</option>
		 				<option value="10" #if("$!result.mult1"=="10") selected #end>10</option>
		 				<option value="20" #if("$!result.mult1"=="20") selected #end>20</option>
		 				<option value="30" #if("$!result.mult1"=="30") selected #end>30</option>
		 				<option value="40" #if("$!result.mult1"=="40") selected #end>40</option>
		 				<option value="50" #if("$!result.mult1"=="50") selected #end>50</option>
		 			</select>分
		 			<span class="txt_sp">
		 				(建议凌晨执行备份)
		 			</span>
	 			</span>
 			</div>		 
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">第二备份时间点：</label>
	 			<span class="rt_sp">
		 			<select name="hour2">
		 				<option value=""></option>
		 				<option value="00" #if("$!result.hour2"=="00") selected #end>00</option>
		 				<option value="01" #if("$!result.hour2"=="01") selected #end>01</option>
		 				<option value="02" #if("$!result.hour2"=="02") selected #end>02</option>
		 				<option value="03" #if("$!result.hour2"=="03") selected #end>03</option>
		 				<option value="04" #if("$!result.hour2"=="04") selected #end>04</option>
		 				<option value="05" #if("$!result.hour2"=="05") selected #end>05</option>
		 				<option value="06" #if("$!result.hour2"=="06") selected #end>06</option>
		 				<option value="07" #if("$!result.hour2"=="07") selected #end>07</option>
		 				<option value="08" #if("$!result.hour2"=="08") selected #end>08</option>
		 				<option value="09" #if("$!result.hour2"=="09") selected #end>09</option>
		 				<option value="10" #if("$!result.hour2"=="10") selected #end>10</option>
		 				<option value="11" #if("$!result.hour2"=="11") selected #end>11</option>
		 				<option value="12" #if("$!result.hour2"=="12") selected #end>12</option>
		 				<option value="13" #if("$!result.hour2"=="13") selected #end>13</option>
		 				<option value="14" #if("$!result.hour2"=="14") selected #end>14</option>
		 				<option value="15" #if("$!result.hour2"=="15") selected #end>15</option>
		 				<option value="16" #if("$!result.hour2"=="16") selected #end>16</option>
		 				<option value="17" #if("$!result.hour2"=="17") selected #end>17</option>
		 				<option value="18" #if("$!result.hour2"=="18") selected #end>18</option>
		 				<option value="19" #if("$!result.hour2"=="19") selected #end>19</option>
		 				<option value="20" #if("$!result.hour2"=="20") selected #end>20</option>
		 				<option value="21" #if("$!result.hour2"=="21") selected #end>21</option>
		 				<option value="22" #if("$!result.hour2"=="22") selected #end>22</option>
		 				<option value="23" #if("$!result.hour2"=="23") selected #end>23</option>
		 			</select>时
		 			<select name="mult2">
		 				<option value=""></option>
		 				<option value="00" #if("$!result.mult2"=="00") selected #end>00</option>
		 				<option value="10" #if("$!result.mult2"=="10") selected #end>10</option>
		 				<option value="20" #if("$!result.mult2"=="20") selected #end>20</option>
		 				<option value="30" #if("$!result.mult2"=="30") selected #end>30</option>
		 				<option value="40" #if("$!result.mult2"=="40") selected #end>40</option>
		 				<option value="50" #if("$!result.mult2"=="50") selected #end>50</option>
		 			</select>分
		 			<span class="txt_sp">
		 				(为空表示不执行备份)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">备份保存时间：</label>
	 			<span class="rt_sp">
		 			<input name="backDay" type="text" style="width:100px" value="$!result.backDay" disableautocomplete="true" autocomplete="off">天
		 			<span class="txt_sp">
		 				(为节省备份磁盘空间，系统将在备份的保存时间到后自动删除)
		 			</span>
	 			</span>
 			</div> 		
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">备份路径：</label>
	 			<span class="rt_sp">
		 			<input name="backPath" type="text" style="width:220px" #if("$!result.backPath"=="") value="$!defPath" #else  value="$!result.backPath" #end disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(请选择安全的备份设备，最好和程序文件不是同一磁盘)
		 			</span>
	 			</span>
 			</div> 	
 					
		 	<div class="hr_link"></div>
		 	
		</div>
		
		
		<div id="sd_remoteSet" class="conDiv">
			<font class="title_font">索引碎片整理	</font>
		 	<div class="wp_ul">
	 			<span class="rt_sp" >
		 			<span class="txt_sp">
		 				&nbsp;&nbsp;&nbsp;&nbsp;当系统长时间运行后索引会产生过多碎片。<br/>
		 				&nbsp;&nbsp;&nbsp;&nbsp;这会导致系统性能下将，运行缓慢。<br/>
		 				&nbsp;&nbsp;&nbsp;&nbsp;及时进行碎片整理有助提升性能。
		 				&nbsp;&nbsp;&nbsp;&nbsp;碎片整理时会极大消耗系统资源，请选择系统空闲时间执行。
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>		 	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">执行日期：</label>
	 			<span class="rt_sp">
		 			<input name="dayPiece" type="checkbox" id="dayPiece0" #if($!result.dayPiece.indexOf("0,") > -1) checked #end  value="0" disableautocomplete="true" autocomplete="off">
		 			<label for="dayPiece0" style="padding-right:10px">星期日</label>
		 			<input name="dayPiece" type="checkbox" id="dayPiece1" #if($!result.dayPiece.indexOf("1,") > -1) checked #end  value="1" disableautocomplete="true" autocomplete="off">
		 			<label for="dayPiece1" style="padding-right:10px">星期一</label>
		 			<input name="dayPiece" type="checkbox" id="dayPiece2" #if($!result.dayPiece.indexOf("2,") > -1) checked #end  value="2" disableautocomplete="true" autocomplete="off">
		 			<label for="dayPiece2" style="padding-right:10px">星期二</label>
		 			<input name="dayPiece" type="checkbox" id="dayPiece3" #if($!result.dayPiece.indexOf("3,") > -1) checked #end  value="3" disableautocomplete="true" autocomplete="off">
		 			<label for="dayPiece3" style="padding-right:10px">星期三</label>
		 			<input name="dayPiece" type="checkbox" id="dayPiece4" #if($!result.dayPiece.indexOf("4,") > -1) checked #end  value="4" disableautocomplete="true" autocomplete="off">
		 			<label for="dayPiece4" style="padding-right:10px">星期四</label>
		 			<input name="dayPiece" type="checkbox" id="dayPiece5" #if($!result.dayPiece.indexOf("5,") > -1) checked #end  value="5" disableautocomplete="true" autocomplete="off">
		 			<label for="dayPiece5" style="padding-right:10px">星期五</label>
		 			<input name="dayPiece" type="checkbox" id="dayPiece6" #if($!result.dayPiece.indexOf("6,") > -1) checked #end  value="6" disableautocomplete="true" autocomplete="off">
		 			<label for="dayPiece6" style="padding-right:10px">星期六</label>		 
		 			<span class="txt_sp">
		 				(建议周日执行碎片整理)
		 			</span>			
	 			</span>
 			</div> 		
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">执行时间点：</label>
	 			<span class="rt_sp">
		 			<select name="hourPiece">
		 				<option value="00" #if("$!result.hourPiece"=="00") selected #end>00</option>
		 				<option value="01" #if("$!result.hourPiece"=="01") selected #end>01</option>
		 				<option value="02" #if("$!result.hourPiece"=="02") selected #end>02</option>
		 				<option value="03" #if("$!result.hourPiece"=="03") selected #end>03</option>
		 				<option value="04" #if("$!result.hourPiece"=="04") selected #end>04</option>
		 				<option value="05" #if("$!result.hourPiece"=="05") selected #end>05</option>
		 				<option value="06" #if("$!result.hourPiece"=="06") selected #end>06</option>
		 				<option value="07" #if("$!result.hourPiece"=="07") selected #end>07</option>
		 				<option value="08" #if("$!result.hourPiece"=="08") selected #end>08</option>
		 				<option value="09" #if("$!result.hourPiece"=="09") selected #end>09</option>
		 				<option value="10" #if("$!result.hourPiece"=="10") selected #end>10</option>
		 				<option value="11" #if("$!result.hourPiece"=="11") selected #end>11</option>
		 				<option value="12" #if("$!result.hourPiece"=="12") selected #end>12</option>
		 				<option value="13" #if("$!result.hourPiece"=="13") selected #end>13</option>
		 				<option value="14" #if("$!result.hourPiece"=="14") selected #end>14</option>
		 				<option value="15" #if("$!result.hourPiece"=="15") selected #end>15</option>
		 				<option value="16" #if("$!result.hourPiece"=="16") selected #end>16</option>
		 				<option value="17" #if("$!result.hourPiece"=="17") selected #end>17</option>
		 				<option value="18" #if("$!result.hourPiece"=="18") selected #end>18</option>
		 				<option value="19" #if("$!result.hourPiece"=="19") selected #end>19</option>
		 				<option value="20" #if("$!result.hourPiece"=="20") selected #end>20</option>
		 				<option value="21" #if("$!result.hourPiece"=="21") selected #end>21</option>
		 				<option value="22" #if("$!result.hourPiece"=="22") selected #end>22</option>
		 				<option value="23" #if("$!result.hourPiece"=="23") selected #end>23</option>
		 			</select>时
		 			<select name="multPiece">
		 				<option value="00" #if("$!result.multPiece"=="00") selected #end>00</option>
		 				<option value="10" #if("$!result.multPiece"=="10") selected #end>10</option>
		 				<option value="20" #if("$!result.multPiece"=="20") selected #end>20</option>
		 				<option value="30" #if("$!result.multPiece"=="30") selected #end>30</option>
		 				<option value="40" #if("$!result.multPiece"=="40") selected #end>40</option>
		 				<option value="50" #if("$!result.multPiece"=="50") selected #end>50</option>
		 			</select>分
		 			<span class="txt_sp">
		 				(建议凌晨执行碎片整理)
		 			</span>
	 			</span>
 			</div>	
 					
		 	<div class="hr_link"></div>
		 	<input type="button" onclick="beforeSubmit()" style="margin: 20px 310px;font-size: 20px; height: 30px;padding: 0px 10px;" value="保存" />
		</div>
 		<div class="hr_link"></div>	
	</div>
	<script type="text/javascript">
			$("#con").height($(window).height()-70);
			var sHeight = $(window).height()-70;	
		</script>
</form>
</body>
</html>