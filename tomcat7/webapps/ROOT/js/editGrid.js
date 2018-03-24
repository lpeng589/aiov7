function addCols(griddata,name,display,width,fieldType,nullable,defaultValue,maxLength,inputType,inputValue,popupType,fieldIdentityStr,copyType,listOrder,groupName,inputTypeOld,relationKey){
	if(inputType==-2){
		width=0;
	}
	griddata.cols[griddata.cols.length] = new Cols(name,display,width,fieldType,nullable,defaultValue,maxLength,inputType,inputValue,popupType,fieldIdentityStr,copyType,listOrder,groupName,inputTypeOld,relationKey);	
}

function changeDV(griddata,name,defaultValue){
	
	if(typeof(griddata) != "undefined"){
		for(i=0;i<griddata.cols.length;i++){ 
			if(griddata.cols[i].name==name){
				griddata.cols[i].defaultValue = defaultValue;
				break;
			}
		}
	}
}

function changeDetailValue(name,defaultValue){
	var names = document.getElementsByName(name);
	if(names.length > 0){
		names[0].value = defaultValue;
	}
	
}

function addRows(griddata,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22,r23,r24,r25,r26,r27,
r28,r29,r30,r31,r32,r33,r34,r35,r36,r37,r38,r39,r40,r41,r42,r43,r44,r45,r46,r47,r48,r49,r50
,r51,r52,r53,r54,r55,r56,r57,r58,r59,r60,r61,r62,r63,r64,r65,r66,r67,r68,r69,r70
,r71,r72,r73,r74,r75,r76,r77,r78,r79,r80,r81,r82,r83,r84,r85,r86,r87,r88,r89,r90,r91,r92,r93,r94,r95,r96,r97,r98,r99,r100
,r101,r102,r103,r104,r105,r106,r107,r108,r109,r110,r111,r112,r113,r114,r115,r116,r117,r118,r119,r120
,r121,r122,r123,r124,r125,r126,r127,r128,r129,r130,r131,r132,r133,r134,r135,r136,r137,r138,r139,r140
,r141,r142,r143,r144,r145,r146,r147,r148,r149,r150,r151,r152,r153,r154,r155,r156,r157,r158,r159,r160
,r161,r162,r163,r164,r165,r166,r167,r168,r169,r170,r171,r172,r173,r174,r175,r176,r177,r178,r179,r180
,r181,r182,r183,r184,r185,r186,r187,r188,r189,r190,r191,r192,r193,r194,r195,r196,r197,r198,r199,r200
,r201,r202,r203,r204,r205,r206,r207,r208,r209,r210,r211,r212,r213,r214,r215,r216,r217,r218,r219,r220
,r221,r222,r223,r224,r225,r226,r227,r228,r229,r230,r231,r232,r233,r234,r235,r236,r237,r238,r239,r240
,r241,r242,r243,r244,r245,r246,r247,r248,r249,r250,r251,r252,r253,r254,r255,r256,r257,r258,r259,r260){
	if(r1.indexOf("@koron@")!=-1){
		var rowMap = new Map() ;
		for(var i=1;i<=260;i++){
			var strCol = eval("r"+i) ;
			if(typeof(strCol)!="undefined"){
				strCol = strCol.split("@koron@") ;
				var rowValue = typeof(strCol[1])=="undefined"?"":strCol[1] ;
				rowValue = rowValue==null?"":rowValue ;
				rowMap.put(strCol[0].replace(".","_"),rowValue) ;
			}
		}
		griddata.rows[griddata.rows.length] = rowMap;	
	}else{
		var rowcols = new Array(); 
		for(var i=1;i<=260;i++){
			var strCol = eval("r"+i) ;
			if(typeof(strCol)!="undefined"){
				rowcols[i-1] = strCol;
			}
		}
		griddata.rows[griddata.rows.length] = rowcols;	
	}
}

function gridData(){
	this.cols = new Array();
	this.rows = new Array();
}

function TableList(tableDiv,tableId){  
    this.tableDiv = tableDiv; 
    this.table = tableId; 
	this.table.createTHead();
	this.table.tHead.insertRow(0);

	var row = this.table.tHead.rows[0];	
	var newTh = row.insertCell(0);
	newTh.className = "listheade";
	newTh.innerHTML = 'No.'; 
	newTh.style.width = 35 +"px";
	newTh.setAttribute("defaultwidth",35);
	newTh.align = 'center';
	newTh.colId = 0;

}
	
function Cols(name,display,width,fieldType,nullable,defaultValue,maxLength,inputType,inputValue,popupType,fieldIdentityStr,copyType,listOrder,groupName,inputTypeOld,relationKey){
	this.name = name;
	this.display = display;
	this.width = width;
	this.fieldType = fieldType;
	this.nullable = nullable;
	this.defaultValue = defaultValue;
	this.maxLength = maxLength;
	this.inputType = inputType;
	this.inputValue = inputValue;
	this.popupType = popupType ;
	this.fieldIdentityStr = fieldIdentityStr ;
	this.listOrder = listOrder ;
	this.groupName = groupName ;
	this.copyType = copyType ;
	this.inputTypeOld=inputTypeOld;
	this.relationKey=relationKey;
}


TableList.prototype.addCols = function(col,colId,count){
	if(col.inputType != -2){
		var newTh = document.createElement("td");
		if (colId == 2) {
			newTh.className = "listhead30";
		}
		if(col.inputType==2){
			newTh.innerHTML = col.display+"..";
		}else{
			newTh.innerHTML = col.display;
		}
		newTh.style.width = col.width+"px";
		newTh.setAttribute("defaultwidth",col.width);
		newTh.align = 'center';	
		newTh.colId = colId;
		if(this.hasGroup){
			if(col.groupName == "" || col.groupCount == -1){				
				//newTh = this.table.tHead.rows[0].insertCell();
				newTh.rowSpan = 2;				
				newTh.height = "44px";
				this.table.tHead.rows[0].appendChild(newTh);
			}else if(col.groupName != "" && col.groupCount >0){
				newTh.height = "22px";
				this.table.tHead.rows[1].appendChild(newTh);
				
				//newTh = this.table.tHead.rows[0].insertCell();
				newTh = document.createElement("td");
				newTh.colSpan = col.groupCount;
				newTh.innerHTML = col.groupName;
				newTh.align = 'center';			
				this.table.tHead.rows[0].appendChild(newTh);
				//newTh = this.table.tHead.rows[1].insertCell();
			}else{			
				//newTh = this.table.tHead.rows[1].insertCell();
				//newTh = document.createElement("td");
				newTh.height = "22px";
				this.table.tHead.rows[1].appendChild(newTh);
			}
		}else{
			this.table.tHead.rows[0].appendChild(newTh);
		}
	}
}

