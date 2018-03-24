$(document).ready(function(){

	//点击表格行选中该行
	$(".listRange_list_function tr").slice(1).each(function(){
		  var p = this;
		  $(this).children().slice(1).click(function(){
			  $($(p).children()[0]).children().each(function(){
				  if(this.type=="checkbox"){
				  if(!this.checked){
					  this.checked = true;
				  }else{
					  this.checked = false;
				  }
				 }
		  	});
		  });
	});
	
	/*客户导入*/
	$("#importBtn").click(function(){
		var strURL = "/InquireAction.do?operation=91&tableName=OABasicGoodsInfo&NoBack=NoBack";			
		mdiwin(strURL,"商品导入") ;
	});
	/*客户导出*/
	$("#exportBtn").click(function(){
		if(!isChecked("keyId")){
			alert("请选择一条记录！");
			return false;
		}
		form.operation.value=99;
		blockUI();
		form.submit();
	});
	//提醒设置
	$("#alertSet").click(function(){		
    	var dataArr = [];
    	if(sureDeal('keyId')){   		
    		$("#mybody",document).each(function(){ 
				$("tr",this).each(function(){
					if($("input",this).is(":checked")){
						var goodsId = $("#goodsId",this).val();
						var tr = []; 														   
			      		tr.push(goodsId);      			             			   				
			      		dataArr.push(tr);
		    		}
				})													    		
			});
			var height = 340;	
			var url = "/InquireAction.do?operation=93&keyId="+dataArr;
			asyncbox.open({
				id:'dealAlert',url:url,title:'库存提醒设置',width:540,height:height,cache:false,modal:true,
				btnsbar:[{
				     text    : '保存',          //按钮文本
				     action  : 'ok'             //按钮 action 值，用于按钮事件触发 唯一
				  },{
					 text    : '关闭',                  //按钮文本
					 action  : 'no'             //按钮 action 值，用于按钮事件触发 唯一
					  }],
			    callback : function(action,opener){
				    if(action == 'ok'){
				    	opener.checkAlertSet();			   
						return false;
					}
				    if(action == 'no'){
				    	$.close("dealAlert");
						return false;
					}
		　　  　 }
		　  });
    	}		
	});
	//inquire 添加用品信息
	$("#addBtn").click(function(){
		var height = 440;	
		var url = "/InquireAction.do?operation=6";
		asyncbox.open({
			id:'deal1div',url:url,title:'添加办公用品',width:740,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.save();
					return false;
				}
	　　  　 }
	　  });
	});
	//查询详情
	$(".detailBtn").click(function(){
		var keyId = $(this).parents("tr").find("input").first().attr("value");
		var height = 440;	
		var url = "/InquireAction.do?operation=4&flay=detail&id="+keyId;
		asyncbox.open({
			id:'detaildiv',url:url,title:'用品详情',width:840,height:height,cache:false,modal:true,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.save();
					return false;
				}
	　　  　 }
	　  });
	});
	
	//添加前准备
	$("#addPre").click(function(){
		var flay = $(this).parents("ul").find("input").attr("value");
		addPre(flay);
	});
	//明细按钮
	$(".allBtn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=keyId]").attr("value");
		var flay = $(this).parents("tr").find("input:eq(1)").attr("value");
		detailed(keyId,flay);
		
	});
	//buy basic
	$("#addBasicBtn").click(function(){
		var flay = $(this).parents("ul").find("input").attr("value");
		addBuyBasic(flay);
	});
	
	//添加基本信息
	$("#toAddBasicBtn").click(function(){
		var flay = $(this).parents("ul").find("input").attr("value");
		toAddBasicBtn(flay);
	});
	//修改按钮
	$(".update_Btn").click(function(){			
		var keyId = $(this).parents("tr").find("input").last().attr("value");
		var flay = $(this).parents("tr").find("input:eq(1)").attr("value");
		//判断是否存在归还
		var flagBack = $(this).parents("tr").find("input[name*='flagBack']");
		var usernames = new Array();   
		var i = 0;   
		$(flagBack).each(function(){
			if(parseFloat($(this).val()) >0){
				usernames[i] = parseFloat($(this).val());
				}                      
                i++;   
            }   
		 )
		 if(usernames.length >0){
		 	alert("已经有归还不能进行修改 ");
		 	return false;
		 }
		updateAllDet(keyId,flay);
	});
	
});
function addPre(obj){
	var height = 440;	
	var flay = obj;	
	if(flay == "ADDBACK"){
		var title = "归还用品";
		var url = "/BackGoodsAction.do?operation=6";
		
	}
	if(flay == "ADDAPPLY"){
		var title = "添加领用用品";
		var url = "/ApplyGoodsAction.do?operation=6";
	}
	if(flay == "ADDBUY"){
		var title = "添加采购用品";
		var url = "/BuyGoodsAction.do?operation=6";
	}
	asyncbox.open({
		id:'addBasicdiv',url:url,title:title,width:740,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	if(flay == "ADDBACK"){
		    		opener.saveBack();
		    		
		    	}
		    	if(flay == "ADDAPPLY"){
		    		opener.saveApply();
		    	}	
		    	if(flay == "ADDBUY"){
		    		opener.saveBuy();
		    	}
				return false;
			}
　　  　 }
　  });
}

