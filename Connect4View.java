import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Taylor McLaughlin and Alec Miller
 * This class is the view of this MVC connect 4 game.
 * This creates a board that the user can interact with by clicking on one of the seven columns.  The 
 * next move will then be made by another person running another instance of the program, or a computer.
 * The user can create and play as many games as they want.
 */

public class Connect4View extends Application implements Observer {
	
	private Connect4Model model = new Connect4Model();
	private Connect4Controller controller = new Connect4Controller(model);
	private static String[] arguments;
	private static ArrayList<ArrayList<Circle>> circles = new ArrayList<ArrayList<Circle>>();
	private Alert a;
	private static int serverOrClient = -1; // 0 = server, 1 = client
	private int humanOrComputer = 0; // 0 = human, 1 = computer
	private int serverNum;
	private int portNum;
	
	final static int NUM_ROWS = 6;
	final static int NUM_COLS = 7;
	final static int CIRCLE_SIZE = 20;
	final int PANE_WIDTH = 344;
	final int PANE_HEIGHT = 296;
	
	/**
	 * setup initial white tokens for board and call launch
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(serverOrClient == 0) {
			server();
		}
		launch(args);
	}
	
	/**
	 * Update the view when the model has changed
	 * 
	 * @param o observable, not used in this case
	 * @param arg - a Connect4MoveMessage that contains the color and location of token that has changed
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		if(((Connect4MoveMessage) arg).getColor() == ((Connect4MoveMessage) arg).RED) {
			circles.get(NUM_ROWS - 1 - ((Connect4MoveMessage) arg).getRow()).get(((Connect4MoveMessage) arg).getColumn()).setFill(javafx.scene.paint.Color.RED);
		} else {
			circles.get(NUM_ROWS - 1 -((Connect4MoveMessage) arg).getRow()).get(((Connect4MoveMessage) arg).getColumn()).setFill(javafx.scene.paint.Color.YELLOW);
		}
		
	}
	
	/**
	 * Builds view that the user interacts with.
	 * Creates a menu bar on top, with the board in the center of the screen
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model.addObserver(this);
		setupInitialCircles();
		BorderPane root = new BorderPane();
		a  = new Alert(AlertType.NONE);
		model.addObserver(this);  // CHANGE THIS
		primaryStage.setTitle("Connect 4");
		Menu file = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(e -> {
			openMenu(primaryStage);
		});
		file.getItems().add(newGame);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(file);
		VBox vb = new VBox(menuBar);
		GridPane pane = new GridPane(); // hold connect 4 board
		Insets insets = new Insets(8, 8, 8, 8);
		pane.setHgap(8);
		pane.setVgap(8);
		pane.setPadding(insets);
		pane.setStyle("-fx-background-color: blue;" + "-fx-border-insets: 8;");
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				int val = controller.makeHumanMove(me, PANE_WIDTH, NUM_COLS);
				if(val == 1) {
					winPopup(pane, "Yellow");
				}else if(val == 2) {
					winPopup(pane, "Red");
				}else if(val == 3) {
					tiePopup(pane, "It's a tie!");
				}else if(val == 4) {
					invalidPopup(pane, "Column full, pick somewhere else!");
				}
				if((val != 1 && val != 2 && val != 3) && humanOrComputer == 1) {
					controller.makeComputerMove();
				}
			}			
		});
		setCircles(pane);
		root.setPrefSize(360,312);
		root.setTop(vb);
		root.setCenter(pane);
		Scene scene = new Scene(root, 344, 328);
		primaryStage.setScene(scene);
		primaryStage.show();
		if(serverOrClient == 0 && humanOrComputer == 1) {
			controller.makeComputerMove();
		}
	}

	/**
	 * Creates a popup window if the player has placed an invalid move
	 * 
	 * @param pane
	 * @param string
	 */
	protected void invalidPopup(GridPane pane, String string) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(string);
		alert.show();
	}
	
	/**
	 * Creates a popup window if the game ends in a tie
	 * 
	 * @param pane
	 * @param string
	 */
	protected void tiePopup(GridPane pane, String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(string);
		alert.setHeaderText("Game Over!");
		alert.show();
		pane.setDisable(true);
	}
	
	/**
	 * Creates a popup window for the winning player
	 * 
	 * @param pane
	 * @param string
	 */
	private void winPopup(GridPane pane, String team) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(team + " has won!");
		alert.setHeaderText("Game Over!");
		alert.show();
		pane.setDisable(true);
	}
	
	/**
	 * This creates the board based on the local 2d array of circles which is updated by update
	 * 
	 * @param pane - a GridPane that stores the tokens of the board
	 */
	private void setCircles(GridPane pane) {
		for(int y = 0; y < NUM_ROWS; y++) {
			for(int x = 0; x < NUM_COLS; x++) {
				pane.add(circles.get(y).get(x), x, y);
			}
		}
	}
	
	/**
	 * Builds a 2d array of white circles that represent the board.
	 * Mdifies local variable circles
	 */
	public static void setupInitialCircles() {
		for(int i = 0; i < NUM_ROWS; i++) {
			ArrayList<Circle> circlesRow = new ArrayList<Circle>();
			for(int j = 0; j < NUM_COLS; j++) {
				Circle circle = new Circle(CIRCLE_SIZE);
				circle.setFill(javafx.scene.paint.Color.WHITE);
				circlesRow.add(circle);
			}
			circles.add(circlesRow);
		}
	}
	
	/**
	 * Create a new Connect4Menu();
	 */
	public void openMenu(Stage stage) {
		Connect4Menu menu = new Connect4Menu(stage);
	}
	
	/**
	 * 
	 * @author Taylor McLaughlin
	 * This class is the creates the screen that the user uses to create a new game.
	 * The screen created allows the user to create another game either with a human or a computer.  The
	 * user then decide if they are the server or the client and create the new game
	 */
	private class Connect4Menu extends Stage {
		public Connect4Menu(Stage primaryStage) {
			//this.initModality(APPLICATION_MODAL);
			Label create = new Label("Create:");
			Label play = new Label("Play as:");
			
			// Server/Client Buttons
			final ToggleGroup createGroup = new ToggleGroup();
			RadioButton rbServer = new RadioButton("Server");
			rbServer.setToggleGroup(createGroup);
			RadioButton rbClient = new RadioButton("Client");
			rbClient.setToggleGroup(createGroup);
			HBox createRow = new HBox(10); 
			createRow.getChildren().addAll(create, rbServer, rbClient);
			setServerButton(rbServer);
			setClientButton(rbClient);
			
			// Human/Computer Buttons
			final ToggleGroup playGroup = new ToggleGroup();
			RadioButton rbHuman = new RadioButton("Human");
			rbHuman.setToggleGroup(playGroup);
			RadioButton rbComputer = new RadioButton("Computer");
			rbComputer.setToggleGroup(playGroup);
			HBox playRow = new HBox(10); 
			playRow.getChildren().addAll(play, rbHuman, rbComputer);
			setHumanButton(rbHuman);
			setComputerButton(rbComputer);
			
			// Server/Port Textfields
			Label server = new Label("Server");
			Label port = new Label("Port");
			TextField serverTextField = new TextField();
			TextField portTextField = new TextField();
			HBox serverRow = new HBox(10); 
			serverRow.getChildren().addAll(server, serverTextField, port, portTextField);
			
			// Okay/Cancel Buttons
			Button ok = new Button("OK");
			Button cancel = new Button("Cancel");
			HBox buttonRow = new HBox(10); 
			buttonRow.getChildren().addAll(ok, cancel);
			setOK(ok, primaryStage);
			setCancel(cancel);
			
			
			VBox holder = new VBox(10);
			holder.getChildren().addAll(createRow, playRow, serverRow, buttonRow);
            
			Scene scene = new Scene(holder, 500, 200);
			this.setScene(scene);
			this.show();
			
		}
		
		/**
		 * Sets the "Computer" radio button, indicating that a computer is the new opponent
		 * 
		 * @param rbComputer
		 */
		private void setComputerButton(RadioButton rbComputer) {
			rbComputer.setOnAction(event -> {
				humanOrComputer = 1;
			});
		}

		/**
		 * Sets the "Human" radio button, indicating that a human is the other player
		 * 
		 * @param rbHuman
		 */
		private void setHumanButton(RadioButton rbHuman) {
			rbHuman.setOnAction(event -> {
				humanOrComputer = 0;
			});
		}

		/**
		 * Sets the "Client" radio button, indicating that this is the client
		 * 
		 * @param rbClient
		 */
		private void setClientButton(RadioButton rbClient) {
			rbClient.setOnAction(event -> {
				serverOrClient = 1;
			});
		}

		/**
		 * Sets the "Server" radio button, indicating that this is the server
		 * 
		 * @param rbServer
		 */
		private void setServerButton(RadioButton rbServer) {
			rbServer.setOnAction(event -> {
				serverOrClient = 0;
			});
		}

		/**
		 * Sets the "Cancel" button to close out of the new game window
		 * 
		 * @param cancel
		 */
		private void setCancel(Button cancel) {
			cancel.setOnAction(event -> {
				this.hide();
			});
		}

		/**
		 * Sets the "Ok" button to make a new game
		 * 
		 * @param ok
		 * @param primaryStage
		 */
		private void setOK(Button ok, Stage primaryStage) {
			ok.setOnAction(event -> {
	            primaryStage.hide();
	            try {
	            	model.reset();
	            	circles = new ArrayList<ArrayList<Circle>>();
					start(new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
	            this.hide();
			});
		}
		
		
	}
	
	/**
	 * Alert handler for when someone wins/ties
	 * 
	 * @param pane
	 * @param string
	 */
	EventHandler<WindowEvent> message = new EventHandler<WindowEvent>() { 
	public void handle(WindowEvent e) 
	{ 
	   // set alert type 
	   a.setAlertType(AlertType.INFORMATION); 
	
	   // show the dialog 
	   a.show(); 
	} 
	};
	
	private static void server() {
		try {
			ServerSocket server = new ServerSocket(4000);
			Socket connection  = server.accept();
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void client() {
		try {
			Socket server = new Socket("localhost", 4000);
			ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(server.getInputStream());
			server.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
