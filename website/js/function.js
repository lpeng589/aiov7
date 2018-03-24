function getDateDiff(startDate,type,num){
	var year=startDate.substr(0,4);
    var month=Number(startDate.substr(5,2))-1;
    var date=Number(startDate.substr(8,2));
    if(type=="month"){
    //	month = month+Number(num);
    }else{
    //	date = date +Number(num);
    }
    var d=new Date(year,month,date);
    if(type=="month"){
    	yn = parseInt((month + Number(num))/12);
    	mn = (month + Number(num)) % 12;
    	//找到这个月的最大天数
    	d = new Date(Number(year)+yn,mn+1,'01');
    	d.setDate(d.getDate()-1);
    	maxday = d.getDate();
    	if(date>maxday){
    		date = maxday;
    	}
    	d = new Date(Number(year)+yn,mn,date);
    }else{
    	d.setDate(d.getDate()+num);
    }
    month=d.getMonth()+1;
    if(month<10)month='0'+month;
    if(d.getDate()<10)date='0'+d.getDate();else date=d.getDate();
    var newDate=d.getFullYear()+'-'+month+'-'+date;
	return newDate;
}
/**
	zxy 用于数据回填，如弹出窗
	isMainTable:表示该字段是否是主表字段，明细表字段为false
	colNames:回填数据字段名称列表，以";"号分格
	win:当前回填的窗口
	ids:回填数据，多行数据以"|"分格，同一行数据以";"号分格
*/
function dataBackIn(isMainTable,colNames,ids,win){
	if(isMainTable=="true" ){
		rcols = colNames.split(";");
		rowids = ids.split("#;#");
		lastDisObj=null;//最后一个可见的对象
		for(var i=0;i<rcols.length;i++){
			if(rcols[i] != ""){
				fobj = rcols[i];
				if(fobj.indexOf("@TABLENAME.") > -1){
					fobj = fobj.substring(11);
				}
				if(fobj.indexOf(win.$("#tableName").val()+".") == 0){ //以主表开头，先偿试看对象是否存在，中果不存在，再去掉表头来测试，目录发现时霸的物料中有引用自己的情况产生的字段是没去表头的
					var tempfobj = fobj.replace(".","_");
					if(win.$("#"+tempfobj).size() == 0){
						fobj = fobj.substring((win.$("#tableName").val()+".").length);    
					}  
				}
				fobj = fobj.replace(".","_");
				
				win.$("#"+fobj).val(rowids[i]);
				if(win.$("#"+fobj)[0]!=undefined)
				win.$("#"+fobj)[0].oldValue = rowids[i];
				win.$("#tbl_"+fobj).val(rowids[i]);
				if(win.$("#tbl_"+fobj)[0]!=undefined)
					win.$("#tbl_"+fobj)[0].oldValue = rowids[i];
				 
				if(typeof(win.$("#"+fobj).change) =="function"){
					win.$("#"+fobj).trigger("change"); //原来的回填方式是判断字段有calculate时才触发事件，这是可以都触发。
				}

				if(win.$("#"+fobj).attr("selectRName") != undefined && win.$("#"+fobj).attr("selectRName") !=""){
					var sval = win.$("#"+fobj).find("option:selected").text();
					win.$("#"+win.$("#"+fobj).attr("selectRName")).val(sval);
				}
				
				if(win.$("#"+fobj).attr("type") !="hidden"){
					lastDisObj = win.$("#"+fobj);
				}
			}
		}
		//光标下移	
		if(lastDisObj != null){	
			for(var i=0;i<50;i++){
				var tmpLi = lastDisObj.parents("li").next();
				var j = 0;
				while(j < 5)
				{
					if(tmpLi.size() > 0 && tmpLi[0].nodeName == 'LI')
						break;
					else
					{	
						tmpLi = tmpLi.next();
						j++;
					}
				}
				dobj = tmpLi.find("input");	
				if(dobj.size()==0){
					dobj = tmpLi.find("select");
				}
				if(dobj.attr("readonly") !=undefined || dobj.attr("disabled") != undefined){
					lastDisObj = dobj;
				}else{
					dobj.focus();
					break;						
				}
			}
		}	
	}else{
		//明细表的回填
		rcols = colNames.split(";");
		idmrows = ids.split("#|#");
		lastDisObj=null;//最后一个可见的对象
		allrow=-1;
		for(var j=0;j<idmrows.length;j++){
			if(idmrows[j] =="") continue;
			if(j>0){
				win.curGridRowNum.curLine ++; //如果一次返回多行数据，则当前行要加一，

			}
			//判断是否要换行
			if( allrow > -1 && win.curGridRowNum.curLine >= allrow-2 ){
				//增加一行
				 var trObj=lastDisObj[0].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
					 $(trObj.cells[1].childNodes[0]).click();					 
			}
			if(j>0&&document.form.fieldName!=undefined){
				//增加行时要执行复制，如在多选商品时，前面的多仓库要自动复制下来
				for(var k2=0;k2<win.gridDatas.length;k2++){
					if(win.gridDatas[k2].cols[0].name.indexOf(win.curGridRowNum.curKey)>-1){
						tgridData  = win.gridDatas[k2];
						for(var k=0;k<tgridData.cols.length;k++){
							if(tgridData.cols[k].name == document.form.tableName.value+"_"+document.form.fieldName.value){
								if(tgridData.cols[k].copyType=="triggerCopy" || tgridData.cols[k].copyType=="count" || tgridData.cols[k].copyType=="triggerOrCopy"){
									win.copyFields2(win.curGridRowNum.curLine,k,win.curGridRowNum.curKey) ;
								}
							}
						}
					}
				}
			}
			rowids = idmrows[j].split("#;#");				
			for(var i=0;i<rcols.length;i++){
				if(rcols[i] != ""){
					fobj = rcols[i];
					if(fobj.indexOf("@TABLENAME.") > -1){
						fobj =  win.curGridRowNum.curKey+"."+fobj.substring(11);
					}else if(fobj.indexOf(win.curGridRowNum.curKey+".") != 0){
						fobj =  win.curGridRowNum.curKey+"_"+fobj;
					}
					fobj = fobj.replace(".","_");
					cfobjs = win.document.getElementsByName(fobj);
					if(cfobjs != null && cfobjs.length > win.curGridRowNum.curLine){
						allrow = cfobjs.length; //取得对象的行数
						cfobj = cfobjs[win.curGridRowNum.curLine];
						if(cfobj== undefined) {
							console.log(fobj +" is not found");
						}
						var isImg = cfobj.getAttribute("isImg");
						if(isImg ){
							imgValue = rowids[i];
							cfobj.value=imgValue;
							imgobj = win.document.getElementsByName(fobj+"_span")[win.curGridRowNum.curLine];
							if(rowids[i] == ""){
								$(imgobj).attr('osrc','');
								$(imgobj).css("color","");
								imgobj.innerHTML="";
							}else{
								
								imgTable = fobj.substring(fobj.indexOf("_")+1,fobj.lastIndexOf("_"));
								var imgUrl = imgValue;
								if(imgValue.toLowerCase().indexOf("http")==-1){
									imgUrl = "/ReadFile.jpg?tempFile=false&type=PIC&YS=true&tableName=" + imgTable + "&fileName="+imgValue;
								}
								var imgUrl2= imgUrl;
								if(imgUrl2.indexOf(";")> 0){
									imgUrl2 = imgUrl2.substring(0,imgUrl2.indexOf(";"));
								}
								$(imgobj).attr('osrc',imgUrl);
								if(win.tableListViewImg){ 
									$(imgobj).css("color","");
									imgobj.innerHTML="<img height="+(win.tableListRowHeight-6)+" src='"+imgUrl2+"'>";
								}else{
									imgobj.innerHTML="图片";
									$(imgobj).css("color","blue");
								}
							}							
						}else{
							$(cfobj).val(rowids[i]);
						}
						if(cfobj.value.length>0&&cfobj.value!=0){
							$(cfobj).trigger("change"); //原来的回填方式是判断字段有calculate时才触发事件，这是可以都触发。
							//只读选择框，改变显示框的值

							if($(cfobj).attr("selectRName") != undefined && $(cfobj).attr("selectRName") !=""){
								var sval = $(cfobj).find("option:selected").text();
								win.document.getElementsByName($(cfobj).attr("selectRName"))[win.curGridRowNum.curLine].value=sval;
							}
						}			
						
						if($(cfobj).attr("type") !="hidden"){
							lastDisObj = $(cfobj);
						}
					}
				}
			}
			if(colNames.indexOf("GoodsCode")> -1 &&  typeof(win.setSeqQtyReadOnly) != "undefined"){
				//回填字段中包括商品字段时，要较验是否启用序列号
				win.setSeqQtyReadOnly(win.curGridRowNum.curKey,win.curGridRowNum.curLine,true);
			}
			
			
		}
		//光标下移	
		
		if(lastDisObj != null){	
			for(var i=0;i<50;i++){
				dobj = lastDisObj.parents("td").next().find("input");	
				if(dobj.size()==0){
					dobj = lastDisObj.parents("td").next().find("select");
				}
				if(dobj.attr("readonly") !=undefined || dobj.attr("disabled") != undefined){
					lastDisObj = dobj;
				}else{					
					dobj.trigger("focus") ;
					break;						
				}
			}
		}			
	} 
}