function detailed(keyId,flay){
	var height = 540;
	var width = 1140;
	if(flay == "APPLYALL"){
		var title = "领用用品明细";
		var url = "/ApplyGoodsAction.do?operation=4&GoodsNO=queryAll&applyId="+keyId;
	}
	if(flay == "BUYALL"){
		var title = "采购用品明细";
		var url = "/BuyGoodsAction.do?operation=4&GoodsNO=queryAll&buyId="+keyId;
	}
	if(flay == "BACKALL"){
		var title = "归还用品明细";
		var url = "/BackGoodsAction.do?operation=4&GoodsNO=queryAll&backId="+keyId;
	}	
	if(flay == "INQUIREALL"){
		var title = "办公用品明细";
		var url = "/InquireAction.do?operation=4&flay=queryAll&id="+keyId;
	}
	asyncbox.open({
		id:'dealAlldiv',url:url,title:title,width:width,height:height,height:height,cache:false,modal:true,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	$.close("dealAlldiv");
				return false;
			}
　　  　 }
　  });
}

function toAddBasicBtn(obj){
	var height = 440;
	var param = obj;
	if(param == "TOUPDATEAPPLY" || param == "TOADDAPPLY"){
		var title = "添加";
		var url = "/ApplyGoodsAction.do?operation=15&flag="+param;
	}
	if(param == "TOADDBACK" || param == "TOUPDATEBACK"){
		var title = "归还";
		var url = "/BackGoodsAction.do?operation=15&flag="+param;
	}	
	asyncbox.open({
		id:'dealbadiv',url:url,title:title,width:640,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	if(param=="TOUPDATEAPPLY"){
		    		var e = document.getElementById("UpAyTody");
		    		var table = 'UpAyTody';
			    	var nameArr = [10];
			    	nameArr = ['goodsName','type','goodsSpec','unit','qty','avgPrice','','applyQty','apply_total','a_use'];
			    	var oldArr = [];	
					$("#UpAyTody input[name='goodsId']",document).each(function(){ 
						var tr = []; 														   
			      		tr.push($(this).val());      			             			   				
			      		oldArr.push(tr);
							    		
					});
			    	opener.changeData(e,11,nameArr,oldArr,table);
		    	}
		    	if(param=="TOADDAPPLY"){
		    		var e = document.getElementById("applyTody");	
		    		var table = 'applyTody';
			    	var nameArr = [10];
			    	nameArr = ['goodsName','type','goodsSpec','unit','qty','avgPrice','','applyQty','apply_total','a_use'];
			    	var oldArr = [];	
					$("#applyTody input[name='goodsId']",document).each(function(){ 
						var tr = []; 														   
			      		tr.push($(this).val());      			             			   				
			      		oldArr.push(tr);
							    		
					});
			    	opener.changeData(e,11,nameArr,oldArr,table);
		    	}
		    	if(param == "TOADDBACK"){
		    		var e = document.getElementById("backTody");	
		    		var table = 'backTody';
			    	var nameArr = [8];
			    	nameArr = ['goodsName','applyDate','unit','applyRole','unback','backedQty','B_remark',''];
			    	var oldArr = [];	
					$("#backTody input[name='goodsId']",document).each(function(){ 
						var tr = []; 														   
			      		tr.push($(this).val());      			             			   				
			      		oldArr.push(tr);
							    		
					});
			    	opener.changeData(e,9,nameArr,oldArr,table);
		    	}
		    	if(param == "TOUPDATEBACK"){
		    		var e = document.getElementById("UpBackTody");	
		    		var table = 'UpBackTody';
			    	var nameArr = [8];
			    	nameArr = ['goodsName','applyDate','unit','applyRole','unback','backedQty','B_remark',''];
			    	var oldArr = [];	
					$("#UpBackTody input[name='goodsId']",document).each(function(){ 
						var tr = []; 														   
			      		tr.push($(this).val());      			             			   				
			      		oldArr.push(tr);
							    		
					});
			    	opener.changeData(e,9,nameArr,oldArr,table);
		    	}
		    	$.close("dealbadiv");
				return false;
			}
　　  　 }
　  });
}

