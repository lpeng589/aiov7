$(document).ready(function(){
 $(".h-child-btn").mouseover(function(){
 $(this).addClass("h-height").find(".d-more").show().siblings(".br-link").show();
 }).mouseout(function(){
 $(this).removeClass("h-height").find(".d-more").hide().siblings(".br-link").hide();
 });

 
 $(".h-child-btn").each(function(){
 if($(this).find(".d-more a").size()==0){ 
 $(this).parents("li").hide();
 }
 });
 
 $("#qdate_zt").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
 $("#date_start").val(getDateStr(-1));
 $("#date_end").val("");
 }); 
 $("#qdate_jt").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
 $("#date_start").val(getDateStr(0));
 $("#date_end").val("");
 }); 
 $("#qdate_yz").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
 $("#date_start").val(getDateStr(-7));
 $("#date_end").val("");
 }); 
 $("#qdate_yy").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
 $("#date_start").val(getDateStr(-30));
 $("#date_end").val("");
 }); 
 $("#qdate_by").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
 $("#date_start").val(getMonthStr());
 $("#date_end").val("");
 }); 
 var nd = $("#date_start").val();
 if(nd == getDateStr(-1)){
 $(".curqd_selected").removeClass("curqd_selected"); $("#qdate_zt").addClass("curqd_selected");
 }else if(nd == getDateStr(0)){
 $(".curqd_selected").removeClass("curqd_selected"); $("#qdate_jt").addClass("curqd_selected");
 }else if(nd == getDateStr(-7)){
 $(".curqd_selected").removeClass("curqd_selected"); $("#qdate_yz").addClass("curqd_selected");
 }else if(nd == getDateStr(-30)){
 $(".curqd_selected").removeClass("curqd_selected"); $("#qdate_yy").addClass("curqd_selected");
 }else if(nd == getMonthStr()){
 $(".curqd_selected").removeClass("curqd_selected"); $("#qdate_by").addClass("curqd_selected");
 }
 $("#btnConfirmSCA").click(function() {
 if( beforeButtonQuery()) {
 submitQuery(); 
 } 
 });
 $("#btnClearSCA").click(function() {
 clearCondition();
 });
 
});

function clearCondition(){
 $("#conditionDIV").find("input[type=text]").val("");
 $("#conditionDIV").find("input:not([type])").val("");
 $("#conditionDIV").find("select").val("");
 $("#conditionDIV").find("input[belongTableName]").val("");
 
}

function showAdvancePanel(){
 var urls = "/ReportDataAction.do?operation=advance&reportNumber="+$("#tableName").val();
 asyncbox.open({id:'advancePanel',title:'条件高级设置',url:urls,width:450,height:400});
}
function showAdvanceUpdatePanel(opType,keyId){
 var urls = "/ReportDataAction.do?operation=advance&opType="+opType+"&keyId="+keyId+"&reportNumber="+$("#tableName").val();
 
 var tit = '条件方案设置';
 if(opType == 'sort'){
 tit = '字段排序';
 }
 asyncbox.open({id:'advanceUpdatePanel',title:tit,url:urls,width:450,height:500});
}
function closeAdvanceUpdate(){
 jQuery.close("advanceUpdatePanel");
 jQuery.reload('advancePanel',"/ReportDataAction.do?operation=advance&reportNumber="+$("#tableName").val()); 
}

function m(value){
 return document.getElementById(value) ;
}
function n(value){
 if(document.getElementsByName(value).length==0){
 return document.getElementsByName("conver");
 }
 return document.getElementsByName(value) ;
}
function r(num,fieldName){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)
}
function v(num,fieldName,pos){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)[pos].value;
}
function o(num,fieldName,pos){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)[pos];
}

if(typeof(top.junblockUI)!="undefined"){
 top.junblockUI();
}

function onLockClick(str){
 AjaxRequest("/UtilServlet?operation=lockColumn&report="+$("#tableName").val()+"&number="+str); 
}

function displayQueryCondition(){
 var varHtml =document.getElementById("listid").outerHTML;
}

function viewChange(fieldName,fieldView){
 var textValue = document.getElementById(fieldView) ;
 var varField = document.getElementById(fieldName);
 for(var i=0;i<varField.options.length;i++){
 if(varField.options[i].value==textValue.value){
 varField.options[i].selected = true ;
 }
 }
 document.form.queryChannel.value="normal";
 document.form.submit() ;
}

var lngIfClass="0";
var f_brother ="";
var parentCode ="";
var draftQuery ="";
var draftQuery ="";
var winCurIndex ="";
var saveDraft ="";

var minValue = -999999999;
var maxValue = 999999999;

function beforeButtonQuery(){
 document.getElementById("export").value = "" ; 
 document.form.query.value='true';

 if(parentTableName !=""){
 var keyVal="";
 var varId = window.parent.document.getElementById("clientId") ;
 if(typeof(varId)!="undefined" && varId!=null){
 
 keyVal = varId.value ;
 }else{ 
 
 var listObj = window.parent.frames[0].document.getElementById("functionListObj");
 if(listObj!=null&&typeof(listObj)!="undefined"){
 var keyVal = listObj.getCBoxValue("hidden");
 }else{
 var keyIds = window.parent.frames[0].document.getElementsByName("keyId") ;
 var index = 0 ;
 for(var i=0;i<keyIds.length;i++){
 if(keyIds[i].checked){
 keyVal=keyIds[i].value; 
 index=index+1 ;
 }
 }
 
 var reportName = window.parent.frames[0].document.getElementById("reportName"); 
 if(index == 0 && typeof(reportName)!="undefined" && reportName!=null){
 alert("必须要选择一个"+reportName.value+"!") ;
 return false;
 }
 if(index > 1){
 alert("不能同时选择多个"+reportName.value+"!") ;
 return false;
 }
 }
 }
 if(keyVal.length>0){
 document.getElementById("f_brother").value=keyVal ;
 }
 }
 document.getElementById("isAllListQuery").value="";
 if(lngIfClass == "1"){
 var selectType = document.getElementById("selectType") ;
 if("endClass"==selectType){
 document.getElementById("isAllListQuery").value="YES";
 }
 }
 
 if(!validate(form)){
 return false ;
 } 
 document.form.pageNo.value = 1; 
 document.form.queryChannel.value="normal";
 document.getElementById("form").target = "" ;
 return true;
}

if(""!="" 
 && window.parent.document.getElementById("bottomFrame")){
 var displayType = window.parent.document.getElementById("displayType");
 if(typeof(displayType)!="undefined"
 && displayType!=null){
 displayType.value = "list" ;
 }
}

