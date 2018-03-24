<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<title>$text.get("mrp.lb.mrpCount")</title>
<script type="text/javascript">
//下达采购订单
function doOrder(){
	var flag=false;
	var isSelect = false;
	var choose = document.getElementsByName("choose");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"){
			isSelect = true;
			break;
		}
	}
	if(!isSelect){
		alert("$text.get('mrp.lb.noChooseGoods')");
		return;
	}
	var needProducts = document.getElementsByName("needProduct");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"&&Number(needProducts[i].value)>0){
			flag = true;
			break;
		}			
	}	
	if(!flag){
		alert("$text.get('mrp.msg.AllGoodsNeededQtyBuy')");
		return;
	}
	if(!confirm('$text.get("mrp.msg.doOrderPre")')){
		return false;
	}
	var url = "/MrpBP.do?method=doOrder";
	form.action=url;
	form.submit();		
}
		
//下达请购单
function doQuoteBuy(){
	var flag=false;
	var isSelect = false;
	var choose = document.getElementsByName("choose");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"){
			isSelect = true;
			break;
		}
	}
	if(!isSelect){
		alert("$text.get('mrp.lb.noChooseGoods')");
		return;
	}
	var needProducts = document.getElementsByName("needProduct");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"&&Number(needProducts[i].value)>0){
			flag = true;
			break;
		}			
	}
	var zeroApply = document.getElementsByName("zeroApply")[0]
	if(!flag&&zeroApply.checked==false){
		alert("$text.get('mrp.msg.AllGoodsNeededQtyQuoteBuy')");
		return;
	}
	if(!confirm('$text.get("mrp.msg.doQuoteBuyPre")')){
		return false;
	}
	var url = "/MrpBP.do?method=doQuoteBuy";
	form.action=url;
	form.submit();		
}
		
function selectLow(chk){
	var billNos = document.getElementsByName("billNo");
	var chooses = document.getElementsByName("choose");
	var i = parseInt(chk.value);
	var k=i-1;
	chooses[k].value=chk.checked;
	for(i;i<billNos.length;i++){
		if(billNos[i].value==""){
			chooses[i].value=chk.checked;
		}else{
			break;
		}
	}
}

function checkAllSub(chkCap){
	var cbs = document.getElementsByName("cb");
	for(i=1;i<cbs.length;i++){
		if(!cbs[i].disabled){
			cbs[i].checked=chkCap.checked;
			selectLow(cbs[i]);
		}
	}
}

//下达生产任务单











function doProduceOrder(){
	var flag=false;
	var isSelect = false;
	var chks = document.getElementsByName("choose");
	var goodsNums=document.getElementsByName("goodsNumber");
	for(i=0; i<chks.length; i++){
		if(goodsNums[i].value.indexOf("~")>=0)continue;
			if(chks[i].value=="true"){
				isSelect = true;
				break;
			}
	}
	if(!isSelect){
		alert("$text.get('mrp.lb.noChooseGoods')");
		return;
	}
	var needProducts = document.getElementsByName("needProduct");
	for(i=0; i<chks.length; i++){
		if(goodsNums[i].value.indexOf("~")>=0)continue;
		if(chks[i].value=="true"&&Number(needProducts[i].value)>0){
			flag = true;
			break;
		}			
	}
	if(!flag){
		alert("$text.get('mrp.msg.AllGoodsNeededQtyProduce')");
		return;
	}
	if(!confirm('$text.get("mrp.msg.doProducePre")')){
		return false;
	}
	
	var url = "/MrpBP.do?method=doProduceOrder";
	form.action=url;
	form.submit();
	return;	
}

