/*
 * File: CheckListener.java
 * Auth: Creator
 * Date: 06 februarie 2008, 21:08
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

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Creator
 */
public class CheckListener implements MouseListener, KeyListener {

    protected CheckBoxList m_parent;
    protected JList m_list;

    /**
     * Creates a new instance of CheckListener
     */
    public CheckListener(CheckBoxList parent) {
        m_parent = parent;
        m_list = parent.jList1;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getX() < 20) {
            doCheck();
        } else if (e.getClickCount() == 2) {
            doCheck();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == ' ') {
            doCheck();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    protected void doCheck() {
        int index = m_list.getSelectedIndex();
        if (index < 0) {
            return;
        }
        InstallData data = (InstallData) m_list.getModel().
                getElementAt(index);
        data.invertSelected();
        m_list.repaint();
        //m_parent.recalcTotal();
    }
}
