jQuery().ready(function() {
 $(document).on("click", function(){
 hideAbout();
 });
 
 
 $('textarea').live('mouseover mouseout', function(event) {
 if (event.type == 'mouseover') {
 $("#showKK").unbind("mousedown");
 }else {
 publicDragDiv($("#showKK"));
 }
 });
 $('.kk_class').live('mouseover mouseout', function(event) {
 
 if (event.type == 'mouseover') {
 $("#showKK").unbind("mousedown");
 }else {
 publicDragDiv($("#showKK")); 
 }
 }); 
 $('.mag_search').live('mouseover mouseout', function(event) {
 
 if (event.type == 'mouseover') {
 $("#popup_msg").unbind("mousedown");
 }else {
 publicDragDiv($("#popup_msg"));
 }
 });
 $('.mag_list').live('mouseover mouseout', function(event) {
 
 if (event.type == 'mouseover') {
 $("#popup_msg").unbind("mousedown");
 }else {
 publicDragDiv($("#popup_msg"));
 }
 }); 
 $('#userList').live('mouseover mouseout', function(event) {
 
 if (event.type == 'mouseover') {
 $("#popup_msg").unbind("mousedown");
 }else {
 publicDragDiv($("#popup_msg"));
 }
 }); 
 $('#deptLlist').live('mouseover mouseout', function(event) {
 
 if (event.type == 'mouseover') {
 $("#popup_msg").unbind("mousedown");
 }else {
 publicDragDiv($("#popup_msg"));
 }
 }); 
 
 setTimeout('flushMsg()',1000);
 var menuWidth = $(window.document).width()-560;
 var menuNum = menuWidth/78;
 $('.head_menu ul li').each(function(i){
 if(i>menuNum){
 $('#menuList').append($(this));
 }
 });

 $('.head_menu').hover(function (){
 var leftWidth = $('.head_menu ul li').size()*78 + 10;
 
 },function (){
 
 });
 
 $('.menuother').mouseover(function (){
 if($('#menu_more ul li').size()>0){
 if($('#menu_more ul li').size()>8){
 $('#scroll').height(370);
 }
 var leftWidth = $('.head_menu ul li').size()*78 + 10;
 $('#menu_more').css('left',leftWidth+153).show();
 $('#menu_more').bgiframe();
 var varScript = document.createElement('script');
 varScript.setAttribute('type','text/javascript');
 varScript.src = '/js/crm/scroll.js';
 document.body.appendChild(varScript);
 }
 });
 $('.menuother').click(function (){
 if($('#menu_more ul li').size()==0){
 mdiwin('/moduleFlow.do?operation=destSet&src=menu','我收藏的菜单');
 }
 });
 
 $('#menu_more').mouseleave(function (){
 $('#menu_more').hide();
 });

 
 
 jQuery(".chang_btn").click(
 function (event) {
 if($(".head_menu").is(":hidden")){
 $(this).addClass("po-menu").removeClass("po-fav");
 $(".head_menu").show();
 $("#menuListId").hide();
 jQuery.get("/UtilServlet?operation=viewMenu&viewType=0");
 }else{
 $(this).addClass("po-fav").removeClass("po-menu");
 $(".head_menu").hide();
 $("#menuListId").show();
 jQuery.get("/UtilServlet?operation=viewMenu&viewType=1");
 }
 }
 );
 
 $(document).bind("mouseup",function(){
 $(this).unbind("mousemove");
 });
 publicDragDiv($(".head_nav_menu"));
 
 
 $(".advice-li").hover(
 function() { 
 getAdvice();
 $("#adviceList").show();
 var frameId = $(".curholder").attr("id").replace("holder","iframe") ;
 },
 function() { $("#adviceList").hide(); }
 );
 
 $(".meddage-li").hover(
 function() { $("#msgList").show();},
 function() { $("#msgList").hide(); }
 );

 
 $("#search").focus(function(){ 
 $(this).addClass("hasFocus"); 
 if($(this).val()=="有问题,来找我"){
 $(this).val("");
 }
 $(this).parent().find("ul").show();
 $(this).parent().find("li").removeClass("liFocus");
 $(this).parent().find("li:eq(0)").addClass("liFocus"); 
 }).blur(function(){ 
 
 if(!curPosSearchUl){ 
 $(this).parent().find("ul").hide(); 
 $(this).removeClass("hasFocus"); 
 if($(this).val()==""){
 $(this).val("有问题,来找我");
 } 
 }
 }).keyup(function(){ 
 if(event.keyCode==13){
 searchBtGo();
 }else if(event.keyCode==38){ 
 lis = $(this).parent().find("li");
 pos = jQuery.inArray( $(this).parent().find(".liFocus")[0],lis ) ;
 if(pos > 0){
 $(this).parent().find("li").removeClass("liFocus");
 $(lis[pos-1]).addClass("liFocus"); 
 }
 }else if(event.keyCode==40){ 
 lis = $(this).parent().find("li");
 pos = jQuery.inArray( $(this).parent().find(".liFocus")[0],lis ) ;
 if(pos <lis.length -1){
 $(this).parent().find("li").removeClass("liFocus");
 $(lis[pos+1]).addClass("liFocus"); 
 }
 }
 });
 $("#search").parent().find("li").click(function (){$(this).parent().find("li").removeClass("liFocus");$(this).addClass("liFocus");searchBtGo();});
 
 $("#search").parent().find("ul").hover(function(){curPosSearchUl = true;},function(){curPosSearchUl = false;}); 
});

