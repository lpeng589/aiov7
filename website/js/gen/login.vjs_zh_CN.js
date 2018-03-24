function setCookie(name,value,hours,path){
 var name = escape(name);
 var value = escape(value);
 var expires = new Date();
 expires.setTime(expires.getTime() + hours*3600000);
 path = path == "" ? "" : ";path=" + path;
 _expires = (typeof hours) == "string" ? "" : ";expires=" + expires.toUTCString();
 document.cookie = name + "=" + value + _expires + path;
}
function getCookieValue(name){
 var name = escape(name);
 var allcookies = document.cookie;
 name += "=";
 var pos = allcookies.indexOf(name); 
 if (pos != -1){
 var start = pos + name.length;
 var end = allcookies.indexOf(";",start);
 if (end == -1) end = allcookies.length;
 var value = allcookies.substring(start,end);
 return unescape(value);
 } 
 else return "";
}
function deleteCookie(name,path){
 var name = escape(name);
 var expires = new Date(0);
 path = path == "" ? "" : ";path=" + path;
 document.cookie = name + "="+ ";expires=" + expires.toUTCString() + path;
}

function bodyloadDemo(){
 jQuery.get('/UtilServlet?operation=queryAllName',function(response){
 var EmployeeColumn=document.getElementById("EmployeeColumn");
 var value = response;
 if(value!=""&&value!="no response text"){ 
 EmployeeColumn.innerHTML=value;
 } 
 }); 
}

function bodyload(){ 
 var name = getCookieValue('userName');
 document.forms[0].name.value= name;
 if(name != ""){ 
 document.forms[0].password.focus(); 
 }
 
 if(typeof(document.forms[0].remainpass) != "undefined"){ 
 var password = getCookieValue('password');
 document.forms[0].password.value= password;
 if(password != ""){ 
 document.forms[0].remainpass.checked = true;
 }
 }
}
function changeUserName(obj){
 if(typeof(document.forms[0].remainpass) != "undefined"){ 
 var name = getCookieValue('userName');
 var password = getCookieValue('password');
 if(name == obj.value){
 document.forms[0].password.value= password;
 }else{
 document.forms[0].password.value= "";
 }
 }
}

function bodyloadMobile(){ 
 var name = getCookieValue('userName');
 document.forms[0].name.value= name;
 
 if(typeof(document.forms[0].remainpass) != "undefined"){
 var password = getCookieValue('password');
 document.forms[0].password.value= password;
 if(password != ""){ 
 document.forms[0].remainpass.checked = true;
 pwdFocus();
 return;
 }
 }
 pwdBlur();
}
function pwdFocus(){
 passwordFocus(); 
 document.getElementById("password2").style.cssText="display:none";
 document.getElementById("password").style.cssText="display:inline";
 document.getElementById("password").focus();
 document.getElementById("password").focus();
 
}
function pwdBlur(){
 if(document.getElementById("password").value.length==0){
 document.getElementById("password2").style.cssText="display:inline";
 document.getElementById("password").style.cssText="display:none";
 } 
}


var response ="";
function AjaxRequest(path) {
 path=path+"&time="+(new Date()).getTime(); 
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
 }else{
 response = "no response text" ;
 }
 }
 };
 xmlHttp.open("get", path, false);
 xmlHttp.send();
}

function windsMax(){
 window.moveTo(0,0);
 window.resizeTo(screen.availWidth,screen.availHeight);
 document.getElementById("screenWidth").value = $(document).width();
}

var isSubmit=false;
function setFocus()
{
 valLoginPwd();
 if(LoginForm.name.value.length>0){
 LoginForm.password.focus() ;
 
 }else{
 LoginForm.name.focus();
 }
 jQuery("input").attr("disableautocomplete","disableautocomplete").attr("autocomplete","off");
}

function valLoginPwd(){
 AjaxRequest('/UtilServlet?operation=valpwd');
 var value = response;
 if(value!="no response text"){
 if(value == "true"){
 
 }
 } 
}
function keyDownOp()
{ 
 if(event.keyCode==13)
 {
 if(!isSubmit)
 {
 LoginForm.password.focus();
 isSubmit=true;
 }else{
 isSubmit=false;
 formSubmit(); 
 }
 
 }
}
function passwordFocus()
{
 isSubmit=true;
}

