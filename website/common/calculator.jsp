<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>无标题文档</title>
<style type="text/css">
.mod_lifebig{padding:40px 0}
.mod_1b{background:#f7fbfe}
#calculator_base input{color:#fff;border:0px;font-family:"microsoft yahei",Helvetica,sans-serif;}
#calculator_base{position:relative;margin:0 auto;width:260px;}
#calculator_base_container{width:260px;height:275px;background:url(/style/images/calculator/siverbg.png) no-repeat 0 0;}
/* table */
#base_table_main td {height:29px;}
.calculator_table {width:100%; margin:0 auto}
.calculator_table td {text-align:center;padding:0px;margin:0px;}
.topRowCss td {height:30px;*height:32px;color:#FFF;padding-top:3px;}
#baseEprsPanel,#completeEprsPanel {text-align:right;}
/* button */
.baseBtnCommonCss {width:40px;height:19px;background:url(/style/images/calculator/d1btn.gif) no-repeat 0 -19px;font-size:12px;color:#FFF;}
.baseBtnCss1 {background-image:url(/style/images/calculator/d0btn.gif);}
.baseBtnCss2 {background-image:url(/style/images/calculator/d2btn.gif);font-size:12px;}
.baseBtnCss3 {background-image:url(/style/images/calculator/d3btn.gif);font-size:12px;}
.baseBtnCss4 {background-image:url(/style/images/calculator/d4btn.gif);font-size:12px;}
/* common */
#calculator_base .displayCss{width:220px;height:30px;color:#000;text-align:right;font-size:20px;padding:0 5px;*line-height:140%; /* for ie */}
</style>
<script type="text/javascript">
function FloatAdd(arg1,arg2){   
	var r1,r2,m;   
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}   
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}   
	m=Math.pow(10,Math.max(r1,r2))   
	return (arg1*m+arg2*m)/m   
}
function FloatSub(arg1,arg2){   
	var r1,r2,m,n;   
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}   
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}   
	m=Math.pow(10,Math.max(r1,r2));   
	//动态控制精度长度   
	n=(r1>=r2)?r1:r2;   
	return ((arg1*m-arg2*m)/m).toFixed(n);   
}
 //浮点数乘法运算   
function FloatMul(arg1,arg2)    
{    
	var m=0,s1=arg1.toString(),s2=arg2.toString();    
	try{m+=s1.split(".")[1].length}catch(e){}    
	try{m+=s2.split(".")[1].length}catch(e){}    
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)    
}    
//浮点数除法运算   
function FloatDiv(arg1,arg2){    
	var t1=0,t2=0,r1,r2;    
	try{t1=arg1.toString().split(".")[1].length}catch(e){}    
	try{t2=arg2.toString().split(".")[1].length}catch(e){}    
	with(Math){    
		r1=Number(arg1.toString().replace(".",""))    
		r2=Number(arg2.toString().replace(".",""))    
		return (r1/r2)*pow(10,t2-t1);    
	}    
}

// global variables
var g_type = 0;
var endNumber = true;
var mem = 0,carry = 10,layer = 0;
var hexnum = "0123456789abcdef";
var angle = "d",stack = "",level = "0";
var $c_get = function(tagId){return document.getElementById(tagId);}
var lastOperator = "";
var isMaxLen = false;
//数字键
function inputkey(key,ent){
	if(isMaxLen == false){//alert(isMaxLen);

	// input express by key
	if(lastOperator == "="){
		$c_get("completeEprsPanel").innerHTML = key;
	}else{
		if(lastOperator == "m"){
			$c_get("completeEprsPanel").innerHTML = "";
		}
		$c_get("completeEprsPanel").innerHTML += key;
	}

	var index=key.charCodeAt(0);
	if ((carry == 2 && (index == 48 || index == 49)) || (carry == 8 && index >= 48 && index <= 55) || (carry == 10 && (index >= 48 && index <= 57 || index == 46)) || (carry == 16 && ((index >= 48 && index <= 57) || (index >= 97 && index <= 102))))
	if(endNumber){
		endNumber = false;
		if(index == 46){
			if(document.calc.display.value.indexOf(".") != -1){
				//
			}else{
				document.calc.display.value += key;
			}
		}else{
			document.calc.display.value = key;
		}
	}
	else if(document.calc.display.value == null || document.calc.display.value == "0")
		if(index == 46){
			if(document.calc.display.value.indexOf(".") != -1){
				//
			}else{
				document.calc.display.value += key;
			}
		}else{
			document.calc.display.value = key;
		}
	else{
		if(index == 46){
			if(document.calc.display.value.indexOf(".") != -1){
				//
			}else{
				document.calc.display.value += key;
			}
		}else{
		document.calc.display.value += key;
		}
		}
		//document.calc.display.value += key;
	
	//
	lastOperator = "";
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
	
	if(document.calc.display.value.length > 16){
		isMaxLen = true;
	}
	
	}// isMaxLen
	else{
		alert("只能输入不大于17位的字符");
	}
}


