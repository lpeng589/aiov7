<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择模板字段</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<style type='text/css'>
body,div,form,p,input,button,h1,h2,h3,h4,h5,h6,span,img,font,ul,li,ol,a,table,thead,tboady,tr,td{margin:0;padding:0;}
body{font-size:12px;font-family:Microsoft Yahei;}
li{list-style:none;}
img{border:0;}
#mybody{padding:0 10px;}
#mybody>li{float:left;display:inline-block;margin:0 5px 5px 0;padding:0 0 0 20px;position:relative;}
#mybody>li .r-box{position:absolute;left:2px;top:2px;width:15px;height:15px;}
#mybody>li label{cursor:pointer;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function fillFileName(datas){	
	parent.fillData(datas);
}
function filedSubmit(){
	var datas="";
	$("#mybody",document).each(function(){
		$("li",this).each(function(){
			if($("input",this).is(":checked")){
				datas = $("#filedName",this).val();
			}
		})		
	});
	return datas;
}

</script>
</head>
<body >
<div align="center">         
 	<p>字段名称</p>	
	<ul id="mybody">
	#foreach($filedName in $viewBean.pageFields.split(","))
		#if($filedName.indexOf("contact")==-1)
			#set($fieldBean = $globals.getFieldBean($tableName,$filedName))
			<li ondblclick="fillFileName('$fieldBean.display.get("$globals.getLocale()")');">				
				<label>
					<input class="r-box" id="filedName" type="radio" name="filedName" value="$fieldBean.display.get("$globals.getLocale()")"/>
					 $fieldBean.display.get("$globals.getLocale()")
				</label>
			</li>
		#else
			#set($fieldBean = $globals.getFieldBean($contectTable,$filedName.replace('contact','')))
				<li ondblclick="fillFileName('$fieldBean.display.get("$globals.getLocale()")');">				
					<label>
						<input class="r-box" id="filedName" type="radio" name="filedName" value="$fieldBean.display.get("$globals.getLocale()")"/>
						$fieldBean.display.get("$globals.getLocale()")
					</label>
				</li>
		#end
	#end
			<li ondblclick="fillFileName('目的地');">				
						<label>
							<input class="r-box" id="filedName" type="radio" name="filedName" value="目的地" />
							目的地
						</label>
			</li>
			<li ondblclick="fillFileName('省份');">				
						<label>
							<input class="r-box" id="filedName" type="radio" name="filedName" value="省份" />
							省份
						</label>
			</li>
			<li ondblclick="fillFileName('城市');">				
						<label>
							<input class="r-box" id="filedName" type="radio" name="filedName" value="城市" />
							城市
						</label>
			</li>
			<li ondblclick="fillFileName('区、县');">				
						<label>
							<input class="r-box" id="filedName" type="radio" name="filedName" value="区、县" />
							区、县
						</label>
			</li>
	</ul>
</div>        
</body>
</html>
