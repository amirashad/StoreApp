package az.store.db;

import az.store.client.Client;
import az.store.invoice.Invoice;
import az.store.invoice.InvoiceDetailed;
import az.store.invoice.InvoiceType;
import az.store.product.Product;
import az.store.production.Expenditure;
import az.store.types.CodeType;
import az.store.types.CodeValue;
import az.store.types.Report;
import az.store.types.User;
import az.store.payment.Payment;
import az.store.production.Production;
import az.store.types.PostavsikHesab;
import az.util.db.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amirjanov
 */
public class SelectQueries {

    private final DB db;
    private PreparedStatement prStmt;

    public static final SimpleDateFormat DATE_TIME_MS_FORMAT = new SimpleDateFormat("hh:mm:ss.SSS");

    public SelectQueries(DB db) {
        this.db = db;
    }

    public User checkUser(String userName, String passWord) {
        User result = null;

        String sqlStr = ""
                + " select "
                + "     ID, USER_NAME, PASS_WORD"
                + " from "
                + "     users "
                + " where "
                + "     USER_NAME = ? and PASS_WORD = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setString(1, userName);
            prStmt.setString(2, passWord);

            ResultSet rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new User();
                result.setId(rs.getInt("ID"));
                result.setUsername(rs.getString("USER_NAME"));
                result.setPassword(rs.getString("PASS_WORD"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, null);
        }

        return result;
    }