var curPosSearchUl = false;

function searchBtGo(){
 txt = $("#search").parent().find(".liFocus").text();
 href = $("#search").parent().find(".liFocus").attr("href"); 
 if(href.indexOf("http") ==0){
 href = href.replace("@condition",$("#search").val());
 window.open("/toUrl.jsp?url="+encodeURIComponent(href));
 }else{
 href = href.replace("@condition",($("#search").val()));
 mdiwin(href,txt);
 }
 
 $("#search").parent().find("ul").hide(); 
 $("#search").removeClass("hasFocus"); 
 if($("#search").val()==""){
 $("#search").val("有问题,来找我");
 }
 
 
}


function showMenu(tab,pageNo){

 jQuery.getJSON("/UtilServlet?operation=menu2&defSys="+tab+"&pageNo="+pageNo
 +"&time="+(new Date()).getTime(),function(data){
 var strMenu = '';
 if(data.total>pageNo){
 strMenu += '<div class="arr_rig"><a href="javascript:showMenu('+tab+','+(pageNo+1)+');">&nbsp;</a></div>';
 }
 if(pageNo>1){
 strMenu += '<div class="arr_left"><a href="javascript:showMenu('+tab+','+(pageNo-1)+');">&nbsp;</a></div>';
 }
 var moduleName = "";
 if(tab == "1"){
 moduleName = "ERP导航"
 }else if(tab == "2"){
 moduleName = "OA导航"
 }else if(tab == "3"){
 moduleName = "CRM导航"
 }else if(tab == "4"){
 moduleName = "HR导航"
 }
 strMenu += '<div class="gongnlist"><ul class="icon_ul">';
 if(data.menuList.length>6){
 jQuery.each(data.menuList, function(i, menu) {
 strMenu += '<li title='+menu.name+' class="menuTag" onmouseover="addC(this);" onmouseout="removeC(this);" onclick="mdiwin(\'/newMenu.do?keyId='+menu.id+'&name='+menu.name+'&operation=docFlow&menu=src\',\''+moduleName+'\')"><div class="img_dv"><img class="face front" src="/style/images/newIcon/sale'+(i+1)+'.png"/><img class="face back" src="/style/images/newIcon/sale'+(i+1)+'.png"/></div><span class="word_sp">'+menu.name+'</span></li>';
 });
 }else{
 jQuery.each(data.menuList, function(i, menu) {
 strMenu += '<li title='+menu.name+' class="menuTag less" onmouseover="addC(this);" onmouseout="removeC(this);" onclick="mdiwin(\'/newMenu.do?keyId='+menu.id+'&name='+menu.name+'&operation=docFlow&menu=src\',\''+moduleName+'\')"><div class="img_dv"><img class="face front" src="/style/images/newIcon/sale'+(i+1)+'.png"/><img class="face back" src="/style/images/newIcon/sale'+(i+1)+'.png"/></div><span class="word_sp">'+menu.name+'</span></li>';
 });
 }
 strMenu += '</ul></div><div class="listmore" style="width:'+data.total*(14+3)+'px;">';
 if(data.total>1){
 for(var i=1;i<=data.total;i++){
 if(i==pageNo){
 strMenu += '<a href="javascript:showMenu('+tab+','+i+');" class="sel">'+i+'</a>';
 }else{
 strMenu += '<a href="javascript:showMenu('+tab+','+i+');">'+i+'</a>';
 }
 }
 }
 strMenu += '</div>';
 $("#tab"+tab).html(strMenu);
 });
}

