package com.edgar.vertx.service.discovery.consul;

import io.vertx.servicediscovery.Record;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询从列表中选择
 *
 * @author Edgar  Date 2016/8/5
 */
public class RadomRobinLookupStrategy implements LookupStrategy {

    private final AtomicInteger seq = new AtomicInteger(0);

    @Override
    public Record getRecord(List<Record> records) {
        System.out.println("records:" + records);
        if (records == null || records.isEmpty()) {
            return null;
        }
        int index = Math.abs(seq.getAndIncrement());
        System.out.println(String.format("%d, %d, %d", index, records.size(), index % records.size()));
        return records.get(index % records.size());
    }

}
