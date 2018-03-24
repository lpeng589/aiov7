<html>
<head>
<meta name="renderer" content="webkit">
<title>$text.get("calc.lb.calc")</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="JavaScript">
var oper1=0; //操作数一
var oper2=0; //操作数二
var operator=0;    //运算符 
var point=false;
function calc(num)

{
     with(calculator.lcd)   //下面的value默认为calculator.lcd的value 
     {
         if(operator==0)    //当没有运算符被按下的时候  
         {
              if(point==true)value+=num;

              else

              {
                   if(parseFloat(value)==0 || oper1!=0)value = num;  

                   //当屏幕上现显示的不是（初始为）或者应经有操作数一，直接对value赋值   
                   else value = "" + parseFloat(value) + num;     //否则，连接到原来的末尾   
                   value += ".";
              }

         }
         else //当有运算符被按下

         {
              if(point==true)

              {
                   value+=num;
              }

              else

              {
                   if(oper2==0)value=num; //当操作数二还没输入的时候，直接对value赋值   
                   else value = "" + parseFloat(value) + num;     //操作数二已被输入的时候，连接到原来的末尾 
				   //else value = "" + num;     //操作数二已被输入的时候，连接到原来的末尾 

                   value += ".";
              }
              oper2 = parseFloat(value); //把屏幕上现显示的数值放到操作数二  
         }

     }
}


function compute()

{
     point=false;

     if(operator==0)    //若之前没有其他操作符被按下   
     {
         oper1 = parseFloat(calculator.lcd.value); //保存屏幕上的值到操作数一 
         return; //退出 
     }

     //若是其他情况

     result=""+eval(oper1 + operator + oper2);

     result+=(result.indexOf(".")==-1)?"":"";

     calculator.lcd.value = result;   //将屏幕上的值设置为两操作数计算结果

     oper1 = parseFloat(calculator.lcd.value); //把计算结果放入操作数一，准备下一次计算 
     oper2 = 0;    //操作数二重新置为

}


function input(key)

{
     if(key>=0 && key<=9)

     {

         calc(key);

     }

     else

     { 
              compute();    //调用计算函数
              operator=(key=="=")?0:key;
     }
}


function keyboardInput()

{
     var key = event.keyCode;

     if(key>=48 && key<=57)

     {

         key-=48; //0~9对应于ASCII编码~57

     }

     else if(key==46){point=true;return;}

     else

     {

         switch(key)

         {

              case 43:

                   key = "+";break;

              case 45:

                   key = "-";break;

              case 42:

                   key = "*";break;

              case 47:

                   key = "/";break;

              case 61:

              case 13:

                   key = "=";

                   input(key);

                   getValue();
                   
                   btok();

                   break;

              default:

                   {return;}
         }
     }

     input(key);

}


function mouseInput(key)

{
     if(key=="."){point=true;return;}

     if(parseFloat(key)>=0 && parseFloat(key<=9))

         key=parseFloat(key);

     input(key);

}


function clearLcd()

{
     point=false;

     oper1=0; //操作数一

     oper2=0; //操作数二

     operator=0;   //运算符







     calculator.lcd.value = "0";

}


function getValue()

{
     window.returnValue = calculator.lcd.value;

    //window.close();

     //alert(33);

}


//function readTxt() 
//{ 
//alert(window.clipboardData.getData("text")); 
//} 
function setTxt() 
{ 
var t=document.getElementById("lcd"); 
with(event)if((keyCode==99 | keyCode==67)&ctrlKey){
		t.select();
		//var val=p.value;
		//val=val.substring(0,val.length-1);
		window.clipboardData.setData('text',t.createTextRange().text);}


//alert("213"); 
}
function tp() 
{
	var p=document.getElementById("lcd");
   with(event)if((event.keyCode==99 | keyCode==86)&ctrlKey)
    { 	
		//p.select();
		//p.focus();
		document.all.lcd.value=clipboardData.getData("Text");
        oper2 = parseFloat(calculator.lcd.value);
		//alert('粘贴成功！')
		//document.execCommand("Paste");
		//alert('成功！');
		//var val=p.value;
		//val=val.substring(0,val.length-1);
		//if(event.ctrlKey&&event.keyCode==86) 
		//with(event)if((keyCode==99 | keyCode==86)&ctrlKey) 
    } 
}