function deleditGridRow(obj){
	if(typeof(readOnly)!="undefined" && readOnly!=null && readOnly) return ;
	if(confirm("确定要删除这一行吗?")==0){
		return;
	}
	
	gridTable = obj.parentNode.parentNode.parentNode.parentNode;
	//找到对应的div 从而取得gridData
	tableDiv = document.getElementById(gridTable.id+"DIV");
	gridData = tableDiv.gridData;
	rowC = gridTable.rows.length-gridTable.tHead.rows.length;
	rowId = parseInt(obj.parentNode.getAttribute("rowId"));
	if(rowC>2){
		gridTable.rowCount = rowC -1;
		var rowIdObj=rowId-1;		
		cols = gridData.cols;
		for(i=0;i<cols.length;i++){
			if(cols[i].fieldType==0||cols[i].fieldType==1){
				document.getElementsByName(cols[i].name)[rowIdObj].value="0";
			}else if(cols[i].inputType!=5){				
				document.getElementsByName(cols[i].name)[rowIdObj].value="";
			}				
		}
		if(typeof(delFireEvent)!="undefined"){
			delFireEvent(rowIdObj,gridTable.id);
		}
		if(gridTable.tHead.rows.length>1){
			gridTable.deleteRow(rowId+1);				
			var colDataRows = gridTable.rows;    
	        for (var i=2; i < colDataRows.length-1; i++) {
	              var cs = colDataRows[i].cells;
				  if(cs[0].innerHTML.indexOf("<")>-1){
					  cs[0].innerHTML = (i-1)+cs[0].innerHTML.substr(cs[0].innerHTML.indexOf("<"));
				  }else{
					  cs[0].innerHTML = (i-1);
				  }			  
				  cs[1].setAttribute("rowId",(i-1));
	        }
		}else{
			gridTable.deleteRow(rowId);	
			var colDataRows = gridTable.rows;    
	        for (var i=1; i < colDataRows.length-1; i++) {
	              var cs = colDataRows[i].cells;
				  if(cs[0].innerHTML.indexOf("<")>-1){
					  cs[0].innerHTML = i+cs[0].innerHTML.substr(cs[0].innerHTML.indexOf("<"));
				  }else{
					  cs[0].innerHTML = i;
				  }			  
				  cs[1].setAttribute("rowId",i);
	        }
        }
        //删除行时注册的事件被取消
        if(typeof(initCalculate)!="undefined"){
        	initCalculate();
        }
        if(typeof(execStat)!="undefined"){
			execStat();
		}
	}
}
function getRowHTML(gridTable,cols,rowMap,rowNum){
    stdStr = '<td class="listheadonerow" align="center" style="width:20px;">'+gridTable.rowCount+'';	
	gridTable.rowCount = parseInt(gridTable.rowCount) +1 ;
	deltdStr ='<td align="center" style="cursor:pointer;width:25px;" rowId="'+(parseInt(gridTable.rowCount)-1)+'" ><div title=Ctrl+- class="delImg" onclick=deleditGridRow(this)></div></td>';
	var hiddenStr='';
	var hasGroup = "false" ;
	var colStr='';
	var headLen = gridTable.tHead.rows.length ;
	if(headLen>1){
		hasGroup = "true" ;
	}
	for (i=0;i<cols.length;i++){
		var row = "" ;
		if(rowMap==""){
			row = "" ;
		}else if(typeof(rowMap.size)=="undefined"){
			row = rowMap[i] ;
		}else{
			var fieldName = cols[i].name ;
			fieldName = fieldName.substring(fieldName.indexOf("_")+1,fieldName.length) ;
			row = rowMap.get(fieldName) ;
			if(typeof(row)=="undefined" || row==null){
				row = "" ;
			}
		}
		if(cols[i].inputType == -2){
			hiddenStr = hiddenStr+getHiddenTypeHTML(cols[i],row,i); 
		}else{
	   		var newTd = "";
	   		if(cols[i].inputType == 0){
	   			if(cols[i].fieldType == 3){
	  				newTd = getInputTypeTextArea(cols[i],row,i) ;
	  			}else{
	    	 		newTd = getInputTypeHTML(cols[i],row,i); 
	    	 	}
	  		}else if(cols[i].inputType == 2){
			     newTd = getMainsTypeHTML(cols[i],row,i,cols,hasGroup);		     
			} else if(cols[i].inputType == 4){
			     newTd = getMoreLanguageHTML(cols[i],row,i,cols);		     
			} else if(cols[i].inputType == 5){
				 newTd = getCheckBoxTypeHTML(cols[i],row,i,cols,rowNum) ;
			} else if(cols[i].inputType == 9){
			 	newTd = getExpressionHTML(cols[i],row,i) ;
			} else if(cols[i].inputType == 44){
			 	newTd = getEnumeration(cols[i],row,i,cols)
			}else if(cols[i].inputType == -1){
	      		newTd = getDefineTypeHTML(cols[i],row,i); 
	   		}else if(cols[i].inputType == 6){
	   			newTd = getInputTypeHTML(cols[i],row,i);
	   		}else if(cols[i].inputType == 45){
	   			newTd = getPopupTypeHTML(cols[i],row,i);
	   		}else if(cols[i].inputType == 8){
	   		 	/*选项数据的 选项值 不能修改 只能是只读的*/
	   			if(typeof(editValue)!="undefined" && !editValue && row==""){
	   		 		newTd = getInputTypeHTML(cols[i],row,i); 
	   		 	}else{
	   		 		newTd = getInputTypeOnlyRead(cols[i],row,i) ;
	   		 	}
	   		} else if(cols[i].inputType  == 1){
	  		    newTd = getEnumerateTypeHTML(cols[i],row,i);
			}  else if(cols[i].inputType  == 16){
	  		    newTd = getEnumerateTypeHTML(cols[i],row,i);
			} 
	   		colStr +='<td style="width:'+cols[i].width+'px">'+newTd+'</td>';
		}
	} 
	return  '<tr>'+stdStr+hiddenStr+'</td>'+ deltdStr +colStr+'</tr>';
}
//详情界面取html
function getDetailRowHTML(gridTable,cols,rowMap,rowNum){
    stdStr = '<td class="listheadonerow" align="center" style="width:35px;">'+gridTable.rowCount+'';	
	gridTable.rowCount = parseInt(gridTable.rowCount) +1 ;
	var hasGroup = "false" ;
	var colStr='';
	var headLen = gridTable.tHead.rows.length ;
	if(headLen>1){
		hasGroup = "true" ;
	}
	for (i=0;i<cols.length;i++){
		var row = "" ;
		if(rowMap==""){
			row = "" ;
		}else if(typeof(rowMap.size)=="undefined"){
			row = rowMap[i] ;
		}else{
			var fieldName = cols[i].name ;
			fieldName = fieldName.substring(fieldName.indexOf("_")+1,fieldName.length) ;
			row = rowMap.get(fieldName) ;
			if(typeof(row)=="undefined" || row==null){
				row = "" ;
			}
		}
		if(cols[i].inputType == -2){				 
		}else if(cols[i].inputType == 1){//如果是枚举型，则把值转换成显示名
			var inpV = cols[i].inputValue.split(";");
			colStr +='<td style="width:'+cols[i].width+'px">';
			for (var j=0;j<inpV.length;j++){
				if(row.length!=0 && inpV[j].indexOf(row+":")!=-1)
				{
					colStr += inpV[j].substring(row.length+1);
					break;
				}
			}
			colStr += '&nbsp;</td>';
		}else if(cols[i].inputType == 5){//如果复选框
			var inpV = cols[i].inputValue.split(";");
			var inpR = row.split(",");
			colStr +='<td style="width:'+cols[i].width+'px">';
			for (var j=0;j<inpR.length;j++){
				if(inpR[j]!='')
				for (var k=0;k<inpV.length;k++){
					if(inpV[k].indexOf(inpR[j]+":")!=-1)
					{
						colStr += inpV[k].substring(inpR[j].length+1)+",";
					}
				}
			}
			colStr += '&nbsp;</td>';
		}else{
			if(cols[i].name.indexOf("Seq_hid") >0){
				if(rowMap.get)
				{
					seqstr=rowMap.get("Seq");
					colStr +='<td style="width:'+cols[i].width+'px">'+
					'<img src="/style/images/St.gif" class="search" onClick="javascript:asyncbox.open({id:\'ChildPopup\',title:\'序列号清单\',url:\'/UserFunctionAction.do?operation=22&type=stockSequence&seq='+seqstr+'&childTableName='+$('#tableName').val()+'&seqFname='+cols[i].name+'&page=detail\',width:450,height:400});" />'
					+row+'&nbsp;</td>';
				}
			}else{
	   			colStr +='<td style="width:'+cols[i].width+'px" title="'+row+'">'+row+'&nbsp;</td>';
	   		}
		}
	} 
	return  '<tr onclick="focusDetailRow(\''+gridTable.id.substring(0,gridTable.id.length-5)+'\','+rowNum+');">'+stdStr+'</td>'+ colStr+'</tr>';
}

 function addTableRow(gridTable,cols,rowMap){	
	//rowC = parseInt(gridTable.rowCount);
	var headLen = gridTable.tHead.rows.length ;
	var rowC = gridTable.rows.length-headLen
	var newTr = gridTable.insertRow(gridTable.rows.length-1);
	var sTd = newTr.insertCell(0);
	sTd.className = "listheadonerow";
	sTd.align = 'center';
	sTd.innerHTML = rowC; 	
	sTd.style.width = 20 + "px";
	
	//gridTable.rowCount = rowC +1 ;
	//gridTable.setCount(rowC+1) ;
	//var sdTd = newTr.insertCell(1);
	var sdTd = document.createElement("td");
	sdTd.align = 'center';
	sdTd.innerHTML = '<div title=Ctrl+- class="delImg" onclick=deleditGridRow(this)></div>'; 
	sdTd.style.width = 25 + "px";
	sdTd.style.cursor = 'pointer';
	sdTd.setAttribute("rowId",rowC);
	newTr.appendChild(sdTd);
	
	var count=1;
	for (i=0;i<cols.length;i++){
		var row = "" ;
		if(rowMap==""){
			row = "" ;
		}else if(typeof(rowMap.size)=="undefined"){
			row = rowMap[i] ;
		}else{
			var fieldName = cols[i].name ;
			fieldName = fieldName.substring(fieldName.indexOf("_")+1,fieldName.length) ;
			row = rowMap.get(fieldName) ;
		}
		if(cols[i].inputType == -2){
			sTd.innerHTML = sTd.innerHTML+getHiddenTypeHTML(cols[i],row,i); 
		}else{
			count++;
	   		//var newTd = newTr.insertCell(count);
	   		var newTd = document.createElement("td");
	   		if(cols[i].inputType == 0){
	   			if(cols[i].fieldType == 3){
	  				newTd.innerHTML = getInputTypeTextArea(cols[i],row,i) ;
	  			}else{
	    	 		newTd.innerHTML = getInputTypeHTML(cols[i],row,i); 
	    	 	}
	  		}else if(cols[i].inputType == 2){
			     newTd.innerHTML = getMainsTypeHTML(cols[i],row,i,cols,headLen>1?"true":"false");		     
			} else if(cols[i].inputType == 4){
			     newTd.innerHTML = getMoreLanguageHTML(cols[i],row,i,cols);		     
			} else if(cols[i].inputType == 5){
				 newTd.innerHTML = getCheckBoxTypeHTML(cols[i],row,i,cols,rowC) ;
			} else if(cols[i].inputType == 9){
			 	newTd.innerHTML = getExpressionHTML(cols[i],row,i) ;
			} else if(cols[i].inputType == 44){
			 	newTd.innerHTML = getEnumeration(cols[i],row,i,cols)
			}else if(cols[i].inputType == -1){
	      		newTd.innerHTML = getDefineTypeHTML(cols[i],row,i); 
	   		}else if(cols[i].inputType == 6){
	   			newTd.innerHTML = getInputTypeHTML(cols[i],row,i);
	   		}else if(cols[i].inputType == 45){
	   			newTd.innerHTML = getPopupTypeHTML(cols[i],row,i);
	   		}else if(cols[i].inputType == 8){
	   		 	/*选项数据的 选项值 不能修改 只能是只读的*/
	   			if(typeof(editValue)!="undefined" && !editValue && row==""){
	   		 		newTd.innerHTML = getInputTypeHTML(cols[i],row,i); 
	   		 	}else{
	   		 		newTd.innerHTML = getInputTypeOnlyRead(cols[i],row,i) ;
	   		 	}
	   		} else if(cols[i].inputType  == 1){
	  		    newTd.innerHTML = getEnumerateTypeHTML(cols[i],row,i);
			}  else if(cols[i].inputType  == 16){
	  		    newTd.innerHTML = getEnumerateTypeHTML(cols[i],row,i);
			} 
			newTd.style.width = cols[i].width;
			newTr.appendChild(newTd);
		}	   
	} 
}


