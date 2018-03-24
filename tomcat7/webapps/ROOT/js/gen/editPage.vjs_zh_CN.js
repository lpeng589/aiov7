



function changeConfirm(){
 var allinput = document.getElementsByTagName("input");
 for(i=0;i<allinput.length ;i++){
 if(allinput[i].type=="text"){
 $(allinput[i]).attr("_oldval",allinput[i].value);
 }else if(allinput[i].type=="password"){
 $(allinput[i]).attr("_oldval",allinput[i].value);
 }else if(allinput[i].type=="checkbox"){
 var _v = allinput[i] ? 'on' : 'off'; 
 $(allinput[i]).attr("_oldval",_v); 
 }
 }
 var alltextarea = document.getElementsByTagName("textarea");
 for(i=0;i<alltextarea.length;i++){ 
 $(alltextarea[i]).attr("_oldval",alltextarea[i].value);
 }
 var allselect = document.getElementsByTagName("select");
 for(i=0;i<allselect.length;i++){ 
 $(allselect[i]).attr("_oldval",allselect[i].value); 
 } 
}



window.onbeforeunload = function() { 
 if(window.save) return; 
 if(typeof(thebeforeunload) == "function"){
 thebeforeunload();
 }
 if(is_form_changed()) { 
 return "您有未保存的数据，是否确定放弃保存并离开"; 
 } 
}

