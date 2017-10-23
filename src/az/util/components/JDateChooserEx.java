package az.util.components;

import com.toedter.calendar.JDateChooser;

/**
 *
 * @author Rashad Amirjanov
 */
public class JDateChooserEx extends JDateChooser {

    public JDateChooserEx() {
        super("dd.MM.yyyy", "##.##.####", '_');
        getJCalendar().setWeekOfYearVisible(false);
        setDateFormatString("dd.MM.yyyy");
    }
}
