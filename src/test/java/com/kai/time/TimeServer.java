package com.kai.time;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	private static final int port=8080;
	
	public void start() throws InterruptedException {
		ServerBootstrap b=new ServerBootstrap();
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workGroup=new NioEventLoopGroup();
		try {
			b.group(bossGroup, workGroup);
			b.channel(NioServerSocketChannel.class);
			b.localAddress(new InetSocketAddress(port));
			b.childHandler(new ChannelInitializer<SocketChannel>() {
	
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					.addLast(new TimeEncode())
					.addLast("myHandler",new TimeHandler());
				}
			});
			ChannelFuture f = b.bind().sync();
			System.out.println(f.channel().localAddress());
			f.channel().closeFuture().sync();
		}finally {
			bossGroup.shutdownGracefully().sync();
			workGroup.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new TimeServer().start();
	}
}
