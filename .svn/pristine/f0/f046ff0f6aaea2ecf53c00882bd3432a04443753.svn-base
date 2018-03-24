<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志列表</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/date.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>

<script type="text/javascript">

</script>
<style type="text/css">
.w{width:45%;}
.w40{width:4%}
.w100{width:10%}
.w120{width:12%}
.w130{width:13%}
.w160{width:16%}
</style>

</head>
<body>
		<table style="width: 100%;	height:32px;line-height:32px;margin-bottom: 2px;margin-top: 32px;font-size: 12px;">
    	<tr bgcolor="#92caeb";>
    		<td class="w100">日志内容</td>
    		<td class="w100">日志总结</td>
    		<td class="w40">日志进度</td>
    		<td class="w40">日志时间</td>
    	</tr>
		#foreach($!log in $!worklogList)
		<tr bgcolor="#dfdfdf"; >
			<input type="hidden" id="workLogId" name="workLogId" value="$!log.id"/>
			<td class="w100" title="$!globals.get($log,1)" >$!globals.get($log,1)</td>
    		<td class="w100" style="text-align: left;">$!globals.get($log,2)</td>
    		<td class="w40" title="$!globals.get($log,3)" >#if("$!globals.get($log,3)==null")0 #end $!globals.get($log,3) %</td>
    		<td class="w40" style="text-align: left;">$!globals.get($log,4)</td>
		</tr>
		#end
		</table>
		#if($worklogList.size()==0)
			<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">暂无日志信息</div>
		#end
</body>
</html>
