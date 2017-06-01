package www.tencent.com.superframe.globalconfig;

/** 作者：王文彬 on 2017/4/24 14：06 邮箱：wwb199055@126.com */
public class Urls {

  //基接口
  public static String BaseUrl = "http://10.167.201.120:8888";
  //String BaseUrl = "http://223.202.12.55:8247";

  public static String ProUrl = BaseUrl + "/HX_CMR_APP/";

  //登录
  public static String LoginUrl = ProUrl + "login.do";

  //注册
  public static String RegisterUrl = ProUrl + "login.do";

  //验证码
  public static String VerificationCodeUrl = ProUrl + "login.do";

  //忘记密码
  public static String ChangePassword = ProUrl + "login.do";
}
