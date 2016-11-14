package com.cisetech.androidunit.dao;

import com.cisetech.androidunit.bean.AddressBean;

import java.util.List;

/**
 * author：yinqingy
 * date：2016-11-13 21:52
 * blog：http://blog.csdn.net/vv_bug
 * desc：地址数据访问层
 */

public interface AddressDao {
    /**
     * 关闭数据库
     */
    void closeDb();

    /**
     * 根据城市Code找到所有的地区
     * @param cityCode
     * @return
     * @throws Exception
     */
    List<AddressBean> getDistirctsByPcode(String cityCode) throws Exception;

    /**
     * 根据省份code找到所有的市
     * @param pCode
     * @return
     * @throws Exception
     */
    List<AddressBean> getCitiesByPcode(String pCode) throws Exception;

    /**
     * 获取所有的省份
     * @return
     * @throws Exception
     */
    List<AddressBean> getAllProvince() throws Exception;
}
