package az.store.db;

import az.store.production.Expenditure;
import az.store.payment.Payment;
import az.util.db.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rashad Amiranov
 */
public class DeleteQueries {

    private DB db;
    private PreparedStatement prStmt;

    public DeleteQueries(DB db) {
        this.db = db;
    }

    public boolean deletePayment(Payment payment) throws Exception {
        boolean success = true;

        String sqlStr = "delete from payment where id=?";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, payment.getId());

            prStmt.executeUpdate();
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(DeleteQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean deleteInvoice(int invoiceId) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " delete from "
                + "     invoices "
                + " where "
                + "     id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, invoiceId);
            int cnt = prStmt.executeUpdate();
            if (cnt != 1) {
                throw new Exception("No any invoice deleted:" + invoiceId);
            }
        } catch (Exception ex) {
            success = false;
            Logger.getLogger(DeleteQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean deleteInvoiceDetailed(Integer invoiceDetailedId, Integer invoiceId) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " delete from "
                + "     invoices_detailed "
                + " where ";
        if (invoiceDetailedId != null) {
            sqlStr += "     id = ? ";
        } else if (invoiceId != null) {
            sqlStr += "     invoice_id = ? ";
        } else {
            throw new Exception("invoiceDetailedId and invoiceId both are null!");
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            if (invoiceDetailedId != null) {
                prStmt.setInt(1, invoiceDetailedId);
            } else if (invoiceId != null) {
                prStmt.setInt(1, invoiceId);
            }
            prStmt.executeUpdate();
        } catch (Exception ex) {
            success = false;
            Logger.getLogger(DeleteQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean deleteProduct(int productId) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " update "
                + "     products "
                + " set "
                + "     valid = 'FALSE' "
                + " where "
                + "     id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, productId);
            if (prStmt.executeUpdate() != 1) {
                throw new Exception("No any product deleted: " + productId);
            }
        } catch (Exception ex) {
            success = false;
            Logger.getLogger(DeleteQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }

    public boolean deleteProductExpenditure(int productId, Expenditure expenditure) throws Exception {
        boolean result = true;
        ResultSet rs = null;

        String sqlStr = ""
                + " delete from "
                + "     products_templates "
                + " where "
                + "     pp_id = ? ";
        if (expenditure.isProduct()) {
            sqlStr += "and ps_id = ? ";
        } else {
            sqlStr += "and ex_id = ? ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, productId);
            prStmt.setInt(2, expenditure.isProduct()
                    ? expenditure.getObjectAsProduct().getId()
                    : expenditure.getObjectAsCodeValue().getId());

            int tempCount = prStmt.executeUpdate();
            Logger.getGlobal().log(Level.INFO, sqlStr + "\n {0}, {1}, ispr={2}, iscv={3}", new Object[]{
                productId, expenditure.isProduct()
                ? expenditure.getObjectAsProduct().getId()
                : expenditure.getObjectAsCodeValue().getId(),
                expenditure.isProduct(), expenditure.isCodeValue()
            });
            if (tempCount != 1) {
                throw new Exception("No any expenditure deleted from products_templates: "
                        + "pp=" + productId + ", "
                        + "ps=" + expenditure.getObjectAsProduct()
                        + "ex=" + expenditure.getObjectAsCodeValue());
            }
        } catch (SQLException ex) {
            result = false;
            Logger.getLogger(DeleteQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public boolean deleteProduction(int productionId) throws Exception {
        boolean success = true;

        String sqlStr = ""
                + " delete from "
                + "     products_productions "
                + " where "
                + "     id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, productionId);
            int cnt = prStmt.executeUpdate();
            if (cnt != 1) {
                throw new Exception("No any production deleted:" + productionId);
            }
        } catch (Exception ex) {
            success = false;
            Logger.getLogger(DeleteQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            throw ex;
        } finally {
            db.closeStmtRs(prStmt, null);
        }

        return success;
    }
}
