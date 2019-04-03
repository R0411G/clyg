package com.kingdown88.hook;

import android.app.Application;
import android.content.Context;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.kingdown88.hook.HookLog.log;

public class wb implements IXposedHookLoadPackage {

  private static final String FILTER_PKGNAME = "com.wuba";

  public static String bytesToHexString(byte[] bArr) {
    StringBuilder stringBuilder = new StringBuilder("");
    if (bArr == null || bArr.length <= 0) {
      return null;
    }
    for (byte b : bArr) {
      String toHexString = Integer.toHexString(b & 255);
      if (toHexString.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(toHexString);
    }
    return stringBuilder.toString();
  }

  public static String byteToHexString(byte[] bytes) {
    StringBuffer sb = new StringBuffer(bytes.length);
    String sTemp;
    for (int i = 0; i < bytes.length; i++) {
      sTemp = Integer.toHexString(0xFF & bytes[i]);
      if (sTemp.length() < 2) {
        sb.append(0);
      }
      sb.append(sTemp.toUpperCase());
    }
    return sb.toString();
  }

  public static byte[] parseHexStr2Byte(String str) {
    if (str.length() < 1) {
      return null;
    }
    byte[] bArr = new byte[(str.length() / 2)];
    for (int i = 0; i < str.length() / 2; i++) {
      bArr[i] =
          (byte) ((Integer.parseInt(str.substring(i * 2, (i * 2) + 1), 16) * 16) + Integer.parseInt(
              str.substring((i * 2) + 1, (i * 2) + 2), 16));
    }
    return bArr;
  }

  @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
    String pkgname = loadPackageParam.packageName;
    log("pkgname-->" + pkgname);
    if (FILTER_PKGNAME.equals(pkgname)) {
      XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
          new XC_MethodHook() {
            @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              Context context = (Context) param.args[0];
              ClassLoader classLoader = context.getClassLoader();

              Class<?> cStringUtils =
                  XposedHelpers.findClass("com.wuba.commons.utils.StringUtils", classLoader);

              Class<?> StringUtils = classLoader.loadClass("com.wuba.commons.utils.StringUtils");
              final String methodName = "getStr";
              XposedHelpers.findAndHookMethod(StringUtils, methodName, String.class,
                  new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.beforeHookedMethod(param);
                    }

                    @Override protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.afterHookedMethod(param);
                      log("afterHookedMethod args  =" + param.args[0]);
                      log("afterHookedMethod result=" + param.getResult());
                    }
                  });

              XposedHelpers.findAndHookMethod(StringUtils, methodName, String.class, int.class,
                  new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.beforeHookedMethod(param);
                    }

                    @Override protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                      super.afterHookedMethod(param);
                      log("afterHookedMethod args  =" + param.args[0]);
                      log("afterHookedMethod args  =" + param.args[1]);
                      log("afterHookedMethod result=" + param.getResult());
                    }
                  });

              //getSecretPhone

              //String phone = null;
              //Class<?> Exec = classLoader.loadClass("com.wuba.aes.Exec");
              //Method method = XposedHelpers.findMethodBestMatch(Exec, "encryptPhoneData", byte[].class,
              //    int.class);
              //String[] jiamis = { "13581830665", "13718031586" };
              //for (int i = 0; i < jiamis.length; i++) {
              //  String str = jiamis[i];
              //  log("第" + i + "次 加密开始=" + str);
              //  byte[] bytes = str.getBytes();
              //  try {
              //    byte[] decryptPhoneData =
              //        (byte[]) method.invoke(Exec.newInstance(), bytes, bytes.length);
              //    log("第" + i + "次 加密= decryptPhoneData " + decryptPhoneData.length);
              //    phone = bytesToHexString(decryptPhoneData);
              //  } catch (Exception e) {
              //    e.printStackTrace();
              //  }
              //  log("第" + i + "次 加密结束=" + phone);
              //}

              ////解密
              //String phone = null;
              //Class<?> AESUtil = classLoader.loadClass("com.wuba.aes.AESUtil");
              //String[] jiemis =
              //    { "2903E3649F622D51A7E96F8816FD58D2", "BADD6950287559EEC37D6E0D4E9B76E7" };
              //log("数量" + jiemis.length);
              //for (int j = 0; j < jiemis.length; j++) {
              //  int len = 11;
              //  String str = jiemis[j];
              //  log("第" + j + "次 解密开始=" + str);
              //  byte[] parseHexStr2Byte = parseHexStr2Byte(str);
              //  try {
              //    if (parseHexStr2Byte != null) {
              //      byte[] decrypt_decrypt =
              //          (byte[]) XposedHelpers.callMethod(AESUtil.newInstance(), "decrypt",
              //              parseHexStr2Byte, parseHexStr2Byte.length);
              //      byte[] trip_decrypt =
              //          (byte[]) XposedHelpers.callMethod(AESUtil.newInstance(), "trip",
              //              decrypt_decrypt, len);
              //      phone = new String(trip_decrypt, "utf-8");
              //    }
              //  } catch (Exception e) {
              //    e.printStackTrace();
              //  }
              //  log("第" + j + "次 解密结束=" + phone);
              //}
            }
          });
    }
  }
}