function addStatRow(table,cols){
	var rowCount = table.rows.length ;
    var newTr = table.insertRow(rowCount);
	newTr.style.background = "#FFFFEE"
	newTr.stat = "true";
	//var sTd = newTr.insertCell(0);	
	var sTd = document.createElement("td");	
	sTd.align = 'center';
	//sTd.className = "listheadonerow10";
	sTd.innerHTML = '&nbsp;'; 	
	sTd.style.width = 20+"px";
	newTr.appendChild(sTd);
	
	//var sdTd = newTr.insertCell(1);
	var sdTd = document.createElement("td");
	sdTd.align = 'center';
	sdTd.innerHTML = '&nbsp;'; 
	sdTd.style.width = 25+"px";
	sdTd.style.cursor = 'pointer';
	sdTd.table = table;
	sdTd.setAttribute("rowId",parseInt(rowCount) -1);
	newTr.appendChild(sdTd);
	
	var count=1;
	for (i=0;i<cols.length;i++){
		if(cols[i].inputType == -2){
			
		}else{
			count++;
	   		//var newTd = newTr.insertCell(count);
	   		var newTd = document.createElement("td");
			//if (i == 1) {
			//newTd.className = "listheadonerow10_statistic";
			//	}
			newTd.align = "left";
	   		if(cols[i].inputType == 0){
	    	    newTd.innerHTML = '<div id="'+cols[i].name+'_total">&nbsp;</div> '; 
	  		 } else if(cols[i].inputType == 1){
	  		    newTd.innerHTML = '&nbsp;'; 
			 } else if(cols[i].inputType == 16){
	  		    newTd.innerHTML = '&nbsp;'; 
			 } else if(cols[i].inputType == 2){
			      newTd.innerHTML = '<div id="'+cols[i].name+'_total">&nbsp;</div> ';		
			 } else if(cols[i].inputType == 4){
			      newTd.innerHTML = '&nbsp;';				  
			 } else if(cols[i].inputType == 5){
			 	newTd.innerHTML = '&nbsp;' ;
			 }else if(cols[i].inputType == 6){
				 newTd.innerHTML = '<div id="'+cols[i].name+'_total">&nbsp;</div>' ;
			 }else if(cols[i].inputType == 44 || cols[i].inputType == 45){
			 	newTd.innerHTML = '&nbsp;' ;
			 }else if(cols[i].inputType == -1){
	      		newTd.innerHTML = '&nbsp;'; 
	   		 }else if(cols[i].inputType == 8){
	   		 	newTd.innerHTML = '<div id="'+cols[i].name+'_total">&nbsp;</div> '; 
	   		 }else if(cols[i].inputType == 9){
	   		 	newTd.innerHTML = '<div id="'+cols[i].name+'_total">&nbsp;</div> ';
	   		 }else{
	   		 	newTd.innerHTML = '&nbsp;'; 
	   		 }
			 newTd.width = cols[i].width;
			 
			 newTr.appendChild(newTd);
		}	   
	}   
}
function addDetailStatRow(table,gridData,cols){
	var rowCount = table.rows.length ;
    var newTr = table.insertRow(rowCount);
	newTr.style.background = "#FFFFEE"
	newTr.stat = "true";
	//var sTd = newTr.insertCell(0);	
	var sTd = document.createElement("td");	
	sTd.align = 'center';
	//sTd.className = "listheadonerow10";
	sTd.innerHTML = '&nbsp;'; 	
	sTd.style.width = 20+"px";
	newTr.appendChild(sTd);
	
	
	var count=1;
	for (i=0;i<cols.length;i++){
		if(cols[i].inputType == -2){
			
		}else{
			count++;
	   		//var newTd = newTr.insertCell(count);
	   		var newTd = document.createElement("td");
			//if (i == 1) {
			//newTd.className = "listheadonerow10_statistic";
			//	}
			
			rowi=0;
			
			if(cols[i].fieldType == 0 || cols[i].fieldType == 1){
	   			for(j=0;j<gridData.rows.length;j++){
					var row = "" ;
					if(typeof(gridData.rows[j].size)=="undefined"){
					}else{
						var fieldName = cols[i].name ;
						fieldName = fieldName.substring(fieldName.indexOf("_")+1,fieldName.length) ;
						rowi += Number(gridData.rows[j].get(fieldName) ) ;
					}	
				}	   
			}		
			
			if(rowi==0 || rowi == NaN){
				rowi = '';
			}
			newTd.align = "left";
	   		if(cols[i].inputType == 0){	   			
	    	    newTd.innerHTML = '<div id="'+cols[i].name+'_total" sum="'+rowi+'">&nbsp;</div> '; 
	  		 } else if(cols[i].inputType == 1){
	  		    newTd.innerHTML = '&nbsp;'; 
			 } else if(cols[i].inputType == 16){
	  		    newTd.innerHTML = '&nbsp;'; 
			 } else if(cols[i].inputType == 2){
	    	    newTd.innerHTML = '<div id="'+cols[i].name+'_total" sum="'+rowi+'">&nbsp;</div> '; 
			 } else if(cols[i].inputType == 4){
			      newTd.innerHTML = '&nbsp;';				  
			 } else if(cols[i].inputType == 5){
			 	newTd.innerHTML = '&nbsp;' ;
			 }else if(cols[i].inputType == 6){
	    	    newTd.innerHTML = '<div id="'+cols[i].name+'_total" sum="'+rowi+'">&nbsp;</div> '; 
			 }else if(cols[i].inputType == 44 || cols[i].inputType == 45){
			 	newTd.innerHTML = '&nbsp;' ;
			 }else if(cols[i].inputType == -1){
	      		newTd.innerHTML = '&nbsp;'; 
	   		 }else if(cols[i].inputType == 8){
	    	    newTd.innerHTML = '<div id="'+cols[i].name+'_total" sum="'+rowi+'">&nbsp;</div> ';  
	   		 }else if(cols[i].inputType == 9){
	    	    newTd.innerHTML = '<div id="'+cols[i].name+'_total" sum="'+rowi+'">&nbsp;</div> '; 
	   		 }else{
	   		 	newTd.innerHTML = '&nbsp;'; 
	   		 }
			 newTd.width = cols[i].width;
			 
			 newTr.appendChild(newTd);
		}	   
	}   
}
var curGridRowNum= {  
	curKey:'',curLine:-1,
	set : function(key,value){this[key] = value;this.curKey=key;this.curLine=value;},  
	get : function(key){return this[key]},  
	contains : function(key){return this.Get(key) == null?false:true},  
	remove : function(key){delete this[key]}  
}
function focusDetailRow(tb,line){	//.addClass("dragSelect")
	curGridRowNum.set(tb,line); 
	
	$(".detailTableSelect").removeClass("detailTableSelect");
	$("#"+tb+"Table").find("tbody tr:eq("+(line-1)+")").addClass("detailTableSelect");
}
function focusAddRow(obj){	  
	  if(typeof(readOnly)!="undefined" && readOnly!=null && readOnly) return ;
	  var objs = document.getElementsByName(obj.name);
	  	//计算当前当号
		for(i=0;i<objs.length;i++){
			if(obj == objs[i]){  
				tb= obj.name.substring(0,obj.name.indexOf("_"));
				curGridRowNum.set(tb,i);
			}
		}
	  if(obj == objs[objs.length -1]){  
		    var trObj=obj.parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
		   	trObj.cells[1].childNodes[0].click();
	  }

}