//运算符
function operation(join,newlevel,ent){
	endNumber = true
	var temp = stack.substr(stack.lastIndexOf("(") + 1) + document.calc.display.value;	
	while (newlevel != 0 && (newlevel <= (level.charAt(level.length - 1)))){
		temp=parse(temp);
		level=level.slice(0,-1);
	}

			if (temp.match(/^(.*\d[\+\-\*\/\%\^\&\|x])?([+-]?[0-9a-f\.]+)$/));
				document.calc.display.value = RegExp.$2;
//			}
//		}
	stack = stack.substr(0,stack.lastIndexOf("(") + 1) + temp + join;
	document.calc.operator.value = " "+ join +" ";
	level = level + newlevel;

	// add express by operator
	$c_get("completeEprsPanel").innerHTML += document.calc.operator.value;
	lastOperator = join;
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
	
	isMaxLen = false;
}

function disbracket(obj,ent){
	endNumber = true;
	var temp = stack.substr(stack.lastIndexOf("(") + 1) + document.calc.display.value;

	while ((level.charAt(level.length - 1)) > 0)
	{
		temp=parse(temp);
		level=level.slice(0,-1);
	}	
	document.calc.display.value = temp;
	
	stack = stack.substr(0,stack.lastIndexOf("("));
	document.calc.operator.value = "   ";
	level = level.slice(0,-1);
	layer -= 1;
	if (layer > 0)
		document.calc.bracket.value = "(=" + layer;
	else
		document.calc.bracket.value = "";
		
	if(lastOperator == "="){
		$c_get("completeEprsPanel").innerHTML = obj.value;
	}else{
		if(lastOperator == "m"){
			$c_get("completeEprsPanel").innerHTML = "";
		}
		$c_get("completeEprsPanel").innerHTML += obj.value;
	}
	lastOperator = obj.value;
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
}
//等号
function result(ent){
	endNumber = true;
	while (layer > 0)
		disbracket();
	var temp = stack + document.calc.display.value;

	while((level.charAt(level.length - 1)) > 0){
		temp=parse(temp);
		level=level.slice(0,-1);
	}
	document.calc.display.value = temp;//parseFloat(temp).toLocaleString();//temp
	document.calc.bracket.value = "";
	document.calc.operator.value = "";
	stack = "";
	level = "0";
	
	// clear express
	lastOperator = "=";
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
	
	isMaxLen = false;
}
//修改键
function backspace(ent){
	try{
		document.getElementById("completeEprsPanel").innerHTML = document.getElementById("completeEprsPanel").innerHTML.substring(0,document.getElementById("completeEprsPanel").innerHTML.length-1);
	}catch(e){}
	if (!endNumber){
		if(document.calc.display.value.length > 1){
			document.calc.display.value = document.calc.display.value.substring(0,document.calc.display.value.length - 1);
		}else{
			document.calc.display.value = 0;
		}
	}
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
	
	if(document.calc.display.value.length <= 16){
		isMaxLen = false;
	}
}
function clearall(ent){
	document.calc.display.value = 0;
	endNumber = true;
	stack = "";
	level = "0";
	layer = "";
	document.calc.operator.value = "";
	document.calc.bracket.value = "";
	
	document.getElementById("completeEprsPanel").innerHTML = "";
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
	
	isMaxLen = false;
}

function inputChangAngle(angletype,ent){
	endNumber = true
	angle = angletype
	if (angle=="d")
		document.calc.display.value = radiansToDegress(document.calc.display.value)
	else
		document.calc.display.value = degressToRadians(document.calc.display.value)
	endNumber = true
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
}

