package com.edgar.vertx.buffer;

import io.vertx.core.buffer.Buffer;

/**
 * Created by Administrator on 2015/9/1.
 */
public class BufferExample {

    public static void main(String[] args) {
        //Create a new empty buffer:
        Buffer buffer = Buffer.buffer();
        //Create a buffer from a String. The String will be encoded in the buffer using UTF-8.
        buffer = Buffer.buffer("some string");

        //Create a buffer from a String: The String will be encoded using the specified encoding, e.g:
        buffer = Buffer.buffer("some string", "UTF-16");

        //Create a buffer from a byte[]
        byte[] bytes = new byte[] {1, 3, 5};
        buffer = Buffer.buffer(bytes);

        //Note that buffers created this way are empty. It does not create a buffer filled with zeros up to the specified size.
        buffer = Buffer.buffer(10000);

        //Writing to a Buffer
        //Appending to a Buffer
        buffer = Buffer.buffer();
        buffer.appendInt(123).appendString("hello\n");
        //socket.write(buffer);

        //Random access buffer writes
        buffer = Buffer.buffer();
        buffer.setInt(1000, 123);
        buffer.setString(0, "hello");

        //Reading from a Buffer
        buffer = Buffer.buffer();
        buffer.appendInt(1).appendInt(10);
        for (int i = 0; i < buffer.length(); i += 4) {
            System.out.println("int value at " + i + " is " + buffer.getInt(i));
        }

      //Working with unsigned numbers
      Buffer buff = Buffer.buffer(128);
      int pos = 15;
      buff.setUnsignedByte(pos, (short) 200);
      System.out.println(buff.getUnsignedByte(pos));
    }
}
