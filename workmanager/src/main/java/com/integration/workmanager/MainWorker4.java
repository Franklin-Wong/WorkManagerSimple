package com.integration.workmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/9/13 - 23:46
 * @Description com.integration.workmanager
 */
public class MainWorker4 extends Worker {
    public static final String TAG = "MainWorker4";
    private Context mContext;
    private WorkerParameters mWorkerParameters;


    public MainWorker4(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        mWorkerParameters = workerParams;

    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "MainWorker4 doWork: 后台任务执行了");

        return new Result.Success();
    }
}
