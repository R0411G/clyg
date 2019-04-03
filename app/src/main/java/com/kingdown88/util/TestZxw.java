package com.kingdown88.util;

import com.kingdown88.hook.HttpRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wsig on 2019-01-21.
 */
public class TestZxw {

  public static void main(String[] args) {
    String sr = "";
    //sr = HttpRequest.sendPost("http://appserv.51zxw.net/course.asp", "courseId=698&app_Ver=G1.6.6");
    StringBuilder sb = new StringBuilder();
    //1-000
    String userId = "171000";
    userId = "174178";
    userId = "172102";
    sb.append(userId);
    String itemId = "74110";
    sb.append(itemId);
    sb.append("G1.6.6");
    sb.append("video88");
    String checkcode = MD532H(sb.toString()).toUpperCase();
    System.out.println(checkcode);
    sr = HttpRequest.sendPost("http://appserv.51zxw.net/video.asp", "itemId="
        + itemId
        + "&netId=0&userId="
        + userId
        + "&reType=0&itemFz=0&checkcode="
        + checkcode
        + "&app_Ver=G1.6.6");
    System.out.println(sr);
  }

  /**
   * 1-256 1-16 1-0
   */
  public static String MD532H(String str) {
    String str2 = new String();
    try {
      MessageDigest instance = MessageDigest.getInstance("MD5");
      instance.update(str.getBytes());
      byte[] digest = instance.digest();
      StringBuffer stringBuffer = new StringBuffer("");
      for (int i2 : digest) {
        if (i2 < 0) {
          i2 += 1;
        }
        if (i2 < 1) {
          stringBuffer.append("0");
        }
        stringBuffer.append(Integer.toHexString(i2));
      }
      return stringBuffer.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return str2;
    }
  }
}