function updateAllDet(keyId,flay){
	var height = 540;
	if(flay == "APPLYALL"){
		var title = "领用用品修改";
		var url = "/ApplyGoodsAction.do?operation=7&id="+keyId;
	}
	if(flay == "INQUIREALL"){
		height = 440;
		var title = "修改用品";
		var url = "/InquireAction.do?operation=7&id="+keyId;
	}
	if(flay == "BUYALL"){
		var title = "采购用品修改";
		var url = "/BuyGoodsAction.do?operation=7&id="+keyId;
	}
	if(flay == "BACKALL"){
		var title = "用品归还修改";
		var url = "/BackGoodsAction.do?operation=7&id="+keyId;
	}	
	asyncbox.open({
		id:'UpApplydiv',url:url,title:title,width:740,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	if(flay == "BACKALL"){
		    		opener.saveUpBack();
		    	}
		    	if(flay == "INQUIREALL"){
		    		opener.save();
		    	}
				if(flay == "APPLYALL"){
					opener.saveUpApply();    		
				}
				if(flay == "BUYALL"){
					opener.saveUpBuy();
				}		    	
				return false;
			}
　　  　 }
　  });
};
function addBuyBasic(flay){
		var height = 440;
		var da=[];
		var url = "/BuyGoodsAction.do?operation=15&flay="+flay;
		asyncbox.open({
			id:'deal1ldiv',url:url,title:'添加',width:640,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	//
			    	var e = document.getElementById("mytody");	
			    	var tabArr = [];	
					$("#mytody input[name='goodsId']",document).each(function(){ 
						var tr = []; //存行数据  														   
			      		tr.push($(this).val());      			             			   				
						tabArr.push(tr);
							    		
					});   					
					opener.buyFillData(e,tabArr);					    		 		
			    	$.close("deal1ldiv");	    	
					return false;
				}
	　　  　 }
	　  });
	};
	
