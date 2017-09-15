/**
 * Created by jh on 9/3/17.
 */
public class Player
{
  public String[] dominoes;

  Player(String id)
  {
    Domino tmp = new Domino(id);
    this.dominoes = tmp.dominoes;
  }
}
