var scrollcallback;
function startScroll(callback){
	scrollcallback = callback;
	/*document.addEventListener('touchmove', function(e) {
	        e.preventDefault();
	}, false); */
	
	setTimeout(loaded, 200);
}
var myScroll;
var pullUpEl;
var pullUpOffset;
function loaded() {//加载完成
        pullUpEl = document.getElementById('pullUp');
        pullUpOffset = pullUpEl.offsetHeight;
        myScroll = new iScroll(
                        'wrapper',
                        {
                                useTransition : true,
                                onRefresh : function() {
                                        if (pullUpEl.className.match('loading')) {
                                                pullUpEl.className = '';
                                                pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉显示更多...';
                                        }
                                },
                                onScrollMove : function() {
                                        if (this.y <(this.maxScrollY - 5)
                                                        && !pullUpEl.className.match('flip')) {
                                                pullUpEl.className = 'flip';
                                                pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放准备刷新...';
                                                this.maxScrollY = this.maxScrollY;
                                        } else if (this.y > (this.maxScrollY + 5)
                                                        && pullUpEl.className.match('flip')) {
                                                pullUpEl.className = '';
                                                pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉显示更多...';
                                                this.maxScrollY = pullUpOffset;
                                        }
                                },
                                onScrollEnd : function() {
                                        if (pullUpEl.className.match('flip')) {
                                                pullUpEl.className = 'loading';
                                                pullUpEl.querySelector('.pullUpLabel').innerHTML = '正在加载...';
                                                setTimeout(function() {
                                                	if(typeof(scrollcallback) == "function") {
                                                		scrollcallback(); 
                                                	}
                                                    //myScroll.refresh();
                                                }, 1000);
                                                
                                        }
                                }
                        });

        setTimeout(function() {
                document.getElementById('wrapper').style.left = '0';
        }, 800);
}

var mbimgs=new Array();
function showWeiXinPic(container)
{
  mbimgs = new Array();
  $("#"+container).find('img[weixinshow=true]').each(function(){
  	 mbimgs.push(this.src); 
  	 var nowImgurl = this.src;
  	 $(this).bind("click", function(){
      WeixinJSBridge.invoke("imagePreview",{
        "urls":mbimgs,
        "current":nowImgurl
        })
     });
  });
}


/**
*确认框
*title, 确认框标题
*okcallback, 点击确定后的回调函数
*nocallback 点击取消后的回调函数
*/
function mconfirm(title,okcallback,nocallback) {
	var popupDialogId = 'popupDialog';
	$('<div id="' + popupDialogId + '" style="min-width: 216px; max-width: 500px; position: absolute; z-index: 20000; width: 375px; height: 667px; vertical-align: middle; background-color: #fff;"> \
            <div  class="ui-content" style=" margin: 150px auto; background-color: #777973; border-radius: 8px; width: 60%;">\
                <span class="ui-title" style="color: #fff; text-align: center;margin-bottom: 15px;letter-spacing: 3px;font-weight: 400;display: inline-block;">'+title+'</span>\
                <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionConfirm"  style="background: #1784fd;width: 33%;border-radius: 5px;height: 30px;line-height: 30px;padding: 0;font-size: .9em;margin: 0 0 0 12%;font-weight: 100;">确定</a>\
                <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionCancel"  style="background: #DBDBDB;width: 33%;border-radius: 5px;height: 30px;line-height: 30px;padding: 0;font-size: .9em;margin: 0 0 0 5%;font-weight: 100;color: #333;text-shadow: none;">取消</a>\
            </div>\
        </div>').appendTo(jQuery.mobile.pageContainer);
    var popupDialogObj = $('#' + popupDialogId);
    $(".optionConfirm").on('click', function () {
        okcallback();
        $("#"+popupDialogId).remove();
    });
    $(".optionCancel").on('click', function () {
        nocallback();
        $("#"+popupDialogId).remove();
    });
    popupDialogObj.show();
    popupDialogObj.height(document.documentElement.clientHeight);
    popupDialogObj.width(document.documentElement.clientWidth);
	/* 
	var popupDialogId = 'popupDialog';
     $('<div data-role="popup" id="' + popupDialogId + '" data-confirmed="no" data-transition="pop" data-overlay-theme="b" data-theme="b" data-dismissible="false" style="min-width:216px;max-width:500px;"> \
                     <div role="main" class="ui-content">\
                         <h4 class="ui-title" style="color:#fff; text-align:left;margin-bottom:15px">'+title+'</h4>\
                         <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionConfirm" data-rel="back" style="background: #1784fd;width: 33%;border-radius: 5px;height: 30px;line-height: 30px;padding: 0;font-size: .9em;margin: 0 0 0 12%;font-weight: 100;">确定</a>\
                         <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionCancel" data-rel="back" data-transition="flow" style="background: #DBDBDB;width: 33%;border-radius: 5px;height: 30px;line-height: 30px;padding: 0;font-size: .9em;margin: 0 0 0 5%;font-weight: 100;color: #333;text-shadow: none;">取消</a>\
                     </div>\
                 </div>')
         .appendTo(jQuery.mobile.pageContainer);
     var popupDialogObj = $('#' + popupDialogId);
     popupDialogObj.trigger('create');
     popupDialogObj.popup({
         afterclose: function (event, ui) {
             popupDialogObj.find(".optionConfirm").first().off('click'); //取消绑定的事件
             var isConfirmed = popupDialogObj.attr('data-confirmed') === 'yes' ? true : false;
             $(event.target).remove();
             if (isConfirmed) {
                okcallback();
             }else{
             	nocallback();
             }
         }
     });
     popupDialogObj.popup('open');
     popupDialogObj.find(".optionConfirm").first().on('click', function () {
         popupDialogObj.attr('data-confirmed', 'yes');
     });*/
}

