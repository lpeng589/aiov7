var strObj = '' 
var index = 10000;
document.onmouseup = moveUp; 
document.onmousemove = moveMove; 

function moveDown(Object){ 
 strObj=Object.id; 
 document.all(strObj).setCapture(); 
 pX=event.x-document.all(strObj).style.pixelLeft; 
 pY=event.y-document.all(strObj).style.pixelTop; 
} 
function moveMove(){ 
 if(strObj!=''){ 
 document.all(strObj).style.left=event.x-pX; 
 document.all(strObj).style.top=event.y-pY; 
 } 
} 
function moveUp(){ 
 if(strObj!=''){ 
 document.all(strObj).releaseCapture(); 
 strObj=''; 
 } 
} 
function getFocus(obj){ 
 if(obj.style.zIndex!=index){ 
 index = index + 2; 
 var idx = index; 
 obj.style.zIndex=idx; 
 } 
}
function submitForm()
{
 var colSelects = document.getElementById("FieldsToShowList") ;
 var colSelect = "colConfig@@";
 if(colSelects.length==0)
 {
 alert('显示字段不能全部移除.') ;
 setDefaultFields('FieldsToShowList');
 return false ;
 }
 for(var x=0;x<colSelects.length;x++)
 {
 var opt = colSelects.options[x];
 colSelect+=opt.value+",";
 }
 window.parent.fillColConfig(colSelect);
 window.parent.jQuery.close('customPopup');
}

function submitForm2()
{
 var colSelects = document.getElementById("FieldsToShowList") ;
 var colSelect = "";
 if(colSelects.length==0)
 {
 alert('显示字段不能全部移除.') ;
 setDefaultFields('FieldsToShowList');
 return false ;
 }
 
 for(var x=0;x<colSelects.length;x++)
 {
 var opt = colSelects.options[x]; 
 if(colConfigForm.colType.value=='popup'){
 colSelect+=opt.value+"],";
 }else{
 colSelect+=opt.value+",";
 }
 }
 document.getElementById("colSelect").value = colSelect ;
 colConfigForm.submit() ;
}

function defaultForm(type){
 window.parent.fillColConfig(type+"@@");
 window.parent.jQuery.close('customPopup');
}
function localize(ContainerDiv,xPoint,yPoint)
{
 var varContainer = document.getElementById(ContainerDiv) ;
 var widthReg = /px/ig;
 var heightReg = /px/ig;
 inputObjWidth = 600;
 if(widthReg.test(varContainer.style.width))
 {
 inputObjWidth = varContainer.style.width.replace(widthReg,'');
 }
 varContainer.style.width = inputObjWidth+"px";
 varContainer.style.left = xPoint-800+"px";
 varContainer.style.top = yPoint+90+"px";
}

function setDefaultFields(ItemList)
{
 var varItemList = document.getElementById(ItemList) ;
 for(var x=varItemList.length-1; x>=0; x--)
 {
 var opt = varItemList.options[x];
 varItemList.options[x] = null;
 }
 var defaultFields = document.getElementById("FieldsToSelectList") ;
 for(var x=0; x<= defaultFields.length-1; x++)
 {
 varItemList.options[x] = new Option(defaultFields[x].text, defaultFields[x].value, 0, 0);
 }
}

