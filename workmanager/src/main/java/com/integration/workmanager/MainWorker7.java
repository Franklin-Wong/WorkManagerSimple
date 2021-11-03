package com.integration.workmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
public class MainWorker7 extends Worker {
    public static final String TAG = "MainWorker7";

    public static final String SP_NAME = "spNAME"; // SP name
    public static final String SP_KEY = "spKEY"; // KEY

    private Context mContext;
    private WorkerParameters mWorkerParameters;


    public MainWorker7(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        mWorkerParameters = workerParams;

    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "MainWorker7 doWork: 后台任务执行了");

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences sp = getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        int spInt = sp.getInt(SP_KEY, 0);

        sp.edit().putInt(SP_KEY, ++spInt).apply();

        Log.d(TAG, "MainWorker7 doWork: 后台任务执行了 end "+spInt);
        //本地执行 doWork 任务时 成功 执行任务完毕
        return new Result.Success();
    }
}
