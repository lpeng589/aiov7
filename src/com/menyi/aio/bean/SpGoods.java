package com.menyi.aio.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="tblGoods")
public class SpGoods implements java.io.Serializable {

	// Fields

	private String id;

	private String goodsFullName;

	private String goodsName;

	private String goodsNumber;

	private String goodsProp;

	private String goodsUnitIdbuy;

	private String goodsUnitIdsales;

	private String goodsUnitIdstock;

	private Integer goodsAccMethod;

	private Integer goodsQtydecimal;

	private String createBy;

	private String lastUpdateBy;

	private String createTime;

	private String lastUpdateTime;

	private String statusId;

	private Integer goodsPriceDecimal;

	private String defStockState;

	private String defaultStock;

	private String defaultCompanyCode;

	private Float defaultTax;

	private Integer defaultCompany;

	private String goodsSpec;

	private String colorProp;

	private String sizeProp;

	private String longProp;

	private String widthProp;

	private String highProp;

	private String specProp;

	private String batchCodeProp;

	private String yearProp;

	private String classCode;

	private Float limitSalesPrice;

	private Float upBuyPrice;

	private String remark;

	private String goodsBaseUnitId;

	private String brand;

	private String comeFrom;

	private Float preBuyPrice;

	private Float proSalesPrice;

	private String picture;

	private String attachment;

	private String baseUnit;

	private String scompanyId;

	private String rowOn;

	private String costMethod;

	private String accCode;

	private Float pricefixing;

	private String workFlowNodeName;

	private String workFlowNode;

	private Integer printCount;

	private Float upperQty;

	private Float lowerQty;

	private String ingredients;

	private String weight;

	private Integer isCatalog;

	private Float monPayPrice;

	private Integer validity;

	private String barCode;

	private Float box;

	private String netContent;

	private String boxSpecifications;

	private String commoditySpecifications;

	private String grossWeight;

	private String netWeight;

	private String casingForm;

	private String stockCode;

	private String relationGoods;

	private String companyCode;

	private Float facePrice;

	private Float projectPrice;

	private Float pifaPrice;

	private Float corePrice;

	private Float producerPrice;

	private String goodsFullNamePym;

	private Float proSalesPrice2;

	private Float proSalesPrice3;

	private Float proSalesPrice4;

	private String costComputingSign;

	private Float gross;

	private String storehouseGrid;

	private String oldclassCode;

	private String checkPersons;

	private String availablyType;

	private String spec1;

	private String spec2;

	private String spec3;
	private int leastOrder ;
	private String shelfType ;
	private String shelfTime ;
	private int sellQuantity ;

	// Constructors

	/** default constructor */
	public SpGoods() {
	}

	

