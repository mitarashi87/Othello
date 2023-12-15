import java.util.Scanner;

public class Player {
	private String discIcon;
	
	
	public Player(String discIcon) {
		this.discIcon = discIcon;
	}
	
	public Disc playDisc() {
		return new Disc(discIcon);
	}
	
	public Pos playPos() {

		Scanner scanner = new Scanner(System.in);
		System.out.print("x:");
		int x = scanner.nextInt();
		System.out.print("y:");
		int y = scanner.nextInt();
		Pos pos = new Pos(x, y);
		return pos;
	}
	public void receiveMessage(String message) {
		System.out.println(message);
	}
	
}
