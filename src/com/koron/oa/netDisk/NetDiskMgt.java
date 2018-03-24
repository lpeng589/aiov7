package com.koron.oa.netDisk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.dbfactory.Result;
import com.koron.oa.albumTree.TreeVo;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.directorySeting.DirectorySettingMgt;
import com.koron.oa.util.FileOperateUtil;
import com.koron.oa.util.FileUtils;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.PageUtil;


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
public class NetDiskMgt extends AIODBManager {
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


	public static  String replaceStr(String path,LoginBean login,String curNodeName) {
		//������е� Ŀ¼��Ӧ��·��Map
	    Result rs = DirectorySettingMgt.getTreeRootData(login,1);
	    List<DirectorySetting> ds = (List<DirectorySetting>) rs.getRetVal();
	    
	    //��ø�·����Ҫ�滻���ļ�Ŀ¼����
	    int realityIndex = -1;
	    for (int i = 0; i < ds.size(); i++) {
	    	DirectorySetting dir = ds.get(i);
	    	
	    	
			String p = dir.getPath();
			if(p.indexOf("\\\\")>=0){
				p = p.replaceAll("\\\\", "\\\\\\\\");
			} else if (p.indexOf("\\") >= 0){
				p = p.replaceAll("\\\\", "\\\\\\\\");
			} else if (p.indexOf("/") >= 0  ) {
				p = p.replaceAll("/", "\\\\\\\\");
			} 
			int idx = path.indexOf(p);
			if (idx == 0  ) {

				if (realityIndex == -1) {//�˿̽����ڵ�ֵ����
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
	    if (realityIndex == -1) {
	    	
	    	return path;
	    }
	    System.out.println(realityIndex);
	    DirectorySetting dir = ds.get(realityIndex);
	    
		String p = dir.getPath();
		p = p.replaceAll("\\\\", "\\\\\\\\");
		
		if (p.length()<=4) {
			path = path.replace(p, dir.getName()+"\\\\");
		} else {
			path = path.replace(p, dir.getName());
		}
	
		return path;
		
		
	}

	/**
	 * ���ļ����Ͻ�������
	 * 
	 * @param files
	 *            file[]
	 * @param key
	 *            �����ֶ�
	 * @param sortType
	 *            asc or desc
	 * @return file[]
	 * @author mj
	 */
	@SuppressWarnings("unchecked")
	public File[] sortCollection(File[] files, int key, int sortType) {
		// SortCollection sort = new SortCollection(phos);
		// sort.addSortField("lastUpdateTime", SortCollection.ASC);
		// sort.sort();
		// filesȡ�õı�����ǰ�������

		Arrays.sort(files, new FileUtils.CompratorBySortType(sortType, key));
		return files;
	}

	/**
	 * ��ҳ
	 * 
	 * @param list
	 * @param currentPage
	 * @return
	 */
	public Object[] pageList(List<File> list, int currentPage, int pageRecords,int minPageRecords) {
		PageUtil<File> pu = new PageUtil<File>(list, pageRecords,minPageRecords);
		List<File> files = pu.getResult(currentPage);
		Object[] arr = new Object[4];
		arr[0] = files;
		arr[1] = pu;
		return arr;
	}

	/**
	 * ���Ѿ���ҳ������List[File]���з�װ
	 * 
	 * @param files
	 * @param isCompress
	 * @return
	 */
	public List<TreeVo> getFilesByPath(List<File> files) {
		List<TreeVo> fileVos = new ArrayList<TreeVo>();
		long beginAll = System.currentTimeMillis();
		int j = 0;
		for (int i = 0, length = files.size(); i < length; i++) {
			File f = files.get(i);
			String fileName = f.getName();
			TreeVo bean = new TreeVo();
			bean.setTempName(fileName);
			bean.setLastUpdateTime(BaseDateFormat.format(new Date(f
					.lastModified()), BaseDateFormat.yyyyMMddHHmmss));
			bean.setFileSize(f.length());
			String p = f.getPath();
			int idx = p.lastIndexOf("\\");
			p = p.substring(0, idx);
			p = p.replaceAll("\\\\", "\\\\\\\\");
			bean.setTNo(j);
			bean.setPath(p);
			bean.setFileDesc(FileOperateUtil.getFileDesc(fileName));
			bean.setFileIcon(FileOperateUtil.getFileIcon(fileName));
			fileVos.add(bean);
			j++;
		}
		long endAll = System.currentTimeMillis();
		System.out.println("consume time  :" + (endAll - beginAll));
		return fileVos;
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
	public static  String replaceStrAll(String path,LoginBean login) {
		//������е� Ŀ¼��Ӧ��·��Map
	    Result rs = DirectorySettingMgt.getTreeRootData(login,1);
	    List<DirectorySetting> ds = (List<DirectorySetting>) rs.getRetVal();
	    
	    //��ø�·����Ҫ�滻���ļ�Ŀ¼����
	    String p="";
	    for (int i = 0; i < ds.size(); i++) {
	    	DirectorySetting dir = ds.get(i);

			  p = dir.getId();
		
		}
	    return p;
	}
		
}
	
 
