package com.integration.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/9/13 - 20:20
 * @Description com.integration.workmanager
 */
public class MainWorker1 extends Worker {
    private static final String TAG = "MainWorker1";
    public MainWorker1(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * 后台任务 并且 异步的
     * @return
     */
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "MainWorker1 doWork: run started ... ");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Result.failure();
        }finally {
            Log.d(TAG, "MainWorker1 doWork: run end ... ");
        }
        return Result.success();
    }
}
