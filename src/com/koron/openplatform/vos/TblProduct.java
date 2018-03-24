package com.koron.openplatform.vos;


/**
 * Description£º²úÆ·
 * 
 */

public class TblProduct implements java.io.Serializable {
	Integer id;
	String CerCode;
	String GoodsID;
	String Tittle;
	String Props;
	String Description;
	String Skus ;
	String PropsName;
	String Validity  ;
	String Weight;
	String Size;
	String Stock;
	String Unit;
	String Price ;
	String MarketPrice;
	String BuyPrice;
	String Picture;
	String TransportID;
	String SaleNick;
	String WithholdingStock;
	String Location;
	String OutID;
	String SmallModel;
	String PackType ;
	String HasInvoices;
	String Spec;
	String Producter ;
	String Remark;
	public String getCerCode() {
		return CerCode;
	}
	public void setCerCode(String cerCode) {
		CerCode = cerCode;
	}
	public String getGoodsID() {
		return GoodsID;
	}
	public void setGoodsID(String goodsID) {
		GoodsID = goodsID;
	}
	public String getTittle() {
		return Tittle;
	}
	public void setTittle(String tittle) {
		Tittle = tittle;
	}
	public String getProps() {
		return Props;
	}
	public void setProps(String props) {
		Props = props;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getSkus() {
		return Skus;
	}
	public void setSkus(String skus) {
		Skus = skus;
	}
	public String getPropsName() {
		return PropsName;
	}
	public void setPropsName(String propsName) {
		PropsName = propsName;
	}
	public String getValidity() {
		return Validity;
	}
	public void setValidity(String validity) {
		Validity = validity;
	}
	public String getWeight() {
		return Weight;
	}
	public void setWeight(String weight) {
		Weight = weight;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public String getStock() {
		return Stock;
	}
	public void setStock(String stock) {
		Stock = stock;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getMarketPrice() {
		return MarketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		MarketPrice = marketPrice;
	}
	public String getBuyPrice() {
		return BuyPrice;
	}
	public void setBuyPrice(String buyPrice) {
		BuyPrice = buyPrice;
	}
	public String getPicture() {
		return Picture;
	}
	public void setPicture(String picture) {
		Picture = picture;
	}
	public String getTransportID() {
		return TransportID;
	}
	public void setTransportID(String transportID) {
		TransportID = transportID;
	}
	public String getSaleNick() {
		return SaleNick;
	}
	public void setSaleNick(String saleNick) {
		SaleNick = saleNick;
	}
	public String getWithholdingStock() {
		return WithholdingStock;
	}
	public void setWithholdingStock(String withholdingStock) {
		WithholdingStock = withholdingStock;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getOutID() {
		return OutID;
	}
	public void setOutID(String outID) {
		OutID = outID;
	}
	public String getSmallModel() {
		return SmallModel;
	}
	public void setSmallModel(String smallModel) {
		SmallModel = smallModel;
	}
	public String getPackType() {
		return PackType;
	}
	public void setPackType(String packType) {
		PackType = packType;
	}
	public String getHasInvoices() {
		return HasInvoices;
	}
	public void setHasInvoices(String hasInvoices) {
		HasInvoices = hasInvoices;
	}
	public String getSpec() {
		return Spec;
	}
	public void setSpec(String spec) {
		Spec = spec;
	}
	public String getProducter() {
		return Producter;
	}
	public void setProducter(String producter) {
		Producter = producter;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "TblProduct [id=" + id + ", CerCode=" + CerCode + ", GoodsID="
				+ GoodsID + ", Tittle=" + Tittle + ", Props=" + Props
				+ ", Description=" + Description + ", Skus=" + Skus
				+ ", PropsName=" + PropsName + ", Validity=" + Validity
				+ ", Weight=" + Weight + ", Size=" + Size + ", Stock=" + Stock
				+ ", Unit=" + Unit + ", Price=" + Price + ", MarketPrice="
				+ MarketPrice + ", BuyPrice=" + BuyPrice + ", Picture="
				+ Picture + ", TransportID=" + TransportID + ", SaleNick="
				+ SaleNick + ", WithholdingStock=" + WithholdingStock
				+ ", Location=" + Location + ", OutID=" + OutID
				+ ", SmallModel=" + SmallModel + ", PackType=" + PackType
				+ ", HasInvoices=" + HasInvoices + ", Spec=" + Spec
				+ ", Producter=" + Producter + ", Remark=" + Remark + "]";
	}
}
