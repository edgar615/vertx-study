package com.edgar.vertx.eventbus.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.IOException;

/**
 * Created by Edgar on 2016/1/31.
 *
 * @author Edgar  Date 2016/1/31
 */
public class MyCodec implements MessageCodec<MyPojo, MyPojo> {
  ObjectMapper mapper = new ObjectMapper();

  @Override
  public void encodeToWire(Buffer buffer, MyPojo result) {
    try {
      byte[] encoded = mapper.writeValueAsBytes(result);
      buffer.appendInt(encoded.length);
      Buffer buff = Buffer.buffer(encoded);
      buffer.appendBuffer(buff);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public MyPojo decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);
    pos += 4;
    byte[] encoded = buffer.getBytes(pos, pos + length);
    try {
      return mapper.readValue(encoded, MyPojo.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public MyPojo transform(MyPojo result) {
    try {
      byte[] bytes = mapper.writeValueAsBytes(result);
      return mapper.readValue(bytes, MyPojo.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String name() {
    return "myCodec";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}