function printData(){
 if(!isChecked("keyId")){
 alert('请选择至少一条记录');
 return false;
 }
 
 var keyVal = $("input[type=checkbox][name=keyId]:checked");
 if(keyVal.size()==0){
 alert('请选择至少一条记录');
 return false;
 }else if(keyVal.size()>1){
 alert('您只能选择一条记录');
 return false;
 }
 
 
 if(printRight){ 
 if(AuditPrint =="1"){ 
 var ktr = keyVal.parents("tr");
 if(ktr.attr("workflowNodeName")!="finish"){
 
 alert('单据未审核完毕，不允许打印');
 return;
 }
 }
 bid = keyVal.val().split(";")[0];
 var urls = "/UserFunctionQueryAction.do?tableName=tblCompany&reportNumber="+reportNumber+"&moduleType="+moduleType+"&operation=18&BillID="+bid+"&parentTableName=&winCurIndex="+winCurIndex;
 asyncbox.open({id:'print',title:'打印单据',url:urls,width:300,height:280});
 
 }
}
function printMoreData(){
 
 var keyVal = $("input[type=checkbox][name=keyId]:checked");
 if(keyVal.size()==0){
 alert('请选择至少一条记录');
 return false;
 } 
 if(printRight){ 
 if(AuditPrint =="1"){ 
 hasNoreverse = false;
 keyVal.each(function(){ 
 var ktr = $(this).parents("tr");
 if(ktr.attr("workflowNodeName")!="finish"){
 
 if(!hasNoreverse){
 alert('单据未审核完毕，不允许打印');
 hasNoreverse = true;
 }
 }
 });
 if(hasNoreverse){
 return;
 }
 } 
 }
 selPrintMoreFormat(); 
}
function selPmf(obj){
 $(".printMore").find(".selectedprintMore").removeClass("selectedprintMore");
 $(obj).addClass("selectedprintMore");
}

function printLabelData(){
 if(!isChecked("hidden")){
 alert('请选择至少一条记录');
 return false;
 }
 var listObj = document.getElementById("functionListObj");
 var keyVal = listObj.getCBoxValue("hidden");
 if(printRight){ 
 
 var urls = "/UserFunctionQueryAction.do?tableName=tblCompany&reportNumber=PrintSeqtblCompany&printType=labelPrint&operation=18&keyId="+keyVal+"&parentTableName=&winCurIndex="+winCurIndex;
 asyncbox.open({id:'print',title:'标签打印',url:urls,width:300,height:280});
 }
}

function sureDel(itemName){
 if(delHasChild(itemName)){
 alert('请先删除子级.');
 return false;
 }
 if(!isChecked(itemName)){
 alert('请选择至少一条记录');
 return false;
 }
 form.operation.value=3;
 if(!confirm('确定删除吗')){
 form.operation.value = 4;
 cancelSelected("input");
 return false;
 }else{
 return true;
 }
}

function deleteList(rowRemark){
 if(rowRemark==''){ alert("单据未设置行标识字段"); return; }
 var del = getSelectRowValue("del").split("#|#");
 var rowRemarkVal=getSelectRowValue(rowRemark).split("#|#");
 var rowVals="";
 for(var i=0;i<del.length;i++){
 if(del[i]=="false"){
 rowVals+=rowRemarkVal[i].split("#;#")[0]+",";
 }
 }
 if(rowVals.length>0){
 alert("您没有权限删除或单据已审核"+rowVals.substring(0,rowVals.length-1));
 return false;
 }
 
 if(sureDel('keyId')){ 
 if(draftQuery=="draft"){
 document.forms[0].draftQuery.value='draft';
 }
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 form.target = '' ;
 form.submit();
 }
}
function deleteOne(id){
 if(id.indexOf(";")>0){
 alert('请先删除子级.');
 return false;
 }
 if(confirm("确定删除？")){
 $("input[name='keyId']").each(function(){
 if($(this).val()==id){
 $(this).attr("checked",'true');
 }else{
 $(this).removeAttr("checked");
 }
 })
 reloadkeyIds();
 
 form.operation.value=3;
 
 if(draftQuery=="draft"){
 document.forms[0].draftQuery.value='draft';
 }
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 form.target = '' ;
 form.submit();
 }
}

function setIframSrc(srcUrl,obj){
 if(window.parent.document.getElementById("bottomFrame")){ 
 if(window.parent.document.getElementById("bottomFrame") && ""==""){
 var win=window.parent.document.getElementById("moddiFrame").contentWindow;
 for(var i=0;i<win.count;i++){
 var divObj = win.document.getElementById("divShow_"+i)
 if(i == 0){
 divObj.className = "listRange_1_space_1_div_a";
 }else{
 divObj.className = "listRange_1_space_1_div_b";
 }
 }
 srcUrl = srcUrl.replace("operation=7","operation=5") ;
 window.parent.document.getElementById("bottomFrame").src=srcUrl+"&tabList=Y";
 }else{
 window.location.href=srcUrl;
 }
 }
}
var currentId = "" ;
function onItemClick(str){
 var listObj = document.getElementById("functionListObj");
 var keyId=listObj.getCBoxValue("hidden").split("|")[0];
 if(str.length>0){
 str = str.substring(8,str.length) ;
 str = str.substring(0,str.indexOf(",")) ;
 }
 if(str.length==0){
 str = keyId ;
 }
 var strIds = listObj.getCBoxValue("hidden").split("|") ;
 for(var i=0;i<strIds.length;i++){
 listObj.selectItem("hidden",strIds[i],false) ; 
 }
 listObj.selectItem("hidden",str,true) ;
 if(currentId!=str){
 currentId = str ;
 setIframSrc2("/UserFunctionAction.do?tableName=tblCompany&keyId="+str+"&operation=7&winCurIndex="+winCurIndex,currentId);
 }
}


function setIframSrc2(srcUrl,obj){
 if(window.parent.document.getElementById("bottomFrame") && ""==""){
 
 var keyIds=document.getElementsByName("keyId");
 var keyVal="";
 var f_brother="" ;
 var listObj = document.getElementById("functionListObj");
 if(typeof(listObj)!="undefined" && listObj!=null){
 keyVal=listObj.getCBoxValue("hidden").split("|")[0];
 f_brother=keyVal;
 }else{
 if(window.event.srcElement.type != "checkbox"){
 for(var i=0;i<keyIds.length;i++){
 keyIds[i].checked=''; 
 keyVal=keyIds[i].value; 
 if(srcUrl.indexOf(keyVal)!=-1){
 keyIds[i].checked='true' ;
 f_brother = keyVal ;
 }
 }
 }else{
 for(var i=0;i<keyIds.length;i++){
 keyVal=keyIds[i].value; 
 if(srcUrl.indexOf(keyVal)!=-1){
 f_brother = keyVal ;
 }
 }
 }
 }
 var checkItem = window.parent.document.getElementById("moddiFrame").contentWindow.document.getElementById("checkItem").value;
 
 
 if("tblCompany"!=checkItem){
 var srcUrl2 = window.parent.document.getElementById("moddiFrame").src ;
 srcUrl2=srcUrl2.replace("&tabIndex=&","&") ;
 if(srcUrl2.indexOf("tabIndex=")!= -1){
 var re = /tabIndex=\w+/g; 
 srcUrl2 = srcUrl2.replace(re,"tabIndex="+checkItem);
 }else{
 srcUrl2+="&tabIndex="+checkItem ;
 }
 
 if(srcUrl2.indexOf("f_brother")==-1){
 window.parent.document.getElementById("moddiFrame").src = srcUrl2+"&f_brother="+f_brother ;
 }else{
 window.parent.document.getElementById("moddiFrame").src = srcUrl2.substring(0,srcUrl2.indexOf("&f_brother"))+"&f_brother="+f_brother ;
 }
 }else{
 if(f_brother.length==""){
 f_brother = obj ;
 }
 window.parent.document.getElementById("moddiFrame").contentWindow.document.getElementById("mainKeyId").value=f_brother;
 setIframSrc(srcUrl,obj);
 
 }
 
 
 }
}

