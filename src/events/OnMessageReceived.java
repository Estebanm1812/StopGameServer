package events;

import comm.Sesion;

public interface OnMessageReceived {

	String messageReceived(String msg,Sesion sessions);
	
	
}
