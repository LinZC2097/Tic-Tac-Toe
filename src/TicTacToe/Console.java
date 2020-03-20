package TicTacToe;

import java.util.Scanner;

import TicTacToe.Board.State;

public class Console {
	public Scanner sc = new Scanner(System.in);
	public Board board = new Board();
	
	Console(){
		board.initialization();
	}
	
	public void play() {
		System.out.print("please input the next step:");
		int x = sc.nextInt();
		int y = sc.nextInt();
		
		board.move(x, y);
		board.show();
		if(board.checkWin(x, y)) {
			System.out.println("win!!!");
		}
	}
	
	
	public static void main(String[] args) {
		Console console = new Console();
		console.board.show();
		while(true) {
			console.play();
			
		}
	}
	
}
