package danry_sky.top.okhttpdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bean.DataBean;
import okhttp3.Call;
import okhttp3.Request;
import utils.CacheUtils;


/**
 * @author : danry
 * @version : 1.0
 * @email : cdanry@163.com
 * @github : https://github.com/Danry-sky
 * @time : 2017/7/14
 * @desc :使用OKhttp获取图片列表数据
 */
public class OkHttpListImgActivity extends Activity {

    private static final String TAG = OkHttpListImgActivity.class.getSimpleName();
    private ListView mLvShowImg;
    private ProgressBar mPgBar;
    private TextView mTvNoData;
    private OkHttpListImgAdapter adapter;
    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_list_img);
        initView();
        getDtaFromNet();
    }

    private void initView() {
        mLvShowImg = (ListView) findViewById(R.id.lv_show_img);
        mPgBar = (ProgressBar) findViewById(R.id.pg);
        mTvNoData = (TextView) findViewById(R.id.tv_nodata);
    }

    private void getDtaFromNet() {
        url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        String saveJson = CacheUtils.getString(this, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
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
            mTvNoData.setVisibility(View.VISIBLE);
//            mTvShow.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            mTvNoData.setVisibility(View.GONE);
//            mTvShow.setText("onResponse:" + response);

            switch (id) {
                case 100:
                    Toast.makeText(OkHttpListImgActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(OkHttpListImgActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
            //解析数据和显示数据
            if (response != null) {
                //缓存数据
                CacheUtils.putString(OkHttpListImgActivity.this, url, response);
                processData(response);
            }
        }


        // TODO: 2017/7/14 用于下载上传显示用的进度条未实现
        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
        }

    }

    /**
     * 解析和显示数据
     *
     * @param json
     */
    private void processData(String json) {
        DataBean dataBean = parsedJson(json);
        List<DataBean.ItemData> datas = dataBean.getTrailers();
        if (datas != null && datas.size() > 0) {
            //有数据
            mTvNoData.setVisibility(View.GONE);
            //显示适配器
            adapter = new OkHttpListImgAdapter(OkHttpListImgActivity.this, datas);
            mLvShowImg.setAdapter(adapter);
        } else {
            //没有数据
            mTvNoData.setVisibility(View.VISIBLE);

        }
        mPgBar.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     *
     * @param response
     * @return
     */
    private DataBean parsedJson(String response) {
        DataBean dataBean = new DataBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray != null && jsonArray.length() > 0) {
                List<DataBean.ItemData> trailers = new ArrayList<>();
                dataBean.setTrailers(trailers);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);

                    if (jsonObjectItem != null) {

                        DataBean.ItemData mediaItem = new DataBean.ItemData();

                        String movieName = jsonObjectItem.optString("movieName");//name
                        mediaItem.setMovieName(movieName);

                        String videoTitle = jsonObjectItem.optString("videoTitle");//desc
                        mediaItem.setVideoTitle(videoTitle);

                        String imageUrl = jsonObjectItem.optString("coverImg");//imageUrl
                        mediaItem.setCoverImg(imageUrl);

                        String hightUrl = jsonObjectItem.optString("hightUrl");//data
                        mediaItem.setHightUrl(hightUrl);

                        //把数据添加到集合
                        trailers.add(mediaItem);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataBean;
    }
}
