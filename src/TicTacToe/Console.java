package TicTacToe;

import java.util.Scanner;


public class Console {
	public Scanner sc = new Scanner(System.in);
	public Board board = new Board();
	
	Console(){
//		board.initialization();
	}
	
	public void play() {		
		System.out.print("please input the next step(format eg: 6 6):");
		int x = sc.nextInt();
		int y = sc.nextInt();
		
		if(board.isUseless(x, y)) {
			board.move(x + y * Board.BOARD_LENGTH);
			board.show();
			if(board.checkWin(x, y)) {
				System.out.println("win !!!!!!!!!!!!!!!!!!!1");
			}
			else {
				System.out.println("????");
				
			}
		}else {
			System.out.println("this move is useless");
		}
		
		
//		if(board.checkWin()) {
//			System.out.println("win!!!");
//		}
	}
	
	
	public static void main(String[] args) {
		Console console = new Console();
		console.board.show();
	
		int x = console.sc.nextInt();
		int y = console.sc.nextInt();
		
		console.board.move(x + y * Board.BOARD_LENGTH);
		console.board.show();
		
		
		for(int i = 0; ; i++) {
			console.play();
			
		}
		
	}
	
}