//存储器部分
function clearmemory(ent){
	mem = 0
	document.calc.memory.value = "   "

	var evnt = ent || window.event;
	var target = evnt.srcElement || evnt.target;
	target.blur();
}
function getmemory(ent){
	endNumber = true
	document.calc.display.value = decto(mem,carry)
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
}
function putmemory(ent){
	endNumber = true
	if (document.calc.display.value != 0)
	{
		mem = todec(document.calc.display.value,carry)
		document.calc.memory.value = " M "
	}
	else
		document.calc.memory.value = "   "
	
	lastOperator = "m";
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
}
function addmemory(ent){
	endNumber = true
	mem = parseFloat(mem)+parseFloat(todec(document.calc.display.value,carry))
	if (mem==0)
		document.calc.memory.value = "   "
	else
		document.calc.memory.value = " M "
	
	lastOperator = "m";
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
}
function multimemory(ent){
	endNumber = true
	mem = parseFloat(mem)*parseFloat(todec(document.calc.display.value,carry))
	if (mem==0)
		document.calc.memory.value = "   "
	else
		document.calc.memory.value = " M "
	
	lastOperator = "m";
	
	try{
		var evnt = ent || window.event;
		var target = evnt.srcElement || evnt.target;
		target.blur();
	}catch(e){}
}


function deg(n){
	var neg = (n<0)
	with(Math)
	{
		n = abs(n)
		var d = floor(n)
		var m = floor((n-d)*100)
		var s = (n-d)*100-m
	}
	var deg = d+m/60+s/36
	if (neg) 
		deg=-deg
	return(deg)
}
function degressToRadians(degress){
	return(degress*Math.PI/180)
}
function radiansToDegress(radians){
	return(radians*180/Math.PI)
}
//界面

var data = {left:"",sign:"",right:"",result:""}
var current = false; // write to data.left
var m = "";
var lastIsMemory = false; // last operator is memory
var isMaxLength = false; // maxlength
var c_get = function(tagId){return document.getElementById(tagId);}
var c_getByName = function(tagParent,tagName,tagType){
	var eles = "";
	if(!tagParent){
		eles = document.getElementsByTagName(tagName);
	}else{
		eles = c_get(tagParent).getElementsByTagName(tagName);
	}
	if(tagType){
		var tags = [];
		for(var i=0;i<eles.length;i++){
			if(eles[i].type == tagType){
				tags.push(eles[i]);
			}
		}
		return tags;
	}
	return eles;
}

