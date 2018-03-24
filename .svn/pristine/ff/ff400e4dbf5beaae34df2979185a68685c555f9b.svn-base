var getObj = function(id) {return document.getElementById(id);};//mj
var userAgent = navigator.userAgent.toLowerCase();
var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);
var imageType = "gif,jpg,jpeg,png,bmp,iff,jp2,jpx,jb2,jpc,xbm,wbmp";
function isUndefined(variable) {
	return typeof variable == 'undefined' ? true : false;
}
//修复setTimeout bug，使用window.setTimeout调用
if(!+'\v1') {
    (function(f){
        window.setTimeout =f(window.setTimeout);
        window.setInterval =f(window.setInterval);
    })(function(f){
        return function(c,t){
            if(typeof(c) == "string")
               return eval(c);
               
            var a=[].slice.call(arguments,2);
            return f(function(){
                c.apply(this,a)},t)
            }
    });
}

var jsmenu = new Array();
var ctrlobjclassName;
jsmenu['active'] = new Array();
jsmenu['timer'] = new Array();
jsmenu['iframe'] = new Array();

function initCtrl(ctrlobj, click, duration, timeout, layer) {
	if(ctrlobj && !ctrlobj.initialized) {
		ctrlobj.initialized = true;
		ctrlobj.unselectable = true;

		ctrlobj.outfunc = typeof ctrlobj.onmouseout == 'function' ? ctrlobj.onmouseout : null;
		ctrlobj.onmouseout = function() {
			if(this.outfunc) this.outfunc();
			if(duration < 3) jsmenu['timer'][ctrlobj.id] = setTimeout(hideMenu, timeout, layer);
		}

		ctrlobj.overfunc = typeof ctrlobj.onmouseover == 'function' ? ctrlobj.onmouseover : null;
		ctrlobj.onmouseover = function(e) {
			doane(e);
			if(this.overfunc) this.overfunc();
			if(click) {
				clearTimeout(jsmenu['timer'][this.id]);
			} else {
				for(var id in jsmenu['timer']) {
					if(jsmenu['timer'][id]) clearTimeout(jsmenu['timer'][id]);
				}
			}
		}
	}
}

function initMenu(ctrlid, menuobj, duration, timeout, layer, drag) {
	if(menuobj && !menuobj.initialized) {
		menuobj.initialized = true;
		menuobj.ctrlkey = ctrlid;
		menuobj.onclick = ebygum;
		menuobj.style.position = 'absolute';
		if(duration < 3) {
			if(duration > 1) {
				menuobj.onmouseover = function() {
					clearTimeout(jsmenu['timer'][ctrlid]);
				}
			}
			if(duration != 1) {
				menuobj.onmouseout = function() {
					jsmenu['timer'][ctrlid] = setTimeout(hideMenu, timeout, layer);
				}
			}
		}
		menuobj.style.zIndex = 50;
		if(is_ie) {
			//menuobj.style.filter += "progid:DXImageTransform.Microsoft.shadow(direction=135,color=#CCCCCC,strength=2)";
		}
		if(drag) {
			menuobj.onmousedown = function(event) {try{menudrag(menuobj, event, 1);}catch(e){}};
			menuobj.onmousemove = function(event) {try{menudrag(menuobj, event, 2);}catch(e){}};
			menuobj.onmouseup = function(event) {try{menudrag(menuobj, event, 3);}catch(e){}};
		}
	}
}

var menudragstart = new Array();
function menudrag(menuobj, e, op) {
	if(op == 1) {
		if(in_array(is_ie ? event.srcElement.tagName : e.target.tagName, ['TEXTAREA', 'INPUT', 'BUTTON', 'SELECT'])) {
			return;
		}
		menudragstart = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
		menudragstart[2] = parseInt(menuobj.style.left);
		menudragstart[3] = parseInt(menuobj.style.top);
		doane(e);
	} else if(op == 2 && menudragstart[0]) {
		var menudragnow = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
		menuobj.style.left = (menudragstart[2] + menudragnow[0] - menudragstart[0]) + 'px';
		menuobj.style.top = (menudragstart[3] + menudragnow[1] - menudragstart[1]) + 'px';
		doane(e);
	} else if(op == 3) {
		menudragstart = [];
		doane(e);
	}
}

