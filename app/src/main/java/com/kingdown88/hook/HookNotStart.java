package com.kingdown88.hook;

/**
 * Created by wsig on 2019-04-03.
 */

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 这种方案建议只在开发调试的时候使用，因为这将损耗一些性能(需要额外加载apk文件)，调试没问题后，直接修改xposed_init文件为正确的类即可
 * 可以实现免重启，由于存在缓存，需要杀死宿主程序以后才能生效
 * 这种免重启的方式针对某些特殊情况的hook无效
 * 例如我们需要implements IXposedHookZygoteInit,并将自己的一个服务注册为系统服务，这种就必须重启了
 */

public class HookNotStart implements IXposedHookLoadPackage {
  /**
   * 实际hook包名
   */
  private static final String modulePackage = "xxx.xxx.xxx";
  /**
   * 实际hook逻辑处理类
   */
  private final String handleHookClass = HookNotStart.class.getName();
  /**
   * 实际hook逻辑处理类的入口方法
   */
  private final String handleHookMethod = "handleLoadPackage";
  //
  private static List<String> hostAppPackages = new ArrayList<>();

  static {
    hostAppPackages.add("com.eg.android.AlipayGphone");
    hostAppPackages.add(modulePackage);
  }

  @Override public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
    if (hostAppPackages.contains(loadPackageParam.packageName)) {
      //将loadPackageParam的classloader替换为宿主程序Application的classloader,解决宿主程序存在多个.dex文件时,有时候ClassNotFound的问题
      XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
          new XC_MethodHook() {
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              Context context = (Context) param.args[0];
              loadPackageParam.classLoader = context.getClassLoader();
              invokeHandleHookMethod(context, modulePackage, handleHookClass, handleHookMethod,
                  loadPackageParam);
            }
          });
    }
  }

  /**
   * 安装app以后，系统会在/data/app/下备份了一份.apk文件，通过动态加载这个apk文件，调用相应的方法
   * 这样就可以实现，只需要第一次重启，以后修改hook代码就不用重启了
   */
  private void invokeHandleHookMethod(Context context, String modulePackageName,
      String handleHookClass, String handleHookMethod,
      XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
    File apkFile = findApkFile(context, modulePackageName);
    if (apkFile == null) {
      throw new RuntimeException("寻找模块apk失败");
    }
    //加载指定的hook逻辑处理类，并调用它的handleHook方法
    PathClassLoader pathClassLoader =
        new PathClassLoader(apkFile.getAbsolutePath(), ClassLoader.getSystemClassLoader());
    Class<?> cls = Class.forName(handleHookClass, true, pathClassLoader);
    Object instance = cls.newInstance();
    Method method = cls.getDeclaredMethod(handleHookMethod, XC_LoadPackage.LoadPackageParam.class);
    method.invoke(instance, loadPackageParam);
  }

  /**
   * 根据包名构建目标Context,并调用getPackageCodePath()来定位apk
   */
  private File findApkFile(Context context, String modulePackageName) {
    if (context == null) {
      return null;
    }
    try {
      Context packageContext = context.createPackageContext(modulePackageName,
          Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
      String apkPath = packageContext.getPackageCodePath();
      return new File(apkPath);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
