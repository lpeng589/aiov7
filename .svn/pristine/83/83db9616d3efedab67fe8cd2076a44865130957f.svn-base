

//特殊字符转码 encodeURIComponent 这个方法可以自动解析
function encodiURILocal(data) {
	//var d = encodeURI(data).replace(/&/g,'%26').replace(/\+/g,'%2B').replace(/\$/g,'%20').replace(/&/g,'%26');
	var d = data.replace(/\"/g,'').replace(/\'/g, '').replace(/\\/g, '');
	return d;
}
//判断是否存在特殊字符
function isHaveSpecilizeChar(data){
	var i = data.indexOf("\"");
	var j = data.indexOf("\'");
	var m = data.indexOf("\\");
	if (i >= 0 || j >= 0 || m >= 0) {
		return false;
	}
	return true;
}

//判断是否存在特殊字符
function isHaveSpecilizeChar2(data){
	var i = data.indexOf("\"");
	var j = data.indexOf("\'");
	var m = data.indexOf("\\");
	var n = data.indexOf("\/");
	var c0=data.indexOf("@");
	var c1=data.indexOf("#");
	var c2=data.indexOf("$");
	var c3=data.indexOf("%");
	var c4=data.indexOf("^");
	var c5=data.indexOf("&");
	var c6=data.indexOf("*");
	var c7=data.indexOf("~");
	var c8=data.indexOf("!");
	var c8=data.indexOf(";");
	var c9=data.indexOf("-");
	if (i >= 0 || j >= 0 || m >= 0 || n >= 0 ||c0>=0 || c1>=0 || c2>=0
		||c3>=0||c4>=0||c5>=0||c6>=0||c7>=0||c8>=0 || c9>=0) {
		return false;
	}
	return true;
}

//判断是否存在特殊字符
function isHaveSpecilizeChar3(data){
	var i = data.indexOf("\"");
	var m = data.indexOf("\\");
	var n = data.indexOf("\/");
	if (i >= 0 || m >= 0 || n >= 0) {
		return false;
	}
	return true;
}


function regSpecilizeChar(name,data){
     var reg=/[@#\$%\^&\*\+\=\|\-\_\)\(\!\~\`\\\/\.\,\<\>\;\'\:\"\ ]+/g ;
     if(reg.test(data)){
            alert(name+"含有特殊字符");
             return false;
     }
     return true;
}

function substr(str, len){
	if(!str || !len) { return ''; }

	//预期计数：中文2字节，英文1字节
	var a = 0;

	//循环计数
	var i = 0;

	//临时字串
	var temp = '';

	for (i=0;i<str.length;i++){
		if (str.charCodeAt(i)>255){
			//按照预期计数增加2
			a+=2;
		}
		else
		{
			a++;
		}
		//如果增加计数后长度大于限定长度，就直接返回临时字符串
		if(a > len) { return temp; }

		//将当前内容加到临时字符串
		temp += str.charAt(i);
	}
	//如果全部是单字节字符，就直接返回源字符串
	return str;
}
//根据id,获得值
function getValueById(value){
	return document.getElementById(value) ;
}


/*将指定名字的checkbox设置为全选或者不全选*/
var isAllSelectSelectedShare = false;
function checkAllShare(name){
	var items = document.getElementsByName(name);
	for(var i=0;i<items.length;i++){
	    //items[i].checked = !items[i].checked;
	    items[i].checked = !isAllSelectSelectedShare;
	}
	
  isAllSelectSelectedShare = !isAllSelectSelectedShare;
}
//全选
function isCheckAll(isCheckAll,item) {
	
	//选择所有
	var phos = document.getElementsByName(item);
	for (var i = 0 ; i < phos.length; i++ ) {
		var c  = phos[i];
		if (c.type == 'checkbox') {
		   if( c.disabled == false ) {
		   	   if (isCheckAll == 'false') {
		  	 	   c.checked = false;
		  	 	   document.getElementById("checkAll").value = true;
		   	   } else {
		  		   c.checked = true;
		   	   	   document.getElementById("checkAll").value = false;
		   	   
		   	   }
		    }	
		}
	}
}
//获取坐标
function mouseCoords(ev){
		if(ev.pageX || ev.pageY){
			return {x:ev.pageX, y:ev.pageY};
		}
		return {
			x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
			y:ev.clientY + document.body.scrollTop  - document.body.clientTop
		};
}


/*处理通知指定对象函数
popname 表示是哪个选择进入 deptGroup表示部门 userGroup表示个人 empGroup表示职员分组
fieldName 传的是<select>标签的名字
fieldNameIds 隐藏域的ID,用于把相关ID传到后台处理
flag 用于表示是否需要过滤检查  默认为1表示不用  2表示进入知识中心  3表示规章制度
*/

var fieldNames;
var fieldNIds;
var flag;
function deptPop(popname,fieldName,fieldNameIds,flag){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	var titles = "请选择部门";
	if(popname == "userGroup"){
		titles = "请选择个人"
	}
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	flag=flag;
	asyncbox.open({
	id : 'Popdiv',
	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。
　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds,flag)
　　　　　	}
　　　	}
　	});
}


//双击回填数据(适用于多选下拉框),
function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,flag);
	jQuery.close('Popdiv');
}
/*处理通知指定对象集合函数
employees 把( 部门 或 个人 或 职员 )的集合进行处理
fieldName 传的是<select>标签的名字
fieldNameIds 隐藏域的ID,用于把相关ID传到后台处理
flag 用于表示是否需要过滤检查  默认为1表示不用  2表示进入知识中心  3表示规章制度
*/
function newOpenSelect(employees,fieldName,fieldNameIds,flag){
	var employees = employees.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = getValueById(fieldName).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++){
				if(existOption[i].value==field[0]){
					talg = true;
				}
			}
			if(!talg){
				getValueById(fieldName).options.add(new Option(field[1],field[0]));
				getValueById(fieldNameIds).value+=field[0]+",";
			}
		}
	}
}

