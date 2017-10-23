package az.store.invoice;

import az.store.Inits;
import az.store.configs.Config;
import az.store.report.Reportable;
import az.util.utils.Utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rashad Amirjanov
 */
public class InvoiceReport extends Reportable {

    String header = "<html>\n"
            + "<head>\n"
            + "<meta charset=\"UTF-8\"> \n"
            + "<style>\n"
            + " table {\n"
            + "     font-size: 14;\n"
            + " }\n"
            + "	table#products, table#products th, table#products td {\n"
            + "     border: 1px solid black;\n"
            + "     border-collapse: collapse;\n"
            + " }\n"
            + "	table#products th, table#products td {\n"
            + "     padding: 5px;\n"
            + "     font-size: 12;\n"
            + "	}\n"
            + "</style>\n"
            + "</head>\n"
            + "<body>";

    String invoiceNumber = "<h2 align=\"center\">Qaimə No %s</h2>";
    String invoiceHeader = "<table id=\"TableInvoiceInfo\" style=\"width:100%%\">\n"
            + "  <tr>\n"
            + "    <td>Tarix:</td>\n"
            + "    <td>%s</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td>Müştəri:</td>\n"
            + "    <td>%s</td>\n"
            + "  </tr>\n"
            + "  <tr>\n"
            + "    <td>Satıcı:</td>\n"
            + "    <td>%s</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "\n"
            + "<br/>\n"
            + "<br/>";

    String tableHeader = "<table id=\"products\" style=\"width:100%\">\n"
            + "  <tr>\n"
            + "    <th>No</th>\n"
            + "    <th>Kodu</th>\n"
            + "    <th>Malın adı</th>\n"
            + "    <th>Sayı</th>\n"
            + "    <th>Qiyməti<br>(AZN)</th>\n"
            + "    <th>Cəmi<br>(AZN)</th>\n"
            + "  </tr>";

    String tableRow = "  <tr>\n"
            + "    <td>%s</td>\n"
            + "    <td>%s</td>\n"
            + "    <td>%s</td>\n"
            + "    <td>%s</td>\n"
            + "    <td>%s</td>\n"
            + "    <td>%s</td>\n"
            + "  </tr>";

    String tableFooter = "\n</table>";

    String footer = "<br/>\n"
            + "<br/>\n"
            + "<table id=\"TableFooter\" style=\"width:100%\">\n"
            + "  <tr>\n"
            + "    <td>Təhvil verdim</td>\n"
            + "    <td>_____________________</td>\n"
            + "    <td>Təhvil aldım</td>\n"
            + "    <td>_____________________</td>\n"
            + "  </tr>\n"
            + "</table>\n"
            + "<br/>\n"
            + "<br/>\n"
            //            + "<p align=\"right\" style=\"opacity:0.6\">ButaGlassProduction sistemindən çap olunmuşdur</p>\n"
            + "\n"
            + "</body>\n"
            + "</html>";

    @Override
    public boolean createReport(Object object) {

        if (object == null) {
            return false;
        }

        Invoice invoice = (Invoice) object;

        BufferedWriter writer = null;
        try {
            File file = createTemporaryFile();
            if (file != null) {
                Logger.getLogger(InvoiceReport.class.getName()).warning("Can't create temporary file for report!");
            }

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));

            writer.write(header);
            writer.write(String.format(invoiceNumber, String.valueOf(invoice.getId())));
            writer.write(String.format(invoiceHeader,
                    Utils.toStringDate(invoice.getDate()),
                    invoice.getClient().getName(),
                    Config.instance(Inits.CONFIG_FILE_MAIN).getString(Inits.CONFIG_KEY_COMPANYNAME)));
            writer.write(tableHeader);
            int i = 0;
            for (InvoiceDetailed tempInvoice : invoice.getDetaileds()) {
                i++;
                writer.write(String.format(tableRow,
                        String.valueOf(i),
                        tempInvoice.getProduct().getCode(),
                        tempInvoice.getProduct().getName(),
                        String.valueOf(tempInvoice.getCount()),
                        Utils.toString(tempInvoice.getPriceSale()),
                        Utils.toString(tempInvoice.getCount() * tempInvoice.getPriceSale())
                ));
            }

            writer.write(String.format(tableRow, "", "", " ", "", "", "-"));
            writer.write(String.format(tableRow, "", "", "Ümumi", "",
                    "",
                    Utils.toString(invoice.getTotalPriceSale())
            ));
            writer.write(tableFooter);
            writer.write(footer);
            writer.close();

            openReportExternally();
        } catch (IOException ex) {
            Logger.getLogger(InvoiceReport.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

}