function getMoreLanguageHTML(col,row,i,cols){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	var ml ='';
	
	var enter = "moreLanguage("+col.maxLength+",this);";
	var theclick = "onDblClick='moreLanguage("+col.maxLength+",this);';";
	//onKeyDown='  if(event.keyCode==13) "+enter+"' "+theclick+"    暂时不支持多语言
    return "<input list='true' onfocus='focusAddRow(this)' name="+col.name+" onClick='select()' type=text style='width:"+col.width+"' value ='"+dv+"'  "+ml+" size=1  disableautocomplete=\"true\" autocomplete=\"off\"  >";
}

function getEnumeration(col,row,i,cols){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	var ml ='';
	
	var enter = "selectEnumeration("+col.maxLength+",this);";
	var theclick = "onDblClick='selectEnumeration("+col.maxLength+",this);';";
	var losefocus = "lostfocus(this);";
	
    return "<input list='true' onfocus='focusAddRow(this)'  onfocusout='" + losefocus + "' name="+col.name+" onClick='select()' type=text style='width:"+col.width+"' value ='"+dv+"'  "+ml+" size=1  onKeyDown='  if(event.keyCode==13) "+enter+"' "+theclick+"  disableautocomplete=\"true\" autocomplete=\"off\" >";
}

function getPopupTypeHTML(col,row,i,cols){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	var enter = "selectPopup(\""+col.display+"\",\""+col.inputValue+"\",this) ";
	var theclick = "onDblClick='selectPopup(\""+col.display+"\",\""+col.inputValue+"\",this)' ";
	
	return "<input list='true' onfocus='focusAddRow(this)' name="+col.name+" onClick='select()' type=text style='width:"+col.width+"' value ='"+dv+"' size=1  onKeyDown='if(event.keyCode==13)"+enter+"' "+theclick+"  disableautocomplete=\"true\" autocomplete=\"off\" >";
}

