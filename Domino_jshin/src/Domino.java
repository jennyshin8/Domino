import com.sun.java.browser.plugin2.DOM;

import java.util.Random;

/**
 * Created by jh on 9/3/17.
 */
public class Domino
{
  public static final String[] DOMINOES = {"00",
          "01", "11",
          "02", "12", "22",
          "03", "13", "23", "33",
          "04", "14", "24", "34", "44",
          "05", "15", "25", "35", "45", "55",
          "06", "16", "26", "36", "46", "56", "66"};

  public static String[] shuffledDominoes = new String[28];
  public String[] dominoes;

  Domino()
  {
    this.dominoes = new String[28];
    shuffleDominoes();
    for (int i = 0; i < 14; i++)
    {
      this.dominoes[i] = shuffledDominoes[i + 14];
    }
  }

  Domino(int id)
  {
    this.dominoes = new String[28];
    if (id == 0) // USER
    {
      for (int i = 0; i < 7; i++)
      {
        this.dominoes[i] = shuffledDominoes[i];
      }
    }
    else if (id == 1) // COMPUTER
    {
      for (int i = 0; i < 7; i++)
      {
        this.dominoes[i] = shuffledDominoes[i + 7];
      }
    }
  }



  String[] shuffleDominoes()
  {

    int[] dummy = new int[28];


    Random random = new Random();
    int randNum;
    boolean duplicate = false;

    // nextInt(28) => 0 ~ 27
    for (int i = 0; i < 28; i++)
    {
      randNum = random.nextInt(28);

      for (int j = 0; j < i; j++)
      {
        if (dummy[j] == randNum)
        {
          i--;
          duplicate = true;
          break;
        }
      }

      if (duplicate)
      {
        duplicate = false;
        continue;
      }

      dummy[i] = randNum;
    }


    for (int i = 0; i < 28; i++)
    {
      shuffledDominoes[i] = DOMINOES[dummy[i]];
    }

    return shuffledDominoes;
  }


}
