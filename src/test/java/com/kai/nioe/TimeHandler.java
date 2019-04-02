package com.kai.nioe;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeHandler extends ChannelInboundHandlerAdapter {
	private  ByteBuf  buf;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf bb=(ByteBuf)msg;
		//FIXME ggk 2019-04-02    修改代码实现
		/*try {
			long ctm=(bb.readUnsignedInt()-2208988800L)*1000L;
			System.out.println(bb.toString());
			System.out.println(new Date(ctm));
			ctx.close();
		}finally {
			bb.release();
		}*/
		buf.writeBytes(bb);
		bb.release();
		if(buf.readableBytes()>=4) {
			long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
			System.out.println(new Date(currentTimeMillis));
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		buf=ctx.alloc().buffer(4);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		buf.release();
		buf=null;
	}

	
}