function showTab(tab,obj){
 $('.mTab').each(function(i){
 if(tab==i+1){
 $(this).show();
 }else{
 $(this).hide();
 }
 });
 $('.msub').each(function(i){
 if(tab==$(this).attr("type")){
 $(this).addClass("sel");
 }else{
 $(this).removeClass("sel");
 }
 });
 
 var nLen = $(obj).attr("move");
 $("em.msub_j").stop().animate({left:nLen*49});
 var mLeft = $("em.msub_z").css("left");
 $(".tsub_m a.msub").each(function(){
 $(this).attr("onmouseout","outL("+parseInt(mLeft)+");")
 });
 showMenu(tab,1);
 
}


function openMenu(){ 
 showMenu(defSys,1);
 if($("#menulayer").css("display") == "none"){
 
 $('#menulayer').bgiframe();
 
 $("#menulayer").show();
 $(document).click(function(event){
 if($(event.target).parent().attr("class")=="menuTag"){
 $("#menulayer").hide();
 }
 if($(event.target).parent().attr("id") != "menuId" && $(event.target).attr("id")!="menulayer"){
 if(typeof($(event.target).parents(".menuDiv").attr("id"))=="undefined" 
 || (event.target.tagName=="A" && $(event.target).parent().attr("class")!="tsub_m" 
 && typeof($(event.target).parents(".mTab").attr("class"))=="undefined") ){
 $("#menulayer").hide();
 }
 }
 });
 
 var frameId = $(".curholder").attr("id").replace("holder","iframe") ;
 $(window.frames[frameId].document).click(function(){
 $("#menulayer").hide();
 });
 for(var j=0;j<window.frames[frameId].frames.length;j++){
 $(window.frames[frameId].frames[j].document).click(function(){
 $("#menulayer").hide();
 });
 }
 }else{
 $("#menulayer").hide();
 }
}

function addTab(title,strUrl){
 jQuery.fn.jerichoTab.addTab({
 title: title,
 tabFirer:strUrl,
 closeable: true,
 data: {
 dataType: 'iframe',
 dataLink: strUrl
 }
 }).loadData(); 
}


function mdiwin(strUrl,title,alertId,strType){
 jQuery.fn.jerichoTab.addTab({
 title: title,
 closeable: true,
 tabFirer:strUrl,
 data: {
 dataType: 'iframe',
 dataLink: strUrl
 }
 }).loadData();
 if(typeof(alertId)!="undefined"){
 jQuery.get("/UtilServlet?operation=updateAlertStatus&alertId="+alertId+"&type="+strType);
 }
 
}


function getAdvice(){
 jQuery.getJSON("/UtilServlet?operation=adviceData"+"&time="+(new Date()).getTime(),function(data){
 var adviceBody = $("#adviceBody") ;
 adviceBody.html("") ;
 if(data.length==0){
 $("#headTitle").html("<img src='/style/images/body/tip.gif'/>今天没有您的通知消息了") ;
 adviceBody.append("<li class='noAdvice'>今天没有您的通知消息了，做点其它的吧！</li>") ;
 }else{
 jQuery.each(data, function(i, advice) {
 if(i%2==1){
 adviceBody.append("<li class='txt-li' id='"+advice.id+"' title='"+advice.Title+"'>"+advice.Content+"<b title='已阅' onclick=\"removeAdvice('"+advice.id+"')\"/></b></li>") ;
 }else{
 adviceBody.append("<li class='txt-li' id='"+advice.id+"' title='"+advice.Title+"'>"+advice.Content+"<b id='closeBt' title='已阅' onclick=\"removeAdvice('"+advice.id+"')\"></b></li>") ;
 }
 });
 }
 });
}