/** 弹出窗提示框
*title:提示内容 
*/
function malert(title){
	 var popupDialogId = 'popupDialog';
     $('<div data-role="popup" id="' + popupDialogId + '" data-confirmed="no" data-transition="pop" data-overlay-theme="b" data-theme="b" data-dismissible="false" style="min-width:216px;max-width:500px;"> \
                     <div role="main" class="ui-content"  style="text-align: center;">\
                         <h4 class="ui-title" style="color:#fff; text-align:center;margin-bottom:15px">'+title+'</h4>\
                         <a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b optionConfirm" data-rel="back" style="background: #1784fd;width: 33%;border-radius: 5px;height: 30px;line-height: 30px;padding: 0;font-size: .9em;margin: 0 ;font-weight: 100;">确定</a>\
                     </div>\
                 </div>')
         .appendTo(jQuery.mobile.pageContainer);
     var popupDialogObj = $('#' + popupDialogId);
     popupDialogObj.trigger('create');
     popupDialogObj.popup({
         afterclose: function (event, ui) {
             $(event.target).remove();
         }
     });
     popupDialogObj.popup('open');	
}

//不修改在详情界面直接审核时保存后校验define
var checkdefineInfo="";
function checkBillDeliver(tableName,keyId,successCallback){
	updateSuccessCallback = successCallback;
	checkAjax(tableName,keyId,'')
}
function checkAjax(tableName,keyId,defineInfo){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""}); 
	url = "/MobileAjax?op=check";
	if(defineInfo == undefined || defineInfo == ""){
		checkdefineInfo = "";
		defineInfo = "";
	}else{
		checkdefineInfo += defineInfo;
	}
	jQuery.post(url,{tableName:tableName,keyId:keyId,defineInfo:checkdefineInfo},function(data){ 		
		jQuery.mobile.loading('hide'); 	   
		if(data.code=="CONFIRM"){
		    msg = data.msg.split("#;#");
		    
		    mconfirm(msg[0].replace(/\\n/g,'<br/>'),
		    function(){
		    	if(msg[1] != ""){
					defineInfo = msg[1];				
					checkAjax(tableName,keyId,defineInfo);
				}
		    },
		    function(){
		    	if(msg[2] != ""){
					defineInfo =msg[2];			
					checkAjax(tableName,keyId,defineInfo);
				}
		    });
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else if(data.code=="OK"){
			deliverToPrepare(keyId,tableName)
		}else{
			alert(data.msg);	
		}
	},"json" ); 
}

/**
*执行自定义功能
*defineName, 自定义define的名字
*defineNameDisplay, 自定义功能的中文名称
*tableName,  自定义的表名
*keyId,  单据的id
*defineInfo, 必须填空““
*callback 回调函数
*/
var mydefineInfo = "";
function execDefine(defineName,defineNameDisplay,tableName,keyId,defineInfo,callback){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""}); 
	url = "/MobileAjax?op=execDefine";
	if(defineInfo == undefined || defineInfo == ""){
		mydefineInfo = "";
		defineInfo = "";
	}else{
		mydefineInfo += defineInfo;
	}
	jQuery.post(url,{defineName:defineName,buttonTypeName:defineNameDisplay,tableName:tableName,keyId:keyId,defineInfo:mydefineInfo},function(data){ 		
		jQuery.mobile.loading('hide'); 	
		if(data.code=="CONFIRM"){
		    msg = data.msg.split("#;#");
		    
		    mconfirm(msg[0].replace(/\\n/g,'<br/>'),
		    function(){
		    	if(msg[1] != ""){
					defineInfo = msg[1];				
					execDefine(defineName,defineNameDisplay,tableName,keyId,defineInfo,callback)
				}
		    },
		    function(){
		    	if(msg[2] != ""){
					defineInfo =msg[2];			
					execDefine(defineName,defineNameDisplay,tableName,keyId,defineInfo,callback)
				}
		    });
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else if(data.code=="OK"){
			callback();
		}else{
			alert(data.msg);	
		}  
	},"json" ); 
}


