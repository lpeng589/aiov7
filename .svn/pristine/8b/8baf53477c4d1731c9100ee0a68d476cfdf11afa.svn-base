/*******************************************************************************
* KindEditor - WYSIWYG HTML Editor for Internet
* Copyright (C) 2006-2011 kindsoft.net
*
* @author Roddy <luolonghao@gmail.com>
* @site http://www.kindsoft.net/
* @licence http://www.kindsoft.net/license.php
*******************************************************************************/
KindEditor.plugin('template', function(K) {
	var self = this, name = 'template', lang = self.lang(name + '.'),
		//htmlPath = self.pluginsPath + name + '/html/'+'allTemplate.jsp?selectId='+selectId;
		htmlPath = self.pluginsPath + name + '/html/'+'allTemplate.jsp';
		htmlPaths = self.pluginsPath + name + '/html/'+'1.html';
		
	function getFilePath(fileName) {
		return htmlPath;
		//return htmlPath + fileName + '?ver=' + encodeURIComponent(K.DEBUG ? K.TIME : K.VERSION);
	}
	
	function getFilePaths(fileName){
		return htmlPaths;
	}
	self.clickToolbar(name, function() {
		var lang = self.lang(name + '.'),
			arr = ['<div class="ke-plugin-template" style="padding:10px 20px;">',
				'<div class="ke-header">',
				// left start
				'<div class="ke-left">',
				lang. selectTemplate + ' <select id="selectId" name="selectId" onchange="change()">'];
			AjaxRequest('/UtilServlet?operation=queryHTMLModule');
			K.each(lang.fileList, function(key, val) {
				arr.push('<option value="' + key + '">' + val + '</option>');
			});
			arr.push(response);
			html = [arr.join(''),
				'</select></div>',
				// right start
				'<div class="ke-right">',
				'<input type="checkbox" id="keReplaceFlag" name="replaceFlag" value="1" style="display:none"/> <label for="keReplaceFlag" style="display:none">' + lang.replaceContent + '</label>',
				'</div>',
				'<div class="ke-clearfix">',
				'<a href="/UserFunctionQueryAction.do?tableName=tblHTMLModuleManager&winCurIndex=A" target="top" style="margin-left:8px;">模板维护</a>',
				'</div>',
				'</div>',
				'<iframe class="ke-textarea" frameborder="0" style="width:458px;height:260px;background-color:#FFF;"></iframe>',
				'</div>'].join('');
		var dialog = self.createDialog({
			name : name,
			width : 500,
			title : self.lang(name),
			body : html,
			yesBtn : {
				name : self.lang('yes'),
				click : function(e) {
					var doc = K.iframeDoc(iframe);
					self[checkbox[0].checked ? 'html' : 'insertHtml'](doc.body.innerHTML).hideDialog().focus();
				}
			}
		});
		var selectBox = K('select', dialog.div),
			checkbox = K('[name="replaceFlag"]', dialog.div),
			iframe = K('iframe', dialog.div);
		checkbox[0].checked = true;
		iframe.attr('src', getFilePaths(selectBox.val()));
		selectBox.change(function() {  
			iframe.attr('src', getFilePath(this.value)+'?selectId='+this.value);
		});
		
	});
});

