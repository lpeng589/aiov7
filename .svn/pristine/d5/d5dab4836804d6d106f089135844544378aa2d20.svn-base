jQuery.fn.contextPopup = function(menuData) {
	var settings = {
		gutterLineClass: 'gutterLine',
		headerClass: 'header',
		seperatorClass: 'divider',
		title: '',
		items: []
	};
	
	$.extend(settings, menuData);

  function createMenu(e) {
  	var updateOp = $(e.target).parent("ul").attr("updateOp");//获取是否有修改权限
  	$(".contextMenuPlugin").remove();
    var menu = $('<ul style="z-index:1000001" class="contextMenuPlugin"><div class="' + settings.gutterLineClass + '"></div></ul>')
      .appendTo(document.body);
    if (settings.title) {
      $('<li class="' + settings.headerClass + '"></li>').text(settings.title).appendTo(menu);
    }
    $.each(settings.items,function(i,item) {
	      if (item) {
	        if(((item.label == "修改" || item.label =="新增联系人" || item.label =="共享")) && updateOp == "false"){
	        		return "false";
	        }
	        
	        var rowCode = '<li><a href="javascript:void()"><span></span></a></li>';
	        // if(item.icon)
	        //   rowCode += '<img>';
	        // rowCode +=  '<span></span></a></li>';
	        var row = $(rowCode).appendTo(menu);
	        if(item.icon){
	          var icon = $('<div style="background-position:'+item.icon+';background-repeat:no-repeat;"></div>');
	          icon.insertBefore(row.find('span'));
	        }
	        row.find('span').text(item.label);
	        if (item.action) {
	          row.find('a').click(function(){ item.action(e); });
	        }
	      } else {
	        $('<li class="' + settings.seperatorClass + '"></li>').appendTo(menu);
	      }
    });
    menu.find('.' + settings.headerClass ).text(settings.title);
    return menu;
  }

  // On contextmenu event (right click)
  this.bind('contextmenu', function(e) {	
    var selectValue = "" ;
    if(typeof(document.selection)!="undefined"){
    	selectValue = document.selection.createRange().text ;
    }else{
    	selectValue = document.getSelection()+"" ;
    }
    if(selectValue.length>0) return ;
    var menu = createMenu(e).show();
    var left = e.pageX + 5, /* nudge to the right, so the pointer is covering the title */
        top = e.pageY;
    if (top + menu.height() >= $(window).height()) {
        top -= menu.height();
    }
    if (left + menu.width() >= $(window).width()) {
        left -= menu.width();
    }

    // Create and show menu
    menu.css({left:left, top:top})
      .bind('contextmenu', function() { return false; });

    // Cover rest of page with invisible div that when clicked will cancel the popup.
    $(document).bind('contextmenu click', function() {
        // If click or right click anywhere else on page: remove clean up.
        menu.remove();
    });

    // When clicking on a link in menu: clean up (in addition to handlers on link already)
    menu.find('a').click(function() {
      menu.remove();
    });
	
    return false;
  });

  return this;
};

