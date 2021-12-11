package com.icafdev.huellitas.utils;

import android.app.Application;

public class LightDarkApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ThemeSetup.applyTheme(this);
  }

}
