#set($isHead = $!request.getParameter("isHead"))
#set($itemEnter = $!request.getParameter("itemEnter"))

<div class="a-title">
   <span class="lf pr">
   <b class="icons pa"></b>情况描述</span>
</div>
<div class="d-review">
	<textarea class="t-area" id="feedbackContent"></textarea>
</div>
<div class="btn-items-wp">
	<span class="btn-items celBtn" onclick="closeLayer(this);">取消</span>
	#if("$!isHead" == "true")
		<button disabled="disabled"></button>
		#if("$!itemEnter" == "true")
			<button id="feedbackSubmitBtn" class="btn-items conBtn" operation="1" keyid="" onclick="feedbackItemSubmit();">确定</button>
		#else
			<button id="feedbackSubmitBtn" class="btn-items conBtn" operation="1" keyid="" onclick="feedbackSubmit();">确定</button>
		#end
	#else
		<button id="feedbackSubmitBtn" class="btn-items conBtn" operation="1" keyid="" onclick="updateTaskStatus();" >确定</button>
	#end
</div>