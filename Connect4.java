
public class Connect4 {

	public static void main(String args[]) {
		Connect4Model model = new Connect4Model();
		//Connect4View view = new Connect4View(model);
		Connect4Controller controller = new Connect4Controller(model);
		model.add(0); // yellow
		model.add(1); // red
		model.add(3); // yellow
		model.add(2); // red
		model.add(3); // yellow
		model.add(2); // red
		model.add(1); // yellow
		model.add(3); // red
		model.add(3); // yellow
		model.add(1); // red
		model.add(2); // yellow
	}
	
}
