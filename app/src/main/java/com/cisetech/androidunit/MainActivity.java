package com.cisetech.androidunit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cisetech.androidunit.bean.AddressBean;
import com.cisetech.androidunit.dao.AddressDao;
import com.cisetech.androidunit.dao.AddressDaoImp;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final  int FILL_PROVINCE=0x01 ;
    private static final  int FILL_CITY =0x02 ;
    private static final  int FILL_DISTRICT =0x03 ;
    private Spinner sp_province,sp_city,sp_district;
    private static ExecutorService mExecutorService;
    static {
        mExecutorService= Executors.newFixedThreadPool(1);
    }
    private AddressDao addressDao;
    private List<AddressBean>provinces;
    private List<AddressBean>cities;
    private List<AddressBean>districts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp_province= (Spinner) findViewById(R.id.sp_province);
        sp_city= (Spinner) findViewById(R.id.sp_city);
        sp_district= (Spinner) findViewById(R.id.sp_district);
        sp_province.setOnItemSelectedListener(this);
        sp_city.setOnItemSelectedListener(this);
        addressDao=new AddressDaoImp(this);
        getAllProvinces();
    }

    /**
     * 获取所有的省
     */
    private void getAllProvinces() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    provinces=addressDao.getAllProvince();
                    if(provinces!=null&&provinces.size()>0){
                        handler.sendEmptyMessage(FILL_PROVINCE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 根据省份获取城市
     * @param code
     */
    private void findCitiesByPcode(final String code) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cities=addressDao.getCitiesByPcode(code);
                    if(cities!=null&&cities.size()>0){
                        handler.sendEmptyMessage(FILL_CITY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }/**
     * 根据城市获取地区
     * @param code
     */
    private void findDistrictsByCcode(final String code) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    districts=addressDao.getDistirctsByPcode(code);
                    if(districts!=null&&districts.size()>0){
                        handler.sendEmptyMessage(FILL_DISTRICT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.sp_province){
            findCitiesByPcode(provinces.get(position).getCode());
        }else if(parent.getId()==R.id.sp_city){
            findDistrictsByCcode(cities.get(position).getCode());
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    final MyHandler handler=new MyHandler(this);
    static class MyHandler extends Handler{
        private final WeakReference<MainActivity> mActivity;
        public MyHandler(MainActivity activity) {
            mActivity=new WeakReference<MainActivity>(activity);
        }
        public void handleMessage(android.os.Message msg) {
            MainActivity activity=mActivity.get();
            if(activity!=null){
                if(FILL_PROVINCE==msg.what){
                    activity.fillPorince();
                }else if(FILL_CITY==msg.what){
                    activity.fillCities();
                }else if(FILL_DISTRICT==msg.what){
                    activity.fillDistricts();
                }
            }
        }
    }

    private void fillDistricts() {
        sp_district.setAdapter(new MyAdapter(this,districts));
    }

    private void fillCities() {
        sp_city.setAdapter(new MyAdapter(this,cities));
    }

    private void fillPorince() {
        sp_province.setAdapter(new MyAdapter(this,provinces));
    }
    private class MyAdapter extends  ArrayAdapter<AddressBean>{
        public MyAdapter(Context context, List<AddressBean>objects) {
            super(context, android.R.layout.simple_spinner_item,objects);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(getApplicationContext(),android.R.layout.simple_spinner_item,null);
            }
            TextView text= (TextView) convertView.findViewById(android.R.id.text1);
            text.setText(getItem(position).getName());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(getApplicationContext(),android.R.layout.simple_spinner_item,null);
            }
            TextView text= (TextView) convertView.findViewById(android.R.id.text1);
            text.setText(getItem(position).getName());
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeMessages(FILL_PROVINCE);
            handler.removeMessages(FILL_CITY);
            handler.removeMessages(FILL_DISTRICT);
        }
    }
}