//计算
function demand(flag,flagstr){
	try{		
		if(flagstr=="store"){
			storeCheck=flag;
		}else if(flagstr=="used"){
			usedCheck=flag;
		}else if(flagstr=="storeing"){
			storeingCheck=flag;
		}else if(flagstr=="producting"){
			productingCheck=flag;
		}else if(flagstr=="storeingSelf"){
			storeingSelfCheck=flag;
		}else if(flagstr=="productingSelf"){
			productingSelfCheck=flag;
		}else if(flagstr=="usedSelf"){
			usedSelfCheck=flag;
		}else if(flagstr=="orderSelf"){
			orderSelfCheck=flag;
		}					
		var tbl = $("tblSort");//获得表格
		var rows = tbl.lastChild.childNodes;//获得表格行



		var stores = document.getElementsByName("store");//实际库存文本框



		var storeings = document.getElementsByName("storeing");//在途量文本框



		var storeingsHid = document.getElementsByName("storeingHid");//在途量文本框



		var productings = document.getElementsByName("producting");//在产量



		var productingsHid = document.getElementsByName("productingHid");//在产量



		var useds = document.getElementsByName("used");//已分配库存文本框
		var usedsHid = document.getElementsByName("usedHid");//已分配库存文本框
		var usedsLeft = document.getElementsByName("usedLeft");
		var storeingSelfs = document.getElementsByName("storeingSelf");//在途量_本单
		var productingSelfs = document.getElementsByName("productingSelf");//在产量_本单
		var usedSelfs = document.getElementsByName("usedSelf");//库存已分配量_本单
		var orderSelfs = document.getElementsByName("orderSelf");//订单已分配量_本单
		var neededs = document.getElementsByName("needed");//净需求文本框
		var needProducts = document.getElementsByName("needProduct");
		var totalPrices = document.getElementsByName("totalPrice");//总价文本框



		var prices = document.getElementsByName("price");//单价文本框



		var counts = document.getElementsByName("count");//毛需求文本框
		var qtys = document.getElementsByName("qty");
		var goodscodes = document.getElementsByName("goodscode");
		var cbs = document.getElementsByName("cb");
		var goodsNumbs=new Array();
		var factQtys=new Array();
		var flag2=false;
		for(i=0;i<rows.length;i++){	
			var exisPlace=false;
			if(cbs[i+1].path.length>0&&cbs[i+1].mainPath==""){//物料的path有值,并且不是其他物流的替换料
				for(var j=i;j<cbs.length;j++){//是否有替换料
					if(cbs[j].mainPath==cbs[i+1].path){
						exisPlace=true;
					}
				}
			}
			
			if(exisPlace)continue;//如果这个物料存在替换料，那么它的各个数量都不用计算



			
			var counti = parseFloat(counts[i].value.replace(/,/g,""));//当前物料的毛需求



			
			/***********计算替换物料的毛需求量: 假如是其它物料的替换料，先计算出被替换物料的毛需求量，以便计算出分配给此物料的库存，在途，在产*/
			if(cbs[i+1].isPlace=="true"&&cbs[i+1].checked){
				//找到主物料的位置,以便设置主物料的毛需求量
				var placeMain=0;
				for(var j=0;j<cbs.length;j++){
					if(cbs[j].path==cbs[i+1].mainPath&&cbs[j].mainPath==""){
						placeMain=j-1;//库存等变量的要比cbs少一个表头



						break;
					}
				}
				
				if(placeMain>0){
					//确定当前物料是否是第一个被选中的替换料
					var isFirst=false;
					for(var j=placeMain+2;j<cbs.length;j++){
						if(cbs[j].checked&&cbs[j].mainPath==cbs[i+1].mainPath){
							if(j==i+1){
								isFirst=true;
							}
							break;
						}
					}
					
					//第一个被选中的替换料的毛需求量=主物料的毛需求量=所有替换料的毛需求量/替换料的种数
					if(isFirst){	
						var placeCount=0;
						var count=0;
						for(var j=1;j<cbs.length;j++){
							if(cbs[j].checked&&cbs[j].mainPath==cbs[i+1].mainPath){
								count=count+parseFloat(counts[j-1].value);//累加替换料毛需求量
								placeCount++;
							}	
						}
						
						count=f(count/placeCount,$globals.getDigitsOrSysSetting("tblStockDet","TotalQty"));
						counts[placeMain].value=count;	
						counti=count;
					}else{//之后被选中的替换料的毛需求量=主物料的毛需求量-这个替换料之前的所有替换料的(隐藏库存+隐藏在途+隐藏在产+本单_在途+本单_库存已分配量+本单_订单已分配量+本单_在产)						
						counti=counts[placeMain].value;	
						
						for(var j=1;j<i+1;j++){
							if(cbs[j].checked&&cbs[j].mainPath==cbs[i+1].mainPath){
								counti=counti-(Number(usedsHid[j-1].value)+Number(storeingsHid[j-1].value)+Number(productingsHid[j-1].value)+Number(storeingSelfs[j-1].value)+Number(usedSelfs[j-1].value)+Number(orderSelfs[j-1].value)+Number(productingSelfs[j-1].value))
							}	
						}
					}
				}
			}
			/************以上计算替换物料的毛需求量*******************************************************************************/	
			
			var needed = parseFloat(neededs[i].value.replace(/,/g,""));//净需求		
			var price = parseFloat(prices[i].value.replace(/,/g,""));//单价	
			if("store"==flagstr){
				if(flag){
					stores[i].value=stores[i].defaultvalue;
				}else{
					stores[i].value="0";
				}
			}else if("used"==flagstr){
				if(flag){
					useds[i].value=useds[i].defaultvalue;
				}else{
					useds[i].value="0";
				}
			}else if("storeing"==flagstr){
				if(flag){
					storeings[i].value=storeings[i].defaultvalue;
				}else{
					storeings[i].value="0";
				}
			}else if("producting"==flagstr){
				if(flag){
					productings[i].value=Number(productings[i].defaultvalue)==0?"0":productings[i].defaultvalue;
				}else{
					productings[i].value="0";
				}
			}else if("storeingSelf"==flagstr){
				if(flag){
					storeingSelfs[i].value=Number(storeingSelfs[i].defaultvalue)==0?"0":storeingSelfs[i].defaultvalue;
				}else{
					storeingSelfs[i].value="0";
				}
			}else if("productingSelf"==flagstr){
				if(flag){
					productingSelfs[i].value=Number(productingSelfs[i].defaultvalue)==0?"0":productingSelfs[i].defaultvalue;
				}else{
					productingSelfs[i].value="0";
				}
			}else if("usedSelf"==flagstr){
				if(flag){
					usedSelfs[i].value=Number(usedSelfs[i].defaultvalue)==0?"0":usedSelfs[i].defaultvalue;
				}else{
					usedSelfs[i].value="0";
				}
			}else if("orderSelf"==flagstr){
				if(flag){
					orderSelfs[i].value=Number(orderSelfs[i].defaultvalue)==0?"0":orderSelfs[i].defaultvalue;
				}else{
					orderSelfs[i].value="0";
				}
			}else if(""==flagstr){
				if("$mrpSets".length>0){
					if("$mrpSets".indexOf("cbStore")>=0){
						stores[i].value=stores[i].defaultvalue;
					}else{
						stores[i].value="0";
					}
					if("$mrpSets".indexOf("cbUsed")>=0){
						useds[i].value=useds[i].defaultvalue;
					}else{
						useds[i].value="0";
					}
					if("$mrpSets".indexOf("cbStoring")>=0){
						storeings[i].value=storeings[i].defaultvalue;
					}else{
						storeings[i].value="0";
					}
				}else{
					if(flag){
						useds[i].value=useds[i].defaultvalue;
						stores[i].value=stores[i].defaultvalue;
						storeings[i].value=storeings[i].defaultvalue;
					}else{
						useds[i].value="0";
						stores[i].value="0";
						storeings[i].value="0";
					}
				}
			}
			//（要判断{实际库存-库分}是否大于毛需求，如果毛需求小，分配给当前订单的库分=毛需求，界面库分=库分+毛需求）
			//如果毛需求量大，则分配给当前订单的库分=毛需求，库分=库分+实际库存
			if(usedCheck&&storeCheck&&(cbs[i+1].isPlace!="true"||(cbs[i+1].isPlace=="true"&&cbs[i+1].checked))){
				goodscode=goodscodes[i].value;
				var countNeed=Number(counti)-(Number(storeingSelfs[i].value)+Number(productingSelfs[i].value)+Number(usedSelfs[i].value)+Number(orderSelfs[i].value))

				var hasUsedHid=0;
				for(var j=i-1;j>=0;j--){//查询之前的此商品共占用了多少实际库存
					var use = 0;
					if(goodscode==goodscodes[j].value){
						hasUsedHid=hasUsedHid+Number(usedsHid[j].value);
					}
				}			
				useds[i].value=Number(useds[i].defaultvalue)+Number(hasUsedHid);//界面显示的库存已分配量


				if((Number(stores[i].defaultvalue)-Number(useds[i].value))>countNeed){
					usedsHid[i].value=countNeed;
				}else{
					usedsHid[i].value=Number(stores[i].defaultvalue)-Number(useds[i].value);
				}	
			}else{
				usedsHid[i].value="0";
			}
				
			//（要判断毛需求是否大于在途量，1.毛需求小于在途量，则本条记录掩藏的在途量=毛需求,2.如果毛需求大于在途量，则本条记录掩藏的在途量=在途量）


			if(storeingCheck&&(cbs[i+1].isPlace!="true"||(cbs[i+1].isPlace=="true"&&cbs[i+1].checked))){
				goodscode=goodscodes[i].value;//当前行的goodscode
				var countNeed=Number(counti)-(Number(usedsHid[i].value)+Number(storeingSelfs[i].value)+Number(productingSelfs[i].value)+Number(usedSelfs[i].value)+Number(orderSelfs[i].value))
				var hasStoreingHid=0;
				for(var j=i-1;j>=0;j--){//查询之前的此商品共占用了多少在途量
					var use = 0;
					if(goodscode==goodscodes[j].value){
						hasStoreingHid=hasStoreingHid+Number(storeingsHid[j].value);
					}
				}
					
				storeings[i].value=Number(storeings[i].defaultvalue)-Number(hasStoreingHid);
				if((Number(storeings[i].defaultvalue)-Number(hasStoreingHid))>countNeed){
					storeingsHid[i].value=countNeed;
				}else{
					storeingsHid[i].value=Number(storeings[i].defaultvalue)-Number(hasStoreingHid);
				}
			}else{
				storeingsHid[i].value="0.0";
			}
			//在产量同理在途量
			if(productingCheck&&(cbs[i+1].isPlace!="true"||(cbs[i+1].isPlace=="true"&&cbs[i+1].checked))){
				goodscode=goodscodes[i].value;//当前行的goodscode
				var countNeed=Number(counti)-(Number(usedsHid[i].value)+Number(storeingsHid[i].value)+Number(storeingSelfs[i].value)+Number(productingSelfs[i].value)+Number(usedSelfs[i].value)+Number(orderSelfs[i].value))
				var hasProductingsHid=0;
				for(var j=i-1;j>=0;j--){//查询之前的此商品共占用了多少在产量


					var use = 0;
					if(goodscode==goodscodes[j].value){
						hasProductingsHid=hasProductingsHid+Number(productingsHid[j].value);
					}
				}
			
				productings[i].value=Number(productings[i].defaultvalue)-Number(hasProductingsHid);
				if((Number(productings[i].defaultvalue)-Number(hasProductingsHid))>countNeed){
					productingsHid[i].value=countNeed;
				}else{
					productingsHid[i].value=Number(productings[i].defaultvalue)-Number(hasProductingsHid);
				}					
			}else{
				productingsHid[i].value="0";
			}
				
			/**********计算本级物料的净需求量************************************************/	
			var store = parseFloat(stores[i].value.replace(/,/g,""));//库存
			var storeing = parseFloat(storeingsHid[i].value.replace(/,/g,""));//在途量
			var used = parseFloat(useds[i].value.replace(/,/g,""));//库存已分配


			
			var storeingSelf = parseFloat(storeingSelfs[i].value.replace(/,/g,""));//在途量_本单
			var usedSelf = parseFloat(usedSelfs[i].value.replace(/,/g,""));//库存已分配_本单
			var orderSelf = parseFloat(orderSelfs[i].value.replace(/,/g,""));//订单已分配_本单
			var totalPrice = 0.0;//总价
			//计算净需求/总价
			//if(cbs[i+1].isPlace=="true"){//只有主物料有净需求量，替换料没有净需求量,所勾选的所有的替换料的各种数量都累加到主物料上
			//	needed=0;
			//}else{
				needed = counti-(store-used)-storeing-storeingSelf-usedSelf-orderSelf;
			//}
			if(isNaN(needed)){//如果不能算出数字则为0;
				needed = 0;
			}
			
			if(needed<0)needed=0;				
			neededs[i].value=f(needed,$globals.getDigitsOrSysSetting("tblStockDet","TotalQty"));
			totalPrice = price*needed;
			if(isNaN(totalPrice))totalPrice=0;//不能让结果出现NaN
			totalPrices[i].value=Number(totalPrice);
			ch1 = rows[i].cells[3].innerText.lastIndexOf("~");
			pqty = parseFloat(qtys[i].value.replace(/,/g,""));//父级qty
			
			/********通过本级的净需求量计算出下级的毛需求量************************************************************/
			for(j=i+1;j<rows.length;j++){					
				ch2 = rows[j].cells[3].innerText.lastIndexOf("~");
				//如果当前行的～字符比下一行的～字符少，说明是下一行的父级
				if(ch1==-1){
					if(ch1<(ch2-1)){
						needed = parseFloat(neededs[i].value.replace(/,/g,""));
						qty = parseFloat(qtys[j].value.replace(/,/g,""));//子级qty
						counts[j].value=f(Number(needed*qty),$globals.getDigitsOrSysSetting("tblStockDet","TotalQty"));//子级毛需求=父级净需求*（父子比）



					}
				}else if(ch1==(ch2-1)){						
					needed = parseFloat(neededs[i].value.replace(/,/g,""));
					qty = parseFloat(qtys[j].value.replace(/,/g,""));//子级qty
					counts[j].value=f(Number(needed*qty),$globals.getDigitsOrSysSetting("tblStockDet","TotalQty"));//子级毛需求=父级净需求*（父子比）



				}
				//当遇更高级/同级(说明它没有子级)时跳出



				if(ch1>=ch2){
					break;
				}
			}
			
			/****************计算加上生产任务单后的净需求量,这个主要用于再下达生产任务单时给出提示*****************************/
			needProducts[i].value=Number(neededs[i].value)-Number(productings[i].value)-Number(productingSelfs[i].value);
			if(Number(needProducts[i].value)<0){
				needProducts[i].value=0;
			}
			
			/******假如所选择的物料是其被替换物料的最后一个物料，那么计算被替换物料的总的库存，在途，在产.... ************************/
			if(cbs[i+1].isPlace=="true"&&cbs[i+1].checked){//是替换料，并且被选中
				var lastPlace=0;
				for(var j=0;j<cbs.length;j++){
					if(cbs[j].checked&&cbs[j].mainPath==cbs[i+1].mainPath){
						lastPlace=j;
					}
				}	
				
				if((i+1)==lastPlace){//其主料的最后一个替换料
					var placeMain=0;
					for(var j=0;j<cbs.length;j++){
						if(cbs[j].path==cbs[i+1].mainPath&&cbs[j].mainPath==""){
							placeMain=j-1;//库存等变量的要比cbs少一个表头



							break;
						}
					}
					if(placeMain>0){//找到替换料的主物料


						counti=counts[placeMain].value;
						store=0;
						var storeHid=0;
						storeing=0;
						var storeingHid=0;
						used=0;
						proing=0;
						storeingSelf=0;
						usedSelf=0;
						orderSelf=0;
						proSelf=0;
						var placeCount=0;
						for(var j=1;j<cbs.length;j++){
							if(cbs[j].checked&&cbs[j].mainPath==cbs[i+1].mainPath){
								store=store+parseFloat(stores[j-1].value); //累加替换料库存


								storeHid=storeHid+parseFloat(usedsHid[j-1].value); //累加替换料隐藏库存


								storeing=storeing+parseFloat(storeings[j-1].value) //累加替换料在途量
								storeingHid=storeingHid+parseFloat(storeingsHid[j-1].value) //累加替换料隐藏在途量
								used=used+parseFloat(useds[j-1].value); //累加替换料库存已分配量


								proing=proing+parseFloat(productings[j-1].value)//累加替换料在产量
								storeingSelf=storeingSelf+parseFloat(storeingSelfs[j-1].value);//累加替换料本单在途量
								usedSelf=usedSelf+parseFloat(usedSelfs[j-1].value);//累加替换料本单库存已分配量



								orderSelf=orderSelf+ parseFloat(orderSelfs[j-1].value);//累加替换料本单订单已分配量


								proSelf=proSelf+parseFloat(productingSelfs[j-1].value);//累加替换料本单在产量
								placeCount++;
							}
						}
						
						stores[placeMain].value=store;
						storeings[placeMain].value=storeing;
						useds[placeMain].value=used;
						productings[placeMain].value=proing;
						storeingSelfs[placeMain].value=storeingSelf;
						usedSelfs[placeMain].value=usedSelf;
						orderSelfs[placeMain].value=orderSelf;
						productingSelfs[placeMain].value=proSelf;
						neededs[placeMain].value=f(counti-storeHid-storeingHid-storeingSelf-usedSelf-orderSelf,$globals.getDigitsOrSysSetting("tblStockDet","TotalQty"));	
						needProducts[placeMain].value=Number(neededs[placeMain].value)-Number(productings[placeMain].value)-Number(productingSelfs[placeMain].value);
						if(Number(needProducts[placeMain].value)<0){
							needProducts[i].value=0;
						}					
					}
				}	
			}
			/***************以上计算被替换物料的总的库存，在途，在产.... ********************************************/
		}
	}catch(e){alert(e.description);}
}

