/**
 * 现金流量交互
 */

jQuery.extend({
	cf_initEvent:function(){
		$(document.body).on("click",".cf_subjects_item",function(){
						
			//*****添加class******//
			$(".cf_subjects_item").removeClass('cf_subjects_active');
			$(this).addClass('cf_subjects_active');
			var accCode = $(this).attr("accCode");
			var row_id = $(this).attr('row_id');
			var obj = $("input[name=tblAccDetail_AccCode][value="+accCode+"]");
			
			$(".cf_refs_area[row_id!="+row_id+"]").addClass('cf_container_hidden');
			$(".cf_refs_area[row_id="+row_id+"]").removeClass('cf_container_hidden');
			
			
		})
		.on("blur",".cf_subjects_item",function(){
			$(this).removeClass('cf_subjects_active');
			
		})
		.on("click",".cf_sure",function(){
			$.cf_submit();
		})
		.on("click",".cf_cancel",function(){
			$.cf_cancel();
		})
		.on("change",".debit ",function(){
			var accCode = $(this).parents('.cf_refs_area').attr('accCode');
			var row_id = $(this).parents('.cf_refs_area').attr('row_id');
			var rid = $(this).find('option:selected').val();
			if(rid != undefined && rid != ''){
				var  cashItem = '';				
				$(".cf_refs_area_debit tr[accCode!="+accCode+"] table tr").each(function(){
					//****查找当前科目是否已被指定****//
					if($(this).find(".lend option:selected[value="+rid+"]").length>0){
						other = $(this).parents('.cf_refs_area_debit').attr('accCode');
						return;
					}	
				});
			}
			
			if(cashItem != ''){
				alert(cashItem+' 已指定该科目 ');
				return false;
			}
			
			if(rid !=''){
				var amount = $(".copyTr[row_id="+rid+"] input[name=tblAccDetail_DebitAmount]").val();
				$(this).parents("tr:eq(0)").find(".cf_amount").text(amount);
			} else{
				$(this).parents("tr:eq(0)").find(".cf_amount").text('');
			}
			if($(this).parents("tr:eq(0)").next("tr").length==0){
				$.cf_addRow($(this).parents("tr:eq(0)"),'debit');
			}
			//***调整所有条目选项***//
			$.cf_alterRef("debit");
			//*******end******//
		})
		.on("change",".lend",function(){
			var accCode = $(this).parents('.cf_refs_area').attr('accCode');
			var rid = $(this).find("option:selected").attr('value');
			var other = '';
			if(rid != undefined && rid != ''){
				$(".cf_refs_area_lend[accCode!="+accCode+"] table tr").each(function(){
					//****查找当前科目是否已被指定****//
					if($(this).find(".lend option:selected[value="+rid+"]").length>0){
						other = $(this).parents('.cf_refs_area_lend').attr('accCode');
						return;
					}					
					//****************end*************//
				});
			}
			if(other  != ''){
				//*******查找到已指定*******//
				var name = $(".cf_subjects_item[accCode="+accCode+"]").find(".cf_accCodeName").text();
				alert('当前科目已被现金科目：'+name+'指定，请重新选择！');
				return false;
			}
			if(rid != ''){
				var amount = $(".copyTr[row_id="+rid+"] input[name=tblAccDetail_LendAmount]").val();
				$(this).parents("tr:eq(0)").find(".cf_amount").text(amount);
			} else{
				$(this).parents("tr:eq(0)").find(".cf_amount").text('');
			}
			if($(this).parents("tr:eq(0)").next("tr").length==0){
				$.cf_addRow($(this).parents("tr:eq(0)"),'lend');
			}	
			//***调整所有条目选项***//
			$.cf_alterRef("lend");
			//*******end******//
		});

	},
	cf_alterRef:function(type){
		if(type=='debit'){
			var refs = [];
			$(".debit option:selected").each(function(){
				var val = $(this).attr("row_id");
				if(val != undefined && val != ''){
					refs.push(val);
				}
			});
			
			$(".debit option:not(:selected)").each(function(){
				var val = $(this).attr("row_id");
				if($.inArray(val,refs) == -1){
					$(this).removeClass("cf_ref_hidden");
				} else{
					$(this).addClass("cf_ref_hidden");
				}
			});
		} else if(type=='lend'){
			var refs = [];
			$(".lend option:selected").each(function(){
				var val = $(this).attr("row_id");
				if(val != undefined && val != ''){
					refs.push(val);
				}
			});
			
			$(".lend option:not(:selected)").each(function(){
				var val = $(this).attr("row_id");
				if($.inArray(val,refs) == -1){
					$(this).removeClass("cf_ref_hidden");
				} else{
					$(this).addClass("cf_ref_hidden");
				}
			});
		}
	},
	cf_addRow:function(obj,dir){
		var mItems = global_cf.mSelect;
		var sItems = global_cf.sSelect;
		
		var conf = $.cf_getProp();
		var items = '<option value="">请选择</option>';
		$(".copyTr").each(function(){
			var accCode = $(this).find("input[name=tblAccDetail_AccCode]").val();
			if($.inArray(accCode,conf) == -1 && !accCode.startsWith('1001') && !accCode.startsWith('1002')){
				if(dir=='lend'){
					var amount = $(this).find("input[name=tblAccDetail_LendAmount]").val();
					if(amount!=''){
						items += '<option row_id ='+$(this).attr('row_id')+' value="'+$(this).attr('row_id')+'">'+$(this).find('input[name=AccCodeName]').val()+'</option>';
					}
					
				} else if(dir=='debit'){
					var amount = $(this).find("input[name=tblAccDetail_DebitAmount]").val();
					if(amount!=''){
						items += '<option row_id = '+$(this).attr('row_id')+' value="'+$(this).attr('row_id')+'">'+$(this).find('input[name=AccCodeName]').val()+'</option>';
					}
				}
			}
		});
		items = '<select class="cf_selectRef '+dir+'">'+items+'</select>';
		
		
		var tr = "<tr><td>"+items+"</td><td>"+mItems+"</td><td>"+sItems+"</td><td></td><td class='cf_amount'></td><td class='cf_delItem'><span onclick='javascript:jQuery.cf_remove_ref(this);'>×</span></td></tr>";
		$(obj).after(tr);
		
	},
	cf_getProp:function(){
		return ['1001','1002'];
	},
	cf_initPop:function(){
		var div = "<div class='cf_container cf_container_hidden'>"+
				  "<div class='cf_title'>现金流量项目指定</div>" +
				  "<div class='cf_subjects'>" +
				  "<table border='0' cellspacing='0' cellpadding='0'>" +
				  "<thead><tr><td>现金流量科目</td><td>币别</td><td>原币金额</td><td>本位币金额</td></tr></thead>" +
				  "<tbody></tbody>" +
				  "</table>" +
				  "</div>"+
				  "<div class='cf_refs'>" +
				  "<table border='0' cellspacing='0' cellpadding='0'>" +
				  "<thead><tr><th>对方科目分录</th><th>主表项目</th><th>附表项目</th><th>原币</th><th>本位币</th><th>-</th></tr></thead>" +
				  "<tbody class='cf_refsTr'></tbody>" +
				  "</table>" +
				  "</div>"+
				  "<div class='cf_buttons'>" +
				  "<span class='cf_sure btn'>确定</span><span class='cf_cancel btn'>取消</span>" +
				  "</div>"
				  "</div>";
		var layer ='<div class="cf_layer"></div>';
		$(document.body).append(div).append(layer);
		
		//****初始化下拉框参数****//
		var global_mItems = global_cf.mItems;
		var global_sItems = global_cf.sItems;
		var mItems = "<option value=''>请选择</option>";
		var sItems = "<option value=''>请选择</option>";
		for(var i = 0 ;i<global_mItems.length;i++){
			var item = global_mItems[i];
			mItems += "<option value ='"+item[0]+"'>"+item[1]+"</option>";
		}
		mItems = "<select class='selectmItems'>"+mItems+"</select>";
		
		for(var j = 0;j<global_sItems.length;j++){
			var item = global_sItems[j];
			sItems += "<option value ='"+item[0]+"'>"+item[1]+"</option>";
		}
		sItems = "<select class='selectsItems'>"+sItems+"</select>";
		
		global_cf.mSelect = mItems;
		global_cf.sSelect = sItems;
	
	},
	cf_setCashFlow : function(){
		var items=$.cf_getProp();
		var nanme = '现金流量';
		var flag = false;
		/***查询当前凭据是否含有现金流量科目***/
		$("input[name=tblAccDetail_AccCode]").each(function(){
			var accCode = $(this).val();
			
			if(accCode == undefined || accCode ==''){
				return;
			}
			accCode = accCode.substring(0,4);			
			
			if($.inArray(accCode,items) != -1 || accCode.startsWith('1001') || accCode.startsWith('1002')){
				flag = true;
				return;
			}
		});
		if(!flag){
			return alert('没有相关现金科目');
		}
		
		/***弹出相应显示框***/
		$.cf_showPop();
		$.cf_alterRef("lend");
		$.cf_alterRef("debit");
		
	},
	cf_showPop:function(){
		$.cf_reload();
		$(".cf_container").draggable();
		$(".cf_container").removeClass('cf_container_hidden');
		//$(".cf_layer").show();
	},
	cf_remove_ref:function(obj){
		$(obj).parents("tr:eq(0)").remove();
	},
	cf_del:function(obj){
		if($(obj).parents("tr").prev("tr").length>0){
			$(obj).parents("tr").remove();
			$.cf_alterRef("lend");
			$.cf_alterRef("debit");
		}
	},
	cf_reload:function(){
		var global_mItems = global_cf.mItems; 
		var global_sItems = global_cf.sItems
		var items = $.cf_getProp();
		var mItems = global_cf.mSelect; 
		var sItems = global_cf.sSelect;
		
		//***借方科目
		var debit = "<option value=''>请选择</option>";
		
		//***贷方科目
		var lend = "<option value=''>请选择</option>";
		
		$(".copyTr").each(function(){
			var accCode = $(this).find("input[name=tblAccDetail_AccCode]").val();
			var row_id =  $(this).attr("row_id");
			accCode = accCode.substring(0,4);
			if(accCode ==''){
				return;
			}
			if($.inArray(accCode,items)== -1 && !accCode.startsWith('1001') && !accCode.startsWith('1002')){
				if($(this).find("input[name=tblAccDetail_DebitAmount]").val() != ''){
					debit +="<option row_id='"+row_id+"' value='"+row_id+"'>"+$(this).find("input[name=AccCodeName]").val()+"</option>";
				} else if($(this).find("input[name=tblAccDetail_LendAmount]").val() != ''){
					lend +="<option row_id='"+row_id+"' value='"+row_id+"'>"+$(this).find("input[name=AccCodeName]").val()+"</option>";
				}				
			}			
		});
		debitSelect = "<select class='cf_selectRef debit'>"+debit+"</select>";
		lendSelect = "<select class='cf_selectRef lend'>"+lend+"</select>";
		
		
		/***重新加载现金流量窗口***/
		$(".copyTr").each(function(){
			var accCode = $(this).find("input[name=tblAccDetail_AccCode]").val();
			var row_id = $(this).attr('row_id');
			accCode = accCode.substring(0,4);
			if(accCode ==''){
				return;
			}
			var tr = '';
			if($.inArray(accCode,items)!= -1 || accCode.startsWith('1001') || accCode.startsWith('1002')){//若为现金流量科目
				
				/**判断当前现金流量科目属于借/贷**/
				var debit = $(this).find("input[name=tblAccDetail_DebitAmount]").val();
				var lend = $(this).find("input[name=tblAccDetail_LendAmount]").val();
				
				if(debit !=''){
					/****插入现金流量科目条目****/
					$(".cf_subjects table tbody").append("<tr class='cf_subjects_item' row_id="+$(this).attr("row_id")+" accCode ='"+$(this).find("input[name=tblAccDetail_AccCode]").val()+"'><td class='cf_accCodeName'>"+$(this).find("input[name=AccCodeName]").val()+"</td><td>RMB-人民币</td><td>0</td><td class='amount'>"+$(this).find("input[name=tblAccDetail_DebitAmount]").val()+"</td><tr>");
					/****插入对方流量科目区域****/	
					$(".cf_refs .cf_refsTr").append("<tr class='cf_refs_area cf_refs_area_lend cf_container_hidden' row_id="+$(this).attr("row_id")+" accCode = '"+$(this).find("input[name=tblAccDetail_AccCode]").val()+"' ><td colspan = 6><table></table></td></tr>");
					
					//****若已配置对方科目，则加载对方科目****//
					var refs = $(this).find(".Refs").attr("value");
					if(refs != undefined && refs != ''){
						var refArr = refs.split(';');
						for(var i = 0 ;i < refArr.length;i++){
							var t = '<tr><td>'+lendSelect+'</td><td>'+mItems+'</td><td>'+sItems+'</td><td></td><td class="cf_amount"></td><td class="cf_delItem"><span onclick="javascript:$.cf_del(this)">×</span></td></tr>';
							var amount = $(".copyTr[row_id="+refArr[i]+"] input[name=tblAccDetail_LendAmount]").val();
							//var mItem = $(".copyTr[row_id="+refArr[i]+"]").attr('mItem');
							//var sItem = $(".copyTr[row_id="+refArr[i]+"]").attr('sItem');
							var mItem = $(".copyTr[row_id="+refArr[i]+"] .MainItem").attr('value');
							var sItem = $(".copyTr[row_id="+refArr[i]+"] .SecItem").attr('value');
							var tObj = $(t);
							tObj.appendTo(".cf_refs_area[row_id="+row_id+"] td table").find(".lend option[value="+refArr[i]+"]").attr('selected','selected').end().find(".cf_amount").text(amount);
							if(mItem != undefined && mItem !=''){
								tObj.find(".selectmItems option[value="+mItem+"]").attr("selected","selected");								
							}
							if(sItem != undefined && sItem !=''){
								tObj.find(".selectsItems option[value="+sItem+"]").attr("selected","selected");								
							}
						}
					}
					//********end*******//
					tr = '<tr><td>'+lendSelect+'</td><td>'+mItems+'</td><td>'+sItems+'</td><td></td><td class="cf_amount"></td><td class="cf_delItem"><span onclick="javascript:$.cf_del(this)">×</span></td></tr>';					
				} else if(lend !=''){
					/****插入现金流量科目条目****/
					$(".cf_subjects table tbody").append("<tr class='cf_subjects_item' row_id="+$(this).attr("row_id")+" accCode ='"+$(this).find("input[name=tblAccDetail_AccCode]").val()+"'><td class='cf_accCodeName'>"+$(this).find("input[name=AccCodeName]").val()+"</td><td>RMB-人民币</td><td>0</td><td class='amount'>"+$(this).find("input[name=tblAccDetail_LendAmount]").val()+"</td><tr>");
					/****插入对方流量科目区域****/
					$(".cf_refs .cf_refsTr").append("<tr class='cf_refs_area cf_refs_area_debit cf_container_hidden' row_id="+$(this).attr("row_id")+"  accCode = '"+$(this).find("input[name=tblAccDetail_AccCode]").val()+"' ><td colspan=6><table></table></td></tr>");
					//****若已配置对方科目，则加载对方科目****//
					var refs = $(this).find('.Refs').attr("value");
					if(refs != undefined && refs != ''){
						var refArr = refs.split(';');
						for(var i = 0 ;i < refArr.length;i++){
							var t = '<tr><td>'+debitSelect+'</td><td>'+mItems+'</td><td>'+sItems+'</td><td></td><td class="cf_amount"></td><td class="cf_delItem"><span onclick="javascript:$.cf_del(this)">×</span></td></tr>';
							var amount = $(".copyTr[row_id="+refArr[i]+"] input[name=tblAccDetail_DebitAmount]").val();
//							var mItem = $(".copyTr[row_id="+refArr[i]+"]").attr('mItem');
//							var sItem = $(".copyTr[row_id="+refArr[i]+"]").attr('sItem');
							var mItem = $(".copyTr[row_id="+refArr[i]+"] .MainItem").attr('value');
							var sItem = $(".copyTr[row_id="+refArr[i]+"] .SecItem").attr('value');
							var tObj = $(t);
							tObj.appendTo(".cf_refs_area[row_id="+row_id+"] td table").find(".debit option[value="+refArr[i]+"]").attr('selected','selected').end().find(".cf_amount").text(amount);
							if(mItem != undefined && mItem !=''){
								tObj.find(".selectmItems option[value="+mItem+"]").attr("selected","selected");								
							}
							if(sItem != undefined && sItem !=''){
								tObj.find(".selectsItems option[value="+sItem+"]").attr("selected","selected");								
							}
							
						}
					}
					//********end*******//
					tr = '<tr><td>'+debitSelect+'</td><td>'+mItems+'</td><td>'+sItems+'</td><td></td><td class="cf_amount"></td><td class="cf_delItem"><span onclick="javascript:$.cf_del(this)">×</span></td></tr>';
				}
				
				$(tr).appendTo(".cf_refs_area[row_id="+row_id+"] td table");
				
			}else{
				//$(".cf_refs table tbody").append("<tr class='cf_refs_item' accCode ='"+$(this).find("input[name=tblAccDetail_AccCode]")+"'><td>"+$(this).find("#AccCodeName").val()+"</td><td>"+mItems+"</td><td>"+sItems+"</td><td>0</td><td></td><td><span onclick='javascript:jQuery.cf_remove_ref(this);'>×</span></td></tr>");
			}
		});
	},
	cf_submit:function(){
		
		var name = '';
		
		$(".cf_subjects_item").each(function(){
			var accCode = $(this).attr('accCode');
			var row_id = $(this).attr('row_id');
			var amount = parseFloat($(this).find('.amount').text()==''?'0':$(this).find('.amount').text());
			var refAmount = 0;
			if(name != ''){
				return;
			}
			//*****判断借/贷 是否均衡*****//
			$(".cf_refs_area[row_id="+row_id+"] .cf_amount").each(function(){
				if($(this).text()!=''){
					refAmount += parseFloat($(this).text());
				}
			});
			if(refAmount != amount){
				name = $(this).find(".cf_accCodeName").text();
	            return;
			}
		
		});
		
		if(name != ''){
			alert("现金科目："+name+' 借贷现金不平。');
			return;
		}
		$(".Refs").attr("value",'');
		$(".MainItem").attr('value','');
		$(".SecItem").attr('value','');
		$(".cf_subjects_item").each(function(){
		
			var row_id = $(this).attr('row_id');
			
			var refs = '';
			//*****存储本科目对应对方科目****//
			$(".cf_refs_area[row_id="+row_id+"] .cf_selectRef option:selected").each(function(){
				var row = $(this).attr('value');
				if(refs.indexOf(row)==-1){
					refs += (refs==''?'':';')+row;
				}
				var mItem = $(this).parents("tr:eq(0)").find(".selectmItems option:selected").val();
				var sItem = $(this).parents("tr:eq(0)").find(".selectsItems option:selected").val();
			
				$(".copyTr[row_id="+row+"] .MainItem").attr("value",mItem);
				$(".copyTr[row_id="+row+"] .SecItem").attr("value",sItem);

				//$(".copyTr[row_id="+row+"]").attr('mItem',mItem).attr('sItem',sItem);
				
			});
			
			$(".copyTr[row_id="+row_id+"] .Refs").attr('value',refs);
			
		});
		$(".cf_container").addClass("cf_container_hidden").addClass("cf_active");
		//******修改凭证标志位******//
		$(".cf_flag").val('1');
		$(".cashflowFlag").text("已指定");
		$.cf_flush();
	},
	cf_cancel:function(){
		
		$(".cf_subjects table tbody").html();
		$(".cf_refs table tbody").html();
		$(".cf_container").addClass("cf_container_hidden");
		$(".cf_layer").hide();
		$.cf_flush();
	},
	cf_flush:function(){
		$(".cf_subjects table tbody tr").remove();
		$(".cf_refs table tbody tr").remove();
	},
	cf_check:function(){
		//*****判断当前凭证是否需要指定流量*******//
		var flag = $(".cf_flag").attr('value');
		if(flag == '1'){
			return 1;
		} else{
			var flag = '0';
			//****查找是否存在需要制定科目项目****//
			$("input[name=tblAccDetail_AccCode]").each(function(){
				var accCode = $(this).val();
				if(accCode.startsWith('1001') || accCode.startsWith('1002')){
					flag = '-1';
				}
			});
			$(".cf_flag").val(flag);
			//***********end************//
		}
		//************end****************//
	}
});