function setLocStyle(){
 var loginName = document.LoginForm.name.value; 
 AjaxRequest( '/UtilServlet?operation=getUserLocStyle&name='+loginName) ;
 var value = response; 
 if(value!="no response text"){ 
 defLoc=trim(value.substring(value.indexOf("<defLoc>")+"<defLoc>".length,value.indexOf("</defLoc>")));
 defStyle=trim(value.substring(value.indexOf("<defStyle>")+"<defStyle>".length,value.indexOf("</defStyle>")));
 if(defLoc != ""){
 document.LoginForm.loc.value = defLoc;
 }
 if(defStyle != ""){
 document.LoginForm.style.value = defStyle;
 }
 }
}

var userKeyId2 ='';
var aObject;
var bObject;
var DevicePath;
var userStr1 = '';
var userStr2 = '';
function formSubmit()
{
 document.getElementById("po-psw").style.display = "none";
 var loginName = document.LoginForm.name.value; 
 var loc = document.LoginForm.loc.value ;
 if(loginName!=""){
 
 setCookie("userName",loginName,10000,"/");
 if(typeof(document.LoginForm.remainpass) != "undefined"){
 if(document.getElementById("remainpass").checked){
 if(document.LoginForm.password.value.length!=32)
 setCookie("password",hex_md5(document.LoginForm.password.value),10000,"/");
 else
 setCookie("password",document.LoginForm.password.value,10000,"/");
 }else{
 deleteCookie("password","/");
 
 }
 }
 AjaxRequest( '/UtilServlet?operation=getUserInfo&name='+encodeURI(loginName)) ;
 var value = response; 
 if(value!="no response text"){ 
 userKeyId2 = ""; 
 userStr1=trim(value.substring(value.indexOf("<userStr1>")+"<userStr1>".length,value.indexOf("</userStr1>")));
 if(userStr1.length>0){ 
 try{
 var macObj ;
 var mimetype = navigator.mimeTypes["application/np-print"];
 if(mimetype){
 if(mimetype.enabledPlugin){
 macObj = document.getElementById("plugin");
 }
 }else{
 macObj = new ActiveXObject("KoronCom.MACAddress");
 }
 var str1 = macObj.A1();
 fd = "";
 us = userStr1.split(",");
 str1s = str1.split(",");
 for(jj=0;jj<us.length;jj++){
 for(jj2=0;jj2<str1s.length;jj2++){
 if(str1s[jj2] ==us[jj]){
 fd = us[jj];
 break;
 }
 }
 }
 
 if(fd == ""){
 alert(document.getElementById("name").value+"您只能在限定的电脑上使用本系统");
 document.getElementById("name").focus();
 document.getElementById("name").value="";
 
 return;
 }else{
 document.getElementById("strM1").value=fd ;
 var userStr2 = trim(value.substring(value.indexOf("<userStr2>")+"<userStr2>".length,value.indexOf("</userStr2>")));
 str2 = macObj.A2(fd,userStr2);
 document.getElementById("strM2").value= str2; 
 }
 }catch (e) 
 {
 alert("您本机可能未正确安装安全控件");
 document.getElementById("name").focus();
 document.getElementById("name").value="";
 
 return;
 }
 }
 userKeyId=trim(value.substring(value.indexOf("<userKeyId>")+"<userKeyId>".length,value.indexOf("</userKeyId>")));
 if(userKeyId != ""){
 try
 {
 userKeyId2=trim(value.substring(value.indexOf("<userKeyId2>")+"<userKeyId2>".length,value.indexOf("</userKeyId2>")));
 
 if(navigator.userAgent.indexOf("MSIE")>0 && !navigator.userAgent.indexOf("opera") > -1)
 {
 aObject = new ActiveXObject("Syunew6A.s_simnew6");
 }else{
 aObject = document.getElementById('s_simnew61');
 }
 
 DevicePath = aObject.FindPort(0);
 if( aObject.LastError!= 0 )
 {
 window.alert ("请插入您的安全U盾");
 document.getElementById("name").focus();
 
 return ;
 }
 rUserKeyId=toHex(aObject.GetID_1(DevicePath))+toHex(aObject.GetID_2(DevicePath));
 if( aObject.LastError!= 0 )
 {
 window.alert("读取安全U盾编号错误:" +aObject.LastError.toString());
 document.getElementById("name").focus();
 
 return ;
 }
 if(userKeyId.toUpperCase() != rUserKeyId.toUpperCase()){
 window.alert(document.getElementById("name").value+ " 您插入的安全U盾与您的编号不一致");
 document.getElementById("name").focus();
 
 return ;
 }
 
 }
 catch (e) 
 { 
 alert("您本机可能未正确安装安全控件");
 document.getElementById("name").focus();
 
 return;
 }
 }
 } 
 }
 if(userKeyId2 != ''){
 LoginForm.ukId.value=toHex(aObject.GetID_1(DevicePath))+toHex(aObject.GetID_2(DevicePath));
 if( aObject.LastError!= 0 )
 {
 window.alert("读取安全U盾编号错误:" +aObject.LastError.toString());
 document.getElementById("name").focus();
 return ;
 } 
 LoginForm.clientKey.value=aObject.EncString(userKeyId2,DevicePath);
 if( aObject.LastError!= 0 )
 {
 window.alert("安全U盾执行错误:" +aObject.LastError.toString());
 document.getElementById("name").focus();
 return ;
 }
 }
 AjaxRequest('/UtilServlet?operation=sunCompany&name='+encodeURI(encodeURI(loginName)));
 if(response != "" && response != "no response text"){
 alert(response);
 document.getElementById("name").select();
 return false ;
 }
 if(document.getElementById("password").value.length!=32)
 document.getElementById("password").value=hex_md5(document.getElementById("password").value);
 LoginForm.submit();
}

