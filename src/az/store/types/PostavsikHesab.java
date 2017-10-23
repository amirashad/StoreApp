package az.store.types;

import java.util.Date;

/**
 *
 * @author Rashad Amirjanov
 */
public class PostavsikHesab {

    private Integer id;
    private Date dateFrom;
    private short cari;
    private Date dateTo;
    private CodeValue client;
    private Double summa;
    private CodeValue currencyType;
    private Double currencyValue;

    private Double payment;
    private Double paymentCurrency;
    private Double productPrixod;
    private Double productPrixodCurrency;
    private Double productVazvrat;
    private Double productVazvratCurrency;
    private Double lastSumma;
    private Double lastSummaCurrency;

    public PostavsikHesab() {
        cari = 1;
        summa = 0.0;
        currencyValue = 0.0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public short getCari() {
        return cari;
    }

    public void setCari(short cari) {
        this.cari = cari;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public CodeValue getClient() {
        return client;
    }

    public void setClient(CodeValue client) {
        this.client = client;
    }

    public CodeValue getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CodeValue currencyType) {
        this.currencyType = currencyType;
    }

    public Double getSumma() {
        return summa;
    }

    public void setSumma(Double summa) {
        this.summa = summa;
    }

    public Double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(Double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(Double paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public Double getProductPrixod() {
        return productPrixod;
    }

    public void setProductPrixod(Double productPrixod) {
        this.productPrixod = productPrixod;
    }

    public Double getProductPrixodCurrency() {
        return productPrixodCurrency;
    }

    public void setProductPrixodCurrency(Double productPrixodCurrency) {
        this.productPrixodCurrency = productPrixodCurrency;
    }

    public Double getProductVazvrat() {
        return productVazvrat;
    }

    public void setProductVazvrat(Double productVazvrat) {
        this.productVazvrat = productVazvrat;
    }

    public Double getProductVazvratCurrency() {
        return productVazvratCurrency;
    }

    public void setProductVazvratCurrency(Double productVazvratCurrency) {
        this.productVazvratCurrency = productVazvratCurrency;
    }

    public Double getLastSumma() {
        return lastSumma;
    }

    public void setLastSumma(Double lastSumma) {
        this.lastSumma = lastSumma;
    }

    public Double getLastSummaCurrency() {
        return lastSummaCurrency;
    }

    public void setLastSummaCurrency(Double lastSummaCurrency) {
        this.lastSummaCurrency = lastSummaCurrency;
    }
}
