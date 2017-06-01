package www.tencent.com.superframe.base.presenter;

import java.lang.ref.WeakReference;

import www.tencent.com.superframe.base.model.BaseModel;
import www.tencent.com.superframe.base.view.view.BaseView;

/** 作者：王文彬 on 2017/4/21 17：18 邮箱：wwb199055@126.com */
public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {

  protected M model;
  protected WeakReference<V> weakReference;
  protected V v;

  public BasePresenter() {
    model = createModel();
  }

  public void bind(V view) {
    this.v = view;
    weakReference = new WeakReference<V>(v);
  }

  public void unbind() {
    if (weakReference != null) {
      weakReference.clear();
      weakReference = null;
    }
  }

  public boolean isUnbind() {
    return weakReference != null && weakReference.get() != null;
  }

  public V getView() {
    if (weakReference != null) {
      return weakReference.get();
    }
    return null;
  }

  protected abstract M createModel();
}
