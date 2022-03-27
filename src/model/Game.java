package model;

import java.util.Random;

import com.google.gson.Gson;

import comm.Sesion;

public class Game {

<<<<<<< HEAD
	private static  int state;
=======
	private int state;
>>>>>>> parent of 127e410 (Program properly works)
	
	private Sesion sesionA;
	
	private Sesion sesionB;
	
	public Game(Sesion sesionA, Sesion sesionB) {
		
		this.sesionA = sesionA;
		
		this.sesionB = sesionB;
		
		state = 0;
		
		startGame();
	}
	
	public void startGame() {
		

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
		
		int test = 0;
		
		Thread aWaiting = new Thread() {
		
			public void run() {
			
		String a = sesionA.readMessage();
			
		
		
			System.out.println("Esto es antes del if");
			if(aEnded.contains("Answer")==true) {
				System.out.println("Entro al if dentro del hilo A");
				state += 1;
				System.out.println(state);

			}
		
		sesionB.sendMessage(a);
		
		
		//new Thread(() -> {
			
			//String b = sesionB.readMessage();
			
			
			//sesionA.sendMessage(b);
			
		
		
		
		System.out.println("En este sitio esta esperando la respuesta de la pantalla final");	
		sesionA.readMessage();
		System.out.println("La Pantalla b espera su mensaje");
		sesionB.readMessage();
		
		sesionA.readMessage();
			}
		};
		
		
		Thread bWaiting = new Thread() {
			
			public void run() {
			
		String b = sesionB.readMessage();
			
			
			
			if(bEnded.contains("Answer")==true) {
				
				System.out.println("Entro al if dentro del hilo B");

				state += 2;
				
				System.out.println(state);

			}
			
			
		
		//System.out.println("Envio mensaje 1");
		
		//sesionA.sendMessage(b);
		
		
		
		//new Thread(() -> {
		
			String a = sesionA.readMessage();
			
			//System.out.println("Envio mensaje 2");
			sesionB.sendMessage(a);
			//sesionB.sendMessage("");
			
			//}).start();
		
			
		System.out.println("En este sitio esta esperando la respuesta de la pantalla final");	
		sesionA.readMessage();
		System.out.println("La Pantalla A espera su mensaje");

		sesionB.readMessage();
			
		sesionB.readMessage();
			}
		};
		aWaiting.start();
		bWaiting.start();
		
		/*
		while(aWaiting.isAlive()==true || bWaiting.isAlive()==true) {
			
			System.out.println("Tiene que dar vueltas aqui");
		}
		*/
		/*
		
		while(state==0) {
			
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
