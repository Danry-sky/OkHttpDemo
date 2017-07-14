package danry_sky.top.okhttpdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author : danry
 * @version : 1.0
 * @email : cdanry@163.com
 * @github : https://github.com/Danry-sky
 * @time : 2017/7/12
 * @desc :
 */
public class SecondActivity extends Activity implements View.OnClickListener {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final String TAG = SecondActivity.class.getSimpleName();
    private static final int GET = 1;
    private static final int POST = 2;
    private Button mBtnGetShow, mBtnPostShow, mBtnMain, mBtnGetOkhttpUtils, mBtnOkHttpImgSlider, mBtnOkHttpListImg;
    private TextView mTvShow;
    private ProgressBar mProgressBar;

    OkHttpClient client = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET:
                    mTvShow.setText((String) msg.obj);
                    break;
                case POST:
                    mTvShow.setText((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    private void initView() {
        mBtnGetShow = (Button) findViewById(R.id.btn_get_show);
        mBtnPostShow = (Button) findViewById(R.id.btn_post_show);
        mTvShow = (TextView) findViewById(R.id.tv_show);
        mBtnMain = (Button) findViewById(R.id.btn_main);
        mBtnGetOkhttpUtils = (Button) findViewById(R.id.btn_get_okhttputils);
        mBtnOkHttpImgSlider = (Button) findViewById(R.id.btn_okhttp_imgslider);
        mBtnOkHttpListImg = (Button) findViewById(R.id.btn_okhttp_list_img);


        mBtnGetShow.setOnClickListener(this);
        mBtnPostShow.setOnClickListener(this);
        mBtnMain.setOnClickListener(this);
        mBtnGetOkhttpUtils.setOnClickListener(this);
        mBtnOkHttpImgSlider.setOnClickListener(this);
        mBtnOkHttpListImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_show:
                mTvShow.setText("");
                getDataFromGet();
                break;
            case R.id.btn_post_show:
                mTvShow.setText("");
                getDataFromPost();
                break;
            case R.id.btn_get_okhttputils:
                mTvShow.setText("");
                getDataByGetokhttputils();
                break;
            case R.id.btn_main:
                Intent intent = new Intent(SecondActivity.this, ImageSliderActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_okhttp_imgslider:
                Intent intent1 = new Intent(SecondActivity.this, OkHttpImageSliderActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_okhttp_list_img:
                Intent intent2 = new Intent(SecondActivity.this,OkHttpListImgActivity.class);
                startActivity(intent2);
        }
    }

    /**
     * OKhttp的Get和Post方法的原生写法
     */
    private void getDataFromGet() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = get("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
                    Log.e(TAG, result);
                    Message msg = Message.obtain();
                    msg.what = GET;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void getDataFromPost() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = post("http://api.m.mtime.cn/PageSubArea/TrailerList.api", "");
                    Log.e(TAG, result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    private String get(String url) throws IOException {//原生的写发法
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    String post(String url, String json) throws IOException {//原生的写法
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 使用okhttputils来进行Get请求数据
     */
    public void getDataByGetokhttputils() {
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .get()//
                .url(url)//
                .id(101)
                .build()//
                .execute(new MyStringCallback());

    }

    /**
     * 使用okhttputils来进行Post请求数据
     */
    public void getDataPostByOkhttpUtils(){
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .post()
                .url(url)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id) {
            setTitle("okHttp-utils");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            mTvShow.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            mTvShow.setText("onResponse:" + response);

            switch (id) {
                case 100:
                    Toast.makeText(SecondActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(SecondActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        // TODO: 2017/7/14 用于下载上传显示用的进度条未实现
        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
            mProgressBar.setProgress((int) (100 * progress));
        }

    }
}
