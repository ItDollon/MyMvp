package www.tencent.com.superframe.registermodule.bean;

import java.io.Serializable;
import java.util.List;

import www.tencent.com.superframe.base.bean.BaseJsonBean;


public class UserBean extends BaseJsonBean {
    public String token;
    public Customer customer;//客户信息

    public  class Customer implements Serializable {
        public String id;//客户ID
        public String userName;//用户名
        public String mobilePhone;//手机号
        public String name;//用户真实姓名
        public String balance;//账户金额
        public String identityNum;//身份证号码
        public String grade;//客户等级 - 0 是新手 1 铜牌 2 银牌 3金牌
        public String headPicPath;//头像路径
        public String personPicPath;//个人手持身份证图片路径
        public String cardFrontPicPath;//身份证正面图片路径
        public String cardBackPicPath;//身份证反面图片路径
        public String cardHeadPicPath;//身份证头像切图图片路径
        public String faceAuth;//面部识别是否通过 1是通过 下同
        public String policeAuth;//公安认证是否通过
        public String electricAuth;//商认证是否通过
        public String identityAuth;//身份认证是否通过  1 表示通过  0 表示不通过
        public String contactsAuth;//联系人信息是否认证
        public String etradeAuth;//电商信息是否认证
        public String educationAuth;//学历信息是否认证
        public String cardBindStatus="0";//绑定银行卡标识 1是 0否
        public String autoRepay;//自动还款状态 0否1是
        public String cardStartTime;//身份证起期
        public String cardEndTime;//身份证止期
        public String valideOrg;//身份证发证机关
        public String cardTime;//身份证起期  身份证止期

        public String addressAuth;//详细地址是否完善 0否1是
        public String detailAddress;//详细地址

        /**
         * 联系人
         */
        public List<Connecter> contactsList;

        /**
         * 教育   选项数字
         */
        public int education=-1;

        /**
         * 结婚 选项数字
         */
        public int marriageStatus=-1;

        /**
         *
         * @param field  要认证的字段
         * @return  是否通过了认证
         */

        public boolean isAuth(String field) {
            return "1".equalsIgnoreCase(field);
        }
    }
}
