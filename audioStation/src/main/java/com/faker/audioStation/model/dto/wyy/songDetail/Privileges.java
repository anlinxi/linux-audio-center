/**
 * Copyright 2023 bejson.com
 */
package com.faker.audioStation.model.dto.wyy.songDetail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2023-03-10 21:38:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@NoArgsConstructor
public class Privileges implements Serializable {

    private int flag;
    private String dlLevel;
    private int subp;
    private long fl;
    private int fee;
    private int dl;
    private String plLevel;
    private String maxBrLevel;
    private long maxbr;
    private long id;
    private int sp;
    private int payed;
    private int st;
    private List<ChargeInfoList> chargeInfoList;
    private FreeTrialPrivilege freeTrialPrivilege;
    private long downloadMaxbr;
    private String downloadMaxBrLevel;
    private int cp;
    private boolean preSell;
    private String playMaxBrLevel;
    private boolean cs;
    private boolean toast;
    private long playMaxbr;
    private String flLevel;
    private long pl;

}