
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>BOM树</title>
<link type="text/css" rel="stylesheet" href="/js/ztree3/zTreeStyle/zTreeStyle.css" />
<link type="text/css" href="/style1/css/classFunction.css" rel="stylesheet" />
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<link type="text/css" href="/style/css/jquery.contextmenu.css" rel="stylesheet"/>
<link type="text/css" href="/js/dialog/skins/default.css" rel="stylesheet" />
<link type="text/css" href="/style/css/base_button.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/style1/css/listgrid.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script> 
<script type="text/javascript" charset="utf-8" src="/js/lang/zh_CN.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/formtab.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/Map.js"></script>
<script type="text/javascript" src="/js/function.js?1458111389519"></script>
<script type="text/javascript" src="/js/ztree3/jquery.ztree.core.js"></script>




<script language="javascript">
var tableName='PDBom';
#set($bomId=$request.getParameter("bomId"))
var bomId='$bomId';

var zTree1;
var setting;
var setting = {view: {nameIsHTML: true,showTitle: false}	};

var zNodes =[
	{ name:'<span class="bomDet"><ul>\
		<li style="width:120px">物料编号</li>\
		<li style="width:110px">名称</li>\
		<li style="width:300px">规格</li>\
		<li style="width:40px">来源</li>\
		<li style="width:50px">单位</li>\
		<li style="width:50px">数量</li>\
		<li style="width:50px">损耗率</li>\
		<li style="width:50px">允许包料</li>\
		<li style="width:50px">替换料</li>\
	</ul><span style="width:0px;clear:both"></span></span>', open:true,
		children: []
	}

];
	
	
function zTreeOnDBlClick(event, treeId, treeNode) {

}

function viewReplace(bomDetId){
	
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	jQuery.getJSON("/BomAction.do?type=getReplace&bomDetId="+bomDetId,
		       function(json){
					if(json.Code=="Error"){
						alert("执行错误");
					}else{
						var url = "";
						if(json.id==undefined || json.id==""){
							url= "/UserFunctionAction.do?tableName=PDGoodsReplace&operation=6&defaultValue=true&GoodsCode="+json.GoodsCode+"&PGoodsCode="+json.pGoodsCode;
						}else{
							url= "/UserFunctionAction.do?tableName=PDGoodsReplace&operation=5&keyId="+json.id;
						}
						openPop('PopTooldiv','替换料',url,width,height,true,true);
					}
		       } 
		       
		); 
	
}

function recShowChild(child,list){
	for(var i=0;i<list.length;i++){
		if(child['children']==undefined){
			child['children']=[];
		}
		child['open']=true;
		attrType = list[i]['attrType'];
		if(attrType==0)
			attrTypeName='M自制';
		else if(attrType==1)
			attrTypeName='P采购';
		else if(attrType==2)
			attrTypeName='S委外';
		else if(attrType==3)
			attrTypeName='X虚拟';
		else if(attrType==4)
			attrTypeName='Z杂项';
		
		canSuper = list[i]['canSuper'];
		if(canSuper=='1'){
			canSuperName = '是';
		}else if(canSuper=='2'){
			canSuperName = '否';
		}else{
			canSuperName = '';
		}
		
		
		var pspanno = child.spanNo;
		
		var cno = child['children'].length;
		
		child['children'][cno]={GoodsCode:list[i]['GoodsCode']  , name:'<span class="bomDet"><span >'+pspanno+'.'+(i+1)+'</span><ul>\
				<li style="width:120px;text-align:left">'+list[i]['GoodsNumber']+'</li>\
				<li style="width:110px;text-align:left">'+list[i]['GoodsFullName']+'</li>\
				<li style="width:300px;text-align:left">'+list[i]['GoodsSpec']+'</li>\
				<li style="width:40px;text-align:left">'+attrTypeName+'</li>\
				<li style="width:50px;text-align:left">'+list[i]['BaseUnit']+'</li>\
				<li style="width:50px;text-align:right">'+(Number(child['qty']) * Number(list[i]['qty']))+'</li>\
				<li style="width:50px;text-align:right">'+(Number(child['lossRate']) +Number(list[i]['lossRate']))+'</li>\
				<li style="width:50px">'+(canSuperName)+'</li>\
				<li style="width:50px"  onclick=viewReplace("'+list[i]['bomDetId']+'") >查看</li>\
		</ul><span style="width:0px;clear:both"></span></span>',
		spanNo:(pspanno+'.'+(i+1)),
		qty: (Number(child['qty']) *Number(list[i]['qty'])),
		lossRate:(Number(child['lossRate']) +Number(list[i]['lossRate']))
		};
		
		if(attrType != 1 && attrType !=4 && list[i].childs.length > 0){
			recShowChild(child['children'][cno],list[i].childs);
		}
	}
}
function showChild(childs,gc){
	jQuery.getJSON("/BomAction.do?type=getChild",
	       {goods:gc},
	       function(json){
				for(obj in json){
					if(json[obj].length > 0){
						var child = {};
						for(var j=0;j<childs.length;j++){
							if(obj==childs[j].GoodsCode){
								child = childs[j];
								break;
							}
						}
						if(child.attrType != 1 && child.attrType !=4 ){
							recShowChild(child,json[obj]);
						}
					}
				}
				jQuery.fn.zTree.init($("#treeDemo"), setting, zNodes);
	       } 
	       
	); 
}

