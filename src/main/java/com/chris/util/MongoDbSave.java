package com.chris.util;

import com.mongodb.MongoClientURI;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;
import java.util.Collection;

/**
 * @Description:MongoDb操作
 * @Author: zhangliangfu
 * @Create on: 2019-03-22 10:08
 */
public class MongoDbSave {

    private static MongoTemplate mongoTemplate;

    public MongoDbSave(){
        try {
            String uri = "mongodb://devtrunk:Eatdevtrunk@192.168.10.10:27017/DevTrunk";
            MongoClientURI mongoClientURI = new MongoClientURI(uri);
            SimpleMongoDbFactory simpleMongoDbFactory;
            simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);
            mongoTemplate = new MongoTemplate(simpleMongoDbFactory);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void insert(Object o){
        mongoTemplate.insert(o);
    }
    public void insert(Collection<? extends Object> batchToSave, Class<?> entityClass){
        mongoTemplate.insert(batchToSave, entityClass);
    }
    public void save(Object objectToSave){
        mongoTemplate.save(objectToSave);
    }

}
