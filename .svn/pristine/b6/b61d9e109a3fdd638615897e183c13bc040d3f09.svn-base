<!doctype html>
<html class="html">
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
<link type="text/css" href="/style/css/oa_myItems.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/oa/oaItems.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的项目</title>
#end
<script type="text/javascript">
function dateChange(){

}
</script>
</head>
<body>
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
<div>
#else
<div id="scroll-wrap">
#end
<form action="/OAItemsAction.do" name="form" method="post">
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" id="tabSelectName" name="tabSelectName" value="$!oaItemsform.tabSelectName">
<input type="hidden" id="searchStatus" name="searchStatus" value="$!oaItemsform.searchStatus">
<input type="hidden" id="tableName" name="tableName"  value="OAItems">
<input type="hidden" id="hasSearchCondition" name="hasSearchCondition"  value="$!hasSearchCondition">
<input type="hidden" name="addTHhead" id="addTHhead" value="$addTHhead"/>	
	<div class="wrap" style="overflow: auto;" id="wrap">
    	<!-- 头部header 分割线 Start -->
    	<div class="header">
        	<div class="leftTitle">
                <b class="icons projectPhoto"></b>
                <em>我的项目</em> 
            </div>
            <span onclick="addItem();" class="add">
            	<b class="icons ab"></b>
                <em>新建项目</em>
            </span>
            <div class="tagScreen">
                <ul class="tagU">
                    <li id="executor">我负责的</li>
                    <li id="participant"> 我参与的</li>
                </ul>
                <div class="tagS rf">
					<span class="lf pr wp-status" status="1">
                    	<i #if("$!oaItemsform.searchStatus" =="1") class="sel" #end status="1">进行中</i>
						<i #if("$!oaItemsform.searchStatus" =="2") class="sel" #end status="2">完成</i>
					</span>
					
                	<span class="lf pr key-search">
                		<input class="lf key-inp" type="text"  id="searchKeyWord" name="searchKeyWord" value="$!oaItemsform.searchKeyWord" placeholder="关键字搜索..." onKeyDown="if(event.keyCode==13) searchConSubmit();"/> 
                		<b class="lf icons" onclick="javascript:form.submit();"></b>
                		<i class="lf i-senior highSearch" >高级</i>
                		 <div class="tag-pa">
		                	<ul class="tag-su">
		                		<b class="cz">重置</b>
                		 		<b class="qx">取消</b>
                		 		<li>
		                			<i class="lf iM">任务编号:</i>
					                <span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchItemOrder" name="searchItemOrder" value="$!oaItemsform.searchItemOrder" onKeyDown="if(event.keyCode==13)searchConSubmit();"/>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">开始时间</i>
		                			<span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchBeginStartTime" name="searchBeginStartTime" value="$!oaItemsform.searchBeginStartTime" onclick="selectDate('searchBeginStartTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchBeginStartTime');"></b>
					                </span>
					                <b class="lf link"></b>
					                <span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchBeginOverTime" name="searchBeginOverTime" value="$!oaItemsform.searchBeginOverTime" onclick="selectDate('searchBeginOverTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchBeginOverTime');"></b>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">结束时间</i>
		                			<span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchEndStartTime" name="searchEndStartTime" value="$!oaItemsform.searchEndStartTime" onclick="selectDate('searchEndStartTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchEndStartTime');"></b>
					                </span>
					                <b class="lf link"></b>
					                <span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchEndOverTime" name="searchEndOverTime" value="$!oaItemsform.searchEndOverTime" onclick="selectDate('searchEndOverTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchEndOverTime');"></b>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">关联客户</i>
		                			<span class="lf pr icon-txt">
		                				<input type="hidden" id="searchClientId" name="searchClientId" value="$!oaItemsform.searchClientId"/>
										<input class="txtIcon ip_txt" type="text" id="searchClientIdName" name="searchClientIdName" value="$!clientName" readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','searchClientId','normalId','false');">
					                    <b onclick="publicPopSelect('CrmClickGroup','searchClientId','normalId','false');" class="icons pa bMag"></b>
					                </span>
		                		</li>
		                		<li>
		                			<input class="btn-add" type="button"  value="查询" onclick="searchConSubmit();"/>
		                		</li>
		                	</ul>
		                </div>
                	</span>
                </div>
            </div>
        </div>
        <!-- 头部header 分割线 End -->
        #if($itemsList.size() == 0)
        	<div class="no-items">
	       		<img src="/style/images/item/no-item.jpg" alt="添加项目" onclick="addItem();"/>
	       	</div>
        #else
        <!-- 项目列表 proList 分割线 Start -->
        <div class="proList" >
        	<ul class="proListU" >
        		#foreach($item in $itemsList)
        		#set($statusFlag = "")
        		#set($statusName = "")
        		#set($status = "")
        		#if("$item.status" == "1")
        			#set($statusFlag = "running")
        			#set($statusName = "执行中")
        			#set($status = "2")
        		#else
        			#set($statusFlag = "achieve")
        			#set($statusName = "完成")
        			#set($status = "1")
        		#end
            	<li id="$item.id">
                	<a class="lf userPhoto" href="#"><img src='$globals.checkEmployeePhoto("48",$item.executor)' /></a>
                	<em class="No-color lf" style="padding:0 3px;margin:23px 0 0 15px;font-size:16px;">NO.$item.itemOrder</em>
                    <span class="lf proName"> $item.title</span>
                    
                    #if("$item.status" == "2")
                    	  <span class="lf pr timeQ">$item.beginTime - $item.endTime</span> 
                    #else
	                    #set($timeDiff= $globals.getTimeDiff($item.endTime,$globals.getHongVal("sys_date")))
	                    #set($timeDiff = $math.toNumber($timeDiff))
	                    <span class="lf pr countDay"><b class="icons pa iRemind"></b>
		                    #if($timeDiff>0)
		                    	剩余<i class="iDay">$timeDiff</i>天 
		                    #elseif($timeDiff==0)
								<span class="iDay">今天到期</span>
		                    #else 
		                    	超过<i class="iDay">$math.abs($timeDiff)</i>天









		                    #end
	                    </span>
                    #end
                    	#if("$!oaItemsform.tabSelectName" == "executor")
                    	<span class="del-item icons del" title='删除' ></span>
                    	#end
	                    <span class="proBtn $statusFlag" status="$status" isDetail="false">$statusName</span>
                </li>
                #end
                
            </ul>
             $pageBar
        </div>
        <!-- 项目列表 proList 分割线 End -->
       	#end
    </div>
    
    
    <!-- 弹出添加层 add 分割线 Start -->
    <div class="addWrap pop-layer" id="addItemDiv"></div>
    <!-- 弹出添加层 add 分割线 End -->
    