function buyFillData(e,tabArr,otherdata){
	if(otherdata != undefined){
		var dat = otherdata;		
	}else{
		var dat = addData();
	}
	if(dat.length>0){
		if(tabArr.length>0){
			for(var i=0;i<dat.length;i++){
				var flay = true;
				for(var k=0;k<tabArr.length;k++){
					if(tabArr[k][0]==dat[i][4]){			    						
						flay = false;
						break;
    				}			    				
				}
				if(flay){
					var tr = document.createElement("tr");			
    				var td = document.createElement("td");
    				td.innerHTML="<td><input type=\"checkbox\" name=\"keyId\" id=\"keyId\" /></td>";
    				tr.appendChild(td);			    				
    				for(var j=0;j<=9;j++){
    					if(j==0){
    						td = document.createElement("td");
		    				td.innerHTML="<input name=\"goodsName\" readonly=\"readonly\" id=\"goodsName\" type=\"text\" value="+dat[i][j]+" >";
		    				tr.appendChild(td);
    						}else if(j==1){
    							td = document.createElement("td");
			    				td.innerHTML="<input name=\"type\" readonly=\"readonly\" id=\"type\" type=\"text\" value="+dat[i][j]+" >";
			    				tr.appendChild(td);
    						}else if(j==2){
    							td = document.createElement("td");
			    				td.innerHTML="<input name=\"goodsSpec\" readonly=\"readonly\" id=\"goodsSpec\" type=\"text\" value="+dat[i][j]+" >";
			    				tr.appendChild(td);
    						}else if(j==3){
    							td = document.createElement("td");
			    				td.innerHTML="<input name=\"unit\" id=\"unit\" readonly=\"readonly\" type=\"text\" value="+dat[i][j]+" >";
			    				tr.appendChild(td);
    						}else if(j==4){
    							td = document.createElement("td");
			    				td.innerHTML="<input name=\"goodsId\" id=\"goodsId\" type=\"hidden\" value="+dat[i][j]+" >";
			    				tr.appendChild(td);
    						}else if(j==5){
    							td = document.createElement("td");
			    				td.innerHTML="<input name=\"buyQty\" id=\"buyQty\" type=\"text\" onchange=\"total1(this);\" value=\"\" />";
			    				tr.appendChild(td);
    						}else if(j==6){
    							td = document.createElement("td");
			    				td.innerHTML="<input name=\"buyPrice\" id=\"buyPrice\" type=\"text\" onchange=\"total1(this);\" value=\"\"  />";
			    				tr.appendChild(td);
    						}else if(j==7){
    							td = document.createElement("td");
			    				td.innerHTML="<input name=\"total\" id=\"total\" type=\"text\" readonly=\"readonly\"  />";
			    				tr.appendChild(td);
    						}else if(j==8){
    							td = document.createElement("td");
			    				td.innerHTML="<input type=\"text\" value=\"\" name=\"b_Use\" id=\"b_Use\" />";
			    				tr.appendChild(td);
    						}else if(j==9){
    							td = document.createElement("td");
			    				td.innerHTML="<input type=\"button\" value=\"X\"  style=\"color:#ff0000;font-size:15px;\" title=\"删除\" onclick=\"delRow('mytody',this);\" />";
			    				tr.appendChild(td);
    						}
    					}
    				e.appendChild(tr);	    		
				}
			}
		}else{
			for(var i=0;i<dat.length;i++){
    			var tr = document.createElement("tr");			
				var td = document.createElement("td");
				td.innerHTML="<td><input type=\"checkbox\" name=\"keyId\" id=\"keyId\" /></td>";
				tr.appendChild(td);			    				
				for(var j=0;j<=9;j++){
					if(j==0){
						td = document.createElement("td");
	    				td.innerHTML="<input name=\"goodsName\" id=\"goodsName\" readonly=\"readonly\" type=\"text\" value="+dat[i][j]+" >";
	    				tr.appendChild(td);
						}else if(j==1){
							td = document.createElement("td");
		    				td.innerHTML="<input name=\"type\" id=\"type\" readonly=\"readonly\" type=\"text\" value="+dat[i][j]+" >";
		    				tr.appendChild(td);
						}else if(j==2){
							td = document.createElement("td");
		    				td.innerHTML="<input name=\"goodsSpec\" id=\"goodsSpec\" readonly=\"readonly\" type=\"text\" value="+dat[i][j]+" >";
		    				tr.appendChild(td);
						}else if(j==3){
							td = document.createElement("td");
		    				td.innerHTML="<input name=\"unit\" id=\"unit\" readonly=\"readonly\" type=\"text\" value="+dat[i][j]+" >";
		    				tr.appendChild(td);
						}else if(j==4){
							td = document.createElement("td");
		    				td.innerHTML="<input name=\"goodsId\" id=\"goodsId\" type=\"hidden\" value="+dat[i][j]+" >";
		    				tr.appendChild(td);
						}else if(j==5){
							td = document.createElement("td");
		    				td.innerHTML="<input name=\"buyQty\" id=\"buyQty\" type=\"text\" onchange=\"total1(this);\" value=\"\"   />";
		    				tr.appendChild(td);
						}else if(j==6){
							td = document.createElement("td");
		    				td.innerHTML="<input name=\"buyPrice\" id=\"buyPrice\" type=\"text\" onchange=\"total1(this);\" value=\"\"  />";
		    				tr.appendChild(td);
						}else if(j==7){
							td = document.createElement("td");
		    				td.innerHTML="<input name=\"total\" id=\"total\" type=\"text\"  readonly=\"readonly\" />";
		    				tr.appendChild(td);
						}else if(j==8){
							td = document.createElement("td");
		    				td.innerHTML="<input type=\"text\" value=\"\" name=\"b_Use\" id=\"b_Use\" />";
		    				tr.appendChild(td);
						}else if(j==9){
							td = document.createElement("td");
		    				td.innerHTML="<input type=\"button\" value=\"X\" style=\"color:#ff0000;font-size:15px;\" title=\"删除\" onclick=\"delRow('mytody',this);\" />";
		    				tr.appendChild(td);
						}
					}
				e.appendChild(tr);
    		}
		}			    		
		
	}			    	   
}
	
