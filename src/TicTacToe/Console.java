package TicTacToe;

import java.util.Scanner;

public class Console {
	public Scanner sc = new Scanner(System.in);
	public Board board = new Board();
	
	 private Console() {
	        board = new Board();
	    }

	    /**
	     * Begin the game.
	     */
	    private void play () {

	        System.out.println("Starting a new game.");

	        while (true) {
	            printGameStatus();
	            playMove();

	            if (board.isGameOver()) {
	                printWinner();

	                if (!tryAgain()) {
	                    break;
	                }
	            }
	        }
	    }

	    /**
	     * Handle the move to be played, either by the player or the AI.
	     */
	    private void playMove () {
	    	
	        if (board.getTurn() == Board.State.X) {
	        	Algorithms.alphaBetaAdvanced(Board.State.X, board);
	        	
	            
	        } else {
//	        	Algorithms.alphaBetaAdvanced(Board.State.O, board);
	        	getPlayerMove();
	        }
	        
	    	//Algorithms.alphaBetaAdvanced(board.getTurn(), board);
	    }

	    /**
	     * Print out the board and the player who's turn it is.
	     */
	    private void printGameStatus () {
	        System.out.println("\n" + board + "\n");
	        System.out.println(board.getTurn().name() + "'s turn.");
	    }

	    /**
	     * For reading in and interpreting the move that the user types into the console.
	     */
	    private void getPlayerMove () {
	        System.out.print("Index of move: ");

	        int move_row = sc.nextInt();
	        int move_col = sc.nextInt();
	        int move = move_row * Board.BOARD_LENGTH + move_col;
	        

	        if (move < 0 || move >= Board.BOARD_LENGTH* Board.BOARD_LENGTH) {
	            System.out.println("\nInvalid move.");
	            System.out.println("\nThe index of the move must be between 0 and "
	                    + (Board.BOARD_LENGTH * Board.BOARD_LENGTH - 1) + ", inclusive.");
	        } else if (!board.move(move)) {
	            System.out.println("\nInvalid move.");
	            System.out.println("\nThe selected index must be blank.");
	        }
	    }

	    /**
	     * Print out the winner of the game.
	     */
	    private void printWinner () {
	        Board.State winner = board.getWinner();

	        System.out.println("\n" + board + "\n");

	        if (winner == Board.State.Blank) {
	            System.out.println("The TicTacToe is a Draw.");
	        } else {
	            System.out.println("Player " + winner.toString() + " wins!");
	        }
	    }

	   

	    public static void main(String[] args) {
	        Console ticTacToe = new Console();
	        ticTacToe.play();
	    }
}
