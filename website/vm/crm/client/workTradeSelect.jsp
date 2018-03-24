<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
.btn_g{
	width: 55px;
}
.addBut{height: 25px;}
.addTrade li input{width: 200px;height: 15px;}
.addTrade li{height: 20px;}
.cf li{height: 20px;}
</style>
<script type="text/javascript">
	$(function() {
		if($("#isIndexEnter").val() == "true"){
			var tradeIds = window.parent.form.trade.value;
			for(var i=0;i<tradeIds.split(",").length;i++){
				$("#"+tradeIds.split(",")[i].replaceAll("'","") ).attr("checked","checked");
			}
		
		}
	});
	
	function selectPro(professionId,professionName){
		var tradeIds = "";
		//var tradeNames = "";
		if(jQuery('#isIndexEnter').val() == "true"){
			if(professionId !=""){
				var str = professionId + ",";
			}else{
				jQuery("input[name='tradeId']:checked").each(function(){
					tradeIds +=  $(this).val() + ",";
				}) 
				var str = tradeIds;
			}
			window.parent.indexSearch(str,"trade");
		}else{
			  if(parent.jQuery.exist('dealdiv')){
			      parent.jQuery.opener('dealdiv').dealTrade(professionId,professionName);
			  }else if(parent.jQuery.exist('dealdiv')){
			  	  //parent.dealTrade(professionId,professionName);
			  	  parent.jQuery.opener("crmOpDiv").dealTrade(professionId,professionName); 
			  }else{
			  	  parent.dealTrade(professionId,professionName); 
			  }
		}
			parent.jQuery.close('crmOpenId');
	}
	
	/*全选*/
	function checkAll(obj){
		var check = jQuery(obj).attr("checked") ;
		jQuery("input[name='tradeId']").each(function() {
			if(typeof(check)!="undefined"){
				jQuery(this).attr("checked", "checked");
			}else{
				jQuery(this).removeAttr("checked");
			}
		});
	}
	
	
	//新增行业	
	function addProfession(workTradeId,obj){
		var str = '<li><input type="text" name="professionName" id="professionName"> <button type="button" class="addBut" onclick="addSubmit(\''+workTradeId+'\',this)">确认</button> <button type="button" class="addBut" onclick="cancel(this)">取消</button></li>'
		$(obj).parent().next().append(str);
		$(obj).parent().next().find("input:last").focus();
	}
	
	//取消行业
	function cancel(obj){
		$(obj).parent().remove();
	}
	
	//单个行业提交
	function addSubmit(workTradeId,obj){
		var professionName = jQuery(obj).parent().find("input").val();
		var isIndexEnter = jQuery("#isIndexEnter").val();
		jQuery.ajax({
		    type: "POST",
		    url: "/CRMopenSelectAction.do?operation=1&workTradeId="+workTradeId+"&professionName="+encodeURIComponent(encodeURIComponent(professionName)),
		    success: function(msg){
		    	if(msg=="no"){
		    		asyncbox.tips('添加失败 !','error');
		    	}else{
		    		var strLi = "<li>";
		    		if(isIndexEnter == "true"){
		    			strLi += '<input type="checkbox" name="tradeId" value="'+msg+'" tradeName="'+professionName+'" id="'+msg+'"/>';
		    		} 
		    		strLi += '<a href="#" onclick="selectPro(\''+msg+'\',\''+professionName+'\')" style="cursor: pointer;"><label for="a1">'+professionName+'</label></a></li>';
		    		$(obj).parent().before(strLi);
		    		$(obj).parent().remove();
		    		asyncbox.tips('添加成功 !','success');
		    	}
		    }
		});	
	}
	
	//删除行业
	function delProfession(professionId,obj){
		var count = checkCount(professionId,'profession');
		if(count > 0){
			alert("本行业存在"+count+"个客户,不允许此操作")	;
			return false;						    		
		}
		if(confirm("确定删除?")){
			jQuery.ajax({
			    type: "POST",
			    url: "/CRMopenSelectAction.do?operation=3&professionId="+professionId,
			    success: function(msg){
			    	if(msg=="ok"){
			    		asyncbox.tips('删除成功 !','success');
			    		$(obj).parent().remove();
			    	}else{
			    		asyncbox.tips('删除失败 !','error');
			    	}
			    }
			});	
		}
	}
	
	//删除行业类型
	function delWorkTrade(workTradeId,obj){
		var count = checkCount(workTradeId,'workTrade');
		if(count > 0){
			alert("本类型存在"+count+"个客户,不允许此操作")	;
			return false;						    		
		}
		if(confirm("确定删除?")){
			jQuery.ajax({
			    type: "POST",
			    url: "/CRMopenSelectAction.do?operation=3&type=workTrade&workTradeId="+workTradeId,
			    success: function(msg){
			    	if(msg=="ok"){
			    		asyncbox.tips('删除成功 !','success');
			    		$(obj).parent().parent().remove();
			    	}else{
			    		asyncbox.tips('删除失败 !','error');
			    	}
			    }
			});	
		}
		
	}
	
	//添加行业类型
	function addTrades(){
		var copyTrade = jQuery("div[id='copyAddTrade']:first").clone(true);
		jQuery("div[id='copyAddTrade']:first").after(copyTrade);
		jQuery("div[id='copyAddTrade']:first").next().show();
	}
	
	//添加行业input框



	function addInput(obj){
		var str='<li>行业名称:<input type="text"  name="professionName"><img src="/style/images/plan/del_02.gif"  style="cursor: pointer;margin-left: 10px;margin-top:5px;" onclick="delInput(this)"/></li>';
		jQuery(obj).parent().next().find("li:last").after(str)
	}
	//删除行业input框



	function delInput(obj){
		$(obj).parent().remove();
	}
	
	//提交
	function tradeSubmit(obj){
		var workTradeName=$(obj).parent().find("input[id='workTradeName']").val();
		if(jQuery.trim(workTradeName)==""){
			alert("行业类型不能为空");
			$(obj).parent().find("input[id='workTradeName']").focus();
			return false;
		}
		var professionNames = "";
		jQuery(obj).parent().next().find("li").each(function(){
			if($(this).find("input").val() != ""){
				professionNames += $(this).find("input").val() +",";
			}
		})
		jQuery.ajax({
		    type: "POST",
		    url: "/CRMopenSelectAction.do?operation=1&isIndexEnter=$!isIndexEnter&type=workTrade&workTradeName="+encodeURIComponent(encodeURIComponent(workTradeName))+"&professionNames="+encodeURIComponent(encodeURIComponent(professionNames)),
		    success: function(msg){
		    	if(msg=="no"){
		    		asyncbox.tips('添加失败 !','error');
		    	}else{
		    		asyncbox.tips('添加成功 !','success');
		    		jQuery(obj).parent().parent().before(msg);
		    		jQuery(obj).parent().parent().remove();
		    		
		    	}
		    }
		});	
	}
	
	//取消添加类型
	function tradeCancel(obj){
		jQuery(obj).parent().parent().remove();
	}
	
	function checkCount(tradeId,checkFlag){
		var count = 0;
		jQuery.ajax({
		    type: "POST",
		    url: "/CRMopenSelectAction.do?operation=4&type=tradeCount&checkFlag="+checkFlag+"&tradeId="+tradeId,
		    async: false,
		    success: function(msg){
		    	count = msg;
		    }
		});	
		
		return count;
	}
