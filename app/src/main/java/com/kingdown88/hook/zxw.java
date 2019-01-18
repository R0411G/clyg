package com.kingdown88.hook;

import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class zxw implements IXposedHookLoadPackage {

  @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

    String pkgname = loadPackageParam.packageName;

    log("pkgname-->" + pkgname);

    if ("com.fzisen.zxw".equals(loadPackageParam.packageName)) {

      log("findAndHookMethod``````````````````````````````````````````````````````````1");
      XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
          new XC_MethodHook() {
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {

              log("findAndHookMethod``````````````````````````````````````````````````````````");

              final Context context = (Context) param.args[0];
              final ClassLoader classLoader = context.getClassLoader();

              //Class<?> Utils = classLoader.loadClass("com.fzisen.zxw.util.Utils");
              //XposedHelpers.findAndHookMethod(Utils, "MD532H", String.class,
              //    new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //        log("beforeHookedMethod --------------------------------------"
              //            + param.getClass().getName());
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //        log("afterHookedMethod args  =" + param.args[0]);
              //        log("afterHookedMethod result=" + param.getResult());
              //        log("afterHookedMethod 0------------------------------------"
              //            + param.getClass().getName());
              //      }
              //    });

              //Class<?> VideoContentEntity = classLoader.loadClass("com.fzisen.zxw.android.entity.VideoContentEntity");
              //XposedHelpers.findAndHookMethod(VideoContentEntity, "getItemType", new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    //log("afterHookedMethod result=" + param.getResult());
              //    param.setResult("1");
              //  }
              //});
              //XposedHelpers.findAndHookMethod(VideoContentEntity, "getVideoUrl", new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    log("afterHookedMethod result=" + param.getResult());
              //  }
              //});

              ////Object jsonThreeAnalysisObject3 = JsonAnalysis.jsonThreeAnalysisObject(str, "mesVnum");
              //Class<?> JsonAnalysis =
              //    classLoader.loadClass("com.fzisen.zxw.android.jsonanalysis.JsonAnalysis");
              //XposedHelpers.findAndHookMethod(JsonAnalysis, "jsonThreeAnalysisObject", String.class,
              //    String.class, new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //        log("beforeHookedMethod --------------------------------------jsonThreeAnalysisObject");
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //        log("afterHookedMethod args  =" + param.args[0]);
              //        log("afterHookedMethod args  =" + param.args[1]);
              //        log("afterHookedMethod result=" + param.getResult());
              //        log("afterHookedMethod ------------------------------------jsonThreeAnalysisObject");
              //        if ("mesVnum".equals(param.args[1])) {
              //          param.setResult("400");
              //        }
              //      }
              //    });
              //

              //Class<?> User = classLoader.loadClass("com.fzisen.zxw.model.User");
              //XposedHelpers.findAndHookMethod(User, "getUserId", new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    param.setResult("17200002");
              //  }
              //});
              //
              //Class<?> DecryptionRun =
              //    classLoader.loadClass("com.fzisen.zxw.encryptiondispose.DecryptionRun");
              //
              //XposedHelpers.findAndHookMethod(DecryptionRun, "decoding", InputStream.class,
              //    FileOutputStream.class, long.class, int.class, new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //
              //        StringBuilder stringBuilder = new StringBuilder();
              //        stringBuilder.append(context.getFilesDir().getAbsolutePath());
              //        stringBuilder.append(File.separator);
              //        stringBuilder.append("cache.flv");
              //        if (new File(stringBuilder.toString()).exists()) {
              //          String fromFile =
              //              context.getFilesDir().getAbsolutePath() + File.separator + "cache.flv";
              //          String toFile = context.getFilesDir().getAbsolutePath()
              //              + File.separator
              //              + System.currentTimeMillis()
              //              + "cache.flv";
              //          log("复制 " + CopySdcardFile(fromFile, toFile));
              //        }
              //      }
              //    });

              //XposedHelpers.findAndHookMethod(User, "setUserVnum", String.class,
              //    new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //        log("beforeHookedMethod --------------------------------------setUserVnum");
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //        log("afterHookedMethod args  =" + param.args[0]);
              //        log("afterHookedMethod ------------------------------------setUserVnum");
              //      }
              //    });
              //XposedHelpers.findAndHookMethod(User, "getUserVnum", new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //    log("beforeHookedMethod --------------------------------------getUserVnum");
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    log("afterHookedMethod result=" + param.getResult());
              //    log("afterHookedMethod ------------------------------------getUserVnum");
              //    param.setResult("400");
              //  }
              //});
            }
          });
    } else {
      log("findAndHookMethod``````````````````````````````````````````````````````````2");
    }
  }

  private void log(Object str) {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMDD HH:mm:ss", Locale.CHINA);
    String text = "[" + df.format(new Date()) + "]:  " + str.toString();
    System.out.println(text);
    XposedBridge.log(text);
  }

  public int copyFile(String fromFile, String toFile) {
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
        CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
      }
    }
    return 0;
  }

  public int CopySdcardFile(String fromFile, String toFile) {
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
