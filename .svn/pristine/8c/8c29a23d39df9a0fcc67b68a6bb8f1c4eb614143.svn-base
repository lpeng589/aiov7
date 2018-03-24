package com.menyi.msgcenter.server;

import java.nio.ByteBuffer;
import java.util.Vector;

import com.menyi.msgcenter.msgif.MsgHeader;

/**
 * 消息队列，用于存储发送消息
 * @author Administrator
 * 
 * @preserve all
 *
 */
public class MSGQueue {
	public Vector list = new Vector();
	
	public synchronized void put(MsgHeader msg){
		list.add(msg);
	}
	
	public synchronized MsgHeader get(){
		return (MsgHeader)list.remove(0);
	}
	
	public synchronized void putFile(ByteBuffer bb){
		
		list.add(bb);
	}
	
	public synchronized ByteBuffer getFile(){
		return (ByteBuffer)list.remove(0);
	}
	
	public synchronized void clear(){
		list.clear();
	}

}
