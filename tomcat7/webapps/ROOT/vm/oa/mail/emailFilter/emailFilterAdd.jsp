<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("emailFilter.title.add")</title>
		<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css">
		<link type="text/css" rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 
		<script type="text/javascript" src="/js/function.js"></script>
		<script type="text/javascript" src="/js/validate.vjs"></script>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
		<script type="text/javascript">
		putValidateItem("refOaMailinfoSettingId","$text.get("EmailFilter.mailAddress")","any","",false,2,70);
		putValidateItem("refOaFolderId","$text.get("EmailFilter.folderName")","any","",false,1,99);
		putValidateItem("filterCondition","$text.get("EmailFilter.filterCondition")","any","",false,1,3000);
		
		function changeForder(){
			var id = document.getElementById('emailIdOfAdd').value;
			jQuery("#folderSelectIdOfAdd").empty();
			if(id) {
				jQuery.get("/EmailFilterAction.do?operation=19&account="+id,{account:''+id}, function(data){ 
					//var o1 = "<option value='$text.get('oa.mail.receive.box')'>$text.get('oa.mail.receive.box')</option>";
					//var o2 = "<option value='$text.get('oa.mail.draft')'>$text.get('oa.mail.draft')</option>";
					//var o3 = "<option value=' $text.get("oa.mail.draft")$text.get("oa.mail.box")'> $text.get("oa.mail.draft")$text.get("oa.mail.box")</option>";
					//var o4 = "<option value='$text.get("oa.mail.outBox")$text.get("oa.mail.box")'>$text.get("oa.mail.outBox")$text.get("oa.mail.box")</option>";
					var o5 = "<option value='5'>$text.get('oa.mail.deleted')</option>";
					var o6 = "<option value='4'>$text.get('oa.mail.dust')</option>";
					var html = o5+o6+data; 
					jQuery("#folderSelectIdOfAdd").empty();
					jQuery("#folderSelectIdOfAdd").append(html);
					
				})
			}
		}

		
		function beforSubmit(form){   
			if(!validate(form))return false;
			disableForm(form);
			return true;
		}
</script>
<style type="text/css">
.listrange_jag li{width:100%;}
.listrange_jag li>span{width:80px;display:inline-block;text-align:right;}
</style>
	</head>

	<body onLoad="showStatus();">

		<iframe name="formFrame" style="display:none"></iframe>
		<form method="post" scope="request" name="form"
			action="/EmailFilterAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
			<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
			<input type="hidden" name="winCurIndex" value="$!winCurIndex">
			<input type="hidden" name="org.apache.struts.taglib.html.TOKEN"
				value="$!globals.getToken()">
			<input type="hidden" name="createBy" value="$result.createBy">
			<input type="hidden" name="id" value="$result.id">
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("emailFilter.title.add")
				</div>
				<ul class="HeadingButton">
				</ul>
			</div>
			<div id="listRange_id" style="text-align: center;margin-left: auto;margin: auto;" >
			  <script type="text/javascript">
					var oDiv=document.getElementById("listRange_id");
					var sHeight=document.body.clientHeight-38;
					oDiv.style.height=sHeight+"px";
			  </script>
	<div class="listRange_div_jag">
					<div class="listRange_div_jag_div"></div>
					<ul class="listRange_jag" style="margin-top:10px;background:#f3f1f2;padding:20px 0 20px 0;">
						<li >
							<span>$text.get("EmailFilter.mailAddress")：</span>
							<select name="refOaMailinfoSettingId" id = "emailIdOfAdd" onchange="changeForder()" style="width: 230px;">
								<option value = "">-$text.get('please.choice')-</option>
								#foreach($emRow in $emailList)
								<option title="$!globals.get($emRow,2)"
									value="$!globals.get($emRow,0)">
									$!globals.get($emRow,2)
								</option>
								#end
							</select><font color="#FF0000">* </font>
						</li>
						<li>
							<span>$text.get("EmailFilter.folderName")：</span>
							<select name="refOaFolderId" id = "folderSelectIdOfAdd"  style="width: 230px;"></select>
							<font color="#FF0000">*</font>
						</li>
						<li>
							<span >$text.get("EmailFilter.filterCondition")：</span>
							<textarea rows="6" style="width: 230px;" name="filterCondition" ></textarea>
							<font  color="#FF0000">*</font>
						</li>
						<li style="color: red;margin-left: 40px;">$text.get('filterCondition.desc')</li>
						<li style="margin-top:10px;margin-left:40px; float:left; ">
							<center>
								<button type="submit" class="b2">
									$text.get("common.lb.add")
								</button>
								<button type="button"
									onClick="location.href='/EmailFilterQueryAction.do?operation=$globals.getOP("OP_QUERY")'" class="b2">
									$text.get("common.lb.back")
								</button>
							</center>
						</li>
					</ul>
					<!-- 
					<div>
						<span> ($text.get("common.title.mustInput") </span>
						<img src="/$globals.getStylePath()/images/biaozianniu.gif"
							width="15" height="15">
						)
					</div>
					 -->
				</div>
			</div>
		</form>
	</body>
</html>
