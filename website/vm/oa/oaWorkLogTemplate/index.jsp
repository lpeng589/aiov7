<b class="arrow-point"></b>
<span class="btn-add" onclick="addWorkLogTemplate()">新增模板</span>
<ul class="fit-ul">
#foreach($template in $templateBeanList)
<li  class="fit-li" id="$template.id">$template.name 
	<i class="#if("$!template.statusId" == "0") templateStatus #else unTemplateStatus #end">
	#if("$!template.statusId" == "0")
		启用
	#else
		不启用
	#end
	</i>
	<a class="updateTemplate" >编辑</a>
	<a class="delTemplate" >删除</a>
</li>
#end
</ul>


