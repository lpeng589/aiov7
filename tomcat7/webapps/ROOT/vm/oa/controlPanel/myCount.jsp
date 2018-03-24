<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <link rel="stylesheet" href="/style/css/controlPanel.css" type="text/css"/>
 <script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
 

<style type="text/css">
.column {
	margin: 4px 0;
}

li label {
	margin-top: 5px;
}

</style>
</head>

<body>
	<form action="/ControlPanelAction.do" method="post">
	<input type="hidden" name="operation" value="2"/>
	<input type="hidden" name="type" value="updateUser"/>
	<input type="hidden" name="employeeId" value="$!employee.Id"/>
	<table cellpadding="0" cellspacing="0" class="framework">
		<tr>
			<div style="float: right; margin: -2px;"><input  type="submit" value="$text.get("mrp.lb.saveAdd")" /></div>
		</tr>
		<tr>
			<!--右边列表开始-->
			<td>
				<div class="data"  >
					
					<div class="data_title" ><div class="control_list">$text.get("oa.bbs.specialismData")</div></div>
					<ul class="column">
						<li><label>$text.get("stat.empFullName")：</label><span><input type="text" value="$!employee.empFullName" disabled="disabled"/></span></li>
						<li><label>$text.get("common.lb.PinYin")：</label><span><input type="text" value="$!employee.sysName" disabled="disabled"/></span></li>
						<li><label>$text.get("oa.common.shorter")：</label><span><input type="text" value="$!employee.EmpName" name="empName"/></span></li>
						<li>
							<label>$text.get("oa.common.sex")：</label>
								<span>
									<select name="gender">
									#foreach($row_sex in $globals.getEnumerationItems("sex"))
											<option value="$row_sex.value" #if("$!employee.Gender" == "$row_sex.value") selected="selected" #end>$row_sex.name</option>
									#end
									</select>
								</span>
						</li>
					</ul>
					<ul class="column">
						<li>
							<label>$text.get("oa.common.educational")：</label>
							<span>
								<select disabled="disabled">
								#foreach($row_knowledge in $globals.getEnumerationItems("knowledge"))
									#if("$!employee.GraduateId" == "$row_knowledge.value")
										<option value="$row_knowledge.value">$row_knowledge.name</option>
									#end
								#end
								</select>
							</span>
						</li>
						<li><label>$text.get("oa.common.belongDepart")：</label><span><input type="text" value="$!department" disabled="disabled"/></span></li>
						<li><label>$text.get("oa.common.entryTime")：</label><span><input type="text" value="$!employee.EmployDate" disabled="disabled"/></span></li>
						<li><label>$text.get("oa.common.job.name")：</label><span><select disabled="disabled">
							#foreach($row_duty in $globals.getEnumerationItems("duty"))
									#if("$!employee.TitleID" == "$!row_duty.value")
										<option value="row_duty.value">$row_duty.name</option>
									#end
							#end
						</select></span></li>
					</ul>
					<ul class="column">
						<li><label>$text.get("oa.common.born")：</label><span><input type="text" value="$!employee.BornDate" name="bornDate" onClick="WdatePicker({lang:'zh_CN'});" /></span></li>
						<li><label>$text.get("oa.common.emailSpace")：</label><span><input type="text" value="$!employee.MailSize" disabled="disabled" name="mailSize"/></span></li>
					</ul>
				<div class="column_title" style="margin-top: 15px;"><div class="control_list">$text.get("oa.common.contactWay")：</div></div>
					<ul class="column" style="width: 100%; height: 130px;" >
						<li style="width: 100%;"><label>$text.get("upgrade.lb.tel")：</label><span><input type="text" size="40" value="$!employee.Tel" name="tel"/></span></li>
						<li style="width: 100%;"><label>$text.get("oa.common.phone")：</label><span><input type="text" size="40" value="$!employee.Mobile" name="mobile"/></span></li>
						<li style="width: 100%;"><label>$text.get("timeNote.lb.email")：</label><span><input type="text" size="40" value="$!employee.Email" name="email"/></span></li>
					</ul>
				</div>
				
				
			</td><!--右边列表结束-->
		</tr>
		
	</table>
	</form>
</body>
</html>
