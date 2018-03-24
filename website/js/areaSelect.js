document.write("<iframe id='AreaFrame' scrolling='no' name='AreaFrame' frameborder='0' src='/js/areaSelect.htm' style='display:none;position:absolute;z-index:100;background-color:transparent'></iframe>");
var wcf=window.frames.AreaFrame;

var allAreaList = new Array();

function areaItem(id,name)
{
    this.id=id;
	this.name=name;
	this.parentId = id.substring(0,id.length-2);
}

function addArea(areaId,areaName)
{
    allAreaList[allAreaList.length] = new areaItem(areaId,areaName);    
}

function getAreaName(areaId){
	for(var i=0;i<allAreaList.length;i++){
		if(allAreaList[i].id==areaId)return allAreaList[i].name
	}
}

function drawAreaSelect(itemName,rootAreaId,currAreaId,currAreaName)//----------------
{
	if(!rootAreaId)rootAreaId="";
	if(!currAreaId)currAreaId="";
	if(!currAreaName)currAreaName="";
	if(currAreaId.length>0){
		currAreaName=getAreaName(currAreaId);
	}
    var str  = "<INPUT readonly id='areaNameShow' class='textbox' style='width:100px' onclick='this.select()' size=10 value='"+currAreaName+"'>&nbsp;"
	str += "<input id='hiddenAreaItem' name='"+itemName+"' value='"+currAreaId+"' type='hidden' style='width:100'>";
    str += "<IMG src='/images/place.gif' style='cursor:hand' align='absmiddle' border='0' ";
    str += "onClick='event.cancelBubble=true;showAreaSelect(this,\"areaNameShow\",\"hiddenAreaItem\",\""+rootAreaId+"\");'>";
	document.write(str);
}

function showAreaSelect(oImg,itemName,itemId,rootAreaId,currAreaId)
{
    document.onclick=hideSelectArea;
	var fld1 = document.getElementById(itemName);
	var cf=document.getElementById("AreaFrame");
    if(cf.style.display=="block"){cf.style.display="none";return;}
	var eT=0,eL=0,p=oImg;
	var sT=document.body.scrollTop,sL=document.body.scrollLeft;
	var eH=oImg.height,eW=oImg.width;
	while(p&&p.tagName!="BODY"){eT+=p.offsetTop;eL+=p.offsetLeft;p=p.offsetParent;}
	cf.style.top=(document.body.clientHeight-(eT-sT)-eH>=cf.height)?eT+eH:eT-cf.height;
	cf.style.left=(document.body.clientWidth-(eL-sL)>=cf.width)?eL:eL+eW-cf.width;
	cf.style.display="block";
	wcf.allAreaList = this.allAreaList;
	wcf.drawAreaSelect(rootAreaId,document.getElementById("hiddenAreaItem").value);
	wcf.resizeFrame()
	wcf.retItemId = itemId;
	wcf.retItemName = itemName;
}

//***********************************************************************
function drawAreaSelect1(itemName,rootAreaId,currAreaId,currAreaName)//----------------
{
	if(!rootAreaId)rootAreaId="";
	if(!currAreaId)currAreaId="";
	if(!currAreaName)currAreaName="";
	if(currAreaId.length>0){
		currAreaName=getAreaName(currAreaId);
	}
    var str  = "<INPUT readonly id='areaNameShow' class='textbox' style='width:100px' onclick='this.select()' size=10 value='"+currAreaName+"'>&nbsp;"
	str += "<input id='hiddenAreaItem' name='"+itemName+"' value='"+currAreaId+"' type='hidden' style='width:100'>";
    str += "<IMG src='/images/place.gif' style='cursor:hand' align='absmiddle' border='0' ";
    str += "onClick='event.cancelBubble=true;showAreaSelect1(this,\"areaNameShow\",\"hiddenAreaItem\",\""+rootAreaId+"\");'>";
	document.write(str);
}

function showAreaSelect1(oImg,itemName,itemId,rootAreaId,currAreaId)
{
    document.onclick=hideSelectArea;
	var fld1 = document.getElementById(itemName);
	var cf=document.getElementById("AreaFrame");
    if(cf.style.display=="block"){cf.style.display="none";return;}
	var eT=0,eL=0,p=oImg;
	var sT=document.body.scrollTop,sL=document.body.scrollLeft;
	var eH=oImg.height,eW=oImg.width;
	while(p&&p.tagName!="BODY"){eT+=p.offsetTop;eL+=p.offsetLeft;p=p.offsetParent;}
	cf.style.top=(document.body.clientHeight-(eT-sT)-eH>=cf.height)?eT+eH:eT-cf.height;
	cf.style.left=(document.body.clientWidth-(eL-sL)>=cf.width)?eL:eL+eW-cf.width;
	cf.style.display="block";
	wcf.allAreaList = this.allAreaList;
	wcf.drawAreaSelect1(rootAreaId,document.getElementById("hiddenAreaItem").value);
	wcf.resizeFrame()
	wcf.retItemId = itemId;
	wcf.retItemName = itemName;
}
//***********************************************************************

function hideSelectArea()
{
	var cf=document.getElementById("AreaFrame");
	cf.style.display="none";
}