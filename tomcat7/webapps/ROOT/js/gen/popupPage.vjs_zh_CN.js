

function closeSubmit()
{
 
 if(typeof(detailPopup) != "undefined" && detailPopup != ""){
 gotoDetail(detailPopup,true);
 return;
 }
 if($("#fieldName",document).val()=="Seq"){
 
 
 var v = "";
 jQuery("input[name='varKeyId']").each(function(){
 if(this.checked){
 var rasa = returnCols.split(";");
 var rava = this.value.split(";");
 var rseq = "";
 for(var rsi=0;rsi<rasa.length;rsi++){
 if(rasa[rsi].indexOf(".Seq") > 0){
 rseq = rava[rsi];
 break;
 }
 }
 parent.returnSeq(returnCols,this.value,$(this).attr("compv"),tableName+"_GoodsCode",parent.curGridRowNum.curLine,tableName+"_Seq",rseq);
 }
 }); 
 window.parent.jQuery.close(pupupWin);
 return;
 }
 var boxValue = reloadkeyIds();
 if(boxValue.length==0){
 alert("请至少选择一条记录.") ;
 setIniFocus();
 return false ;
 }
 var items = boxValue.split("#|#") ;
 if(items[items.length-1]=='')
 items.length = items.length -1;
 var ids="";
 if(isChild){
 ids=hasChildids; 
 for(var i=0;i<items.length;i++){
 ids=ids+items[i]; 
 } 
 returnSelect(ids);

 window.close();
 }else if(popType=="multiSeleToRow"){ 
 for(var i=0;i<items.length;i++){
 ids=ids+items[i]+"#|#";
 } 
 returnSelect(ids);

 window.close();
 }else if(popType=="checkBox"){
 ids = varIds ; 
 
 
 var submitFlag=true;
 var intNum = 0 ;
 for(var i=0;i<items.length;i++){
 intNum++ ;
 if(items[i].length>0){
 if(!saveParentFlag){
 if(items[i].indexOf("@hasChild:")!=-1){
 alert("有子级，此类别不能提交");
 setIniFocus();
 submitFlag=false;
 break ;
 }
 }
 var strValue = items[i];
 if(items[i].indexOf("@#")!=-1)
 strValue = items[i].substring(items[i].indexOf("@#")+2,items[i].length) ;
 if(strValue!=";" && ids.indexOf(strValue)==-1){
 ids=ids+strValue+"#|#";
 }
 }
 }
 if(submitFlag){
 returnSelect(ids);
 }
 } else{
 for(var i=0;i<items.length;i++){
 ids=items[i];
 var idArrays=ids.split("#;#");
 var submitFlag=true;
 if(!saveParentFlag){
 if(idArrays[idArrays.length-1]=="@hasChild:"){
 alert("有子级，此类别不能提交");
 setIniFocus();
 submitFlag=false;
 }
 }
 if(submitFlag){ 
 returnSelect(ids);
 } 
 }
 }
}

