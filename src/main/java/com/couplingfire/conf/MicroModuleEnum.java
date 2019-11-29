package com.couplingfire.conf;

/**
 * @Date 2019/11/29 13:52
 * @Author lee
 **/
public class MicroModuleEnum {

    public enum ListenerGroup {

        LOCAL_LISTENER("local_listener",3),
        REMOTE_LISTENER("remote_listener",2);

        ListenerGroup(String name, Integer code) {
            this.name = name;
            this.code = code;
        }

        private String name;
        private Integer code;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }

}
