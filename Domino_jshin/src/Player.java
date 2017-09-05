/**
 * Created by jh on 9/3/17.
 */
public class Player
{
  public String[] dominoes;
  public boolean nextTurn;
  // 0 = Defeat, 1 = Victory, 3 = Not yet
  public int victory;

  Player(boolean turn, int id)
  {
    Domino tmp = new Domino(id);

    this.dominoes = tmp.dominoes;
    this.nextTurn = turn;
    this.victory = 3;
  }
}
