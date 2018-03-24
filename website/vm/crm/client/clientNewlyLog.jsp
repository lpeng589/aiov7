<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/history.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
.cf li{float: left;text-align: center;}
.histroyUl li{text-align: left;}
.changeInfo{color: gray;}
a{color:#006699;}
a:link{color:#006699;}
a:visited{color:#006699;}
a:hover{color:#006699;}

</style>

</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<div class="box-163css">
<div class="boxbg subbox_w660" style="width: 785px;">
<div class="subbox cf">
  <div class="bd">
      <div class="subitem_hy">
        <ul class="histroyUl">
          <li style="width: 50px;">序号</li>
          <li style="width: 150px;" class="contents">操作人</li>
          <li style="width: 200px;" class="contents">操作时间</li>
          <li style="width: 300px;">内容</li>
        </ul>
        
        #foreach($log in $logList)
        <ul class="histroyUl">
          <li style="width: 50px;">$velocityCount</li>
          <li style="width: 150px;" class="contents">$globals.getEmpFullNameByUserId($globals.get($log,4))</li>
          <li style="width: 200px;" class="contents">$globals.get($log,3)</li>
          <li style="width: 300px;">$globals.get($log,1)</li>
        </ul>
        #end
      </div>
  </div>
</div>
</div>
</div>
</body>
</html>
