
var tabObj ;
$(document).ready(function(){
 document.ondragstart = function(){return false;}; 
 tabObj= new TabObj("input_type"); 
 
 $("#saveButDIV").each(function(){
 if($(this).find(".d-more a").size()==1){
 var clk = $(this).find(".d-more a").attr("onClick");
 if(clk == undefined || clk == "" ){
 clk = $(this).find(".d-more a").attr("href");
 } 
 var cid = $(this).find(".d-more a").attr("id");
 var cname = $(this).find(".d-more a").attr("name");
 var ctitle = $(this).find(".d-more a").attr("title");
 var newli = '<span class="btn btn-small" defbtn="save" id="'+ (cid=='' || cid == undefined ?'':cid) +
 '" name="'+(cname=='' || cname == undefined ?'':cname) +
 '" title="'+ (ctitle=='' || ctitle == undefined ?'':ctitle) +'" onClick="'+clk+'">'+
 $(this).find(".d-more a").html()+
 '</span>';
 $(this).parents("li").html(newli);
 }
 });
 
 $(".h-child-btn").each(function(){
 if($(this).find(".d-more a").size()==0){ 
 $(this).parents("li").hide();
 }
 });
 $(".h-child-btn").mouseover(function(){
 $(this).addClass("h-height").find(".d-more").show().siblings(".br-link").show();
 }).mouseout(function(){
 $(this).removeClass("h-height").find(".d-more").hide().siblings(".br-link").hide();
 });
 
 $(".Detzoom").click(function(){
 if($(this).find(".zoomUp").size()>0){
 $(this).find("span").removeClass("zoomUp");
 $(this).find("span").addClass("zoomDown");
 $("#listRange_mainField").hide();
 $("#listRange_remark").hide();
 $("#listRange_tableInfo").height(tabH+mainFieldH+remarkH); 
 }else{
 $(this).find("span").removeClass("zoomDown");
 $(this).find("span").addClass("zoomUp");
 $("#listRange_mainField").show();
 $("#listRange_remark").show();
 $("#listRange_tableInfo").height(tabH); 
 }
 
 $("#listRange_tableInfo > div").height($("#listRange_tableInfo").height()); 
 $("div[id*=_tableLayout]").height($("#listRange_tableInfo").height());
 $("div[id*=_tableData]").height($("#listRange_tableInfo").height());
 $("div[id*=_tableColumn]").height($("#listRange_tableInfo").height()-25);
 });
 
 $(document).mouseup( function() { 
 if(curDragObj != null){
 
 var dobjs = document.getElementsByName(curDragObj.attr("name"));
 sl = curDragStartLine;
 el = curDragEndLine;
 if(el==-1){
 el=sl; 
 }
 if(sl>el){
 sl = curDragEndLine;
 el = curDragStartLine;
 }
 for(var i=sl;i<=el;i++){
 dobjs[i].value= curDragObj.val();
 $(dobjs[i]).css("border","");
 $(dobjs[i]).trigger("change");
 
 }
 execStat();
 curDragObj = null;
 curchildOpObj=null;
 curDragStartLine = -1;
 curDragEndLine = -1;
 
 if(window.k_interval != undefined)
 {
 clearInterval(window.k_interval);
 window.k_interval = undefined;
 }
 }
 } ); 
 $(document).mousemove( function() { 
 if(curDragObj != null){
 
 dirObj = window.event.srcElement;
 
 var dragobjs = document.getElementsByName(curDragObj.attr("name"));
 
 var myDiv = jQuery("#"+curGridRowNum.curKey+"Table_tableData");
 if(event.clientY < myDiv[0].getBoundingClientRect().top+10)
 {
 if(curDragEndLine == -1) curDragEndLine = curDragStartLine;
 if(window.k_interval==undefined)
 {
 window.k_interval = setInterval(function(){
 myDiv.scrollTop(myDiv.scrollTop()-25); 
 curDragEndLine--; 
 $(dragobjs[curDragEndLine]).css("border","1px solid #ff00ff");
 },300);
 } 
 
 }else if(event.clientY > myDiv[0].getBoundingClientRect().bottom-10)
 {
 if(curDragEndLine == -1) curDragEndLine = curDragStartLine;
 if(window.k_interval==undefined)
 {
 window.k_interval = setInterval(function(){
 myDiv.scrollTop(myDiv.scrollTop()+25); 
 curDragEndLine++; 
 $(dragobjs[curDragEndLine]).css("border","1px solid #ff00ff");
 },300);
 }
 
 }else
 {
 if(window.k_interval != undefined)
 {
 clearInterval(window.k_interval);
 window.k_interval = undefined;
 }
 
 } 
 
 var dirLine = -1;
 var objs = document.getElementsByName(dirObj.name);
 if(objs.length<2)return; 
 for(i=0;i<objs.length;i++){
 if(dirObj == objs[i]){ 
 dirLine = i;
 }
 }
 if(dirLine == -1 || curDragEndLine == dirLine){ return; }
 curDragEndLine = dirLine;
 var sl = curDragStartLine;
 var el = dirLine;
 if(sl>el){
 sl = dirLine;
 el = curDragStartLine;
 }
 
 for(var i=0;i<dragobjs.length;i++){
 if(i<sl||i>el){
 $(dragobjs[i]).css("border","");
 }else{
 $(dragobjs[i]).css("border","1px solid #ff00ff");
 }
 }
 }
 } ); 
});

var detOpDivPanal=null;
var detTObj=null;
function detOpShow(tobj){
 detTObj = tobj;
 if(detOpDivPanal == null){
 $(document.body).append("<div id='detOpDivPanal' > </div>");
 detOpDivPanal = $("#detOpDivPanal");
 var inStr = ""; 
 inStr += "<li id='o_upline' class='f-icon16' title='上移'></li>";
 inStr += "<li id='o_downline' class='f-icon16' title='下移'></li>";
 inStr += "<li id='o_addline' class='f-icon16' title='插入一行'></li>";
 inStr += "<li id='o_stickline' class='f-icon16' title='复制整行'></li>";
 detOpDivPanal.html(inStr); 
 detOpDivPanal.mouseover( function() { $(this).show();}).mouseout(function() { $(this).hide(); });
 $("#o_upline").click(function(){
 tabn = $(detTObj).parents("table").attr("id");
 tabn = tabn.substring(0,tabn.indexOf("Table_"));
 if($(detTObj).prev().html().indexOf("<")!=-1)
 lineId = Number($(detTObj).prev().html().substring(0,$(detTObj).prev().html().indexOf("<")))-1;
 else 
 lineId = Number($(detTObj).prev().html())-1;
 $("#"+tabn+"TableDIV")[0].fix.moveRow(lineId,true);
 detOpDivPanal.hide(); 
 detTObj=null;
 });
 $("#o_downline").click(function(){
 tabn = $(detTObj).parents("table").attr("id");
 tabn = tabn.substring(0,tabn.indexOf("Table_"));
 if($(detTObj).prev().html().indexOf("<")!=-1)
 lineId = Number($(detTObj).prev().html().substring(0,$(detTObj).prev().html().indexOf("<")))-1;
 else 
 lineId = Number($(detTObj).prev().html())-1;
 $("#"+tabn+"TableDIV")[0].fix.moveRow(lineId,false);
 detOpDivPanal.hide(); 
 detTObj=null;
 });
 $("#o_stickline").click(function(){
 asyncbox.prompt('复制整行','小提示:请输入插入的行号，默认复制到下一行','','text',function(action,val){
 　　　if(action == 'ok'){
 line = curGridRowNum.curLine+1;
 if(val != ""){
 　　　　　 if(!isInt(val)){ alert('请输入整数'); return;}
 line = parseInt(val); 
 }
 tabn = $(detTObj).parents("table").attr("id");
 tabn = tabn.substring(0,tabn.indexOf("Table_"));
 if($(detTObj).prev().html().indexOf("<")!=-1)
 lineId = Number($(detTObj).prev().html().substring(0,$(detTObj).prev().html().indexOf("<")))-1;
 else 
 lineId = Number($(detTObj).prev().html())-1;
 $("#"+tabn+"TableDIV")[0].fix.copyRow(lineId,line);
 
 var gdata = eval(tabn+"Data");
 for(var i=0;i<gdata.cols.length;i++){
 if(gdata.cols[i].fieldType==0 || gdata.cols[i].fieldType==1){
 var robj = document.getElementsByName(gdata.cols[i].name);
 if(robj.length>0){
 $(robj[line]).trigger("change");
 }
 }
 }
 curGridRowNum.curLine = line;
 　　　}
 detOpDivPanal.hide(); 
 detTObj=null;
 　});

 });
 $("#o_addline").click(function(){
 tabn = $(detTObj).parents("table").attr("id");
 tabn = tabn.substring(0,tabn.indexOf("Table_"));
 if($(detTObj).prev().html().indexOf("<")!=-1)
 lineId = Number($(detTObj).prev().html().substring(0,$(detTObj).prev().html().indexOf("<")))-1;
 else 
 lineId = Number($(detTObj).prev().html())-1;
 insertRowClick(tabn+"Table",lineId+1); 
 detOpDivPanal.hide(); 
 detTObj=null;
 });
 
 }
 
 
 
 
 detOpDivPanal.show();
 ftop =$(tobj).offset().top+$(tobj).height()-1;
 fleft =$(tobj).offset().left+6;
 detOpDivPanal.css("left",fleft+"px"); 
 detOpDivPanal.css("top",ftop+"px"); 
}
function detOpHide(obj){
 detOpDivPanal.hide();
}


var childOpDiv=null;
var curchildOpObj=null;
var curDragObj=null; 
var curDragStartLine=-1; 
var curDragEndLine=-1;
function childFocus(tobj){
 if(curchildOpObj ==null || curchildOpObj[0] != tobj[0]){ 
 curchildOpObj = tobj; 
 } 
}

var confirmdeldet = "true";


function celldelline(){
 if(curchildOpObj == null || typeof(curchildOpObj) == "undefined"){
 alert("请先选择明细表需要删除的行");
 return;
 }
 deleditGridRow(curchildOpObj[0]);
}


function celladdline(){
 if(curchildOpObj == null || typeof(curchildOpObj) == "undefined"){
 alert("请先选择明细表需要插入的行");
 return;
 }
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 insertRowClick(tabn+"Table",curGridRowNum.get(tabn)+1); 
 curchildOpObj[0].focus(); 
}

function cellstickline(){
 if(curchildOpObj == null || typeof(curchildOpObj) == "undefined"){
 alert("请先选择明细表需要复制的行");
 return;
 }
 asyncbox.prompt('复制整行','小提示:请输入插入的行号，默认复制到下一行','','text',function(action,val){
 　　　if(action == 'ok'){
 line = curGridRowNum.curLine+1;
 if(val != ""){
 　　　　　 if(!isInt(val)){ alert('请输入整数'); return;}
 line = parseInt(val); 
 }
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 $("#"+tabn+"TableDIV")[0].fix.copyRow(curGridRowNum.get(tabn),line);
 
 var gdata = eval(tabn+"Data");
 for(var i=0;i<gdata.cols.length;i++){
 if(gdata.cols[i].fieldType==0 || gdata.cols[i].fieldType==1){
 var robj = document.getElementsByName(gdata.cols[i].name);
 if(robj.length>0){
 $(robj[line]).trigger("change");
 }
 }
 }
 
 curchildOpObj[0].focus(); 
 　　　}
 　});

}

function celupline(){
 if(curchildOpObj == null || typeof(curchildOpObj) == "undefined"){
 alert("请先选择明细表需要上移的行");
 return;
 }
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 $("#"+tabn+"TableDIV")[0].fix.moveRow(curGridRowNum.get(tabn),true);
 curchildOpObj[0].focus(); 
}

function celdownline(){
 if(curchildOpObj == null || typeof(curchildOpObj) == "undefined"){
 alert("请先选择明细表需要下移的行");
 return;
 }
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 $("#"+tabn+"TableDIV")[0].fix.moveRow(curGridRowNum.get(tabn),false);
 curchildOpObj[0].focus(); 
}


function cellDrag(){
 if(curchildOpObj == null || typeof(curchildOpObj) == "undefined"){
 alert("请先选择明细表需要复制的输入框");
 return;
 }
 if(curchildOpObj.attr("ondblclick") != null && curchildOpObj.attr("relationkey") !="true"){
 alert("弹出窗字段不允许进行拖选复制");
 return;
 }
 curDragObj = curchildOpObj; 
 curDragObj.css("border","1px solid #ff00ff");
 curDragStartLine = curGridRowNum.curLine; 
}

function cellCalc(){
 if(curchildOpObj == null || typeof(curchildOpObj) == "undefined"){
 alert("请先选择明细表需要计算的输入框");
 return;
 }
 if(curchildOpObj.attr("num") != "true"){
 alert("非数字录入框不能使用计算器");
 return;
 }
 var urls = "/common/calc.jsp"; 
 asyncbox.open({
 id : 'PopCalcdiv',
 title : '计算器',
 url : urls,
 width : 210,
 height : 260,
 top: 25 ,
 　　 btnsbar : jQuery.btn.OKCANCEL, 
 　　 callback : function(action,iframe){
 　　　　　 if(action == 'ok'){
 　　　　　　　 if(curchildOpObj != null && typeof(curchildOpObj) != "undefined"){
 val = iframe.document.getElementById("lcd").value;
 if(val.substring(val.length-1)=="."){
 val = val.substring(0,val.length-1);
 } 
 for(i=0;i<fieldDigit.length;i++){
 if(fieldDigit[i].name ==curchildOpObj.attr("name")){
 val=f(val,fieldDigit[i].digit);
 } 
 } 
 curchildOpObj.val(val);
 curchildOpObj.trigger("change");
 }
 　　　　　 }
 curchildOpObj[0].focus(); 
 }
　　　}); 
 
 
}

function childDbClick(){
 alert(tobj);
}
function childBlur(){
 if(childOpDiv != null){
 childOpDiv.hide();
 }
}

function cFireEvent(fn,defaultValue){
 var z=n(fn); 
 for(var i = 0;i<z.length;i++){ 
 if(z[i].value.length>0){ 
 if((defaultValue.length>0&&z[i].value!=defaultValue) ||defaultValue.length==0){
 $(z[i]).change();
 }
 }
 }
}

function loadFireEvent()
{ 
 cFireEvent('tblSalesOrderDet_Qty',''); 
 cFireEvent('tblSalesOrderDet_Price',''); 
 }