function showMenu(ctrlid, align, click, offset, duration, timeout, layer, showid, maxh, drag) {
	var ctrlobj = getObj(ctrlid);
	if(!ctrlobj) return;
	if(isUndefined(align)) align = 0;
	if(isUndefined(click)) click = false;
	if(isUndefined(offset)) offset = 0;
	if(isUndefined(duration)) duration = 2;
	if(isUndefined(timeout)) timeout = 200;
	if(isUndefined(layer)) layer = 0;
	if(isUndefined(showid)) showid = ctrlid;
	var showobj = getObj(showid);
	var menuobj = getObj(showid + ' _menu');//mj
	if(!showobj|| !menuobj) return;
	if(isUndefined(maxh)) maxh = 400;
	if(isUndefined(drag)) drag = false;

	if(click && jsmenu['active'][layer] == menuobj) {
		hideMenu(layer);
		return;
	} else {
		hideMenu(layer);
	}

	var len = jsmenu['timer'].length;
	if(len > 0) {
		for(var i=0; i<len; i++) {
			if(jsmenu['timer'][i]) clearTimeout(jsmenu['timer'][i]);
		}
	}

	initCtrl(ctrlobj, click, duration, timeout, layer);
	ctrlobjclassName = ctrlobj.className;
	ctrlobj.className += ' hover';
	initMenu(ctrlid, menuobj, duration, timeout, layer, drag);
	
	//xjj
	//用于处理图片错位的问题
	var _pf = navigator.platform; 
	var appVer = navigator.userAgent; 
	var sysInfo = GetOSInfo(_pf,appVer);//调用GetOSInfo()获得是什么系统
	if(sysInfo == "Win7" || appVer.indexOf("IE 8.0") > -1){
		menuobj.style.marginTop = 0 - outer_div.scrollTop;//若是WIN7或者_pf信息中含有IE 8.0的就执行,XP不执行此代码
	}

	menuobj.style.display = 'block';
	if(!is_opera) {
		menuobj.style.clip = 'rect(auto, auto, auto, auto)';
	}

	//setMenuPosition(showid, align, offset);//如果页面内容过多会出现滚动条 此刻 该方法将出现严重的问题。需要进行优化
	setMenuPosition_edit(showid, align, offset);
	if(is_ie && is_ie < 7) {
		if(!jsmenu['iframe'][layer]) {
			var iframe = document.createElement('iframe');
			iframe.style.display = 'none';
			iframe.style.position = 'absolute';
			iframe.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';
			getObj('append_parent') ? getObj('append_parent').appendChild(iframe) : menuobj.parentNode.appendChild(iframe);
			jsmenu['iframe'][layer] = iframe;
		}
		jsmenu['iframe'][layer].style.top = menuobj.style.top;
		jsmenu['iframe'][layer].style.left = menuobj.style.left;
		jsmenu['iframe'][layer].style.width = menuobj.w;
		jsmenu['iframe'][layer].style.height = menuobj.h;
		jsmenu['iframe'][layer].style.display = 'block';
	}

	
	if(maxh && menuobj.scrollHeight > maxh) {
		menuobj.style.height = maxh + 'px';
		if(is_opera) {
			menuobj.style.overflow = 'auto';
		} else {
			menuobj.style.overflowY = 'auto';
		}
	}

	if(!duration) {
		setTimeout(hideMenu, timeout, layer);
	}

	jsmenu['active'][layer] = menuobj;
}

//获得系统名称
function GetOSInfo(_pf,appVer){ 
	
	if(_pf == "Win32" || _pf == "Windows") 
	{ 
		if(appVer.indexOf("WOW64")>-1){ 
		_bit = "64位"; 
		}else{ 
		_bit = "32位"; 
	} 
	if(appVer.indexOf("Windows NT 6.0") > -1 || appVer.indexOf("Windows Vista") > -1) 
	{ 
		if(_bit=='64位' || appVer.indexOf("Windows Vista") > -1){ 
			return 'Windows_vista '+_bit; 
		}else{ 
			return "Unknow1"; 
		} 
	}else if(appVer.indexOf("Windows NT 6.1") > -1 || appVer.indexOf("Windows 7") > -1) { 
		if(_bit=='32位' || appVer.indexOf("Windows 7") > -1){ 
			return 'Win7'; 
		}else{ 
			return "Unknow"; 
		} 
		}else { 
		try{ 
			var _winName = Array('2000','XP','2003'); 
			var _ntNum = appVer.match(/Windows NT 5.\d/i).toString(); 
			return 'Windows_' + _winName[_ntNum.replace(/Windows NT 5.(\d)/i,"$1")]+" "+_bit; 
		}catch(e){return 'Windows';} 
	} 
	}else if(_pf == "Mac68K" || _pf == "MacPPC" || _pf == "Macintosh") 
	{ 
		return "Mac"; 
	}else if(_pf == "X11") 
	{ 
		return "Unix"; 
	}else if(String(_pf).indexOf("Linux") > -1) 
	{ 
		return "Linux"; 
	}else 
	{ 
		return "Unknow"; 
	} 
}

