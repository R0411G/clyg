package com.kingdown88.hook;

import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.kingdown88.util.LogUtil.getMethodSign;
import static com.kingdown88.util.LogUtil.log;

public class HookMTLX implements IXposedHookLoadPackage {

  //adb shell logcat -s zjdroid-shell-com.meituan.tower
  //adb shell logcat -s zjdroid-apimonitor-com.meituan.tower
  private static final String FILTER_PKGNAME = "com.meituan.tower";

  //adb shell logcat -s zjdroid-shell-ctrip.android.view
  //adb shell logcat -s zjdroid-apimonitor-ctrip.android.view
  //private static final String FILTER_PKGNAME = "ctrip.android.view";

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

              //c = classLoader.loadClass("ctrip.foundation.util.JsonUtils");
              //c = classLoader.loadClass("ctrip.foundation.util.CtripURLUtil");
              //c = classLoader.loadClass("ctrip.android.http.CtripHTTPClientV2");
              //c = classLoader.loadClass("ctrip.android.httpv2.CTHTTPClient");
              //c = classLoader.loadClass("ctrip.android.http.CtripHttpResponse");
              //printClassMethod(c);

              //"[[Ljava.lang.String;", //参数1
              //final Class<?> ArgClass= XposedHelpers.findClass("aqcxbom.xposedhooktarget.ArgClass", classLoader);
              //final Class<?> ArrayList= XposedHelpers.findClass("java.util.ArrayList", classLoader);
              //final Class<?> Map = XposedHelpers.findClass("java.util.Map", classLoader);
              final Class<?> cClass = XposedHelpers.findClass("java.lang.Class", classLoader);
              //final Class<?> cObject = XposedHelpers.findClass("java.lang.Object", classLoader);
              //final Class<?> CTHTTPRequest =
              //    XposedHelpers.findClass("ctrip.android.httpv2.CTHTTPRequest", classLoader);
              //final Class<?> CTHTTPCallback =
              //    XposedHelpers.findClass("ctrip.android.httpv2.CTHTTPCallback", classLoader);
              //final Class<?> CtripHTTPCallbackV2 =
              //    XposedHelpers.findClass("ctrip.android.http.CtripHTTPCallbackV2", classLoader);

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

              //XposedHelpers.findAndHookMethod(c, "asyncPostWithTimeout", String.class, String.class,
              //    CtripHTTPCallbackV2, int.class, Map, boolean.class, boolean.class,
              //    new XC_MethodHook() {
              //      @Override protected void beforeHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.beforeHookedMethod(param);
              //        log("``````````````````````````````````````````");
              //        log("b  =" + param.args[0]);
              //        log("b  =" + param.args[1]);
              //        log("b  =" + param.args[3]);
              //        log("b  =" + param.args[4]);
              //        log("b  =" + param.args[5]);
              //        log("b  =" + param.args[6]);
              //      }
              //
              //      @Override protected void afterHookedMethod(MethodHookParam param)
              //          throws Throwable {
              //        super.afterHookedMethod(param);
              //        log("a  =" + param.getResult());
              //      }
              //    });
              //
              //XposedHelpers.findAndHookMethod(c, "getResponse", new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //    log("``````````````````````````````````````````");
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    log("a  =" + param.getResult().toString());
              //  }
              //});

              //c = classLoader.loadClass("com.sankuai.meituan.retrofit2.Retrofit");
              c = classLoader.loadClass("com.sankuai.meituan.retrofit2.ClientCall");
              //printClassMethod(c);

              //final Class<?> cObject = XposedHelpers.findClass("java.lang.Object", classLoader);
              //final Class<?> cServiceMethod =
              //    XposedHelpers.findClass("retrofit2.ServiceMethod", classLoader);
              //final Class<?> mList = XposedHelpers.findClass("java.util.List", classLoader);
              //final Class<?> mList1 = XposedHelpers.findClass("java.util.List", classLoader);
              //final Class<?> mExecutor =
              //    XposedHelpers.findClass("java.util.concurrent.Executor", classLoader);
              //final Class<?> mCache =
              //    XposedHelpers.findClass("com.sankuai.meituan.retrofit2.cache.Cache", classLoader);

              //final Class<?> mRequest =
              //    XposedHelpers.findClass("com.sankuai.meituan.retrofit2.Request", classLoader);
              //final Class<?> mRawResponse =
              //    XposedHelpers.findClass("com.sankuai.meituan.retrofit2.raw.RawResponse",
              //        classLoader);
              //
              //String methodName = "getResponseWithInterceptorChain";
              //log("==============================================" + methodName);
              //XposedHelpers.findAndHookMethod(c, methodName, mRequest, new XC_MethodHook() {
              //  @Override protected void beforeHookedMethod(MethodHookParam param)
              //      throws Throwable {
              //    super.beforeHookedMethod(param);
              //    log("====================================getResponseWithInterceptorChain");
              //    printClassMethod(param.args[0].getClass());
              //  }
              //
              //  @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //    super.afterHookedMethod(param);
              //    log(param.getResult().toString());
              //  }
              //});
              //log("\n\n");

              c = classLoader.loadClass("com.sankuai.meituan.retrofit2.Request");
              XposedHelpers.findAndHookMethod(c, "url", new XC_MethodHook() {
                @Override protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                  super.beforeHookedMethod(param);
                }

                @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                  super.afterHookedMethod(param);
                  log(param.getResult().toString());
                }
              });
              log("///////////////======================================================================");
            }
          });
    }
  }

  /**
   * 根据属性名获取属性值
   */
  private String getFieldValueByFieldName(String fieldName, Object object) {
    try {
      Field field = object.getClass().getDeclaredField(fieldName);
      //设置对象的访问权限，保证对private的属性的访问
      field.setAccessible(true);
      return (String) field.get(object);
    } catch (Exception e) {
      return "错误";
    }
  }

  private void printClassMethod(Class<?> c) {
    Method[] allMethods = c.getDeclaredMethods();
    for (Method method : allMethods) {
      log("methodName=====> " + getMethodSign(method));
    }

    log(getFieldValueByFieldName("url", c));
  }
}