function getMainsTypeHTML(col,row,i,cols,hasGroup){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	
	var tt =col.inputValue.split(":");	
	var keyId="";
	for (i=0;i<cols.length;i++){
		var fieldName=cols[i].name;
		if(fieldName.indexOf("_")>=0){
		   fieldName=fieldName.substr(fieldName.indexOf("_")+1);
		   if(typeof(row)!="undefined" && row.length>0 && fieldName==tt[1]){
			  keyId=row+';';
			  break;
	       }
	    }
	}  
	
	var ml ='';
	if(col.fieldType == 2 ||col.fieldType == 3 || col.fieldType ==4){
		ml ='maxlength='+col.maxLength;
	}	
	
	//用于提示此弹出框的条件中包含页面字段，此字段是必须输入的，则弹出框前提示先输入此字段值	var headCount = 1 ;
	if(hasGroup=='true'){
		headCount = 2 ;
	}
	var str ="";
	var ajaxUrlStr="";

	if(col.copyType=="triggerCopy" || col.copyType=="triggerOrCopy"){
		str="copyFields(\"dbClick\",\""+col.listOrder+"\");";
		ajaxUrlStr="copyFields(\"dbClick\",\""+col.listOrder+"\");";
	}
	if(col.fieldIdentityStr=="count" ){
		str="copyFields(\"dbClick\",\""+col.listOrder+"\",\"copyAll\");";
		ajaxUrlStr="copyFields(\"dbClick\",\""+col.listOrder+"\",\"copyAll\");";
	}
	
	str += "if(popCondInput(\""+col.inputValue+"\",this))openChildSelect(\"/UserFunctionAction.do?keyId="+keyId+"&tableName="+tt[0]+"&fieldName="+tt[1]+"&operation=22";
	var childUrlStr = "/UserFunctionAction.do?keyId="+keyId+"&tableName="+tt[0]+"&fieldName="+tt[1]+"&operation=22";																																	
	ajaxUrlStr += "if(popCondInput(\""+col.inputValue+"\",this))childSelect(\"UtilServlet?keyId="+keyId+"&tableName="+tt[0]+"&fieldName="+tt[1]+"&operation=Ajax";																																		
	if(tt[3]!=undefined){
		var autoParam=tt[3].split(";");
		if(autoParam.length>0){
			for(var i=0;i<autoParam.length;i++){
				if(autoParam[i].length>0){
					ajaxUrlStr+="&"+autoParam[i]+"=\"+document.getElementsByName(\""+autoParam[i]+"\")[this.parentNode.parentNode.rowIndex-"+headCount+"].value+\"";
				}
			}
		}
	}
	
	for(var i=4;i<tt.length;i++){	
		tt[i]=tt[i].substr(tt[i].indexOf("@")+1);	
		if(tt[i].indexOf(tt[0])==0){
			var elements=document.getElementsByName(col.name);		
			str=str+"&"+tt[i]+"=\"+document.getElementsByName(\""+tt[i]+"\")[this.parentNode.parentNode.rowIndex-"+headCount+"].value+\"";
			ajaxUrlStr=ajaxUrlStr+"&"+tt[i]+"=\"+document.getElementsByName(\""+tt[i]+"\")[this.parentNode.parentNode.rowIndex-"+headCount+"].value+\"";
			childUrlStr = childUrlStr+"&"+tt[i]+"=\"+document.getElementsByName(\""+tt[i]+"\")[this.parentNode.parentNode.rowIndex-"+headCount+"].value+\"";			
		}else{			
			str=str+"&"+tt[i]+"=\"+document.getElementsByName(\""+tt[i]+"\")[0].value+\"";
			ajaxUrlStr=ajaxUrlStr+"&"+tt[i]+"=\"+document.getElementsByName(\""+tt[i]+"\")[0].value+\"";
			childUrlStr = childUrlStr+"&"+tt[i]+"=\"+document.getElementsByName(\""+tt[i]+"\")[0].value+\"";
		}
	}
	str=str+"\",this,\""+tt[0]+"_"+tt[1]+"\",\""+tt[2]+"\");";	
	ajaxUrlStr = ajaxUrlStr + "\",\""+childUrlStr+"\",this,\""+tt[0]+"_"+tt[1]+"\");";	
	var ajaxUrlStr = "if(event.keyCode==13&&this.value.length>0) {"+ajaxUrlStr+"}";
	var dbStr = "" ;
	var keyStr = "" ;
	if(typeof(col.popupType)!="undefined" && col.popupType==1){
		dbStr = str ;
		ajaxUrlStr = "if(event.keyCode==13){event.keyCode=9;}" ;
	}else if(typeof(col.popupType)!="undefined" && col.popupType==2){
		keyStr = str ;
	}else{
		dbStr = str ;
		keyStr = str ;
	}
	window[col.name + "ajaxUrlStr"] = ajaxUrlStr ;
	window[col.name + "keyStr"] = keyStr ;
	window[col.name + "dbStr"] = dbStr ;
	if(col.maxLength=="tblGoodsProp"){
	  var cg="iniPropValueChange(this)";
	  var fieldName = encodeURI(col.display) ;
	  str = str.replace("operation=22","operation=22&LinkType=@URL:&displayName="+fieldName) ;
	  if(col.fieldType=="1"){
		   return "<input list='true' onfocus='focusAddRow(this);select();' name="+col.name+" style='width:"+col.width+"' type=text value ='"+dv+"'  "+ml+" size=1   onDblClick='eval("+col.name+"dbStr);'   onkeypress=' if(event.keyCode ==13){eval("+col.name+"keyStr);}' onchange='"+cg+"'  disableautocomplete=\"true\" autocomplete=\"off\" >";
	  }else{
	  	   return "<input list='true' onfocus='focusAddRow(this);select();' name="+col.name+" style='width:"+col.width+"' type=text value ='"+dv+"'  "+ml+" size=1   onDblClick='eval("+col.name+"dbStr);'   onkeypress=' if(event.keyCode ==13){eval("+col.name+"keyStr);}' onchange='"+cg+"' readonly style='color:#666666'  disableautocomplete=\"true\" autocomplete=\"off\" >";
	  }
    }else if(col.inputValue.indexOf("@SEQ")!=-1){
	    var tc="openChildSelect(\"/UserFunctionAction.do?operation=22&type=addSequence&colname="+col.name+"&seq="+col.name+"\",document.getElementsByName(\""+col.name+"\")[this.parentNode.parentNode.rowIndex-"+headCount+"],\""+col.name.split("_")[1]+"\",\""+col.name+"\",this)";
	    var keyDown = "if(event.keyCode ==13){addSeq(this);" ;
	    window[col.name + "tc"] = tc ;
	    if(col.inputValue.indexOf('@IOO:Outstore')!=-1){
	    	keyDown += "}" ;
	    	window[col.name + "keyDown"] = keyDown ;
	    	var tq = "openChildSelect(\"/UserFunctionAction.do?operation=22&type=stockSequence&colname="+col.name+"&seq="+col.name+"\",document.getElementsByName(\""+col.name+"\")[this.parentNode.parentNode.rowIndex-"+headCount+"],\""+col.name.split("_")[1]+"\",\""+col.name+"\",this)" ;
	    	window[col.name + "tq"] = tq ;
	    	return "<input list='true' style=\"width:"+(parseInt(col.width)-20)+"px;\" onfocus='focusAddRow(this);select();' name="+col.name+" style='width:"+col.width+"' type=text value ='"+dv+"'  "+ml+" size=1  onDblClick='eval("+col.name+"tc);'   onKeyDown=' eval("+col.name+"keyDown);'><img src='/style/images/St.gif' class='search' onClick='eval("+col.name+"tq);' />";	
	    }else{
	    	keyDown += "select()}";
	    	window[col.name + "keyDown"] = keyDown ;
	    	window[col.name + "tq"] = tq ;
	    	return "<input list='true' style=\"width:"+(parseInt(col.width)-20)+"px;\" onfocus='focusAddRow(this);select();' name="+col.name+" style='width:"+col.width+"' type=text value ='"+dv+"'  "+ml+" size=1  onDblClick='eval("+col.name+"tc);'   onKeyDown=' eval("+col.name+"keyDown);'><img src='/style/images/St.gif' class='search' onClick='eval("+col.name+"tc);'  disableautocomplete=\"true\" autocomplete=\"off\" />";	
	    }
	}
	return "<input list='true' onfocus='focusAddRow(this);select();' name="+col.name+" style='width:"+col.width+"' type=text value ='"+dv+"'  "+ml+" size=1  onKeyDown='eval("+col.name+"ajaxUrlStr);if(event.keyCode ==13 && this.value.length==0){eval("+col.name+"keyStr);}' onDblClick='eval("+col.name+"dbStr);'  disableautocomplete=\"true\" autocomplete=\"off\" >";
}