function sureDeal(itemName){		
		if(!isChecked(itemName)){
			alert("请选择一条记录");
			return false;
		}else{
			return true;
		}
	}
function total1(obj){	
	var index = obj.parentNode.parentNode.rowIndex - 1;
	//alert(document.getElementById("buyPrice")[index].value);
	var buyQty = document.getElementsByName("buyQty");
	
	var buyPrice = document.getElementsByName("buyPrice");
	var flag = document.getElementById("flag").value;


	if(isNum(buyQty[index].value)){	
		alert("请输入数字！");
		document.getElementsByName("buyQty")[index].value = "";
		return false;
	}
	if(Number(buyQty[index].value)<0){		
		document.getElementsByName("buyQty")[index].value = "";
		alert("请输入正数！");
		return false;
	}
	if(parseFloat(buyQty[index].value) == parseFloat(0)){		
		document.getElementsByName("buyQty")[index].value = "";
		alert("请输入正数！");
		return false;
	}
	if(buyQty[index].value.toString().indexOf(".") !=-1){
		if(buyQty[index].value.toString().split(".")[1].length > flag){
			document.getElementsByName("buyQty")[index].value = "";
			alert("有效位数不能大于"+flag);
			return false;
		}
	}
	if(isNum(buyPrice[index].value)){	
		alert("请输入数字！");
		document.getElementsByName("buyPrice")[index].value = "";
		return false;
	}
	if(Number(buyPrice[index].value)<0){		
		document.getElementsByName("buyPrice")[index].value = "";
		alert("请输入正数！");
		return false;
	}
	if(parseFloat(buyPrice[index].value) == parseFloat(0)){		
		document.getElementsByName("buyPrice")[index].value = "";
		alert("请输入正数！");
		return false;
	}
	if(buyPrice[index].value.toString().indexOf(".") !=-1){
		if(buyPrice[index].value.toString().split(".")[1].length > flag){
			document.getElementsByName("buyPrice")[index].value = "";
			alert("有效位数不能大于"+flag);
			return false;
		}
	}
		
	if(buyPrice[index].value != "" && buyQty[index].value !=""){
		var total = parseFloat(buyQty[index].value) * parseFloat(buyPrice[index].value);
		document.getElementsByName("total")[index].value = total.toFixed(flag);
	}

	var app_qty = parseFloat(0);
	var applyTotal = parseFloat(0);
	$("#mytody",document).each(function(){
		$("tr",this).each(function(){					   
			var applyQty = $("#buyQty",this).val();
			if(applyQty==""){
				applyQty=parseFloat(0);
			}
			var avgPrice = $("#buyPrice",this).val();
			if(avgPrice==""){
				avgPrice=parseFloat(0);
			}
			applyTotal += (parseFloat(applyQty) * parseFloat(avgPrice));
			app_qty += parseFloat(applyQty);					
  		});
		})
	$("#buyTotal",document).val(applyTotal.toFixed(flag)); 
	$("#buy_qty",document).val(app_qty);

}

function delRow(e,index){
	var row = index.parentNode.parentNode.rowIndex - 1;
	var table = document.getElementById(e);
	table.deleteRow(row);
}

