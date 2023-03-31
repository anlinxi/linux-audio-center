package com.faker.audioStation.enums;

public enum WyyApiTypeEnum {
    /**
     * 不加密的api
     */
    API("api"),
    /**
     * web的api
     */
    WE_API("weapi"),
    /**
     * linux的api
     */
    LINUX_API("linuxapi"),
    /**
     * e的api
     */
    E_API("eapi");

    /**
     * api名称
     */
    private String name;

    WyyApiTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
