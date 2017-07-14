package danry_sky.top.okhttpdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.ArrayList;
import java.util.List;

import bean.BannerInfo;
import okhttp3.Call;

/**
 * @author : danry
 * @version : 1.0
 * @email : cdanry@163.com
 * @github : https://github.com/Danry-sky
 * @time : 2017/7/13
 * @desc :数据写死的，实现图片轮播
 */
public class ImageSliderActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mSlider;
    private PagerIndicator indicator;
    private Button mBtnImgShow;
    private ImageView mImgShow;
    private TextView mTvShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageslider);
        initView();
        getBannerData();
        initSlider();
    }

    private void initView() {
        mSlider = (SliderLayout) findViewById(R.id.home_slider_ad);
        indicator = (PagerIndicator) findViewById(R.id.home_indicator_ad);
        mImgShow = (ImageView) findViewById(R.id.img_show);
        mBtnImgShow = (Button) findViewById(R.id.btn_imgshow);
        mTvShow = (TextView) findViewById(R.id.tv_show);
        initOnclick();
    }

    private void initOnclick() {
        mBtnImgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }

    List<BannerInfo> listBanner = new ArrayList<>();

    private void getBannerData() {
        BannerInfo bannerInfo_01 = new BannerInfo();
        bannerInfo_01.setName("音箱狂欢");
        bannerInfo_01.setImgUrl("http://7mno4h.com2.z0.glb.qiniucdn.com/5608f3b5Nc8d90151.jpg");
        BannerInfo bannerInfo_02 = new BannerInfo();
        bannerInfo_02.setName("手机国庆礼");
        bannerInfo_02.setImgUrl("http://7mno4h.com2.z0.glb.qiniucdn.com/5608eb8cN9b9a0a39.jpg");
        BannerInfo bannerInfo_03 = new BannerInfo();
        bannerInfo_03.setName("IT生活");
        bannerInfo_03.setImgUrl("http://7mno4h.com2.z0.glb.qiniucdn.com/5608cae6Nbb1a39f9.jpg");
        listBanner.add(bannerInfo_01);
        listBanner.add(bannerInfo_02);
        listBanner.add(bannerInfo_03);
    }

    private void initSlider() {
        if (listBanner != null) {
            for (BannerInfo bannerInfo : listBanner) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView.image(bannerInfo.getImgUrl())
                        .description(bannerInfo.getName())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(ImageSliderActivity.this);
                mSlider.addSlider(textSliderView);
            }
        }

        mSlider.setCustomIndicator(indicator);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSlider.setDuration(3000);
        mSlider.addOnPageChangeListener(ImageSliderActivity.this);
    }

    //请求一张图片
    public void getImage() {
        mTvShow.setText("");
        String url = "http://images.csdn.net/20150817/1.jpg";
        OkHttpUtils
                .get()//
                .url(url)//
                .tag(this)//
                .build()//
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mTvShow.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse：complete");
                        mImgShow.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle()
        // on the slider before activity or fragment is destroyed
        mSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
