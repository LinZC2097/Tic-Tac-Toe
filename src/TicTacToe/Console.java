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
		System.out.print("please input the next step(format eg: 6 6):");
		int x = sc.nextInt();
		int y = sc.nextInt();
		
		board.move(x, y);
		board.show();
//		if(board.checkWin()) {
//			System.out.println("win!!!");
//		}
	}
	
	
	public static void main(String[] args) {
		Console console = new Console();
		console.board.show();
		for(int i = 0; i <2; i++) {
			console.play();
			
		}
		Board newBoard = new Board();
		newBoard = console.board.deepcopy();
		newBoard.move(14, 14);
		newBoard.show();
		System.out.println("new length" + newBoard.getAvaliableMoveLen());
		console.board.show();
		System.out.println("old length" + console.board.getAvaliableMoveLen());
	}
	
}
