package com.edgar.vertx.service.discovery.consul;

import io.vertx.servicediscovery.Record;

import java.util.List;

/**
 * Created by Edgar on 2016/8/5.
 *
 * @author Edgar  Date 2016/8/5
 */
public interface LookupStrategy {

    /**
     * 从一组records中取出一个record
     *
     * @return
     */
    Record getRecord(List<Record> records);
}
