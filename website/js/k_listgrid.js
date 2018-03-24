var tTD;
function drag22(tableid)
{
	var divs = ["#kt_head","#k_head","#k_data","#k_column"];
	if(tableid != undefined)
	{
		divs = ["#"+tableid+"_tableFix","#"+tableid+"_tableHead","#"+tableid+"_tableData","#"+tableid+"_tableColumn"];
	}

	var n = 0;
	$(divs[1]+" td,"+divs[0]+" td[nohidden]").each(function(){
		if($(this).attr('colspan')==undefined)
			n +=1;
		else
			n += Number($(this).attr('colspan'));
		//以下三个事件为调整列宽使
		this.onmousedown = function () {
			if (event.offsetX > this.offsetWidth - 10) {
				tTD = this;
				tTD.mouseDown = true;
				tTD.oldX = event.x;
				tTD.oldWidth = tTD.offsetWidth;
				tTD.xoldWidth = jQuery(divs[2]+" table").width();
				tTD.xtoldWidth = jQuery(divs[3]+" table").width();
				
			}
		};
		this.onmouseup = function () {
			if(tTD != undefined)
			{
				//结束宽度调整
				tTD.mouseDown = false;
				tTD.style.cursor = 'default';
			}
		};
		this.onmousemove = function () {
			//更改鼠标样式
			if (event.offsetX > this.offsetWidth - 10)
				this.style.cursor = 'col-resize';
			else
				this.style.cursor = 'default';

			if(tTD == undefined) return;
		
			//调整宽度
			if (tTD.mouseDown != null && tTD.mouseDown == true) {
				if (tTD.oldWidth + (event.x - tTD.oldX)>0)
				{
					tTD.width = tTD.oldWidth + (event.x - tTD.oldX);
					tTD.xwidth = tTD.xoldWidth + (event.x - tTD.oldX);
					tTD.xtwidth = tTD.xtoldWidth + (event.x - tTD.oldX);
					var indextd = jQuery(divs[1]).find("thead td").index($(tTD));
					//调整列宽
					if(indextd>-1)
					{
						jQuery(divs[1]+" thead td:eq("+indextd+")")[0].width = tTD.width;
						jQuery(divs[1]+" table").width(tTD.xwidth);
						jQuery(divs[2]+" thead td:eq("+indextd+")")[0].width = tTD.width;
						jQuery(divs[2]+" table").width(tTD.xwidth);
						tTD.style.width = tTD.width;
					}else{ //此时调整的是锁定列前的列
						var indextd = jQuery(divs[0]).find("thead td").index($(tTD));
						//调整列宽
						if(indextd>-1)
						{
							jQuery(divs[1]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[1]+" table").width(tTD.xwidth);
							jQuery(divs[2]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[2]+" table").width(tTD.xwidth);
							
							jQuery(divs[0]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[0]+" table").width(tTD.xtwidth);
							jQuery(divs[3]+" thead td:eq("+indextd+")")[0].width = tTD.width;
							jQuery(divs[3]+" table").width(tTD.xtwidth);
							jQuery(divs[0]).width(jQuery(divs[3]+" table").width()+1);
							jQuery(divs[3]).width(jQuery(divs[3]+"#k_column table").width()+1);
							
							tTD.style.width = tTD.width;
						}					
					}
				}
				tTD.style.cursor = 'col-resize';
			}
		};
	});
}

