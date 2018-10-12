package com.kingdown88.wb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() {
    assertEquals(4, 2 + 2);
    try {
      ex();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  OkHttpClient client = new OkHttpClient();

  MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  String get(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    Response response = client.newCall(request).execute();
    System.out.println(response.toString());
    return response.body().string();
  }

  String post(String url, String json) throws IOException {
    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder().url(url).post(body).build();
    Response response = client.newCall(request).execute();
    return response.body().string();
  }

  static String str1 = "";

  public static void ex() {

    str1 = "adb connect  127.0.0.1:62001";
    execCmd(str1);
    str1 = "adb connect  127.0.0.1:26944";
    execCmd(str1);
    str1 = "adb connect  127.0.0.1:21503";
    execCmd(str1);
    //str1 = "adb_server connect 127.0.0.1:7555";
    //execCmd(str1);

    str1 = "adb shell getprop ro.product.model";
    execCmd(str1);
    str1 = "adb shell getprop ro.build.version.release";
    execCmd(str1);
    str1 = "adb shell getprop ro.product.name";
    execCmd(str1);

    System.out.println("finish...");
  }

  public static void execCmd(String str1) {
    try {
      Runtime runtime = Runtime.getRuntime();
      Process po = runtime.exec(str1);
      BufferedReader br = new BufferedReader(new InputStreamReader(po.getInputStream()));
      String line;
      StringBuffer stringBuffer = new StringBuffer();
      while ((line = br.readLine()) != null) {
        stringBuffer.append(line);
      }
      System.out.println(stringBuffer.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}