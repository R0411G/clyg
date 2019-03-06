package com.kingdown88.hook;

import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import java.lang.reflect.Method;

import static com.kingdown88.hook.HookLog.getMethodSign;
import static com.kingdown88.hook.HookLog.log;

public class HookMTLX implements IXposedHookLoadPackage {

  //adb shell logcat -s zjdroid-shell-com.meituan.tower
  //adb shell logcat -s zjdroid-apimonitor-com.meituan.tower
  //private static final String FILTER_PKGNAME = "com.meituan.tower";

  //adb shell logcat -s zjdroid-shell-ctrip.android.view
  //adb shell logcat -s zjdroid-apimonitor-ctrip.android.view
  private static final String FILTER_PKGNAME = "ctrip.android.view";

  @Override public void handleLoadPackage(final LoadPackageParam loadPackageParam) {
    String pkgname = loadPackageParam.packageName;
    log("pkgname-->" + pkgname);
    if (FILTER_PKGNAME.equals(pkgname) && loadPackageParam.isFirstApplication) {

      XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
          new XC_MethodHook() {
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              Context context = (Context) param.args[0];
              ClassLoader classLoader = context.getClassLoader();

              log("======================================================================");
              Class<?> c;

              c = classLoader.loadClass("ctrip.foundation.util.JsonUtils");
              c = classLoader.loadClass("ctrip.foundation.util.CtripURLUtil");
              c = classLoader.loadClass("ctrip.android.http.CtripHTTPClientV2");
              //printClassMethod(c);

              //"[[Ljava.lang.String;", //参数1
              //final Class<?> ArgClass= XposedHelpers.findClass("aqcxbom.xposedhooktarget.ArgClass", classLoader);
              //final Class<?> ArrayList= XposedHelpers.findClass("java.util.ArrayList", classLoader);
              final Class<?> Map = XposedHelpers.findClass("java.util.Map", classLoader);
              final Class<?> cClass = XposedHelpers.findClass("java.lang.Class", classLoader);
              final Class<?> cObject = XposedHelpers.findClass("java.lang.Object", classLoader);
              final Class<?> CtripHTTPCallbackV2 =
                  XposedHelpers.findClass("ctrip.android.http.CtripHTTPCallbackV2", classLoader);

              //XposedHelpers.findAndHookMethod(c, "parse", String.class, cClass,
              //    new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //        log("``````````````````````````````````````````");
              //        log("b  =" + param.args[0]);
              //        log("b  =" + param.args[1]);
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //        log("a  =" + param.getResult());
              //      }
              //    });
              //
              //XposedHelpers.findAndHookMethod(c, "toJson", cObject, new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //    log("``````````````````````````````````````````");
              //    log("b  =" + param.args[0]);
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    log("a  =" + param.getResult());
              //  }
              //});

              XposedHelpers.findAndHookMethod(c, "asyncPostWithTimeout", String.class, String.class,
                  CtripHTTPCallbackV2, int.class, Map, boolean.class, boolean.class,
                  new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.beforeHookedMethod(param);
                      log("``````````````````````````````````````````");
                      log("b  =" + param.args[0]);
                      log("b  =" + param.args[1]);
                      log("b  =" + param.args[3]);
                      log("b  =" + param.args[4]);
                      log("b  =" + param.args[5]);
                      log("b  =" + param.args[6]);
                    }

                    @Override protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.afterHookedMethod(param);
                      log("a  =" + param.getResult());
                    }
                  });
              log("///////////////======================================================================");
            }
          });
    }
  }

  private void printClassMethod(Class<?> c) {
    Method[] allMethods = c.getDeclaredMethods();
    for (Method method : allMethods) {
      log("methodName=====> " + getMethodSign(method));
    }
  }
}
