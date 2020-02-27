/*
 * <P>Title:      工具包 Util v1.0</P>
 * <P>Description:扩展功能表的列信息</P>
 * <P>Copyright:  Copyright (c) 2008</P>
 * <P>Company:    BoRoBoRoMe Co. Ltd.</P>
 * @author        BoRoBoRoMe
 * @version       1.0 2008-1-29
 */
package com.happy3w.utils.downlod.httpdownload.ref;

import java.io.Serializable;

/**
 * <P>Title:      工具包 Util v1.0</P>
 * <P>Description:扩展功能表的列信息</P>
 * <P>Copyright:  Copyright (c) 2008</P>
 * <P>Company:    BoRoBoRoMe Co. Ltd.</P>
 * @author        BoRoBoRoMe
 * @version       1.0 2008-1-29
 */
public class ExtTableColumn implements Serializable
{
    /**
     * 本列的标示
     */
    protected String name;
    
    /**
     * 本子段的类型
     */
    protected Class<?> type;
    
    /**
     * 构造函数
     */
    public ExtTableColumn()
    {
        //Nothing to initalized
    }

    /**
     * 获取本列的标示
     * @return 本列的标示
     */
    public String getName()
    {
        return name;
    }

    /**
     * 设置本列的标示
     * @param name 本列的标示
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * 获取类型
     * @return 类型
     */
    public Class<?> getType()
    {
        return type;
    }

    /**
     * 设置类型
     * @param type 类型
     */
    public void setType(final Class<?> type)
    {
        this.type = type;
    }
    
}