function closeWin(popWinName){
    
	if(typeof(popWinName) != "undefined" &&  popWinName != "" && parent.window.jQuery.exist(popWinName)){
		var v = parent.window;
		if(v.jQuery.find(":text:eq(0)").length>0)
		v.jQuery.find(":text:eq(0)")[0].focus();
		v.jQuery.close(popWinName);
	}else if(typeof(popWinName) != "undefined" &&  popWinName != "" && parent.parent.window.jQuery.exist(popWinName)){
		var v = parent.parent.window;
		if(v.jQuery.find(":text:eq(0)").length>0)
		v.jQuery.find(":text:eq(0)")[0].focus();
		v.jQuery.close(popWinName);
	}else if(typeof(popWinName) != "undefined" &&  popWinName != "" && jQuery.exist(popWinName)){
		jQuery.close(popWinName);
	}else if(typeof(this.parent.win)!="undefined"){
		this.parent.win.removewin(this.parent.win.currentwin);
	}else if(typeof(parent.jQuery)!="undefined" && typeof(parent.jQuery.fn.closeActiveTab)!="undefined"){
		this.parent.jQuery.fn.closeActiveTab();
	}else if(typeof(parent.parent.jQuery)!="undefined" && typeof(parent.parent.jQuery.fn.closeActiveTab)!="undefined"){
		this.parent.parent.jQuery.fn.closeActiveTab();
	}else{
		window.close() ;
	}
}

function refreshcloseWindows(pid){    
	if(!window.save && is_form_changed()){
		if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
			return;
		}
	}
	if(parent.window.jQuery.exist('MainPopup')){
		if(parent.window.frames["MainPopup_content"].frames['mainTreeFrame']){
			if(parent.window.frames["MainPopup_content"].frames['mainTreeFrame'].contentWindow){
				parent.window.frames["MainPopup_content"].frames['mainTreeFrame'].contentWindow.beforeSubmit();
			}else{
				parent.window.frames["MainPopup_content"].frames['mainTreeFrame'].beforeSubmit();
			}
		}else{
			if(parent.window.frames["MainPopup_content"].contentWindow){
				parent.window.frames["MainPopup_content"].contentWindow.beforeSubmit();
			}else{
				parent.window.frames["MainPopup_content"].beforeSubmit();
			}
		}
	}else if(parent.window.jQuery.exist('ChildPopup')){
		if(parent.window.frames["ChildPopup_content"].frames['mainTreeFrame']){
			if(parent.window.frames["ChildPopup_content"].frames['mainTreeFrame'].contentWindow){
				parent.window.frames["ChildPopup_content"].frames['mainTreeFrame'].contentWindow.beforeSubmit();
			}else{
				parent.window.frames["ChildPopup_content"].frames['mainTreeFrame'].beforeSubmit();
			}
		}else{
			if(parent.window.frames["ChildPopup_content"].contentWindow){
				parent.window.frames["ChildPopup_content"].contentWindow.beforeSubmit();
			}else{
				parent.window.frames["ChildPopup_content"].beforeSubmit();
			}
		}
	}
	if(typeof(pid) == 'undefined' || pid == ""){
		pid = 'PopMainOpdiv';
	}
	closeWin(pid);
}
function gridselectKeyId(obj){
	ind = jQuery(obj).index();//取得行号
	trobj = $("#k_column > table >tbody > tr:eq("+ind+")",document)
	
	keyi = trobj.find("input[name=keyId]");
	if(keyi.size() > 0){
		if(keyi.attr("type")=="radio"){
			keyi[0].checked = true;
		}else{		
			keyi[0].checked = !keyi[0].checked;
		}
	}
}

var response ;
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

function showHelp(type){
	//if(type=='bill'){
		asyncbox.alert('1、CTRL+S 保存<br/>2、CTRL+D 保存新增<br/>3、CTRL+Z 关闭编辑窗口<br/>4、CTRL+= 明细表增插入一行<br/>5、CTRL+- 明细表删除一行<br/>'+
			'6、CTRL+M 切换明细表<br/>7、CTRL+R 编辑备注<br/>8、CTRL+Q 关闭编辑窗口<br/>9、ALT+S 存为草稿<br/>'+
			'10、CTRL+A 全选<br/>11、ENTER回车 焦点下移<br/>12、左方向键 焦点左移，<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在有上下级查询或弹出窗中切换上一级<br/>'+
			'13、右方向键 焦点右移，<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在有上下级查询或弹出窗中切换下一级<br/>14、上方向键 焦点上移<br/>15、下方向键 焦点下移<br/> ','温馨提示');
	//}
}

function mdiwin(url,title){
	//如果发现没有多窗口方法就调用window.open
	if(typeof(top.showreModule)=="undefined"){
		window.open(url);
	}else{
		top.showreModule(title,url);
	}
}
function changeTxtTitle(eve){ 
	$(eve.target).attr("title",$(eve.target).val());
}
function initTxtTitle(){
	$("input[type=text]:not([num=true])").each(function (){
		$(this).attr("title",$(this).val());
		$(this).unbind("change",changeTxtTitle);
		$(this).bind("change", changeTxtTitle); 
	});
}

function is_form_changed() {     
	var allinput = document.getElementsByTagName("input");
	for(i=0;i<allinput.length && i<460;i++){
		if(allinput[i].type=="text"){
			if(typeof($(allinput[i]).attr("_oldval"))=="undefined"){
				$(allinput[i]).attr("_oldval","");
			}
			if($(allinput[i]).attr("_oldval") != allinput[i].value){
				return true;
			}
		}else if(allinput[i].type=="password"){
			if(typeof($(allinput[i]).attr("_oldval"))=="undefined"){
				$(allinput[i]).attr("_oldval","");
			}
			if($(allinput[i]).attr("_oldval") != allinput[i].value){
				return true;
			}		
		}else if(allinput[i].type=="checkbox"){
			if(typeof($(allinput[i]).attr("_oldval"))=="undefined"){
				$(allinput[i]).attr("_oldval","");
			}
			var _v = allinput[i] ? 'on' : 'off';   
			if($(allinput[i]).attr("_oldval") != _v){
				return true; 
			}
		}
	}
	var alltextarea = document.getElementsByTagName("textarea");
	for(i=0;i<alltextarea.length;i++){
		
		if(typeof($(alltextarea[i]).attr("_oldval"))=="undefined"){
			$(alltextarea[i]).attr("_oldval","");
		}
		if($(alltextarea[i]).attr("_oldval") != alltextarea[i].value){
			return true;
		}	
	}
	var allselect = document.getElementsByTagName("select");
	for(i=0;i<allselect.length;i++){		 
		if(typeof($(allselect[i]).attr("_oldval"))=="undefined"){
			$(allselect[i]).attr("_oldval","");
		}
		if($(allselect[i]).attr("_oldval") != allselect[i].value){
			return true;
		}	
	}  
	return false;
	
}  

