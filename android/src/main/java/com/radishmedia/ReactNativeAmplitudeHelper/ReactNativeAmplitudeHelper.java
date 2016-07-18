package com.radishfiction.ReactNativeAmplitudeHelper;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Revenue;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ReactNativeAmplitudeHelper extends ReactContextBaseJavaModule {

  private Activity activity = null;
  private Application application = null;

  public ReactNativeAmplitudeHelper(ReactApplicationContext reactContext, Activity activity, Application application) {
    super(reactContext);
    this.activity = activity;
    this.application = application;
  }

  @Override
  public String getName() {
    return "ReactNativeAmplitudeHelper";
  }

  @ReactMethod
  public void setup(String writeKey) {
    Amplitude.getInstance().initialize(this.activity, apiKey).enableForegroundTracking(this.application);
  }

  @ReactMethod
  public void identifyWithTraits(String userId, ReadableMap traits) {
    Amplitude.getInstance().setUserId(userId);
    try {
      JSONObject props = convertReadableToJsonObject(properties);
      Amplitude.getInstance().setUserProperties(props);
    } catch (JSONException exception) {
      return;
    }
  }

  @ReactMethod
  public void identify(String userId) {
    Amplitude.getInstance().setUserId(userId);
  }

  @ReactMethod
  public void screenWithProps(String name, ReadableMap properties) {
    try {
      StringBuilder screen = new StringBuilder("Viewed ");
      screen.append(name);
      screen.append(" screen");
      JSONObject props = convertReadableToJsonObject(properties);
      Amplitude.getInstance().logEvent(screen, props);
    } catch (JSONException exception) {
      return;
    }
  }

  @ReactMethod
  public void screen(String name) {
    StringBuilder screen = new StringBuilder("Viewed ");
    screen.append(name);
    screen.append(" screen");
    Amplitude.getInstance().logEvent(screen);
  }

  @ReactMethod
  public void trackWithProps(String event, ReadableMap properties) {
    try {
      JSONObject props = convertReadableToJsonObject(properties);
      Amplitude.getInstance().logEvent(event, props);
    } catch (JSONException exception) {
      return;
    }
  }

  @ReactMethod
  public void track(String event) {
    Amplitude.getInstance().logEvent(event);
  }

  @ReactMethod
  public void trackPurchase(String productIdentifier, double price, int quantity, String receipt) {
    Revenue revenue = new Revenue().setProductId(productIdentifier).setPrice(price).setQuantity(quantity).setReceipt(receipt);
    Amplitude.getInstance().logRevenueV2(revenue);
  }

  public static JSONObject convertReadableToJsonObject(ReadableMap map) throws JSONException{
    JSONObject json = new JSONObject();
    ReadableMapKeySetIterator it = map.keySetIterator();
    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      switch (type) {
        case Map:
            json.put(key, convertReadableToJsonObject(map.getMap(key)));
            break;
        case String:
            json.put(key, map.getString(key));
            break;
        case Number:
            json.put(key, map.getDouble(key));
            break;
        case Boolean:
            json.put(key, map.getBoolean(key));
            break;
        case Null:
            json.put(key, null);
            break;
      }
    }
    return json;
  }
}
