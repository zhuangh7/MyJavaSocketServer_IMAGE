package Zhuangh7.JFrame.main;

import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;  
import java.net.Socket;
import java.util.Vector;  
  
public class ChatSocket extends Thread {  
	public static Vector<byte[]> map = new Vector<byte[]>();
	public static Vector<Integer> Size = new Vector<Integer>();
    Socket socket;  
    public static boolean tag = true;
    public ChatSocket(Socket s){  
        this.socket = s;  
    }  
      
   /* public void out(String out) {  
        try {  
        	//PrintWriter os=new PrintWriter(socket.getOutputStream());
            socket.getOutputStream().write((out+"\n").getBytes("UTF-8"));  
        	//os.println(out);
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            MainClass.print("断开了一个客户端链接");  
            ChatManager.getChatManager().remove(this);  
            e.printStackTrace();  
        }  
    }  */
    
    public void outByte(byte[] b){
    	try{
    		socket.getOutputStream().write(b);
    	}catch(IOException e){
    		MainClass.print("断开了一个客户端链接");  
            ChatManager.getChatManager().remove(this);  
            e.printStackTrace();  
    	}
    }
    
    public void outInt(int u){
    	try {
			socket.getOutputStream().write(u);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override  
    public void run() {  
    	if(ServerListener.Client_num<=ServerListener.max_client){
    		//out("你已经连接到本服务器了"); 
    		//out("你是第"+ServerListener.Client_num+"个客户");
    		if(ServerListener.Client_num==1){
    			//out("等待客户2");
    			while(ServerListener.Client_num!=2){
    			//	out(""+ServerListener.Client_num);
    				try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			//out("客户2加入到聊天");
    		}
    		else{
    			//out("与客户1配对");
    		}
    		try {  
    			new sender(this).start();
    			InputStream is = socket.getInputStream();
    			DataInputStream dis = new DataInputStream(is);
    			int size = dis.readInt();
    			while(tag){
	    			if(size!=0){
	    				if(size == -1){
	    					tag = false;
	    				}
		    			Size.add(size);
		                byte[] data = new byte[size];    
		                int len = 0;    
		                while (len < size) {    
		                    len += dis.read(data, len, size - len);    
		                }
		                map.add(data);
	    			}
	    			size = dis.readInt();
    			}
                
                
    			//String line = null;  
    			//while ((line = br.readLine()) != null) {  
    				//MainClass.print(line);  
    				//ChatManager.getChatManager().publish(this, line);  
    			//}  
	          //  br.close();
    			dis.close();
    			is.close();
	            MainClass.print("断开了一个客户端链接");  
	            ChatManager.getChatManager().remove(this);  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	        	MainClass.print("断开了一个客户端链接");  
	            ChatManager.getChatManager().remove(this);  
	            e.printStackTrace();  
	        }  
    	}
    	else{
    		//out("服务器已满！");
    		try {
				socket.close();//关闭连接
				ChatManager.getChatManager().remove(this);  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }  
}  