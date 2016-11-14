package com.cisetech.androidunit.dao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.cisetech.androidunit.bean.AddressBean;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * author：yinqingy
 * date：2016-11-14 21:38
 * blog：http://blog.csdn.net/vv_bug
 * desc：
 */
@RunWith(AndroidJUnit4.class)
public class AddressDaoImpTest {
    @Test
    public void testDao() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        String name = appContext.getPackageName();
        assertEquals("com.cisetech.androidunit", appContext.getPackageName());
        AddressDaoImp dao=new AddressDaoImp(appContext);
        List<AddressBean> allProvince = dao.getAllProvince();
        for (AddressBean bean:allProvince) {
            Log.e("TAG","province--->"+bean.getName());
        }
    }
}