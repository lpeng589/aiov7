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
import com.koron.oa.bean.PhotoInfoBean;
import com.menyi.aio.bean.*;
import com.menyi.web.util.*;
import org.apache.commons.lang.StringUtils;
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
public class PhotoInfoMgt extends AIODBManager {
	
	private AlbumTreeMgt mgt = new AlbumTreeMgt();
	
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
				bean.setIsCover(1);// 是
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
	 * 删除PhotoInfo 传入pID
	 * 
	 * @param loginId
	 *            String
	 * @return Result
	 */
	public Result del_photo(String pId) {
		final String newPid = pId; // id

		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							// 先删除对应的新闻回复信息 mj
							String del_reply_sql = "delete  from oaNewsInfoReply where newsId =?";
							PreparedStatement ps = conn
									.prepareStatement(del_reply_sql);
							ps.setString(1, newPid);
							ps.executeUpdate();
							String del_sql = "delete tblPhotoInfo where 1=1 and id = '"
									+ newPid + "'";
							boolean bool = st.execute(del_sql);
							if (bool) { // 如果执行成功
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	 * 根据id查询对应的bean mj
	 * 
	 * @param userId
	 * @param type
	 * @param value
	 * @return
	 */
	public Result getPhotoInfoById(String id) {
		Result rs = new Result();
		rs = loadBean(id, PhotoInfoBean.class);
		return rs;
	}
	
	
	
	//返回所有的该相册里面的照片 以当前照片为第一位
	public Result queryPhosOrderById(String albumId,String pid) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select p.id,p.tempName,p.beginName from tblphotoInfo p join tblAlbum a on a.id = p.albumId where 1=1 and p.albumId = ? and p.id != ?");
		param.add(albumId);
		param.add(pid);
		Result rst = aioMgt.sqlList(sql.toString(),param);
		List list = (List) rst.getRetVal();
		List<PhotoInfoBean> listPhoto = new ArrayList<PhotoInfoBean>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			PhotoInfoBean obj = new PhotoInfoBean();
			obj.setId(o[0].toString());
			obj.setTempName(o[1] == null ? null : o[1].toString());
			obj.setBeginName(o[2] == null ? null : o[2].toString());
			obj.setAlbum(null);
			obj.setIsSaveReading(null);
			obj.setUploadBy(null);
			obj.setPhoneDesc(null);
			obj.setCreateTime(null);
			obj.setLastUpdateTime(null);
			obj.setAgreeNum(0);
			obj.setIsCover(0);
			obj.setReplyCount(0);
			listPhoto.add(obj);
		}
		rst.setRetVal(listPhoto);
		return rst;
	}
	
	
	//返回照片名称 照片id 等信息 但是效率不是很高 也不支持 让当前照片排在首位
	public Result queryBegnameByAlbumId(String orderType,String albumId, int pageNo, int pageSize) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(
				"select p.id,p.tempName,p.beginName from tblphotoInfo p join tblAlbum a on a.id = p.albumId where 1=1 ");
		if (StringUtils.isNotBlank(albumId)) {
			sql.append("and p.albumId = ?");
			param.add(albumId);
		}
		if ("desc".equals(orderType)) {
			sql.append(" order by p.createTime desc");
		} else {
			sql.append(" order by p.createTime asc");
		}
		Result rst = aioMgt.sqlList(sql.toString(), param, pageSize, pageNo,
				true);
		List list = (List) rst.getRetVal();
		List<PhotoInfoBean> listPhoto = new ArrayList<PhotoInfoBean>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			PhotoInfoBean obj = new PhotoInfoBean();
			obj.setId(o[0].toString());
			obj.setTempName(o[1] == null ? null : o[1].toString());
			obj.setBeginName(o[2] == null ? null : o[2].toString());
			obj.setAlbum(null);
			obj.setIsSaveReading(null);
			obj.setUploadBy(null);
			obj.setPhoneDesc(null);
			obj.setCreateTime(null);
			obj.setLastUpdateTime(null);
			obj.setAgreeNum(0);
			obj.setIsCover(0);
			obj.setReplyCount(0);
			listPhoto.add(obj);
		}
		rst.setRetVal(listPhoto);
		return rst;
	}



	public Result queryPhotosByAlbumId(String orderType,String albumId, int pageNo, int pageSize) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(
				"select p.* from tblphotoInfo p join tblAlbum a on a.id = p.albumId where 1=1 ");
		if (StringUtils.isNotBlank(albumId)) {
			sql.append("and p.albumId = ?");
			param.add(albumId);
		}
		if ("desc".equals(orderType)) {
			sql.append(" order by p.createTime desc");
		} else {
			sql.append(" order by p.createTime asc");
		}
		//sql.append(" order by p.lastUpdateTime desc");
		System.out.println(sql);
		Result rst = aioMgt.sqlList(sql.toString(), param, pageSize, pageNo,
				true);
		List list = (List) rst.getRetVal();
		AlbumTreeMgt amMgt = new AlbumTreeMgt();
		List<PhotoInfoBean> listPhoto = new ArrayList<PhotoInfoBean>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			PhotoInfoBean obj = new PhotoInfoBean();
			obj.setId(o[0].toString());
			obj.setTempName(o[1] == null ? null : o[1].toString());
			obj.setBeginName(o[2] == null ? null : o[2].toString());
			AlbumBean album = (AlbumBean) amMgt.load(o[3].toString())
					.getRetVal();
			obj.setAlbum(album);
			obj.setIsSaveReading(o[4] == null ? null : o[4].toString());
			obj.setUploadBy(o[5] == null ? null : o[5].toString());
			obj.setPhoneDesc(o[6] == null ? null : o[6].toString());
			obj.setCreateTime(o[7].toString());
			obj.setLastUpdateTime(o[8].toString());
			obj.setAgreeNum(Integer.parseInt(o[9].toString()));
			obj.setIsCover(Integer.parseInt(o[10].toString()));
			//点评数量
			int replyCount = mgt.getQueryCount("oaNewsInfoReply", "newsId", o[0].toString());
			obj.setReplyCount(replyCount);
			listPhoto.add(obj);
		}
		rst.setRetVal(listPhoto);
		return rst;
	}
	/**
	 * 修改将该相册里面的封面修改为非封面
	 */
	
	public Result updatePhoCoverByAId(String albumIdStr) {
		final String albumId = albumIdStr; // id

		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							StringBuffer sql = new StringBuffer(
							"update  p  set p.isCover = 0 from tblphotoInfo p  where  p.iscover = 1 and p.albumId = ?");
							PreparedStatement ps = conn
							.prepareStatement(sql.toString());
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
	 * 修改bean by mj
	 */
	public Result update(PhotoInfoBean bean) {
		Result rs = new Result();
		rs = updateBean(bean);
		return rs;
	}


	/**
	 * 删除多条过滤数据
	 * 
	 * @param ids
	 *            long[]
	 * @return Result
	 */
	public Result delete(String[] ids) {
		return deleteBean(ids, PhotoInfoBean.class, "id");
	}



	public Result load(String id) {
		Result rs = loadBean(id, PhotoInfoBean.class);
		return rs;
	}
	
	//根据id查找对应的实体bean
	public String getAIdByPId(String pId) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(
				"select albumId from tblphotoInfo where id = ? ");
		param.add(pId);
		Result rst = aioMgt.sqlList(sql.toString(), param);
		List list = (List) rst.getRetVal();
		if (list.size()>0) {
			Object[] obj = (Object[])list.get(0);
			return obj[0].toString();
			
		}
		return null;
	}

}
