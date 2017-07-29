package com.example.marcus.newsapp.data;

import com.example.marcus.newsapp.utilities.RefreshUtils;

import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.JobParameters;
import android.widget.Toast;
import android.os.AsyncTask;

/**
 * Created by Marcus on 7/28/2017.
 */

public class NewsJob extends JobService{

    AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob (final JobParameters job){
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                RefreshUtils.updateItems(NewsJob.this);

                return null;
            }
            @Override
            protected void onPreExecute(){
                Toast.makeText(NewsJob.this, "Updated", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(Object x){
                jobFinished(job, false);
                super.onPostExecute(x);
            }
        };
        mBackgroundTask.execute();
        return true;
    }
    @Override
    public boolean onStopJob(JobParameters job){
        if(mBackgroundTask != null){
            mBackgroundTask.cancel(false);
        }
        return true;
    }
}
