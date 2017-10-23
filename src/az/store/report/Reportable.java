package az.store.report;

import az.util.utils.Utils;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rashad Amirjanov
 */
public abstract class Reportable {

    protected File file;

    protected File createTemporaryFile() {
        try {
            String prefix = "invoice_" + Utils.toStringDate(new Date()) + "_";
            file = File.createTempFile(prefix, ".html");
        } catch (IOException ex) {
            Logger.getLogger(Reportable.class.getName()).log(Level.SEVERE, null, ex);
        }

        return file;
    }

    abstract public boolean createReport(Object object);

    public boolean openReportExternally() {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(Reportable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }
}
