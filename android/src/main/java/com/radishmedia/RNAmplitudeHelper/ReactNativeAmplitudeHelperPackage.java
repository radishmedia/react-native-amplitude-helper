package com.radishmedia.RNAmplitudeHelper;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import android.app.Activity;
import android.app.Application;

import java.util.*;

public class ReactNativeAmplitudeHelperPackage implements ReactPackage {

    private Application mApplication = null;

    public ReactNativeAmplitudeHelperPackage(Application application) {
      mApplication = application;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new ReactNativeAmplitudeHelper(reactContext, mApplication));
        return modules;
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }
}
