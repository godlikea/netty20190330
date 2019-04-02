package com.kai.time;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
	private String ip;
	
	private int port;
	
	public TimeClient(String ip,int port) {
		this.ip=ip;
		this.port=port;
	}
	public void run() throws InterruptedException {
		Bootstrap b=new Bootstrap();
		EventLoopGroup workGroup=new NioEventLoopGroup();
		try {
		b.group(workGroup);
		b.channel(NioSocketChannel.class);
		b.remoteAddress(new InetSocketAddress(ip, port));
		//客户端处理
		b.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new TimeDecoder1(),new TimeClientHandler());
			}
		});
		ChannelFuture f = b.connect().sync();
		//添加监听
		f.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					System.out.println("连接成功");
				}else {
					System.out.println("连接失败");
					future.cause().printStackTrace();
				}
			}
		});
		f.channel().closeFuture().sync();
		}finally {
			workGroup.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new TimeClient("127.0.0.1", 8080).run();
	}
}
