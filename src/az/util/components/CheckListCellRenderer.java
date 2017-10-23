/*
 * File: CheckListCellRenderer.java
 * Auth: Creator
 * Date: 06 februarie 2008, 21:06
 ********************************************
 Copyright (C) 2007  Bogdan
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 ********************************************
 */
package az.util.components;

import java.awt.Component;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author Creator
 */
public class CheckListCellRenderer extends JCheckBox implements ListCellRenderer {

    protected static Border m_noFocusBorder
            = new EmptyBorder(1, 1, 1, 1);

    /**
     * Creates a new instance of CheckListCellRenderer
     */
    public CheckListCellRenderer() {
        setOpaque(true);
        setBorder(m_noFocusBorder);
    }

    public Component getListCellRendererComponent(JList list,
            Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (value != null) {
            setText(value.toString());
        }

        setBackground(isSelected ? list.getSelectionBackground()
                : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground()
                : list.getForeground());

        InstallData data = (InstallData) value;
        setSelected(data.isSelected());

        setFont(list.getFont());
        setBorder((cellHasFocus)
                ? UIManager.getBorder("List.focusCellHighlightBorder")
                : m_noFocusBorder);

        return this;
    }
}
