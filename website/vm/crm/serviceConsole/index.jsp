<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" href="/style/css/serviceConsole.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_serviceConsole.js"></script>
</head>

<body >
<input type="hidden" id="clientId" name="clientId" value="">
<div align="left">
	<img src="/style/images/client/cusdesk.gif" />
</div>
<div align="center"  style="overflow: auto;" id="center">
	<table border="0" width="99%" cellpadding="0" cellspacing="0">
  	<tbody>
    	<tr>
      	<td width="59%" valign="top" style="border:1px solid #999999;">
        	<table border="0" width="100%" cellpadding="5" cellspacing="0">
          	<tbody>
            	<tr>
              	<td width="50%" bgcolor="#EFEFEF">
                	<img border="0" src="/style/images/client/sfind.png" />
                  客户：
                  <font color="#999999">（关键字，客户名称，客户编号，电话号码）</font>
                  <br/>
                  <input type="text" id="keyWord" name="keyWord" size="20" onKeyDown="if(event.keyCode==13){keyWordSubmit();}"/>
                  <input type="button" value="查询" name="B1" onclick="keyWordSubmit();"/>&nbsp;
                </td>
              </tr>
              <tr>
              	<td width="50%" >
                	<!-- 查询 list_client Start -->
                	<div class="list_client"  id="clientsDiv"></div>
                  	<!-- 查询 list_client End -->
                  	<!-- 报表iframe Start -->
                  	<div class="reportList" id="contentsDiv" style="display: none;">
	                  	<div class="reportListT">
	                    	<a class="clientName showClientName" href="#" ></a>
	                      <i class="clientD">客户信息</i>
	                      <i class="service">服务和投诉</i>
	                    </div>
	                   <!-- 服务和投诉 Start -->
	                   <div class="ServerAndComplain">
		                    <div class="operate">
		                    	<p>
		                        <a class="addService" href="#" onclick="addBill('CRMClientService');">记录客服</a>
		                        <a class="addComplain" href="#" onclick="addBill('CRMcomplaints');">记录投诉</a>
		                        <a class="addContact" href="#" onclick="addBill('CRMClientInfo');">新建联系人</a>
		                        <!-- <a class="addTask" href="#">新建待办任务</a> -->
		                        </p>
		                    </div> 
		                    <div class="iframe" id="billFrameDiv">
		                    	<iframe frameborder="0" scrolling="0" width="100%"  id="billFrame"></iframe>
		                    </div>  
		                    <hr size="1" />
       					</div>
       					<!-- 服务和投诉 End --> 
       					<!-- 客户信息 Start -->
	                     <div class="ClientInfor" style="display:none;">
	                     		<p></p>
	                     		<div>
	                        	<span>【客户名称】</span>
	                          <ul>
	                          	<li>
	                            	<i style="color:#A0A0A0;">●</i><a href="#" class="showClientName">韩立平(13000080001)</a>
	                            </li>
	                          </ul>
	                        </div>
	                        <div>
	                        	<span>【联系人】</span>
	                          <ul id="showCRMClientInfoDet"></ul>
	                        </div>
	                        <div>
	                        	<span>【待办任务】</span>
	                        </div>
	                        <div>
	                        	<span>【销售跟单】</span>
	                        	<ul id="showCRMSaleFollowUp" tableName="CRMSaleFollowUp">
	                        </div>
	                     </div>
	                     
                     <!-- 客户信息 End --> 
                  <!-- 报表iframe End -->
                  		 <div class="QAShow">
	                    	<!-- <span style="background:#FFFF00;">【对应QA】</span> -->
	                      	<span style="background:#a0a0a0;color:#ffffff;">【客户服务】</span>
		                    <ul id="showCRMClientService" tableName="CRMClientService">
		                    </ul>
		                    <span style="background:#a0a0a0;color:#ffffff;">【客户投诉】</span>
	                      	<ul id="showCRMcomplaints" tableName="CRMcomplaints">
	                      	</ul>
	                    </div> 
                </td>
              </tr>
            </tbody>
          </table>
        </td>
        <td width="1%" valign="top"></td>
        <td width="40%" valign="top" style="border:1px solid #999999;">
        	<div align="right">
          	<table border="0" width="100%" cellpadding="5" cellspacing="0">
            	<tbody>
              	<tr>
                	<td bgcolor="#EFEFEF">
                  	<img border="0" src="/style/images/client/sfind.png" />
                 	 	QA（问题和回答）：
                    <br/>
                    <input type="text" size="20" />
                    <input type="button" value="全文检索" />&nbsp;
                    <a href="#">QA内容维护</a>
                  </td>
                </tr>
                <tr>
                	<td valign="top" bgcolor="#F7FBEA">
                  	 <div class="QA_list">
                    	<!-- 注意事项 Start -->
                    	<div class="matter" >
                      	<font color="#999999">
                        	QA全文检索注意事项：
                          <br/>
                          <b>1</b>.关键字不能太普遍，比如查询"的"，可能很多QA条目中都有这个字，自动过滤，不返回查询结果
                          <br/>
                          <b>2</b>.查询关键字可以多些，全文检索会拆分匹配查询结果
                          <br/>
                          <b>3</b>.QA表内的数据如少于3条，无法实现全文检索
                        </font>
                      </div>
                     	<!-- 注意事项 End -->
                     	<!-- 
                      <ul>
                      	<li>·<a href="#">应收款提醒，如款项到账，应如何制作取消提醒？</a>[0.88]</li>
                        <li>·<a href="#">老板和经理如何查看销售人员每天的任务和日程？</a>[0.78]</li>
                        <li>·<a href="#">如何使用菜单条客户查询，有无规则说明？</a>[0.69]</li>
                        <li>·<a href="#">热点客户作用是什么？怎么设置？</a>[0.61]</li>
                        <li>·<a href="#">应收款提醒，如款项到账，应如何制作取消提醒？</a>[0.88]</li>
                        <li>·<a href="#">应收款提醒，如款项到账，应如何制作取消提醒？</a>[0.88]</li>
                        <li>·<a href="#">老板和经理如何查看销售人员每天的任务和日程？</a>[0.78]</li>
                        <li>·<a href="#">如何使用菜单条客户查询，有无规则说明？</a>[0.69]</li>
                        <li>·<a href="#">热点客户作用是什么？怎么设置？</a>[0.61]</li>
                        <li>·<a href="#">应收款提醒，如款项到账，应如何制作取消提醒？</a>[0.88]</li>
                      </ul>
                       -->
                    </div>
                    <!-- QA详情 Start -->
                    <!-- 
                    <div class="QADetail">
                      <table border="1" width="100%" cellspacing="0" cellpadding="2" style="line-height: 130%;" bordercolorlight="#F5F3EF" bordercolordark="#FFFFFF">
                      	<tbody>
                        	<tr>
                          	<td>
                            	<p style="color:#CC0066;">
                               <a href="#"><img src="/style/images/client/addqas.gif" alt="" /></a>
                              </p>
                            </td>
                          </tr>
                        	<tr>
                          	<td>
                            	<p style="color:#CC0066;">
                                问题：<b>客户管理中 "视图"按钮是做什么用的？</b> 
                              </p>
                            </td>
                          </tr>
                          <tr>
                          	<td>
                            	<p>
                                <span style="color:#CC0066;">解答：</span><br/>
                                1、视图指的是围绕某核心数据的所有相关数据的集合。<br/>
                                2、客户视图的作用：客户视图内集成管理某客户的几乎全部信息和数据， 客户视图内的各个模块都可以直接的查看详细信息，新建数据或者编辑。一个界面，解决操作中80%的问题。<br/>
                                3、客户视图中包含：客户基本信息、财务数据统计、联系人、客户跟踪、售前、售中、售后等完整的信息模块<br/>
                                4、XToolsCRM的视图包括：客户视图、销售机会视图、合同视图、订单视图、项目视图、产品视图等。<br/>
                              </p>
                            </td>
                          </tr>
                          <tr>
                          	<td>
                            	<p>
                                <span style="color:#CC0066;">提示：</span><br/>
                              </p>
                            </td>
                          </tr>
                          <tr>
                          	<td>
                            	<p>
                                <span style="color:#CC0066;">使用次数：</span><br/>
                              </p>
                            </td>
                          </tr>
                          <tr>
                          	<td>
                            	<p>
                                <span style="color:#CC0066;">附件：</span><br/>
                              </p>
                            </td>
                          </tr>
                          <tr>
                          	<td>
                            	<p style="color:#CC0066;">
                               <a href="#"><img src="images/addqas.gif" alt="" /></a>
                              </p>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div> 
                     -->
                    <!-- QA详情 End -->
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</div>
<script type="text/javascript">
	var oDiv=document.getElementById("center");
	var sHeight=document.documentElement.clientHeight-80;
	oDiv.style.height=sHeight+"px";
</script>
</body>
</html>