function returnSelect(ids)
{
 ids = ids.substring(0,ids.length-3);
 if(isQuote!=""){
 exeQuotepopup(ids);
 }else{
 if(isMain =="true" || isMain=="false"){
 if(parent.frames['mainTreeFrame']){ 
 dataBackIn(isMain,returnCols,ids,parent.parent);
 window.parent.parent.jQuery.close(pupupWin);
 }else{
 dataBackIn(isMain,returnCols,ids,parent);
 window.parent.jQuery.close(pupupWin);
 }
 return;
 }
 
 if(parent.frames['mainTreeFrame']){ 
 var frameStr = "";
 
 ids = ids.replaceAll('\\\\','\\\\').replaceAll("'","\\'").replaceAll('"','\\"');
 if(typeof(parent.parent.frames["moddiFrame"])!="undefined"){
 if(typeof(eval('window.parent.parent.frames["topFrame"].exe'+pupupWin))!="undefined" && parentTableName==""){
 frameStr = '.frames["topFrame"]' ;
 }else{
 frameStr = '.frames["bottomFrame"]' ;
 }
 }
 var popDiv = $(".asyncbox_normal",parent.parent.document) ;
 if(popDiv.size()>1){
 var popId = popDiv.last().prev().attr("id");
 eval('parent.parent.jQuery.opener("'+popId+'")'+frameStr+'.exe'+pupupWin+'("'+ids+'")');
 }else{
 eval('window.parent.parent'+frameStr+'.exe'+pupupWin+'("'+ids+'");');
 }
 window.parent.parent.jQuery.close(pupupWin);
 }else{
 var frameStr = "";
 
 ids = ids.replace('\\','\\\\').replace("'","\\'").replace('"','\\"');
 if(typeof(parent.frames["moddiFrame"])!="undefined"){
 if(typeof(eval('window.parent.frames["topFrame"].exe'+pupupWin))!="undefined" && parentTableName==""){
 frameStr = '.frames["topFrame"]' ;
 }else{
 frameStr = '.frames["bottomFrame"]' ;
 }
 }
 var popDiv = $(".asyncbox_normal",parent.document) ;
 if(popDiv.size()>1){
 var popId = popDiv.last().prev().attr("id");
 eval('parent.jQuery.opener("'+popId+'")'+frameStr+'.exe'+pupupWin+'("'+ids+'")');
 }else{
 eval('window.parent'+frameStr+'.exe'+pupupWin+'("'+ids+'");');
 }
 window.parent.jQuery.close(pupupWin);
 }
 }
}
function moveRoot(){
 var frameStr = "";
 if(typeof(parent.frames["mainTreeFrame"])!="undefined"){
 frameStr = '.parent' ; 
 }
 var popDiv = $(".asyncbox_normal",parent.document) ;
 if(popDiv.size()>1){
 var popId = popDiv.last().prev().attr("id");
 eval('parent.jQuery.opener("'+popId+'")'+frameStr+'.exe'+pupupWin+'("ROOT")');
 }else{
 eval('window.parent'+frameStr+'.exe'+pupupWin+'("ROOT");');
 } 
}


function closeWindow(){
 if(pupupWin != ""){
 if(parent.frames['mainTreeFrame']){ 
 window.parent.parent.jQuery.close(pupupWin);
 }else{
 window.parent.jQuery.close(pupupWin);
 }
 }else{
 window.close();
 }
}

function addSelRow()
{ 
 var items=document.getElementsByName("varKeyId");
 var selRow=document.getElementById("tblSelTable").rows;
 var count=selRow.length;
 var flag=true;
 for(var i=0;i<items.length;i++){
 if(items[i].checked){
 for(var j=1;j<selRow.length;j++){
 if(items[i].value==selRow[j].cells[1].firstChild.value){
 flag=false;
 break;
 }
 }
 if(flag){
 index=items[i].parentNode.parentNode.cells[0].innerHTML;
 tableList.addRows(listgriddataSel.cols,listgriddataAll.rows[index-1],count)
 count=count+1;
 }
 flag=true;
 }
 }
}
function addClickRow(rowid){
 var selRow=document.getElementById("tblSelTable").rows;
 var items=document.getElementsByName("varKeyId");
 var keyVal = "";
 var flag=true;
 var count=selRow.length;
 if(window.event.srcElement.type != "checkbox"){
 for(var i=0;i<items.length;i++){ 
 items[i].checked=''; 
 if(i==rowid-1){
 items[i].checked="checked";
 keyVal = items[i].value ;
 }
 }
 }
 for(var j=1;j<selRow.length;j++){
 if(keyVal==selRow[j].cells[1].firstChild.value){
 flag=false;
 break;
 }
 }
 if(flag){
 tableList.addRows(listgriddataSel.cols,listgriddataAll.rows[rowid-1],count)
 count=count+1;
 }
 flag=true;
}

