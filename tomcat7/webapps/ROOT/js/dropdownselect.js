/**
 * 下拉选择框对象 * 生成的table的id为：t_[this.id]
 * @param id 用来存放生成TABLE的DIV的ID
 * @param param ajax动态取数据用到的参数参数 * @param inp 弹出下拉选择的输入框对象
 * @param selectName 弹出窗名
 * @returns {dropDownSelect}
 */
function dropDownSelect(id,param,inp,selectName)
{
	//下拉框的ID
	this.id = id;
	this.id = this.id.replace('.','');
	//进行AJAX取数据时用到的参数	this.param = param;
	//弹出选择框的输入框	this.inp = inp;
	//下拉框法前选择行	this.curLine = 0;
	//数据的总行数	this.rowsNumber = 0;
	//关闭的标签，当是false时，执行关闭的时候不会关闭当前下拉弹出窗
	this.closeFlag = true;
	//用来显示table的方法	this.showData = dataToTable;
	//当前选择向下移	this.selectDown = selectDown;
	//当前选择向上移	this.selectUp = selectUp;
	this.pageUp = pageUp;
	this.pageDown = pageDown;
	//用来表示是否已加载弹出框标题
	this.popupHead = null;
	inp.dropdown=this;
	//设置当前选择栏	this.setCurLine = setCurLine;
	this.returnFields = null;
	//获取选择值	this.curValue = curValue;
	//回填数据对应的函数,三个参数分别为：回填值，弹出窗名，输入文本框名	this.retFun = undefined;
	//回填数据对应的函数,三个参数分别为：是否主表，回填值，回填字段名	this.retFun2 = undefined;
	//ajax自动回填方法，根据window系统参数k_autofill非undefine来就
	this.jaxData = jaxData;
	//数据是否已翻到最后一页	this.pageEnd = false;
	//加载了几页数据
	this.pageNo = 1;
	//关闭当前弹出窗	this.close = close;
	//回填数据用到,自定义设置也用到，作为弹出窗名称
	this.selectName = selectName;
	//设置用户设置参数，并重新加载数据
	this.setParam = setParam;
	//加载当前页数据，并且设置pageEnd和pageNo
	this.loadPage = loadPage;
 }

/**
 * 展示下拉弹出框。 * 先判断是否已加载抬头，如果未加载则加载抬头 * 再根据输入框的内容加载显示数据 * status是当显示后是否要把关闭标志改成true
 */
