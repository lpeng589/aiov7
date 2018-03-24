﻿addEvent(window, "load", sortables_init);
var SORT_COLUMN_INDEX;
function sortables_init() {
// Find all tables with class sortable and make them sortable
if (!document.getElementsByTagName) return;
tbls = document.getElementsByTagName("table");
for (ti=0;ti<tbls.length;ti++) {
thisTbl = tbls[ti];
if (((' '+thisTbl.className+' ').indexOf("listRange_list_function") != -1) && (thisTbl.id)) {
//initTable(thisTbl.id);
ts_makeSortable(thisTbl);
}
}
}
function ts_makeSortable(table) {
if (table.rows && table.rows.length > 0) {
var firstRow = table.rows[0];
}
if (!firstRow) return;
// We have a first row: assume it's the header, and make its contents clickable links
for (var i=0;i<firstRow.cells.length;i++) {
var cell = firstRow.cells[i];
//var txt = ts_getInnerText(cell);
var txt = ts_getInnerHTML(cell);
cell.innerHTML ='<div class="resizeDiv" onmousedown="MouseDownToResize(this);" onmousemove="MouseMoveToResize(this,'+i+');" onmouseup="MouseUpToResize(this);"></div>'+ 
txt+'<span class="sortarrow"></span>';
}
}
function ts_getInnerText(el) {
if (typeof el == "string") return el;
if (typeof el == "undefined") { return el };
if (el.innerText) return el.innerText;	//Not needed but it is faster
var str = "";
var cs = el.childNodes;
var l = cs.length;
for (var i = 0; i < l; i++) {
switch (cs[i].nodeType) {
case 1: //ELEMENT_NODE
str += ts_getInnerText(cs[i]);
break;
case 3:	//TEXT_NODE
str += cs[i].nodeValue;
break;
}
}
return str;
}
function ts_getInnerHTML(el) {
if (typeof el == "string") return el;
if (typeof el == "undefined") { return el };
if (el.innerHTML) return el.innerHTML;	//Not needed but it is faster
var str = "";
var cs = el.childNodes;
var l = cs.length;
for (var i = 0; i < l; i++) {
switch (cs[i].nodeType) {
case 1: //ELEMENT_NODE
str += ts_getInnerHTML(cs[i]);
break;
case 3:	//TEXT_NODE
str += cs[i].nodeValue;
break;
}
}
return str;
}

function getParent(el, pTagName) {
if (el == null) return null;
else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())	// Gecko bug, supposed to be uppercase
return el;
else
return getParent(el.parentNode, pTagName);
}
function ts_sort_date(a,b) {
// y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
if (aa.length == 10) {
dt1 = aa.substr(6,4)+aa.substr(3,2)+aa.substr(0,2);
} else {
yr = aa.substr(6,2);
if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
dt1 = yr+aa.substr(3,2)+aa.substr(0,2);
}
if (bb.length == 10) {
dt2 = bb.substr(6,4)+bb.substr(3,2)+bb.substr(0,2);
} else {
yr = bb.substr(6,2);
if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
dt2 = yr+bb.substr(3,2)+bb.substr(0,2);
}
if (dt1==dt2) return 0;
if (dt1<dt2) return -1;
return 1;
}
function ts_sort_currency(a,b) {
aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
return parseFloat(aa) - parseFloat(bb);
}
function ts_sort_numeric(a,b) {
aa = parseFloat(ts_getInnerText(a.cells[SORT_COLUMN_INDEX]));
if (isNaN(aa)) aa = 0;
bb = parseFloat(ts_getInnerText(b.cells[SORT_COLUMN_INDEX]));
if (isNaN(bb)) bb = 0;
return aa-bb;
}
function ts_sort_caseinsensitive(a,b) {
aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
if (aa==bb) return 0;
if (aa<bb) return -1;
return 1;
}
function ts_sort_default(a,b) {
aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
if (aa==bb) return 0;
if (aa<bb) return -1;
return 1;
}
function addEvent(elm, evType, fn, useCapture)
// addEvent and removeEvent
// cross-browser event handling for IE5+,  NS6 and Mozilla
// By Scott Andrew
{
if (elm.addEventListener){
elm.addEventListener(evType, fn, useCapture);
return true;
} else if (elm.attachEvent){
var r = elm.attachEvent("on"+evType, fn);
return r;
} else {
alert("Handler could not be removed");
}
}
