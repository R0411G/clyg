package com.kingdown88.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import static com.kingdown88.hook.HookLog.log;

/**
 * FDex2核心代码MainHook
 */
public class FDex2MainHook implements IXposedHookLoadPackage {

  XSharedPreferences xsp;
  Class Dex;
  Method Dex_getBytes;
  Method getDex;
  String packagename;

  @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam)
      throws Throwable {
    xsp = new XSharedPreferences("com.ppma.appinfo", "User");
    xsp.makeWorldReadable();
    xsp.reload();
    initReflect();
    packagename = xsp.getString("packagename", null);
    log("设定包名：" + packagename);
    if ((!lpparam.packageName.equals(packagename)) || packagename == null) {
      log("当前程序包名与设定不一致或者包名为空");
      return;
    }
    log("目标包名：" + lpparam.packageName);
    String str = "java.lang.ClassLoader";
    String str2 = "loadClass";

    XposedHelpers.findAndHookMethod(str, lpparam.classLoader, str2, String.class, Boolean.TYPE,
        new XC_MethodHook() {
          @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Class cls = (Class) param.getResult();
            if (cls == null) {
              return;
            }
            String name = cls.getName();
            log("当前类名：" + name);
            byte[] bArr =
                (byte[]) Dex_getBytes.invoke(getDex.invoke(cls, new Object[0]), new Object[0]);
            if (bArr == null) {
              log("数据为空：返回");
              return;
            }
            log("开始写数据");
            String dex_path =
                "/data/data/" + packagename + "/" + packagename + "_" + bArr.length + ".dex";
            log(dex_path);
            File file = new File(dex_path);
            if (file.exists()) {
              return;
            }
            writeByte(bArr, file.getAbsolutePath());
          }
        });
  }

  public void initReflect() {
    try {
      Dex = Class.forName("com.android.dex.Dex");
      Dex_getBytes = Dex.getDeclaredMethod("getBytes", new Class[0]);
      getDex = Class.forName("java.lang.Class").getDeclaredMethod("getDex", new Class[0]);
    } catch (Exception e) {
      e.printStackTrace();
      log("initReflect失败");
    }
  }

  public void writeByte(byte[] bArr, String str) {
    try {
      OutputStream outputStream = new FileOutputStream(str);
      outputStream.write(bArr);
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
      log("writeByte失败");
    }
  }
}