function mobiteSubmit()
{



 var loginName = document.LoginForm.name.value; 
 var loc = document.LoginForm.loc.value ;
 if(loginName!=""){
 
 setCookie("userName",loginName,10000,"/");
 if(typeof(document.LoginForm.remainpass) != "undefined"){
 if(document.getElementById("remainpass").checked){
 setCookie("password",document.LoginForm.password.value,10000,"/");
 }else{
 deleteCookie("password","/");
 
 }
 }
 
 AjaxRequest( '/UtilServlet?operation=getUserInfo&name='+loginName) ;
 var value = response; 
 if(value!="no response text"){ 
 userKeyId2 = ""; 
 userStr1=trim(value.substring(value.indexOf("<userStr1>")+"<userStr1>".length,value.indexOf("</userStr1>")));
 if(userStr1.length>0){ 
 alert("启用MAC地址绑定的帐户不允许从移动终端登陆");
 return; 
 }
 userKeyId=trim(value.substring(value.indexOf("<userKeyId>")+"<userKeyId>".length,value.indexOf("</userKeyId>")));
 if(userKeyId != ""){
 alert("启用安全U盾的帐户不允许从移动终端登陆");
 return; 
 }
 } 
 }
 alert(loginName)
 AjaxRequest('/UtilServlet?operation=sunCompany&name='+encodeURI(encodeURI(loginName)));
 if(response != "no response text"){
 alert(value);
 return false ;
 }
 LoginForm.submit();
}

var digitArray = new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');

function toHex( n ) {

 var result = ''
 var start = true;

 for ( var i=32; i>0; ) {
 i -= 4;
 var digit = ( n >> i ) & 0xf;

 if (!start || digit != 0) {
 start = false;
 result += digitArray[digit];
 }
 }

 return ( result == '' ? '0' : result );
}
function trim(str){ 
 return str.replace(/(^\s*)|(\s*$)/g, "");
}

function changeLoc (){
 window.location.href = "/LocaleServlet?LOCALE="+document.LoginForm.loc.value; 
}
function changeremainpass(){
 rp = document.getElementById("remainpass");
 rp.checked = !rp.checked;
}

var hexcase = 0; 
var b64pad = ""; 
var chrsz = 8; 
 

function hex_md5(s){ return binl2hex(core_md5(str2binl(s), s.length * chrsz));}
function b64_md5(s){ return binl2b64(core_md5(str2binl(s), s.length * chrsz));}
function hex_hmac_md5(key, data) { return binl2hex(core_hmac_md5(key, data)); }
function b64_hmac_md5(key, data) { return binl2b64(core_hmac_md5(key, data)); }
 

function calcMD5(s){ return binl2hex(core_md5(str2binl(s), s.length * chrsz));}
 

function md5_vm_test()
{
 return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72";
}
 

