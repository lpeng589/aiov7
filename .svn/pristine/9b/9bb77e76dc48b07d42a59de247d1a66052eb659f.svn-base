<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/style/css/reg.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript">

#if("$encryptionType" == "3" || "updateFunOffline"=="$!utype")
putValidateItem("certFile","证书文件","any","",false,5,50);
#else
putValidateItem("companyName","$text.get("upgrade.lb.compName")","any","",false,5,50);
putValidateItem("offlineNo","$text.get("upgrade.lb.companyAddress")","any","",false,1,100);
#end

function beforSubmit(form){
	if(!validate(form))return false;	
	disableForm(form);
	document.getElementById("titleDiv").innerHTML = "$text.get("upgrade.msg.busy")"; 
	return true;
}	
function applayPcNo(dogId,pcNo){
	　asyncbox.prompt('申请更新软件特殊码','请输入电话号码，方便和您联系','','text',function(action,val){
	　　　//prompt 返回三个 action 值，分别是 'ok' 、'cancel' 和 'close'。
	　　　if(action == 'ok'){
	　　　　　if(val == ""){
				alert("电话号码不能为空");
				return;
			}
			window.location.href
			='/RegisterAction.do?regFlag=2&from=offline&encryptionType=3&applayPcNo=true&dogId='+dogId+'&pcNo='+pcNo+'&tel='+val;
	　　　}
	　});
}
function submitUpgrade(){
	document.form.updateDog.value="upload";   
	if(form.certFile.value== ""){
		alert("证书不能为空");
		return false;
	}
	if(form.certFile.value.indexOf(".cert") <= 0){
		alert("文件类型不正确!");
		return false;
	}
	disableForm(form);
	document.getElementById("titleDiv").innerHTML = "$text.get("upgrade.msg.busy")"; 
     document.form.submit();
}
function submitUpgradeOnline(){  
	document.form.updateDog.value="true";   
	disableForm(form);
	document.getElementById("titleDiv").innerHTML = "$text.get("upgrade.msg.busy")"; 
     document.form.submit();
}
function copyClb(txt){
	if (window.clipboardData) {   
        window.clipboardData.clearData();  
        window.clipboardData.setData("Text", txt);  
        alert("复制成功！")  
    } else if (navigator.userAgent.indexOf("Opera") != -1) { 
        alert("复制成功！");  
    } else if (window.netscape) {  
        try {  
            netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");  
        } catch (e) {  
            alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将 'signed.applets.codebase_principal_support'设置为'true'");  
        }  
        var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);  
        if (!clip)  
            return;  
        var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);  
        if (!trans)  
            return;  
        trans.addDataFlavor('text/unicode');  
        var str = new Object();  
        var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);  
        var copytext = txt;  
        str.data = copytext;  
        trans.setTransferData("text/unicode", str, copytext.length * 2);  
        var clipid = Components.interfaces.nsIClipboard;  
        if (!clip)  
            return false;  
        clip.setData(trans, null, clipid.kGlobalClipboard);  
        alert("复制成功！")  
    }else{
    	alert("您的浏览器不支持剪贴板操作，请手工复制！")  
    }  
}
</script>
<style type="text/css">

</style>
</head>

<body onLoad="showStatus();">

