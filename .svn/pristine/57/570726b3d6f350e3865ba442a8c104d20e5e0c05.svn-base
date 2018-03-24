<script language="javascript" id="faces">
/*$(function(){ 
	$('#face2').qqFace({
		id : 'facebox1', //表情盒子的I
		assign: 'repContent', //给那个控件赋值
		path: '/style1/images/face/'	//表情存放的路径
	}); 	
});
function fillFace(obj){
	$("#faces").css('top',$(obj).offset().top-100);
	$("#faces").css('left',$(obj).offset().left+180);
	//alert($(obj).offset().top-100);
	//alert($(obj).offset().left+180);
}*/

</script>
#foreach($debate in $list)
<li class="debateli" >		          
<a href="#" class="lf person-photo">
	<img src="/ReadFile.jpg&tempFile=false&type=PIC&tableName=tblEmployee&fileName=$globals.get($globals.getEmployeePhoto($debate.userId).split(';'),0)" />
</a>
<div>
	<input id="createById" type="hidden"/>
	<input id="discutId" type="hidden"/>
	<em class="em-name">$!globals.getEmpFullNameByUserId($debate.userId)</em>	
	<div class="d-content">$debate.debateContent</div>
	<p class="p-time" style="float: left;">
	 	#if("$today"=="$debate.createTime.substring(0,10)") 今天 $debate.createTime.substring(10,19) #else $debate.createTime #end
	</p>
	<div class="click-replay" onclick="replayCont(this,'$debate.userId','$debate.id');",>回复</div>
	#set($replyList = "")
	#set($replyList = $!replyMap.get("$debate.id"))
	#if("$!replyList"  != "")
	#foreach($reply in $replyList)
	<div class="c-replay-wrap">
		<a href="#" class="lf person-photo">
		<img src="/ReadFile.jpg&tempFile=false&type=PIC&tableName=tblEmployee&fileName=$globals.get($globals.getEmployeePhoto($globals.get($reply,4)).split(';'),0)" />
		</a>
		<div class="lf">
			<em class="em-name">
				$globals.getEmpFullNameByUserId($globals.get($reply,4)) #if("$!globals.get($reply,4)" !="") <i>回复</i>  $globals.getEmpFullNameByUserId($globals.get($reply,2))#end
			</em>	
			<div class="d-content">$globals.get($reply,1)</div>
			<p class="p-time" style="float: left;">
			 	#if("$today"=="$globals.get($reply,3).substring(0,10)") 今天 $globals.get($reply,3).substring(10,19) #else $globals.get($reply,3) #end
			</p>
			<div class="click-replay" onclick="replayCont(this,'$globals.get($reply,4)','$debate.id');">回复</div>
		</div>
	</div>
	#end
	#end	
</div>
#if("$loginer"=="$debate.userId" || "$loginer"=="$sponsor")
	<b href="javascript:void(0);" class="icons b-del" onclick="deletedebate('$debate.id')"></b>
	 #end
</li>
#end
<div id="discutform" style="display: none">
		<textarea id="repContent" style="border: 1px solid #6ca928;width: 600px;height: 35px;"></textarea>
		<div class="b-operate">		
			<span class="btns-span">
				<input type="button" class="btn-can" onclick="discutSubmit()" value="提交" />
				<input type="button" class="btn-cel" onclick="discutText()" value="取消" />
			</span>
		</div>
	</div>