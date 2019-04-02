package com.kai.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncode extends ChannelOutboundHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		UnixTime un=(UnixTime)msg;
		ByteBuf buf = ctx.alloc().buffer(4);
		buf.writeInt((int)un.value());
		ctx.write(buf,promise);
	}
	
	
}
