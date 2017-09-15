import java.util.Random;

/**
 * Created by jh on 9/3/17.
 */
public class Domino
{
  public static final String[] DOMINOES = {"00",
          "10", "11",
          "20", "21", "22",
          "30", "31", "32", "33",
          "40", "41", "42", "43", "44",
          "50", "51", "52", "53", "54", "55",
          "60", "61", "62", "63", "64", "65", "66"};

  public static String[] shuffledDominoes = new String[28];
  public String[] dominoes;
  public int[] dominoesIndex;

  Domino()
  {
    this.dominoes = new String[28];
    this.dominoesIndex = new int[28];

    shuffleDominoes();
    for (int i = 0; i < 14; i++)
    {
      this.dominoes[i] = shuffledDominoes[i + 14];
    }
  }

  Domino(String id)
  {
    this.dominoes = new String[28];

    if (id.equals("user")) // USER
    {
      for (int i = 0; i < 7; i++)
      {
        this.dominoes[i] = shuffledDominoes[i];
      }
    }
    else if (id.equals("computer")) // COMPUTER
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
      dominoesIndex[i] = dummy[i];
    }

    return shuffledDominoes;
  }

  String[] putDominoes(String inputStr)
  {
    for(int i = 0; i < this.dominoes.length; i++)
    {
      if(this.dominoes[i].equals(""))
      {
        this.dominoes[i] = inputStr;
        break;
      }
    }

    return this.dominoes;
  }


  String findOwner(String dominoValue)
  {
    int index = -1;
    String parent;

    for(int i = 0; i < 28; i++)
    {
      if (shuffledDominoes[i].equals(dominoValue))
      {
        index = i;
        break;
      }
    }


    if (index < 7)
    {
      parent = "user";
    }
    else if (index < 14)
    {
      parent = "computer";
      index -= 7;
    }
    else
    {
      parent = "boneyard";
      index -= 14;
    }


    return parent + index;
  }

}
