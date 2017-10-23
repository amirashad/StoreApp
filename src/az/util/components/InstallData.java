/*
 * File: InstallData.java
 * Auth: Creator
 * Date: 06 februarie 2008, 21:09
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

import az.store.product.Product;

/**
 *
 * @author Creator
 */
public class InstallData {

//    public String m_name;
    protected boolean m_selected;
    Product m_product;

    /**
     * Creates a new instance of InstallData
     */
    public InstallData(Product product, Boolean value) {
        m_product = product;
        m_selected = value;
    }

    public void setSelected(boolean selected) {
        m_selected = selected;
    }

    public void invertSelected() {
        m_selected = !m_selected;
    }

    public boolean isSelected() {
        return m_selected;
    }

    public String toString() {
        return m_product.getName() + " (" + m_product.getTypeName() + ")";
    }

    public Product getProduct() {
        return m_product;
    }

    public void setProduct(Product m_product) {
        this.m_product = m_product;
    }

}