<form  method="post" scope="request" name="form"  action="/RegisterAction.do" onSubmit="return beforSubmit(this);" enctype="multipart/form-data">
<input type="hidden" name="evalpost" value="true">
<input type="hidden" name="from" value="offline">
<input type="hidden" name="regFlag" value="$regFlag">
<input type="hidden" name="encryptionType" value="$encryptionType">
<input type="hidden" name="updateDog" value="">
<input type="hidden" name="utype" value="$!utype">
<input type="hidden" name="from" value="$from">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="winCurIndex" value="$!request.getParameter('winCurIndex')">
#if("updateFunOffline"=="$!utype" )
<div id="listRange_id"  align="center">
	<div class="upgrade_mtop" style="margin-top:10px; border:1px solid #FFFFFF">
	    <div id="contentleft" style="height:100%;">
		    <div id="contentright" >
		    <div id="contentMain" style="height:1px;"> 
        	<div style="padding:20px 0 0 20px;overflow:hidden;">
           <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px 0px;float:none;text-align:left;line-height:20px;font-size:16px;">
            购买服务、产品、升级用户数
            </div>
            <p style="height:25px">
              <span style="color:#166492;float:left;line-height:35px;">点击进入在线服务平台  <a href='http://m.krrj.cn/login.htm?operate=aiologin&validcode=$validcode' target='_blank'> http://m.krrj.cn </a></span>
            </p>
            <p style="height:25px">
              <span style="color:#166492;float:left;line-height:35px;">请在在线服务平台中选择您要购买的产品并支付相关费用后按第二步要求下载证书 </span>
            </p>
          </div>
          <div style="padding:0 0 0 20px;overflow:hidden;clear:both;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px -20px;float:none;text-align:left;line-height:20px;font-size:16px;">
            打开注册网址下载证书
            </div>
            <p style="height:25px">			
              <span style="color:#166492;float:left;line-height:35px;">注册网址：http://reg.koronsoft.com</span>
            </p>
            <p style="height:25px">			
              <span style="color:#166492;float:left;line-height:35px;">按要求登入注册网址，并下载证书</span>
            </p>
          </div>
          <div style="padding:0 0 0 20px;overflow:hidden;clear:both;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px -40px;float:none;text-align:left;line-height:20px;font-size:16px;">
            导入证书

            </div>
            <p>            
             <input type="file" name="certFile" style="border:1px #a1a1a1 solid;float:left;margin:2px 0 0 0;height:25px;"/> 
             <button type="button" name="Submit" onClick="submitUpgrade()" class="regbut" style="padding-left:20px">导入证书</button>
            </p>
          </div>
          <div style="padding:10px 0 0 10px;overflow:hidden;">    
            <p style="width:300px;">
            <!-- 信息提示块，别删除 -->
            <div id="titleDiv"></div>
             </p>
          </div>
          <div style="padding:10px 0 0 20px;overflow:hidden;">    
            <p style="width:300px;">
             <button type="button" name="Submit" onClick="window.location.href='/RegisterAction.do?regFlag=2&winCurIndex=$!winCurIndex'" class="regbut">$text.get("common.lb.back")</button>
            </p>
          </div>
          
        </div>
	</div>
</div>
#elseif("updateFun"=="$!utype")
<div id="listRange_id"  align="center">
	<div class="upgrade_mtop" style="margin-top:10px; border:1px solid #FFFFFF">
	    <div id="contentleft" style="height:100%;">
		    <div id="contentright" >
		    <div id="contentMain" style="height:1px;"> 
        	<div style="padding:20px 0 0 20px;overflow:hidden;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px 0px;float:none;text-align:left;line-height:20px;font-size:16px;">
            购买服务、产品、升级用户数
            </div>
            <p style="height:25px">
              <span style="color:#166492;float:left;line-height:35px;">点击进入在线服务平台  <a href='http://m.krrj.cn/login.htm?operate=aiologin&validcode=$validcode' target='_blank'> http://m.krrj.cn </a></span>
            </p>
            <p style="height:25px">
              <span style="color:#166492;float:left;line-height:35px;">请在在线服务平台中选择您要购买的产品并支付相关费用后点击第2步的刷新证书 </span>
            </p>
          </div>
          <div style="padding:0 0 0 20px;overflow:hidden;clear:both;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px -20px;float:none;text-align:left;line-height:20px;font-size:16px;">
            刷新证书
            </div>
            <p style="height:25px">              
              <button type="button" name="Submit" onClick="submitUpgradeOnline()" class="regbut" style="padding-left:20px">刷新证书</button>
            </p>
            <p style="height:25px">
              <span style="color:#166492;float:left;line-height:35px;">请确认您的购买已经生效后再点击刷新证书。</span>
            </p>
          </div>
          <div style="padding:20px 0 0 20px;overflow:hidden;">
            <div style="padding:0 0 0 25px;float:none;text-align:left;line-height:20px;font-size:16px;border-top: solid 2px #7F9CB8;">
            如果您的服务器不能连入网络，请使用离线升级
            </div>
          </div>
          <div style="padding:0 0 0 20px;overflow:hidden;clear:both;">
                      
            <p>
              <span style="color:#166492;float:left;line-height:35px;"></span>
              <button type="button" name="Submit" onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFunOffline&winCurIndex=$!winCurIndex';" class="regbut" style="padding-left:20px">离线升级</button>
            </p>
          </div>
          
          <div style="padding:10px 0 0 10px;overflow:hidden;">    
            <p style="width:300px;">
            <!-- 信息提示块，别删除 -->
            <div id="titleDiv"></div>
             </p>
          </div>
          <div style="padding:10px 0 0 20px;overflow:hidden;">    
            <p style="width:300px;">
             <button type="button" name="Submit" onClick="window.location.href='/RegisterAction.do?regFlag=2&winCurIndex=$!winCurIndex'" class="regbut">$text.get("common.lb.back")</button>
            </p>
          </div>
          
        </div>
	</div>
