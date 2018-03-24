// Download by http://www.codefans.net
var YAO = function(){
	var D = document, OP = Object.prototype, OA = 'object Array', OF = '[object Function]', nt = 'nodeType', reCache = {}, patterns = {
		HYPHEN: /(-[a-z])/i,
		ROOT_TAG: /body|html/i,
		CLASS_RE_TOKENS: /([\.\(\)\^\$\*\+\?\|\[\]\{\}])/g
	}, CUSTOM_ATTRIBUTES = (!document.documentElement.hasAttribute) ? { // IE < 8
		'for': 'htmlFor',
		'class': 'className'
	} : { // w3c
		'htmlFor': 'for',
		'className': 'class'
	}, _CLASS = 'class', CLASS_NAME = 'className', EMPTY = '', SPACE = ' ', C_START = '(?:^|\\s)', C_END = '(?= |$)', _G = 'g';
	
	return {
		isArray: function(obj){
			return OP.toString.apply(obj) === OA;
		},
		isString: function(s){
			return typeof s === 'string';
		},
		isFunction: function(fn){
			return OP.toString.apply(fn) === OF;
		},
		isIE: function(){
			var A = navigator.userAgent.match(/MSIE\s([^;]*)/);
			if (A && A[1]) {
				return parseFloat(A[1]);
			}
			else {
				return false;
			}
		}(),
		
		trim: function(str){
			try {
				return str.replace(/^\s+|\s+$/g, '');
			} 
			catch (a) {
				return str;
			}
		},
		toCamel: function(property){
			if (!patterns.HYPHEN.test(property)) {
				return property;
			}
			if (propertyCache[property]) {
				return propertyCache[property];
			}
			var converted = property;
			while (patterns.HYPHEN.exec(converted)) {
				converted = converted.replace(RegExp.$1, RegExp.$1.substr(1).toUpperCase());
			}
			propertyCache[property] = converted;
			return converted;
		},
		
		getEl: function(elem){
			var elemID, E, m, i, k, length, len;
			if (elem) {
				if (elem[nt] || elem.item) {
					return elem;
				}
				if (this.isString(elem)) {
					elemID = elem;
					elem = D.getElementById(elem);
					if (elem && elem.id === elemID) {
						return elem;
					}
					else {
						if (elem && elem.all) {
							elem = null;
							E = D.all[elemID];
							for (i = 0, len = E.length; i < len; i += 1) {
								if (E[i].id === elemID) {
									return E[i];
								}
							}
						}
					}
					return elem;
				}
				else {
					if (elem.DOM_EVENTS) {
						elem = elem.get("element");
					}
					else {
						if (this.isArray(elem)) {
							m = [];
							for (k = 0, length = elem.length; k < length; k += 1) {
								m[m.length] = this.getEl(elem[k]);
							}
							return m;
						}
					}
				}
			}
			return null;
		},
		getStyle: function(el, property){
			if (D.defaultView && D.defaultView.getComputedStyle) {
				var value = null;
				if (property == 'float') {
					property = 'cssFloat';
				}
				var computed = D.defaultView.getComputedStyle(el, '');
				if (computed) {
					value = computed[this.toCamel(property)];
				}
				return el.style[property] || value;
			}
			else {
				if (D.documentElement.currentStyle && this.isIE) {
					switch (this.toCamel(property)) {
						case 'opacity':
							var val = 100;
							try {
								val = el.filters['DXImageTransform.Microsoft.Alpha'].opacity;
							} 
							catch (e) {
								try {
									val = el.filters('alpha').opacity;
								} 
								catch (e) {
								}
							}
							return val / 100;
							break;
						case 'float':
							property = 'styleFloat';
						default:
							var value = el.currentStyle ? el.currentStyle[property] : null;
							return (el.style[property] || value);
					}
				}
				else {
					return el.style[property];
				}
			}
		},
		setStyle: function(el, property, val){
			if (this.isIE) {
				switch (property) {
					case 'opacity':
						if (this.isString(el.style.filter)) {
							el.style.filter = 'alpha(opacity=' + val * 100 + ')';
							if (!el.currentStyle || !el.currentStyle.hasLayout) {
								el.style.zoom = 1;
							}
						}
						break;
					case 'float':
						property = 'styleFloat';
					default:
						el.style[property] = val;
				}
			}
			else {
				if (property === 'float') {
					property = 'cssFloat';
				}
				el.style[property] = val;
			}
		},
		setStyles: function(el, propertys){
			for (var p in propertys) {
				this.setStyle(el, p, propertys[p]);
			}
			return el;
		},
		setOpacity: function(el, val){
			this.setStyle(el, 'opacity', val);
		},
		hasClass: function(el, className){
			var ret = false, current;
			
			if (el && className) {
				current = this.getAttribute(el, CLASS_NAME) || EMPTY;
				if (className.exec) {
					ret = className.test(current);
				}
				else {
					ret = className && (SPACE + current + SPACE).indexOf(SPACE + className + SPACE) > -1;
				}
			}
			else {
				throw new Error('hasClass called with invalid arguments');
			}
			
			return ret;
		},
		addClass: function(el, className){
			var ret = false, current;
			
			if (el && className) {
				current = this.getAttribute(el, CLASS_NAME) || EMPTY;
				if (!this.hasClass(el, className)) {
					this.setAttribute(el, CLASS_NAME, this.trim(current + SPACE + className));
					ret = true;
				}
			}
			else {
				throw new Error('addClass called with invalid arguments');
			}
			
			return ret;
		},
		removeClass: function(el, className){
			var ret = false, current, newClass, attr;
			
			if (el && className) {
				current = this.getAttribute(el, CLASS_NAME) || EMPTY;
				this.setAttribute(el, CLASS_NAME, current.replace(this.getClassRegex(className), EMPTY));
				
				newClass = this.getAttribute(el, CLASS_NAME);
				if (current !== newClass) { // else nothing changed
					this.setAttribute(el, CLASS_NAME, this.trim(newClass)); // trim after comparing to current class
					ret = true;
					
					if (this.getAttribute(el, CLASS_NAME) === '') { // remove class attribute if empty
						attr = (el.hasAttribute && el.hasAttribute(_CLASS)) ? _CLASS : CLASS_NAME;
						el.removeAttribute(attr);
					}
				}
			}
			else {
				throw new Error('removeClass called with invalid arguments');
			}
			
			return ret;
		},
		replaceClass: function(el, classObj){
			var className, from, to, ret = false, current;
			
			if (el && classObj) {
				from = classObj.from;
				to = classObj.to;
				
				if (!to) {
					ret = false;
				}
				else {
					if (!from) { // just add if no "from"
						ret = this.addClass(el, classObj.to);
					}
					else {
						if (from !== to) { // else nothing to replace
							// May need to lead with DBLSPACE?
							current = this.getAttribute(el, CLASS_NAME) || EMPTY;
							className = (SPACE + current.replace(this.getClassRegex(from), SPACE + to)).split(this.getClassRegex(to));
							
							// insert to into what would have been the first occurrence slot
							className.splice(1, 0, SPACE + to);
							this.setAttribute(el, CLASS_NAME, this.trim(className.join(EMPTY)));
							ret = true;
						}
					}
				}
			}
			else {
				throw new Error('replaceClass called with invalid arguments');
			}
			
			return ret;
		},
		setAttribute: function(el, attr, val){
			attr = CUSTOM_ATTRIBUTES[attr] || attr;
			el.setAttribute(attr, val);
		},
		getAttribute: function(el, attr){
			attr = CUSTOM_ATTRIBUTES[attr] || attr;
			return el.getAttribute(attr);
		},
		getClassRegex: function(className){
			var re;
			if (className !== undefined) { // allow empty string to pass
				if (className.exec) { // already a RegExp
					re = className;
				}
				else {
					re = reCache[className];
					if (!re) {
						// escape special chars (".", "[", etc.)
						className = className.replace(patterns.CLASS_RE_TOKENS, '\\$1');
						re = reCache[className] = new RegExp(C_START + className + C_END, _G);
					}
				}
			}
			return re;
		},
		getElByClassName: function(className, tag, rootId){
			var elems = [], i, tempCnt = this.getEl(rootId).getElementsByTagName(tag), len = tempCnt.length;
			for (i = 0; i < len; ++i) {
				if (this.hasClass(tempCnt[i], className)) {
					elems.push(tempCnt[i]);
				}
			}
			if (elems.length < 1) {
				return false;
			}
			else {
				return elems;
			}
		},
		fadeUp: function(elem){
			if (elem) {
				var level = 0, fade = function(){
					var timer = null;
					level += 0.05;
					if (timer) {
						clearTimeout(timer);
						timer = null;
					}
					if (level > 1) {
						YAO.setOpacity(elem, 1);
						return false;
					}
					else {
						YAO.setOpacity(elem, level);
					}
					timer = setTimeout(fade, 50);
				};
				fade();
			}
		},
		moveElement: function(element, finalX, finalY, speed){
			var elem = this.isString(element) ? this.getEl(element) : element, style = null, xpos = 0, ypos = 0, dist = 0, that = this;
			if (elem) {
				if (elem.movement) {
					clearTimeout(elem.movement);
				}
				if (!this.getStyle(elem, 'left')) {
					this.setStyle(elem, 'left', 0);
				}
				if (!this.getStyle(elem, 'top')) {
					this.setStyle(elem, 'top', 0);
				}
				xpos = parseInt(this.getStyle(elem, 'left'), 10);
				ypos = parseInt(this.getStyle(elem, 'top'), 10);
				if (xpos === finalX && ypos === finalY) {
					return true;
				}
				if (xpos < finalX) {
					dist = Math.ceil((finalX - xpos) / 10);
					xpos += dist;
				}
				else {
					if (xpos > finalX) {
						dist = Math.ceil((xpos - finalX) / 10);
						xpos -= dist;
					}
				}
				if (ypos < finalY) {
					dist = Math.ceil((finalY - ypos) / 10);
					ypos += dist;
				}
				else {
					if (ypos > finalY) {
						dist = Math.ceil((ypos - finalY) / 10);
						ypos -= dist;
					}
				}
				this.setStyle(elem, 'left', (xpos + 'px'));
				this.setStyle(elem, 'top', (ypos + 'px'));
				elem.movement = setTimeout(function(){
					that.moveElement(element, finalX, finalY, speed);
				}, speed);
			}
		},
		
		getEvent: function(e, boundEl){
			var ev = e || window.event;
			
			if (!ev) {
				var c = this.getEvent.caller;
				while (c) {
					ev = c.arguments[0];
					if (ev && Event == ev.constructor) {
						break;
					}
					c = c.caller;
				}
			}
			
			return ev;
		},
		on: function(el, sType, fn, obj, overrideContext, bCapture){
			var oEl = null, context = null, wrappedFn = null, that = this;
			if (this.isString(el)) {
				el = this.getEl(el);
			}
			if (!el || !fn || !fn.call) {
				return false;
			}
			bCapture = bCapture || false;
			context = el;
			if (overrideContext) {
				if (overrideContext === true) {
					context = obj;
				}
				else {
					context = overrideContext;
				}
			}
			wrappedFn = function(e){
				return fn.call(context, that.getEvent(e, el), obj);
			};
			try {
				if (window.addEventListener) {
					el.addEventListener(sType, wrappedFn, bCapture);
				}
				else {
					if (window.attachEvent) {
						el.attachEvent('on' + sType, wrappedFn);
					}
					else {
						el['on' + sType] = wrappedFn;
					}
				}
			} 
			catch (e) {
				return false;
			}
		},
		stopEvent: function(evt){
			this.stopPropagation(evt);
			this.preventDefault(evt);
		},
		stopPropagation: function(evt){
			if (evt.stopPropagation) {
				evt.stopPropagation();
			}
			else {
				evt.cancelBubble = true;
			}
		},
		preventDefault: function(evt){
			if (evt.preventDefault) {
				evt.preventDefault();
			}
			else {
				evt.returnValue = false;
			}
		},
		
		ajaxRequest: function(config){
			var that = this, option = {
				oXhr: null,
				method: config.method ? config.method.toUpperCase() : 'GET',
				url: config.url || '',
				fn: config.fn || null,
				postData: config.data || null,
				elem: config.id ? that.getEl(config.id) : (config.element || null),
				load: config.loadFn ? config.loadFn : (config.loading || '正在获取数据，请稍后...')
			};
			
			if (!option.url) {
				return;
			}
			
			if (window.XMLHttpRequest) {
				option.oXhr = new XMLHttpRequest();
			}
			else {
				if (window.ActiveXObject) {
					option.oXhr = new ActiveXObject("Microsoft.XMLHTTP");
				}
			}
			
			if (option.oXhr) {
				try {
					option.oXhr.open(option.method, option.url, true);
					option.oXhr.onreadystatechange = function(){
						if (option.oXhr.readyState !== 4) {
							return false
						}
						if (option.oXhr.readyState == 4) {
							if (option.oXhr.status == 200 || location.href.indexOf('http') === -1) {
								if (option.fn) {
									option.fn.success(option.oXhr);
								}
								else {
									if (that.isFunction(option.load)) {
										option.load(option.oXhr);
									}
									else {
										option.elem.innerHTML = option.oXhr.responseText;
									}
								}
							}
							else {
								if (option.fn) {
									option.fn.failure(option.oXhr);
								}
								else {
									if (that.isFunction(option.load)) {
										option.load(option.oXhr);
									}
									else {
										option.elem.innerHTML = option.load;
									}
								}
							}
						}
					};
					option.oXhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
					if (option.postData) {
						option.oXhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
					}
					option.oXhr.send(option.postData);
				} 
				catch (e) {
					throw new Error(e);
					return false;
				}
			}
			else {
				throw new Error("Your browser does not support XMLHTTP.");
				return false;
			}
		},
		
		JSON: function(){
			function f(n){
				return n < 10 ? '0' + n : n;
			}
			
			Date.prototype.toJSON = function(){
				return this.getUTCFullYear() + '-' + f(this.getUTCMonth() + 1) + '-' + f(this.getUTCDate()) + 'T' + f(this.getUTCHours()) + ':' + f(this.getUTCMinutes()) + ':' + f(this.getUTCSeconds()) + 'Z';
			};
			
			var m = {
				'\b': '\\b',
				'\t': '\\t',
				'\n': '\\n',
				'\f': '\\f',
				'\r': '\\r',
				'"': '\\"',
				'\\': '\\\\'
			};
			
			function stringify(value, whitelist){
				var a, i, k, l, r = /["\\\x00-\x1f\x7f-\x9f]/g, v;
				switch (typeof value) {
					case 'string':
						return r.test(value) ? '"' +
						value.replace(r, function(a){
							var c = m[a];
							if (c) {
								return c;
							}
							c = a.charCodeAt();
							return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
						}) +
						'"' : '"' + value + '"';
					case 'number':
						return isFinite(value) ? String(value) : 'null';
					case 'boolean':
					case 'null':
						return String(value);
					case 'object':
						if (!value) {
							return 'null';
						}
						
						if (typeof value.toJSON === 'function') {
							return stringify(value.toJSON());
						}
						a = [];
						if (typeof value.length === 'number' && !(value.propertyIsEnumerable('length'))) {
						
							l = value.length;
							for (i = 0; i < l; i += 1) {
								a.push(stringify(value[i], whitelist) || 'null');
							}
							
							return '[' + a.join(',') + ']';
						}
						if (whitelist) {
							l = whitelist.length;
							for (i = 0; i < l; i += 1) {
								k = whitelist[i];
								if (typeof k === 'string') {
									v = stringify(value[k], whitelist);
									if (v) {
										a.push(stringify(k) + ':' + v);
									}
								}
							}
						}
						else {
							for (k in value) {
								if (typeof k === 'string') {
									v = stringify(value[k], whitelist);
									if (v) {
										a.push(stringify(k) + ':' + v);
									}
								}
							}
						}
						return '{' + a.join(',') + '}';
				}
			}
			
			return {
				stringify: stringify,
				parse: function(text, filter){
					var j;
					
					function walk(k, v){
						var i, n;
						if (v && typeof v === 'object') {
							for (i in v) {
								if (OP.hasOwnProperty.apply(v, [i])) {
									n = walk(i, v[i]);
									if (n !== undefined) {
										v[i] = n;
									}
									else {
										delete v[i];
									}
								}
							}
						}
						return filter(k, v);
					}
					
					if (/^[\],:{}\s]*$/.test(text.replace(/\\./g, '@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
						j = eval('(' + text + ')');
						
						return typeof filter === 'function' ? walk('', j) : j;
					}
					
					throw new SyntaxError('parseJSON');
				}
			};
		}(),
		
		YTabs: function(){
			var i, len = arguments.length, tabs = [];
			for (i = 0; i < len; ++i) {
				tabs[i] = new tabView(arguments[i]);
				tabs[i].init();
			}
			return tabs;
		}
	}
}();

var tabView = function(oConfigs){
	this.tabRoot = YAO.isString(oConfigs.tabRoot) ? YAO.getEl(oConfigs.tabRoot) : (oConfigs.tabRoot || null);
	this.tabs = YAO.isString(oConfigs.tabs) ? this.tabRoot.getElementsByTagName(oConfigs.tabs) : (oConfigs.tabs || null);
	this.contents = YAO.isString(oConfigs.contents) ? this.tabRoot.getElementsByTagName(oConfigs.contents) : (oConfigs.contents || null);
	
	if(!this.tabs || !this.contents){
		return false;
	}
	
	this.length = this.tabs.length || 0;
	this.defaultIndex = oConfigs.defaultIndex || 0;
	this.lastIndex = this.defaultIndex;
	this.lastTab = this.tabs[this.lastIndex] || null;
	this.lastContent = this.contents[this.lastIndex] || null;
	this.evtName = oConfigs.evt || 'mouseover';
	this.defaultClass = oConfigs.defaultClass || this.CURRENT_TAB_CLASS;
	this.previousClass = oConfigs.previousClass || '';
	this.hideAll = oConfigs.hideAll || false;
	this.auto = oConfigs.auto || false;
	this.autoSpeed = oConfigs.autoSpeed || 6000;
	this.fadeUp = oConfigs.fadeUp || false;
	this.scroll = oConfigs.scroll || false;
	this.scrollId = oConfigs.scrollId || null;
	this.scrollSpeed = oConfigs.scrollSpeed || 5;
    this.direction = oConfigs.direction || 'V';
	this.activeTag = oConfigs.activeTag || 'IMG';
	this.stepHeight = oConfigs.stepHeight || 0;
	this.stepWidth = oConfigs.stepWidth || 0;
	this.ajax = oConfigs.ajax || false;
	this.ajaxDefaultInfo = this.contents.innerHTML;
	this.aPath = oConfigs.aPath || '';
};
tabView.prototype.timer = null;
tabView.prototype.isPause = false;
tabView.prototype.CURRENT_TAB_CLASS = 'current', 

tabView.prototype.init = function(){
	var i, that = this;
	if (this.tabs && this.contents) {
		if (this.auto) {
			this.timer = setTimeout(function(){
				that.autoChange();
			}, that.autoSpeed);
		}
		if (!this.hideAll) {
			YAO.addClass(this.lastTab, this.defaultClass);
			if (!this.ajax && !this.scroll) {
				if (this.lastContent) {
					this.lastContent.style.display = 'block';
				}
			}
			if (this.ajax) {
				this.ajaxTab(this.lastTab);
			}
			if (this.scroll) {
				this.scrollCnt((this.lastContent || this.contents), this.defaultIndex);
			}
		}
		else {
			YAO.removeClass(this.lastTab, this.defaultClass);
		}
		for (i = 0; i < this.length; ++i) {
			if (i !== this.defaultIndex) {
				YAO.removeClass(this.tabs[i], this.CURRENT_TAB_CLASS);
				if (!this.ajax && !this.scroll) {
					this.contents[i].style.display = 'none';
				}
			}
			YAO.on(this.tabs[i], this.evtName, function(index){
				return function(event){
					var evt = null, curClass = (this.tabs[index] === this.tabs[this.defaultIndex]) ? this.defaultClass : this.CURRENT_TAB_CLASS;
					if (!YAO.hasClass(this.tabs[index], curClass)) {
						var currentContent = (this.ajax || (this.scroll && (this.stepHeight || this.stepWidth))) ? this.contents : (this.contents[index] || null);
						
						this.setCurrent(currentContent, index);
						this.lastIndex = index;
					}
					if (this.auto) {
						this.isPause = true;
					}
					evt = event || window.event;
					YAO.stopEvent(evt);
				}
			}(i), this.tabs[i], that);
			YAO.on(this.tabs[i], 'mouseout', function(index){
				return function(){
					var curTab = this.tabs[index];
					if (this.hideAll && this.evtName === 'mouseover') {
						if (this.lastTab === curTab) {
							YAO.removeClass(curTab, (YAO.hasClass(curTab, that.defaultClass) ? this.defaultClass : this.CURRENT_TAB_CLASS));
						}
						if (this.previousClassTab) {
							YAO.removeClass(this.previousClassTab, this.previousClass);
						}
						if (!this.scroll && !this.ajax) {
							this.contents[index].style.display = 'none';
						}
					}
					else {
						if (this.auto) {
							this.isPause = false;
						}
					}
				}
			}(i), this.tabs[i], that);
		}
	}
};
tabView.prototype.autoChange = function(){
	var that = this;
	if (!this.isPause) {
		var currentContent = null, currentTab = null;
		if (this.timer) {
			clearTimeout(this.timer);
			this.timer = null;
		}
		this.lastIndex = this.lastIndex + 1;
		if (this.lastIndex === this.length) {
			this.lastIndex = 0;
		}
		currentContent = this.ajax ? this.contents : (this.contents[this.lastIndex] || null);
		this.setCurrent(currentContent, this.lastIndex);
		this.timer = setTimeout(function(){
			that.autoChange();
		}, this.autoSpeed);
	}
	else {
		this.timer = setTimeout(function(){
			that.autoChange()
		}, this.autoSpeed);
		return false;
	}
};
tabView.prototype.setCurrent = function(curCnt, index){
	var activeObj = null;
	curTab = this.tabs[index];
	YAO.removeClass(this.lastTab, (YAO.hasClass(this.lastTab, this.defaultClass) ? this.defaultClass : this.CURRENT_TAB_CLASS));
	if (curTab === this.tabs[this.defaultIndex]) {
		YAO.addClass(curTab, this.defaultClass);
	}
	else {
		YAO.addClass(curTab, this.CURRENT_TAB_CLASS);
	}
	if (this.previousClass) {
		if (this.previousClassTab) {
			YAO.removeClass(this.previousClassTab, this.previousClass);
		}
		if (index !== 0) {
			YAO.addClass(this.tabs[index - 1], this.previousClass);
			if ((index - 1) === this.defaultIndex) {
				YAO.removeClass(this.tabs[index - 1], this.defaultClass);
			}
			this.previousClassTab = (this.tabs[index - 1]);
		}
	}
	if (!this.scroll && !this.ajax) {
		if (this.lastContent) {
			this.lastContent.style.display = "none";
		}
		if (curCnt) {
			curCnt.style.display = "block";
		}
	}
	
	if (this.fadeUp) {
		activeObj = (curCnt.tagName.toUpperCase() === 'IMG') ? curCnt : curCnt.getElementsByTagName('img')[0];
		if (this.lastContent !== curCnt) {
			YAO.fadeUp(activeObj);
		}
	}
	else {
		if (this.scroll) {
			this.scrollCnt(curCnt, index);
		}
	}
	if (!this.ajax) {
		this.lastContent = curCnt;
	}
	else {
		if (this.ajax) {
			this.ajaxTab(curTab);
		}
	}
	this.lastTab = curTab;
};
tabView.prototype.scrollCnt = function(curCnt, index){
	var activeObj = null, itemHeight = 0, itemWidth = 0, scrollWidth = 0, scrollHeight = 0;
	if (this.activeTag) {
		activeObj = (curCnt.tagName.toUpperCase() === this.activeTag) ? curCnt : curCnt.getElementsByTagName(this.activeTag)[0];
	}
	if (this.direction === 'V') {
		itemHeight = activeObj ? activeObj.offsetHeight : this.stepHeight;
		scrollHeight = -(index * itemHeight);
	}
	else {
		itemWidth = activeObj ? activeObj.offsetWidth : this.stepWidth;
		scrollWidth = -(index * itemWidth);
	}
	YAO.moveElement(this.scrollId, scrollWidth, scrollHeight, this.scrollSpeed);
};
tabView.prototype.ajaxTab = function(curTab){
	var that = this, url = '', ajaxLink = null, loadFn = null, cnt = this.contents, uriData = this.aPath.split('/'), json = (uriData[1].toLowerCase() === '.js' || uriData[1].toLowerCase() === 'json') ? true : false;
	ajaxLink = (curTab.tagName.toUpperCase() === 'A') ? curTab : curTab.getElementsByTagName('a')[0];
	url = uriData[0] + '/' + ajaxLink.rel + uriData[1] + uriData[2] + ajaxLink.rel;
	
	if (curTab === this.tabs[this.defaultIndex]) {
		cnt.innerHTML = this.ajaxDefaultInfo;
	}
	else {
		if(json){
			loadFn = function(jsonData){
				var data = YAO.JSON.parse(jsonData.responseText);
				cnt.innerHTML = data.html;
			}
		}
		else{
			loadFn = cnt.innerHTML;
		}
		YAO.ajaxRequest({
			url: url,
			element: cnt,
			loadFn: loadFn
		});
	}
};

YAO.Carousel = function(oConfig){
	this.btnPrevious = YAO.isString(oConfig.btnPrevious) ? YAO.getEl(oConfig.btnPrevious) : (oConfig.btnPrevious || null);
	this.lnkBtnPrevious = this.btnPrevious.getElementsByTagName('a')[0] || null;
	this.Container = YAO.isString(oConfig.Container) ? YAO.getEl(oConfig.Container) : (oConfig.Container || null);
	this.Scroller = YAO.isString(oConfig.Scroller) ? YAO.getEl(oConfig.Scroller) : (oConfig.Scroller || null);
	this.btnNext = YAO.isString(oConfig.btnNext) ? YAO.getEl(oConfig.btnNext) : (oConfig.btnNext || null);
	this.lnkBtnNext = this.btnNext.getElementsByTagName('a')[0] || null;
	this.items = YAO.isString(oConfig.items) ? this.Container.getElementsByTagName(oConfig.items) : (oConfig.items || null);
	
	if(!this.btnPrevious || !this.lnkBtnPrevious || !this.Container || !this.Scroller || !this.btnNext || !this.lnkBtnNext || !this.items){
		return false;
	}
	
	this.length = this.items.length;
	this.itemWidth = this.items[0].offsetWidth;
	this.itemHeight = this.items[0].offsetHeight;
	this.scrollerWidth = this.itemWidth * this.length;
	this.scrollHeight = this.itemHeight * this.length;
	this.derection = oConfig.derection || 'H';
	this.stepHeight = oConfig.stepHeight || this.itemHeight;
	this.stepWidth = oConfig.stepWidth || this.itemWidth;
	this.groups = this.derection === 'H' ? Math.ceil(this.scrollerWidth / this.stepWidth) : Math.ceil(this.scrollHeight / this.stepHeight);
	this.maxMovedNum = this.derection === 'H' ? (this.groups - (this.Container.offsetWidth / this.stepWidth)) : (this.groups - (this.Container.offsetHeight / this.stepHeight));
	this.scrollSpeed = oConfig.speed || 50;
	
	this.init();
};
YAO.Carousel.prototype.movedNum = 0;
YAO.Carousel.prototype.LNK_BTNS_DISABLED_CLASS = 'dis'; 
YAO.Carousel.prototype.init = function(){
	var oSelf = this;
	if (this.derection === 'H') {
		this.Scroller.style.width = this.scrollerWidth + 'px';
	}
	else {
		this.Scroller.style.height = this.scrollerHeight + 'px';
	}
	this.Container.style.overflow = 'hidden';
	if (this.lnkBtnNext && this.movedNum === this.maxMovedNum) {
		YAO.addClass(this.lnkBtnNext, this.LNK_BTNS_DISABLED_CLASS);
	}
	if (this.lnkBtnPrevious && this.movedNum === 0) {
		YAO.addClass(this.lnkBtnPrevious, this.LNK_BTNS_DISABLED_CLASS);
	}
	YAO.on(this.btnPrevious, 'click', this.scrollPrevious, this.btnPrevious, oSelf);
	YAO.on(this.btnNext, 'click', this.scrollNext, this.btnNext, oSelf);
};
YAO.Carousel.prototype.scrollPrevious = function(event){
	var evt = event || window.event;
	if (this.movedNum > 0) {
		this.movedNum -= 1;
		if (this.lnkBtnNext && YAO.hasClass(this.lnkBtnNext, this.LNK_BTNS_DISABLED_CLASS)) {
			YAO.removeClass(this.lnkBtnNext, this.LNK_BTNS_DISABLED_CLASS);
		}
		if (this.movedNum <= 0) {
			this.movedNum = 0;
			if (this.lnkBtnPrevious) {
				YAO.addClass(this.lnkBtnPrevious, this.LNK_BTNS_DISABLED_CLASS);
			}
		}
		this.scroll(this.movedNum);
	}
	YAO.stopEvent(evt);
};
YAO.Carousel.prototype.scrollNext = function(event){
	var evt = event || window.event;
	if (this.movedNum < this.maxMovedNum) {
		this.movedNum += 1;
		if (this.lnkBtnPrevious && YAO.hasClass(this.lnkBtnPrevious, this.LNK_BTNS_DISABLED_CLASS)) {
			YAO.removeClass(this.lnkBtnPrevious, this.LNK_BTNS_DISABLED_CLASS);
		}
		if (this.movedNum >= this.maxMovedNum) {
			this.movedNum = this.maxMovedNum;
			if (this.lnkBtnNext) {
				YAO.addClass(this.lnkBtnNext, this.LNK_BTNS_DISABLED_CLASS);
			}
		}
		this.scroll(this.movedNum);
	}
	YAO.stopEvent(evt);
}; 
YAO.Carousel.prototype.scroll = function(steps){
	var scrollWidth = 0, scrollHeight = 0;
	if (this.derection === 'H') {
		if (this.stepWidth) {
			scrollWidth = -(this.stepWidth * steps);
		}
		else {
			scrollWidth = -(this.itemWidth * steps);
		}
	}
	else {
		if (this.stepHeight) {
			scrollHeight = -(this.stepHeight * steps);
		}
		else {
			scrollHeight = -(this.itemHeight * steps);
		}
	}
	YAO.moveElement(this.Scroller, scrollWidth, scrollHeight, this.scrollSpeed);
};