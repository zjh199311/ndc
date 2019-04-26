package com.zhongjian.dto.message.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class MessageReqDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 发送者accid，用户帐号，最大32字符，必须保证一个APP内唯一
     */
    private String from;
    /**
     * 0：点对点个人消息，1：群消息（高级群），其他返回414
     */
    private Integer Ope;
    /**
     * ope==0是表示accid即用户id，ope==1表示tid即群id
     */
    private String to;
    /**
     * 0 表示文本消息,
     * 1 表示图片，
     * 2 表示语音，
     * 3 表示视频，
     * 4 表示地理位置信息，
     * 6 表示文件，
     * 100 自定义消息类型
     */
    private Integer type;


    /**
     * 请参考下方消息示例说明中对应消息的body字段，最大长度5000字符，为一个JSON串
     */
    private String body;

    /**
     * 发消息时特殊指定的行为选项,JSON格式，可用于指定消息的漫游，存云端历史，发送方多端同步，推送，消息抄送等特殊行为;option中字段不填时表示默认值 ，option示例:
     * <p>
     * {"push":false,"roam":true,"history":false,"sendersync":true,"route":false}
     * 字段说明：
     * 1. roam: 该消息是否需要漫游，默认true（需要app开通漫游消息功能）；
     * 2. history: 该消息是否存云端历史，默认true；
     *  3. sendersync: 该消息是否需要发送方多端同步，默认true；
     *  4. push: 该消息是否需要APNS推送或安卓系统通知栏推送，默认true；
     *  5. route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能);
     *  6. badge:该消息是否需要计入到未读计数中，默认true;
     * 7. needPushNick: 推送文案是否需要带上昵称，不设置该参数时默认true;
     * 8. persistent: 是否需要存离线消息，不设置该参数时默认true。
     */
    private String option;

    /**
     * ios推送内容，不超过150字符，option选项中允许推送（push=true），此字段可以指定推送内容
     */
    private String pushcontent;
    /**
     * ios 推送对应的payload,必须是JSON,不能超过2k字符
     */
    private String payload;

}
