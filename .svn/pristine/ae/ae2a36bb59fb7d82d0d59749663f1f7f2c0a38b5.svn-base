#if($pattern.size() != 0)
<div class="wl-pf">
	<textarea id="comments" class="txt-area" placeholder="评语..." >$!globals.get($!existSroce,0)</textarea>
	<div class="pf-bm">
		<span class="left-span">		
			<select class="slt" id="sroces">		
			#if("$!ENorCN" == "EN")
				#foreach($log in $setEN.split(";"))
				<option value="$log" #if("$!globals.get($!existSroce,1)" == "${log}.0")selected="selected" #end>$log</option>
				#end
			#else
				#foreach($log in $setCN.split(";"))
				<option value="$setMaps.get($log)" #if("$!globals.get($!existSroce,1)" == "${setMaps.get($log)}.0")selected="selected" #end>$log</option>
				#end
			#end
			</select>
		</span>
		<select class="lf-slt" id="sroceType">
			<option value="-1">类别</option>	
			#foreach($type in $!pattern)
			<option value="$!globals.get($!type,2);$!globals.get($!type,3)" #if("$!globals.get($!existSroce,2)" == "$!globals.get($!type,2)")selected="selected" #end>$!globals.get($!type,2)</option>
			#end
		</select>
		<b class="con-btn" workId="$workLogId" createPlanDate="$createPlanDate" sroceManId="$sroceManId" pingfenId="$!globals.get($!existSroce,3)">确定</b>
	</div>
</div>
#end