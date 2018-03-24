/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2003-2008 Frederico Caldeira Knabben
 *
 * == BEGIN LICENSE ==
 *
 * Licensed under the terms of any of the following licenses at your
 * choice:
 *
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 *
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 *
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * == END LICENSE ==
 *
 * Scripts for the fck_select.html page.
 */

function Select( combo )
{
	var iIndex = combo.selectedIndex ;

	oListText.selectedIndex		= iIndex ;
	oListValue.selectedIndex	= iIndex ;

	var oTxtText	= document.getElementById( "txtText" ) ;
	var oTxtValue	= document.getElementById( "txtValue" ) ;

	oTxtText.value	= oListText.value ;
	oTxtValue.value	= oListValue.value ;
}

function Add()
{
	var oTxtText	= document.getElementById( "txtText" ) ;
	if(oTxtText.value.Trim().length==0 && oTxtText.value!=null){
		alert("标签值不能为空，请重新输入!");
		return false;
	}
	AddComboOption( oListText, oTxtText.value, oTxtText.value ) ;

	oListText.selectedIndex = oListText.options.length - 1 ;

	oTxtText.value	= '' ;

	oTxtText.focus() ;
}

function Modify(){
	var iIndex = oListText.selectedIndex ;
	if ( iIndex < 0 ) return ;
	var oTxtText	= document.getElementById( "txtText" ) ;
	oListText.options[ iIndex ].innerHTML	= HTMLEncode( oTxtText.value ) ;
	oListText.options[ iIndex ].value		= oTxtText.value ;
	oTxtText.value	= '' ;
	oTxtText.focus() ;
}

function Move( steps ){
	ChangeOptionPosition( oListText, steps ) ;
}

function Delete(){
	RemoveSelectedOptions( oListText ) ;
}

//下拉框
function SetSelectedValue(flag){
	var oTxtValue = document.getElementById( "txtSelValue" ) ;
	if(flag){
		var iIndex = oListText.selectedIndex ;
		if ( iIndex < 0 ) return ;
		oTxtValue.value = oListText.options[ iIndex ].value ;
	}else{
		oTxtValue.value="";
	}
}
//复选框
function SetSelectedValue2(flag){
	var oTxtValue = document.getElementById( "txtSelValue" ) ;
	if(flag){
		var iIndex = oListText.selectedIndex ;
		if ( iIndex < 0 ) return ;
			var selectVal="";
			for(var i=0;i<oListText.length;i++){
				if(oListText.options[i].selected == true){
					selectVal=selectVal+oListText.options[i].value+";";
			}
		}
		oTxtValue.value = selectVal ;
	}else{
		oTxtValue.value="";
	}
}
//单选框
function SetSelectedValue3(flag){
	var oTxtValue = document.getElementById( "txtSelValue" ) ;
	if(flag){
		var iIndex = oListText.selectedIndex ;
		if ( iIndex < 0 ) return ;
		var oTxtValue = document.getElementById( "txtSelValue") ;
		var selectVal="";
		for(var i=0;i<oListText.length;i++){
			if(oListText.options[i].selected == true){
				selectVal=oListText.options[i].value;
			}
		}
		oTxtValue.value = selectVal ;
	}else{
		oTxtValue.value="";
	}
}


// Moves the selected option by a number of steps (also negative)
function ChangeOptionPosition( combo, steps ){
	var iActualIndex = combo.selectedIndex ;

	if ( iActualIndex < 0 )
		return ;

	var iFinalIndex = iActualIndex + steps ;

	if ( iFinalIndex < 0 )
		iFinalIndex = 0 ;

	if ( iFinalIndex > ( combo.options.length - 1 ) )
		iFinalIndex = combo.options.length - 1 ;

	if ( iActualIndex == iFinalIndex )
		return ;

	var oOption = combo.options[ iActualIndex ] ;
	var sText	= HTMLDecode( oOption.innerHTML ) ;
	var sValue	= oOption.value ;

	combo.remove( iActualIndex ) ;

	oOption = AddComboOption( combo, sText, sValue, null, iFinalIndex ) ;

	oOption.selected = true ;
}

// Remove all selected options from a SELECT object
function RemoveSelectedOptions(combo)
{
	// Save the selected index
	var iSelectedIndex = combo.selectedIndex ;

	var oOptions = combo.options ;

	// Remove all selected options
	for ( var i = oOptions.length - 1 ; i >= 0 ; i-- )
	{
		if (oOptions[i].selected) combo.remove(i) ;
	}

	// Reset the selection based on the original selected index
	if ( combo.options.length > 0 )
	{
		if ( iSelectedIndex >= combo.options.length ) iSelectedIndex = combo.options.length - 1 ;
		combo.selectedIndex = iSelectedIndex ;
	}
}

// Add a new option to a SELECT object (combo or list)
function AddComboOption( combo, optionText, optionValue, documentObject, index )
{
	var oOption ;

	if ( documentObject )
		oOption = documentObject.createElement("OPTION") ;
	else
		oOption = document.createElement("OPTION") ;

	if ( index != null )
		combo.options.add( oOption, index ) ;
	else
		combo.options.add( oOption ) ;

	oOption.innerHTML = optionText.length > 0 ? HTMLEncode( optionText ) : '&nbsp;' ;
	oOption.value     = optionValue ;

	return oOption ;
}

function HTMLEncode( text )
{
	if ( !text )
		return '' ;

	text = text.replace( /&/g, '&amp;' ) ;
	text = text.replace( /</g, '&lt;' ) ;
	text = text.replace( />/g, '&gt;' ) ;

	return text ;
}


function HTMLDecode( text )
{
	if ( !text )
		return '' ;

	text = text.replace( /&gt;/g, '>' ) ;
	text = text.replace( /&lt;/g, '<' ) ;
	text = text.replace( /&amp;/g, '&' ) ;

	return text ;
}