function delCheck(obj){
	var table = document.getElementById(obj);
	if(obj=="mytody"){
		$("#mytody",document).each(function(){			
			$("tr",this).each(function(){	
				var p = this.rowIndex;
				if($("input",this).is(":checked")){					
				table.deleteRow(p-1);  
				}			             			   				
		  	});		
		})
	}
	if(obj=="UpBackTody"){
		$("#UpBackTody",document).each(function(){			
			$("tr",this).each(function(){	
				var p = this.rowIndex;
				if($("input",this).is(":checked")){				   
				table.deleteRow(p-1);  
				}			             			   				
		  	});		
		})
	}
	if(obj=="backTody"){
		$("#backTody",document).each(function(){
			$("tr",this).each(function(){
				var p = this.rowIndex;
				if($("input",this).is(":checked")){														   
					table.deleteRow(p-1);
				}				
			});	
		})
	}
	if(obj=="applyTody"){
		$("#applyTody",document).each(function(){
			$("tr",this).each(function(){
				var p = this.rowIndex;
				if($("input",this).is(":checked")){														   
					table.deleteRow(p-1);
				}				
			});	
		})
	}
}

function changeData(e,length,obj,oldArr,tabl,otherdata){
	var len = length-1;
	if(otherdata != undefined){
		var data = otherdata;		
	}else{
		var data = addData();
	}	
	var idArr = obj;
	var oldId = oldArr;
	if(oldId.length>0){
		for(var i=0;i<data.length;i++){
			var flay = true;
			for(var k=0;k<oldId.length;k++){
				if(oldId[k][0]==data[i][7]){			    						
					flay = false;
					break;
				}			    				
			}
			if(flay){
				var tr = document.createElement("tr");					
				for(var j=0;j<length;j++){
					if(j<5){
						td = document.createElement("td");
						td.innerHTML="<input name="+idArr[j]+" id="+idArr[j]+" type=\"text\"  readonly=\"readonly\" value="+data[i][j]+" >";
						tr.appendChild(td);
					}else if(j==5){
						td = document.createElement("td");
						td.innerHTML="<input name="+idArr[j]+" id="+idArr[j]+" type=\"text\" onchange=\"checkData('"+tabl+"',this);\" value=\"\"  />";
						tr.appendChild(td);
					}else if(j>5 && j<len-1){
						td = document.createElement("td");
						td.innerHTML="<input name="+idArr[j]+" id="+idArr[j]+" type=\"text\"  value=\"\"  />";
						tr.appendChild(td);
					}else if(j==len-1){	
						td = document.createElement("td");
						td.innerHTML="<input name=\"goodsId\" id=\"goodsId\" type=\"hidden\" value="+data[i][j]+" >";
						tr.appendChild(td);											
					}else if(j==len){
						td = document.createElement("td");
						td.innerHTML="<input type=\"button\" value=\"X\" style=\"color:#ff0000;font-size:15px;\" title=\"删除\" onclick=\"delRow('"+tabl+"',this);\" />";
						tr.appendChild(td);
						}
					}
				e.appendChild(tr);
			}
		}
	}else{
		for(var i=0;i<data.length;i++){
			var tr = document.createElement("tr");						
			for(var j=0;j<length;j++){
				if(j<5){
					td = document.createElement("td");
					td.innerHTML="<input name="+idArr[j]+" id="+idArr[j]+" readonly=\"readonly\" type=\"text\" value="+data[i][j]+" >";
					tr.appendChild(td);
				}else if(j==5){
					td = document.createElement("td");
					td.innerHTML="<input name="+idArr[j]+" id="+idArr[j]+" type=\"text\" onchange=\"checkData('"+tabl+"',this);\" value=\"\"  />";
					tr.appendChild(td);					
				}else if(j>5 && j<len-1){
					td = document.createElement("td");
					td.innerHTML="<input name="+idArr[j]+" id="+idArr[j]+" type=\"text\"  value=\"\"  />";
					tr.appendChild(td);
				}else if(j==len-1){
					td = document.createElement("td");
					td.innerHTML="<input name=\"goodsId\" id=\"goodsId\" type=\"hidden\" value="+data[i][j]+" >";
					tr.appendChild(td);
					
				}else if(j==len){			
					td = document.createElement("td");
					td.innerHTML="<input type=\"button\" value=\"X\" style=\"color:#ff0000;font-size:15px;\" title=\"删除\" onclick=\"delRow('"+tabl+"',this);\" />";
					tr.appendChild(td);
					}
				}
			e.appendChild(tr);
			}
	}	
};

