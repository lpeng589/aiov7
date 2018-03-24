var KB = function(str){
	this.nod = new Array();
	return this.initShortCut(str);
}
KB.prototype={
	version:'1.0',
	initShortCut: function(str){
		str = str||document;
		if(str.nodeType ) {
			return this;
		} else if ( typeof str == "string" ) {
				this.proStr = str;
				this.nodParent = document;
				var tm = str.split(":");
				if(tm.length>1){
					for(var i=0; i<tm.length; i++){
						this.getNodes(tm[i]);
					}
					return this;
				}else{
					return this.getNodes(str);
				}
				
		}
	},
	getNodes:function(str){
		this.proStr = str;
		if(str.indexOf('=')!=-1){
			this.getId();	
		}else if(str.indexOf('@')!=-1){
			this.getCN();	
		}else if(str.indexOf('&')!=-1){
			this.getN();
		}else{
			this.getEN();	
		}
		this.nodParent = this.nod[1];
		typeof this.nodParent == "undefined"?this.nodParent = document:this.nodParent;
		return this;
	},
	getChild:function(c){
		var f = new Array();
		if(typeof this.nod[0]=='undefined')return f;
		var s = this.nod[0].getElementsByTagName(c);
		for(var i=0; i<s.length; i++){
			f.push(s[i]);
		}
		return f;
	},
	getParent:function(){
		var arr = new Array();
		for(var i=1; i<this.nod.length; i++){
			arr.push(this.nod[i].parentNode);	
		}
		return arr;
	},
	getId : function(){
		var a = this.proStr.split('=');
		this.nod[0] = this.nod[1] = this.nodParent.getElementById(a[1]);
		return this;
	},
	getCN : function(){
		var a = this.proStr.split('@');
		var s = this.nodParent.getElementsByTagName(a[0]);
		var ii = 1;
		for(var i=0; i<s.length; i++){
			if((' '+s[i].className+' ').indexOf(' '+a[1]+' ')!=-1){
				this.nod[ii] = s[i];
				ii++;
			}
		}
		this.nod[0] = this.nod[1];
		return this;
	},
	getN : function(){
		var a = this.proStr.split('&');
		var s = this.nodParent.getElementsByTagName(a[0]);
		var ii = 1;
		for(var i=0; i<s.length; i++){
			if(s.item(i).getAttribute(a[1])){
				this.nod[ii] = s[i];
				ii++;
			}
		}
		this.nod[0] = this.nod[1];
		return this;
	},
	getEN : function(){
		var s = this.nodParent.getElementsByTagName(this.proStr);
		for(var i=0; i<s.length; i++){
			this.nod[i+1] = s[i];	
		}
		this.nod[0] = this.nod[1];
		return this;	
	},
	getNod: function(val){
		var t = this.nod.length-1;
		if(val<0)val=0;
		if(val>=t){this.nod[0]=this.nod[t];return this;}
		this.nod[0] = this.nod[val];
		this.nodParent = this.nod[0];
		return this;
	},
	val : function(num){
		if(!num)num=0;
		if(this.nod.length>0){
			for(var i=0; i<this.nod.length; i++){
				if(i==num){
					return 	this.nod[i].innerHTML;
				}
			}
		}
		return null;
	},
	value:function(val){
		if(val){
			return this.nod[0].value = val;
		}else{
			return this.nod[0].value;	
		}
	},
	setVal:function(val){
		this.nod[0].innerHTML = val;
	},
	hidden : function(){ 
		if(arguments.length==0){
			for(var i=0; i<this.nod.length; i++){ 
				this.nod[i].style.display = 'none';
				if(this.nod[i].className=='shortcut')
					document.getElementById("shortBg").style.display = 'none';
			}
		}else{
			this.nod[arguments[0]].style.display = 'none';
			if(this.nod[i].className=='shortcut')
				document.getElementById("shortBg").style.display = 'none';
		}
	},
	show : function(){ 
		if(arguments.length==0){
			for(var i=0; i<this.nod.length; i++){
				this.nod[i].style.display = 'block';	
				if(this.nod[i].className=='shortcut')
					document.getElementById("shortBg").style.display = 'block';
							
			}
		}else{ 
			this.nod[arguments[0]].style.display = 'block';
			if(this.nod[i].className=='shortcut')
				document.getElementById("shortBg").style.display = 'block';
		}
	}
}
KB.prototype.play = function(val){
	if(val==''||val==null){val=0;}
	this.alpha(0,val);
	var nod = this.nod[val];
	var proOp = 0;
	(function(){
			  	if(proOp==10){return;}
				proOp++;
			  	nod.style.opacity = proOp/10;
				nod.style.filter = 'alpha(opacity='+ proOp*10 +')';
				setTimeout(arguments.callee,50);
			  })();
}


KB.reg = function(target, type, func){
		if (target.addEventListener){
        	target.addEventListener(type, func, false);
		}else if (target.attachEvent){
	        target.attachEvent("on" + type, func);
	    }else{ target["on" + type] = func;}
	}