var calculator = {
	arith : function(sn){
		if(parseFloat(data.sign) != -2){
			switch(parseFloat(data.sign)){
				case 0:
					data.result = FloatAdd(parseFloat(data.left) , parseFloat(data.right));
					break;
				case 1:
					data.result = FloatSub(parseFloat(data.left) , parseFloat(data.right));
					break;
				case 2:
//					data.result = parseFloat(data.left) * parseFloat(data.right);
					data.result = FloatMul(parseFloat(data.left) , parseFloat(data.right));
					break;
				case 3:
//					data.result = parseFloat(data.left) / parseFloat(data.right);
					data.result = FloatDiv(parseFloat(data.left) , parseFloat(data.right));
					break;
			}
			data.left = "";
			data.sign = sn;
			data.right = "";
			current = false;
//			if(data.result.toString().length >= 3){
//				c_get("resultIpt").value = Number(data.result).toLocaleString();
//			}else{
//				c_get("resultIpt").value = data.result;
//			}
			c_get("resultIpt").value = data.result;//Number(data.result).toLocaleString();
		}
	},
	foo : function(sn){
		if(!lastIsMemory){ // 不是M
			if(data.left == ""){
				if(data.result != ""){
					data.left = data.result;
					current = true;
				}else{
					return false;
				}
			}else{
				if(data.right == ""){
					current = true;
				}else{
					calculator.arith(sn);
					data.left = data.result;
					data.sign = sn;
					data.right = "";
					current = true;
				}
			}
		}else{ // 是M
			if(data.left != "" && data.right != ""){
				calculator.arith(sn);
				data.left = data.result;
				data.sign = sn;
				data.right = "";
				current = true;
			}else{
				data.left = m;
				current = true;
			}
			lastIsMemory = false;
		}
		data.sign = sn;
	},
	input : function(key,type){
		if(type != -2){
			if(data.sign.toString() == "-2"){
				c_get("baseEprsPanel").innerHTML = key.value;
			}else{
				if(!lastIsMemory){
					c_get("baseEprsPanel").innerHTML += key.value;
				}else{
					c_get("baseEprsPanel").innerHTML = key.value;
				}
			}
			//c_get("baseEprsPanel").scrollLeft = c_get("baseEprsPanel").scrollWidth;
			//c_get("resultIpt").scrollLeft = c_get("resultIpt").scrollWidth;/* for ie */
		}
		switch(type){
			case -1: // number
				if(isMaxLength == false){
					if(c_get("resultIpt").value.substring(0,1) == "0" && key.value == "0" && c_get("resultIpt").value.length <= 1){
					}else{
						if(!lastIsMemory){ // 不是M
							if(current){
								if((data.right.toString().indexOf(".") != -1 && key.value == ".")){
									return false;
								}else{
									if(key.value == "." && (data.right == "" || data.right == "0")){
										data.right = "0" + key.value;
									}else{
										if(data.right.toString().length <= 1 && data.right == "0"){
											data.right = key.value
										}else{
											data.right += key.value;
										}
									}
									c_get("resultIpt").value = data.right;
								}
							}else{
								if((data.left.toString().indexOf(".") != -1 && key.value == ".")){
									return false;
								}else{
									if(key.value == "." && (data.left == "" || data.left == "0")){
										data.left = "0" + key.value;
									}else{
										if(data.left.toString().length <= 1 && data.left == "0"){
											data.left = key.value
										}else{
											data.left += key.value;
										}
									}
									c_get("resultIpt").value = data.left;
								}
							}
						}else{
							current = false;
							data.left = key.value;
							c_get("resultIpt").value = data.left;
							lastIsMemory = false;
							//c_get("baseEprsPanel").innerHTML = "";
						}
						if(data.sign.toString() == "-2"){
							data.sign = "";
						}
						if(c_get("resultIpt").value.length > 16){
							isMaxLength = true;
						}
					}
				}// isMaxLength
				else{
					alert("只能输入不大于17位的字符");
				}
				break;
			case -2:
				if(data.left != "" && data.right != ""){
					calculator.arith(-2);
				}else{
					if(data.result != "" && data.sign != "" && parseFloat(data.sign) != -2){
						data.right = data.left;
						data.left = data.result;
						calculator.arith(-2);
					}
				}
				isMaxLength = false;
				break;
			case -3:
				if(c_get("resultIpt").value.substring(0,1) == "-"){
					c_get("resultIpt").value = c_get("resultIpt").value.substr(1);
				}else{
					c_get("resultIpt").value = "-" + c_get("resultIpt").value;
				}

				//if(data.result == ""){
					if(current == false){
						if(data.left == ""){
							if(data.result != ""){
								data.left = data.result;
								data.left = -Number(data.left);
							}
						}else{
							//data.left = -data.left;
							data.left = c_get("resultIpt").value;
						}
					}else{
						data.right = c_get("resultIpt").value;
					}
				//}else{
					//data.result = -data.result;
					//c_get("resultIpt").value = data.result;
				//}
				//alert(data.left + " @@ " + data.sign + " @@ " + data.right);
				break;
			case 0:
				calculator.foo(0);isMaxLength = false;
				break;
			case 1:
				calculator.foo(1);isMaxLength = false;
				break;
			case 2:
				calculator.foo(2);isMaxLength = false;
				break;
			case 3:
				calculator.foo(3);isMaxLength = false;
				break;
		}
		key.blur();
	},
	output : function(pnl,str,isLink){
		if(isLink){
			pnl.innerHTML += str;
		}else{
			pnl.innerHTML = str;
		}
	},
	remove : function(){
		if(current == false){
			if(data.left.length > 0){
				data.left = data.left.substring(0,data.left.length-1);
				c_get("resultIpt").value = data.left;
			}
		}else{
			if(data.right.length > 0){
				data.right = data.right.substring(0,data.right.length-1);
				c_get("resultIpt").value = data.right;
			}
		}
		try{
			c_get("baseEprsPanel").innerHTML = c_get("baseEprsPanel").innerHTML.substring(0,c_get("baseEprsPanel").innerHTML.length-1);
		}catch(e){}
		c_get("simpleDel").blur();
		
		if(c_get("resultIpt").value.length <= 16){
			isMaxLength = false;
		}
	},
	clearAll : function(){
		c_get("resultIpt").value = 0;
		current = false;
		data.left = data.right = data.result = "";
		//m = "";
		c_get("baseEprsPanel").innerHTML = "";
		c_get("simpleClearAllBtn").blur();
		
		isMaxLength = false;
	},
	memory : function(obj,type){
		switch(type){
			// m
			case 0:
				if(c_get("resultIpt").value != ""){
					m = parseFloat(c_get("resultIpt").value);
				}
				lastIsMemory = true;
				break;
			// output m
			case 1:
				if(m != ""){
					if(parseFloat(data.sign) != -2 && data.sign.toString() != ""){
						if(current){
							data.right = m;
						}else{
							data.left = m;
						}
						c_get("baseEprsPanel").innerHTML += m;
					}
					c_get("resultIpt").value = m;
				}
				lastIsMemory = true;
				break;
			// clear m
			case 2:
				m = "";
				break;
			// m+
			case -1:
				if(m == ""){
					if(c_get("resultIpt").value != ""){
						m = parseFloat(c_get("resultIpt").value);
					}
				}else{
					if(c_get("resultIpt").value != ""){
						m = parseFloat(m) + parseFloat(c_get("resultIpt").value);
					}
				}
				lastIsMemory = true;
				break;
			// m*
			case -2:
				if(m == ""){
					if(c_get("resultIpt").value != ""){
						m = parseFloat(c_get("resultIpt").value);
					}
				}else{
					if(c_get("resultIpt").value != ""){
						m = parseFloat(m) * parseFloat(c_get("resultIpt").value);
					}
				}
				lastIsMemory = true;
				break;
		}
		obj.blur();
	}
}