function core_md5(x, len)
{
 
 x[len >> 5] |= 0x80 << ((len) % 32);
 x[(((len + 64) >>> 9) << 4) + 14] = len;
 
 var a = 1732584193;
 var b = -271733879;
 var c = -1732584194;
 var d = 271733878;
 
 for(var i = 0; i < x.length; i += 16)
 {
 var olda = a;
 var oldb = b;
 var oldc = c;
 var oldd = d;
 
 a = md5_ff(a, b, c, d, x[i+ 0], 7 , -680876936);
 d = md5_ff(d, a, b, c, x[i+ 1], 12, -389564586);
 c = md5_ff(c, d, a, b, x[i+ 2], 17, 606105819);
 b = md5_ff(b, c, d, a, x[i+ 3], 22, -1044525330);
 a = md5_ff(a, b, c, d, x[i+ 4], 7 , -176418897);
 d = md5_ff(d, a, b, c, x[i+ 5], 12, 1200080426);
 c = md5_ff(c, d, a, b, x[i+ 6], 17, -1473231341);
 b = md5_ff(b, c, d, a, x[i+ 7], 22, -45705983);
 a = md5_ff(a, b, c, d, x[i+ 8], 7 , 1770035416);
 d = md5_ff(d, a, b, c, x[i+ 9], 12, -1958414417);
 c = md5_ff(c, d, a, b, x[i+10], 17, -42063);
 b = md5_ff(b, c, d, a, x[i+11], 22, -1990404162);
 a = md5_ff(a, b, c, d, x[i+12], 7 , 1804603682);
 d = md5_ff(d, a, b, c, x[i+13], 12, -40341101);
 c = md5_ff(c, d, a, b, x[i+14], 17, -1502002290);
 b = md5_ff(b, c, d, a, x[i+15], 22, 1236535329);
 
 a = md5_gg(a, b, c, d, x[i+ 1], 5 , -165796510);
 d = md5_gg(d, a, b, c, x[i+ 6], 9 , -1069501632);
 c = md5_gg(c, d, a, b, x[i+11], 14, 643717713);
 b = md5_gg(b, c, d, a, x[i+ 0], 20, -373897302);
 a = md5_gg(a, b, c, d, x[i+ 5], 5 , -701558691);
 d = md5_gg(d, a, b, c, x[i+10], 9 , 38016083);
 c = md5_gg(c, d, a, b, x[i+15], 14, -660478335);
 b = md5_gg(b, c, d, a, x[i+ 4], 20, -405537848);
 a = md5_gg(a, b, c, d, x[i+ 9], 5 , 568446438);
 d = md5_gg(d, a, b, c, x[i+14], 9 , -1019803690);
 c = md5_gg(c, d, a, b, x[i+ 3], 14, -187363961);
 b = md5_gg(b, c, d, a, x[i+ 8], 20, 1163531501);
 a = md5_gg(a, b, c, d, x[i+13], 5 , -1444681467);
 d = md5_gg(d, a, b, c, x[i+ 2], 9 , -51403784);
 c = md5_gg(c, d, a, b, x[i+ 7], 14, 1735328473);
 b = md5_gg(b, c, d, a, x[i+12], 20, -1926607734);
 
 a = md5_hh(a, b, c, d, x[i+ 5], 4 , -378558);
 d = md5_hh(d, a, b, c, x[i+ 8], 11, -2022574463);
 c = md5_hh(c, d, a, b, x[i+11], 16, 1839030562);
 b = md5_hh(b, c, d, a, x[i+14], 23, -35309556);
 a = md5_hh(a, b, c, d, x[i+ 1], 4 , -1530992060);
 d = md5_hh(d, a, b, c, x[i+ 4], 11, 1272893353);
 c = md5_hh(c, d, a, b, x[i+ 7], 16, -155497632);
 b = md5_hh(b, c, d, a, x[i+10], 23, -1094730640);
 a = md5_hh(a, b, c, d, x[i+13], 4 , 681279174);
 d = md5_hh(d, a, b, c, x[i+ 0], 11, -358537222);
 c = md5_hh(c, d, a, b, x[i+ 3], 16, -722521979);
 b = md5_hh(b, c, d, a, x[i+ 6], 23, 76029189);
 a = md5_hh(a, b, c, d, x[i+ 9], 4 , -640364487);
 d = md5_hh(d, a, b, c, x[i+12], 11, -421815835);
 c = md5_hh(c, d, a, b, x[i+15], 16, 530742520);
 b = md5_hh(b, c, d, a, x[i+ 2], 23, -995338651);
 
 a = md5_ii(a, b, c, d, x[i+ 0], 6 , -198630844);
 d = md5_ii(d, a, b, c, x[i+ 7], 10, 1126891415);
 c = md5_ii(c, d, a, b, x[i+14], 15, -1416354905);
 b = md5_ii(b, c, d, a, x[i+ 5], 21, -57434055);
 a = md5_ii(a, b, c, d, x[i+12], 6 , 1700485571);
 d = md5_ii(d, a, b, c, x[i+ 3], 10, -1894986606);
 c = md5_ii(c, d, a, b, x[i+10], 15, -1051523);
 b = md5_ii(b, c, d, a, x[i+ 1], 21, -2054922799);
 a = md5_ii(a, b, c, d, x[i+ 8], 6 , 1873313359);
 d = md5_ii(d, a, b, c, x[i+15], 10, -30611744);
 c = md5_ii(c, d, a, b, x[i+ 6], 15, -1560198380);
 b = md5_ii(b, c, d, a, x[i+13], 21, 1309151649);
 a = md5_ii(a, b, c, d, x[i+ 4], 6 , -145523070);
 d = md5_ii(d, a, b, c, x[i+11], 10, -1120210379);
 c = md5_ii(c, d, a, b, x[i+ 2], 15, 718787259);
 b = md5_ii(b, c, d, a, x[i+ 9], 21, -343485551);
 
 a = safe_add(a, olda);
 b = safe_add(b, oldb);
 c = safe_add(c, oldc);
 d = safe_add(d, oldd);
 }
 return Array(a, b, c, d);
 
}
 

