/* Huyen Tran
* Connect Four Game (using Minimax)
* I have neither given nor received unauthorized aid on this program
*/
import java.util.Scanner;
import java.util.*;

public class C4{
  //driver code
  public static void main(String[] args){
    //Run Part A(minimax), B(minimax w/ alpha-beta pruning), or C(custom)
    Scanner input = new Scanner(System.in);
    System.out.print("Run part A, B, or C? ");
    String part = input.next();
    part = part.toUpperCase();

    //error checking
    while(!(part.equals("A") || part.equals("B") || part.equals("C"))){
      System.out.print("enter either A, B, or C: ");
      part = input.next();
      part = part.toUpperCase();
    }

    System.out.print("Include debugging info? (y/n) ");
    String debug = input.next();
    debug = debug.toUpperCase();

    //board size
    System.out.print("Enter rows: ");
    int row = input.nextInt();
    System.out.print("Enter columns: ");
    int col = input.nextInt();
    System.out.print("Enter number in a row to win: ");
    int connectX = input.nextInt();

    int depth = 0;
    if(part.equals("C")){
      System.out.print("Number of moves to look ahead (depth): ");
      depth = input.nextInt();
    }

    //generate board and transposition table before game only for part A & break;
    //part C is run during the game
    Board board = new Board(row, col, connectX);
    Algorithm alg = new Algorithm(board);
    Table t = new Table();
    if(part.equals("A") || part.equals("B")){
      t = alg.generateTable(board, part, debug);
      System.out.println(t.printSize());
      //determine winner if both players play optimally
      alg.perfectPlay(board, t);
    }

    System.out.print("Who play first? 1 = human, 2 = computer: ");
    int p1 = input.nextInt(); //first player
    //print initial empty baord
    System.out.println("\n\n" + board.to2DString());
    if(part.equals("C")){
      alg.setDepth(depth);
      alg.heuristic(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, t);
    }
    MinimaxInfo info = t.getMinimaxInfo(board);
    System.out.println("Minimax value for this state: " + info.val() + " " + info.action());


    //user play first
    if(p1 == 1){
      System.out.println("It is MAX's turn!");
      System.out.print("Enter a move: ");
      board = board.makeMove(input.nextInt());
      System.out.println(board.to2DString() + "\n\n");
      info = t.getMinimaxInfo(board);
      System.out.println("Minimax value for this state: " + info.val() + " " + info.action());
    }

    //GAME
    String play = "Y";
    while(play.equals("Y")){
      //run one game
      while(true){
        //GAME OVER
        if(board.getGameState() == GameState.MAX_WIN){
          System.out.println("The winner is MAX.");
          break;
        }
        else if(board.getGameState() == GameState.MIN_WIN){
          System.out.println("The winner is MIN.");
          break;
        }
        else if(board.getGameState() == GameState.TIE){
          System.out.println("TIE");
          break;
        }

        //computer's turn where user is max and computer is min
        else if(p1 == 1 && board.getPlayerToMoveNext() == Player.MIN){
          System.out.println("It is MIN's turn");
          int move = t.getMinimaxInfo(board).action();
          board = board.makeMove(move);
          System.out.println("Computer chooses move: " + move);
          System.out.println(board.to2DString());
        }
        //computer's turn where user is min and computer is max
        else if((p1 == 2 && board.getPlayerToMoveNext() == Player.MAX)){
          System.out.println("It is MAX's turn");
          int move = t.getMinimaxInfo(board).action();
          board = board.makeMove(move);
          System.out.println("Computer chooses move: " + move);
          System.out.println(board.to2DString());
        }
        //user's turn
        else{
          if(board.getPlayerToMoveNext() == Player.MIN){
            System.out.println("It is MIN's turn");
          }
          else{
            System.out.println("It is MAX's turn");
          }
          System.out.print("Enter a move: ");
          board = board.makeMove(input.nextInt());
          System.out.println(board.to2DString());

        }

        //it's it the heuristic function (c), we generate a new table every game
        if(part.equals("C")){
          //alg.setDepth(depth);
          t = alg.generateTable(board, part, debug);
          System.out.println(t.printSize());
        }
        //for alpha beta pruning, if state doesn't exist, reprune
        else if(!t.containsState(board)){
          System.out.println("This is a state that was previously pruned; re-runing alpha beta from here.");
          Table newT = alg.generateTable(board, part, debug);
          t = newT;
        }
        info = t.getMinimaxInfo(board);
        System.out.println("Minimax value for this state: " + info.val() + " " + info.action());
      }
      System.out.print("Play again? (y/n): ");
      play = input.next();
      play = play.toUpperCase();
      //error checking
      while(!(play.equals("Y") || play.equals("N"))){
        System.out.print("Play again? (y/n): ");
        play = input.next();
        play = play.toUpperCase();
      }
      //clear board for each game;
      board = new Board(row, col, connectX);
    }
  }
}
