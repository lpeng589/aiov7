#if($listfameTop.size()>0)
<ul class="Block_honor_list">
	#foreach($listTop in $listfameTop)
	<li>
		<div><img id="$globals.get($listTop,4)" src=#if("$!globals.get($listTop,3)"=="") "/style/images/no_head.gif" #else "/ReadFile.jpg?fileName=$globals.urlEncode($globals.get($listTop,3))&tempFile=false&type=PIC&tableName=tblEmployee" #end
				onMouseDown="onfamewish('$globals.get($listTop,4)')" onClick="fameWishdetail('0')"
				title="$text.get('crm.deskTop.Eval'):$!globals.get($listTop,9)
$text.get('crm.deskTop.Applause')：$!globals.get($listTop,10)
$text.get('crm.deskTop.Blood.Arouse')：$!globals.get($listTop,11)
$text.get('crm.deskTop.Defiant')：$!globals.get($listTop,12)"/></div>
		<div>$!globals.get($listTop,1)</div>
		<div>$!globals.get($listTop,2)</div>
		<span>
			<a href="javascript:void(0)" onClick="detail('$globals.get($listTop,0)','1','$globals.get($listTop,4)')" title="$text.get('crm.deskTop.Applause')"><img src="/$globals.getStylePath()/images/desk/ti_08.gif" /></a>
			<a href="javascript:void(0)" onClick="detail('$globals.get($listTop,0)','2','$globals.get($listTop,4)')" title="$text.get('crm.deskTop.Blood.Arouse')" ><img src="/$globals.getStylePath()/images/desk/ti_09.gif" /></a>
			<a href="javascript:void(0)" onClick="detail('$globals.get($listTop,0)','3','$globals.get($listTop,4)')" title="$text.get('crm.deskTop.Defiant')" ><img src="/$globals.getStylePath()/images/desk/ti_10.gif" /></a>
		</span>
	</li>
	#end
</ul>
#end