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
 * Ϊ������ݴ�������еġ�ճ���İ�����⣬������һ�����棬������ﵽһ���������ĳ���ʱ����ִ�ж���
 * 
 * @author Administrator
 * @preserve all
 */
public class MSGConnectSocket {

	public static final int CLIENT_NORMAL = 0;

	public static final int CLIENT_FILETRAN = 1; // �ļ�����ͨ��

	private Logger log;

	private HashMap sessionPool;

	private HashMap fileSessionPool;

	public SocketChannel client;

	public int clientType = CLIENT_NORMAL;

	private byte[] inBuffer;

	private Object rLock = new Object(); // ��ȡ��

	private Object wLock = new Object(); // ��ȡ��

	private MSGSession session;

	private long fileSize = 0;

	private long hadReadSize = 0;

	private ByteBuffer readBuffer = ByteBuffer.allocate(512 * 1024);

	public ReadThread readThread = new ReadThread();

	public ReadFileThread readFileThread = new ReadFileThread();

	public WriteFileThread writeFileThread = new WriteFileThread();

	public WriteThread writeThread = new WriteThread();

	MSGQueue queue = new MSGQueue(); // ��Ϣ����

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
	 * ��ȡ�ļ�
	 * 
	 */
	public void readFile() {
		try {
			readBuffer.clear();
			if (client == null) {
				return;
			}

			synchronized (rLock) {
				int count = client.read(readBuffer);// ��ͨ���ж�ȡ����
				if (count == 0) {
					return;
				}
				readBuffer.flip();// �����ݷ�ת�����ڶ���
				if (hadReadSize >= fileSize) {
					session.fileTransport(readBuffer.array(), count, true);
				} else {
					session.fileTransport(readBuffer.array(), count, false);
				}
				hadReadSize += count;
				if (hadReadSize >= fileSize) {
					log.debug("MSGConnectSocket.readFile() "+(session == null ? "Session is Null " : "userID:"
							+ session.userId + ":userName:" + session.userName)
							+ "�ļ���ȡ��ϣ��ر�����");
					closeFileConnect();
				}
			}
		} catch (Exception e) {
			log.error("MSGConnectSocket.readFile() "+(session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName)
					+ "���ļ�", e);

		}
	}

