package com.menyi.msgcenter.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import org.apache.log4j.Logger;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Administrator
 * @preserve all
 */
public class MSGConnectServer extends Thread {
    private Selector selector; 
    private HashMap sessionPool;
    private HashMap fileSessionPool;
    private Logger log;
    private int port;
    private boolean go = true;
    ServerSocketChannel serverSocketChannel;
    
	public static int MAX_POOL_SIZE = 10;
	public static int FILE_MAX_POOL_SIZE = 20;//文件转输专用线程池
	private ExecutorService threadPool = Executors.newFixedThreadPool(MAX_POOL_SIZE); //创建线程池
	private ExecutorService fileTranThreadPool = Executors.newFixedThreadPool(FILE_MAX_POOL_SIZE); //创建线程池
    
    /**
     * @param port
     * @param sessionPool
     * @return boolean
     * @roseuid 49D1932801C5
     */
    public boolean init(int port, HashMap sessionPool, Logger log,HashMap fileSessionPool) {
        this.log = log;
        this.port = port;
        this.sessionPool =  sessionPool;
        this.fileSessionPool=fileSessionPool;
        
        this.setName("MSGConnectServerSocketThread");
        //建立soket服务
        try {
	        //通过open()方法找到Selector  
	        selector = Selector.open();  
	        // 打开服务器套接字通道  
	        serverSocketChannel = ServerSocketChannel.open();  
	        // 服务器配置为非阻塞  
	        serverSocketChannel.configureBlocking(false);  
	        // 检索与此通道关联的服务器套接字  
	        ServerSocket serverSocket = serverSocketChannel.socket();  
	        // 进行服务的绑定  
	        serverSocket.bind(new InetSocketAddress(port));  
	        serverSocket.setReuseAddress(true);
	        // 注册到selector，等待连接  
	        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  

            return true;
        } catch (Exception ex) {
            log.error("MSGConnectServer.init Create ServerSoket Error ",ex);
            return false;
        }
    }

    /**
     * @roseuid 49D1937300AB
     */
    public void run() {
        while (go) {
            try {
            	//选择一组键，并且相应的通道已经打开  
                selector.select();  
                // 返回此选择器的已选择键集。  
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();  
                while (iterator.hasNext()) {          
                    SelectionKey selectionKey = iterator.next();  
                    iterator.remove(); 
                    handleKey(selectionKey);                      
                }  
	        	try{Thread.sleep(1);}catch(Exception ex){}
            } catch (Exception ex) {
                log.error("MSGConnectServer.run accept client error ",ex);
            }
        }
    }
    
    // 处理请求  
    private void handleKey(SelectionKey selectionKey)  {  
    	//接受请求  
        ServerSocketChannel server = null;  
        SocketChannel client = null;  
    	try{
	        
	        // 测试此键的通道是否已准备好接受新的套接字连接。  
	        if (selectionKey.isAcceptable()) {  
	            // 返回为之创建此键的通道。  
	            server = (ServerSocketChannel) selectionKey.channel();  
	            // 接受到此通道套接字的连接。  
	            // 此方法返回的套接字通道（如果有）将处于阻塞模式。  
	            client = server.accept();  
	            // 配置为非阻塞  
	            client.configureBlocking(false);  
	            // 注册到selector，等待连接  
	            SelectionKey key = client.register(selector, SelectionKey.OP_READ);	
	        } else if (selectionKey.isReadable()) {  
	            // 返回为之创建此键的通道。  
	            client = (SocketChannel) selectionKey.channel();  
	            try {
	            	
	            	MSGConnectSocket attach=null;
	            	if(selectionKey.attachment() == null){
	            		//第一次读取数据，还未建立会话信息
	            		attach = new MSGConnectSocket(log,sessionPool,client,fileSessionPool);
	            		client.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ);	
	            		selectionKey.attach(attach);
	            	}else{
	            		attach = (MSGConnectSocket)selectionKey.attachment();
	            	}
	            	if(attach.clientType==MSGConnectSocket.CLIENT_FILETRAN){
//	            		client.register(selector, SelectionKey.OP_READ);	
	            		attach.readThread.future = fileTranThreadPool.submit(attach.readThread);//采用文件传输专用线程池，执行读写操作
	            	}else{	            	
	            		attach.readThread.future = threadPool.submit(attach.readThread);//采用线程池，执行读写操作
	            	}
					
				} catch (Exception ex) {
					log.error("MSGConnectThread.run read InputStream Error ", ex);
					//这里只要报错，就关闭soket通道，要求客户端重连，因为象丢包等情况，如果soket不重连，
					//则无法定位消息起点。而产生所有消息错位
					try{
						MSGConnectSocket attach = (MSGConnectSocket)selectionKey.attachment();
						if(attach != null){
							attach.close("MSGConnectServer.handleKey 报错，被迫关闭"+ex);
						}
		    			selectionKey.attach(null);
		    		}catch(Exception ee){}
				}
	        }else  if (selectionKey.isWritable()){	
	        	if(selectionKey.attachment() != null){
	        		MSGConnectSocket attach = (MSGConnectSocket)selectionKey.attachment();
	        		if(attach.clientType==MSGConnectSocket.CLIENT_FILETRAN){
	            		attach.writeThread.future = fileTranThreadPool.submit(attach.writeThread);//采用文件传输专用线程池，执行读写操作
	            	}else{	            	
	            		attach.writeThread.future =threadPool.submit(attach.writeThread);//采用线程池，执行读写操作
	            	}	        		
            	}	    
	        	try{Thread.sleep(5);}catch(Exception ex){}    	
	        }else{
	        	log.warn( "接收到其它事件:"+selectionKey.readyOps());
	        }
    	}catch(Exception e){
    		log.error("MSGConnectThread.run read InputStream Error ", e);
    		try{
    			selectionKey.attach(null);
    			selectionKey.cancel();
    			if (null!=client)
    				client.close();	// PWY
    		}catch(Exception ee){
    		}
    	}
    }  

    public void destroyObj(){
        go = false;
		try {

			Iterator it = sessionPool.keySet().iterator();
			while(it.hasNext()) {
				Object o = it.next();				
				MSGSession ms = (MSGSession)sessionPool.get(o) ;
				ms.connect.close("MSGConnectServer.destroyObj 销毁所有消息连接");
			}
			sessionPool.clear();
			
			it = fileSessionPool.keySet().iterator();
			while(it.hasNext()) {
				Object o = it.next();				
				MSGSession ms = (MSGSession)fileSessionPool.get(o) ;
				ms.fileTranConnect.close("MSGConnectServer.destroyObj 销毁所有文件连接");
			}			
			fileSessionPool.clear();
			
			selector.close();
			serverSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

    
}
