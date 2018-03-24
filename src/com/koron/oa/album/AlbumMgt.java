package com.koron.oa.album;

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
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * 
 * <p>
 * Title: ��ҵ���
 * </p>
 * 
 * @Copyright: �������
 * 
 * @author ë��
 * 
 */
public class AlbumMgt extends AIODBManager {

	/**
	 * mj ��ѯ���е���� ����һ
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
		// ��������
		List param = new ArrayList();
		String hql = "select bean from AlbumBean as bean";
		// ����list���ؽ��
		if (isNeedPage) {
			return list(hql, param, pageNo, pageSize, true);
		} else {
			return list(hql, param);
		}
	}

	/**
	 * mj ��ѯ���е���� ������
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
			// ����id��� �ظ����� �� ��Ƭ����
			bean.setTotalPhotoNum(this.getQueryCount("tblPhotoInfo", "albumId",
					id));
			bean.setTotalReplyNum(this.getQueryCount("oaNewsInfoReply",
					"newsId", id));
			// ������ķ���
			Result rs = getCoverById(id);
			List<Object[]> covers = (List<Object[]>) rs.getRetVal();

			String cover = "";
			if (covers.size() > 0) {// û��Ƭ
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
	 * mj ��ӿ�ʼ�ϴ�����Ƭ��Ϣ
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
				bean.setIsCover(0);// ��
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
	 * ����id��ѯ��Ӧ��bean mj
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
	 * ���ݹ������в�ѯ ��Ӧ�������ݴ�С mj
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
	 * ���mj
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result add(final AlbumBean album) {
		// ���û��෽��addBeanִ�в������
		return addBean(album);
	}

	/**
	 * �޸�bean by mj
	 */
	public Result update(AlbumBean album) {
		Result rs = new Result();
		rs = updateBean(album);
		return rs;
	}

	/**
	 * ɾ���������� mj
	 * 
	 * @param ids
	 *            long[]
	 * @return Result
	 */
	public Result delete(String[] ids) {
		return deleteBean(ids, AlbumBean.class, "id");
	}

	/**
	 * ɾ����� mj ��ɾ�� ��������Ƭ ���� ����1 �Ƚ����ܵ���
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
								// ��ɾ����Ӧ�����Żظ���Ϣ mj
								String del_reply_sql = "delete  from oaNewsInfoReply where newsId =?";
								PreparedStatement ps = conn
										.prepareStatement(del_reply_sql);
								ps.setString(1, delPId);
								int i = ps.executeUpdate();
								System.out.println(i + "ɾ���ظ��ɹ�ʧ��" + delPId);
								// String del_sql = "delete tblPhotoInfo where
								// 1=1 and id = '" + delPId + "'";
								// System.out.println("ɾ����Ƭsql="+del_sql);
								//								
								// boolean bool = st.execute(del_sql);
								// if (bool) { // ���ִ�гɹ�
								// rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								// } else {
								// System.out.println("����######################"
								// + delPId);
								// }
							}
							// ɾ����������������Ƭ
							String del_sql = "delete tblPhotoInfo where 1=1 and albumId = '"
									+ albumId + "'";
							System.out.println("ɾ����Ƭsql=" + del_sql);
							boolean bool = st.execute(del_sql);
							if (bool) { // ���ִ�гɹ�
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								System.out.println("����######################"
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
	 * ɾ����� mj ��ɾ�� ��������Ƭ ���� ����2
	 * 
	 * @param loginId
	 *            String
	 * @return Result
	 */
	public Result del_photo(final String albumId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				//���������Ե���hibbernate����
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from oaNewsInfoReply where newsId in (select p.id from tblPhotoInfo p where albumId = ?)";
							PreparedStatement ps = conn
							.prepareStatement(sql);
							ps.setString(1, albumId);
							int i = ps.executeUpdate();
							System.out.println("ɾ��������Ƭ�ظ�����ֵ="+i);
							// ɾ����������������Ƭ
							String del_sql = "delete tblPhotoInfo where 1=1 and albumId = ?";
							System.out.println("ɾ����Ƭsql=" + del_sql);
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
	 * mj ��ѯ��Ӧ�ķ�����Ϣ
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
	
	//������Ƭ���� ��Ƭid ����Ϣ ����Ч�ʲ��Ǻܸ� Ҳ��֧�� �õ�ǰ��Ƭ������λ
	public Result queryBegnameByAlbumId(String orderType,String albumId, int pageNo, int pageSize) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select p.id,p.tempName,p.beginName from tblphotoInfo p join tblAlbum a on a.id = p.albumId where 1=1 ");
		if (StringUtils.isNotBlank(albumId)) {
			sql.append("and p.albumId = ?");
			param.add(albumId);
		}
		if ("desc".equals(orderType)) {
			sql.append(" order by p.createTime desc");
		} else {
			sql.append(" order by p.createTime asc");
		}
		Result rst = aioMgt.sqlList(sql.toString(), param, pageSize, pageNo,true);
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
	
	/**
	 * ����id��ѯ��Ӧ��bean mj
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
}
