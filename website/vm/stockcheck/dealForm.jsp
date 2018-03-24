<!DOCTYPE html>
<html>
  <head>
<meta name="renderer" content="webkit">    <title>My JSP 'dealForm.jsp' starting page</title>
	<script type="text/javascript" src="/js/jquery.js"></script>
  </head>
  
  <body>
    <form action="/UserFunctionQueryAction.do?winCurIndex=6&src=menu" id="form" name="form" method="post">
    	#foreach($map in $list)
    		#foreach ( $key in $map.keySet())
	    		<input type="hidden" id="$key" name="$key" value="$map.get($key)"/>
			#end
    	#end
    </form>
	<script type="text/javascript">
		$(document).ready(function(){
			form.submit();
		});
	</script>
  </body>
</html>
