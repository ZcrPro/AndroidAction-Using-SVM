package com.zcrpro.svm.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcrpro.svm.R;
import com.zcrpro.svm.activity.MainActivity;
import com.zcrpro.svm.svm.SVM;
import com.zcrpro.svm.svmlib.svm_predict;
import com.zcrpro.svm.svmlib.svm_scale;
import com.zcrpro.svm.svmlib.svm_train;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static com.zcrpro.svm.util.Constant.SEPERATOR_SPACE;
import static com.zcrpro.svm.util.Constant.dir;
import static com.zcrpro.svm.util.Constant.modelFileName;
import static com.zcrpro.svm.util.Constant.modelInfo;
import static com.zcrpro.svm.util.Constant.prdictInfo;
import static com.zcrpro.svm.util.Constant.range;
import static com.zcrpro.svm.util.Constant.resultFileName;
import static com.zcrpro.svm.util.Constant.scaleFileName;
import static com.zcrpro.svm.util.Constant.train;
import static com.zcrpro.svm.util.Constant.trainFileName;
import static java.io.File.separator;

public class PredictFragment extends Fragment implements View.OnClickListener {

    private View view;                                  // 整个页面视图
    private ListView mFeatureList;                      // 显示特征的listview
    private FeatureListAdapter mFeatureListAdapter;     // listView的适配器
    private Button mBtnTakeASimpleData, mBtnTrainData;                 // 取一条样本数据的按钮

    private TextView mTvResult, mTvTrainResult;                         // 显示样本数据用model测试的结果
    private  String[] mFeatureData;
    private BufferedReader mBufferedReader;


    public PredictFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_predict, container, false);
        init();
        return view;
    }

    /**
     * 做一些初始化的操作
     */
    private void init() {
        findView();
        setClickListener();
        openStream();
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mFeatureData = new String[]{};
        mFeatureListAdapter = new FeatureListAdapter();
        mFeatureList.setAdapter(mFeatureListAdapter);
    }

    /**
     * 打开数据流
     */
    private void openStream() {
        try {
            mBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dir + separator + trainFileName+".txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置点击事件
     */
    private void setClickListener() {
        mBtnTakeASimpleData.setOnClickListener(this);
        mBtnTrainData.setOnClickListener(this);
    }

    private void findView() {
        mFeatureList = (ListView) view.findViewById(R.id.feature_list);
        mBtnTakeASimpleData = (Button) view.findViewById(R.id.btn_take_a_simple_data);
        mBtnTrainData = (Button) view.findViewById(R.id.btn_train_data);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvTrainResult = (TextView) view.findViewById(R.id.tv_train_result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_a_simple_data:
                try {
                    String readLine = mBufferedReader.readLine();
                    if (readLine!=null){
                        mFeatureData = readLine.split(SEPERATOR_SPACE);
                        mFeatureListAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getActivity(), "数据已取完", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_train_data:
                new TrainModel().run();
                break;
        }
    }

    /**
     * listView适配器类
     */
    class FeatureListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFeatureData.length;
        }

        @Override
        public String getItem(int position) {
            return mFeatureData[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.predict_list_item, null);
                viewHolder.mTvPredict = (TextView) convertView.findViewById(R.id.tv_predict);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mTvPredict.setText(getItem(position));
            return convertView;
        }

        class ViewHolder {
            TextView mTvPredict;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            mTvTrainResult.setText(info);
        }
    };

    class TrainModel extends Thread {
        @Override
        public void run() {
            try {
                tarinModel(dir + separator + "train.txt",
                        dir + separator + range,
                        dir + separator + scaleFileName,
                        dir + separator + modelFileName,
                        dir + separator + train + separator + resultFileName,
                        dir + separator + train + separator + modelInfo,
                        dir + separator + train + separator + prdictInfo);
                readTrainInfo();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "请先采集数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readTrainInfo() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dir + separator + train + separator + prdictInfo)));
            String readLine = reader.readLine();
            if (readLine != null) {
                Message obtain = Message.obtain();
                obtain.what = 1;
                obtain.obj = readLine;
                mHandler.sendMessage(obtain);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 训练模型
     *
     * @param trainFile
     * @param rangeFile
     * @param modelFile
     * @param result    显示精度结果
     */
    public void tarinModel(String trainFile, String rangeFile, String scaleFile, String modelFile, String result, String modelInfo, String prdictInfo) {
        creatScaleFile(new String[]{"-l", "0", "-u", "1", "-s", rangeFile, trainFile}, scaleFile);
        creatModelFile(new String[]{"-s", "0", "-c", "128.0", "-t", "2", "-g", "8.0", "-e", "0.1", scaleFile, modelFile}, modelInfo);
        creatPredictFile(new String[]{scaleFile, modelFile, result}, prdictInfo);
    }

    /**
     * 训练数据train 进行归一化处理并生生scale文件
     *
     * @param args      String[] args = new String[]{"-l","0","-u","1",path+"/train"};
     * @param scalePath 结果输出文件路径
     */
    private static void creatScaleFile(String[] args, String scalePath) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(scalePath);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_scale.main(args);//开始归一化
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void creatModelFile(String[] args, String outInfo) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(outInfo);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_train.main(args);//开始训练模型
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void creatPredictFile(String[] args, String outInfo) {
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            File file = new File(outInfo);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream);
            // old stream
            PrintStream oldStream = System.out;
            System.setOut(printStream);//重新定义system.out
            svm_predict.main(args);//开始测试精度
            System.setOut(oldStream);//回复syste.out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (printStream != null) {
                    printStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
