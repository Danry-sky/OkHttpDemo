package danry_sky.top.okhttpdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import bean.DataBean;
import danry_sky.top.okhttpdemo.R;
import okhttp3.Call;

/**
 * @author : danry
 * @version : 1.0
 * @email : cdanry@163.com
 * @github : https://github.com/Danry-sky
 * @time : 2017/7/14
 * @desc :
 */
public class OkHttpListImgAdapter extends BaseAdapter {

    private final Context context;
    private final List<DataBean.ItemData> datas;

    public OkHttpListImgAdapter(Context context, List<DataBean.ItemData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_okhttp_list_img, null);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据位置获得数据
        DataBean.ItemData itemData = datas.get(position);
        viewHolder.tvName.setText(itemData.getMovieName());
        viewHolder.tvDesc.setText(itemData.getVideoTitle());


        //在列表中使用OKhttputils请求图片
        OkHttpUtils
                .get()//
                .url(itemData.getCoverImg())//
                .tag(this)//
                .build()//
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mTvShow.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse：complete");
                        viewHolder.ivIcon.setImageBitmap(bitmap);
                    }
                });

        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvDesc;
    }
}
