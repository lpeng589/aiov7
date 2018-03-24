function FileUpload_onselect(){
	var loginId = jQuery("#loginId").val();
	
	jQuery.ajaxFileUpload({
	              url:'/MyDesktopAction.do?operation=53&uploadFlag=ajaxType',            
	              secureuri:false,
	              fileElementId:'imageFile',                       
	              dataType: 'json',                                     
	              success: function (data,status){
	              		//alert(1)
	              		//location.reload();
	              	  	//jQuery("#cropbox").attr('src','/ReadFile.jpg?fileName='+loginId+'.jpg&tempFile=false&type=PIC&tableName=tblEmployee_bak').show();
						//jQuery("#previewBig").attr('src','/ReadFile.jpg?fileName='+loginId+'.jpg&tempFile=false&type=PIC&tableName=tblEmployee_bak').show();
						//jQuery("#previewSmall").attr('src','/ReadFile.jpg?fileName='+loginId+'.jpg&tempFile=false&type=PIC&tableName=tblEmployee_bak').show();
	              	 	//jcrop_api.setImage("/ReadFile.jpg?fileName='+loginId+'.jpg&tempFile=false&type=PIC&tableName=tblEmployee_bak");
	              	 	//location.reload();	              	 	     	                 
	              },
	              error: function (data,status,e){            	  
	                  asyncbox.tips('上传图片失败','error');
	              }
	          });
}