</form>

<script type="text/javascript">
	$("#scroll-wrap").height(document.documentElement.clientHeight);
</script>

<script type="text/html" id="copyAddItem">
	<div class="a-title">
        	<span class="lf pr">
            	<b class="icons pa"></b>
            	新建项目
            </span>
        </div>
        <ul class="aU">
        	<li>
            	<i class="lf iM">项目名称：</i>
                <input class="lf inpTxt" type="text" id="title" name="title"/>
            </li>
            <li>
            	<i class="lf iM">详情描述：</i>
                <textarea class="lf areaTxt" id="remark" name="remark"></textarea>
            </li>
            <li>
            	<i class="lf iM">执行时间：</i>
                <span class="lf pr icon-txt">
                	<input class="txtIcon ip_txt" type="text" id="beginTime" name="beginTime" value='$globals.getHongVal("sys_date")' defValue='$globals.getHongVal("sys_date")' onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
                    <b class="icons pa bDate" onclick="WdatePicker({el:$dp.$('beginTime')})"></b>
                </span>
                <b class="lf link"></b>
                <span class="lf pr icon-txt">
                	<input class="txtIcon ip_txt" type="text" id="endTime" name="endTime" onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
                    <b class="icons pa bDate" onclick="WdatePicker({el:$dp.$('endTime')})"></b>
                </span>
            </li>
			<li>
            	<i class="lf iM">关联客户：</i>
                <span class="lf pr icon-txt">
               		<input type="hidden" name="clientId" value="" id="clientId" />
					<input class="txtIcon ip_txt" type="text" name="clientIdName" value="" id="clientIdName"  readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','clientId','normalId','false');"/>
                    <b onclick="publicPopSelect('CrmClickGroup','clientId','normalId','false');" class="icons pa bMag"></b>
                </span>
            </li>
			<input type="hidden" id="schedule" name="schedule" value="0">
        </ul>
        <div class="btn-items-wp">
        	<span class="btn-items celBtn" onclick="closeLayer(this);">取消</span>
	        <span class="btn-items conBtn" onclick="itemSubmit(this);">确定</span>
    </div>	   
</script>
</div>
<div id="hideBg" class="hideBg"></div>  
</body>
</html>