/** 取枚举信息
*	enumName 枚举名称
*/
function getEnum(enumName){	
	url = "/MobileAjax?op=getEnum&enum="+enumName;
	var ret = jQuery.ajax({
		 url: url,
		 async: false,
		 dataType:   "json" 		 
		}).responseJSON; 
	if(ret.code=="OK"){
		return ret.obj;	
	}else{
		alert(ret.msg);	
	}	
}
/**
* tableName:表的名称
* parentTableName：父表的名称
* moduleType：模块类型（同一表不同模块）
*callback:回调函数,回调函数必须接收参数right:true,false 在回调函数中一般用显示和隐藏添加按扭

*/
function canAdd(tableName,parentTableName,moduleType,callback){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	url = "/MobileAjax?op=canAdd";
	adddata = new Object();
	adddata.tableName= tableName;
	adddata.parentTableName= parentTableName;
	adddata.moduleType= moduleType;
	jQuery.post(url,adddata,function(data){ 
		if(data.code=="OK"){
			if(callback != undefined){
				callback(data.msg);
			}			
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
		jQuery.mobile.loading('hide'); 	   
	},"json" ); 
}

/**添加单据
*tableName：单据的表名
*parentTableName:父表名
*moduleType:模块名
*f_brother:邻居表关联代号
*parentCode:分级编号
*adddata其它参数
*callback:回调函数,	返回对象包含values 单据值的hashMap，childTableList 明细表名列表，flowDepict，fieldList，childShowField;;
*/
function addPrepare(tableName,parentTableName,moduleType,f_brother,parentCode,adddata,callback){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	url = "/MobileAjax?op=addPrepare";
	if(adddata == undefined || adddata==''){
		adddata = new Object();
	}
	adddata.tableName= tableName;
	adddata.parentTableName= parentTableName;
	adddata.moduleType= moduleType;
	adddata.f_brother= f_brother;
	adddata.parentCode= parentCode;
	jQuery.post(url,adddata,function(data){ 
		if(data.code=="OK"){
			if(callback != undefined){
				callback(data.obj);
			}			
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
		jQuery.mobile.loading('hide'); 	   
	},"json" ); 
}

/**添加单据
*tableName:表名 
*parentTableName：父表名
*saveType 保存类型 草稿为saveDraft
*values 需上传数据的json字符串，明细表数据为TABLENAME_表名的数组
*isCheck 表示是保存完后是否执行审核动作，即保存并审核为true,否则为false
*successCallback:修改成功后的回调函数,	用户可以此刷新界面或其它动作
*/
function addBill(tableName,parentTableName,saveType,isCheck,values,successCallback){
	updateSuccessCallback = successCallback;
	addAjax(tableName,parentTableName,saveType,isCheck,values,'')
}
function addAjax(tableName,parentTableName,saveType,isCheck,values,defineInfo){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""}); 
	url = "/MobileAjax?op=add";
	if(defineInfo == undefined || defineInfo == ""){
		updatedefineInfo = "";
		defineInfo = "";
	}else{
		updatedefineInfo += defineInfo;
	}
	jQuery.post(url,{tableName:tableName,parentTableName:parentTableName,saveType:saveType,values:values,defineInfo:updatedefineInfo},function(data){ 		
		jQuery.mobile.loading('hide'); 	   
		if(data.code=="CONFIRM"){
		    msg = data.msg.split("#;#");
		    
		    mconfirm(msg[0].replace(/\\n/g,'<br/>'),
		    function(){
		    	if(msg[1] != ""){
					defineInfo = msg[1];				
					addAjax(tableName,parentTableName,saveType,isCheck,values,defineInfo);
				}
		    },
		    function(){
		    	if(msg[2] != ""){
					defineInfo =msg[2];			
					addAjax(tableName,parentTableName,saveType,isCheck,values,defineInfo);
				}
		    });
		}else if(data.code=="OK"){
			keyId = data.obj.keyId;
			if(isCheck){
				deliverToPrepare(keyId,tableName);
			}else{
				alert(data.obj.msg);	
				if(typeof(updateSuccessCallback) == 'function'){
					updateSuccessCallback();
				}
			}
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			alert(data.msg);	
					
		}
	},"json" ); 
}


