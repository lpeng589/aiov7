
/*
declare @a_code int
declare @a_msg varchar(255)
exec proc_validator @a_code,@a_msg
*/


--检查启用审核流单据是否都已审核
create proc proc_validator
		@a_code int out,
		@a_msg varchar(255) out 
as
declare @tableName varchar(100)
declare @ModulesName varchar(100)
declare @BillNo varchar(300)
set @a_code =0
set @a_msg=''
declare cur_WorkFlow cursor for 
select b.tableName,zh_cn from Tableinfo_wokflow  b join tblDBTableinfo a on a.tablename=b.tablename left join tbllanguage on tbllanguage.id=a.languageid  where IsStatart=1 and (select top 1 Order_ID from tblUpdateinfo where version_id<5 order by Order_ID desc)<7544
open cur_WorkFlow
fetch next from cur_WorkFlow into @tableName,@ModulesName
while(@@fetch_status=0)
begin
	declare @WorkFlowName varchar(500)
	declare @Exist varchar(500)
	declare @CTable varchar(500)
	declare @exec varchar(500)
    declare @id varchar(8000)
    declare @WBillNo varchar(8000)
	set @WorkFlowName='select ''existTable'' as Exist,'''+@ModulesName+''' as Modules,id  from ' +@tableName+' where workFlowNodeName!=''finish'' and workFlowNodeName!=''draft'' and workFlowNodeName!=''print'''
	set @exec='declare WorkFlow_Cursor cursor for '+@WorkFlowName 
	print(@exec)
	exec (@exec)
	open WorkFlow_Cursor
	fetch next from WorkFlow_Cursor into @Exist,@CTable,@id
		while(@@fetch_status=0)
		begin
			if (@Exist='existTable')
			begin
				set @BillNo=@CTable
			end
		fetch next from WorkFlow_Cursor into @Exist,@CTable,@id
		end
	deallocate WorkFlow_Cursor

		if (len(@BillNo) != 0)
		begin
				set @a_code=-1
                set @a_msg=@a_msg+'  '+@ModulesName+'有未审核单据'
	            print(@a_code)
	            print(@a_msg)	
		end
	fetch next from cur_WorkFlow into  @tableName,@ModulesName
end
deallocate cur_WorkFlow

