package com.kai.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpProtobufServer {
	public static final String IP="127.0.0.1";
	public static final int PORT=8080;
	/**
	 * 用于分配处理业务线程的线程组数
	 * @author ggk
	 * @data 2019年4月1日上午9:40:48
	 * @param args
	 */
	protected static final int BIZGROUPSIZE= Runtime.getRuntime().availableProcessors()*2;
	
	protected static final int BIZTHREADSIZE=4;
	
	private static final EventLoopGroup bossGroup=new NioEventLoopGroup(BIZGROUPSIZE);
	
	private static final EventLoopGroup wrokerGroup=new NioEventLoopGroup(BIZTHREADSIZE);
	
	protected static void run() throws InterruptedException {
		ServerBootstrap b=new ServerBootstrap();
		b.group(bossGroup, wrokerGroup);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder",new HttpRequestDecoder());
				pipeline.addLast("servercodec",new HttpServerCodec());
				pipeline.addLast("aggegator",new HttpObjectAggregator(1024*1024*64));
				pipeline.addLast(new HttpProtobufServerHandler());
				pipeline.addLast("responseencoder",new HttpResponseEncoder());
				
			}
		});
		b.bind(IP, PORT).sync();
		System.out.println("TCP服务器启动");
	}
	
	protected static void shutdown() {
		wrokerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("启动http服务器.....");
		HttpProtobufServer.run();

	}

}
