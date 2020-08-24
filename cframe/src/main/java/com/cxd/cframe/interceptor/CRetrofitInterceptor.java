package com.cxd.cframe.interceptor;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;


/**
 * 自定义的OkHttp3信息拦截类
 */
public class CRetrofitInterceptor implements Interceptor {
    private final String TAG = "OkHttp";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final int finalLineLen ;

    public CRetrofitInterceptor() {
        finalLineLen = 90 ;
    }

    public CRetrofitInterceptor(int lineLen) {
        finalLineLen = lineLen ;
    }

    @Override
    public  synchronized Response intercept(Chain chain) throws IOException {

        /*文件下载不过滤*/
        if(chain.request().url().toString().endsWith("downloadApk")
                || chain.request().url().toString().endsWith("upload")){
            return chain.proceed(chain.request());
        }


        long t1 = System.currentTimeMillis();//请求发起的时间
        Request request = chain.request();

        //获取request中的参数信息
        String bufferString = null ;
        Buffer buffer = null ;
        if(request != null && request.body() != null){
            buffer = new Buffer();
            request.body().writeTo(buffer);
            bufferString = buffer.readString(UTF8);
        }

        Log.i(TAG, "  ");
        showHornLog("start",true);
        showContentLog(" ");
        showTitleLog("Request:");
        showContentLog(request.method());
        showContentLog(request.url().toString());
        showContentLog(bufferString); //只有POST才有的参数


        /**
         * 发送request
         */
        Response response = chain.proceed(request);
        long t2 = System.currentTimeMillis(); //收到响应的时间

        //获取responset中的返回结果信息
        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE);
        buffer = source.buffer();
        bufferString = buffer.clone().readString(UTF8); //必须用clone（）以防流被读完，response不会传递

        showContentLog((t2-t1)+" ms");
        showContentLog(" ");
        showTitleLog("Response:");
        showContentLog(bufferString);
        showContentLog(" ");
        showHornLog(" end ",false);

        return response;
    }

    /**
     * content
     * @param content
     */
    private void showContentLog(String content){
        if(content == null){return ;}
        final int tabLen = 4 + 1 ;
        final String vLineStr = "|";
        final String tabStr = "    ";

        StringBuilder sb = null;
        while(content != null && length(content)>0){
            sb = new StringBuilder();
            sb.append(vLineStr);
            sb.append(tabStr);
            if(length(content) < finalLineLen - tabLen * 2){
                sb.append(content);
                for (int i = 0; i < finalLineLen - tabLen * 2 - length(content); i++) {
                    sb.append(" ");
                }
                content = null;
            }else{
                sb.append(content.substring(0,finalLineLen - tabLen * 2));
                content = content.substring(finalLineLen - tabLen * 2);
            }

            sb.append(tabStr);
            sb.append(vLineStr);
//            sb.append("\n");
            Log.i(TAG, sb.toString());
        }

    }

    /**
     * Request or Response
     * @param title
     */
    private void showTitleLog(String title){
        if(title == null)return;
        StringBuilder sb = new StringBuilder();
        final int tabLen = 4 + 1 ;
        final String vLineStr = "|";
        final String tabStr = "    ";
        sb.append(vLineStr);
        sb.append(title);
        for (int i = 0; i < finalLineLen - 1 - length(title) - tabLen; i++) {
            sb.append(" ");
        }
        sb.append(tabStr);
        sb.append(vLineStr);
        Log.i(TAG, sb.toString());
    }

    /**
     *
     * @param hornName start or end
     * @param isTop true-start false-end
     */
    private void showHornLog(String hornName , boolean isTop){
        if(hornName == null)return;
        StringBuilder sb = null;

        if(isTop){
            //第一条线
            sb = new StringBuilder();
            for (int i = 0; i < finalLineLen; i++) {
                sb.append("-");
            }
            Log.i(TAG, sb.toString());
            //两个角标
            sb = new StringBuilder();
            final String hornStr = "| "+hornName+" |";
            sb.append(hornStr);
            for (int i = 0; i < finalLineLen - length(hornStr) * 2; i++) {
                sb.append(" ");
            }
            sb.append(hornStr);
            Log.i(TAG, sb.toString());
            //角标下面的横线
            StringBuilder bsb = new StringBuilder() ;
            bsb.append("|");
            for (int i = 0; i < length(hornStr) -1; i++) {
                bsb.append("-");
            }
            for (int i = 0; i < finalLineLen - length(hornStr) * 2; i++) {
                bsb.append(" ");
            }
            for (int i = 0; i < length(hornStr) -1; i++) {
                bsb.append("-");
            }
            bsb.append("|");
            Log.i(TAG, bsb.toString());
        }else{
            final String hornStr = "| "+hornName+" |";
            //角标上面的横线
            StringBuilder bsb = new StringBuilder() ;
            bsb.append("|");
            for (int i = 0; i < length(hornStr) -1; i++) {
                bsb.append("-");
            }
            for (int i = 0; i < finalLineLen - length(hornStr) * 2; i++) {
                bsb.append(" ");
            }
            for (int i = 0; i < length(hornStr) -1; i++) {
                bsb.append("-");
            }
            bsb.append("|");
            Log.i(TAG, bsb.toString());

            //两个角标
            sb = new StringBuilder();
            sb.append(hornStr);
            for (int i = 0; i < finalLineLen - length(hornStr) * 2; i++) {
                sb.append(" ");
            }
            sb.append(hornStr);
            Log.i(TAG, sb.toString());
            //第三条线
            sb = new StringBuilder();
            for (int i = 0; i < finalLineLen; i++) {
                sb.append("-");
            }
            Log.i(TAG, sb.toString());
        }

    }


    private int length(String value) {
        return value.length();
    }
}