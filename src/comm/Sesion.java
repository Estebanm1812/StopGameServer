package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;

import events.OnMessageReceived;

import main.Main;

public class Sesion extends Thread{

	private Socket socket;
	
	private String id;
	
	private BufferedReader br;
	
	private BufferedWriter bw;
	
	private Main main;
	
	private boolean onGame;
	
	private OnMessageReceived received;
	//private OnMessageSend send;

	public Sesion(Socket socket, Main main) {
		
		this.socket = socket;
		
		id = UUID.randomUUID().toString();
		
		this.main = main;
		
		setReceived(main);
		
		onGame = false;
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
				
				System.out.println(line);
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
	
	public String readMessage() {
		
		String msgfinal = " ";
		try {
			String msg = br.readLine();
			System.out.println(msg);
			 while(msg==null || msg.isEmpty() ) {

                 System.out.println("Espera en el readLine");
                 msg = br.readLine();

                 //windows0.msgMain=msg;


             }
			msgfinal = received.messageReceived(msg,this);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			}
		return msgfinal;
		}
		
		
		
	
	
	


}