function setFrameButDis(f_brother){
 
 var crmFramObj=window.parent.document.getElementById("crmFramset");
 var bomHeight=crmFramObj.rows.split(",")[2];
 
 if(bomHeight=="0"){
 var win=window.parent.document.getElementById("moddiFrame").contentWindow;
 var divObj1 = win.document.getElementById("divShow_1");
 if(f_brother.length>0){
 for(var i=0;i<win.count;i++){
 var divObj = win.document.getElementById("divShow_"+i);
 if(divObj.innerHTML.indexOf("↑")<0&&divObj.innerHTML.indexOf("↓")<0){
 divObj.innerHTML="<font color='red'>↑</font>"+divObj.innerHTML;
 }
 }
 }else{
 for(var i=0;i<win.count;i++){
 var divObj = win.document.getElementById("divShow_"+i);
 if(divObj.innerHTML.indexOf("↑")>=0){
 divObj.innerHTML=divObj.innerHTML.replace("<FONT color=red>↑</FONT>","");
 }
 }
 }
 }
}

function dbSetIframSrc(srcUrl){
 if(updateRight || (draftQuery=="draft" && draftUpdate=="true")) {
 window.location.href=srcUrl;
 }else{
 srcUrl = srcUrl.replace("operation=7","operation=5") ;
 window.location.href=srcUrl; 
 }
}
function onItemDBClick(str){
 dbSetIframSrc(str); 
}

function setColumnSet(str,lockName){
 if(curloginUserId == "1"){
 if(!confirm("您是超级管理员，所做列配置操作将会影响其它所有用户，是否继续？")){
 return;
 }
 }
 if(lockName == undefined)
 lockName = "";
 var strURL = "/UtilServlet?operation=colconfig&tableName=tblCompany&colType=list&lockName="+lockName+"&colSelect="+str ;
 AjaxRequest(strURL);
 if(location.href.indexOf("src=menu")!=-1)
 {
 window.location.reload();
 return;
 }
 if( beforeButtonQuery()) {
 submitQuery();
 }
}

function onDefaultCol(){
 if(curloginUserId == "1"){
 if(!confirm("您是超级管理员，所做列配置操作将会影响其它所有用户，是否继续？")){
 return;
 }
 }
 var strURL = "/UtilServlet?operation=defaultConfig&tableName=tblCompany&colType=list" ;
 AjaxRequest(strURL);
 if(location.href.indexOf("src=menu")!=-1)
 {
 window.location.reload();
 return;
 }
 if( beforeButtonQuery()) {
 submitQuery();
 }
}


function mdiwin(url,title){
 top.showreModule(title,url);
}

function sendMsg(){ 
 mdiwin("FrameworkAction.do?","组织架构"); 
}

function taskAllot(){
 if(!isChecked("keyId")){ 
 alert('请选择至少一条记录');
 return false;
 }
 displayName = encodeURI("任务分派") ;
 var selectName = document.getElementById('mySelectName').value ;
 var strURL = "/UserFunctionAction.do?&selectName="+selectName+"&operation=22&popupWin=TaskAllot&MOID="+MOID+"&MOOP=add&LinkType=@URL:&displayName="+displayName ;
 asyncbox.open({id:'TaskAllot',title:'任务分派',url:strURL,width:700,height:470});
}

function exeTaskAllot(str){
 if(typeof(str)=="undefined") return false ;
 var fieldValue=str.split(";"); 
 document.getElementById("classCode").value=fieldValue[0];
 document.getElementById("ButtonType").value = "taskAllot" ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 form.operation.value=25;
 form.submit();
}

function instant(){
 var str="/StockAnalysisServlet?a=1";
 AjaxRequest(str);
 var value = response;
 
 if(value!="no response text" && value.length>0){
 window.location.reload();
 }else{
 alert('执行失败');
 }
}

function G(id){
 return document.getElementById(id);
} 
function costing()
{
 if(!validate(form)){
 return false ;
 } 
 form.operation.value=4;
 form.optionType.value="costing";
 form.action="/UserFunctionQueryAction.do"
 form.submit();
}
function openInputDate(obj)
{
 WdatePicker({lang:'zh_CN'});
}
function copyOperation()
{
 if(!isChecked("keyId")){ 
 alert('请选择至少一条记录');
 return false;
 }
 
 var moduleTypeValue = "" ;
 var moduleType = document.getElementById("moduleType") ;
 if(typeof(moduleType)!= "undefined" && moduleType!=null){
 moduleTypeValue = moduleType.value ;
 } 
 width=1050;
 height=600;
 if(width ==0){
 width=document.documentElement.clientWidth;
 }
 if(height ==0){
 height=document.documentElement.clientHeight;
 }
 
 usrc="/UserFunctionAction.do?&operation=17&tableName=tblCompany&saveDraft=&parentTableName=&moduleType="+moduleTypeValue+"&f_brother="+f_brother+"&noback=false&winCurIndex="+winCurIndex+"&keyId="+document.getElementById("varKeyIds").value.split("|")[0];
 
 openPop('PopMainOpdiv','',usrc,width,height,true,height==document.documentElement.clientHeight); 
}

function copyBom(){
 if(!isChecked("keyId")){ 
 alert('请选择至少一条记录');
 return false;
 }
 form.operation.value=17;
 form.action="/BomAction.do"
 form.submit();
}

function buttonAuditing()
{
 form.operation.value=26 ;
 form.optionType.value="auditing" ;
 if(!isChecked('keyId')){
 alert('请选择至少一条记录') ;
 return false ;
 }
 form.submit() ;
}

function buttonReverse()
{
 form.operation.value=27 ;
 form.optionType.value="reverse" ;
 if(!isChecked('keyId')){
 alert('请选择至少一条记录') ;
 return false ;
 }
 form.submit() ;
}