</div>

#elseif("$encryptionType" == "3")
<div id="listRange_id"  align="center">
	<div class="upgrade_mtop" style="margin-top:10px; border:1px solid #FFFFFF">
	    <div id="contentleft" style="height:100%;">
		    <div id="contentright" >
		    <div id="contentMain" style="height:1px;"> 
		    <div style="padding:20px 0 0 20px;overflow:hidden;border-bottom: solid 2px #7F9CB8;font-size:15px;color:red;">
            <div style="padding:0 0 0 25px;float:none;text-align:left;line-height:20px;font-size:17px;">
            在线申请更新软件特征码
            </div>
            
				#if("0"==$!appstate)
			<p style="height: 25px">
              <span style="color:#166492;float:left;line-height:35px;"> 
	            		您在$!createTime的申请还未审核，请耐心等待或联系客服审批;
	            		</span>
			</p>
	            #elseif("1"==$!appstate)
	            <p style="height: 25px">
              <span style="color:#166492;float:left;line-height:35px;"> 
	            		您在$!createTime的申请已经审核通过，如果这是您最新一次的申请请点击“刷新证书”;<br/>如果这不是您最新的更新申请，请点击“申请更新”重新申请 
	            		</span>
			</p>
			<p><span style="color:#166492;float:left;line-height:35px;padding-bottom:8px"> 
				<button type="button" name="Submit" onClick="applayPcNo('$!globals.getSystemState().dogId','$!pcNo')" class="regbut">申请更新</button>
				<button type="button" name="Submit" onClick="submitUpgradeOnline()" class="regbut">刷新证书</button>
			</span></p>
	            #elseif("2"==$!appstate)
	            <p style="height: 25px">
              <span style="color:#166492;float:left;line-height:35px;"> 
	            		您在$!createTime的申请已经被拒绝，请联系客服处理，或重新申请一次;
	            		</span>
			</p>
			<p><span style="color:#166492;float:left;line-height:35px;padding-bottom:8px"> 
				<button type="button" name="Submit" onClick="applayPcNo('$!globals.getSystemState().dogId','$!pcNo')" class="regbut">申请更新</button>
			</span></p>
	            #else
	            <p style="height: 25px">
              <span style="color:#166492;float:left;line-height:35px;"> 
	            		您还未申请更新软件特征码;		
	            		</span>
			</p>
			<p><span style="color:#166492;float:left;line-height:35px;padding-bottom:8px"> 
				<button type="button" name="Submit" onClick="applayPcNo('$!globals.getSystemState().dogId','$!pcNo')" class="regbut">申请更新</button>
			</span></p>
	            #end 
            
          </div>
          <div style="padding:20px 0 0 20px;overflow:hidden;">
            <div style="padding:0 0 0 25px;float:none;text-align:left;line-height:20px;font-size:16px;">
            如果您的服务器不能连入网络，请按下面步骤处理
            </div>
          </div>
          <div style="padding:20px 0 0 20px;overflow:hidden;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px 0px;float:none;text-align:left;line-height:20px;font-size:16px;">
            打开注册网站
            </div>
            <p style="height: 25px">
              <span style="color:#166492;float:left;line-height:35px;">注册网站网址：http://reg.koronsoft.com</span><br/>
            </p>
            <p>
                <span>用可以上网的电脑打开此网站后，选择软加密，并按要求输入序列号和密码，登入注册网站</span>
            </p>
          </div>
          <div style="padding:0 0 0 20px;overflow:hidden;clear:both;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px -20px;float:none;text-align:left;line-height:20px;font-size:16px;">
            输入机器特征码及其它信息
            </div>
            <p style="height: 25px">
              <span style="color:#166492;float:left;line-height:35px;">机器特征码：$!pcNo</span>
              </p>
            <p>
                <span>按要求输入本机器特征码</span>
            </p>
          </div>
          <div style="padding:0 0 0 20px;overflow:hidden;clear:both;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px -40px;float:none;text-align:left;line-height:20px;font-size:16px;">
            下载证书并导入


            </div>
            <p>
              <span style="color:#166492;float:left;line-height:35px;">申请审核通过后，下载证书，并导入</span>
            </p>
            <p>
             <input type="file" name="certFile" style="border:1px #a1a1a1 solid;float:left;margin:2px 0 0 0;height:25px;"/> 
             <button type="button" name="Submit" onClick="submitUpgrade()" class="regbut" style="margin:0 0 0 20px;float:left;">导入证书</button>
            </p>
          </div>
          <div style="padding:10px 0 0 10px;overflow:hidden;">    
            <p style="width:300px;">
            <!-- 信息提示块，别删除 -->
            <div id="titleDiv"></div>
             </p>
          </div>
          <div style="padding:10px 0 0 20px;overflow:hidden;">    
            <p style="width:300px;">
             <button type="button" name="Submit" onClick="window.location.href='/RegisterAction.do?regFlag=2&winCurIndex=$!winCurIndex'" class="regbut">$text.get("common.lb.back")</button>
            </p>
          </div>
          
        </div>
	</div>