</script>
</head>
<body>
<input type="hidden" name="isFirst" id="isFirst" value="$!isFirst"/>
<input type="hidden" name="isIndexEnter" id="isIndexEnter" value="$!isIndexEnter"/>
<input type="hidden" name="tradeIds" id="tradeIds" value=""/>
<div class="boxbg subbox_w660" style="margin: 0 auto;">
<div class="subbox cf" >
  <div class="operate"> #if("$!isIndexEnter" == "true") <ul class="cf" style="width: 100px;float: left;margin-top: 2px;"><li><input type="checkbox" id="checkAll" onclick="checkAll(this)"/>全选</li></ul><a href="#" class="btnbg btn_g opl" onclick="parent.selectAll('trade');">清空</a> <a href="#" class="btnbg btn_g opl" onclick="selectPro('','')">确定</a>#end #if($LoginBean.operationMap.get("/CRMClientAction.do").update())<a href="#" class="btnbg btn_g opl" onclick="addTrades();">添加类型</a>#end</div>
   <div class="bd" >
  	  <div class="subitem_hy" id="copyAddTrade" style="display:none;" >
	      <h4><span style="font-weight: bolder;">行业类型:</span><input type="text" style="width: 250px;" id="workTradeName" name="workTradeName"/> <span style="cursor: pointer;font-weight: normal;color: #006699" onclick="addInput(this)">添加</span> <input type="button" style="float: right;margin-right: 20px;" value="取消" onclick="tradeCancel(this)"/><input type="button" style="float: right;margin-right: 5px;" value="确定" onclick="tradeSubmit(this)"/></h4>
	      <ul class="cf addTrade">
	      	<li style="display: none;"><input type="text"  name="professionName"></li>
          	<li>行业名称:<input type="text"  name="professionName"><img src="/style/images/plan/del_02.gif"  style="cursor: pointer;margin-left: 10px;margin-top:5px;" onclick="delInput(this)"/></li>
          	<li>行业名称:<input type="text"  name="professionName"><img src="/style/images/plan/del_02.gif"  style="cursor: pointer;margin-left: 10px;margin-top:5px;" onclick="delInput(this)"/></li>
          </ul>
      </div>
   	  #if($!workTrades.size() !=0)
   	  	#foreach($workTrade in $workTrades)
	      <div class="subitem_hy">
	        <h4 name="h4">$!workTrade.name  &nbsp;&nbsp;
		        #if($LoginBean.operationMap.get("/CRMClientAction.do").update())<span style="cursor: pointer;font-weight: normal;color: #006699" onclick="addProfession('$!workTrade.id',this)">添加</span> #end
		        #if($LoginBean.operationMap.get("/CRMClientAction.do").delete())<span style="cursor: pointer;font-weight: normal;color: #006699" onclick="delWorkTrade('$!workTrade.id',this)">删除</span> #end
	       </h4>
	        <ul class="cf">
	        	#foreach($workProfession in $workTrade.workProfessionBeans)
	          		<li>
	          			#if("$!isIndexEnter" == "true")<input type="checkbox" name="tradeId" value="$!workProfession.id" tradeName="$!workProfession.name" id="$!workProfession.id"/>#end<a href="#" onclick="selectPro('$!workProfession.id','$!workProfession.name')" style="cursor: pointer;"><label for="a1">$!workProfession.name</label></a>
	          			#if($LoginBean.operationMap.get("/CRMClientAction.do").delete())<img src="/style/images/plan/del_02.gif"  style="cursor: pointer;margin-left: 10px;margin-top:5px;" onclick="delProfession('$!workProfession.id',this)"/>#end
	          		</li>
	         	#end
	        </ul>
	      </div>
	   #end
	  #end
   <br/>
  </div>
</div>
</div>
</body>
</html>
