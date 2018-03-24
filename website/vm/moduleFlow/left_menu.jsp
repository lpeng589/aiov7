<ul id="nav">
	<li><a href="#Menu=ChildMenu"  onclick="DoMenu('ChildMenu')">$text.get("common.lb.myoften")</a>
	<ul id="ChildMenu" >
#foreach ($row in $result )	
#if("$!globals.get($row,2)" != "" && "$!globals.get($row,5)" == "")	
<li ><a href="javascript:leftmdiwin('$globals.get($row,2)','$globals.get($row,1)')" ><span ><img src="$globals.get($row,3)" /></span>$globals.get($row,1)</a></li>
#end
#end
	</ul>
	</li> 
	
	#foreach ($cl in $class )	
	<li><a href="#Menu=ChildMenu$globals.get($cl,0)"  onclick="DoMenu('ChildMenu$globals.get($cl,0)')">$globals.get($cl,1)</a>
	<ul id="ChildMenu$globals.get($cl,0)" class="collapsed">
#foreach ($row in $result )	
#if("$!globals.get($row,2)" != ""  && "$!globals.get($row,5)" == "$globals.get($cl,0)")	
<li ><a href="javascript:leftmdiwin('$globals.get($row,2)','$globals.get($row,1)')" ><span ><img src="$globals.get($row,3)" /></span>$globals.get($row,1)</a></li>
#end
#end
	</ul>
	</li> 
	#end
</ul>	