function setMenuPosition_edit(showid, align, offset) {
	var showobj = getObj(showid);
	var menuobj =document.getElementById(showid+" _menu");
	if(isUndefined(align)) align = 0;
	if(isUndefined(offset)) offset = 0;
	if(showobj) {
	   if(align==0)
	   {
		   mousePos = getMousePos();
		   offsetPos = fetchOffset(showobj);
		   showobj.pos = {left:mousePos.left,top:offsetPos.top};
		   showobj.X = showobj.pos['left']-20;
		   showobj.Y = showobj.pos['top']+showobj.scrollHeight-3;
		}
		else
		{
		   showobj.pos = fetchOffset(showobj);
		   showobj.X = showobj.pos['left'];
		   showobj.Y = showobj.pos['top']+showobj.scrollHeight-3;
		}
	
		var menu_offset = getMenuOffset(showobj.id);
		showobj.w = showobj.offsetWidth;
		showobj.h = showobj.offsetHeight;
	
		menuobj.w = menuobj.offsetWidth;
		menuobj.h = menuobj.offsetHeight;
		
		menuobj.style.left = ((showobj.X + menuobj.w > document.body.clientWidth) && (showobj.X + showobj.w - menuobj.w >= 0) ? showobj.X + showobj.w - menuobj.w : showobj.X) - menu_offset['left'] + 'px';
		if(menuobj.style.clip && !is_opera) {
			menuobj.style.clip = 'rect(auto, auto, auto, auto)';
		} 
	}
}

function setMenuPosition(showid, align, offset) {
	var showobj = getObj(showid);
	//alert(showid);
	//var menuobj = getObj(showid + '_menu');
	var menuobj =document.getElementById(showid+" _menu");
	if(isUndefined(align)) align = 0;
	if(isUndefined(offset)) offset = 0;
	if(showobj) {
	   if(align==0)
	   {
	   	
		   mousePos = getMousePos();
		   offsetPos = fetchOffset(showobj);
		   showobj.pos = {left:mousePos.left,top:offsetPos.top};
		   showobj.X = showobj.pos['left']-20;
		   showobj.Y = showobj.pos['top']+showobj.scrollHeight-3;
		}
		else
		{
		
		   showobj.pos = fetchOffset(showobj);
		   showobj.X = showobj.pos['left'];
		   showobj.Y = showobj.pos['top']+showobj.scrollHeight-3;
		}
		var menu_offset = getMenuOffset(showobj.id);
		showobj.w = showobj.offsetWidth;
		showobj.h = showobj.offsetHeight;
	
		
		menuobj.w = menuobj.offsetWidth;
		menuobj.h = menuobj.offsetHeight;
		//if(align == 1)
		   //menuobj.style.width = ((menuobj.clientWidth < showobj.w ? showobj.w : menuobj.clientWidth)+2) + 'px';
		
		if(offset < 3) {
			
			menuobj.style.left = ((showobj.X + menuobj.w > document.body.clientWidth) && (showobj.X + showobj.w - menuobj.w >= 0) ? showobj.X + showobj.w - menuobj.w : showobj.X) - menu_offset['left'] + 'px';
			
			if(offset == 1)
			   menuobj.style.top = showobj.Y - menu_offset['top'] + 'px';
			else if(offset == 2)
			   menuobj.style.top = (showobj.Y - menuobj.h - menu_offset['top']) + 'px';
			else
			{
			  // alert("XY"+showobj.X+"Y"+showobj.Y);
			   //showobj.Y = showobj.Y-100;
			   
			   menuobj.style.top = showobj.Y - menu_offset['top'] + 'px';
			   var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
               if(parseInt(menuobj.style.top) + menuobj.h > bb.clientHeight + bb.scrollTop){
                  menuobj.style.top = showobj.Y - menuobj.h + 'px';
               }
               //alert("m"+menuobj.style.top);
            }
		} else if(offset == 3) {
			
			menuobj.style.left = (document.body.clientWidth - menuobj.clientWidth) / 2 + document.body.scrollLeft - menu_offset['left'] + 'px';
			menuobj.style.top = (document.body.clientHeight - menuobj.clientHeight) / 2 + document.body.scrollTop - menu_offset['top'] + 'px';
		}
		if(menuobj.style.clip && !is_opera) {
			
			menuobj.style.clip = 'rect(auto, auto, auto, auto)';
		} 
		
		//alert(menuobj.style.left+" "+menuobj.style.top);
	}
}