function auditing(){
 var varReturn = window.showModalDialog("/OAFileWorkFlowAction.do?operation=94&type=submit&tableName=tblSalesOrder&workFlowNode="+workFlowNode,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value =strFile[0] ;
 m("varNextNode").value =strFile[1] ;
 m("varCheckPersons").value =strFile[2] ;
 m("varAttitude").value =strFile[3] ;
 m("operation").value ="79" ;
 m("optionType").value ="auditing" ;
 return true ;
}
function overFlow(){
 var varReturn = window.showModalDialog("/vm/oa/fileflow/repealFile.jsp?type=over&&wakeup="+strWakeUp,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value =strFile[0] ;
 m("varAttitude").value =strFile[1] ;
 m("operation").value ="79" ;
 m("optionType").value ="overFlow" ;
}

function repelFile(){
 var varReturn = window.showModalDialog("/vm/oa/fileflow/repealFile.jsp?wakeup="+strWakeUp,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value =strFile[0] ;
 m("varAttitude").value =strFile[1] ;
 m("operation").value ="79" ;
 m("optionType").value ="repeal" ;
}

function backAuditing(){
 var varReturn = window.showModalDialog("/OAFileWorkFlowAction.do?operation=94&type=back&keyId="+valueId+"&tableName=tblSalesOrder&workFlowNode="+workFlowNode,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value = strFile[0] ;
 m("varNextNode").value = strFile[1] ;
 m("varCheckPersons").value = strFile[2] ;
 m("varAttitude").value = strFile[3] ;
 m("operation").value = "79" ;
 m("optionType").value = "returnAuditing" ;
}

function auditLogList(){
 var file = window.showModalDialog("/UserFunctionAction.do?tableName=tblSalesOrder&keyId="+valueId+"&checkTab=Y&optionType=Auditing&type=audtiting&operation=5&fromPage=yes",
 winObj,"dialogWidth:800px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
}
function selectDispense(){
 var varPerson = window.showModalDialog("/vm/oa/fileflow/selectReadPerson.jsp?keyId="+valueId+"&type=dispense&wakeup="+strWakeUp,
 winObj,"dialogWidth:700px;dialogHeight:300px;center:yes;help:no;resizable:no;status:no;scroll:no;") ;
}
function selectReadPerson(){
 var varPerson = window.showModalDialog("/vm/oa/fileflow/selectReadPerson.jsp?keyId="+valueId+"&tableName=tblSalesOrder&wakeup="+strWakeUp,
 winObj,"dialogWidth:700px;dialogHeight:300px;center:yes;help:no;resizable:no;status:no;scroll:no;") ;
}
function newSave()
{ 
 form.id.value="";
 operation=1 ;
 if(beforSubmit(document.form)) {window.save=true; document.form.submit(); }
}

function draftSave()
{ 
 operation=1;
 if(beforSubmit(document.form)) {
 form.button.value="quoteDraft"; 
 window.save=true;
 form.operation.value=1 ;
 document.form.submit(); 
 }
}
function draftSaveDeliver()
{ 
 operation=1;
 if(beforSubmit(document.form)) {
 form.button.value="quoteDraft"; 
 window.save=true;
 form.operation.value=1;
 form.deliverTo.value='true';
 document.form.submit(); 
 }
}

function copyAdd(){ 
 if(beforSubmit(document.form)) {
 form.id.value="";
 form.button.value="copyAdd" ;
 window.save=true; 
 form.operation.value=1 ;
 document.form.submit(); 
 }

}

var gridDatas = new Array();
var gridDataNum = 0;







function copyFields2(index,order,tableName,copyAll){
 if(index==0)return ;
 var copyFields;
 if(typeof(tableName)!="undefined" && tableName=="tblSalesOrderDet"){
 
 copyFields=n('tblSalesOrderDet_StockCode');
 if(copyFields[index].value !=0 && copyFields[index].value != ""){return}
 if((order > 0 || (typeof(copyAll)!="undefined" && "copyAll"==copyAll)) && typeof(copyFields[index])!="undefined" && (copyisNull(copyFields[index].value))){
 copyFields[index].value = copyFields[index-1].value;
 }
 copyFields=n('tblSalesOrderDet_GoodsCode');
 if((order > 1 || (typeof(copyAll)!="undefined" && "copyAll"==copyAll)) && typeof(copyFields[index])!="undefined" && (copyisNull(copyFields[index].value))){
 copyFields[index].value = copyFields[index-1].value;
 var tblSalesOrderDet_tblGoods_GoodsNumber = n('tblSalesOrderDet_tblGoods_GoodsNumber');
 if(typeof(tblSalesOrderDet_tblGoods_GoodsNumber) != 'undefined' && tblSalesOrderDet_tblGoods_GoodsNumber.length >index){
 tblSalesOrderDet_tblGoods_GoodsNumber[index].value = tblSalesOrderDet_tblGoods_GoodsNumber[index-1].value;
 }
 var tblSalesOrderDet_tblGoods_GoodsFullName = n('tblSalesOrderDet_tblGoods_GoodsFullName');
 if(typeof(tblSalesOrderDet_tblGoods_GoodsFullName) != 'undefined' && tblSalesOrderDet_tblGoods_GoodsFullName.length >index){
 tblSalesOrderDet_tblGoods_GoodsFullName[index].value = tblSalesOrderDet_tblGoods_GoodsFullName[index-1].value;
 }
 var tblSalesOrderDet_tblGoods_GoodsSpec = n('tblSalesOrderDet_tblGoods_GoodsSpec');
 if(typeof(tblSalesOrderDet_tblGoods_GoodsSpec) != 'undefined' && tblSalesOrderDet_tblGoods_GoodsSpec.length >index){
 tblSalesOrderDet_tblGoods_GoodsSpec[index].value = tblSalesOrderDet_tblGoods_GoodsSpec[index-1].value;
 }
 var tblSalesOrderDet_tblGoods_BaseUnit = n('tblSalesOrderDet_tblGoods_BaseUnit');
 if(typeof(tblSalesOrderDet_tblGoods_BaseUnit) != 'undefined' && tblSalesOrderDet_tblGoods_BaseUnit.length >index){
 tblSalesOrderDet_tblGoods_BaseUnit[index].value = tblSalesOrderDet_tblGoods_BaseUnit[index-1].value;
 }
 var tblSalesOrderDet_tblStocks_lastQty = n('tblSalesOrderDet_tblStocks_lastQty');
 if(typeof(tblSalesOrderDet_tblStocks_lastQty) != 'undefined' && tblSalesOrderDet_tblStocks_lastQty.length >index){
 tblSalesOrderDet_tblStocks_lastQty[index].value = tblSalesOrderDet_tblStocks_lastQty[index-1].value;
 }
 var tblSalesOrderDet_ViewVirtualStock_UseQty = n('tblSalesOrderDet_ViewVirtualStock_UseQty');
 if(typeof(tblSalesOrderDet_ViewVirtualStock_UseQty) != 'undefined' && tblSalesOrderDet_ViewVirtualStock_UseQty.length >index){
 tblSalesOrderDet_ViewVirtualStock_UseQty[index].value = tblSalesOrderDet_ViewVirtualStock_UseQty[index-1].value;
 }
 }
  copyFields=n('tblSalesOrderDet_SendDate');
 if((order > 29 || (typeof(copyAll)!="undefined" && "copyAll"==copyAll)) && typeof(copyFields[index])!="undefined" && (copyisNull(copyFields[index].value))){
 copyFields[index].value = copyFields[index-1].value;
 }
  }
 }

function copyisNull(str){
 if(str == ""){ 
 return true;
 }else{
 var pattern = new RegExp("^[0-9\.]*$");
 ret = str.search(pattern);
 if(ret > -1){
 if(str==0){
 return true;
 }
 }
 }
 return false;
}

function copyFields(trigType,order,copyAll,obj){

var index = curGridRowNum.curLine ;

if(trigType=="dbClick" && index>0){
 var copyFields;
 if(typeof(curGridRowNum.curKey)!="undefined" && curGridRowNum.curKey=="tblSalesOrderDet"){
 
 copyFields=n('tblSalesOrderDet_StockCode'); 
 if(copyFields[index].value !=0 && copyFields[index].value != ""){return}
 if( (order > 0 || (typeof(copyAll)!="undefined" && "copyAll"==copyAll)) && (copyisNull(copyFields[index].value))){
 copyFields[index].value = copyFields[index-1].value;
 }
 copyFields=n('tblSalesOrderDet_GoodsCode'); 
 if( (order > 1 || (typeof(copyAll)!="undefined" && "copyAll"==copyAll)) && (copyisNull(copyFields[index].value))){
 copyFields[index].value = copyFields[index-1].value;
 var tblSalesOrderDet_tblGoods_GoodsNumber = n('tblSalesOrderDet_tblGoods_GoodsNumber'); 
 if(typeof(tblSalesOrderDet_tblGoods_GoodsNumber) != 'undefined' && tblSalesOrderDet_tblGoods_GoodsNumber.length >index){
 tblSalesOrderDet_tblGoods_GoodsNumber[index].value = tblSalesOrderDet_tblGoods_GoodsNumber[index-1].value;
 }
 var tblSalesOrderDet_tblGoods_GoodsFullName = n('tblSalesOrderDet_tblGoods_GoodsFullName'); 
 if(typeof(tblSalesOrderDet_tblGoods_GoodsFullName) != 'undefined' && tblSalesOrderDet_tblGoods_GoodsFullName.length >index){
 tblSalesOrderDet_tblGoods_GoodsFullName[index].value = tblSalesOrderDet_tblGoods_GoodsFullName[index-1].value;
 }
 var tblSalesOrderDet_tblGoods_GoodsSpec = n('tblSalesOrderDet_tblGoods_GoodsSpec'); 
 if(typeof(tblSalesOrderDet_tblGoods_GoodsSpec) != 'undefined' && tblSalesOrderDet_tblGoods_GoodsSpec.length >index){
 tblSalesOrderDet_tblGoods_GoodsSpec[index].value = tblSalesOrderDet_tblGoods_GoodsSpec[index-1].value;
 }
 var tblSalesOrderDet_tblGoods_BaseUnit = n('tblSalesOrderDet_tblGoods_BaseUnit'); 
 if(typeof(tblSalesOrderDet_tblGoods_BaseUnit) != 'undefined' && tblSalesOrderDet_tblGoods_BaseUnit.length >index){
 tblSalesOrderDet_tblGoods_BaseUnit[index].value = tblSalesOrderDet_tblGoods_BaseUnit[index-1].value;
 }
 var tblSalesOrderDet_tblStocks_lastQty = n('tblSalesOrderDet_tblStocks_lastQty'); 
 if(typeof(tblSalesOrderDet_tblStocks_lastQty) != 'undefined' && tblSalesOrderDet_tblStocks_lastQty.length >index){
 tblSalesOrderDet_tblStocks_lastQty[index].value = tblSalesOrderDet_tblStocks_lastQty[index-1].value;
 }
 var tblSalesOrderDet_ViewVirtualStock_UseQty = n('tblSalesOrderDet_ViewVirtualStock_UseQty'); 
 if(typeof(tblSalesOrderDet_ViewVirtualStock_UseQty) != 'undefined' && tblSalesOrderDet_ViewVirtualStock_UseQty.length >index){
 tblSalesOrderDet_ViewVirtualStock_UseQty[index].value = tblSalesOrderDet_ViewVirtualStock_UseQty[index-1].value;
 }
 }
  copyFields=n('tblSalesOrderDet_SendDate'); 
 if( (order > 29 || (typeof(copyAll)!="undefined" && "copyAll"==copyAll)) && (copyisNull(copyFields[index].value))){
 copyFields[index].value = copyFields[index-1].value;
 }
  }
 }
}

 var tblName=new Array(1);
 
var goodsCodeLogicValidate =new Array();
 tblName[0]="tblSalesOrderDet"; 
 



function setSeqQtyReadOnly(tableN,row,checked){
 if($('input[name='+tableN+'_Seq_hid]',document).size()==0){ 
 
 return;
 } 
}


var __statKey =0;
function execStat(){
 __statKey ++;
 setTimeout(function(){
 if(__statKey==1){ 
 execStat2(); 
 } __statKey -= 1;},200);
}
function execStatOne(fn,digit){
 var statmi = n(fn); 
 var totalstat = 0;
 for(i=0;i<statmi.length;i++){
 if(isFloat(statmi[i].value)){
 totalstat = totalstat + Number(statmi[i].value);
 }
 }
 var mistat = m(fn+'_total'); 
 if(mistat!=undefined){
 mistat.innerHTML = f(totalstat,digit);
 }
}

function execStat2(){
 execStatOne('tblSalesOrderDet_Qty',2);
 execStatOne('tblSalesOrderDet_Amount',2);
 
}


var urlstr;
var obj ;
var field;
function openSelect(urlstr,obj,field){
 urlstr +="&isMain=true";
 width =800;
 height=470;
 if(width > document.documentElement.clientWidth -50){
 width = document.documentElement.clientWidth -50;
 }
 if(height > document.documentElement.clientHeight -50){
 height = document.documentElement.clientHeight -50;
 }

 window.urlstr = urlstr;
 window.obj = obj;
 window.field = field;
 
 
 
 if(urlstr.indexOf("&url=@URL:")==-1){
 urlstr = encodeURI(urlstr) ;
 }
 
 urlstr += "&url=@URL:"; 
 if(urlstr.indexOf("+")!=-1){
 urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 if(urlstr.indexOf("&amp;")!=-1){
 urlstr=urlstr.replaceAll("&amp;","@join:amp;") ;
 }
 urlstr = urlstr.replaceAll("#","%23") ;
 urlstr += "&src=menu&MOID="+MOID+"&MOOP=add&popupWin=MainPopup";
 asyncbox.open({id:'MainPopup',title:'',url:urlstr,width:width,height:height});
 
 $("#MainPopup_content").height($("#MainPopup").height()-25); 
}

var thisField;
var current;


function nz(fieldName,row){
 var v = n(fieldName)[row].value;
 return v.replace('&','&amp;');
}

function openChildSelect(urlstr,obj,field,thisField,current){
 
 
 
 urlstr +="&isMain=false";
 window.obj = obj;
 window.urlstr = urlstr;
 window.field = field; 
 window.thisField = thisField;
 window.current = current;
 
 obj.t = "t"; 
 var id = 0; 
 var mi = n(thisField); 
 for(i=0;i<mi.length;i++){
 if(mi[i].t == "t"){
 id = i;
 obj.t = ""; 
 } 
 }
 urlstr=urlstr+'&src=menu&MOID='+MOID+'&MOOP=add'; 
 
 urlstr=encodeURI(urlstr);
 
 var flag=false;
 if("tblSalesOutStockDet_SQty1"==field || "tblAllotDet_Qty1"==field || "tblAllotChangeDet_Qty1"==field || "tblBuyOutStockDet_Qty1"==field || "tblOtherOutDet_Qty1"==field || "tblAdjustPriceDet_Qty1"==field || "tblProDrawManysDet_Qty1"==field || "tblProCheckManysDet_Qty1"==field || "tblClearBackMaterialDet_Qty1"==field || "tblInProductBPDet_Qty1"==field || "tblOutMaterialsBPDet_Qty1"==field || "tblAddMaterialsDet_Qty1"==field || "tblEntrustGoodsDet_Qty1"==field || "tblRetCheckDet_Qty1"==field){
 urlstr += "&popType=sqty" ;
 }
 urlstr += "&url=@URL:";
 if(urlstr.indexOf("#")!=-1){
 urlstr=urlstr.replaceAll("#","%23") ;
 }
 if(urlstr.indexOf("+")!=-1){
 urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 urlstr += "&popupWin=ChildPopup";
 
 if(urlstr.indexOf("&amp;")!=-1){
 urlstr=urlstr.replaceAll("&amp;","@join:amp;") ;
 }
 asyncbox.open({id:'ChildPopup',title:'弹出窗口',url:urlstr,width:1000,height:490}); 
 
 
}

function deleteupload(fileName,tempFile,tableName,type){
 
 if(!confirm('确定删除吗')){
 return;
 }
 if("true" == tempFile){
 var str="/UploadDelAction.do?NOTTOKEN=NOTTOKEN&operation=3&fileName="+encodeURIComponent(fileName)+"&tempFile="+tempFile+"&tableName="+tableName+"&type="+type;
 AjaxRequest(str);
 var value = response; 
 if(value=="no response text" && value.length==0){
 return;
 } 
 } 
 var li=m('uploadfile_'+fileName);
 if("false" == tempFile && type == "AFFIX"){
 li.outerHTML = "<input type=hidden name=uploadDelAffix value='"+fileName+"'>";
 }else if("false" == tempFile && type == "PIC"){
 li.outerHTML = "<input type=hidden name=uploadDelPic value='"+fileName+"'>";
 }else{ 
 li.outerHTML = "";
 }
}
var curUploadType = "";
function openAttachUpload(type,btnId){
 curUploadType = type;
 var filter = "";
 if(type == "PIC"){
 filter = "Image";
 }
 
 var attachUpload = document.getElementById("attachUpload");
 if(attachUpload == null){
 uploadDiv = document.createElement("div"); 
 uploadDiv.id = "attachUpload" ;
 uploadDiv.style.cssText = "position:absolute;top:10px;left:30px;width:600px;height:300px;display:block;z-index:100";
 document.body.appendChild(uploadDiv);
 attachUpload = document.getElementById("attachUpload");
 }
 var clientHeight = document.documentElement.clientHeight;
 if(clientHeight==0) {
 clientHeight = document.body.clientHeight ;
 }
 attachUpload.style.left= ((document.body.clientWidth - 500) /2) +"px";
 attachUpload.style.top= ((clientHeight - 250) /2) +"px";
 attachUpload.style.display="block";
 attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
 ' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="maxSize=0&uploadUrl=/UploadServlet;jsessionid='+aioSessionId+'?path=/temp/@amp;fieldName='+btnId+'@amp;fileType='+type+'&filter='+filter+'&btnId='+btnId+'" />'+
 ' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7" width="500" height="250" name="column" align="middle" play="true" loop="false"'+
 ' flashvars="maxSize=0&uploadUrl=/UploadServlet;jsessionid='+aioSessionId+'?path=/temp/@amp;fieldName='+btnId+'@amp;fileType='+type+'&filter='+filter+'&btnId='+btnId+'" quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
} 
function onCompleteUpload(retstr,btnId){ 
 retstr = decodeURIComponent(retstr); 
 if(curUploadType == 'PIC'){
 var buttonId = "picuploadul";
 var hiddenName = "uploadpic";
 if(btnId!="undefined" && btnId!=null){
 buttonId = "picuploadul_"+btnId;
 hiddenName = btnId;
 }
 mstrs = retstr.split(";");
 var ul=$('#'+buttonId);
 for(i=0;i<mstrs.length;i++){
 str = mstrs[i];
 if(str == "") continue;
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div><a href='javascript:void(0);' style=\"cursor:pointer;\" onclick='deleteupload(\""+str+"\",\"true\",\"tblSalesOrder\",\"PIC\");'>删除</a><em>"+str+"</em></div><div class='hImage'><a href=\"/ReadFile?fileName="+str+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\" height=\"120\" border=\"0\"></a></div></li>";
 ul.append(imgstr);
 }
 }else if(curUploadType == 'AFFIX'){
 var buttonId = "affixuploadul";
 var hiddenName = "uploadaffix";
 if(btnId!="undefined" && btnId!=null){
 buttonId = "affixuploadul_"+btnId;
 hiddenName = btnId;
 }
 mstrs = retstr.split(";");
 for(i=0;i<mstrs.length;i++){
 str = mstrs[i];
 if(str == "") continue;
 var ul=$('#'+buttonId);
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div class='showAffix'>"+str+"</div><a class='showAffixDel' href='javascript:deleteupload(\""+str+"\",\"true\",\"tblSalesOrder\",\"AFFIX\");'>删除</a><a class='showAffixDown' href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=AFFIX\" target=_blank>下载</a></li>";
 ul.append(imgstr);
 }
 } 
 var attachUpload = document.getElementById("attachUpload");
 attachUpload.style.display="none";
}
var selectPicUl;
var selectPicHiddenName;
function selectPIC(btnId){
 var buttonId = "picuploadul";
 var hiddenName = "uploadpic";
 if(btnId!="undefined" && btnId!=null){
 buttonId = "picuploadul_"+btnId;
 hiddenName = btnId;
 }
 var ul=$('#'+buttonId);
 selectPicUl = ul;
 selectPicHiddenName = hiddenName;
 turl="/UserFunctionAction.do?&operation=401";
 
 width=document.documentElement.clientWidth-100;
 height=document.documentElement.clientHeight-100;
 
 
 asyncbox.open({id:'popSelectPic',url:turl,title:'选择图片',width:width,height:height,btnsbar:jQuery.btn.OKCANCEL.concat([{
 text : '上传图片',
 action : 'batchupload' 
 }]), 
 　　　 callback:function(action,opener){
 　　　　　 if(action == 'ok'){
 opener.$("input[name=files]:checked",opener.document).each(function(){
 var str = $(this).val();
 var rootp = opener.rootPath;
 if(rootp.substring(rootp.length-1)!="/"){
 rootp = rootp + "/";
 }
 str = "PICPATH:"+ str.substring(rootp.length); 
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'>"+
 "<div><a style=\"cursor:pointer;\" href='javascript:void(0);' onclick='deleteupload(\""+str+"\",\"false\",\""+$("#tableName").val()+"\",\"PIC\");'>删除</a><em>"+str.substring(8)+"</em></div><div><a href=\"/ReadFile?type=PIC&fileName="+encodeURI(str)+"\" target=\"_blank\"><img src=\"/ReadFile?type=PIC&fileName="+encodeURI(str)+"\" height=\"120\" border=\"0\"></a></div></li>";
 ul.append(imgstr);
 }); 
 　　　　　 }else if(action == 'batchupload'){
 　　　　　 opener.openAttachUpload();
 　　　　　 return false;
 　　　　　 }
 　　　 }
 　 }); 
}
function linkPIC(btnId){
 var buttonId = "picuploadul";
 var hiddenName = "uploadpic";
 if(btnId!="undefined" && btnId!=null){
 buttonId = "picuploadul_"+btnId;
 hiddenName = btnId;
 }
 var ul=$('#'+buttonId);
 
 asyncbox.prompt('请输入图片链接','','http://','text',function(action,val){
 　　　if(action == 'ok'){
 　　　　　str = val;
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div><a style=\"cursor:pointer;\" href='javascript:void(0);' onclick='deleteupload(\""+str+"\",\"false\",\"tblSalesOrder\",\"PIC\");'>删除</a><em>"+str+"</em></div><div><a href=\""+str+"\" target=\"_blank\"><img src=\""+str+"\" height=\"120\" border=\"0\"></a></div></li>";
 ul.append(imgstr);
 　　　}
 }); 
}

function upload(type,btnId){
 openAttachUpload(type,btnId);
 return; 
}
function openInputDate(obj)
{
 WdatePicker();
}
function openInputTime(obj)
{
 WdatePicker({lang:'zh_CN',dateFmt:'yyyy-MM-dd HH:mm:ss'});
}

function delFireEventOne(rowId,tableName,fn,ctn){
 var z=n(fn); 
 if(z.length>0 && tableName==ctn+"Table"){
 $(z[rowId]).change();
 }
}

function delFireEvent(rowId,tableName)
{
 delFireEventOne(rowId,tableName,'tblSalesOrderDet_Qty',"tblSalesOrderDet"); 
 delFireEventOne(rowId,tableName,'tblSalesOrderDet_Price',"tblSalesOrderDet"); 
 delFireEventOne(rowId,tableName,'tblSalesOrderDet_Amount',"tblSalesOrderDet"); 
 }

var fieldDigit = new Array();
 fieldDigit[fieldDigit.length] = {name:'AccountAmount',dis:'预收订金',digit:2,isMain:true};
 fieldDigit[fieldDigit.length] = {name:'TotalAmount',dis:'单据总额',digit:2,isMain:true};
 fieldDigit[fieldDigit.length] = {name:'tblSalesOrderDet_Qty',dis:'数量',digit:2,isMain:false};
 fieldDigit[fieldDigit.length] = {name:'tblSalesOrderDet_Price',dis:'单价',digit:4,isMain:false};
 fieldDigit[fieldDigit.length] = {name:'tblSalesOrderDet_Amount',dis:'金额',digit:2,isMain:false};
 
function beforSubmit(form){ 
 if($('div[dropName=myDropName]').size()>0&&$('div[dropName=myDropName]').is(':visible')){
 
 return;
 }
 for(i=0;i<fieldDigit.length;i++){
 if(!validateDigit(fieldDigit[i].name,fieldDigit[i].dis,fieldDigit[i].digit,fieldDigit[i].isMain))return false; 
 }

 if(typeof(flowNodeid)!="undefined" && flowNodeid!=""){
 var checkPerson = document.getElementById("auditingCheckPersonIds"+flowNodeid).value ;
 if(checkPerson.length==0){
 alert(flowNodedisplay+"的审核人不能为空.") ;
 return false ;
 }
 } 
 
 submitBefore = false ;
 if(!validate(form))return false; 
 disableForm(form);
 if(existFlow=="exist" && detailFlow !="detailFlow"&&form.button.value!='deliverTo'){
 if("false"==form.OAWorkFlow.value){
 var strReturn = window.showModalDialog("/OAFileWorkFlowAction.do?operation=94&type=add&tableName=tblSalesOrder",
 winObj,"dialogWidth:600px;dialogHeight:350px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(strReturn)=="undefined")return false ;
 var strFile = strReturn.split("||") ;
 m("varWakeUp").value=strFile[0] ;
 m("varNextNode").value=strFile[1] ;
 m("varCheckPersons").value=strFile[2] ;
 m("varAttitude").value=strFile[3] ;
 }
 }
 if(typeof(fromCRM)!="undefined" && fromCRM=="service"){
 var strContent = m("content") ;
 if(strContent.value.length==0){
 alert("分配内容不能为空");
 strContent.focus() ;
 return false ;
 }
 }
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 if(typeof(operation) != "undefined"){
 form.operation.value= operation;
 }
 return true;
}
var varTableName;
function quoteDraft(tableName,moduleType){
 window.varTableName = tableName ;
 var urlstr = "/UserFunctionQueryAction.do?tableName="+tableName+"&src=menu&moduleType="+moduleType+"&parentTableName=&draftQuery=draftPop&popupWin=QuoteDraft";
 asyncbox.open({id:'QuoteDraft',title:'引用草稿',url:urlstr,width:870,height:470});
}

function exeQuoteDraft(varStr,moduleType){
 if(typeof(varStr)=="undefined")return ;
 window.location.href="/UserFunctionAction.do?tableName="+varTableName+"&operation=7&moduleType="+moduleType+"&parentTableName=&f_brother=&keyId="+varStr+"&saveDraft=quoteDraft&winCurIndex="+winCurIndex+"&popWinName="+$("#popWinName").val() ; 
}

function subAdd(){
 form.button.value="saveAdd"; 
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ;
}
function saveDraft2(){
 operation=1;
 form.button.value="saveDraft"; 
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ;
}
function updateDraft2(){
 operation=2;
 form.button.value="saveDraft"; 
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ;
}

function printSave(){
 form.button.value="printSave";
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ; 
 
}
function printData(billId,reportNumber){ 
 if(printRight){
 if(confirm("是否打印单据?")){
 var strUrl = "/UserFunctionQueryAction.do?tableName=tblSalesOrder&reportNumber="+reportNumber+"&moduleType="+moduleType+"&operation=18&BillID="+billId+"&parentTableName=&printType=savePrint&winCurIndex="+winCurIndex ;
 asyncbox.open({id:'printPopup',title:'科荣控件',url:strUrl,width:300,height:280,
 callback : function(action){
 if(action=="close"){
 location.href='/common/message.jsp';
 }
 }});
 enableForm(form);
 return ;
 }else{
 location.href='/common/message.jsp';
 }
 }
}
var reportNumber2;
var billId2;
function prints(billId,reportNumber){
 reportNumber2=reportNumber;
 billId2=billId
 var strUrl = "/UserFunctionQueryAction.do?tableName=tblSalesOrder&reportNumber="+reportNumber+"&moduleType="+moduleType+"&operation=18&BillID="+billId+"&parentTableName=&winCurIndex="+winCurIndex;
 asyncbox.open({id:'printPopup',title:'科荣控件',url:strUrl,width:300,height:280});
 enableForm(form);
}

function down(){
 if(event.ctrlKey&&event.keyCode==83){
 $(document.activeElement).trigger("blur") ;
 if(jQuery("[defbtn=\"save\"]").length>0)
 jQuery("[defbtn=\"save\"]").click();
 else
 jQuery("a[name='save']").click();
 
 stopBubble();
 stopDefault();
 }else if(event.ctrlKey&&event.keyCode==68){
 $(document.activeElement).trigger("blur") ;
 
 jQuery("[defbtn=\"saveAdd\"]").click();
 stopBubble();
 stopDefault();
 }else if(event.ctrlKey&&event.keyCode==90){
 $(document.activeElement).trigger("blur") ;
 jQuery("[defbtn=\"backList\"]").click();
 stopBubble();
 stopDefault();
 }else if(event.ctrlKey&&event.keyCode==187){
 var div=null;
 var ele = document.activeElement;
 var rowId = 0;
 if(ele!=undefined)
 {
 var tmpArray = jQuery("[name='"+ele.name+"']");
 if(tmpArray.length>1)
 rowId = jQuery.inArray(ele,tmpArray)+2;
 }
 
 if(curGridRowNum.curKey!='')
 {
 div = jQuery(m(curGridRowNum.curKey+"TableDIV"));
 }else
 div = jQuery("#listRange_tableInfo div:eq(0)");
 if(div.length>0 && div[0].fix!=undefined)
 {
 if(rowId==0)
 div[0].fix.addRow();
 else
 div[0].fix.addRow2(rowId);
 }
 stopBubble();
 stopDefault();
 }else if(event.ctrlKey&&event.keyCode==189){ 
 var rowId = 0;
 var ele = document.activeElement;
 var eleName = undefined;
 if(ele!=undefined)
 {
 var tmpArray = jQuery("[name='"+ele.name+"']");
 if(tmpArray.length>1)
 {
 rowId = jQuery.inArray(ele,tmpArray)+1;
 eleName = ele.name;
 }
 }
 var div=null;
 if(curGridRowNum.curKey!='')
 {
 div = jQuery(m(curGridRowNum.curKey+"TableDIV"));
 }else
 div = jQuery("#listRange_tableInfo div:eq(0)");
 if(div.length>0 && div[0].fix!=undefined)
 {
 if(rowId==0)
 {
 div[0].fix.deleteRow(div[0].fix.dataTable.find("tbody tr").length-1);
 if(eleName != undefined)
 jQuery("[name='"+eleName+"']:eq(0)").focus();
 }
 else
 {
 div[0].fix.deleteRow(rowId);
 if(eleName != undefined)
 {
 jQuery("[name='"+eleName+"']:eq("+(rowId-1)+")").focus();
 }
 }
 }
 stopBubble();
 stopDefault();
 }else if(event.ctrlKey&&event.keyCode==77){
 var v = jQuery(".tagSel[name='detTitle']").next();
 if(v.length>0 && v.attr("name")=="detTitle")
 {
 v.click();
 }
 else
 jQuery("[name='detTitle']:eq(0)").click();
 stopDefault(event);
 stopBubble(event);
 }else if(event.ctrlKey&&event.keyCode==82)
 {
 jQuery("[name='Remark']:eq(0)").focus();
 stopDefault(event);
 stopBubble(event);
 }else if(event.ctrlKey&&event.keyCode==81)
 {
 jQuery("span:contains('关闭')").click();
 stopDefault(event);
 stopBubble(event);
 }else if(event.altKey&&event.keyCode==83)
 {
 jQuery("a:contains('存为草稿')").click();
 stopDefault(event);
 stopBubble(event);
 }
}


function mainSelect2(urlstr,openUrl,obj,field,isChild)
{ 
 if(event.ctrlKey&&event.keyCode==67){
 return; 
 }

 var dropdown=obj.dropdown;
 
 var v = [37,38,39,40,13,16,17,18];
 if(dropdown==undefined && jQuery.inArray(event.keyCode,v)==-1)
 {
 var tmpUrl = urlstr.substring(urlstr.indexOf("?")+1);
 var temp_par = tmpUrl.split("&");
 var data = {
 operation:"DropdownPopup",
 MOID:MOID,
 MOOP:"add",
 selectField:obj.name,
 selectValue:obj.value
 };
 
 for(var i = 0; i<temp_par.length ;i++)
 {
 var temp_item =temp_par[i].split("=");
 jQuery(data).attr(temp_item[0],temp_item[1]);
 }
 
 dropdown = new dropDownSelect("t_"+obj.id+new Date().getTime(),data,obj);
 if(isChild == undefined || isChild == false)
 {
 dropdown.retFun2 = k_fillMainField;
 }else
 dropdown.retFun2 = k_fillChildField;
 dropdown.showData();
 return ;
 }
 
 if(event.keyCode == 38)
 {
 if(dropdown!=undefined)
 {
 dropdown.selectUp();
 stopDefault(event);
 stopBubble(event);
 }
 return ;
 }else if(event.keyCode==33)
 {
 if(dropdown!=undefined)
 {
 dropdown.pageDown();
 stopDefault(event);
 stopBubble(event);
 }
 return ;
 }else if(event.keyCode==34)
 {
 if(dropdown!=undefined)
 {
 dropdown.pageDown();
 stopDefault(event);
 stopBubble(event);
 }
 return ;
 }else if(event.keyCode==40)
 {
 if(dropdown!=undefined)
 {
 dropdown.selectDown();
 stopDefault(event);
 stopBubble(event);
 }
 return ;
 }else if(event.keyCode==13)
 {
 if(dropdown!=undefined)
 {
 dropdown.curValue();
 stopDefault(event);
 stopBubble(event);
 }
 return ;
 }else if(event.keyCode==27)
 {
 if(dropdown!=undefined)
 {
 dropdown.close();
 stopDefault(event);
 stopBubble(event);
 }
 return ;
 }
 if(dropdown!=undefined)
 dropdown.showData();
}

function k_fillMainField(a,b)
{
 dataBackIn("true",a,b,window);
}

function k_fillChildField(a,b)
{ 
 dataBackIn("false",a,b,window);
}

function dyGetPYM(url,obj,fileName){
 var tempUrl = url + "&chinese="+encodeURIComponent(obj.value);
 AjaxRequest(tempUrl);
 var pym = m(fileName+"PYM");
 pym.value = response;
}


function isCheckField(obj,fieldName){
 var name = m("tbl_"+obj.name).value; 
 if(obj.value != name){
 resetSubmit(fieldName); 
 }
}

function resetSubmit(fieldName){
 if(fieldName == "CompanyCode"){
 var saveField=n("CompanyCode"); 
 saveField[0].value = "";
 var obj = m("tbl_tblCompany_classCode");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("Discount"); 
 saveField[0].value = "";
 var obj = m("tbl_case when tblCompany_Discount>0 then tblCompany.Discount else 100 end");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("DepartmentCode"); 
 saveField[0].value = "";
 var obj = m("tbl_case when len(tblCompany_DepartmentCode)>0 then tblCompany.DepartmentCode else '@Sess:DepartmentCode' end");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("tblDepartment_DeptFullName"); 
 saveField[0].value = "";
 var obj = m("tbl_case when len(tblCompany_DepartmentCode)>0 then tblDepartment.DeptFullName else '@Sess:DepartmentName' end");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("EmployeeID"); 
 saveField[0].value = "";
 var obj = m("tbl_case when len(tblCompany_EmployeeID)>0 then tblCompany.EmployeeID else '@Sess:UserId' end");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("tblEmployee_EmpFullName"); 
 saveField[0].value = "";
 var obj = m("tbl_case when len(tblCompany_EmployeeID)>0 then tblEmployee.EmpFullName else '@Sess:UserName' end");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("ReceiveDate"); 
 saveField[0].value = "";
 var obj = m("tbl_CONVERT(varchar(10),dateadd(Day,case tblCompany_SettleCys when 0 then null else tblCompany.SettleCys end,'@ValueofDB:BillDate'),121)");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("Tax"); 
 saveField[0].value = "";
 var obj = m("tbl_case tblCompany_InVoiceType when 1 then '@MEM:Commoninvoice' when 2 then '@MEM:VATinvoice' else '0' end");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("tblCompany_ComFullName");
 saveField[0].value = ""; 
 m("tbl_tblCompany_ComFullName").value = "" ;
 }
 if(fieldName == "EmployeeID"){
 var saveField=n("EmployeeID"); 
 saveField[0].value = "";
 var obj = m("tbl_tblEmployee_id");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("DepartmentCode"); 
 saveField[0].value = "";
 var obj = m("tbl_tblEmployee_DepartmentCode");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("tblEmployee_EmpFullName");
 saveField[0].value = ""; 
 m("tbl_tblEmployee_EmpFullName").value = "" ;
 var saveField=n("tblDepartment_DeptFullName");
 saveField[0].value = ""; 
 m("tbl_tblDepartment_DeptFullName").value = "" ;
 }
 if(fieldName == "Account"){
 var saveField=n("Account"); 
 saveField[0].value = "";
 var obj = m("tbl_tblAccTypeInfo_AccNumber");
 if(obj != null){
 obj.value = "";
 }
 var saveField=n("tblAccTypeInfo_AccName");
 saveField[0].value = ""; 
 m("tbl_tblAccTypeInfo_AccName").value = "" ;
 }
   }
function moreLanguage(len,moreLanguagefield){
 moreLanguagefield.t = "t"; 
 var id = 0; 
 var mi = n(moreLanguagefield.name); 
 for(i=0;i<mi.length;i++){
 if(mi[i].t == "t"){
 id = i;
 moreLanguagefield.t = ""; 
 } 
 }
 var flan=moreLanguagefield.name.substr(0,moreLanguagefield.name.lastIndexOf("_"));
 var cf=n(flan);
 var url = "/common/moreLanguage.jsp?len="+len+"&str="+encodeURI(cf[id].value) ;
 url = url.replaceAll("#","%23") ;
 var str = window.showModalDialog(url,winObj,"dialogWidth=730px;dialogHeight=450px");
 if(typeof(str)=="undefined") return ;
 var strs=str.split(";");
 for(var i=0;i<strs.length;i++){
 var lanstr=strs[i].split(":");
 if(lanstr[0]=='zh_CN'){
 moreLanguagefield.value=lanstr[1];break;
 }
 }
 cf[id].value = str;
}

var moreLanguagefield;
function mainMoreLanguage(len,moreLanguagefield){
 window.moreLanguagefield = moreLanguagefield;
 var mlf = n(moreLanguagefield);
 var url = "/common/moreLanguage.jsp?popupWin=Language&len="+len+"&str="+encodeURI(mlf[0].value) ;
 url = url.replaceAll("#","%23") ;
 asyncbox.open({id:'Language',title:'多语言',url:url,width:530,height:300});
}

function fillLanguage(str){
 if(typeof(str)=="undefined"){return };
 var mlf = n(moreLanguagefield);
 mlf[0].value = str;
 var mf=n(moreLanguagefield+"_lan");
 if(str.length==0){
 mf[0].value= "" ;
 }else{
 var strs=str.split(";");
 for(var i=0;i<strs.length;i++){
 var lanstr=strs[i].split(":");
 if(lanstr[0]=='zh_CN'){
 mf[0].value=lanstr[1];break;
 }
 }
 }
}

function initSeqQtySet(){
 if(false){
 }
       }

function delOpation(fileName,popedomId){
 var formatName=m(fileName);
 if(formatName.selectedIndex==-1)
 {
 alert("请选择要移除的项.");
 }
 if(formatName.selectedIndex<0)return false ;
 var value = formatName.options[formatName.selectedIndex].value;
 var appcers = m(popedomId).value;
 appcers = appcers.replace(value+",","");
 m(popedomId).value =appcers
 formatName.options.remove(formatName.selectedIndex);
}

function showUserGroup(fieldName,fieldNameIds){
 var displayName=encodeURI("职员分组") ;
 var urlstr = '/UserFunctionAction.do?operation=22&src=menu&tableName=tblEmpGroup&selectName=SelectEmpGroup' ;
 var str = window.showModalDialog(urlstr+"&MOID="+MOID+"&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var employees = str.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 var existOption = m(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m(fieldName).options.add(new Option(field[1],field[0]));
 m(fieldNameIds).value+=field[0]+",";
 }
 }
 }
}

function showEmployee(fieldName,fieldNameIds){
 var displayName=encodeURI("审核人") ;
 var urlstr = '/UserFunctionAction.do?operation=22&selectName=SelectSMSEmployee' ;
 var str = window.showModalDialog(urlstr+"&MOID="+MOID+"&MOOP=query&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=830px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var employees = str.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 var existOption = m(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m(fieldName).options.add(new Option(field[1],field[0]));
 m(fieldNameIds).value+=field[0]+",";
 }
 }
 }
}

function changeMoreLanguage(strName,strValue){
 var localeValue = m(strName).value ;
 var lanValue = m(strValue).value ;
 var locale = "zh_CN" ;
 var moreValue = "" ;
 if(lanValue.indexOf(":"+localeValue+";")==-1){
 if(lanValue.indexOf(locale)!=-1){
 moreValue = lanValue.substring(0,lanValue.indexOf(locale))+lanValue.substring(lanValue.indexOf(";",lanValue.indexOf(locale))+1) ;
 }
 moreValue = moreValue+locale+":"+localeValue+";";
 m(strValue).value=moreValue ;
 }
}

function mdiwin(url,title){
 top.showreModule(title,url);
}

function openCRMSelect(selectName,displayName){
 displayName=encodeURI(displayName) ;
 var str = window.showModalDialog("/UserFunctionAction.do?selectName="+selectName+"&operation=22&MOID="+MOID+"&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
 if(typeof(str)!="undefined"){
 var mutli=str.split(";"); 
 m("taskPersonName").value = mutli[3] ;
 m("taskPerson").value = mutli[0] ;
 }
}
function dyGetCalculate(url,obj){
 if(obj.value.match(/^\s*$/)){
 if(event.keyCode==13){event.keyCode=9;};
 }else{
 var fieldName = "" ;
 if(typeof(obj.name)!="undefined" && obj.name.indexOf("_")!=-1){
 fieldName = obj.name ;
 }else{
 fieldName = "tblSalesOrder"+"_"+obj.name ;
 }
 var tempUrl = url + "&fieldName="+fieldName+"&value="+encodeURIComponent(obj.value);
 AjaxRequest(tempUrl);
 if(response=="error"){
 alert("表达式输入有误!只能输入数字,+,-,*(乘法),/(除法)及()运算符，请重试!");
 }else{
 obj.value = response;
 $(obj).change();
 if(event.keyCode==13){event.keyCode=9;};
 }
 }
}


function sendBillMsg(action){
 var popupUserIds = m("popedomUserIds").value ;
 var popedomDeptIds = m("popedomDeptIds").value ;
 var otherEmail = m("otherEmail").value ;
 var otherSMS = m("otherSMS").value ;
 var strurl = "/vm/classFunction/sendBillMsg.jsp?tableName=tblSalesOrder&action="+action
 +"&userIds="+encodeURI(popupUserIds)+"&deptIds="+encodeURI(popedomDeptIds)+"&otherEmail="+otherEmail+"&otherSMS="+otherSMS;
 asyncbox.open({id:'billMgsId',url:strurl,title:'发送消息',width:800,height:400,btnsbar:jQuery.btn.OKCANCEL, 
　　　 callback:function(action,opener){
　　　　　 if(action == 'ok'){
　　　　　　　 var returnValue = opener.sendBillMsg();
 if(typeof(returnValue)!="undefined" && returnValue!="false"){
 var strValue = returnValue.split("@koron@") ;
 m("wakeUpMode").value = strValue[0] ;
 m("wakeUpContent").value = strValue[1] ;
 m("popedomUserIds").value = strValue[2] ;
 m("popedomDeptIds").value = strValue[3] ;
 
 
 m("otherEmail").value = strValue[4] ;
 m("otherSMS").value = strValue[5] ;
 }
 m("smsType").value = "sms" ;
　　　　　 }
　　　 }
　 });
}


function telCall()
{
 var str = window.showModalDialog("/TelAction.do?operator=callTel&from=tblSalesOrder&keyId="+valueId,"","dialogWidth=600px;dialogHeight=350px"); 
}

function checkKeyword(fieldName,definedId){
 document.getElementById("ButtonType").value = "keyWord" ;
 var fieldValue = document.getElementById(fieldName).value ;
 if(fieldValue.length==0){
 alert("简称不能为空") ;
 return ;
 }
 form.checkFieldName.value = fieldValue;
 form.defineName.value=definedId;
 form.operation.value=25;
 form.submit();
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

function nec(value){
 return $("#"+value).val().replace('&','&amp;'); ;
}
function nn(value,row){ 
 if(document.getElementsByName(value).length==0){ 
 return document.getElementsByName("conver"); 
 }
 var eob = document.getElementsByName(value) ;
 if(eob.length <=row){ 
 alert("公式错误，rn行号大于行数");
 }else{ 
 return Number(eob[row].value);
 }
}
function r(num,fieldName){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)
}
function rn(num,fieldName,row){
 var value= tblName[num]+"_"+fieldName;
 var eob = document.getElementsByName(value);
 if(eob.length <=row){
 alert("公式错误，rn行号大于行数"+value+":"+row);
 }else{
 return Number(eob[row].value);
 }
}
function v(num,fieldName,pos){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)[pos].value;
}
function o(num,fieldName,pos){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)[pos];
}

function sm(fieldName){
 var st=n(fieldName); 
 var retv=0;
 for(i = 0;i<st.length;i++){ 
 if(st[i].value.length>0 && isFloat(st[i].value) ){ 
 retv += parseFloat(st[i].value); 
 }
 }
 return retv;
}

function autoBalance(type){
 
 if(type=="RefReceive"){
 var CompanyCode = m('CompanyCode').value;
 var ExeBalAmt = m('ExeBalAmt').value;
 var AcceptTypeID = m('AcceptTypeID').value;
 if(CompanyCode.length==0){
 alert('请输入客户'); 
 return;
 }
 if(isNaN(ExeBalAmt)){
 alert('请输入收款金额且收款金额必须大于0');
 return;
 }
 if(ExeBalAmt<=0){
 alert('请输入收款金额且收款金额必须大于0');

 return;
 }
 var billNo = m("BillNo").value;
 var str="/RefPay?type=receive&CompanyCode="+CompanyCode+"&money="+ExeBalAmt+"&AcceptTypeID="+AcceptTypeID+"&billNo="+billNo;
 AjaxRequest(str);
 var value = response;
 
 if(value!="no response text" && value.length>0){
 for(var l=0;l<gridDatas.length;l++){
 var checkItem = gridDatas[l].cols[0];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 var fieldName = checkItem.name.substring(checkItem.name.indexOf("_")+1);
 if(table == "tblSaleReceiveDet"){
 var saveField2=n("tblSaleReceiveDet_RefbillNo");
 $(saveField2[items.length-1]).focus();
 $(saveField2[items.length-1]).click();
 var leng = items.length;
 for(var j =0;j<leng;j++){
 var saveField=n("tblSaleReceiveDet_RefbillNo");
 $(saveField[0]).focus();
 $(saveField[0]).click();
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=parseInt(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 $(tbody.rows[trIndex].cells[1].firstChild).focus();
 $(trObj.cells[1]).click(); 
 }
 }
 }
 
 var vs = value.split('|');
 var inde = 0;
 for(var i = 0;i<vs.length;i++){
 var v =vs[i];
 var vo = v.split(',');
 var saveField=n("tblSaleReceiveDet_RefbillNo"); saveField[i].value = vo[0]; 
 var saveField=n("tblSaleReceiveDet_BillAmt"); saveField[i].value = vo[1]; 
 var saveField=n("tblSaleReceiveDet_SettledAmt");saveField[i].value = vo[2]; 
 var saveField=n("tblSaleReceiveDet_BackAmt"); saveField[i].value = vo[3]; 
 var saveField=n("tblSaleReceiveDet_WexeBalAmt");saveField[i].value = vo[4]; 
 var saveField=n("tblSaleReceiveDet_RefbillID"); saveField[i].value = vo[5]; 
 var saveField=n("tblSaleReceiveDet_SalesOrderNo");saveField[i].value = vo[6]; 
 var saveField=n("tblSaleReceiveDet_ReceiveBillType");saveField[i].value = vo[7]; 
 var saveField=n("tblSaleReceiveDet_BillName"); saveField[i].value = vo[8]; 
 var saveField=n("tblSaleReceiveDet_SalesOrderID");saveField[i].value = vo[9]; 
 var saveField=n("tblSaleReceiveDet_ExeBalAmt"); saveField[i].value = vo[10];
 var saveField=n("tblSaleReceiveDet_Remark"); saveField[i].value = vo[11];
 $(saveField[i]).focus();
 $(saveField[i]).click();
 inde=i;
 }
 inde++;
 
 var saveField=n("tblSaleReceiveDet_ExeBalAmt");
 $(saveField[inde]).focus();
 $(saveField[inde]).click();
 
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=parseInt(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 tbody.rows[trIndex].cells[1].firstChild.focus();
 $(trObj.cells[1]).click(); 
 
 }
 }else if(type=="RefPay"){
 var ViewCompanyTotal_ComFullName = m('ViewCompanyTotal_ComFullName').value;
 var CompanyCode = m('CompanyCode').value;
 var SettleAmt = m('SettleAmt').value;
 var PaytypeID = m('PaytypeID').value;
 if(ViewCompanyTotal_ComFullName.length==0){
 alert('请输入客户');
 return;
 }
 if(isNaN(SettleAmt)){
 alert('请输入付款金额且付款金额必须大于0');
 return;
 }
 if(SettleAmt<=0){
 alert('请输入付款金额且付款金额必须大于0');
 return;
 }
 var billNo = m("BillNo").value;
 var str="/RefPay?type=pay&companyCode="+CompanyCode+"&ViewCompanyTotal_ComFullName="+ViewCompanyTotal_ComFullName+"&money="+SettleAmt+"&PaytypeID="+PaytypeID+"&billNo="+billNo;
 AjaxRequest(str);
 var value = response;
 
 if(value!="no response text" && value.length>0){
 for(var l=0;l<gridDatas.length;l++){
 var checkItem = gridDatas[l].cols[0];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 var fieldName = checkItem.name.substring(checkItem.name.indexOf("_")+1);
 if(table == "tblPaydet"){
 var saveField2=n("tblPayDet_RefbillNo");
 $(saveField2[items.length-1]).focus();
 $(saveField2[items.length-1]).click();
 var leng = items.length;
 for(var j =0;j<leng;j++){
 var saveField=n("tblPayDet_RefbillNo");
 $(saveField[0]).focus();
 $(saveField[0]).click();
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 $(tbody.rows[trIndex].cells[1].firstChild).focus();
 $(trObj.cells[1]).click(); 
 }
 }
 }
 
 var vs = value.split('|');
 var inde = 0;
 for(var i = 0;i<vs.length;i++){
 var v =vs[i];
 var vo = v.split(',');
 var saveField=n("tblPayDet_RefbillNo"); saveField[i].value = vo[0]; 
 var saveField=n("tblPayDet_BillAmt"); saveField[i].value = vo[1]; 
 var saveField=n("tblPayDet_SettledAmt");saveField[i].value = vo[2]; 
 var saveField=n("tblPayDet_BackAmt"); saveField[i].value = vo[3]; 
 var saveField=n("tblPayDet_WexeBalAmt");saveField[i].value = vo[4]; 
 var saveField=n("tblPayDet_RefbillID"); saveField[i].value = vo[5]; 
 var saveField=n("tblPayDet_BuyOrderNo");saveField[i].value = vo[6]; 
 var saveField=n("tblPayDet_PayBillType");saveField[i].value = vo[7]; 
 var saveField=n("tblPayDet_BillName"); saveField[i].value = vo[8]; 
 var saveField=n("tblPayDet_BuyOrderID");saveField[i].value = vo[9]; 
 var saveField=n("tblPaydet_ExeBalAmt"); saveField[i].value = vo[10];
 var saveField=n("tblPaydet_Remark"); saveField[i].value = vo[11];
 $(saveField[i]).focus();
 $(saveField[i]).click();
 inde=i;
 }
 inde++;
 var saveField=n("tblPaydet_ExeBalAmt");
 $(saveField[inde]).focus();
 $(saveField[inde]).click();
 
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=parseInt(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 tbody.rows[trIndex].cells[1].firstChild.focus();
 $(trObj.cells[1]).click(); 
 
 }
 }
}



function toStockDistributing(reportName){
 
 var goodsFullName = "";
 
 for(var l=0;l<gridDatas.length;l++){
 for(var i=0;i<gridDatas[l].cols.length;i++){
 var checkItem = gridDatas[l].cols[i];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 var fieldName = checkItem.name.substring(checkItem.name.indexOf("_")+1);
 if(fieldName.indexOf("_" != -1)){
 fieldName = fieldName.substring(fieldName.indexOf("_")+1);
 }
 if(fieldName=='GoodsFullName'){
 for(var j =0;j<items.length;j++){
 if(items[j].value.length > 0){
 if(goodsFullName!=""){
 goodsFullName = goodsFullName+",";
 }
 goodsFullName = goodsFullName + items[j].value;
 } 
 }
 }
 }
 }
 goodsFullName =encodeURI(goodsFullName).replaceAll("#","%23") ;
 window.open("/ReportDataAction.do?reportNumber="+reportName+"&src=menu&LinkType=@URL&selectType=endClass&query=true&GoodsFullName="+goodsFullName);
}



function cut(){
 var billTableName = m('billTableName').value;
 var str = "";
 if(billTableName == ""){
 alert('请先选择表');
 return;
 }else{
 str = window.showModalDialog("/common/fieldSelectSMS.jsp?tableName="+billTableName);
 }
 if(str == undefined || str == ""){
 return;
 }
 m('ModelContent').innerHTML = m('ModelContent').innerHTML + "["+str+"]";
}

function initMainCaculate(){
 var z=n('AccountAmount');
 for(i = 0;i<z.length;i++){ 
 z[i].onchange = function(){ 
 n('CurAccountAmount')[0].value=f(nn('AccountAmount',0)/nn('CurrencyRate',0),2);

 
 }
 } 

 var z=n('CurAccountAmount');
 for(i = 0;i<z.length;i++){ 
 z[i].onchange = function(){ 
 n('AccountAmount')[0].value=f(nn('CurAccountAmount',0)*nn('CurrencyRate',0),2);

 
 }
 } 

 var z=n('InVoiceType');
 for(i = 0;i<z.length;i++){ 
 z[i].onchange = function(){ 
 if(nn('InVoiceType',0)==2)n('Tax')[0].value=f("0",4);

if(nn('InVoiceType',0)==1)n('Tax')[0].value=f("0",4);

if(nn('InVoiceType',0)==3)n('Tax')[0].value=f(0,4);

for(var j=0;j<r(0,'TaxPrice').length;j++){
r(0,"TaxPrice")[j].value=f(((nn('Tax',0)/100)+1)*rn(0,"Discount",j)*rn(0,"Price",j)/100,4);
}

for(var j=0;j<r(0,'TaxAmount').length;j++){
r(0,"TaxAmount")[j].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",j)*rn(0,"Discount",j)/100,2);
}

for(var j=0;j<r(0,'CurPrice').length;j++){
r(0,"CurPrice")[j].value=f((((nn('Tax',0)/100)+1)*rn(0,"Discount",j)*rn(0,"Price",j)/100)/nn('CurrencyRate',0),4);
}

for(var j=0;j<r(0,'CurAmount').length;j++){
r(0,"CurAmount")[j].value=f((((nn('Tax',0)/100)+1)*rn(0,"Amount",j)*rn(0,"Discount",j)/100)/nn('CurrencyRate',0),2);
}

n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);

n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);

n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);

for(var j=0;j<r(0,'CoTaxAmt').length;j++){
r(0,"CoTaxAmt")[j].value=f(rn(0,"TaxAmount",j)-rn(0,"DisBackAmt",j),2);
}

n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);

for(var j=0;j<r(0,'DisAmount').length;j++){
r(0,"DisAmount")[j].value=f(rn(0,"Amount",j)-rn(0,"DisBackAmt",j),2);
}

n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 
 }
 } 

 var z=n('Discount');
 for(i = 0;i<z.length;i++){ 
 z[i].onchange = function(){ 
 for(var j=0;j<r(0,'Discount').length;j++){
r(0,"Discount")[j].value=f(nn('Discount',0),4);
}

for(var j=0;j<r(0,'DisPrice').length;j++){
r(0,"DisPrice")[j].value=f(rn(0,"Discount",j)*rn(0,"Price",j)/100,4);
}

for(var j=0;j<r(0,'DisBackAmt').length;j++){
r(0,"DisBackAmt")[j].value=f(rn(0,"Amount",j)*rn(0,"Discount",j)/100,2);
}

for(var j=0;j<r(0,'TaxPrice').length;j++){
r(0,"TaxPrice")[j].value=f(((nn('Tax',0)/100)+1)*rn(0,"Discount",j)*rn(0,"Price",j)/100,4);
}

for(var j=0;j<r(0,'TaxAmount').length;j++){
r(0,"TaxAmount")[j].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",j)*rn(0,"Discount",j)/100,2);
}

n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);

n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);

n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);

for(var j=0;j<r(0,'CoTaxAmt').length;j++){
r(0,"CoTaxAmt")[j].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",j)*rn(0,"Discount",j)/100-rn(0,"Amount",j)*rn(0,"Discount",j)/100,2);
}

n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);

for(var j=0;j<r(0,'DisAmount').length;j++){
r(0,"DisAmount")[j].value=f(rn(0,"Amount",j)-rn(0,"Amount",j)*rn(0,"Discount",j)/100,2);
}

for(var j=0;j<r(0,'CurPrice').length;j++){
r(0,"CurPrice")[j].value=f((((nn('Tax',0)/100)+1)*rn(0,"Discount",j)*rn(0,"Price",j)/100)/nn('CurrencyRate',0),4);
}

for(var j=0;j<r(0,'CurAmount').length;j++){
r(0,"CurAmount")[j].value=f((((nn('Tax',0)/100)+1)*rn(0,"Amount",j)*rn(0,"Discount",j)/100)/nn('CurrencyRate',0),2);
}

n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 
 }
 } 

 var z=n('Tax');
 for(i = 0;i<z.length;i++){ 
 z[i].onchange = function(){ 
 for(var j=0;j<r(0,'TaxPrice').length;j++){
r(0,"TaxPrice")[j].value=f(((nn('Tax',0)/100)+1)*rn(0,"Discount",j)*rn(0,"Price",j)/100,4);
}

for(var j=0;j<r(0,'TaxAmount').length;j++){
r(0,"TaxAmount")[j].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",j)*rn(0,"Discount",j)/100,2);
}

for(var j=0;j<r(0,'CurPrice').length;j++){
r(0,"CurPrice")[j].value=f((((nn('Tax',0)/100)+1)*rn(0,"Discount",j)*rn(0,"Price",j)/100)/nn('CurrencyRate',0),4);
}

for(var j=0;j<r(0,'CurAmount').length;j++){
r(0,"CurAmount")[j].value=f((((nn('Tax',0)/100)+1)*rn(0,"Amount",j)*rn(0,"Discount",j)/100)/nn('CurrencyRate',0),2);
}

n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);

n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);