function telCall()
{
 var allCheckBoxs=document.getElementsByName("keyId");
 var keyIds="";
 for(var i=0;i<allCheckBoxs.length;i++)
 {
 if(allCheckBoxs[i].type=="checkbox")
 {
 if(allCheckBoxs[i].checked==true)
 {
 var value = allCheckBoxs[i].value;
 keyIds+=value+","
 }
 }
 }
 if(keyIds!="" && keyIds.length!=0)
 {
 keyIds = keyIds.substr(0,keyIds.length-1);
 if(keyIds.indexOf(",") >0 ){
 alert("您每次只能选择一条记录进行呼叫"); 
 }else{
 var str = window.showModalDialog("/TelAction.do?operator=callTel&from=tblCompany&keyId="+keyIds,"","dialogWidth=600px;dialogHeight=350px"); 
 } 
 }else
 {
 alert("请选择至少一条记录"); 
 } 
}
var mydefineInfo = "";
function execDefine(defineName,buttonTypeName,tableName,keyId,obj,defineInfo){
 url = "/MobileAjax?op=execDefine";
 if(defineInfo == undefined || defineInfo == ""){
 mydefineInfo = "";
 defineInfo = "";
 if(!confirm("确定"+buttonTypeName+"?")){
 return;
 }
 }else{
 mydefineInfo += defineInfo;
 }
 jQuery.post(url,{defineName:defineName,buttonTypeName:buttonTypeName,tableName:tableName,keyId:keyId,defineInfo:mydefineInfo},function(data){ 
 if(data.code=="CONFIRM"){
 msg = data.msg.split("#;#");
 
 if(confirm(msg[0])){
 if(msg[1] != ""){
 defineInfo = msg[1]; 
 execDefine(defineName,buttonTypeName,tableName,keyId,obj,defineInfo)
 }
 }else{
 if(msg[2] != ""){
 defineInfo =msg[2]; 
 execDefine(defineName,buttonTypeName,tableName,keyId,obj,defineInfo)
 }
 } 
 }else if(data.code=="OK"){
 alert(data.msg);
 
 if(data.toUrl != ""){
 width=document.documentElement.clientWidth;
 height=document.documentElement.clientHeight;
 openPop('PopMainOpdiv','',data.toUrl,width,height,false,true); 
 if(obj != undefined && obj != ""){
 $(obj).hide();
 }
 }else{
 if(obj != undefined && obj != ""){
 $(obj).hide();
 }else{
 document.forms[0].submit();
 }
 }
 } else{
 alert(data.msg);
 }
 },"json" ); 
}
function extendSubmit(vars,vname,selected)
{
 document.getElementById("ButtonTypeName").value = vname;
 form.target="";
 if((typeof(selected)=="undefined" && !isChecked("keyId") && vars!="all") 
 || (typeof(selected)!="undefined" && selected && !isChecked("keyId"))){ 
 alert('请选择至少一条记录');
 return false;
 }
 if(vars == "custom"){
 document.getElementById("ButtonType").value = 'custom';
 
 }else if(vars == "handover"){
 var varValue = window.showModalDialog("/vm/classFunction/selectCRMCustomer.jsp","","dialogWidth=465px;dialogHeight=260px") ;
 if(typeof(varValue)=="undefined")return ;
 var arrValue = varValue.split("|") ;
 document.getElementById("newCreateBy").value=arrValue[0] ;
 document.getElementById("wakeUp").value=arrValue[1] ;
 document.getElementById("ButtonType").value = "handover" ;
 }else if(vars == "sendEmail"){
 document.getElementById("ButtonType").value = "sendEmail" ;
 if(window.parent.document.getElementById("bottomFrame")){
 var varMin = window.parent.document.getElementById("moddiFrame").contentWindow.document.getElementById("div_min");
 if(typeof(varMin)!="undefined"){
 varMin.click() ;
 }
 }
 }else if(vars == "sendSMS"){
 var varValue = window.showModalDialog("/vm/classFunction/sendMessage.jsp","","dialogWidth=645px;dialogHeight=350px") ;
 if(typeof(varValue)=="undefined")return ;
 document.getElementById("sendMessage").value = varValue ;
 document.getElementById("ButtonType").value = "sendSMS" ;
 }else if(vars == "folderRight"){
 var listObj = document.getElementById("functionListObj");
 var keyIdStr=listObj.getCBoxValue("hidden");
 
 AjaxRequest('/UtilServlet?operation=folderRight&type=getData&tableName='+form.tableName.value+'&keyId='+keyIdStr);
 var varValue = window.showModalDialog("/vm/oa/oaPublicMsg/folderRight.jsp?tableName=tblCompany&keyId="+keyIdStr,"","dialogWidth=565px;dialogHeight=350px") ;
 if(typeof(varValue)=="undefined")return ;
 }else if(vars == "dataMove"){
 displayName = encodeURI("分级目录") ;
 var selectName = document.getElementById("selectName").value ;
 var moduleType = document.getElementsByName("moduleType")[0].value ;
 
 var strURL = "/UserFunctionAction.do?&selectName="+selectName+"&moduleType="+moduleType+"&operation=22" + "&popDataType=dataMove&MOID="+MOID+"&MOOP=query&popupWin=DataMoveDiv&LinkType=@URL:&displayName="+displayName ;
 
 
 asyncbox.open({id:'DataMoveDiv',title:'数据搬移',url:strURL,width:750,height:470});
 
 }else if(vars=="billExport"){
 if(!confirm("确定导出数据吗?"))return
 document.getElementById("ButtonType").value = "billExport" ;
 
 
 
 }else if(vars=="StopValue_BaseInfo_tblEmployee"){
 var chks = document.getElementsByName("keyId");
 for(var i=0;i<chks.length;i++){
 if(chks[i].checked){
 var v = chks[i].value;
 if(v=="1"){
 alert("admin是超级管理员，不能被停用");
 return false;
 }
 }
 }
 document.getElementById("ButtonType").value = "execDefine" ;
 form.defineName.value=vars;
 }else if(vars == "on" || vars == "down" || vars == "all"){
 var opType = "update" ;
 var updateImg = "" ;
 if("all"==vars){
 if(!confirm("同步电子商务:就是把电子商务网站上所有商品全部删除后重新上架商品。\r\r这一过程可能需要一段时间，你确定要同步电子商务网站？")){
 return ;
 }
 if(confirm("上传图片可能影响上传速度,是否要上传图片？\n\n注：如果启用商品图片根据商品编号规则自动读取,则可以直接复制图片到\n服务器AIOSHop\\ShopImage相应目录下，可能更快些.")){
 updateImg = "yes" ;
 }
 }else{
 if("down"==vars){
 if(confirm("是否删除网店上的商品资料")){
 opType = "delete" ;
 }
 }else{
 if(confirm("是否更新图片")){
 updateImg = "yes" ;
 }
 }
 }
 document.getElementById("ButtonType").value = "shelf" ;
 document.getElementById("opType").value = opType ;
 document.getElementById("updateImg").value = updateImg ;
 document.getElementById("shelfType").value = vars ;
 if("down"==vars){
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 }else{
 setInterval("Refresh()",1000); 
 }
 }else if(vars=="ImageCatalog"){
 document.getElementById("ButtonType").value = "ImageCatalog" ;
 alert("生成图片目录：在启用商品图片根据商品编号规则自动读取下,用户不需要手工上传图片\n\n只需要复制商品图片到服务器AIO\\fileServer_gm\\ShopPic相应的目录下.") ;
 }else{
 document.getElementById("ButtonType").value = "execDefine" ;
 form.defineName.value=vars;
 
 form.target="formFrame";
 }
 
 
 if(vars != "dataMove"){
 form.operation.value=25;
 form.submit();
 }
}
function extendSubmitPopDefine(selectName,defineName,displayName){
 if(!isChecked("keyId")){ 
 alert('请选择至少一条记录');
 return false;
 }
 var moduleType = document.getElementsByName("moduleType")[0].value ;
 form.defineName.value=defineName;
 document.getElementById("ButtonTypeName").value = displayName;
 var strURL = "/UserFunctionAction.do?&selectName="+selectName+"&moduleType="+moduleType+"&operation=22" + "&MOID="+MOID+"&MOOP=query&popupWin=PopDefineDiv&LinkType=@URL:&displayName="+displayName ;
 asyncbox.open({id:'PopDefineDiv',title:displayName,url:strURL,width:750,height:470});
}