/** 单据删除
*keyId 要操作的单据id
*tableName 操作的表
*/
var deletedefineInfo="";
function deleteBill(keyId,tableName,callback){
	mconfirm("确定要删除吗？",
	    function(){
	    	deleteAjax(keyId,tableName,callback,'')
	    },
	    function(){
	    });
}
function deleteAjax(keyId,tableName,callback,defineInfo){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""}); 
	url = "/MobileAjax?op=delete";
	if(defineInfo == undefined || defineInfo == ""){
		deletedefineInfo = "";
		defineInfo = "";
	}else{
		deletedefineInfo += defineInfo;
	}
	jQuery.post(url,{tableName:tableName,keyId:keyId,defineInfo:deletedefineInfo},function(data){ 		
		jQuery.mobile.loading('hide'); 	   
		if(data.code=="CONFIRM"){
		    msg = data.msg.split("#;#");
		    
		    mconfirm(msg[0].replace(/\\n/g,'<br/>'),
		    function(){
		    	if(msg[1] != ""){
					defineInfo = msg[1];				
					deleteAjax(keyId,tableName,callback,defineInfo);
				}
		    },
		    function(){
		    	if(msg[2] != ""){
					defineInfo =msg[2];			
					deleteAjax(keyId,tableName,callback,defineInfo);
				}
		    });
		}else if(data.code=="OK"){
			alert(data.msg);
			if(typeof(callback)=='function'){
				callback();
			}
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			alert(data.msg);	
					
		}
	},"json" ); 
}
/** 工作流撤回
*keyId 要操作的单据id
*tableName 操作的表
*callback回调函数
*/
function cancelTo(keyId,tableName,callback){
	mconfirm("确定要撤回吗？",
	    function(){
			jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
			jQuery.post("/MobileAjax?op=cancelTo",
		       { keyId: keyId,tableName:tableName},
		       function(data){
		       	  jQuery.mobile.loading('hide'); 	       	  
		          malert(data.msg);
		          if(data.code=="OK"){
		          	callback();
		          }else if(data.code=="NOLOGIN"){
						location.href="/MobileQueryAction.do?operation=21";
				  }
		       },
		       "json" 
			); 
			$("#detailBtDiv").hide() ;
	    },
	    function(){});
}
/** 工作流反审核
*keyId 要操作的单据id
*tableName 操作的表
*callback回调函数
*/
function retCheck(keyId,tableName,callback){
	mconfirm("确定要反审核吗？",
	    function(){
			jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
			jQuery.post("/MobileAjax?op=retCheck",
		       { keyId: keyId,tableName:tableName},
		       function(data){
		       	  jQuery.mobile.loading('hide'); 	       	  
		          malert(data.msg);
		          if(data.code=="OK"){
		          	callback();
		          }else if(data.code=="NOLOGIN"){
					location.href="/MobileQueryAction.do?operation=21";
				  }
		       },
		       "json" 
			); 
			$("#detailBtDiv").hide() ;
	    },
	    function(){});
}
/** 工作流催办
*keyId 要操作的单据id
*tableName 操作的表
*display 表的中文名
*/
function hurryTrans(keyId,tableName,display){
	$("#detailBtDiv").hide() ;
	if($("hurryTransDiv").size()==0){
		$(document.body).append('\
			 <div data-role="dialog"  id="hurryTransDiv">\
			 	<div data-role="header">\
				    <h1>请输入催办信息</h1>\
				</div>\
 					  <div data-role="content">\
					<textarea id="hurryTranscontent" ></textarea>\
				  </div>					\
				  <div data-role="footer" class="ui-btn">\
				  <div data-role="controlgroup" data-type="horizontal">\
				    <a href="javascript:dohurryTrans()" id="hurryTransbt" data-role="button" data-icon="check">确定</a>\
				    <a href="#" data-role="button" data-rel="back" data-icon="delete">关闭</a>\
				  </div>  \
				  </div>\
				</div>\
		');
	}
	$("#hurryTranscontent").val("有"+display+"需要你审核，请查看 ");
	$("#hurryTransbt").attr("href","javascript:dohurryTrans('"+keyId+"','"+tableName+"')");
	jQuery.mobile.changePage($("#hurryTransDiv"),{ transition: "fade"} );	
	return;
}
function dohurryTrans(keyId,tableName){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""}); 
	url = "/MobileAjax?op=hurryTrans";
	wakeType = "4,";
	jQuery.post(url,{tableName:tableName,keyId:keyId,content:$("#hurryTranscontent").val(),wakeType:wakeType},function(data){ 		
		jQuery.mobile.loading('hide'); 	   
		alert(data.msg);	
		if(data.code=="OK"){
			$('.ui-dialog').dialog('close'); //关闭弹出窗	
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}
	},"json" ); 
}


