package com.koron.hr.textUpload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

/**
 * txt 打卡记录文件导入
 * @author cary
 * @category 2012-08-25
 */
public class ImportTextUpload extends DBManager {

	public void addTextData(final String[] str) throws Exception {
		final Result rs = new Result();
		@SuppressWarnings("unused")
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						// TODO Auto-generated method stub
						try {
							String sql = "insert into tblBrushCardAnnal(id,annalNo,dutyCardTime,createBy,createTime,lastUpdateTime) values(?,?,?,?,?,?)";
							PreparedStatement st = conn.prepareStatement(sql);
							SimpleDateFormat sdmat = null;
							SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if (str.length > 0) {
								for (int i = 0; i < str.length; i++) {
									st.setString(1, IDGenerater.getId());
									st.setString(2, str[1]);
									try{
										sdmat = new SimpleDateFormat("yyyy-mm-dd");
										ParsePosition pos = new ParsePosition(0);
										sdmat.setLenient(false);
										Date date = sdmat.parse(str[2],pos);
										if(pos.getIndex() > sdmat.format(date).length()){
											return;
										}
										st.setString(3, str[2] + " " + str[3]);
									}catch(Exception e){
										System.out.println("文件日期有误！");
										e.printStackTrace();
										rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
										rs.setRetVal(e.getMessage());
										return;
									}
									st.setString(4, str[1]);
									st.setString(5, dateformat.format(new Date()));
									st.setString(6, dateformat.format(new Date()));
								}
								st.execute();
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			throw new BusinessException("import.card.annals.faliure");
		}
	}
}
