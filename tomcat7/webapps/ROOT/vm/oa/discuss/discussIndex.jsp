<!doctype html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/discuss-common.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/oa/discuss.js"></script>
#if("$!tableName" == "OAWorkLogDiscuss")
<style type="text/css">
.wrap{background:#f7f7f7;width:100%;}
.trendWp{width:auto;}
</style>
#end

<script type="text/javascript">

//拼接
var replyDiv = ' <div class="in-block" id="replyAddDiv"><a class="in-phone" href="#"><img src="$globals.checkEmployeePhoto("48",$!loginBean.id)" />'
replyDiv +='</a><div class="in-dv"><input type="text" class="reply-txt" value="" id="replyContents" /><button type="button" class="reply-submit" onclick="replySubmit()" >提交</button></div></div>'
</script>

</head>

<body>
<input type="hidden" id="f_ref" name="f_ref" value="$!f_ref">
<input type="hidden" id="tableName" name="tableName" value="$!tableName">
<input type="hidden" id="commentId" name="commentId" value="">
<input type="hidden" id="replyId" name="replyId" value="">
<input type="hidden" id="parentIframeName" name="parentIframeName" value="$!parentIframeName">
<div class="wrap">
    <!--  动态trend 分割线Start-->
    <div class="trendWp" style="margin:0;">
        <div class="shareTrend" id="shareTrend">
        	<span class="short-txt-span">请输入内容</span>
        	<div class="long-txt" style="display:none;">
	            <textarea class="Atxt"  id="commentContents" ></textarea>
	            <input class="shareBtn" type="button" value="提交" id="commentSubmitBtn" onclick="commentSubmit(this)" />
            </div>
        </div>
        #foreach($comment in $commentList)
        	#set($commentId = $globals.get($comment,0))
	        <div class="t-block">
	            <a class="t-phone" href="#"><img src="$globals.checkEmployeePhoto("48",$globals.get($comment,3))" /></a>
	            <div class="t-dv">
	            	<a class="t-name" href="#">$!globals.getEmpFullNameByUserId($globals.get($comment,3))</a>
	                <span class="t-content">$globals.get($comment,2)</span> 
	                <a class="a-reply" onclick="addReply(this,'$commentId','$globals.get($comment,3)');">回复</a>
	                #if($loginBean.id == $globals.get($comment,3) && "$!globals.get($comment,7)" !="1")
	               		<a class="a-del discussDel" delType="comment" keyId="$commentId">删除</a>
	                #end
	                <em class="t-time">$globals.get($comment,4)</em>
	                
		            <div class="in-reply-wrap">
		             #set($replyList = "")
		            #set($replyList = $!replyMap.get("$globals.get($comment,0)"))
		            #if("$!replyList" != "")
		            	#foreach($reply in $replyList)
			                <div class="in-block" id="$globals.get($reply,0)">
			                    <a class="in-phone" href="#"><img src="$globals.checkEmployeePhoto("48",$globals.get($reply,2))"/></a>
			                    <div class="in-dv">
			                        <span>
			                            <a class="in-name" href="#">$!globals.getEmpFullNameByUserId($globals.get($reply,2))</a>
			                            #if("$!globals.get($reply,4)" !="") 
			                           	回复<a class="in-name" href="#">$!globals.getEmpFullNameByUserId($globals.get($reply,4))</a>
			                            #end
			                            
			                            :$globals.get($reply,1) 
			                        </span>
			                        <a class="a-reply" onclick="addReply(this,'$commentId','$globals.get($reply,2)');">回复</a>
			                        #if($loginBean.id == $globals.get($reply,2) && "$!globals.get($comment,5)" !="1")
			                       		<a class="a-del discussDel" delType="reply" keyId="$globals.get($reply,0)">删除</a>
			                        #end
			                        <em class="in-time">$globals.get($reply,3)</em>
			                    </div>
			                </div>
		                #end
		            #end
		            </div>
		            
	            </div>
	        </div>
        #end
    </div>
    <!--  动态trend 分割线End-->

</div>
</body>
</html>
