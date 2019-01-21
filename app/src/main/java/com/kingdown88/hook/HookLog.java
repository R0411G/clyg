package com.kingdown88.hook;

import de.robv.android.xposed.XposedBridge;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wsig on 2019-01-21.
 */
public class HookLog {

  public static void log(Object str) {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMDD HH:mm:ss", Locale.CHINA);
    String text = "[" + df.format(new Date()) + "]:  " + str.toString();
    System.out.println(text);
    XposedBridge.log(text);
  }

  public static int copyFile(String fromFile, String toFile) {
    File[] currentFiles;
    File root = new File(fromFile);
    if (!root.exists()) {
      return -1;
    }
    currentFiles = root.listFiles();
    File targetDir = new File(toFile);
    if (!targetDir.exists()) {
      targetDir.mkdirs();
    }
    for (int i = 0; i < currentFiles.length; i++) {
      if (currentFiles[i].isDirectory()) {
        copyFile(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");
      } else {
        copySdFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
      }
    }
    return 0;
  }

  public static int copySdFile(String fromFile, String toFile) {
    try {
      InputStream fosfrom = new FileInputStream(fromFile);
      OutputStream fosto = new FileOutputStream(toFile);
      byte bt[] = new byte[1024];
      int c;
      while ((c = fosfrom.read(bt)) > 0) {
        fosto.write(bt, 0, c);
      }
      fosfrom.close();
      fosto.close();
      log("成功" + toFile);
      return 0;
    } catch (Exception ex) {
      return -1;
    }
  }
}