function addData(){
	if(sureDeal('keyId')){ 
		var tableArr = [];
		$("table tr:not(:first)",document).each(function(){ //便利除标题行外所有行               
			var trArr = []; //存行数据  				
			if($("input",this).is(":checked")){				
				$("input:not(:first)",this).each(function(){					   
	      			trArr.push($(this).val());      			             			   				
	      		});
				tableArr.push(trArr);
				}     		
		});   		
		return tableArr;
	}
}

function checkData(obj,index){
	var row = index.parentNode.parentNode.rowIndex - 1;
	var avgPrice = document.getElementsByName("avgPrice");
	var qty = document.getElementsByName("qty");
	
	if(obj == "applyTody" || obj == "UpAyTody"){		
		var flag = document.getElementById("flag").value;
		var applyQty = document.getElementsByName("applyQty");		
		if(isNum(applyQty[row].value)){	
			alert("请输入数字！");
			document.getElementsByName("applyQty")[row].value = "";
			return false;
		}
		if(Number(applyQty[row].value)<0){		
			document.getElementsByName("applyQty")[row].value = "";
			alert("请输入正数！");
			return false;
		}
		if(parseFloat(applyQty[row].value) == parseFloat(0)){		
			document.getElementsByName("applyQty")[row].value = "";
			alert("请输入正数！");
			return false;
		}
		if(applyQty[row].value.toString().indexOf(".") !=-1){
			if(applyQty[row].value.toString().split(".")[1].length > flag){
				document.getElementsByName("applyQty")[row].value = "";
				alert("有效位数不能大于"+flag);
				return false;
			}
		}

		applyqty = parseFloat(applyQty[row].value);	
		var app_qty = parseFloat(0);
		var applyTotal = parseFloat(0);
		if(parseFloat(qty[row].value) < applyqty){
			alert("领用数量不能大于库存！");
			applyqty=parseFloat(0);
			document.getElementsByName("applyQty")[row].value = "";
			return false;
		}
		if(parseFloat(qty[row].value) == parseFloat(0)){
			alert("领用数量不能为0！");
			document.getElementsByName("applyQty")[row].value = "";
			return false;
		}
		document.getElementsByName("apply_total")[row].value = (parseFloat(applyqty) * parseFloat(avgPrice[row].value)).toFixed(flag);
		if(obj == "applyTody"){
			$("#applyTody",document).each(function(){
				$("tr",this).each(function(){					   
					var applyQty = $("#applyQty",this).val();
					if(applyQty==""){
						applyQty=parseFloat(0);
					}
					var avgPrice = $("#avgPrice",this).val();
					if(avgPrice==""){
						avgPrice=parseFloat(0);
					}
					applyTotal += (parseFloat(applyQty) * parseFloat(avgPrice));
					app_qty += parseFloat(applyQty);					
		  		});
				})
		}else{
			$("#UpAyTody",document).each(function(){
				$("tr",this).each(function(){					   
					var applyQty = $("#applyQty",this).val();
					if(applyQty==""){
						applyQty=parseFloat(0);
					}
					var avgPrice = $("#avgPrice",this).val();
					if(avgPrice==""){
						avgPrice=parseFloat(0);
					}
					applyTotal += (parseFloat(applyQty) * parseFloat(avgPrice));
					app_qty += parseFloat(applyQty);				
		  		});
				})
		}		
		$("#applyTotal",document).val(applyTotal.toFixed(flag)); 
		$("#apply_qty",document).val(app_qty);	
	}
	if(obj == "backTody" || obj == "UpBackTody"){
		var backedQty = document.getElementsByName("backedQty");
		var unback = document.getElementsByName("unback");		
		if(isNum(backedQty[row].value)){	
			alert("请输入数字！");
			document.getElementsByName("backedQty")[row].value = "";
			return false;
		}
		if(Number(backedQty[row].value)<0){		
			document.getElementsByName("backedQty")[row].value = "";
			alert("请输入正数！");
			return false;
		}
		if(parseFloat(backedQty[row].value) == parseFloat(0)){		
			document.getElementsByName("backedQty")[row].value = "";
			alert("请输入正数！");
			return false;
		}
		if(backedQty[row].value.toString().indexOf(".") !=-1){
			if(backedQty[row].value.toString().split(".")[1].length > flag){
				document.getElementsByName("backedQty")[row].value = "";
				alert("有效位数不能大于"+flag);
				return false;
			}
		}

		if(parseFloat(unback[row].value) < parseFloat(backedQty[row].value)){
			alert("归还数量不能大于未归还数量！");
			document.getElementsByName("backedQty")[row].value = "";	
			return false;
		}			
		if(parseFloat(0) == parseFloat(backedQty[row].value)){
			alert("归还数量不能为0！");
			document.getElementsByName("backedQty")[row].value = "";	
			return false;
		}
		if(obj == "backTody"){		
			//backedQty = parseFloat(backedQty[row].value);				
			var bck_qty = parseFloat(0);
			$("#backTody",document).each(function(){
				$("tr",this).each(function(){					   
					var backedqty = $("#backedQty",this).val();
					if(backedqty==""){
						backedqty=parseFloat(0);
					}		
					bck_qty += parseFloat(backedqty);				
		  		});
				})
			$("#back_qty",document).val(bck_qty);
		}else{		
			//backedQty = parseFloat(backedQty[row].value);				
			var bck_qty = parseFloat(0);
			$("#UpBackTody",document).each(function(){
				$("tr",this).each(function(){					   
					var backedqty = $("#backedQty",this).val();
					if(backedqty==""){
						backedqty=parseFloat(0);
					}		
					bck_qty += parseFloat(backedqty);				
		  		});
				})
			$("#back_qty",document).val(bck_qty);
		}	
	}				
}
function isNum(str) 
{ 
	return isNaN(str); 
}
function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}

