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
@Document(collection="CaptureElemeResInfo")
public class ElemeResInfo extends StringSerializer {
    @Id
    private Integer resHashCode;
    private String resName;
    private String distance;
    private String tag;
    private String deliveryTime;
    private String deliveryFee;
    private String deliveryType;
    private String saleMonth;
    private String ratingText;
    private String comment;
    private Date createTime;
    private Long createTimestamp;
    private Double latitude;
    private Double longitude;
}
