<input type="hidden" id="classCode" name="classCode" value="$classCode"/>
<ul class="det_ul">
<li class="det_tli">
	<span class="item_sp sp-1"><input class="cbox" type="checkbox" /></span>
	<span class="item_sp sp-2">商品编号</span>
	<span class="item_sp sp-3">商品名称</span>
	<span class="item_sp sp-2">商品规格</span>
	<span class="item_sp sp-2">商品单位</span>
	<span class="item_sp sp-2">单价</span>
</li>
#foreach($good in $goodsList)
<li class="det_li" ondblclick="dbGoodsSelect(this);">
	<span class="item_sp sp-1"><input class="cbox" type="checkbox" /></span>
	<span class="item_sp sp-2">$!globals.get($good,2)</span>
	<span class="item_sp" style="display: none;" name="classCode">$!globals.get($good,0)</span>
	<span class="item_sp sp-3" name="goodsName">$!globals.get($good,1)</span>
	<span class="item_sp sp-2">$!globals.get($good,3)</span>
	<span class="item_sp" style="display: none;" name="unit">$!globals.get($good,5)</span>
	<span class="item_sp sp-2" name="unitName">$!globals.get($good,6)</span>
	<span class="item_sp sp-2" name="price">$!globals.dealDoubleDigits("$!globals.get($good,4)","amount") </span>
</li>
#end
</ul>
<div class="sg_page">
	<div class="pager">
		$pageBar
	</div>
</div>
