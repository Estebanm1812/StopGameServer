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
import events.OnPlayerLeaving;
import model.Game;
import model.Generic;
import model.Message;

public class Main implements OnMessageReceived, OnClose, OnGameEnded, OnPlayerLeaving{
	
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
					
					addTheNewSesion(session);
					
					
				}
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void createGame() {
		
		//System.out.println("Entro");
		
		new Thread(() -> {
				
			//System.out.println("En este momento hay: " + sessions.size() + " jugadores");
				
				Sesion sesionA = null;
				
				Sesion sesionB = null;
				
				//System.out.println("Va a entrar al for");
				
				//System.out.println(sessions.size() + " Cantidad de jugadores");
				
				boolean out = false;
				
				for(int i=0; i < sessions.size() && out==false;i++) {
					
					Sesion tmp = sessions.get(i);
					
					//System.out.println("Esta en el index " + i);
					
					if(tmp.isOnGame()==false) {
						
						sesionA = tmp;
						sesionA.setOnGame(true);
						System.out.println(sesionA.isOnGame());
						sessions.get(i).setOnGame(true);
						out = true;
						
					}
					
					
				}
				out = false;
				
				
				//System.out.println("Va a entrar al segundo for");
				
				for(int i=0; i < sessions.size() && out==false;i++) {
					
					Sesion tmp = sessions.get(i);
					
					//System.out.println(tmp.getName());
					
					//System.out.println("Esta en el index " + i);
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
					
					
				Game game = new Game(sesionA,sesionB);
					
					
				}
				
			
			
			
		}).start();
		;
	}
	
	//Este metodo se podria quitar si funciona
	
	public void startGame(Sesion sesionA,Sesion sesionB) {
		
		Message msg = new Message("sendPlayer");
		
		Gson gson = new Gson();
		
		String message = gson.toJson(msg);
		
		//System.out.print("SendPlayer");
		
		sesionA.sendMessage(message);
		
		String playerA = sesionA.readMessage();
		
		//System.out.println("");
		
		sesionB.sendMessage(message);
		
		String playerB = sesionB.readMessage();
		
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'a');
		
		System.out.println(c + " esta letra va a enviar");
		
		//System.out.println(playerA);
		
		//System.out.println(playerB);
		
		sesionA.sendMessage(playerB+"//"+c);
		
		sesionB.sendMessage(playerA+"//"+c);
		
		String aEnded = " ";
		
		String bEnded = " ";
		new Thread(() -> {
		
			
			
		String a = sesionA.readMessage();
		
			//if(bEnded.contains("Answer")) {
				
			
			//}
		
		//System.out.println("Envio mensaje 1");
		
		sesionB.sendMessage(a);
		
		
		//new Thread(() -> {
			
			String b = sesionB.readMessage();
			
			//System.out.println("Envio mensaje 2");
			sesionA.sendMessage(b);
			sesionB.sendMessage("");
			
			//}).start();
		
			
		System.out.println("En este sitio esta esperando la respuesta de la pantalla final");	
		sesionA.readMessage();
		sesionB.readMessage();
			
		}).start();
		
		
		
		new Thread(() -> {
			
			String b = sesionB.readMessage();
			
			//System.out.println("Envio mensaje 3");
			sesionA.sendMessage(b);
			
			//new Thread(() -> {
				
				String a = sesionA.readMessage();
				
				
				//System.out.println("Envio mensaje 4");
				
				sesionB.sendMessage(a);
				
					
			//}).start();
				
		sesionB.readMessage();	
		//sesionA.readMessage();		
			}).start();
		
		//sesionA.readMessage();
		//sesionB.readMessage();
		
		
	}
		
	

	@Override
	public String messageReceived(String msg, Sesion sessions) {
		
		
		return msg;
		
		
	}
			

	@Override
	public void OnSesionClosed(Sesion sesion) {
		
		System.out.println(sessions.size() + " antes del remove");
		
		boolean out = false;
		
		for(int i=0; i < sessions.size() && out == false;i++) {
			
			if(sessions.get(i).equals(sesion)) {
				Sesion tmp = sessions.get(i);
				
			
				
				sessions.get(i).setOnGame(false);
				
			
				sessions.remove(i);
				System.out.println(sessions.size() + " despues del remove");
				
				
				
				
				out = true;
				//System.out.println("Cambio de estado, ahora es: " + sessions.get(i).isOnGame() );
				
				//System.out.println(i);
				
				//addTheNewSesion(sesion);
				
			}
			
		}
		
	}
		
	

	@Override
	public void gameEnded(Sesion sesion) {
		
		boolean out = false;
		
		System.out.println("entro al gameEnded");
		
		for(int i=0; i < sessions.size() && out == false;i++) {
			
			if(sessions.get(i).equals(sesion)) {
			
				sessions.get(i).setOnGame(false);
				sessions.remove(i);
			
				out = true;
				//System.out.println("Cambio de estado, ahora es: " + sessions.get(i).isOnGame() );
				
				System.out.println(i);
				
				addTheNewSesion(sesion);
				
			}
			
		}
		//createGame();
		
		
	}
	
	
	
	public void addTheNewSesion(Sesion sesion){
		
		sesion.setReceived(this);
		sesion.setEnded(this);
		sesion.setClose(this);
		sesion.setLeaving(this);
		
		if(sesion.isAlreadyWorking()==true) {
			
		}else {
		
			sesion.start();	

		}
		
				
		sesion.setAlreadyWorking(true);
		
		System.out.println(sessions.size() + " Antes del Add" );
		
		sessions.add(sesion);
		
		System.out.println(sessions.size() + " Despues del Add" );
		
		//new Thread(() -> {
		
		int count = 0;	
			
		for(int i = 0; i < sessions.size();i++) {
		
			Sesion tmp = sessions.get(i);
			
			if(tmp.isOnGame()==false) {
				
				count++;
			}
			
			
		}
		
		System.out.println("la cantidad jugadores disponibles es: " + count);
		
		if(count%2==0) {
			
		createGame();
		
		}
		
		//}).start();
		
	}

	@Override
	public void playerleaving(Sesion sesion) {
		
		System.out.println(sessions.size() + " antes del remove");
		
		boolean out = false;
		
		for(int i=0; i < sessions.size() && out == false;i++) {
			
			if(sessions.get(i).equals(sesion)) {
			
				sessions.get(i).setOnGame(false);
				sessions.remove(i);
				System.out.println(sessions.size() + " antes del remove");

				
				out = true;
				//System.out.println("Cambio de estado, ahora es: " + sessions.get(i).isOnGame() );
				
				System.out.println(i);
				
				//addTheNewSesion(sesion);
				
			}
			
		}
		
	}
	
}
