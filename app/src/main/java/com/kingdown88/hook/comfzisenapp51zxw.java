package com.kingdown88.hook;

import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.kingdown88.hook.HookLog.log;

public class comfzisenapp51zxw implements IXposedHookLoadPackage {

  private static final String FILTER_PKGNAME = "com.fzisen.app51zxw";

  @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
    String pkgname = loadPackageParam.packageName;
    log("pkgname-->" + pkgname);
    if (FILTER_PKGNAME.equals(pkgname)) {
      XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
          new XC_MethodHook() {
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              final Context context = (Context) param.args[0];
              final ClassLoader classLoader = context.getClassLoader();

              //Class<?> Utils = classLoader.loadClass("com.fzisen.app51zxw.util.Utils");
              //XposedHelpers.findAndHookMethod(Utils, "MD532H", String.class,
              //    new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //        log("afterHookedMethod args  =" + param.args[0]);
              //        log("afterHookedMethod result=" + param.getResult());
              //      }
              //    });
              //Class<?> VideoContentEntity = classLoader.loadClass("com.fzisen.app51zxw.android.entity.VideoContentEntity");
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
              //    classLoader.loadClass("com.fzisen.app51zxw.android.jsonanalysis.JsonAnalysis");
              //XposedHelpers.findAndHookMethod(JsonAnalysis, "jsonThreeAnalysisObject", String.class,
              //    String.class, new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //        log("afterHookedMethod args  =" + param.args[0]);
              //        log("afterHookedMethod args  =" + param.args[1]);
              //        log("afterHookedMethod result=" + param.getResult());
              //      }
              //    });
              //
              //Class<?> User = classLoader.loadClass("com.fzisen.app51zxw.model.User");
              //XposedHelpers.findAndHookMethod(User, "getUserId", new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    param.setResult("00000");
              //  }
              //});
              //
              //Class<?> DecryptionRun =
              //    classLoader.loadClass("com.fzisen.app51zxw.encryptiondispose.DecryptionRun");
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
              //          log("复制 " + copySdFile(fromFile, toFile));
              //        }
              //      }
              //    });
            }
          });
    }
  }
}
