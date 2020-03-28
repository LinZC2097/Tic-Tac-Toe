package TicTacToe;
import TicTacToe.Algorithms;

import java.util.Scanner;


public class Console {

    private Board board;
    private Scanner sc = new Scanner(System.in);
    private APISender sender;
    
    private Console(int GameId) {
        board = new Board();
        this.sender = new APISender(GameId);
    }

    private void play () {

        System.out.println("Starting a new game.");

        while (true) {
        	printGameStatus();
            playMove();
//            playManual();

            if (board.isGameOver()) {
                printWinner();

                if (!tryAgain()) {
                    break;
                }
            }
        }
    }

    private void playMove () {
    	
        if (board.getTurn() == Board.State.X) {
        	long bt = System.currentTimeMillis();  
        	Algorithms.alphaBetaAdvanced(Board.State.X, board);
        	int[] move = board.getPreMove();
        	System.out.printf("x:%d, y:%d", move[0], move[1]);
        	System.out.printf("\nmoveNum:%d", board.getMoveCount());
        	long et2 = System.currentTimeMillis();  
        	System.out.println("\n[1]耗时:"+(et2 - bt)+ "ms");  
        	
        } else {
        	long bt = System.currentTimeMillis();  
        	Algorithms.alphaBetaAdvanced(Board.State.O, board);
//        	getPlayerMove();
        	int[] move = board.getPreMove();
        	System.out.printf("x:%d, y:%d", move[0], move[1]);
        	System.out.printf("\nmoveNum:%d", board.getMoveCount());
        	long et2 = System.currentTimeMillis();  
        	System.out.println("\n[1]耗时:"+(et2 - bt)+ "ms");
        }
        
    	//Algorithms.alphaBetaAdvanced(board.getTurn(), board);
    }
    
    private void playManual() {
    	getPlayerMove();
    	printGameStatus();

    	int[] move = board.getPreMove();
    	if(board.isMoreTwo(move[0], move[1])){
    		System.out.println("is more than 2");
    	}else {
    		System.out.println("!!!!!!!< 2");
    	}
    	
    }

    private void printGameStatus () {
        System.out.println("\n" + board + "\n");
        System.out.println(board.getTurn().name() + "'s turn.");
    }

    private void getPlayerMove () {
        System.out.print("Index of move: ");

        int move_row = sc.nextInt();
        int move_col = sc.nextInt();
        int move = move_row * Board.BOARD_WIDTH + move_col;
        

        if (move < 0 || move >= Board.BOARD_WIDTH* Board.BOARD_WIDTH) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (Board.BOARD_WIDTH * Board.BOARD_WIDTH - 1) + ", inclusive.");
        } else if (!board.move(move)) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
    }

    private void printWinner () {
        Board.State winner = board.getWinner();

        System.out.println("\n" + board + "\n");

        if (winner == Board.State.Blank) {
            System.out.println("The TicTacToe is a Draw.");
        } else {
            System.out.println("Player " + winner.toString() + " wins!");
        }
    }

    private boolean tryAgain () {
        if (promptTryAgain()) {
            board.reset();
            System.out.println("Started new game.");
            System.out.println("X's turn.");
            return true;
        }

        return false;
    }

    private boolean promptTryAgain () {
        while (true) {
            System.out.print("Would you like to start a new game? (Y/N): ");
            String response = sc.next();
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Invalid input.");
        }
    }

    public static void main(String[] args) {
    	System.out.println("Input the game Id: ");
        Console ticTacToe = new Console(new Scanner(System.in).nextInt());
        ticTacToe.play();
    }

}
