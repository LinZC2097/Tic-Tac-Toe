package TicTacToe;

public class Move {
	private int moveX;
	private int moveY;

	public Move() {
		
	}
	
	public int getMoveX() {
		return this.moveX;
	}
	
	public int getMoveY() {
		return this.moveY;
	}
	
	public int[] getMove() {
		return new int[] {this.moveX, this.moveY};
	}
	
}
