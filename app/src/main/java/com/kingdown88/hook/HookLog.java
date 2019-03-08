package com.kingdown88.hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wsig on 2019-01-21.
 */
public class HookLog {

  public static void log(Object str) {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMDD HH:mm:ss", Locale.CHINA);
    String text = "[" + df.format(new Date()) + "]:  " + str.toString();
    System.out.println(text);
    XposedBridge.log(text);
  }

  public static int copyFile(String fromFile, String toFile) {
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
        copySdFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
      }
    }
    return 0;
  }

  public static int copySdFile(String fromFile, String toFile) {
    try {
      InputStream fosfrom = new FileInputStream(fromFile);
      OutputStream fosto = new FileOutputStream(toFile);
      byte[] bt= new byte[1024];
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

  public static String getMethodSign(XC_MethodHook.MethodHookParam param) {
    try {
      StringBuilder methodSign = new StringBuilder();
      methodSign.append(Modifier.toString(param.method.getModifiers()) + " ");
      Object result = param.getResult();
      if (result == null) {
        methodSign.append("void ");
      } else {
        methodSign.append(result.getClass().getCanonicalName() + " ");
      }
      methodSign.append(
          param.method.getDeclaringClass().getCanonicalName() + "." + param.method.getName() + "(");
      for (int i = 0; i < param.args.length; i++) {
        //这里有一个问题：如果方法的参数值为null,那么这里就会报错! 得想个办法如何获取到参数类型？
        if (param.args[i] == null) {
          methodSign.append("?");
        } else {
          methodSign.append(param.args[i].getClass().getCanonicalName());
        }
        if (i < param.args.length - 1) {
          methodSign.append(",");
        }
      }
      methodSign.append(")");
      return methodSign.toString();
    } catch (Exception e) {
    return null;
    }
  }

  public static String getMethodSign(Method method) {
    try {
      //如果这个方法是继承父类的方法，也需要做过滤
      String methodClass = method.getDeclaringClass().getCanonicalName();
      if (methodClass.startsWith("android.") || methodClass.startsWith("java.")) {
        return null;
      }
      StringBuilder methodSign = new StringBuilder();
      methodSign.append(Modifier.toString(method.getModifiers()) + " \n");

      Class<?> returnTypes = method.getReturnType();
      methodSign.append(returnTypes.getCanonicalName() + " \n");

      methodSign.append(methodClass + "." + method.getName() + "(");

      Class<?>[] paramTypes = method.getParameterTypes();
      for (int i = 0; i < paramTypes.length; i++) {
        methodSign.append(paramTypes[i].getCanonicalName());
        if (i < paramTypes.length - 1) {
          methodSign.append(",");
        }
      }
      methodSign.append(")");
      return methodSign.toString();
    } catch (Exception e) {
      return null;
    }
  }
}
