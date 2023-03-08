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
@TableName("js_mobile_user")
@ApiModel(value = "JsMobileUser对象", description = "移动端用户表")
public class JsMobileUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户编码")
    @Excel(name = "用户编码")
    @TableId(value = "user_code", type = IdType.ASSIGN_ID)
    private String userCode;

    @ApiModelProperty(value = "登录账号")
    @Excel(name = "登录账号")
    @TableField("login_code")
    private String loginCode;

    @ApiModelProperty(value = "用户昵称")
    @Excel(name = "用户昵称")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "app登录token")
    @Excel(name = "app登录token")
    @TableField("token")
    private String token;

    @ApiModelProperty(value = "登录密码")
    @Excel(name = "登录密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "电子邮箱")
    @Excel(name = "电子邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "手机号码")
    @Excel(name = "手机号码")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "办公电话")
    @Excel(name = "办公电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    @Excel(name = "用户性别")
    @TableField("sex")
    private String sex;

    @ApiModelProperty(value = "头像路径")
    @Excel(name = "头像路径")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "个性签名")
    @Excel(name = "个性签名")
    @TableField("sign")
    private String sign;

    @ApiModelProperty(value = "绑定的微信号")
    @Excel(name = "绑定的微信号")
    @TableField("wx_openid")
    private String wxOpenid;

    @ApiModelProperty(value = "绑定的手机串号")
    @Excel(name = "绑定的手机串号")
    @TableField("mobile_imei")
    private String mobileImei;

    @ApiModelProperty(value = "用户类型")
    @Excel(name = "用户类型")
    @TableField("user_type")
    private String userType;

    @ApiModelProperty(value = "用户类型引用编号")
    @Excel(name = "用户类型引用编号")
    @TableField("ref_code")
    private String refCode;

    @ApiModelProperty(value = "用户类型引用姓名")
    @Excel(name = "用户类型引用姓名")
    @TableField("ref_name")
    private String refName;

    @ApiModelProperty(value = "管理员类型（0非管理员 1系统管理员  2二级管理员）")
    @Excel(name = "管理员类型（0非管理员 1系统管理员  2二级管理员）")
    @TableField("mgr_type")
    private String mgrType;

    @ApiModelProperty(value = "密码安全级别（0初始 1很弱 2弱 3安全 4很安全）")
    @Excel(name = "密码安全级别（0初始 1很弱 2弱 3安全 4很安全）")
    @TableField("pwd_security_level")
    private Long pwdSecurityLevel;

    @ApiModelProperty(value = "密码最后更新时间")
    @Excel(name = "密码最后更新时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("pwd_update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pwdUpdateDate;

    @ApiModelProperty(value = "密码修改记录")
    @Excel(name = "密码修改记录")
    @TableField("pwd_update_record")
    private String pwdUpdateRecord;

    @ApiModelProperty(value = "密保问题")
    @Excel(name = "密保问题")
    @TableField("pwd_question")
    private String pwdQuestion;

    @ApiModelProperty(value = "密保问题答案")
    @Excel(name = "密保问题答案")
    @TableField("pwd_question_answer")
    private String pwdQuestionAnswer;

    @ApiModelProperty(value = "密保问题2")
    @Excel(name = "密保问题2")
    @TableField("pwd_question_2")
    private String pwdQuestion2;

    @ApiModelProperty(value = "密保问题答案2")
    @Excel(name = "密保问题答案2")
    @TableField("pwd_question_answer_2")
    private String pwdQuestionAnswer2;

    @ApiModelProperty(value = "密保问题3")
    @Excel(name = "密保问题3")
    @TableField("pwd_question_3")
    private String pwdQuestion3;

    @ApiModelProperty(value = "密保问题答案3")
    @Excel(name = "密保问题答案3")
    @TableField("pwd_question_answer_3")
    private String pwdQuestionAnswer3;

    @ApiModelProperty(value = "密码问题修改时间")
    @Excel(name = "密码问题修改时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("pwd_quest_update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pwdQuestUpdateDate;

    @ApiModelProperty(value = "最后登陆IP")
    @Excel(name = "最后登陆IP")
    @TableField("last_login_ip")
    private String lastLoginIp;

    @ApiModelProperty(value = "最后登陆时间")
    @Excel(name = "最后登陆时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_login_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDate;

    @ApiModelProperty(value = "冻结时间")
    @Excel(name = "冻结时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("freeze_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date freezeDate;

    @ApiModelProperty(value = "冻结原因")
    @Excel(name = "冻结原因")
    @TableField("freeze_cause")
    private String freezeCause;

    @ApiModelProperty(value = "用户权重（降序）")
    @Excel(name = "用户权重（降序）")
    @TableField("user_weight")
    private BigDecimal userWeight;

    @ApiModelProperty(value = "状态（0正常 1删除 2停用 3冻结）")
    @Excel(name = "状态（0正常 1删除 2停用 3冻结）")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "创建者")
    @Excel(name = "创建者")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "更新者")
    @Excel(name = "更新者")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    @ApiModelProperty(value = "备注信息")
    @Excel(name = "备注信息")
    @TableField("remarks")
    private String remarks;

    @ApiModelProperty(value = "租户代码")
    @Excel(name = "租户代码")
    @TableField("corp_code")
    private String corpCode;

    @ApiModelProperty(value = "租户名称")
    @Excel(name = "租户名称")
    @TableField("corp_name")
    private String corpName;

    @ApiModelProperty(value = "扩展 String 1")
    @Excel(name = "扩展 String 1")
    @TableField("extend_s1")
    private String extendS1;

    @ApiModelProperty(value = "扩展 String 2")
    @Excel(name = "扩展 String 2")
    @TableField("extend_s2")
    private String extendS2;

    @ApiModelProperty(value = "扩展 String 3")
    @Excel(name = "扩展 String 3")
    @TableField("extend_s3")
    private String extendS3;

    @ApiModelProperty(value = "扩展 String 4")
    @Excel(name = "扩展 String 4")
    @TableField("extend_s4")
    private String extendS4;

    @ApiModelProperty(value = "扩展 String 5")
    @Excel(name = "扩展 String 5")
    @TableField("extend_s5")
    private String extendS5;

    @ApiModelProperty(value = "扩展 String 6")
    @Excel(name = "扩展 String 6")
    @TableField("extend_s6")
    private String extendS6;

    @ApiModelProperty(value = "扩展 String 7")
    @Excel(name = "扩展 String 7")
    @TableField("extend_s7")
    private String extendS7;

    @ApiModelProperty(value = "扩展 String 8")
    @Excel(name = "扩展 String 8")
    @TableField("extend_s8")
    private String extendS8;

    @ApiModelProperty(value = "扩展 Integer 1")
    @Excel(name = "扩展 Integer 1")
    @TableField("extend_i1")
    private Long extendI1;

    @ApiModelProperty(value = "扩展 Integer 2")
    @Excel(name = "扩展 Integer 2")
    @TableField("extend_i2")
    private BigDecimal extendI2;

    @ApiModelProperty(value = "扩展 Integer 3")
    @Excel(name = "扩展 Integer 3")
    @TableField("extend_i3")
    private BigDecimal extendI3;

    @ApiModelProperty(value = "扩展 Integer 4")
    @Excel(name = "扩展 Integer 4")
    @TableField("extend_i4")
    private BigDecimal extendI4;

    @ApiModelProperty(value = "扩展 Float 1")
    @Excel(name = "扩展 Float 1")
    @TableField("extend_f1")
    private BigDecimal extendF1;

    @ApiModelProperty(value = "扩展 Float 2")
    @Excel(name = "扩展 Float 2")
    @TableField("extend_f2")
    private BigDecimal extendF2;

    @ApiModelProperty(value = "扩展 Float 3")
    @Excel(name = "扩展 Float 3")
    @TableField("extend_f3")
    private BigDecimal extendF3;

    @ApiModelProperty(value = "扩展 Float 4")
    @Excel(name = "扩展 Float 4")
    @TableField("extend_f4")
    private BigDecimal extendF4;

    @ApiModelProperty(value = "扩展 Date 1")
    @Excel(name = "扩展 Date 1", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("extend_d1")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD1;

    @ApiModelProperty(value = "扩展 Date 2")
    @Excel(name = "扩展 Date 2", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("extend_d2")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD2;

    @ApiModelProperty(value = "扩展 Date 3")
    @Excel(name = "扩展 Date 3", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("extend_d3")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD3;

    @ApiModelProperty(value = "扩展 Date 4")
    @Excel(name = "扩展 Date 4", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("extend_d4")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extendD4;

    @ApiModelProperty(value = "扩展json")
    @Excel(name = "扩展json")
    @TableField("extend_json")
    private String extendJson;


}