n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);

for(var j=0;j<r(0,'CoTaxAmt').length;j++){
r(0,"CoTaxAmt")[j].value=f(rn(0,"TaxAmount",j)-rn(0,"DisBackAmt",j),2);
}

n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);

for(var j=0;j<r(0,'DisAmount').length;j++){
r(0,"DisAmount")[j].value=f(rn(0,"Amount",j)-rn(0,"DisBackAmt",j),2);
}

n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 
 }
 } 

 var z=n('DiscountAmount');
 for(i = 0;i<z.length;i++){ 
 z[i].onchange = function(){ 
 n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);

 
 }
 } 

 var z=n('CurrencyRate');
 for(i = 0;i<z.length;i++){ 
 z[i].onchange = function(){ 
 for(var j=0;j<r(0,'TaxPrice').length;j++){
r(0,"TaxPrice")[j].value=f(rn(0,"CurPrice",j)*nn('CurrencyRate',0),4);
}

for(var j=0;j<r(0,'DisPrice').length;j++){
r(0,"DisPrice")[j].value=f(rn(0,"CurPrice",j)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1),4);
}

for(var j=0;j<r(0,'Price').length;j++){
r(0,"Price")[j].value=f(rn(0,"CurPrice",j)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1)/(rn(0,"Discount",j)/100),4);
}

