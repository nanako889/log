package com.qbw.log;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qbw.l.L;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L log =new L();
        log.setEnabled(true);
        log.v("v");
        log.logV("logv");
        log.jsonV("{\n" + "                \"id\": 5008,\n" + "                \"name\": \"vivo 0%Z5x 6GB+128GB 极光色 极点屏手机  5000mAh大电池 三摄拍照手机 移动联通电信全网通4G手机\",\n" + "                \"price\": \"1677.6\",\n" + "                \"productImages\": \"http://img11.360buyimg.com/n5/s180x180_jfs/t1/42583/20/7497/163984/5d11ec8bEf59466f6/e1ee156f444b6aef.jpg\",\n" + "                \"productCategoryId\": 734,\n" + "                \"rebateRadio\": \"0%\",\n" + "                \"storeId\": 353,\n" + "                \"storeName\": \"手机旗舰店\",\n" + "                \"storeType\": \"普通\",\n" + "                \"usdt\": \"239.28\"\n" + "            }");
        Map<String,Object> map=new HashMap<>();
        map.put("aaa","bb");
        log.urlV("http://www.baidu.com", map);
        try {
            int i = 1/0;
        }catch (Exception e){
            log.w(e);
        }
    }
}
