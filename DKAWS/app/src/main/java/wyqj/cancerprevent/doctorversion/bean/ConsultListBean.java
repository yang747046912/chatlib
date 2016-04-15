package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

import java.util.List;

/**
 * Created by 杨才 on 2016/3/27.
 */
public class ConsultListBean {


    /**
     * consulting_unread_count : 1
     * orders : [{"id":"123","created_at":"2016-03-25T21:04:47+08:00","call_at":"2016-03-26T21:34:47+08:00","status":"waiting","has_read":false,"medical_record":{"name":"患者姓名","age":"年龄","sex":"性别","diseased_state_name":"癌种"}}]
     */

    private int consulting_unread_count;
    /**
     * id : 123
     * created_at : 2016-03-25T21:04:47+08:00
     * call_at : 2016-03-26T21:34:47+08:00
     * status : waiting
     * has_read : false
     * medical_record : {"name":"患者姓名","age":"年龄","sex":"性别","diseased_state_name":"癌种"}
     */

    private List<OrdersBean> orders;

    public int getConsulting_unread_count() {
        return consulting_unread_count;
    }

    public void setConsulting_unread_count(int consulting_unread_count) {
        this.consulting_unread_count = consulting_unread_count;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        private String id;
        private String created_at;
        private String call_at;

        private String state;
        private boolean has_read;
        @ParamNames("updated_at")
        private String updatedAt;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getState() {
            return state;
        }

        /**
         * name : 患者姓名
         * age : 年龄
         * sex : 性别
         * diseased_state_name : 癌种
         */

        private MedicalRecordBean medical_record;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCall_at() {
            return call_at;
        }

        public void setCall_at(String call_at) {
            this.call_at = call_at;
        }



        public boolean Hasread() {
            return has_read;
        }

        public void setHas_read(boolean has_read) {
            this.has_read = has_read;
        }

        public MedicalRecordBean getMedical_record() {
            return medical_record;
        }

        public void setMedical_record(MedicalRecordBean medical_record) {
            this.medical_record = medical_record;
        }

        public static class MedicalRecordBean {
            private String name;
            private String age;
            private String sex;
            private String diseased_state_name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getDiseased_state_name() {
                return diseased_state_name;
            }

            public void setDiseased_state_name(String diseased_state_name) {
                this.diseased_state_name = diseased_state_name;
            }
        }
    }
}