//windows max 
function windMax(){
	top.moveTo(0,0);
	top.resizeTo(screen.availWidth,screen.availHeight);
}
//解析序列号

function getSeqArr(seqStr){
	//var seqStr='0:)1:)a001ee;1:)0:)a~ee~1~10;'
	var seqs=new Array();
	var seqStrArr=seqStr.split(';');
	for(var i=0;i<seqStrArr.length-1;i++){
	   var tempSeq=seqStrArr[i].split(':)');
	   var curSeq=tempSeq[2];
	   if(tempSeq[1]=="1"){//具体序列号

	     seqs[seqs.length]=curSeq;
	   }else{//范围序列号

	     var areaSeq=curSeq.split('~');
		 var starNum=areaSeq[2];
		 var count=areaSeq[3];
		 for(var j=0;j<parseInt(count,10);j++){
          if(starNum.length!=(parseFloat(starNum)+"").length){
		     var zeroStr=starNum.substr(0,starNum.length-(parseFloat(starNum)+"").length);
			 var tempRes=zeroStr+(parseFloat(starNum)+j);
			 var lastRes=tempRes.substr(tempRes.length-starNum.length);
			  seqs[seqs.length]=areaSeq[0]+lastRes+areaSeq[1];
		   }else{
		     var res=areaSeq[0]+(parseInt(areaSeq[2])+j)+areaSeq[1];
			 seqs[seqs.length]=res;
		   }
		 }
	   }
	}
	return seqs;
}
function DateAdd(startDate,day){
    var year=startDate.substr(0,4);
    var month=Number(startDate.substr(5,2))-1;
    var date=Number(startDate.substr(8,2))+Number(day);
    var d=new Date(year,month,date);
    month=d.getMonth()+1;
    if(month<10)month='0'+month;
    if(d.getDate()<10)date='0'+d.getDate();else date=d.getDate();
    var newDate=d.getFullYear()+'-'+month+'-'+date;
	return newDate;
}

function f(number,fielddigits){
	if(!isFloat(number)) return 0;
	var formatNumber;

	if(fielddigits>0){
		formatNumber=Number(number).toFixed(fielddigits);
	}else{
		formatNumber=Math.round(Number(number));
	}
	formatNumber = ""+formatNumber; //因为从Math.round 转化出来的对象是Number ,所以必须转为String 下面才能用
	if(formatNumber.indexOf('.')>0){
       while(true){
        if(formatNumber.length-1==formatNumber.lastIndexOf('0')){
              formatNumber=formatNumber.substr(0,formatNumber.length-1);
        }else{
           if(formatNumber.indexOf(".")==formatNumber.length-1){
               formatNumber=formatNumber.substr(0,formatNumber.length-1);
           }
           break;
        }

      } 
  	}
	return formatNumber;
}
/*将给出的值转换成大写，且设置到指定的对象中*/
function setCapital(thisVal){
 var oldVal=thisVal.value;
 if(oldVal.length==0)return;
 var setObj=document.getElementById('cap'+thisVal.name); 

 var oldValTemp=oldVal;
 var lastStr='';
 var capVal='';
 while(oldValTemp.length>0){
 	 var num=oldValTemp.substr(0,1); 	 
 	 if(num=='0'){
 	 	 if(capVal.substr(capVal.length-1)!='0'&&oldValTemp.length!=1){
 	 	 		capVal=capVal+'0' 
 	 	 }
 	 }else{
		 if(oldValTemp.length==8 ||oldValTemp.length==4){
		 	capVal=capVal+num+'仟'
		 }
		 if(oldValTemp.length==7 ||oldValTemp.length==3){
		 	capVal=capVal+num+'佰'
		 }
		 if(oldValTemp.length==6 ||oldValTemp.length==2){
		 	capVal=capVal+num+'拾'
		 }
		 if(oldValTemp.length==5 ||oldValTemp.length==1){
		 	capVal=capVal+num
		 }
	 }
	 lastStr=capVal.substr(capVal.length-1,1);
	 if((oldValTemp.length==5||oldValTemp.length==1)&&lastStr=='0')
	 capVal=capVal.substr(0,capVal.length-1);
	 if(oldValTemp.length==5)	capVal= capVal+'万'
	 if(oldValTemp.length==1)	capVal=capVal+'元'
	 oldValTemp=oldValTemp.substr(1,oldValTemp.length)
 }
 while(capVal.indexOf('1')>=0||capVal.indexOf('2')>=0||capVal.indexOf('3')>=0||capVal.indexOf('4')>=0||capVal.indexOf('5')>=0||capVal.indexOf('6')>=0||capVal.indexOf('7')>=0||capVal.indexOf('8')>=0||capVal.indexOf('9')>=0||capVal.indexOf('0')>=0){
 	capVal=capVal.replace('1','壹').replace('2','贰').replace('3','叁').replace('4','肆').replace('5','伍').replace('6','陆').replace('7','柒').replace('8','捌').replace('9','玖').replace('0','零');
 }
 setObj.value=capVal+'整';
}
/*判断是否整数*/
function isInt(str){
	var pattern = new RegExp("^[-]{0,1}[0-9]*$");
	ret = str.search(pattern);
	return ret>-1;
}
/*判断是否正整数*/
function isInt2(str){
	if(isInt(str)){
		return str>0 ;
	}else{
		return false ;
	}
}

/*判断是否数字（包括小数和整数）*/
function isFloat(str){
	if(str == 'Infinity'||str=='-Infinity') return false;
	return !isNaN(str);
}

/*大于0*/
function gtZero(str){
	if(isFloat(str)){
		return str>0 ;
	}else{
		return false ;
	}
}

/*判断小数位数*/
function getDecimalDigit(decimal){		
	if(decimal.indexOf(".")<0)	return 0;
	var digit = decimal.substr(decimal.indexOf(".")+1).length;
	return digit;
}

/*判断是否只含有数字和英文字符*/
function isEn(str){
	var pattern = new RegExp("^[0-9A-Za-z]*$");
	ret = str.search(pattern);
	return ret>-1;
}
/*判断是否整数*/
function isDate(str){
	var pattern = new RegExp("^[0-9]{4}-[0-1][0-9]-[0-3][0-9]");
	ret = str.search(pattern);
	if(ret>-1){//条件为真
		var yyyy = str.substring(0,4);
		
		var MM = str.substring(str.indexOf('-')+1,str.indexOf('-')+3);
		if(MM>12){
			return false;	
		}
		var dd = str.substring(8,10);
		if(MM==4||MM==6||MM==9||MM==11){//30天
			if(dd>30){
				return false;	
			}
		}else if(MM==2){
			if((yyyy%4==0 && yyyy%100!=0)|| yyyy%400==0){//闰年29天
				if(dd>29){
					return false;
				}
			}else{
				if(dd>28){
					return false;
				}	
			}
		}else{
			if(dd>31){//31天
				return false;	
			}	
		}
	}
	return ret>-1;
}
/*判断是否是00:00:00类型*/
function isTime(str){
	var pattern = new RegExp("[0-2][0-9]:[0-6][0-9]:[0-6][0-9]");
	ret = str.search(pattern);
	return ret>-1;
}
/*判断是否整数*/
function isDatetime(str){
	var pattern = new RegExp("^[0-9]{4}-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-6][0-9]:[0-6][0-9]");
	ret = str.search(pattern);
	return ret>-1;
}
/*判断是否整数*/
function isIp(str){
	var pattern = new RegExp("^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}");
	ret = str.search(pattern);
	return ret>-1;
}
/*判断是否含有非法字符*/
function isAny(str){
	if(!str)return true;
	if(str.length==0)return true;
	if(str.indexOf("</textarea>")>=0)return false;
	if(str.indexOf("<textarea>")>=0)return false;
	if(str.indexOf("<select>")>=0)return false;
	if(str.indexOf("</select>")>=0)return false;
	if(str.indexOf("<input>")>=0)return false;
	if(str.indexOf("</input>")>=0)return false;
	if(str.indexOf("<button>")>=0)return false;
	if(str.indexOf("</button>")>=0)return false;
	return true;
}

