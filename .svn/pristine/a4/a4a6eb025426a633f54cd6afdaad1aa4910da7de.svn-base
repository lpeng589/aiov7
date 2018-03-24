(function($){
	$.fn.select = function(options){
		//定义常量
		var settings = $.extend({},options);
		this.each(function() {
			var $html = $('<div class="menu_select"><div class="menu_span"></div><div class="menu_border_bg"><ul class="menu_bg"></ul></div></div>');
			//将下拉框隐藏，把模版插入其后
			//alert($(this).attr("name"));
			var $this = $(this).hide().after($html);
			//声明全局变量
			var $list = $html.find('ul'),$span = $html.find('.menu_span'),$select = $html.find('.menu_border_bg');
			//将option遍历到li中			$this.find('option').each(function(){
				var $option = $(this);
				$('<li val="'+$option.val()+'">'+$option.text()+'</li>').appendTo($list);
				if($option.prop('selected') === true){
					$this.val($option.val());
					$span.text($option.text());
				}
			});
			if($this.attr("id") !="Gender" && $this.attr("id") !="dateType" && $this.attr("id") !="mainUser" ){
				$('<span ></span>').appendTo($list);
				$('<li val="new">新增选项...</li>').appendTo($list);
			}
			$span.click(function(event){
				$(".menu_border_bg").slideUp(200);
				//阻止事件冒泡
				event.stopPropagation();
				if(!$list.find('li').size())
					return ;
				$select.css({top:$html.position().top+24+"px"})
				if($select.css("display")=="none"){
					$select.slideToggle(200);
				}else{
					$select.slideUp(200);
				}
			});
			
			$list.find('li').click(function(){
				var $li = $(this);
				if($li.attr("val")=="new"){
					//alert("编辑选项数据");
					var url = "/CRMClientAction.do?operation=6&type=enumer&enumerName=" + $this.attr("enumerName")+"&selName="+$this.attr("enumerName");
					asyncbox.open({
						id:'enumerId',url:url,title:'新增'+$this.prev().text().replace("*","").replace("：",""),width:430,height:140,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
						callback : function(action,opener){
							if(action == "ok"){
								if(typeof(top.jblockUI)!="undefined"){
									top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
								}
								var enumerVal = opener.form.enumerVal.value;
								var languageVal = opener.form.languageVal.value;
								
								var enumerId = opener.form.enumerId.value; 
								$.ajax({
									type: "POST",
									url: "/CRMClientAction.do",
									data: "operation=1&type=enumer&sort=100&enumerVal="+enumerVal+"&languageVal="+languageVal+"&enumerId="+enumerId,
									success: function(msg){
										if(msg == "success"){
											alert("添加成功");
											//$("select [id='" + $this.attr("id") +"']").append('<option value="'+enumerVal+'">'+languageVal+'</option>');
											jQuery("select[enumerName='"+$this.attr("enumerName")+"']").each(function(){
												$(this).append('<option value="'+enumerVal+'">'+languageVal+'</option>')
												$(this).next().find("ul").find("span").before($('<li onclick="newOption(this)" selectName="' + $this.attr("id") + '"  val="'+enumerVal+'" >'+languageVal+'</li>'));
											});
											parent.jQuery.close('enumerId');
										}else{
											alert("添加失败");
										}
										if(typeof(top.junblockUI)!="undefined"){
											top.junblockUI();
										}
									}
								});
							return false;	
								/*
								if(opener.beforeSubmit() != "false"){
									alert("添加成功");
									$("#" + $this.attr("id")).append('<option value="'+enumerVal+'">'+languageVal+'</option>');
									$li.prev().before($('<li onclick="newOption(this)" selectName="' + $this.attr("id") + '"  val="'+enumerVal+'" >'+languageVal+'</li>'));
								}else{
									return false;
								}
								*/
							}
						}
				　  });
				}else{
					$span.text($li.text());
					if($this.val() != $li.attr('val'))
						$this.val($li.attr('val')).change();
				}
			});
			
			
			$this.change(function(){
				var index = $this[0].selectedIndex,$li = $list.find('li:eq('+index+')');
				$span.text($li.text());
			});

			$(document).click(function(){
				$select.slideUp(0);
			});
		});
		return this;
	};
})(jQuery);

