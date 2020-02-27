package com.happy3w.utils.downlod.httpdownload.ref;

import com.happy3w.utils.downlod.httpdownload.ref.BaseExtTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ExtTable extends JScrollPane// JTable
{
    protected JTable innerTable = new JTable();
    
    /*
     * 表头菜单
     */
    private JPopupMenu headMenu;
    
    /*
     * 排序某一列需要点击表头的次数
     */
    private int sortClickCount;
    
    /*
     * 表体的菜单
     */
    private JPopupMenu tblMenu;
    private TblMouseListener tblMouseListener;
    
    /**
     * 构造函数
     */
    public ExtTable()
    {
        super();
        this.setViewportView(innerTable);
        
        sortClickCount = 2;
        innerTable.getTableHeader().addMouseListener(new HeadMouseListener());
    }

    /**
     * 获取内部使用的表格
     * @return 内部使用的表格
     */
    public JTable getInnerTable()
    {
        return innerTable;
    }

    /**
     * @return
     * @see JTable#getModel()
     */
    public TableModel getModel()
    {
        return innerTable.getModel();
    }

    /**
     * @param viewColumnIndex
     * @return
     * @see JTable#convertColumnIndexToModel(int)
     */
    public int convertColumnIndexToModel(int viewColumnIndex)
    {
        return innerTable.convertColumnIndexToModel(viewColumnIndex);
    }

    /**
     * @param modelColumnIndex
     * @return
     * @see JTable#convertColumnIndexToView(int)
     */
    public int convertColumnIndexToView(int modelColumnIndex)
    {
        return innerTable.convertColumnIndexToView(modelColumnIndex);
    }

    /**
     * @param dataModel
     * @see JTable#setModel(TableModel)
     */
    public void setModel(TableModel dataModel)
    {
        innerTable.setModel(dataModel);
    }

    /**
     * @param selectionMode
     * @see JTable#setSelectionMode(int)
     */
    public void setSelectionMode(int selectionMode)
    {
        innerTable.setSelectionMode(selectionMode);
    }

    /**
     * @return
     * @see JTable#getRowCount()
     */
    public int getRowCount()
    {
        return innerTable.getRowCount();
    }

    /**
     * @return
     * @see JTable#getSelectedRow()
     */
    public int getSelectedRow()
    {
        return innerTable.getSelectedRow();
    }

    /**
     * @return
     * @see JTable#getSelectedRows()
     */
    public int[] getSelectedRows()
    {
        return innerTable.getSelectedRows();
    }

    /**
     * @return
     * @see JTable#getSelectionModel()
     */
    public ListSelectionModel getSelectionModel()
    {
        return innerTable.getSelectionModel();
    }

    /**
     * 获取tblMenu
     * @return tblMenu
     */
    public JPopupMenu getTableMenu()
    {
        return tblMenu;
    }

    /**
     * 设置tblMenu
     * @param tblMenu tblMenu
     */
    public void setTableMenu(final JPopupMenu tblMenu)
    {
        this.tblMenu = tblMenu;
        if (tblMenu != null)
        {
            if (tblMouseListener == null)
            {
                tblMouseListener = new TblMouseListener();
            }
            this.addMouseListener(tblMouseListener);
            innerTable.addMouseListener(tblMouseListener);
        }
        else
        {
            this.removeMouseListener(tblMouseListener);
            innerTable.removeMouseListener(tblMouseListener);
            tblMouseListener = null;
        }
    }

    /**
     * 获取表头菜单
     * @return 表头菜单
     */
    public JPopupMenu getHeadMenu()
    {
        return headMenu;
    }

    /**
     * 设置表头菜单
     * @param headMenu 表头菜单
     */
    public void setHeadMenu(final JPopupMenu headMenu)
    {
        this.headMenu = headMenu;
    }

    /**
     * 获取排序某一列需要点击表头的次数
     * @return 排序某一列需要点击表头的次数
     */
    public int getSortClickCount()
    {
        return sortClickCount;
    }

    /**
     * 设置排序某一列需要点击表头的次数
     * @param sortClickCount 排序某一列需要点击表头的次数
     */
    public void setSortClickCount(final int sortClickCount)
    {
        this.sortClickCount = sortClickCount;
    }
    
    /**
     * 对给定列排序
     * @param column 需要排序的列
     * @param isSmallToBig 排序方向
     */
    @SuppressWarnings("unchecked")
	public void sort(final int column, final boolean isSmallToBig)
    {
        TableModel model = innerTable.getModel();
        if (model instanceof BaseExtTableModel)
        {
            BaseExtTableModel extModel = (BaseExtTableModel) model;
            extModel.sort(innerTable.convertColumnIndexToModel(column), isSmallToBig);
        }
    }
    
    /**
     * <P>Title:      工具包 Util v1.0</P>
     * <P>Description:表头鼠标动作，包括点击表头排序，表头菜单</P>
     * <P>Copyright:  Copyright (c) 2008</P>
     * <P>Company:    BoRoBoRoMe Co. Ltd.</P>
     * @author        BoRoBoRoMe
     * @version       1.0 2008-1-30
     */
    private class HeadMouseListener extends MouseAdapter
    {
        private int curColumn = -1;
        private boolean isSmallToBig = true;
        
        /* (non-Javadoc)
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent e)
        {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == sortClickCount)
            {
                int column = innerTable.columnAtPoint(e.getPoint());
                if (column != curColumn)
                {
                    curColumn = column;
                    isSmallToBig = false;
                }
                
                isSmallToBig = !isSmallToBig;
                sort(column, isSmallToBig);
            }
            else if (headMenu != null && SwingUtilities.isRightMouseButton(e))
            {
                headMenu.setVisible(false);
                headMenu.show(innerTable.getTableHeader(), e.getX(), e.getY());
            }
        }
    }
    private class TblMouseListener extends MouseAdapter
    {
        /* (non-Javadoc)
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
    	@Override
        public void mouseClicked(final MouseEvent e)
        {
            if (tblMenu != null && SwingUtilities.isRightMouseButton(e))
            {
                tblMenu.setVisible(false);
                tblMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    /**
     * 调整滚动条让指定的行可见
     * @param row 需要显示的行号，如果小于0或者超出范围则忽略
     */
    public void scrollRowToVisible(int row)
    {
        if (row < 0 || row >= innerTable.getModel().getRowCount())
        {
            return;
        }
        
        Rectangle bounds = innerTable.getBounds();
        int height = innerTable.getRowHeight();
        int width = bounds.width;
        int x = bounds.x;
        int y = height * row;
        
        innerTable.scrollRectToVisible(new Rectangle(x, y, width, height));
    }
}
