package www.tencent.com.superframe.base.bean;

import java.io.Serializable;

/** 作者：王文彬 on 2017/4/24 15：35 邮箱：wwb199055@126.com */
public class BaseJsonBean implements Serializable {

  private static final long serialVersionUID = 1L;

  public String message;
  public String result;

  public boolean isOk() {
    return "200".equals(result);
  }
}
