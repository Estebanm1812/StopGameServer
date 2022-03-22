package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import comm.Sesion;
import events.OnMessageReceived;

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
					
					session.setReceived(app);
					
					
					session.start();	
					
					sessions.add(session);
					
					
				}
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void createGame() {
		
		new Thread(() -> {

			if(sessions.size()%2==0) {
				
				Sesion sesionA = null;
				
				Sesion sesionB = null;
				
				for(int i=0; i < sessions.size();i++) {
					
					Sesion tmp = sessions.get(i);
					
					if(tmp.isOnGame()==false) {
						
						sesionA = tmp;
						sesionA.setOnGame(true);
						
					}
					
					
				}
				
				for(int i=0; i < sessions.size();i++) {
					
					Sesion tmp = sessions.get(i);
					
					if(tmp.isOnGame()==false) {
						
						sesionB = tmp;
						sesionB.setOnGame(true);
						
					}
					
					
					
					
				}
				if(sesionA!=null && sesionB!=null) {
					
					
					
					
				}
				
			}
			
			
		}).start();
		;
	}
	
	public void startGame(Sesion sesionA,Sesion sesionB) {
		
		
	}
			
		
	
}