for(var j=0;j<r(0,'TaxAmount').length;j++){
r(0,"TaxAmount")[j].value=f(rn(0,"CurAmount",j)*nn('CurrencyRate',0),2);
}

for(var j=0;j<r(0,'DisBackAmt').length;j++){
r(0,"DisBackAmt")[j].value=f(rn(0,"CurAmount",j)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1),2);
}

for(var j=0;j<r(0,'Amount').length;j++){
r(0,"Amount")[j].value=f(rn(0,"CurAmount",j)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1)/(rn(0,"Discount",j)/100),2);
}

n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);

n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);

for(var j=0;j<r(0,'CoTaxAmt').length;j++){
r(0,"CoTaxAmt")[j].value=f(rn(0,"TaxAmount",j)-rn(0,"DisBackAmt",j),2);
}

n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);

n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

n('AccountAmount')[0].value=f(nn('CurAccountAmount',0)*nn('CurrencyRate',0),2);

 
 }
 } 

 
}

function getrp(obj,field){
 obj.t = "t"; 
 var p = 0; 
 var mi = n(field); 
 for(i=0;i<mi.length;i++){
 if(mi[i].t == "t"){
 p = i;
 obj.t = ""; 
 } 
 } 
 return p;
}

function initCalculate(){
 var z=r(0,'Qty');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_Qty'); 
 r(0,"Amount")[p].value=f(rn(0,"Price",p)*rn(0,"Qty",p),2);
r(0,"DisBackAmt")[p].value=f(rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
r(0,"TaxAmount")[p].value=f(((nn('Tax',0)/100)+1)*(rn(0,"Amount",p)*rn(0,"Discount",p)/100),2);
r(0,"CurAmount")[p].value=f((((nn('Tax',0)/100)+1)*(rn(0,"Amount",p)*rn(0,"Discount",p)/100))/nn('CurrencyRate',0),2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"CostAmount")[p].value=f(rn(0,"CostPrice",p)*rn(0,"Qty",p),2);

 execStat(); 
 }
 }
 var z=r(0,'Price');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_Price'); 
 r(0,"Amount")[p].value=f(rn(0,"Price",p)*rn(0,"Qty",p),2);
