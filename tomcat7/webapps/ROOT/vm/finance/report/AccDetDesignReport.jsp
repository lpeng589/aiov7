<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css" type="text/css"/>
<link rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/oa_news.css" type="text/css"/>
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<style type="text/css"> 
body,div,h1,h2,h3,h4,h5,h6,hr,p,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,button,input,textarea,th,td,table,span,p,a,em,form,iframe {margin:0;padding:0;}
body{font-size:12px;font-family:微软雅黑;text-align: left;}
li{list-style:none;}
img{border:none;}
.img1{cursor: pointer;}
em{font-style:normal;font-family:微软雅黑;}
input,select,button{outline:0;}
.my_wrap{overflow:hidden;margin:5px 0 0 10px;height:100%;}
.my_wrap .wp_lt{float:left;display:inline-block;width:85%;}
.my_wrap .wp_rt{float:left;display:inline-block;}
 
 
.t_ul{overflow:hidden;}
.t_ul .t_li{float:left;display:inline-block;margin:0 15px 0 0;}
.t_txt{box-sizing:border-box;border:1px #a1a1a1 solid;width:120px;height:21px;line-height:19px;}
 
.b_dv_wp{overflow:hidden;margin:15px 0 0 10px;}
.b_dv_wp .b_dv_lt{float:left;display:inline-block;height:250px;width:360px;overflow-y:auto;}
.b_dv_wp .b_dv_lt1{float:left;display:inline-block;width: 300px;}
.b_dv_wp .b_dv_rt{display:inline-block;height:250px;}
.b_dv_wp .div_tr{margin:0 0 0 20px;height:220px;}
.b_dv_tbl{border-collapse:collapse;width:360px;}
.b_dv_tbl3{border-collapse:collapse;width:360px;}
.b_dv_tbl1{border-collapse:collapse;width:360px;}
.b_dv_tbl1 thead{height:28px;background:url(/style1/images/TheadBG.jpg) repeat-x 0 0;line-height:28px;text-align:center;}
.b_dv_tbl thead{height:28px;background:url(/style1/images/TheadBG.jpg) repeat-x 0 0;line-height:28px;text-align:center;}
.b_dv_tbl tr td{padding:0 5px;line-height:27px;font-family:微软雅黑;}
.b_dv_tbl3 thead{height:28px;background:url(/style1/images/TheadBG.jpg) repeat-x 0 0;line-height:28px;text-align:center;}
.b_dv_tbl3 tr td{padding:0 5px;line-height:27px;font-family:微软雅黑;}
.b_dv_tbl1 tr td{padding:0 5px;line-height:27px;font-family:微软雅黑;}

.bb_dv{overflow:hidden;margin:5px 0 0 10px;border: 1px solid #a1a1a1;height: 50px;padding-left: 10px;}
.bb_dv .bb_dv_sp{}
.bb_dv .bb_dv_sp .inp_cb{float:left;margin:2px 5px 0 0;}
.bb_dv .bb_dv_sp em{float:left;}
.bb_dv .inp_dv{clear:both;overflow:hidden;padding:5px 0 0 0;margin:5px 0 0 0;}
 
 
.wp_rt .btn_ul{}
.wp_rt .btn_ul .btn_li{margin:0 0 5px 0;}
.wp_rt .btn_ul .btn_li .btn_inp{width:85px;height:28px;cursor:pointer;background:url(/style1/images/TheadBG.jpg) repeat-x 0 0;border:none;line-height:27px;font-family:微软雅黑;box-sizing:border-box;border:1px #a1a1a1 solid;}


.b_dv_tbl tbody input{width:98%;border: 0px;}
.select1{width: 100%}
.tr_count td{height:27px;}
.tr_back{background: #E7FCA9;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		#if("$!dealType"=="addPre")
			jQuery("#tblcolumn").find("tbody").append(tr_value());
		#end
		#if("$!dealType"=="updatePre")
			accTypeItem();
		#end
		hideInput();
		jQuery("#accType").focus();
	});
	
	
	function monseovers(obj){
		$(obj).css("background","#f9f9f9");
	}
	
	function mouseouts(obj){
		$(obj).css("background","#fff");
	}
	
	var tr_obj = null;
	function click_tr(obj){
		if(tr_obj!=null){
			$(tr_obj).removeClass("tr_back");
		}
		$(obj).addClass("tr_back");
		tr_obj = obj;
	}
	
	var acctypeobject = null;
	//弹出框选择会计科目
	function selectTypeCode(obj,acctype){
		var urlstr = '/PopupAction.do?popupName=popAccTypeInfo&inputType=radio&returnName=accExe&isCheckItem=false';
		if(jQuery(obj).attr("id")=="accType"){
			urlstr += "&chooseType=choosePerent";
		}else{
			urlstr += "&chooseType=chooseChild";
		}
		if(acctype!=null &&　acctype!=""){
			urlstr += "&selectType=keyword&selectValue="+acctype;
		}
		urlstr = encodeURI(encodeURI(urlstr));
		acctypeobject = obj;
		asyncbox.open({
			id:'Popdiv',
			title:'会计科目',
			url:urlstr,
			width:750,
			height:470,
			btnsbar : [{
		     text    : '清&nbsp;&nbsp;&nbsp;空',
		      action  : 'clearbtn'
		  	}].concat(jQuery.btn.OKCANCEL),
			callback : function(action,opener){
				if(action == 'ok'){
					var values = opener.datas();
					jQuery("#tblcolumn").find("tbody tr").remove();
					addtr('add');
					accExe(values);
				}else if(action == 'clearbtn'){
					var acc_id = jQuery(acctypeobject).attr("id");
					if(acc_id=="accType"){
						jQuery("#accType").val('');
						jQuery("#accName").val('');
					}else{
						jQuery(acctypeobject).val('');
						jQuery(acctypeobject).parent().next().find("input[name=td_accName]").val('');
					}
				}
			}
		});
	}
	
	//返回数据
	function accExe(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var nateStr = returnValue.split("#|#");
		for(var j=0;j<nateStr.length;j++){
			if(nateStr[j]!=""){
				var note = nateStr[j].split("#;#");
				jQuery(acctypeobject).val(note[0]);
				var acc_id = jQuery(acctypeobject).attr("id");
				if(acc_id=="accType"){
					jQuery("#accName").val(note[1]+"多栏式明细账");
				}else{
					jQuery(acctypeobject).parent().next().find("input[name=td_accName]").val(note[1]);
				}
			}
		}
		accTypeItem();
	}
	
	
	//根据accNumber查询核算哪些项

	var falg  = false;
	function accTypeItem(){
		falg = false;
		var accNumber = jQuery("#accType").val();
		var urls = "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=queryAccIsItem&accNumber="+accNumber;
		jQuery.ajax({ type: "POST", url: urls,async: false,dataType: "json",success: function(response){
				var datas = response;
				var isdept = datas.IsDept;
				var ispersonal = datas.IsPersonal;
				var isClient = datas.IsClient;
				var isProject = datas.IsProject;
				var isprovider = datas.IsProvider;
				var isstock = datas.isStock;
				if(isdept == "1" || ispersonal == "1" || isClient == "1" || isprovider == "1" || isstock == "1" || isProject == "1"){
					falg = true;
					jQuery("#li_item").show();
					jQuery("#td_code").html("核算项目编号");
				}else{
					jQuery("#li_item").hide();
					jQuery("#td_code").html("代码");
				}
				if(falg){
					jQuery("#itemSort").empty();
					var itemvalue = "";
					if(isdept == "1"){
						itemvalue += "<option value='DepartmentCode'>部门</option>";
					}
					if(ispersonal == "1"){
						itemvalue += "<option value='EmployeeID'>职员</option>";
					}
					if(isstock == "1"){
						itemvalue += "<option value='StockCode'>仓库</option>";
					}
					if(isClient == "1"){
						itemvalue += "<option value='ClientCode'>客户</option>";
					}
					if(isprovider == "1"){
						itemvalue += "<option value='SuplierCode'>供应商</option>";
					}
					if(isProject == "1"){
						itemvalue += "<option value='ProjectCode'>项目</option>";
					}
					jQuery("#itemSort").append(itemvalue);
				}else{
					jQuery("#itemSort").empty();
				}			
	    	}
		});
	}
	
	//直接取数据

	function selectCodeData(obj,type){
		var acctype = jQuery(obj).val();
		var urls = "/PopupAction.do?operation=ajaxPopDetail&selectValue="+acctype;
		if(type!=null && type!=""){
			urls += "&chooseType=choosePerent";
		}else{
			urls += "&chooseType=chooseChild";
		}
		jQuery.ajax({ type: "POST", url: urls,async: false,success: function(response){
				if(response == null || response == "" || response == "$msg"){
					alert("科目无权使用或者不存在");
					jQuery(obj).val('');
					jQuery(obj).parent().next().find("input[name=td_accName]").val('');
				}else{
					acctypeobject = obj;
					accExe(response);
				}
	    	}
		});
	}
	
	//自动编排
	function autoArrange(){
		var accType = jQuery("#accType").val();
		if(accType==""){
			alert("请先选择会计科目");
			jQuery("#accType").focus();
			return false;
		}
		var levelValue = jQuery("#levelvalue").val();
		if(jQuery("#levelsetting").is(":checked") && levelValue == ""){
			alert("请输入级别");
			jQuery("#levelvalue").focus();
			return false;
		}
		if((jQuery("#levelsetting").is(":checked") && !gtZero(levelValue)) || (jQuery("#levelsetting").is(":checked") && !isInt(levelValue))){
			alert("级别必须为正整数");
			jQuery("#levelvalue").focus();
			return false;
		}
		var urls = "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=ajaxBearData&dealType=childAccType&accType="+accType+"&level="+levelValue;
		if(falg){
			var itemSort = jQuery("#itemSort").val();
			urls += "&itemSort="+itemSort;
		}
		jQuery("#tblcolumn").find("tbody tr").remove();
		jQuery.ajax({ type: "POST", url: urls,async: false,dataType: "json",success: function(result){
				var datas = result.accData;
				tr_id = 1;
				for(var i = 0; i < datas.length; i++) {
				    var dataValue = datas[i];
				    var jdFlag = dataValue.jdFlag;
				    var str = "<tr id='tr_"+tr_id+"' class='tr_count' onclick='click_tr(this)'><td align='center' width='10%'><img src='/style/plan/del_icon.gif' title='删除行' class='img1' onClick='delimg(this)'/></td><td width='20%'><select class='select1' name='td_jdflag' id='td_jdflag'>";
				    if(jdFlag=="1"){
				    	str += "<option value='1' selected>借</option><option value='2'>贷</option>";
				    }else if(jdFlag=="2"){
				    	str += "<option value='1'>借</option><option value='2' selected>贷</option>";
				    }
				    str += "</select></td><td width='30%'><input name='td_acctype' id='td_acctype' value='"+dataValue.AccNumber+"'/></td><td><input name='td_accName' id='td_accName' value='"+dataValue.AccName+"'/></td></tr>";
				    jQuery("#tblcolumn").find("tbody").append(str);
				    tr_id ++;
				}
	    	}
		});
	}
	
	//tr数据
	var tr_id = 1;
	#if("$!globals.get($!designBean,0)"!="")
		#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
			tr_id ++;
		#end
	#end
	function tr_value(){
		var value = "<tr id='tr_"+tr_id+"' class='tr_count' onclick='click_tr(this)'>";
	    value += "<td align='center' width='10%'><img src='/style/plan/del_icon.gif' title='删除行' class='img1' onClick='delimg(this)'/></td>";
	    value += "<td width='20%'><select class='select1' name='td_jdflag' id='td_jdflag'><option value='1'>借</option><option value='2'>贷</option></select></td>";
	    value += "<td width='30%'><input name='td_acctype' id='td_acctype' value='' onKeyDown='if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;selectCodeData(this)}'/></td>";
	    value += "<td><input name='td_accName' id='td_accName' value=''/></td>";
	    value += "</tr>";
	    tr_id ++;
	    return value;
	}
	
	//插入一行

	function addtr(type){
		if(type=="add"){
			jQuery("#tblcolumn").find("tbody").append(tr_value());
		}else if(type=="cut"){
			if(tr_obj!=null){
				jQuery("#tblcolumn").find("tr[id="+jQuery(tr_obj).attr("id")+"]").after(tr_value());
			}else{
				jQuery("#tblcolumn").find("tbody").append(tr_value());
			}
		}
	}
	
	//删除一行

	function delimg(obj){
		jQuery(obj).parent().parent().remove();
	}
	
	
	//关闭弹出框

	function closediv(){
		parent.jQuery.close('designdiv');
	}
	
	//移动
	function move(type){
		if(tr_obj==null){
			alert("请选择一行记录");
			return false;
		}else{
			if(type=="top"){
				var checkedTR = $(tr_obj).prev();
				checkedTR.insertAfter($(tr_obj));
			}else{
				var checkedTR = $(tr_obj).next();
				checkedTR.insertBefore($(tr_obj));
			}
		}
	}
	
	//提交表单
	function saves(){
		var acctype = jQuery("#accType").val();
		var accname = jQuery("#accName").val();
		if(acctype == ""){
			alert("请选择会计科目");
			jQuery("#accType").focus();
			return false;
		}
		if(accname == ""){
			alert("多栏账名称不能为空");
			jQuery("#accName").focus();
			return false;
		}
		if(jQuery("#td_acctype").length==0){
			alert("会计科目组必须要有记录！");
			return false;
		}
		var flag = false;
		jQuery("#td_acctype").each(function (){
			if($(this).val() == ""){
				flag = true;
			}else{
				flag = false;
			}
		});
		if(flag){
			alert("会计科目组必须要有记录！");
			return false;
		}
		if(jQuery("#levelsetting").is(":checked")){
			if(jQuery("#levelvalue").val()==""){
				alert('请输入级别');
				jQuery("#levelvalue").focus();
				return false;
			}
		}
		form.submit();
	}
	
	function hideInput(){
		if(jQuery("#levelsetting").is(":checked")){
			jQuery("#levelvalue").removeAttr("readonly");
		}else{
			jQuery("#levelvalue").attr("readonly","readonly");
			jQuery("#levelvalue").val('');
		}
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" action="/FinanceReportAction.do?optype=ReporttblAccAllDet&type=dealDesign" name="form" id="form" target="formFrame">
<input type="hidden" name="id" id="id" value="$!globals.get($!designBean,0)"/>
<div class="my_wrap">
	<div class="wp_lt">
    <ul class="t_ul">
      <li class="t_li">
        <em class="t_em" style="margin-left: 20px;"> 
          会计科目： 
        </em>
        <input class="t_txt" type="text" name="accType" id="accType" ondblclick="selectTypeCode(this)" 
        onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;selectCodeData(this,'acctype')}" value="$!globals.get($!designBean,1)"/>
        <img src='/style1/images/search.gif' onclick="selectTypeCode(document.getElementById('accType'))" class='search'/>
      </li>
      <li class="t_li">
        <em class="t_em" style="margin-left: 20px;">
          多栏账名称：
        </em>
        <input class="t_txt" type="text" style="width: 250px;" name="accName" id="accName" value="$!globals.get($!designBean,2)"/>
      </li>
      <li class="t_li" id="li_item" style="padding-top: 10px;display: none;">
        <em class="t_em" style="margin-left: 20px;">
          核&nbsp;算&nbsp;项&nbsp;目：
        </em>
        <select id="itemSort" name="itemSort">
        	
        </select>
      </li>
    </ul>
    <div class="b_dv_wp">
      <div class="b_dv_lt1">
      	<table class="b_dv_tbl1" border="1" bordercolor="#a1a1a1" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
      		<thead>
            <tr>
              <td width="10%"></td>
              <td width="20%">方向</td>
              <td width="30%"><span id="td_code">代码</span></td>
              <td>栏目名称</td>
            </tr>
          </thead>
      	</table>
      </div>
      <div class="b_dv_lt">
        <table class="b_dv_tbl" id="tblcolumn" border="1" bordercolor="#a1a1a1" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
          <tbody>
          	#if("$!globals.get($!designBean,0)"!="")
          	#set($counts = 1)
          	#set($itemsvalue = "")
          	#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
          	#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
          	<tr id='tr_$!counts' class='tr_count' onclick='click_tr(this)'>
		    	<td align='center' width='10%'><img src="/style/plan/del_icon.gif" title="删除行" class="img1" onClick="delimg(this)"/></td>
		    	<td width='20%'><select class='select1' name='td_jdflag' id='td_jdflag'><option value='1' #if("$!globals.get($!itemsvalue,0)"=="1")selected#end>借</option><option value='2' #if("$!globals.get($!itemsvalue,0)"=="2")selected#end>贷</option></select></td>
		    	<td width='30%'><input name='td_acctype' id='td_acctype' value='$!globals.get($!itemsvalue,1)' onKeyDown='if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;selectCodeData(this)}' /></td>
		    	<td><input name='td_accName' id='td_accName' value='$!globals.get($!itemsvalue,2)'/></td>
	    	</tr>
	    	#set($counts = $!counts+1)
	    	#end
	    	#end
          </tbody>
        </table>
      </div>
      <div class="b_dv_rt" style="height:300px;">
      	<div style="overflow-y:auto;float: none;width:220px;" class="div_tr">
        <table class="b_dv_tbl3" border="1" bordercolor="#a1a1a1" cellpadding="0" cellspacing="0" style="width:200px;">
          <thead>
            <tr>
              <td></td>
              <td>币种代码</td>
              <td>分栏目名称</td>
            </tr>
          </thead>
          <tbody>
          	#set($curr = "false")
          	#foreach($!currency in $!currencyList)
            <tr>
            	#set($curr = "false")
            	#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
            		#if("$!designcurr"=="$!globals.get($!currency,0)")
            			#set($curr = "true")
            		#end
            	#end
            	<td><input name="currency_id" id="currency_id" value="$!globals.get($!currency,0)" type="checkbox" #if("$curr"=="true")checked#end/></td>
              	<td>$!globals.get($!currency,3)</td>
              	<td>$!globals.get($!currency,1)</td>
            </tr>
            #end
          </tbody>
        </table>
        </div>
        <div class="bb_dv" style="width: 200px;margin:10px 0 0 20px;">
          <span class="bb_dv_sp">
            <input class="inp_cb" type="checkbox" name="levelsetting" id="levelsetting" onclick="hideInput()" value="1" #if("$!globals.get($!designBean,5)"=="1")checked#end/>
            <em><label for="levelsetting">按级别设置多栏账</label></em>
          </span>
          <div class="inp_dv">
            <input class="t_txt" type="text" name="levelvalue" id="levelvalue" value="$!globals.get($!designBean,6)"/>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="wp_rt">
  	<ul class="btn_ul">
    	<li class="btn_li">
      	<input class="btn_inp" type="button" value="保存" onclick="saves()"/>
      </li>
      <li class="btn_li">
      	<input class="btn_inp" type="button" value="关闭" onclick="closediv()"/>
      </li>
      <li class="btn_li">
      	<input class="btn_inp" type="button" value="自动编排" onclick="autoArrange()"/>
      </li>
      <li class="btn_li">
      	<input class="btn_inp" type="button" value="新增一行" onclick="addtr('add')"/>
      </li>
      <li class="btn_li">
      	<input class="btn_inp" type="button" value="插入行" onclick="addtr('cut')"/>
      </li>
      <li class="btn_li">
      	<input class="btn_inp" type="button" value="上移" onclick="move('top')"/>
      </li>
      <li class="btn_li">
      	<input class="btn_inp" type="button" value="下移" onclick="move('down')"/>
      </li>
    </ul>
  </div>
</div>
</form>
</body>
</html>

