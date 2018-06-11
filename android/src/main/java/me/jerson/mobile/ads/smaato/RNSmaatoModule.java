
package me.jerson.mobile.ads.smaato;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNSmaatoModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNSmaatoModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNSmaato";
  }
}