function removeAdvice(keyId){
 $("#"+keyId).remove();
 read(keyId);
}





function msgCommunicate(empId,empName){
 if(empId.indexOf("001")==0){
 openMsgDialog("dept",empId,empName) ;
 }else{
 openMsgDialog("person",empId,empName) ;
 } 
}


function showreModule(title,strUrl){ 
 
 if(strUrl.indexOf("src=")== -1){
 if(strUrl.indexOf("?") > 0){
 strUrl = strUrl + "&src=menu";
 }else{
 strUrl = strUrl + "?src= menu";
 }
 } 
 jQuery.fn.jerichoTab.addTab({
 title: title,
 closeable: true,
 tabFirer:strUrl,
 data: {
 dataType: 'iframe',
 dataLink: strUrl
 }
 }).loadData(); 
 $(".second-menu-div").css("display","none");
}


function sureLoginOut(){
 if(confirm('确定要退出系统?','')) {
 window.location='/loginAction.do?operation=11';
 }
}


function promptSound(){
 jQuery.get('/UtilServlet?operation=promptSound&time='+(new Date()).getTime(),function(response){
 if(response=="true"){
 $("#promptSound").attr('class','n5');
 }else{
 $("#promptSound").attr('class','n3');
 }
 });
}

function showAlert(){
 mdiwin('/AlertAction.do?src=menu','预警消息');
}


function flushMsg(){
 jQuery.get("/UtilServlet?operation=msgData"+"&time="+(new Date()).getTime(),function(response){
 if(response.length>0 && response!="no response text"&&response!="close"){
 var arrayMsg = response.split("||") ; 
 if(arrayMsg[0] == 0){ 
 $("#alertId").html("") ;
 }else{
 $("#alertId").html('<div class="newsmag">'+arrayMsg[0]+'</div>') ;
 }
 if(arrayMsg[1] == 0){ 
 $('#adviceId').html('') ;
 }else{
 if(arrayMsg[1] > 99){
 $('#adviceId').html('<div class="newsmag" title='+arrayMsg[1]+'>99+</div>');
 }else{
 $('#adviceId').html('<div class="newsmag">'+arrayMsg[1]+'</div>');
 }
 
 } 
 if(arrayMsg[2] == 0){ 
 $("#messageId").html("") ;
 }else{ 
 if(arrayMsg[3].indexOf("show") != -1){
 var idpre = arrayMsg[3].split("'")[3]; 
 
 if($("#li_"+idpre).val() != undefined){
 if($("#showKKDiv").is(":visible")){
 $("#showKKDiv").addClass('showKKlog-back'); 
 }
 if($("#ul_left>li").size()>1 && !$("#li_"+idpre).hasClass("focus-li")){
 $("#li_"+idpre).addClass('h-li'); 
 }
 }else{ 
 if(arrayMsg[2] > 99){
 $("#messageId").html('<div class="newsmag" title='+arrayMsg[2]+'>99+</div>') ;
 }else{
 $("#messageId").html('<div class="newsmag">'+arrayMsg[2]+'</div>') ;
 } 
 $("#userIdDiv").html(arrayMsg[3]) ;
 } 
 } 
 }
 
 if(arrayMsg[4] != ""){
 $("#soundDivId").html(arrayMsg[4]);
 } 
 }
 var alertTime = 0.5 ; 
 setTimeout('flushMsg()',alertTime*60*1000);
 }); 
}


function read(keyId){
 jQuery.get("/UtilServlet?operation=updateAlertStatus&type=advice&alertId="+keyId+"&time="+(new Date()).getTime(),function(data){
 var adNum = $("#adviceId div") ;
 if(adNum.html()!="" && parseInt(adNum.html())>0){
 if(parseInt(adNum.html())==1){
 $("#adviceId").html("") ;

 }else{
 adNum.html(parseInt(adNum.html())-1) ;
 }
 }
 });
}