var byKeyBoard = function(kyCd,evnt){
	var $g = function(tagId){return document.getElementById(tagId);}
	var cd = Number(kyCd);
	var chars = "simple";
	if(c_get("calculator_base").style.display == "none"){
		chars = "complete";
	}
	if(cd == 48 || cd == 96){
		$g(chars + "0").click();
		return false;
	}
	if(cd == 49 || cd == 97){
		$g(chars + "1").click();
		return false;
	}
	if(cd == 50 || cd == 98){
		$g(chars + "2").click();
		return false;
	}
	if(cd == 51 || cd == 99){
		$g(chars + "3").click();
		return false;
	}
	if(cd == 52 || cd == 100){
		$g(chars + "4").click();
		return false;
	}
	if(cd == 53 || cd == 101){
		$g(chars + "5").click();
		return false;
	}
	if(cd == 54 || cd == 102){
		$g(chars + "6").click();
		return false;
	}
	if(cd == 55 || cd == 103){
		$g(chars + "7").click();
		return false;
	}
	if(cd == 56 || cd == 104){
		$g(chars + "8").click();
		return false;
	}
	if(cd == 57 || cd == 105){
		$g(chars + "9").click();
		return false;
	}
	if(cd == 13 || cd == 187){
		$g(chars + "Equal").click();
		return false;
	}
	if(cd == 190 || cd == 110){
		$g(chars + "Dot").click();
		return false;
	}
	if(cd == 109 || cd == 189){
		$g(chars + "Subtr").click();
		return false;
	}
	switch(kyCd){
		case 107:
			$g(chars + "Add").click();
			break;
		case 109:
			$g(chars + "Subtr").click();
			break;
		case 189:
			$g(chars + "Subtr").click();
			break;
		case 106:
			$g(chars + "Multi").click();
			break;
		case 111:
			$g(chars + "Divi").click();
			break;
		case 46:
			//calculator.global.$("simpleDel").click();
			$g(chars + "Del").click();
			break;
	}
}

