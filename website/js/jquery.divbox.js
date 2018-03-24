jQuery.fn.extend({
    OpenDiv: function(){
        var sWidth, sHeight;
        sWidth = window.screen.availWidth;
        if (window.screen.availHeight > document.body.scrollHeight){
            sHeight = window.screen.availHeight;
        }else{
            sHeight = document.body.scrollHeight + 20;
        }
        var maskObj = document.createElement("div");
        maskObj.setAttribute('id', 'BigDiv');
        maskObj.style.position = "absolute";
        maskObj.style.top = "0";
        maskObj.style.left = "0";
        maskObj.style.background = "azure";
        if(typeof(varOpacity)!="undefined" && varOpacity!=null){
        	maskObj.style.filter = "Alpha(opacity=100);";
        	maskObj.style.opacity = "1";
        }else{
        	maskObj.style.filter = "Alpha(opacity=30);";
        	maskObj.style.opacity = "0.3";
        }
        maskObj.style.width = sWidth + "px";
        maskObj.style.height = sHeight + "px";
        maskObj.style.zIndex = "1000";
        $("body").attr("scroll", "no");
        document.body.appendChild(maskObj);
        $("#BigDiv").html("<IFRAME width=100%; height=100%; style=\"position:absolute;top:0px;left:0px;z-index:-1;border:none ;filter:Alpha(opacity=1)\" frameborder=1></IFRAME>");
        //$("#BigDiv").bgiframe()
        $("#BigDiv").data("divbox_selectlist", $("select:visible"));
        $("select:visible").hide();
        $("#BigDiv").attr("divbox_scrolltop", $.ScrollPosition().Top);
        $("#BigDiv").attr("divbox_scrollleft", $.ScrollPosition().Left);
        $("#BigDiv").attr("htmloverflow", $("html").css("overflow"));
        $("html").css("overflow", "hidden");
        window.scrollTo($("#BigDiv").attr("divbox_scrollleft"), $("#BigDiv").attr("divbox_scrolltop"));
        var MyDiv_w = this.width();
        var MyDiv_h = this.height();
        MyDiv_w = parseInt(MyDiv_w);
        MyDiv_h = parseInt(MyDiv_h);
        var width = $.PageSize().Width;
        var height = $.PageSize().Height;
        var left = $.ScrollPosition().Left;
        var top = $.ScrollPosition().Top;
        var Div_topposition = top + (height / 2) - (MyDiv_h / 2);
        var Div_leftposition = left + (width / 2) - (MyDiv_w / 2);
        this.css("position", "absolute");
        this.css("z-index", "1300");
        this.css("background", "#fff");
        this.css("left", Div_leftposition + "px");
        this.css("top", "20px");
        if ($.browser.mozilla){
            this.show();
            return;
        }
        this.fadeIn("fast");
    }
    , CloseDiv: function(){
        if ($.browser.mozilla){
            this.hide();
        } else{
            this.fadeOut("fast");
        } $("html").css("overflow", $("#BigDiv").attr("htmloverflow"));
        window.scrollTo($("#BigDiv").attr("divbox_scrollleft"), $("#BigDiv").attr("divbox_scrolltop"));
        $("#BigDiv").data("divbox_selectlist").show();
        $("#BigDiv").remove();
    }
});
$.extend({
    PageSize:function (){
        var width=0;
        var height=0;
        width=window.innerWidth!=null?window.innerWidth:document.documentElement&&document.documentElement.clientWidth?document.documentElement.clientWidth:document.body!=null?document.body.clientWidth:null;
        height=window.innerHeight!=null?window.innerHeight:document.documentElement&&document.documentElement.clientHeight?document.documentElement.clientHeight:document.body!=null?document.body.clientHeight:null;
        return {Width:width,Height:height};
    }
    ,ScrollPosition:function (){
        var top=0,left=0;
        if($.browser.mozilla){
            top=window.pageYOffset;
            left=window.pageXOffset;  
        }else if($.browser.msie){
            top=document.documentElement.scrollTop;
            left=document.documentElement.scrollLeft;
        }
        else if(document.body){
            top=document.body.scrollTop;
            left=document.body.scrollLeft;
        }
        return {Top:top,Left:left};
    }
});

