package az.store.db;

import az.store.invoice.Invoice;
import az.store.invoice.InvoiceDetailed;
import az.store.product.Product;
import az.store.production.Production;
import az.store.types.CodeValue;
import az.store.payment.Payment;
import az.store.types.PostavsikHesab;
import az.util.db.DB;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rashad Amiranov
 */
public class InsertQueries {

    private DB db;
    private PreparedStatement prStmt;

    public InsertQueries(DB db) {
        this.db = db;
    }

    public int insertCodeValue(CodeValue codeValue) throws SQLException {
        int result = -1;
        ResultSet rs = null;

        String sqlStr = "insert into code_value"
                + "(TYPE_ID, VALUE_NAME, DESCRIPTION, VALID) values"
                + "(?, ?, ?, 'TRUE')";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            prStmt.setInt(1, codeValue.getCodeType().getId());
            prStmt.setString(2, codeValue.getName());
            prStmt.setString(3, codeValue.getDescription());

            prStmt.executeUpdate();

            rs = prStmt.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt("id");
            }
        } catch (SQLException ex) {
            result = -1;
            Logger.getLogger(InsertQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public int insertProduct(Product product) throws Exception {
        int result = -1;
        ResultSet rs = null;

        String sqlStr = ""
                + " insert into "
                + "     products (TYPE_ID, MEASURE_ID, NAME, CODE, PRICE_BUY, PRICE_SALE, PRODUCER_ID, VALID, SOURCE_TYPE_ID) "
                + "     values   (?      , ?         , ?   , ?   , ?        , ?         , ?          , 'TRUE', ?) ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            prStmt.setInt(1, product.getTypeId());
            prStmt.setInt(2, product.getMeasureId());
            prStmt.setString(3, product.getName());
            prStmt.setString(4, product.getCode());
            prStmt.setDouble(5, product.getPriceBuy());
            prStmt.setDouble(6, product.getPriceSale());
            prStmt.setInt(7, product.getProducer() != null ? product.getProducer().getId() : null);
            prStmt.setInt(8, product.getSourceType().getId());

            prStmt.executeUpdate();
            rs = prStmt.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt("id");
            }
        } catch (SQLException ex) {
            result = -1;
            Logger.getLogger(InsertQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public boolean insertPostavsikHesab(PostavsikHesab postavsikHesab) throws SQLException {
        boolean success = true;

        String sqlStr = ""
                + " insert into "
                + "     POSTAVSIK_HESAB"
                + "         (HESAB_POSTAVSIK_ID, HESAB_POSTAVSIK_DATE_FROM, HESAB_POSTAVSIK_SUMMA, "
                + "         HESAB_POSTAVSIK_CARI,CURRENCY_TYPE, CURRENCY_VALUE) "
                + "     values "
                + "         (?, ?, ?,"
                + "         ?, ?, ?)";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setLong(1, postavsikHesab.getClient().getId());
            prStmt.setDate(2, new Date(postavsikHesab.getDateFrom().getTime()));
            prStmt.setDouble(3, postavsikHesab.getSumma());
            prStmt.setDouble(4, postavsikHesab.getCari());
            prStmt.setInt(5, postavsikHesab.getCurrencyType() == null ? -1 : postavsikHesab.getCurrencyType().getId());
            prStmt.setDouble(6, postavsikHesab.getCurrencyValue());

            prStmt.executeUpdate();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(InsertQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean insertPayment(Payment payment) throws SQLException {
        boolean success = true;

        String sqlStr = ""
                + " insert into "
                + "     PAYMENT"
                + "         (CODE_VALUE_TYPE_ID, CODE_VALUE_ID, SUMMA, DESCR, DATE_TIME "
//                + "         , CURRENCY_TYPE, CURRENCY_SUMMA "
                + "         ) "
                + "     values"
                + "         (?, ?, ?, ?, ? "
//                + "         , ?, ?"
                + "         )";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, payment.getClient().getCodeType().getId());
            prStmt.setInt(2, payment.getClient().getId());
            prStmt.setDouble(3, payment.getSumma());
            prStmt.setString(4, payment.getDescr());
            prStmt.setDate(5, new java.sql.Date(payment.getDateTime().getTime()));
//            prStmt.setInt(5, payment.getCurrencyType().getId());
//            prStmt.setDouble(6, payment.getCurrencySumma());

            prStmt.executeUpdate();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(InsertQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    //inserts invoice as temporary
    public int insertInvoice(Invoice invoice) throws Exception {
        int result = -1;
        ResultSet rs = null;

        String sqlStr = ""
                + " insert into "
                + "     invoices("
                + "         invoice_type, invoice_date, client_id, "
                + "         invoice_total_buy_price, invoice_total_sale_price)"
                + "     values(?, ?, ?, ?, ?)";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            prStmt.setInt(1, invoice.getInvoiceType().getId());
            if (invoice.getDate() == null) {
                prStmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            } else {
                prStmt.setDate(2, new java.sql.Date(invoice.getDate().getTime()));
            }
            prStmt.setInt(3, invoice.getClient().getId());
            prStmt.setDouble(4, invoice.getTotalPriceBuy());
            prStmt.setDouble(5, invoice.getTotalPriceSale());

            prStmt.executeUpdate();

            rs = prStmt.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt("id");
            }
        } catch (SQLException ex) {
            result = -1;
            Logger.getLogger(InsertQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public boolean insertInvoiceDetailed(Integer invoiceId, InvoiceDetailed invoiceDetailed) throws Exception {
        boolean success = true;

        if (invoiceId == null) {
            throw new Exception("Inserted InvoiceDetailed.invoiceId can not be null!");
        }

        String sqlStr = ""
                + " insert into "
                + "     invoices_detailed("
                + "         invoice_id, "
                + "         product_id, "
                + "         count, "
                + "         buy_price, "
                + "         sale_price) "
                + "	values(?, ?, ?, ?, ?) ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, invoiceId);
            if (invoiceDetailed.getProduct() != null) {
                prStmt.setInt(2, invoiceDetailed.getProduct().getId());
            } else {
                prStmt.setInt(2, invoiceDetailed.getProduct().getId());
            }
            prStmt.setDouble(3, invoiceDetailed.getCount());
            prStmt.setDouble(4, invoiceDetailed.getPriceBuy());
            prStmt.setDouble(5, invoiceDetailed.getPriceSale());

            if (prStmt.executeUpdate() == 0) {
                success = false;
            }
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(InsertQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public int insertProduction(Production production) throws Exception {
        int result = -1;
        ResultSet rs = null;

        String sqlStr = ""
                + " insert into "
                + "     products_productions("
                + "         pp_id, count, "
                + "         production_price, production_date) "
                + "     values("
                + "         ?, ?, "
                + "         ?, now())";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
            prStmt.setInt(1, production.getProductionProduct().getId());
            prStmt.setDouble(2, production.getCount());
            prStmt.setDouble(3, production.getProductionPrice());
//            prStmt.setDate(4, new Date(production.getProductionDate().getTime()));

            prStmt.executeUpdate();
            rs = prStmt.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt("id");
            }
        } catch (SQLException ex) {
            result = -1;
            Logger.getLogger(InsertQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }
}
