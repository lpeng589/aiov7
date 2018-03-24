var $ = function(id) {

	return document.getElementById(id);

};
var userAgent = window.navigator.userAgent.toLowerCase();
var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);
function MouseOverBtn()
{
   if(event.srcElement.className.indexOf("Hover") < 0)
      event.srcElement.className+="Hover";
}
function MouseOutBtn()
{
   if(event.srcElement.className.indexOf("Hover") > 0)
      event.srcElement.className=event.srcElement.className.substr(0,event.srcElement.className.indexOf("Hover"));
}
function CorrectButton()
{
	
   var inputs=document.getElementsByTagName("INPUT");
   for(var i=0; i<inputs.length; i++)
   {
      var el = inputs[i];
      var elType = el.type.toLowerCase();
      var elClass = el.className.toLowerCase();
      var elLength = Math.ceil(el.value.replace(/[^\x00-\xff]/g,"**").length/2);
      if(elType!="button" && elType!="submit" && elType!="reset" || elClass!="bigbutton"&&elClass!="smallbutton"&&elClass!="toolbutton")
         continue;
      
      if(elLength<=3)
         el.className+="A";
      else if(elLength==4)
         el.className+="B";
      else if(elLength>=5 && elLength<=7)
         el.className+="C";
      else if(elLength>=8 && elLength<=11)
         el.className+="D";
      else
         el.className+="E";
      
      if(is_ie)
      {
         el.attachEvent("onmouseover", MouseOverBtn);
         el.attachEvent("onmouseout",  MouseOutBtn);
      }
   }
}

function CorrectClose()
{
	
   //�Ƿ�2010��
   if(!window.top || !window.top.shortcutArray)
      return;
   
   var inputs=document.getElementsByTagName("INPUT");
   for(var i=0; i<inputs.length; i++)
   {
      var input = inputs[i];
      if(input.type.toLowerCase() != "button")
         continue;
      
      var onclick = input.attributes['onclick'];
      if(onclick==null || typeof(onclick) != 'object' || typeof(onclick.nodeValue) != "string" || onclick.nodeValue.toLowerCase().indexOf("window.close") < 0)
         continue;
      
      input.onclick = function(){window.top.closeTab();};
   }
}

if(is_ie)
{
	alert(22);
   window.attachEvent("onload", CorrectButton);
   window.attachEvent("onload", CorrectClose);
}
else
{
   window.addEventListener("load", CorrectButton,false);
   window.addEventListener("load", CorrectClose,false);
}