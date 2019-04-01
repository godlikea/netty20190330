package com.kai.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.junit.jupiter.api.Test;

import io.netty.channel.socket.ServerSocketChannel;

public class TestChannel {
	
	@Test
	public void testReadMode() throws IOException {
		FileInputStream file=new FileInputStream("C:\\Users\\Administrator\\Desktop\\测试服务器地址.txt");
		FileChannel channel = file.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		channel.read(buffer);
		System.out.println(buffer);
	}
	
	@Test
	public void testWriteMode() throws IOException {
		FileOutputStream file=new FileOutputStream("C:\\Users\\Administrator\\Desktop\\测试服务器地址.txt");
		FileChannel channel = file.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		for(int i=0;i<=20;i++) {
			buffer.put(("get"+i).getBytes());
		}
		buffer.flip();
		channel.write(buffer);
	}
	
	public void testSelector() throws IOException {
		Selector selector = Selector.open();
		FileInputStream file=new FileInputStream("C:\\Users\\Administrator\\Desktop\\测试服务器地址.txt");
		FileChannel channel = file.getChannel();
		SocketChannel open = SocketChannel.open();
		open.configureBlocking(false);
		SelectionKey key = open.register(selector, SelectionKey.OP_READ);
	}
}
