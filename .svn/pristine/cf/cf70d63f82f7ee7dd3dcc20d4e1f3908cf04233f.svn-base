package com.menyi.aio.web.customize;

import java.util.*;

import com.menyi.aio.dyndb.DDLOperation;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class PopField implements Cloneable{
	public static final int SEARCH_MORE =6; //大于
    public static final int SEARCH_MATCHR =5; //匹配以输入的数据结尾的字段
	public static final int SEARCH_MATCHL = 4;//匹配以输入的数据开始的字段
    public static final int SEARCH_SCOPE = 3;
    public static final int SEARCH_MATCH = 2;//输入的数据跟字段的某部分匹配
    public static final int SEARCH_EQUAL = 1;
    public static final int SEARCH_NO = 0;
    public static final int SEARCH_INPUT = 7;//用户只在界面输入值，不自动生成条件查询

    public static final int TYPE_DISPLAY = 0;
    public static final int TYPE_SAVE = 1;
    
    public int type; //0表示显示字段，1表示保存字段
    public String fieldName;
    public String asName;//别名
    public String sysType;
    
    //保存字段
    public String parentName; //回填的字段名
    public boolean hidden = false;//回填字段是否隐藏
    public boolean compare ;//保存字段 是否参与 与上一行比较,默认是参与    
    public boolean relationKey;//是否是关联字段
    
    //显示字段
    public String display;//显示名
    public boolean parentDisplay;//是否回显
    public int width;//显示宽度
    private int searchType;
    public String defaultValue;
    public boolean keySearch;//是否是关键字搜索    
    public String hiddenInput ;//隐藏可显示
    public String inputType; //字段输入类型，部分组合字段需用此来指定类型，如为零不显示
    public String inputValue; //输入值，当指定inputType时用此来指定输入值   
    public String popSelect ;//弹出窗口名字
    public String linkAddr ;//链接地址 V7暂未发现有使用的地方，暂不处理
    
    
    public String returnToIdentity; //用于布匹版数1致数10回填时标识定位用，V7不用，暂时不处理此字段
    public String marker; //暂不明白意义zxy 暂不处理
    public int repeatNotShow ;//多行数据中如果此字段重复，那么后面重复的字段不显示 值为1，表示重复不显示，弹出窗v7暂不启用此功能
    public boolean hasValNotFill ;//如果保存字段上面有值，那么就不用弹出框中的值填充  v7未使用，暂不启用
    
    public int fieldType; //字段类型，不由弹窗配置，面是系统自动根据对应字段的表结构类型来设置  
    
    public int isMobile; //字段类型，不由弹窗配置，面是系统自动根据对应字段的表结构类型来设置  
    
    


	public int getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(int isMobile) {
		this.isMobile = isMobile;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public boolean getHasValNotFill() {
		return hasValNotFill;
	}

	public void setHasValNotFill(boolean hasValNotFill) {
		this.hasValNotFill = hasValNotFill;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getReturnToIdentity() {
		return returnToIdentity;
	}

	public void setReturnToIdentity(String returnToIdentity) {
		this.returnToIdentity = returnToIdentity;
	}

	public int getSearchType() {

        return searchType;
    }

    public String getDisplay() {
        return display;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getParentName() {
        return parentName;
    }

    public boolean isParentDisplay() {
        return parentDisplay;
    }

    public boolean isRelationKey() {
        return relationKey;
    }

    public int getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRelationKey(boolean relationKey) {
        this.relationKey = relationKey;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setParentDisplay(boolean parentDisplay) {
        this.parentDisplay = parentDisplay;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setSearchType(int searchType) {

        this.searchType = searchType;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

	public String getAsName() {
		return asName;
	}

	public void setAsName(String asName) {
		this.asName = asName;
	}

	public String getHiddenInput() {
		return hiddenInput;
	}

	public void setHiddenInput(String hiddenInput) {
		this.hiddenInput = hiddenInput;
	}

	public String getPopSelect() {
		return popSelect;
	}

	public void setPopSelect(String popSelect) {
		this.popSelect = popSelect;
	}

	public String getLinkAddr() {
		return linkAddr;
	}

	public void setLinkAddr(String linkAddr) {
		this.linkAddr = linkAddr;
	}

	public boolean isCompare() {
		return compare;
	}

	public void setCompare(boolean compare) {
		this.compare = compare;
	}

	public int getRepeatNotShow() {
		return repeatNotShow;
	}

	public void setRepeatNotShow(int repeatNotShow) {
		this.repeatNotShow = repeatNotShow;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	/**
	 * 获取弹出框多语言显示
	 * @param tableName 表名，用来解析@TABLENAME这个自定义标签
	 * @param allTables 包含tableName的表
	 * @param locale 区域
	 * @return
	 */
	public String getDisplayLocale(String tableName,Hashtable<String,DBTableInfoBean> allTables,Locale locale)
	{
		String tmpField = getDisplay();
		if(tmpField!=null)
		{
			if(tmpField.indexOf("@TABLENAME")!=-1 && tableName!=null && !tableName.equals(""))
				tmpField = tmpField.replace("@TABLENAME", tableName);
		}
		if(tmpField == null)
			tmpField = getFieldName();
		else if(tmpField.indexOf("@TABLENAME")!=-1)
			tmpField = getFieldName();
		
		if(getDisplay()!=null && getDisplay().length() > 0 && getDisplay().indexOf(".")==-1){
			return getDisplay();
		}
		
		DBFieldInfoBean f = DDLOperation.getField(allTables, tmpField);
		if(f!=null){
			return f.getDisplay().get(locale.toString());
		}else{
			f = DDLOperation.getField(allTables, getFieldName());
			if(f!=null){
				return f.getDisplay().get(locale.toString());
			}else{
				return tmpField;
			}
		}
	}
	
	@Override
	public PopField clone() throws CloneNotSupportedException {
		return (PopField)super.clone();
	}

	public boolean isKeySearch() {
		return keySearch;
	}

	public void setKeySearch(boolean keySearch) {
		this.keySearch = keySearch;
	}
}
