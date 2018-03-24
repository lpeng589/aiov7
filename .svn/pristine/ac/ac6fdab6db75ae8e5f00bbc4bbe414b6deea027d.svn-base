/*
 * jquery.fm.js
 * Date: 2010-06-02
 * (c) 2009-2010 fumin
 */
(function($){
	$.fn.fmTuopiao = function(options){
		var defaults = {
			img:"images/tuopiao_no.gif",//默认的图片
			imgHover:"images/tuopiao_yes.gif"//鼠标经过时的图片
		}
		var options = $.extend(defaults, options);
		this.each(function(){
			sAttay=options.value.split("/");//把选择项转化成图片形式插入到页面中
			img="<img src="+options.img+" />";
			//text="<span>"+sAttay[0]+"</span>";
			for(i=0;i<sAttay.length-1;i++){
				$(this).append(img);
			}
			var n=0;
			
			//$(this).append(text);
			$(this).children("img").bind('mouseover',function(){//鼠标经过的时候 更换图片 并显示相应的文字
				
				g=$(this).parent().children("img").index($(this))+1;
				$(this).parent().children("img").attr('src',options.img);
				$(this).parent().children("img").slice(0,g).attr('src',options.imgHover);
				$(this).parent().children("span").html(options.value.split("/")[g]);
			});
			$(this).children("img").bind('mouseout',function(){//鼠标离开后 还原成点击前的状态
				$(this).parent().children("img").attr('src',options.img);
				$(this).parent().children("img").slice(0,n).attr('src',options.imgHover);
				$(this).parent().children("span").html(options.value.split("/")[n]);
			});
			$(this).children("img").bind('click',function(){ //鼠标点击 更改选择点的位置
				var g=$(this).parent().children("img").index($(this))+1;
				n=g;
				$(this).parent().children("img").attr('src',options.img);
				$(this).parent().children("img").slice(0,g).attr('src',options.imgHover);
				$(this).parent().children("span").html(options.value.split("/")[g]);
			});
		});
	};
})(jQuery);