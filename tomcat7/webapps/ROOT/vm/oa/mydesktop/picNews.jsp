#if($newsList.size()>0)
<div id="YSlide">
	#foreach($new in $newsList)
	<p class="YSample">
		#if($!new.get("read")=="no")<i class="bg_i"></i>#end
		<a onclick="javascript:mdiwin('$!new.get("url")','图片新闻',this)"><img src="/ReadFile.jpg?type=PIC&tempFile=path&path=/news/&fileName=$!new.get('pic')"/></a>
		<strong>$!new.get("title")</strong>
	</p>
	#end
	<p id="jSIndex">#foreach($new in $newsList)<a href="#1" class="current">$velocityCount</a>#end</p>
</div>
#end