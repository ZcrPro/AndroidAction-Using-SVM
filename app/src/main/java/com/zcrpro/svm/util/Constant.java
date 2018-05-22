package com.zcrpro.svm.util;

import android.os.Environment;

import java.util.HashMap;
import java.util.Map;

import static java.io.File.separator;

public class Constant {

    public static String dir = Environment.getExternalStorageDirectory() + separator + "ActionSVM";
    public static String modelFileName = "model";
    public static String rangeFileName = "range";
    public static String trainFileName = "train";
    public static final String SEPERATOR_SPACE = " ";       // 特征分隔符
    public static final String SEPERATOR_COLON = ":";       // lable分隔符
    public static final String train = "traindata";
    public static final String scaleFileName = "scale";
    public static final String resultFileName = "result";
    public static final String modelInfo = "modelInfo";
    public static final String prdictInfo = "prdictInfo";
    public static final String range = "range";

    public static Map<String, Double> actMapToCode = new HashMap<String, Double>();
    public static Map<Double, String> actMapFromCode = new HashMap<Double, String>();

    static {
        actMapToCode.put("静止", 1d);
        actMapToCode.put("走路", 2d);
        actMapToCode.put("跑步", 3d);
        actMapToCode.put("上楼", 4d);
        actMapToCode.put("下楼", 5d);
        actMapToCode.put("左右摇动", 6d);
        actMapToCode.put("上下摇动", 7d);

        actMapFromCode.put(1d, "静止");
        actMapFromCode.put(2d, "走路");
        actMapFromCode.put(3d, "跑步");
        actMapFromCode.put(4d, "上楼");
        actMapFromCode.put(5d, "下楼");
        actMapFromCode.put(6d, "左右摇动");
        actMapFromCode.put(7d, "上下摇动");
    }

	/*函数名*/
    /**
     * 最小值 min
     **/
    public static final String FUN_101_MINIMUM_NAME = "min";
    public static final int FUN_101_MINIMUM_CODE = 101;
    /**
     * 最大值 max
     **/
    public static final String FUN_102_MAXIMUM_NAME = "max";
    public static final int FUN_102_MAXIMUM_CODE = 102;
    /**
     * 方差 variance
     **/
    public static final String FUN_103_VARIANCE_NAME = "variance";
    public static final int FUN_103_VARIANCE_CODE = 103;
    /**
     * 过均值率 mcr
     **/
    public static final String FUN_104_MEANCROSSINGSRATE_NAME = "mcr";
    public static final int FUN_104_MEANCROSSINGSRATE_CODE = 104;
    /**
     * 标准差 stddev
     **/
    public static final String FUN_105_STANDARDDEVIATION_NAME = "stddev";
    public static final int FUN_105_STANDARDDEVIATION_CODE = 105;
    /**
     * 平均值 mean
     **/
    public static final String FUN_106_MEAN_NAME = "mean";
    public static final int FUN_106_MEAN_CODE = 106;
    /**
     * 向量幅值 mag
     **/
    public static final String FUN_107_SIGNALVECTORMAGNITUDE_NAME = "mag";
    public static final int FUN_107_SIGNALVECTORMAGNITUDE_CODE = 107;
    /**
     * 四分卫数 1/4 q1
     **/
    public static final String FUN_108_FIRSTQUARTILE_NAME = "q1";
    public static final int FUN_108_FIRSTQUARTILE_CODE = 108;
    /**
     * 中位数 median
     **/
    public static final String FUN_109_MEDIAN_NAME = "median";
    public static final int FUN_109_MEDIAN_CODE = 109;
    /**
     * 四分卫数 3/4 q3
     **/
    public static final String FUN_110_THIRDQUARTILE_NAME = "q3";
    public static final int FUN_110_THIRDQUARTILE_CODE = 110;
    /**
     * 过零率 zcr
     **/
    public static final String FUN_111_ZEROCROSSINGRATE_NAME = "zcr";
    public static final int FUN_111_ZEROCROSSINGRATE_CODE = 111;
    /**
     * 均方根平均值 rms
     **/
    public static final String FUN_112_RMS_NAME = "rms";
    public static final int FUN_112_RMS_CODE = 112;
    /**
     * 向量幅值面积 sma
     **/
    public static final String FUN_113_SMA_NAME = "sma";
    public static final int FUN_113_SMA_CODE = 113;
    /**
     * 四分卫距 iqr
     **/
    public static final String FUN_114_IQR_NAME = "iqr";
    public static final int FUN_114_IQR_CODE = 114;
    /**
     * 绝对平均差 mad
     **/
    public static final String FUN_115_MAD_NAME = "mad";
    public static final int FUN_115_MAD_CODE = 115;
    /**
     * 时域 能量 tenergy
     **/
    public static final String FUN_116_TENERGY_NAME = "tenergy";
    public static final int FUN_116_TENERGY_CODE = 116;


    /**
     * spp最大值的位置 谱峰位置
     **/
    public static final String FUN_201_SPP_NAME = "spp";
    public static final int FUN_201_SPP_CODE = 201;
    /**
     * 能量 energy
     **/
    public static final String FUN_202_ENERGY_NAME = "energy";
    public static final int FUN_202_ENERGY_CODE = 202;
    /**
     * 熵 entropy
     **/
    public static final String FUN_203_ENTROPY_NAME = "entropy";
    public static final int FUN_203_ENTROPY_CODE = 203;
    /**
     * 质心 centroid
     **/
    public static final String FUN_204_CENTROID_NAME = "centroid";
    public static final int FUN_204_CENTROID_CODE = 204;
    /**
     * 频域 标准差 fdev
     **/
    public static final String FUN_205_FDEV_NAME = "fdev";
    public static final int FUN_205_FDEV_CODE = 205;
    /**
     * 频域 平均值 fmean
     **/
    public static final String FUN_206_FMEAN_NAME = "fmean";
    public static final int FUN_206_FMEAN_CODE = 206;
    /**
     * 频域 偏度 skew
     **/
    public static final String FUN_207_SKEW_NAME = "skew";
    public static final int FUN_207_SKEW_CODE = 207;
    /**
     * 频域 峰度 kurt
     **/
    public static final String FUN_208_KURT_NAME = "kurt";
    public static final int FUN_208_KURT_CODE = 208;

}
