package az.util.components.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author Rashad Amirjanov
 */
public class JTextFieldOnlyNumber extends PlainDocument {

    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        if (str == null) {
            return;
        }
        str = str.trim();
        String oldString = getText(0, getLength());
        String newString = oldString.substring(0, offs) + str
                + oldString.substring(offs);
        try {
            Double.parseDouble(newString + "0");
            super.insertString(offs, str, a);
        } catch (NumberFormatException e) {
        }
    }
}