/*判断是否正确的title输入*/
function isTitle(str){
	var pattern = /[\\<>&\/|'\"]+/;
//	var pattern = new RegExp("[a\\<>&\/|'\"]+");
	return str.search(pattern)==-1
}

/*判断是否是邮件*/
function isMail(str){
	var pattern = new RegExp("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
	ret = str.search(pattern);
	return ret>-1;
}

/*判断是否是电话号码



  匹配形式如:0511-4405222 或者021-87888822 或者 021-44055520-555
*/
function isTel(str){
	var pattern = new RegExp("^([0-9]{3,4}-)?[0-9]{7,8}(-[0-9]{1,3})*$");
	ret = str.search(pattern);
	return ret>-1;
}

/*判断是否是手机号码*/
function isMobile(str){

	var pattern = new RegExp("^[0]{0,1}1[0-9]{10}$");
	ret = str.search(pattern);
	//return ret>-1;
	return ret>-1;
}

/*判断是否是邮编*/
function isZip(str){
	var pattern = new RegExp("^[0-9]{6}$");
	ret = str.search(pattern);
	return ret>-1;
}

/*判断是否是网址*/
function isUrl(str){
	var pattern = new RegExp("^[a-zA-Z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");
	ret = str.search(pattern);
	return ret>-1;
}

/*判断是否是密码*/
function isPassword(str){
	var pattern = new RegExp("^[0-9A-Za-z]+$");
	ret = str.search(pattern);
	return ret>-1;
}

/*使字符串类型可以使用trim()方法去除前后空格*/
String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

/*获得字符串的真实长度*/
String.prototype.len=function(){
	//return this.replace(/[^\x00-\xff]/g,"a");
	return this.replace(/[^\x00-\xff]/g,"a").length;
}

/*禁止指定的form被提交*/
function disableForm(form){
	//自定义的大部分的是button,所以以下方法不适用
	bt =document.getElementsByTagName("button");
	for(var i=0;bt != null && i<bt.length;i++){
		bt[i].disabled=true;
	}
	bt =document.getElementsByTagName("submit");
	for(var i=0;bt != null && i<bt.length;i++){
		bt[i].disabled=true;
	}
}

function enableForm(form){
	bt = document.getElementsByTagName("button");
	for(var i=0;bt != null && i<bt.length;i++){
		if(bt[i].disabled)
			bt[i].disabled=false;
	}
	bt = document.getElementsByTagName("submit");
	for(var i=0;bt != null && i<bt.length;i++){
		if(bt[i].disabled)
			bt[i].disabled=false;
	}
}

/*禁止指定的form被提交*/
function readonlyForm(form){
	for(var i=0;i<form.elements.length;i++){
		if((form.elements[i].type=="text")  && form.elements[i].id!="approveId"){
			form.elements[i].readOnly=true;
		}else if((form.elements[i].type=="select-one")  && form.elements[i].id!="approveId"){
			form.elements[i].disabled=true;
		}
		if(form.elements[i].type=="textarea"){
			form.elements[i].readOnly=true;
		}
	}
	//$("input").unbind( "dblclick" ) ;
	//$("input").unbind( "keyup" ) ;
	$("input").removeAttr("ondblclick");
	$("input").removeAttr("onkeyup");
	$("input").removeAttr("onclick");
	
	
	$(".stBtn").hide();
}

/*是否含有特殊字符*/
function containSC(str){
	if(str.indexOf("'")>-1)return false ;
	if(str.indexOf("\"")>-1)return false ;
	if(str.indexOf("|")>-1)return false ;
	if(str.indexOf(";")>-1)return false ;
	if(str.indexOf("\\")>-1)return false ;
	return true ;
}

/*只能包含中文，英文，数字*/
function containSC2(str){
	var reg = /^(\w|[\u4E00-\u9FA5])*$/; 
	if(str.match(reg)) { 
		return true; 
	}else{ 
		return false; 
	} 
}

/*
*	还原特殊字符
*/
function revertTextCode(strValue){
	if(strValue==null || strValue.length==0){
		return "" ;
	}
	strValue = strValue.replaceAll("&#39;","'") ;
	strValue = strValue.replaceAll("&#34;","\"") ;
	return strValue ;
}

/*
*	还原特殊字符
*/
function revertTextCode2(strValue){
	if(strValue==null || strValue.length==0){
		return "" ;
	}
	strValue = strValue.replaceAll("@39@","'") ;
	strValue = strValue.replaceAll("@34@","\"") ;
	return strValue ;
}

/**
*   转换特殊字符
*/
function encodeTextCode(strValue){
	if(strValue==null || strValue.length==0){
		return "" ;
	}
	strValue = strValue.replaceAll("'","&#39;") ;
	strValue = strValue.replaceAll("\"","&#34;") ;
	return strValue ;
}

function openModalDialog(url,value,width,height)
{
	return window.showModalDialog(url,value,"dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;help:no;resizable:no;status:no;scroll:no;");
}
function openWindow(url,win,width,height){
	var left = screen.width/2 - width/2;
	var top = screen.height/2 - height/2;
	return window.open(url,win,"menubar=no,toolbar=no,scrollbars=no,loaction=no,status=yes,resizable=no,width="+width+",height="+height+",left="+left+",top="+top);
}
/*修改指定form中operation元素的数值*/
function changeOP(form,operationType){
	for(var i=0;i<form.elements.length;i++){
		if(form.elements[i].name=="operation")form.elements[i].value=operationType;
	}
}

function getRadioValue(itemName)
{
    var items = document.getElementsByName(itemName);
	for(var i=0;i<items.length;i++)
	{
	    if(items[i].checked){
		    return items[i].value;
		}
	}
	return "";
}

/*将指定名字的checkbox设置为全选或者不全选*/
var isAllSelectSelected = false;
function checkAll(name){
	items = document.getElementsByName(name);
	for(var i=0;i<items.length;i++){
	    //items[i].checked = !items[i].checked;
	    if(!items[i].disabled){
	       items[i].checked = !isAllSelectSelected;
	    }
	}
  isAllSelectSelected = !isAllSelectSelected;
}

/*取消所有选择*/
function cancelCheck(name){
	items = document.getElementsByTagName(name);
	for(var i=0;i<items.length;i++){
		if(items[i].type == "checkbox"){
	    	items[i].checked = false;
		}
	}
	isAllSelectSelected = false;
}

/*判断指定名字的checkbox是否有已选择的*/
function isChecked(name){	
	items = document.getElementsByName(name);
	for(var i=0;i<items.length;i++){
	    if(items[i].checked)return true;
	}
	return false;
}
//判断选中的项是否有子类
function delHasChild(name){
	var items=document.getElementsByName(name);
	for(var i=0;i<items.length;i++){
    	if(items[i].checked&&items[i].value.split(";").length>1)return true;
	}
	return false;
}
/* 全选一个select中的所有option */
function selectedAllOption(selA)
{
	for(var i=0;i<selA.length;i++)
	{
		selA.options[i].selected = true;
	}
}

function unSelectedAllOption(selA)
{
	for(var i=0;i<selA.length;i++)
	{
		selA.options[i].selected = false;
	}
}

/*将一个select中的option转移到另一个select中*/
function moveOption(selA,selB,isMoveAll){
	for(var i=0;i<selA.length;i++)
	{
		if(selA.options[i].selected || isMoveAll){
		    selB.options[selB.length] = new Option(selA.options[i].text,selA.options[i].value,true,true);
			selA.options[i] = null;
			i--;
		}
	}
}

/*返回字符串真实长度(一个中文字符等于两个英文字符)*/
function strLength(str){
	if(!str)return 0;
	if(str.length==0)return 0;
	var count=0;
	for(var i=0;i<str.length;i++){
		count+=str.charAt(i).len();
	}
	return count;
}

/*去除字符串前后的空格*/
function trimStr(str){
    if(str==null || str.length==0)return"";
	return str.trim();
	var i=0;
	while(str.charAt(i)==" "){
		str = str.substring(i+1,str.length);
	}
	i = str.length-1;
	while(str.charAt(i)==" "){
		str = str.substring(0,str.length-1);
		i--;
	}
	return str;
}

/*
 * @ 取得字符串的真实长度 英文为一个字符,中文为两个字符  
 * @ str	目标字符串  
 * @ 返回 str 的长度  
 */
function getStringLength(str){
	var len = 0;
	if (str.length <= 0){
		len = 0;
	}else{
		for (var i=0;i<str.length;i++){
			if (str.charCodeAt(i) < 256){
				len = len + 1;
			}else{
				len = len + 2;
			}
		}
	}
	return len;
}

/*
 * @ 截取字符串 英文为一个符,中文为两个字符  
 * @ 截取时按中文字符来截取,即参数len是按中文来计算的.英文字符串的长度为(2 * len)
 * @ str	目标字符
 * @ len	截取长度
 * @ chr	字符后缀
 * @ 返回截取后的字符串  
 */
function getTopic(str,len,chr){
	var strLen = getStringLength(str);
	var topic = "";
	var i = 0;
	var j = 0;
	if (strLen > len){
		while (i<len){
			if (str.charCodeAt(i) < 256){
				if (str.charCodeAt(i+1) < 256){
					topic = topic + str.substr(j,2);
					i = i + 1;
					j = j + 2;
				}else{
					topic = topic + str.substr(j,1);
					i = i + 1;
					j = j + 1;
				}
			}else{
				topic = topic + str.substr(j,1);
				i = i + 1;
				j = j + 1;
			}
		}
	}else{
		topic = str;
	}
	if (strLen > len){
		return backChar(topic) + chr;
	}else{
		return backChar(topic);
	}
}

/*
 * @ 替换字符
 */
function replaceChar(str){
	var reStr = "";
	try{
		if (str.length > 0){
			reStr = str.replaceAll("'","{");
			reStr = reStr.replaceAll("\"","}");
		}else{
			reStr = "";
		}
	}catch(e){
	}
	return reStr;
}

/*
 * @ 还原字符
 */
function backChar(str){
	var backStr = "";
	try{
		if (str.length > 0){
			backStr = str.replaceAll("{","'");
			backStr = backStr.replaceAll("}","\"");
		}else{
			backStr = "";
		}
	}catch(e){
		//
	}
	return backStr;
}

/*
 * @ 替换所有匹配的字符(用 replaceText 替换 rgExp)
 * @ rgExp			要被替换的字符/字符串
 * @ replaceText	替换的字符/字符串
 */
String.prototype.replaceAll = function(rgExp,replaceText){
	var re = new RegExp(rgExp,"gi");
	return this.replace(re,replaceText);
}

/*数组移除方法*/
Array.prototype.remove=function(index){ 
	this.splice(index,1); 
}

/*
 * @ 是否含有中文
 */
function hasChinese(str){
	var flag = false;
	if (str.length <= 0){
		flag = false;
	}else{
		for (var i=0;i<str.length;i++){
			if (str.charCodeAt(i) > 255 || str.charCodeAt(i) == 13 || str.charCodeAt(i) == 10){
				flag = true;
			}
		}
	}
	return flag;
}

/*
 * @ 如果字符串中含有中文字,则将所有的字符以中文字符长度来计算
 */
function getLength(str){
	var len = 0;
	if (str.length <= 0){
		len = 0;
	}else{
		//alert(hasChinese(str))
		if (hasChinese(str)){
			for (var i=0;i<str.length;i++){
				if (str.charCodeAt(i) < 256){
					if (str.charCodeAt(i) == 10){
						len = len;
					}else{
						len = len + 2;
					}
				}else{
					len = len + 2;
				}
			}
		}else{
			len = str.length;
		}
	}
	return len;
}

/*
 * @ 重新获得焦点
 * @ formName			表单名
 * @ textFieldName		文本域字段名
 */
function setFocus(formName,textFieldName){
	eval(formName).elements[textFieldName].focus();
	if(typeof(jQuery)!="undefined"){
		jQuery("input").attr("disableautocomplete","disableautocomplete").attr("autocomplete","off");
	}
}

/*
 * @ 将列表框中的值复制到文本域中
 */
function putTextArea(selectValue,textAreaName){
	try{
	document.all(textAreaName).value = selectValue;
	}catch(e){
	}
}

/*
 * 重组字符串  
 */
function toDateString(dateString){

	var regExp1 = "^[0-9]{8}";
	var regExp2 = "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
	var pattern = null;
	var flag = false;
	var n = 0;
	var strDate = "";
	var year,month,day,hour,minute,second;
	if (dateString == "" || dateString == null){
		return "";
	}

	pattern = new RegExp(regExp1);
	flag = pattern.test(dateString);

	if (flag){
		year = dateString.substring(0,4);
		month = dateString.substring(4,6);
		day = dateString.substring(6,9);
		if (month<10)month = month.substring(1);
		if (day<10)day = day.substring(1);
		strDate = year+","+month+","+day;
		return strDate;
	}else{
		pattern = new RegExp(regExp2);
		flag = pattern.test(dateString);
		if (flag){
			year = dateString.substring(0,4);
			month = dateString.substring(5,7);
			day = dateString.substring(8,10);
			if (month<10)month = month.substring(1);
			if (day<10)day = day.substring(1);
			strDate = year+","+month+","+day;
			return strDate;
		}else{
			return "";
		}
	}
}

/*取消所有选择*/
function cancelSelected(name){
	items = document.getElementsByTagName(name);
	for(var i=0;i<items.length;i++){
		if(items[i].type == "checkbox"){
	    	items[i].checked = false;
		}
	}
	isAllSelectSelected = false;
}

function clearForm(form){
	for(var i=0;i<form.elements.length;i++){

		if(form.elements[i].type=="text") {
			if(form.elements[i].name != "pageNo"){
				form.elements[i].value="";
			}
		}
		if(form.elements[i].type=="select-one") {
			form.elements[i].options[0].selected = true;
		}
		if(form.elements[i].type=="textarea") form.elements[i].value="";
	}
}

function keyIsNumber()
{
	var keyCode = window.event.keyCode ;
	//如果输入的字符是在0-9之间，或者是backspace、DEL键  
	if(((keyCode>47)&&(keyCode<58))|| ((keyCode>95)&&(keyCode<105)) || (keyCode==8)||(keyCode==46))
	{

	}
	else if(keyCode==9 || keyCode==13)
	{
		window.event.keyCode=9;
	}
	else
	{
		window.event.returnValue=false;
	}
}

/*查询之前的处理函数*/
function beforeBtnQuery(){
	try{
		form.pageNo.value = 1;
		return true;
	}catch(e){
		//alert(e.message);
		//return false;
	}
}
//将焦点置于第1个文本框
function showStatus(){
	if(typeof(jQuery)!="undefined"){
		jQuery("input").attr("disableautocomplete",true).attr("autocomplete","off");
	}
}

function showtable(tablenamestr){
}

/*删除时确认*/
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


//去除后面几位的字串
function subString(strSource,lnglen){
	var subString ;
	var lngSubLen;
	if (strSource.length < lnglen)
	{
		return  "";
	}
	lngSubLen = strSource.length - lnglen;
	subString = strSource.substring(0,lngSubLen)
	return subString;
}


/**
* 获取光标所在的字符位置
* @param obj 要处理的控件, 支持文本域和输入框
* @author hotleave
*/
function getTextPosition(obj,varLeft){
	var result = 0;
	if(obj.selectionStart||obj.selectionStart =='0'){ //非IE浏览器

	   result = obj.selectionStart;
	}else{ //IE	   
	   var rng;
	   if(obj.tagName == "TEXTAREA"){ //如果是文本域		  
		    var pos = 0;
            var s = obj.scrollTop;
            var r = document.selection.createRange(); 
            var t = obj.createTextRange(); 
            t.collapse(true);
            t.select();
            var j = document.selection.createRange(); 
            r.setEndPoint("StartToStart",j);
            var str = r.text;
            var re = new RegExp("[\\n]","g");
			str = str.replace(re,"");
            result = str.length;
            r.collapse(false);
            r.select();
            obj.scrollTop = s;
	   }else{ //输入框

		  if(obj.readOnly && "left"==varLeft){
		  	return 0 ;
		  }
	      rng = document.selection.createRange();
		  rng.moveStart("character",-event.srcElement.value.length);
	   	  result = rng.text.length;
	   }	   
	}
	return result;
}

DateFormat = (function(){      
 var SIGN_REGEXP = /([yMdhsm])(\1*)/g;      
 var DEFAULT_PATTERN = 'yyyy-MM-dd';      
 function padding(s,len){      
  var len =len - (s+'').length;      
  for(var i=0;i<len;i++){s = '0'+ s;}      
  return s;      
 };   
 return({      
  format: function(date,pattern){      
   pattern = pattern||DEFAULT_PATTERN;      
   return pattern.replace(SIGN_REGEXP,function($0){      
    switch($0.charAt(0)){      
     case 'y' : return padding(date.getFullYear(),$0.length);      
     case 'M' : return padding(date.getMonth()+1,$0.length);      
     case 'd' : return padding(date.getDate(),$0.length);      
     case 'w' : return date.getDay()+1;      
     case 'h' : return padding(date.getHours(),$0.length);      
     case 'm' : return padding(date.getMinutes(),$0.length);      
     case 's' : return padding(date.getSeconds(),$0.length);      
    }      
   });      
  },      
  parse: function(dateString,pattern){      
   var matchs1=pattern.match(SIGN_REGEXP);      
   var matchs2=dateString.match(/(\d)+/g);      
   if(matchs1.length==matchs2.length){      
    var _date = new Date(1970,0,1);      
    for(var i=0;i<matchs1.length;i++){   
     var _int = parseInt(matchs2[i]);   
     var sign = matchs1[i];   
    switch(sign.charAt(0)){      
     case 'y' : _date.setFullYear(_int);break;      
     case 'M' : _date.setMonth(_int-1);break;      
     case 'd' : _date.setDate(_int);break;      
     case 'h' : _date.setHours(_int);break;      
     case 'm' : _date.setMinutes(_int);break;      
     case 's' : _date.setSeconds(_int);break;      
    }   
    }      
    return _date;      
   }      
   return null;      
  }      
 });      
})();    

Date.prototype.addDays = function(d)
{
    this.setDate(this.getDate() + d);
};

function getCurDate()
{
	return DateFormat.format(new Date(),'yyyy-MM-dd');
} 

function decDate(sdate){
	var date = DateFormat.parse(sdate,'yyyy-MM-dd');	
	date.setDate(date.getDate()-1);
	return DateFormat.format(date,'yyyy-MM-dd');
}
function addDate(sdate){
	var date = DateFormat.parse(sdate,'yyyy-MM-dd');	
	date.setDate(date.getDate()+1);
	return DateFormat.format(date,'yyyy-MM-dd');
}

function setBackground(obj,boolean){
	if(boolean){
		obj.style.background="#E7FCA9";
	}else{
		obj.style.background=typeof(obj.bk)!="undefined"?obj.bk:"";
	}
}

/*保存窗口对象,传给弹出窗口*/
var winObj = new Object() ;
winObj.parentWindow = window ;
	 

/**
  键盘快速操作规则：
  1. 在查询和弹出窗ctrl+a键，为全选或全不选 (flash中不起作用)
     要求，界面有一个name为"selAll"的全选框，并已实现全选或全不选功能。
  2。左方向键"<-",在输入框中实现焦点向左移动
     在有上下级的查询或弹出窗中，焦点在某一行上时，实现向上级切换功能(返回按扭id="backSubmitBt")。如果本身为最上级，则为普通焦点左移。
  3。右方向键"->",在输入框中实现焦点向右移动
     在有上下级的查询或弹出窗中，焦点在某一行上时，实现向下级切换功能，如果本身没有下一级，则为普通焦点右移。
  4。上方向键"^",为向上功能切换，
     在日期框中为日期减一天，（要求日期录入框带有属性date=true）日期为空则自动填入当前日期
     焦点在列表或输入表格中时，为垂直向上切换焦点   
     焦点在列表或输入表格的最上一行时，为跳焦点到列表或表格上方的查询条件或主表输入框中。
     焦点在下拉列表中时，为向上切换下拉选项。
  5。下方向键,为向下功能切换，
     在日期框中为日期减加天，（要求日期录入框带有属性date=true）日期为空则自动填入当前日期
     焦点在列表或输入表格中时，为垂直向下切换焦点   
     焦点在列表为跳焦点到列表下方分页栏。
     焦点在输入表格的最下一行时，新增一行。
     焦点在下拉列表中时，为向下切换下拉选项。 
  6. 回车按扭，在有弹出窗时，弹出界面，无弹出窗时，实现焦点向下切换。   
         
*/
function pagekeydown() { 

	// 如果ctrl+a则全选
	if (event.ctrlKey && event.keyCode == 65) {
		var al = document.getElementsByName('selAll');
		for ( i = 0; i < al.length; i++) {
			if (document.createEvent) {
				var evt = document.createEvent("HTMLEvents");
				evt.initEvent("click", false, false);
				al[i].dispatchEvent(evt);
			} else {
				al[i].fireEvent("onClick");
			}

		}
		return;
	}

	var ele = document.activeElement;
	if (ele.type == "text" || ele.type == "checkbox" || ele.name=='Remark') { 
		var v = [13,33,34,38,40];
		if (ele.dropdown != undefined && jQuery.inArray(event.keyCode,v)!=-1) {
			return;
		} else if (event.keyCode == 38) {			// 上
			// 如果是日期框
			if (ele.date == "true") {
				if (ele.value == "") {
					ele.value = getCurDate();
				} else {
					ele.value = decDate(ele.value);
				}
				return;
			}
			if(ele.name=='Remark')
			{
				if(getTextPosition(ele, "left")==0)
				{
					jQuery(":text:eq(0)").focus();
					stopBubble();
					stopDefault();
				}
			}
			var objs = document.getElementsByName(ele.name);
			if (objs.length > 0 && ele == objs[0]) {
				// 回到第一个文本框
				for ( i = 0; i < document.all.length; i++) {
					if (document.all(i).type == "text") {
						document.all(i).focus();
						break;
					}
				}
			} else {
				for ( i = 1; i < objs.length; i++) {
					if (ele == objs[i]) {
						objs[i].parentNode.parentNode.style.background = "";
						objs[i - 1].parentNode.parentNode.style.background = "#E7FCA9";
						objs[i - 1].focus();
						if (event.preventDefault != undefined)
							event.preventDefault();
						break;
					}
				}
			}
			return;
		}
		else if (event.keyCode == 40) {
			// 下			// 如果是日期框
			if (ele.date == "true") {
				if (ele.value == "") {
					ele.value = getCurDate();
				} else {
					ele.value = addDate(ele.value);
				}
				return;
			}

			var objs = document.getElementsByName(ele.name);
			if (objs.length > 1) {
				for ( i = 0; i < objs.length - 1; i++) {
					if (ele == objs[i]) {
						objs[i].parentNode.parentNode.style.background = "";
						objs[i + 1].parentNode.parentNode.style.background = "#E7FCA9";
						objs[i + 1].focus();
						if (event.preventDefault != undefined)
							event.preventDefault();
						break;
					}
				}
			} else if (objs.length == 1) {
				jQuery(".tagSel[name='detTitle']").click();
				stopBubble();
				stopDefault();

			}
			return;
		}
	}
	
	// 左	if (event.keyCode == 37) {
		// 如果是表格中
		if (ele.list == "true") {
			event.keyCode = 0;
			if (document.getElementById("backSubmitBt")) {
				if (document.createEvent) {
					var evt = document.createEvent("HTMLEvents");
					evt.initEvent("click", false, false);
					document.getElementById("backSubmitBt").dispatchEvent(evt);
				} else {
					document.getElementById("backSubmitBt").fireEvent("onClick");
				}

				return;
			}
		}
		if ((ele.tagName == "TEXTAREA" || ele.type == "text" || ele.type == "password") && getTextPosition(ele, "left") != 0)
			return;
		var v = jQuery(":text:not([readonly]),:checkbox,select");
		if(v.index(ele) > 0)
		{
			v[v.index(ele)-1].focus();
			stopDefault(event);
			stopBubble(event);
		}
	} else if (event.keyCode == 39 || (!event.ctrlKey && event.keyCode ==13 && (ele.name!='scanBarcode' && ele.name.indexOf('_Seq_hid')==-1) )) {
		// 如果是表格中
		// 右
		if (ele.list == "true") {
			event.keyCode = 0;
			if (ele.value.indexOf("hasChild") != -1) {
				if (document.createEvent) {
					var evt = document.createEvent("HTMLEvents");
					evt.initEvent("dblClick", false, false);
					ele.parentNode.dispatchEvent(evt);
				} else {
					ele.parentNode.fireEvent("onDblClick");
				}
				return;
			}

		}
		
		if ((ele.tagName == "TEXTAREA" || ele.type == "text" || ele.type == "password") && getTextPosition(ele) != ele.value.length && event.keyCode==39) {
			return;
		}
		var v = jQuery(":text:not([readonly]),:checkbox,select");
		if(v.index(ele)== -1){
		}else if(v.length > v.index(ele)+1)
		{ 
			v[v.index(ele)+1].focus();
			stopDefault(event);
			stopBubble(event);
		}else
		{
			v[0].focus();
			stopDefault(event);
			stopBubble(event);
		}
	}else if(event.ctrlKey && event.keyCode ==13)
	{
		if(ele.name!=undefined )
		{
			var v = jQuery("[name='"+ele.name+"']");
			if(v.index(ele) < v.length-1)//有多个往下一个跳
			{
				jQuery(v[v.index(ele)+1]).parents("tr").find(":text:eq(0)").focus();
			}
		}
	}
}

function showObject(obj) {
	var ms = []
	for(var m in obj) ms.push(m)
	ms.sort()
	var html = ""
	for (var i in ms) {
		var m = ms[i]
		html += m + ": " + obj[m] + "\n"
	}
	html += "";
	return (html)
}

//用于flash中焦点切换加html对象中

function focusbackhtml(){
	for(i=0;i<document.all.length;i++){
		if(document.all(i).type == "text"){
			document.all(i).focus();
			break;
		}
  }
}

function mainFocus(obj){
	if(obj.tagName !="SELECT"){
		obj.select();
	}
	$(".inputFocus").removeClass("inputFocus");
	$(obj).addClass("inputFocus");
}



function bodyload(){ 
	if (document.forms.length>0 && document.forms[0].addEventListener){ 
		document.forms[0].addEventListener('keydown', pagekeydown, false);
	} else if (document.forms.length>0 && document.forms[0].attachEvent){ 
		document.forms[0].attachEvent('onkeydown', pagekeydown);
	}
}

if (window.addEventListener){
	window.addEventListener('load', bodyload, false);
} else if (window.attachEvent){
	window.attachEvent('onload', bodyload);
}

function initSelectOption(){
	var objSelect = document.getElementsByTagName("select") ;
	//alert(objSelect.length) ;
}





function closeWindows(pid){
	if(!window.save && is_form_changed()){
		if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
			return;
		}
	}
	if(typeof(pid) == 'undefined' || pid == ""){
		pid = 'PopMainOpdiv';
	}
	closeWin(pid);
}


