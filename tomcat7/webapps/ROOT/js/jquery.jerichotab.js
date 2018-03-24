
$.extend($.fn, {
    initJerichoTab: function(setting) {
        var opts = $.fn.extend({
            renderTo: null,
            uniqueId: null,
            tabs: [],
            activeTabIndex: 0,
            loadOnce: true,
            tabWidth: 100,
            loader: 'righttag'
        }, setting);
        function createJerichoTab() {
            if (opts.renderTo == null) { alert('you must set the \'renderTo\' property\r\t--JeirchoTab'); return; }
            if (opts.uniqueId == null) { alert('you must set the \'uniqueId\' property\r\t--JeirchoTab'); return; }
            if ($('#' + opts.uniqueId).length > 0) { alert('you must set the \'uniqueId\' property as unique\r\t--JeirchoTab'); return; }
            var jerichotab = $('<div class="jericho_tab"><div class="tab_pages" ><div class="tabs"><ul /></div></div></div>')
                              	.appendTo($(opts.renderTo));
            $.fn.jerichoTab = {
                master: jerichotab,
                tabWidth: opts.tabWidth,
                tabPageWidth: $('.tab_pages', jerichotab).width(),
                loader: opts.loader,
                loadOnce: opts.loadOnce,
                activeIndex : 0,
                tabpage: $('.tab_pages>.tabs>ul', jerichotab),
                addTab: function(tabsetting) {
                    var ps = $.fn.extend({
                        tabFirer: null,
                        title: 'Jericho Tab',
                        data: { dataType: '', dataLink: '' },
                        iconImg: '',
                        closeable: true,
                        onLoadCompleted: null
                    }, tabsetting);
                    tagGuid = (typeof tagGuid == 'undefined' ? 0 : tagGuid + 1);
                    var curIndex = tagGuid;
                    if (ps.tabFirer != null && typeof(tabHash) != 'undefined') {
                        var tabIndex = containTab(ps.tabFirer,tabHash)
                        if(tabIndex == $.fn.jerichoTab.activeIndex){
                        	return $.fn.setTabActive(tabIndex).adaptSlider() ;
                        }
                        if (tabIndex!=-1 && (tabsetting.tabFirer.indexOf("CustomQueryAction.do") == -1 && tabsetting.tabFirer.indexOf("ReportDataAction.do") == -1 )){
                            return $.fn.setTabActive(tabIndex).adaptSlider().loadData();
                        }
                    }
                    var linkAddr = ps.data.dataLink ;
                    if(linkAddr.indexOf("src=menu")!=-1){
                    	linkAddr = linkAddr.substring(0,linkAddr.indexOf("src=menu")-1);
                    }
                    var newTab;
                   	if(curIndex == "0"){
                    	newTab = $('<li class="jericho_tabs tab_selected"  id="jerichotab_' + curIndex + '" dataType="' + ps.data.dataType + '" dataLink="' + ps.data.dataLink + '">' +
                                   '<span title='+ps.title+'>'+ps.title+'</span>'+(ps.closeable?'<a class="tab_close" title="关闭窗口"></a>':'')+'</li>')
                                   		.dblclick(function (){
                                   			$.fn.closeActiveTab();
                                   		}).appendTo($.fn.jerichoTab.tabpage).css('opacity', '0')
                                          .applyHover().applyCloseEvent().stop().animate({ 'opacity': '1', width: opts.tabWidth }, function() {
                                             $.fn.setTabActive(curIndex);});
                    }else{
                    	newTab = $('<li class="jericho_tabs tab_selected"  id="jerichotab_' + curIndex + '" dataType="' + ps.data.dataType + '" dataLink="' + ps.data.dataLink + '">' +
                                   '<span title='+ps.title+'>'+ps.title+'</span>'+(ps.closeable?'<a class="tab_close" title="关闭窗口"></a>':'')+'</li>')
                                   		.dblclick(function (){
                                   			$.fn.closeActiveTab();
                                   		}).contextPopup({
                                   			title: '',items: [{icon:'0 0',label:'加入快捷',action:function(e) {addMyDest(linkAddr,ps.title)}},{icon:'0 0',label:'关闭',action:function(e) {$.fn.closeActiveTab();}}]
                                   		}).appendTo($.fn.jerichoTab.tabpage).css('opacity', '0')
                                          .applyHover().applyCloseEvent().stop().animate({ 'opacity': '1', width: opts.tabWidth }, function() {
                                             $.fn.setTabActive(curIndex);});
                    }   
                        
                    tabHash = (typeof tabHash == 'undefined' ? [] : tabHash);
                    tabHash.push({
                        index: curIndex,
                        tabFirer: ps.tabFirer,
                        tabId: 'jerichotab_' + curIndex,
                        holderId: 'jerichotabholder_' + curIndex,
                        iframeId: 'jerichotabiframe_' + curIndex,
                        onCompleted: ps.onLoadCompleted
                    });
                    return newTab.applySlider();
                }
            };
            $.each(opts.tabs, function(i, n) {
                $.fn.jerichoTab.addTab(n);
            });
            if (tabHash.length == 0)
                jerichotab.css({ 'display': 'none' });
        }
        createJerichoTab();
        $.fn.setTabActive(opts.activeTabIndex).loadData();
		$(function(){
			$.fn.jerichoTab.tabPageWidth = $('.tab_pages', $.fn.jerichoTab.master).width() - (($('.jericho_slider').length > 0) ? 78 : 0);
            $('.tabs', $.fn.jerichoTab.master).width($.fn.jerichoTab.tabPageWidth).applySlider().adaptSlider();

		});
        $(window).resize(function() {
            $.fn.jerichoTab.tabPageWidth = $('.tab_pages', $.fn.jerichoTab.master).width() - (($('.jericho_slider').length > 0) ? 78 : 0);
            $('.tabs', $.fn.jerichoTab.master).width($.fn.jerichoTab.tabPageWidth).applySlider().adaptSlider();
        });
    },
    setTabActiveByOrder: function(orderKey) {
        var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
        if (lastTab.length > 0) lastTab.swapTabEnable();
        return $('#jericho_tabs').filter(':nth-child(' + orderKey + ')').swapTabEnable();
    },
    setTabActive: function(orderKey) {
        var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
        if (lastTab.length > 0) lastTab.swapTabEnable();
        return $('#jerichotab_' + orderKey).swapTabEnable();
    },
    buildIFrame: function(src) {
        return this.each(function() {
            var onComleted = null, jerichotabiframe = '';
            for (var tab in tabHash) {
                if (tabHash[tab].holderId == $(this).attr('id')) {
                    onComleted = tabHash[tab].onCompleted;
                    jerichotabiframe = tabHash[tab].iframeId;
                    break;
                }
            }
            var height = $(window).height()-102;
    
            var iframe = $('<iframe id="' + jerichotabiframe + '" name="' + jerichotabiframe + '" src="' + src + '" frameborder="0" scrolling="no" />')
                                        .css({ width: $(this).parent().width(), height: height, border: 0 })
                                            .appendTo($(this));
            document.getElementById(jerichotabiframe).onload = function() {
                !!onComleted ? onComleted(arguments[1]) : true;
            }
        });
    },
    loadData: function() {
        return this.each(function() {
            var onComleted = null, holderId = '', tabId = '',curIndex=0;
            for (var tab in tabHash) {
                if (tabHash[tab].tabId == $(this).attr('id')) {
                    onComleted = tabHash[tab].onCompleted;
                    holderId = '#' + tabHash[tab].holderId;
                    tabId = '#' + tabHash[tab].tabId;
                    curIndex = tabHash[tab].index;
                    break;
                }
            }
            $.fn.jerichoTab.activeIndex = curIndex ;
            var dataType = $(this).attr('dataType');
            var dataLink = $(this).attr('dataLink');
            dataLink = dataLink + "&winCurIndex=" + curIndex ;
            if (typeof dataType == 'undefined' || dataType == '' || dataType == 'undefined') { removeLoading(); return; }
            $('#jerichotab_contentholder').children('div[class=curholder]').attr('class', 'holder').css({ 'display': 'none' });
            var holder = $(holderId);
            if (holder.length == 0) {
                holder = $('<div class="curholder" id="' + holderId.replace('#', '') + '" />').appendTo($('#jerichotab_contentholder'));
                load(holder);
            }
            else {
                holder.attr('class', 'curholder').css({ 'display': 'block' });
                if (!$.fn.jerichoTab.loadOnce){
                    holder.html('');
                    load(holder);
                }
            }

            function load(c) {
                switch (dataType) {
                    case 'formtag':
                        $(dataLink).css('display', 'none');
                        var clone = $(dataLink).clone(true).appendTo(c).css('display', 'block');
                        break;
                    case 'html':
                        holder.load(dataLink + '?t=' + Math.floor(Math.random()));
                        break;
                    case 'iframe':
                        holder.buildIFrame(dataLink, holder);
                        break;
                    case 'ajax':
                        $.ajax({
                            url: dataLink,
                            data: { t: Math.floor(Math.random()) },
                            error: function(r) {
                                holder.html('error! can\'t load data by ajax');
                            },
                            success: function(r) {
                                holder.html(r);
                            }
                        });
                        break;
                }
            }
            function removeLoading(h) {
                !!onComleted ? onComleted(h) : true;
            }
        });
    },
    attachSliderEvent: function() {
        return this.each(function() {
            var me = this;
            $(me).mouseup(function() {
                if ($(me).is('[slide=no]')) return;
                var offLeft = parseInt($.fn.jerichoTab.tabpage.css('left'));
                var maxLeft = tabHash.length * $.fn.jerichoTab.tabWidth - $.fn.jerichoTab.tabPageWidth + 58;
                switch ($(me).attr('pos')) {
                    case 'left':
                        if (offLeft + $.fn.jerichoTab.tabWidth < 0)
                            $.fn.jerichoTab.tabpage.stop().animate({ left: offLeft + $.fn.jerichoTab.tabWidth });
                        else
                            $.fn.jerichoTab.tabpage.stop().animate({ left: 0 }, function() {
                                $(me).attr({ 'slide': 'no', 'class': 'jericho_sliders jericho_sliderleft_disable' });
                            });
                        $('.jericho_sliders[pos=right]').attr({ 'slide': 'yes', 'class': 'jericho_sliders jericho_sliderright_enable' });
                        break;
                    case 'right':
                        if (offLeft - $.fn.jerichoTab.tabWidth > -maxLeft)
                            $.fn.jerichoTab.tabpage.stop().animate({ left: offLeft - $.fn.jerichoTab.tabWidth });
                        else
                            $.fn.jerichoTab.tabpage.stop().animate({ left: -maxLeft }, function() {
                                $(me).attr({ 'slide': 'no', 'class': 'jericho_sliders jericho_sliderright_disable' });
                            });
                        $('.jericho_sliders[pos=left]').attr({ 'slide': 'yes', 'class': 'jericho_sliders jericho_sliderleft_enable' });
                        break;
                }
            });
        });
    },
    applySlider: function() {
        return this.each(function() {
            if (typeof tabHash == 'undefined' || tabHash.length == 0) return;
            var offWidth = tabHash.length * $.fn.jerichoTab.tabWidth - $.fn.jerichoTab.tabPageWidth + 58;
            if (tabHash.length > 0 && offWidth > 0) {
                $.fn.jerichoTab.tabpage.parent().css({ width: $.fn.jerichoTab.tabPageWidth - 58 });
                $.fn.jerichoTab.tabpage.css({ width: offWidth + $.fn.jerichoTab.tabPageWidth - 58 }).stop().animate({ left: -offWidth }, function() {
                    if ($('.jericho_sliders').length <= 0) {
                        $.fn.jerichoTab.tabpage.parent()
                            .before($('<div class="jericho_sliders jericho_sliderleft_enable" slide="yes" pos="left" />'));
                        $.fn.jerichoTab.tabpage.parent()
                            .after($('<div class="jericho_sliders jericho_sliderright_disable" pos="right" slide="yes" />'));
                        $(".close_all").remove() ;
                        $('<a class="close_all" href="javascript:void(0)" title="关闭所有"></a>').click(function(){
		                 	$(".tabs ul li").each(function() {
					    		$("#"+$(this).attr("id")+" a").mouseup()
					    	}) ;
			    			$(".close_all").remove() ;
	                	}).appendTo($('.tab_pages')) ;
                        $('.jericho_sliders').attachSliderEvent();
                    }
                });
            }else if (tabHash.length > 0 && offWidth <= 0) {
                $('.jericho_sliders').remove();
                $(".close_all").remove() ;
                $.fn.jerichoTab.tabpage.parent().css({ width: $.fn.jerichoTab.tabPageWidth });
                $.fn.jerichoTab.tabpage.css({ width: -offWidth + $.fn.jerichoTab.tabPageWidth }).stop().animate({ left: 0 });
                if(tabHash.length>3){
	                $('<a class="close_all" href="javascript:void(0)" title="关闭所有"></a>').click(function(){
	                 	$(".tabs ul li").each(function() {
				    		$("#"+$(this).attr("id")+" a").mouseup()
				    	}) ;
			    		$(".close_all").remove() ;
	                }).appendTo($.fn.jerichoTab.tabpage) ;
                }
            }
        });
    },
    adaptSlider: function() {
        return this.each(function() {
            if ($('.jericho_sliders').length > 0) {
                var offLeft = parseInt($.fn.jerichoTab.tabpage.css('left'));
                var curtag = '#', index = 0;
                for (var t in tabHash) {
                    if (tabHash[t].tabId == $(this).attr('id')) {
                        curtag += tabHash[t].tabId;
                        index = parseInt(t);
                        break;
                    }
                }
                var tabWidth = $.fn.jerichoTab.tabPageWidth - 58;
                var space_l = $.fn.jerichoTab.tabWidth * index + offLeft;
                var space_r = $.fn.jerichoTab.tabWidth * (index + 1) + offLeft;
                function setSlider(pos, enable) {
                    $('.jericho_sliders[pos=' + pos + ']').attr({ 'slide': (enable ? 'yes' : 'no'), 'class': 'jericho_sliders jericho_slider' + pos + '_' + (enable ? 'enable' : 'disable') });
                }
                if ((space_l < 0 && space_l > -$.fn.jerichoTab.tabWidth) && (space_r > 0 && space_r < $.fn.jerichoTab.tabWidth)) {
                    $.fn.jerichoTab.tabpage.stop().animate({ left: -$.fn.jerichoTab.tabWidth * index }, function() {
                        if (index == 0) setSlider('left', false);
                        else setSlider('left', true);
                        setSlider('right', true);
                    });
                }
                if ((space_l < tabWidth && space_l > tabWidth - $.fn.jerichoTab.tabWidth) && (space_r > tabWidth && space_r < tabWidth + $.fn.jerichoTab.tabWidth)) {
                    $.fn.jerichoTab.tabpage.stop().animate({ left: -$.fn.jerichoTab.tabWidth * (index + 1) + tabWidth }, function() {
                        if (index == tabHash.length - 1) setSlider('right', false);
                        else setSlider('left', true);
                        setSlider('left', true);
                    });
                }
            }
        });
    },
    applyCloseEvent: function() {
        return this.each(function() {
            var me = this;
            
            $('.tab_close', this).mouseup(function(e) {
                e.stopPropagation();                
                if ($(this).length == 0) return;
                //zxy 修改，增加判断是否有未保存单据                   
                var framid = me.id;
                framid = framid.replace('jerichotab','jerichotabiframe');
                var framObj = window.frames[framid];
                if(framObj.contentWindow){
					framObj = framObj.contentWindow;
				}
                if(!framObj.save && framObj.changeConfirm && framObj.is_form_changed()){
					if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
						return;
					}
				}
				//该单下是否有子窗口
				var hasNoSData = false;
				var popDiv = $(".asyncbox_normal",framObj.document).each(function(){
					popDivId = this.id;
					popDivFrame = framObj.frames[popDivId+"_Frame"];
					if(popDivFrame.contentWindow){
						popDivFrame = popDivFrame.contentWindow;
					}
					if(!popDivFrame.save && popDivFrame.changeConfirm && popDivFrame.is_form_changed()){
						hasNoSData = true;
					}
				}); 
				if(hasNoSData && ! confirm("您有未保存的数据，是否确定放弃保存并离开")){
					return;
				}
				
                
                $(me).stop().animate({ 'opacity': '0', width: '0px' }, function() {
                    var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
                    if (lastTab.attr('id') == $(this).attr('id')) {
                        $(this).prev().swapTabEnable().loadData();
                    }
                    for (var t in tabHash) {
                        if (tabHash[t].tabId == $(me).attr('id')) {
                            if (tabHash[t].tabFirer != null){
                                tabHash[t].tabFirer = "" ;
                            }
                            tabHash.splice(t, 1);
                        }
                    }
                    $(me).applySlider().remove();
                    $('#jerichotabholder_' + $(me).attr('id').replace('jerichotab_', '')).remove();
                })
            })
        });
    },
    applyHover: function() {
        return this.each(function() {
            $(this).hover(function() {
                if ($(this).hasClass('tab_unselect')) $(this).addClass('tab_unselect_h');
            }, function() {
                if ($(this).hasClass('tab_unselect_h')) $(this).removeClass('tab_unselect_h');
            }).mouseup(function() {
                if ($(this).hasClass('tab_selected')) return;
                var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
                lastTab.attr('class', 'jericho_tabs tab_unselect');
                $('#jerichotabholder_' + lastTab.attr('id').replace('jerichotab_', '')).css({ 'display': 'none' });
                $(this).attr('class', 'jericho_tabs tab_selected').loadData().adaptSlider();
            });
        });
    },
    swapTabEnable: function() {
        return this.each(function() {
            if ($(this).hasClass('tab_selected'))
                $(this).swapClass('tab_selected', 'tab_unselect');
            else if ($(this).hasClass('tab_unselect'))
                $(this).swapClass('tab_unselect', 'tab_selected');
        });
    },
    swapClass: function(css1, css2) {
        return this.each(function() {
            $(this).removeClass(css1).addClass(css2);
        })
    },
    closeActiveTab: function(){    	
    	var activeIndex = $.fn.jerichoTab.activeIndex ;
    	$("#jerichotab_"+activeIndex+" a").mouseup()
    }
});

function containTab(tabFirer,tabHash){
	for (var tab in tabHash) {
       if (tabHash[tab].tabFirer == tabFirer) {
           return tabHash[tab].index ;
       }
   }
   return -1 ;
}    
String.prototype.cut = function(len) {
	len = len / 10 - 1 ;
    var position = 0;
    var result = [];
    var tale = '';
    for (var i = 0; i < this.length; i++) {
        if (position >= len) {
            tale = '...';
            break;
        }
        if (this.charCodeAt(i) > 255) {
            position += 2;
            result.push(this.substr(i, 1));
        }
        else {
            position++;
            result.push(this.substr(i, 1));
        }
    }
    return result.join("") + tale;
}