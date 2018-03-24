package com.menyi.aio.bean;

public class CompanyIniBean {
   String companyCode;
   String billDate;
   int itemNo;
   double receiveTotalRemain;
   double payTotalRemain;
   double preReceiveTotalRemain;
   double prePayTotalRemain;
   double ReceiveBegin;
   double ReceiveTotalDebit;
   double ReceiveTotalLend;
   double PreReceiveBegin;
   double PreReceiveTotalDebit;
   double PreReceiveTotalLend;
   double PayBegin;
   double PayTotalDebit;
   double PayTotalLend;
   double PrePayBegin;
   double PrePayTotalDebit;
   double PrePayTotalLend;
   double FcreceiveTotalRemain;
   double FcpayTotalRemain;
   double FcpreReceiveTotalRemain;
   double FcprePayTotalRemain;
   double FcReceiveBegin;
   double FcReceiveTotalDebit;
   double FcReceiveTotalLend;
   double FcPreReceiveBegin;
   double FcPreReceiveTotalDebit;
   double FcPreReceiveTotalLend;
   double FcPayBegin;
   double FcPayTotalDebit;
   double FcPayTotalLend;
   double FcPrePayBegin;
   double FcPrePayTotalDebit;
   double FcPrePayTotalLend;
   String CurrencyTypeID;
   double CurrencyRate;
   
    public String getCompanyCode() {
        return companyCode;
    }

    public int getItemNo() {
        return itemNo;
    }

    public double getPayTotalRemain() {
        return payTotalRemain;
    }

    public double getPrePayTotalRemain() {
        return prePayTotalRemain;
    }

    public double getPreReceiveTotalRemain() {
        return preReceiveTotalRemain;
    }

