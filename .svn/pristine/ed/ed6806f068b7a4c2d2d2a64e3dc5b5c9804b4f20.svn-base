<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/date.vjs"></script>
		<script language="javascript" src="/js/function.js"></script>
		
		<script language="javascript">
			function parentId(id){
				this.parent.document.getElementById('childId').value=id;
				this.parent.fillSelected();
			}
			
			
			function deleteFile(id){
				if(confirm('$text.get("oa.common.sureDelete")')){
						document.location = "LabelListAction.do?operation=$globals.getOP("OP_DELETE")&id="+id;
				}
			}
		</script>
		<style type="text/css">
		.text{background:none;padding:0;box-sizing:border-box;border:1px solid #8CBEDC;border-radius:3px;}
		</style>
	</head>
	<body style="overflow:hidden;">
	<form name="form1" id="form1" action="/LabelListAction.do" style="margn:0;padding:0;">
	<input name="operation" id="operation" value="4" type="hidden"/>
	<div align="center" style="padding:5px 0 0 0;">
							关键字查询：<input name="keywordSearch" id="keywordSearch" value="$!LabelSearchForm.keywordSearch" class="text"/>&nbsp;<button name="btn" type="submit" class="button">查询</button>
						</div>
	<div id="oalistRange" style="overflow:hidden;overflow-y:auto;width:99%;float:none;">
					<script type="text/javascript">
						$(function(){
							var iH = parent.parent.jQuery("div.curholder").height();
							$("#oalistRange").css("height",iH-335);
						});
					</script>
						
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="listRange_list_function" name="table" id="tblSort">
							<thead>
								<tr
									style="position:relative;top:expression(this.offsetParent.scrollTop);">
									<td width="30" class="oabbs_tc">
										&nbsp;
									</td>
									
									#foreach($!listtype in $!msg.split(','))
									#if("$!listtype" == "goodsCode")
									<td class="oabbs_tr">
										$text.get("iniGoods.lb.goodsNo")
									</td>
									#elseif("$!listtype" == "goodsFullName")
									<td class="oabbs_tr">
										$text.get("call.lb.goodsName")
									</td>
									#elseif("$!listtype" == "seq")
									<td class="oabbs_tr">
										$text.get("pop.title.sequence1")
									</td>
									#elseif("$!listtype" == "qty")
									<td class="oabbs_tr">
										$text.get("mrp.lb.count")
									</td>
									#elseif("$!listtype" == "trackNo")
									<td class="oabbs_tr">
										$text.get("mrp.lb.trackNo")
									</td>
									#elseif("$!listtype" == "procedures")
									<td class="oabbs_tr">
										工序
									</td>
									#end
									#end
									#foreach($!listtype in $!msg.split(','))
									#if("$!listtype" == "color")
									<td class="oabbs_tc">
										$text.get("Label.color")
									</td>
									<td class="oabbs_tc">
										$text.get("Label.colorName")
									</td>
									#end #end 
									#foreach($!names in $!nameList)
										#if("$!globals.get($!names,0)" != "color")
										<td class="oabbs_tr">
										$!globals.get($!names,1)
										</td>
										#end
									#end
									<td>
										操作
									</td>
								</tr>
							</thead>
							<tbody>
								#set ($Index = 1) #foreach($label in $LabelList)
								<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);"
									onDblClick="parentId('$!label.get('id')')">
									<td>
										$Index
									</td>
									#foreach($!names in $!msg.split(','))
										#if("$!names" == "goodsCode")
										<td>
										<span title="$!label.get('goodsNumber')">$!label.get('goodsNumber')</span>
										</td>
										#elseif("$!names" == "goodsFullName")
										<td>
										<span title="$!label.get('goodsFullName')">$!label.get('goodsFullName')</span>
										</td>
										#elseif("$!names" == "seq")
										<td width="10%">
										<span title="$!label.get('seq')">$!label.get('seq')</span>
										</td>
										#elseif("$!names" == "qty")
										<td align=right>
											#if($!label.get('qty')=='0E-8')
											0
											#else
												$!globals.subStrNum("$label.get('qty')","$!decimalnum")
											#end
										</td>
										#elseif("$!names" == "trackNo")
										<td>
											<span title="$!label.get('trackNo')">$!label.get('trackNo')&nbsp;</span>
										</td>
										#elseif("$!names" == "procedures")
										<td>
											#foreach($row in $globals.getEnumerationItems("Procedures"))
											#if("$!label.get('procedures')"=="$row.value")
												$!row.name
											#end
											#end
										</td>
										#end
									#end
									#foreach($!listtype in $!msg.split(','))
									#if("$!listtype" == "color")
									<td class="oabbs_tc">
										<span title="$!label.get('color')">$!label.get('color')&nbsp;</span>
									</td>
									<td class="oabbs_tc">
										<span title="$!label.get('colorName')">$!label.get('colorName')&nbsp;</span>
									</td>
									#end #end 
									#foreach($!names in $!nameList)
										#if("$!globals.get($!names,0)" != "color")
											#if("$!globals.get($!names,0)" == "Design")
												<td class="oabbs_tr">
												$!label.get('design')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "User1")
												<td class="oabbs_tr">
												$!label.get('user1')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "User2")
												<td class="oabbs_tr">
												$!label.get('user2')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "ColorBit")
												<td class="oabbs_tr">
												$!label.get('colorBit')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "BatchNo")
												<td class="oabbs_tr">
												$!label.get('batchNo')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "Gram")
												<td class="oabbs_tr">
												$!label.get('gram')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "Breadth")
												<td class="oabbs_tr">
												$!label.get('breadth')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "Density")
												<td class="oabbs_tr">
												$!label.get('density')&nbsp;
												</td>
											#elseif("$!globals.get($!names,0)" == "Meter")
												<td class="oabbs_tr">
												#if($!label.get('meter')=='0E-8')
												&nbsp;
												#else
													<script type="text/javascript">document.write($!label.get('meter'))</script>&nbsp;
												#end
												</td>
											#elseif("$!globals.get($!names,0)" == "Coil")
												<td class="oabbs_tr">
												#if($!label.get('coil')=='0E-8')
												&nbsp;
												#else
												$!label.get('coil')&nbsp;
												#end
												</td>
											#end
										#end
									#end
									<td align="center">
										<a href="javascript:deleteFile('$!label.get('id')')">删除</a>
									</td>
								</tr>
								#set ($Index = $Index + 1) #end
							</tbody>
						</table>
					</div>
					<div class="listRange_pagebar">
					$!pageBar
					</div>
	</form>
				</body>
</html>