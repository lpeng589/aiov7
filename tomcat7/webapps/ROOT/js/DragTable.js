var dragFlag=false;
//获取目标
function MouseDownToResize(obj){
	obj.mouseDownX=event.clientX;
	obj.pareneTdW=obj.parentElement.offsetWidth;
	obj.pareneTableW=tblSort.offsetWidth;
	obj.setCapture();
	dragFlag=true;
}
//拖动目标
//download by http://www.codefans.net
function MouseMoveToResize(obj,index){
    if(!obj.mouseDownX) 
    	return false;
    var newWidth=obj.pareneTdW*1+event.clientX*1-obj.mouseDownX;
    if(newWidth>0)
    {
    	document.getElementById("tdWidth"+index).value = newWidth ;
		obj.parentElement.style.width = newWidth;
		tblSort.style.width=obj.pareneTableW*1+event.clientX*1-obj.mouseDownX;
	}
}
//释放目标
function MouseUpToResize(obj){
	obj.releaseCapture();
	obj.mouseDownX=0;
}