//var p=document.getElementById("lcd"); 
//with(event)if((keyCode==99 | keyCode==118)&ctrlKey){
//p.focus();
//document.execCommand("Paste");
//;alert('成功！')
//}


//} 

function copyin() 
{ 
var co=document.getElementById("lcd"); 
co.select(); 
window.clipboardData.setData('text',co.createTextRange().text); 
}
function Pastein() {
document.all.lcd.value=clipboardData.getData("Text");
        oper2 = parseFloat(calculator.lcd.value);
		//alert('粘贴成功！')
 }
 function backspace() 
{
if(document.calculator.lcd.value.length>1) 
document.calculator.lcd.value=document.calculator.lcd.value.substring(0,document.calculator.lcd.value.length - 1) 
else 
document.calculator.lcd.value=0 
} 

function btok(){
	if(parent.curchildOpObj != null && typeof(parent.curchildOpObj) != "undefined"){
		val = document.getElementById("lcd").value;
		if(val.substring(val.length-1)=="."){
			val = val.substring(0,val.length-1);
		}
		
		for(i=0;i<parent.fieldDigit.length;i++){
			if(parent.fieldDigit[i].name ==parent.curchildOpObj.attr("name")){
				val=parent.f(val,parent.fieldDigit[i].digit);
			}  	
		}
		
		parent.curchildOpObj.val(val);
		parent.curchildOpObj.trigger("change");
	}
	parent.jQuery.close("PopCalcdiv");
}

</script> 
<style type="text/css">
body{margin:0;padding:0;background:#f9f9f9}
input{ width:40px;color:#0000ff; height:24px; line-height:18px;}
.hBtns{padding:1px 12px;font-weight:bold;cursor:pointer;display:inline-block;height:21px;line-height:21px;color:#666;border:1px #bbb solid;border-radius:3px;}
.hBtns:hover{background:#f2f2f2;}
input.hBtns{background:none;font-family:microsoft yahei;height:25px;line-height:21px;}
input.hBtns:hover{background:#f2f2f2;}
</style>

</head>

<body onKeyPress="keyboardInput();" onClick="document.getElementById('ok').focus();" onLoad="$('#cebt').focus();">

<form name="calculator" onKeyDown="javascript: if (event.keyCode == 13){return getValue();};tp();setTxt();javascript: if (event.keyCode == 46){return clearLcd();};javascript: if (event.keyCode == 8){return backspace();};">
<table id="ok" align="center">

<tr><td colspan=4><input type="text" value="0." readonly id="lcd" name="lcd" style="width:100%;text-align:right"><td></tr>



<tr>

	 <td colspan=2><input class="hBtns" type="button" value="$text.get("calc.lb.Backspace")" onClick="backspace()" style="width:100%"></td>
     <td colspan=2><input class="hBtns" type="button" id="cebt" value="CE" onClick="clearLcd()" style="width:100%"></td>

</tr>
<tr>

     <td><input class="hBtns" type="button" value="7" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="8" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="9" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="/" onClick="mouseInput(value)"></td>

</tr>

<tr>

     <td><input class="hBtns" type="button" value="4" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="5" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="6" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="x" onClick="mouseInput('*')"></td>

</tr>

<tr>

     <td><input class="hBtns" type="button" value="1" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="2" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="3" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="-" onClick="mouseInput(value)"></td>

</tr>

<tr>

     <td><input class="hBtns" type="button" value="0" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="." onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="=" onClick="mouseInput(value)"></td>

     <td><input class="hBtns" type="button" value="+" onClick="mouseInput(value)"></td>

</tr>
</table>
</form>

</body>

</html>
