--2016-09-21 10:57:00新增枚举
if exists(select * from tblDBEnumeration where enumName='zlBank')
begin
delete from tblLanguage where id in (select languageId from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='zlBank'))
delete from tblLanguage where id in (select languageId from tblDBEnumeration where enumName='zlBank')
delete from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='zlBank');
delete from tblDBEnumeration where enumName='zlBank';
end
insert tblDBEnumeration(id,enumName,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,mainModule,checkPersons) values('980e7065_1609211056598200026','zlBank','1','2016-09-21 10:56:59','1','2016-09-21 10:56:59','','','','','','0','980e7065_1609211056598220027','0','')
insert tblDBEnumerationItem(id,enumValue,enumId,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,checkPersons,enumOrder) values('980e7065_1609211056598220028','jsyh','980e7065_1609211056598200026','','','','','','0','980e7065_1609211056598230029','','0')
insert tblDBEnumerationItem(id,enumValue,enumId,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,checkPersons,enumOrder) values('980e7065_1609211056598230030','zsyh','980e7065_1609211056598200026','','','','','','0','980e7065_1609211056598230031','','1')

 insert into tblLanguage(id,zh_CN) values('980e7065_1609211056598220027','银企直连银行')

 insert into tblLanguage(id,zh_CN) values('980e7065_1609211056598230029','建议银行')

 insert into tblLanguage(id,zh_CN) values('980e7065_1609211056598230031','招商银行')

--2016-09-21 11:06:54修改枚举
if exists(select * from tblDBEnumeration where enumName='zlBank')
begin
delete from tblLanguage where id in (select languageId from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='zlBank'))
delete from tblLanguage where id in (select languageId from tblDBEnumeration where enumName='zlBank')
delete from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='zlBank');
delete from tblDBEnumeration where enumName='zlBank';
end
insert tblDBEnumeration(id,enumName,createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,mainModule,checkPersons) values('980e7065_1609211056598200026','zlBank','1','2016-09-21 10:56:59','1','2016-09-21 11:06:53','','','','','','0','980e7065_1609211106537830259','0','')
insert tblDBEnumerationItem(id,enumValue,enumId,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,checkPersons,enumOrder) values('980e7065_1609211106537830260','jsyh','980e7065_1609211056598200026','','','','','','0','980e7065_1609211106537830261','','0')
insert tblDBEnumerationItem(id,enumValue,enumId,SCompanyID,classCode,RowON,workFlowNodeName,workFlowNode,printCount,languageId,checkPersons,enumOrder) values('980e7065_1609211106537830262','zsyh','980e7065_1609211056598200026','','','','','','0','980e7065_1609211106537840263','','1')

 insert into tblLanguage(id,zh_CN) values('980e7065_1609211106537830259','银企直连银行')

 insert into tblLanguage(id,zh_CN) values('980e7065_1609211106537830261','建设银行')

 insert into tblLanguage(id,zh_CN) values('980e7065_1609211106537840263','招商银行')

