package com.integration.workmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class WorkManagerActivity extends AppCompatActivity
implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "WANG";
    private Button btSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);
        btSp = findViewById(R.id.bt6);
        //注册监听
        SharedPreferences sp = getSharedPreferences(MainWorker7.SP_NAME, Context.MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(this);
        updateToUi();

    }

    /**
     * 最简单的 执行任务
     * @param view
     */
    public void testBackgroundWork1(View view) {

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MainWorker1.class)
                .build();
        WorkManager.getInstance(this).enqueue(request);
    }


    /**
     * 数据 互相传递
     * 测试后台任务 2
     * @param view
     */
    public void testBackgroundWork2(View view) {
        OneTimeWorkRequest oneTimeWorkRequest;

        Data sendData = new Data.Builder().putString("WANG", "workManager 给后台").build();

        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MainWorker2.class)
                .setInputData(sendData)
                .build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d(MainWorker2.TAG, "状态：" + workInfo.getState().name());
                        if (workInfo.getState().isFinished()) {

                            // 知道 本次任务执行的时候 各种状态
                            // （SUCCEEDED，isFinished=true， 我再接收 任何回馈给我的数据）
                            // 状态机 成功的时候 才去打印
                            Log.d(MainWorker2.TAG, "Activity取到了任务回传的数据: "
                                    + workInfo.getOutputData().getString("WANG"));

                            Log.d(MainWorker2.TAG, "状态：isFinished=true 同学们注意：后台任务已经完成了...");
                        }
                    }
                });

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);

    }

    public void testBackgroundWork3(View view) {
        OneTimeWorkRequest oneTimeWorkRequest3 = new OneTimeWorkRequest.Builder(MainWorker3.class).build();
        OneTimeWorkRequest oneTimeWorkRequest4 = new OneTimeWorkRequest.Builder(MainWorker4.class).build();
        OneTimeWorkRequest oneTimeWorkRequest5 = new OneTimeWorkRequest.Builder(MainWorker5.class).build();
        OneTimeWorkRequest oneTimeWorkRequest6 = new OneTimeWorkRequest.Builder(MainWorker6.class).build();

//        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest3)
//                .then(oneTimeWorkRequest4)
//                .then(oneTimeWorkRequest5)
//                .then(oneTimeWorkRequest6)
//                .enqueue();


        ArrayList<OneTimeWorkRequest> workRequests = new ArrayList<>();
        workRequests.add(oneTimeWorkRequest3);
        workRequests.add(oneTimeWorkRequest4);
        WorkManager.getInstance(this).beginWith(workRequests)
                .then(oneTimeWorkRequest6)
                .enqueue();


    }

    /**
     * 重复执行后台任务  非单个任务，多个任务
     * @param view
     */
    public void testBackgroundWork4(View view) {
        // 重复的任务  多次/循环/轮询  , 哪怕设置为 10秒 轮询一次,   那么最少轮询/循环一次 15分钟（Google规定的）
        // 不能小于15分钟，否则默认修改成 15分钟 对电池做了优化
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest
                .Builder(MainWorker4.class, 10, TimeUnit.SECONDS).build();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        Log.i(TAG, "onChanged: 状态 ：" + workInfo.getState().name());
                        if (workInfo.getState().isFinished()) {
                            Log.i(TAG,
                                    "onChanged: 状态：isFinished=true 同学们注意：后台任务已经完成了...");
                        }
                    }
                });

        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
    }

    /**
     * 约束条件，约束后台任务执行
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void testBackgroundWork5(View view) {
        Constraints constraints = new Constraints.Builder()
                //必须是连接网络状态
                .setRequiredNetworkType(NetworkType.CONNECTED)
                //必须是充电状态
                .setRequiresCharging(true)
                //设备是否是空闲状态
                .setRequiresDeviceIdle(true)
                .build();
        /**
         * 除了上面设置的约束外，WorkManger还提供了以下的约束作为Work执行的条件：
         *  setRequiredNetworkType：网络连接设置
         *  setRequiresBatteryNotLow：是否为低电量时运行 默认false
         *  setRequiresCharging：是否要插入设备（接入电源），默认false
         *  setRequiresDeviceIdle：设备是否为空闲，默认false
         *  setRequiresStorageNotLow：设备可用存储是否不低于临界阈值
         */

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MainWorker5.class)
                .setConstraints(constraints).build();

        WorkManager.getInstance(this).enqueue(request);

    }

    /**
     * 你怎么知道，他被杀掉后，还在后台执行？）写入文件的方式（SP），
     * 向同学们证明 Derry说的 所言非虚
     * @param view
     */
    public void testBackgroundWork6(View view) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MainWorker7.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(request);

    }
    /**
     * 监听 SP 里面数据的变化，你只要敢变，我这里就知道啦
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateToUi();
    }

    private void updateToUi() {
        SharedPreferences sp = getSharedPreferences(MainWorker7.SP_NAME, Context.MODE_PRIVATE);

        int resultValue = sp.getInt(MainWorker7.SP_KEY, 0);
        btSp.setText("测试后台任务六 -- " + resultValue);
        Log.i(TAG, "updateToUi: "+resultValue);

    }
    public void spReset(View view) {
        SharedPreferences sp = getSharedPreferences(MainWorker7.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(MainWorker7.SP_KEY, 0).apply();
        updateToUi();

    }

    public void codeStudy(View view) {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MainWorker3.class).build();

        /**
         * 这是第二次执行getInstance  ---  最终返回：WorkManagerImpl
         *
         * APK清单文件里面（第一次）执行  面试官
         * 1.初始化 数据库 ROOM 来保存你的任务 （持久性保存的） 手机重新，APP被杀掉 没关系 一定执行
         * 2.初始化 埋下伏笔  new GreedyScheduler 贪婪执行器
         * 3.初始化 配置信息 configuration  （执行的信息，线程池任务，...）
         */
        //这是第二次执行，APK清单文件里面（第一次）执行
        WorkManager.getInstance(this)
                //加入队列执行 执行流程
                .enqueue(request);


    }
}