/**修改单据
*tableName:表名 
*parentTableName：父表名
*saveType 保存类型 草稿为saveDraft
*isCheck 表示是保存完后是否执行审核动作，即保存并审核为true,否则为false
*values 需上传数据的json字符串，明细表数据为TABLENAME_表名的数组
*successCallback:修改成功后的回调函数,	用户可以此刷新界面或其它动作
*/
var updatedefineInfo="";
var updateSuccessCallback;
function updateBill(tableName,parentTableName,saveType,isCheck,values,successCallback){
	updateSuccessCallback = successCallback;
	updateAjax(tableName,parentTableName,saveType,isCheck,values,'')
}
function updateAjax(tableName,parentTableName,saveType,isCheck,values,defineInfo){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""}); 
	url = "/MobileAjax?op=update";
	if(defineInfo == undefined || defineInfo == ""){
		updatedefineInfo = "";
		defineInfo = "";
	}else{
		updatedefineInfo += defineInfo;
	}
	jQuery.post(url,{tableName:tableName,parentTableName:parentTableName,saveType:saveType,values:values,defineInfo:updatedefineInfo},function(data){ 		
		jQuery.mobile.loading('hide'); 	   
		if(data.code=="CONFIRM"){
		    msg = data.msg.split("#;#");
		    
			mconfirm(msg[0].replace(/\\n/g,'<br/>'),
			function(){
				if(msg[1] != ""){
					defineInfo = msg[1];				
					updateAjax(tableName,parentTableName,saveType,isCheck,values,defineInfo);
				}
			},
			function(){
				if(msg[2] != ""){
					defineInfo =msg[2];			
					updateAjax(tableName,parentTableName,saveType,isCheck,values,defineInfo);
				}
			});
		}else if(data.code=="OK"){
			if(isCheck){
				deliverToPrepare(keyId,tableName);
			}else{
				alert(data.msg);	
				if(typeof(updateSuccessCallback) == 'function'){
					updateSuccessCallback();
				}
			}
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			alert(data.msg);	
					
		}
	},"json" ); 
}
/**审核准备
*keyId:单据的编号 
*tableName：单据的表名
*/
var curDeliverInfo;
var lastActivePage = '';
var curDeliverKeyId='';
function deliverToPrepare(keyId,tableName){
	curDeliverKeyId = keyId;
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	url = "/MobileAjax?op=deliverToPrepare";
	jQuery.post(url,{keyId:keyId,tableName:tableName},function(data){ 
		  jQuery.mobile.loading('hide'); 	   
		if(data.code=="OK"){
			curDeliverInfo =(data.obj);		
			showDeliverToPrepare();
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
		jQuery.mobile.loading('hide'); 	   
	},"json" ); 
}
function showDeliverToPrepare(){
	if($("#pageAppNode").size() ==0){
		$(document.body).append('  \
		 <div data-role="page" id="pageAppNode"> \
		  <div data-role="header" data-position="fixed">\
		  	<a href="#pageone"  data-transition="flip" id="pageAppNodeBack" data-role="button" data-icon="back">返回</a>\
		    <h1>请选择下一步</h1>\
		  </div>\
		  <div data-role="content">\
		    <div data-role="fieldcontain">\
		    <label for="info">审核意见</label>\
		    <textarea name="deliverance" id="deliverance"></textarea>\
		    </div>\
		  	<ul data-role="listview" id="pageAppNodeUl" style="padding-top:15px">\
		    </ul>    \
		  </div>\
		</div>' );
	}	
	lastActivePage = jQuery.mobile.activePage.attr( "id" );
	$("#pageAppNodeBack").attr('href','#'+lastActivePage);
	
	flag=false;//是否有回退
	$("#pageAppNodeUl").empty();
	$("#pageAppNodeUl").append('<li data-role="list-divider">转交下一结点</li>');	
	for(var i = 0 ;i< curDeliverInfo.nextNodes.length;i++){
		nextNB = curDeliverInfo.nextNodes[i];
		if(nextNB.keyId.indexOf("back")<0){
			$("#pageAppNodeUl").append('<li><a href="javascript:showPeople(\''+nextNB.keyId+'\')">'+nextNB.display+'</a></li>');	
		}else{
			flag=true;
		}
	}
	if(flag){
		$("#pageAppNodeUl").append('<li data-role="list-divider">转交回退结点</li>');	
		for(var i = 0 ;i< curDeliverInfo.nextNodes.length;i++){
			nextNB = curDeliverInfo.nextNodes[i];
			if(nextNB.keyId.indexOf("back")>=0){
				$("#pageAppNodeUl").append('<li><a href="javascript:showPeople(\''+nextNB.keyId+'\')">'+nextNB.display+'</a></li>');	
			}else{
				flag=true;
			}
		}
	}
	jQuery.mobile.changePage($("#pageAppNode"),{ transition: "flip"} );		
	$("#pageAppNodeUl").listview('refresh');

}
var selKeyid;
var selNodeCheck; //选择的当前结点
function showPeople(keyid){	
	$("#oaTimeLimit").val(''); //清空时间结点
	selKeyid = keyid;	
	for(var i = 0 ;i< curDeliverInfo.nextNodes.length;i++){
		nextNB = curDeliverInfo.nextNodes[i];
		if(nextNB.keyId==keyid){
			selNodeCheck = curDeliverInfo.nextNodes[i];
			break;
		}
	}
	if( keyid == "-1"){
		saveDeliver();
		return;
	}
	
	if($("#peopleShow").size() ==0){
		$(document.body).append('  \
		 <div data-role="page" id="peopleShow">\
			  <div data-role="header" data-position="fixed">\
			  	<a href="#pageAppNode" data-transition="slidedown" data-role="button" data-icon="back">返回</a>\
			    <h1>请选择审核人</h1>\
			    <a href="javascript:saveDeliverOne()" data-transition="slideup" data-role="button" data-icon="forward">下一步</a>  \
			  </div>\
			  <div data-role="content">\
				<fieldset data-role="controlgroup" id="peopleset">\
				</fieldset>\
			  </div>\
		</div>' );
	}
	
	jQuery.mobile.changePage($("#peopleShow"),  { transition: "slideup"});
	$("#peopleset").empty();
	$("#peopleset").append('<legend>所选审核步骤：'+selNodeCheck.display+'</legend>');
	ckd = "";
	if(selNodeCheck.checkPeople.length==1 || selNodeCheck.approvePeople=="fix"){
		ckd = " checked ";
	}
	for(var i=0;i<selNodeCheck.checkPeople.length;i++){
		$("#peopleset").append('<label for="l_'+selNodeCheck.checkPeople[i][0]+'">'+selNodeCheck.checkPeople[i][1]+'</label><input type="checkbox" '+ckd+'  name="checkPeople" id="l_'+selNodeCheck.checkPeople[i][0]+'" value="'+selNodeCheck.checkPeople[i][0]+'">');
	}
	$("#peopleset").trigger('create');
}
function saveDeliverOne(){
	if(selNodeCheck.forwardTime=="true"){
		if($("#peopleShow").size() ==0){
		$(document.body).append('  \
			<div data-role="page" id="approveTime">\
			  <div data-role="header" data-position="fixed">\
			  	<a href="#peopleShow" data-transition="slidedown" data-role="button" data-icon="back">返回</a>\
			    <h1>请指定办理时限</h1>\
			    <a href="javascript:saveDeliver()" data-transition="slideup" data-role="button" data-icon="forward">下一步</a>  \
			  </div>\
			  <div data-role="content">\
				<fieldset data-role="controlgroup" id="peopleset">\
					<legend>请指定下一步办理的时限：</legend>\
					<label for="oaTimeLimit">下一步办理时限:</label>\
					<input type="text" name="oaTimeLimit" id="oaTimeLimit">\
				</fieldset>\
				<fieldset data-role="controlgroup" data-type="horizontal">        \
			          <label for="t0">天</label>\
			          <input type="radio" name="oaTimeLimitUnit" id="t0" value="0">\
			          <label for="t1" >时</label>\
			          <input type="radio" name="oaTimeLimitUnit" checked id="t1" value="1">\
			          <label for="t2">分</label>\
			          <input type="radio" name="oaTimeLimitUnit" id="t2" value="2">	\
			     </fieldset>\
			  </div>\
			</div>');
		}
		jQuery.mobile.changePage($("#approveTime"),  { transition: "slideup"});
	}else{
		saveDeliver();
	}
}
var selectcheckPerson="";
function saveDeliver(){
	selectcheckPerson = "";
	$("input[type=checkbox]:checked").each(function(){
		selectcheckPerson += $(this).val()+";";
	});
	
	if(selKeyid != "-1" && selectcheckPerson == ""){
		malert("审核人不能为空");
        return;
	}else if(selKeyid == "-1"){
		selectcheckPerson = "";
	}
	
	if(selNodeCheck.forwardTime!="true"){
		$("#oaTimeLimit").val("");
	}else if(selNodeCheck.forwardTime=="true" && $("#oaTimeLimit").val()==""){
		malert("请指定办理时限");
        return;
	}else if(selNodeCheck.forwardTime=="true" && !isInt($("#oaTimeLimit").val())){
		malert("办理时限必须是整数");
        return;
	}
	mconfirm('确定要提交审核吗？',
	    function(){
			confirmDeliver();
	    },
	    function(){});
}
function confirmDeliver(){		
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	jQuery.post("/MobileAjax?op=deliverTo",
       { keyId: curDeliverKeyId,type:'', nextStep: selKeyid,currNode:curDeliverInfo.currNode.keyId,designId:curDeliverInfo.designId,tableName:curDeliverInfo.tableName,
       	checkPerson:selectcheckPerson,deliverance:$("#deliverance").val(),oaTimeLimit:$("#oaTimeLimit").val(),oaTimeLimitUnit:$("input[name=oaTimeLimitUnit]:checked").val() },
       function(data){
       	  jQuery.mobile.loading('hide'); 	       	  
          if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		  }else if(data.code != "OK"){
          	malert(data.msg);
          }else{ 
          	jQuery.mobile.changePage($("#"+lastActivePage),  { transition: "slidedown"});
	          	if(typeof(updateSuccessCallback) == 'function'){
					updateSuccessCallback();
				}
          }
       },
       "json" 
	); 
}