function addMyDest(url,title){
 if(url.trim().length>0 && title.trim().length>0){
 jQuery.get("/moduleFlow.do?operation=addMyDest&title="+encodeURIComponent(title)
 +"&url="+encodeURIComponent(url)+"&time="+(new Date()).getTime(), function(data){
 if("ok"==data){
 if(url.indexOf("?")==-1){url = url+"?1=1"}
 var menuWidth = $(window.document).width()-560;
 var menuNum = menuWidth/78;
 if($(".head_menu li").size()>0 && $(".head_menu li").size()<menuNum){
 $(".head_menu ul").append('<li class="tmenu_li" onmouseover="addC(this);" onmouseout="removeC(this);" onclick="javascript:mdiwin(\''+url+'\',\''+title+'\');">'
 + '<div class="img_dv"><img class="face front" src="/style/images/newIcon/sale'+($('.head_menu ul li').size()+1)+'.png"><img class="face back" src="/style/images/newIcon/sale'+($('.head_menu ul li').size()+1)+'.png"></div><span class="word_sp">'+title+'</span></li>');
 }else{
 $("#menuList").append('<li class="tmenu_li" onmouseover="addC(this);" onmouseout="removeC(this);" onclick="javascript:mdiwin(\''+url+'\',\''+title+'\');">'
 + '<div class="img_dv"><img class="face front" src="/icon/ProdManager.png"><img class="face back" src="/icon/ProdManager.png"></div><span class="word_sp">'+title+'</span></li>'); 
 }
 asyncbox.tips('收藏菜单成功')
 var leftWidth = $('.head_menu ul li').size()*78 + 10;
 $('.menuother').css('left',leftWidth+"px");
 }else if("two"==data){
 asyncbox.tips('收藏菜单重复');
 }else{
 asyncbox.tips('收藏菜单失败');
 }
 }); 
 }
}

function changeSystem(sysType){
 location.href='/MenuQueryAction.do?sysType='+sysType ;
}

var posX;
var posY; 
var fdiv;
document.onmouseup = function(){
 document.onmousemove = null;
}
function mousemove(ev){
 if(ev==null) ev = window.event;
 fdiv.style.left = (ev.clientX - posX) + "px";
 fdiv.style.top = (ev.clientY - posY) + "px";
}


var mobDowDivPanal=null;
function showMOBDiv(url,apk,title){
 if(mobDowDivPanal == null){
 $(document.body).append('<div id="mobDowDivPanal" class="search_popup" style="display:none;top: 150px;">'+
 '<div id="Divtitle" class="move_dv" style="cursor:move;"></div>'+
 ' <p class="img_p"><img id="mobDowDivPanalImg" src="" alt="手机二维码" width="150" /></p>'+
 '<p style="text-align:center;margin:30px 0 0 0;">'+
 '<a id="mobDowDivPanalTitle" href="" target="_blank" class="btn btn-success" style="color:#fff;"></a>'+
 '<a class="btn" onclick="$(\'#mobDowDivPanal\').hide();">关闭</a></p>'+
 '<iframe style="width:362px;height:312px;position:absolute;left:-6px;top:-6px;z-index:-1;display:block;border-style:none;filter:Alpha(opacity=0);"></iframe>'+
 '</div>');
 mobDowDivPanal = $("#mobDowDivPanal");
 
 fdiv = document.getElementById("mobDowDivPanal"); 
 $("#Divtitle").mousedown(function(e){
 if(!e) e = window.event; 
 posX = e.clientX - parseInt(fdiv.style.left);
 posY = e.clientY - parseInt(fdiv.style.top);
 document.onmousemove = mousemove; 
 });
 
 }
 $("#mobDowDivPanalImg").attr("src","/CommonServlet?operation=qrcodeDownload&url="+encodeURIComponent(url));
 $("#mobDowDivPanalTitle").html(title);
 $("#mobDowDivPanalTitle").attr("href",apk);
 var sWidth = $(document.body).outerWidth(true);
 var BW = (sWidth-300)/2;
 mobDowDivPanal.css("left",BW);
 
 mobDowDivPanal.show();
}


function addC(obj){
 $(obj).addClass("card-flipped");
}
function removeC(obj){
 $(obj).removeClass("card-flipped");
}
function overL(obj){
 var tNum = $(obj).attr("move");
 $("em.msub_z").stop().animate({left:tNum*49},150);
}
function outL(mLeft){
 $("em.msub_z").stop().animate({left:mLeft},150);
}


