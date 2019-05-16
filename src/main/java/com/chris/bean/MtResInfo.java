package com.chris.bean;

import lombok.Data;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @Description:门店信息
 * @Author: zhangliangfu
 * @Create on: 2019-03-01 13:55
 */

@Data
@Document(collection="CaptureMtResInfo")
public class MtResInfo extends StringSerializer {
    @Id
    private Integer resHashCode;
    //门店名称
    private String resName;
    //距离
    private String distance;
    //标签
    private String tag;
    //配送，人均消费
    private String avgPrice;
    //评分
    private String ratingText;
    //门店分类
    private String cate;
    //优惠信息
    private String voucherInfo;
    //月售
    private String monthSale;
    //配送方式
    private String delivery;
    //地址
    private String address;
    //配送时间
    private String deliveryTime;

    private Date createTime;
    private Long createTimestamp;
    private Double latitude;
    private Double longitude;

    /**
     * 未采集
     */
    private String area;
    private String discountTxt;
    private String discountPromotion;
    private String labelContent;

}