/**查询单据详情
*keyId:单据的编号 
*tableName：单据的表名
*callback:查询完毕的回调函数,	返回对象包含values 单据值的hashMap，childTableList 明细表名列表，flowDepict，fieldList，childShowField;;
*/
function detail(keyId,tableName,callback){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	url = "/MobileAjax?op=detail";
	jQuery.post(url,{keyId:keyId,tableName:tableName},function(data){ 
		  
		if(data.code=="OK"){
			if(callback != undefined){
				callback(data.obj);
			}			
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
		jQuery.mobile.loading('hide'); 	   
	},"json" ); 
}
/**报表查询
*reportNumber:报表的名称
*isTableData：如果是数据表列表为true,否则是普通报表
*options：提交查询的条件参数
*callback:查询完毕的回调函数,回调函数必须接收参数condition,cols,rows;
*    condition:查询条件，格式 : [["字段名","值"],["字段名","值"]]  
*    cols:显示的列,格式：[["字段中文名","显示宽度","字段类型","分组名称","字段英文名"],["字段中文名","显示宽度","字段类型","分组名称","字段英文名"]]
*    rows：数据行，格式: [{"字段英文名":"值","字段英文名":"值"}]（注意：数组中是一个对象）

*/
var totalPage=0;//总记录数
function reportQuery(reportNumber,isTableData,options,callback){
	//jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	url = "/MobileAjax?op=report";
	if(isTableData){
		url += "&tableName="+reportNumber;
	}else{
		url += "&reportNumber="+reportNumber;
	}
	jQuery.post(url,options,function(data){ 
		//jQuery.mobile.loading('hide'); 	     
		if(data.code=="OK"){
			totalPage = data.obj.totalPage;//设置最大行数
			//处理条件，有些条件因为权限等原因需要被隐藏
			if(callback != undefined){
				callback(data.obj.conditions,data.obj.cols,data.obj.rows==undefined?[]:data.obj.rows);
			}
			if(myScroll){
				myScroll.refresh();
			}
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
	},"json" ); 
}

var popupObject = new Object();

/** 创建弹出窗
*tableName, 表名
*fieldName, 弹出窗关键字段
*moduleType, 本模块的类型，提供本参数，可以不提供
*pageSize,
*popupName,弹出窗的名字（如未指定表名和字段名，可直接指定弹出窗的名字）
*showcallback 查询完毕的回调函数,
*cornfirmCallback 确定选择的回调函数,
*/
var ispopuping = false;
function createPopupSelect(tableName,fieldName,moduleType,pageSize,popupName,showCallback,cornfirmCallback){
	if(ispopuping) 	return;	else ispopuping = true;
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""}); 
	popupObject.tableName=tableName;
	popupObject.fieldName=fieldName;
	popupObject.moduleType=moduleType;
	popupObject.pageSize=pageSize;
	popupObject.popupName=popupName;
	popupObject.showCallback=showCallback;
	popupObject.cornfirmCallback=cornfirmCallback;
	popupObject.keyword = "";
	if($("#popupselectDiv").size()==0){
		$("body").append('<div data-role="dialog" id="popupselectDiv"><div data-role="header"><h1 id="popupselectDivTitle"></h1></div> \
			<div data-role="fieldcontain" style="padding:8px;border: none;"> \
				  	 	<input  type="search" id="popupselectDivkw" onchange="doPopupSelect(this.value)" value=""> \
			</div> \
			<div id="scrollerpop">	 \
					<fieldset id="popupselectListDiv" data-role="controlgroup">  \
					</fieldset>  \
			</div>  \
			<div data-role="footer" class="ui-btn">  \
			<div data-role="controlgroup" data-type="horizontal"> \
			    <a href="javascript:doselectok()" data-role="button" data-icon="check">确定</a> \
			    <a href="javascript:doPopupSelectPageNo(false) " data-role="button" data-icon="arrow-l">上页</a> \
			    <a href="javascript:doPopupSelectPageNo(true) " data-role="button" data-icon="arrow-r">下页</a> \
			</div>   \
			</div> \
		</div>');
		$(document).on("pageshow","#popupselectDiv",function(){
		  	ispopuping = false;
		});
	}
	$("#popupselectDivTitle").text(popupName);	
	$("#popupselectDivkw").val("");	
	
	jQuery.mobile.changePage($("#popupselectDiv"),{ transition: "dialog"} );	
	//取弹出窗信息
	popupSelectInfo(tableName,fieldName,'',moduleType,'',function(showfields,tabParam){
		popupObject.showfields = showfields;
		var tabParamMap = new Object();
		for(i=0;i<tabParam.length;i++){
			tabParamMap[tabParam[i]] = $("#"+tabParam[i]).val();
		}		
		popupObject.tabParam = tabParamMap;
		
		//执行弹窗数据查询
		doPopupSelect('');
	});

}
function doselectok(){
	obj= $("input[name=pkeyId]:checked");
	if(obj.size()==0){
		alert("请先选择再确定");
		return;
	}
	pos = obj.val();	
	popupObject.cornfirmCallback(popupObject.result[pos]);
	$('.ui-dialog').dialog('close'); //关闭弹出窗		  
}
function doPopupSelectPageNo(isNext){
	$("#popupselectListDiv").empty();	
	if(isNext){
		popupObject.pageNo++;
	}else{
		popupObject.pageNo--;
	}
	if(popupObject.pageNo <1){
		popupObject.pageNo = 1;
	}
	popupSelect(popupObject.tableName,popupObject.fieldName,'',popupObject.moduleType,'',popupObject.pageSize,popupObject.pageNo,popupObject.keyword,popupObject.tabParam,
	function (result){
	    popupObject.result = result;
		popupObject.showCallback(popupObject.showfields,result);
	});
}
function doPopupSelect(keyword){
	$("#popupselectListDiv").empty();	
	popupObject.pageNo=1;
	popupObject.keyword=keyword;
	popupSelect(popupObject.tableName,popupObject.fieldName,'',popupObject.moduleType,'',popupObject.pageSize,popupObject.pageNo,popupObject.keyword,popupObject.tabParam,
	function (result){
	    popupObject.result = result;
		popupObject.showCallback(popupObject.showfields,result);
	});
}

