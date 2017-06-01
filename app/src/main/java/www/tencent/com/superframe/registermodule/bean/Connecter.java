package www.tencent.com.superframe.registermodule.bean;

import java.io.Serializable;


public class Connecter implements Serializable {
    public  String  name;
    public  String  phone;
    public  String  relation;  // 关系

    public Connecter() {
    }
    public Connecter(String name, String phone,String relation) {
        this.name = name;
        this.phone = phone;
        this.relation = relation;
    }
}
