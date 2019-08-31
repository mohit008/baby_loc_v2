package baby.watching.weather;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import baby.watching.model.open_weather.Root;
import baby.watching.model.open_weather.Weather;
import cz.msebera.android.httpclient.Header;

public class WeatherMain {
    final static String key = "http://api.openweathermap.org/data/2.5/weather" +
            "?q=Indore,In&appid=d2c6ce810fb5289779ceca926effbb39&units=metric";

    WebView wvTempIcon;
    Activity activity;
    TextView tvTemp,tvTempText;
    LinearLayout llWeather;
    static AsyncHttpClient client;

    public WeatherMain(Activity activity, WebView wvTempIcon
            , TextView tvTemp,LinearLayout llWeather,
                       TextView tvTempText) {
        this.wvTempIcon = wvTempIcon;
        this.activity = activity;
        this.tvTemp = tvTemp;
        this.tvTempText = tvTempText;
        this.llWeather = llWeather;
        createWebView();
        RequestParams params = new RequestParams();
        getPost(key, params, 100);
    }

    /**
     * send data to post
     *
     * @param params
     * @param requestCode
     */
    public void getPost(String url, RequestParams params, final int requestCode) {
        client = new AsyncHttpClient();
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody!=null){
                    Log.i("responseBody", new String(responseBody));
                    get(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {
                if(responseBody!=null){
                    Log.i("responseBody", new String(responseBody));
                }
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onRetry(int retryNo) {
                Log.i("onRetry", retryNo + "");
            }

        };
        post(url, params, asyncHttpResponseHandler);
    }

    public void get(String response) {
        try{
            Gson gson = new GsonBuilder().create();
            Root root = gson.fromJson(response, Root.class);
            String des = "";
            for (Weather weathers : root.getWeather()) {
                des = weathers.getIcon();
            }
            setWeb(des);
            tvTemp.setText(root.getMain().getTemp().toString()+"° C");
            tvTempText.setText(root.getWeather().get(0)
                    .getDescription().toUpperCase().replace(" ", "\n"));
            llWeather.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setWeb(String string) {
        if (string.contains("3") || string.contains("4") ||
                string.contains("50")) {
            wvTempIcon.loadUrl("file:///android_asset/cloudy_3_4_50.html");
            return;
        }
        if (string.contains("2")) {
            wvTempIcon.loadUrl("file:///android_asset/light_rain_2.html");
            return;
        }
        if (string.contains("9") || string.contains("10")) {
            wvTempIcon.loadUrl("file:///android_asset/rain_9_10.html");
            return;
        }
        if (string.contains("13")) {
            wvTempIcon.loadUrl("file:///android_asset/snow_13.html");
            return;
        }
        if (string.contains("11")) {
            wvTempIcon.loadUrl("file:///android_asset/thunder_11.html");
            return;
        }
        if (string.contains("1")) {
            wvTempIcon.loadUrl("file:///android_asset/sunny_1.html");
            return;
        }

    }

    /**
     * create post hit
     *
     * @param params
     * @param responseHandler
     */
    public static void post(String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private void createWebView() {
        setUpWebViewDefaults(wvTempIcon);
        wvTempIcon.setWebChromeClient(new WebChromeClient());
    }

    private void setUpWebViewDefaults(WebView webView) {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.clearCache(true);
        webView.clearHistory();
        webView.setWebViewClient(new Callback());
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }
}
