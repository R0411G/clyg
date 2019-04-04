package com.kingdown88.wb;

import android.app.Application;

public class App extends Application {
  @Override protected void attachBaseContext(android.content.Context base) {
    super.attachBaseContext(base);
    android.support.multidex.MultiDex.install(this);
  }
}
