package com.koron.oa.albumTree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.AlbumBean;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.bean.PhotoInfoBean;
import com.koron.oa.directorySeting.DirectorySettingMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * 
 * <p>
 * Title: 企业相册
 * </p>
 * 
 * @Copyright: 科荣软件
 * 
 * @author 毛晶
 * 
 */
public class AlbumTreeMgt extends AIODBManager {

	/**
	 * mj 查询所有的相册 方法一
	 * 
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @param SCompanyID
	 * @param sysName
	 * @param epat
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public Result query(int pageNo, int pageSize, Boolean isNeedPage) {
		// 创建参数
		List param = new ArrayList();
		String hql = "select bean from AlbumBean as bean";
		// 调用list返回结果
		if (isNeedPage) {
			return list(hql, param, pageNo, pageSize, true);
		} else {
			return list(hql, param);
		}
	}

	/**
	 * mj 查询所有的相册 方法二
	 * 
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @param SCompanyID
	 * @param sysName
	 * @param epat
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public Result queryBySql(String orderType, int pageNo, int pageSize) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();

		String hql = "select * from tblAlbum album where 1=1";

		if ("desc".equals(orderType)) {
			hql += " order by album.createTime desc";
		} else {
			hql += " order by album.createTime asc";
		}
		// StringBuffer sql = new StringBuffer("select * from tblAlbum album");
		Result rst = aioMgt.sqlList(hql, param, pageSize, pageNo, true);
		List list = (List) rst.getRetVal();
		List<AlbumBean> listAlbum = new ArrayList<AlbumBean>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			AlbumBean bean = new AlbumBean();
			String id = o[0].toString();
			bean.setId(o[0].toString());
			bean.setName(o[1] == null ? null : o[1].toString());
			bean.setAlbumDesc(o[2] == null ? null : o[2].toString());
			bean.setCreateTime(o[3].toString());
			bean.setIsSaveReading(o[4].toString());
			bean.setCreateBy(o[5].toString());
			bean.setLastUpdateTime(o[6].toString());
			bean.setCover(o[7] == null ? null : o[7].toString());
			bean.setAgreeNum(Integer.parseInt(o[8].toString()));
			// 根据id获得 回复总数 和 照片总数
			bean.setTotalPhotoNum(this.getQueryCount("tblPhotoInfo", "albumId",
					id));
			bean.setTotalReplyNum(this.getQueryCount("oaNewsInfoReply",
					"newsId", id));
			// 获得相册的封面
			Result rs = getCoverById(id);
			List<Object[]> covers = (List<Object[]>) rs.getRetVal();

			String cover = "";
			if (covers.size() > 0) {// 没照片
				Object obj = covers.get(0)[0];
				cover = obj == null ? "" : obj.toString();
			}
			bean.setCover(cover);
			listAlbum.add(bean);
		}
		rst.setRetVal(listAlbum);
		return rst;
	}

	/**
	 * mj 添加开始上传的照片信息
	 * 
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @param SCompanyID
	 * @param sysName
	 * @param epat
	 * @return
	 */
	public Result saveOrUpdate(String[] picArr, AlbumBean album, String userId) {
		Result rst = new Result();
		try {
			List<PhotoInfoBean> list = new ArrayList<PhotoInfoBean>();
			for (int i = 0; i < picArr.length; i++) {
				String pic = picArr[i];
				PhotoInfoBean bean = new PhotoInfoBean();
				bean.setAgreeNum(0);
				bean.setAlbum(album);
				bean.setId(IDGenerater.getId());
				bean.setBeginName(pic);
				String tempName = pic.substring(0, pic.lastIndexOf("."))
						.toLowerCase();
				bean.setTempName(tempName);
				bean.setIsCover(0);// 是
				bean.setIsSaveReading("1");
				bean.setPhoneDesc(null);
				bean.setUploadBy(userId);
				bean.setCreateTime(BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss));
				bean.setLastUpdateTime(BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss));
				addBean(bean);
				list.add(bean);
			}
			rst.setRetVal(list);
		} catch (Exception ex) {
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);

		}
		return rst;
	}

	/**
	 * 根据id查询对应的bean mj
	 * 
	 * @param userId
	 * @param type
	 * @param value
	 * @return
	 */
	public Result getAlbumById(String id) {
		Result rs = new Result();
		rs = loadBean(id, AlbumBean.class);
		return rs;
	}

	/**
	 * 根据关联的列查询 对应的总数据大小 mj
	 * 
	 * @param tableName
	 * @param row
	 * @param rowValue
	 * @return
	 */
	public int getQueryCount(String tableName, String rowName, String rowValue) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select count(*) from " + tableName
				+ " as bean where " + rowName + " = ? ");
		param.add(rowValue);
		System.out.println(sql);
		Result rst = aioMgt.sqlList(sql.toString(), param);
		List list = (List) rst.getRetVal();

		Object[] obj = (Object[]) list.get(0);
		int count = Integer.parseInt(obj[0].toString());

		return count;
	}

	/**
	 * 添加mj
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result add(final AlbumBean album) {
		// 调用基类方法addBean执行插入操作
		return addBean(album);
	}

	/**
	 * 修改bean by mj
	 */
	public Result update(AlbumBean album) {
		Result rs = new Result();
		rs = updateBean(album);
		return rs;
	}

	/**
	 * 删除多条数据 mj
	 * 
	 * @param ids
	 *            long[]
	 * @return Result
	 */
	public Result delete(String[] ids) {
		return deleteBean(ids, AlbumBean.class, "id");
	}

	/**
	 * 删除相册 mj 先删除 点评和照片 传入 方法1 比较性能低下
	 * 
	 * @param loginId
	 *            String
	 * @return Result
	 */
	public Result del_photoInfo(final List<PhotoInfoBean> list,
			final String albumId) {
		// final String delPId = pId; // id
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {

							Statement st = conn.createStatement();
							for (PhotoInfoBean bean : list) {
								String delPId = bean.getId();
								// 先删除对应的新闻回复信息 mj
								String del_reply_sql = "delete  from oaNewsInfoReply where newsId =?";
								PreparedStatement ps = conn
										.prepareStatement(del_reply_sql);
								ps.setString(1, delPId);
								int i = ps.executeUpdate();
								System.out.println(i + "删除回复成功失败" + delPId);
								// String del_sql = "delete tblPhotoInfo where
								// 1=1 and id = '" + delPId + "'";
								// System.out.println("删除照片sql="+del_sql);
								//								
								// boolean bool = st.execute(del_sql);
								// if (bool) { // 如果执行成功
								// rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								// } else {
								// System.out.println("错误######################"
								// + delPId);
								// }
							}
							// 删除所有相册里面的照片
							String del_sql = "delete tblPhotoInfo where 1=1 and albumId = '"
									+ albumId + "'";
							System.out.println("删除照片sql=" + del_sql);
							boolean bool = st.execute(del_sql);
							if (bool) { // 如果执行成功
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								System.out.println("错误######################"
										+ albumId);
							}

						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}
	

	/**
	 * 删除相册 mj 先删除 点评和照片 传入 方法2
	 * 
	 * @param loginId
	 *            String
	 * @return Result
	 */
	public Result del_photo(final String albumId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				//这个里面可以调用hibbernate方法
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from oaNewsInfoReply where newsId in (select p.id from tblPhotoInfo p where albumId = ?)";
							PreparedStatement ps = conn
							.prepareStatement(sql);
							ps.setString(1, albumId);
							int i = ps.executeUpdate();
							System.out.println("删除所有照片回复返回值="+i);
							// 删除所有相册里面的照片
							String del_sql = "delete tblPhotoInfo where 1=1 and albumId = ?";
							System.out.println("删除照片sql=" + del_sql);
							ps = conn
							.prepareStatement(del_sql);
							ps.setString(1, albumId);
							ps.executeUpdate();
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}
	

	/**
	 * mj 查询对应的封面信息
	 * 
	 * @param userId
	 * @param account
	 * @param groupName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Result getCoverById(String aId) {
		List<String> param = new ArrayList<String>();
		String hql = "select top 1 p.beginName from tblphotoInfo p join tblAlbum a on a.id = p.albumId where a.id = ? and isCover = 1  order by p.lastUpdateTime desc";
		param.add(aId);
		Result rs = sqlList(hql, param);
		List<Object[]> list = (List<Object[]>) rs.getRetVal();
		if (list.size() <= 0) {
			return getCoverByIdRound(aId);
		} else {
			return rs;
		}

	}

	@SuppressWarnings("unchecked")
	public Result getCoverByIdRound(String aId) {
		List<String> param = new ArrayList<String>();
		String hql = "select top 1 p.beginName from tblphotoInfo p join tblAlbum a on a.id = p.albumId where a.id = ? and isCover = 0  order by p.lastUpdateTime desc";
		param.add(aId);
		return sqlList(hql, param);
	}

	@SuppressWarnings("unchecked")
	public Result queryGroupDetail(String userId, String account,
			String groupName) {
		List<String> param = new ArrayList<String>();
		String hql = "select g.id from OAMailGroup g join mailinfosetting ms on ms.id = g.account  where g.account = ? and g.userID = ? and g.groupName = ?";
		param.add(account);
		param.add(userId);
		param.add(groupName);
		return sqlList(hql, param);
	}

	public Result load(String setId) {
		Result rs = loadBean(setId, AlbumBean.class);
		return rs;
	}

	public Result deleteBean(String id) {
		return deleteBean(id, AlbumBean.class, "id");
	}
	
	public static  String replaceStr(String path,LoginBean login,String curNodeName) {
		//获得所有的 目录对应的路径Map
	    Result rs = DirectorySettingMgt.getTreeRootData(login,0);
	    List<DirectorySetting> ds = (List<DirectorySetting>) rs.getRetVal();
	    
	    //获得该路径需要替换的文件目录对象
	    int realityIndex = -1;
	    for (int i = 0; i < ds.size(); i++) {
	    	DirectorySetting dir = ds.get(i);
			String p = dir.getPath();
			p = p.replaceAll("\\\\", "\\\\\\\\");
			int idx = path.indexOf(p);
			if (idx == 0  ) {

				if (realityIndex == -1) {//此刻将现在的值赋予
					realityIndex = i;
				} else {
					int strLen = ds.get(realityIndex).getPath().length();
					int curLen = p.length();
					if (curLen > strLen) {
						realityIndex = i;
					}
				}
			}
		}
	    if (realityIndex!=-1) {
	    	
	    	DirectorySetting dir = ds.get(realityIndex);
	 	    
	 		String p = dir.getPath();
	 		p = p.replaceAll("\\\\", "\\\\\\\\");
	 		
	 		if (p.length()<=4) {
	 			path = path.replace(p, dir.getName()+"\\\\");
	 		} else {
	 			path = path.replace(p, dir.getName());
	 		}
	    }
		return path;
		
	}
 
	public List<DirectorySetting> getDepartment(String pid){
//		ArrayList<String> param = new ArrayList<String>();
		String hql = "FROM DirectorySetting bean where bean.id='"+pid+"'"; 
//		param.add(pid);
		Result result =list(hql, null);
		System.out.println("---");
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return (List<DirectorySetting>) result.getRetVal();
		}else{
			return null ;
		}
	}
}