function MoveNum(){
 var aLen = $(".tsub_m a.msub").size(); 
 for(var i=0;i<aLen;i++){
 $(".tsub_m a.msub:eq("+i+")").attr("move",i);
 }
 var zz = $(".tsub_m a.sel").attr("move");
 $("em.msub_j").css("left",zz*49);
 $("em.msub_z").css("left",zz*49);
 var mLeft = $("em.msub_j").css("left");
 for(var i=0;i<aLen;i++){
 $(".tsub_m a.msub:eq("+i+")").attr("onmouseout","outL("+parseInt(mLeft)+");");
 }
}

function v7NewMenu(){
 var bodyWidth = $(document.body).outerWidth(true); 
 var fisrtMenuNum = $(".v7-new-menu>div.m-block").size();
 var fisrtLiNum = $(".first-li").size();
 var allMW = (fisrtLiNum*55)+(fisrtMenuNum*5); 
 var menuMove = "<span class=\"menu-move-span\" move=\"right\" title=\"右移\"><b class=\"icons move-right\"></b></span>";
 
 $(".first-menu-list").each(function(){
 if($(this).find(".first-li").size() == "0"){
 $(this).parents("div.m-block").remove();
 }
 });
 var CRMliNum = $("#CRM_UL>li").size();
 var ERPliNum = $("#ERP_UL>li").size();
 var OAliNum = $("#OA_UL>li").size();
 if((bodyWidth-260) > allMW){
 $(".first-li").css("display","inline-block");
 }else if(bodyWidth < "1000"){
 for(var iNum = 0;iNum<3;iNum++){
 $("#CRM_UL>li:eq("+iNum+")").css("display","inline-block");
 $("#OA_UL>li:eq("+iNum+")").css("display","inline-block");
 }
 for(var iNum = 0;iNum<4;iNum++){
 $("#ERP_UL>li:eq("+iNum+")").css("display","inline-block");
 }
 if(OAliNum>3){
 $("#OA_UL").after(menuMove);
 $("#OA_UL").siblings("span").click(function(){
 spanClick(this,3); 
 });
 }
 if(CRMliNum>3){
 $("#CRM_UL").after(menuMove);
 $("#CRM_UL").siblings("span").click(function(){
 spanClick(this,3); 
 });
 }
 if(ERPliNum>4){
 $("#ERP_UL").after(menuMove);
 $("#ERP_UL").siblings("span").click(function(){
 spanClick(this,4); 
 });
 }
 
 }else{
 for(var iNum = 0;iNum<3;iNum++){
 $("#CRM_UL>li:eq("+iNum+")").css("display","inline-block");
 $("#OA_UL>li:eq("+iNum+")").css("display","inline-block");
 }
 
 for(var iNum = 0;iNum<5;iNum++){
 $("#ERP_UL>li:eq("+iNum+")").css("display","inline-block");
 }
 if(OAliNum>3){
 $("#OA_UL").after(menuMove);
 $("#OA_UL").siblings("span").click(function(){
 spanClick(this,3); 
 });
 }
 if(CRMliNum>3){
 $("#CRM_UL").after(menuMove);
 $("#CRM_UL").siblings("span").click(function(){
 spanClick(this,3); 
 });
 }
 if(ERPliNum>5){
 $("#ERP_UL").after(menuMove);
 $("#ERP_UL").siblings("span").click(function(){
 spanClick(this,5); 
 });
 }
 }
 
 $(".common-memu").each(function(){
 $(this).find(".second-third-menu").each(function(){
 $(this).find("ul").append($(this).find(".second-third-menu").find("li").clone()).end().find(".second-third-menu").remove();
 $(this).parents(".second-menu-div").append($(this).clone()).end().remove();
 });
 
 var dNum = $(this).siblings(".second-third-menu").size();
 if(dNum == "0"){
 $(this).find(">ul").addClass("no-siblings").parents("div.second-menu-div").addClass("d-no-siblings"); 
 }
 });
 $(".second-menu-div li>em").each(function(){
 if($(this).text().getBytesLength()>22){
 $(this).addClass("long-var");
 }
 });
 $(".common-memu .no-siblings li>em").each(function(){
 if($(this).text().getBytesLength()>18){
 $(this).addClass("long-var");
 }
 });
 
}

