package az.store.db;

import az.store.invoice.Invoice;
import az.store.invoice.InvoiceDetailed;
import az.store.product.Product;
import az.store.production.Expenditure;
import az.store.types.CodeValue;
import az.util.db.DB;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rashad Amiranov
 */
public class UpdateQueries {

    private DB db;
    private PreparedStatement prStmt;

    public UpdateQueries(DB db) {
        this.db = db;
    }

    public boolean updateProduct(Product product) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " update "
                + "     products "
                + " set "
                + "     name = ?, "
                + "     type_id = ?, "
                + "     measure_id = ?, "
                + "     price_buy = ?, "
                + "     price_sale = ?, "
                + "     producer_id = ?, "
                + "     code = ?, "
                + "     count = ? "
                //                + "   ,  valid = ? "
                + " where "
                + "     id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setString(1, product.getName());
            prStmt.setInt(2, product.getTypeId());
            prStmt.setInt(3, product.getMeasureId());
            prStmt.setDouble(4, product.getPriceBuy());
            prStmt.setDouble(5, product.getPriceSale());
            prStmt.setInt(6, product.getProducer().getId());
            prStmt.setString(7, product.getCode());
            prStmt.setInt(8, product.getCount());
//            prStmt.setBoolean(9, product.isValid());
            prStmt.setInt(9, product.getId());
            if (prStmt.executeUpdate() != 1) {
                success = false;
            }
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(UpdateQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean updateProductPrice(int productId, Double priceBuy, Double priceSale) throws Exception {
        boolean success = true;

        if (priceBuy == null && priceSale == null) {
            return false;
        }

        String sqlStr = ""
                + " update "
                + "     products "
                + " set ";
        if (priceBuy != null) {
            sqlStr += "     price_buy = ? " + (priceSale != null ? ", " : "");
        }
        if (priceSale != null) {
            sqlStr += "     price_sale = ? ";
        }
        sqlStr += " where "
                + "     id = ? ";
        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (priceBuy != null) {
                prStmt.setDouble(parameterIndex++, priceBuy);
            }
            if (priceSale != null) {
                prStmt.setDouble(parameterIndex++, priceSale);
            }
            prStmt.setInt(parameterIndex++, productId);
            if (prStmt.executeUpdate() != 1) {
                success = false;
            }
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(UpdateQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean increaseProductCount(int productId, double count) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " update "
                + "     products "
                + " set "
                + "     count = (coalesce(count, 0) + ?) "
                + " where "
                + "     id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setDouble(1, count);
            prStmt.setLong(2, productId);
            if (prStmt.executeUpdate() != 1) {
                throw new Exception("No any product count updated: " + productId);
            }
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(UpdateQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean updateCodeValueName(CodeValue codeValue) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " update "
                + "     code_value "
                + " set "
                + "     value_name = ? "
                + " where "
                + "     id = ? ";
        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setString(1, codeValue.getName());
            prStmt.setInt(2, codeValue.getId());
            if (prStmt.executeUpdate() != 1) {
                success = false;
            }
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(UpdateQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean updateInvoice(Invoice invoice) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " update "
                + "     invoices "
                + " set "
                + "     invoice_date = ?, "
                + "     client_id = ?, "
                + "     invoice_total_buy_price = ?, "
                + "     invoice_total_sale_price = ?, "
                + "     temp = ? "
                + " where "
                + "     id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setDate(1, new java.sql.Date(invoice.getDate().getTime()));
            prStmt.setInt(2, invoice.getClient().getId());
            prStmt.setDouble(3, invoice.getTotalPriceBuy());
            prStmt.setDouble(4, invoice.getTotalPriceSale());
            prStmt.setBoolean(5, invoice.isTemp());
            prStmt.setInt(6, invoice.getId());
            prStmt.executeUpdate();
            prStmt.close();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(UpdateQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean updateInvoiceDetailed(InvoiceDetailed invoiceDetailed) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " update "
                + "     invoices_detailed "
                + " set "
                + "     product_id = ?, "
                + "     count = ?, "
                + "     buy_price = ?, "
                + "     sale_price = ? "
                + " where "
                + "     id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, invoiceDetailed.getProduct().getId());
            prStmt.setInt(2, invoiceDetailed.getCount());
            prStmt.setDouble(3, invoiceDetailed.getPriceBuy());
            prStmt.setDouble(4, invoiceDetailed.getPriceSale());
            prStmt.setInt(5, invoiceDetailed.getId());
            System.out.println(invoiceDetailed.getId());
            prStmt.executeUpdate();
            prStmt.close();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(UpdateQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean upsertProductExpenditure(int productId, Expenditure expenditure) throws Exception {
        boolean success = true;

        String sqlStr = "select upsert_products_template(?, ?, ?, ?, ?)";
        try {
            prStmt = db.getConnection().prepareCall(sqlStr);
            prStmt.setInt(1, productId);
            if (expenditure.isProduct()) {
                prStmt.setInt(2, expenditure.getObjectAsProduct().getId());
            } else {
                prStmt.setNull(2, java.sql.Types.INTEGER);
            }
            if (expenditure.isCodeValue()) {
                prStmt.setInt(3, expenditure.getObjectAsCodeValue().getId());
            } else {
                prStmt.setNull(3, java.sql.Types.INTEGER);
            }
            prStmt.setDouble(4, expenditure.getPrice());
            prStmt.setString(5, expenditure.getDescription());

            Logger.getGlobal().log(Level.INFO, "upsert_products_template {0}, {1}, {2}, {3}, {4}", new Object[]{
                    productId,
                    expenditure.isProduct() ? expenditure.getObjectAsProduct().getId() : 0,
                    expenditure.isCodeValue() ? expenditure.getObjectAsCodeValue().getId() : 0,
                    expenditure.getPrice(),
                    expenditure.getDescription()
            });

            prStmt.executeQuery();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(UpdateQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }
}
