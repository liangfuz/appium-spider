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
    private String resName;
    private String distance;
    private String tag;
    private String smartTag;
    private String avgPrice;
    private String area;
    private String discountTxt;
    private String discountPromotion;
    private String labelContent;
    private String ratingText;
    private String cate;
    private String voucherInfo;
    private String monthSale;
    private String delivery;
    private Date createTime;
    private Long createTimestamp;
    private String address;
    private Double latitude;
    private Double longitude;

}
