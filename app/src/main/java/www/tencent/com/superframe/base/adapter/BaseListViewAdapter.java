package www.tencent.com.superframe.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import www.tencent.com.superframe.base.viewholder.BaseViewHolder;

public abstract class BaseListViewAdapter<T> extends BaseAdapter {

  protected Context mContext;
  protected List<T> mData;
  protected LayoutInflater mInflater;
  protected int mLayoutId;

  public BaseListViewAdapter(Context context, int itemLayoutId, List<T> data) {
    this.mContext = context;
    this.mData = data;
    this.mInflater = LayoutInflater.from(context);
    this.mLayoutId = itemLayoutId;
  }

  @Override
  public int getCount() {
    return mData == null ? 0 : mData.size();
  }

  public List<T> getData() {
    return mData;
  }

  public void setData(List<T> data) {
    this.mData = data;
  }

  @Override
  public T getItem(int position) {
    return mData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    BaseViewHolder holder = BaseViewHolder.getViewHolder(mContext, convertView, parent, mLayoutId, position);
    convert(holder, position, getItem(position));
    return holder.getConvertView();
  };

  /**
   * 根据listView成员的数量动态设置listView的高度
   *
   * @param listView
   */
  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }

    int totalHeight = 0; //用来记录内部行的总高度
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    //获得参数对象
    ViewGroup.LayoutParams params = listView.getLayoutParams();
    //内部元素的高度加上分割线的高度
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }

  /** @param holder 是getView中要用来返回的哪个holder，第二个参数是getItem(position)，是list<T>中的那个T，即每行的数据对象 */
  public abstract void convert(BaseViewHolder holder, int position, T t);
}