r(0,"DisPrice")[p].value=f(rn(0,"Discount",p)*rn(0,"Price",p)/100,4);
r(0,"TaxPrice")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"Discount",p)*rn(0,"Price",p)/100,4);
r(0,"CurPrice")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"Discount",p)*rn(0,"Price",p)/100)/nn('CurrencyRate',0),4);
r(0,"DisBackAmt")[p].value=f(rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
r(0,"TaxAmount")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"CurAmount")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100)/nn('CurrencyRate',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'Amount');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_Amount'); 
 r(0,"Price")[p].value=f(rn(0,"Amount",p)/rn(0,"Qty",p),4);
r(0,"DisPrice")[p].value=f(rn(0,"Discount",p)*rn(0,"Price",p)/100,4);
r(0,"TaxPrice")[p].value=f(((nn('Tax',0)/100)+1)*(rn(0,"Discount",p)*rn(0,"Price",p)/100),4);
r(0,"CurPrice")[p].value=f((((nn('Tax',0)/100)+1)*(rn(0,"Discount",p)*rn(0,"Price",p)/100))/nn('CurrencyRate',0),4);
r(0,"DisBackAmt")[p].value=f(rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
r(0,"TaxAmount")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"CurAmount")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100)/nn('CurrencyRate',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'Discount');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_Discount'); 
 r(0,"DisPrice")[p].value=f(rn(0,"Discount",p)*rn(0,"Price",p)/100,4);
r(0,"DisBackAmt")[p].value=f(rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
r(0,"TaxPrice")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"Discount",p)*rn(0,"Price",p)/100,4);
r(0,"TaxAmount")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
r(0,"CurPrice")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"Discount",p)*rn(0,"Price",p)/100)/nn('CurrencyRate',0),4);
r(0,"CurAmount")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100)/nn('CurrencyRate',0),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'DisPrice');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_DisPrice'); 
 r(0,"Discount")[p].value=f(rn(0,"DisPrice",p)/rn(0,"Price",p)*100,4);
r(0,"DisBackAmt")[p].value=f(rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
r(0,"TaxPrice")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"DisPrice",p),4);
r(0,"TaxAmount")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100,2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
r(0,"CurPrice")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"DisPrice",p))/nn('CurrencyRate',0),4);
r(0,"CurAmount")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"Amount",p)*rn(0,"Discount",p)/100)/nn('CurrencyRate',0),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'DisBackAmt');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_DisBackAmt'); 
 r(0,"DisPrice")[p].value=f(rn(0,"DisBackAmt",p)/rn(0,"Qty",p),4);
r(0,"Discount")[p].value=f(rn(0,"DisPrice",p)/rn(0,"Price",p)*100,4);
r(0,"TaxPrice")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"DisPrice",p),4);
r(0,"TaxAmount")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"DisBackAmt",p),2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
r(0,"CoTaxAmt")[p].value=f(((nn('Tax',0)/100)+1)*rn(0,"DisBackAmt",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
r(0,"CurAmount")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"DisBackAmt",p))/nn('CurrencyRate',0),2);
r(0,"CurPrice")[p].value=f((((nn('Tax',0)/100)+1)*rn(0,"DisPrice",p))/nn('CurrencyRate',0),4);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'TaxPrice');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_TaxPrice'); 
 r(0,"TaxAmount")[p].value=f(rn(0,"Qty",p)*rn(0,"TaxPrice",p),2);
r(0,"DisPrice")[p].value=f(rn(0,"TaxPrice",p)/((nn('Tax',0)/100)+1),4);
r(0,"Price")[p].value=f((rn(0,"TaxPrice",p)/((nn('Tax',0)/100)+1)*100)/rn(0,"Discount",p),4);
r(0,"CurPrice")[p].value=f(rn(0,"TaxPrice",p)/nn('CurrencyRate',0),4);
r(0,"Amount")[p].value=f(rn(0,"TaxAmount",p)/((nn('Tax',0)/100)+1)/(rn(0,"Discount",p)/100),2);
r(0,"DisBackAmt")[p].value=f(rn(0,"TaxAmount",p)/((nn('Tax',0)/100)+1),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"CurAmount")[p].value=f(rn(0,"TaxAmount",p)/nn('CurrencyRate',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'TaxAmount');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_TaxAmount'); 
 r(0,"TaxPrice")[p].value=f(rn(0,"TaxAmount",p)/rn(0,"Qty",p),4);
r(0,"DisPrice")[p].value=f(rn(0,"TaxPrice",p)/((nn('Tax',0)/100)+1),4);
r(0,"Price")[p].value=f((rn(0,"TaxPrice",p)/((nn('Tax',0)/100)+1))*100/rn(0,"Discount",p),4);
r(0,"CurPrice")[p].value=f((rn(0,"TaxAmount",p)/rn(0,"Qty",p))/nn('CurrencyRate',0),4);
r(0,"Amount")[p].value=f((((rn(0,"TaxAmount",p)/rn(0,"Qty",p))/((nn('Tax',0)/100)+1))*100/rn(0,"Discount",p))*rn(0,"Qty",p),2);
r(0,"DisBackAmt")[p].value=f(((((rn(0,"TaxAmount",p)/rn(0,"Qty",p))/((nn('Tax',0)/100)+1))*100/rn(0,"Discount",p))*rn(0,"Qty",p))*rn(0,"Discount",p)/100,2);
r(0,"DisAmount")[p].value=f(((((rn(0,"TaxAmount",p)/rn(0,"Qty",p))/((nn('Tax',0)/100)+1))*100/rn(0,"Discount",p))*rn(0,"Qty",p))-(((((rn(0,"TaxAmount",p)/rn(0,"Qty",p))/((nn('Tax',0)/100)+1))*100/rn(0,"Discount",p))*rn(0,"Qty",p))*rn(0,"Discount",p)/100),2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-(((((rn(0,"TaxAmount",p)/rn(0,"Qty",p))/((nn('Tax',0)/100)+1))*100/rn(0,"Discount",p))*rn(0,"Qty",p))*rn(0,"Discount",p)/100),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
r(0,"CurAmount")[p].value=f(rn(0,"TaxAmount",p)/nn('CurrencyRate',0),2);
n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'CurPrice');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_CurPrice'); 
 r(0,"TaxPrice")[p].value=f(rn(0,"CurPrice",p)*nn('CurrencyRate',0),4);
 r(0,"DisPrice")[p].value=f(rn(0,"CurPrice",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1),4);
r(0,"Price")[p].value=f(rn(0,"CurPrice",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1)/(rn(0,"Discount",p)/100),4);
r(0,"CurAmount")[p].value=f(rn(0,"CurPrice",p)*rn(0,"Qty",p),2);
r(0,"TaxAmount")[p].value=f(rn(0,"CurAmount",p)*nn('CurrencyRate',0),2);
r(0,"DisBackAmt")[p].value=f(rn(0,"CurAmount",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1),2);
r(0,"Amount")[p].value=f(rn(0,"CurAmount",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1)/(rn(0,"Discount",p)/100),2);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
n('CurTotalAmount')[0].value=f(sm('tblSalesOrderDet_CurAmount'),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'CurAmount');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_CurAmount'); 
 r(0,"TaxAmount")[p].value=f(rn(0,"CurAmount",p)*nn('CurrencyRate',0),2);
r(0,"DisBackAmt")[p].value=f(rn(0,"CurAmount",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1),2);
r(0,"Amount")[p].value=f(rn(0,"CurAmount",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1)/(rn(0,"Discount",p)/100),2);
r(0,"CurPrice")[p].value=f(rn(0,"CurAmount",p)/rn(0,"Qty",p),4);
r(0,"TaxPrice")[p].value=f(rn(0,"CurPrice",p)*nn('CurrencyRate',0),4);
r(0,"DisPrice")[p].value=f(rn(0,"CurPrice",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1),4);
r(0,"Price")[p].value=f(rn(0,"CurPrice",p)*nn('CurrencyRate',0)/((nn('Tax',0)/100)+1)/(rn(0,"Discount",p)/100),4);
r(0,"DisAmount")[p].value=f(rn(0,"Amount",p)-rn(0,"DisBackAmt",p),2);
n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);
n('CurTotalAmount')[0].value=f(sm('tblSalesOrderDet_CurAmount'),2);
r(0,"CoTaxAmt")[p].value=f(rn(0,"TaxAmount",p)-rn(0,"DisBackAmt",p),2);
n('TotalCoTaxAmt')[0].value=f(sm('tblSalesOrderDet_CoTaxAmt'),2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 var z=r(0,'SecQty');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_SecQty'); 
 
 execStat(); 
 }
 }
 var z=r(0,'CostAmount');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_CostAmount'); 
 r(0,"CostPrice")[p].value=f(rn(0,"CostAmount",p)/rn(0,"Qty",p),4);

 execStat(); 
 }
 }
 var z=r(0,'CostPrice');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_CostPrice'); 
 r(0,"CostAmount")[p].value=f(rn(0,"CostPrice",p)*rn(0,"Qty",p),2);

 execStat(); 
 }
 }
 var z=r(0,'PresentSampleType');
 for(i = 0;i<z.length;i++){
 z[i].onchange = function(){ 
 var p =getrp(this,'tblSalesOrderDet_PresentSampleType'); 
 r(0,"Price")[p].value=f(0,4);

r(0,"Amount")[p].value=f(0,2);

r(0,"DisPrice")[p].value=f(0,4);

r(0,"TaxPrice")[p].value=f(0,4);

r(0,"CurPrice")[p].value=f(0,4);

r(0,"DisBackAmt")[p].value=f(0,2);

r(0,"TaxAmount")[p].value=f(0,2);

n('TotalAmount')[0].value=f(sm('tblSalesOrderDet_DisBackAmt'),2);
n('TotalTaxAmount')[0].value=f(sm('tblSalesOrderDet_TaxAmount')-nn('DiscountAmount',0),2);

r(0,"CoTaxAmt")[p].value=f(0,2);

r(0,"CurAmount")[p].value=f(0,2);

n('CurTotalAmount')[0].value=f(nn('TotalTaxAmount',0)/nn('CurrencyRate',0),2);

r(0,"DisAmount")[p].value=f(0,2);
n('DisAmount')[0].value=f(sm('tblSalesOrderDet_DisAmount'),2);

 execStat(); 
 }
 }
 }

