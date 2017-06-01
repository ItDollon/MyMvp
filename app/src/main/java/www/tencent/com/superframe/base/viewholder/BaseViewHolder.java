package www.tencent.com.superframe.base.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import www.tencent.com.superframe.R;
import www.tencent.com.superframe.utils.UIUtils;

/** 作者：王文彬 on 2017/5/31 10：33 邮箱：wwb199055@126.com */
public class BaseViewHolder {

  private int mPosition;
  private View mConvertView;
  private SparseArray<View> mViews;

  private BaseViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
    this.mPosition = position;
    this.mViews = new SparseArray<View>();
    mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
    mConvertView.setTag(this);
  }

  public static BaseViewHolder getViewHolder(
      Context context, View convertView, ViewGroup parent, int layoutId, int position) {
    if (convertView == null) {
      return new BaseViewHolder(context, parent, layoutId, position);
    } else {
      BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
      holder.mPosition = position;
      return holder;
    }
  }

  //通过控件的id获取对应的控件，如果没有则加入viewId
  public <V extends View> V getView(int viewId) {
    View view = mViews.get(viewId);
    if (view == null) {
      view = mConvertView.findViewById(viewId);
      mViews.put(viewId, view);
    }
    return (V) view;
  }

  public View getConvertView() {
    return mConvertView;
  }

  //为textView赋值
  public BaseViewHolder setText(int viewId, String text) {
    TextView textView = getView(viewId);
    textView.setText(text);
    return this;
  }

  //为imageView设置图片
  public BaseViewHolder setImageResource(int viewId, int resId) {
    ImageView view = getView(viewId);
    view.setImageResource(resId);
    return this;
  }

  //为imageView设置bitmap图片
  public BaseViewHolder setImageBitmap(int viewId, Bitmap bitMap) {
    ImageView view = getView(viewId);
    view.setImageBitmap(bitMap);
    return this;
  }

  //为imageView设置图片
  public BaseViewHolder setImageURL(int viewId, String url) {
    ImageView imageView = getView(viewId);
    Picasso.with(UIUtils.getContext())
        .load(url)
        //.resize(200, 200)//默认使用的单位是px
        //.centerCrop()
        .placeholder(R.mipmap.icon_refresh_bg)
        .error(R.mipmap.ic_empty)
        .tag(UIUtils.getContext())
        .into(imageView);
    return this;
  }
}