function openInputDate(obj)
{
 c.showMoreDay = false;
 c.show(obj);
}
function backSubmit()
{
 form.pageNo.value=0 ;
 form.parentCode.value=parentCode5;
 form.backType.value = "backSubmit" ;
 form.submit();
}
function backSubmit2()
{
 form.pageNo.value=0 ;
 form.parentCode.value=parentCode5;
 form.backType.value = "backSubmit" ;
 form.isRoot.value = "isRoot";
 form.submit();
}
function backMain()
{
 form.mainId.value="";
 form.mainPop.value="";
 form.backType.value="backMain" ;
 form.action="/UserFunctionAction.do?selectName="+mainPop;
 form.submit();
}
function resetSubmit()
{
 
 if(isMain =="true" || isMain=="false"){
 if(parent.frames['mainTreeFrame']){ 
 dataBackIn(isMain,returnCols,retpopvalue,parent.parent);
 window.parent.parent.jQuery.close(pupupWin);
 }else{
 parent.dataBackIn(isMain,returnCols,retpopvalue,parent);
 window.parent.jQuery.close(pupupWin);
 }
 }
 
 if(pupupWin != ""){
 if(parent.frames['mainTreeFrame']){ 
 var frameStr = "";
 if(typeof(parent.parent.frames["moddiFrame"])!="undefined"){
 if(typeof(eval('window.parent.parent.frames["topFrame"].exe'+pupupWin))!="undefined" && parentTableName==""){
 frameStr = '.frames["topFrame"]' ;
 }else{
 frameStr = '.frames["bottomFrame"]' ;
 }
 }
 var popDiv = $(".asyncbox_normal",parent.parent.document) ;
 if(popDiv.size()>1){
 var popId = popDiv.last().prev().attr("id");
 eval('parent.parent.jQuery.opener("'+popId+'")'+frameStr+'.exe'+pupupWin+'("'+retpopvalue+'")');
 }else{
 eval('window.parent.parent'+frameStr+'.exe'+pupupWin+'("'+retpopvalue+'");');
 }
 window.parent.parent.jQuery.close(pupupWin);
 }else{
 var frameStr = "";
 if(typeof(parent.frames["moddiFrame"])!="undefined"){
 if(typeof(eval('window.parent.frames["topFrame"].exe'+pupupWin))!="undefined" && parentTableName==""){
 frameStr = '.frames["topFrame"]' ;
 }else{
 frameStr = '.frames["bottomFrame"]' ;
 }
 }
 var popDiv = $(".asyncbox_normal",parent.document) ;
 if(popDiv.size()>1){
 var popId = popDiv.last().prev().attr("id");
 eval('parent.jQuery.opener("'+popId+'")'+frameStr+'.exe'+pupupWin+'("'+retpopvalue+'")');
 }else{
 eval('window.parent'+frameStr+'.exe'+pupupWin+'("'+retpopvalue+'");');
 }
 window.parent.jQuery.close(pupupWin);
 }
 }else{
 window.parent.returnValue =retpopvalue;
 window.close();
 }
}
function setIniFocus(){
 var showStatus = document.getElementsByTagName("input"); 
 for(var i=0;i<showStatus.length;i++){
 if(showStatus[i].type.toLowerCase() == "text" && !showStatus[i].readOnly && !showStatus[i].disabled){
 showStatus[i].focus();
 break;
 } 
 } 
}

function keyDown(obj){
 if(event.keyCode==13){ 
 var submitFlag=true; 
 if(!saveParentFlag){
 ids=obj.value;
 var idArrays=ids.split(";");
 if(ids.indexOf("@hasChild:")!=-1)
 {
 if(document.createEvent){
 var evt = document.createEvent("HTMLEvents"); 
 evt.initEvent("dblClick",false,false);
 obj.parentElement.parentElement.dispatchEvent(evt); 
 }else{
 obj.parentElement.parentElement.fireEvent('ondblclick');
 } 
 
 submitFlag=false;
 }
 }
 if(submitFlag){
 closeSubmit();
 }
 }
}
document.onkeydown=function(){
 if(event.keyCode == 27){
 closeWindow();
 }
};
function down(){ 
 if(event.ctrlKey&&event.keyCode==81){
 window.close();
 }else if(event.ctrlKey&&event.keyCode==79){
 submitBefore();
 }else if(event.ctrlKey&&event.keyCode==75){
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 }else if(event.ctrlKey&&event.keyCode==82){
 
 }else if(event.ctrlKey&&event.keyCode==76){
 
 
 
 }
}