	/**
	 * ��ȡ�ӿͻ��˹��������ݣ�������ճ�����Ͱ���� ����ԭ��Ϊ��
	 * 1.��bs�����ȡ���ݻ����У����������棬����ж����Ϣ����ִ�ж�Σ��������ȫ����Ϣ����ȴ���һ�λ���
	 * 
	 * @param bs
	 */
	public void read() {
		try {
			readBuffer.clear();
			if (client == null) {
				log.info("MSGConnectSocket.read() ����Ϣ��clientΪ��" + (session == null ? "Session is Null " : "userID:"
					+ session.userId + ":userName:" + session.userName)  );
				return;
			}
			synchronized (rLock) {
				int count = client.read(readBuffer);// ��ͨ���ж�ȡ����
				if (count < 0) {
					this.close("MSGConnectSocket.read ����С��0�����ݣ�˵����ͨ�������쳣�����ܷ������ж�");
					return;
				}
				readBuffer.flip();// �����ݷ�ת�����ڶ���
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
						// ����12���ֽڣ��϶�����һ�������İ�
						inBuffer = bs;
						return;
					}
					int length = MsgHeader.getLength(bs);
					if (bs.length < length) {
						// �����Ϣ���ȴ���byte�ĳ��ȣ�˵�����Ǹ��������Ҫ�ȴ���һ����������������ִ��
						inBuffer = bs;
						return;
					} else if (bs.length == length) {
						// �������˵����������һ�������İ�,�������Ϣ
						inBuffer = null;
						handMsg(bs);
						return;
					} else {
						// �����˳��ȣ�˵�����Ǹ�ճ�����ֶδ���
						byte[] temp = new byte[length];
						System.arraycopy(bs, 0, temp, 0, length);
						// ����˰�
						handMsg(temp);

						temp = new byte[bs.length - length];
						System.arraycopy(bs, length, temp, 0, bs.length
								- length);
						bs = temp;
					}
				}
			}
		} catch (Exception e) {
			close("MSGConnectSocket.read ����try���� "+e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void handMsg(byte[] bs) throws Exception{
		try{
			int command = MsgHeader.getCommand(bs);
			// �����������Ϣ���轨���û��ĻỰ��Ϣ��
			if (command == MsgHeader.HANDSHAKE) {
				HandShakeReq msg = new HandShakeReq();
				msg.decode(bs);
				// ���û��Ự����ȡ���ݣ��������µĻỰ�������ڴ�й©
				session = (MSGSession) sessionPool.get(msg.userId);
				String userName = MSGConnectCenter.employeeMap.get(msg.userId).name;
				
				log.debug("MSGConnectSocket.handMsg() ���յ�������Ϣ userID:" + msg.userId + ":userName:"
						+ userName + "ʱ��:"+BaseDateFormat.getNowTimeLong()+":" + msg.toString());
				
				if (session == null) {
					session = new MSGSession(msg.userId, userName, log,
							sessionPool, null, this);
					sessionPool.put(msg.userId, session);
				} else {
					if (session.connect != this) { // ��һ���ͻ��˵�¼ʱ����֤���û��Ƿ��Ѿ���¼
						ForceOfflineReq req = new ForceOfflineReq();
						if (session.connect != null)
							session.connect.write(req);	// �ÿͻ����Լ������Ӹ�����
					}
					session.init(userName, this);
				}
				session.handShake(msg);
	
			} else if (session != null && session.connect != null) {
				// �ҵ���Ӧ�ĻỰ��Ϣ����������Ϣ
				if (session != null) {
					session.clientMsgHandler(bs);
				} else {
					log.error("��������Ϣ��sessionΪ�ա�����");
				}
			} else if (command == MsgHeader.FILESHAKE) { // ������ļ�����֪ͨ��Ϣ����˵������һ����ͨ����ר�������ļ�����
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
				
				log.debug("MSGConnectSocket.handMsg() ���յ��ļ�����������Ϣ userID:" + userId + ":userName:"
						+ userName + "ʱ��:"+BaseDateFormat.getNowTimeLong()+":" + msg.toString());
				
				if (session == null) {
					session = new MSGSession(userId, userName, log, null,
							fileSessionPool, this);
					fileSessionPool.put(mapKey, session);
				} else {
					if (null != session.fileTranConnect && session.fileTranConnect != this) {
						session.fileTranConnect.close("�ļ�����ͨ������Ϊ���Ҳ������Լ�");
					}
				}
				readBuffer = ByteBuffer.allocate(5 * 1024 * 1024); // �Ӵ��ļ�����Ϊ5M.
				session.initFileTran(msg, this);
				session.fileShake(msg); // Ӧ��
	
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
					log.error("MSGConnectSocket.handMsg() ����������Ϣ���� userID:" + userId + ":userName:"
							+ userName + "ʱ��:"+BaseDateFormat.getNowTimeLong()+":" + msg.toString(),e);
				}
			} else if (session != null && session.fileTranConnect != null) {// �ļ�����
				session.lastActiveTime = System.currentTimeMillis();
				session.lostHeartbeatTimes = 0; // �����������ʹ���û�Ͽ�
				if (command == MsgHeader.MSG) {
					MsgReq req = new MsgReq();
					req.decode(bs);
					log.debug("MSGConnectSocket.handMsg() ���յ��ļ���Ϣ userID:" + (session == null ? "Session is Null " : "userID:"
						+ session.userId + ":userName:" + session.userName) + "ʱ��:"+BaseDateFormat.getNowTimeLong()+":" + req.toString());
					if (FileShakeReq.SEND == session.fileShakeReq.direct) {
						// ת��
						String recvKey = session.fileShakeReq.sessionKey + "$$"
								+ session.fileShakeReq.receiveId;
						MSGSession ms = (MSGSession) fileSessionPool.get(recvKey);
						if (ms != null) {
							ms.fileTranConnect.write(req);
						} else {
							// ͨ��û�������ѷ��Ͷ˸�����
							close("�ļ�����ͨ��û�������ѷ��Ͷ˸�����");
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
						rsp.result = IfResult.RECEIVE; // ��ʶ���յ�
						write(rsp);
						
						if (0 == req.dataLength) {
							// �ļ����������־
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
						log.debug("MSGConnectSocket.handMsg() ���յ��ļ�Ӧ����Ϣ " + (session == null ? "Session is Null " : "userID:"
							+ session.userId + ":userName:" + session.userName) + "ʱ��:"+BaseDateFormat.getNowTimeLong()+":" + rsp.toString());
						String recvKey = session.fileShakeReq.sessionKey + "$$"
								+ session.fileShakeReq.sendId;
						MSGSession ms = (MSGSession) fileSessionPool.get(recvKey);
						if (ms != null) {
							ms.fileTranConnect.write(rsp);
						} else {
							// �����߹ر�ͨ�����رշ���
							close("�ļ����䷢���߹ر�ͨ�����رշ���");
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
					+ "������Ϣ����", e);
			throw e;
		}
	}
	
	private void readFile(String fileName) {
		try {
			// һ�ζ�����ֽ�
			byte[] tempbytes = new byte[1024 * 1024];
			int byteread = 0;
			FileInputStream in = new FileInputStream(fileName);
			// �������ֽڵ��ֽ������У�bytereadΪһ�ζ�����ֽ���
			while ((byteread = in.read(tempbytes)) != -1) {
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			
		}
	}

	
	/**
	 * д������Ͷ���.���ģ�����̫�죬������̫������������ڴ�ѻ�������ֱ��д�����ǷŶ���
	 * 
	 * @param bs
	 * @param len
	 */
	public void write(MsgHeader msg) {
		queue.put(msg);
	}

	/**
	 * д�ļ�����
	 * 
	 * @param bs
	 * @param count
	 */
	public void write(byte[] bs, int count) {

		if (client == null) {
			return;
		}
		if (!client.isConnected()) {
			this.close("MSGConnectSocket.writeд����,���ͻ���δ����");
			return;
		}
		ByteBuffer sendbuffer = ByteBuffer.allocate(count);
		sendbuffer.clear();
		sendbuffer.put(bs);
		// ������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
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
						throw new IOException("д��Ϣ�������ų���500��δ�ɹ�");
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
		// //������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
		// sendbuffer.flip();
		// queue.putFile(sendbuffer);
	}

	/**
	 * д�ļ����ݵ��ͻ��ˣ����ģ�����̫�죬������̫������������ڴ�ѻ�������ֱ��д�����ǷŶ���
	 * 
	 */
	private void writeFileToClient() {

		if (client == null) {
			log.debug("MSGConnectSocket.writeFileToClient ����д�߳�ָ���clientΪ��"+(session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName));	
			return;
		}
		if (!client.isConnected()) {
			this.close("MSGConnectSocket.writeFileToClientд�ļ�,���ͻ���δ����");
			return;
		}
		log.debug("MSGConnectSocket.writeFileToClient() д�ļ�" + (session == null ? "Session is Null " : "userID:"
			+ session.userId + ":userName:" + session.userName) + "ʱ��:"+BaseDateFormat.getNowTimeLong()+":" );
		ByteBuffer sendbuffer = queue.getFile();
		if (sendbuffer != null) {
			try {
				int attempts = 0;
				while (sendbuffer.hasRemaining()) {
					int len = client.write(sendbuffer);
					// log.debug("д��Ϣ ����:"+len);
					if (len < 0) {
						throw new EOFException();
					}
					attempts++;
					if (attempts > 500)
						throw new IOException("д��Ϣ�������ų���500��δ�ɹ�");
				}
			} catch (Exception e) {
				closeFileConnect();
			} finally {
				sendbuffer = null;
			}
		}
	}

	/**
	 * ����������������ݣ���д��ͻ�����
	 * 
	 */
	private void writeToClient() {

		if (client == null) {
			log.debug("MSGConnectSocket.writeToClient ����д�߳�ָ���clientΪ��"+(session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName));			
			return;
		}
		if (!client.isConnected()) {
			this.close("MSGConnectSocket.writeToClientд��Ϣ,���ͻ���δ����");
			return;
		}

		MsgHeader outMsg = queue.get();
		if (outMsg != null) {

			try {
				byte b[] = outMsg.encode();
				log.debug("MSGConnectSocket.writeToClient() д��Ϣ " + (session == null ? "Session is Null " : "userID:"
					+ session.userId + " :userName:" + session.userName) + " ʱ��:"+BaseDateFormat.getNowTimeLong()+" :"+ outMsg.toString());
				
				ByteBuffer sendbuffer = ByteBuffer.allocate(b.length);
				sendbuffer.clear();
				sendbuffer.put(b);
				// ������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
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
						this.close("MSGConnectSocket.writeToClientд��Ϣ��30��δ������ɣ��������ֽ�"+totalLen+"[0]="+b[0]+"[1]="+b[1]+"[2]="+b[2]+"[3]="+b[3]+",���ͻ���δ�ر�����");
						throw new IOException("д��Ϣ��30��δ������ɣ��ر�����");
					}
				}
			} catch (Exception e) {
				close("MSGConnectSocket.writeToClientд��Ϣ������"+e.getMessage());
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
	 * �رտͻ�������
	 * 
	 */
	public void close(String reason) {
		try {
			log.debug("MSGConnectSocket.close() " + (session == null ? "Session is Null " : "userID:"
				+ session.userId + ":userName:" + session.userName) + ":�ر�����;hashCode=" + this.hashCode()+";ԭ��="+reason);
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
	 * �ر��ļ������ն�
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
		public Future<?> future;// �����жϵ�ǰ�߳�״̬

		public synchronized void run() {
			read();
		}
	}

	public class ReadFileThread implements Runnable {
		public Future<?> future;// �����жϵ�ǰ�߳�״̬

		public synchronized void run() {
			readFile();
		}
	}

	public class WriteThread implements Runnable {
		public Future<?> future;// �����жϵ�ǰ�߳�״̬

		public synchronized void run() {
			writeToClient();

		}
	}

	public class WriteFileThread implements Runnable {
		public Future<?> future;// �����жϵ�ǰ�߳�״̬

		public synchronized void run() {
			writeFileToClient();

		}
	}
}
