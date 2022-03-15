//transpositon table
import java.util.HashMap;
import java.util.Map;

public class Table{
  private Map<Board, MinimaxInfo> table;

  public Table(){
    table = new HashMap<>();
  }

  public void addState(Board b, MinimaxInfo mm){
    table.put(b, mm);
  }

  public boolean containsState(Board b){
    return table.containsKey(b);
  }

  public MinimaxInfo getMinimaxInfo(Board b){
    MinimaxInfo mm = table.get(b);
    if (b == null) throw new IllegalArgumentException("State doesn't exist in table.");
    return mm;
  }

  public void printTable(){
    for(Board key: table.keySet()){
      System.out.println(key.toString() + "val="+table.get(key).val()
        +" best move: " + table.get(key).action());
    }
  }
  public String printSize(){
    return "Transposition table has " + table.size() + " states";
  }

}
