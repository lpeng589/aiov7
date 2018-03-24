var submitBefore = true ;
var checkList = new Array();
var childCheckList = new Array();
var childData=new Array();
var tabIsNulls=new Array();
var tabDiss=new Array();

var intswitch=true;
var digits=2;
var digitsDet=2;

function putValidateItem(name,title,type,fieldSysType,nullable,minLimit,maxLimit,isSC){
 checkList[checkList.length] = new checkItem(name,title,type,fieldSysType,nullable,minLimit,maxLimit,isSC);
}

function putChildValidateItem(name,title,type,fieldSysType,nullable,minLimit,maxLimit,hasDefault,defaultValue,isSC){ 
 childCheckList[childCheckList.length] = new childCheckItem(name,title,type,fieldSysType,nullable,minLimit,maxLimit,hasDefault,defaultValue,isSC);
}

function removeValidateItem(name){
 for(var i=0;i<checkList.length;i++){
 if(checkList[i].name == name)
 {
 checkList.splice(i,1);
 return true;
 }
 }
 return false;
}
function updateValidateItem(name,nullable){
 for(var i=0;i<childCheckList.length;i++){
 if(childCheckList[i].name == name)
 {
 childCheckList[i].nullable = nullable;
 return true;
 }
 }
 return false;
}

function checkItem(name,title,type,fieldSysType,nullable,minLimit,maxLimit,isSC){
 this.name = name;
 this.title = title;
 this.type = type;
 this.fieldSysType=fieldSysType;
 this.nullable = nullable;
 this.minLimit = parseInt(minLimit);
 this.maxLimit = parseInt(maxLimit);
 this.isSC = isSC ;
}
function childCheckItem(name,title,type,fieldSysType,nullable,minLimit,maxLimit,hasDefault,defaultValue,isSC){
 this.name = name;
 this.title = title;
 this.type = type;
 this.fieldSysType=fieldSysType;
 this.nullable = nullable;
 this.minLimit = parseInt(minLimit);
 this.maxLimit = parseInt(maxLimit);
 this.hasDefault = hasDefault;
 this.isSC = isSC ;
 this.defaultValue = defaultValue;
}

function sureSubmit(){
 return confirm("确定提交?"); 
}

function focusItem(curItem){
 if(curItem.type){
 if(curItem.type.toLowerCase()=="text"){
 curItem.focus();
 }else
 {
 if(curItem.type.toLowerCase()=="hidden"){
 var v = jQuery("input");
 for(var i = jQuery.inArray(curItem,v)+1;i<v.length;i++)
 {
 if(v[i].type.toLowerCase()=="text")
 {
 v[i].focus();
 break;
 }
 }
 }
 }
 }
}


function validateDigit(input,name,digit,isMain){
 if(digit>0){
 if(isMain){
 var inp = document.getElementsByName(input);
 if(getDecimalDigit(inp[0].value) > digit){
 alert(name+"小数位不能大于"+digit+"位")
 focusItem(input);
 return false;
 }
 }else{
 var inp = document.getElementsByName(input);
 for (var i = 0;i<inp.length;i++){
 if(getDecimalDigit(inp[i].value) > digit){
 alert(name+"小数位不能大于"+digit+"位")
 focusItem(input);
 return false;
 }
 }
 } 
 }else if (isMain){ 
 var inp = document.getElementsByName(input);
 if(getDecimalDigit(inp[0].value) > 2){
 alert(name+"小数位数不能大于2位")
 focusItem(input);
 return false;
 }
 }else {
 var inp = document.getElementsByName(input);
 for (var i = 0;i<inp.length;i++){
 if(getDecimalDigit(inp[i].value) > 2){
 alert(name+"小数位数不能大于2位")
 focusItem(input);
 return false;
 }
 }
 }
 return true;
}