function getEnumerateTypeHTML(col,row,i){
	var dv = "";
	if(row != ""){
		
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	
	
		
	var str = "<select list='true' onfocus='focusAddRow(this)' onKeyDown=' if(event.keyCode==13) event.keyCode=9' name='"+col.name+"' style='width:"+col.width+"' >";
	
	if(col.name=="isOnly"){
		str= "<select list='true' onfocus='focusAddRow(this)' onKeyDown=' if(event.keyCode==13) event.keyCode=9' name='"+col.name+"' style='width:"+col.width+"' onChange='selectChange(this)' >";
	}
	//if(col.nullable){
	if(col.nullable && (col.fieldType == 0 || col.fieldType == 1))
	   str = str + "<option value = '-100000'> </option>";
	else
	   str = str + "<option value =''></option>";
	//}
	var ts =col.inputValue.split(";");
	for(k = 0;k<ts.length;k++){
		if(ts[k] == '') continue;
		
	    var tt = ts[k].split(":");	
		if(dv != '' && dv == tt[0]){
			str = str+"<option value = '"+tt[0]+"' selected> "+tt[1]+" </option>";
		}else{
		  str = str+"<option value = '"+tt[0]+"'> "+tt[1]+" </option>";
		}
	}
	str = str+ "</select>";
	return str;
}

function getCheckBoxTypeHTML(col,row,i,cols,rowNum){
	var str = " ";
	var checkValue = "";
	if(row.length>0){
		checkValue = row; 
	}
	var ts =col.inputValue.split(";");
	for(k = 1;k<ts.length;k++){
		var cv = ts[k].split(":") ;
		
		if(checkValue.length>0){
			if(checkValue.indexOf(cv[0]+",")!=-1){
				str = str +"<input list='true'  type='checkbox' style='width:15px;' name='"+col.name+rowNum+"' onfocus='focusAddRow(this)'  checked value='"+ cv[0]+"'/>"+cv[1];
			}else{
				str = str +"<input list='true'  type='checkbox' style='width:15px;' name='"+col.name+rowNum+"' onfocus='focusAddRow(this)'  value='"+ cv[0]+"'/>"+cv[1];
			}
		}else{
			str = str +"<input list='true'  type='checkbox' style='width:15px;' name='"+col.name+rowNum+"' onfocus='focusAddRow(this)'  value='"+ cv[0]+"'/>"+cv[1];
		}
	}
	return str;
}

function getExpressionHTML(col,row,i){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	var ml ='';
	if(col.fieldType == 2 ||col.fieldType == 3 || col.fieldType ==4){
		ml ='maxlength='+col.maxLength;
	}
    return "<input list='true' name="+col.name+" onFocus='focusAddRow(this); select()' type=text style='width:"+col.width+"' value ='"+dv+"'  "+ml+" size=1  onKeyDown='if(event.keyCode==13) dyGetCalculate(\"/UtilServlet?operation=calculate\",this);' >";
}

function getInputTypeTextArea(col,row,i){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	var ml = 'maxlength='+col.maxLength;
    return "<textarea list='true' name="+col.name+" onFocus='focusAddRow(this); select()' style='width:"+col.width+";height:70px'  "+ml+"  row='8' >"+dv+"</textarea>";
}

function getInputTypeHTML(col,row,i){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	var ml ='';
	if(col.fieldType == 2 ||col.fieldType == 3 || col.fieldType ==4){
		ml ='maxlength='+col.maxLength;
	}
	var enter = 'event.keyCode=9;';
	var theclick = "";
	var readonly = "" ;
	var date = "";
	var num="";
	var ajaxUrlStr="";

	if(col.copyType=="triggerCopy" || col.copyType=="triggerOrCopy"){
		ajaxUrlStr="copyFields(\"dbClick\",\""+col.listOrder+"\");";
	}
	if(col.fieldIdentityStr=="count" ){
		ajaxUrlStr="copyFields(\"dbClick\",\""+col.listOrder+"\",\"copyAll\");";
	}
	
	ajaxUrlStr = "if(event.keyCode==13&&this.value.length>0) {"+ajaxUrlStr+"event.keyCode=9;}";
	if(col.fieldType == 5){
		enter = 'openInputDate(this);';
		theclick = "onClick='openInputDate(this);';";
		date = "date='true'";
	}else if(col.fieldType == 6){
		enter = 'openInputTime(this);';
		theclick = "onClick='openInputTime(this);';";
		date = "date='true'";
	}
	
	if(col.fieldType == 0 || col.fieldType == 1){
		num = "num='true'";
	}
	
	if(col.name=="orderBy")
   		 return "<input list='true' "+date+" "+num+" name="+col.name+readonly+" onFocus='focusAddRow(this); select()' type=text style='width:"+col.width+"' value ='"+dv+"'  "+ml+" size=1  onKeyDown='"+ajaxUrlStr+"' "+theclick+"  disableautocomplete=\"true\" autocomplete=\"off\" >";
	else
   		 return "<input list='true' "+date+" "+num+" name="+col.name+readonly+" onblur='changeValue(this);' onFocus='focusAddRow(this); select()' type=text style='width:"+col.width+"' value ='"+dv+"'  "+ml+" size=1  onKeyDown='"+ajaxUrlStr+"' "+theclick+"  disableautocomplete=\"true\" autocomplete=\"off\" >";
		
}

function changeValue(obj){
	
}
function getInputTypeOnlyRead(col,row,i){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}
	var ml ='';
	if(col.fieldType == 2 ||col.fieldType == 3 || col.fieldType ==4){
		ml ='maxlength='+col.maxLength;
	}
	var enter = 'event.keyCode=9;';
	var theclick = "";
	var date = "";
	if(col.fieldType == 5){
		enter = 'openInputDate(this);';
		theclick = "onClick='openInputDate(this);';";
		date = "date='true'";
	}else if(col.fieldType == 6){
		enter = 'openInputTime(this);';
		theclick = "onClick='openInputTime(this);';";
		date = "date='true'";
	}
    return "<input list='true' "+date+" name="+col.name+" onFocus='focusAddRow(this); select()' readonly='readonly'  type=text style='width:"+col.width+"' value ='"+dv+"'  "+ml+" size=1  onKeyDown=' if(event.keyCode==13) "+enter+"' "+theclick+" >";
	
}

