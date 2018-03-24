package com.menyi.msgcenter.server;

import java.util.HashMap;

import org.apache.commons.validator.Msg;
import org.apache.log4j.Logger;

import java.util.concurrent.Future;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.msgcenter.msgif.*;

/**
 * 为解决数据传输过程中的。粘包的半包问题，这里设一个缓存，当缓存达到一个完整包的长度时，才执行动作
 * 
 * @author Administrator
 * @preserve all
 */
public class MSGConnectSocket {

	public static final int CLIENT_NORMAL = 0;

	public static final int CLIENT_FILETRAN = 1; // 文件传输通道

	private Logger log;

	private HashMap sessionPool;

	private HashMap fileSessionPool;

	public SocketChannel client;

	public int clientType = CLIENT_NORMAL;

	private byte[] inBuffer;

	private Object rLock = new Object(); // 读取锁

	private Object wLock = new Object(); // 读取锁

	private MSGSession session;

	private long fileSize = 0;

	private long hadReadSize = 0;

	private ByteBuffer readBuffer = ByteBuffer.allocate(512 * 1024);

	public ReadThread readThread = new ReadThread();

	public ReadFileThread readFileThread = new ReadFileThread();

	public WriteFileThread writeFileThread = new WriteFileThread();

	public WriteThread writeThread = new WriteThread();

	MSGQueue queue = new MSGQueue(); // 消息队列

	FileInputStream fis;

	FileOutputStream fos;

	public MSGConnectSocket(Logger log, HashMap sessionPool,
			SocketChannel client, HashMap fileSessionPool) {

		this.fileSessionPool = fileSessionPool;
		this.sessionPool = sessionPool;
		this.log = log;
		this.client = client;
	}

	/**
	 * 读取文件
	 * 
	 */
	public void readFile() {
		try {
			readBuffer.clear();
			if (client == null) {
				return;
			}

			synchronized (rLock) {
				int count = client.read(readBuffer);// 从通道中读取数据
				if (count == 0) {
					return;
				}
				readBuffer.flip();// 将数据翻转，用于读出
				if (hadReadSize >= fileSize) {
					session.fileTransport(readBuffer.array(), count, true);
				} else {
					session.fileTransport(readBuffer.array(), count, false);
				}
				hadReadSize += count;
				if (hadReadSize >= fileSize) {
					log.debug("MSGConnectSocket.readFile() "+(session == null ? "Session is Null " : "userID:"
							+ session.userId + ":userName:" + session.userName)
							+ "文件读取完毕，关闭连接");
					closeFileConnect();
				}
			}
		} catch (Exception e) {
			log.error("MSGConnectSocket.readFile() "+(session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName)
					+ "读文件", e);

		}
	}

