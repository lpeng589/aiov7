--2018-03-19 10:58:12:修改表

 update tblDBTableInfo set lastUpdateTime='2018-03-19 10:58:12' where tableName='tblexpenseaccount'

 update tblDBFieldInfo set inputType='8',inputTypeOld='8',fieldSysType='Normal' where tableid=(select id from tblDBTableInfo where tableName='tblexpenseaccount') and fieldName = 'BillNo'

--2018-03-23 14:32:21:修改表

 update tblDBTableInfo set lastUpdateBy='1',lastUpdateTime='2018-03-23 14:32:21' where tableName='tblSalesOrder'

 update tblDBFieldInfo set inputValue='' where tableid=(select id from tblDBTableInfo where tableName='tblSalesOrder') and fieldName = 'StockCode'

--2018-03-23 14:39:10:修改表

 update tblDBTableInfo set lastUpdateTime='2018-03-23 14:39:10' where tableName='tblSalesOrder'

 update tblDBFieldInfo set inputType='0',inputTypeOld='0' where tableid=(select id from tblDBTableInfo where tableName='tblSalesOrder') and fieldName = 'StockCode'

--2018-03-23 14:41:34:修改表

 update tblDBTableInfo set lastUpdateTime='2018-03-23 14:41:34' where tableName='tblSalesOrder'

 update tblDBFieldInfo set inputType='3',inputTypeOld='3' where tableid=(select id from tblDBTableInfo where tableName='tblSalesOrder') and fieldName = 'StockCode'

--2018-03-23 14:48:54:修改表

 update tblDBTableInfo set lastUpdateTime='2018-03-23 14:48:54' where tableName='tblSalesOrderDet'

 update tblDBFieldInfo set inputType='2',inputTypeOld='2' where tableid=(select id from tblDBTableInfo where tableName='tblSalesOrderDet') and fieldName = 'StockCode'

 update tblDBFieldInfo set listOrder='57' where tableid=(select id from tblDBTableInfo where tableName='tblSalesOrderDet') and fieldName = 'PlanQty'

