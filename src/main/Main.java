package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

import comm.Sesion;
import events.OnClose;
import events.OnGameEnded;
import events.OnMessageReceived;
import model.Generic;
import model.Message;

public class Main implements OnMessageReceived, OnClose, OnGameEnded{
	
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
					session.setEnded(this);
					
					session.start();	
					
					sessions.add(session);
					
					new Thread(() -> {
					
					if(sessions.size()%2==0) {
						
					createGame();
					
					}
					
					}).start();
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
				
				Sesion sesionA = null;
				
				Sesion sesionB = null;
				
				System.out.println("Va a entrar al for");
				
				System.out.println(sessions.size() + " Cantidad de jugadores");
				
				boolean out = false;
				
				for(int i=0; i < sessions.size() && out==false;i++) {
					
					Sesion tmp = sessions.get(i);
					
					System.out.println("Esta en el index " + i);
					
					if(tmp.isOnGame()==false) {
						
						sesionA = tmp;
						sesionA.setOnGame(true);
						System.out.println(sesionA.isOnGame());
						sessions.get(i).setOnGame(true);
						out = true;
						
					}
					
					
				}
				out = false;
				
				
				System.out.println("Va a entrar al segundo for");
				
				for(int i=0; i < sessions.size() && out==false;i++) {
					
					Sesion tmp = sessions.get(i);
					
					System.out.println(tmp.getName());
					
					System.out.println("Esta en el index " + i);
					if(tmp.isOnGame()==false) {
						
						sesionB = tmp;
						sesionB.setOnGame(true);
						sessions.get(i).setOnGame(true);
						out = true;
						
					}
					
					
				
				}
				if(sesionA==null) {
					
					System.out.println("la sesionA es nula");
				}
				if(sesionB==null) {
					
					System.out.println("La sesionB es nula");
				}
				
				if(sesionA!=null && sesionB!=null) {
					
					
					startGame(sesionA,sesionB);
					
					
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
		
		
		System.out.println("Envio mensaje 1");
		
		sesionB.sendMessage(a);
		
		
		new Thread(() -> {
			
			String b = sesionB.readMessage();
			
			System.out.println("Envio mensaje 2");
			sesionA.sendMessage(b);
				
			}).start();
		
		sesionA.readMessage();
			
		}).start();
		
		
		
		new Thread(() -> {
			
			String b = sesionB.readMessage();
			
			System.out.println("Envio mensaje 3");
			sesionA.sendMessage(b);
			
			new Thread(() -> {
				
				String a = sesionA.readMessage();
				
				
				System.out.println("Envio mensaje 4");
				
				sesionB.sendMessage(a);
				
					
			}).start();
		sesionB.readMessage();	
				
			}).start();
		
		
		
	}
		
	

	@Override
	public String messageReceived(String msg, Sesion sessions) {
		
		
		return msg;
		
		
	}
			
	public void removeFromSessions() {
		
	}

	@Override
	public void OnSesionClosed(Sesion sesion) {
		
		sessions.remove(sesion);
		
	}

	@Override
	public void gameEnded(Sesion sesion) {
		
		boolean out = false;
		
		for(int i=0; i < sessions.size() && out == false;i++) {
			
			if(sessions.get(i).equals(sesion)) {
			
				//sessions.get(i).setOnGame(false);
				sessions.remove(i);
				out = true;
				System.out.println("Cambio de estado, ahora es: " + sessions.get(i).isOnGame() );
				
			}
			
		}
		createGame();
		
		
	}	
	
}
