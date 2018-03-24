<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<style>
li {
	cursor:pointer;
	background-color:white;
	border: 1px solid #ff8040;
	float:left;
	width:75px;
	margin-left:5px;
	margin-top:7px;
	line-height:20px;
	text-align:center;
	position:relative;
}

.delImg{
	width:10px;
	height:10px;
	float:right;
	display:none;
	position:absolute;
	bottom:8px;
	right:2px;
	z-index:9;
}
.img_op{
	float:right; 
	width:12px;
	margin-top:2px;
	margin-left:8px;
	height:12px;
	cursor: pointer;
}
</style>
<script>
$(document).ready(function(){	
	$("li").live("hover",function(){
		if($(this).attr("id") != "userAdd"){
		  $(this).children("img").toggle();
		}
	})
	$(".delImg").live("click",function(){
		var userId=$(this).attr("name")+",";
		var newUser=jQuery("#allUser").val().replace(userId,'');
		jQuery("#allUser").val(newUser);
		$(this).parent().remove();
	});
});

function addUser(fieldName,fieldNameIds,flag){
	var urls = "/Accredit.do?popname=userGroup&inputType=checkbox&condition=openFlagNoAdmin";
	var titles = "请选择用户";
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	asyncbox.open({
	id : 'Popdiv',
	title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				addLi(employees);
　　　　　	}
　　　	}
　	});
}
function clearAll(){
	if (confirm("您确定要撤销该角色的所有用户?")) {
			jQuery("#allUser").val("");
			document.myform.submit();
	}
}

function fillData(datas){	
	addLi(datas);
	parent.jQuery.close('Popdiv');
}
function addLi(str){
	if(str == "") return; 
    var allUser=jQuery("#allUser").val(); //当前拥有该角色的用户
	var empUsers = str.split("|") ; //弹出框选择的用户

	
	allUser=","+allUser
	for(var i=0;i<empUsers.length;i++){
		var user = empUsers[i].split(";") ;
		var userId=","+user[0]+",";
		if(user!="" && allUser.indexOf(userId)<=-1 && user[0] != "1"){
			jQuery("#allUser").val(jQuery("#allUser").val()+user[0]+",");
			var newUser="<li>"+user[1]+"<img class='delImg' name='"+user[0]+"' src='/style/images/del.gif' alt='撤销用户'/></li>"
			jQuery(newUser).insertBefore("#userAdd");
		}
	}
}

function addSingleLi(){
	var newLi=jQuery("#dbData").val();
	if(newLi!=""){
		addLi(newLi);
	}
}



</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" name="myform" action="/RoleAction.do?operation=2&updateType=updateSingleRole" target="formFrame">
	<input type="hidden" name="roleId" value="$!roleId" id="roleId"/>
	<input type="hidden" name="oldUser" value="$!allUser" id="oldUser"/>
	<input type="hidden" name="allUser" value="$!allUser" id="allUser"/> <!-- 拥有该角色的用户 -->
	<input type="hidden" name="dbData" id="dbData" value="" onpropertychange="addSingleLi();" />
	
	<ul id="showUser" style="margin-top:5px; clear:both;padding:2px; "> 
		$msg		
		<li id="userAdd" style="background-color:#FFFFFF;padding:0px;float:left;border:none;">
		<img class="img_op"  style="float:left;padding:5px 0 0 10px;width:16px;height:16px;" src="/style/themes/icons/edit_add.png" onClick="addUser('empUser','addUser','1');"  title="添加用户"/>
		</li>
	</ul>
</form>
</body>
</html>