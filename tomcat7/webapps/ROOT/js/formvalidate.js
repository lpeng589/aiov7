function checkText(myText)
{
	var variable = myText.value;
	if(variable=="" || variable==null)
	{
		myText.value = "00";
		return false;
	}else
	{
		if(isNaN(variable))
		{
			myText.value = "00";
			return false;
		}else
		{
			return true;
		}
	}

}
function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+'$text.get("common.validate.error.null")');
		return false;
	}else
	{
		return true;
	}
			
}
function isNum(variable,message)
{

	if(isNull(variable,message))
	{
		if(isNaN(variable))
		{
			alert(message+'$text.get("common.validate.error.number")');
			return false;
		}else
		{
			return true;
		}		
	}else
	{
		return false;	
	}

}  
function isSpecialCharacter(variable,message)
{
	if(isNull(variable,message))
	{
		if(variable.indexOf('|')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"'|'");
			return false;	
		}else if(variable.indexOf('&')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"'&'");
			return false;		
		}else if(variable.indexOf('%')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"'%'");
			return false;		
		}else if(variable.indexOf('*')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"'*'");
			return false;		
		}else if(variable.indexOf('(')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"'('");
			return false;		
		}else if(variable.indexOf(')')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"')'");
			return false;		
		}else if(variable.indexOf('!')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"'!'");
			return false;		
		}else if(variable.indexOf('#')>=0)
		{
			alert(message+'$text.get("oa.common.special.character")'+"'#'");
			return false;		
		}
		else
		{
			return true;
		}	
	}else
	{
		return false;	
	}	

}
function isEmail(variable,message)
{
	if(isNull(variable,message))
	{
		if(!isEmailAddress(variable))
		{
		alert(message+'$text.get("oa.common.error.email")');
		return false;
		}else
		{
		return true;
		}
	
	}else
	{
		return false;	
	}	

}
function isEmailAddress(str)
{
	var result = false;
	if(str.indexOf('@')>0)
	{
		var a1 = str.split('@')[0];
		var a2 = str.split('@')[1];
		if(isW(a1) && a2.indexOf('.')>0)
		{
			if(isW(a2.split('.')[0]) && isW(a2.split('.')[1]))
			result = true;
		}
	}
	return result;
}
function isW(str)
{
	if(str.length==0)
	{
		return false;
		for(var i=0; i<str.length; i++)
		{
			var tmp = str.substring(i,i+1);
			if(!(tmp>='a'&&tmp<='z') && !(tmp>='A'&&tmp<='Z')&& !(tmp>='0'&&tmp<='9') && tmp!='_')
			{
				return false;
			}
		}
	}
	return true;
}
function scopeValiadte(variable,start,end,message)
{
	
	if(isNum(variable,message))
	{
		var value = parseInt(variable);
		if(value<start || value>end)
		{
			alert(message+'$text.get("oa.word.mustbetween")'+start+'$text.get("oa.word.and")'+end+'$text.get("oa.word.middle")');
			return false;		
		}else
		{
			return true;
		}	
	}else
	{
		return false;	
	}		
}
//最大值

function maxValiadte(variable,num,message) 
{ 

	if(isNum(variable,message))
	{
		  var value = parseInt(variable); 
		  if(value>num) 
		
		  { 
			alert(message+'$text.get("oa.less.than")'+num);
			return false;    
		  }else 
		  { 
			return true; 
		  } 	
	}else
	{
		return false;	
	}	
}
//最小值

function minValiadte(variable,num,message) 
{ 
 	if(isNum(variable,message))
	{
		  var value = parseInt(variable); 
		  if(value<num) 
		  { 
			alert(message+'$text.get("oa.larger.than")'+num); 
			return false;    
		  }else 
		  { 
			return true; 
		  }	
	}else
	{
		return false;	
	} 
}
//字符最少长度

function minLenghValiadte(variable,num,message) 
{ 
  
  var value = variable.length; 
  if(value<num) 
  { 
    alert(message+'$text.get("oa.common.not.less.than")'+num+'$text.get("oa.common.word.countOfCharacter")'); 
    return false;    
  }else 
  { 
    return true; 
  } 
} 
//字符最大长度

function maxLenghValiadte(variable,num,message) 
{ 
  
  var value = variable.length; 
  if(value>num) 
  { 
    alert(message+'$text.get("oa.common.not.more.than")'+num+'$text.get("oa.common.word.countOfCharacter")');  
    return false;    
  }else 
  { 

    return true; 
  } 
}
//字符长度范围
function LenghScopeValiadte(variable,beginnumer,endnumber,message) 
{ 
  
  var value = variable.length; 
  if(value<beginnumer || value>endnumber) 
  { 
    alert(message+'$text.get("oa.common.character.between")'+beginnumer+'$text.get("oa.word.and")'+endnumber+'$text.get("oa.word.middle")'); 
    return false;    
  }else 
  { 
    return true; 
  } 
}