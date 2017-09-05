import java.util.Scanner;

public class Main
{

  public static void main(String[] args)
  {

    // BONEYARD IS MADE BY SHUFFLE
    Domino boneyard = new Domino();

    // USER PLAYS FIRST
    Player user = new Player(true, 0);
    Player computer = new Player(false, 1);

    System.out.println("whole *********************************");
    for (int i = 0; i < 14; i++)
    {
      System.out.println(boneyard.dominoes[i]);
    }

    System.out.println("user *********************************");
    for (int i = 0; i < 7; i++)
    {
      System.out.println(user.dominoes[i]);
    }

    System.out.println("computer *********************************");
    for (int i = 0; i < 7; i++)
    {
      System.out.println(computer.dominoes[i]);
    }

    Scanner scanner = new Scanner(System.in);

    boolean gameStart = true;
    int countZeros;

    while(gameStart)
    {
      countZeros = 0;

      System.out.println("user *********************************");
      for (int i = 0; i < 7; i++)
      {
        System.out.println(user.dominoes[i]);

      }

      System.out.print( "\nPut down the domino: " );
      String input = scanner.next();


      for (int i = 0; i < 7; i++)
      {
        if(user.dominoes[i].equals(input)) user.dominoes[i] = "";
        if(user.dominoes[i].equals("")) countZeros++;
      }

      if(countZeros == 7)
      {
        countZeros = 0;
        gameStart = false;
      }

    }

  }
}