function hideMenu(layer) {
    //return;
	if(isUndefined(layer)) layer = 0;
	if(jsmenu['active'][layer]) {
		try {
			getObj(jsmenu['active'][layer].ctrlkey).className = ctrlobjclassName;
		} catch(e) {}
		clearTimeout(jsmenu['timer'][jsmenu['active'][layer].ctrlkey]);
		jsmenu['active'][layer].style.display = 'none';
		if(is_ie && is_ie < 7 && jsmenu['iframe'][layer]) {
			jsmenu['iframe'][layer].style.display = 'none';
		}
		jsmenu['active'][layer] = null;
	}
}

function fetchOffset(obj) {
	var left_offset = obj.offsetLeft;
	var top_offset = obj.offsetTop;
	while((obj = obj.offsetParent) != null) {
	    var position = obj.currentStyle ? obj.currentStyle.position : document.defaultView.getComputedStyle(obj, null).getPropertyValue('position');
	    if(position == "relative")
	        break;
		left_offset += obj.offsetLeft;
		top_offset += obj.offsetTop;
	}
	return { 'left' : left_offset, 'top' : top_offset };
}

function ebygum(eventobj) {
	if(!eventobj || is_ie) {
		window.event.cancelBubble = true;
		return window.event;
	} else {
		if(eventobj.target.type == 'submit') {
			eventobj.target.form.submit();
		}
		eventobj.stopPropagation();
		return eventobj;
	}
}

function doane(event) {
	e = event ? event : window.event;
	if(is_ie) {
		e.returnValue = false;
		e.cancelBubble = true;
	} else if(e) {
		e.stopPropagation();
		e.preventDefault();
	}
}

function in_array(needle, haystack) {
	if(typeof needle == 'string' || typeof needle == 'number') {
		for(var i in haystack) {
			if(haystack[i] == needle) {
					return true;
			}
		}
	}
	return false;
}
function getEvent() //ͬʱ����ie��ff��д��
{
    if(document.all)  return window.event;
    func=getEvent.caller;
    while(func!=null){
        var arg0=func.arguments[0];
        if(arg0)
        {
          if((arg0.constructor==Event || arg0.constructor ==MouseEvent) || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation))
          {
          return arg0;
          }
        }
        func=func.caller;
    }
    return null;
}
function getEventSrc()
{
   var event=getEvent();
   return event.srcElement ? event.srcElement : event.target;
}
function getMousePos()
{
   var mouseX = 0;
   var mouseY = 0;
   var e = getEvent();//alert(e.clientX)
   var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;

   mouseX = e.clientX + bb.scrollLeft;
   mouseY = e.clientY + bb.scrollTop;

   return {left:mouseX,top:mouseY};
}

function getMenuOffset(id)
{
   var dialogLeft=dialogTop=0;
   var el = document.getElementById(id);
   while(el)
   {
      el=el.parentElement ? el.parentElement : el.parentNode;
      if(el && el.className=="ModalDialog")
      {
         dialogLeft=parseInt(el.style.left);
         dialogTop=parseInt(el.style.top);
         break;
      }
   }
   return {left:dialogLeft, top:dialogTop};
}

