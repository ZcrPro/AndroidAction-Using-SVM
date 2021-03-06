package com.zcrpro.svm.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zcrpro.svm.R;
import com.zcrpro.svm.fragment.CollectionFragment;
import com.zcrpro.svm.fragment.PredictFragment;
import com.zcrpro.svm.fragment.SensorFragment;
import com.zcrpro.svm.svm.SVM;
import com.zcrpro.svm.view.CustomViewPager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import libsvm.svm;

import static com.zcrpro.svm.svm.SVM.inputStreamToArray;
import static com.zcrpro.svm.util.Constant.dir;
import static com.zcrpro.svm.util.Constant.modelFileName;
import static com.zcrpro.svm.util.Constant.rangeFileName;
import static com.zcrpro.svm.util.Constant.train;
import static com.zcrpro.svm.util.Constant.trainFileName;
import static com.zcrpro.svm.util.PermissionUtil.requestWriteFilePermission;
import static java.io.File.separator;

public class MainActivity extends AppCompatActivity {

    private CustomViewPager pager = null;
    private PagerTabStrip tabStrip = null;
    public String TAG = "tag";
    private List<Fragment> pagers;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (CustomViewPager) this.findViewById(R.id.pg_main);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.tab_strip);
        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        tabStrip.setBackgroundColor(0);
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.colorPrimary));
        tabStrip.setTextSpacing(200);

        //页签项
        titles = new ArrayList<>();
        titles.add("采集数据");
        titles.add("训练模型");
        titles.add("实时数据");

        pagers = new ArrayList<>();
        pagers.add(new CollectionFragment());
        pagers.add(new PredictFragment());
        pagers.add(new SensorFragment());

        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.d(TAG, "--------changed:" + arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.d(TAG, "-------scrolled arg0:" + arg0);
                Log.d(TAG, "-------scrolled arg1:" + arg1);
                Log.d(TAG, "-------scrolled arg2:" + arg2);
            }

            @Override
            public void onPageSelected(int arg0) {
                Log.d(TAG, "------selected:" + arg0);
            }
        });

        pager.setScanScroll(true);

        requestWriteFilePermission(this);
        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.pg_main, new CollectionFragment())
                .commit();

        crateDataDir();
    }

    /**
     * 创建数据目录
     */
    private void crateDataDir() {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        File trainFile = new File(dir + separator + train);
        if (!trainFile.exists()) {
            trainFile.mkdirs();
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return pagers.get(position);
        }

        @Override
        public int getCount() {
            return pagers.size();
        }
    }

}
