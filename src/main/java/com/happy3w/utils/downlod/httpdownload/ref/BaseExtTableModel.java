package com.happy3w.utils.downlod.httpdownload.ref;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class BaseExtTableModel<T> extends AbstractTableModel implements Iterable<T>
{
    /*
     * 正序
     */
    private static final int ASE = 1;
    
    /*
     * 反序
     */
    private static final int DESA = -1;
    
    //排序方向
    private int orderDirection = ASE;
    
    /*
     * 表头列表（ExtTableColumn）
     */
    private ExtTableColumn[] columns;
    
    /**
     * 每行记录对应对象的列表（RowPair）
     */
    protected List<RowPair> lstRow;
   
    /**
     * 构造函数
     */
    public BaseExtTableModel()
    {
        lstRow = new ArrayList<RowPair>();
        columns = new ExtTableColumn[0];
    }

    /**
     * 设置表列
     * @param columns 表使用的列
     */
    public void setColumns(final ExtTableColumn[] columns)
    {
        if (columns == null)
        {
            throw new IllegalArgumentException("setColumns not accept null param."); //$NON-NLS-1$
        }
        this.columns = columns;
        this.fireTableStructureChanged();
    }
    
    /**
     * 设置表列
     * @param columns 表使用的列
     */
    public void setColumns(final String[] columns)
    {
        if (columns == null)
        {
            throw new IllegalArgumentException("setColumns not accept null param."); //$NON-NLS-1$
        }
        
        ExtTableColumn[] cs = new ExtTableColumn[columns.length];
        for (int i = 0; i < columns.length; i++)
        {
            cs[i] = new ExtTableColumn();
            cs[i].setName(columns[i]);
            cs[i].setType(Object.class);
        }
        setColumns(cs);
    }
    
    /**
     * 获取表使用的列
     * @return 表使用的列
     */
    public ExtTableColumn[] getColumns()
    {
        return columns;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return columns.length;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columns[columnIndex].getType();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column)
    {
        return columns[column].getName();
    }

    /**
     * 清除表中内容
     */
    public void clear() {
        if (lstRow.size() > 0)
        {
            lstRow.clear();
            this.fireTableDataChanged();
        }
    }
    
    /**
     * 向表中添加记录
     * @param data 需要添加的数据
     */
    public void addItem(final T data)
    {
        justAddItem(data);
        this.fireTableDataChanged();
    }
    
    /**
     * 将表格中信息收集到列表中
     * @param lst 用于保存表格中信息的列表
     */
    public void collectData(final List<T> lst)
    {
        lst.clear();
        for (T value : this)
        {
            lst.add(value);
        }
    }
    
    /**
     * 将列表中信息显示在表格中
     * @param lst
     */
    public void showData(final List<T> lst)
    {
        this.showData(lst.iterator());
    }
    
    /**
     * 将迭带器中内容显示在表格中
     * @param it
     */
    public void showData(Iterator<T> it)
    {
        clear();
        if (it != null)
        {
            while (it.hasNext())
            {
                T value = it.next();
                if (value != null)
                {
                    justAddItem(value);
                }
            }
        }
        this.fireTableDataChanged();
    }
    
    protected void justAddItem(final T data)
    {
        lstRow.add(new RowPair(null, data));
    }
    
    /**
     * <P>Title:      [产品名称和版本号]</P>
     * <P>Description:表格的行迭带器</P>
     * <P>Copyright:  Copyright (c) 2008</P>
     * <P>Company:    BoRoBoRoMe Co. Ltd.</P>
     * @author        BoRoBoRoMe
     * @version       1.0 2010-2-16
     */
    private class RowIterator implements Iterator<T>
    {
        private Iterator<RowPair> it;
        public RowIterator(Iterator<RowPair> iterator)
        {
            it = iterator;
        }
        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public T next()
        {
            return it.next().data;
        }

        @Override
        public void remove()
        {
            it.remove();
        }
    }

    /**
     * 修改指定行的对象
     * @param row 行号
     * @param data 新对象
     */
    public void setItem(final int row, final T data)
    {
        justSetItem(row, data);
        this.fireTableDataChanged();
    }

    public void justSetItem(final int row, final T data)
    {
        lstRow.set(row, new RowPair(null, data));
    }
    
    /**
     * 向表格中插入一行记录
     * @param row 行索引
     * @param data 需要插入的数据
     */
    public void insertItem(final int row, final T data)
    {
        justInsertItem(row, data);
        this.fireTableDataChanged();
    }
    
    protected void justInsertItem(final int row, final T data)
    {
        lstRow.add(row, new RowPair(null, data));
    }
    
    /**
     * 获取指定行的信息
     * @param row 行号
     * @return 指定行的数据
     */
    public T getItem(final int row)
    {
        return lstRow.get(row).data;
    }
    /* (non-Javadoc)
     * @see javax.swing.table.DefaultTableModel#getRowCount()
     */
    @Override
    public int getRowCount()
    {
        int count = 0;
        if (lstRow != null)
        {
            count = lstRow.size();
        }
        return count;
    }


    /**
     * 删除指定行数据
     * @param row 行索引
     */
    public void removeRow(final int row)
    {
        justRemoveRow(row);
        fireTableRowsDeleted(row, row);
    }
    
    protected void justRemoveRow(final int row)
    {
        lstRow.remove(row);
    }
    
//    /* (non-Javadoc)
//     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
//     */
//    @Override
//    public boolean isCellEditable(final int rowIndex, final int columnIndex)
//    {
//        return true;
//    }
    
    /**
     * 排序，仅仅是排序，不做任何其他动作
     * @param column 需要排序的列号
     * @param isSmallToBig 排序是否为正序
     */
    @SuppressWarnings("unchecked")
    protected void justSort(final int column, final boolean isSmallToBig)
    {
//      准备排序
        for (int i = 0, l = lstRow.size(); i < l; i++)
        {
            RowPair rp = lstRow.get(i);
            Object v = getValueAt(rp.getData(), column);
            if (v instanceof Comparable)
            {
                rp.key = (Comparable) v;
            }
            else
            {
                rp.key = null;
            }
        }
        
        //设置排序方向，这个变量（OrderDirection）会被RowPair对象的比较方法使用
        if (isSmallToBig)
        {
            orderDirection = ASE;
        }
        else
        {
            orderDirection = DESA;
        }
        
//      排序
        lstRow.sort(new Comparator<RowPair>() {
            @Override
            public int compare(RowPair o1, RowPair o2) {
                return o1.compareTo(o2);
            }
        });
//        RowPair[] lst = lstRow.toArray(new RowPair[lstRow.size()]);
//        Arrays.sort(lst);
//
//        //填充数据
//        lstRow.clear();
//        for (int i = 0; i < lst.length; i ++)
//        {
//            lstRow.add(lst[i]);
//        }
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        RowPair rp = lstRow.get(rowIndex);
        return getValueAt(rp.getData(), columnIndex);
    }

    public abstract Object getValueAt(T data, int column);

    /**
     * 对数据中内容进行排序
     * @param column 列号
     * @param isSmallToBig 是否由小到大
     */
    public void sort(final int column, final boolean isSmallToBig)
    {
        justSort(column, isSmallToBig);
        
        //通知监听
        this.fireTableDataChanged();
    }

    /**
     * <P>Title:      工具包 Util v1.0</P>
     * <P>Description:排序时用于保存列和对应健值之间的关系</P>
     * <P>Copyright:  Copyright (c) 2008</P>
     * <P>Company:    BoRoBoRoMe Co. Ltd.</P>
     * @author        BoRoBoRoMe
     * @version       1.0 2008-1-30
     */
    protected class RowPair implements Comparable<RowPair>
    {
        private Comparable<Object> key;
        private T data;
        
        /**
         * 构造函数
         */
        public RowPair()
        {
            super();
        }
        /**
         * 构造函数
         * @param key 健值
         * @param data 数据
         */
        public RowPair(final Comparable<Object> key, final T data)
        {
            this.key = key;
            this.data = data;
        }
        /**
         * 获取数据
         * @return 数据
         */
        public T getData()
        {
            return data;
        }
        /**
         * 设置数据
         * @param data 数据
         */
        public void setData(final T data)
        {
            this.data = data;
        }
        /**
         * 获取健值
         * @return 健值
         */
        public Comparable<?> getKey()
        {
            return key;
        }
        /**
         * 设置健值
         * @param key 健值
         */
        public void setKey(final Comparable<Object> key)
        {
            this.key = key;
        }
        
        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(RowPair rp)
        {
            int result = 0;
            if (rp.key != key)
            {
                if (key != null)
                {
                    result = key.compareTo(rp.key);
                }
                else
                {
                    result = 1;
                }
            }
            
            result *= orderDirection;
            
            return result;
        }
    }

    @Override
    public Iterator<T> iterator()
    {
        return new RowIterator(this.lstRow.iterator());
    }

    /**
     * 将选中的几条记录做成迭代器
     * @param selectedRows
     * @return
     */
    public Iterator<T> getItemIterator(int[] selectedRows)
    {
        List<T> lst = new ArrayList<T>();
        for (int i = 0; i < selectedRows.length; i++)
        {
            lst.add(this.getItem(i));
        }
        return lst.iterator();
    }
}