var childCount=0;
var minValue = -9999999999;
var maxValue = 9999999999;
childData[childCount]='tblSalesOrderDet';
tabIsNulls[childCount]='1';
tabDiss[childCount]='销售订单明细';
childCount++;
 var tblSalesOrderDetData = new gridData();
 gridDatas[gridDataNum] = tblSalesOrderDetData;
 gridDataNum++;
 var tblSalesOrderDet=1;
 
 
 
 
 
 
 var param4="";
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_StockCode','仓库','80',2,true,'',100,2,'tblSalesOrderDet:StockCode:tblSalesOrderDet_StockCode:'+param4,":","","copy","0",'',"2",true);
 putChildValidateItem("tblSalesOrderDet_StockCode","仓库","any","",true,minValue,100,true,'',true);
 
 
 
 
 
 
 
 var param4="";
 param4=param4+":0仓库@StockCode"; 
 param4=param4+":0仓库@tblSalesOrderDet_StockCode";
 param4=param4+":0批号@tblSalesOrderDet_BatchNo";
 param4=param4+":0厚度@tblSalesOrderDet_Inch";
 param4=param4+":0颜色@tblSalesOrderDet_Hue";
 param4=param4+":0尺码@tblSalesOrderDet_yearNO";
 param4=param4+":0生产日期@tblSalesOrderDet_ProDate";
 param4=param4+":0保质期限@tblSalesOrderDet_Availably";
 param4=param4+":1客户@CompanyCode"; 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_GoodsCode','商品名称','120',2,false,'',200,-2,'','',"","triggerOrCopy","1",'',"2",true);
 putChildValidateItem("tblSalesOrderDet_GoodsCode","商品名称","any","",false,minValue,200,false,'');
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_tblGoods_GoodsNumber','商品编号','100',2,true,'',1000000,2,'tblSalesOrderDet:GoodsCode:tblSalesOrderDet_tblGoods_GoodsNumber:'+param4,':',"","triggerOrCopy","1",'',"2",false); 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_tblGoods_GoodsFullName','商品名称','150',18,true,'',1000000,2,'tblSalesOrderDet:GoodsCode:tblSalesOrderDet_tblGoods_GoodsFullName:'+param4,':',"","triggerOrCopy","1",'',"2",false); 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_tblGoods_GoodsSpec','商品规格','200',2,true,'',1000000,2,'tblSalesOrderDet:GoodsCode:tblSalesOrderDet_tblGoods_GoodsSpec:'+param4,':',"","triggerOrCopy","1",'',"2",false); 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_tblGoods_BaseUnit','单位','30',2,true,'',1000000,2,'tblSalesOrderDet:GoodsCode:tblSalesOrderDet_tblGoods_BaseUnit:'+param4,':',"","triggerOrCopy","1",'',"2",false); 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_tblStocks_lastQty','实际库存','90',1,true,'',1000000,2,'tblSalesOrderDet:GoodsCode:tblSalesOrderDet_tblStocks_lastQty:'+param4,':',"","triggerOrCopy","1",'',"2",false); 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_ViewVirtualStock_UseQty','虚拟库存','90',1,true,'',1000000,2,'tblSalesOrderDet:GoodsCode:tblSalesOrderDet_ViewVirtualStock_UseQty:'+param4,':',"","triggerOrCopy","1",'',"2",false); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_BatchNo','批号','120',2,true,'',100,-2,'','',"","triggerCopy","2",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_BatchNo","批号","any","GoodsField",true,minValue,100,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Inch','厚度','70',2,true,'',100,-2,'','',"","triggerCopy","3",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_Inch","厚度","any","GoodsField",true,minValue,100,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Hue','颜色','70',2,true,'',100,-2,'','',"","triggerCopy","4",'',"2",false); 
 putChildValidateItem("tblSalesOrderDet_Hue","颜色","any","GoodsField",true,minValue,100,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_yearNO','尺码','70',2,true,'',100,-2,'','',"","triggerCopy","5",'',"2",false); 
 putChildValidateItem("tblSalesOrderDet_yearNO","尺码","any","GoodsField",true,minValue,100,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_ProDate','生产日期','80',5,true,'',10,-2,'','',"","","6",'',"2",false); 
 putChildValidateItem("tblSalesOrderDet_ProDate","生产日期","date","GoodsField",true,minValue,10,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Availably','保质期限','80',5,true,'',10,-2,'','',"","","7",'',"2",false); 
 putChildValidateItem("tblSalesOrderDet_Availably","保质期限","date","GoodsField",true,minValue,10,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Qty','数量','60',1,false,'',0,0,'','',"Qty","","8",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_Qty","数量","double","",false,minValue,maxValue,false,'',true); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Price','单价','80',1,true,'',0,0,'','',"SPriceIdentifier","","9",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_Price","单价","double","",true,minValue,maxValue,false,'',true); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Amount','金额','100',1,true,'',0,0,'','',"SAmountIdentifier","","10",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_Amount","金额","double","",true,minValue,maxValue,false,'',true); 
 
 
 
 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_InvoiceStatus','开票状态','200',2,true,'0',10,-2,'','',"","","15",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_InvoiceStatus","开票状态","any","",true,minValue,10,true,'0'); 
 
 
 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Discount','折扣率%','100',1,true,'100',0,-2,'','',"Price","","19",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_Discount","折扣率%","double","BillDiscount",true,minValue,maxValue,true,'100'); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_DisPrice','折后单价','100',1,true,'',0,-2,'','',"SPriceIdentifier","","20",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_DisPrice","折后单价","double","BillDiscount",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_DisBackAmt','折后金额','100',1,true,'',0,-2,'','',"SAmountIdentifier","","21",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_DisBackAmt","折后金额","double","BillDiscount",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_DisAmount','折让金额','100',1,true,'',0,-2,'','',"SAmountIdentifier","","22",'',"8",false); 
 putChildValidateItem("tblSalesOrderDet_DisAmount","折让金额","double","BillDiscount",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_TaxPrice','含税单价','100',1,true,'',0,-2,'','',"SPriceIdentifier","","23",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_TaxPrice","含税单价","double","BillTax",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_TaxAmount','含税金额','100',1,true,'',0,-2,'','',"SAmountIdentifier","","24",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_TaxAmount","含税金额","double","BillTax",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_CoTaxAmt','税额','100',1,true,'0',0,-2,'','',"SAmountIdentifier","","25",'',"8",false); 
 putChildValidateItem("tblSalesOrderDet_CoTaxAmt","税额","double","BillTax",true,minValue,maxValue,true,'0'); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_CurPrice','外币单价','100',1,true,'',0,-2,'','',"SPriceIdentifier","","26",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_CurPrice","外币单价","double","currency",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_CurAmount','外币金额','100',1,true,'',0,-2,'','',"SAmountIdentifier","","27",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_CurAmount","外币金额","double","currency",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_ViewTotalQty','当前库存','80',1,true,'',0,-2,'','',"Qty","","28",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_ViewTotalQty","当前库存","double","",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_SendDate','发货日期','80',5,true,'',10,-2,'','',"","copy","29",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_SendDate","发货日期","date","",true,minValue,10,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_NotPayQty','未收款数量','224',1,true,'',0,-2,'','',"Qty","","30",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_NotPayQty","未收款数量","double","",true,minValue,maxValue,false,''); 
 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_SecQty','辅助数量','224',0,true,'0',0,-2,'','',"","","32",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_SecQty","辅助数量","int","",true,minValue,maxValue,true,'0'); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_BaseQty','基本单位','224',0,true,'0',0,-2,'','',"","","33",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_BaseQty","基本单位","int","",true,minValue,maxValue,true,'0'); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_OutPayQty','已出库付款数','224',1,true,'',0,-2,'','',"Qty","","34",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_OutPayQty","已出库付款数","double","",true,minValue,maxValue,false,''); 
 
 
 
 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_EmployeeID','经手人','0',2,true,'Sess_UserId',100,-2,'','',"","","39",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_EmployeeID","经手人","any","",true,minValue,100,true,'Sess_UserId'); 
 
 
 
 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_CostAmount','成本总额','60',1,true,'0',0,-2,'','',"AmountIdentifier","","44",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_CostAmount","成本总额","double","",true,minValue,maxValue,true,'0'); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_CostPrice','成本单价','60',1,true,'0',0,-2,'','',"priceIdentifier","","45",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_CostPrice","成本单价","double","",true,minValue,maxValue,true,'0'); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_DepartmentCode','部门','0',2,true,'Sess_DepartmentCode',100,-2,'','',"","","46",'',"3",false); 
 putChildValidateItem("tblSalesOrderDet_DepartmentCode","部门","any","",true,minValue,100,true,'Sess_DepartmentCode'); 
 
 
 
 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_PresentSampleType','赠(样)品','80',2,true,'',10,-2,'','',"","","51",'',"1",false); 
 putChildValidateItem("tblSalesOrderDet_PresentSampleType","赠(样)品","any","BillPresentSampleType",true,minValue,10,false,''); 
 
 
 
 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_Remark','备注','100',2,true,'',8000,0,'','',"","","56",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_Remark","备注","any","",true,minValue,8000,false,'',true); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_PlanQty','生产规划数','200',1,true,'',0,-2,'','',"","","57",'',"0",false); 
 putChildValidateItem("tblSalesOrderDet_PlanQty","生产规划数","double","",true,minValue,maxValue,false,''); 
 
 
 
 addCols(tblSalesOrderDetData,'tblSalesOrderDet_detsId','明细ID','0',2,true,'',100,-2,'','',"","","",'','3'); 
 
 
 
function sendSMS(){
 var keyIds = m("id").value ;
 var left = 250; 
 var top = screen.height/2 - 200;
 var str = window.open("/ClientAction.do?type=sendPrepare&msgType=sms&keyId="+keyIds,null,'menubar=no,toolbar=no,scrollbars=no,loaction=no,status=yes,resizable=no,width=750,height=400,left='+left+',top='+top);
}

function sendEmail(){
 var keyIds = m("id").value ;
 var left = 250; 
 var top = screen.height/2 - 200 ;
 var str = window.open("/ClientAction.do?type=sendPrepare&msgType=email&keyId="+keyIds,null,'menubar=no,toolbar=no,scrollbars=no,loaction=no,status=yes,resizable=no,width=750,height=400,left='+left+',top='+top);
}

function G(id){
 return document.getElementById(id);
}

function showDivCustomSetTable(){ 
 if(notOpenAccount){
 alert("未开帐不能时进行列配置");
 return;
 }
 var moduleType = document.getElementById("moduleType").value ;
 var strUrl = "/UserFunctionAction.do?tableName=tblSalesOrder&moduleType="+moduleType+"&winCurIndex="+winCurIndex+"&f_brother=&noback="+noback+"&parentCode=&operation=6&parentTableName=&configType=colConfig" ;
 asyncbox.open({id:'customPopup',title:'列配置',url:strUrl,width:700,height:430});
}

function fillColConfig(returnValue){
 if(typeof(returnValue)!="undefined"){
 var strType = returnValue.split("@@") ;
 if("colConfig"==strType[0]){
 var tables = "tblSalesOrder,tblSalesOrderDet,".split(",") ;
 var strCols = strType[1] ;
 for(var i=0;i<tables.length;i++){ 
 if(tables[i].length>0){
 strCols = strCols.replaceAll(tables[i]+":","a"+i+":") ;
 }
 }
 strCols = encodeURI(strCols);
 var strURL = "/UtilServlet?operation=colconfig&tableName=tblSalesOrder&allTableName=tblSalesOrder,tblSalesOrderDet,&colType=bill";
 jQuery.ajax({url: strURL,type: "POST",data:"colSelect="+strCols,async: false}); 
 document.location.href = document.location.href ;
 return;
 }else if("defaultConfig"==strType[0]){
 var strURL = "/UtilServlet?operation=defaultConfig&tableName=tblSalesOrder&allTableName=tblSalesOrderDet,&colType=bill" ;
 }else if("colWidth"==strType[0]){
 var strURL = "/UtilServlet?operation=colconfig&tableName=tblSalesOrder&colType=colWidth&colSelect="+strType[1] ;
 }else if("defaultWidth"==strType[0]){
 var strURL = "/UtilServlet?operation=defaultConfig&tableName=tblSalesOrder&allTableName=tblSalesOrderDet,&colType=colWidth" ;
 }else if(strType[0]=="colNameSet"){ 
 mdiwin(strType[1],'列名设置') ;
 return ;
 }
 AjaxRequest(strURL);
 document.location.href = document.location.href ;
 }
}

function showDept(){
 var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblDepartment&fieldName=DeptFullName&selectName=SelectDepartmentMes' ;
 var str = window.showModalDialog(urlstr+"&MOID="+MOID+"&MOOP=add",winObj,"dialogWidth=750px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var depts = str.split("|") ;
 for(var j=0;j<depts.length;j++){
 var field = depts[j].split(";") ;
 var existOption = m("formatDeptName").options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m("formatDeptName").options.add(new Option(field[1],field[0]));
 m("popedomDeptIds").value+=field[0]+",";
 }
 }
}

function showEmployee(fieldName,fieldNameIds){
 var urlstr = "/UserFunctionAction.do?operation=22&tableName=tblEmployee&selectName=SelectSMSEmployee" ;
 var str = window.showModalDialog(urlstr+"&MOID="+MOID+"&MOOP=query&LinkType=@URL:",winObj,"dialogWidth=750px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var employees = str.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 var existOption = m(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m(fieldName).options.add(new Option(field[1],field[0]));
 m(fieldNameIds).value+=field[0]+",";
 }
 }
 }
}

function delOpation(fileName,popedomId){
 var formatName=m(fileName);
 if(formatName.selectedIndex==-1){
 alert("请选择要移除的项.");
 }
 if(formatName.selectedIndex<0)return false ;
 var value = formatName.options[formatName.selectedIndex].value;
 var appcers = m(popedomId).value;
 appcers = appcers.replace(value+",","");
 m(popedomId).value =appcers
 formatName.options.remove(formatName.selectedIndex);
}

function compareDate(startDatetime,hour,minute){
 var sdStr = startDatetime.split("-") ;
 var sd = new Date(sdStr[0],sdStr[1]-1,sdStr[2], hour, minute,"00");
 var diff = sd - new Date() ;
 if (diff < 0){
 alert("提醒时间不能少于当前系统时间");
 return false;
 }
 return true;
}

function checkAlertSet(){
 if(m("alertDivId")==null)return true;
 var alertDiv = m("alertDivId").style.display ;
 if("none"==alertDiv){return true ;}
 var alertType = n("alertType") ;
 var hasSelect = false ;
 for(var i=0;i<alertType.length;i++){
 if(alertType[i].checked){
 hasSelect = true ;
 break ;
 }
 }
 if(!hasSelect){
 alert("请选择至少一种提醒方式") ;
 return false ;
 }
 var alertDate = m("alertDate").value ;
 if(alertDate.length==0){
 alert("提醒时间不能为空") ;
 return false ;
 }
 var alertHour = m("alertHour").value ;
 var alertMinute = m("alertMinute").value ;
 if(!compareDate(alertDate,alertHour,alertMinute)){
 return false;
 }
 var alertContent = m("alertContent").value ;
 if(alertContent.length==0){
 alert("提醒内容不能为空") ;
 return false ;
 }
 var popedomUserIds = m("popedomUserIds") ;
 var popedomDeptIds = m("popedomDeptIds") ;

 if(typeof(popedomUserIds)!="undefined" && popedomUserIds!=null 
 && popedomUserIds.value.length==0 && popedomDeptIds.value.length==0){
 alert("请选择提醒对象") ;
 return false ;
 }
 var loopTime = m("loopTime").value ;
 if(!isInt(loopTime)){
 alert("每隔多少(天,周,月,年)提醒一次必须是整数") ;
 return false ;
 }
 var hasEndDate = m("endDateId") ;
 if(hasEndDate.checked){
 var endDate = m("endDate").value ;
 if(endDate.length==0){
 alert("结束日期不能为空") ;
 return false ;
 }
 }
 return true ;
}

function attention(attention,tableName,empId,keyId){
 if(attention=="attention"){
 document.location.href="/UserFunctionQueryAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=4&optionType="+attention+"&empId="+empId;
 }else{
 document.location.href="/UserFunctionQueryAction.do?tableName="+tableName+"&empId="+empId+"&keyId="+keyId+"&operation=4&optionType="+attention;
 }
}