function dataToTable(status)
{
	if(window.k_autofill != undefined)
	{
		var ret = this.jaxData();
		if(ret == true)return;
	}
	var self = this;
	if(jQuery("#"+this.id).size() ==0)
	{
		var reloadData = "if(this.inp.dropdown != undefined)this.inp.dropdown.closeFlag=false;";
		var str ="<div class=\"data-list\"  dropName=myDropName id=\""+this.id+"\" name=\""+this.id+"\" onmousedown=\""+reloadData+"\"></div>";
		jQuery(str).appendTo(jQuery(document.body));
		document.getElementById(this.id).inp = this.inp;
	}
	if(this.popupHead == null)
	{
		var tableStr = "<div class=\"tb-scroll\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"t_"+this.id+"\"><thead></thead><tbody></tbody></table></div>";
		jQuery("#"+this.id).html(tableStr);
		this.param.operation="PopupTitle";
		var width = 0;
		//生成抬头的HTML
		jQuery.post("/UtilServlet",this.param,function (dat, textStatus){
			var configData = eval("("+dat+")");
			var data = configData.showfields;
			var tr = "<tr>";
			for(var j = 0; j < data.length; j++)
			{
				tr += "<td width=\""+data[j].width+"\">"+data[j].displayname+"</td>";
				width += Number(data[j].width);
			}
			tr += "</tr>";
			if(width>200)
				jQuery("#t_"+self.id).attr("width",width);
			else
				jQuery("#t_"+self.id).attr("width","100%");
			jQuery("#"+self.id).attr("width",width);
			jQuery("#t_"+self.id).find("thead").html(tr);
			//回填字段
			data = configData.returnfields;
			var temp_str = "";
			for(var i = 0 ;i<data.length;i++)
				temp_str += data[i].fieldname +";";
			self.returnFields = temp_str;
			//加载完抬头,开始加载下拉框
			data = configData.condition;
			var selStr = "<select id=\"s_"+self.id+"\" style=\"float:left\">";
			selStr += "<option value=\"\">全部</option>";
			for(var j = 0; j < data.length; j++)
			{
				selStr += "<option value=\""+data[j].fieldname+"\">"+data[j].displayname+"</option>";
			}
			selStr += "</select>";
			jQuery("#"+self.id).append(selStr);
			jQuery("#"+self.id).append("<span class=\"cbox-span\" style=\"float:left\"><input class=\"c-box\" id=\"e_"+self.id+"\" type=\"checkbox\" value=\"true\"/><label class=\"l-bel\" for=\"e_"+self.id+"\">全匹配</label></span>");
			jQuery("#"+self.id).append("<span class=\"cbox-span\" style=\"float:left;color:blue;cursor:pointer\" id=\"clearspan\">清空</span>");
			jQuery("#"+self.id).append("<span class=\"cbox-span\" style=\"float:left;color:blue;cursor:pointer\" id=\"pu_"+self.id+"\"><<</span>");
			jQuery("#"+self.id).append("<span class=\"cbox-span\" style=\"float:left;color:blue;cursor:pointer\" id=\"p_"+self.id+"\"><</span>");
			jQuery("#"+self.id).append("<span class=\"cbox-span\" style=\"float:left;color:blue;cursor:pointer\" id=\"n_"+self.id+"\">></span>");
			jQuery("#"+self.id).append("<span class=\"cbox-span\" style=\"float:left;color:blue;cursor:pointer\" id=\"pd_"+self.id+"\">>></span>");
			jQuery("#"+self.id).append("<span class=\"cbox-span\" style=\"float:right;color:blue;cursor:pointer\" id=\"closespan\">关闭</span>");
			data = configData.favour;
			if(self.selectName == undefined) self.selectName =  data[0].selectName;
			jQuery("#s_"+self.id).val(data[0].searchfield);
			jQuery("#e_"+self.id).attr("checked",data[0].searchtype=='checked');
			jQuery("#s_"+self.id).change(function(){self.setParam();});
			jQuery("#e_"+self.id).change(function(){self.setParam();});
			jQuery("#n_"+self.id).click(function(){self.selectDown();});
			jQuery("#p_"+self.id).click(function(){self.selectUp();});
			jQuery("#pd_"+self.id).click(function(){self.pageDown();});
			jQuery("#pu_"+self.id).click(function(){self.pageUp();});
			jQuery("#clearspan").click(function(){
				var rfs = self.returnFields.split(";");
				var cls = "";
				for(rf in rfs){
					cls +="#;#";
				}
				self.curValue(cls);
			});
			jQuery("#closespan").click(function(){self.closeFlag=true;self.close();});
			var tmpLeft = self.inp.getBoundingClientRect().left+document.documentElement.scrollLeft;
			var tmpLeft2 = document.body.clientWidth - jQuery("#"+self.id).width();
			if(tmpLeft > tmpLeft2)
				tmpLeft = tmpLeft2;
			jQuery("#"+self.id).css("left",tmpLeft+"px");
			var tmpTop = self.inp.getBoundingClientRect().bottom+document.documentElement.scrollTop;
			var tmpTop2 = document.body.clientHeight - jQuery("#"+self.id).height();
			if(tmpTop > tmpTop2)
			{
				tmpTop2 = self.inp.getBoundingClientRect().top - jQuery("#"+self.id).height();
				if(tmpTop2>0)
					tmpTop = tmpTop2;
			}
			jQuery("#"+self.id).css("top",tmpTop);
		});
	}
	
	this.popupHead = "";
	this.pageNo = 1;
	this.pageEnd = false;
	this.rowsNumber = 0;
	if(status==undefined || status)
		self.closeFlag = true;
	if(window._qqq != undefined)
		clearTimeout(window._qqq);
	window._qqq = setTimeout(function(){jQuery("#t_"+self.id).find("tbody").html('');self.loadPage();},200);
	
}

