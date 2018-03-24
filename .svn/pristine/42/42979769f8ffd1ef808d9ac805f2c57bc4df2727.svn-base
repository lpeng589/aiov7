<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="javascript">
	if(typeof(top.jblockUI)!="undefined")top.junblockUI();
	
<!--
function enableForm(docu){
	bt =docu.getElementsByTagName("button");
	for(var i=0;bt != null && i<bt.length;i++){
		if(bt[i].disabled)
			bt[i].disabled=false;
	}
	bt =docu.getElementsByTagName("submit");
	for(var i=0;bt != null && i<bt.length;i++){
		if(bt[i].disabled)
			bt[i].disabled=false;
	}
	window.save=false;
}

function setToken(token){
	var items = this.parent.document.getElementsByName("org.apache.struts.taglib.html.TOKEN");
	if(items[0]){
		items[0].value = token;
//		alert("token reset OK!")
        setReset();
	}
	else{
		alert("reset token failed !")
	}
}

function resetForm(){
//    alert(0)
    var tempToken = this.form.elements("org.apache.struts.taglib.html.TOKEN").value;
//	alert(1)
	this.form.reset();
//	alert(2)
//	alert(this.form.elements("org.apache.struts.taglib.html.TOKEN").value)
	this.form.elements("org.apache.struts.taglib.html.TOKEN").value = tempToken;
//	alert(this.form.elements("org.apache.struts.taglib.html.TOKEN").value)
	return false;
};

function setReset(){
	if(this.parent){
		if(this.parent.document.forms[0]){
			res = this.parent.document.getElementsByTagName("reset");
			for(var i=0;res != null &&i<res.length;i++){
				res[i].onclick=resetForm;//function(){resetForm();return false};
			}
		}
		else{
			//alert("setParentEnable() Error! No form found !")
		}
	}
	else{
		//alert("setParentEnable() Error! No Top window found !")
	}
}

function setParentEnable(){
	if(this.parent){
		if(this.parent.document.forms[0]){
			enableForm(this.parent.document)
		}
		else{
			//alert("setParentEnable() Error! No form found !")
		}
	}
	else{
		//alert("setParentEnable() Error! No Top window found !")
	}
}

function goResultPage(){
	if(typeof(this.parent.document)!="undefined" && this.parent.document!=null){
		enableForm(this.parent.document) ;
	}
	#if("$!print"=="true"&&"$!saveType"=="printSave")
		this.parent.prints("$!BillId","$!BillRepNumber") ;
	#elseif($!directJump)
		#if($!prints)
			$BACK_URL
		#elseif($!noback)
			parent.deliverTo("$BACK_URL","$retValUrl","true");
		#else
			parent.deliverTo("$BACK_URL","$retValUrl");
		#end 		
	#elseif($!loadFresh == "true")
		$BACK_URL;
	#else
		#if("$!print"=="true")
			this.parent.printData("$!BillId","$!BillRepNumber") ;
		#else
			this.parent.location.href='/common/message.jsp?popWinName=$!popWinName';
		#end
	#end
}
function goAction(vars){
 	this.parent.location.href=vars;
}

function dealWithSubmit(){
	if(typeof(parent.parent.dealAsyncbox)!="undefined"){
		parent.parent.dealAsyncbox();		
	}else{
		parent.parent.frames["topFrame"].dealAsyncbox();
		parent.parent.jQuery.close('dealdiv');
	}
}
function dealCheck(checkBackUrl){
	parent.parent.dealCheck(checkBackUrl);
}
-->
</script>
</head>
<body>
</body>
</html>
<script language="javascript">
<!--


if(parent.document.formFrame){
	#if("$!MESSAGE_RELOGIN"=="true")
		window.location.href='/common/message.jsp?popWinName=$!popWinName';
	#else
		$!MESSAGE_HTML
		#if("$!firstFocus"!="")
			var firstFocus = window.parent.document.getElementById("$!firstFocus") ;
			firstFocus.value="" ;
			firstFocus.focus() ;
		#end	
	#end
}else{
	window.location.href='/common/message.jsp?popWinName=$!popWinName';
}
-->
</script>