function RecAutoSettlement(){
 var tableName=document.getElementById("tableName").value;
 var companyCode=document.getElementById("CompanyCode").value;
 var BillDate=document.getElementById("BillDate").value;
 
 
 var AcceptTypeIDStr='AcceptTypeID';if(tableName=="tblPay")AcceptTypeIDStr='PaytypeID';
 var AcceptTypeID=document.getElementById(AcceptTypeIDStr).value;
 
 var ExeBalAmtStr='FactIncome';if(tableName=="tblPay")ExeBalAmtStr='FactOutcome';
 var ExeBalAmt=document.getElementById(ExeBalAmtStr).value;
 
 if(companyCode.length==0){
 if(tableName=="tblPay"){
 alert("请选择供应商");
 }else{
 alert("请选择客户");
 }
 return;
 }
 
 if(isNaN(ExeBalAmt)||Number(ExeBalAmt)<=0){
 if(tableName=="tblPay"){
 alert("请在付款账户明细表中输入付款金额");
 }else{
 alert("请在收款账户明细表中输入收款金额");
 }
 return;
 }
 
 window.thisField = 'tblSaleReceiveDet_RefbillNo';
 if(tableName=='tblPay')window.thisField = 'tblPaydet_RefBillNo';
 
 window.field = window.thisField;
 window.obj=document.getElementsByName(window.thisField)[0];
 var url="";
 if(tableName=='tblPay'){
 var url ="UtilServlet?keyId=&tableName=tblPaydet&fieldName=RefBillNo";
 }else{ 
 var url ="UtilServlet?keyId=&tableName=tblSaleReceiveDet&fieldName=RefbillNo";
 }
 var url=url+"&operation=RecAutoSettlement&CompanyCode="+companyCode+"&"+AcceptTypeIDStr+"="+AcceptTypeID+"&BillDate="+BillDate+"&MOID="+MOID+"&MOOP=add&selectField=&selectValue=";

 url = encodeURI(url) ;
 if(url.indexOf("+")!=-1){
 url = url.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 AjaxRequest(url) ; 
 
 var str=response;
 var newStr='';

 var strArray=str.split('|');
 for(var i=0;i<strArray.length;i++){
 if(ExeBalAmt>0&&strArray[i].length>0){
 var fieldValArray=strArray[i].split(';');
 var hanExeBalAmt=fieldValArray[7];

 if(Number(ExeBalAmt)<Number(hanExeBalAmt)){
 hanExeBalAmt=ExeBalAmt
 }
 fieldValArray[7]=hanExeBalAmt;
 
 ExeBalAmt=f(ExeBalAmt-hanExeBalAmt,2);
 var fieldVal='';
 for(var j=0;j<fieldValArray.length;j++){
 fieldVal=fieldVal+fieldValArray[j]+';';
 }
 newStr=newStr+fieldVal+'|';
 }else{
 break;
 }
 }
 if(newStr.length==0){
 alert("没有需要结算的单据");
 }
 var colNames='';
 if(tableName=='tblPay'){
 curGridRowNum.curKey='tblPaydet';
 var colNames='@TABLENAME.RefBillDate;@TABLENAME.RefBillNo;@TABLENAME.BillAmt;@TABLENAME.SettledAmt;@TABLENAME.BackAmt;@TABLENAME.WexeBalAmt;@TABLENAME.BillName;@TABLENAME.ExeBalAmt;@TABLENAME.RefbillID;@TABLENAME.BuyOrderNo;@TABLENAME.PayBillType;@TABLENAME.BuyOrderID;@TABLENAME.CompanyCode;@TABLENAME.Account;';
 }else{
 curGridRowNum.curKey='tblSaleReceiveDet';
 var colNames='@TABLENAME.RefbillNo;@TABLENAME.BillAmt;@TABLENAME.SettledAmt;@TABLENAME.BackAmt;@TABLENAME.WexeBalAmt;@TABLENAME.BillName;@TABLENAME.RefBillDate;@TABLENAME.ExeBalAmt;@TABLENAME.RefbillID;@TABLENAME.SalesOrderNo;@TABLENAME.ReceiveBillType;@TABLENAME.SalesOrderID;@TABLENAME.CompanyCode;@TABLENAME.SubCode;';
 }
 
 gridTable =document.getElementById(curGridRowNum.curKey+"Table");
 tableDiv = gridTable.parentNode.parentNode.parentNode;
 var dc = document.getElementsByName(window.thisField).length;
 for(var i=0;i<dc-1;i++){
 tableDiv.fix.deleteRow(1);
 }
 curGridRowNum.curLine=0;
 tableDiv.fix.addRow();
 
 
 var divTitle =document.getElementById(curGridRowNum.curKey+"TableDIVTitle");
 changeTags(divTitle);
 
 $("#BillNo").focus();
 dataBackIn(false,colNames,newStr,window)
}



function extendSubmit(vars,vname,selected)
{
 document.getElementById("ButtonTypeName").value = vname;
 if(vars == "RecAutoSettlement"){
 RecAutoSettlement();
 return;
 }else if(vars == "custom"){
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
 var varValue = window.showModalDialog("/vm/oa/oaPublicMsg/folderRight.jsp?tableName=tblSalesOrder&keyId="+keyIdStr,"","dialogWidth=565px;dialogHeight=350px") ;
 if(typeof(varValue)=="undefined")return ;
 }else if(vars == "dataMove"){
 displayName = encodeURI("分级目录") ;
 var selectName = document.getElementById("selectName").value ;
 var moduleType = document.form.moduleType;
 var strURL = "/UserFunctionAction.do?&selectName="+selectName+"&isRoot=isRoot&operation=22" ;
 if(typeof(moduleType)!="undefined"){
 strURL = strURL+"&moduleType="+moduleType.value ; 
 }
 var str = window.showModalDialog(strURL+"&isQuote=1&popDataType=dataMove&MOID="+MOID+"&MOOP=query&LinkType=@URL:&displayName="+displayName,"",
 "dialogWidth=750px;dialogHeight=430px");
 if(typeof(str)=="undefined")return false ;
 var fieldValue=str.split(";"); 
 
 document.getElementById("classCode").value=fieldValue[0];
 document.getElementById("ButtonType").value = "dataMove" ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 }else if(vars=="billExport"){
 if(!confirm("确定导出数据吗?"))return
 document.getElementById("ButtonType").value = "billExport" ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
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
 if(!confirm("确定"+vname+"?")){ 
 return;
 }
 document.getElementById("ButtonType").value = "execDefine" ;
 form.defineName.value=vars;
 }
 form.operation.value=25;
 form.submit();
}

function extendSubmitPopDefine(selectName,defineName,displayName){ 
 var moduleType = document.getElementsByName("moduleType")[0].value ;
 document.getElementById("ButtonTypeName").value = displayName;
 form.defineName.value=defineName;
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
 form.submit();
}



function initDownSelect(){ 
 
} 



function billQuery(value,dis,width,height){
 billTool(value,dis,width,height);
}

function billTool(value,dis,width,height){
 while(value.indexOf("@ValueofMustDB:") >-1){
 pos = value.indexOf("@ValueofMustDB:");
 epos = value.indexOf("&",pos);
 dpos = value.indexOf("#;",pos);
 if(dpos > 0 && dpos<epos){
 epos = dpos;
 }
 if(epos==-1){
 epos = value.length;
 }
 
 name = value.substring(pos+15,epos);
 if(name.indexOf("@TABLENAME") ==0){
 if(curGridRowNum.curKey == ""){
 alert("请选择要查询的明细行");
 return;
 }
 name = curGridRowNum.curKey+name.substring(10);
 }
 obj=null;
 objName = name; 
 
 nv = "";
 ns = name.split("|"); 
 for(var j=0;j<ns.length;j++){ 
 objName = ns[j]; 
 obj = document.getElementsByName(objName); 
 objOne = null;
 if(obj.length > 1 ){ 
 curl = curGridRowNum.curLine;
 if((curl>=obj.length||curl<0)){
 curl = 0;
 } 
 objOne = obj[curl] ; 
 }else if(obj.length == 1 ){
 objOne = obj[0];
 }else{ 
 continue;
 }
 nv = objOne.value; 
 if(nv != "" && nv != null && nv != undefined){
 break;
 } 
 }

 if(nv==""){ 
 if(objName.indexOf("_")>0){
 for(var i=0;i<childCheckList.length;i++){
 if(childCheckList[i].name == objName)
 {
 alert("请先输入当前选择明细行的"+childCheckList[i].title); 
 return;
 }
 }
 alert("请先输入"+dis+":"+objName); 
 return; 
 }else{
 for(var i=0;i<checkList.length;i++){
 if(checkList[i].name == objName)
 {
 asyncbox.alert("请先输入"+checkList[i].title,'提示');
 return;
 }
 }
 alert("请先输入"+dis+":"+objName); 
 return; 
 } 
 } 
 value = value.substring(0,pos)+encodeURIComponent(nv)+value.substring(epos); 
 }
 while(value.indexOf("@ValueofDB:") >-1){
 pos = value.indexOf("@ValueofDB:");
 epos = value.indexOf("&",pos);
 dpos = value.indexOf("#;",pos);
 if(dpos > 0 && dpos<epos){
 epos = dpos;
 }
 if(epos==-1){
 epos = value.length;
 }
 
 name = value.substring(pos+11,epos);
 if(name.indexOf("@TABLENAME") ==0){
 if(curGridRowNum.curKey == ""){
 alert("请选择要查询的明细行");
 return;
 }
 name = curGridRowNum.curKey+name.substring(10);
 }
 obj=null;
 objName = name; 
 
 nv = "";
 ns = name.split("|"); 
 for(var j=0;j<ns.length;j++){ 
 objName = ns[j];
 
 obj = document.getElementsByName(objName); 
 objOne = null;
 if(obj.length > 1 ){ 
 curl = curGridRowNum.curLine;
 if((curl>=obj.length||curl<0)){
 curl = 0;
 } 
 objOne = obj[curl] ; 
 }else if(obj.length == 1 ){
 objOne = obj[0];
 }else{ 
 continue;
 }
 nv = objOne.value; 
 if(nv != "" && nv != null && nv != undefined){
 break;
 } 
 }
 
 value = value.substring(0,pos)+encodeURIComponent(nv)+value.substring(epos); 
 }
 
 
 if(width=='' || width>document.documentElement.clientWidth-100){
 width=document.documentElement.clientWidth-100;
 }
 if(height=='' || height > document.documentElement.clientHeight-80){
 height=document.documentElement.clientHeight-80;
 }
 
 value = value.replaceAll('#;#',encodeURIComponent('#;#'));
 
 value= value+"&LinkType=@URL:&src=menu";
 
 openPop('PopTooldiv',dis,value,width,height,false,false);
}
function closePopWin(){
 if(jQuery.exist('Popdiv')){ 
 jQuery.close('Popdiv'); 
 }
}

var curpushUser;
var curpushdetId;
function billPush(destTableName,tableDisplay){
 width=document.documentElement.clientWidth;
 height=document.documentElement.clientHeight;
 if($("#workFlowNodeName").val()!="finish"){
 alert("审核未结束的单据不可以推送");
 return;
 }
 var turl="/UserFunctionAction.do?tableName="+destTableName+"&parentTableName="+$("#parentTableName").val()+"&sourceTableName="+$("#tableName").val()+"&sourceId="+$("#id").val()+"&operation=15";
 openPop('PopPushMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight); 
 return;
 
 asyncbox.open({
 id : 'Popdiv',
 title : '推单['+tableDisplay+']',
 　　 html : '<span style="padding:20px 0 10px 20px;display:block">请选择推送职员，默认为自己</span><input type=hidden id=pushUser name=pushUser>'+
 '<input type=text id=pushUserName name=pushUserName readOnly style="width:100px;margin-left:20px">'+
 '<input type=button name=pbt onclick="selectpushUser()" value="选择职员"/>',
 　　 width : 300,
 　　 height : 180,
 btnsbar : [{text : '整单推送',action : 'allPush' },
 
 {text : '取消',action : 'cancel' }],
 callback : function(action){
 if(action != 'allPush' && action != 'partPush'){
 return;
 }
 curpushUser = $("#pushUser").val()
 　　　　　if(action == 'allPush'){
 
 　　　　　}
 　　　　　if(action == 'partPush'){ 
 
 url = "/UtilServlet?operation=pushBill&tableName="+$("#tableName").val()+"&destTableName="+destTableName+"&getFirstChildTable=true";
 alert(url);
 ftable = jQuery.ajax({
 url: url , 
 async: false 
 }).responseText; 
 openSelectPushDet(ftable); 
 　　　　　　　return;
 　　　　　}
 
 jQuery.post("/UtilServlet?operation=pushBill",
 { tableName: $("#tableName").val(), 
 destTableName: destTableName, 
 userId: curpushUser,
 varKeyIds: $("#varKeyIds").val(),
 detId :'' },
 function(data){ 
 if(data == "HASPUSH"){
 
 asyncbox.confirm('此单已进行过推单['+tableDisplay+'],是否继续?','推单确认',function(action){
 　　　if(action == 'ok'){
 　　　　　jQuery.post("/UtilServlet?operation=pushBill",
 { tableName: $("#tableName").val(), 
 destTableName: destTableName, 
 userId: curpushUser,
 varKeyIds: $("#varKeyIds").val(),
 isContinue: 'true',
 detId :'' },
 function(data){ 
 billPushAfter(data);
 } 
 );
 　　　}
 　}); 
 }else{
 billPushAfter(data);
 }
 } 
 );
 
 　　　} 
　 });
}
function billPushAfter(data){
 if(data.indexOf("ok:") == 0){
 ds = data.split(":");
 right = ds[1];
 width = Number(ds[2]);
 height = Number(ds[3]); 
 turl = ds[4];
 if(right != "update"){ 
 alert("推单成功");
 return;
 }
 
 asyncbox.confirm('推单成功，是否立即打开新单据?','提示',function(action){
 　　　if(action == 'ok'){
 if(width ==0){
 width=document.documentElement.clientWidth;
 }
 if(height ==0){
 height=document.documentElement.clientHeight;
 }
 openPop('PopPushMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight); 
 　　　}
 });

 
 }else{
 alert(data);
 }

}
function openSelectPushDet(ftable){

 asyncbox.open({
 id : 'PopSelBilldiv',
 title : '选择明细',
 　　 html : '<div name="tblBuyOrderDetTableDIV" id="tblBuyOrderDetTableDIV" ah="tblBuyOrderDetTable" style="">'+$("#tblBuyOrderDetTableDIV").html()+'</div>'
 });
 return;
}
 
function selectpushUser(){
 condition='openFlag';
 popname="userGroup";
 var urls = "/Accredit.do?popname=" + popname + "&inputType=radio&condition="+condition; 
 titles = "请选择职员"
 
 asyncbox.open({
 id : 'PopUserdiv',
 title : titles,
 url : urls,
 width : 755,
 height : 435,
 top: 5,
 btnsbar : jQuery.btn.OKCANCEL,
 callback : function(action,opener){
　　　　　 if(action == 'ok'){
 var employees = opener.strData;
 emps = employees.split(";");
 $("#pushUser").val(emps[0]);
 $("#pushUserName").val(emps[1]);
　　　　　 }
　　　 }
　 }); 
}


function fillData(datas){
 var employees = datas;
 emps = employees.split(";");
 $("#pushUser").val(emps[0]);
 $("#pushUserName").val(emps[1]);
 jQuery.close('PopUserdiv');
}

function calc(){
 var urls = "/common/calc.jsp"; 
 asyncbox.open({
 id : 'PopCalcdiv',
 title : '计算器',
 url : urls,
 width : 210,
 height : 220,
 top: 25
　 }); 
}

function flowDepict(applyType,keyId){ 
 var winWidth = 1024;
 if($(window).width()>1024){
 winWidth = 1150;
 }
 window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=5&applyType="+applyType,null,"height=570, width="+winWidth+",top=50,left=100 ");
}
function changeTags(obj){
 $(obj).addClass("tagSel").siblings("span").removeClass("tagSel");
 $("#"+$(obj).attr("show")).show().siblings("div").hide();
 $("#"+$(obj).attr("show")).find(":text:eq(0)").focus();
 if(jQuery.exist('PopBatchInputdiv')){ 
 jQuery.close("PopBatchInputdiv");
 }
}
function showGridTags(){
 var firstTag = $(".showGridTags .tags:eq(0)");
 firstTag.addClass("tagSel");
 $("#"+firstTag.attr("show")).show().siblings("div").hide();
}
 
function update(){ 
 location.href="/UserFunctionAction.do?tableName=tblSalesOrder&designId=eae3edac_0906121936571710021&keyId="+$("#id").val()+
 "&f_brother="+f_brother+"&parentCode="+$("#parentCode").val()+"&operation=7&noback=&winCurIndex="+winCurIndex+
 "&fromPage=detail&moduleType="+moduleType+"&parentTableName="+$("#parentTableName").val()+"&saveDraft="+$("#saveDraft").val()+"&popWinName="+$("#popWinName").val(); 
}

function copyOp(keyId)
{ 
 form.target = ""; 
 form.action="/UserFunctionAction.do?&operation=17&tableName=tblSalesOrder&saveDraft=&&keyId="+keyId+
 "&parentTableName=&moduleType="+moduleType+"&f_brother="+f_brother+"&noback="+noback+"&winCurIndex="+winCurIndex;
 form.submit();
}

function draftAudit(){
 

 
 form.action = '/UserFunctionQueryAction.do?varKeyIds='+$("#id").val()+'|'+'&ButtonType=draftAudit';
 form.operation.value = 25 ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 document.getElementById("form").target = "formFrame" ; 
 form.submit() ; 
}




function checkDialog(url){
 asyncbox.open({
 id:'deliverApproveTo',url:url,title:'审核',width:650,height:370,
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
function checkSave(str){ 
 var defi = "&defineInfo=";
 var cid = $("#id").val(); 
 if(str != null && str != ""){
 defi += str;
 } 
 jQuery.post("/UserFunctionQueryAction.do?tableName=tblSalesOrder&operation=25&ButtonType=saveCheckBill&varKeyIds="+cid+defi+"&moduleType="+moduleType,
 {ButtonTypeName:'审核保存'},
 function(data){
 if(data=="OK"){
 checkDialog("/OAMyWorkFlow.do?keyId="+cid+"&tableName=tblSalesOrder&operation=82&fromPage=erp");
 }else if(data.indexOf("defineInfo")==0){
 var ds = data.split(":");
 if(confirm(ds[4])){
 if(ds[2] != ""){
 checkSave(ds[1]+":"+ds[2]+";");
 }
 }else{
 if(ds[3] != ""){
 checkSave(ds[1]+":"+ds[3]+";");
 }
 }
 } else{
 alert(data);
 } 
 });
}

function reversecheck(){
 if(confirm('是否确认反审核')){
 form.operation.value = '83';
 form.submit();
 }
}
function checkcancel(){
 if(confirm('确定撤回?')){
 form.nextStep.value='cancel';
 form.operation.value='79';
 form.submit();
 }
}

function hurryTrans(){
 ren = '销售订单';
 var url="/vm/oa/workflow/noteModel.jsp?tableName=tblSalesOrder&moduleName="+encodeURI(ren)+"&keyId="+$("#id").val();
 asyncbox.open({ 
 id:'deliverhurryTransTo',url:url,title:'催办提醒方式',
　　 　 width:750,height:300,
 btnsbar : jQuery.btn.OKCANCEL,
 callback : function(action,opener){
 if(action == 'ok'){
 opener.submitModenew();
 return false;
 }
　　 　}
　 });
}
function deleteBill(){ 
 if(confirm("是否确定删除？")){ 
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 form.operation.value=3;
 form.submit();
 }
}

function billExport(id){
 if(!confirm("确定导出数据吗?"))return
 
 
 
 
 document.form.operation.value='25';
 document.form.ButtonType.value='billExport';
 window.save=true; 
 document.form.submit();
 
}
var returnBarTitle=null;
var returnBarTitle_name=null;

function scanBarCodeEv(obj,tableName,isMerger){ 
 scanBarCodeEvOne(obj.value,tableName,isMerger,"tblGoods.BarCode");
 obj.value="";
 $(obj).focus();
 
}
function scanBarCodeEvOne(bsvalue,tableName,isMerger,search_favour){ 

 if(bsvalue == ""){
 return false;
 }
 tableName = $(".tagSel").attr("id");
 tableName = tableName.substring(0,tableName.indexOf("TableDIVTitle"));

 var vdata = eval( tableName+'Data');
 var barcodefield = ''; 
 var barcodeIndex = 0;
 var qtyfield=''; 
 
 var discountfield=''; 
 
 var pricefield=''; 
 var binputValue = ''; 
 for(var i=0;i<vdata.cols.length;i++){
 if(search_favour=="tblGoods.BarCode" && vdata.cols[i].name.indexOf('_BarCode')>0 && vdata.cols[i].name.indexOf(tableName+'_BarCode')==-1){ 
 barcodefield = vdata.cols[i].name;
 binputValue = vdata.cols[i].inputValue;
 barcodeIndex = vdata.cols[i].listOrder;
 if( vdata.cols[i].inputType!=2){
 alert("明细表中没有条码字段，请在列配置中启用条码");
 return false;
 } 
 }else{
 if(vdata.cols[i].name.indexOf(search_favour.replace('.','_'))>-1){
 barcodefield = vdata.cols[i].name;
 binputValue = vdata.cols[i].inputValue;
 barcodeIndex = vdata.cols[i].listOrder;
 }
 }
 if(vdata.cols[i].name.indexOf('_Qty')>0){
 qtyfield = vdata.cols[i].name;
 }
 if(vdata.cols[i].name.indexOf('_Price')>0){
 pricefield = vdata.cols[i].name;
 }
 if(vdata.cols[i].name.indexOf('_Discount')>0 && vdata.cols[i].inputType != -2){
 discountfield = vdata.cols[i].name;
 }
 }
 if(barcodefield==''){
 alert($(".tagSel").html()+"不能进行条码扫描");
 return false;
 }
 
 
 var lstr = bsvalue.substring(bsvalue.length-1);
 if(lstr=="+"){
 if(qtyfield==""){
 alert("不存在字段Qty");
 return false;
 }
 qstr = bsvalue.substring(0,bsvalue.length-1);
 var qobj = document.getElementsByName(qtyfield);
 qobj[curGridRowNum.curLine].value=qstr;
 $(qobj[curGridRowNum.curLine]).trigger("change"); 
 return true;
 }else if(lstr=="-"){
 if(pricefield==""){
 alert("不存在字段Price");
 return false;
 }
 qstr = bsvalue.substring(0,bsvalue.length-1);
 var qobj = document.getElementsByName(pricefield);
 
 if($(qobj[curGridRowNum.curLine]).attr("readOnly") !="readonly"){ 
 qobj[curGridRowNum.curLine].value=qstr;
 $(qobj[curGridRowNum.curLine]).trigger("change"); 
 }else{
 alert("单价字段不允许修改");
 return false;
 }
 return true;
 }else if(lstr=="*"){
 if(discountfield==""){
 alert("不存在折扣字段");
 return false;
 }
 qstr = bsvalue.substring(0,bsvalue.length-1);
 var qobj = document.getElementsByName(discountfield);
 
 if($(qobj[curGridRowNum.curLine]).attr("readOnly") !="readonly"){ 
 qobj[curGridRowNum.curLine].value=qstr;
 $(qobj[curGridRowNum.curLine]).trigger("change"); 
 }else{
 alert("折扣字段不允许修改");
 return false;
 }
 return true;
 }
 
 
 var barobj = document.getElementsByName(barcodefield);
 if(!popCondInput(binputValue,barobj[0])) {return false; }
 
 var param = {
 operation:"DropdownPopup",
 MOID:MOID,
 MOOP:"add",
 selectField:barcodefield,
 selectValue:bsvalue,
 tableName:tableName,
 fieldName:'GoodsCode',
 CompanyCode:$("#CompanyCode").val()
 };
 
 var tt = binputValue.split(":");
 if(tt[3]!=undefined){
 var autoParam=tt[3].split(";");
 if(autoParam.length>0){
 for(var i=0;i<autoParam.length;i++){
 if(autoParam[i].length>0){
 eval("param."+autoParam[i]+"=getValues(\""+autoParam[i]+"\")[curGridRowNum.curLine].value");
 }
 }
 }
 }

 for(var i=4;i<tt.length;i++){ 
 tt[i]=tt[i].substr(tt[i].indexOf("@")+1); 
 if(tt[i].indexOf(tt[0])==0){
 
 }else{ 
 eval("param."+tt[i]+"=getValue(\""+tt[i]+"\")");
 }
 }
 
 if(returnBarTitle == null || returnBarTitle_name != tableName){ 
 
 param.operation="PopupTitle"; 
 var btstr = jQuery.ajax({url: "/UtilServlet",
 async: false,
 type: "POST",
 data: param
 }).responseText; 
 
 returnBarTitle = eval("("+btstr+")"); 
 returnBarTitle_name = tableName;
 }
 
 param.search_favour="";
 if(search_favour != undefined && search_favour != ""){
 param.search_favour ='{"searchfield":"'+search_favour+'","searchtype":"checked"}';
 }else{ 
 for(var i=0;i<returnBarTitle.condition.length;i++){
 if(returnBarTitle.condition[i].fieldname.indexOf(".BarCode")> 0 ){
 param.search_favour ='{"searchfield":"'+returnBarTitle.condition[i].fieldname+'","searchtype":"checked"}';
 }
 }
 }
 if(param.search_favour==""){
 alert("商品弹出窗中未找到条码查询条件，请检查列配置中是否隐藏");
 return false;
 }
 param.operation="DropdownPopup"; 
 var btstr = jQuery.ajax({url: "/UtilServlet",
 async: false,
 type: "POST",
 data: param
 }).responseText; 
 var retDataObj = eval("("+btstr+")"); 
 if(retDataObj.length==0) {
 alert("找不到"+bsvalue+"对应的数据");
 return false;
 }
 retDataObj[0][1] = retDataObj[0][1].replace("&apos;","'");
 retDataObj[0][1] = retDataObj[0][1].replace("&quot;",'"');
 retDataObj[0][1] = retDataObj[0][1].replace("&#92;","\\");
 var retData = retDataObj[0][1].split("#;#");
 if(isMerger){ 
 
 var lineCount = document.getElementsByName(barcodefield).length;
 var sameLine = -1;
 for(var i = 0 ;i<lineCount;i++){ 
 var isSame = true;
 for(var j = 0 ;j<returnBarTitle.returnfields.length;j++){
 if(returnBarTitle.returnfields[j].compare=="false") continue;
 var fname = returnBarTitle.returnfields[j].fieldname;
 fname = fname.replaceAll("@TABLENAME",tableName);
 fname = fname.replaceAll("\\.","_");
 if(fname.indexOf(tableName)!=0){
 fname = tableName+"_"+fname;
 }
 var fdobj = document.getElementsByName(fname);
 if(fdobj.length < i +1){
 alert("字段"+returnBarTitle.returnfields[j].fieldname+"行数小于"+i);
 return false;
 }
 if(fdobj[i].value != retData[j]){
 isSame = false;
 break;
 }
 }
 if(isSame){
 sameLine = i;
 break;
 }
 }
 if(sameLine > -1){
 
 var fdobj = document.getElementsByName(qtyfield); 
 fdobj[sameLine].value =Number(fdobj[sameLine].value)+1;
 $(fdobj[sameLine]).trigger("change"); 
 curGridRowNum.set(tableName,sameLine);
 
 $("#"+tableName+'Table_tableData').scrollTop((sameLine+4)*($(fdobj[sameLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 $("#"+tableName+'Table_tableColumn').scrollTop((sameLine+4)*($(fdobj[sameLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 
 childFocus($(fdobj[sameLine]));
 return true;
 }
 }
 
 lastLine = -1;
 var fdobj = document.getElementsByName(tableName+"_GoodsCode");
 for(var i = 0;i<fdobj.length;i++){
 if(fdobj[i].value ==""){
 lastLine=i;
 break;
 }
 }
 fdobj = document.getElementsByName(barcodefield);
 if(lastLine == -1){
 
 var trObj=fdobj[lastLine-1].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click();
 fdobj = document.getElementsByName(barcodefield); 
 }
 if(lastLine == fdobj.length -1){ 
 var trObj=fdobj[lastLine].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click();
 }
 
 curGridRowNum.set(tableName,lastLine);
 
 $("#"+tableName+'Table_tableData').scrollTop((lastLine+4)*($(fdobj[lastLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 $("#"+tableName+'Table_tableColumn').scrollTop((lastLine+4)*($(fdobj[lastLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 
 childFocus($(fdobj[lastLine]));
 copyFields("dbClick",""+barcodeIndex);
 var retColNames= "";
 for(var j = 0 ;j<returnBarTitle.returnfields.length;j++){
 retColNames += returnBarTitle.returnfields[j].fieldname +";";
 }
 dataBackIn(false,retColNames,retDataObj[0][1],window); 
 
 
 var fdobj = document.getElementsByName(qtyfield); 
 fdobj[lastLine].value ='1';
 $(fdobj[lastLine]).trigger("change"); 
 
 return true;
 
}

var returnSeqTitle=null;
var returnSeqTitle_name=null;
function scanSeqEv(obj,tableName){ 
 scanSeqEvOne(obj.value,tableName);
 obj.value="";
 $(obj).focus();
}
function scanSeqEvOne(bsvalue,tableName){ 
 if(bsvalue == ""){
 return false;
 } 
 tableName = $(".tagSel").attr("id");
 tableName = tableName.substring(0,tableName.indexOf("TableDIVTitle"));

 var vdata = eval( tableName+'Data');
 var Seq_hidfield = ''; 
 var Seq_hidIndex = 0;
 var pricefield=''; 
 
 var discountfield = ''; 
 
 var qtyfield = ''; 
 var binputValue = ''; 
 for(var i=0;i<vdata.cols.length;i++){
 if(vdata.cols[i].name.indexOf('_Seq_hid')>0){
 Seq_hidfield = vdata.cols[i].name; 
 Seq_hidIndex = vdata.cols[i].listOrder;
 if( vdata.cols[i].inputType!=2){
 alert("请启用序列号属性");
 return false;
 } 
 }
 if(vdata.cols[i].name.indexOf('_Seq')>0 && vdata.cols[i].name.indexOf('_Seq_hid')==-1){
 
 binputValue = getInputVal(vdata.cols[i].name);
 }
 if(vdata.cols[i].name.indexOf('_Qty')>0){
 qtyfield = vdata.cols[i].name;
 }
 if(vdata.cols[i].name.indexOf('_Price')>0){
 pricefield = vdata.cols[i].name;
 }
 if(vdata.cols[i].name.indexOf('_Discount')>0 && vdata.cols[i].inputType != -2){
 discountfield = vdata.cols[i].name;
 }
 
 }
 
 
 if(Seq_hidfield==''){
 alert($(".tagSel").html()+"不能进行序列号扫描");
 return false;
 }
 
 
 fdobj = document.getElementsByName(tableName+"_Seq"); 
 for(var i=0;i<fdobj.length;i++){
 if(fdobj[i].value.indexOf(bsvalue+"~")>-1){
 alert("序列号已存在");
 return false;
 }
 }
 
 
 var lstr = bsvalue.substring(bsvalue.length-1);
 if(lstr=="-"){
 if(pricefield==""){
 alert("不存在字段Price");
 return false;
 }
 qstr = bsvalue.substring(0,bsvalue.length-1);
 var qobj = document.getElementsByName(pricefield);
 
 if($(qobj[curGridRowNum.curLine]).attr("readOnly") !="readonly"){ 
 qobj[curGridRowNum.curLine].value=qstr;
 $(qobj[curGridRowNum.curLine]).trigger("change"); 
 }else{
 alert("单价字段不允许修改");
 return false;
 }
 return true;
 }else if(lstr=="*"){
 if(discountfield==""){
 alert("不存在折扣字段");
 return false;
 }
 qstr = bsvalue.substring(0,bsvalue.length-1);
 var qobj = document.getElementsByName(discountfield);
 
 if($(qobj[curGridRowNum.curLine]).attr("readOnly") !="readonly"){ 
 qobj[curGridRowNum.curLine].value=qstr;
 $(qobj[curGridRowNum.curLine]).trigger("change"); 
 }else{
 alert("折扣字段不允许修改");
 return false;
 }
 return true;
 }
 
 
 var seqobj = document.getElementsByName(Seq_hidfield);
 if(!popCondInput(binputValue,seqobj[0])) {return false; }
 
 
 var param = {
 operation:"DropdownPopup",
 MOID:MOID,
 MOOP:"query",
 selectField:Seq_hidfield,
 selectValue:bsvalue,
 tableName:tableName,
 fieldName:'Seq',
 CompanyCode:$("#CompanyCode").val(),
 StockCode:$("#StockCode").val()
 };
 
 var tt = binputValue.split(":");
 var str="";
 for(var i=3;i<tt.length;i++){ 
 tt[i]=tt[i].substr(tt[i].indexOf("@")+1); 
 if(tt[i].indexOf(tt[0])==0){
 
 }else{
 eval("param."+tt[i]+"=getValue(\""+tt[i]+"\")"); 
 }
 }
 

 
 
 if(returnSeqTitle == null || returnSeqTitle_name != tableName){ 
 
 param.operation="PopupTitle"; 
 var btstr = jQuery.ajax({url: "/UtilServlet",
 async: false,
 type: "POST",
 data: param
 }).responseText; 
 
 returnSeqTitle = eval("("+btstr+")"); 
 returnSeqTitle_name = tableName;
 }
 
 param.search_favour="";
 for(var i=0;i<returnSeqTitle.condition.length;i++){
 if(returnSeqTitle.condition[i].fieldname.indexOf(".Seq")> 0 ){
 param.search_favour ='{"searchfield":"'+returnSeqTitle.condition[i].fieldname+'","searchtype":"checked"}';
 }
 }
 if(param.search_favour==""){
 alert("商品弹出窗中未找到序列号查询条件，请检查列配置中是否隐藏");
 return false;
 }
 param.operation="DropdownPopup"; 
 var btstr = jQuery.ajax({url: "/UtilServlet",
 async: false,
 type: "POST",
 data: param
 }).responseText; 
 var retDataObj = eval("("+btstr+")"); 
 if(retDataObj.length==0) {
 alert("序列号不存在");
 return false;
 }
 retDataObj[0][1] = retDataObj[0][1].replace("&apos;","'");
 retDataObj[0][1] = retDataObj[0][1].replace("&quot;",'"');
 retDataObj[0][1] = retDataObj[0][1].replace("&#92;","\\");
 var retData = retDataObj[0][1].split("#;#");
 
 var lineCount = document.getElementsByName(Seq_hidfield).length;
 var sameLine = -1;
 var seqVal="";
 for(var i = 0 ;i<lineCount;i++){ 
 var isSame = true;
 for(var j = 0 ;j<returnSeqTitle.returnfields.length;j++){ 
 if(returnSeqTitle.returnfields[j].fieldname.indexOf(".Seq") > 0) {seqVal =retData[j]; continue;}
 if(returnSeqTitle.returnfields[j].compare=="false") continue;
 var fname = returnSeqTitle.returnfields[j].fieldname;
 fname = fname.replaceAll("@TABLENAME",tableName);
 fname = fname.replaceAll("\\.","_");
 if(fname.indexOf(tableName)!=0){
 fname = tableName+"_"+fname;
 }
 var fdobj = document.getElementsByName(fname);
 if(fdobj.length < i +1){
 alert("字段"+returnSeqTitle.returnfields[j].fieldname+"行数小于"+i);
 return false;
 }
 if(fdobj[i].value != retData[j]){
 isSame = false;
 break;
 }
 }
 if(isSame){
 sameLine = i;
 break;
 }
 }
 if(sameLine > -1){
 
 
 
 var fdobj = document.getElementsByName(Seq_hidfield); 
 fdobj[sameLine].value =seqVal; 
 fdobj = document.getElementsByName(tableName+"_Seq"); 
 fdobj[sameLine].value =fdobj[sameLine].value+seqVal+"~"; 
 
 var seqQt = fdobj[sameLine].value.split("~").length -1 ;
 fdobj = document.getElementsByName(qtyfield); 
 fdobj[sameLine].value =seqQt; 
 $(fdobj[sameLine]).trigger("change"); 
 
 curGridRowNum.set(tableName,sameLine);
 
 $("#"+tableName+'Table_tableData').scrollTop((sameLine+4)*($(fdobj[sameLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 $("#"+tableName+'Table_tableColumn').scrollTop((sameLine+4)*($(fdobj[sameLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 childFocus($(fdobj[sameLine]));
 
 return true;
 }
 
 lastLine = -1;
 var fdobj = document.getElementsByName(tableName+"_GoodsCode");
 for(var i = 0;i<fdobj.length;i++){
 if(fdobj[i].value ==""){
 lastLine=i;
 break;
 }
 }
 fdobj = document.getElementsByName(pricefield);
 if(lastLine == -1){
 
 lastLine = fdobj.length; 
 var trObj=fdobj[lastLine-1].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click(); 
 }
 if(lastLine == fdobj.length -1){ 
 var trObj=fdobj[lastLine].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click();
 }
 fdobj = document.getElementsByName(Seq_hidfield); 
 
 curGridRowNum.set(tableName,lastLine);
 
 $("#"+tableName+'Table_tableData').scrollTop((lastLine+4)*($(fdobj[lastLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 $("#"+tableName+'Table_tableColumn').scrollTop((lastLine+4)*($(fdobj[lastLine]).height()+1)-$("#"+tableName+'Table_tableData').height());
 childFocus($(fdobj[lastLine]));
 copyFields("dbClick",""+Seq_hidIndex);
 var retColNames= "";
 for(var j = 0 ;j<returnSeqTitle.returnfields.length;j++){
 retColNames += returnSeqTitle.returnfields[j].fieldname +";";
 }
 dataBackIn(false,retColNames,retDataObj[0][1],window); 
 
 for(var j = 0 ;j<returnSeqTitle.returnfields.length;j++){
 var fname = returnSeqTitle.returnfields[j].fieldname;
 fname = fname.replaceAll("@TABLENAME",tableName);
 fname = fname.replaceAll("\\.","_");
 if(fname.indexOf(tableName)!=0){
 fname = tableName+"_"+fname;
 }
 var fdobj = document.getElementsByName(fname); 
 if(fdobj.length>lastLine){
 if(fname.indexOf("_Seq") > 0){ 
 fdobj[lastLine].value = retData[j]+"~";
 var fdobj = document.getElementsByName(fname+"_hid"); 
 fdobj[lastLine].value = retData[j];
 }
 }
 }
 
 
 var fdobj = document.getElementsByName(qtyfield); 
 fdobj[lastLine].value ='1';
 $(fdobj[lastLine]).trigger("change"); 
 $(fdobj[lastLine]).attr("readonly","true");
 
 return true;
 
}
function scanBarCodeHelp(type){
 if(type == "seq")
 asyncbox.alert('1、输入数字和“-”号并回车可修改单价<br/>2、输入数字和“*”号并回车可修改折扣','温馨提示'); 
 else
 asyncbox.alert('1、输入数字和“+”号并回车可修改数量<br/>2、输入数字和“-”号并回车可修改单价<br/>3、输入数字和“*”号并回车可修改折扣','温馨提示'); 
} 
function batchInput(){
 tableName = $(".tagSel").attr("id");
 tableName = tableName.substring(0,tableName.indexOf("TableDIVTitle"));
 
 var vdata = eval( tableName+'Data'); 
 var hasGoods = false;
 for(var i=0;i<vdata.cols.length;i++){
 if(vdata.cols[i].name.indexOf('_GoodsCode')>0){
 hasGoods = true;
 break; 
 }
 }
 if(!hasGoods){
 alert($(".tagSel").html()+"不能商品批量录入");
 return;
 }
 
 
 
 var param = {
 operation:"DropdownPopup",
 MOID:MOID,
 MOOP:"query",
 selectField:tableName+"_GoodsCode",
 selectValue:'',
 tableName:tableName,
 fieldName:'GoodsCode',
 CompanyCode:$("#CompanyCode").val()
 };
 
 if(returnBarTitle == null || returnBarTitle_name != tableName){ 
 
 param.operation="PopupTitle"; 
 var btstr = jQuery.ajax({url: "/UtilServlet",
 async: false,
 type: "POST",
 data: param
 }).responseText; 
 
 returnBarTitle = eval("("+btstr+")"); 
 returnBarTitle_name = tableName;
 }
 var constr = "";
 for(var i=0;i<returnBarTitle.condition.length;i++){
 var sed = "";
 if(returnBarTitle.condition[i].fieldname.indexOf(".BarCode")> 0 ){
 sed=" selected ";
 }
 
 for(var k = 0;k<vdata.cols.length;k++){
 if(vdata.cols[k].name==tableName+'_'+(returnBarTitle.condition[i].fieldname.replace(".","_"))){
 constr +="<option value='"+returnBarTitle.condition[i].fieldname+"' "+sed+">"+returnBarTitle.condition[i].displayname+"</option>"; 
 break;
 }
 } 
 }
 asyncbox.open({ id:'PopBatchInputdiv',
　　　 html:'<div style="text-align:center;"><div style="text-align: left;padding-left: 20px;"><p>'+
 '提示：请选择录入格式，分隔符可以是空格或，或；如果名称中包分隔符号则不能批量录入</p><p><select id="batchFieldsel">'+constr+'</select> +分隔符+数量(可为空)+分隔符+单价（可为空）</p></div>'
 + '<textarea name="batchstr" id="batchstr" style="width:400px;height:190px"></textarea>'+
 '<div style="text-align: left;padding-left: 20px;"><p><!--<input type="checkbox" id="batchMerger" checked /><label for="batchMerger">合并数量。</label> -->'+
 '可从excel复制,分隔符不能是中文字符</p></div></div>', 
　　　 width:430,
　　　 height:390,
 　 title:'商品批量录入',
 　　　 btnsbar:jQuery.btn.OKCANCEL, 
 　　　 callback : function(action){
 　　　　　if(action == 'ok'){
 　　　　　　　 search_favour = $("#batchFieldsel").val();
 
 isMerger = false;
 batchstr = $("#batchstr").val();
 
 batchLines = batchstr.split("\n"); 
 var batchObjs = new Array();
 for(var i=0;i<batchLines.length;i++){
 if(batchLines[i].trim()!=""){
 batchObjs[batchObjs.length] = batchLines[i].trim();
 }
 }
 if(batchObjs.length==0){
 return;
 }
 if(search_favour=="Seq"){
 
 if($("#CompanyCode").val()==""){
 alert("请先选择客户");
 return false;
 }
 
 var errorBatch = new Array();
 for(var i=0;i<batchObjs.length;i++){ 
 var blineObjs = batchObjs[i].split(/\s*,\s*|\s*;\s*|\s+/g);
 if(!scanSeqEvOne(blineObjs[0],tableName)){
 errorBatch[errorBatch.length]=batchObjs[i];
 }
 }
 if(errorBatch.length>0){
 asyncbox.error("有错误序列号，请修改后重新提交","提示");
 var errorStr = "";
 for(var i=0;i<errorBatch.length;i++){
 errorStr+=errorBatch[i]+"\r\n";
 }
 $("#batchstr").val(errorStr);
 return false;
 }
 }else{
 
 var fname = search_favour;
 fname = fname.replaceAll("@TABLENAME",tableName);
 fname = fname.replaceAll("\\.","_");
 if(fname.indexOf(tableName)!=0){
 fname = tableName+"_"+fname;
 } 
 var vdata = eval( tableName+'Data');
 for(var i=0;i<vdata.cols.length;i++){
 if(vdata.cols[i].name.indexOf(fname)>-1){
 var barobj = document.getElementsByName(fname);
 if(!popCondInput(vdata.cols[i].inputValue,barobj[0])) {return false; } 
 }
 } 
 
 var errorBatch = new Array();
 for(var i=0;i<batchObjs.length;i++){
 
 var blineObjs = batchObjs[i].split(/\s*,\s*|\s*;\s*|\s+/g);
 if(!scanBarCodeEvOne(blineObjs[0],tableName,isMerger,search_favour)){
 errorBatch[errorBatch.length]=batchObjs[i];
 }else{
 if(blineObjs.length>1){
 
 scanBarCodeEvOne(blineObjs[1]+"+",tableName,isMerger,search_favour);
 }
 if(blineObjs.length>2){
 
 scanBarCodeEvOne(blineObjs[2]+"-",tableName,isMerger,search_favour);
 }
 }
 }
 if(errorBatch.length>0){
 asyncbox.error("有错误商品，请修改后重新提交","提示");
 var errorStr = "";
 for(var i=0;i<errorBatch.length;i++){
 errorStr+=errorBatch[i]+"\r\n";
 }
 $("#batchstr").val(errorStr);
 return false;
 }
 }
 
 　　　　　}
 　　　}
　 });
 
}


function tuoZhan(){ 
 var keyId = $("#id").val();
 if($("#isCatalog").val()=="1"){
 alert('不能对父类授权');
 return false;
 }
 var urlstr = "/RoleQueryAction.do?moduleId="+keyId;
 asyncbox.open({id:'MainPopup',title:'权限分配',url:urlstr,width:800,height:410}); 
}

function bpStockSelect(){
 if($("#CompanyCode",document).val()==0){
 alert("请录入公司");
 return;
 }
 var urlstr = "/vm/classFunction/bpStockSelect.jsp?";
 width = document.documentElement.clientWidth ;
 height = document.documentElement.clientHeight ;
 openPop('bpStockSelect','库存搜索',urlstr,width,height,false,true)
 
}
