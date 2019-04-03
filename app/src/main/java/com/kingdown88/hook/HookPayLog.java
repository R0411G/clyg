package com.kingdown88.hook;

/**
 * Created by wsig on 2019-04-03.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.json.JSONObject;

public class HookPayLog implements IXposedHookLoadPackage {
  private final static String TAG = "XposedPay-HookPayLog";
  private Bundle PushNoticeDisplayBundle = null;
  private Class<?> PushNoticeDisplayClazz;

  @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
    if (!lpparam.packageName.equals("com.eg.android.AlipayGphone")) {
      return;
    }
    Log.d(TAG, "Hook alipay begin......");
    XposedHelpers.findAndHookMethod("com.alipay.mobile.quinox.LauncherApplication",
        lpparam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
          @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Log.d(TAG, "Hook attachBaseContext successful......");
            ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();
            try {
              PushNoticeDisplayClazz =
                  classLoader.loadClass("com.alipay.mobile.rome.pushservice.integration.d");
              XposedHelpers.findAndHookConstructor(PushNoticeDisplayClazz, Context.class,
                  Bundle.class, new XC_MethodHook() {
                    @Override protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.afterHookedMethod(param);
                      Log.d(TAG, "Hook PushNoticeDispaly.d successful......");
                      PushNoticeDisplayBundle = (Bundle) param.args[1];
                      if (PushNoticeDisplayBundle == null) {return;}
                      LogPushNoticeDisplay("push_show_title");
                      LogPushNoticeDisplay("push_show_text");

                      JSONObject jsonObject_data =
                          new JSONObject(PushNoticeDisplayBundle.getString("push_msg_data"));

                      JSONObject paramsJsonObject = jsonObject_data.getJSONObject("params");
                      Log.d(TAG, "订单：" + paramsJsonObject.getString("tradeNO"));

                      JSONObject jsonObject_ext =
                          new JSONObject(PushNoticeDisplayBundle.getString("push_show_ext"));
                      Log.d(TAG, "支付金额：" + jsonObject_ext.getString("soundValue"));
                    }
                  });
            } catch (ClassNotFoundException e) {
              e.printStackTrace();
            }
          }
        });
  }

  private void LogPushNoticeDisplay(String PushExtConstants) {
    Log.d(TAG, PushNoticeDisplayBundle.getString(PushExtConstants));
  }

  private void findAndHookMethod(Class clazz, String methodName,
      Object... parameterTypesAndCallback) {
    XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
  }
}
