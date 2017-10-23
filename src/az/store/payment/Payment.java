package az.store.payment;

import az.store.types.CodeValue;
import java.util.Date;

/**
 *
 * @author Rashad Amirjanov
 */
public class Payment {

    private Integer id;
    private CodeValue client;
    private Double summa;
    private Date dateTime;
    private String descr;

    public Payment() {
    }

    public Payment(Integer id) {
        this.id = id;
    }

    public Payment(Integer id, CodeValue client, Double summa, Date dateTime) {
        this.id = id;
        this.client = client;
        this.summa = summa;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getSumma() {
        return summa;
    }

    public void setSumma(Double summa) {
        this.summa = summa;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public CodeValue getClient() {
        return client;
    }

    public void setClient(CodeValue client) {
        this.client = client;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String str = dateTime + " tarixində " + summa + " manat ";
        if (client != null && !client.getName().isEmpty()) {
            str += client.getName() + " ödənişi";
        } else {
            str += " ödəniş";
        }

        return str;
    }
}