function reloginToUrl(){
	location=location;
}

function initData(){ 
	
	url = "/MobileAjax?op=detail";
	jQuery.post(url,{keyId:bomId,tableName:"PDBom"},function(data){ 
		  
		if(data.code=="OK"){
			for (item in data.obj.values){
				name = item.replace(".","_");
				$("#"+name).val(data.obj.values[item]);
			}	
			var clist = data.obj.values['TABLENAME_'+tableName+'Det'];
			var gc = "";
			for(var i =0;i<clist.length;i++){
				canSuper = clist[i]['canSuper'];
				if(canSuper=='1'){
					canSuper = '是';
				}else if(canSuper=='2'){
					canSuper = '否';
				}
				zNodes[0]['children'][zNodes[0]['children'].length]={GoodsCode:clist[i]['GoodsCode']  , name:'<span class="bomDet"><span >'+(i+1)+'</span><ul>\
						<li style="width:120px;text-align:left">'+clist[i]['tblGoods.GoodsNumber']+'</li>\
						<li style="width:110px;text-align:left">'+clist[i]['tblGoods.GoodsFullName']+'</li>\
						<li style="width:300px;text-align:left">'+clist[i]['tblGoods.GoodsSpec']+'</li>\
						<li style="width:40px;text-align:left">'+clist[i]['tblGoods.attrType']+'</li>\
						<li style="width:50px;text-align:left">'+clist[i]['tblGoods.BaseUnit']+'</li>\
						<li style="width:50px;text-align:right">'+clist[i]['qty']+'</li>\
						<li style="width:50px;text-align:right">'+clist[i]['lossRate']+'</li>\
						<li style="width:50px">'+(canSuper)+'</li>\
						<li style="width:50px" onclick=viewReplace("'+clist[i]['id']+'") >查看</li>\
				</ul><span style="width:0px;clear:both"></span></span>',
				spanNo:(i+1),
				qty:clist[i]['qty'],
				lossRate:clist[i]['lossRate']
				};
				gc += clist[i]['GoodsCode']+",";
			}
			showChild(zNodes[0]['children'],gc);
			
			//zTree1 = jQuery("#treeDemo").zTree(setting, node);
			//if(zNodes.length>0){
			//	zTree1.expandNode(zNodes[0],true,false,true,false);
			//}
			
		}else if(data.code=="NOLOGIN"){
			top.reLogin(window);		
		}else{
			malert(data.msg);
		}
	},"json" ); 
}

jQuery(document).ready(function(){
	initData();
	
	showStatus(); 
	bpWidth();
	jQuery(':text:eq(0)').focus();
	initTxtTitle();
});

</script>