	/**
	 * 读取从客户端过来的数据，并处理粘包，和半包。 处理原理为：
	 * 1.把bs存入读取数据缓存中，并分析缓存，如果有多个消息，则执行多次，如果有完全整消息，则等待下一次机会
	 * 
	 * @param bs
	 */
	public void read() {
		try {
			readBuffer.clear();
			if (client == null) {
				log.info("MSGConnectSocket.read() 读信息但client为空" + (session == null ? "Session is Null " : "userID:"
					+ session.userId + ":userName:" + session.userName)  );
				return;
			}
			synchronized (rLock) {
				int count = client.read(readBuffer);// 从通道中读取数据
				if (count < 0) {
					this.close("MSGConnectSocket.read 读到小于0的数据，说明读通道出现异常，可能服务已中断");
					return;
				}
				readBuffer.flip();// 将数据翻转，用于读出
				byte[] inbs = new byte[count];
				System.arraycopy(readBuffer.array(), 0, inbs, 0, count);
				byte[] bs = inbs;
				if (inBuffer == null) {
					bs = inbs;
				} else {
					bs = new byte[inBuffer.length + inbs.length];
					System.arraycopy(inBuffer, 0, bs, 0, inBuffer.length);
					System.arraycopy(inbs, 0, bs, inBuffer.length, inbs.length);
				}
				while (true) {
					if (bs.length < 12) {
						// 不足12个字节，肯定不是一个完整的包
						inBuffer = bs;
						return;
					}
					int length = MsgHeader.getLength(bs);
					if (bs.length < length) {
						// 如果消息长度大于byte的长度，说明这是个半包，需要等待下一个包补充完整才能执行
						inBuffer = bs;
						return;
					} else if (bs.length == length) {
						// 长度相等说明这正好是一个完整的包,处理此消息
						inBuffer = null;
						handMsg(bs);
						return;
					} else {
						// 超过此长度，说明这是个粘包，分段处理
						byte[] temp = new byte[length];
						System.arraycopy(bs, 0, temp, 0, length);
						// 处理此包
						handMsg(temp);

						temp = new byte[bs.length - length];
						System.arraycopy(bs, length, temp, 0, bs.length
								- length);
						bs = temp;
					}
				}
			}
		} catch (Exception e) {
			close("MSGConnectSocket.read 出现try错误 "+e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void handMsg(byte[] bs) throws Exception{
		try{
			int command = MsgHeader.getCommand(bs);
			// 如果是握手消息，需建立用户的会话信息。
			if (command == MsgHeader.HANDSHAKE) {
				HandShakeReq msg = new HandShakeReq();
				msg.decode(bs);
				// 从用户会话池中取数据，不产生新的会话，避免内存泄漏
				session = (MSGSession) sessionPool.get(msg.userId);
				String userName = MSGConnectCenter.employeeMap.get(msg.userId).name;
				
				log.debug("MSGConnectSocket.handMsg() 接收到握手消息 userID:" + msg.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + msg.toString());
				
				if (session == null) {
					session = new MSGSession(msg.userId, userName, log,
							sessionPool, null, this);
					sessionPool.put(msg.userId, session);
				} else {
					if (session.connect != this) { // 另一个客户端登录时，验证该用户是否已经登录
						ForceOfflineReq req = new ForceOfflineReq();
						if (session.connect != null)
							session.connect.write(req);	// 让客户端自己把连接给断了
					}
					session.init(userName, this);
				}
				session.handShake(msg);
	
			} else if (session != null && session.connect != null) {
				// 找到对应的会话信息，并处理消息
				if (session != null) {
					session.clientMsgHandler(bs);
				} else {
					log.error("非握手消息，session为空。。。");
				}
			} else if (command == MsgHeader.FILESHAKE) { // 如果是文件传输通知消息，则说明这是一条新通道，专门用于文件传输
				FileShakeReq msg = new FileShakeReq();
				msg.decode(bs);
				String userId;
				if (msg.direct == FileShakeReq.RECEIVE || msg.direct == FileShakeReq.OFFLINE_RECV) {
					userId = msg.receiveId;
				} else {
					userId = msg.sendId;
				}
				String mapKey = msg.sessionKey + "$$" + userId;
				session = (MSGSession) fileSessionPool.get(mapKey);
				String userName = MSGConnectCenter.employeeMap.get(userId).name;
				
				log.debug("MSGConnectSocket.handMsg() 接收到文件请求握手消息 userID:" + userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + msg.toString());
				
				if (session == null) {
					session = new MSGSession(userId, userName, log, null,
							fileSessionPool, this);
					fileSessionPool.put(mapKey, session);
				} else {
					if (null != session.fileTranConnect && session.fileTranConnect != this) {
						session.fileTranConnect.close("文件传输通道，不为空且不属于自己");
					}
				}
				readBuffer = ByteBuffer.allocate(5 * 1024 * 1024); // 加大文件缓存为5M.
				session.initFileTran(msg, this);
				session.fileShake(msg); // 应答
	
				try {
					if (msg.direct == FileShakeReq.OFFLINE_RECV) {
						fis = new FileInputStream(msg.fileName);
						MsgReq req = new MsgReq();
						req.msgType = MsgReq.FILE;
						byte[] tmp = new byte[1024 * 1024];
						int len = fis.read(tmp);
						if (len != -1) {
							req.dataFile = new byte[len];
							System.arraycopy(tmp, 0, req.dataFile, 0, len);
						}
						write(req);
					}
				} catch (Exception e) {
					log.error("MSGConnectSocket.handMsg() 请求握手消息错误 userID:" + userId + ":userName:"
							+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + msg.toString(),e);
				}
			} else if (session != null && session.fileTranConnect != null) {// 文件传输
				session.lastActiveTime = System.currentTimeMillis();
				session.lostHeartbeatTimes = 0; // 有数据上来就代表没断开
				if (command == MsgHeader.MSG) {
					MsgReq req = new MsgReq();
					req.decode(bs);
					log.debug("MSGConnectSocket.handMsg() 接收到文件消息 userID:" + (session == null ? "Session is Null " : "userID:"
						+ session.userId + ":userName:" + session.userName) + "时间:"+BaseDateFormat.getNowTimeLong()+":" + req.toString());
					if (FileShakeReq.SEND == session.fileShakeReq.direct) {
						// 转发
						String recvKey = session.fileShakeReq.sessionKey + "$$"
								+ session.fileShakeReq.receiveId;
						MSGSession ms = (MSGSession) fileSessionPool.get(recvKey);
						if (ms != null) {
							ms.fileTranConnect.write(req);
						} else {
							// 通道没建立，把发送端给关了
							close("文件传输通道没建立，把发送端给关了");
						}
					} else if (FileShakeReq.OFFLINE_SEND == session.fileShakeReq.direct){
						try {
							if (new File(session.fileShakeReq.fileName).exists()) {
								fos.write(req.dataFile);
							} else {
								File f = new File(session.fileShakeReq.fileName);
								if (!f.getParentFile().exists()) {
									f.getParentFile().mkdirs();
								}
								fos = new FileOutputStream(f, true);
								fos.write(req.dataFile);
							}						
						} catch (Exception e) {
							e.printStackTrace();
						}
						MsgRsp rsp = new MsgRsp();
						rsp.sequence_Id = req.sequence_Id;
						rsp.result = IfResult.RECEIVE; // 标识已收到
						write(rsp);
						
						if (0 == req.dataLength) {
							// 文件传输结束标志
							try {
								fos.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							MSGConnectCenter.sendMsg(session.fileShakeReq.sendId, session.fileShakeReq.receiveId, session.fileShakeReq.fileName.substring(session.fileShakeReq.fileName.lastIndexOf("/") + 1), "file");
						}
					}
				} else if (command == MsgHeader.MSG_RSP) {
					if (FileShakeReq.RECEIVE == session.fileShakeReq.direct) {
						MsgRsp rsp = new MsgRsp();
						rsp.decode(bs);
						log.debug("MSGConnectSocket.handMsg() 接收到文件应答消息 " + (session == null ? "Session is Null " : "userID:"
							+ session.userId + ":userName:" + session.userName) + "时间:"+BaseDateFormat.getNowTimeLong()+":" + rsp.toString());
						String recvKey = session.fileShakeReq.sessionKey + "$$"
								+ session.fileShakeReq.sendId;
						MSGSession ms = (MSGSession) fileSessionPool.get(recvKey);
						if (ms != null) {
							ms.fileTranConnect.write(rsp);
						} else {
							// 发送者关闭通道，关闭发送
							close("文件传输发送者关闭通道，关闭发送");
						}
					} else if (FileShakeReq.OFFLINE_RECV == session.fileShakeReq.direct) {
						try {
							MsgReq req = new MsgReq();
							req.msgType = MsgReq.FILE;
							byte[] tmp = new byte[1024 * 1024];
							int len = fis.read(tmp);
							if (len != -1) {
								req.dataFile = new byte[len];
								System.arraycopy(tmp, 0, req.dataFile, 0, len);
							} else {
								req.dataFile = new byte[0];
								MSGConnectCenter.receiveMsg(session.fileShakeReq.sessionKey);						}
							write(req);
						} catch (Exception e) {
						}
					}
				}
			}
		}catch(Exception e){
			log.error("MSGConnectSocket.handMsg() "+(session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName)
					+ "处理消息错误", e);
			throw e;
		}
	}
	
	private void readFile(String fileName) {
		try {
			// 一次读多个字节
			byte[] tempbytes = new byte[1024 * 1024];
			int byteread = 0;
			FileInputStream in = new FileInputStream(fileName);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			
		}
	}

	
	/**
	 * 写入待发送队列.因担心，发送太快，而接收太慢的情况导致内存堆积，这里直接写而不是放队列
	 * 
	 * @param bs
	 * @param len
	 */
	public void write(MsgHeader msg) {
		queue.put(msg);
	}

	/**
	 * 写文件数据
	 * 
	 * @param bs
	 * @param count
	 */
	public void write(byte[] bs, int count) {

		if (client == null) {
			return;
		}
		if (!client.isConnected()) {
			this.close("MSGConnectSocket.write写数据,但客户端未连接");
			return;
		}
		ByteBuffer sendbuffer = ByteBuffer.allocate(count);
		sendbuffer.clear();
		sendbuffer.put(bs);
		// 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
		sendbuffer.flip();
		if (sendbuffer != null) {
			try {
				int attempts = 0;
				while (sendbuffer.hasRemaining()) {
					int len = client.write(sendbuffer);
					if (len < 0) {
						throw new EOFException();
					}
					attempts++;
					if (attempts > 500)
						throw new IOException("写消息，连续信尝试500次未成功");
				}
			} catch (Exception e) {
				closeFileConnect();
			} finally {
				sendbuffer = null;
			}
		}
		// ByteBuffer sendbuffer = ByteBuffer.allocate(count);
		// sendbuffer.clear();
		// sendbuffer.put(bs);
		// //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
		// sendbuffer.flip();
		// queue.putFile(sendbuffer);
	}

	/**
	 * 写文件数据到客户端，因担心，发送太快，而接收太慢的情况导致内存堆积，这里直接写而不是放队列
	 * 
	 */
	private void writeFileToClient() {

		if (client == null) {
			log.debug("MSGConnectSocket.writeFileToClient 接收写线程指令，但client为空"+(session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName));	
			return;
		}
		if (!client.isConnected()) {
			this.close("MSGConnectSocket.writeFileToClient写文件,但客户端未连接");
			return;
		}
		log.debug("MSGConnectSocket.writeFileToClient() 写文件" + (session == null ? "Session is Null " : "userID:"
			+ session.userId + ":userName:" + session.userName) + "时间:"+BaseDateFormat.getNowTimeLong()+":" );
		ByteBuffer sendbuffer = queue.getFile();
		if (sendbuffer != null) {
			try {
				int attempts = 0;
				while (sendbuffer.hasRemaining()) {
					int len = client.write(sendbuffer);
					// log.debug("写消息 长度:"+len);
					if (len < 0) {
						throw new EOFException();
					}
					attempts++;
					if (attempts > 500)
						throw new IOException("写消息，连续信尝试500次未成功");
				}
			} catch (Exception e) {
				closeFileConnect();
			} finally {
				sendbuffer = null;
			}
		}
	}

	/**
	 * 如果，缓存中有数据，则写入客户端中
	 * 
	 */
	private void writeToClient() {

		if (client == null) {
			log.debug("MSGConnectSocket.writeToClient 接收写线程指令，但client为空"+(session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName));			
			return;
		}
		if (!client.isConnected()) {
			this.close("MSGConnectSocket.writeToClient写消息,但客户端未连接");
			return;
		}

		MsgHeader outMsg = queue.get();
		if (outMsg != null) {

			try {
				byte b[] = outMsg.encode();
				log.debug("MSGConnectSocket.writeToClient() 写消息 " + (session == null ? "Session is Null " : "userID:"
					+ session.userId + " :userName:" + session.userName) + " 时间:"+BaseDateFormat.getNowTimeLong()+" :"+ outMsg.toString());
				
				ByteBuffer sendbuffer = ByteBuffer.allocate(b.length);
				sendbuffer.clear();
				sendbuffer.put(b);
				// 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
				sendbuffer.flip();
				long curTime = System.currentTimeMillis();
				int totalLen = 0;
				while (sendbuffer.hasRemaining()) {
					int len = client.write(sendbuffer);
					if (len < 0) {
						throw new EOFException();
					}
					totalLen +=len;
					if ((System.currentTimeMillis()-curTime) > 30000) {
						this.close("MSGConnectSocket.writeToClient写消息，30秒未传送完成，共传送字节"+totalLen+"[0]="+b[0]+"[1]="+b[1]+"[2]="+b[2]+"[3]="+b[3]+",但客户端未关闭连接");
						throw new IOException("写消息，30秒未传送完成，关闭连接");
					}
				}
			} catch (Exception e) {
				close("MSGConnectSocket.writeToClient写消息，报错"+e.getMessage());
			} finally {
				outMsg = null;
			}
		}
	}

	public static long flushChannel(SocketChannel socketChannel, ByteBuffer bb,
			long writeTimeout) throws IOException {

		SelectionKey key = null;
		Selector writeSelector = null;
		int attempts = 0;
		int bytesProduced = 0;
		try {
			while (bb.hasRemaining()) {
				int len = socketChannel.write(bb);
				attempts++;
				if (len < 0) {
					throw new EOFException();
				}
				bytesProduced += len;
				if (len == 0) {
					if (writeSelector == null) {
						writeSelector = Selector.open();
						if (writeSelector == null) {
							continue;
						}
					}

					key = socketChannel.register(writeSelector, key.OP_WRITE);
					if (writeSelector.select(writeTimeout) == 0) {
						if (attempts > 2)
							throw new IOException("Client disconnected");
					} else {
						attempts--;
					}
				} else {
					attempts = 0;
				}
			}
		} finally {
			if (key != null) {
				key.cancel();
				key = null;
			}
			if (writeSelector != null) {
				writeSelector.selectNow();
				writeSelector.close();
			}
		}
		return bytesProduced;
	}

	public void init() {
		inBuffer = null;
		queue.clear();
	}

	/**
	 * 关闭客户端连接
	 * 
	 */
	public void close(String reason) {
		try {
			log.debug("MSGConnectSocket.close() " + (session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName) + ":关闭连接;hashCode=" + this.hashCode()+";原因="+reason);
			if (null != session) {
				if (session.connect == this || session.fileTranConnect == this)
					session.close();
			}
			inBuffer = null;
			queue.clear();
			if(client != null)
				client.close();
			client = null;

			try {
				if (null != fis)
					fis.close();
				if (null != fos)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			log.error("MSGConnectSocket.close Error:",e);
		}
	}

	/**
	 * 关闭文件传输终端
	 * 
	 */
	public void closeFileConnect() {
		try {
			session.fileTranClose();
			session.fileTranConnect = null;
			session.fileShakeReq = null;
			inBuffer = null;
			queue.clear();
			client.close();
			client = null;
		} catch (Exception e) {
		}
	}

	public class ReadThread implements Runnable {
		public Future<?> future;// 用于判断当前线程状态

		public synchronized void run() {
			read();
		}
	}

	public class ReadFileThread implements Runnable {
		public Future<?> future;// 用于判断当前线程状态

		public synchronized void run() {
			readFile();
		}
	}

	public class WriteThread implements Runnable {
		public Future<?> future;// 用于判断当前线程状态

		public synchronized void run() {
			writeToClient();

		}
	}

	public class WriteFileThread implements Runnable {
		public Future<?> future;// 用于判断当前线程状态

		public synchronized void run() {
			writeFileToClient();

		}
	}
}