function loadPage()
{
	if(this.pageEnd)//如果已经没数据就不加载
		return;
	var param = this.param;
	param.selectValue=this.inp.value;
	param.operation="DropdownPopup";
	param.pageNo = this.pageNo;
	var id = this.id;
	var self = this;
	//生成数据HTML
	 $.ajaxSetup({  
		    async : false  
	});     
	jQuery.post("/UtilServlet",param,function (dat, textStatus){
		var data = eval("("+dat+")");
		var tr = '';
		for(var i = 0; i < data.length; i++)
		{
			tr += "<tr style=\"display:none\" ret=\""+data[i][1]+"\">";
			var items = data[i][0].split("','");
			for(var j = 0; j < items.length; j++)
			{
				if(j==0)
					items[j] = items[j].substring(1);
				if(j==items.length-1)
					items[j] = items[j].substring(0,items[j].length-2);
				tr += "<td>"+items[j]+"</td>";
			}
			tr += "</tr>";
		}
		self.pageNo = self.pageNo + 1;
		
		if(data.length < 100)
		{
			self.pageEnd = true;
		}
		
		self.rowsNumber += data.length;
		jQuery("#t_"+id).find("tbody").append($(tr));
		if(self.pageNo==2)
			self.setCurLine(0);

		jQuery("#t_"+id).find("tbody tr").each(function(){
			this.onmousemove = function () {
				$(this).parents("div.data-list")[0].inp.dropdown.setCurLine($(this).index());
			};
			this.onclick = function () {
				$(this).parents("div.data-list")[0].inp.dropdown.setCurLine($(this).index());
				$(this).parents("div.data-list")[0].inp.dropdown.curValue();
			};
		});
	});
	 $.ajaxSetup({  
		   async : true  
	});     
}

//自动查数据，如果回填数据是一条则调回填函数并返回true，否则返回false
function jaxData()
{
	var self=this;
	var param = this.param;
	param.selectValue=this.inp.value;
	var id = this.id;
	jQuery.ajaxSetup({async : false});
	if(self.returnFields==null)
	{
		this.param.operation="PopupTitle";
		var width = 0;
		//生成抬头的HTML
		jQuery.post("/UtilServlet",this.param,function (dat, textStatus){
			var configData = eval("("+dat+")");
			data = configData.returnfields;
			var temp_str = "";
			for(var i = 0 ;i<data.length;i++)
				temp_str += data[i].fieldname +";";
			self.returnFields = temp_str;
		});
	}
	//生成数据HTML
	param.operation="DropdownPopup";
	var retv = false;
	jQuery.post("/UtilServlet",param,function (dat, textStatus){
		var data = eval("("+dat+")");
		if(data.length == 1)
		{
			var ret = data[0][1];
			self.curValue(ret);
			retv = true;
		}
	});
	jQuery.ajaxSetup({async : true});
	return retv;
}

function selectDown()
{
//	if(this.curLine < this.rowsNumber -1)
		this.setCurLine(this.curLine+1);
}
function selectUp()
{
	if(this.curLine > 0 )
		this.setCurLine(this.curLine-1);
}
function pageDown()
{
	this.setCurLine(this.curLine+5);
}
function pageUp()
{
	var pos = this.curLine-5;
	if(pos<0)
		pos =0;
	this.setCurLine(pos);
}
/**
 * 设置当前选择行 * @param num 行数
 * @returns {setCurLine}
 */
function setCurLine(num)
{
	if(num < 0) return;
	if(num >= this.rowsNumber)
	{
		if(this.pageEnd)
			return;
		else
		{
			this.loadPage();
		}
	}
	
	if(num >= this.rowsNumber)
		return;
		
	var tr = jQuery("#t_"+this.id).find("tbody tr:eq("+num+")");
	if(tr.length==1)
	{
		tr.addClass("sel").siblings("tr").removeClass("sel");
		this.curLine = num;
		if(tr.is(":hidden"))
		{
			tr.siblings("tr").hide();
			var i =0;
			while(i<5 && tr.length>0)
			{
				tr.show();
				tr = tr.next();
				i++;
			}
		}
	}
}
/**
 * 关闭当前弹出选择框 */
