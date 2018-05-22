package com.zcrpro.svm.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zcrpro.svm.R;
import com.zcrpro.svm.activity.MainActivity;
import com.zcrpro.svm.svm.SVM;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import libsvm.svm;

import static com.zcrpro.svm.svm.SVM.dataToFeaturesArr;
import static com.zcrpro.svm.svm.SVM.inputStreamToArray;
import static com.zcrpro.svm.util.Constant.actMapFromCode;
import static com.zcrpro.svm.util.Constant.dir;
import static com.zcrpro.svm.util.Constant.modelFileName;
import static com.zcrpro.svm.util.Constant.rangeFileName;
import static java.io.File.separator;


public class SensorFragment extends Fragment implements View.OnClickListener {

    private View view;
    private MainActivity mActivity;
    private SensorManager sensorManager;
    private MySensorListener sensorListener;
    private Button mBtnOpenSensor, mBtnCloseSensor;
    private TextView mTvSensor, mTvResult;

    public SensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sensor, container, false);
        findView();
        setListener();
        return view;
    }

    private void setListener() {
        mBtnOpenSensor.setOnClickListener(this);
        mBtnCloseSensor.setOnClickListener(this);
    }

    private void findView() {
        mBtnOpenSensor = (Button) view.findViewById(R.id.btn_open_sensor);
        mBtnCloseSensor = (Button) view.findViewById(R.id.btn_close_sensor);
        mTvSensor = (TextView) view.findViewById(R.id.tv_sensor_data);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (MainActivity) getActivity();
        sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new MySensorListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_sensor:
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        1000 * 1000 / 32);
                loadModelAndRange();
                mBtnCloseSensor.setVisibility(View.VISIBLE);
                mBtnOpenSensor.setVisibility(View.GONE);
                break;
            case R.id.btn_close_sensor:
                sensorManager.unregisterListener(sensorListener);
                mBtnCloseSensor.setVisibility(View.GONE);
                mBtnOpenSensor.setVisibility(View.VISIBLE);
                break;
        }
    }

    SVM mSvm;

    /**
     * 加载model和range
     */
    private void loadModelAndRange() {
        try {
            mSvm = new SVM(svm.svm_load_model(
                    new BufferedReader(new InputStreamReader(new FileInputStream(dir + separator + modelFileName)))),
                    inputStreamToArray(new FileInputStream(dir + separator + rangeFileName)));
        } catch (IOException e) {
            e.printStackTrace();
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
             * 采集128个数据,归一化,预测
             */
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double a = Math.sqrt((double) (x * x + y * y + z * z));
                mTvSensor.setText("加速度:" + a);
                if (currentIndex >= num) {
                    String[] data = dataToFeaturesArr(accArr.clone());
                    double code = mSvm.predictUnscaled(data, false);
                    double act = (int) code / 100;
                    String strAct = actMapFromCode.get(act);
                    mTvResult.setText("action：" + strAct);
                    currentIndex = 0;
                }
                accArr[currentIndex++] = a;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
