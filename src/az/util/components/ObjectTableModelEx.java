package az.util.components;

import java.util.Arrays;

/**
 *
 * @author Rashad Amirjanov
 */
public abstract class ObjectTableModelEx extends ObjectTableModel {

    protected boolean[] columnVisibilities;

    public ObjectTableModelEx(String[] columnNames) {
        super(columnNames);
        this.columnVisibilities = new boolean[columnNames.length];
        Arrays.fill(columnVisibilities, true);
    }

    @Override
    public int getColumnCount() {
        int count = columnNames.length;
        for (boolean v : columnVisibilities) {
            if (!v) {
                count -= 1;
            }
        }
        return count;
    }

    /**
     * Converts JTable column to model's column index.
     *
     * @param columnIndex - JTable column index
     * @return
     */
    protected int convertToMyColumn(int columnIndex) {
        int visibleColumnCount = -1;

        for (int i = 0; i < columnVisibilities.length; i++) {
            visibleColumnCount += columnVisibilities[i] == true ? 1 : 0;
            if (visibleColumnCount == columnIndex) {
                return i;
            }
        }

        return visibleColumnCount;
    }

    public void setColumnsVisible(boolean b, int... columns) {
        for (int c : columns) {
            columnVisibilities[c] = b;
        }
        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[convertToMyColumn(column)];
    }

    @Override
    public void setColumnName(int column, String name) {
        columnNames[convertToMyColumn(column)] = name;
        fireTableStructureChanged();
    }

    @Override
    public double getSumOfColumn(int columnIndex) {
        double summa = 0;

        int col = convertToMyColumn(columnIndex);

        for (int i = 0; i < this.getRowCount(); i++) {
            Object object = this.getValueAt(i, col);
            if (object == null) {
                continue;
            }

            double d = (Double) object;
            summa += d;
        }

        return summa;
    }
}