function show_attach_op(id, a)
{
   var pos = fetchOffset(a);
   var el = document.getElementById(id);
   el.style.display="block";
   el.style.width=a.offsetWidth;
   el.style.left=pos['left'];
   el.style.top=pos['top']+a.offsetHeight-3;
   el.style.filter = "progid:DXImageTransform.Microsoft.shadow(direction=135,color=#CCCCCC,strength=4)";
   
   var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
   if(parseInt(el.style.top) + el.offsetHeight > bb.offsetHeight + bb.scrollTop)
      el.style.top = pos['top'] - el.offsetHeight+5;
}
function hide_attach_op(id)
{
   var el = document.getElementById(id);
   el.style.display="none";
}
function SaveFile(ATTACHMENT_ID,ATTACHMENT_NAME)
{
  URL="/module/save_file?ATTACHMENT_ID="+ATTACHMENT_ID+"&ATTACHMENT_NAME="+ATTACHMENT_NAME+"&A=1";
  loc_x=screen.availWidth/2-200;
  loc_y=screen.availHeight/2-90;
  window.open(URL,null,"height=180,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}

function sel_attach(div_id,dir_field,name_field,disk_id,filter)
{
   if(!filter)
      filter="";
   var URL="/module/sel_file?EXT_FILTER=" + filter + "&MULTI_SELECT=1&DIV_ID=" + div_id + "&DIR_FIELD=" + dir_field + "&NAME_FIELD=" + name_field + "&TYPE_FIELD=" + disk_id;
   window.open(URL,null,"height=300,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=200,left=300,resizable=yes");
}

function upload_limit_check(file_name)
{
   if(upload_limit==0 || file_name=="")
      return true;
   
   file_name=file_name.substring(file_name.lastIndexOf("\\")+1).toLowerCase();
   var ext_name="";
   if(file_name.lastIndexOf(".")>=0)
      ext_name=file_name.substring(file_name.lastIndexOf(".")+1,file_name.length);
   if(ext_name=="" || ext_name==file_name)
      ext_name="*";
   
   var bFound=limit_type.indexOf(ext_name+",")==0 || limit_type.indexOf(","+ext_name+",")>0;
   if(upload_limit==1 && !bFound || upload_limit==2 && bFound)
      return true;
   
   if(ext_name=="*")
      alert(td_lang.inc.msg_1);//"�������ϴ��޺�׺����ļ�"
   else
   	{
   		var msg1 = sprintf(td_lang.inc.msg_106, ext_name);//"�������ϴ���׺��Ϊ%s���ļ�"
      alert(msg1);
    }
   return false;
}

function upload_image_check(file_name)
{
   if(file_name=="")
   {
      alert(td_lang.inc.msg_2);//"ͼƬֻ��Ϊgif,jpg,png,bmp��ʽ"
      return false;
   }
   
   file_name=file_name.substring(file_name.lastIndexOf("\\")+1).toLowerCase();
   var ext_name="";
   if(file_name.lastIndexOf(".")>=0)
      ext_name=file_name.substring(file_name.lastIndexOf(".")+1,file_name.length);
   if(ext_name=="" || ext_name==file_name)
   {
      alert(td_lang.inc.msg_2);//"ͼƬֻ��Ϊgif,jpg,png,bmp��ʽ"
      return false;
   }
   
   var file_types = "gif,jpg,jpeg,png,bmp,";
   if(file_types.indexOf(ext_name+",")<0 && file_types.indexOf(","+ext_name+",")<=0)
   {
      alert(td_lang.inc.msg_2);//"ͼƬֻ��Ϊgif,jpg,png,bmp��ʽ"
      return false;
   }
   
   return true;
}

function GetParentEl(el, tagName)
{
   el=el.parentElement ? el.parentElement : el.parentNode;
   if(!el)
     return null;

   if(el.tagName.toLowerCase()==tagName)
      return el;
   else
      return GetParentEl(el, tagName);
}
function CreateFileEl(id,onchange)
{
   if(isUndefined(onchange))
      onchange = AddFile;
   
   var attach=document.createElement("input");
   attach.type="file";
   attach.className="addfile";
   attach.name=id;
   attach.id=id;
   attach.size="1";
   attach.hideFocus="true";
   attach.onchange=onchange;
   return attach;
}
function AddFile(file_type)
{
   var file=getEventSrc();
   var prefix=file.id.substring(0,file.id.lastIndexOf("_"));
   if(!prefix)
      prefix="ATTACHMENT";
   
   var attach_div = document.getElementById(prefix+"_div");
   var form_el = GetParentEl(file,"form");//alert(addFileLink.name)
   var addFileLink = GetParentEl(file,"a");
   if(!attach_div || !form_el || !addFileLink)
   {
      alert(td_lang.inc.msg_3);//"������Ч"
      return;
   }
   
   if(!upload_limit_check(file.value) || file_type == "image" && !upload_image_check(file.value))
   {
      var attach = CreateFileEl(file.id, file.onchange);
      addFileLink.removeChild(file);
      addFileLink.appendChild(attach);
      return;
   }
   
   var id=parseInt(file.id.substring(prefix.length+1));
   var el=form_el.children ? form_el.children : form_el.childNodes;
   for(var i=0; i<el.length;i++)
   {
      if(el[i].tagName && el[i].tagName.toLowerCase()=="input" && el[i].type.toLowerCase()=="file" && el[i].id!=file.id && el[i].value==file.value)
      {
         alert(td_lang.inc.msg_4);//"���ļ��Ѿ����"
         addFileLink.removeChild(file);
         var attach = CreateFileEl(file.id, file.onchange);
         addFileLink.appendChild(attach);
         return;
      }
   }
   
   var attach_name = file.value.substring(file.value.lastIndexOf("\\")+1);
   attach_div.innerHTML+="<span id='"+prefix+"_span_"+id+"' title='"+file.value+"'><img src='/images/attach.png' align='absMiddle'>"+attach_name+"<img src='/images/remove.png' onclick='RemoveFile(this)' align='absMiddle' style='cursor:hand;'>;&nbsp;</span>";
   file.style.zIndex = "-1";
   form_el.appendChild(file);
   
   id++;
   var attach = CreateFileEl(prefix+'_'+id, file.onchange);   
   addFileLink.appendChild(attach);
   document.getElementById(prefix+"_upload_div").style.display="";
   
   if(file_type=="image")
   {      
      InsertImage("file://"+file.value.replace(/\\/g,"/"));
   }
}

function RemoveFile(img)
{
   var span = GetParentEl(img,"span");
   var file = document.getElementById(span.id.replace("_span_","_"));
   if(span && span.parentElement)
      span.parentElement.removeChild(span);
   
   if(file && file.parentElement)
      file.parentElement.removeChild(file);
}
function ShowAddFile(postfix,show_sel_attach)
{
   if(isUndefined(postfix)) postfix="";
   document.write('<span id="ATTACHMENT'+postfix+'_div"></span><span id="ATTACHMENT'+postfix+'_upload_div" style="display:none;"></span><div id="SelFileDiv'+postfix+'"></div><a class="addfile" href="javascript:;">'+td_lang.inc.msg_5+'<input class="addfile" type="file" name="ATTACHMENT'+postfix+'_0" id="ATTACHMENT'+postfix+'_0" size="1" hideFocus="true" onchange="AddFile();" /></a>');//��Ӹ���
   if(show_sel_attach!='0')
      document.write('&nbsp;|&nbsp;<a href="#" onclick="sel_attach(\'SelFileDiv'+postfix+'\',\'ATTACH_DIR'+postfix+'\',\'ATTACH_NAME'+postfix+'\',\'DISK_ID'+postfix+'\');" class="selfile">'+td_lang.inc.msg_6+'</a><input type="hidden" value="" name="ATTACH_NAME'+postfix+'"><input type="hidden" value="" name="ATTACH_DIR'+postfix+'"><input type="hidden" value="" name="DISK_ID'+postfix+'">');//���ļ��������Ӳ��ѡ�񸽼�
}
function ShowAddImage(postfix)
{
   if(isUndefined(postfix)) postfix="";
   document.write('&nbsp;&nbsp;<a id="add_image" class="addimage" href="javascript:;">'+td_lang.inc.msg_7+'<input class="addfile" type="file" name="ATTACHMENT'+postfix+'_1000" id="ATTACHMENT'+postfix+'_1000" size="1" hideFocus="true" onchange="AddFile(\'image\');" /></a>');//����ͼƬ
}

function AddImage2Editor(name, src)
{
   if(typeof(insertEditorImage) == 'function')
     insertEditorImage(name, src);
}