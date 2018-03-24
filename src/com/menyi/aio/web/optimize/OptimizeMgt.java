package com.menyi.aio.web.optimize;

import java.util.*;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.ConnectionEnv;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.DBUtil;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;

import org.hibernate.Session;
import java.sql.PreparedStatement;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import java.text.SimpleDateFormat;
import com.menyi.web.util.SecurityLock;
import com.menyi.web.util.BaseEnv;

/**
 * <p>Title: </p>
 *
 * <p>Description: 单位管理的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class OptimizeMgt extends DBManager {

	public Result freeProcCache() {
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "dbcc freeProcCache ";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        psmt.execute();
                        
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;

    }  
	public Result indexFragmentation() {
        final Result res = new Result();
        String sql =
            "set nocount on \n" +
            " --使用游标重新组织指定库中的索引,消除索引碎片 \n" +
            "--R_T层游标取出当前数据库所有表 \n" +
            " declare @dbid int,@objid int \n" +
            " set @dbid = DB_ID() \n" +
            "declare R_T cursor \n" +
            "for select name from sys.tables \n" +
            "declare @T varchar(50) \n" +
            "open r_t \n" +
            "fetch next from r_t into @t \n" +
            "while @@fetch_status=0 \n" +
            "begin \n" +
            "--R_index游标判断指定表索引碎片情况并优化 \n" +
            "set @objid = object_id(@T) \n" +
            "declare R_Index cursor \n" +
            "for select t.name,i.name,s.avg_fragmentation_in_percent from sys.tables t \n" +
            "  join sys.indexes i on i.object_id=t.object_id \n" +
            "  join sys.dm_db_index_physical_stats(@dbid,@objid,null,null,'limited') s \n" +
            "   on s.object_id=i.object_id and s.index_id=i.index_id \n" +
            "   where s.avg_fragmentation_in_percent >10 \n" +
            " declare @TName varchar(50),@IName varchar(50),@avg int,@str varchar(500) \n" +
            "open r_index \n" +
            "fetch next from r_index into @TName,@Iname,@avg \n" +
            "while @@fetch_status=0 \n" +
            "begin \n" +
            "  if @avg>=30  --如果碎片大于30,重建索引 \n" +
            "  begin \n" +
            "   set @str='alter index '+rtrim(@Iname)+' on dbo.'+rtrim(@tname)+' rebuild' \n" +
            "  end \n" +
            "  else   --如果碎片小于30,重新组织索引 \n" +
            "  begin \n" +
            "   set @STR='alter index '+rtrim(@Iname)+' on dbo.'+rtrim(@tname)+' reorganize' \n" +
            "  end \n" +
            "  exec (@str)  --执行 \n" +
            "  fetch next from r_index into @TName,@Iname,@avg \n" +
            "end \n" +
            "--结束r_index游标 \n" +
            "close r_index \n" +
            "deallocate r_index \n" +
            "fetch next from r_t into @t \n" +
            "end \n" +
            "--结束R_T游标 \n" +
            "close r_t \n" +
            "deallocate r_t \n" +
            "set nocount off \n";
        try{	
        	Connection conn = ConnectionEnv.getConnection();
        	try{
	        	PreparedStatement psmt = conn.prepareStatement(sql);
	        	psmt.execute();
        	}catch(Exception e){
            	e.printStackTrace();
            	res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            	res.retVal=e.getMessage();
            }
        	conn.close();
        }catch(Exception e){
        	e.printStackTrace();
        	res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        	res.retVal=e.getMessage();
        }
        return res;

    } 
	
	
    public Result runTime() {
        final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String oldCatalog = conn.getCatalog();
                        conn.setCatalog("master");
                        try{
	                        String sql =
	                            " SELECT  db_name(st.dbid ) N'数据库名称'" +
	                            "        ,last_execution_time N'执行时间'" +
	                            "        ,total_physical_reads N'物理读取总次数'" +
	                            "        ,total_logical_reads/execution_count N'每次逻辑读次数'" +
	                            "        ,total_logical_writes N'逻辑写入总次数'" +
	                            "        , execution_count  N'执行次数'" +
	                            "        , total_worker_time/1000 N'所用的CPU总时间ms'" +
	                            "        , total_elapsed_time/1000  N'总花费时间ms'" +
	                            "        , (total_elapsed_time / execution_count)/1000  N'平均时间ms'" +
	                            "        ,SUBSTRING(st.text, (qs.statement_start_offset/2) + 1," +
	                            "         ((CASE statement_end_offset " +
	                            "          WHEN -1 THEN DATALENGTH(st.text)" +
	                            "          ELSE qs.statement_end_offset END " +
	                            "            - qs.statement_start_offset)/2) + 1) N'执行语句'" +
	                            "          " +
	                            " FROM sys.dm_exec_query_stats AS qs" +
	                            " CROSS APPLY master.sys.dm_exec_sql_text(qs.sql_handle) st" +
	                            " where SUBSTRING(st.text, (qs.statement_start_offset/2) + 1," +
	                            "         ((CASE statement_end_offset " +
	                            "          WHEN -1 THEN DATALENGTH(st.text)" +
	                            "          ELSE qs.statement_end_offset END " +
	                            "            - qs.statement_start_offset)/2) + 1) not like '%fetch%' and (total_elapsed_time / execution_count)/1000 >10" +
	                            " ORDER BY  total_elapsed_time / execution_count DESC";
	
	                        PreparedStatement psmt = conn.prepareStatement(sql);
	                        ResultSet rst = psmt.executeQuery();
	                        ArrayList list = new ArrayList();
	                        res.retVal = list;
							Object[] os = new Object[rst.getMetaData()
									.getColumnCount()];
							for (int i = 1; i <= os.length; i++) {
								os[i - 1] = rst.getMetaData().getColumnName(i);
							}
							list.add(os);
	                        while (rst.next()) {
								os = new Object[rst.getMetaData()
										.getColumnCount()];
								for (int i = 1; i <= os.length; i++) {
									os[i - 1] = rst.getObject(i);
								}
								list.add(os);
							}
                        }catch(Exception e){
                        	e.printStackTrace();
                        }
                        //恢复原catalog
                        conn.setCatalog(oldCatalog);
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }
    public Result tableRows() {
       final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try{
                			
                			CallableStatement cs = conn.prepareCall("{call proc_parseTableInfo()}");
                            ResultSet rst = cs.executeQuery();
                            
	                        ArrayList list = new ArrayList();
	                        res.retVal = list;
							Object[] os = new Object[rst.getMetaData()
									.getColumnCount()];
							for (int i = 1; i <= os.length; i++) {
								os[i - 1] = rst.getMetaData().getColumnName(i);
							}
							list.add(os);
	                        while (rst.next()) {
								os = new Object[rst.getMetaData()
										.getColumnCount()];
								for (int i = 1; i <= os.length; i++) {
									os[i - 1] = rst.getObject(i);
								}
								list.add(os);
							}
                        }catch(Exception e){
                        	e.printStackTrace();
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }
    
    public Result tableInfo(String tableName) {
        final Result res = new Result();         
         int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                 session.doWork(new Work() {
                     public void execute(Connection connection) throws
                         SQLException {
                         Connection conn = connection;
                         try{
                 			String sql = "SELECT     " +
                 					"    Column_id=C.column_id," +
                 					"    ColumnName=C.name," +
                 					"    PrimaryKey=ISNULL(IDX.PrimaryKey,N'')," +
                 					"    [IDENTITY]=CASE WHEN C.is_identity=1 THEN N'√'ELSE N'' END," +
                 					"    Computed=CASE WHEN C.is_computed=1 THEN N'√'ELSE N'' END," +
                 					"    Type=T.name," +
                 					"    Length=C.max_length," +
                 					"    Precision=C.precision," +
                 					"    Scale=C.scale," +
                 					"    NullAble=CASE WHEN C.is_nullable=1 THEN N'√'ELSE N'' END," +
                 					"    [Default]=ISNULL(D.definition,N'')," +
                 					"    ColumnDesc=cast(ISNULL(PFD.[value],N'') as varchar(200))," +
                 					"    IndexName=ISNULL(IDX.IndexName,N'')," +
                 					"    IndexSort=ISNULL(IDX.Sort,N'')," +
                 					"    Create_Date=O.Create_Date," +
                 					"    Modify_Date=O.Modify_date" +
                 					" FROM sys.columns C" +
                 					"    INNER JOIN sys.objects O" +
                 					"        ON C.[object_id]=O.[object_id]" +
                 					"            AND O.type='U'" +
                 					"            AND O.is_ms_shipped=0" +
                 					"    INNER JOIN sys.types T" +
                 					"        ON C.user_type_id=T.user_type_id" +
                 					"    LEFT JOIN sys.default_constraints D" +
                 					"        ON C.[object_id]=D.parent_object_id" +
                 					"            AND C.column_id=D.parent_column_id           " +
                 					" AND C.default_object_id=D.[object_id]" +
                 					"" +
                 					"    LEFT JOIN sys.extended_properties PFD" +
                 					"        ON PFD.class=1 " +
                 					"            AND C.[object_id]=PFD.major_id " +
                 					"            AND C.column_id=PFD.minor_id" +
                 					"" +
                 					"    LEFT JOIN                       (" +
                 					"        SELECT IDXC.[object_id], IDXC.column_id," +
                 					" Sort=CASE INDEXKEY_PROPERTY(IDXC.[object_id],IDXC.index_id,IDXC.index_column_id,'IsDescending')" +
                 					"                WHEN 1 THEN 'DESC' WHEN 0 THEN 'ASC' ELSE '' END," +
                 					"            PrimaryKey=CASE WHEN IDX.is_primary_key=1 THEN N'√'ELSE N'' END," +
                 					"            IndexName=IDX.Name" +
                 					"        FROM sys.indexes IDX" +
                 					"        INNER JOIN sys.index_columns IDXC" +
                 					"            ON IDX.[object_id]=IDXC.[object_id]" +
                 					"                AND IDX.index_id=IDXC.index_id" +
                 					"        LEFT JOIN sys.key_constraints KC" +
                 					"            ON IDX.[object_id]=KC.[parent_object_id]" +
                 					"                AND IDX.index_id=KC.unique_index_id" +
                 					"        INNER JOIN  " +
                 					"        (            SELECT [object_id], Column_id, index_id=MIN(index_id)" +
                 					"            FROM sys.index_columns            GROUP BY [object_id], Column_id        ) IDXCUQ" +
                 					"            ON IDXC.[object_id]=IDXCUQ.[object_id] AND IDXC.Column_id=IDXCUQ.Column_id" +
                 					"                AND IDXC.index_id=IDXCUQ.index_id    ) IDX        ON C.[object_id]=IDX.[object_id]" +
                 					"            AND C.column_id=IDX.column_id" +
                 					"" +
                 					" WHERE O.name=N'tblStockDet'       " +
                 					" ORDER BY O.name,C.column_id ";
                 			PreparedStatement cs = conn.prepareStatement(sql);
                             ResultSet rst = cs.executeQuery();
                             
 	                        ArrayList list = new ArrayList();
 	                        res.retVal = list;
 							Object[] os = new Object[rst.getMetaData()
 									.getColumnCount()];
 							
 	                        while (rst.next()) {
 								os = new Object[rst.getMetaData()
 										.getColumnCount()];
 								for (int i = 1; i <= os.length; i++) {
 									os[i - 1] = rst.getObject(i);
 								}
 								list.add(os);
 							}
                         }catch(Exception e){
                         	e.printStackTrace();
                         }
                     }
                 });
                 return res.getRetCode();
             }
         });
         res.setRetCode(retCode);
         return res;
    }
    
    public Result getTableDesc(final String tableName) {
        final Result res = new Result();
          
        int retCode = DBUtil.execute(new IfDB() {
              public int exec(Session session) {
                  session.doWork(new Work() {
                      public void execute(Connection connection) throws
                          SQLException {
                          Connection conn = connection;
                          try{
  	                        String sql =
  	                            "select cast(isnull(pfd.[value],'') AS VARCHAR(200)) AS '备注' " +
  	                            " from sys.extended_properties PFD " +
  	                            " WHERE PFD.class=1 AND OBJECT_ID(?)=PFD.major_id AND PFD.minor_id=0";
  	
  	                        PreparedStatement psmt = conn.prepareStatement(sql);
  	                        psmt.setString(1, tableName);
  	                        
  	                        ResultSet rst = psmt.executeQuery();
  	                        while (rst.next()) {  								
  								res.retVal = rst.getString(1);
  							}
                          }catch(Exception e){
                          	e.printStackTrace();
                          }
                      }
                  });
                  return res.getRetCode();
              }
          });
          res.setRetCode(retCode);
          return res;
      }
    
    public Result getTableIndex(final String tableName) {
        final Result res = new Result();
          
        int retCode = DBUtil.execute(new IfDB() {
              public int exec(Session session) {
                  session.doWork(new Work() {
                      public void execute(Connection connection) throws
                          SQLException {
                          Connection conn = connection;
                          try{
  	                        String sql =
  	                            "BEGIN \n" +
  	                            "        WITH tx AS\n" +
  	                            "        (\n" +
  	                            "                SELECT a.object_id\n" +
  	                            "                      ,b.name AS schema_name\n" +
  	                            "                      ,a.name AS table_name\n" +
  	                            "                      ,c.name as ix_name\n" +
  	                            "                     ,c.is_unique AS ix_unique\n" +
  	                            "                      ,c.type_desc AS ix_type_desc\n" +
  	                            "                      ,d.index_column_id\n" +
  	                            "                      ,d.is_included_column\n" +
  	                            "                      ,e.name AS column_name\n" +
  	                            "                      ,f.name AS fg_name\n" +
  	                            "                      ,d.is_descending_key AS is_descending_key\n" +
  	                            "                     ,c.is_primary_key\n" +
  	                            "                      ,c.is_unique_constraint\n" +
  	                            "                 FROM sys.tables AS a\n" +
  	                            "                 INNER JOIN sys.schemas AS b            ON a.schema_id = b.schema_id AND a.is_ms_shipped = 0\n" +
  	                            "                 INNER JOIN sys.indexes AS c            ON a.object_id = c.object_id\n" +
  	                            "                 INNER JOIN sys.index_columns AS d      ON d.object_id = c.object_id AND d.index_id = c.index_id\n" +
  	                            "                 INNER JOIN sys.columns AS e            ON e.object_id = d.object_id AND e.column_id = d.column_id\n" +
  	                            "                 INNER JOIN sys.data_spaces AS f        ON f.data_space_id = c.data_space_id\n" +
  	                            "        )\n" +
  	                            "        \n" +
  	                            "        SELECT\n" +
  	                            "               CASE WHEN a.ix_unique = 1 THEN 'UNIQUE' END AS '唯一索引'\n" +
  	                            "              ,a.ix_type_desc as '类型'\n" +
  	                            "              ,a.ix_name as '名称'\n" +
  	                            "              ,indexColumns.ix_index_column_name as '索引列'\n" +
  	                            "              ,IncludeIndex.ix_included_column_name as '包含列'\n" +
  	                            "              ,a.is_primary_key as '主键'\n" +
  	                            "        FROM\n" +
  	                            "        (\n" +
  	                            "                SELECT DISTINCT\n" +
  	                            "                       ix_unique\n" +
  	                            "                      ,ix_type_desc\n" +
  	                            "                      ,ix_name\n" +
  	                            "                      ,schema_name\n" +
  	                            "                      ,table_name\n" +
  	                            "                      ,fg_name\n" +
  	                            "                      ,is_primary_key\n" +
  	                            "                      ,is_unique_constraint\n" +
  	                            "                  FROM tx\n" +
  	                            "        ) AS a\n" +
  	                            "        OUTER APPLY\n" +
  	                            "        (\n" +
  	                            "                SELECT ix_index_column_name\n" +
  	                            "                       = STUFF((\n" +
  	                            "                                SELECT ',' + column_name + CASE WHEN is_descending_key = 1 THEN ' DESC' ELSE '' END\n" +
  	                            "                                  FROM tx AS b\n" +
  	                            "                                 WHERE schema_name = a.schema_name\n" +
  	                            "                                   AND table_name=a.table_name\n" +
  	                            "                                   AND ix_name=a.ix_name\n" +
  	                            "                                   AND ix_type_desc=a.ix_type_desc\n" +
  	                            "                                   AND fg_name=a.fg_name\n" +
  	                            "                                   AND is_included_column=0\n" +
  	                            "                                 ORDER BY index_column_id\n" +
  	                            "                                   FOR XML PATH('')\n" +
  	                            "                                ),1,1,'')\n" +
  	                            "        )IndexColumns\n" +
  	                            "        OUTER APPLY\n" +
  	                            "        (\n" +
  	                            "                SELECT ix_included_column_name\n" +
  	                            "                       = STUFF((\n" +
  	                            "                                SELECT ',' + column_name\n" +
  	                            "                                  FROM tx AS b\n" +
  	                            "                                 WHERE schema_name = a.schema_name\n" +
  	                            "                                   AND table_name=a.table_name\n" +
  	                            "                                   AND ix_name=a.ix_name\n" +
  	                            "                                   AND ix_type_desc=a.ix_type_desc\n" +
  	                            "                                   AND fg_name=a.fg_name\n" +
  	                            "                                   AND is_included_column=1\n" +
  	                            "                                 ORDER BY index_column_id\n" +
  	                            "                                   FOR XML PATH('')\n" +
  	                            "                                ), 1,1,'')\n" +
  	                            "        )IncludeIndex\n" +
  	                            "        WHERE a.table_name=?\n" +
  	                            "        ORDER BY a.schema_name,a.table_name,a.ix_name;\n" +
  	                            "END ";
  	
  	                        PreparedStatement psmt = conn.prepareStatement(sql);
  	                        psmt.setString(1, tableName);
  	                        ResultSet rst = psmt.executeQuery();
  	                        ArrayList list = new ArrayList();
  	                        res.retVal = list;
  							Object[] os = new Object[rst.getMetaData()
  									.getColumnCount()];
  							for (int i = 1; i <= os.length; i++) {
  								os[i - 1] = rst.getMetaData().getColumnName(i);
  							}
  							list.add(os);
  	                        while (rst.next()) {
  								os = new Object[rst.getMetaData()
  										.getColumnCount()];
  								for (int i = 1; i <= os.length; i++) {
  									os[i - 1] = rst.getObject(i);
  								}
  								list.add(os);
  							}
                          }catch(Exception e){
                          	e.printStackTrace();
                          }
                      }
                  });
                  return res.getRetCode();
              }
          });
          res.setRetCode(retCode);
          return res;
      }
    
    public Result saveDesc(final String tableName,final ArrayList<String[]> descs,final String savePath) {
        final Result res = new Result();
        
        
        int retCode = DBUtil.execute(new IfDB() {
              public int exec(Session session) {
                  session.doWork(new Work() {
                      public void execute(Connection connection) throws
                          SQLException {                    	  
                          Connection conn = connection;
                          try{
                        	  FileOutputStream fos = new FileOutputStream(savePath,true);
                        	  StringBuffer sb= new StringBuffer();
                        	  
                        	  sb.append(" --以下每一段请分开拷贝到脚本库中\n " +
                        	  		"IF(EXISTS ( select *  from sys.extended_properties PFD  WHERE PFD.class=1 AND OBJECT_ID('tblStockDet')=PFD.major_id AND PFD.minor_id=0)) \n" +
                        	  		" EXEC sys.sp_dropextendedproperty @name=N'MS_Description' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'tblStockDet' \n" +
                        	  		" DECLARE @columnName VARCHAR(100) \n" +
                        	  		" DECLARE remCusor CURSOR LOCAL FOR \n" +
                        	  		" SELECT c.NAME FROM sys.columns C \n" +
                        	  		" JOIN sys.extended_properties PFD  on PFD.class=1 AND c.object_id=PFD.major_id AND PFD.minor_id=C.column_id \n" +
                        	  		" WHERE OBJECT_ID('tblstockdet')=PFD.major_id \n" +
                        	  		" OPEN remCusor \n" +
                        	  		" FETCH remCusor INTO @columnName \n" +
                        	  		" while(@@FETCH_STATUS=0) \n" +
                        	  		" BEGIN \n" +
                        	  		"	EXEC sys.sp_dropextendedproperty @name=N'MS_Description' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'tblStockDet',@level2type=N'COLUMN', @level2name=@columnName \n" +
                        	  		"	FETCH remCusor INTO @columnName \n" +
                        	  		" END \n" +
                        	  		" CLOSE remCusor \n");
                        	  
                        	  for(String[] desc:descs){
                        		  try{
                        			  CallableStatement cs = conn.prepareCall("{call sp_dropextendedproperty(?,?,?,?,?,?,?)}");
		                              cs.setString(1, "MS_Description");
		                              cs.setString(2, "SCHEMA");
		                              cs.setString(3, "dbo");
		                              cs.setString(4, "TABLE");
		                              cs.setString(5, tableName);
		                              cs.setString(6, desc[0]==null?null:"COLUMN");
		                              cs.setString(7, desc[0]);
		                              cs.execute();
                        		  }catch(Exception e){
                        			  
                        		  }
                        		  
                        		  if(desc[1] != null && desc[1].length() > 0){
		                        	  CallableStatement cs = conn.prepareCall("{call sp_addextendedproperty(?,?,?,?,?,?,?,?)}");
		                              cs.setString(1, "MS_Description");
		                              cs.setString(2, desc[1]);
		                              cs.setString(3, "SCHEMA");
		                              cs.setString(4, "dbo");
		                              cs.setString(5, "TABLE");
		                              cs.setString(6, tableName);
		                              cs.setString(7, desc[0]==null?null:"COLUMN");
		                              cs.setString(8, desc[0]);
		                              cs.execute();
		                              
		                              if(desc[0]==null){
		                            	  sb.append("EXECUTE sp_addextendedproperty N'MS_Description', '"+desc[1]+"', N'SCHEMA', N'dbo', N'TABLE', N'"+tableName+"' \n");
		                              }else{
		                            	  sb.append("EXECUTE sp_addextendedproperty N'MS_Description', '"+desc[1]+"', N'SCHEMA', N'dbo', N'TABLE', N'"+tableName+"', N'COLUMN', N'"+desc[0]+"'\n");
		                              }
                        		  }
                        	  }
                        	  sb.append("\n\n");
                              fos.write(sb.toString().getBytes());
                              fos.close();
  	                        
                          }catch(Exception e){
                          	e.printStackTrace();
                          }
                      }
                  });
                  return res.getRetCode();
              }
          });
          res.setRetCode(retCode);
          return res;
      }
    
    public Result indexUse() {
      final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try{
	                        String sql =
	                            "declare @dbid int set @dbid = DB_ID() " +
	                            "select object_name(s.object_id) AS '表名', i.name AS '索引名称' ,CAST( p.avg_fragmentation_in_percent as numeric(18,2)) as '碎片'" +
	                            "   , user_seeks AS  '通过用户查询执行的搜索次数', user_scans AS  '通过用户查询执行的扫描次数', user_lookups AS  '通过用户查询执行的查找次数', user_updates AS  '通过用户查询执行的更新次数'" +
	                            " from sys.dm_db_index_usage_stats s " +
	                            " JOIN sys.indexes i ON i.object_id = s.object_id AND i.index_id = s.index_id " +
	                            " JOIN sys.dm_db_index_physical_stats(@dbid,null,null,null,'limited') p ON P.object_id = s.object_id AND P.index_id = s.index_id  " +
	                            " where S.database_id = db_id() and objectproperty(s.object_id,'IsUserTable') = 1" +
	                            " order by (user_seeks + user_scans + user_lookups + user_updates) desc";
	
	                        PreparedStatement psmt = conn.prepareStatement(sql);
	                        ResultSet rst = psmt.executeQuery();
	                        ArrayList list = new ArrayList();
	                        res.retVal = list;
							Object[] os = new Object[rst.getMetaData()
									.getColumnCount()];
							for (int i = 1; i <= os.length; i++) {
								os[i - 1] = rst.getMetaData().getColumnName(i);
							}
							list.add(os);
	                        while (rst.next()) {
								os = new Object[rst.getMetaData()
										.getColumnCount()];
								for (int i = 1; i <= os.length; i++) {
									os[i - 1] = rst.getObject(i);
								}
								list.add(os);
							}
                        }catch(Exception e){
                        	e.printStackTrace();
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }
    public Result block() {
       final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String oldCatalog = conn.getCatalog();
                        conn.setCatalog("master");
                        try{
	                        String sql =
	                            "    select distinct '阻塞或被阻塞语句' = '引起阻塞的语句'," +
	                            "    '进程ID'              = str( a.spid, 4 )," +
	                            "    '进程ID状态'          = convert( char(10), a.status )," +
	                            "    '分块进程的进程ID'    = str( a.blocked, 2 )," +
	                            "    '工作站名称'          = convert( char(10), a.hostname )," +
	                            "    '执行命令的用户'      = convert( char(10), suser_name( a.uid ) )," +
	                            "    '数据库名'            = convert( char(10), db_name(a.dbid ) )," +
	                            "    '应用程序名'          = convert( char(10), a.program_name )," +
	                            "    '正在执行的命令'      = convert( char(16), a.cmd )," +
	                            "    '累计CPU时间'         = str( a.cpu, 7 )," +
	                            "    'IO'                  = str( a.physical_io, 7 )," +
	                            "        '登录名'              = a.loginame," +
	                            "    '执行语句'=b.text" +
	                            "    from master..sysprocesses a" +
	                            "    cross apply sys.dm_exec_sql_text(a.sql_handle) b" +
	                            "    where spid in ( select blocked from master..sysprocesses )" +
	                            "    and blocked = 0" +
	                            "	union" +
	                            "    select '阻塞或被阻塞语句'='被阻塞的等待执行的语句'," +
	                            "    '进程ID'              = str( a.spid, 4 )," +
	                            "    '进程ID状态'          = convert( char(10), a.status )," +
	                            "    '分块进程的进程ID'    = str( a.blocked, 2 )," +
	                            "    '工作站名称'          = convert( char(10), a.hostname )," +
	                            "    '执行命令的用户'      = convert( char(10), suser_name( a.uid ) )," +
	                            "    '数据库名'            = convert( char(10), db_name(a.dbid ) )," +
	                            "    '应用程序名'          = convert( char(10), a.program_name )," +
	                            "    '正在执行的命令'      = convert( char(16), a.cmd )," +
	                            "    '累计CPU时间'         = str( a.cpu, 7 )," +
	                            "    'IO'                  = str( a.physical_io, 7 )," +
	                            "        '登录名'              = a.loginame," +
	                            "    '执行语句'=b.text" +
	                            "    from master..sysprocesses a" +
	                            "    cross apply sys.dm_exec_sql_text(a.sql_handle) b" +
	                            "    where blocked <> 0";
	
	                        PreparedStatement psmt = conn.prepareStatement(sql);
	                        ResultSet rst = psmt.executeQuery();
	                        ArrayList list = new ArrayList();
	                        res.retVal = list;
							Object[] os = new Object[rst.getMetaData()
									.getColumnCount()];
							for (int i = 1; i <= os.length; i++) {
								os[i - 1] = rst.getMetaData().getColumnName(i);
							}
							list.add(os);
	                        while (rst.next()) {
								os = new Object[rst.getMetaData()
										.getColumnCount()];
								for (int i = 1; i <= os.length; i++) {
									os[i - 1] = rst.getObject(i);
								}
								list.add(os);
							}
                        }catch(Exception e){
                        	e.printStackTrace();
                        }
                        //恢复原catalog
                        conn.setCatalog(oldCatalog);
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }
    
    public Result acc() {
        final Result res = new Result();
         
         int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                 session.doWork(new Work() {
                     public void execute(Connection connection) throws
                         SQLException {
                         Connection conn = connection;
                         try{
 	                        String sql =
 	                            "SELECT BillDate '单据日期'," +
 	                            " '<a href=/UserFunctionAction.do?tableName=tblAccMain&keyId='+amid+'&operation=5&noback=true&queryChannel=normal&LinkType=@URL: target=_blank>'+CredTypeID+'</a>' as '凭证号'," +
 	                            " '<a href=/UserFunctionAction.do?tableName='+RefBillType+'&keyId='+RefBillID+'&operation=5  target=_blank>'+RefBillNo+'</a>' as '关联单号'," +
 	                            "zh_CN '单据类型',RefBillType '单据类型',RecordComment '摘要'," +
 	                            " sdebitAmount '借',slendAmount '贷', sdebitAmount - slendAmount '差额'" +
 	                            " FROM (" +
 	                            "select tblAccMain.BillDate as BillDate," +
 	                            "    (tblAccMain.CredTypeID+'_'+cast(tblAccMain.OrderNo as varchar(30))) as CredTypeID," +
 	                            "    (case ISNULL(tblAccMain.RefBillNo,tblAccMain.RefBillID) when 'settleAcc' then '月结' else ISNULL(tblAccMain.RefBillNo,'') end) as RefBillNo," +
 	                            "    (case isnull(tblAccMain.RefBillType,'') when 'ProfitLossCarryForward' then (Select languageid from tblDBEnumerationItem where enumid=(Select id from tblDBEnumeration where enumname='CommEnum') and enumValue='Profityear') WHEN 'adjustExchange' THEN (Select languageid from tblDBEnumerationItem where enumid=(Select id from tblDBEnumeration where enumname='CommEnum') and enumValue='EndRate') when '' then (Select languageid from tblDBEnumerationItem where enumid=(Select id from tblDBEnumeration where enumname='CommEnum') and enumValue='hv') else tblDBTableInfo.languageId end) as modelName," +
 	                            "    tblAccDetail.RecordComment as RecordComment," +
 	                            "    tblAccMain.Period as Period," +
 	                            "    (case tblAccMain.RefBillID when 'settleAcc' then tblAccMain.id else tblAccMain.RefBillID end) as RefBillID," +
 	                            "    (case tblAccMain.RefBillID  when 'settleAcc' then 'tblAccMain' else tblAccMain.RefBillType end) as RefBillType," +
 	                            " tblAccMain.id as amid," +
 	                            "    (SELECT SUM(LendAmount)  FROM tblAccDetail d WHERE d.f_ref=tblAccMain.id ) as  slendAmount," +
 	                            "    (SELECT SUM(DebitAmount) FROM tblAccDetail d WHERE d.f_ref=tblAccMain.id ) as sdebitAmount" +
 	                            " from tblAccMain " +
 	                            " left join tblAccDetail on (tblAccDetail.f_ref=tblAccMain.id and tblAccDetail.id=(select top 1 a.id from tblAccDetail a where a.f_ref=tblAccMain.id ))" +
 	                            " left join tblDBTableInfo on tblDBTableInfo.tableName=tblAccMain.RefBillType" +
 	                            " left join tblAccTypeInfo on tblAccTypeInfo.AccNumber=tblAccDetail.AccCode" +
 	                            " ) ta  join tbllanguage l ON ta.modelName=l.id" +
 	                            " WHERE slendAmount<>sdebitAmount";
 	                        System.out.println(sql);
 	                        PreparedStatement psmt = conn.prepareStatement(sql);
 	                        ResultSet rst = psmt.executeQuery();
 	                        ArrayList list = new ArrayList();
 	                        res.retVal = list;
 							Object[] os = new Object[rst.getMetaData()
 									.getColumnCount()];
 							for (int i = 1; i <= os.length; i++) {
 								os[i - 1] = rst.getMetaData().getColumnName(i);
 							}
 							list.add(os);
 	                        while (rst.next()) {
 								os = new Object[rst.getMetaData()
 										.getColumnCount()];
 								for (int i = 1; i <= os.length; i++) {
 									os[i - 1] = rst.getObject(i);
 								}
 								list.add(os);
 							}
                         }catch(Exception e){
                         	e.printStackTrace();
                         }
                     }
                 });
                 return res.getRetCode();
             }
         });
         res.setRetCode(retCode);
         return res;
     }
    
	public Result bug() {
        final Result res = new Result();
        res.retVal = "<br/>";
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "{call proc_BugCheck()} ";

                        CallableStatement stmt = conn.prepareCall(sql);
                        stmt.execute();
                        SQLWarning warn = stmt.getWarnings();

                        while(warn != null){
                        	
                        	if(warn.getMessage().indexOf("无游标") == -1)
                        		res.retVal = res.retVal.toString() + warn.getMessage()+"<br/>";
                        	warn = warn.getNextWarning();
                        }
                        
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;

    }
	public Result certHistory() {
final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try{
                        	
	                        String sql =
	                            "select * from tblCertLog order by id ";
	
	                        PreparedStatement psmt = conn.prepareStatement(sql);
	                        ResultSet rst = psmt.executeQuery();
	                        ArrayList list = new ArrayList();
	                        res.retVal = list;
							Object[] os = new Object[rst.getMetaData()
									.getColumnCount()];
							for (int i = 1; i <= os.length; i++) {
								os[i - 1] = rst.getMetaData().getColumnName(i);
							}
							list.add(os);
	                        while (rst.next()) {
								os = new Object[rst.getMetaData()
										.getColumnCount()];
								for (int i = 1; i <= os.length; i++) {
									os[i - 1] = rst.getObject(i);
								}
								list.add(os);
							}
                        }catch(Exception e){
                        	e.printStackTrace();
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }
}