KB.ready = function(func){ 
   if(!window.__load_events){
	   var initShortCut = function (){
          if (arguments.callee.done) return; 
          arguments.callee.done = true; 
          if (window.__load_timer) { 
              clearInterval(window.__load_timer); 
              window.__load_timer = null; 
          } 
          for (var i=0;i < window.__load_events.length;i++) { 
              window.__load_events[i](); 
          } 
          window.__load_events = null; 
      };
	  
      if (document.addEventListener) { 
          document.addEventListener("DOMContentLoaded", initShortCut, false); 
      }
	  if(window.attachEvent){ 
		document.attachEvent("onreadystatechange", function(){
			if ( document.readyState === "complete" ) {
				document.detachEvent( "onreadystatechange", arguments.callee );
				initShortCut();
			}
		});
	  }
      if (/WebKit/i.test(navigator.userAgent)) { 
          window.__load_timer = setInterval(function() { 
              if (/loaded|complete/.test(document.readyState)) { 
                  initShortCut();
              } 
          }, 10);
      } 
      window.onload = initShortCut; 
      window.__load_events = []; 
   }
   window.__load_events.push(func); 
};

var ShortCut = {
	shut:false,
	initShortCut:function(){
		KB.reg(ShortCut.cutBtn.nod[0],'mouseover',ShortCut.show);
		KB.reg(ShortCut.cut.nod[0],'mouseout',ShortCut.hidden);
		new KB('b=short_cu').nod[0].title = ShortCut.title1;
	 	ShortCut.hidden();
	},
	open:function(){
		if(!ShortCut.shut){
			ShortCut.cut.show();
			ShortCut.cutBtn.hidden();	
			ShortCut.shut = false;
		}
	},
	show:function(){
		ShortCut.shut = false;
		ShortCut.open();
	},
	hidden:function(){
		if(!ShortCut.shut){
			ShortCut.shut = true;
			ShortCut.close();
		}	
	},
	close:function(){
		if(ShortCut.autoCu){ShortCut.shut = false;return;}
		if(ShortCut.shut){
			ShortCut.cut.hidden();
			ShortCut.cutBtn.show();	
		}
	},
	autoCu:false,
	cu:function(){
		if(ShortCut.autoCu){
			ShortCut.autoCu = false;
			ShortCut.cuBtn.className="icon57";
			ShortCut.cuBtn.title = ShortCut.title1;
			document.getElementById("leftTd").style.width="0px";
			jQuery.get("/UtilServlet?operation=changeViewMenu&type=viewLeftMenu&value=2", function(data){});
		}else{
			ShortCut.autoCu = true;
			ShortCut.cuBtn.className="icon58";
			ShortCut.cuBtn.title = ShortCut.title2;
			document.getElementById("leftTd").style.width="178px";
			jQuery.get("/UtilServlet?operation=changeViewMenu&type=viewLeftMenu&value=1", function(data){});
		}
	},
	cu2:function(){
		if(ShortCut.autoCu){
			ShortCut.autoCu = false;
			ShortCut.cuBtn.className="icon57";
			ShortCut.cuBtn.title = ShortCut.title1;
			document.getElementById("leftTd").style.width="0px";
		}else{
			ShortCut.autoCu = true;
			ShortCut.cuBtn.className="icon58";
			ShortCut.cuBtn.title = ShortCut.title2;
			document.getElementById("leftTd").style.width="178px";
		}
	}
}

var ShortCutRight = {
	shut:false,
	initShortCut:function(){
		KB.reg(ShortCutRight.cutBtn.nod[0],'mouseover',ShortCutRight.show);
		KB.reg(ShortCutRight.cut.nod[0],'mouseout',ShortCutRight.hidden);
		new KB('b=short_cu').nod[0].title = ShortCutRight.title1;
	 	ShortCutRight.hidden();
	},
	open:function(){
		if(!ShortCutRight.shut){
			ShortCutRight.cut.show();
			ShortCutRight.cutBtn.hidden();	
			ShortCutRight.shut = false;
		}
	},
	show:function(){
		ShortCutRight.shut = false;
		ShortCutRight.open();
	},
	hidden:function(){
		if(!ShortCutRight.shut){
			ShortCutRight.shut = true;
			ShortCutRight.close();
		}	
	},
	close:function(){
		if(ShortCutRight.autoCu){ShortCutRight.shut = false;return;}
		if(ShortCutRight.shut){
			ShortCutRight.cut.hidden();
			ShortCutRight.cutBtn.show();	
		}
	},
	autoCu:false,
	cu:function(){
		if(ShortCutRight.autoCu){
			ShortCutRight.autoCu = false;
			ShortCutRight.cuBtn.className="icon57";
			ShortCutRight.cuBtn.title = ShortCutRight.title1;
		}else{
			ShortCutRight.autoCu = true;
			ShortCutRight.cuBtn.className="icon58";
			ShortCutRight.cuBtn.title = ShortCut.title2;
		}
	}
}