function openLink(url){
	width=document.documentElement.clientWidth-100;
	height=document.documentElement.clientHeight-50;
	
	if($("#tableName").size() >0){
		//这是数据表列表
		url+="&reportCond="+$("#tableName").val()+moduleType;
	}else{
		//报表
		url+="&reportCond="+reportNumber;
	}

	if(url.indexOf("&src=")  == -1){
		url = url+"&src=menu";
	}
	if(url.indexOf("&noback=")  == -1){
		url = url+"&noback=true";
	}
	var now=new Date(); 
	var number = now.getTime();
	
	
	openPop('openLinkUrl'+number,'',url,width,height,false,false);
}

/*
*	层弹出窗口
*	id: 弹出窗口ID
*	title:	弹出窗口标题
*	usrc:	弹出窗口连接
*	width:	弹出窗口宽度
*	height:	弹出窗口高度
*	checkFormChange:是否检查表单变化验证
*	noTitle:  去弹出窗口的标题头
*/
function openPop(id,title,usrc,width,height,checkFormChange,noTitle){
	if(height > document.documentElement.clientHeight-20){
		height=document.documentElement.clientHeight-20;
	}
	if(width > document.documentElement.clientWidth-20){ 
		width=document.documentElement.clientWidth-20;
	}
	asyncbox.open({
		id:id,title:title,width:width,height:height,
	　　html : '<div  style="text-align: center;width: 100%;	height: 100%;border:none">'
			  + '<iframe id="'+id+'_Frame" frameborder="no" border="0" marginwidth="0" marginheight="0" style="width: 100%;	height: 99%;top:0px;left:0px;z-index:-1;border:none;filter:Alpha(opacity=100);"></iframe>'
			  + '<div id="'+id+'_Frame_1" style="position:absolute;top:'+(height/2-80)+'px;left:'+(width/2-80)+'px;text-align: center;width:188px;height:128px;line-height:230px;font-weight:bold;color:#007eff;background: url(/style1/images/ll.gif) no-repeat center;">正在加载，请稍候...</div></div>',		
		callback : function(action,opener){
	  		if(action == 'close' && checkFormChange){
	  			iframe1 = document.getElementById(id+"_Frame");
	  			if(iframe1 &&  iframe1.contentWindow.is_form_changed()){
					if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
						return false;
					}
				}
	  		}
  	  	}
　	});	 
	if(noTitle){ //全屏界面去掉上面的title
		$("#"+id+" .b_t_m").parent().remove();
		//设置框架没有按扭后的高度
	}
	$("#"+id+"_content").height($("#"+id).height()-7);
	

	var iframe = document.getElementById(id+"_Frame");
	if(iframe==null)
		iframe = parent.document.getElementById(id+"_Frame");
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

 //本方法用于在打开单据列表时让条件查询按扭获取焦点，以前条件输入界面和列表页在同一界面时，第一个输入框能获得焦点
 //现在改为弹出了，如界面不能获得焦点，则无法使用快捷方式。
