var eg={version:'0.1'};
function fnToTop(){
	window.scrollTo(0,0);
}
function autoalert(str,func){
	if(typeof ymPrompt == 'undefined'){
		o2js.build('ymprompt');
	}
	ymPrompt.alert({
		message:str,titleBar:false,btn:[],showMask:true
	});
	if(typeof func == 'function'){
		setTimeout(function(){
			ymPrompt.close();
			func();
		},1500);
	}else{
		setTimeout(function(){
			ymPrompt.close();
		},1500);
	}
}
function serializeObj(who){
	var tmp = $(who).serializeArray();
	var rt = {};
	for(var vvi=0;vvi<tmp.length; vvi++){
		rt[tmp[vvi].name] = tmp[vvi].value;
	}
	return rt;
}
/* 去除空格 */
String.prototype.trim = function()
{
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.getBytes = function() {   
	var cArr = this.match(/[^\x00-\xff]/ig);   
	return this.length + (cArr == null ? 0 : cArr.length);   
}
/* 用于input标签输入数据时检查，强制性转祛除非法字符    \ / : * ? " < > | & % '~  */
function fnFCC(obj)   
{   
	var reg = /[\\\/\*\?"<>|&%'`~]/g;
	obj.value = obj.value.replace(reg,"");   
}
function FCC(obj)
{   
	var reg = /[\\\/"'<>]/g;
	obj.value = obj.value.replace(reg,"");   
} 
 
/* 用于input标签输入数据时检查，强制性转祛除非法字符   只能是数字 */
function fnFCCN(obj)   
{   
	var reg = /[\D]/g;
	obj.value = obj.value.replace(reg,"");   
} 
/* 是否为空 */
function isNull(_sVal)
{
	return ((typeof _sVal == "undefined") || (_sVal == null) || (_sVal == ""));
}
/* 得到今天并格式化 */
function today(){
	return (new Date()).eformat('yyyy-MM-dd');
}
/*格式化日期原型控制  yyyy-M-dd*/
Date.prototype.eformat = function(format)
{
	var o = {
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(),    //day
		"h+" : this.getHours(),   //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3),  //quarter
		"S"  : this.getMilliseconds() //millisecond
	}
	if(/(y+)/.test(format)){
		format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}	
	for(var k in o){
		if(new RegExp("("+ k +")").test(format)){
			format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
		}
	}		
	return format;
}
/*---------------------得到累计表格数据begin---------------------*/
function fnZGetFields(_args)
{
	var returns = [];
	var bRename = false;
	var beachF = false;
	var splits = '';
	if(_args.rename){
		bRename = true;
	}
	if(_args.eachFields){
		beachF = true;
	}
	if(_args.splits){
		splits = _args.splits;
	}
	for(var ii=0;ii<_args.nums;ii++)
	{ 
		if(eval('document.getElementById("'+_args.fields[0] + splits + ii+'")'))
		{
			this.sn = ii;																					 
			var oTemp = new function(){
						if(beachF){
							for(var pp=0;pp<(_args.eachFields.length/2);pp++){
								eval('this.'+_args.eachFields[pp*2]+' = "'+_args.eachFields[pp*2+1]+'"');
							}
						}
						for(var kk=0;kk<_args.fields.length;kk++)
						{
							if(eval('document.getElementById("'+_args.fields[kk] + splits + ii+'")')){
								var this_id = _args.fields[kk] + splits + ii;
								if(bRename){
									eval('this.'+_args.rename[kk]+' = fnZGetFields_val({id:"'+this_id+'",type:_args.fieldtype[kk]})');
								}else{
									eval('this.'+_args.fields[kk]+' = fnZGetFields_val({id:"'+this_id+'",type:_args.fieldtype[kk]})');
								}
							}
						}
					}
			returns.push(oTemp);
		}
	}
	return returns;
}
function fnZGetFields_val(_args)
{
	var returns = null;
	switch(_args.type)
	{
		case 'select':
		case 'textarea':
		case 'hidden':
		case 'text':
			returns = $('#'+_args.id).val();
			break;
		case 'checkbox':
			if(document.getElementById(_args.id).checked){
				returns = '1';
			}else{
				returns = '0';
			}
			break;
		case 'nodetext':
			if(document.getElementById((_args.id)).firstChild){
				returns = document.getElementById((_args.id)).firstChild.nodeValue;
			}else{
				returns='';
			}
			break;
		case 'seletct_html':
			returns = $('#'+_args.id+' option:selected').get(0).innerHTML;
			break;
		default:
			returns = $('#'+_args.id).val();
			break;
	}
	return returns;
}
/*---------------------得到表格数据end---------------------*/

/**
*设置日期选择
*/
function fnSetSltDate(_Field,_button)
{
	Calendar.setup({
		inputField:_Field,
		button:_button,
		ifFormat:"%Y-%m-%d"
//		align:"Tl"
	});
}
/* 是否有dom节点 */
function fnDom(element){
	if(typeof element == 'string'){
		element = document.getElementById(element);
	}	
	if(element)
		return true;
	else
		return false;
}
var EJSON=new function(){
	this.decode=function(msg){
		msg=msg.replace(/^\s+|\s+$/g, '');
		if(msg.match(/^\{.*\}$/g)){
			return eval('('+msg+')');
		}else{
			return msg;	
		}
	}
}
/**
* json返回错误处理
* json串返回的格式为 
*{"success":"1","msg":"保存成功.","id":"1"}
*{"success":"0","msg":"保存失败."}	
*/ 
function errorHandler(msg){
	var error=EJSON.decode(msg);
	if(typeof(error)=='object'){
		if(error['success']==1){
			if(error['msg']){
				errorHandlerAlert(error['msg']);
			}
			return error;
		}else{
			if(error['msg']){
				errorHandlerAlert(error['msg']);
			}
			return error;
		}
	}else{
		errorHandlerAlert(msg);
		return false;
	}
}
function errorHandlerAlert(str){
	alert(str);
}
/* 通用正则验证 */
function fnRegExp(regkey,$val){								
	var eRegexEnum = 
	{
		reg_int:"^([+-]?)\\d+$",					//整数
		reg_int1:"^([+]?)\\d+$",					//正整数
		reg_int2:"^-\\d+$",						//负整数
		reg_num:"^([+-]?)\\d*\\.?\\d+$",			//数字
		reg_num1:"^([+]?)\\d*\\.?\\d+$",			//正数
		reg_num2:"^-\\d*\\.?\\d+$",					//负数
		reg_dec:"^([+-]?)\\d*\\.\\d+$",			//浮点数
		reg_dec1:"^([+]?)\\d*\\.\\d+$",			//正浮点数
		reg_dec2:"^-\\d*\\.\\d+$",				//负浮点数
		reg_email:"^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", //邮件
		reg_color:"^[a-fA-F0-9]{6}$",				//颜色
		reg_url:"^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$",	//url
		reg_chinese:"^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$",					//仅中文
		reg_ascii:"^[\\x00-\\xFF]+$",				//仅ACSII字符
		reg_zipcode:"^\\d{6}$",						//邮编
		reg_mobile:"^(13|15)[0-9]{9}$",				//手机
		reg_ip:"^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]).(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]).(d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]).(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$",				//ip地址
		reg_notempty:"^\\S+$",						//非空
		reg_picture:"(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$",	//图片
		reg_rar:"(.*)\\.(rar|zip|7zip|tgz)$",								//压缩文件
		reg_date:"^\\d{4}\\-\\d{1,2}\\-\\d{1,2}$",					//日期
		reg_qq:"^[1-9]*[1-9][0-9]*$",				//QQ号码
		reg_tel:"(\\d{3}-|\\d{4}-)?(\\d{8}|\\d{7})",	//国内电话
		reg_username:"^\\w+$",						//用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
		reg_letter:"^[A-Za-z]+$",					//字母
		reg_letter_u:"^[A-Z]+$",					//大写字母
		reg_letter_l:"^[a-z]+$",					//小写字母
		reg_idcard:"^[1-9]([0-9]{14}|[0-9]{17})$"	//身份证
	};
	var regexpress = eval("eRegexEnum."+regkey);		 
	if(regexpress==undefined || regexpress==""){
		throw("Can't find the validator type \""+regkey+"\"! ");
	}
	var $exps = new RegExp(regexpress,"g");
	if ($exps.test($val)){
		return true;
	}else{
		return false;
	}
}
/* 得到select的值和html，包括单个的和多个的 */
function fnGetSltHtml(_objstr){
	return $('#'+_objstr+' option:selected').get(0).innerHTML;
}
function fnGetSltValue(_objstr){
	return $('#'+_objstr).val();
}
function fnGetMSltHtml(_objstr){
	var returns = [];
	var i=0;
	$('#'+_objstr+' option:selected').each(function(){	    
			returns[i++] = this.text;
	});
	return returns;	
}
function fnGetMSltValue(_objstr){
	var returns = [];
	var i=0;
	$('#'+_objstr+' option:selected').each(function(){	    
			returns[i++] = this.value;
	});
	return returns;	
}
/* 通过id选中selet标签 */
function fnSelected(_id){
	if($.browser.msie){
		if(fnDom(_id)){
			document.getElementById(_id).setAttribute('selected','selected');
		}	
	}else{
		$('#'+_id).attr({'selected':'selected'});
	}
}
/* 得到checkde的值 */
function fnGetCheckBox(element){
	if(typeof element == 'string'){
		element = document.getElementById(element);
	}
	if(element.checked){
		returns = '1';
	}else{
		returns = '0';
	}
	return returns;
}
/* 得到checkde的值 */
function fnSetCheckBox(element,val){
	if(typeof element == 'string'){
		element = document.getElementById(element);
	}
	element = $(element);
	if(val == '1'){
		element.attr('checked',true);
	}else{
		element.attr('checked',false);
	}
}
/* 是否是数字，包括有小数的和没小数的 */
function fnNum(val){
	if(isNaN(val)){
		return false;
	}else{
		var pf_temp = parseFloat(val);
		if(isNaN(pf_temp)){
			return false;
		}else{
			return true;
		}
	}
}
/* 设置小数位的 */
function fnDec(val,dig){
	var temp = 0;
	if(fnNum(val)){
		temp = parseFloat(val);
	}
	return temp.toFixed(dig);	
}
function strToNum(val){
	if(!fnNum(val)){
		return 0;
	}else{
		return parseFloat(val);
	}
}
/*拷贝数组*/
function fnCpArray(aCopyObj)
{
 	return aCopyObj.concat([]);
}
/* 设置select的通用的 */
function fnMakeSelect(element,selects){
	/* selects对象格式为：
			selects = {
				val:-1,
				opt:[{"id":"-1","name":"男"},{"id":"1","name":"女"}
				group:''(group可有可无)
		 }
	*/
	if(typeof element == 'string'){
		element = document.getElementById(element);
	}
	element = $(element);
	var temp_id = element.attr('id');
	var htmls = '';
	var slt_id = '';
	var group='';
	var group_first = true;
	for(var i=0; i < selects.opt.length ; i++){
		var opts = selects.opt[i];
		var names = opts.name;
		if(selects.group){
			if(opts[selects.group] !== group){
				if(group_first){
					htmls += '<optgroup label="'+opts[selects.group]+'">';
				}else{
					htmls += '</optgroup><optgroup label="'+opts[selects.group]+'">';
				}
			}
			group = opts[selects.group];
			group_first = false;
			//names = group + '-' + names;
		}
		var cur_id = '';
		if(selects.val == opts.id){
			slt_id = 'e-mkselect-' + (new Date()).getTime()+'-'+temp_id;
			cur_id = ' id= "'+slt_id+'" ';
		} 
		htmls += '<option value="'+opts.id+'" '+cur_id+' title="'+names+'" >'+names+'</option>';
	  
	}
	if(selects.group){
		htmls += '</optgroup>';
	}
	element.html(htmls);
 	if(slt_id != '') fnSelected(slt_id);
}
/* 只要有jquery的ajax时，右上角提示正在加载图标 */
function showLoading(){
	var whether = true;
	var pic = '/misc/images/new/loading2.gif';
	var msg =  ' 请稍后... ';
	if(typeof arguments[0] != "undefined"){
		msg = arguments[0];
	}
	if(document.getElementById('emvc-ajaxloading-div')){
		$('#emvc-ajaxloading-div').remove();
	}
	/*被卷去的高度，设置一下相对的top，让他始终可以看得到*/
	$(document.body.firstChild).before('<div id="emvc-ajaxloading-div" style="display:none;position:absolute;border:0px;background:#EB6D59 none repeat scroll 0 0;padding-top:10px;padding-bottom:10px;text-align:center;right:5px;top: 5px; width:120px;z-index:1001; "><p id="emvc-ajaxloading-msg" style="color:#ffffff;font-weight:bold;letter-spacing:0.1em">'+msg+'</p></div>');
	$("#emvc-ajaxloading-div").ajaxStart(function(){																									
		//var top = (document.documentElement.scrollTop)+10;
		//$(this).css('top',top+'px');
　　 $(this).show();
		$('#emvc-ajaxloading-msg').html(msg);
	});
	$("#emvc-ajaxloading-div").ajaxError(function(){
　　 $(this).show();
		$('#emvc-ajaxloading-msg').html('操作错误，请联系管理员...');
	});
	$("#emvc-ajaxloading-div").ajaxStop(function(){
			$(this).hide();
	});
	
	
	
	/* 滑动显示 */
	lastScrollYChat=0;
 
	window.setInterval(function(){
		var diffY;
		if (document.documentElement && document.documentElement.scrollTop)
			diffY = document.documentElement.scrollTop;
		else if (document.body)
			diffY = document.body.scrollTop
		else{/*Netscape stuff*/}
		
		percent=.1*(diffY-lastScrollYChat); 
		if(percent>0)percent=Math.ceil(percent); 
		else percent=Math.floor(percent);
		document.getElementById("emvc-ajaxloading-div").style.top=parseInt(document.getElementById("emvc-ajaxloading-div").style.top)+percent+"px";
		lastScrollYChat=lastScrollYChat+percent;
		
	},3);
}

function fnLoading(){
	var div_id = 'dannybox-ajaxloading-div';
	var msg_id= 'dannybox-ajaxloading-msg';
	var pic = '/misc/images/loading.gif';
	var msg =  ' 正在操作中,请稍候... ';
	var clientWidth = parseInt(document.documentElement.clientWidth/2,10) - 125;
	if(typeof arguments[0] != "undefined"){
		msg = arguments[0];
	}
	if(document.getElementById(div_id)){
	  $('#'+div_id).remove();
		clearInterval(window.danneyboy_time_handler);
	}
	/*被卷去的高度，设置一下相对的top，让他始终可以看得到*/
	$(document.body.firstChild).before('<div id="'+div_id+'" style="position:absolute;left:'+clientWidth+'px;top:0px;background-color:#E2F0FC;z-index:10000;padding:10px;width:250px;"  ><div style="border:0px solid #CCCCCC;background-color:#fff;color:#336699;text-align:center;padding:20px 10px;"><img src="'+pic+'" /> <span style="font-size:12px;" id="dannyboy-loading-div-span">'+msg+'</span></div></div>');
	/* 滑动显示 */
	lastScrollYChat=0; 
	window.danneyboy_time_handler = window.setInterval(function(){
		var diffY;
		if (document.documentElement && document.documentElement.scrollTop)
			diffY = document.documentElement.scrollTop;
		else if (document.body)
			diffY = document.body.scrollTop
		else{/*Netscape stuff*/}
		diffY += 100;
		percent=.1*(diffY-lastScrollYChat); 
		if(percent>0)percent=Math.ceil(percent); 
		else percent=Math.floor(percent);
		document.getElementById(div_id).style.top=parseInt(document.getElementById(div_id).style.top,10)+percent+"px";
		lastScrollYChat=lastScrollYChat+percent;
	},3); 
}
function fnLoadingEnd(){
	if(document.getElementById('dannybox-ajaxloading-div')){
		$('#dannybox-ajaxloading-div').remove();
		clearInterval(window.danneyboy_time_handler);
	}
}
/* 提示确认通用函数 */
function fnConfirm(_obj){
	var dft = {
		str:'......?',
		func_true:function(){},
		func_false:function(){}
	};
	_obj = jQuery.extend(dft, _obj);
	var temp = window.confirm(_obj.str);
	if(temp){
		_obj.func_true();
	}else{
		_obj.func_false();
	}
}
function stopBubble(e) {if(e && e.stopPropagation ) e.stopPropagation(); else window.event.cancelBubble = true;}
function stopDefault( e ) {
	if( e && e.preventDefault )
		e.preventDefault();
	else
		window.event.returnValue = false;
	return false;
}

/**
*	页面跳转函数
*/
function jumpTo(url)
{
	//document.location.href=url;
	window.location=url;
}
/**
*	页面回跳转函数
*/
function jumpBack(n){
	n=(n||-1);
	window.history.go(n);
}
//查看变量类型
function defined(obj){
	return obj!=undefined;
}
function type(obj){
	if (obj==undefined) return false;
	if (obj.htmlElement) return 'element';
	var type = typeof obj;
	if (type == 'object' && obj.nodeName){
		switch(obj.nodeType){
			case 1: return 'element';
			case 3: return (/\S/).test(obj.nodeValue) ? 'textnode' : 'whitespace';
		}
	}
	if (type == 'object' || type == 'function'){
		switch(obj.constructor){
			case Array: return 'array';
			case RegExp: return 'regexp';
		}
		if (typeof obj.length == 'number'){
			if (obj.item) return 'collection';
			if (obj.callee) return 'arguments';
		}
	}
	return type;
};
 
/**************************************************
*	打开模式窗口，非IE还是打开新窗体popWindow
**************************************************/
function PopModalWindow(url,width,height,func)
{
	if($.browser.msie)
	{
		if(typeof func != undefined){
			func(window.showModalDialog(url,"win","dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;status:no;scroll:auto;dialogHide:no;resizable:yes;help:no;edge:sunken;"));
		}else{
			return window.showModalDialog(url,"win","dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;status:no;scroll:auto;dialogHide:no;resizable:yes;help:no;edge:sunken;");
		}
	}else{
		return popWindow(url,width,height);
	}
}
/**************************************************
*	打开新窗口
**************************************************/
function popWindow(url,width,height) 
{
	var popwin=window.open(url,"_blank",winOpenArgs(width,height)); 
	return popwin; 
} 
/* 弹出窗体返回值 */
function PopModalWindowHandler(callback,data){
	data=data||true;
	if(typeof window['opener']=='object'){//弹出式，不支持ModalWindow
		window.opener[callback](data);
		window.returnValue=false;//firefox3
	}else if(typeof window['opener']=='undefined'){//支持ModalWindow
		window.returnValue=data;
	}else{
		var tf=typeof window['opener'];
		throw('Error::unknow opener type:'+tf+'!');
	}
}
/* 打开一个窗体默认的参数,主要是窗体居中用 */
function winOpenArgs(){
	var width=800;
	var height=400;
	if(arguments[0]) width = arguments[0];
	if(arguments[1]) height = arguments[1];
	return 'width='+width+',height='+height+',resizable=1,scrollbars=1,left='+(screen.width-width)/2+',top='+(screen.height-height)/2;
}
function AddFavorite(url, title)
{
	if (document.all){
		 window.external.addFavorite(url,title);
	}else if (window.sidebar){
		 window.sidebar.addPanel(title, url, "");
	}
}
function setHomepage(url)
{
    if (document.all){
        document.body.style.behavior='url(#default#homepage)';
        document.body.setHomePage(url);
    }else if (window.sidebar){
        if(window.netscape){
            try{
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
            }catch (e){
                alert( "该操作被浏览器拒绝，如果想启用该功能，请在地址栏内输入 about:config,然后将项 signed.applets.codebase_principal_support 值该为true" );
            }
        }
        var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components. interfaces.nsIPrefBranch);
        prefs.setCharPref('browser.startup.homepage',url);
    }
}
