package wyqj.cancerprevent.doctorversion.bean;

import java.io.Serializable;

/**
 * Created by 杨才 on 2016/2/17.
 */
public class ChooseBean implements Serializable{
    private int id;
    private String name;

    public ChooseBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
