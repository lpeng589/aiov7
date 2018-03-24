/**
 * 用来实现多个输入框中跳转的功能
 * @param className 用来跳转的输入框的类名
 * @returns {TabObj}
 */
function TabObj(className)
{
	this.ele = $("."+className);
	this.next = next;
	
}

function next(unit)
{
	return;
/**	var tmp = this.ele;
	$.each(this.ele,function(index,element){
		if(this == unit){
			if(tmp.length > index+1)
				tmp[index+1].focus();
			else
				tmp[0].focus();
			return false;
		}
	});
*/
}