function setListFocus(){
	query = document.getElementById("queryCondition");
	if(typeof(query) != 'undefined' && query != null){
		query.focus();
	}
} 

//本方法用于添加工作流转交，从alert.jsp中的goResultPage()方法调用
function deliverTo(url,retValUrl){
	$("#retValUrl").val(retValUrl);
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : url,
	 	title : '转交',
　　 　 	width : 650,
　　 	height : 370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    if(action == 'ok'){
			if(opener.beforSubmit(opener.form)){
				opener.form.submit();
			}
			return false;
		}else if(action == 'close'){
			window.location.href = retValUrl ;
		}else if(action == 'cancel'){
			window.location.href = retValUrl ;
		}
　　  　}
　 });
}

//本方法用于工作流转交弹出页面点击确定后处理的函数，用来关闭弹出框口与刷新页面
function dealAsyncbox(){
	var retValUrl = $("#retValUrl").val();
	jQuery.close('dealdiv');//根据弹出框ID关闭窗口，
	window.location.href = retValUrl;
}

/*判断是否有数据选中*/
function hasCheck(keyId){
	var hasCheck = $("input:[name='"+keyId+"']:checked").val() ;
	if(typeof(hasCheck)=="undefined"){
		return false ;
	}
	return true ;
}


/*判断长度是否合格 
  s:字符串
  n:字节长度	
*/
function WidthCheck(s, n){   
	var w = 0;   
	for (var i=0; i<s.length; i++) {   
	   var c = s.charCodeAt(i);   
	   //单字节加1    
	   if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {   
	    w++;   
	   }   
	   else {   
	    w+=2;   
	   }   
	}   
	if (w > n) {   
	   return false;   
	}   
	return true;   
}  