function close()
{
	if(this.closeFlag)
	{
		jQuery("#"+this.id).remove();
		this.inp.dropdown = undefined;
	}
}
/**
 * 获取选择行的值 * @returns
 */
function curValue(retValue)
{
	var tmp = null;
	if(retValue != undefined)
	{
		tmp = retValue;
	}
	else
	{
		var tr = jQuery("#t_"+this.id).find("tbody tr:eq("+this.curLine+")");
		if(tr.length > 0)
		{
			tmp = tr.attr("ret");
		}
	}
	if(tmp != null)
	{
		this.inp.oldValue=undefined;
		if(this.retFun!=undefined)
		this.retFun(tmp,this.selectName,this.inp.id);
		if(this.retFun2!=undefined)
		{
			if(tmp.indexOf("TOPID:") ==0){
				//返回值以TOPID:开头表明这是一个主弹窗选择的返回值，要取明细弹窗来回填
				jQuery.ajaxSetup({async : false});				
				var tt="";
				this.param.operation="DropdownChildData";
				jQuery.post("/UtilServlet?topId="+tmp,this.param,function (dat, textStatus){
					 tt= dat.split("::::");
				});
				
				this.retFun2(tt[0],tt[1]);				
				jQuery.ajaxSetup({async : true});
			}else{
				this.retFun2(this.returnFields,tmp);
			}
		}
		this.inp.oldValue=this.inp.value;
	}
	this.closeFlag = true;
	this.close();
}
/**
 * 下拉框和复选框的点击事件，设置用户自定义属性并重新读取数据
 */
function setParam()
{
	var par = "{\"searchfield\":\""+jQuery("#s_"+this.id).val()+"\",\"searchtype\":\""+jQuery("#e_"+this.id).attr("checked")+"\"}";
	var self = this;
	$.ajax({
		url:"favourstyle",
		async:false,data:{OPERATE:"SET",formId:this.selectName,property:"search_favour",value:par},
		success:function(data){
			self.popupHead = null;
			self.showData(false);
			self.closeFlag = false;
			}
	});
}
//下面所有代码均非些对象内函数，均为辅助的函数function timoutcl(inp){
	//关闭时，检查value和oldValue是否相同，如果不同，要清空
	if(inp.oldValue != undefined && inp.oldValue !=inp.value && inp.getAttribute("relationkey") != 'true' ){ //如果是relationKey不清除
		if(inp.dropdown != undefined){
			var rfs = inp.dropdown.returnFields.split(";");
			var cls = "";
			for(rf in rfs){
				cls +="#;#";
			}
			var tmp = cls;
			if(tmp != null)
			{
				inp.oldValue=undefined;
				if(inp.dropdown.retFun!=undefined)
					inp.dropdown.retFun(tmp,inp.dropdown.selectName,inp.id);
				if(inp.dropdown.retFun2!=undefined)
				{
					if(tmp.indexOf("TOPID:") ==0){
						//返回值以TOPID:开头表明这是一个主弹窗选择的返回值，要取明细弹窗来回填

						jQuery.ajaxSetup({async : false});				
						var tt="";
						inp.dropdown.param.operation="DropdownChildData";
						jQuery.post("/UtilServlet?topId="+tmp,inp.dropdown.param,function (dat, textStatus){
							 tt= dat.split("::::");
						});
						
						inp.dropdown.retFun2(tt[0],tt[1]);				
						jQuery.ajaxSetup({async : true});
					}else{
						inp.dropdown.retFun2(inp.dropdown.returnFields,tmp);
					}
				}
				inp.oldValue=inp.value;
			}
		}
	}
	
}
//关闭函数
function cl(inp)
{	
	if(typeof(reportcl) == "function"){ //如果报表清空函数存在，则调用此函数，否则按标准动作清空
		reportcl(inp);
	}else{ 
		if(inp.oldValue != undefined && inp.getAttribute("relationkey") != 'true' ){ //如果是relationKey不清除			//改为到关闭窗口时执行
			//inp.value=inp.oldValue;
			timoutcl(inp); //清除当前值
		}
	}
	setTimeout(function(){if(inp.dropdown != undefined)inp.dropdown.close();},200);
}
