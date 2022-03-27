package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;

import com.google.gson.Gson;

import model.Message;
import events.OnClose;
import events.OnGameEnded;
import events.OnMessageReceived;
import events.OnPlayerLeaving;
import main.Main;
import model.Generic;

public class Sesion extends Thread{
	
	private Message playerinfo;
	
	private Socket socket;
	
	private String id;
	
	private BufferedReader br;
	
	private BufferedWriter bw;
	
	private Main main;
	
	private boolean onGame;
	
	private OnClose close;
	
	private OnGameEnded ended;
	
	private OnMessageReceived received;
	
	private OnPlayerLeaving leaving;
	
	private boolean alreadyWorking;
	//private OnMessageSend send;

	public Sesion(Socket socket, Main main) {
		
		this.socket = socket;
		
		id = UUID.randomUUID().toString();
		
		this.main = main;
		
		setReceived(main);
		
		onGame = false;
		
		setAlreadyWorking(false);
		//main.setReceivd(this);
	}
	
	@Override
	public void run() {
		
		try {
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			//received.onOpen(this);
				
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public BufferedReader getBr() {
		return br;
	}

	public void setBr(BufferedReader br) {
		this.br = br;
	}

	public BufferedWriter getBw() {
		return bw;
	}

	public void setBw(BufferedWriter bw) {
		this.bw = bw;
	}

	public void sendMessage(String line) {

		new Thread(() -> {

			try {
				//send.messageSend(line);
				bw.write(line + "\n");
				
				//System.out.println(line);
				bw.flush();
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		;

	}

	public void setReceived(OnMessageReceived received) {
		this.received = received;
	}

	public String getSessionId() {
		return id.toString();
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isOnGame() {
		return onGame;
	}

	public void setOnGame(boolean onGame) {
		this.onGame = onGame;
	}
	
	public synchronized String readMessage() {
		
		String msgfinal = " ";
		
		//String msgCouldUse = " ";
		
		System.out.println("Esta Esperando un Mensaje");
		
		try {
			String msg = br.readLine();
			Gson gson = new Gson();
			
			msgfinal = msg;
			
			if(msgfinal!=null) {
			
			if(msgfinal.startsWith("{")) {	
				
				Generic generic = gson.fromJson(msgfinal, Generic.class);
				
				
				if(generic.type.equals("Message")) {
				
					
					Message msg2 = gson.fromJson(msgfinal, Message.class); 
					
					if(msg2.getMessageText().equals("gameEnded")) {
				
						ended.gameEnded(this);
				
					}else if(msg2.getMessageText().equals("leave")) {
				
						leaving.playerleaving(this);
				
					} 
			
				}
			}	
				
				
				
				
			}else{
				
				System.out.println("deberia entrar a aqui");
				close.OnSesionClosed(this);
			}
			
			//System.out.println(msg);
			
			 while(msg==null || msg.isEmpty() ) {
				 
				 if(msg==null) {
					 
					 //leaving.playerleaving(this);
					 
				 }
					 
					 /*
					 
					 Generic generic = gson.fromJson(msg, Generic.class);
					 //System.out.println("Entro al else");
					 
					 switch(generic.type) {
					 
					 case "Message":
						 
						 System.out.println("Entro al caso correcto");
						 Message m = gson.fromJson(msg, Message.class);
						 
						 String msg2 = m.getMessageText();
						 if(msg2.equals("gameEnded")) {
							 
							 ended.gameEnded(this);
							 
						 }
					break;	 
					 
					 }
					 */
				 
                //System.out.println("Espera en el readLine");
                 //msg = br.readLine();

                 //windows0.msgMain=msg;


             }
			System.out.println(msgfinal + " Este mensaje es del metodo readMessage"); 
			
			
			msgfinal = received.messageReceived(msg,this);
			//Thread.currentThread().interrupt();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			}
		return msgfinal;
		
		
		}

	public synchronized void setClose(OnClose close) {
		this.close = close;
	}

	public OnGameEnded getEnded() {
		return ended;
	}

	public void setEnded(OnGameEnded ended) {
		this.ended = ended;
	}

	public boolean isAlreadyWorking() {
		return alreadyWorking;
	}

	public void setAlreadyWorking(boolean alreadyWorking) {
		this.alreadyWorking = alreadyWorking;
	}

	public OnPlayerLeaving getLeaving() {
		return leaving;
	}

	public void setLeaving(OnPlayerLeaving leaving) {
		this.leaving = leaving;
	}
		
		
		
	
	
	


}
