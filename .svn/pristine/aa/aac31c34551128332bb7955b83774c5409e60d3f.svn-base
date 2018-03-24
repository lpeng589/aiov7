package com.menyi.msgcenter.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.menyi.msgcenter.msgif.FileReq;
import com.menyi.msgcenter.msgif.FileRsp;
import com.menyi.msgcenter.msgif.FriendReq;
import com.menyi.msgcenter.msgif.FriendRsp;
import com.menyi.msgcenter.msgif.GroupReq;
import com.menyi.msgcenter.msgif.GroupRsp;
import com.menyi.msgcenter.msgif.HandShakeReq;
import com.menyi.msgcenter.msgif.HandShakeRsp;
import com.menyi.msgcenter.msgif.HeartReq;
import com.menyi.msgcenter.msgif.HeartRsp;
import com.menyi.msgcenter.msgif.IfCommand;
import com.menyi.msgcenter.msgif.MsgHeader;
import com.menyi.msgcenter.msgif.MsgReq;
import com.menyi.msgcenter.msgif.MsgRsp;
import com.menyi.msgcenter.msgif.StatusReq;
import com.menyi.msgcenter.msgif.StatusRsp;

public class ClientTest extends Thread {

	public static final String HOST = "192.168.2.210";

	public static final int PORT = 6321;

	boolean exist = false;
	
	public String userId;
	public String userName;
	OutputStream ous;
	DataInputStream ins;
	
