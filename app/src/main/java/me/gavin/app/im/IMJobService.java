package me.gavin.app.im;

import android.app.job.JobParameters;
import android.app.job.JobService;

import me.gavin.util.L;
import me.gavin.util.NotificationHelper;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/3/9
 */
public class IMJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        L.e("onStartJob - " + params);
        doSampleJob(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        L.e("onStopJob - " + params);
        return false;
    }

    public void doSampleJob(JobParameters params) {
        // Do some heavy operation
        L.e("doSampleJob - " + params);
        NotificationHelper.notify(this, "IMJobService", "doSampleJob", "doSampleJob", null);

        // App.get().startService(new Intent(App.get(), IMService.class));// TODO: 2018/3/9 Not allowed to start service Intent


        // At the end inform job manager the status of the job.
        jobFinished(params, false);
    }
}
