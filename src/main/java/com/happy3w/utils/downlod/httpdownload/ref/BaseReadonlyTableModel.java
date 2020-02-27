package com.happy3w.utils.downlod.httpdownload.ref;

import com.happy3w.utils.downlod.httpdownload.ref.BaseExtTableModel;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseReadonlyTableModel<T> extends BaseExtTableModel<T>
{
    /*
     * 保存用于显示数据列表(Object[])
     */
    private List<Object[]> lstRowData;
    
    /**
     * 构造函数
     */
    public BaseReadonlyTableModel(final String[] columns)
    {
        lstRowData = new ArrayList<Object[]>();
        this.setColumns(columns);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
        Object result = null;
        Object[] rows = null;
        if (rowIndex < lstRowData.size())
        {
            rows = lstRowData.get(rowIndex);
            if (rows != null && columnIndex < rows.length)
            {
                result = rows[columnIndex];
            }
        }
        return result;
    }
    
    @Override
    public final Object getValueAt(T data, int column)
    {
    	throw new UnsupportedOperationException("The method getValueAt in BaseReadonly class is not usable.");
    }
    /**
     * 将数据格式化成需要显示的列表
     * @param data 需要格式化的数据
     * @return 格式后需要显示的内容
     */
    public abstract Object[] formatItem(final T data);
    

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.boroborome.extui.model.BaseExtTableModel#clear()
     */
    @Override
    public void clear()
    {
        lstRowData.clear();
        super.clear();
    }

    @Override
    protected void justAddItem(final T data)
    {
        super.justAddItem(data);
        lstRowData.add(formatItem(data));
    }

    @Override
    protected void justInsertItem(int row, T data)
    {
        lstRowData.add(row, formatItem(data));
        super.justInsertItem(row, data);
    }

    @Override
    protected void justRemoveRow(int row)
    {
        lstRowData.remove(row);
        super.justRemoveRow(row);
    }

    @Override
    public void justSetItem(int row, T data)
    {
        lstRowData.set(row, formatItem(data));
        super.justSetItem(row, data);
    }

    /* (non-Javadoc)
     * @see com.boroborome.common.ui.model.BaseExtTableModel#sort(int, boolean)
     */
    @Override
    public void sort(final int column, final boolean isSmallToBig)
    {
        justSort(column, isSmallToBig);
        
        for (int i = 0, l = lstRow.size(); i < l ; i++)
        {
            lstRowData.set(i, formatItem(getItem(i)));
        }
        this.fireTableDataChanged();
    }
    
    
}
