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
 
function G(id){
 return document.getElementById(id);
} 
 
function openInputDate(obj)
{
 WdatePicker();
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

function allListQuerys(){ 
 var selectType = document.getElementById("selectType").value ;
 if("endClass"==selectType){
 document.getElementById("isAllListQuery").value="YES";
 }
 form.submit() ;
}

function moreBlur(e){
 if(G("more2")!=null&& G("tb_mr2")!=null){
 e = e||window.event;
 var tar = e.target || e.srcElement;
 var btn = window.event?e.button:e.which; 
 while (tar != document.body&&tar.tagName.toLowerCase()!="html") { 
 if (tar == G("tb_mr2") || tar == G("more2")) { 
 break;} 
 tar = tar.parentNode; 
 }
 if (tar != G("more2") && tar != G("tb_mr2")) { 
 G("more2").style.display = "none"; 
 }else {
 if (btn < 2) {G("more2").style.display = "block";}
 }};
 }
 

function showDivCustomSetTable(){
 var returnValue = window.showModalDialog("/ReportDataAction.do?operation=0&reportNumber="+reportNumber+"&configType=listConfig","","dialogWidth=670px;dialogHeight=450px") ;
 if(typeof(returnValue)!="undefined"){
 var strType = returnValue.split("@@") ;
 if(strType[0]=="colWidth"){
 colConfigForm.action = "/ColDisplayAction.do?operation=1" ;
 }else if(strType[0]=="defaultWidth"){
 colConfigForm.action = "/ColDisplayAction.do?operation=6" ;
 }else if(strType[0]=="defaultConfig"){
 colConfigForm.action = "/ColConfigAction.do" ;
 }
 colConfigForm.colSelect.value=strType[1] ;
 colConfigForm.submit() ;
 }
}
function showChildReport(urlstr){
 strs = urlstr.split(":");
 divName = strs[0];
 position = strs[1];
 url = strs[2];
 childRep = document.getElementById("div"+divName);
 childDiv2 = document.getElementById("div2"+divName);
 if(childRep == null || typeof(childRep) == "undefined"){ 
 childRep = document.createElement("<div id='div"+divName+"' style='border:#CCCCCC solid 1px;width:100%;height:100%;margin-top:10px;margin-right:10px;'></div>");
 titleDiv = document.createElement("<div id=divtitle style='padding:3px;height:20px;width:180px'></div>"); 
 titleDiv.innerHTML=divName; 
 childDiv2 = document.createElement("<div id=div2"+divName+"></div>");
 childRep.appendChild(titleDiv);
 childRep.appendChild(childDiv2);
 }
 
 childDiv2.innerHTML = '<iframe src="'+url+'&isTab=yes" scrolling="no" class="list" width="100%" height="100%" frameborder=no ></iframe>'; 
 
 var oDiv=document.getElementById("conter");
 var sh = oDiv.style.height;
 var oDivHeight = Number(sh.substr(0,sh.length-2)); 
 obj = document.getElementById("functionListObj");
 if(obj.height != "100%"){
 if(obj.height > oDivHeight /2){
 obj.height="50%";
 }
 }else{
 obj.height="50%";
 }
 if("R" == position){
 document.getElementById("rightReportDiv").style.height="40%";
 document.getElementById("rightReportDiv").appendChild(childRep); 
 }else if("T" == position){
 document.getElementById("topReportDiv").style.height="40%";
 document.getElementById("topReportDiv").appendChild(childRep); 
 }else{
 document.getElementById("leftReportDiv").style.height="40%";
 document.getElementById("leftReportDiv").appendChild(childRep); 
 }
}

function link(linkAddress){
 if(linkAddress=='') return;
 if(linkAddress.indexOf("javascript:")>=0){
 eval(linkAddress);
 }else{
 
 mnumber = document.form.mainNumber.value;
 if(mnumber==""){
 mnumber=reportNumber;
 }
 linkAddress = linkAddress+"&mainNumber="+mnumber;
 
 var width=document.documentElement.clientWidth;
 var height=document.documentElement.clientHeight; 
 openPop("link",'',linkAddress,width,height,false,true);
 }
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


function openPop(id,title,usrc,width,height,checkFormChange,noTitle){
 asyncbox.open({
 id : id, 
 title : title, 
 　　 html : '<div style="text-align: center;width: 100%; height: 100%;border:none">'+
 '<iframe id="'+id+'_Frame" frameborder="no" border="0" marginwidth="0" marginheight="0" style="width: 100%; height: 99%;top:0px;left:0px;z-index:-1;border:none;filter:Alpha(opacity=100);"></iframe>'+
 '<div id="'+id+'_Frame_1" style="position:absolute;top:'+(height/2-80)+'px;left:'+(width/2-80)+'px;text-align: center;width:128px;height:128px; background: url(/style1/images/ll.gif) no-repeat center;"></div></div>', 
 　　 width : width,
 　　 height : height,
 callback : function(action,opener){
 if(action == 'close' && checkFormChange){
 iframe1 = document.getElementById(id+"_Frame");
 if(iframe1 && iframe1.contentWindow.is_form_changed()){
 if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
 return false;
 }
 }
 }
 }
　 }); 
 if(noTitle){ 
 $("#"+id+" .b_t_m").parent().remove();
 }
 
 $("#"+id+"_content").height($("#"+id).height()-22);
 
 var iframe = document.getElementById(id+"_Frame");
 iframe.src=usrc+"&popWinName="+id; 
 
 if (iframe.attachEvent){ 
 iframe.attachEvent("onload", function(){ 
 $("#"+id+"_Frame_1").hide();
 }); 
 } else { 
 iframe.onload = function(){ 
 $("#"+id+"_Frame_1").hide();
 }; 
 }
}
 

function billTool(value,dis,width,height){ 
 if(width==''){
 width=document.documentElement.clientWidth-10;;
 }
 if(height==''){
 height=document.documentElement.clientHeight-10;;
 }
 value= value+"&LinkType=@URL:";
 
 var now=new Date(); 
 var number = now.getTime();
 openPop('PopReportTooldiv'+number,dis,value,width,height,false,false);
}

function billQuery(value,dis,width,height){ 
 if(width==''){
 width=document.documentElement.clientWidth-10;;
 }
 if(height==''){
 height=document.documentElement.clientHeight-10;;
 }
 value= value+"&LinkType=@URL:";
 var now=new Date(); 
 var number = now.getTime();
 openPop('PopReportQuerydiv'+number,dis,value,width,height,false,false);
}

function showSerach(){
 $(".more-search").click(function(){
 var mShow = $(this).attr("show");
 if(mShow == "f"){
 $("#conditionDIV2").height($(".search-list-ul").height());
 $(this).attr("show","t").addClass("more-up").removeClass("more-down").attr("title","收起搜索列表");
 $("#conter").css("height",$("#conter").height()-($(".search-list-ul").height()-55));
 }else{
 $("#conditionDIV2").height("55px");
 $(this).attr("show","f").addClass("more-down").removeClass("more-up").attr("title","展开搜索列表");
 $("#conter").css("height",$("#conter").height()+($(".search-list-ul").height()-55));
 }
 });
}