function spanClick(obj,num){
 var move = $(obj).attr("move")
 var oUL = $(obj).siblings(".first-menu-list");
 var lNum =oUL.find(".first-li").size();
 if(move == "right"){
 $(obj).attr("title","左移").attr("move","left").find("b").removeClass("move-right").addClass("move-left");
 oUL.find(".first-li").show();
 var num1 = lNum -num;
 for(var i=0;i<num1;i++){
 oUL.find(".first-li:eq("+i+")").hide();
 }
 }else{
 $(obj).attr("title","右移").attr("move","right").find("b").removeClass("move-left").addClass("move-right");
 oUL.find(".first-li").hide();
 for(var i=0;i<num;i++){
 oUL.find(".first-li:eq("+i+")").show();
 }
 }

}


function showAbout(){
 $(".about-soft").show();
 $(".in-bg").show();
 stopEvent();
}
function hideAbout(){
 $(".about-soft").hide();
 $(".in-bg").hide();
}

function publicDragDiv(obj){
 $(obj).bind("mousedown",function(event){
 var offset_x = $(this)[0].offsetLeft;
 var offset_y = $(this)[0].offsetTop;
 
 var mouse_x = event.pageX;
 var mouse_y = event.pageY; 
 
 
 
 $(document).bind("mousemove",function(ev){
 
 var _x = ev.pageX - mouse_x;
 var _y = ev.pageY - mouse_y;
 
 
 var now_x = (offset_x + _x ) + "px";
 var now_y = (offset_y + _y ) + "px"; 
 
 $(obj).css({
 top:now_y,
 left:now_x
 });
 });
 })
}
var largerImgPanal=null;
function setPICPos(pobj){
 fh =document.documentElement.clientHeight-120;
 fw =document.documentElement.clientWidth;
 oh =$(pobj).height();
 ow =$(pobj).width();
 if(ow > fw-20 || oh > fh-20){
 if((fw-20) * oh/ow < fh-20){
 $(pobj).width(fw-20);
 }else{
 $(pobj).height(fh-20);
 }
 }
 
 fleft = (fw-$(pobj).width())/2;
 ftop =(fh-$(pobj).height())/2+100;
 largerImgPanal.css("left",fleft+"px"); 
 largerImgPanal.css("top",ftop+"px"); 
 
 bh = (largerImgPanal.height()-40)/2;
 $(".lipleftbt").css("top",bh+"px");
 $(".liprightbt").css("top",bh+"px");
}
function changePicPos(isRight){
 if(isRight){
 showPicIndex ++;
 if(showPicIndex == showPICs.length-1){
 $(".liprightbt").hide();
 }
 $(".lipleftbt").show();
 }else{
 showPicIndex --;
 if(showPicIndex == 0){
 $(".lipleftbt").hide();
 }
 $(".liprightbt").show();
 }
 $("#largerImgPanalImg").html('<img id="largerImg" onload="setPICPos(this)" style="border:0px;min-height:200px;min-widht:200px" src="'+showPICs[showPicIndex]+'">');
}
var showPICx=-10000;
var showPICy=-10000;
var showPICsx=-10000;
var showPICsy=-10000;
var showPICs =null;
var showPicIndex =0;
function showPICTop(pic){ 
 if(largerImgPanal == null){
 $(document.body).append("<div id='largerImgPanal'>"+
 "<div class='lipclosebt'> <a onclick='javascript:largerImgPanal.hide()' ></a> </div>"+
 "<div class='lipleftbt'> <a href='javascript:changePicPos(false)' ></a> </div>"+
 "<div class='liprightbt'> <a href='javascript:changePicPos(true)' ></a> </div>"+
 "<div id='largerImgPanalImg'></div> </div>");
 largerImgPanal = $("#largerImgPanal");
 largerImgPanal.mousedown(function(){
 showPICx = event.x;
 showPICy = event.y;
 showPICsx=largerImgPanal.offset().left;
 showPICsy=largerImgPanal.offset().top;;
 });
 largerImgPanal.mouseup(function(){
 showPICx = -10000;
 showPICy = -10000;
 });
 largerImgPanal.mousemove(function(){
 if(showPICx != -10000){
 largerImgPanal.css("left",(showPICsx + event.x - showPICx)+"px"); 
 largerImgPanal.css("top",(showPICsy + event.y - showPICy)+"px"); 
 }
 });
 }
 var picfileName = "";
 if(pic.indexOf("ReadFile.jpg?tempFile=false")> -1){
 picfileName = pic.substring(pic.indexOf("fileName=")+"fileName=".length);
 if(picfileName.indexOf("&")>0){
 picfileName = picfileName.substring(0,picfileName.indexOf("&"));
 }
 }else{
 picfileName = pic;
 }
 picfileName = picfileName.replaceAll("%3B",";"); 
 showPICs = picfileName.split(";");
 showPicIndex = 0;
 for(var i =0;i<showPICs.length && pic.indexOf("ReadFile.jpg?tempFile=false")> -1 ;i++){
 showPICs[i] = pic.substring(0,pic.indexOf("fileName=")+"fileName=".length)+showPICs[i];
 }
 $(".lipleftbt").hide();
 $(".liprightbt").hide();
 $("#largerImgPanalImg").html('<img id="largerImg" onload="setPICPos(this)" style="border:0px;min-height:200px;min-widht:200px" src="'+showPICs[0]+'">');
 if(showPICs.length>1){
 $(".liprightbt").show();
 } 
 largerImgPanal.show();
}