</div>
#else

<div id="listRange_id"  align="center">
	<div class="upgrade_mtop" style="margin-top:10px; border:1px solid #FFFFFF">
	    <div id="contentleft" style="height:100%;">
		    <div id="contentright" >
		    <div id="contentMain" style="height:1px;"> 
        	<div style="padding:20px 0 0 20px;overflow:hidden;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px 0px;float:none;text-align:left;line-height:20px;font-size:16px;">
            $text.get("upgrade.lb.offlinemsg1")
            </div>
            <p>
            #if("$encryptionType" == "1")
				<span style="color:#166492;float:left;line-height:35px;">$text.get("upgrade.lb.offlinemsg2") 
				$globals.getBol88()/offlineRegister.do
				</span>
				<a href="javascript:copyClb('$globals.getBol88()/offlineRegister.do')" style="width:50px;line-height:35px;padding-left:20px;float:left;">复制</a>
				<a href="$globals.getBol88()/offlineRegister.do" target="_blank" style="width:50px;line-height:35px;float:left;">打开</a>
			#else
				<span style="color:#166492;float:left;line-height:35px;">$text.get("upgrade.lb.offlinemsg2") 
				http://reg.koronsoft.com
				</span>
				<a href="javascript:copyClb('http://reg.koronsoft.com')" style="width:50px;line-height:35px;padding-left:20px;float:left;">复制</a>
				<a href="http://reg.koronsoft.com" target="_blank" style="width:50px;line-height:35px;float:left;">打开</a>
			#end
            </p>
          </div>
          <div style="padding:0 0 0 20px;overflow:hidden;clear:both;">
            <div style="padding:0 0 0 25px;background:url(/style/images/client/num.gif) no-repeat 0px -20px;float:none;text-align:left;line-height:20px;font-size:16px;">
            输入离线注册号注册

            </div>
            <p>
              <span style="color:#166492;float:left;line-height:35px;text-align:right;width:110px">$text.get("upgrade.lb.offlineNo")：</span>
              <input name="offlineNo" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" type="text" value="" maxlength="50">
            </p>
            <p>
              <span style="color:#166492;float:left;line-height:35px;text-align:right;width:110px">$text.get("upgrade.lb.compName")：</span>
              <input name="companyName" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" type="text" value="" maxlength="50">
              公司名称必须与注册时一致

            </p>
          </div>
         <div style="padding:10px 0 0 10px;overflow:hidden;">    
            <p style="width:300px;">
            <!-- 信息提示块，别删除 -->
            <div id="titleDiv"></div>
             </p>
          </div>
          <div style="padding:10px 0 0 20px;overflow:hidden;">    
            <p style="width:500px;">
            <button type="submit" name="Submit" class="regbut" >$text.get("upgrade.lb.submit")</button>
             <button type="button" name="Submit" onClick="window.location.href='/RegisterAction.do?regFlag=2&winCurIndex=$!winCurIndex'" class="regbut" >$text.get("common.lb.back")</button>
            </p>
          </div>
          
        </div>
	</div>
</div>

#end

<div id="listRange_id"  align="center" style="border:none;">
	<div class="upgrade_mtop" style="margin-top:10px; ">
	    <div id="contentleft" style="background:none;" > 
		 <div id="regfoot" style="width:1000px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
     </div>

</div>

</div>
</form> 
</body>
</html>

