Ext.BLANK_IMAGE_URL='../../js/extjs/resources/images/default/s.gif'
Ext.QuickTips.init();

	function selectAll(boolvalue)
	{
		var allCheckBoxs=document.getElementsByName("cb");
		for(var i=0;i<allCheckBoxs.length;i++)
		{
			if(allCheckBoxs[i].type=="checkbox")
			allCheckBoxs[i].checked=boolvalue;
		}
	}


	
	var cm_grade = new Ext.grid.ColumnModel([
	
	new Ext.grid.RowNumberer(), 
	{

		header : '<input type="checkbox" onclick="selectAll(this.checked)">',
		dataIndex : 'id',
		renderer:function(value)
		{
			return '<input type="checkbox" value='+value+' name="cb">';
		}
		
	},
	{

		header : '姓名',
		dataIndex : 'Name',
		sortable : true
	}, {

		header : '手机号',
		dataIndex : 'Phone',
		sortable : true
	}, {
		header : '家庭电话',
		dataIndex : 'HomeTel',
		sortable : true
	}, {
		header : '公司电话',
		dataIndex : 'UnitTel',
		sortable : true
	}, {
		header : '邮箱',
		dataIndex : 'Email',
		sortable : true
	}]);

	 var store_grade = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'OACommunication.do?operation=21'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		},[
		{
			name : 'id',
			mapping : 'id'
		},
		{
			name : 'Name',
			mapping : 'Name'
		},
		 {
			name : 'Phone',
			mapping : 'Phone'
		},
		 {
			name : 'HomeTel',
			mapping : 'HomeTel'
		},
		{
			name : 'UnitTel',
			mapping : 'UnitTel'
		},
		{
			name : 'Email',
			mapping : 'Email'
		}
		])
	});

	 var grid = new Ext.grid.GridPanel({
		height : 430,
		trackMouseOver : false,
		loadMask : {
			msg : '正在加载数据，请稍后.....'
		},
		ds : store_grade,
		cm : cm_grade,
		frame:true,
		sm : new Ext.grid.RowSelectionModel()

	});
	store_grade.load();
	
    var tree = new Ext.tree.TreePanel({   
		width:200,
		height:400, 
        loader: new Ext.tree.TreeLoader({dataUrl:'OACommunication.do?operation=20'})
    });   
  
    var root = new Ext.tree.AsyncTreeNode({   
        id: '0',   
        text:'通讯录管理' 
    });    
    tree.setRootNode(root);   	

	var mywind = new Ext.Window({	
	layout:"border",
	title:'员工选择框',
	width:500,
	modal : true,
	height:400,
		items:[
			{region:'west',items:[tree],width:150,height:400,collapsible:true,border:true,split:true},
			{region:'center',items:[grid],height:400}		
			],
		buttonAlign : 'right',
		minButtonWidth : 60,
		buttons : [
			{
			text : '确定',
				handler : function(btn) 
				{
					var ids; 
					var names;
					var allCheckBoxs=document.getElementsByName("cb");
					for(var i=0;i<allCheckBoxs.length;i++)
					{
						if(allCheckBoxs[i].checked==true)
						{
						var value_ = allCheckBoxs[i].value;
						var id_name = value_.split("|");
						var id =id_name[0];
						var name = id_name[1]; 
						ids+=id+",";
						names+=name+","
						}
						
					}
					names = names.substr(0,names.length-1);
					ids = ids.substr(0,ids.length-1);
					names = names.substr(9);
					ids = ids.substr(9);
					document.getElementById("selectedCommunicationId").value=ids;
					document.getElementById("selectedCommunicationName").value=names;
					alert(document.getElementById("selectedCommunicationId").value);
					alert(document.getElementById("selectedCommunicationName").value);
				}
			}						
			],
			listeners:
			{
				"show":function(){},
				"hide":function()
				{
					mywind.hide();
					return false;
				}
			}
					   
});

Ext.onReady(function(){
tree.on("click",clickTree)
});
function clickTree(node)
{

		var groupId = node.id;
		alert(groupId);
		store_grade.load({params : {GroupId : groupId}});	

}
function show()
{
mywind.show();
}
