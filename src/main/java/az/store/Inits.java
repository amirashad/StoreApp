package az.store;

import az.store.configs.Config;
import az.store.db.DeleteQueries;
import az.store.db.InsertQueries;
import az.store.db.SelectQueries;
import az.store.db.UpdateQueries;
import az.store.main.LoginDialog;
import az.store.types.User;
import az.util.db.DB;
import az.util.utils.Utils;
import com.jtattoo.plaf.mint.MintLookAndFeel;

import javax.swing.*;
import java.io.File;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rashad Amiranov
 */
public class Inits {

    public static final String CONFIG_FILE_MAIN = "az.store.configs.main";

    public static final String CONFIG_KEY_DBHOST = "db_host";
    public static final String CONFIG_KEY_DBNAME = "db_name";
    public static final String CONFIG_KEY_DBUSER = "db_user";
    public static final String CONFIG_KEY_DBPASS = "db_pass";
    public static final String CONFIG_KEY_COMPANYNAME = "company_name";
    public static final String CONFIG_KEY_APPTYPE = "app_type";
    public static final String CONFIG_KEY_DEFAULTUSER = "default_user";
    public static final String CONFIG_KEY_DEFAULTPASS = "default_pass";

    public static final String CONFIG_VALUE_APPTYPE_STORE = "store";
    public static final String CONFIG_VALUE_APPTYPE_PRODUCTION = "production";

    private static SelectQueries selectQueries = null;
    private static InsertQueries insertQueries = null;
    private static DeleteQueries deleteQueries = null;
    private static UpdateQueries updateQueries = null;

    private static DB db = null;
    private static User user = null;

    public static boolean initLookAndFeel() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
//            JDialog.setDefaultLookAndFeelDecorated(true);
//            UIManager.setLookAndFeel(new SubstanceNebulaLookAndFeel());
            UIManager.setLookAndFeel(new MintLookAndFeel());
//            UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
            return true;
        } catch (Exception ex) {
            Logger.getGlobal().warning(ex.getMessage());
            return false;
        }
//        return true;
    }

    public static void initDb() {
        String dbHost = Config.instance(CONFIG_FILE_MAIN).value(CONFIG_KEY_DBHOST);
        String dbName = Config.instance(CONFIG_FILE_MAIN).value(CONFIG_KEY_DBNAME);
        String dbUser = Config.instance(CONFIG_FILE_MAIN).value(CONFIG_KEY_DBUSER);
        String dbPass = Config.instance(CONFIG_FILE_MAIN).value(CONFIG_KEY_DBPASS);

        db = DB.instance(dbHost, "5432", dbName, dbUser, dbPass + "741");
        if (db != null) {
            Logger.getLogger("db").info("Database connected!");
            selectQueries = new SelectQueries(db);
            insertQueries = new InsertQueries(db);
            deleteQueries = new DeleteQueries(db);
            updateQueries = new UpdateQueries(db);
        } else {
            Logger.getLogger("db").severe("Can't connect to database!");
            System.exit(1);
        }
    }

    public static DB getDb() {
        return db;
    }

    public static void disposeDb() {
        if (db != null) {
            db.closeDB();
        }
        Logger.getGlobal().info("Database disconnected!");
    }

    public static InsertQueries getInsertQueries() {
        if (insertQueries == null) {
            Logger.getGlobal().warning("is null");
        }

        return insertQueries;
    }

    public static SelectQueries getSelectQueries() {
        if (selectQueries == null) {
            Logger.getGlobal().warning("is null");
        }

        return selectQueries;
    }

    public static DeleteQueries getDeleteQueries() {
        if (deleteQueries == null) {
            Logger.getGlobal().warning("is null");
        }

        return deleteQueries;
    }

    public static UpdateQueries getUpdateQueries() {
        if (updateQueries == null) {
            Logger.getGlobal().warning("is null");
        }
        return updateQueries;
    }

    public static void initConfig() {
        try {
            Config.instance(CONFIG_FILE_MAIN);
        } catch (MissingResourceException ex) {
            Logger.getGlobal().log(Level.SEVERE, CONFIG_FILE_MAIN, ex);
        }
    }

    public static User getUser() {
        return user;
    }

    public static void checkUser() {
        LoginDialog dialog = new LoginDialog(null, true);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        user = dialog.checkUser();
    }

    public static void initLogger() {
        File file = new File(System.getProperty("user.home") + "/.store/");
        file.mkdirs();

        String loggingConfigFile = Utils.getApplicationPath() + "logging.properties";
        System.setProperty("java.util.logging.config.file", loggingConfigFile);
        Logger.getGlobal().info("-----------------------------------------------");

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Logger.getGlobal().log(Level.SEVERE, t.toString(), e);
            }
        });
    }

    public static boolean isAppTypeProduction() {
        return Config.instance(Inits.CONFIG_FILE_MAIN).getString(Inits.CONFIG_KEY_APPTYPE).equals(CONFIG_VALUE_APPTYPE_PRODUCTION);
    }
}
