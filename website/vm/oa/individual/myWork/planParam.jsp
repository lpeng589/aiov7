<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link type="text/css" rel="stylesheet" href="style/css/WorkingPlan.css" />
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script src="/js/jquery.js" type="text/javascript"></script> 
<script language="javascript">

putValidateItem("weekplanhour","$text.get("common.lb.useTime")","int","",false,0,23);
putValidateItem("weekplanmin","$text.get("common.lb.useTime")","int","",false,0,59);
putValidateItem("weeksumhour","$text.get("common.lb.useTime")","int","",false,0,23);
putValidateItem("weeksummin","$text.get("common.lb.useTime")","int","",false,0,59);
putValidateItem("dayplanhour","$text.get("common.lb.useTime")","int","",false,0,23);
putValidateItem("dayplanmin","$text.get("common.lb.useTime")","int","",false,0,59);
putValidateItem("daysumday","$text.get("common.lb.useTime")","int","",false,0,7);
putValidateItem("daysumhour","$text.get("common.lb.useTime")","int","",false,0,23);
putValidateItem("daysummin","$text.get("common.lb.useTime")","int","",false,0,59);

function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else
	{
		return true;
	}
			
}
function beforSubmit(form){
 
	
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	

</script>

</head>

<body style="text-align:center">
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" method="post"  action="/OAWorkPlanAction.do"  onSubmit="return beforSubmit(this);" target="formFrame">
	
	<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
	<input type="hidden" name="noback" value="$!noback">
	<input type="hidden" name="winCurIndex" value='$!winCurIndex'>
	<input type="hidden" name="opType" value='param'>
	<input type="hidden" name="save" value='true'>
	
<div style="margin:5px;">
	<table cellpadding="0" cellspacing="0" border="0" width="70%">
		<tr>
			<td colspan="2" class="dateShow" height="20px;">
			&nbsp;
			</td>
		</tr>
		<tr>
			<td class="WP_addC">
				<div class="WP_title">
					<span>$text.get("workplan.lb.checkparam1")
					<select name=weekplanday>
					<option value="1" #if($!result.get("weekplanday")=="1") selected #end>$text.get("data.lb.w1")</option>
					<option value="2" #if($!result.get("weekplanday")=="2") selected #end>$text.get("data.lb.w2")</option>
					<option value="3" #if($!result.get("weekplanday")=="3") selected #end>$text.get("data.lb.w3")</option>
					<option value="4" #if($!result.get("weekplanday")=="4") selected #end>$text.get("data.lb.w4")</option>
					<option value="5" #if($!result.get("weekplanday")=="5") selected #end>$text.get("data.lb.w5")</option>
					<option value="6" #if($!result.get("weekplanday")=="6") selected #end>$text.get("data.lb.w6")</option>
					<option value="7" #if($!result.get("weekplanday")=="7") selected #end>$text.get("data.lb.w0")</option>
					</select>
					<input type="text" name="weekplanhour" style="width:30px" value="$!result.get("weekplanhour")"/>$text.get("workplan.lb.checkparam2")
					<input type="text" name="weekplanmin" style="width:30px" value="$!result.get("weekplanmin")"/>$text.get("workplan.lb.checkparam3")
					</span>
				</div>
				<div class="WP_title">
					<span>$text.get("workplan.lb.checkparam4")
					<input name="weeksumnext" type="radio" value="0" #if($!result.get("weeksumnext")=="0") checked #end/>
					$text.get("workplan.lb.checkparam5")
					<input name="weeksumnext" type="radio" value="1"#if($!result.get("weeksumnext")=="1") checked #end/>
					$text.get("workplan.lb.checkparam6")
					<select name=weeksumday>
					<option value="1" #if($!result.get("weeksumday")=="1") selected #end>$text.get("data.lb.w1")</option>
					<option value="2" #if($!result.get("weeksumday")=="2") selected #end>$text.get("data.lb.w2")</option>
					<option value="3" #if($!result.get("weeksumday")=="3") selected #end>$text.get("data.lb.w3")</option>
					<option value="4" #if($!result.get("weeksumday")=="4") selected #end>$text.get("data.lb.w4")</option>
					<option value="5" #if($!result.get("weeksumday")=="5") selected #end>$text.get("data.lb.w5")</option>
					<option value="6" #if($!result.get("weeksumday")=="6") selected #end>$text.get("data.lb.w6")</option>
					<option value="7" #if($!result.get("weeksumday")=="7") selected #end>$text.get("data.lb.w0")</option>
					</select>
					<input type="text" name="weeksumhour" style="width:30px" value="$!result.get("weeksumhour")"/>$text.get("workplan.lb.checkparam2")
					<input type="text" name="weeksummin" style="width:30px" value="$!result.get("weeksummin")"/>$text.get("workplan.lb.checkparam3")
					</span>
				</div>
				<div class="WP_title">
					<span>$text.get("workplan.lb.checkparam7")
					<input type="text" name="dayplanhour" style="width:30px" value="$!result.get("dayplanhour")"/>$text.get("workplan.lb.checkparam2")
					<input type="text" name="dayplanmin" style="width:30px" value="$!result.get("dayplanmin")"/>$text.get("workplan.lb.checkparam3")
					</span>
				</div>
				<div class="WP_title">
					<span>$text.get("workplan.lb.checkparam8")
					<input name="daysumnext" type="radio" value="0" #if($!result.get("daysumnext")=="0") checked #end/>
					$text.get("workplan.lb.checkparam9")
					<input name="daysumnext" type="radio" value="1" #if($!result.get("daysumnext")=="1") checked #end/>
					$text.get("workplan.lb.checkparam10")
					<input type="text" name="daysumday" style="width:30px" value="$!result.get("daysumday")"/>$text.get("workplan.lb.checkparam11")
					<input type="text" name="daysumhour" style="width:30px" value="$!result.get("daysumhour")"/>$text.get("workplan.lb.checkparam2")
					<input type="text" name="daysummin" style="width:30px" value="$!result.get("daysummin")"/>$text.get("workplan.lb.checkparam3")
					</span>
				</div>
				<div class="WP_title">
					<span>
					<input name="daysumall" type="radio" value="0" #if($!result.get("daysumall")=="0") checked #end/>
					$text.get("workplan.lb.checkparam12")
					<input name="daysumall" type="radio" value="1" #if($!result.get("daysumall")=="1") checked #end/>
					$text.get("workplan.lb.checkparam13")
					</span>
				</div>
				<div class="WP_title">
					<span>
					<a href="javascript:mdiwin('/HolidaySettingAction.do?sr=menu','$text.get("workplan.lb.checkparam14")')">$text.get("workplan.lb.checkparam14")</a> 
					</span>
				</div>
				<div class="WP_addButton"><input type="submit" value="$text.get("common.lb.save")" />
				</div>
			</td>
		</tr>
	</table>
</div>
</form>
</body>
</html>