function calc(){
	var mrpSetChk = document.getElementsByName("mrpSet");
	for(var i=0;i<mrpSetChk.length;i++){
		demand(mrpSetChk[i].checked,mrpSetChk[i].flag);
	}	
	
	if(typeof(top.junblockUI)!="undefined"){
 		top.junblockUI();
	}
}
		var storeCheck=false;
		var usedCheck=false;
		var storeingCheck=false;
		var productingCheck=false;
		function checkMrpSets(flag){
			demand(flag,"store");
			demand(flag,"used");
			demand(flag,"storeing");
		}
		//保存
		function save(){
		try{
			var isSelect = false;
			var chks = document.getElementsByName("cb");
			for(i=1; i<chks.length; i++){
				if(chks[i].checked){
					isSelect = true;
					break;
				}
			}
			if(!isSelect){
				alert("$text.get('mrp.matereiDemandJs.info1')");
				return;
			}
			
			var form = document.getElementsByName("form")[0];
			form.action="/MrpBP.do?method=saveMrp";
			form.submit();
		}catch(e)	{alert(e.description);}
		}
		function goBack(){
			#if($!operType=="upMrp")
			form.action="/MrpBP.do?mrpFrom=$!mrpFrom";
			#else
			form.action="/MrpBP.do?method=bomSelPro&mrpFrom=$!mrpFrom";
			#end
			
			form.submit();
		}
		function disabledSonBomCb(){
			var bomCbs = document.getElementsByName("cb");
			var rows = $("tblSort").rows;
			for(i=0;i<bomCbs.length;i++){
				//if(bomCbs[i].value=="")
				if(rows[i].cells[2].innerText.replaceAll(" ","")=="")
					bomCbs[i].disabled="disabled";
			}
		}
		function checkLastBom(){
			//获得表"tblSort"的第"1"行开始的第"2"列的"文本内容"'
			var boms = getCellInfos('tblSort',1,3,true);
			getCellInfos('tblSort',1,6,true);
			var bom ;
			var cbs = n("cb");//获得复选框
			var len = boms.length;
			for(i=0;i<len;i++){
				bom = boms[i].trim();
				if(bom.indexOf("~")!=-1){
					if(cbs[i+1].isPlace=="true"){//检查如果当前主料没有替换料，默认第一个被选中
						if(cbs[i+1].path==cbs[i+1].mainPath){
							var isselect=false;
							for(j=0;j<len;j++){
								if(cbs[j+1].mainPath==cbs[i+1].mainPath&&cbs[j+1].checked){
									isselect=true;
								}
							}
							if(!isselect){
								cbs[i+1].checked=true;
							}
						}
					}else{
						cbs[i+1].disabled="disabled";
						cbs[i+1].checked=false;
					}
				}else{
					cbs[i+1].checked=true;
				}
			}
		}
	function openSelect(urlstr,obj,field){  
				var str  = window.showModalDialog(urlstr+"&MOID=83e8760a_0811132128219840114&MOOP=add"
				,"","dialogWidth=730px;dialogHeight=450px"); 
				if(!str)return;//如果没有返回数据
				fs=str.split("|"); 
				var curr=0;
				for(var i=0;i<document.getElementsByName(field).length;i++){
					var obj2=document.getElementsByName(field)[i];					 
					if(obj2==obj){						
						curr=i;
						break;
					}
				}
				document.getElementsByName("stockCode")[curr].value="";
				document.getElementsByName("stockName")[curr].value="";
				if(fs.length==1){
					var kv=fs[0].split(";");
					if(kv[0].length>0){
						document.getElementsByName("stockCode")[curr].value+=kv[0]+";";
						document.getElementsByName("stockName")[curr].value+=kv[1]+";";
					}
				}
				for(var i=0;i<fs.length-1;i++){
					var kv=fs[i].split(";");
					
					document.getElementsByName("stockCode")[curr].value+=kv[0]+";";
					document.getElementsByName("stockName")[curr].value+=kv[1]+";";
				}
	}
	function oper(){
		var mrpSets=document.getElementsByName("mrpSet"); 
		
		var mrpArray=new Array();
		for(i=0;i<mrpSets.length;i++){
			if(mrpSets[i].checked){
				mrpArray[mrpArray.length]=mrpSets[i].value;
			}
		}
		var from = document.getElementsByName("form")[0];
		form.action =encodeURI(encodeURI( "/MrpBP.do?method=bomDemand&mrpId=$!mrpId&pageFrom=demand&opeType=oper"));
		if("$!mrpId"!=""){
			var chkArr = new Array();
			var cbs = document.getElementsByName("cb");
			for(i=1; i<cbs.length; i++){
				chkArr[i-1]=cbs[i].checked;
			}
			form.action = encodeURI( "/MrpBP.do?method=bomDemand&mrpId=$!mrpId&opeType=oper");
		}
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		form.submit();
	}
	
	function printData(){
		form.target="formFrame";
		form.action="/MrpBP.do?method=savePrintData";
		form.submit();
		window.showModalDialog("/ReportDataMrpAction.do?operation=printActiveX&reportNumber=mrpDemandPrint",window,"dialogWidth=208px;dialogHeight=285px,scroll=no;")
		form.target="";		
	}
	function openSelect1(urlstr,displayName,obj,field){

	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)!="undefined"){
		fs=str.split("|"); 
				
		document.getElementById("StockCode1").value="";
		document.getElementById("Stock").value="";
		if(fs.length==1){
			var kv=fs[0].split(";");
			if(kv[0].length>0){
				document.getElementById("StockCode1").value+=kv[0]+";";
				document.getElementById("Stock").value+=kv[1]+";";
			}
		}
		for(var i=0;i<fs.length-1;i++){
			var kv=fs[i].split(";");
					
			document.getElementById("StockCode1").value+=kv[0]+";";
			document.getElementById("Stock").value+=kv[1]+";";
		}
	}
	if(obj=="Stock"){
		var stocks = document.getElementsByName("stockCode");
		var stockNames = document.getElementsByName("stockName");
		for(i=0;i<stocks.length;i++){
			stocks[i].value=document.getElementById("StockCode1").value;
			stockNames[i].value=document.getElementById("Stock").value;
		}
	}
}

