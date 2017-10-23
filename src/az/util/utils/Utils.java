package az.util.utils;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/**
 *
 * @author Rashad Amirjanov
 */
public class Utils {

    private static final DecimalFormat DECIMAL_FORMAT_LOCALE = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);//getDefault()
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    //new DecimalFormat("#.###");

    public static double toDouble(String str) {
        double result = 0;

        try {
            Number number = DECIMAL_FORMAT_LOCALE.parse(str);
            result = number.doubleValue();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static String toString(double d) {
        return Utils.DECIMAL_FORMAT_LOCALE.format(d);
    }

    public static String toString(int d) {
        return Integer.toString(d);
    }

    public static String toStringDate(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMAT.format(date);
    }

    public static String toStringDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_TIME_FORMAT.format(date);
    }

    public static String leftPad(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static String rightPad(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String getApplicationPath() {
        try {
            String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (path.contains("/build/classes/")) {//when running from netbeans
                path = path.replace("/build/classes/", "/");
            } else if (path.endsWith(".jar")) {
                path = path.substring(0, path.lastIndexOf("/") + 1);
            }
            return path;
        } catch (URISyntaxException ex) {
            Logger.getGlobal().severe(ex.getMessage());
            return null;
        }
    }

    public static boolean isPositiveNumber(String number) {
        try {
            return Double.parseDouble(number) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
