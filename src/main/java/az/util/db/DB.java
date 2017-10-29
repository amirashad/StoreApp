package az.util.db;

import javax.swing.*;
import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rashad Amirjanov
 */
public class DB implements Serializable {

    private static DB tempDB = null;
    private String host;
    private String port;
    private String dbName;
    private String user;
    private String password;
    private Connection connection;
    private boolean connectionExists = false;

    /**
     * Constructor of DB class.
     *
     * @param host     Database server IP
     * @param port     Database port number
     * @param dbName   Database name
     * @param user     User name
     * @param password Password of user
     */
    public DB(String host, String port, String dbName, String user, String password) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
    }

    public static DB getDB() {
        return tempDB;
    }

    public static DB instance(String host, String port, String sid, String user, String pass) {
        if (tempDB == null) {
            tempDB = new DB(host, port, sid, user, pass);
        }

        if (tempDB.connectionExists == false) {
            if (tempDB.openDB()) {
                return tempDB;
            } else {
                JOptionPane.showMessageDialog(null, "Bazaya qoşulmaq mümkün olmadı", "Səhv", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            return tempDB;
        }
    }

    public boolean isConnectionExists() {
        return connectionExists;
    }

    /**
     * Creates the connection with oracle server
     *
     * @return Connection result. Success or not
     */
    public boolean openDB() {
        boolean success = true;
        try {
            // Load the driver
            DriverManager.registerDriver(new org.postgresql.Driver()); //oracle.jdbc.driver.OracleDriver());
            // Connect to the database
            // You can put a database name after the @ sign in the connection URL.
            // It shows there is connection or not

            connection = java.sql.DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + dbName, user, password);

            connection.setAutoCommit(true);
            connectionExists = true;
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "jdbc:postgresql://" + host + ":" + port + "/" + dbName, ex);
        }
        return success;
    }

    /**
     * Closes the connection with database server
     *
     * @return
     */
    public boolean closeDB() {
        boolean success = true;

        try {
            connectionExists = false;
            connection.close();
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Closing connection error", ex);
            success = false;
        }

        return success;
    }

    private boolean isReconnectNeeded() {
        int question = JOptionPane.showConfirmDialog(null,
                "Baza ilə əlaqə zamanı səhv baş verdi! Əlaqə yenidən yoxlansınmı?\n"
                        + "'Xeyr' cavabında sistem söndürüləcək!",
                "Səhv!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);
        return question == JOptionPane.YES_OPTION;
    }

    public Connection getConnection() {
        boolean closed = isClosed();

        if (closed == false) {
            return connection;
        } else {
            while (isReconnectNeeded()) {
                //ProgressDialog progressDialog = new ProgressDialog(false);
                // progressDialog.startProgress();
                if (openDB()) {
                    // progressDialog.stopProgress();
                    return connection;
                }/* else {
                    progressDialog.stopProgress();
                }*/
            }

            System.exit(1);
            return null;
        }
    }

    public boolean isClosed() {
        PreparedStatement prStmt = null;

        try {
            prStmt = connection.prepareStatement("select now()");
            prStmt.executeQuery();
            prStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).warning(ex.getMessage());
        } finally {
            closeStmtRs(prStmt, null);
        }

        try {
            return connection.isClosed();
        } catch (SQLException ex) {
            return true;
        }
    }

    public String getDbName() {
        return dbName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void closeStmtRs(Statement stmt, ResultSet rs) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).warning(ex.getMessage());
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).warning(ex.getMessage());
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).warning(ex.getMessage());
        }
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).warning(ex.getMessage());
        }
    }
}