function deployChange(obj,path){
	if(obj.src.indexOf("/$globals.getStylePath()/images/tree/nolines_plus.gif")>=0){
		obj.src="/$globals.getStylePath()/images/tree/nolines_minus.gif";		
		var items = document.getElementsByTagName("tr");
		for(var i=0;i<items.length;i++){	
			if(items[i].getAttribute("mainPath") != undefined ){
				if(path==items[i].getAttribute("mainPath")){
					items[i].style.display ="block";
				}
			}		
		}
	}else{
		obj.src="/$globals.getStylePath()/images/tree/nolines_plus.gif";
		var items = document.getElementsByTagName("tr");
		for(var i=0;i<items.length;i++){	
			if(items[i].getAttribute("mainPath") != undefined ){
				if(path==items[i].getAttribute("mainPath")){
					items[i].style.display ="none";
				}
			}		
		}
	}
}
</script>
<style type="text/css">
.resizeDiv{width:2px;overflow:hidden; float:right; cursor:col-resize;}
.listRange_list_function thead td{
	padding-left:1px;
	padding-right:1px;
}
</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body onLoad="showtable('tblSort');showStatus();checkLastBom(); checkAll('cb',true);calc();sortables_init('tblSort');">
<form method="post" scope="request" name="form" action="">	
<input type="hidden" name="neededAll" value="">
<input type="hidden" name="storeingAll" value="">
<input type="hidden" name="productingAll" value="">
<input type="hidden" name="usedAll" value="">
<input type="hidden" name="method" value="">
<input type="hidden" name="mrpId" value="$!mrpId">
<input type="hidden" name="mrpFrom" value="$!mrpFrom">
<div class="Heading">
	<div class="HeadingIcon">
		<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
	</div>
	<div class="HeadingTitle">$text.get("mrp.lb.mrpCount")</div>
	<ul class="HeadingButton">
		<li><button type="button" name="saveList" onClick="oper()" class="b4">$text.get("mrp.lb.showBom")</button></li><li>
		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblBuyApplication").add())
		<button type="button" onClick="doQuoteBuy()">$text.get("mrp.lb.addQuoteBuy")</button>
		#end
		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblBuyOrder").add())
		<button onClick="doOrder()">$text.get("mrp.lb.doBuyOrder")</button>
		#end
		#if($globals.getVersion()==8)
		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblProduce").add())
		<button onClick="doProduceOrder()">$text.get("mrp.lb.doProduceOrder")</button>
		#end
		#else
		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblProduceBP").add())
		<button type="button" onClick="doProduceOrder()">$text.get("mrp.lb.doProduceOrder")</button>
		#end
		#end
		</li>
		<li><button type="button" name="saveList" onClick="if(confirm('$text.get("mrp.msg.sureSave")')){save()}" class="b4">$text.get("mrp.lb.save")</button></li>
		<li><button type="button" onClick="goBack()" class="b2">$text.get("common.lb.back")</button></li>
		#if($LoginBean.operationMap.get("/MrpBP.do").print())		
		<li><button type="button" onClick='printData();' class="b2">$text.get("common.lb.print")</button></li>
		#end
		</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("mrp.lb.countParm")：</span></li>
			<li><label for="mrpSetId1">-$text.get("mrp.lb.storeQty")</label>
				<input type="checkbox" class="cbox" id="mrpSetId1" name="mrpSet" flag="store" value="cbStore" onClick="" #if($mrpSets.indexOf("cbStore")>=0) checked #end>
			</li>
			<li>
				<label for="mrpSetId2">-$text.get("mrp.lb.storingQty")</label>
				<input type="checkbox" class="cbox" id="mrpSetId2" name="mrpSet" flag="storeing" value="cbStoring"  onClick=""  #if($mrpSets.indexOf("cbStoring")>=0) checked #end>
			</li>
			<!-- <li>
				-$text.get("mrp.lb.productingQty")
				<span><input type="checkbox" class="cbox" name="mrpSet" value="cbProducting" onClick="demand(this.checked,'producting')"  #if($mrpSets.indexOf("cbProducting")>=0) checked #end></span>
				</li>-->
			<li> 
				<label for="mrpSetId3">+$text.get("mrp.lb.usedQty")</label>
				<input type="checkbox" class="cbox" id="mrpSetId3" name="mrpSet" flag="used" value="cbUsed" onClick=""  #if($mrpSets.indexOf("cbUsed")>=0) checked #end>
			</li>
			<li> 
				<label for="mrpSetId4">-$text.get("mrp.lb.storingQtySelf")</label>
				<input type="checkbox" class="cbox" id="mrpSetId4" name="mrpSet" flag="storeingSelf" value="storeingSelf" onClick="" disabled="disabled" checked >
			</li>
			<!-- <li> 
				-$text.get("mrp.lb.productingQtySelf")
				<label><input type="checkbox" class="cbox" name="mrpSet" value="productingSelf" onClick="demand(this.checked,'productingSelf')"  #if($mrpSets.indexOf("cbStoreingSelf")>=0) checked #end></label>
			</li> -->
			<li> 
				<label for="mrpSetId5">-$text.get("mrp.lb.usedQtySelf")</label>
				<input type="checkbox" class="cbox" id="mrpSetId5" name="mrpSet" flag="usedSelf" value="usedSelf" onClick="" disabled="disabled" checked >
			</li>
			<li> 
				<label for="mrpSetId6">-$text.get("mrp.lb.orderQtySelf")</label>
				<input type="checkbox" class="cbox" id="mrpSetId6" name="mrpSet" flag="orderSelf" value="orderSelf" onClick="" disabled="disabled"  checked >
			</li>
			<li> 
				<label for="mrpSetId7">$text.get("mrp.lb.zeroApply")</label>
				<input type="checkbox" class="cbox" id="mrpSetId7" name="zeroApply"  value="true" onClick="" checked >
			</li>
		</div>
		<div class="listRange_1">
			<li> 
				<span>$text.get("mrp.lb.defaultStock")：</span>
				<input id="StockCode1" name="StockCode1" type="hidden" value="$!StockCode1" /><input type="input" id="Stock" name="Stock" value="$!Stock" onDblClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectStocksCheck','$text.get('mrp.lb.defaultStock')','Stock','StockCode1');" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectStocksCheck','$text.get('mrp.lb.defaultStock')','Stock','StockCode1');" >
			</li>
		</div>
