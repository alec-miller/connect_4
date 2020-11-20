import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;

public class Connect4 {

	public static void main(String args[]) {
		Application.launch(Connect4View.class, args);
	}
	
}
