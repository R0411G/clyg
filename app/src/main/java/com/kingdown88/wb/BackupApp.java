package com.kingdown88.wb;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by wsig on 2019-04-03.
 */
public class BackupApp {
  static final String TAG = "backup";

  static final String NEW_FILE_BASE = Environment.getExternalStorageDirectory().getAbsolutePath()
      + File.separator
      + "ApkBackup"
      + File.separator;
  File mBaseFile = new File(NEW_FILE_BASE);

  public void backupApp(String path, String outname) throws IOException {
    File in = new File(path);

    if (!mBaseFile.exists()) mBaseFile.mkdir();
    File out = new File(mBaseFile, outname + ".apk");
    if (!out.exists()) out.createNewFile();
    FileInputStream fis = new FileInputStream(in);
    FileOutputStream fos = new FileOutputStream(out);

    int count;
    byte[] buffer = new byte[256 * 1024];
    while ((count = fis.read(buffer)) > 0) {
      fos.write(buffer, 0, count);
    }

    fis.close();
    fos.flush();
    fos.close();
  }

  public void backupAllUserApp(Context context, final TextView mInfo) {
    PackageManager packageManager = context.getPackageManager();
    List<PackageInfo> allPackages = packageManager.getInstalledPackages(0);
    for (int i = 0; i < allPackages.size(); i++) {
      PackageInfo packageInfo = allPackages.get(i);
      String path = packageInfo.applicationInfo.sourceDir;
      String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
      Log.i(TAG, path);
      Log.i(TAG, name);

      try {
        if (isUserApp(packageInfo)) {
          Log.e(TAG, name + " is not user app,skip...");
          updateView(mInfo, name + " is not user app,skip...");
          continue;
        }
        updateView(mInfo, "Start backup:" + name + "...");
        backupApp(path, name);
        updateView(mInfo, "*****Succeed backup:" + name + "******");

        Toast.makeText(context, "Succeed backup:" + path, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Succeed backup:" + name);
      } catch (Exception e) {
        Log.e(TAG, path + "Failed backup  " + e.getMessage());
        e.printStackTrace();
        continue;
      }
    }
  }

  StringBuilder mInfoStr = new StringBuilder();

  public void updateView(final TextView mInfo, String info) {
    mInfoStr.append(info + "\n");
    mInfo.post(new Runnable() {
      @Override public void run() {
        mInfo.setText(mInfoStr.toString());
      }
    });
  }

  public boolean isUserApp(PackageInfo pInfo) {
    return (((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
        && ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0));
  }
}