function exePopDefineDiv(returnValue){
 var str = returnValue;
 if(typeof(str)=="undefined" || str=="#;#"){
 form.defineName.value="";
 return false;
 }
 document.getElementById("popReturnVal").value=returnValue;
 document.getElementById("ButtonType").value = "popDefine" ;
 
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 form.operation.value=25;
 form.target="formFrame";
 form.submit();
}


function exeDataMoveDiv(returnValue){
 var str = returnValue;
 if(typeof(str)=="undefined"){
 return false;
 }
 var fieldValue=str.split(";"); 
 document.getElementById("classCode").value=fieldValue[0];
 document.getElementById("ButtonType").value = "dataMove" ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 form.operation.value=25;
 form.submit();
}
 


function importDataUser(tableName,winCurIndex){
 window.location.href='/importDataAction.do?fromPage=importList&operation=91&tableName='+tableName+'&winCurIndex='+winCurIndex;
}

jQuery(window).keydown(function(event){
 if(event.ctrlKey && event.which == 68){
 event.preventDefault();
 return false;
 }else{
 return true;
 }
});

function down(){
 if(event.ctrlKey&&event.keyCode==68){ 
 $("#addPre").click();
 event.keyCode=9;
 }else if(event.ctrlKey&&event.keyCode==90){ 
 $("#backList").click();
 event.keyCode=9;
 }
}




function addPrepare(){
 if(notOpenAccount){
 　asyncbox.confirm('未开账不能做单，是否立即开账','开账提示',function(action){
 　　　if(action == 'ok'){
 　　　　　window.location.href="/SysAccAction.do?type=beginAcc";
 　　　}
 　}); 
 return;
 }

 var moduleTypeValue = "" ;
 var moduleType = document.getElementById("moduleType") ;
 if(typeof(moduleType)!= "undefined" && moduleType!=null){
 moduleTypeValue = moduleType.value ;
 } 
 width=1050;
 height=600;
 if(width ==0){
 width=document.documentElement.clientWidth;
 }
 if(height ==0){
 height=document.documentElement.clientHeight;
 }
 

 usrc ='/UserFunctionAction.do?tableName=tblCompany&parentCode='+parentCode+'&operation=6&moduleType='+moduleTypeValue+'&f_brother='+f_brother+'&parentTableName=&winCurIndex='+winCurIndex;
 
 openPop('PopMainOpdiv','',usrc,width,height,true,height==document.documentElement.clientHeight); 
}


function colWidthSet(){
 document.getElementById("colWidthSetting").style.display="block" ; 
 document.getElementById("colNameSetting").style.display="none" ; 
}

function returnColConfig(){
 document.getElementById("colWidthSetting").style.display="none" ; 
 document.getElementById("colNameSetting").style.display="block" ; 
}

function mdiwin(url,title){
 top.showreModule(title,url); 
}
function goURL(url,target,bname){
 var tempurl="";
 var listObj = document.getElementById("functionListObj");
 
 while(url.indexOf("@ValueofDB:")>=0){
 tempurl=url.substring(0,url.indexOf("@ValueofDB:"));
 url=url.substring(url.indexOf("@ValueofDB:")+"@ValueofDB:".length);
 var fieldName="";
 if(url.indexOf("&")>=0){
 fieldName=url.substring(0,url.indexOf("&"));
 }else{
 fieldName=url.substring(0);
 } 
 url=url.substring(fieldName.length);
 var boxValue = listObj.getCBoxValue(fieldName).split("|")[0].split(";")[0];
 url=boxValue+url;
 }
 url=tempurl+url;
 if(target=="new"){
 mdiwin(url,bname) ;
 }else{
 location.href=url ;
 }
}
function importData(target,importType){
 
 if(importType=="bill"){
 
 var strURL = "/billExportAction.do?operation=6&type=addBillPrepare&keyId="+target ;
 mdiwin(strURL,"单据导入") ;
 }else{
 var strURL = "/importDataAction.do?operation=91&tableName="+document.getElementById("tableName").value+"&parentTableName="+parentTableName+"&moduleType="+moduleType ;
 
 strURL = strURL + "&NoBack=NoBack";
 mdiwin(strURL,"数据导入") ;
 
 }
}

function hasSC(fieldName){
 var varValue = document.getElementById(fieldName) ;
 if(!containSC(varValue.value)){
 alert("不能含有特殊字符(' \" | ; \\)") ;
 varValue.focus() ;
 return false;
 }
 return true ;
}

var linkStr="";

function showData(inp,selectName,fieldName,display){

 var dropdown=inp.dropdown;
 if(dropdown==undefined)
 {
 if(event.keyCode==13){
 if(!beforeButtonQuery())
 return false ; 
 submitQuery();
 return;
 }
 
 var data = {
 selectName:selectName,
 operation:"DropdownPopup",
 MOID:MOID,
 MOOP:"query",
 selectField:fieldName,
 };
 dropdown = new dropDownSelect("t_"+inp.id,data,inp,selectName);
 dropdown.retFun = setFieldValue;
 dropdown.showData();
 return ;
 }
 if(event.keyCode == 38)
 {
 if(dropdown!=undefined)
 {
 dropdown.selectUp();
 }
 return ;
 }else if(event.keyCode==40)
 {
 if(dropdown!=undefined)
 {
 dropdown.selectDown();
 }
 return ;
 }else if(event.keyCode==13)
 {
 if(dropdown!=undefined)
 {
 dropdown.curValue();
 }
 return ;
 }else if(event.keyCode==27)
 {
 if(dropdown!=undefined)
 {
 dropdown.close();
 }
 return ;
 }
 
 dropdown.showData();
}