function popMenu() {
	if(jQuery("#k_data table").attr("configscope") == undefined || jQuery("#k_data table").attr("configscope") == 'false')
	{
		return;
	}
    var menuItems = [{
        label : "隐藏此列",
        action : function(e) {
            if ($(e.target).attr("df") != undefined) {
                var str = "";
                jQuery("#k_head").find("thead td").each(function() {
                    if (jQuery(this).attr("df") != undefined && jQuery(this).attr("df") != jQuery(e.target).attr("df")) {
                        str += jQuery(this).attr("df") + ":" + this.width + "|";
                    }
                });
                setColumnSet(str);
            }
        }
    },{
        label : "锁定此列",
        action : function(e) {
            var str = "";
            var showField = jQuery("#k_data table").attr("showField");
            jQuery.each(showField.split("|"),function(){
            	if(this=='') return;
            	var f = this.split(":");
            	var cel = jQuery("#k_head thead td[df='"+f[0]+"']");
            	if(cel.length>0)
            	f[1] = (new Number(cel.width())).toFixed(0);
            	str += f[0] + ":" + f[1]+"|";
            });
            setColumnSet(str,$(e.target).attr("df"));
        }
    }, {
        label : "列配置",
        action : function(e) {
		    if(typeof(curloginUserId) != "undefined" && curloginUserId == "1"){
				if(!confirm("您是超级管理员，所做列配置操作将会影响其它所有用户，是否继续？")){
					return;
				}
			}
            var str = "";
            var showField = jQuery("#k_data table",document).attr("showField");
            var rightSelect = "<select id=\"rightselect\" multiple=\"multiple\" size=\"12\" class=\"dfselect\" style=\"width:200px\">";
            jQuery.each(showField.split("|"),function(){
            	if(this=='') return;
            	var f = this.split(":");
            	var cel = jQuery("#k_head thead td[df='"+f[0]+"']");
            	if(cel.length>0)
            	f[1] = cel.width()+11;
            	
            	if(f[1]!=undefined && f[1] != null)
            	{
            		rightSelect += "<option value=\""+f[1]+"\" df=\""+f[0]+"\">"+f[2]+"</option>";
            	}
            });
            rightSelect += "</select>";
            
            var leftSelect = "<select id=\"leftselect\" multiple=\"multiple\" size=\"12\" class=\"dfselect\" style=\"width:200px\">";
            var hideField = jQuery("#k_data table",document).attr("hiddenField");
            jQuery.each(hideField.split("|"),function(){
            	var f = this.split(":");
            	if(f[1]!=undefined)
            	{
            		leftSelect += "<option value=\""+f[1]+"\" df=\""+f[0]+"\">"+f[2]+"</option>";
            	}
            });
            leftSelect += "</select>";
            
            var cenButton = "<div style=\"display:inline-block\"><input onclick=\"listToList('leftselect','rightselect')\" type=\"button\" value=\"&rarr;\" style=\"display:block\"/><input type=\"button\" onclick=\"listToList('rightselect','leftselect')\" value=\"&larr;\" style=\"display:block\"/>&nbsp;</div>";
            var rightButton = "<div style=\"display:inline-block\"><input onclick=\"k_selectUp('rightselect')\" type=\"button\" value=\"&uarr;\" style=\"display:block\"/><input type=\"button\" value=\"&darr;\" style=\"display:block\"  onclick=\"k_selectDown('rightselect')\"/>&nbsp;</div>";
            
            str = '<div id="k_dialog" style="width:540px;padding:10px">' + leftSelect +cenButton + rightSelect + rightButton + '</div>';
            asyncbox.open({
            	html: str,
            	modal: true,
                width : 570,
                height: 310,
                title:'列配置',
                btnsbar : jQuery.btn.OKCANCEL,
                   　		callback : function(action){
                //判断 action 值。                   　			if(action == 'ok'){
                		var str = "";
                		var doc = document;
                		if(jQuery("#"+this.id).length == 0)
                		{
                			doc = parent.document;
                		}
                		jQuery("#rightselect option",doc).each(function() {
                			str += jQuery(this).attr("df") + ":" + (new Number(this.value)).toFixed(0) + "|";
                			});
                		setColumnSet(str);
                	　}
                	　if(action == 'cancel'){
                	　	jQuery.close(this.id);
                	　}
                }}
           );
        }
    }, {
        label : "保存列配置",
        action : function(e) {
            var str = "";
            var showField = jQuery("#k_data table").attr("showField");
            jQuery.each(showField.split("|"),function(){
            	if(this=='') return;
            	var f = this.split(":");
            	var cel = jQuery("#k_head thead td[df='"+f[0]+"']");
            	if(cel.length>0)
            	f[1] = (new Number(cel.width())).toFixed(0);
            	str += f[0] + ":" + f[1]+"|";
            });
            setColumnSet(str);
        }
    }, {
        label : "默认列配置",
        action : function(e) {
            onDefaultCol();
        }
    }];
    
    
    jQuery("#k_head td").each(function() {
    	jQuery(this).contextPopup({
            title : '',
            items : menuItems
        });
    });
    jQuery("#kt_head td[nohidden]").each(function() {
    	jQuery(this).contextPopup({
            title : '',
            items : menuItems
        });
    });
    
}


function listToList(left,right)
{
	jQuery("#"+left).find("option:selected").appendTo(jQuery("#"+right));
	
	jQuery("#"+right).find("option[df=statusView]").insertBefore(jQuery("#"+right).find("option:eq(0)")); 
	jQuery("#"+right).find("option[df=operationbt]").insertBefore(jQuery("#"+right).find("option:eq(0)")); 
}

function k_selectUp(sel)
{
	jQuery("#"+sel).find("option:selected").each(function(){
		if($(this).attr("df") == "operationbt" || $(this).attr("df") == "statusView"){
			alert("操作和单据状态的位置不可变动");
		}else{
			$(this).after($(this).prev());
		}
	});
}

function k_selectDown(sel)
{
	jQuery(jQuery("#"+sel).find("option:selected").toArray().reverse()).each(function(){
		if($(this).attr("df") == "operationbt" || $(this).attr("df") == "statusView"){
			alert("操作和单据状态的位置不可变动");
		}else{
			$(this).before($(this).next());
		}
	});
}