function rigthMove(){
 var keyIds = document.getElementsByName("varKeyId") ;
 var num = 0 ;
 for(var i=0;i<keyIds.length;i++){
 var varKey = keyIds[i] ;
 if(varKey.checked){
 var keyValue = varKey.value ;
 if(existValue(keyValue)){
 var varValues = keyValue.split(";") ;
 addTr(keyValue,varValues[varValues.length-3],'') ;
 }
 num++ ;
 }
 } 
 if(num==0){
 alert("请至少选择一条左边的记录") ;
 }
}

function existValue(keyValue){
 var keyIds = document.getElementsByName("cutKeyId") ;
 for(var i=0;i<keyIds.length;i++){
 if(keyIds[i].value==keyValue){
 return false ;
 break ;
 }
 }
 return true ;
}
function setDefault()
{
 colConfigForm.operation.value="" ;
 colConfigForm.submit() ;
}

function colWidthSet(){
 document.getElementById("colWidthSetting").style.display="block" ; 
 document.getElementById("colNameSetting").style.display="none" ; 
}

function beforeUpdate(){
 var colSelects = "" ;
 for(var i=1;i<=varColWidth;i++){
 var obj = document.getElementById("tdWidth"+i) ;
 var fieldName = obj.name ;
 var colWidth = obj.value ;
 colSelects += fieldName+":"+colWidth+";" ;
 }
 document.getElementById("colSelect").value=colSelects ;
 colConfigForm.action = "/ColDisplayAction.do?operation=1" ;
 colConfigForm.submit() ;
}
function returnColConfig(){
 document.getElementById("colWidthSetting").style.display="none" ; 
 document.getElementById("colNameSetting").style.display="block" ; 
}

function setDefaultColWidth(){
 colConfigForm.action = "/ColDisplayAction.do?operation=6" ;
 colConfigForm.submit() ;
}


var fieldName ;
function openSelect(displayName,selectName,fieldName){
 window.fieldName = fieldName;
 var strURL = "/UserFunctionAction.do?&selectName="+selectName+"&operation=22&popupWin=DraftPopup&MOID="+MOID+"&MOOP=add&popPop=true&LinkType=@URL:&&displayName="+encodeURI(displayName) ;
 asyncbox.open({id:'DraftPopup',title:displayName,url:strURL,width:800,height:470});
}

function exeDraftPopup(str){
 if(typeof(str)!="undefined"){
 var fieldValue=str.split(";"); 
 document.getElementsByName(fieldName)[0].value=fieldValue[0];
 }
}


function openNewWin(varURL,width,height){
 if(width ==0){
 width=parent.document.documentElement.clientWidth;
 }
 if(height ==0){
 height=parent.document.documentElement.clientHeight;
 }
 if(parent.frames['mainTreeFrame']){ 
 parent.parent.openPop('PopMainOpdiv','',varURL,width,height,true,height==parent.document.documentElement.clientHeight); 
 }else{
 parent.openPop('PopMainOpdiv','',varURL,width,height,true,height==parent.document.documentElement.clientHeight); 
 }
}


function beforeSubmit(){
 document.forms[0].queryChannel.value='normal';
 if(mainPop=="" && typeof(document.form.pageNo)!="undefined"){
 document.form.pageNo.value = 1;
 }
 document.forms[0].submit();
}


function setColumnSet(str){
 var strURL = "/UtilServlet?operation=colconfig&tableName="+tableName+"&popupName="+strSelectName+"&reportName="+encodeURI($("#reportName").val())+"&colType=popup&colSelect="+str ;
 AjaxRequest(strURL);
 beforeSubmit() ;
}

function onDefaultCol(){
 
 var strURL = "/UtilServlet?operation=defaultConfig&tableName="+tableName+"&popupName="+strSelectName+"&colType=popup&reportName="+encodeURI($("#reportName").val()) ;
 AjaxRequest(strURL);
 beforeSubmit() ;
}

function reloadkeyIds()
{
 var v = "";
 jQuery("input[name='varKeyId']").each(function(){if(this.checked)v+=this.value+"#|#"});
 return v;

}

function getSelectRowValue(field) 
{
 var v = "";
 jQuery("input[name='varKeyId']").each(function(){if(this.checked)v+=jQuery(this.parentNode.parentNode).attr(field)});
 return v;
}