function hiddenDivCustomSetTable()
{
 var divIsFocus = document.getElementById("CustomDivIsFocus") ;
 if(divIsFocus.value == '0')
 {
 hiddenObj("DivCustomSetTable");
 }
}
function hiddenObj(objId)
{
 var objIdList=objId.split(',');
 for(i=0;i<objIdList.length;i++)
 {
 document.getElementById(objIdList[i]).style.display = "none";
 }
}
function displayObj(objId)
{
 var objIdList=objId.split(',');
 for(i=0;i<objIdList.length;i++)
 {
 document.getElementById(objIdList[i]).style.display = "block";
 }
}
function showDivCustomSetTable(xPoint,yPoint)
{
 var custom = document.getElementById("DivCustomSetTable") ;
 var divIsFocus = document.getElementById("CustomDivIsFocus") ;
 if(custom.style.display == 'block')
 {
 divIsFocus.value = '0';
 hiddenDivCustomSetTable();
 return;
 }
 if(divIsFocus.value == "")
 {
 divIsFocus.value = '0';
 setTimeout("displayObj('"+"DivCustomSetTable"+"')",200);
 }
 else if(divIsFocus.value=='0')
 {
 if(divIsFocus.style.display != 'block')
 {
 divIsFocus.value = '0';
 setTimeout("displayObj('"+"DivCustomSetTable"+"')",200);
 }
 else
 {
 hiddenDivCustomSetTable();
 }
 }
 else
 {
 divIsFocus.value = '0';
 setTimeout("displayObj('"+"DivCustomSetTable"+"')",200);
 }
 localize('DivCustomSetTable',xPoint,yPoint);
}
function addItem(ItemList,Target)
{
 var varItemList = document.getElementById(ItemList) ;
 var varTarget = document.getElementById(Target) ;
 for(var x=0;x<varItemList.length;x++)
 {
 var opt = varItemList.options[x];
 if (opt.selected)
 {
 flag = true;
 for (var y=0;y<varTarget.length;y++)
 {
 var myopt = varTarget.options[y];
 if (myopt.value == opt.value)
 {
 flag = false;
 }
 } 
 if(flag)
 {
 varTarget.options[varTarget.options.length] = new Option(opt.text, opt.value, 0, 0);
 varItemList.options.remove(x); 
 x-- ;
 }
 }
 }
}
function delItem(ItemList,Target)
{
 var varItemList = document.getElementById(ItemList) ;
 var varTarget = document.getElementById(Target) ;
 for(var x=varItemList.length-1;x>=0;x--)
 {
 var opt = varItemList.options[x];
 if (opt.selected)
 {
 if(opt.value.indexOf(":1")>0){
 alert(opt.text+'为必选项,不能移除.') ;
 return false ;
 }
 varItemList.options[x] = null;
 varTarget.options[varTarget.options.length] = new Option(opt.text, opt.value, 0, 0);
 }
 }
}

function popDelItem(ItemList,Target)
{
 var varItemList = document.getElementById(ItemList) ;
 var varTarget = document.getElementById(Target) ;
 for(var x=varItemList.length-1;x>=0;x--)
 {
 var opt = varItemList.options[x];
 if (opt.selected)
 {
 var bools= true ;
 var selTable = document.getElementById("tblSelTable") ;
 if(selTable.rows.length>0){
 for(var i=2;i<selTable.rows[0].cells.length;i++){
 if(opt.innerHTML==selTable.rows[0].cells[i].innerHTML){
 bools = false ;
 break;
 }
 }
 }
 if(bools){
 varItemList.options.remove(x);
 varTarget.options[varTarget.options.length] = new Option(opt.text, opt.value, 0, 0);
 }else{
 alert(opt.innerHTML+"不能移除！！") ;
 }
 }
 }
}
function upItem(ItemList)
{
 var varItemList = document.getElementById(ItemList) ;
 for(var x=1;x<varItemList.length;x++)
 {
 var opt = varItemList.options[x];
 if(opt.selected)
 {
 tmpUpValue = varItemList.options[x-1].value;
 tmpUpText = varItemList.options[x-1].text;
 varItemList.options[x-1].value = opt.value;
 varItemList.options[x-1].text = opt.text;
 varItemList.options[x].value = tmpUpValue;
 varItemList.options[x].text = tmpUpText;
 varItemList.options[x-1].selected = true;
 varItemList.options[x].selected = false;
 break;
 }
 }
}
function downItem(ItemList)
{
 var varItemList = document.getElementById(ItemList) ;
 for(var x=0;x<varItemList.length-1;x++)
 {
 var opt = varItemList.options[x];
 if(opt.selected)
 {
 tmpUpValue = varItemList.options[x+1].value;
 tmpUpText = varItemList.options[x+1].text;
 varItemList.options[x+1].value = opt.value;
 varItemList.options[x+1].text = opt.text;
 varItemList.options[x].value = tmpUpValue;
 varItemList.options[x].text = tmpUpText;
 varItemList.options[x+1].selected = true;
 varItemList.options[x].selected = false;
 break;
 }
 }
}
