import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class Main extends Application
{
  private static final int WINDOW_WIDTH = 1300;
  private static final int WINDOW_HEIGHT = 500;

  // Controller(hand) for players
  private static Button[] btnArray = new Button[28];


  // hBox0         : Warning Message (When player picked invalid domino)
  // hBox1 & hBox2 : The first row and second row of table
  // hBox3         : User's dominoes
  // hBox4         : Result message when the game is over
  private static HBox hBox0 = new HBox();
  private static HBox hBox1 = new HBox();
  private static HBox hBox2 = new HBox();
  private static HBox hBox3 = new HBox();
  private static HBox hBox4 = new HBox();

  // Container for hBox0, hBox1, hBox2, hBox3, and hBox4
  private static VBox vbox = new VBox();

  // To inform the right place to put a domino
  private static HBox headHBox, tailHBox;
  private static String headValue, tailValue;


  private static boolean gameover = false;
  private static Text winner= new Text();

  // When boneyard is initialized, whole dominoes are shuffled.
  // And also, shuffled dominoes are saved in each player's dominoes array and in boneyard
  private static Domino boneyard = new Domino();

  private static Player user = new Player("user");
  private static Player computer = new Player("computer");

  @Override
  public void start(Stage stage)
  {
    Scene scene = new Scene(new Group());
    stage.setTitle("Dominoes_JShin");
    stage.setWidth(WINDOW_WIDTH);
    stage.setHeight(WINDOW_HEIGHT);


    System.out.println("boneyard *********************************");
    for (int i = 0; i < 28; i++)
    {
      if (boneyard.dominoes[i] != null) System.out.println(boneyard.dominoes[i]);
    }

    System.out.println("user *********************************");
    for (int i = 0; i < 28; i++)
    {
      if (user.dominoes[i] != null) System.out.println(user.dominoes[i]);
    }

    System.out.println("computer *********************************");
    for (int i = 0; i < 28; i++)
    {
      if (computer.dominoes[i] != null) System.out.println(computer.dominoes[i]);
    }


    // Set up a controller.
    // Each button is directly assigned to each domino.
    for (int i = 0; i < 28; i++)
    {
      String ownerIdx = boneyard.findOwner(Domino.DOMINOES[i]);

      btnArray[i] = new Button();
      btnArray[i].setId(Domino.DOMINOES[i] + ownerIdx);
      btnArray[i].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resource/" + Domino.DOMINOES[i] + ".png"))));

      // Register event handler to get input
      if (ownerIdx.contains("user"))
      {
        btnArray[i].addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handle);
        btnArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, this::handle);
      }
    }


    // User's dominoes are setting up and shown up on display
    for (int i = 0; i < 28; i++)
    {
      if (user.dominoes[i] != null) putDomino(hBox3, user.dominoes[i]);
    }

    hBox0.setPrefWidth(WINDOW_WIDTH);
    hBox0.setPrefHeight(50);
    hBox0.setAlignment(Pos.CENTER);
    hBox0.setTranslateY(100);

    hBox1.setPrefWidth(WINDOW_WIDTH);
    hBox1.setPrefHeight(50);
    hBox1.setAlignment(Pos.CENTER);

    hBox2.setPrefWidth(WINDOW_WIDTH);
    hBox2.setPrefHeight(50);
    hBox2.setAlignment(Pos.CENTER);

    hBox3.setPrefWidth(WINDOW_WIDTH);
    hBox3.setPrefHeight(50);
    hBox3.setAlignment(Pos.CENTER);
    hBox3.setTranslateY(100);


    Text invalidInput = new Text();
    invalidInput.setFont(Font.font("Arial Red", FontWeight.BLACK, 20));
    invalidInput.setTextAlignment(TextAlignment.CENTER);
    invalidInput.setText("Pick another one!");

    hBox0.getChildren().add(invalidInput);
    hBox0.setVisible(false);


    Text gameOver = new Text();
    gameOver.setFont(Font.font("Arial Black", FontWeight.BLACK, 35));
    gameOver.setTextAlignment(TextAlignment.CENTER);
    gameOver.setText("GAME OVER,   Winner:".toUpperCase());

    winner.setFont(Font.font("Arial Black", FontWeight.BLACK, 35));
    winner.setTextAlignment(TextAlignment.CENTER);
    winner.setText("User");

    hBox4.getChildren().add(gameOver);



    vbox.setTranslateY(150);
    vbox.getChildren().addAll(hBox1, hBox2, hBox3, hBox0);

    ((Group) scene.getRoot()).getChildren().addAll(vbox);

    stage.setScene(scene);
    stage.show();
  }

  //============================================================================================
  // Called by JavaFX when the user clicks or dragged user's dominoes.
  //============================================================================================
  public void handle(MouseEvent event)
  {
    Node node = (Node) event.getSource();
    HBox targetComputer = hBox1;

    /*********************************************************************
     * [MOUSE_CLICKED]  Domino Flipping
     *********************************************************************/
    if (event.getEventType() == MouseEvent.MOUSE_CLICKED)
    {
      hBox0.setVisible(false);

      int userDominoIdx = Integer.parseInt(node.getId().substring(6));

      ImageView rotatedImage = rotateImage(userDominoIdx, user);
      btnArray[boneyard.dominoesIndex[userDominoIdx]].setGraphic(rotatedImage);

      String newId = changeId(userDominoIdx, user);
      btnArray[boneyard.dominoesIndex[userDominoIdx]].setId(newId);
    }

    /*********************************************************************
     * [MOUSE_DRAGGED] Put a Domino on the Table
     *********************************************************************/
    if (event.getEventType() == MouseEvent.MOUSE_DRAGGED)
    {
      hBox0.setVisible(false);

      // (MOUSE_DRAGGED's direction) Left : -1 , Right : 1
      int direction;
      if (event.getX() < 49) direction = -1;
      else direction = 1;


      // Initializing the first turn
      if (hBox1.getChildren().size() == 0 && hBox2.getChildren().size() == 0)
      {
        headValue = node.getId().substring(0, 1);
        tailValue = node.getId().substring(1, 2);

        headHBox = hBox1;
        tailHBox = hBox1;
      }
      else
      {
        headValue = headHBox.getChildren().get(0).getId().substring(0, 1);
        tailValue = tailHBox.getChildren().get(tailHBox.getChildren().size() - 1).getId().substring(1, 2);
      }

      // When the game is over, table is cleaned and the winner will be shown up
      if (gameover)
      {
        if(hBox3.getChildren().size() != 0) winner.setText("Computer");
        else winner.setText("User");

        hBox4.getChildren().add(winner);

        vbox.getChildren().removeAll(hBox1, hBox2, hBox3);
        vbox.getChildren().add(hBox4);

      }
      /**********************************************************
       * User is Putting a Domino ...
       **********************************************************/
      else if (!gameover)
      {
        if (hBox1.getChildren().size() == 0)
        {
          hBox1.getChildren().add(node);
        }
        else if (hBox2.getChildren().size() != 0)
        {
          // When user dragged domino to the left side
          if (direction == -1)
          {
            // User dragged invalid domino - escape this handler of MOUSE_DRAGGED
            // So it won't move the user's domino and will give warning
            if (!headValue.equals("0") &&
                    !node.getId().substring(1, 2).equals("0") &&
                    !node.getId().substring(1, 2).equals(headValue))
            {
              hBox0.setVisible(true);
              return;
            }
            // User dragged valid domino
            else
            {
              tailHBox.getChildren().add(0, node);
              if (tailHBox.getChildren().size() != 1) tailHBox.setTranslateX(tailHBox.getTranslateX() - 49);

              if (tailHBox.equals(hBox1)) headHBox = hBox1;
              else headHBox = hBox2;

            }
          }
          // When user dragged domino to the right side
          else if (direction == 1)
          {
            // User dragged invalid domino - escape this handler of MOUSE_DRAGGED
            // So it won't move the user's domino and will give warning
            if (!tailValue.equals("0") &&
                    !node.getId().substring(0, 1).equals("0") &&
                    !node.getId().substring(0, 1).equals(tailValue))
            {
              hBox0.setVisible(true);
              return;
            }
            // User dragged valid domino
            else
            {
              headHBox.getChildren().add(node);
              if (headHBox.getChildren().size() != 1) headHBox.setTranslateX(headHBox.getTranslateX() + 49);

              if (headHBox.equals(hBox1)) tailHBox = hBox1;
              else tailHBox = hBox2;
            }
          }

        }

        // Because program checked that user put a valid domino,
        // now that domino should be removed from user's side
        int userDominoIdx = Integer.parseInt(node.getId().substring(6));
        user.dominoes[userDominoIdx] = null;
        hBox3.getChildren().remove(node);


        // Set the head value and tail value to computer knows the right place
        if (hBox1.getChildren().size() != 1 || hBox2.getChildren().size() != 0)
        {
          headValue = headHBox.getChildren().get(0).getId().substring(0, 1);
          tailValue = tailHBox.getChildren().get(tailHBox.getChildren().size() - 1).getId().substring(1, 2);

          if (headHBox.equals(hBox1) && tailHBox.equals(hBox1)) targetComputer = hBox2;
          else if (headHBox.equals(hBox2) && tailHBox.equals(hBox2)) targetComputer = hBox1;
        }

        // Check if the game is over
        if (hBox3.getChildren().size() == 0)
        {
          gameover = true;
          winner.setText("User");
          hBox4.getChildren().add(winner);

          vbox.getChildren().removeAll(hBox1, hBox2, hBox3);
          vbox.getChildren().add(hBox4);
        }


        /**********************************************************
         * Computer is Putting a Domino ...
         **********************************************************/
        int i, j, nullCount = 0;

        // Unlike User picked a domino,
        // computer's all dominoes should be checked validity and picked up
        for (i = 0; i < computer.dominoes.length; i++)
        {

          if (computer.dominoes[i] == null)
          {
            nullCount++;
            if (nullCount == computer.dominoes.length) break;
          }


          // Computer doesn't have matched one with both head and tail domino.
          // Computer is picking a right domino from boneyard until he can find it.
          if (i == (computer.dominoes.length - 1) && computer.dominoes[i] == null)
          {
            String candidate = "";
            int candidateIdx = 0;

            for (j = 0; j < 14; j++)
            {
              if ((boneyard.dominoes[j] != null) && (boneyard.dominoes[j].contains(headValue)
                      || boneyard.dominoes[j].contains(tailValue)
                      || boneyard.dominoes[j].contains("0")))
              {
                candidate = boneyard.dominoes[j];
                candidateIdx = j;
                break;
              }
            }

            for (int k = 0; k < computer.dominoes.length; k++)
            {
              if (computer.dominoes[k] == null)
              {
                computer.dominoes[k] = candidate;
                boneyard.dominoes[candidateIdx] = null;

                String val = btnArray[boneyard.dominoesIndex[j + 14]].getId().substring(0, 2);
                String newId2 = val + btnArray[boneyard.dominoesIndex[k + 7]].getId().substring(2);
                btnArray[boneyard.dominoesIndex[k + 7]].setId(newId2);
                boneyard.dominoesIndex[k + 7] = boneyard.dominoesIndex[j + 14];
                boneyard.dominoesIndex[j + 14] = 0;

                i = -1;
                nullCount = 0;
                break;
              }
            }
          }

          if (i == -1)
          {
            i = 0;
            continue;
          }

          if (computer.dominoes[i] == null) continue;

          double tmpStartX = 0, tmpEndX = 0;

          // Computer has a domino which is matched with a head domino
          if (computer.dominoes[i].contains(headValue) || node.getId().substring(0, 1).contains("0"))
          {
            tmpStartX = -49;

            if (computer.dominoes[i].substring(0, 1).equals(headValue))
            {
              ImageView rotatedImage = rotateImage(i, computer);
              btnArray[boneyard.dominoesIndex[i + 7]].setGraphic(rotatedImage);

              String newId = changeId(i, computer);
              btnArray[boneyard.dominoesIndex[i + 7]].setId(newId);
            }

            if (hBox2.getChildren().size() == 0)
            {
              hBox2.getChildren().add(btnArray[boneyard.dominoesIndex[i + 7]]);
              hBox2.setTranslateX(hBox2.getTranslateX() + tmpStartX);

              headHBox = hBox2;
              tailHBox = hBox1;
            } else
            {
              targetComputer.getChildren().add(0, btnArray[boneyard.dominoesIndex[i + 7]]);
              targetComputer.setTranslateX(tailHBox.getTranslateX() + tmpStartX);

              if (targetComputer.equals(hBox1))
              {
                headHBox = hBox1;
                tailHBox = hBox2;
              } else
              {
                headHBox = hBox2;
                tailHBox = hBox1;
              }
            }
            computer.dominoes[i] = null;
            break;

          }
          // Computer has a domino which is matched with a tail domino
          else if (computer.dominoes[i].contains(tailValue) || node.getId().substring(1, 2).contains("0"))
          {
            tmpEndX = 49;

            if (computer.dominoes[i].substring(1, 2).equals(tailValue))
            {
              ImageView rotatedImage = rotateImage(i, computer);
              btnArray[boneyard.dominoesIndex[i + 7]].setGraphic(rotatedImage);

              String newId = changeId(i, computer);
              btnArray[boneyard.dominoesIndex[i + 7]].setId(newId);
            }


            if (hBox2.getChildren().size() == 0)
            {
              hBox2.getChildren().add(btnArray[boneyard.dominoesIndex[i + 7]]);
              hBox2.setTranslateX(hBox2.getTranslateX() + tmpEndX);

              headHBox = hBox1;
              tailHBox = hBox2;
            } else
            {
              targetComputer.getChildren().add(btnArray[boneyard.dominoesIndex[i + 7]]);
              targetComputer.setTranslateX(headHBox.getTranslateX() + tmpEndX);

              if (targetComputer.equals(hBox1))
              {
                tailHBox = hBox1;
                headHBox = hBox2;
              } else
              {
                tailHBox = hBox2;
                headHBox = hBox1;
              }
            }

            computer.dominoes[i] = null;
            break;

          }
          // Computer has a wildcard
          else if (computer.dominoes[i].contains("0"))
          {
            tmpEndX = 49;

            if (computer.dominoes[i].substring(1, 2).equals("0"))
            {
              ImageView rotatedImage = rotateImage(i, computer);
              btnArray[boneyard.dominoesIndex[i + 7]].setGraphic(rotatedImage);

              String newId = changeId(i, computer);
              btnArray[boneyard.dominoesIndex[i + 7]].setId(newId);
            }

            if (hBox2.getChildren().size() == 0)
            {
              hBox2.getChildren().add(btnArray[boneyard.dominoesIndex[i + 7]]);
              hBox2.setTranslateX(hBox2.getTranslateX() + tmpEndX);

              headHBox = hBox1;
              tailHBox = hBox2;
            } else
            {
              targetComputer.getChildren().add(btnArray[boneyard.dominoesIndex[i + 7]]);
              targetComputer.setTranslateX(headHBox.getTranslateX() + tmpEndX);

              if (targetComputer.equals(hBox1))
              {
                tailHBox = hBox1;
                headHBox = hBox2;
              } else
              {
                tailHBox = hBox2;
                headHBox = hBox1;
              }
            }

            computer.dominoes[i] = null;
            break;
          }
        }

        // Computer put every dominoes on the table.
        // Winner is Computer
        if (nullCount == (computer.dominoes.length))
        {
          gameover = true;

          winner.setText("Computer");
          hBox4.getChildren().add(winner);

          vbox.getChildren().removeAll(hBox1, hBox2, hBox3);
          vbox.getChildren().add(hBox4);
        }


        // By a domino that computer put on the table,
        // head domino or tail domino is changed.
        headValue = headHBox.getChildren().get(0).getId().substring(0, 1);
        tailValue = tailHBox.getChildren().get(tailHBox.getChildren().size() - 1).getId().substring(1, 2);


        int x;
        boolean stopAdd = false;

        // Check if user has at least one valid domino
        for (x = 0; x < user.dominoes.length; x++)
        {
          if (user.dominoes[x] != null && (user.dominoes[x].contains(headValue)
                  || user.dominoes[x].contains(tailValue)
                  || user.dominoes[x].contains("0")))
          {
            x = -1;
            break;
          }
        }

        // User doesn't have matched domino with head or tail domino.
        // User is picking a right domino from boneyard until he can find it.
        if ((x != -1) && (!gameover))
        {
          while (!stopAdd)
          {
            String candidate2 = "";
            int candidateIdx2 = 0;

            for (int y = 0; y < 14; y++)
            {

              if (boneyard.dominoes[y] != null)
              {
                candidate2 = boneyard.dominoes[y];
                candidateIdx2 = y;

                if (boneyard.dominoes[y].contains(headValue)
                        || boneyard.dominoes[y].contains(tailValue)
                        || boneyard.dominoes[y].contains("0"))
                {
                  stopAdd = true;
                }
                break;
              }
            }


            for (int z = 0; z < user.dominoes.length; z++)
            {
              if (user.dominoes[z] == null)
              {
                user.dominoes[z] = candidate2;
                boneyard.dominoes[candidateIdx2] = null;

                String val = btnArray[boneyard.dominoesIndex[candidateIdx2 + 14]].getId().substring(0, 2);
                String newId2 = val + btnArray[boneyard.dominoesIndex[z]].getId().substring(2);

                boneyard.dominoesIndex[z] = boneyard.dominoesIndex[candidateIdx2 + 14];
                boneyard.dominoesIndex[candidateIdx2 + 14] = 0;
                btnArray[boneyard.dominoesIndex[z]].setId(newId2);
                hBox3.getChildren().add(btnArray[boneyard.dominoesIndex[z]]);

                btnArray[boneyard.dominoesIndex[z]].addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handle);
                btnArray[boneyard.dominoesIndex[z]].addEventHandler(MouseEvent.MOUSE_CLICKED, this::handle);

                x = -1;
                break;
              }
            }
          }
        }
      }
    }
  }

  //============================================================================================
  // Called by Button
  // when Button's image is flipped by MOUSE_CLICKED
  // or player picked a domino from boneyard
  //============================================================================================
  private String changeId(int i, Player player)
  {

    String newId = "";

    if (player.equals(computer))
    {
      newId = player.dominoes[i].substring(1, 2) + player.dominoes[i].substring(0, 1) +
              btnArray[boneyard.dominoesIndex[i + 7]].getId().substring(2);
    } else if (player.equals(user))
    {
      newId = player.dominoes[i].substring(1, 2) + player.dominoes[i].substring(0, 1) +
              btnArray[boneyard.dominoesIndex[i]].getId().substring(2);
    }

    player.dominoes[i] = player.dominoes[i].substring(1, 2) + player.dominoes[i].substring(0, 1);

    return newId;
  }

  //============================================================================================
  // Called by Button to flip a domino by MOUSE_CLICKED
  //============================================================================================
  private ImageView rotateImage(int i, Player player)
  {
    ImageView rotatedImage = new ImageView(new Image(getClass().getResourceAsStream("resource/" + player.dominoes[i] + ".png")));
    rotatedImage.setRotate(180);

    return rotatedImage;
  }


  //============================================================================================
  // To initialize Button's array fitting with right Image
  //============================================================================================
  private void putDomino(HBox hBox, String domino)
  {
    switch (domino)
    {
      case "00":
        hBox.getChildren().add(btnArray[0]);
        break;
      case "10":
        hBox.getChildren().add(btnArray[1]);
        break;
      case "11":
        hBox.getChildren().add(btnArray[2]);
        break;

      case "20":
        hBox.getChildren().add(btnArray[3]);
        break;
      case "21":
        hBox.getChildren().add(btnArray[4]);
        break;
      case "22":
        hBox.getChildren().add(btnArray[5]);
        break;

      case "30":
        hBox.getChildren().add(btnArray[6]);
        break;
      case "31":
        hBox.getChildren().add(btnArray[7]);
        break;
      case "32":
        hBox.getChildren().add(btnArray[8]);
        break;
      case "33":
        hBox.getChildren().add(btnArray[9]);
        break;

      case "40":
        hBox.getChildren().add(btnArray[10]);
        break;
      case "41":
        hBox.getChildren().add(btnArray[11]);
        break;
      case "42":
        hBox.getChildren().add(btnArray[12]);
        break;
      case "43":
        hBox.getChildren().add(btnArray[13]);
        break;
      case "44":
        hBox.getChildren().add(btnArray[14]);
        break;

      case "50":
        hBox.getChildren().add(btnArray[15]);
        break;
      case "51":
        hBox.getChildren().add(btnArray[16]);
        break;
      case "52":
        hBox.getChildren().add(btnArray[17]);
        break;
      case "53":
        hBox.getChildren().add(btnArray[18]);
        break;
      case "54":
        hBox.getChildren().add(btnArray[19]);
        break;
      case "55":
        hBox.getChildren().add(btnArray[20]);
        break;

      case "60":
        hBox.getChildren().add(btnArray[21]);
        break;
      case "61":
        hBox.getChildren().add(btnArray[22]);
        break;
      case "62":
        hBox.getChildren().add(btnArray[23]);
        break;
      case "63":
        hBox.getChildren().add(btnArray[24]);
        break;
      case "64":
        hBox.getChildren().add(btnArray[25]);
        break;
      case "65":
        hBox.getChildren().add(btnArray[26]);
        break;
      case "66":
        hBox.getChildren().add(btnArray[27]);
        break;
      default:
        break;
    }
  }


  public static void main(String[] args)
  {
    launch(args);
  }

}
