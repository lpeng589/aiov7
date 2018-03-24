function enableselectBox(){
	$(".CRselectBox").unbind("hover");
	$(".CRselectBox").hover(function(){
		$(this).addClass("CRselectBoxHover");
	},function(){
		$(this).removeClass("CRselectBoxHover");
	});
	$(".CRselectValue").unbind("click");
	$(".CRselectValue").click(function(){
		$(".CRselectBoxOptions").hide();
		$(this).blur();
		
		$(this).parent().find("ul").each(function(i) {
			$(this).show();
			//alert($(this).css("left"));
			//alert(event.mouseX);
			//$(this).css("left",$(this).parent(".CRselectBox").css("left"));
		});
		//$(".CRselectBoxOptions").show();
		return false;
	});
	$(".CRselectBoxItem").unbind("click");
	$(".CRselectBoxItem").click(function(){
		$(this).blur();
		var value = $(this).attr("rel");
		var txt = $(this).text();
		
		var emname = $(this).parent().parent(".CRselectBox").attr("emname");
		$("#"+emname).val(value);
		$("#"+emname+"_CRtext").val(txt);		
		sv = $(this).parent().parent(".CRselectBox").find(".CRselectValue");
		if(sv.attr("aioselectDown") != "true"){
			sv.text(txt);
		}
		$(".CRselectBoxItem").removeClass("selected");
		$(this).parent().addClass("selected");
		$(".CRselectBoxOptions").hide();
		
		var emchange = $(this).parent().parent(".CRselectBox").attr("onchange");
		eval(emchange);
		return false;
	});
	/*点击任何地方关闭层*/
	$(document).click(function(event){
		if( $(event.target).attr("class") != "CRselectBox" ||  $(event.target).attr("emname") != curemname){
			$(".CRselectBoxOptions").hide();
		}
	}); 	
	$(this).bind("mouseout", function(){
		if( $(event.target).attr("class") != "CRselectBox" ||  $(event.target).attr("emname") != curemname){
			//$(".CRselectBoxOptions").hide();
		}
	});
	if(typeof($.fn.bgiframe) != 'undefined'){
		$('.CRselectBoxOptions').bgiframe();   
	}
};

function aioselect(name,data,value,width,changeEvent){
	datastr ="";
	valueName="";
	for(i=0;i<data.length;i++){
		if(data[i].value == value){
			datastr +='	<a class="CRselectBoxItem" href="#" style="width:'+width+';text-align:left" rel="'+data[i].value+'">'+data[i].name+'</a>';
			if(data[i].name.length<6){
			valueName = data[i].name;
			}else{
			valueName = data[i].name.substring(0,5)+'...';
			}
		}else {
			datastr +='	<a class="CRselectBoxItem" href="#"  style="width:'+width+';text-align:left" rel="'+data[i].value+'">'+data[i].name+'</a>';
		}
	}
	if(valueName==""){
		value = data[0].value;
		valueName = data[0].name;
	}
	
	chage="";
	if(changeEvent != ""){
		chage = 'onchange="'+changeEvent+'"';
	}
	
	str ='<div class="CRselectBox"   emname="'+name+'" '+chage+'>'+
	'<input type="hidden" value="'+value+'"  name="'+name+'" id="'+name+'"/>'+
	'<input type="hidden" value="'+valueName+'"  name="'+name+'_CRtext" id="'+name+'_CRtext"/>'+
	'<a class="CRselectValue" id="'+name+'_CRDis" href="#">'+valueName+'</a>'+
	'<ul class="CRselectBoxOptions"> '+datastr+'</ul>'+
	'</div>';
	document.write(str);
	enableselectBox();
}

function aioselectDown(name,data,value,valueName,width,changeEvent){
	datastr ="";
	for(i=0;i<data.length;i++){
			var click = "selectChange('" + changeEvent +"')";
			datastr +='	<li class="CRselectBoxItem" rel="'+data[i].value+'"><a href="#" style="width:'+width+'" onclick="' + click +'">'+data[i].name+'</a></li>';			
	}
		
	chage="";
	if(changeEvent != ""){
		chage = 'onchange="'+changeEvent+'"';
	}
	
	str ='<div class="CRselectBox"   emname="'+name+'" '+chage+'>'+
	'<input type="hidden" value="'+value+'"  name="'+name+'" id="'+name+'"/>'+
	'<input type="hidden" value="'+valueName+'"  name="'+name+'_CRtext" id="'+name+'_CRtext"/>'+
	'<a class="CRselectValue" id="'+name+'_CRDis" aioselectDown=true  href="#"><div style="margin-top: 2px;"></div><font style="font-size: 12px;">'+valueName+'</font></a>'+
	'<ul class="CRselectBoxOptions"> '+datastr+'</ul>'+
	'</div>';
	document.write(str);
	enableselectBox();
}


function aioselectMail(name,data,value,width,changeEvent){
	datastr ="";
	valueName="";
	for(i=0;i<data.length;i++){
		if(data[i].value == value){
			var click = "selectChange('" + changeEvent +"')";
			datastr +='	<a class="CRselectBoxItem" href="#" style="width:'+width+';text-align:left" rel="'+data[i].value+'" onclick="' + click +'">'+data[i].name+'</a>';
			if(data[i].name.length<6){
			valueName = data[i].name;
			}else{
			valueName = data[i].name.substring(0,5)+'...';
			}
		}else {
			var click = "selectChange('" + changeEvent +"')";
			datastr +='	<a class="CRselectBoxItem" href="#"  style="width:'+width+';text-align:left" rel="'+data[i].value+'" onclick="' + click +'">'+data[i].name+'</a>';
		}
	}
	if(valueName==""){
		value = data[0].value;
		valueName = data[0].name;
	}
	
	chage="";
	if(changeEvent != ""){
		chage = 'onchange="'+changeEvent+'"';
	}
	
	str ='<div class="CRselectBox"   emname="'+name+'" '+chage+'>'+
	'<input type="hidden" value="'+value+'"  name="'+name+'" id="'+name+'"/>'+
	'<input type="hidden" value="'+valueName+'"  name="'+name+'_CRtext" id="'+name+'_CRtext"/>'+
	'<a class="CRselectValue" id="'+name+'_CRDis" href="#">'+valueName+'</a>'+
	'<ul class="CRselectBoxOptions"> '+datastr+'</ul>'+
	'</div>';
	document.write(str);
	enableselectBox();
}