/**弹出窗选择
*tableName, 表名
*fieldName, 弹出窗关键字段
*selectName, 弹出窗名字，未指定，则按表名和字段名自动选择弹出窗
*moduleType, 本模块的类型，提供本参数，可以不提供
*MOID, 本模块的ID可以不提供本参数,在非单据的弹出窗中必须提供本参数
*pageSize,
*pageNo,
*keyword, 弹出窗关键字
*tabParam,需转入参数给服务端的界面值map
*callback 查询完毕的回调函数,
*/
function popupSelect(tableName,fieldName,selectName,moduleType,MOID,pageSize,pageNo,keyword,tabParam,callback){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	url = "/MobileAjax?op=popup&tableName="+tableName+"&fieldName="+fieldName+"&selectName="+selectName+"&MOID="+MOID+
		"&moduleType="+moduleType+"&pageSize="+pageSize+"&pageNo="+pageNo;
	
	if(tabParam==null || tabParam == undefined){
		tabParam={keyword:keyword };
	}else{
		tabParam.keyword=keyword;
	}
	
	jQuery.post(url,tabParam,function(data){ 
		jQuery.mobile.loading('hide'); 	     
		if(data.code=="OK"){
			if(callback != undefined){
				callback(data.obj.result); 
			}
			
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
	},"json" ); 
}
/**了弹出窗显示字段信息和回传参数
*tableName, 表名
*fieldName, 弹出窗关键字段
*selectName, 弹出窗名字，未指定，则按表名和字段名自动选择弹出窗
*moduleType, 本模块的类型，提供本参数，可以不提供
*MOID, 本模块的ID可以不提供本参数,在非单据的弹出窗中必须提供本参数
*callback 查询完毕的回调函数,
*/
function popupSelectInfo(tableName,fieldName,selectName,moduleType,MOID,callback){
	jQuery.mobile.loading('show', {text: '正在努力中...',textVisible: true,theme: 'a',textonly: false,html: ""});  
	url = "/MobileAjax?op=popupInfo&tableName="+tableName+"&fieldName="+fieldName+"&selectName="+selectName+"&MOID="+MOID+
		"&moduleType="+moduleType+"&pageSize=10&pageNo=1";
	
	jQuery.post(url,function(data){ 
		jQuery.mobile.loading('hide'); 	     
		if(data.code=="OK"){
			if(callback != undefined){
				callback(data.obj.showfields,data.obj.tabParam); 
			}
			
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
	},"json" ); 
}



//取滚动条件的位置
function getSTop(){
    if (document.documentElement && document.documentElement.scrollTop) {
    	return document.documentElement.scrollTop;
    } else if (document.body) {
    	return document.body.scrollTop;
    }
}
//取滚动条高度，减去了屏幕的高度
function getSHeight(){
    if (document.documentElement && document.documentElement.scrollTop) {
    	return document.documentElement.scrollHeight-document.documentElement.clientHeight;
    } else if (document.body) {
    	return document.body.scrollHeight-document.documentElement.clientHeight;
    }
}

/**
*显示工作流审核信息
*flowDepict 单据detail返回的工作流审核记录对象
*container  审核记录插入的容器ID
*/
function showFlowDepict(flowDepict,container){
	if($("#flowDepict").size()==0){
		$("#"+container).append('\
		<div class="view-ul-wp" id="flowDepict">\
			<p class="t-p">审核记录</p>\
			<ul class="view-ul">\
				\
			</ul>\
		</div>');
	}
	if(flowDepict != undefined && flowDepict != null && flowDepict.length>0){
		vul =$("#flowDepict").find("ul");
		vul.empty();
		for(k = 1;k <=flowDepict.length;k++ ){
			deliver = flowDepict[k -1];
			vul.append( 
				 '<li class="view-li">\
					<div class="d-pa">\
						<em class="node-em">第'+k+'步&nbsp;'+deliver.nodeID+'</em>\
						<span class="check-person">'+deliver.checkPerson+'</span>\
					</div>\
					<div class="d-dbk">\
						<em class="end-time">['+deliver.endTime+']</em>\
						<div class="app-work-check">\
							&nbsp;'+(deliver.approvalOpinions != undefined ? deliver.approvalOpinions:'')+'<br>\
						</div>\
						<em class="node-type">\
							'+deliver.nodeType+ (deliver.workFlowNode != undefined && deliver.workFlowNode!=''?'-> ['+deliver.workFlowNode+']'+(deliver.checkPersons!=undefined && deliver.checkPersons!=''? '-'+deliver.checkPersons:''):'' ) +' \
						</em>\
					</div>\
				</li>'
			 ); 
		}
	}else{
		$("#flowDepict").hide();
	}
	
}
//取url中keyId
function getUrlkeyId() { 
var reg = new RegExp("(^|&)keyId=([^&]*)(&|$)", "i"); 
var r = window.location.search.substr(1).match(reg); 
if (r != null) return unescape(r[2]); return null; 
} 
function showpic(pic){
	if($("#imgObj").size()==0){
		$(document.body).append('\
	<div data-role="popup" id="pageimg"  class="photopopup" data-position-to="window" data-overlay-theme="a" data-corners="true" data-tolerance="0,0,0,0">\
	    <a href="javascript:closeImg();" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">关闭</a>\
	    <img id="imgObj"  /> \
	</div>');
	}
	$("#imgObj").attr("src",pic);
	$('#pageimg').popup().popup('open');
	//jQuery.mobile.changePage("#pageimg", 'pop', true, true);
	//jQuery.mobile.changePage($("#pageimg"), { transition: "pop" });
}
function closeImg(){
	$('#pageimg').popup('close'); //关闭弹出窗	
	//$('#pageimg').popup().popup('close'); //关闭弹出窗	
	//$('#pageimg').hide();
}

//我的工作流
function myWorkflowQuery(approveStatus,keyWord,pageNo){
	url = "/MobileAjax?op=myworkflow";
	jQuery.post(url,{approveStatus:approveStatus,keyWord:keyWord,pageNo:pageNo},function(data){ 
		if(data.code=="OK"){
			totalPage = data.obj.totalPage;//设置最大行数
			for(var i=0;i<data.obj.list.length;i++){
				flow =data.obj.list[i];
				if($("a[href*="+flow.billId+"]").size() > 0){
					continue;
				}
				$("#myflowlist").append(' \
		<li>\
			<a href="javascript:tourl(\''+flow.tableName+'\',\''+flow.billId+'\')" >\
				<h2><span class="w4">'+flow.templateName+'</span> <span class="w5">'+flow.applyContent+'</span></h2>\
				<p><span class="w2"> '+flow.applyBy+'</span>\
				<span class="w3">'+flow.currNodeName+(flow.checkPersonName != undefined && flow.checkPersonName != ''? '('+flow.checkPersonName+')':'')+'</span></p>\
			</a>\
		</li>\
			');
			}

			$("#myflowlist").listview('refresh');
			if(myScroll)
				myScroll.refresh();
		}else if(data.code=="NOLOGIN"){
			location.href="/MobileQueryAction.do?operation=21";
		}else{
			malert(data.msg);
		}
	},"json" ); 
}
/**
* 通过ajax读取目标url取得内容后写入容器中
*  url 目标url
*  container: 目标对象
*  callback 回调方法
*/
function ajaxUpdateHtml(url,container,callback){
	jQuery.post(url,{},
       function(data){
       	  html = data.substring(data.indexOf("<body>")+6,data.indexOf("</body>")).trim();
       	  html = html.substring(html.indexOf(">")+1,html.length - 7);
       	  $("#"+container).empty();
       	  $("#"+container).append(html);
       	  callback();
       }
	); 
}

