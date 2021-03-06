package com.zcrpro.svm.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.zcrpro.svm.R;
import com.zcrpro.svm.fragment.base.BaseFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.zcrpro.svm.svm.SVM.dataToFeaturesArr;
import static com.zcrpro.svm.util.Constant.dir;

public class CollectionFragment extends BaseFragment {

    private View view;                                          // 页面根视图
    private Spinner mSpAction;                                  // 下拉选则器
    private Button mBtnStartCollection, mBtnStopCollection;     // 开始采集,结束采集的按钮
    private TextView mTvResult, mTvNum;                         // 显示结果
    private SeekBar mSensorHz;
    private int trainNum;

    private double mAtionInt = 1;                               // action 的label
    private int mSensorHzInt;                                   // sensor采样频率

    SensorManager sensorManager;                        // 传感器管理器
    MySensorListener sensorListener;                    // 传感器监听类,当传感器数据变化时会调用该类的onSensorChanged()方法

    FileOutputStream outputStream;                      // 输出流,用来保存结果

    public CollectionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_collection, container, false);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new MySensorListener();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 创建训练数据文件
     */
    private void crateTrainFile() {
        try {
            String fileNume = "train";
            fileNume += ".txt";
            outputStream = new FileOutputStream(dir + File.separator + fileNume);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void findView() {
        mSpAction = (Spinner) view.findViewById(R.id.sp_action);
        mBtnStartCollection = (Button) view.findViewById(R.id.btn_start_collection);
        mBtnStopCollection = (Button) view.findViewById(R.id.btn_stop_collection);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvNum = (TextView) view.findViewById(R.id.tv_num);
        mSensorHz = (SeekBar) view.findViewById(R.id.sb_hz);
    }

    @Override
    protected void setOnClickListener() {
        mBtnStartCollection.setOnClickListener(this);
        mBtnStopCollection.setOnClickListener(this);
    }

    @Override
    protected void setOnItemSelectListener() {
        mSpAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAtionInt = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSensorHz.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSensorHzInt = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_collection:
                trainNum = 0;
                mTvNum.setText("0");
                mBtnStartCollection.setVisibility(View.GONE);
                mBtnStopCollection.setVisibility(View.VISIBLE);
                crateTrainFile();
                openSensor();
                break;
            case R.id.btn_stop_collection:
                mBtnStartCollection.setVisibility(View.VISIBLE);
                mBtnStopCollection.setVisibility(View.GONE);
                colseSensor();
                break;
        }
    }

    /**
     * 关闭传感器
     */
    private void colseSensor() {
        sensorManager.unregisterListener(sensorListener);
    }

    /**
     * 根据采样频率打开传感器
     */
    private void openSensor() {
        switch (mSensorHzInt) {
            case 0:
                /*sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_FASTEST);*/
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        1000 * 1000 / 32);
                break;
            case 1:
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_GAME);
                break;
            case 2:
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_UI);
                break;
            case 3:
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
        }
    }

    public class MySensorListener implements SensorEventListener {
        int num = 128;
        public double[] accArr = new double[num];
        public int currentIndex = 0;

        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            /**
             * 采集128个数据,转换成特征值
             */
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double a = Math.sqrt((double) (x * x + y * y + z * z));
                mTvResult.setText("lable:" + (mAtionInt * 100) + "加速度:" + a);
                mTvNum.setText("采集样本数量:" + trainNum);
                if (currentIndex >= num) {
                    String[] data = dataToFeaturesArr(accArr.clone());
                    saveDataToFile(data);
                    currentIndex = 0;
                    trainNum++;
                }
                accArr[currentIndex++] = a;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    /**
     * 把数据保存到文件中
     *
     * @param data
     */
    private void saveDataToFile(String[] data) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(mAtionInt * 100);
        for (String s : data) {
            stringBuffer.append(" " + s);
        }
        stringBuffer.append("\n");
        try {
            outputStream.write(stringBuffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        colseSensor();
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
    }
}