function newOption(obj){
	var $span = $(obj).parent().parent().prev();
	$span.text($(obj).text());
	jQuery(obj).parent().parent().parent().prev().val($(obj).attr('val'))
	/*
	if($(obj).parent().parent().parent().prev().attr("id") == "Emergency"){
		addEmergencyWhy($(obj).parent().parent().parent().prev())
	}
	*/
}

function addSelectDiv(obj){
	jQuery(obj).find("select").each(function(){
			var $html = $('<div class="menu_select"><div class="menu_span"></div><div class="menu_border_bg"><ul class="menu_bg"></ul></div></div>');
			//将下拉框隐藏，把模版插入其后
			//alert($(this).attr("name"));
			var $this = $(this).hide().after($html);
			//声明全局变量
			var $list = $html.find('ul'),$span = $html.find('.menu_span'),$select = $html.find('.menu_border_bg');
			//将option遍历到li中
			$this.find('option').each(function(){
				var $option = $(this);
				$('<li val="'+$option.val()+'">'+$option.text()+'</li>').appendTo($list);
				if($option.prop('selected') === true){
					$this.val($option.val());
					$span.text($option.text());
				}
			});
			$('<span ></span>').appendTo($list);
			$('<li val="new">新增选项...</li>').appendTo($list);
			$span.click(function(event){
				$(".menu_border_bg").slideUp(200);
				//阻止事件冒泡
				event.stopPropagation();
				if(!$list.find('li').size())
					return ;
				$select.css({top:$html.position().top+24+"px"})
				if($select.css("display")=="none"){
					$select.slideToggle(200);
				}else{
					$select.slideUp(200);
				}
			});
			
			$list.find('li').click(function(){
				var $li = $(this);
				if($li.attr("val")=="new"){
					//alert("编辑选项数据");
					var url = "/CRMClientAction.do?operation=6&type=enumer&enumerName=" + $this.attr("enumerName");
					asyncbox.open({
						id:'enumerId',url:url,title:'新增'+$this.prev().text().replace("*","").replace("：",""),width:430,height:140,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
						callback : function(action,opener){
							if(action == "ok"){
								if(typeof(top.jblockUI)!="undefined"){
									top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
								}
								var enumerVal = opener.form.enumerVal.value;
								var languageVal = opener.form.languageVal.value;
								var enumerId = opener.form.enumerId.value; 
								$.ajax({
									type: "POST",
									url: "/CRMClientAction.do",
									data: "operation=1&type=enumer&sort=100&enumerVal="+enumerVal+"&languageVal="+languageVal+"&enumerId="+enumerId,
									success: function(msg){
										if(msg == "success"){
											alert("添加成功");
											//$("select [id='" + $this.attr("id") +"']").append('<option value="'+enumerVal+'">'+languageVal+'</option>');
											jQuery("select[enumerName='"+$this.attr("enumerName")+"']").each(function(){
												$(this).append('<option value="'+enumerVal+'">'+languageVal+'</option>')
												$(this).next().find("ul").find("span").before($('<li onclick="newOption(this)" selectName="' + $this.attr("id") + '"  val="'+enumerVal+'" >'+languageVal+'</li>'));
											});
											parent.jQuery.close('enumerId');
										}else{
											alert("添加失败");
										}
										if(typeof(top.junblockUI)!="undefined"){
											top.junblockUI();
										}
									}
								});
								return false;	
								
							}
						}
				　  });
				}else{
					$span.text($li.text());
					if($this.val() != $li.attr('val'))
						$this.val($li.attr('val')).change();
				}
			});
			
			$this.change(function(){
				var index = $this[0].selectedIndex,$li = $list.find('li:eq('+index+')');
				$span.text($li.text());
			});

			$(document).click(function(){
				$select.slideUp(0);
			});
	});
}



