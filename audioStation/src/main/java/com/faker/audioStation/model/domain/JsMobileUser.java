package com.faker.audioStation.model.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 移动端用户表
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("JS_MOBILE_USER")
@ApiModel(value = "JsMobileUser对象", description = "移动端用户表")
public class JsMobileUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户编码")
    @Excel(name = "用户编码")
    @TableId(value = "USER_CODE", type = IdType.ASSIGN_ID)
    private String userCode;

    @ApiModelProperty(value = "登录账号")
    @Excel(name = "登录账号")
    @TableField("LOGIN_CODE")
    private String loginCode;

    @ApiModelProperty(value = "用户昵称")
    @Excel(name = "用户昵称")
    @TableField("USER_NAME")
    private String userName;

    @ApiModelProperty(value = "app登录token")
    @Excel(name = "app登录token")
    @TableField("TOKEN")
    private String token;

    @ApiModelProperty(value = "登录密码")
    @Excel(name = "登录密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "电子邮箱")
    @Excel(name = "电子邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "手机号码")
    @Excel(name = "手机号码")
    @TableField("MOBILE")
    private String mobile;

    @ApiModelProperty(value = "办公电话")
    @Excel(name = "办公电话")
    @TableField("PHONE")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    @Excel(name = "用户性别")
    @TableField("SEX")
    private String sex;

    @ApiModelProperty(value = "头像路径")
    @Excel(name = "头像路径")
    @TableField("AVATAR")
    private String avatar;

    @ApiModelProperty(value = "个性签名")
    @Excel(name = "个性签名")
    @TableField("SIGN")
    private String sign;

    @ApiModelProperty(value = "绑定的微信号")
    @Excel(name = "绑定的微信号")
    @TableField("WX_OPENID")
    private String wxOpenid;

    @ApiModelProperty(value = "绑定的手机串号")
    @Excel(name = "绑定的手机串号")
    @TableField("MOBILE_IMEI")
    private String mobileImei;

    @ApiModelProperty(value = "用户类型")
    @Excel(name = "用户类型")
    @TableField("USER_TYPE")
    private String userType;

    @ApiModelProperty(value = "用户类型引用编号")
    @Excel(name = "用户类型引用编号")
    @TableField("REF_CODE")
    private String refCode;

    @ApiModelProperty(value = "用户类型引用姓名")
    @Excel(name = "用户类型引用姓名")
    @TableField("REF_NAME")
    private String refName;

    @ApiModelProperty(value = "管理员类型（0非管理员 1系统管理员  2二级管理员）")
    @Excel(name = "管理员类型（0非管理员 1系统管理员  2二级管理员）")
    @TableField("MGR_TYPE")
    private String mgrType;

    @ApiModelProperty(value = "密码安全级别（0初始 1很弱 2弱 3安全 4很安全）")
    @Excel(name = "密码安全级别（0初始 1很弱 2弱 3安全 4很安全）")
    @TableField("PWD_SECURITY_LEVEL")
    private Long pwdSecurityLevel;

    @ApiModelProperty(value = "密码最后更新时间")
    @Excel(name = "密码最后更新时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("PWD_UPDATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pwdUpdateDate;

    @ApiModelProperty(value = "密码修改记录")
    @Excel(name = "密码修改记录")
    @TableField("PWD_UPDATE_RECORD")
    private String pwdUpdateRecord;

    @ApiModelProperty(value = "密保问题")
    @Excel(name = "密保问题")
    @TableField("PWD_QUESTION")
    private String pwdQuestion;

    @ApiModelProperty(value = "密保问题答案")
    @Excel(name = "密保问题答案")
    @TableField("PWD_QUESTION_ANSWER")
    private String pwdQuestionAnswer;

    @ApiModelProperty(value = "密保问题2")
    @Excel(name = "密保问题2")
    @TableField("PWD_QUESTION_2")
    private String pwdQuestion2;

    @ApiModelProperty(value = "密保问题答案2")
    @Excel(name = "密保问题答案2")
    @TableField("PWD_QUESTION_ANSWER_2")
    private String pwdQuestionAnswer2;

    @ApiModelProperty(value = "密保问题3")
    @Excel(name = "密保问题3")
    @TableField("PWD_QUESTION_3")
    private String pwdQuestion3;

    @ApiModelProperty(value = "密保问题答案3")
    @Excel(name = "密保问题答案3")
    @TableField("PWD_QUESTION_ANSWER_3")
    private String pwdQuestionAnswer3;

    @ApiModelProperty(value = "密码问题修改时间")
    @Excel(name = "密码问题修改时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("PWD_QUEST_UPDATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pwdQuestUpdateDate;

    @ApiModelProperty(value = "最后登陆IP")
    @Excel(name = "最后登陆IP")
    @TableField("LAST_LOGIN_IP")
    private String lastLoginIp;

    @ApiModelProperty(value = "最后登陆时间")
    @Excel(name = "最后登陆时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("LAST_LOGIN_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDate;

    @ApiModelProperty(value = "冻结时间")
    @Excel(name = "冻结时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("FREEZE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date freezeDate;

    @ApiModelProperty(value = "冻结原因")
    @Excel(name = "冻结原因")
    @TableField("FREEZE_CAUSE")
    private String freezeCause;

    @ApiModelProperty(value = "用户权重（降序）")
    @Excel(name = "用户权重（降序）")
    @TableField("USER_WEIGHT")
    private BigDecimal userWeight;

    @ApiModelProperty(value = "状态（0正常 1删除 2停用 3冻结）")
    @Excel(name = "状态（0正常 1删除 2停用 3冻结）")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "创建者")
    @Excel(name = "创建者")
    @TableField("CREATE_BY")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "更新者")
    @Excel(name = "更新者")
    @TableField("UPDATE_BY")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    @ApiModelProperty(value = "备注信息")
    @Excel(name = "备注信息")
    @TableField("REMARKS")
    private String remarks;

    @ApiModelProperty(value = "租户代码")
    @Excel(name = "租户代码")
    @TableField("CORP_CODE")
    private String corpCode;

    @ApiModelProperty(value = "租户名称")
    @Excel(name = "租户名称")
    @TableField("CORP_NAME")
    private String corpName;

    @ApiModelProperty(value = "扩展 String 1")
    @Excel(name = "扩展 String 1")
    @TableField("EXTEND_S1")
    private String extendS1;

    @ApiModelProperty(value = "扩展 String 2")
    @Excel(name = "扩展 String 2")
    @TableField("EXTEND_S2")
    private String extendS2;

    @ApiModelProperty(value = "扩展 String 3")
    @Excel(name = "扩展 String 3")
    @TableField("EXTEND_S3")
    private String extendS3;

    @ApiModelProperty(value = "扩展 String 4")
    @Excel(name = "扩展 String 4")
    @TableField("EXTEND_S4")
    private String extendS4;

    @ApiModelProperty(value = "扩展 String 5")
    @Excel(name = "扩展 String 5")
    @TableField("EXTEND_S5")
    private String extendS5;

    @ApiModelProperty(value = "扩展 String 6")
    @Excel(name = "扩展 String 6")
    @TableField("EXTEND_S6")
    private String extendS6;

    @ApiModelProperty(value = "扩展 String 7")
    @Excel(name = "扩展 String 7")
    @TableField("EXTEND_S7")
    private String extendS7;

    @ApiModelProperty(value = "扩展 String 8")
    @Excel(name = "扩展 String 8")
    @TableField("EXTEND_S8")
    private String extendS8;

    @ApiModelProperty(value = "扩展 Integer 1")
    @Excel(name = "扩展 Integer 1")
    @TableField("EXTEND_I1")
    private Long extendI1;

    @ApiModelProperty(value = "扩展 Integer 2")
    @Excel(name = "扩展 Integer 2")
    @TableField("EXTEND_I2")
    private BigDecimal extendI2;

    @ApiModelProperty(value = "扩展 Integer 3")
    @Excel(name = "扩展 Integer 3")
    @TableField("EXTEND_I3")
    private BigDecimal extendI3;

    @ApiModelProperty(value = "扩展 Integer 4")
    @Excel(name = "扩展 Integer 4")
    @TableField("EXTEND_I4")
    private BigDecimal extendI4;

    @ApiModelProperty(value = "扩展 Float 1")
    @Excel(name = "扩展 Float 1")
    @TableField("EXTEND_F1")
    private BigDecimal extendF1;

    @ApiModelProperty(value = "扩展 Float 2")
    @Excel(name = "扩展 Float 2")
    @TableField("EXTEND_F2")
    private BigDecimal extendF2;

    @ApiModelProperty(value = "扩展 Float 3")
    @Excel(name = "扩展 Float 3")
    @TableField("EXTEND_F3")
    private BigDecimal extendF3;

    @ApiModelProperty(value = "扩展 Float 4")
    @Excel(name = "扩展 Float 4")
    @TableField("EXTEND_F4")
    private BigDecimal extendF4;

    @ApiModelProperty(value = "扩展 Float 5")
    @Excel(name = "扩展 Float 5")
    @TableField("EXTEND_F5")
    private BigDecimal extendF5;

    @ApiModelProperty(value = "扩展 Date 1")
    @Excel(name = "扩展 Date 1", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXTEND_D1")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD1;

    @ApiModelProperty(value = "扩展 Date 2")
    @Excel(name = "扩展 Date 2", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXTEND_D2")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD2;

    @ApiModelProperty(value = "扩展 Date 3")
    @Excel(name = "扩展 Date 3", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXTEND_D3")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD3;

    @ApiModelProperty(value = "扩展 Date 4")
    @Excel(name = "扩展 Date 4", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXTEND_D4")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD4;

    @ApiModelProperty(value = "扩展json")
    @Excel(name = "扩展json")
    @TableField("EXTEND_JSON")
    private String extendJson;


}
