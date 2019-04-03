package com.kingdown88.wb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.kingdown88.util.BackupApp;

public class BackupAppActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final BackupApp backupApp = new BackupApp();
    final TextView tvList = (TextView) findViewById(R.id.tvList);

    findViewById(R.id.tvBtn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        new Thread(new Runnable() {
          @Override public void run() {
            backupApp.updateView(tvList, "Backup Start......");
            try {
              backupApp.backupAllUserApp(BackupAppActivity.this, tvList);
            } catch (Exception e) {
              e.printStackTrace();
            }
            backupApp.updateView(tvList, "Backup Finished!");
          }
        }).start();
      }
    });
  }
}
