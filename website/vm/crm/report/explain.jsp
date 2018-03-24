<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style type="text/css">
body {font-size: 13px;}
</style>
</head>
<body>
	#set($isCompare=$request.getParameter("isCompare"))
	说明：<p>
	#if("$!isCompare" == "")
		统计报表<p>
		可将客户进行分类汇总，使之系统化和条理化，并以集中、简明的方式反应客户的总体情况<p>
		动态反应客户的关系曲线，以便及时调整管理方式和管理手段，促进客户关系向良性循环的轨道发展<p>
	#else
		<span>
			同比环比报表<p>
			环比和同比主要用于趋势分析<p>
			同比：与历史同期比较即本期和上期的某个时间段比较<p>
			例如：2005年7月份与2004年7月份相比较或本季度的第一个月和上季度的第一个月比较<p>
			同比增长速度=（本期发展水平 - 去年同期水平）/去年同期水平*100%<p>
			环比：与上一统计段比较<p>
			例如：2005年7月份与2005年6月份相比较或2008年7月第二周和7月第一周比较<p>
			环比增长速度=（本期数 - 上期数）/上期数*100%<p>
			比平均增长速度=（本期数 - 平均数）/平均数*100%<p>
		</span>
	#end
</body>
</html>