	// Property accessors
	@Id
	@Column(name = "id", nullable = false, length = 30)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "GoodsFullName", nullable = false, length = 200)
	public String getGoodsFullName() {
		return this.goodsFullName;
	}

	public void setGoodsFullName(String goodsFullName) {
		this.goodsFullName = goodsFullName;
	}

	@Column(name = "GoodsName", length = 100)
	public String getGoodsName() {
		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@Column(name = "GoodsNumber", nullable = false, length = 50)
	public String getGoodsNumber() {
		return this.goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	@Column(name = "GoodsProp", length = 100)
	public String getGoodsProp() {
		return this.goodsProp;
	}

	public void setGoodsProp(String goodsProp) {
		this.goodsProp = goodsProp;
	}

	@Column(name = "GoodsUnitIDBuy", length = 30)
	public String getGoodsUnitIdbuy() {
		return this.goodsUnitIdbuy;
	}

	public void setGoodsUnitIdbuy(String goodsUnitIdbuy) {
		this.goodsUnitIdbuy = goodsUnitIdbuy;
	}

	@Column(name = "GoodsUnitIDSales", length = 30)
	public String getGoodsUnitIdsales() {
		return this.goodsUnitIdsales;
	}

	public void setGoodsUnitIdsales(String goodsUnitIdsales) {
		this.goodsUnitIdsales = goodsUnitIdsales;
	}

	@Column(name = "GoodsUnitIDStock", length = 100)
	public String getGoodsUnitIdstock() {
		return this.goodsUnitIdstock;
	}

	public void setGoodsUnitIdstock(String goodsUnitIdstock) {
		this.goodsUnitIdstock = goodsUnitIdstock;
	}

	@Column(name = "GoodsAccMethod")
	public Integer getGoodsAccMethod() {
		return this.goodsAccMethod;
	}

	public void setGoodsAccMethod(Integer goodsAccMethod) {
		this.goodsAccMethod = goodsAccMethod;
	}

	@Column(name = "GoodsQTYDecimal")
	public Integer getGoodsQtydecimal() {
		return this.goodsQtydecimal;
	}

	public void setGoodsQtydecimal(Integer goodsQtydecimal) {
		this.goodsQtydecimal = goodsQtydecimal;
	}

	@Column(name = "createBy", nullable = false, length = 30)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "lastUpdateBy", length = 30)
	public String getLastUpdateBy() {
		return this.lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	@Column(name = "createTime", length = 19)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "lastUpdateTime", length = 19)
	public String getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Column(name = "statusId", nullable = false, length = 30)
	public String getStatusId() {
		return this.statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	@Column(name = "GoodsPriceDecimal")
	public Integer getGoodsPriceDecimal() {
		return this.goodsPriceDecimal;
	}

	public void setGoodsPriceDecimal(Integer goodsPriceDecimal) {
		this.goodsPriceDecimal = goodsPriceDecimal;
	}

	@Column(name = "DefStockState", length = 30)
	public String getDefStockState() {
		return this.defStockState;
	}

	public void setDefStockState(String defStockState) {
		this.defStockState = defStockState;
	}

	@Column(name = "DefaultStock", length = 30)
	public String getDefaultStock() {
		return this.defaultStock;
	}

	public void setDefaultStock(String defaultStock) {
		this.defaultStock = defaultStock;
	}

	@Column(name = "DefaultCompanyCode", length = 50)
	public String getDefaultCompanyCode() {
		return this.defaultCompanyCode;
	}

	public void setDefaultCompanyCode(String defaultCompanyCode) {
		this.defaultCompanyCode = defaultCompanyCode;
	}

	@Column(name = "DefaultTax", precision = 12, scale = 0)
	public Float getDefaultTax() {
		return this.defaultTax;
	}

	public void setDefaultTax(Float defaultTax) {
		this.defaultTax = defaultTax;
	}

	@Column(name = "defaultCompany")
	public Integer getDefaultCompany() {
		return this.defaultCompany;
	}

	public void setDefaultCompany(Integer defaultCompany) {
		this.defaultCompany = defaultCompany;
	}

	@Column(name = "GoodsSpec", length = 200)
	public String getGoodsSpec() {
		return this.goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	@Column(name = "Color_prop", length = 200)
	public String getColorProp() {
		return this.colorProp;
	}

	public void setColorProp(String colorProp) {
		this.colorProp = colorProp;
	}

	@Column(name = "Size_Prop", length = 200)
	public String getSizeProp() {
		return this.sizeProp;
	}

	public void setSizeProp(String sizeProp) {
		this.sizeProp = sizeProp;
	}

	@Column(name = "Long_Prop", length = 200)
	public String getLongProp() {
		return this.longProp;
	}

	public void setLongProp(String longProp) {
		this.longProp = longProp;
	}

	@Column(name = "Width_Prop", length = 200)
	public String getWidthProp() {
		return this.widthProp;
	}

	public void setWidthProp(String widthProp) {
		this.widthProp = widthProp;
	}

	@Column(name = "High_Prop", length = 200)
	public String getHighProp() {
		return this.highProp;
	}

	public void setHighProp(String highProp) {
		this.highProp = highProp;
	}

	@Column(name = "Spec_Prop", length = 200)
	public String getSpecProp() {
		return this.specProp;
	}

	public void setSpecProp(String specProp) {
		this.specProp = specProp;
	}

	@Column(name = "BatchCode_Prop", length = 200)
	public String getBatchCodeProp() {
		return this.batchCodeProp;
	}

	public void setBatchCodeProp(String batchCodeProp) {
		this.batchCodeProp = batchCodeProp;
	}

	@Column(name = "Year_Prop", length = 200)
	public String getYearProp() {
		return this.yearProp;
	}

	public void setYearProp(String yearProp) {
		this.yearProp = yearProp;
	}

	@Column(name = "classCode", length = 50)
	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@Column(name = "limitSalesPrice", precision = 12, scale = 0)
	public Float getLimitSalesPrice() {
		return this.limitSalesPrice;
	}

	public void setLimitSalesPrice(Float limitSalesPrice) {
		this.limitSalesPrice = limitSalesPrice;
	}

	@Column(name = "upBuyPrice", precision = 12, scale = 0)
	public Float getUpBuyPrice() {
		return this.upBuyPrice;
	}

	public void setUpBuyPrice(Float upBuyPrice) {
		this.upBuyPrice = upBuyPrice;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "goodsBaseUnitID")
	public String getGoodsBaseUnitId() {
		return this.goodsBaseUnitId;
	}

	public void setGoodsBaseUnitId(String goodsBaseUnitId) {
		this.goodsBaseUnitId = goodsBaseUnitId;
	}

	@Column(name = "Brand", length = 100)
	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "ComeFrom", length = 100)
	public String getComeFrom() {
		return this.comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	@Column(name = "PreBuyPrice", precision = 12, scale = 0)
	public Float getPreBuyPrice() {
		return this.preBuyPrice;
	}

	public void setPreBuyPrice(Float preBuyPrice) {
		this.preBuyPrice = preBuyPrice;
	}

	@Column(name = "ProSalesPrice", precision = 12, scale = 0)
	public Float getProSalesPrice() {
		return this.proSalesPrice;
	}

	public void setProSalesPrice(Float proSalesPrice) {
		this.proSalesPrice = proSalesPrice;
	}

	@Column(name = "Picture", length = 1000)
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Column(name = "Attachment", length = 1000)
	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Column(name = "BaseUnit", length = 50)
	public String getBaseUnit() {
		return this.baseUnit;
	}

	public void setBaseUnit(String baseUnit) {
		this.baseUnit = baseUnit;
	}

	@Column(name = "SCompanyID", length = 30)
	public String getScompanyId() {
		return this.scompanyId;
	}

	public void setScompanyId(String scompanyId) {
		this.scompanyId = scompanyId;
	}

	@Column(name = "RowON", length = 100)
	public String getRowOn() {
		return this.rowOn;
	}

	public void setRowOn(String rowOn) {
		this.rowOn = rowOn;
	}

	@Column(name = "CostMethod", length = 30)
	public String getCostMethod() {
		return this.costMethod;
	}

	public void setCostMethod(String costMethod) {
		this.costMethod = costMethod;
	}

	@Column(name = "AccCode", length = 30)
	public String getAccCode() {
		return this.accCode;
	}

	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}

	@Column(name = "pricefixing", precision = 12, scale = 0)
	public Float getPricefixing() {
		return this.pricefixing;
	}

	public void setPricefixing(Float pricefixing) {
		this.pricefixing = pricefixing;
	}

	@Column(name = "workFlowNodeName", length = 50)
	public String getWorkFlowNodeName() {
		return this.workFlowNodeName;
	}

	public void setWorkFlowNodeName(String workFlowNodeName) {
		this.workFlowNodeName = workFlowNodeName;
	}

	@Column(name = "workFlowNode", length = 50)
	public String getWorkFlowNode() {
		return this.workFlowNode;
	}

	public void setWorkFlowNode(String workFlowNode) {
		this.workFlowNode = workFlowNode;
	}

	@Column(name = "printCount")
	public Integer getPrintCount() {
		return this.printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	@Column(name = "upperQty", precision = 12, scale = 0)
	public Float getUpperQty() {
		return this.upperQty;
	}

	public void setUpperQty(Float upperQty) {
		this.upperQty = upperQty;
	}

	@Column(name = "lowerQty", precision = 12, scale = 0)
	public Float getLowerQty() {
		return this.lowerQty;
	}

	public void setLowerQty(Float lowerQty) {
		this.lowerQty = lowerQty;
	}

	@Column(name = "Ingredients", length = 80)
	public String getIngredients() {
		return this.ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	@Column(name = "Weight", length = 80)
	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	@Column(name = "isCatalog")
	public Integer getIsCatalog() {
		return this.isCatalog;
	}

	public void setIsCatalog(Integer isCatalog) {
		this.isCatalog = isCatalog;
	}

	@Column(name = "MonPayPrice", precision = 12, scale = 0)
	public Float getMonPayPrice() {
		return this.monPayPrice;
	}

	public void setMonPayPrice(Float monPayPrice) {
		this.monPayPrice = monPayPrice;
	}

	@Column(name = "Validity")
	public Integer getValidity() {
		return this.validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	@Column(name = "BarCode", length = 500)
	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Column(name = "Box", precision = 12, scale = 0)
	public Float getBox() {
		return this.box;
	}

	public void setBox(Float box) {
		this.box = box;
	}

	@Column(name = "NetContent", length = 100)
	public String getNetContent() {
		return this.netContent;
	}

	public void setNetContent(String netContent) {
		this.netContent = netContent;
	}

	@Column(name = "BoxSpecifications", length = 100)
	public String getBoxSpecifications() {
		return this.boxSpecifications;
	}

	public void setBoxSpecifications(String boxSpecifications) {
		this.boxSpecifications = boxSpecifications;
	}

	@Column(name = "CommoditySpecifications", length = 100)
	public String getCommoditySpecifications() {
		return this.commoditySpecifications;
	}

	public void setCommoditySpecifications(String commoditySpecifications) {
		this.commoditySpecifications = commoditySpecifications;
	}

	@Column(name = "GrossWeight", length = 100)
	public String getGrossWeight() {
		return this.grossWeight;
	}

	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}

	@Column(name = "NetWeight", length = 100)
	public String getNetWeight() {
		return this.netWeight;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}

	@Column(name = "CasingForm", length = 100)
	public String getCasingForm() {
		return this.casingForm;
	}

	public void setCasingForm(String casingForm) {
		this.casingForm = casingForm;
	}

	@Column(name = "StockCode", length = 50)
	public String getStockCode() {
		return this.stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	@Column(name = "RelationGoods", length = 30)
	public String getRelationGoods() {
		return this.relationGoods;
	}

	public void setRelationGoods(String relationGoods) {
		this.relationGoods = relationGoods;
	}

	@Column(name = "CompanyCode", length = 200)
	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@Column(name = "FacePrice", precision = 12, scale = 0)
	public Float getFacePrice() {
		return this.facePrice;
	}

	public void setFacePrice(Float facePrice) {
		this.facePrice = facePrice;
	}

	@Column(name = "ProjectPrice", precision = 12, scale = 0)
	public Float getProjectPrice() {
		return this.projectPrice;
	}

	public void setProjectPrice(Float projectPrice) {
		this.projectPrice = projectPrice;
	}

	@Column(name = "PifaPrice", precision = 12, scale = 0)
	public Float getPifaPrice() {
		return this.pifaPrice;
	}

	public void setPifaPrice(Float pifaPrice) {
		this.pifaPrice = pifaPrice;
	}

	@Column(name = "CorePrice", precision = 12, scale = 0)
	public Float getCorePrice() {
		return this.corePrice;
	}

	public void setCorePrice(Float corePrice) {
		this.corePrice = corePrice;
	}

	@Column(name = "ProducerPrice", precision = 12, scale = 0)
	public Float getProducerPrice() {
		return this.producerPrice;
	}

	public void setProducerPrice(Float producerPrice) {
		this.producerPrice = producerPrice;
	}

	@Column(name = "GoodsFullNamePYM", length = 30)
	public String getGoodsFullNamePym() {
		return this.goodsFullNamePym;
	}

	public void setGoodsFullNamePym(String goodsFullNamePym) {
		this.goodsFullNamePym = goodsFullNamePym;
	}

	@Column(name = "ProSalesPrice2", precision = 12, scale = 0)
	public Float getProSalesPrice2() {
		return this.proSalesPrice2;
	}

	public void setProSalesPrice2(Float proSalesPrice2) {
		this.proSalesPrice2 = proSalesPrice2;
	}

	@Column(name = "ProSalesPrice3", precision = 12, scale = 0)
	public Float getProSalesPrice3() {
		return this.proSalesPrice3;
	}

	public void setProSalesPrice3(Float proSalesPrice3) {
		this.proSalesPrice3 = proSalesPrice3;
	}

	@Column(name = "ProSalesPrice4", precision = 12, scale = 0)
	public Float getProSalesPrice4() {
		return this.proSalesPrice4;
	}

	public void setProSalesPrice4(Float proSalesPrice4) {
		this.proSalesPrice4 = proSalesPrice4;
	}

	@Column(name = "CostComputingSign", length = 50)
	public String getCostComputingSign() {
		return this.costComputingSign;
	}

	public void setCostComputingSign(String costComputingSign) {
		this.costComputingSign = costComputingSign;
	}

	@Column(name = "Gross", precision = 12, scale = 0)
	public Float getGross() {
		return this.gross;
	}

	public void setGross(Float gross) {
		this.gross = gross;
	}

	@Column(name = "StorehouseGrid", length = 50)
	public String getStorehouseGrid() {
		return this.storehouseGrid;
	}

	public void setStorehouseGrid(String storehouseGrid) {
		this.storehouseGrid = storehouseGrid;
	}

	@Column(name = "OldclassCode", length = 1500)
	public String getOldclassCode() {
		return this.oldclassCode;
	}

	public void setOldclassCode(String oldclassCode) {
		this.oldclassCode = oldclassCode;
	}

	@Column(name = "checkPersons", length = 8000)
	public String getCheckPersons() {
		return this.checkPersons;
	}

	public void setCheckPersons(String checkPersons) {
		this.checkPersons = checkPersons;
	}

	@Column(name = "AvailablyType", length = 10)
	public String getAvailablyType() {
		return this.availablyType;
	}

	public void setAvailablyType(String availablyType) {
		this.availablyType = availablyType;
	}

	@Column(name = "Spec1", length = 200)
	public String getSpec1() {
		return this.spec1;
	}

	public void setSpec1(String spec1) {
		this.spec1 = spec1;
	}

	@Column(name = "Spec2", length = 200)
	public String getSpec2() {
		return this.spec2;
	}

	public void setSpec2(String spec2) {
		this.spec2 = spec2;
	}

	@Column(name = "Spec3", length = 200)
	public String getSpec3() {
		return this.spec3;
	}

	public void setSpec3(String spec3) {
		this.spec3 = spec3;
	}

	@Column(name = "shelfType", length = 45)
	public String getShelfType() {
		return shelfType;
	}

	public void setShelfType(String shelfType) {
		this.shelfType = shelfType;
	}

	@Transient
	public String getShelfTime() {
		return shelfTime;
	}

	public void setShelfTime(String shelfTime) {
		this.shelfTime = shelfTime;
	}
	@Transient
	public int getSellQuantity() {
		return sellQuantity;
	}

	public void setSellQuantity(int sellQuantity) {
		this.sellQuantity = sellQuantity;
	}

	@Column(name = "leastOrder")
	public int getLeastOrder() {
		return leastOrder;
	}

	public void setLeastOrder(int leastOrder) {
		this.leastOrder = leastOrder;
	}
	
}