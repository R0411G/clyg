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

  @Override public void handleLoadPackage(final LoadPackageParam loadPackageParam) {
    String pkgname = loadPackageParam.packageName;
    log("pkgname-->" + pkgname);
    if (FILTER_PKGNAME.equals(pkgname) && loadPackageParam.isFirstApplication) {
      //XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class,
      //    new XC_MethodHook() {
      //      @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
      //        if (param.hasThrowable()) {
      //          return;
      //        }
      //        try {
      //          Class<?> cls = (Class<?>) param.getResult();
      //          String name = cls.getName();
      //          if (!TextUtils.isEmpty(name) && (name.contains("sankuai") || name.contains(
      //              "meituan"))) {
      //            log("name-->" + name);
      //          }
      //        } catch (Exception e) {
      //        }
      //      }
      //    });

      XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
          new XC_MethodHook() {
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              Context context = (Context) param.args[0];
              ClassLoader classLoader = context.getClassLoader();

              Class<?> c;

              //c = classLoader.loadClass("com.sankuai.meituan.retrofit2.Retrofit");
              //printClassMethod(c);
              //
              //XposedHelpers.findAndHookMethod(c, "requestBodyConverter",
              //    java.lang.reflect.Type.class, "[Ljava.lang.annotation.Annotation;",
              //    "[Ljava.lang.annotation.Annotation;", new XC_MethodHook() {
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

              log("======================================================================");

              c = classLoader.loadClass("com.meituan.retrofit2.androidadapter.a");
              //printClassMethod(c);

              //final Class<?> ArgClass= XposedHelpers.findClass("aqcxbom.xposedhooktarget.ArgClass", classLoader);
              //final Class<?> ArrayList= XposedHelpers.findClass("java.util.ArrayList", classLoader);

              final Class<?> CallClass =
                  XposedHelpers.findClass("com.sankuai.meituan.retrofit2.Call", classLoader);
              final Class<?> ResponseClass =
                  XposedHelpers.findClass("com.sankuai.meituan.retrofit2.Response", classLoader);

              final Class<?> Map = XposedHelpers.findClass("java.util.Map", classLoader);

              //"[[Ljava.lang.String;", //参数1
              //    Map, //参数2
              //    Map, //参数3
              //    Map, //参数4
              //    ArrayList, //参数5
              //    ArrayList, //参数6
              //    ArgClass, //参数7

              final Class<?> chain =
                  XposedHelpers.findClass("com.sankuai.meituan.retrofit2.Interceptor.Chain",
                      classLoader);

              XposedHelpers.findAndHookMethod(c, "onResponse", CallClass, ResponseClass,
                  new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.beforeHookedMethod(param);
                      log("beforeHookedMethod args  =" + param.args[0]);
                    }

                    @Override protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.afterHookedMethod(param);
                      log("afterHookedMethod result=" + param.getResult());
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

  private static final String FILTER_PKGNAME = "com.meituan.tower";
}
