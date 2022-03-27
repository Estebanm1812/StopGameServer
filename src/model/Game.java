package model;
import java.util.Random;
import com.google.gson.Gson;
import comm.Sesion;

 public class Game {

 	//private int state;
 	private static int state;

 	private Sesion sesionA;

	private Sesion sesionB;
	
	private Thread aWaiting;
	
	private Thread bWaiting;
	
	public Game(Sesion sesionA, Sesion sesionB) {
		
		this.sesionA = sesionA;
		
		this.sesionB = sesionB;
		
		state = 0;
		
		startGame();
	}
	
	public synchronized void startGame() {
		
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
		
		
		aWaiting = new Thread() {
		
			public void run() {
			
				synchronized(this) {
					String a = sesionA.readMessage();
			
					//System.out.println("Este mensaje deberia activar el if " + a);
					if(a.contains("Answer")) {
						//System.out.println("Entra en el if");
						bWaiting.interrupt();
						state = 1;
					}
		
		//System.out.println("Envio mensaje 1");
		
					sesionB.sendMessage(a);
		
		
		//new Thread(() -> {
			
					String b = sesionB.readMessage();
			
			//System.out.println("Envio mensaje 2");
					sesionA.sendMessage(b);
			//sesionB.sendMessage("");
			
			//}).start();
		
			
		//System.out.println("En este sitio esta esperando la respuesta de la pantalla final");	
		//
		//
					
					new Thread(() -> {
					
						sesionA.readMessage();	
						
						
					}).start();	
					
					new Thread(() -> {
					
						sesionB.readMessage();
						
					}).start();	
					
				}
			}
		};
		
		
		bWaiting = new Thread() {
			
			public void run() {
			
				synchronized(this) {
				
				String b = sesionB.readMessage();
			
				//System.out.println("Este mensaje deberia activar el if " + b);

				if(b.contains("Answer")) {
					//System.out.println("Entra en el if");
					state = 2;
					aWaiting.interrupt();
				}
		
		//System.out.println("Envio mensaje 1");
		
				sesionA.sendMessage(b);
		
		
		//new Thread(() -> {
		
				String a = sesionA.readMessage();
			
			//System.out.println("Envio mensaje 2");
				sesionB.sendMessage(a);
			//sesionB.sendMessage("");
			
			//}).start();
		
			
		//System.out.println("En este sitio esta esperando la respuesta de la pantalla final");	
		//sesionA.readMessage();
		//sesionB.readMessage();
				
				
				new Thread(() -> {
					
					sesionA.readMessage();	
					
					
				}).start();	
				
				new Thread(() -> {
				
					sesionB.readMessage();
					
				}).start();	
				
				}
			}
		};
		aWaiting.start();
 		bWaiting.start();
 		
 		/*
 		
 		while(state==0) {

 			//System.out.println("Esta dando vueltas en el while");
 			//System.out.println(state);
 			if(state==1) {
 				System.out.println("El hilo B tendria que haberse detenido, B esta " + bWaiting.isAlive());
 				bWaiting.interrupt();
				
			}else if(state==2) {
				aWaiting.interrupt();
				System.out.println("El hilo A tendria que haberse detenido, A esta " + aWaiting.isAlive());
			}
			
		}
		*/
		
		
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Sesion getSesionA() {
		return sesionA;
	}
	public void setSesionA(Sesion sesionA) {
		this.sesionA = sesionA;
	}
	public Sesion getSesionB() {
		return sesionB;
	}
	public void setSesionB(Sesion sesionB) {
		this.sesionB = sesionB;
	}
	
	
}