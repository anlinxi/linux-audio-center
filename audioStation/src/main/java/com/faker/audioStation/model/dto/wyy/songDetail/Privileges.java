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

    private Long flag;
    private String dlLevel;
    private Long subp;
    private Long fl;
    private Long fee;
    private Long dl;
    private String plLevel;
    private String maxBrLevel;
    private Long maxbr;
    private Long id;
    private Long sp;
    private Long payed;
    private Long st;
    private List<ChargeInfoList> chargeInfoList;
    private FreeTrialPrivilege freeTrialPrivilege;
    private Long downloadMaxbr;
    private String downloadMaxBrLevel;
    private Long cp;
    private boolean preSell;
    private String playMaxBrLevel;
    private boolean cs;
    private boolean toast;
    private Long playMaxbr;
    private String flLevel;
    private Long pl;

}