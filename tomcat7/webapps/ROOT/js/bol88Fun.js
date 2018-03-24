/*
处理ajax返回的文本内容
*/
function doRsText(txt){
	
}
//开通主页
function doIndex(){
	window.open("","","");
}
/**
全页面的屏蔽层
*/
function fullDiv(opacityN,divId,msg){
		if(!opacityN){
			opacityN = 0;
		}
		var w = this.window.document.body.scrollWidth;
		var h = this.window.document.body.scrollHeight;
		if(divId){
			var divObj = document.getElementById(divId);
			divObj.style="width:"+w+";height:"+h+";left:0;top:0;background:white;position:absolute;z-index:10000;filter=Alpha(opacity="+opacityN+")";
		}else{
			var divs = '<div style="width:'+w+';height:'+h;
			divs+=';left: 0;top:0;background:white;position:absolute;';
			divs+=' z-index: 10000; filter=Alpha(opacity='+opacityN+')">'+msg+'</div>';
			this.window.document.body.innerHTML += divs;
		}		
}
