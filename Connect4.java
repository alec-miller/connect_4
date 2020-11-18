
public class Connect4 {

	public static void main(String args[]) {
		Connect4Model model = new Connect4Model();
		Connect4View view = new Connect4View(model);
		Connect4Controller controller = new Connect4Controller(model);
		model.add(0);
		model.add(0);
		model.add(3);
		model.add(3);
		model.add(1);
		model.add(1);
		model.add(4);
		model.add(2);
		model.add(4);
		model.add(2);
	}
	
}