<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
						<thead>
							<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
								<td width="30"><input type="checkbox" name="cb" onClick="checkAllSub(this)">
								</td>
								<td width="80">
									$text.get("mrp.lb.mrpFrom")
								</td>
								<td width="110"> 
									$text.get("mrp.lb.trackNo")
								</td>
							
								<td width="100"> 
									$text.get("mrp.lb.bomNumber")
								</td>
								<td width="100">
									$text.get("mrp.lb.bomName")
								</td>
								<td width="100">
									$text.get("call.lb.spec")
								</td>
								<td width="50">
									$text.get("iniGoods.lb.Unit")
								</td>
								#if($globals.getVersion()=="8")
								<td width="100">$text.get("mrp.lb.MaterielAttribute")</td>
								#end
								#if($Stock&&$Stock.length()>0)
								#foreach($s in $Stock.split(";"))
									<td width="80">&nbsp;$!s</td>
								#end
								#end
								#foreach($prop in $propList)
									#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
										<td width="80">$prop.display.get($!locale)</td>
									#end
								#end
								<td width="60">$text.get("mrp.lb.employee")</td>
								<td width="60">$text.get("mrp.lb.numUnit")</td>
								<td width="100">
									$text.get("mrp.lb.productQty")
								</td>
								<td width="100">$text.get("mrp.lb.storeQty")</td>
								<td width="60">$text.get("mrp.lb.storingQty")</td>
								<td width="60">$text.get("mrp.lb.productingQty")</td>
								<td width="100">$text.get("mrp.lb.usedQty")</td>
								<td width="80">$text.get("mrp.lb.storingQtySelf")</td>
								<td width="80">$text.get("mrp.lb.productingQtySelf")</td>
								<td width="120">$text.get("mrp.lb.usedQtySelf")</td>
								<td width="120">$text.get("mrp.lb.orderQtySelf")</td>
								<td width="100">$text.get("mrp.lb.neededQty")</td>
							</tr>
						</thead>
						<tbody>
						#set($trackNo="")
						#foreach($row in $list)	
							#if($!row.get("trackNo").length()>0)	
								#set($trackNo=$!row.get("trackNo"))
							#end
						<tr mainPath="$!row.get("mainPath")" #if($!row.get("isPlace")=="true")style="display:none"#end>
							<td width="30">						
								<input type="checkbox" name="cb" value="$velocityCount" isPlace="$!row.get("isPlace")" #if($!row.get("cb")=="true") checked="$!row.get("cb")" #end  path="$!row.get("path")" mainPath="$!row.get("mainPath")" onclick="selectLow(this);">
								<input type="hidden" name="isPlace" value="#if($!row.get("mainPath")==$!row.get("path"))false#else$!row.get("isPlace")#end">
								<input type="hidden" name="isPlaceh" value="$!row.get("isPlace")">
								<input type="hidden" name="mainPath" value="$!row.get("mainPath")">
								<input type="hidden" name="path" value="$!row.get("path")">
								<input type="hidden" name="choose" value="true">
								<input type="hidden" name="keyId" value="$!row.get("detId")">
								<input type="hidden" name="bomNo" #if($!row.get("goodsNumber").indexOf("~")<0) value="$!row.get("bomNo")" #else value="" #end>
								</td>
								<td>
									#if($!row.get("goodsNumber").indexOf("~")<0)#if($!mrpFrom=="1")$text.get("mrp.lb.produceOrder")#end #if($!mrpFrom=="2")$text.get("mrp.lb.salesOrder")#end #if($!mrpFrom=="0")$text.get("mrp.lb.mlProduce")#end#end&nbsp;
								</td>
								<td title="$!row.get("billNo")">
									$!row.get("trackNo")&nbsp;
									<input type="hidden" name="trackNo" value="$!row.get("trackNo")">
									<input type="hidden" name="bomdetId" value="$!row.get("bomdetId")">
								</td>
								<td title="$!row.get("goodsNumber")">#if($!row.get("changeCount")!="0") <img src="/$globals.getStylePath()/images/tree/nolines_plus.gif" onClick="deployChange(this,'$!row.get("path")')"  />#end
									<input type="hidden" name="goodsCode" value="$!row.get("goodsCode")">
									<input type="hidden" name="goodsNumber" value="$!row.get("goodsNumber")">
									<input type="hidden" name="goodsFullName" value="$!row.get("goodsFullName")">
									<input type="hidden" name="employeeId" value="$!row.get("employeeId")">
									<input name="price" type="hidden" value="$!globals.formatNumber($!row.get("price"),false,false,$!globals.getSysIntswitch(),"tblBOM","Price",true)" readonly="readonly" style="width:60px;">
									<input name="totalPrice" type="hidden" value="$!globals.formatNumber($!row.get("amount"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderDet","Amount",true)"  readonly="readonly" style="width:80px;">
									<input type="hidden" name="MaterielAttribute" value="$!row.get("MaterielAttribute")">
									<input type="hidden" name="unitName" value="$!row.get("unitName")">
									<input type="hidden" name="stockCode" value="$!row.get("stockCode")">
									<input type="hidden" name="startDate" value="$!row.get("startDate")">
									<input type="hidden" name="submitDate" value="$!row.get("submitDate")">
									<input type="hidden" name="stockName" readonly="readonly" title="$text.get('mrp.doOrder.dblclickStock')" value="$!row.get("stockName")" onDblClick="openSelect('/UserFunctionAction.do?tableName=tblBuyOrder&selectName=SelectStocksCheck&operation=22',this,'stockName')">
									$!row.get("goodsNumber")&nbsp;
								</td>
								<td title="$!row.get("goodsFullName")">
									$!row.get("goodsFullName")&nbsp;
								</td>
								<td title="$!row.get("goodsSpec")">
									<input type="hidden" name="goodsSpec" value="$!row.get("goodsSpec")" >$!row.get("goodsSpec")&nbsp;
								</td>
								<td title="$!row.get("unitName")">
									$!row.get("unitName")&nbsp;
								</td>
								#if($globals.getVersion()=="8")
								#set($count=0)
								#foreach($erow in $globals.getEnumerationItems("MaterielAttribute"))
									#if($erow.value==$!row.get("MaterielAttribute"))
										<td title="$erow.name">
										<input type="hidden" name="materName" value="$erow.name">
											$erow.name&nbsp;
										</td>
										#set($count=1)
									#end
								#end
								#if($count==0)
									<input type="hidden" name="materName" value="">
									<td title="$erow.name">&nbsp;</td>
								#end
								#end
								#if($Stock&&$Stock.length()>0)
								#foreach($s in $StockCode1.split(";"))
									<td width="80">&nbsp;$!row.get("stockStore").get($!s)</td>
								#end
								#end
								#foreach($prop in $propList)
									#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
								<td><input type="text" name="$prop.propName" value="$!row.get($prop.propName)">&nbsp;</td>
									#end
								#end	
							<td><input type="text" name="empFullName" value="$!row.get("empFullName")">&nbsp;</td>
							<td>
							<input type="input" name="qty" value="$!globals.formatNumber($!row.get("qty"),false,false,$!globals.getSysIntswitch(),"tblBOM","Qty",true)">&nbsp;
							</td>
							<td>
							<input name="count" defaultvalue="$!globals.formatNumber($!row.get("count"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderDet","Qty",true)"
								 value="$!globals.formatNumber($!row.get("count"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderDet","Qty",true)" readonly="readonly"  style="width:100px;">
							</td>
							<td>
							<input name="store"  defaultvalue="$!row.get("store")" value="$!row.get("store")" readonly="readonly" style="width:100px;">
							</td>
							<td>
								<input name="storeingHid" type="hidden" value="0.0" readonly="readonly" style="width:50px;">
								<input name="storeing" defaultvalue="$!row.get("storeing")" 
									value="$!globals.formatNumber($!row.get("storeing"),false,false,$!globals.getSysIntswitch(),"tblBuyOrderDet","Qty",true)" readonly="readonly" style="width:100px;">
							</td>
							<td>
							<input name="productingHid" type="hidden" value="0.0" readonly="readonly" style="width:50px;">
							<input name="producting" defaultvalue="$!row.get("producting")" 
								value="$!globals.formatNumber($!row.get("producting"),false,false,$!globals.getSysIntswitch(),"tblProduce","Qty",true)" readonly="readonly" style="width:100px;">
							</td>
							<td>
							<input name="usedLeft" type="hidden" value="0.0"  readonly="readonly" style="width:50px;">
							<input name="usedHid" type="hidden" value="0.0"  readonly="readonly" style="width:50px;">
							<input name="used" defaultvalue="$!row.get("stockReQuantum")" 
								value="$!globals.formatNumber($!row.get("stockReQuantum"),false,false,$!globals.getSysIntswitch(),"tblStocks","stockQuantum",true)"  readonly="readonly" style="width:100px;">
							</td>
							<td>
							<input name="storeingSelf" defaultvalue="$!row.get("buyIngNum")" 
								value="$!globals.formatNumber($!row.get("buyIngNum"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderQuantum","buyIngNum",true)"  readonly="readonly" style="width:100px;">
							</td>
							<td>
							<input name="productingSelf" defaultvalue="$!row.get("proingNum")" 
								value="$!globals.formatNumber($!row.get("proingNum"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderQuantum","proingNum",true)"  readonly="readonly" style="width:100px;">
							</td>
							<td>
							<input name="usedSelf" defaultvalue="$!row.get("stockQuantum")" 
								 value="$!globals.formatNumber($!row.get("stockQuantum"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderQuantum","stockQuantum",true)"  readonly="readonly" style="width:100px;">
							</td>
							<td>
							<input name="orderSelf" defaultvalue="$!row.get("orderQuantum")"
								 value="$!globals.formatNumber($!row.get("orderQuantum"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderQuantum","orderQuantum",true)"  readonly="readonly" style="width:100px;">
							</td>
							<td>
							<input name="needed" value="$!globals.formatNumber($!row.get("need"),false,false,$!globals.getSysIntswitch(),"tblSalesOrderDet","Qty",true)"  readonly="readonly" style="width:100px;">  
							<input name="needProduct" value="" type="hidden" readonly="readonly" style="width:100px;">  
							</td>
						</tr>
						#end						
						</tbody>
					</table>
					</div>
					
				</div>			
							
		</form>
		<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-200;
oDiv.style.height=sHeight+"px";
//]]>
</script>
	</body>
</html>
