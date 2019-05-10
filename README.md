# appium-data-capture
利用自动化测试框架实现爬虫的功能
运行效果：
<img src="http://github-blog.oss-cn-shenzhen.aliyuncs.com/2019-3-1.gif"/>
项目结构：
```
├─ src
│  └─ main
│     ├─ java
│     │  ├─ com
│     │  │  └─ chris
│     │  │     ├─ app
│     │  │     │  ├─ address
│     │  │     │  │  └─ AddressObtain.java
│     │  │     │  ├─ capture
│     │  │     │  │  └─ MeiTuanCapture.java
│     │  │     │  └─ storm
│     │  │     │     └─ DataHandleStormTopology.java
│     │  │     ├─ bean
│     │  │     │  ├─ AmapGeo.java
│     │  │     │  ├─ AmapReGeo.java
│     │  │     │  ├─ BaseTest.java
│     │  │     │  ├─ ElemeResInfo.java
│     │  │     │  └─ MtResInfo.java
│     │  │     ├─ util
│     │  │     │  ├─ AmapUtil.java
│     │  │     │  ├─ HttpClientUtil.java
│     │  │     │  ├─ KafkaProperties.java
│     │  │     │  ├─ KafkaReceive.java
│     │  │     │  ├─ KafkaSend.java
│     │  │     │  ├─ MongoDbSave.java
│     │  │     │  └─ PinyinUtil.java
│     │  │     └─ Application.java
│     │  ├─ AppiumTest.java
│     │  └─ BaseTest.java
│     └─ resources
│        ├─ address.txt
│        ├─ application.properties
│        └─ log4j.properties
└─ pom.xml
```
本项目分为三个app，包括爬取地址中心点获取、数据爬取、爬取数据处理三部分。
1. 爬取中心点获取：选定一个中心点，然后往东南西北四个方向移动n公里，通过高德地图逆地理编码获取经纬度，并转换成附近的
建筑物（AddressObtain、AmapUtil）。然后使用工具转成拼音（因为App地址Appium操作输入中文会闪退）设置爬取中心位置。
2. 爬取核心爬虫实现：利用自动化测试框架Appium实现模拟人工操作，同时获取元素值以获得门店相关信息，存入mongodbDb或发送至Kafka（MeiTuanCapture）。
3. 数据处理：使用Storm实时处理分析爬取到的数据（DataHandleStormTopology）。

PS：由于App会经常更新，所以爬虫代码需要保持维护才能使用。
环境搭建详见我的博客<a href="https://blog.developabc.com/2019/03/01/appium.html">Appium搭建爬虫环境</a>  

