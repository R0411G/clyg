package com.kingdown88.zf;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wsig on 2019-04-03.
 */
public class HookAlipay implements IXposedHookLoadPackage {
  @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam)
      throws Throwable {
    if (lpparam.packageName.equals("com.eg.android.AlipayGphone")) {
      XposedBridge.log("HookAlipay load alipay");
      ClassLoader classLoader = lpparam.classLoader;
      Class<?> aClass =
          classLoader.loadClass("com.alipay.android.render.engine.viewbiz.AssetsHeaderV2View");
      Class<?> aClass2 =
          classLoader.loadClass("com.alipay.android.render.engine.model.AssetsCardModel");
      if (aClass != null) {
        XposedHelpers.findAndHookMethod(aClass, "setData", aClass2, boolean.class, boolean.class,
            boolean.class, new XC_MethodHook() {
              @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object arg = param.args[0];
                try {
                  XposedBridge.log("HookAlipay   " + arg.getClass()
                      .getField("latestTotalView")
                      .get(arg)
                      .toString());
                  arg.getClass().getField("latestTotalView").set(arg, "1000000.00");
                } catch (IllegalAccessException e) {
                  e.printStackTrace();
                } catch (IllegalArgumentException e) {
                  e.printStackTrace();
                } catch (NoSuchFieldException e) {
                  e.printStackTrace();
                }
              }

              @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
              }
            });
      }
    }
  }
}
