package teotw.com.loadsdhtmldemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private static WebView worldView;

    protected static final int REQUEST_FILE_PERMISSION = 100;
    private String[] permLocation = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        worldView = findViewById(R.id.webview);

        loadWebView("http://nttw.eyeoftheworld.cn/Index/index/v/v1.0.76.html");
    }

    private void loadWebView(String url) {
        worldView.loadUrl(url);
        WebSettings webSettings = worldView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);//文件权限
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        //缓存相关
//        if(InternetUtil.checkInternet(context)){
        //有网络，则加载网络地址
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式LOAD_CACHE_ELSE_NETWORK
//        }else{
//            //无网络，则加载缓存路径
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
        webSettings.setDomStorageEnabled(true);//开启DOM storage API功能
        webSettings.setDatabaseEnabled(true);//开启database storeage API功能
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";//缓存路径
        webSettings.setDatabasePath(cacheDirPath);//设置数据库缓存路径
        webSettings.setAppCachePath(cacheDirPath);//设置AppCaches缓存路径
        webSettings.setAppCacheEnabled(true);//开启AppCaches功能

        worldView.setWebViewClient(new ViewClient());
        worldView.setWebChromeClient(new ChromeClient());

        worldView.addJavascriptInterface(new OnJsToAndroid(), "android");

    }

    private void loadGame(String url) {
        worldView.loadUrl(url);
    }

    private class ViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            webView.loadUrl("javascript:function loadinfoname(){document.getElementsByTagName('input')[0].value='1234';}loadinfoname();");
//            webView.loadUrl("javascript:function loadpassword(){document.getElementsByTagName('input')[1].value='1234';}loadpassword();");
//            worldView.loadUrl("javascript:receiveMobile ("+mData.getMobile()+")");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            if ("ios://backController".equals(url)) {
//                finish();
//            }
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
        }
    }

    private class OnJsToAndroid {
        @JavascriptInterface
        public void goLogin() {
            Toast.makeText(MainActivity.this, "file://" + Environment.getExternalStoragePublicDirectory("tanyitan/index.html").getPath(), Toast.LENGTH_SHORT).show();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getFilePermission();
                }
            });
        }

        @JavascriptInterface
        public void goFootprintView(String bid) {
        }

        @JavascriptInterface
        public void goShop() {

        }

        @JavascriptInterface
        public void buildInfo(int id) {
        }
    }

    //获取定位权限
    @AfterPermissionGranted(REQUEST_FILE_PERMISSION)
    private void getFilePermission() {
        if (hasFilePermission()) {
            loadGame("file://" + Environment.getExternalStoragePublicDirectory("tanyitan/index.html").getPath());
        } else {
            // app还没有使用的权限，调用该方法进行申请，同时给出了相应的说明文案，提高用户同意的可能性
            EasyPermissions.requestPermissions(MainActivity.this, "需要获存储权限",
                    REQUEST_FILE_PERMISSION, permLocation);
        }
    }

    private boolean hasFilePermission() {
        return EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

}
