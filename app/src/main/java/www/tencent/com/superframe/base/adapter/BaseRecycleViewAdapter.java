package www.tencent.com.superframe.base.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.tencent.com.superframe.base.viewholder.BaseRecycleViewHolder;
import www.tencent.com.superframe.utils.AnimationTypeUtils;
import www.tencent.com.superframe.utils.AnimationUtil;

/** 作者：王文彬 on 2017/5/31 14：03 邮箱：wwb199055@126.com */
public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<BaseRecycleViewHolder> {
  private static final String TAG = "BaseRecycleViewAdapter";

  public static class VIEW_TYPE {
    public static final int HEADER = 0x0010;
    public static final int FOOTER = 0x0011;
  }

  public List<T> mData;
  private Context mContext;
  private LayoutInflater mInflater;
  private OnItemClickListener mOnItemClickListener;
  private OnItemLongClickListener mOnItemLongClickListener;
  private OnRecyclerViewItemChildClickListener mChildClickListener;
  private OnRecyclerViewItemChildLongClickListener mChildLongClickListener;
  private Map<Integer, Integer> layoutIdMap, viewTypeMap;
  private int mCurrentViewTypeValue = 0x0107;
  private AnimationTypeUtils mAnimationType;
  private int mAnimationDuration = 300;
  private boolean showItemAnimationEveryTime = false;
  private Interpolator mItemAnimationInterpolator;
  private CustomAnimator mCustomAnimator;
  private int mLastItemPosition = -1;
  private LinearLayout mHeaderLayout;
  private LinearLayout mFooterLayout;
  private LinearLayout mCopyHeaderLayout = null;
  private LinearLayout mCopyFooterLayout = null;

  public BaseRecycleViewAdapter(Context context) {
    this(context, null);
  }

  public BaseRecycleViewAdapter(Context context, List<T> data) {
    mData = null == data ? new ArrayList<T>() : data;
    layoutIdMap = new HashMap<>();
    viewTypeMap = new HashMap<>();
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    BaseRecycleViewHolder baseViewHolder;
    switch (viewType) {
      case VIEW_TYPE.HEADER: // header
        baseViewHolder = new BaseRecycleViewHolder(mHeaderLayout, mContext);
        break;
      case VIEW_TYPE.FOOTER: // footer
        baseViewHolder = new BaseRecycleViewHolder(mFooterLayout, mContext);
        break;
      default:
        baseViewHolder =
            new BaseRecycleViewHolder(
                mInflater.inflate(layoutIdMap.get(viewType), parent, false), mContext);
        initItemClickListener(baseViewHolder);
        break;
    }
    return baseViewHolder;
  }

  @Override
  public int getItemViewType(int position) {
    if (position < getHeaderViewCount()) {
      return VIEW_TYPE.HEADER;
    } else if (position >= mData.size() + getHeaderViewCount()) {
      return VIEW_TYPE.FOOTER;
    } else {
      int currentPosition = position - getHeaderViewCount();
      int currentLayoutId = getItemViewLayoutId(currentPosition, mData.get(currentPosition));
      if (!viewTypeMap.containsKey(currentLayoutId)) {
        mCurrentViewTypeValue++;
        viewTypeMap.put(currentLayoutId, mCurrentViewTypeValue);
        layoutIdMap.put(viewTypeMap.get(currentLayoutId), currentLayoutId);
      }
      return viewTypeMap.get(currentLayoutId);
    }
  }

  @Override
  public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
    switch (getItemViewType(position)) {
      case VIEW_TYPE.HEADER:
        // Do nothing
        break;
      case VIEW_TYPE.FOOTER:
        // Do nothing
        break;
      default:
        convert(holder, getItem(position - getHeaderViewCount()), position - getHeaderViewCount());
        addAnimation(holder);
        break;
    }
  }

  protected final void addAnimation(final BaseRecycleViewHolder holder) {
    int currentPosition = holder.getAdapterPosition();
    if (null != mCustomAnimator) {
      mCustomAnimator.getAnimator(holder.itemView).setDuration(mAnimationDuration).start();
    } else if (null != mAnimationType) {
      if (showItemAnimationEveryTime || currentPosition > mLastItemPosition) {
        new AnimationUtil()
            .setAnimationType(mAnimationType)
            .setTargetView(holder.itemView)
            .setDuration(mAnimationDuration)
            .setInterpolator(mItemAnimationInterpolator)
            .start();
        mLastItemPosition = currentPosition;
      }
    }
  }

  protected final void initItemClickListener(final BaseRecycleViewHolder holder) {
    if (null != mOnItemClickListener) {
      holder.itemView.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              final int position = holder.getAdapterPosition() - getHeaderViewCount();
              mOnItemClickListener.onItemClick(view, position);
            }
          });
    }

    if (null != mOnItemLongClickListener) {
      holder.itemView.setOnLongClickListener(
          new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
              final int position = holder.getAdapterPosition() - getHeaderViewCount();
              mOnItemLongClickListener.onItemLongClick(v, position);
              return true;
            }
          });
    }
  }

  protected abstract void convert(BaseRecycleViewHolder holder, T item, int position);

  protected abstract int getItemViewLayoutId(int position, T item);

  protected T getItem(int position) {
    return mData.get(position);
  }

  @Override
  public int getItemCount() {
    return mData.size() + getHeaderViewCount() + getFooterViewCount();
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
    mOnItemLongClickListener = onItemLongClickListener;
  }

  public void setOnItemChildClickListener(OnRecyclerViewItemChildClickListener childClickListener) {
    this.mChildClickListener = childClickListener;
  }

  public class OnItemChildClickListener implements View.OnClickListener {
    public RecyclerView.ViewHolder mViewHolder;

    @Override
    public void onClick(View v) {
      if (mChildClickListener != null)
        mChildClickListener.onItemChildClick(
            BaseRecycleViewAdapter.this, v, mViewHolder.getLayoutPosition() - getHeaderViewCount());
    }
  }

  public void setOnItemChildLongClickListener(
      OnRecyclerViewItemChildLongClickListener childLongClickListener) {
    this.mChildLongClickListener = childLongClickListener;
  }

  public class OnItemChildLongClickListener implements View.OnLongClickListener {
    public RecyclerView.ViewHolder mViewHolder;

    @Override
    public boolean onLongClick(View v) {
      if (mChildLongClickListener != null) {
        return mChildLongClickListener.onItemChildLongClick(
            BaseRecycleViewAdapter.this, v, mViewHolder.getLayoutPosition() - getHeaderViewCount());
      }
      return false;
    }
  }

  public void setItemAnimation(AnimationTypeUtils animationType) {
    mAnimationType = animationType;
  }

  public void setItemAnimationDuration(int animationDuration) {
    mAnimationDuration = animationDuration;
  }

  public void setItemAnimationInterpolator(Interpolator animationInterpolator) {
    mItemAnimationInterpolator = animationInterpolator;
  }

  public void setShowItemAnimationEveryTime(boolean showItemAnimationEveryTime) {
    this.showItemAnimationEveryTime = showItemAnimationEveryTime;
  }

  public void setCustomItemAnimator(CustomAnimator customAnimator) {
    mCustomAnimator = customAnimator;
  }

  public LinearLayout getHeaderLayout() {
    return mHeaderLayout;
  }

  public LinearLayout getFooterLayout() {
    return mFooterLayout;
  }

  public void addHeaderView(View header) {
    addHeaderView(header, -1);
  }

  public void addHeaderView(View header, int index) {
    if (mHeaderLayout == null) {
      if (mCopyHeaderLayout == null) {
        mHeaderLayout = new LinearLayout(header.getContext());
        mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
        mHeaderLayout.setLayoutParams(
            new RecyclerView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mCopyHeaderLayout = mHeaderLayout;
      } else {
        mHeaderLayout = mCopyHeaderLayout;
      }
    }
    index = index >= mHeaderLayout.getChildCount() ? -1 : index;
    mHeaderLayout.addView(header, index);
    this.notifyDataSetChanged();
  }

  public void addFooterView(View footer) {
    addFooterView(footer, -1);
  }

  public void addFooterView(View footer, int index) {
    if (mFooterLayout == null) {
      if (mCopyFooterLayout == null) {
        mFooterLayout = new LinearLayout(footer.getContext());
        mFooterLayout.setOrientation(LinearLayout.VERTICAL);
        mFooterLayout.setLayoutParams(
            new RecyclerView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mCopyFooterLayout = mFooterLayout;
      } else {
        mFooterLayout = mCopyFooterLayout;
      }
    }
    index = index >= mFooterLayout.getChildCount() ? -1 : index;
    mFooterLayout.addView(footer, index);
    this.notifyDataSetChanged();
  }

  public void removeHeaderView(View header) {
    if (mHeaderLayout == null) return;

    mHeaderLayout.removeView(header);
    if (mHeaderLayout.getChildCount() == 0) {
      mHeaderLayout = null;
    }
    this.notifyDataSetChanged();
  }

  public void removeFooterView(View footer) {
    if (mFooterLayout == null) return;

    mFooterLayout.removeView(footer);
    if (mFooterLayout.getChildCount() == 0) {
      mFooterLayout = null;
    }
    this.notifyDataSetChanged();
  }

  public void removeAllHeaderView() {
    if (mHeaderLayout == null) return;

    mHeaderLayout.removeAllViews();
    mHeaderLayout = null;
  }

  public void removeAllFooterView() {
    if (mFooterLayout == null) return;

    mFooterLayout.removeAllViews();
    mFooterLayout = null;
  }

  public int getHeaderViewCount() {
    return null == mHeaderLayout ? 0 : 1;
  }

  public int getFooterViewCount() {
    return null == mFooterLayout ? 0 : 1;
  }

  /** Some interface */
  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }

  public interface OnItemLongClickListener {
    void onItemLongClick(View view, int position);
  }

  public interface OnRecyclerViewItemChildClickListener {
    void onItemChildClick(BaseRecycleViewAdapter adapter, View view, int position);
  }

  public interface OnRecyclerViewItemChildLongClickListener {
    boolean onItemChildLongClick(BaseRecycleViewAdapter adapter, View view, int position);
  }

  public interface CustomAnimator {
    Animator getAnimator(View itemView);
  }

  private SpanSizeLookup mSpanSizeLookup;

  public interface SpanSizeLookup {
    int getSpanSize(GridLayoutManager gridLayoutManager, int position);
  }

  public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
    this.mSpanSizeLookup = spanSizeLookup;
  }

  @Override
  public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
    if (manager instanceof GridLayoutManager) {
      final GridLayoutManager gridManager = ((GridLayoutManager) manager);
      gridManager.setSpanSizeLookup(
          new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
              int type = getItemViewType(position - 1);
              if (mSpanSizeLookup == null)
                return (type == VIEW_TYPE.HEADER || type == VIEW_TYPE.FOOTER)
                    ? gridManager.getSpanCount()
                    : 1;
              else
                return (type == VIEW_TYPE.HEADER || type == VIEW_TYPE.FOOTER)
                    ? gridManager.getSpanCount()
                    : mSpanSizeLookup.getSpanSize(gridManager, position - getHeaderViewCount());
            }
          });
    }
  }

  // 处理StaggeredGridLayoutManager添加头部以及添加底部的
  @Override
  public void onViewAttachedToWindow(BaseRecycleViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    super.onViewAttachedToWindow(holder);
    int type = holder.getItemViewType();
    if (type == VIEW_TYPE.HEADER || type == VIEW_TYPE.FOOTER) {
      if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
        StaggeredGridLayoutManager.LayoutParams params =
            (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        params.setFullSpan(true);
      }
    }
  }

  public void setData(List<T> list) {
    if (this.mData != null) {
      if (null != list) {
        List<T> temp = new ArrayList<>();
        temp.addAll(list);
        this.mData.clear();
        this.mData.addAll(temp);
      } else {
        this.mData.clear();
      }
    } else {
      this.mData = list;
    }
    notifyDataSetChanged();
  }
}
