import java.util.*;
public class Algorithm{
  Board board;
  Table table;
  public int pruned;
  public int depth;

  public Algorithm(Board b){
    board = b;
    pruned = 0;
  }

  public Table generateTable(Board board, String alg, String debug){
    Table table = new Table();
    //start timer
    final long startTime = System.currentTimeMillis();
    //run minimax
    if(alg.equals("A")){
      minimaxSearch(board, table);
    }
    else if(alg.equals("B")){
      minimaxAB(board, Integer.MIN_VALUE, Integer.MAX_VALUE, table);
      System.out.println("The tree was pruned " + pruned + " times.");
    }
    else{
      System.out.println("Running heuristic search");
      heuristic(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, table);
    }
    //end timer
    final long endTime = System.currentTimeMillis();
    double totalTime = (0.001)*(endTime - startTime);
    System.out.printf("Search completed in %.3f seconds\n", totalTime);

  
    if(debug.equals("Y")){
      table.printTable();
    }
    return table;
  }

  //run one game to see who is guaranteed to win if each player play optimally
  public static void perfectPlay(Board b, Table t){
    if(t.getMinimaxInfo(b).val() > 0){
      System.out.println("First player has a guaranteed win with perfect play.");
    }
    else if(t.getMinimaxInfo(b).val() < 0){
      System.out.println("Second player has a guaranteed win with perfect play.");
    }
    else{
      System.out.println("Neither players has a guaranteed win; game will end in tie with perfect play on both sides.");
    }
  }


  public static MinimaxInfo minimaxSearch(Board state, Table table){
    if(table.containsState(state)){
      return table.getMinimaxInfo(state);
    }
    //terminal state
    else if(state.getGameState() != GameState.IN_PROGRESS){
      int util = state.utility();
      MinimaxInfo m = new MinimaxInfo(util, -1);
      table.addState(state, m);
      return m;

    }

    //MAX
    else if (state.getPlayerToMoveNext() == Player.MAX){
      int v = Integer.MIN_VALUE;
      int bestmove = -1;
      //for every actions in available actions
      for(int a : state.actions()){
        Board childState = state.makeMove(a);
        MinimaxInfo childInfo = minimaxSearch(childState, table);
        int v2 = childInfo.val();
        if(v2 > v){
          v = v2;
          bestmove = a;
        }
      }

      MinimaxInfo m = new MinimaxInfo(v, bestmove);
      table.addState(state, m);
      return m;
    }

    //MIN
    else{
      int v = Integer.MAX_VALUE;
      int bestmove = -1;
      //for every actions in available actions
      for(int a : state.actions()){
        Board childState = state.makeMove(a);
        MinimaxInfo childInfo = minimaxSearch(childState, table);
        int v2 = childInfo.val();
        if(v2 < v){
          v = v2;
          bestmove = a;
        }
      }

      MinimaxInfo m = new MinimaxInfo(v, bestmove);
      table.addState(state, m);
      return m;
    }
  }

  public MinimaxInfo minimaxAB(Board state, int alpha, int beta, Table table){
    if(table.containsState(state)){
      return table.getMinimaxInfo(state);
    }
    //terminal state
    else if(state.getGameState() != GameState.IN_PROGRESS){
      int util = state.utility();
      MinimaxInfo m = new MinimaxInfo(util, -1);
      table.addState(state, m);
      return m;
    }

    //MAX
    else if (state.getPlayerToMoveNext() == Player.MAX){
      int v = Integer.MIN_VALUE;
      int bestmove = -1;
      //for every actions in available actions
      for(int a : state.actions()){
        Board childState = state.makeMove(a);
        MinimaxInfo childInfo = minimaxAB(childState, alpha, beta, table);
        int v2 = childInfo.val();
        if(v2 > v){
          v = v2;
          bestmove = a;
          alpha = Math.max(alpha, v);
        }
        if(v >= beta){
          MinimaxInfo m = new MinimaxInfo(v, bestmove);
          pruned++;
          return m;
        }
      }

      MinimaxInfo m = new MinimaxInfo(v, bestmove);
      table.addState(state, m);
      return m;
    }

    //MIN
    else{
      int v = Integer.MAX_VALUE;
      int bestmove = -1;
      //for every actions in available actions
      for(int a : state.actions()){
        Board childState = state.makeMove(a);
        MinimaxInfo childInfo = minimaxAB(childState, alpha, beta, table);
        int v2 = childInfo.val();
        if(v2 < v){
          v = v2;
          bestmove = a;
          beta = Math.min(beta, v);
        }
        if(v <= alpha){
          MinimaxInfo m = new MinimaxInfo(v, bestmove);
          pruned++;
          return m;
        }
      }

      MinimaxInfo m = new MinimaxInfo(v, bestmove);
      table.addState(state, m);
      return m;
    }
  }

  public void setDepth(int d){
    this.depth = d;
  }

  public MinimaxInfo heuristic(Board state, int alpha, int beta, int d, Table table){
    if(table.containsState(state)){
      return table.getMinimaxInfo(state);
    }
    //terminal state
    else if(state.getGameState() != GameState.IN_PROGRESS){
      int util = state.utility();
      MinimaxInfo m = new MinimaxInfo(util, -1);
      table.addState(state, m);
      return m;
    }

    //cut off
    else if(d >= depth){
      int heuristic = state.eval();
      MinimaxInfo m = new MinimaxInfo(heuristic, -1);
      table.addState(state, m);
      return m;
    }

    //MAX
    else if (state.getPlayerToMoveNext() == Player.MAX){
      int v = Integer.MIN_VALUE;
      int bestmove = -1;
      //for every actions in available actions
      for(int a : state.actions()){
        Board childState = state.makeMove(a);
        MinimaxInfo childInfo = heuristic(childState, alpha, beta, d+1, table);
        int v2 = childInfo.val();
        if(v2 > v){
          v = v2;
          bestmove = a;
          alpha = Math.max(alpha, v);
        }
        if(v >= beta){
          MinimaxInfo m = new MinimaxInfo(v, bestmove);
          pruned++;
          return m;
        }
      }

      MinimaxInfo m = new MinimaxInfo(v, bestmove);
      table.addState(state, m);
      return m;
    }

    //MIN
    else{
      int v = Integer.MAX_VALUE;
      int bestmove = -1;
      //for every actions in available actions
      for(int a : state.actions()){
        Board childState = state.makeMove(a);
        MinimaxInfo childInfo = heuristic(childState, alpha, beta, d+1, table);
        int v2 = childInfo.val();
        if(v2 < v){
          v = v2;
          bestmove = a;
          beta = Math.min(beta, v);
        }
        if(v <= alpha){
          MinimaxInfo m = new MinimaxInfo(v, bestmove);
          pruned++;
          return m;
        }
      }

      MinimaxInfo m = new MinimaxInfo(v, bestmove);
      table.addState(state, m);
      return m;
    }
  }

}