window.onload = function(){
	document.onkeydown = function(evn){
		var evnt = evn || window.event;
		var keyCode = evnt.keyCode;
		byKeyBoard(keyCode,evnt);
	}
	var buttons = c_getByName("calculator_base_container","input","button");
	var complete_buttons = c_getByName("complete_button_panel","input","button");
	var fn_buttons = c_getByName("moreFn","input","button");
	for(var i=0;i<buttons.length;i++){
		buttons[i].onmousedown = function(){
			this.style.backgroundPosition = "0 0";
			this.style.color = "#f0f0f0";
			this.onmouseup = function(){
				this.style.backgroundPosition = "0 -44px";
				this.style.color = "#fff";
			}
		}
	}
	for(var i=0;i<complete_buttons.length;i++){
		complete_buttons[i].onmousedown = function(){
			this.style.backgroundPosition = "0 0";
			this.onmouseup = function(){
				this.style.backgroundPosition = "0 -34px";
			}
		}
	}
	for(var i=0;i<fn_buttons.length;i++){
		fn_buttons[i].onmousedown = function(){
			this.style.backgroundPosition = "0 0px";
			this.onmouseup = function(){
				this.style.backgroundPosition = "0 -34px";
			}
		}
	}

}
</script>
</head>

<body>
	<div id="calculator_base">
		<div id="calculator_base_cup">
			<div id="calculator_base_container">
				<table id="base_table_top" class="calculator_table" cellpadding="0" cellspacing="0" border="0">
					<tbody><tr><td colspan="3" style="height:17px;"></td></tr>
					<tr>
						<td colspan="3" >
							<input type="text" id="resultIpt" readonly class="displayCss" width="390" value="0" size="17" maxlength="17" />
						</td>
					</tr>
					<tr class="topRowCss" style="display:none;">
						<td width="117" valign="middle"></td>
						<td id="baseEprsPanel" valign="middle" width="290" style="text-align:right"></td>
						<td width="30"></td>
					</tr>
					<tr><td colspan="3" style="height:30px;">&nbsp;</td>
				  </tr>
				</tbody></table>
				<table id="base_table_main" class="calculator_table" align="center" cellpadding="0" cellspacing="0" border="0" style="width:90%;">
					<tbody><tr>
						<td><input type="button" value="存储" onclick="calculator.memory(this,0);" class="baseBtnCommonCss"></td>
						<td><input type="button" value="取存" onclick="calculator.memory(this,1);" class="baseBtnCommonCss"></td>
						<td><input type="button" id="simpleDel" value="退格" onclick="calculator.remove();" class="baseBtnCommonCss baseBtnCss1"></td>
						<td><input type="button" id="simpleClearAllBtn" value="清屏" onclick="calculator.clearAll();" class="baseBtnCommonCss baseBtnCss1" style="color: rgb(255, 255, 255);  "></td>
					</tr>
					<tr>
						<td><input type="button" value="累存" onclick="calculator.memory(this,-1);" class="baseBtnCommonCss"></td>
						<td><input type="button" value="积存" onclick="calculator.memory(this,-2);" class="baseBtnCommonCss"></td>
						<td><input type="button" value="清存" onclick="calculator.memory(this,2);" class="baseBtnCommonCss"></td>
						<td><input type="button" id="simpleDivi" value="÷" onclick="calculator.input(this,3);" class="baseBtnCommonCss baseBtnCss2"></td>
					</tr>
					<tr>
						<td><input type="button" id="simple7" value="7" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simple8" value="8" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simple9" value="9" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3" style="color: rgb(255, 255, 255); "></td>
						<td><input type="button" id="simpleMulti" value="×" onclick="calculator.input(this,2);" class="baseBtnCommonCss baseBtnCss2"></td>
					</tr>
					<tr>
						<td><input type="button" id="simple4" value="4" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simple5" value="5" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simple6" value="6" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simpleSubtr" value="-" onclick="calculator.input(this,1);" class="baseBtnCommonCss baseBtnCss2"></td>
					</tr>
					<tr>
						<td><input type="button" id="simple1" value="1" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simple2" value="2" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simple3" value="3" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simpleAdd" value="+" onclick="calculator.input(this,0);" class="baseBtnCommonCss baseBtnCss2"></td>
					</tr>
					<tr>
						<td><input type="button" id="simple0" value="0" onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simpleDot" value="." onclick="calculator.input(this,-1);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" value="+/-" onclick="calculator.input(this,-3);" class="baseBtnCommonCss baseBtnCss3"></td>
						<td><input type="button" id="simpleEqual" value="=" onclick="calculator.input(this,-2);" class="baseBtnCommonCss baseBtnCss4"></td>
					</tr>
				</tbody></table>
			</div>
		</div>
	</div>

</body>
</html>