/*检查是否有iframe,区别与直接进入与客户列表详情*/
function checkHasFrame(){
	if(typeof(window.parent.frames["pindex"]) != "undefined" || typeof(window.parent.frames["bottomFrame"]) != "undefined"){
		$("#hasFrame").val("true");
	}
}

/*functionAdd.jsp与functionDetail.jsp 返回列表函数*/
function returnList(obj){
	var url = $(obj).attr("url");//默认返回的url
	var hasFrame = $("#hasFrame").val();//是否存在名为"pindex"的frame框架
	url += "&hasFrame=" +hasFrame;
	window.location.href = url;
	
}

//打印控件弹出框
function printControl(urls){ 
	asyncbox.open({id:'printControlDiv',title:'打印弹出框',url:urls,width:300,height:280});
}

//用于打印控件、打印样式关闭
function closeAsyn(){
	var popDiv = $(".asyncbox_normal",parent.document);
	var popId = popDiv.last().attr("id");
	parent.jQuery.close(popId);
}

//在文字后加空格，chrome下实现两端对齐
var cyh = {}
cyh.lableAlign = function(){
	var nLength = new Array();
	var reg = /^[u4E00-u9FA5]+$/;
	jQuery('.d_test').each(function(){
		var a = jQuery(this).text();
		var str = '';
		var zStr = '';
		for(var i=0; i<a.length;i++){
			str += a.charAt(i)+' ';
			if(!reg.test(a.charAt(i))){
				zStr += a.charAt(i);
			}
		}
		nLength.push(zStr.trim().length);
		jQuery(this).text(str.trim());	
		if(oCheck(this) == false){
			jQuery(this).addClass("test");
		}else{
			jQuery(this).addClass("d_mid");
		}
	});
	var MaxLength = nLength.sort(function number(a,b){return b-a;})[0];
	if(MaxLength <= "4" == true){
		jQuery(".d_box").each(function(){
			jQuery(this).parent(".swa_c1").addClass("d_4");
		});
	}else{
		jQuery(".d_box").each(function(){
			jQuery(this).parent(".swa_c1").addClass("d_6");	
		});
	}
}
//检查字符串是否含有英文(lableAlign()方法中使用)
function oCheck(obj)
{
	var reg = /^[u4E00-u9FA5]+$/;
	var aTxt = jQuery(obj).text().trim();
	for(var i=0; i<aTxt.length;i++){
		if(reg.test(aTxt.charAt(i))){
			return false;
			break;
		}
	}
}
//明细 表格宽度对齐-布匹版
function bpWidth()
{
	var a = $(".listRange_list_function_b:eq(0)").width();
	var b = $(".listRange_list_function_b:eq(1)").width();
	if(a > b)
	{ 
		var Awh = $(".listRange_list_function_b:eq(1)").attr("width");
		$(".listRange_list_function_b:eq(1)").css("width",parseInt(Awh)+(a-b));
	}
	$(".listRange_list_function_b").each(function(){
		$(this).find("thead tr").each(function(){
			var sHeight = $(this).height();
			if(sHeight == 2)
			{
				$(this).css("display","none").siblings("tr").find("td").css("height","52px");
			}
		})
	});
}
//自定义页面表单ul最大宽度
function ulMaxWidth(){
	var sW = $(document.body).outerWidth(true);
	if(sW>1100){
		$(".wp_ul").css("max-width","1100px");
	}
}

