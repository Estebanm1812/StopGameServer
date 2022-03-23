package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

import comm.Sesion;
import events.OnMessageReceived;
import model.Generic;
import model.Message;

public class Main implements OnMessageReceived{
	
	static Main app;
	
	public ArrayList<Sesion> sessions;
	
	
	
	public static void main(String[] args) {
		
		
		app = new Main();
		
		
		
		app.addConnection();
		
	}
	
	public void addConnection() {
		
		sessions = new ArrayList<Sesion>();
		
		while(true) {
			ServerSocket server;
			try {
				server = new ServerSocket(6000);
				while(true) {
					System.out.println("Esperando Cliente");
					Socket socket = server.accept();
					System.out.println("Nuevo Cliente Conectado! ");
					System.out.println("Entro en el puerto: " + socket.getPort());
					
					Sesion session = new Sesion(socket,app);
					
					if(session==null) {
						System.out.println("la sesion es nula");
					}
					
					session.setReceived(app);
					
					
					session.start();	
					
					sessions.add(session);
					
					createGame();
					
				}
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void createGame() {
		
		System.out.println("Entro");
		
		new Thread(() -> {
				
			System.out.println("En este momento hay: " + sessions.size() + " jugadores");
			if(sessions.size()%2==0) {
				
				Sesion sesionA = null;
				
				Sesion sesionB = null;
				
				//System.out.println("Va a entrar al for");
				
				for(int i=0; i < sessions.size() && sesionA==null;i++) {
					
					Sesion tmp = sessions.get(i);
					
					
					if(tmp.isOnGame()==false) {
						
						sesionA = tmp;
						sesionA.setOnGame(true);
						
					}
					
					
				}
				
				
				//System.out.println("Va a entrar al segundo for");
				
				for(int i=0; i < sessions.size() && sesionB==null;i++) {
					
					Sesion tmp = sessions.get(i);
					
					
					if(tmp.isOnGame()==false) {
						
						sesionB = tmp;
						sesionB.setOnGame(true);
						
						
					}
					
					
				
				}
				

				if(sesionA!=null && sesionB!=null) {
					
					
					startGame(sesionA,sesionB);
					
					
				}
				
			}
			
			
		}).start();
		;
	}
	
	public void startGame(Sesion sesionA,Sesion sesionB) {
		
		Message msg = new Message("sendPlayer");
		
		Gson gson = new Gson();
		
		String message = gson.toJson(msg);
		
		//System.out.print("SendPlayer");
		
		sesionA.sendMessage(message);
		
		String playerA = sesionA.readMessage();
		
		sesionB.sendMessage(message);
		
		String playerB = sesionB.readMessage();
		
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'a');
		
		System.out.println(c);
		
		//System.out.println(playerA);
		
		//System.out.println(playerB);
		
		sesionA.sendMessage(playerB+"//"+c);
		
		sesionB.sendMessage(playerA+"//"+c);
		
		new Thread(() -> {
			
		String a = sesionA.readMessage();
		
		sesionB.sendMessage(a);
			
		}).start();
		
		new Thread(() -> {
			
		String b = sesionB.readMessage();
		
		sesionA.sendMessage(b);
			
		}).start();
		
		
	}

	@Override
	public String messageReceived(String msg, Sesion sessions) {
		
		
		return msg;
		
		
	}
			
		
	
}