	public ClientTest(String userId,String userName){
		this.userId = userId;
		this.userName = userName;
		
		try {

			Socket socket = new Socket(HOST, PORT);

			ous = socket.getOutputStream();
			ins = new DataInputStream(socket.getInputStream());

			// 接收线程，接收服务器的回应  
			RecieverThread reciever = new RecieverThread(ins);
			reciever.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private byte bTest[] = new byte[0];
	private byte bTest1[] = new byte[0];
	private byte bTest2[] = new byte[0];
	public void send(String key){
		MsgHeader msg=null;
		if("handshake".equals(key)){
			HandShakeReq req= new HandShakeReq();
			msg = req;
			req.sessionKey="sessionKey";
			req.shakeType=1;
			req.userId = "1";
			req.deptLastTime="2014-09-01 01:01:01";

			req.empLastTime="2014-09-01 01:01:01";

			req.friendLastTime="2014-09-01 01:01:01";

			req.groupLastTime="2014-09-01 01:01:01";
			bTest = req.encode();
		}else if("msg".equals(key)){
			MsgReq req = new MsgReq();
			msg = req;
			req.msgType=2;
			req.toObjType=2;
			req.fromUserId="1";//char(50)
			req.toObj="admin"; //char(50)
			try{
				req.dataStr="测试消息";
			}catch(Exception e){
				e.printStackTrace();
			}
			bTest1 = new byte[250];
			bTest2 = new byte[bTest.length+req.encode().length-250];
			System.arraycopy(bTest, 0, bTest1, 0, bTest.length);
			System.arraycopy(req.encode(), 0, bTest1, bTest.length, 250-bTest.length);
			System.arraycopy(req.encode(), 250-bTest.length, bTest2, 0,req.encode().length - 250+bTest.length);
			try{
				ous.write(bTest1);
				ous.flush();
				ous.write(bTest2);
				ous.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
			ous.write(msg.encode());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void run() {

		try {

			

//			while (!exist) {

				// 选择一个随机消息  
				String msg = "hello";

//				synchronized (ins) {

					// 发送给服务器端  
					ous.write(msg.getBytes("UTF-8"));
					System.out.println("send Msg:"+msg);
					
					// readLine()会阻塞，直到服务器输出一个 '\n'  
					byte[] bb= new byte[1024];
					int count = 0;
					while ((count = ins.read(bb)) != -1) {

						System.out.println("[Recieved]: " + new String (bb,0,count));
						
					}
					
					// 然后等待接收线程  
//					ins.wait();
//				}

//			}

			ins.close();
			ous.close();
			//socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//	 接收线程  
	class RecieverThread extends Thread {
		private DataInputStream dis;

		public RecieverThread(DataInputStream ins) {
			this.dis = ins;
		}

		@Override
		public void run() {

			try {
				while (true) {
					try {
						byte[] bhead = new byte[12];
						dis.readFully(bhead);						
						int msgLength = MsgHeader.getLength(bhead);
						System.out.println("读消息长度："+msgLength);
						byte[] bbody = new byte[msgLength - 12];
						if (bbody.length > 0) {
							dis.readFully(bbody);
						}
						System.out.println("读消息体："+msgLength);
						byte[] code = new byte[msgLength];
						System.arraycopy(bhead, 0, code, 0, 12);
						System.arraycopy(bbody, 0, code, 12, bbody.length);
						int command = MsgHeader.getCommand(code);

						switch (command) {
						case IfCommand.HANDSHAKE:
							HandShakeReq handShakeReq = new HandShakeReq();
							handShakeReq.decode(code);
							handShake(handShakeReq);
							break;
						case IfCommand.FILE:
							FileReq fileReq = new FileReq();
							fileReq.decode(code);
							handFileReq(fileReq);
							break;	
						case IfCommand.FILE_RSP:
							FileRsp fileRsp = new FileRsp();
							fileRsp.decode(code);
							handFileRsp(fileRsp);
							break;
						case IfCommand.FRIEND:
							FriendReq friendReq = new FriendReq();
							friendReq.decode(code);
							handFriendReq(friendReq);
							break;	
						case IfCommand.FRIEND_RSP:
							FriendRsp friendRsp = new FriendRsp();
							friendRsp.decode(code);
							handFriendRsp(friendRsp);
							break;	
						case IfCommand.GROUP:
							GroupReq groupReq = new GroupReq();
							groupReq.decode(code);
							handGroupReq(groupReq);
							break;	
						case IfCommand.GROUP_RSP:
							GroupRsp groupRsp = new GroupRsp();
							groupRsp.decode(code);
							handGroupRsp(groupRsp);
							break;	
						case IfCommand.MSG:
							MsgReq msgReq = new MsgReq();
							msgReq.decode(code);
							handMsgReq(msgReq);
							break;	
						case IfCommand.MSG_RSP:
							MsgRsp msgRsp = new MsgRsp();
							msgRsp.decode(code);
							handMsgRsp(msgRsp);
							break;
						case IfCommand.HANDSHAKE_RSP:
							HandShakeRsp shakeRsp = new HandShakeRsp();
							shakeRsp.decode(code);
							handShakeRsp(shakeRsp);
							break;
						case IfCommand.HEART:
							HeartReq heartReq = new HeartReq();
							heartReq.decode(code);
							handHeartbeat(heartReq);
							break;	
						case IfCommand.STATUS:	
							StatusReq statusReq = new StatusReq();
							statusReq.decode(code);
							handStatusReq(statusReq);
							break;
						default:
							System.out.println("MSGSession.clientMsgHandler receive unKnow Command ID " + command);
							break;
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void handShakeRsp(HandShakeRsp req) {
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handShake(HandShakeReq req) {
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		
		public void handMsgRsp(MsgRsp req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handMsgReq(MsgReq req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+"totalLength="+req.total_Length+req.toString());
		}
		public void handGroupRsp(GroupRsp req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handGroupReq(GroupReq req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handFriendRsp(FriendRsp req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handFriendReq(FriendReq req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handFileReq(FileReq req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handFileRsp(FileRsp req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}
		public void handStatusReq(StatusReq req){
			System.out.println("userID:"+userId+":userName:"+userName+":"+req.toString());
		}

		/**
		 * 用于接收手机终端的心跳应答
		 */
		public void handHeartbeat(HeartReq rep) {
			System.out.println("userID:"+userId+":userName:"+userName+":"+rep.toString());
			HeartRsp rsp = new HeartRsp();
			try{
				ous.write(rsp.encode());
			}catch(Exception e){
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws Exception {

		//	 开三个客户端线程  
		//for (int i = 0; i < 3; i++) {
			try {
				ClientTest ct = new ClientTest("1","admin");
				
				ct.send("handshake");
				//ct.send("msg");
				DataInputStream dis = new DataInputStream(System.in);
				String line="";
				while((line = dis.readLine()) != null){
					ct.send(line);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}

	}
}