package www.tencent.com.superframe.loginmodule.bean;

import java.io.Serializable;

import www.tencent.com.superframe.base.bean.BaseJsonBean;

/**
 * Created by 南宫贇 on 2016/12/7.
 * $Description:
 */
public class UserInfo extends BaseJsonBean {

    public MessageBean message;
    public ResponseDataBean responseData;
    public String appTokenId;
    public long userId;
    public String roleId;
    public int type;

    public class MessageBean {
        public int code;
        public String msg;
    }

    public class ResponseDataBean implements Serializable {

        public String emp_name;
        public String emp_code;
        public String teamName;
        public String salesDepName;
        public String F_company;
        public String orgName;
        public String emp_roles;
        public String userType;

        public String emp_position;
        public String emp_sex;
        public String emp_mobile;
        public String emp_mail;
        public String emp_sj;
        public String emp_orgname;
        public String emp_status;

    }

}
