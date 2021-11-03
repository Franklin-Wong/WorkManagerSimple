package com.integration.workmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/9/13 - 20:33
 * @Description com.integration.workmanager
 */
public class MainWorker2 extends Worker {
    public static final String TAG = "MainWorker2";
    private Context mContext;
    private WorkerParameters mWorkerParameters;


    public MainWorker2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        mWorkerParameters = workerParams;
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "MainWorker2 doWork: 后台任务执行了");

        String dataString = mWorkerParameters.getInputData().getString("WANG").toString();
        Log.d(TAG, "MainWorker2 doWork: 接收Activity传递过来的数据:" + dataString);
        // 反馈数据 给 MainActivity
        // 把任务中的数据回传到activity中
        Data putString = new Data.Builder().putString("WANG", "返回数据给前台").build();
        Result success = Result.success(putString);
        Result.Success result = new Result.Success(putString);
        // return new Result.Failure(); // 本地执行 doWork 任务时 失败
        // return new Result.Retry(); // 本地执行 doWork 任务时 重试一次
        // return new Result.Success(); // 本地执行 doWork 任务时 成功 执行任务完毕

        return success;
    }
}
