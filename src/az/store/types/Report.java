package az.store.types;

import java.util.Date;

/**
 *
 * @author Rashad Amirjanov
 */
public class Report {

    private Integer id;
    private Date fromDate;
    private Date toDate;
    private Double firstSumma;
    private Double client;
    private Double expenditure;
    private Double postavsik;
    private Double lastSumma;
    private Integer currentSumma;

    public Report() {
    }

    public Integer getCurrentSumma() {
        return currentSumma;
    }

    public void setCurrentSumma(Integer currentSumma) {
        this.currentSumma = currentSumma;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getClient() {
        return client;
    }

    public void setClient(Double income) {
        this.client = income;
    }

    public Double getPostavsik() {
        return postavsik;
    }

    public void setPostavsik(Double postavsik) {
        this.postavsik = postavsik;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Double getFirstSumma() {
        return firstSumma;
    }

    public void setFirstSumma(Double firstSumma) {
        this.firstSumma = firstSumma;
    }

    public Double getLastSumma() {
        return lastSumma;
    }

    public void setLastSumma(Double lastSumma) {
        this.lastSumma = lastSumma;
    }

}