function getDefineTypeHTML(col,row,i){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}	
	var ml ='';
	if(col.fieldType == 2 ||col.fieldType == 3 || col.fieldType ==4){
		ml ='maxlength='+col.maxLength;
	}
    return "<input list='true' name="+col.name+" type=text style='width:"+col.width+"' value ='"+dv+"' "+ml+" size=1 onfocus='focusAddRow(this)'  onkeypress=' if(event.keyCode ==13){"+col.inputValue+"};' >";
	
}

function getHiddenTypeHTML(col,row,i){
	var dv = "";
	if(row != ""){
	   dv = row;
	}else if(col.defaultValue != ''){
	   dv = col.defaultValue;
	}	
	
    return "<input name="+col.name+" type=hidden value ='"+dv+"' >";
	
}

function addRowClick(name,rows){	
	gridTable = document.getElementById(name);
	//找到对应的div 从而取得gridData
	tableDiv = document.getElementById(gridTable.id+"DIV");
	gridData = tableDiv.gridData;
	if(rows!=undefined){
		addTableRow(gridTable,gridData.cols,rows);
	}else{
		addTableRow(gridTable,gridData.cols,"");
	}
	
	if(typeof(initSelect)!="undefined"){
		initSelect(parseInt(gridTable.rows.length-gridTable.tHead.rows.length));
	}
	if(typeof(initCalculate)!="undefined"){
		initCalculate();
	}
	if(typeof(initCountCal)!="undefined"){
		initCountCal();
	}
	
}
function initTableList(gridDIV,gridTable,gridData,childRowCount)
{
	if(gridData.cols.length == 0){
		return;	
	}
    var tableList = new TableList(gridDIV,gridTable);
	gridDIV.gridData = gridData; //把列和行的数据信息记录在gridDIV对象中，方便使用。
	alllength = 0;//计算表格的总宽度	//计算是否有分组	tableList.hasGroup = false;
	tableList.table.hasGroup = false;
	tempGroupName = 'start';
	tempGroupCount = 0;
	tempi=0;
	for(i=gridData.cols.length -1;i>=0;i--){
		if(gridData.cols[i].inputType!=-2 && typeof(gridData.cols[i].groupName) !="undefined" ){			
			if(gridData.cols[i].groupName != tempGroupName){
				tempGroupCount = 1;
				gridData.cols[i].groupCount = -1;
				tempGroupName = gridData.cols[i].groupName;
			}else{
				tempGroupCount ++;
				gridData.cols[i].groupCount = tempGroupCount;				
				gridData.cols[tempi].groupCount = 0; 				
			}	
			tempi = i; //记录上一个需修改的代号			if(gridData.cols[i].groupName != "" && tempGroupCount >1){
				tableList.hasGroup = true;
				tableList.table.hasGroup = true;		
			}
		}
	}
	
	if(tableList.hasGroup){
		tableList.table.tHead.insertRow(1);
		tableList.table.tHead.rows[0].cells[0].rowSpan=2;
	}
	
	var row = tableList.table.tHead.rows[0];
	row.className = "listhead";
	if(typeof(detail)=="undefined" || detail!="detail"){
		//var newTh = row.insertCell(1);
		var newTh = document.createElement("td");	
		newTh.innerHTML = '<IMG title=Ctrl++ src=/style/images/add.gif border=0 onclick="addRowClick(\''+tableList.table.id+'\');"/>';
		newTh.style.width = 25+"px";
		newTh.setAttribute("defalutwidth",25);
		newTh.align = 'center';	
		newTh.colId = i+1;
		newTh.style.cursor = 'pointer';
		//newTh.onclick = 'addRowClick("'+tableList.table.name+'")';
		if(tableList.hasGroup){ //添加按扭也要占两行			newTh.rowSpan = 2;
		}
		row.appendChild(newTh);
	}
	var count=1;
	if(typeof(detail)!="undefined" && detail == "detail"){
		count = 0 ;
	}
	
	for(i=0;i<gridData.cols.length;i++){
		if(gridData.cols[i].inputType!=-2){
			count++;		
			tableList.addCols(gridData.cols[i],i+1,count);		
			alllength +=Number(gridData.cols[i].width);
		}
	}
	
	tableList.table.style.width = alllength+15+"px";
	tableList.table.rowCount = 1; 
	var tableStr = '';
	if(childRowCount >gridData.rows.length){		
		for(j=0;j<gridData.rows.length;j++){
			if(typeof(detail)!="undefined" && detail == "detail"){
				tableStr +=getDetailRowHTML(gridTable,gridData.cols,gridData.rows[j],j+1);
			}	else{
				tableStr +=getRowHTML(gridTable,gridData.cols,gridData.rows[j],j+1);
			}
		}
		for(j=0; j<childRowCount - gridData.rows.length;j++){
			if(typeof(detail)!="undefined" && detail == "detail"){
				tableStr +=getDetailRowHTML(gridTable,gridData.cols,"",j+1);
			}	else{
				tableStr +=getRowHTML(gridTable,gridData.cols,"",j+1);
			}
		}		
	}else{
		for(j=0;j<gridData.rows.length;j++){	
			if(typeof(detail)!="undefined" && detail == "detail"){
				tableStr +=getDetailRowHTML(gridTable,gridData.cols,gridData.rows[j],j+1);
			}	else{	
				tableStr +=getRowHTML(gridTable,gridData.cols,gridData.rows[j],j+1);
			}
		}
	}
	var headIndex = tableList.table.outerHTML.indexOf('</THEAD>') ;
	if(headIndex==-1){
		headIndex = tableList.table.outerHTML.indexOf('</thead>') ;
	}
	tableStr = tableList.table.outerHTML.substring(0,headIndex)+"<TBODY>"+tableStr+'</TBODY></table>';
	
	gridDIV.removeChild(gridTable);
	gridDIV.innerHTML=tableStr;
	
	tableList.table=document.getElementById(gridTable.id);
	
	if(typeof(detail)!="undefined" && detail == "detail"){
		addDetailStatRow(tableList.table,gridData,gridData.cols); 	
	}	else{	
		addStatRow(tableList.table,gridData.cols); 	
	}
	
	//部分需要在明细表第一行填值，但又不需要修改默认值，因为默认值修改了，每一行数据都会变化，可以定义changeDetailDV函数，在些执行

	if(typeof(changeDetailV) != "undefined"){
		changeDetailV();
	}
}