/*新闻中心、通知通告、规章制度高级查询，创建人弹出框*/
var fieldNames;
var fieldNIds;
function deptPopForAccount(popname,fieldName,fieldNameIds){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=radio";
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
	 title : '请选择人员',
	 	 id : 'Popdiv',
	　　　url : urls,
	　　　width : 755,
	　　　height : 430,
		 btnsbar :[{text:'清空',action:'remove'}].concat(jQuery.btn.OKCANCEL),
		 callback : function(action,opener){
　　　　　	//判断 action 值。　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。				var str = opener.strData;
				newOpenSelectSearch(str,fieldName,fieldNameIds);
　　　　　	}
			if(action == "remove"){ //清空数据
				removeData(fieldName,fieldNameIds);
			}
　　　	}
　	});
}

/*清空数据*/
function removeData(fieldName,fieldNameIds){
	$("#"+fieldName).val("");
	$("#"+fieldNameIds).val("");
}

//单选
function newOpenSelectSearch(str,fieldName,fieldNameIds){
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			getValueById(fieldName).value = field[1];
			var existOption = getValueById(fieldNameIds);
			if(existOption!=null && typeof(existOption)!="undefined"){
				getValueById(fieldNameIds).value = field[0];
			}
		}
	}
}
/*结束*/

/*为下拉框的常用方法  --- Begin*/

/*左右移动*/
function MoveOrDelete(selectFields,showFields){
	var showIndex=$("#"+showFields +" option:selected").index(); //获取右侧下拉框选中的索引值    var len1=$("#"+showFields +" option ").size();
    var len2=$("#"+selectFields +" option:selected").size();
    if(len1+len2>10 && selectFields=="SelectTables" && showFields=="ShowTables" ){
    	alert("邻居表最多只能选择10个!");
    	return false;
    }
	if(showIndex==-1){
		$("#"+selectFields +" option:selected").appendTo("#"+showFields);
	}else{
		$("#"+selectFields +" option:selected").insertAfter("#"+showFields +" option:selected:last");
	}
}

/*移动到顶部*/
function MoveFirst(selectFields){
	var index=$("#"+selectFields +" option:selected").index(); //获取当前元素的索引值
	var Len=$("#"+selectFields +" option:selected").length;
	if(index!=0 && index!=Len-1){
		$("#"+selectFields +" option:selected").insertBefore("#"+selectFields+" option:first");
	}
}

/*移动到底部*/
function MoveLast(selectFields){
	var index=$("#"+selectFields +" option:selected").index();  //获取当前元素的索引值
	var len=$("#"+selectFields +" option").length;  //获取总行数
	if(len-1!=index){
		$("#"+selectFields +" option:selected").insertAfter("#"+selectFields+" option:last");
	}
}

/*上移*/
function MoveUp(selectFields){
	$("#"+selectFields +" option:selected").insertBefore($("#"+selectFields +" option:selected:first").prev());
}

/*下移*/
function MoveDown(selectFields){
	$("#"+selectFields +" option:selected").insertAfter($("#"+selectFields +" option:selected:last").next());
}

/*下拉列表的批量删除操作*/
function deleteOpation(fileName,popedomId){
	var index = jQuery("#"+fileName+" option:selected").size();
	if(index==0){
		alert("请选择要移除的项!");
		return false;
	}
	jQuery("#"+fileName+" option:selected").remove();
	getShowContent(fileName,popedomId);
}


/*下拉列表的批量删除操作 在使用AsyncBox弹出层时 用这个方法*/
function deleteOpation2(fileName,popedomId){
	var index = $("#"+fileName+" option:selected",document).size();
	if(index==0){
		alert("请选择要移除的项!");
		return false;
	}
	$("#"+fileName+" option:selected",document).remove();
	getShowContent2(fileName,popedomId);
}

/*获取指定下拉列表的值，并为指定的参数赋值*/
function getShowContent(showOption,param){
	var showContent="";
	jQuery("#"+showOption+" option").each(function(){
	   showContent+= this.value+",";
	});
	jQuery("#"+param).attr("value",showContent);
}


/*获取指定下拉列表的值，并为指定的参数赋值*/
function getShowContent2(showOption,param){
	var showContent="";
	$("#"+showOption+" option",document).each(function(){
	   showContent+= this.value+",";
	});
	$("#"+param,document).attr("value",showContent);
	
}


/*------------end*/

/*页面让删除按钮失效（除输入框）*/
function keyDown(e) { 
	var iekey=event.keyCode; 
	if(iekey==8){
		if(!(event.srcElement.tagName=="INPUT" && event.srcElement.readOnly == false && event.srcElement.type =="text")){
			return false;
		}
	}
}

/*复选框选中该列*/
var isAllSelectSelected = false;
function checkColumn(colId,vars){
	var flag=vars.checked;
	items = document.getElementsByTagName("INPUT");
	for(var i=0;i<items.length;i++){		
		if(items[i].type =="checkbox" && items[i].getAttribute("mcd") != undefined ){
			if(colId==items[i].getAttribute("mcd")){
				items[i].checked = flag;
			}
		}		
	}
	isAllSelectSelected = !isAllSelectSelected;
}