/*添加物料明细调用父类弹出框,为了弹出框不在Iframe里面被遮住*/
function bomDetPop(urlstr,display){
	asyncbox.open({id:'BomDetPop',title:display,url:urlstr,width:750,height:470});
}

/*物料明细回填调用iframe的回填方法*/
function exeBomDetPop(returnValue){
	window.frames["mainFrame"].exePopdiv(returnValue); 
}

/*处理预警设置与定时通知设置，设置弹出框操作成功后刷新页面*/
function dealStatus(){
	window.location.reload();
}




//判断字符串长度
String.prototype.getBytesLength = function() {   
    var totalLength = 0;     
    var charCode;  
    for (var i = 0; i < this.length; i++) {  
        charCode = this.charCodeAt(i);  
        if (charCode < 0x007f)  {     
            totalLength++;     
        } else if ((0x0080 <= charCode) && (charCode <= 0x07ff))  {     
            totalLength += 2;     
        } else if ((0x0800 <= charCode) && (charCode <= 0xffff))  {     
            totalLength += 3;   
        } else{  
            totalLength += 4;   
        }          
    }  
    return totalLength;   
} 



//阻止冒泡事件
function stopEvent(){ 
	 //取消事件冒泡 
	 var e=arguments.callee.caller.arguments[0]||event; //若省略此句，下面的e改为event，IE运行可以，但是其他浏览器就不兼容
	 if (e && e.stopPropagation) { 
	  e.stopPropagation(); 
	 } else if (window.event) { 
	  window.event.cancelBubble = true; 
	 } 
}

/**
比较两个日期相差的天数，可为负值

**/
function DateDiff(sDate1, sDate2){ 
	iDays = parseInt(Math.abs(sDate1 - sDate2) / 1000 / 60 / 60 /24);  
	if((sDate1 - sDate2)<0){
		return -iDays;
	}
	return iDays; 
}
//阻止浏览器冒泡
function stopBubble(e) {
    if ( e && e.stopPropagation )
        e.stopPropagation();
    else
        window.event.cancelBubble = true;
}
//阻止浏览器的默认行为
function stopDefault( e ) {
    if ( e && e.preventDefault )
        e.preventDefault();
    else
        window.event.returnValue = false;
    return false;
}

/*
 me:jquery对象
 count:闪烁的次数倍数
 调用例子:highlight($("#userName"),5);
*/
function highlight(me,count){
	var count = count-1;
	if(count<=0){
		me.css('background-color' , "");
		return;
	}
	if(count%2==0){
		me.css('background-color' , "#ffcccc");
	}else{
		me.css('background-color' , "");
	}
	setTimeout(function(){
		highlight(me,count);
	},200);
}
function showPIC(pic){
	//显示图片	
	pic = pic.replace("YS=true","");
	top.showPICTop(pic);
}
//添加修改界面的显示图片  
function showInpImg(obj){
	var src = $(obj).attr("osrc");
	if(src != ""){
		showPIC(src);
	}
}

//设置上一条，下一条  
function setNextPos(){
	//决定是否有上下条按扭
	if(typeof(parent.window.idListArray) == "undefined"){
		$("#preOne").hide();
		$("#nextOne").hide();
	}else{
		if(parent.window.idListArray.length ==0){
			parent.window.$("input[name=keyId]").each(function(){
				parent.window.idListArray[parent.window.idListArray.length]=$(this).val();
			});
		}
		var nid = $("#varKeyIds").val();
		if(nid != ""){
			var npos = jQuery.inArray(nid,parent.window.idListArray);
			if(npos == -1){
				parent.window.idListArray.splice(0, 0, nid);
			}
		}	
	}
}
//上一条下一条
function changePos(type){
	var curId = $("#varKeyIds").val();
	curid =curId.split(";")[0];
	curPos = jQuery.inArray(curid,parent.window.idListArray);
	if(type=="PRE"){
		
		if(curPos==0){
			alert('本页没有上一条数据了');
			return;
		}else{
			vid = parent.window.idListArray[curPos-1];
			vid = vid.split(";")[0];
			href = window.location.href;
			pos = href.indexOf("keyId=")+6;
			href = href.substring(0,pos)+vid+href.substring(href.indexOf("&",pos));
			window.location.href=href;
		}
	}else{
		if(curPos==parent.window.idListArray.length-1){
			alert('本页没有下一条数据了');
			return;
		}else{
			vid = parent.window.idListArray[curPos+1];
			vid = vid.split(";")[0];			
			href = window.location.href;
			pos = href.indexOf("keyId=")+6;
			href = href.substring(0,pos)+vid+href.substring(href.indexOf("&",pos));
			window.location.href=href;
		}
	}
}
//取当前时间加减多少天
function getDateStr(AddDayCount) 
{ 
	var dd = new Date(); 
	dd.setDate(dd.getDate()+Number(AddDayCount));//获取AddDayCount天后的日期 
	var y = dd.getFullYear()+"";  
	var m = (dd.getMonth()+1)+"";//获取当前月份的日期 
	var d = dd.getDate()+""; 
	return y+"-"+(m.length==1?"0"+m:m)+"-"+(d.length==1?"0"+d:d); 
} 
//指定时间加减多少天，type ,day 按日加减，month按月加减


