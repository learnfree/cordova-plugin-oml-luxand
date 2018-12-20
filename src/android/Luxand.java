
package com.luxand.dsi;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.luxand.FSDK;

import java.util.HashMap;
import java.util.Map;

public class Luxand extends CordovaPlugin {
    private static final int LOGIN_CODE = 3;
    private String dbName;
    private CallbackContext callbackContext;
    private static final int IDENTIFY_CODE = 2;
    private static int loginTryCount;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        Log.e("com.luxand.dsi-------", action+":"+data.toString());
        this.callbackContext = callbackContext;
        Context context = cordova.getActivity().getApplicationContext();
        if (action.equals("init")) {
            String licence = (String)data.get(0);
            dbName = (String) data.get(1);
            loginTryCount = (int) data.get(2);
            Log.e("com.luxand.dsi", licence);
            int res = FSDK.ActivateLibrary(licence);
            if (res != FSDK.FSDKE_OK) {
                callbackContext.error("FaceSDK activation failed"+res);
            } else {
                FSDK.Initialize();
                callbackContext.success("Initialized successfully");
            }
            //callbackContext.success(message);
            return true;
        } else if(action.equals("register")) {
            cordova.setActivityResultCallback (this);
            keepCallback(callbackContext);
            openNewActivity(context, IDENTIFY_CODE);
            return true;
        }else if(action.equals("login")) {
            cordova.setActivityResultCallback (this);
            keepCallback(callbackContext);
            openNewActivity(context, LOGIN_CODE);
            return true;
        }

        else {
            return false;

        }
    }
    private void keepCallback(CallbackContext callbackContext) {
        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);
    }
     private void openNewActivity(Context context, int REQUEST_CODE) {
        Intent intent = new Intent(context, OMLLuxand.class);
        intent.putExtra("DB_NAME", dbName);
        intent.putExtra("LOGIN_TRY_COUNT", loginTryCount);
        if(REQUEST_CODE == LOGIN_CODE) {
            intent.putExtra("TYPE", "FOR_LOGIN");
        }else if(REQUEST_CODE == IDENTIFY_CODE) {
            intent.putExtra("TYPE", "FOR_REGISTER");
        }
         Log.e("com.luxand.dsi::", ""+intent.getExtras());
        cordova.startActivityForResult(this, intent, REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if(data==null) return;
        CallbackContext callback = this.callbackContext;
        Log.e("com.luxand.dsi::", requestCode+":"+resultCode);
        if( requestCode == LOGIN_CODE)
        {
            if( resultCode == Activity.RESULT_OK && data.hasExtra("result") )
            {
                JSONObject res = new JSONObject();
                Log.e("com.luxand.dsi::", ""+data.getExtras());
                boolean error = data.getBooleanExtra("error", true);
                Log.e("com.luxand.dsi::", ""+error);
                try {
                    res.put("status", error? "FAIL" : "SUCCESS");
                    res.put("message", data.getStringExtra("result"));
                    PluginResult result = new PluginResult(PluginResult.Status.OK,res);
                    if(!error) {
                        result.setKeepCallback(true);
                        callback.sendPluginResult(result);
                    }else {
                        result.setKeepCallback(true);
                        callback.sendPluginResult(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    result.setKeepCallback(true);
                    callback.sendPluginResult(result);
                }
            }else {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Unable to identify user" );
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
        }else if(requestCode == IDENTIFY_CODE) {
            if( resultCode == Activity.RESULT_OK && data.hasExtra("result") )
            {
                JSONObject res = new JSONObject();
                Log.e("com.luxand.dsi::", ""+data.getExtras());
                boolean error = data.getBooleanExtra("error", true);
                Log.e("com.luxand.dsi::", ""+error);
                try {
                    res.put("status", error? "FAIL" : "SUCCESS");
                    res.put("message", data.getStringExtra("result"));
                    PluginResult result = new PluginResult(PluginResult.Status.OK,res);
                    if(!error) {
                        result.setKeepCallback(true);
                        callback.sendPluginResult(result);
                    }else {
                        result.setKeepCallback(true);
                        callback.sendPluginResult(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    result.setKeepCallback(true);
                    callback.sendPluginResult(result);
                }
            }else {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Unable to identify user" );
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
        }
    }
}