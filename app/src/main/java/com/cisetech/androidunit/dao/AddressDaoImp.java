package com.cisetech.androidunit.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.cisetech.androidunit.R;
import com.cisetech.androidunit.bean.AddressBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author：yinqingy
 * date：2016-11-13 21:54
 * blog：http://blog.csdn.net/vv_bug
 * desc：地址数据访问层实现层
 */

public class AddressDaoImp implements AddressDao {
    public static final String tag = "AddressDao";
    public static final String CITY_DB_NAME = "CITY_DB";
    private SQLiteDatabase db;
    public AddressDaoImp(Context context) {
        File db_file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断sd卡是否有效
            db_file = new File(Environment.getExternalStorageDirectory(),CITY_DB_NAME);
            Log.i(tag, "拿到内存卡中的city——db");
        }
        if (db_file == null) {// 内存卡不存在的情况（放在手机缓存中）
            Log.i(tag, "拿到手机内存中的city——db");
            db_file = new File(context.getFilesDir(),CITY_DB_NAME);
        }
        if (db_file != null && db_file.length() > 0) {
            db = SQLiteDatabase.openDatabase(db_file.getAbsolutePath(), null,
                    SQLiteDatabase.OPEN_READONLY);
        }else{
            try {
                InputStream is = context.getResources().openRawResource(R.raw.city);
                FileOutputStream fos = new FileOutputStream(db_file);
                byte[] buffer = new byte[512];
                int count = 0;
                while ((count =is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }
                fos.close();
                is.close();
                Log.i(tag, "db拷贝完成");
                db = SQLiteDatabase.openDatabase(db_file.getAbsolutePath(), null,
                        SQLiteDatabase.OPEN_READONLY);
            } catch (Exception e) {
                db_file.delete();//异常就删除db文件
                e.printStackTrace();
                Log.i(tag, "db拷贝异常");
            }
        }
        if (db == null) {
            Log.i(tag, "获取db异常~~~~");
        }
    }

    @Override
    public void closeDb() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    /**
     * 根据城市code获取所有的district
     * @return
     */
    public List<AddressBean> getDistirctsByPcode(String cityCode) {
        List<AddressBean> cities = new ArrayList<AddressBean>();
        try {
            Cursor cursor = db.rawQuery("select * from district where pcode=?",
                    new String[] { cityCode });
            while (cursor.moveToNext()) {
                AddressBean item = new AddressBean();
                item.setCode(cursor.getString(cursor.getColumnIndex("code")));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                if(!TextUtils.isEmpty(name)){
                    name=name.trim();
                }
                item.setName(name);
                cities.add(item);
            }
            cursor.close();
        } catch (Exception e) {
            return cities;
        }
        return cities;
    }
    /**
     * 根据省获取city
     *
     * @return
     */
    @Override
    public List<AddressBean> getCitiesByPcode(String pCode) throws Exception {
        List<AddressBean> cities = new ArrayList<AddressBean>();
        try {
            Cursor cursor = db.rawQuery("select * from city where pcode=?",
                    new String[] { pCode });
            while (cursor.moveToNext()) {
                AddressBean item = new AddressBean();
                item.setCode(cursor.getString(cursor.getColumnIndex("code")));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                if(!TextUtils.isEmpty(name)){
                    name=name.trim();
                }
                item.setName(name);
                cities.add(item);
            }
            cursor.close();
        } catch (Exception e) {
            return cities;
        }
        return cities;
    }

    /**
     * 获取所有的province
     * @return
     */
    @Override
    public List<AddressBean> getAllProvince() {
        List<AddressBean> provinces = new ArrayList<AddressBean>();
        try {
            Cursor cursor = db.rawQuery("select * from province", null);
            while (cursor.moveToNext()) {
                AddressBean item = new AddressBean();
                item.setCode(cursor.getString(cursor.getColumnIndex("code")));
                byte bytes[] = cursor.getBlob(2);
                String name = new String(bytes, "gbk");
                if(!TextUtils.isEmpty(name)){
                    name=name.trim();
                }
                item.setName(name);
                provinces.add(item);
            }
            cursor.close();
        } catch (Exception e) {
            return provinces;
        }
        return provinces;
    }
}