function startIntro(){
 

 var intro = introJs();
 intro.setOptions({
 showBullets:false,
 nextLabel: '下一页 &rarr;',
 prevLabel: '&larr; 上一页',
 skipLabel: '跳过',
 doneLabel: '结束',
 exitOnOverlayClick: false,
 overlayOpacity: 0.5,
 steps: [
 { element: '#search',elementName:'search',
 intro: "有任何不会的问题可以在这里查找<br/>还可以向专家提问哦<br/>这里还可以直接查商品查库存呢",
 position: 'left'
 }, 
 { element: '#newGride',elementName:'newGride',
 intro: "第一次使用软件不知从何下手，那就看看新手引导吧，<br/>他会告诉你系统有些什么功能，如何一步步录入基础资料，开帐，做单.<br/>鼠标移到自己的头像就能找到我哦",
 position: 'left'
 }, 
 { element: '#fastKey',elementName:'fastKey',
 intro: "所有界面快捷按扭的说明可以在这看到",
 position: 'left'
 },
 { element: '.chang_btn',elementName:'head_menu',
 intro: "点这里会显示所有的快捷菜单",
 position: 'bottom'
 }, 
 { element: '.tab_selected',elementName:'tab_selected',
 intro: "想知道这些快捷菜单如何添加吗?<br/>打开一个你常用的模块，在这多窗口栏鼠标右键点击试试!",
 position: 'bottom'
 }, 
 { element: '.ad_em',elementName:'ad_em',
 intro: "如何管理快捷菜单?<br/>删除,调整顺序在这里!",
 position: 'bottom'
 }, 
 { element: '.chang_btn',elementName:'menuListId',
 intro: "要显示所有的功能所有的菜单?<br/>点击这里切换!",
 position: 'bottom'
 }
 ]
 });
 if($(".searchDiv").css('display')=="none"){
 intro._options.steps.shift();
 }
 
 intro.onchange(function(elem,elemName){
 $("#search").parent().find("ul").hide();
 $(".scul-bottom").css("display","");
 if(elemName == "search"){
 $("#search").parent().find("ul").show();
 }else if(elemName == "newGride"||elemName == "fastKey"){
 $(".scul-bottom").css("display","inline-block");
 }else if(elemName == "head_menu"){
 
 $(".chang_btn").addClass("po-menu").removeClass("po-fav");
 $(".head_menu").show();
 $("#menuListId").hide();
 }else if(elemName == "menuListId"){
 $(".chang_btn").addClass("po-fav").removeClass("po-menu");
 $(".head_menu").hide();
 $("#menuListId").show();
 }
 });
 intro.oncomplete(function(){
 jQuery.get("/CommonServlet?operation=firstShow&moduleNo=0&isSave=true");
 });
 intro.onexit(function(){
 jQuery.get("/CommonServlet?operation=firstShow&moduleNo=0&isSave=true");
 });
 intro.start();
}
function changeWebNote(obj){
 
 if(obj.checked){
 $(".head_nav_menu").show();
 }else{
 $(".head_nav_menu").hide();
 }
 jQuery.get("/CommonServlet?operation=changeWebNote&type="+obj.checked);
}