    public ArrayList<CodeType> getAllCodeTypes() {
        ArrayList<CodeType> result = new ArrayList<>();
        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     ID, TYPE_NAME, DESCRIPTION, VALID "
                + " from "
                + "     code_type "
                + " where "
                + "     valid = TRUE "
                + " order by "
                + "     TYPE_NAME ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);

            rs = prStmt.executeQuery();
            while (rs.next()) {
                CodeType codeType = new CodeType();

                codeType.setId(rs.getInt("ID"));
                codeType.setName(rs.getString("TYPE_NAME"));
                codeType.setDescription(rs.getString("DESCRIPTION"));
                codeType.setValid(rs.getBoolean("VALID"));

                result.add(codeType);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<CodeValue> getCodeValues(CodeType codeType) {
        ArrayList<CodeValue> result = new ArrayList<>();
        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     ID, TYPE_ID, VALUE_NAME, DESCRIPTION, VALID "
                + " from "
                + "     code_value "
                + " where "
                + "     valid = '1' ";
        if (codeType != null) {
            sqlStr += "     and TYPE_ID = ? ";
        }
        sqlStr += " order by "
                + "     VALUE_NAME";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            if (codeType != null) {
                prStmt.setInt(1, codeType.getId());
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                CodeValue codeValue = new CodeValue();
                codeValue.setId(rs.getInt("ID"));
                codeValue.setCodeType(new CodeType(rs.getInt("TYPE_ID")));
                codeValue.setName(rs.getString("VALUE_NAME"));
                codeValue.setDescription(rs.getString("DESCRIPTION"));
                codeValue.setValid(rs.getBoolean("VALID"));
                codeValue.setCodeType(codeType);
                result.add(codeValue);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<InvoiceType> getAllInvoiceTypes() {
        return getInvoiceTypes(null);
    }

    public InvoiceType getInvoiceType(int typeId) {
        ArrayList<InvoiceType> types = getInvoiceTypes(typeId);
        if (types != null && types.size() == 1) {
            return types.get(0);
        }
        return null;
    }

    public ArrayList<InvoiceType> getInvoiceTypes(Integer typeId) {
        ArrayList<InvoiceType> result = new ArrayList<>();
        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     id, name, descr, sign, client_label_name, client_code_type, production "
                + " from "
                + "     invoice_type "
                + " where "
                + "     valid = 'TRUE' ";
        if (typeId != null) {
            sqlStr += " and id = ? ";
        }
        sqlStr += " order by "
                + "     id ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            if (typeId != null) {
                prStmt.setInt(1, typeId);
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                InvoiceType invoiceType = new InvoiceType();

                invoiceType.setId(rs.getInt("ID"));
                invoiceType.setName(rs.getString("NAME"));
                invoiceType.setDescription(rs.getString("DESCR"));
                invoiceType.setSign(rs.getInt("SIGN"));
                invoiceType.setClientLabelName(rs.getString("client_label_name"));
                invoiceType.setClientCodeType(new CodeType(rs.getInt("client_code_type")));
                invoiceType.setProduction(rs.getBoolean("production"));

                if (invoiceType.getId() > 7) {
                    Logger.getLogger(SelectQueries.class.getName()).log(Level.WARNING, "Unknown invoiceType: {0}", invoiceType.getId());
                }

                result.add(invoiceType);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<PostavsikHesab> getPostavsikHesabs(CodeType codeType, CodeValue codeValue) {
        ArrayList<PostavsikHesab> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     ID, DATE_FROM, SUMMA, CARI, "
                + "     CLIENT_ID, CLIENT_NAME, CLIENT_DESCRIPTION, CLIENT_TYPE_ID, "
                + "     CURRENCY_TYPE, CURRENCY_TYPE_NAME, CURRENCY_VALUE "
                + " from "
                + "     V_POSTAVSIK_HESAB "
                + " where "
                + "     CARI = 1 ";
        if (codeType != null) {
            sqlStr += "     and CLIENT_TYPE_ID = ? ";
        } else if (codeValue != null) {
            sqlStr += "     and CLIENT_ID = ? ";
        }
        //                + "    and to_date(HESAB_POSTAVSIK_DATE_FROM, 'YYYY.MM.DD') >= to_date(?, 'YYYY.MM.DD') "
        sqlStr += " order by "
                + "     DATE_FROM ";

        Logger.getLogger(SelectQueries.class.getName()).log(Level.INFO, "codeType {0}, codeValue {1}", new Object[]{codeType, codeValue});

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            if (codeType != null) {
                prStmt.setInt(1, codeType.getId());
            } else if (codeValue != null) {
                prStmt.setInt(1, codeValue.getId());
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                PostavsikHesab postavsikClientHesab = new PostavsikHesab();
                postavsikClientHesab.setId(rs.getInt("ID"));
                postavsikClientHesab.setDateFrom(rs.getDate("DATE_FROM"));
                postavsikClientHesab.setSumma(rs.getDouble("SUMMA"));
                postavsikClientHesab.setCari(rs.getShort("CARI"));
                postavsikClientHesab.setClient(new CodeValue(rs.getInt("CLIENT_ID")));
                postavsikClientHesab.getClient().setName(rs.getString("CLIENT_NAME"));
                postavsikClientHesab.getClient().setDescription(rs.getString("CLIENT_DESCRIPTION"));
                postavsikClientHesab.getClient().setCodeType(new CodeType(rs.getInt("CLIENT_TYPE_ID")));
                postavsikClientHesab.setCurrencyType(new CodeValue(rs.getInt("CURRENCY_TYPE"), rs.getString("CURRENCY_TYPE_NAME")));
                postavsikClientHesab.setCurrencyValue(rs.getDouble("CURRENCY_VALUE"));
                result.add(postavsikClientHesab);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public Double[] getPostavsikPaymentsSUM(CodeValue postavsik, Date dtFrom, Date dtTo) {
        Double[] result = null;

        ResultSet rs = null;

        String sqlStr = "select sum(summa) summ,sum(CURRENCY_SUMMA) sumCurrency  "
                + " from v_payment "
                + " where "
                + "     code_value_id = ? ";
        if (dtFrom != null && dtTo != null) {
            sqlStr += " and DATE_TIME >= to_date(?, 'YYYY.MM.DD') ";
            sqlStr += " and DATE_TIME <= to_date(?, 'YYYY.MM.DD') ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setLong(1, postavsik.getId());
            if (dtFrom != null && dtTo != null) {
                prStmt.setDate(2, new java.sql.Date(dtFrom.getTime()));
                prStmt.setDate(3, new java.sql.Date(dtTo.getTime()));
            }

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new Double[2];
                result[0] = rs.getDouble("summ");
                result[1] = rs.getDouble("sumCurrency");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public Double[] getPostavsikPrixodsSUM(CodeValue postavsik, Date dtFrom, Date dtTo, boolean byPriceBuy) {
        Double[] result = null;

        ResultSet rs = null;

        String sqlStr = "select sum(nakladnoy_price_buy) summ,sum(NAKLADNOY_CURRENCY_SUMMA) summCurrency";
        if (!byPriceBuy) {
            sqlStr = "select sum(nakladnoy_price_real_sale) summ,sum(NAKLADNOY_CURRENCY_SUMMA) summCurrency";
        }
        sqlStr = sqlStr + " from v_nakladnoy "
                + " where "
                + "     nakladnoy_type = 1 and "//prixod
                + "     nakladnoy_postavsik_id = ?";
        if (dtFrom != null && dtTo != null) {
            sqlStr += " and NAKLADNOY_DATE >= to_date(?, 'YYYY.MM.DD') ";
            sqlStr += " and NAKLADNOY_DATE <= to_date(?, 'YYYY.MM.DD') ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, postavsik.getId());
            if (dtFrom != null && dtTo != null) {
                prStmt.setDate(2, new java.sql.Date(dtFrom.getTime()));
                prStmt.setDate(3, new java.sql.Date(dtTo.getTime()));
            }

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new Double[2];
                result[0] = rs.getDouble("summ");
                result[1] = rs.getDouble("summCurrency");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public Double[] getPostavsikVazvratsSUM(CodeValue postavsik, Date dtFrom, Date dtTo) {
        Double[] result = null;

        ResultSet rs = null;

        String sqlStr = "select sum(NAKLADNOY_PRICE_REAL_SALE) summ ,sum(NAKLADNOY_CURRENCY_SUMMA) summCurrency  "
                + " from v_nakladnoy "
                + " where "
                + "     nakladnoy_type = 2 and "//vazvrat
                + "     nakladnoy_postavsik_id = ?";
        if (dtFrom != null && dtTo != null) {
            sqlStr += " and NAKLADNOY_DATE >= to_date(?, 'YYYY.MM.DD') ";
            sqlStr += " and NAKLADNOY_DATE <= to_date(?, 'YYYY.MM.DD') ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, postavsik.getId());
            if (dtFrom != null && dtTo != null) {
                prStmt.setDate(2, new java.sql.Date(dtFrom.getTime()));
                prStmt.setDate(3, new java.sql.Date(dtTo.getTime()));
            }

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new Double[2];
                result[0] = rs.getDouble("summ");
                result[1] = rs.getDouble("summCurrency");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<Payment> getPayments(Date dtFrom, Date dtTo, CodeType codeType, CodeValue codeValue) {
        ArrayList<Payment> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     ID, CODE_VALUE_TYPE_ID, CODE_VALUE_ID, SUMMA, DESCR, "
                + "     DATE_TIME, CV_VALUE_NAME, CV_DESCRIPTION, CV_TYPE_NAME, CV_TYPE_DESC, "
                + "     CURRENCY_TYPE, CURRENCY_SUMMA, CURRENCY_TYPE_NAME "
                + " from "
                + "     V_PAYMENT "
                + " where "
                + "     1 = 1 ";
        if (dtFrom != null) {
            sqlStr += " and DATE_TIME >= to_date(?, 'YYYY.MM.DD') ";
        }
        if (dtTo != null) {
            sqlStr += " and DATE_TIME <= to_date(?, 'YYYY.MM.DD') ";
        }
        if (codeType != null) {
            sqlStr += " and CODE_VALUE_TYPE_ID = ? ";
        }
        if (codeValue != null) {
            sqlStr += " and CODE_VALUE_ID = ? ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (dtFrom != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dtFrom.getTime()));
            }
            if (dtTo != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dtTo.getTime()));
            }
            if (codeType != null) {
                prStmt.setInt(parameterIndex++, codeType.getId());
            }
            if (codeValue != null) {
                prStmt.setInt(parameterIndex++, codeValue.getId());
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("ID"));
                payment.setClient(new CodeValue());
                payment.getClient().setCodeType(new CodeType());
                payment.getClient().getCodeType().setId(rs.getInt("CODE_VALUE_TYPE_ID"));
                payment.getClient().getCodeType().setName(rs.getString("CV_TYPE_NAME"));
                payment.getClient().getCodeType().setDescription(rs.getString("CV_TYPE_DESC"));
                payment.getClient().setId(rs.getInt("CODE_VALUE_ID"));
                payment.getClient().setName(rs.getString("CV_VALUE_NAME"));
                payment.getClient().setDescription(rs.getString("CV_DESCRIPTION"));
                payment.setSumma(rs.getDouble("SUMMA"));
                payment.setDateTime(rs.getDate("DATE_TIME"));
                payment.setDescr(rs.getString("DESCR"));
//                payment.setCurrencyType(new CodeValue(rs.getInt("CURRENCY_TYPE"), rs.getString("CURRENCY_TYPE_NAME")));
//                payment.setCurrencySumma(rs.getDouble("CURRENCY_SUMMA"));
                result.add(payment);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public double getPaymentsSum(CodeType codeType, Date dtFrom, Date dtTo) {
        double result = 0.0;

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     sum(SUMMA) summ "
                + " from "
                + "     v_PAYMENT "
                + " where "
                + "     CODE_VALUE_TYPE_ID = ? "
                + "     and DATE_TIME >= to_date(?, 'YYYY.MM.DD') "
                + "     and DATE_TIME <= to_date(?, 'YYYY.MM.DD') ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, codeType.getId());
            prStmt.setDate(2, new java.sql.Date(dtFrom.getTime()));
            prStmt.setDate(3, new java.sql.Date(dtTo.getTime()));

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = rs.getDouble("SUMM");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result = 0.0;
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<Report> getCurrentReport() {
        ArrayList<Report> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = "select ID, FROM_DATE, TO_DATE, SUMMA, client, EXPENDITURE, POSTAVSIK, "
                + " CURRENT_SUMMA "
                + " from REPORT where CURRENT_SUMMA = 1 ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
//            prStmt.setInt(1, codeValue.getId());

            rs = prStmt.executeQuery();
            while (rs.next()) {
                Report report = new Report();

                report.setId(rs.getInt("ID"));
                report.setFromDate(rs.getDate("FROM_DATE"));
                report.setToDate(rs.getDate("TO_DATE"));
                report.setFirstSumma(rs.getDouble("SUMMA"));
                report.setClient(rs.getDouble("client"));
                report.setExpenditure(rs.getDouble("EXPENDITURE"));
                report.setPostavsik(rs.getDouble("POSTAVSIK"));
                report.setCurrentSumma(rs.getInt("CURRENT_SUMMA"));
                result.add(report);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public Product getProduct(Integer productId) {
        ArrayList<Product> products = getProducts(productId, null, null);

        if (products.size() == 1) {
            return products.get(0);
        }

        return null;
    }

    public ArrayList<Product> getProducts(Integer productId,
            CodeValue productType, CodeValue sourceType
    //            ,boolean withFullExpenditureDetails
    ) {
        ArrayList<Product> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     id, code, name, price_buy, price_sale, count, "
                + "     product_type_id, cv_product_type, "
                + "     measure_id, cv_product_measure, "
                + "     producer_id, cv_producer_name, "
                + "     source_type_id, cv_source_type, "
                + "     valid, "
                + "     tb.ps_codename "
                + " from "
                + "     v_products v "
                + " left join "
                + "     ("
                + "       select pp_id, string_agg(ps_code||' '||ps_name, E', ') ps_codename "
                + "         from v_products_templates "
                + "         group by pp_id "
                + "     ) tb on tb.pp_id = v.id "
                + " where "
                + "     valid = TRUE ";
        if (productId != null) {
            sqlStr += " and id = ? ";
        } else if (productType != null) {
            sqlStr += " and product_type_id = ? ";
        }
        if (sourceType != null && sourceType.getId() != null) {
            sqlStr += " and source_type_id = ? ";
        }
        sqlStr += " order by "
                + "     name ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (productId != null) {
                prStmt.setInt(parameterIndex++, productId);
            } else if (productType != null) {
                prStmt.setInt(parameterIndex++, productType.getId());
            }
            if (sourceType != null && sourceType.getId() != null) {
                prStmt.setInt(parameterIndex++, sourceType.getId());
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("ID"));
                product.setCode(rs.getString("code"));
                product.setName(rs.getString("name"));
                product.setPriceBuy(rs.getDouble("price_buy"));
                product.setPriceSale(rs.getDouble("price_sale"));
                product.setCount(rs.getInt("count"));
                product.setValid(rs.getBoolean("VALID"));
                product.setType(new CodeValue(rs.getInt("product_type_id"), rs.getString("cv_product_type")));
                product.setMeasure(new CodeValue(rs.getInt("measure_id"), rs.getString("cv_product_measure")));
                product.setProducer(new CodeValue(rs.getInt("producer_id"), rs.getString("cv_producer_name")));
                product.setSourceType(new CodeValue(rs.getInt("source_type_id"), rs.getString("cv_source_type")));
                if (product.getSourceType().equals(CodeValue.PST_PRODUCTION_PRODUCT)) {
//                    if (withFullExpenditureDetails) {
//                        product.setExpenditures(getProductExpenditures(product.getId()));
//                    } else 
//                    {
                    ArrayList<Expenditure> expenditures = new ArrayList<>();
                    Expenditure e = new Expenditure();
                    e.setObject(new Product());
                    e.getObjectAsProduct().setName(rs.getString("ps_codename"));
                    expenditures.add(e);
                    product.setExpenditures(expenditures);
//                    }
                }
                result.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result = null;
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public boolean existsProductReport() {
        int result = 0;

        ResultSet rs = null;

        String sqlStr = "select count(*) as cnt "
                + " from  report_product ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result > 0;
    }

    /*
    public Double[] getInvoicesSums(CodeType clientType, Date dtFrom,
            Date dtTo, InvoiceType invoiceType) {
        Double[] result = null;
        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     sum(invoice_total_buy_price) as price_buy, "
                + "     sum(invoice_total_sale_price) as price_sale "
                + " from "
                + "     invoices "
                + " where "
                + "     client_id != 0 "
                + "     and invoice_type = ? ";
        if (dtFrom != null) {
            sqlStr += " and invoice_date >= to_date(?, 'YYYY.MM.DD') ";
        }
        if (dtTo != null) {
            sqlStr += " and invoice_date <= to_date(?, 'YYYY.MM.DD') ";
        }
        if (clientType != null) {
            sqlStr += " and client_type = ? ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            prStmt.setInt(parameterIndex++, invoiceType.getId());
            if (dtFrom != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dtFrom.getTime()));
            }
            if (dtTo != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dtTo.getTime()));
            }
            if (clientType != null) {
                prStmt.setInt(parameterIndex++, clientType.getId());
            }

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new Double[2];
                result[0] = rs.getDouble("price_buy");
                result[1] = rs.getDouble("price_sale");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {
           db.closeStmtRs(prStmt, rs);
        }

        return result;
    }
     */
    public Double[] getInvoicesSums(CodeType clientType, Date dtFrom,
            Date dtTo, InvoiceType invoiceType) {
        Double[] result = null;

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     sum(invoice_total_buy_price) as price_buy, "
                + "     sum(invoice_total_sale_price) as price_sale "
                + " from "
                + "     invoices "
                + " where "
                + "     client_id != 0 "
                + "     and invoice_date >= to_date(?, 'YYYY.MM.DD') "
                + "     and invoice_date <= to_date(?, 'YYYY.MM.DD') "
                + "     and invoice_type = ? ";
        if (clientType != null) {
            sqlStr += " and client_type = ? ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setDate(1, new java.sql.Date(dtFrom.getTime()));
            prStmt.setDate(2, new java.sql.Date(dtTo.getTime()));
            prStmt.setInt(3, invoiceType.getId());
            if (clientType != null) {
                prStmt.setInt(4, clientType.getId());
            }

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new Double[2];
                result[0] = rs.getDouble("price_buy");
                result[1] = rs.getDouble("price_sale");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public Double[] getCurrentStoreSums(CodeValue sourceType) {
        Double[] result = null;

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     sum(COUNT * PRICE_BUY) as price_buy, "
                + "     sum(COUNT * PRICE_SALE) as price_sale "
                + " from "
                + "     products "
                + " where "
                + "     valid = true ";
        if (sourceType != null) {
            sqlStr += " and source_type_id = ? ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            if (sourceType != null) {
                prStmt.setInt(1, sourceType.getId());
            }

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new Double[2];
                result[0] = rs.getDouble("price_buy");
                result[1] = rs.getDouble("price_sale");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<Invoice> getInvoices(Integer invoiceId, Integer clientId, Boolean temp,
            java.util.Date dateFrom, java.util.Date dateTo, List<InvoiceType> invoiceTypes) {
        ArrayList<Invoice> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     id, invoice_type, invoice_date, invoice_type_name, "
                + "     client_id, "
                + "     invoice_total_buy_price, invoice_total_sale_price, "
                + "     temp, client_name "
                + " from "
                + "     v_invoices i "
                + " where "
                + "     1 = 1 ";
        if (invoiceId != null) {
            sqlStr += " and id = ? ";
        }
        if (clientId != null) {
            sqlStr += " and client_id = ? ";
        }
        if (temp != null) {
            sqlStr += " and temp = ? ";
        }
        if (dateFrom != null) {
            sqlStr += " and invoice_date >= to_date(?, 'YYYY.MM.DD') ";
        }
        if (dateTo != null) {
            sqlStr += " and invoice_date <= to_date(?, 'YYYY.MM.DD') ";
        }
        if (invoiceTypes != null && !invoiceTypes.isEmpty()) {
            String quest = Collections.nCopies(invoiceTypes.size(), "?").toString().replace("[", "").replace("]", "");
            sqlStr += " and invoice_type in (" + quest + ") ";
        }

        sqlStr += " order by id";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (invoiceId != null) {
                prStmt.setInt(parameterIndex++, invoiceId);
            }
            if (clientId != null) {
                prStmt.setInt(parameterIndex++, clientId);
            }
            if (temp != null) {
                prStmt.setBoolean(parameterIndex++, temp);
            }
            if (dateFrom != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateFrom.getTime()));
            }
            if (dateTo != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateTo.getTime()));
            }
            if (invoiceTypes != null && !invoiceTypes.isEmpty()) {
                for (InvoiceType it : invoiceTypes) {
                    prStmt.setInt(parameterIndex++, it.getId());
                }
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setInvoiceType(new InvoiceType(rs.getInt("invoice_type"), rs.getString("invoice_type_name")));
                invoice.setDate(rs.getDate("invoice_date"));
                invoice.setClient(new CodeValue(rs.getInt("client_id"), rs.getString("client_name")));
                invoice.setTotalPriceBuy(rs.getDouble("invoice_total_buy_price"));
                invoice.setTotalPriceSale(rs.getDouble("invoice_total_sale_price"));
                invoice.setTemp(rs.getBoolean("temp"));

                result.add(invoice);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result.clear();
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<InvoiceDetailed> getInvoiceDetaileds(Integer invoiceId, Integer clientId, Boolean temp,
            java.util.Date dateFrom, java.util.Date dateTo,
            List<InvoiceType> invoiceTypes,
            CodeValue productSourceType, CodeValue productType, Integer productId,
            Integer simpleProductForPP, Integer invoiceDetailedId) {
        ArrayList<InvoiceDetailed> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     id, "
                + "     invoice_id, invoice_date, invoice_total_buy_price, invoice_total_sale_price, "
                + "     invoice_type, invoice_type_name, "
                + "	client_id, client_name, count, buy_price, sale_price, "
                + "	product_id, product_code, product_name, product_price_buy, product_price_sale, "
                + "     product_type_id, product_type_name, "
                + "     product_measure_id, product_measure_name, "
                + "     product_source_type_id, product_source_type "
                + " from "
                + "     v_invoices_detailed "
                + " where "
                + "     1 = 1 ";
        if (invoiceId != null) {
            sqlStr += " and invoice_id = ? ";
        }
        if (clientId != null) {
            sqlStr += " and client_id = ? ";
        }
        if (temp != null) {
            sqlStr += " and temp = ? ";
        }
        if (dateFrom != null) {
            sqlStr += " and invoice_date >= to_date(?, 'YYYY.MM.DD') ";
        }
        if (dateTo != null) {
            sqlStr += " and invoice_date <= to_date(?, 'YYYY.MM.DD') ";
        }
        if (productId != null) {
            sqlStr += " and product_id = ? ";
        }
        if (invoiceTypes != null && !invoiceTypes.isEmpty()) {
            String quest = Collections.nCopies(invoiceTypes.size(), "?").toString().replace("[", "").replace("]", "");
            sqlStr += " and invoice_type in (" + quest + ")";
        }
        if (productType != null) {
            sqlStr += " and product_type_id = ? ";
        }
        if (productSourceType != null) {
            sqlStr += " and product_source_type_id = ? ";
        }
        if (simpleProductForPP != null) {
            sqlStr += " and product_id in (select pp_id from products_templates where ps_id = ?) ";
        }
        if (invoiceDetailedId != null) {
            sqlStr += " and id = ? ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (invoiceId != null) {
                prStmt.setInt(parameterIndex++, invoiceId);
            }
            if (clientId != null) {
                prStmt.setInt(parameterIndex++, clientId);
            }
            if (temp != null) {
                prStmt.setBoolean(parameterIndex++, temp);
            }
            if (dateFrom != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateFrom.getTime()));
            }
            if (dateTo != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateTo.getTime()));
            }
            if (productId != null) {
                prStmt.setInt(parameterIndex++, productId);
            }
            if (invoiceTypes != null && !invoiceTypes.isEmpty()) {
                for (InvoiceType it : invoiceTypes) {
                    prStmt.setInt(parameterIndex++, it.getId());
                }
            }
            if (productType != null) {
                prStmt.setInt(parameterIndex++, productType.getId());
            }
            if (productSourceType != null) {
                prStmt.setInt(parameterIndex++, productSourceType.getId());
            }
            if (simpleProductForPP != null) {
                prStmt.setInt(parameterIndex++, simpleProductForPP);
            }
            if (invoiceDetailedId != null) {
                prStmt.setInt(parameterIndex++, invoiceDetailedId);
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                InvoiceDetailed invoiceDetailed = new InvoiceDetailed();
                invoiceDetailed.setId(rs.getInt("id"));
                invoiceDetailed.setCount(rs.getInt("count"));
                invoiceDetailed.setPriceBuy(rs.getDouble("buy_price"));
                invoiceDetailed.setPriceSale(rs.getDouble("sale_price"));

                invoiceDetailed.setInvoice(new Invoice());
                invoiceDetailed.getInvoice().setInvoiceType(new InvoiceType(rs.getInt("invoice_type"), rs.getString("invoice_type_name")));
                invoiceDetailed.getInvoice().setId(rs.getInt("invoice_id"));
                invoiceDetailed.getInvoice().setDate(rs.getDate("invoice_date"));
                invoiceDetailed.getInvoice().setClient(new CodeValue(rs.getInt("client_id"), rs.getString("client_name")));
                invoiceDetailed.getInvoice().setTotalPriceBuy(rs.getDouble("invoice_total_buy_price"));
                invoiceDetailed.getInvoice().setTotalPriceSale(rs.getDouble("invoice_total_sale_price"));

                invoiceDetailed.setProduct(new Product(rs.getInt("product_id")));
                invoiceDetailed.getProduct().setCode(rs.getString("product_code"));
                invoiceDetailed.getProduct().setName(rs.getString("product_name"));
                invoiceDetailed.getProduct().setType(new CodeValue(rs.getInt("product_type_id"), rs.getString("product_type_name")));
                invoiceDetailed.getProduct().setMeasure(new CodeValue(rs.getInt("product_measure_id"), rs.getString("product_measure_name")));
                invoiceDetailed.getProduct().setSourceType(new CodeValue(rs.getInt("product_source_type_id"), rs.getString("product_source_type")));
                invoiceDetailed.getProduct().setPriceBuy(rs.getDouble("product_price_buy"));
                invoiceDetailed.getProduct().setPriceSale(rs.getDouble("product_price_sale"));

                result.add(invoiceDetailed);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result.clear();
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<InvoiceDetailed> getInvoiceDetailedsGroupByProduct(
            Integer invoiceId, Integer clientId, Boolean temp,
            java.util.Date dateFrom, java.util.Date dateTo,
            List<InvoiceType> invoiceTypes,
            CodeValue productSourceType, CodeValue productType, Integer productId) {
        ArrayList<InvoiceDetailed> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "	invoice_type, invoice_type_name, "
                + "	sum(count) count, "
                //                + "     string_agg(buy_price::text, E',') b_p, string_agg(sale_price::text, E','),  "
                //                + "     avg(buy_price) avg_buy_price, avg(sale_price) avg_sale_price, "
                //                + "     CASE "
                //                + "         WHEN avg(buy_price)=min(buy_price) THEN min(buy_price) "
                //                + "         ELSE null "
                //                + "	END display_buy_price, "
                //                + "	CASE "
                //                + "         WHEN avg(sale_price)=min(sale_price) THEN min(sale_price) "
                //                + "         ELSE null "
                //                + "	END display_sale_price, "
                + "     sum(count * buy_price) sum_buy_price, sum(count * sale_price) sum_sale_price, "
                + "	sum(invoice_total_buy_price) invoice_total_buy_price, sum(invoice_total_sale_price) invoice_total_sale_price, "
                + "	product_id, product_code, product_name, "
                + "     product_price_buy, product_price_sale, "
                + "	product_type_id, product_type_name, "
                + "	product_measure_id, product_measure_name, "
                + "	product_source_type_id, product_source_type "
                + " from "
                + "	v_invoices_detailed "
                + " where "
                + "	1 = 1";
        if (invoiceId != null) {
            sqlStr += " and invoice_id = ? ";
        }
        if (clientId != null) {
            sqlStr += " and client_id = ? ";
        }
        if (temp != null) {
            sqlStr += " and temp = ? ";
        }
        if (dateFrom != null) {
            sqlStr += " and invoice_date >= to_date(?, 'YYYY.MM.DD') ";
        }
        if (dateTo != null) {
            sqlStr += " and invoice_date <= to_date(?, 'YYYY.MM.DD') ";
        }
        if (productId != null) {
            sqlStr += " and product_id = ? ";
        }
        if (invoiceTypes != null && !invoiceTypes.isEmpty()) {
            String quest = Collections.nCopies(invoiceTypes.size(), "?").toString().replace("[", "").replace("]", "");
            sqlStr += " and invoice_type in (" + quest + ")";
        }
        if (productType != null) {
            sqlStr += " and product_type_id = ? ";
        }
        if (productSourceType != null) {
            sqlStr += " and product_source_type_id = ? ";
        }

        sqlStr += " "
                + " group by "
                + "	product_id, product_code, product_name, "
                + "	product_price_buy, product_price_sale, "
                + "	product_type_id, product_type_name, "
                + "	product_measure_id, product_measure_name, "
                + "	product_source_type_id, product_source_type,"
                + "	invoice_type, invoice_type_name ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (invoiceId != null) {
                prStmt.setInt(parameterIndex++, invoiceId);
            }
            if (clientId != null) {
                prStmt.setInt(parameterIndex++, clientId);
            }
            if (temp != null) {
                prStmt.setBoolean(parameterIndex++, temp);
            }
            if (dateFrom != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateFrom.getTime()));
            }
            if (dateTo != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateTo.getTime()));
            }
            if (productId != null) {
                prStmt.setInt(parameterIndex++, productId);
            }
            if (invoiceTypes != null && !invoiceTypes.isEmpty()) {
                for (InvoiceType it : invoiceTypes) {
                    prStmt.setInt(parameterIndex++, it.getId());
                }
            }
            if (productType != null) {
                prStmt.setInt(parameterIndex++, productType.getId());
            }
            if (productSourceType != null) {
                prStmt.setInt(parameterIndex++, productSourceType.getId());
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                InvoiceDetailed invoiceDetailed = new InvoiceDetailed();
//                invoiceDetailed.setId(rs.getInt("id"));
                invoiceDetailed.setCount(rs.getInt("count"));
//                invoiceDetailed.setPriceBuy(rs.getDouble("display_buy_price"));
//                invoiceDetailed.setPriceSale(rs.getDouble("display_sale_price"));
                invoiceDetailed.setTotalPriceBuy(rs.getDouble("sum_buy_price"));
                invoiceDetailed.setTotalPriceSale(rs.getDouble("sum_sale_price"));

                invoiceDetailed.setInvoice(new Invoice());
                invoiceDetailed.getInvoice().setInvoiceType(new InvoiceType(rs.getInt("invoice_type"), rs.getString("invoice_type_name")));
//                invoiceDetailed.getInvoice().setId(rs.getInt("invoice_id"));
//                invoiceDetailed.getInvoice().setDate(rs.getDate("invoice_date"));
//                invoiceDetailed.getInvoice().setClient(new CodeValue(rs.getInt("client_id"), rs.getString("client_name")));
                invoiceDetailed.getInvoice().setTotalPriceBuy(rs.getDouble("invoice_total_buy_price"));
                invoiceDetailed.getInvoice().setTotalPriceSale(rs.getDouble("invoice_total_sale_price"));

                invoiceDetailed.setProduct(new Product(rs.getInt("product_id")));
                invoiceDetailed.getProduct().setCode(rs.getString("product_code"));
                invoiceDetailed.getProduct().setName(rs.getString("product_name"));
                invoiceDetailed.getProduct().setType(new CodeValue(rs.getInt("product_type_id"), rs.getString("product_type_name")));
                invoiceDetailed.getProduct().setMeasure(new CodeValue(rs.getInt("product_measure_id"), rs.getString("product_measure_name")));
                invoiceDetailed.getProduct().setSourceType(new CodeValue(rs.getInt("product_source_type_id"), rs.getString("product_source_type")));
                invoiceDetailed.getProduct().setPriceBuy(rs.getDouble("product_price_buy"));
                invoiceDetailed.getProduct().setPriceSale(rs.getDouble("product_price_sale"));

                result.add(invoiceDetailed);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result.clear();
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public Double getProductCountInInvoice(int productId, int invoiceId) {
        double result = 0;

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     sum(count) as prod_count "
                + " from "
                + "     v_invoices_detailed "
                + " where "
                + "     product_id = ? and invoice_id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, productId);
            prStmt.setInt(2, invoiceId);

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = rs.getDouble("prod_count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result = 0;
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public Double[] getSumPricesOfInvoice(int invoiceId) {
        Double[] result = null;

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "     sum(count * buy_price) sum_total_buy_price,  "
                + "     sum(count * sale_price) sum_total_sale_price "
                + " from "
                + "     v_invoices_detailed "
                + " where "
                + "     invoice_id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, invoiceId);

            rs = prStmt.executeQuery();
            if (rs.next()) {
                result = new Double[2];
                result[0] = rs.getDouble("sum_total_buy_price");
                result[1] = rs.getDouble("sum_total_sale_price");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {

            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<Expenditure> getProductExpenditures(int productId) {
        ArrayList<Expenditure> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " SELECT "
                + "     ps_id, ps_code, ps_name, ps_count, ps_price_buy, ps_valid, "
                + "     ex_id, ex_name, "
                + "     price, descr "
                + " from "
                + "     v_products_templates "
                + " where "
                + "     pp_id = ? ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            prStmt.setInt(1, productId);

            rs = prStmt.executeQuery();
            while (rs.next()) {
                Expenditure expenditure = new Expenditure();

                int simpleProductId = rs.getInt("ps_id");
                int expenditureId = rs.getInt("ex_id");
                Object exp = null;
                if (simpleProductId != 0) {
                    Product ps = new Product(simpleProductId, rs.getString("ps_name"), rs.getString("ps_code"));
                    ps.setCount(rs.getInt("ps_count"));
                    ps.setPriceBuy(rs.getDouble("ps_price_buy"));
                    ps.setValid(rs.getBoolean("ps_valid"));
                    exp = ps;
                } else if (expenditureId != 0) {
                    exp = new CodeValue(expenditureId, rs.getString("ex_name"));
                }
                expenditure.setObject(exp);
                expenditure.setPrice(rs.getDouble("PRICE"));
                expenditure.setDescription(rs.getString("DESCR"));

                result.add(expenditure);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result.clear();
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<Production> getProductions(
            Integer productionProductId,
            java.util.Date dateFrom,
            java.util.Date dateTo) {
        ArrayList<Production> result = new ArrayList<>();

        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "	id, "
                + "	pp_id, "
                + "	pp_code, "
                + "	pp_name, "
                + "	count, "
                + "	production_price, "
                + "	production_date "
                + " from "
                + "	v_products_productions "
                + " where "
                + "	1 = 1 ";
        if (productionProductId != null) {
            sqlStr += " and pp_id = ? ";
        }
        if (dateFrom != null) {
            sqlStr += " and production_date::date >= to_date(?, 'YYYY.MM.DD') ";
        }
        if (dateTo != null) {
            sqlStr += " and production_date::date <= to_date(?, 'YYYY.MM.DD') ";
        }

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (productionProductId != null) {
                prStmt.setInt(parameterIndex++, productionProductId);
            }
            if (dateFrom != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateFrom.getTime()));
            }
            if (dateTo != null) {
                prStmt.setDate(parameterIndex++, new java.sql.Date(dateTo.getTime()));
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                Production production = new Production();
                production.setId(rs.getInt("id"));
                production.setProductionProduct(new Product());
                production.getProductionProduct().setId(rs.getInt("pp_id"));
                production.getProductionProduct().setCode(rs.getString("pp_code"));
                production.getProductionProduct().setName(rs.getString("pp_name"));
                production.setCount(rs.getInt("count"));
                production.setProductionPrice(rs.getDouble("production_price"));
                production.setProductionDate(rs.getTimestamp("production_date"));

                result.add(production);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
            result.clear();
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }

    public ArrayList<Client> getClients(Integer id, Boolean client, Boolean provider) {
        ArrayList<Client> result = new ArrayList<>();
        ResultSet rs = null;

        String sqlStr = ""
                + " select "
                + "	id, name, client, provider "
                + " from "
                + "     clients "
                + " where "
                + "     deactivated is null ";
        if (id != null) {
            sqlStr += " and id = ? ";
        }
        if (client != null) {
            sqlStr += " and client = ? ";
        }
        if (provider != null) {
            sqlStr += " and provider = ? ";
        }
        sqlStr += " order by "
                + "     name ";

        try {
            prStmt = db.getConnection().prepareStatement(sqlStr);
            int parameterIndex = 1;
            if (id != null) {
                prStmt.setInt(parameterIndex++, id);
            }
            if (client != null) {
                prStmt.setBoolean(parameterIndex++, client);
            }
            if (provider != null) {
                prStmt.setBoolean(parameterIndex++, provider);
            }

            rs = prStmt.executeQuery();
            while (rs.next()) {
                Client _client = new Client();
                _client.setId(rs.getInt("id"));
                _client.setName(rs.getString("name"));
                _client.setClient(rs.getBoolean("client"));
                _client.setProvider(rs.getBoolean("provider"));
                result.add(_client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SelectQueries.class.getName()).log(Level.SEVERE, sqlStr, ex);
        } finally {
            db.closeStmtRs(prStmt, rs);
        }

        return result;
    }
}
