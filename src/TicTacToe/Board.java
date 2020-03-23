package TicTacToe;

import java.lang.Math;
import java.util.HashSet;
import java.util.Hashtable;

import javafx.scene.layout.Border;;

public class Board {
	static final int BOARD_LENGTH = 15;
	static final int AIM_LENGTH = 3;

	public enum State {
		X, O, Blank
	};

	private State[][] board;
	private HashSet<Integer> availableMove;
	private int moveNum;
	private State player;
	private int preScore;
	private int score;

	Board() {
		this.board = new State[BOARD_LENGTH][BOARD_LENGTH];
		this.moveNum = 0;
		this.player = State.O;
		this.availableMove = new HashSet<>();
		this.initialization();
	}

	public void initialization() {

		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				this.board[i][j] = State.Blank;
			}
		}

		for (int i = 0; i < BOARD_LENGTH * BOARD_LENGTH; i++) {
			this.availableMove.add(i);
		}
	}

	public Board deepcopy() {
		Board newBoard = new Board();

		newBoard.initialization();

		for (int i = 0; i < Board.BOARD_LENGTH; i++) {
			newBoard.board[i] = this.board[i].clone();
		}

		newBoard.availableMove = (HashSet) this.availableMove.clone();

		newBoard.moveNum = this.moveNum;
		newBoard.player = this.player;

		return newBoard;
	}
	
	public static int[] getMove(int index) {
		return new int[] {(index % BOARD_LENGTH), (index / BOARD_LENGTH)};
	}

	public State getPlayer() {
		return this.player;
	}

	public int getAvaliableMoveLen() {
		return this.availableMove.size();
	}

	public HashSet<Integer> getAvailableMove() {
		return this.availableMove;
	}

	public int[] getMoveIndex(int moveIndex) {
		return new int[] { (moveIndex / BOARD_LENGTH), (moveIndex % BOARD_LENGTH) };
	}

	public void move(int moveIndex) {
		this.move(moveIndex % BOARD_LENGTH, moveIndex / BOARD_LENGTH);
	}
	
	private void move(int x, int y) {

		this.board[y][x] = (this.player == State.O) ? State.O : State.X;
		this.availableMove.remove(x * BOARD_LENGTH + y);
		this.moveNum++;
		this.player = (this.player == State.O) ? State.X : State.O;
	}

	public void show() {
		System.out.print("y    x \t");

		for (int i = 0; i < 10; i++) {
			System.out.print(i + " ");
		}
		for (int i = 10; i < BOARD_LENGTH; i++) {
			System.out.print(i);
		}
		System.out.println();
		for (int i = 0; i < BOARD_LENGTH; i++) {
			System.out.print(i + "\t");
			for (int j = 0; j < BOARD_LENGTH; j++) {
				if (this.board[i][j] == State.Blank) {
					System.out.print("- ");
				} else {
					System.out.print(this.board[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	public boolean isUseless(int x, int y) {
		State oppnent = (this.player == State.O) ? State.X : State.O;

		// check 2M * 2M matrix whose center is (x, y) is empty or not

		int startX = Math.max(0, x - AIM_LENGTH - 1);
		int startY = Math.max(0, y - AIM_LENGTH - 1);

		int endX = Math.min(BOARD_LENGTH - 1, x + AIM_LENGTH - 1);
		int endY = Math.min(BOARD_LENGTH - 1, y + AIM_LENGTH - 1);

		for (int i = startX; i < endX; i++) {
			for (int j = startY; j < endY; j++) {
				if (!this.availableMove.contains(i * BOARD_LENGTH + j)) {
					return true;
				}
			}
		}

		return false;
	}

	// !!!!!!!!!!!!!!!!!!!!
	public int getScore() {
		return 0;
	}

	// !!!!!!!!!!!!!!!!!!!!
	public boolean checkWin(int x, int y) {
		State prePlayer = (this.player == State.O) ? State.X : State.O;
		
		// check row
		int startX = Math.max(x - AIM_LENGTH + 1, 0);
		int endX = Math.min(x + AIM_LENGTH - 1, BOARD_LENGTH - 1);
		
		int count = 0;
		for(int i = startX; i <= endX; i++) {
			if(this.board[y][i] != prePlayer) {
				count = 0;
				continue;
			}
			count++;
			if(count == AIM_LENGTH) return true;
		}
		
		
		// check column 
		int startY = Math.max(y - AIM_LENGTH + 1, 0);
		int endY = Math.min(y + AIM_LENGTH - 1, BOARD_LENGTH - 1);
		
		count = 0;
		for(int j = startY; j <= endY; j++) {
			if(this.board[j][x] != prePlayer) {
				count = 0;
				continue;
			}
			count++;
			if(count == AIM_LENGTH) return true;
		}
		
		// check diagonal \
		startX = Math.max(x - AIM_LENGTH + 1, 0);
		startY = Math.max(y - AIM_LENGTH + 1, 0);

		endX = Math.min(x + AIM_LENGTH - 1, BOARD_LENGTH - 1);
		endY = Math.min(y + AIM_LENGTH - 1, BOARD_LENGTH - 1);
		
		for(int i = startX, j = startY; i <= endX || j <= endY; i++, j++) {
			if(this.board[j][i] != prePlayer) {
				count = 0;
				continue;
			}
			count ++;
			if(count == AIM_LENGTH) return true;
		}
		
		// check diagonal /
		startX = Math.min(x + AIM_LENGTH - 1, BOARD_LENGTH - 1);
		startY = Math.max(y - AIM_LENGTH + 1, 0);

		endX = Math.max(x - AIM_LENGTH + 1, 0);
		endY = Math.min(y + AIM_LENGTH - 1, BOARD_LENGTH - 1);
		
		for(int i = startX, j = startY; i >= endX || j <= endY; i--, j++) {
			if(this.board[j][i] != prePlayer) {
				count = 0;
				continue;
			}
			count ++;
			if(count == AIM_LENGTH) return true;
		}
		
		return false;
	}

	public static void main(String[] args) {
		System.out.println("hello world");
		Board board = new Board();
//		board.initialization();
		board.show();
		int[] a = board.getMoveIndex(14);
		for (int val : a) {
			System.out.println(val);

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}

	}

}