    public double getReceiveTotalRemain() {
        return receiveTotalRemain;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setReceiveTotalRemain(double receiveTotalRemain) {
        this.receiveTotalRemain = receiveTotalRemain;
    }

    public void setPreReceiveTotalRemain(double preReceiveTotalRemain) {
        this.preReceiveTotalRemain = preReceiveTotalRemain;
    }

    public void setPrePayTotalRemain(double prePayTotalRemain) {
        this.prePayTotalRemain = prePayTotalRemain;
    }

    public void setPayTotalRemain(double payTotalRemain) {
        this.payTotalRemain = payTotalRemain;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

	public double getCurrencyRate() {
		return CurrencyRate;
	}

	public void setCurrencyRate(double currencyRate) {
		CurrencyRate = currencyRate;
	}

	public String getCurrencyTypeID() {
		return CurrencyTypeID;
	}

	public void setCurrencyTypeID(String currencyTypeID) {
		CurrencyTypeID = currencyTypeID;
	}

	public double getFcPayBegin() {
		return FcPayBegin;
	}

	public void setFcPayBegin(double fcPayBegin) {
		FcPayBegin = fcPayBegin;
	}

	public double getFcPayTotalDebit() {
		return FcPayTotalDebit;
	}

	public void setFcPayTotalDebit(double fcPayTotalDebit) {
		FcPayTotalDebit = fcPayTotalDebit;
	}

	public double getFcPayTotalLend() {
		return FcPayTotalLend;
	}

	public void setFcPayTotalLend(double fcPayTotalLend) {
		FcPayTotalLend = fcPayTotalLend;
	}

	public double getFcpayTotalRemain() {
		return FcpayTotalRemain;
	}

	public void setFcpayTotalRemain(double fcpayTotalRemain) {
		FcpayTotalRemain = fcpayTotalRemain;
	}

	public double getFcPrePayBegin() {
		return FcPrePayBegin;
	}

	public void setFcPrePayBegin(double fcPrePayBegin) {
		FcPrePayBegin = fcPrePayBegin;
	}

	public double getFcPrePayTotalDebit() {
		return FcPrePayTotalDebit;
	}

	public void setFcPrePayTotalDebit(double fcPrePayTotalDebit) {
		FcPrePayTotalDebit = fcPrePayTotalDebit;
	}

	public double getFcPrePayTotalLend() {
		return FcPrePayTotalLend;
	}

	public void setFcPrePayTotalLend(double fcPrePayTotalLend) {
		FcPrePayTotalLend = fcPrePayTotalLend;
	}

	public double getFcprePayTotalRemain() {
		return FcprePayTotalRemain;
	}

	public void setFcprePayTotalRemain(double fcprePayTotalRemain) {
		FcprePayTotalRemain = fcprePayTotalRemain;
	}

	public double getFcPreReceiveBegin() {
		return FcPreReceiveBegin;
	}

	public void setFcPreReceiveBegin(double fcPreReceiveBegin) {
		FcPreReceiveBegin = fcPreReceiveBegin;
	}

	public double getFcPreReceiveTotalDebit() {
		return FcPreReceiveTotalDebit;
	}

	public void setFcPreReceiveTotalDebit(double fcPreReceiveTotalDebit) {
		FcPreReceiveTotalDebit = fcPreReceiveTotalDebit;
	}

	public double getFcPreReceiveTotalLend() {
		return FcPreReceiveTotalLend;
	}

	public void setFcPreReceiveTotalLend(double fcPreReceiveTotalLend) {
		FcPreReceiveTotalLend = fcPreReceiveTotalLend;
	}

	public double getFcpreReceiveTotalRemain() {
		return FcpreReceiveTotalRemain;
	}

	public void setFcpreReceiveTotalRemain(double fcpreReceiveTotalRemain) {
		FcpreReceiveTotalRemain = fcpreReceiveTotalRemain;
	}

	public double getFcReceiveBegin() {
		return FcReceiveBegin;
	}

	public void setFcReceiveBegin(double fcReceiveBegin) {
		FcReceiveBegin = fcReceiveBegin;
	}

	public double getFcReceiveTotalDebit() {
		return FcReceiveTotalDebit;
	}

	public void setFcReceiveTotalDebit(double fcReceiveTotalDebit) {
		FcReceiveTotalDebit = fcReceiveTotalDebit;
	}

	public double getFcReceiveTotalLend() {
		return FcReceiveTotalLend;
	}

	public void setFcReceiveTotalLend(double fcReceiveTotalLend) {
		FcReceiveTotalLend = fcReceiveTotalLend;
	}

	public double getFcreceiveTotalRemain() {
		return FcreceiveTotalRemain;
	}

	public void setFcreceiveTotalRemain(double fcreceiveTotalRemain) {
		FcreceiveTotalRemain = fcreceiveTotalRemain;
	}

	public double getPayBegin() {
		return PayBegin;
	}

	public void setPayBegin(double payBegin) {
		PayBegin = payBegin;
	}

	public double getPayTotalDebit() {
		return PayTotalDebit;
	}

	public void setPayTotalDebit(double payTotalDebit) {
		PayTotalDebit = payTotalDebit;
	}

	public double getPayTotalLend() {
		return PayTotalLend;
	}

	public void setPayTotalLend(double payTotalLend) {
		PayTotalLend = payTotalLend;
	}

	public double getPrePayBegin() {
		return PrePayBegin;
	}

	public void setPrePayBegin(double prePayBegin) {
		PrePayBegin = prePayBegin;
	}

	public double getPrePayTotalDebit() {
		return PrePayTotalDebit;
	}

	public void setPrePayTotalDebit(double prePayTotalDebit) {
		PrePayTotalDebit = prePayTotalDebit;
	}

	public double getPrePayTotalLend() {
		return PrePayTotalLend;
	}

	public void setPrePayTotalLend(double prePayTotalLend) {
		PrePayTotalLend = prePayTotalLend;
	}

	public double getPreReceiveBegin() {
		return PreReceiveBegin;
	}

	public void setPreReceiveBegin(double preReceiveBegin) {
		PreReceiveBegin = preReceiveBegin;
	}

	public double getPreReceiveTotalDebit() {
		return PreReceiveTotalDebit;
	}

	public void setPreReceiveTotalDebit(double preReceiveTotalDebit) {
		PreReceiveTotalDebit = preReceiveTotalDebit;
	}

	public double getPreReceiveTotalLend() {
		return PreReceiveTotalLend;
	}

	public void setPreReceiveTotalLend(double preReceiveTotalLend) {
		PreReceiveTotalLend = preReceiveTotalLend;
	}

	public double getReceiveBegin() {
		return ReceiveBegin;
	}

	public void setReceiveBegin(double receiveBegin) {
		ReceiveBegin = receiveBegin;
	}

	public double getReceiveTotalDebit() {
		return ReceiveTotalDebit;
	}

	public void setReceiveTotalDebit(double receiveTotalDebit) {
		ReceiveTotalDebit = receiveTotalDebit;
	}

	public double getReceiveTotalLend() {
		return ReceiveTotalLend;
	}

	public void setReceiveTotalLend(double receiveTotalLend) {
		ReceiveTotalLend = receiveTotalLend;
	}


}
