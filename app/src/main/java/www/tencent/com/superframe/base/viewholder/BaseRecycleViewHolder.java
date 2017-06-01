package www.tencent.com.superframe.base.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import www.tencent.com.superframe.base.adapter.BaseRecycleViewAdapter;

/** 作者：王文彬 on 2017/5/31 14：07 邮箱：wwb199055@126.com */
public class BaseRecycleViewHolder extends RecyclerView.ViewHolder {

  private final SparseArray<View> mViews;
  private Context mContext;

  public BaseRecycleViewHolder(View itemView, Context context) {
    super(itemView);
    mViews = new SparseArray<>();
    mContext = context;
  }

  public <V extends View> V getView(int id) {
    View view = mViews.get(id);
    if (view == null) {
      view = itemView.findViewById(id);
      mViews.put(id, view);
    }
    return (V) view;
  }

  public BaseRecycleViewHolder setText(int viewId, CharSequence value) {
    TextView view = getView(viewId);
    view.setText(value);
    return this;
  }

  public BaseRecycleViewHolder setImageURI(int viewId, Uri uri) {
    ImageView view = getView(viewId);
    view.setImageURI(uri);
    return this;
  }

  public BaseRecycleViewHolder setTextColor(int viewId, int textColor) {
    TextView view = getView(viewId);
    view.setTextColor(textColor);
    return this;
  }

  public BaseRecycleViewHolder setTextColorRes(int viewId, int textColorRes) {
    TextView view = getView(viewId);
    view.setTextColor(mContext.getResources().getColor(textColorRes));
    return this;
  }

  public BaseRecycleViewHolder setImageResource(int viewId, int imageResId) {
    ImageView view = getView(viewId);
    view.setImageResource(imageResId);
    return this;
  }

  public BaseRecycleViewHolder setBackgroundColor(int viewId, int color) {
    View view = getView(viewId);
    view.setBackgroundColor(color);
    return this;
  }

  public BaseRecycleViewHolder setBackgroundResource(int viewId, int backgroundRes) {
    View view = getView(viewId);
    view.setBackgroundResource(backgroundRes);
    return this;
  }

  public BaseRecycleViewHolder setVisible(int viewId, boolean visible) {
    View view = getView(viewId);
    view.setVisibility(visible ? View.VISIBLE : View.GONE);
    return this;
  }

  public BaseRecycleViewHolder setTypeface(Typeface typeface, int... viewIds) {
    for (int viewId : viewIds) {
      TextView view = getView(viewId);
      view.setTypeface(typeface);
      view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
    return this;
  }

  public BaseRecycleViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
    View view = getView(viewId);
    view.setOnClickListener(listener);
    return this;
  }

  public BaseRecycleViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
    View view = getView(viewId);
    view.setOnTouchListener(listener);
    return this;
  }

  public BaseRecycleViewHolder setOnLongClickListener(
      int viewId, View.OnLongClickListener listener) {
    View view = getView(viewId);
    view.setOnLongClickListener(listener);
    return this;
  }

  public BaseRecycleViewHolder setOnClickListener(
      int viewId, BaseRecycleViewAdapter.OnItemChildClickListener listener) {
    View view = getView(viewId);
    listener.mViewHolder = this;
    view.setOnClickListener(listener);
    return this;
  }

  public BaseRecycleViewHolder setOnLongClickListener(
      int viewId, BaseRecycleViewAdapter.OnItemChildLongClickListener listener) {
    View view = getView(viewId);
    listener.mViewHolder = this;
    view.setOnLongClickListener(listener);
    return this;
  }

  public BaseRecycleViewHolder setTag(int viewId, Object tag) {
    View view = getView(viewId);
    view.setTag(tag);
    return this;
  }
}
