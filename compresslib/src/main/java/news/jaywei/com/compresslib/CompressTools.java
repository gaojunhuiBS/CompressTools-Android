package news.jaywei.com.compresslib;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * 压缩方法工具类
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-08 9:03
 */

public class CompressTools {
    private static volatile CompressTools INSTANCE;

    private Context context;
    /**
     * 最大宽度，默认为720
     */
    private int maxWidth = 720;
    /**
     * 最大高度,默认为960
     */
    private int maxHeight = 960;
    /**
     * 默认压缩后的方式为JPEG
     */
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    /**
     * 默认的图片处理方式是ARGB_8888
     */
    private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
    /**
     * 默认压缩质量为60,60足够清晰了。可以对比一下。可以自定义
     */
    private int quality = 60;
    /**
     * 默认压缩质量是否为最优，默认为true
     */
    private boolean optimize = true;
    /**
     * 是否使用原图分辨率，默认为false
     */
    private boolean keepResolution = false;
    /**
     * 存储路径
     */
    private String destinationDirectoryPath;
    /**
     * 文件名前缀
     */
    private String fileNamePrefix;
    /**
     * 文件名
     */
    private String fileName;

    public static CompressTools getDefault(Context context) {
        if (INSTANCE == null) {
            synchronized (CompressTools.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CompressTools(context);
                }
            }
        }
        return INSTANCE;
    }

    private CompressTools(Context context) {
        this.context = context;
        destinationDirectoryPath = context.getCacheDir().getPath() + File.pathSeparator + FileUtil.FILES_PATH;
    }

    /**
     * description:
     * author: liujie
     * date: 2017/8/22 18:19
     */
    public void compressToFileJni(final File file,  final OnCompressListener mOnCompressListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BitmapUtil.compressImageJni(context, Uri.fromFile(file), maxWidth, maxHeight, compressFormat, bitmapConfig, quality, destinationDirectoryPath,
                        fileNamePrefix, fileName, optimize,keepResolution, mOnCompressListener);
            }
        }).start();
    }

    /**
     * description:
     * author: liujie
     * date: 2017/8/22 18:19
     */
    public void compressToBitmapJni(final File file, final OnCompressBitmapListener onCompressBitmapListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BitmapUtil.compressTOBitmapJni(context, Uri.fromFile(file), maxWidth, maxHeight, compressFormat, bitmapConfig, quality, destinationDirectoryPath,
                        fileNamePrefix, fileName,optimize, onCompressBitmapListener);
            }
        }).start();

    }

    /**
     * 采用建造者模式，设置Builder
     */
    public static class Builder {
        private CompressTools compressTools;

        public Builder(Context context) {
            compressTools = new CompressTools(context);
        }

        /**
         * 设置图片最大宽度
         *
         * @param maxWidth 最大宽度
         */
        public Builder setMaxWidth(int maxWidth) {
            compressTools.maxWidth = maxWidth;
            return this;
        }

        /**
         * 设置图片最大高度
         *
         * @param maxHeight 最大高度
         */
        public Builder setMaxHeight(int maxHeight) {
            compressTools.maxHeight = maxHeight;
            return this;
        }

        /**
         * 设置压缩的后缀格式
         */
        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            compressTools.compressFormat = compressFormat;
            return this;
        }
        /**
         * 设置是否开启压缩最优化
         */
        public Builder setOptimize(boolean optimize) {
            compressTools.optimize = optimize;
            return this;
        }

        /**
         * 设置Bitmap的参数
         */
        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            compressTools.bitmapConfig = bitmapConfig;
            return this;
        }

        /**
         * 设置分辨率是否保持原图分辨率
         */
        public Builder setKeepResolution(boolean keepResolution) {
            compressTools.keepResolution = keepResolution;
            return this;
        }

        /**
         * 设置压缩质量，建议50,50就足够了，基本无损压缩.
         *
         * @param quality 压缩质量，[0,100]
         */
        public Builder setQuality(int quality) {
            compressTools.quality = quality;
            return this;
        }

        /**
         * 设置目的存储路径
         *
         * @param destinationDirectoryPath 目的路径
         */
        public Builder setDestinationDirectoryPath(String destinationDirectoryPath) {
            compressTools.destinationDirectoryPath = destinationDirectoryPath;
            return this;
        }

        /**
         * 设置文件前缀
         *
         * @param prefix 前缀
         */
        public Builder setFileNamePrefix(String prefix) {
            compressTools.fileNamePrefix = prefix;
            return this;
        }

        /**
         * 设置文件名称
         *
         * @param fileName 文件名
         */
        public Builder setFileName(String fileName) {
            compressTools.fileName = fileName;
            return this;
        }

        public CompressTools build() {
            return compressTools;
        }
    }
}