function md5_cmn(q, a, b, x, s, t)
{
 return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s),b);
}
function md5_ff(a, b, c, d, x, s, t)
{
 return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
}
function md5_gg(a, b, c, d, x, s, t)
{
 return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
}
function md5_hh(a, b, c, d, x, s, t)
{
 return md5_cmn(b ^ c ^ d, a, b, x, s, t);
}
function md5_ii(a, b, c, d, x, s, t)
{
 return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
}
 

function core_hmac_md5(key, data)
{
 var bkey = str2binl(key);
 if(bkey.length > 16) bkey = core_md5(bkey, key.length * chrsz);
 
 var ipad = Array(16), opad = Array(16);
 for(var i = 0; i < 16; i++)
 {
 ipad[i] = bkey[i] ^ 0x36363636;
 opad[i] = bkey[i] ^ 0x5C5C5C5C;
 }
 
 var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);
 return core_md5(opad.concat(hash), 512 + 128);
}
 

function safe_add(x, y)
{
 var lsw = (x & 0xFFFF) + (y & 0xFFFF);
 var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
 return (msw << 16) | (lsw & 0xFFFF);
}
 

function bit_rol(num, cnt)
{
 return (num << cnt) | (num >>> (32 - cnt));
}
 

function str2binl(str)
{
 var bin = Array();
 var mask = (1 << chrsz) - 1;
 for(var i = 0; i < str.length * chrsz; i += chrsz)
 bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (i%32);
 return bin;
}
 

function binl2hex(binarray)
{
 var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
 var str = "";
 for(var i = 0; i < binarray.length * 4; i++)
 {
 str += hex_tab.charAt((binarray[i>>2] >> ((i%4)*8+4)) & 0xF) +
 hex_tab.charAt((binarray[i>>2] >> ((i%4)*8 )) & 0xF);
 }
 return str;
}
 

function binl2b64(binarray)
{
 var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
 var str = "";
 for(var i = 0; i < binarray.length * 4; i += 3)
 {
 var triplet = (((binarray[i >> 2] >> 8 * ( i %4)) & 0xFF) << 16)
 | (((binarray[i+1 >> 2] >> 8 * ((i+1)%4)) & 0xFF) << 8 )
 | ((binarray[i+2 >> 2] >> 8 * ((i+2)%4)) & 0xFF);
 for(var j = 0; j < 4; j++)
 {
 if(i * 8 + j * 6 > binarray.length * 32) str += b64pad;
 else str += tab.charAt((triplet >> 6*(3-j)) & 0x3F);
 }
 }
 return str;
}