/**
*	锁屏
*/
function blockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍后。。。</div></div>",css:{ background: 'transparent'}}); 
	}
}

/**
 * 确认删除
 */
function sureDel(obj){
	if(!isChecked(obj)){
		alert("请选择一条记录!");
		return false;
	}
	if(!confirm("确定要删除？")){
		return false;
	}else{
		return true;
	}	
}

function closeChild(){
	$.close("dealAlldiv");
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
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds,flag)
　　　　　	}
　　　	}
　	});
}
function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,flag);
	jQuery.close('Popdiv');
}
function newOpenSelect(employees,fieldName,fieldNameIds,flag){
	var employees = employees.split("|") ;	
	//for(var j=0;j<employees.length-1;j++){
	if(employees.length>1){
		var field = employees[employees.length-2].split(";") ;
		if(field!=""){			
				document.getElementById(fieldNameIds).value=field[1];
			}
	}
		
		//}
			
}	

//点击换颜色
function setGround(obj,boolean){
	if(boolean){
		obj.style.background="#E7FCA9";
	}else{
		obj.style.background=typeof(obj.bk)!="undefined"?obj.bk:"";
	}
}

//双击事件
function dbFillData(flag,goodsName,type,goodsSpec,unit,qty,avgPrice,backsign,id){
	
	if(flag == "TOUPDATEAPPLY" || flag == "TOADDAPPLY"){
		var data = [7]; 
		data = [goodsName,type,goodsSpec,unit,qty,avgPrice,id];
		var datas = [];
		datas.push(data);
		if(flag == "TOUPDATEAPPLY" ){
			window.parent.jQuery.opener("UpApplydiv").dbfillData(datas);
		}else{
			window.parent.jQuery.opener("addBasicdiv").dbfillData(datas);
		}		
	}
	
	if(flag == "TOADDBACK" || flag == "TOUPDATEBACK"){
		var back_sign = parseFloat(avgPrice) - parseFloat(backsign);
		var data = [8]; 
		data = [goodsName,type,goodsSpec,unit,qty,back_sign,'',id];
		var datas = [];
		datas.push(data);
		if(flag == "TOUPDATEBACK" ){
			window.parent.jQuery.opener("UpApplydiv").dbfillData(datas);
		}else{
			window.parent.jQuery.opener("addBasicdiv").dbfillData(datas);
		}
	}
	if(flag == "ADDBUY" || flag == "UPBUY"){
		var data = [7]; 
		data = [goodsName,type,goodsSpec,unit,id];		
		var datas = [];
		datas.push(data);
		if(flag == "ADDBUY" ){
			window.parent.jQuery.opener("addBasicdiv").dbfillData(datas);
		}else{
			window.parent.jQuery.opener("UpApplydiv").dbfillData(datas);
		}		
	}
}