<style type="text/css">
<!--
.bomDet{
	width:100%;
	height: 18px;
}
.bomDet div{
	border-bottom: 1px solid #D2C8D2;
}
.selectdBomDet{
	background-color:#D6D67B;
}
.bomDet ul{
	text-align: center;
    float:right;
    
}
.bomDet ul li{
	float:left;  
	height: 18px;  
    color: #000;
    line-height: 15px;
    border-left: 1px solid #bbb;
    text-align: center;
    font-size: 12px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.bomDet ul li span{
	display: inline-block;
}
.treeDemoSpan{
	display: inline-block;
	float:right;
	border-bottom: 1px solid #bbb;
    border-right: 1px solid #bbb;
}
.tree li a {
    margin: 0 0 0 -5px;
    display: inline-block;
    cursor: auto;
}

.tree input {
    height: 18px;
    display: block;
    float: left;
    background: none;
    box-sizing: border-box;
    color: #000;
    border: 0;
    text-align: left;
    width: 100%;
    height: expression(this.type=="text"?'100%');
    display: expression(this.type=="text"?'block');
    float: expression(this.type=="text"?'left');
}

.tree li button.ico_docu {background: url(/js/ztree/zTreeStyle/img/folder_Close.gif);}

.ztree li a {width:93%;border-right: 1px solid #D2C8D2;border-bottom: 1px solid #D2C8D2;}
#treeDemo_1_a{
	background: #5fa3e7;
    background-image: -webkit-linear-gradient(top,#5fa3e7,#428bca);
    background-image: linear-gradient(top,#5fa3e7,#428bca);}
#treeDemo_1_a .bomDet ul li{
	color: #fff;
}
-->
</style>
</head>

<body >
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post"  onKeyDown="down()" scope="request" name="form"  action="/UserFunctionAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<div class="Heading" >
	<div class="HeadingTitle">
		<b class="icons"></b>
		 BOM树
	</div>
	<ul class="HeadingButton f-head-u">
	#set($win=$request.getParameter("popWinName"))
		<li><span class="btn btn-small"  defbtn="backList" onClick="window.save=true; closeWindows('$win');">返回</span></li>			
	</ul>	
</div>

	<div id="listRange_id" style="position:relative;"> 
		<div class="wrapInside">
<style type="text/css">p{margin:0px}</style>
<div class="listRange_1" id="listRange_mainField">
<ul class="wp_ul">
    <li>
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="生效日期">
    <div class="d_box">
    <div class="d_test d_mid">生效日期</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="validate" name="validate" type="text" onfocus="mainFocus(this);"   readonly="readonly" onKeyDown="if(event.keyCode==13) event.keyCode=9" ></div>
    </li>
    <li>
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="失效日期">
    <div class="d_box">
    <div class="d_test d_mid">失效日期</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="inValidate" name="inValidate" type="text" onfocus="mainFocus(this);"   readonly="readonly" onKeyDown="if(event.keyCode==13) event.keyCode=9" ></div>
    </li>
    <li>
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="版本">
    <div class="d_box">
    <div class="d_test d_mid">版本</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="version" name="version" type="text" onfocus="mainFocus(this);"   readonly="readonly" onKeyDown="if(event.keyCode==13) event.keyCode=9" ></div>
    </li>
    <li style="float:none;width:0px">&nbsp;</li>
    <li class="longChar">
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="料件编号">
    <div class="d_box">
    <div class="d_test d_mid">料件编号</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="tblGoods_GoodsNumber" name="tblGoods_GoodsNumber" type="text"   class="inputMid input_type" readonly="readonly"  onfocus="this.oldValue=this.value; mainFocus(this);" popup="select"   /></div>
    </li>
    <li class="longChar">
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="料件名称">
    <div class="d_box">
    <div class="d_test d_mid">料件名称</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="tblGoods_GoodsFullName" name="tblGoods_GoodsFullName" type="text"   class="inputMid input_type" readonly="readonly"  onfocus="this.oldValue=this.value; mainFocus(this);" popup="select"   /></div>
    </li>
    <li class="longChar">
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="规格">
    <div class="d_box">
    <div class="d_test d_mid">规格</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="tblGoods_GoodsSpec" name="tblGoods_GoodsSpec" type="text"   class="inputMid input_type" readonly="readonly"  onfocus="this.oldValue=this.value; mainFocus(this);" popup="select"  /></div>
    </li>
    <li>
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="单位">
    <div class="d_box">
    <div class="d_test d_mid">单位</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="tblGoods_BaseUnit" name="tblGoods_BaseUnit" type="text"    readonly="readonly"  onfocus="this.oldValue=this.value; mainFocus(this);" popup="select" /></div>
    </li>
    <li>
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="来源码">
    <div class="d_box">
    <div class="d_test d_mid">来源码</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="tblGoods_attrType" name="tblGoods_attrType" type="text"    readonly="readonly"  onfocus="this.oldValue=this.value; mainFocus(this);" popup="select"   /></div>
    </li>
    <li>
    <div class="swa_c1 d_6" title="工位加总工时(秒)">
    <div class="d_box">
    <div class="d_test d_mid">工位加总工时(秒)</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="totalHours" name="totalHours" type="text"    onfocus="mainFocus(this);" readonly="readonly"  ></div>
    </li>
    <li>
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="标准工时(秒)">
    <div class="d_box">
    <div class="d_test d_mid">标准工时(秒)</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="workHours" name="workHours" type="text"    onfocus="mainFocus(this);" readonly="readonly" ></div>
    </li>
    <li>
    <div class="swa_c1 d_6" style="color: #C0C0C0;" title="平衡率%">
    <div class="d_box">
    <div class="d_test d_mid">平衡率%</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="EquilibriumRate" name="EquilibriumRate" type="text" onfocus="mainFocus(this);"   readonly="readonly"  ></div>
    </li>
    <li>
    <div class="swa_c1 d_6" style="color:#f13d3d;" title="报废率%">
    <div class="d_box">
    <div class="d_test d_mid">报废率%</div>
    </div>
    <div class="d_mh">&nbsp;</div>
    </div>
    <div class="swa_c2"><input id="loseRate" name="loseRate" type="text"    onfocus="mainFocus(this);" readonly="readonly" ></div>
    </li>
</ul>
</div>

		<div class="showGridTags" style="overflow: auto;">
			
			<div class="scroll_function_small_ud" id="listRange_tableInfo" style="overflow:none;height: auto;width: 1200px;">
			
			<div style="overflow:auto;float:left;width:100%;min-width: 1200px;padding-top: 0px;border-top: 1px solid #CEC8CD;" id="trees">
				<script type="text/javascript"> 
					var oDiv=document.getElementById("trees");
					var sHeight=document.documentElement.clientHeight-270;
					oDiv.style.height=sHeight+"px";
				</script>
				<div class="zTreeDemoBackground left">
					<ul id="treeDemo" class="ztree"></ul>
				</div>	
			</div>

				
			</div>	
		</div> 
		
	<script type="text/javascript"> 
		var sHeight2=document.documentElement.clientHeight- 30;
		
		var mainFieldH  =document.getElementById("listRange_mainField").offsetHeight;
		var tabH = sHeight2-mainFieldH-250;
		if(tabH<150) tabH=150;
		//$("#listRange_tableInfo").height(tabH);		
		
</script><br>
<div id="listRange_remark"><!--{14579423288700}-->
<div class="listRange_1_photoAndDoc_1"><span>备注：</span> 				<textarea id="Remark" name="Remark" style=" " ></textarea></div>
</div>
<br type="_moz">
		</div>
	</div>
</form>
	<script type="text/javascript">  
		var oDiv2=document.getElementById("listRange_id"); 
		var sHeight2=document.documentElement.clientHeight-40;
		 		oDiv2.style.height=sHeight2+"px";
				$("#listRange_tableInfo > div").height($("#listRange_tableInfo").height());	
		
	//在文字后加空格，chrome下实现两端对齐    
	cyh.lableAlign(); 
</script>

</body>
</html>