function cl(inp)
{
 setTimeout(function(){if(inp.dropdown != undefined)inp.dropdown.close();},200);
}


var hideFieldName;
var hideSelectName;
function openSelect(selectName,fieldName,display){
 if(typeof(window.parent.$("#bottomFrame").attr("id"))!="undefined"){
 asyncbox = parent.asyncbox;
 }
 hideFieldName = fieldName;
 hideSelectName = selectName;
 var moduleType = document.getElementById("moduleType").value ;
 var urlstr = '/UserFunctionAction.do?operation=22&src=menu&parentTableName='+parentTableName+'&moduleType='+moduleType+'&displayName='+encodeURI(display)+'&LinkType=@URL:&tableName=tblCompany&selectName='+selectName+encodeURI(linkStr) ;
 
 if(urlstr.indexOf("#")!=-1){
 urlstr=urlstr.replaceAll("#","%23") ;
 }
 if(urlstr.indexOf("+")!=-1){
 urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 linkStr="";
 
 urlstr += "&MOID="+MOID+"&MOOP=query&popupWin=Popdiv";
 asyncbox.open({id:'Popdiv',title:display,url:urlstr,width:750,height:470});
} 

function exePopdiv(returnValue){
 var str = returnValue;
 var fieldName = hideFieldName;
 var selectName = hideSelectName
 if(typeof(str)!="undefined"){
 }
}


var advancehideFieldName;
var advancehideSelectName;
function openadvanceSelect(selectName,fieldName,display){
 if(typeof(window.parent.$("#bottomFrame").attr("id"))!="undefined"){
 asyncbox = parent.asyncbox;
 }
 advancehideFieldName = fieldName;
 advancehideSelectName = selectName;
 var moduleType = document.getElementById("moduleType").value ;
 var urlstr = '/UserFunctionAction.do?operation=22&src=menu&parentTableName='+parentTableName+'&moduleType='+moduleType+'&displayName='+encodeURI(display)+'&LinkType=@URL:&tableName=tblCompany&selectName='+selectName+encodeURI(linkStr) ;
 
 if(urlstr.indexOf("#")!=-1){
 urlstr=urlstr.replaceAll("#","%23") ;
 }
 if(urlstr.indexOf("+")!=-1){
 urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 linkStr="";
 
 urlstr += "&MOID="+MOID+"&MOOP=query&popupWin=Popadvancediv";
 asyncbox.open({id:'Popadvancediv',title:display,url:urlstr,width:750,height:470});
} 

function exePopadvancediv(returnValue){
 var str = returnValue;
 var fieldName = advancehideFieldName;
 var selectName = advancehideSelectName
 if(typeof(str)!="undefined"){
 }
}

function setFieldValue(str,selectName,fieldName){
 var fs=str.split("#;#");
 
 }

function AuditingBlockUI(strUrl){
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 window.location.href=strUrl ;
}
function submitQuery(){
 document.forms[0].operation.value=4;
 document.forms[0].draftQuery.value=''; 
 document.forms[0].submit();
}

function draftAudit(rowRemark){
 if(!isChecked("keyId")){ 
 alert('请选择至少一条记录');
 return false;
 }

 var draft = getSelectRowValue("workflownodename").split("#|#");
 var rowRemarkVal=getSelectRowValue(rowRemark).split("#|#");
 var rowVals="";
 for(var i=0;i<draft.length;i++){
 if(draft[i]!="draft" && draft[i] !=""){
 rowVals+=rowRemarkVal[i].split("#;#")[0]+",";
 }
 }
 if(rowVals.length>0){
 alert(rowVals.substring(0,rowVals.length-1)+"不是草稿");
 return false;
 }
 
 
 form.operation.value = 25 ;
 document.getElementById("ButtonType").value = "draftAudit" ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 document.getElementById("form").target = "formFrame" ;
 form.submit() ;
}



function showDivCustomSetTable(){ 
 var returnValue = window.showModalDialog("/UserFunctionQueryAction.do?tableName=tblCompany&operation=4&parentTableName=&configType=listConfig&checkTab=Y&winCurIndex="+winCurIndex,"","dialogWidth=670px;dialogHeight=450px") ;
 if(typeof(returnValue)!="undefined"){
 var strType = returnValue.split("@@") ;
 if(strType[0]=="colWidth"){
 colConfigForm.action = "/ColDisplayAction.do?operation=1" ;
 }else if(strType[0]=="defaultWidth"){
 colConfigForm.action = "/ColDisplayAction.do?operation=6" ;
 }else if(strType[0]=="defaultConfig"){
 colConfigForm.action = "/ColConfigAction.do" ;
 }else if(strType[0]=="colNameSet"){
 mdiwin(strType[1],'列名设置') ;
 return ;
 }
 colConfigForm.colSelect.value=strType[1] ;
 colConfigForm.submit() ;
 }
}

top.cancelImport = false ;
function Refresh() {
 var ready = true ;
 path="/UtilServlet?operation=importInfo&importName=shelfType"+"&time="+(new Date()).getTime();;
 if(top.cancelImport){
 path="/UtilServlet?operation=cancelImport&importName=shelfType"+"&time="+(new Date()).getTime();;
 
 }
 var xmlHttp;
 if (window.ActiveXObject) {
 xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
 } 
 else if (window.XMLHttpRequest) {
 xmlHttp = new XMLHttpRequest();
 }
 xmlHttp.onreadystatechange = function(){
 if(xmlHttp.readyState == 4) {
 if(xmlHttp.status == 200) {
 response = xmlHttp.responseText;
 if(response == "nullObject"){
 ready = false ;
 }else if(response != "" && response != "ok"){
 var is = response.split("|");
 document.getElementById("imTotal").innerHTML=is[0];
 document.getElementById("imSuccess").innerHTML=is[1];
 document.getElementById("imError").innerHTML=is[2]; 
 pspan = document.getElementById("progressSpan");
 pross = (Number(is[1])+Number(is[2])) /Number(is[0]);
 pross = pross*100;
 pross = Math.round(pross);
 
 pspan.style.width=pross+"%";
 if(pross>0){
 pspan.innerHTML=pross+"%"; 
 }
 
 }else if(response == "ok"){ 
 document.getElementById("imCancel").innerHTML="正在中止导入过程...";
 top.cancelImport = true;
 }
 }else{
 response = "no response text" ;
 }
 }
 };
 xmlHttp.open("get", path, false);
 xmlHttp.send();
 var hit ;
 if(ready){
 hit =document.getElementById('ask_load').outerHTML;
 }else{
 hit = document.getElementById('ask_load2').outerHTML;
 }
 top.jblockUI({ message: hit, css: { left:'35%', top:'30%', width: '460px',height:'auto' } });
}

function getShopOrder(){
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 location.href='/UserFunctionQueryAction.do?operation=25&ButtonType=shopOrder&tableName=tblSalesOrder' ;
}

function link(linkAddress){
 if(linkAddress=='') return;
 if(linkAddress.indexOf("javascript:")>=0){
 eval(linkAddress);
 }else{
 if(typeof(moduleType)!="undefined" 
 && moduleType!=null && moduleType.length>0){
 linkAddress = linkAddress + "&moduleType="+ moduleType;
 }
 
 var width=document.documentElement.clientWidth;
 var height=document.documentElement.clientHeight; 
 openPop("link",'',linkAddress,width,height,false,true);
 }
}

function deliverToAll(){ 
 if(!isChecked("keyId")){
 alert('请选择至少一条记录');
 return false;
 }
 errorstr="";
 keyIds="";
 jQuery("input[name='keyId']:checked").each(function(){
 bthtml= jQuery(this.parentNode.parentNode).find(".opbt")[0].outerHTML;
 if(bthtml.indexOf("check(")==-1){
 errorstr+=jQuery(this.parentNode.parentNode).find("td:eq(0)").html()+",";
 }else{
 keyIds += $(this).val()+",";
 } 
 });
 if(errorstr.length > 0){
 alert("第"+errorstr+"行不能审核");
 return;
 }
 keyIds = keyIds.substring(0,keyIds.length-1);
 
 var defi = "&defineInfo=";
 
 
 checkDialog("/OAMyWorkFlow.do?keyId="+keyIds+"&tableName=tblCompany&moduleType="+moduleType+"&operation=82&fromPage=erp&type=deliverToAll");
 
 return;
}



function initDownSelect(){ 
 
} 

function reloadkeyIds()
{
 var v = "";
 jQuery("input[name='keyId']").each(function(){if(this.checked)v+=this.value+"|"});
 document.getElementById("varKeyIds").value = v;;
}

function getSelectRowValue(field) 
{
 var v = "";
 jQuery("input[name='keyId']").each(function(){
 if(this.checked)
 v+=jQuery(this.parentNode.parentNode).attr(field)+"#|#";
 });
 return v;
}



function checkDialog(url){
 asyncbox.open({
 id:'deliverTo',url:url,title:'审核',width:650,height:370,
 btnsbar:jQuery.btn.OKCANCEL,
 callback:function(action,opener){
 if(action == 'ok'){
 if(opener.beforSubmit(opener.form)){
 opener.form.submit();
 }
 return false;
 }
　　 }
　 });
}

function hurryTransDialog(url){
 asyncbox.open({
 id:'deliverTo',url:url,title:'催办提醒方式',
　　 　 width:750,height:300,btnsbar : jQuery.btn.OKCANCEL,
 callback : function(action,opener){
 if(action == 'ok'){
 opener.submitModenew();
 return false;
 }
　　 　}
　 });
}

function dealAsyn(){
 jQuery.close('deliverTo');
}

function dealAsyncbox(){
 
 if( beforeButtonQuery()){submitQuery();}
}


function detail(str){
 
 turl="/UserFunctionAction.do?tableName=tblCompany&keyId="+str.split(",")[0]+"&moduleType="+moduleType+"&f_brother="+f_brother+"&operation=5&winCurIndex="+winCurIndex+"&pageNo=&parentCode="+str.split(",")[1]+"&parentTableName=&saveDraft=";
 
 
 width=1050;
 height=600;
 if(width ==0){
 width=document.documentElement.clientWidth;
 }
 if(height ==0){
 height=document.documentElement.clientHeight;
 }
 openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight); 
 
}

function update(str){
 turl="/UserFunctionAction.do?tableName=tblCompany&keyId="+str.split(",")[0]+"&moduleType="+moduleType+"&f_brother="+f_brother+"&operation=7&winCurIndex="+winCurIndex+"&pageNo=&parentCode="+str.split(",")[1]+"&parentTableName=&saveDraft=";
 
 width=1050;
 height=600;
 if(width ==0){
 width=document.documentElement.clientWidth;
 }
 if(height ==0){
 height=document.documentElement.clientHeight;
 }
 openPop('PopMainOpdiv','',turl,width,height,true,height==document.documentElement.clientHeight); 

}

function next(str){
 location.href="/UserFunctionQueryAction.do?tableName=tblCompany&keyId="+str.split(",")[0]+"&parentCode="+str.split(",")[1]+"&operation=4&moduleType="+moduleType+"&winCurIndex="+winCurIndex+"&parentTableName=&checkTab=Y&selectType="+$("#selectType").val();
}


function doChangeParent(parentCode,name,ttableName){
 location.href="/UserFunctionQueryAction.do?tableName=tblCompany&parentCode="+parentCode+"&operation=4&moduleType="+moduleType+"&winCurIndex="+winCurIndex+"&parentTableName=&checkTab=Y&selectType="+$("#selectType").val();
}


function addClass(parentCodev){
 if(notOpenAccount){
 　asyncbox.confirm('未开帐不能做单，是否立即开帐','开帐提示',function(action){
 　　　if(action == 'ok'){
 　　　　　window.location.href="/SysAccAction.do?type=beginAcc";
 　　　}
 　}); 
 return;
 }

 var moduleTypeValue = "" ;
 var moduleType = document.getElementById("moduleType") ;
 if(typeof(moduleType)!= "undefined" && moduleType!=null){
 moduleTypeValue = moduleType.value ;
 } 
 width=1050;
 height=600;
 if(width ==0){
 width=document.documentElement.clientWidth;
 }
 if(height ==0){
 height=document.documentElement.clientHeight;
 }
 usrc ='/UserFunctionAction.do?tableName=tblCompany&parentCode='+parentCodev+'&operation=6&moduleType='+moduleTypeValue+'&f_brother='+f_brother+'&parentTableName=&winCurIndex='+winCurIndex;
 
 openPop('PopMainOpdiv','',usrc,width,height,true,height==document.documentElement.clientHeight); 
 
}

function hurryTrans(str){
 ren = $("#reportName").val();
 if($("#moduleName").val()!="") {
 ren = $("#moduleName").val();
 }
 hurryTransDialog("/vm/oa/workflow/noteModel.jsp?tableName=tblCompany&moduleName="+encodeURI(ren)+"&keyId="+str.split(",")[0],winObj,"dialogWidth=565px;dialogHeight=350px");
}


function check(str){ 
 if(str.split(",")[1]=="approve"){ 
 var defi = "&defineInfo=";
 if(str.split(",").length>2){
 defi += str.split(",")[2];
 }
 jQuery.post("/UserFunctionQueryAction.do?tableName=tblCompany&parentTableName=&moduleType="+moduleType+"&operation=25&ButtonType=saveCheckBill&varKeyIds="+str.split(",")[0]+defi,
 {ButtonTypeName:'审核保存'},
 function(data){
 if(data=="OK"){
 checkDialog("/OAMyWorkFlow.do?keyId="+str.split(",")[0]+"&tableName=tblCompany&moduleType="+moduleType+"&operation=82&fromPage=erp");
 }else if(data.indexOf("defineInfo")==0){
 var ds = data.split(":");
 if(confirm(ds[4].replace(/\\n/g,'\n'))){
 if(ds[2] != ""){
 check(str+(str.split(",").length>2?"":",")+ds[1]+":"+ds[2]+";");
 }
 }else{
 if(ds[3] != ""){
 check(str+(str.split(",").length>2?"":",")+ds[1]+":"+ds[3]+";");
 }
 }
 } else{
 alert(data);
 } 
 });
 }else if(str.split(",")[1]=="reverse"){
 if(confirm('是否确认反审核')){
 location.href="/UserFunctionQueryAction.do?tableName=tblCompany&moduleType="+moduleType+"&keyId="+str.split(",")[0]+"&f_brother="+f_brother+"&operation=83&winCurIndex="+winCurIndex+"&pageNo=&parentTableName=&saveDraft=&parentCode="+parentCode;
 }
 
 }else if(str.split(",")[1]=="cancel"){
 if(confirm('确定撤回?')){
 location.href='/UserFunctionQueryAction.do?nextStep=cancel&moduleType='+moduleType+'&tableName=tblCompany&lastNodeId='+str.split(",")[2]+'&keyId='+str.split(",")[0]+'&currNode='+str.split(",")[3]+'&f_brother='+f_brother+'&operation=79&winCurIndex='+winCurIndex+'&pageNo=&parentTableName=&saveDraft='
 }
 }
}



function onItemDBClick(str){
 eval(str);
}


function getMonthStr() 
{ 
 var dd = new Date(); 
 dd.setDate(1);
 var y = dd.getFullYear()+""; 
 var m = (dd.getMonth()+1)+"";
 var d = dd.getDate()+""; 
 return y+"-"+(m.length==1?"0"+m:m)+"-"+(d.length==1?"0"+d:d); 
} 




function exportAll() {
 if(!confirm("确定要导出全部数据吗？")){
 return false ;
 }
 if(beforeButtonQuery()){
 document.getElementById("export").value = "all" ; 
 } 
 if(typeof(top.junblockUI)!="undefined"){
 top.junblockUI();
 }
 form.submit() ;
}


function tuoZhan(){ 
 if(!isChecked('keyId')){
 alert('请选择至少一条记录');
 return false;
 }
 var obj = $("input[name=keyId]:checked");
 if(obj.size()>1){
 alert('只能选择一条记录');
 return false;
 }
 var keyId = obj[0].value;
 if(delHasChild("keyId")){
 alert('不能对父类授权');
 return false;
 }
 var urlstr = "/RoleQueryAction.do?moduleId="+keyId;
 asyncbox.open({id:'MainPopup',title:'权限分配',url:urlstr,width:800,height:410}); 
}

function billDetQuery(reportNumber,detTable,reportName){ 
 
 width=document.documentElement.clientWidth-50;
 height=document.documentElement.clientHeight-20;
 
 turl = '/ReportDataAction.do?reportNumber='+reportNumber+'&noback=true&detTable_list='+detTable+'&moduleType='+moduleType+'&src=menu';
 
 openPop('PopBillDetQuerydiv'+reportNumber,'',turl,width,height,false,false);
}

function billQuery(value,dis,width,height){
 if(value.indexOf("@ValueofDB:") >-1){
 alert("列表的联查，只支持无参数方式");
 } 
 if(width==''){
 width=document.documentElement.clientWidth-10;;
 }
 if(height==''){
 height=document.documentElement.clientHeight-10;;
 }
 if(value.indexOf("noback") == -1){
 value = value+"&noback=true";
 }
 if(value.indexOf("src=") == -1){
 value = value+"&src=menu";
 }
 var now=new Date(); 
 var number = now.getTime();
 openPop('PopBillQuerydiv'+number,dis,value,width,height,false,false);
}

function showSerach(){

 $(".more-search").click(function(){
 var mShow = $(this).attr("show");
 if(mShow == "f"){
 $("#conditionDIV2").height($(".search-list-ul").height());
 $(this).attr("show","t").addClass("more-up").removeClass("more-down").attr("title","收起查询列表");
 $("#conter").css("height",$("#conter").height()-($(".search-list-ul").height()-55));
 $("#k_data").css("height",$("#k_data").height()-($(".search-list-ul").height()-55));
 $("#k_column").css("height",$("#k_column").height()-($(".search-list-ul").height()-55));
 }else{
 $("#conditionDIV2").height("55px");
 $(this).attr("show","f").addClass("more-down").removeClass("more-up").attr("title","展开查询列表");
 $("#conter").css("height",$("#conter").height()+($(".search-list-ul").height()-55));
 $("#k_data").css("height",$("#k_data").height()+($(".search-list-ul").height()-55));
 $("#k_column").css("height",$("#k_column").height()+($(".search-list-ul").height()-55));
 }
 });
}

function showSet(tableN){
 if(typeof(tableN)=="undefined"){
 tableN = $("#tableName").val();
 }
 if(moduleType != ""){
 tableN = tableN+"_"+moduleType;
 }
 asyncbox.open({
　　　 id:'setId',url:'/ReportSetQueryAction.do?operation=4&showSet=true&tableName='+tableN+'&winCurIndex=5',
 cache:false,modal:false,title:'显示列设置',width:520,height:490,
 btnsbar : [{
 text:'保 存',action:'save'
 }].concat(jQuery.btn.CANCEL),
 callback : function(action,iframe){
 　　　　 if(action == 'save'){
 　　　　 asyncbox.opener("setId").beforSubmit();
 return false;
 　　　　 }
 　　}
 });
}
function billTool(value,dis,width,height){

 if(width=='' || width>document.documentElement.clientWidth-100){
 width=document.documentElement.clientWidth-100;
 }
 if(height=='' || height > document.documentElement.clientHeight-80){
 height=document.documentElement.clientHeight-80;
 }
 
 value = value.replaceAll('#;#',encodeURIComponent('#;#'));
 
 if(value.indexOf("@ValueofDB:keyId") > 0){
 pos = value.indexOf("@ValueofDB:keyId");
 epos = value.indexOf("&",pos);
 if(epos==-1){
 epos = value.length;
 }
 
 ok =$("input[name=keyId]:checked");
 if(ok.length ==0){
 alert('请选择至少一条记录');
 return false;
 }else if(ok.length > 1){
 alert('只能选择一条记录');
 return false;
 }else{
 val = ok.val();
 value = value.replace("@ValueofDB:keyId",val);
 }
 
 }
 
 value= value+"&LinkType=@URL:&src=menu";
 
 openPop('PopTooldiv',dis,value,width,height,false,false);
}
