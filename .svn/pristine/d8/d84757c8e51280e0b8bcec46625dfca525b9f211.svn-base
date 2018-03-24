var nodeId=0;
var groupId=0;
var path_display="";
var path_hidden="";
var all_parent_group="";
	//打开模式窗口
	function ShowDialog(url,dialogSettings )
	{ 
		var var1 = "";  
		if(dialogSettings==null||dialogSettings=="")
		{

		   dialogSettings = "Center:yes;Resizable:yes;DialogHeight:360px;DialogWidth:380px;Status:no";
		}
		   window.showModalDialog( url, var1, dialogSettings );
	}
   Ext.BLANK_IMAGE_URL='../../js/extjs/resources/images/default/s.gif';
   	//构建右键菜单
        var contextmenu = new Ext.menu.Menu({
        id: 'theContextMenu',
        items:[
            {
                id:'veiw',
				handle:true,
                text:'查看收藏夹组信息',
				handler:function()
				{
				ShowDialog("OAFavorite.do?operation=$globals.getOP("OP_OA_VIEW_SINLE")&type=OAFavoriteGroup&groupId="+nodeId+"&groupNames="+path_hidden);
				}
            }, 
            {
                id:'addNode',
                text:'添加收藏夹组',
				handler:function()
				{
				document.location = "OAFavorite.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=OAFavoriteGroup&grouopId="+nodeId+"&groupNames="+path_hidden;
				
				}
               
            },'-',{
                id:'delNode',
                text:'删除收藏夹组',
                iconCls:'remove',
				handler:function()
				{
					if(confirm("你确定删除吗？"))
					{
						document.location = "OAFavorite.do?operation=$globals.getOP("OP_DELETE")&type=OAFavoriteGroup&groupId="+nodeId;
					}
					
				}
            },{
                id:'modifNode',
                text:'修改收藏夹组',
                iconCls:'edit',
				handler:function()
				{
					document.location = "OAFavorite.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=OAFavoriteGroup&groupId="+nodeId+"&groupNames="+path_hidden;
				}
            }
            ]
        }); 
	Ext.onReady(function(){
    var tree = new Ext.tree.TreePanel({
        loader: new Ext.tree.TreeLoader({dataUrl:'OAFavorite.do?operation=$globals.getOP("OP_OA_JSON")&json_type=all'}),
		listeners:{
                   "click":function(node, event)
                    {
						path_display="";
						path_hidden="";
						nodeId = node.id;
						node = node.parentNode;
						if(node.id!=0)
						{
							setPath(node);
						}
						path_display="根目录\\"+path_display;
						path_hidden="根目录\\"+path_hidden;
						document.getElementById("path_group").innerHTML="";
						document.getElementById("path_group").innerHTML="当前组名："+path_display;
						all_parent_group = path_hidden;
						document.location = "OAFavorite.do?groupId="+nodeId+"&groupNames="+path_display;
                    }

				  }
    });   
  
    var root = new Ext.tree.AsyncTreeNode({   
        id: '0',   
        text:'根目录' 
		
    });    
    tree.setRootNode(root); 
	tree.render('tree');
	tree.expandAll();
	tree.on("contextmenu",function(node,e){
	e.preventDefault();
	if(node.id==0)
	{
		Ext.getCmp('veiw').disable();
		Ext.getCmp('addNode').disable();
		Ext.getCmp('delNode').disable();
		Ext.getCmp('modifNode').disable();
	}else
	{
		Ext.getCmp('veiw').enable();
		Ext.getCmp('addNode').enable();
		Ext.getCmp('delNode').enable();
		Ext.getCmp('modifNode').enable();
	}
	nodeId = node.id;
	node = node.parentNode;
	path_display="";
	path_hidden="";
	if(node.id!=0)
	{
		setPath(node);
	}
	path_display="根目录\\"+path_display;
	path_hidden="根目录\\"+path_hidden;
	document.getElementById("path_group").innerHTML="";
	document.getElementById("path_group").innerHTML="当前组名："+path_display;
	all_parent_group = path_hidden;
	node.select();
	contextmenu.showAt(e.getXY());
	});
	
	});	
	function setPath(node)
	{	
		path_display = '[<a href="javascript:showInfo('+node.id+')">'+node.text+'</a>]'+"\\"+path_display;
		path_hidden = node.text+"\\"+path_hidden;
		var v = node.parentNode;
		if(v.id!=0)
		{
			setPath(v);
		}
	}
	function clickTree(node)
	{
	
			var groupId = node.id;
			alert(groupId);
	
	}
	function showInfo(id)
	{
		ShowDialog("OAFavorite.do?operation=$globals.getOP("OP_OA_VIEW_SINLE")&type=OAFavoriteGroup&groupId="+id+"&groupNames="+path_hidden);
	}
	//全选方法
	function selectAll(boolvalue)
	{
		var allCheckBoxs=document.getElementsByName("cb");
		for(var i=0;i<allCheckBoxs.length;i++)
		{
			if(allCheckBoxs[i].type=="checkbox")
			allCheckBoxs[i].checked=boolvalue;
		}
	}
	//增加收藏夹
	function addPrepareOAFavorite()
	{
		if(nodeId!="0")
		{
		document.location = "OAFavorite.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=OAFavorite&groupId="+nodeId+"&groupNames="+all_parent_group;
		}else
		{
		alert('请选择选择所属组');
		}
	}
	//删除收藏夹
	function delOAFavorite()
	{
		var allCheckBoxs=document.getElementsByName("cb");
		var favoriteIds="";
		for(var i=0;i<allCheckBoxs.length;i++)
		{
			if(allCheckBoxs[i].type=="checkbox")
			if(allCheckBoxs[i].checked==true)
			{
				favoriteIds+=allCheckBoxs[i].value+","
			}
			
		}
		if(favoriteIds!="" && favoriteIds.length!=0)
		{
		favoriteIds = favoriteIds.substr(0,favoriteIds.length-1);
		alert(favoriteIds);
		document.location = "OAFavorite.do?operation=$globals.getOP("OP_DELETE")&type=OAFavorite&favoriteIds="+favoriteIds;
		}else
		{
		alert("未选择需要删除的选项");		
		}
		
	}
	//修改收藏夹
	function updateFavorit(favoriteId)
	{
			document.location = "OAFavorite.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=OAFavorite&favoriteId="+favoriteId;
	}