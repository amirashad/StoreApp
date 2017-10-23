package az.util.components;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Rashad Amirjanov
 */
public abstract class ObjectTableModel extends AbstractTableModel {

    protected String[] columnNames;
    protected ArrayList<Object> rowObjects = new ArrayList<>();

    public ObjectTableModel(String[] columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return rowObjects.size();
    }

    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

//    public String getValueAt(int rowIndex, int columnIndex) {
////        String[] rowValues = this.users.get(rowIndex);
//        CodeValue codeValue = this.rowObjects.get(rowIndex);
//
//        String result = "";
//        switch (columnIndex) {
//            case 0:
//                result = codeValue.getValueName();
//                break;
//            case 1:
//                result = codeValue.getDescription();
//                break;
////            case 2:
////                result = codeValue.getCodeType().getTypeName();
////                break;
//        }
//        if (result == null) {
//            result = "";
//        }
//
//        return result;
//    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setColumnName(int column, String name) {
        columnNames[column] = name;
        fireTableStructureChanged();
    }

    public ArrayList<Object> getRowObjects() {
        return rowObjects;
    }

//    @Override
//    public Class getColumnClass(int column) {
//        int rowIndex = 0;
//        Object o = getValueAt(rowIndex, column);
//        if (column == 2) {
//            return ColoredString.class;
//        } else {
//            return Object.class;
//        }
//    }
    public void addObject(Object object) {
        rowObjects.add(object);
        fireTableDataChanged();
    }

    public Object getObjectAt(int rowIndex) {
        if (rowIndex < 0) {
            return null;
        }
        return this.rowObjects.get(rowIndex);
    }

    public void removeRow(int rowIndex) {
        if (rowObjects.size() > 0) {
            rowObjects.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void removeAll() {
        while (rowObjects.size() > 0) {
            rowObjects.remove(0);
            fireTableRowsDeleted(0, 0);
        }
    }

    public double getSumOfColumn(int columnIndex) {
        double summa = 0;

        for (int i = 0; i < this.getRowCount(); i++) {
            Object object = this.getValueAt(i, columnIndex);
            if (object == null) {
                continue;
            }

            double d = (Double) object;
            summa += d;
        }

        return summa;
    }

    public int indexOf(Object object) {
        return rowObjects.indexOf(object);
    }
}