function validate(form){
 
 for(var i=0;i<checkList.length;i++){
 var checkItem = checkList[i];
 var items = document.getElementsByName(checkItem.name);
 
 if(checkItem.type=="pic"||checkItem.type=="affix"){
 if(!checkItem.nullable&&items.length==0){
 alert(checkItem.title+"不能为空.");
 return false;
 }else{
 continue;
 } 
 }
 if(items.length==0)alert("DEBUG = '"+checkItem.name+"' element not exist!")
 var textValue = "";
 if(checkItem.type=="html"){
 ahtmls= document.getElementsByName(checkItem.name);
 if(ahtmls.length>0){
 textValue = eval(checkItem.name).html();
 }
 }
 if(checkItem.type=="checkBox"){
 if(!checkItem.nullable){
 var str="";
 for(var j=0;j<items.length;j++){
 if(items[j].checked){
 str+=items[j].value;
 }
 }
 if(str.length==0){
 alert(checkItem.title+"不能为空.")
 items[0].focus();
 return false;
 }
 }
 continue;
 }else{
 element = form.elements[checkItem.name];
 if(element.type !="file"){ 
 element.value = trimStr(element.value);
 }
 if(checkItem.type=="html"){
 element.value = encodeURIComponent(textValue);
 }
 
 if(checkItem.nullable && element.value.length==0){
 continue;
 } 
 if(!checkItem.nullable && element.value.length==0 && (typeof(form.button)=="undefined" || "saveDraft"!=form.button.value)){
 alert(checkItem.title+"不能为空.")
 focusItem(element);
 return false;
 }
 if(element.value.length==0 && (typeof(form.button)!="undefined" && "saveDraft" ==form.button.value )){
 continue; 
 }
 }
 
 if(checkItem.type=="int"){
 if(!isInt(element.value)){
 alert(checkItem.title+"必须是整数。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="numberChar"){
 if(!isInt(element.value)){
 alert(checkItem.title+"必须是整数。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="float"){
 if(!isFloat(element.value)){
 alert(checkItem.title+"必须是数字.")
 focusItem(element);
 return false;
 }else{
 element.value=parseFloat(element.value);
 } 
 }
 else if(checkItem.type=="double"){
 if(!isFloat(element.value)){
 alert(checkItem.title+"必须是数字.")
 focusItem(element);
 return false;
 }else{
 if(element.value.length==0)element.value=0;
 element.value=parseFloat(element.value);
 }
 }else if(checkItem.type=="date"){
 if(!isDate(element.value)){
 alert(checkItem.title+"格式不正确.")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="time"){
 if(!isTime(element.value)){
 alert(checkItem.title+"格式不正确.")
 focusItem(element);
 return false;
 }
 
 }else if(checkItem.type=="datetime"){
 if(!isDatetime(element.value)){
 alert(checkItem.title+"必须是日期时间格式(如：2008-01-02 12:20:25)。")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="ip"){
 if(!isIp(element.value)){
 alert(checkItem.title+"必须是IP.")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="en"){
 if(!isEn(element.value)){
 alert(checkItem.title+"必须由数字或者字母组成。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="title"){
 if(!isTitle(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="password"){
 if(!isPassword(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="html"){
 element.value = textValue;
 if(!isAny(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="any"){
 if(!isAny(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="mail"){
 if(!isMail(element.value)){
 alert(checkItem.title+"格式不正确。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="tel"){
 if(!isTel(element.value)){
 alert(checkItem.title+"格式不正确。\n(正确的电话格式如：0755-88888888)")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="mobile"){
 if(!isMobile(element.value)){
 alert(checkItem.title+"格式不正确。\n(正确的手机号码格式如：13500000000)")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="phone"){
 if(!(isMobile(element.value)||isTel(element.value))){
 alert(checkItem.title+"格式不正确。\n(正确的手机号码格式如：13500000000)\n(正确的电话格式如：0755-88888888)")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="zip"){
 if(!isZip(element.value)){
 alert(checkItem.title+"格式不正确。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="url"){
 if(!isUrl(element.value)){
 alert(checkItem.title+"格式不正确。")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="pic"){
 }else{
 alert(checkItem.title+"的数据类型无法解析.")
 focusItem(element);
 return false;
 }

 if(checkItem.type=="int" || checkItem.type=="float"|| checkItem.type=="double"){
 if(parseFloat(element.value)>checkItem.maxLimit)
 {
 alert(checkItem.title+"的值不能大于"+checkItem.maxLimit+"。")
 focusItem(element);
 return false;
 }
 if(parseFloat(element.value)<checkItem.minLimit)
 {
 alert(checkItem.title+"的值不能小于"+checkItem.minLimit+"。")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="password"){
 if (getStringLength(element.value)>checkItem.maxLimit){
 alert(checkItem.title+"的长度不能超过"+checkItem.maxLimit+"。")
 focusItem(element);
 return false;
 }
 if (getStringLength(element.value)<checkItem.minLimit){
 alert(checkItem.title+"的长度不能短于"+checkItem.minLimit+"。")
 focusItem(element);
 return false;
 }
 }else if(element.value.indexOf('zh_CN:') < 0 && checkItem.type!="html"){
 if (getStringLength(element.value)>checkItem.maxLimit)
 {
 alert(checkItem.title+"的长度不能超过"+checkItem.maxLimit+"。\n (一个中文字符长度为2,一个英文字符长度为1)")
 focusItem(element);
 return false;
 }
 if (getStringLength(element.value)<checkItem.minLimit)
 {
 alert(checkItem.title+"的长度不能短于"+checkItem.minLimit+"。\n (一个中文字符长度为2,一个英文字符长度为1)")
 focusItem(element);
 return false;
 }
 }
 
 }
 
 for(var l=0;l<childData.length;l++){
 var tabIsNull=tabIsNulls[l];
 var tabDis=tabDiss[l];
 var childMaxRow =0 ;
 var delRow=-1;
 var lastObj;
 for(var i=0;i<childCheckList.length;i++){
 var checkItem = childCheckList[i];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 if(items.length==0||table!=childData[l]||(checkItem.type=="double"&&checkItem.fieldSysType!="RowMarker")||(checkItem.type=="int"&&checkItem.fieldSysType!="RowMarker"))continue;
 if(!checkItem.hasDefault){
 for(var j =0;j<items.length;j++){
 if(items[j].value !=""){
 if(j+1>childMaxRow)childMaxRow=j+1;
 if(j>delRow)delRow=j;
 
 }
 }
 }else{
 for(var j =0;j<items.length;j++){
 var dv = checkItem.defaultValue;
 if(dv.indexOf("Sess_")==0) dv = eval(dv);
 if(items[j].value !="" && items[j].value != dv){
 if(j+1>childMaxRow) { 
 childMaxRow = j+1;
 }else if(j == items.length-1){
 childMaxRow = j+1;
 }
 if(j>delRow)delRow=j;
 } 
 }
 }
 lastObj=items;
 }
 
 
 if(childMaxRow==0&&tabIsNull==1&&"saveDraft"!=form.button.value){
 alert(tabDis+"至少录入一条记录");
 return false;
 }
 
 if(lastObj!=null&&lastObj!="undefined"&&childMaxRow>0){
 for(var i=0;i<lastObj.length;i++){
 if(i>delRow){
 
 if(lastObj[i].parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix!=null && lastObj[i].parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix!="undefined")
 lastObj[i].parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.fix.deleteRow(i+1,true);
 else
 lastObj[i].parentNode.parentNode.cells[1].click();
 }
 }
 }
 
 for(var i=0;childMaxRow >0 && i<childCheckList.length;i++){
 var checkItem = childCheckList[i];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 if(items.length==0||table!=childData[l])continue;
 for(var j =0;j<childMaxRow;j++){
 element = items[j];
 element.value = trimStr(element.value);
 if(checkItem.nullable && element.value.length==0){
 continue;
 }
 if(!checkItem.nullable && element.value.length==0 && (typeof(form.button)=="undefined" || "saveDraft"!=form.button.value)){
 alert(checkItem.title+"不能为空.")
 focusItem(element);
 return false;
 }
 if(element.value.length==0 && (typeof(form.button)!="undefined" && "saveDraft" ==form.button.value )){
 continue; 
 }
 
 
 
 
 
 
 
 
 if(checkItem.type=="int"){
 if(!isInt(element.value)){
 alert(checkItem.title+"必须是整数。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="numberChar"){
 if(!isInt(element.value)){
 alert(checkItem.title+"必须是整数。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="float"){
 if(!isFloat(element.value)){
 alert(checkItem.title+"必须是数字.")
 focusItem(element);
 return false;
 } 
 }
 else if(checkItem.type=="double"){
 if(!isFloat(element.value)){
 alert(checkItem.title+"必须是数字.")
 focusItem(element);
 return false;
 } 
 }else if(checkItem.type=="date"){
 if(!isDate(element.value)){
 alert(checkItem.title+"格式不正确.")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="time"){
 if(!isTime(element.value)){
 alert(checkItem.title+"格式不正确.")
 focusItem(element);
 return false;
 }
 
 }else if(checkItem.type=="datetime"){
 if(!isDatetime(element.value)){
 alert(checkItem.title+"必须是日期时间格式(如：2008-01-02 12:20:25)。")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="ip"){
 if(!isIp(element.value)){
 alert(checkItem.title+"必须是IP.")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="en"){
 if(!isEn(element.value)){
 alert(checkItem.title+"必须由数字或者字母组成。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="title"){
 if(!isTitle(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="password"){
 if(!isPassword(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="html"){
 if(!isAny(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="any"){
 if(!isAny(element.value)){
 alert(checkItem.title+"含有非法字符。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="mail"){
 if(!isMail(element.value)){
 alert(checkItem.title+"格式不正确。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="tel"){
 if(!isTel(element.value)){
 alert(checkItem.title+"格式不正确。\n(正确的电话格式如：0755-88888888)")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="mobile"){
 if(!isMobile(element.value)){
 alert(checkItem.title+"格式不正确。\n(正确的手机号码格式如：13500000000)")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="phone"){
 if(!(isMobile(element.value)||isTel(element.value))){
 alert(checkItem.title+"格式不正确。\n(正确的手机号码格式如：13500000000)\n(正确的电话格式如：0755-88888888)")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="zip"){
 if(!isZip(element.value)){
 alert(checkItem.title+"格式不正确。")
 focusItem(element);
 return false;
 }
 }
 else if(checkItem.type=="url"){
 if(!isUrl(element.value)){
 alert(checkItem.title+"格式不正确。")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="pic"){
 }else{
 alert(checkItem.title+"的数据类型无法解析.")
 focusItem(element);
 return false;
 }
 
 if(checkItem.type=="int" || checkItem.type=="float"|| checkItem.type=="double"){
 if(parseFloat(element.value)>checkItem.maxLimit)
 {
 alert(checkItem.title+"的值不能大于"+checkItem.maxLimit+"。")
 focusItem(element);
 return false;
 }
 if(parseFloat(element.value)<checkItem.minLimit)
 {
 alert(checkItem.title+"的值不能小于"+checkItem.minLimit+"。")
 focusItem(element);
 return false;
 }
 }else if(checkItem.type=="password"){
 if (getStringLength(element.value)>checkItem.maxLimit){
 alert(checkItem.title+"的长度不能超过"+checkItem.maxLimit+"。")
 focusItem(element);
 return false;
 }
 if (getStringLength(element.value)<checkItem.minLimit){
 alert(checkItem.title+"的长度不能短于"+checkItem.minLimit+"。")
 focusItem(element);
 return false;
 }
 }else if(element.value.indexOf('zh_CN:') < 0){
 
 if (getStringLength(element.value)>checkItem.maxLimit)
 {
 alert(checkItem.title+"的长度不能超过"+checkItem.maxLimit+"。\n (一个中文字符长度为2,一个英文字符长度为1)")
 focusItem(element);
 return false;
 }
 
 if (getStringLength(element.value)<checkItem.minLimit)
 {
 alert(checkItem.title+"的长度不能短于"+checkItem.minLimit+"。\n (一个中文字符长度为2,一个英文字符长度为1)")
 focusItem(element);
 return false;
 }
 }
 }
 }
 }
 if(typeof(top.jblockUI)!="undefined" && submitBefore){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 } 
 return true;
} 

 
function compareDate(startDatetime,endDatetime,isNullable,interval){
 var sdStr = toDateString(startDatetime).split(",");
 var edStr = toDateString(endDatetime).split(",");
 var sd,ed;
 if (startDatetime.length == 17)
 {
 var sh = startDatetime.substr(9,2);
 var sm = startDatetime.substr(12,2);
 var ss = startDatetime.substr(15,2);
 sd = new Date(sdStr[0],sdStr[1],sdStr[2], sh, sm, ss);
 var eh = endDatetime.substr(9,2);
 var em = endDatetime.substr(12,2);
 var es = endDatetime.substr(15,2);
 ed = new Date(edStr[0],edStr[1],edStr[2], eh, em, es);
 }
 else
 {
 sd = new Date(sdStr[0],sdStr[1],sdStr[2]);
 ed = new Date(edStr[0],edStr[1],edStr[2]);
 }
 var diff = Date.parse(ed.toString()) - Date.parse(sd.toString());
 var days = diff/(24*60*60*1000);

 if (!isNullable){
 if (isNaN(days)){
 alert("请选择起始日期与结束日期。");
 return false;
 }
 }
 if (diff < 0){
 alert("起始日期必须小于或等于结束日期.");
 return false;
 }else{
 if (interval || interval!=null){
 if (days > interval){
 alert("起始日期与结束日期的间隔天数必须小于或等于 " + interval + " ");
 return false;
 }
 }
 }
 return true;
}

function popCondInput(tts,vars){
 var tt =tts.split(":"); 
 var headCount = 1 ;
 if(vars.parentNode.parentNode.parentNode.parentNode.tHead.rows.length>1){
 headCount = 2 ;
 }
 for(var i=3;i<tt.length;i++)
 { 
 var isNull=tt[i].charAt(0);
 var fieldDisplay=tt[i].substring(1,tt[i].indexOf("@"));
 var fieldName=tt[i].substr(tt[i].indexOf("@")+1);
 if(isNull=="1"){
 var obj=getValues(fieldName);
 if(fieldName.indexOf(tt[0])==0){
 if(obj[parseInt(vars.parentNode.parentNode.rowIndex)-headCount].value.length==0){
 alert("请先输入 "+fieldDisplay);
 focusItem(obj[parseInt(vars.parentNode.parentNode.rowIndex)-headCount]);
 if ( window.event.stopPropagation )
 window.event.stopPropagation();
 else
 window.event.cancelBubble = true;
 return false;
 } 
 }else{
 if(obj[0].value.length==0){
 alert("请先输入 "+fieldDisplay);
 focusItem(obj[0]); 
 if ( window.event.stopPropagation )
 window.event.stopPropagation();
 else
 window.event.cancelBubble = true;
 return false;
 }
 }
 }
 }
 return true;
}

function getValues(strName){
 var strObjs = document.getElementsByName(strName);
 if(strObjs.length==0){
 alert(strName+" 不存在");
 }else{
 return strObjs;
 }
}


function getCValueEC(strName){
 if($("#"+strName).size()==0){
 alert(strName+" 不存在");
 return "";
 }else{
 return $("#"+strName).val().replace('&','&amp;');
 }
}

function getValue(strName){
 if($("#"+strName).size()==0){
 alert(strName+" 不存在");
 return "";
 }else{
 return $("#"+strName).val();
 }
}
function popMainInput(tts){
 var tt =tts.split(":"); 
 for(var i=0;i<tt.length;i++)
 { 
 var isNull=tt[i].charAt(0);
 var fieldDisplay=tt[i].substring(1,tt[i].indexOf("@"));
 var fieldName=tt[i].substr(tt[i].indexOf("@")+1);
 if(isNull=="1"){
 var obj=document.getElementById(fieldName);
 if(obj.value.length==0){
 alert("请先输入 "+fieldDisplay);
 focusItem(obj);
 return false;
 }
 }
 }
 return true;
}

