/* TowerClimber
 * Bruce Lin
 * 6/9/2022
 * A tower climber turn-based RPG math game in both single and multiplayer with a menu system to navigate the game as well as autosaves and save files (more information on the doc) https://docs.google.com/document/d/1A5LfwO7WIpMUd72f21hURWRgLE1NpAHnDIDlScsZENI/edit?usp=sharing
 */

//Imports from lib folder
import hsa.Console;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class TowerClimber
{
  //Declare Console variable
  static Console c;
  static Console f;
  static Image [] texturePack = new Image [1];                //Texture pack for game
  static Image [] texturePackMulti = new Image [1];           //Texture pack for multiplayer
  static Font statFont = new Font("Monospaced",Font.BOLD,16); //Default font for stats when outputting to the c console
  static Color shade3 = new Color(124, 3, 0);                 //Default color for text in the game
  static int checker=0;                                       //Checks if there are already 2 consoles open (console c and f)
  
  //Main method
  public static void main (String args[])throws IOException{
    //Create Console object and give it dimensions
    c = new Console(27,120);
    while(true){
      BufferedReader input = new BufferedReader(new FileReader ("HighScore.txt"));   //Inputs the highest score and name of the person who earned that score
      String highScore = input.readLine();                                           //Highscore from the text file (HighScore.txt)
      String hiScoreName = input.readLine();                                         //Name of person who hit the high score
      int [] user1Stats = new int []{10,10,5,5,2,50,50,5,0,0,0,0,5,0};               //The first users in game character statistics
      int [] user2Stats = new int []{10,10,5,5,2,50,50,5,999,0,999,999,5};           //The second users in game character statistics
      int [] cpuStats = new int []{5,5,2,2,2,30,0,0,0,0,0,0,0};                      //The computers in game character statistics
      int mainMenuOption;                                                            //The option that was chosen in the main menu
      int avaiableSave;                                                              //Checks if there is already a save file or not
      
      //Validates if all files exist in the texturePack folder
      try{
        texturePack = new Image [] {ImageIO.read(new File("texturePack\\saveLocatedSP.png")),ImageIO.read(new File("texturePack\\invalidInput.png")),ImageIO.read(new File("texturePack\\startingNewGameSP.png")),ImageIO.read(new File("texturePack\\mainMenuSP.png")),ImageIO.read(new File("texturePack\\welcomeSP.png")),ImageIO.read(new File("texturePack\\newOrLoadSP.png")),ImageIO.read(new File("texturePack\\helpSP.png")),ImageIO.read(new File("texturePack\\howToPlaySP.png")),ImageIO.read(new File("texturePack\\gameDescriptionSP.png")),ImageIO.read(new File("texturePack\\combatSP.png")),ImageIO.read(new File("texturePack\\towerGrassSP.png")),ImageIO.read(new File("texturePack\\towerSkySP.png")),ImageIO.read(new File("texturePack\\saveSP.png")),ImageIO.read(new File("texturePack\\closingSP.png")),ImageIO.read(new File("texturePack\\staticSP.png")),ImageIO.read(new File("texturePack\\enemy1.png")),ImageIO.read(new File("texturePack\\enemy2.png")),ImageIO.read(new File("texturePack\\enemy3.png")),ImageIO.read(new File("texturePack\\towerGrass2SP.png")),ImageIO.read(new File("texturePack\\towerSky2SP.png")),ImageIO.read(new File("texturePack\\loadSaveSP.png")),ImageIO.read(new File("texturePack\\fileDNESP.png")),ImageIO.read(new File("texturePack\\powerUpSP.png")),ImageIO.read(new File("texturePack\\gameOverSP.png")),ImageIO.read(new File("texturePack\\newHighScoreSP.png")),ImageIO.read(new File("texturePack\\outputSP.png")),ImageIO.read(new File("texturePack\\highScoreSP.png")),ImageIO.read(new File("texturePack\\enemy1avaSP.png")),ImageIO.read(new File("texturePack\\enemy2avaSP.png")),ImageIO.read(new File("texturePack\\enemy3avaSP.png"))};
      }
      catch(IOException e){
        for(int i=0;i<texturePack.length;i++){ //Turns all textures to null if there is a missing file
          texturePack[i] = null;
        }
      }
      //Validates if all files exist in the texturePack folder
      try{
        texturePackMulti = new Image [] {ImageIO.read(new File("texturePack\\combatP1SP.png")),ImageIO.read(new File("texturePack\\combatP2SP.png")),ImageIO.read(new File("texturePack\\player1StartSP.png")),ImageIO.read(new File("texturePack\\player2StartSP.png")),ImageIO.read(new File("texturePack\\player1TurnSP.png")),ImageIO.read(new File("texturePack\\player2TurnSP.png ")),ImageIO.read(new File("texturePack\\gameOver1WinSP.png")),ImageIO.read(new File("texturePack\\gameOver2WinSP.png"))};
      }
      catch(IOException e){
        for(int i=0;i<texturePackMulti.length;i++){ //Turns all textures to null if there is a missing file
          texturePack[i] = null;
        }
      }
      
      //Check if there is a save to continue from
      avaiableSave = checkIfAvaiableSave();
      
      //if there isn't a save then the available save wouldn't have a value and the game will proceed
      
      //Check if there is a save to continue from
      avaiableSave = checkIfAvaiableSave();
      user1Stats [10] = 0;
      if (avaiableSave==0){ //Does not open the autosave if there if there isn't one
        titleScreen();
        
        //Main menu that also gives user1Stats their corresponding stats
        mainMenuOption = aiPlayHelpQuit(highScore,hiScoreName);
        while(true){
          if(mainMenuOption==1){ //Save or load a game
            user1Stats = saveOrLoad(user1Stats,cpuStats);
            break;
          }
          else if(mainMenuOption==2){ //Multiplayer
            user1Stats [10] = 1;
            break;
          }
          else if(mainMenuOption==3){ //Help menu
            help ();
            mainMenuOption = aiPlayHelpQuit(highScore,hiScoreName);
          }
          else{ //Close game
            System.exit(0);
          }
        }
      }      
      if(avaiableSave==1){ //Opens the auto save if there is one
        user1Stats = loadSave(user1Stats,cpuStats);
      }
      
      //Singleplayer
      if(user1Stats[10]==0){
        user1Stats = towerAndLevelNumber(user1Stats,cpuStats); //First tower screen
        if(cpuStats[11] ==1){
          System.exit(0);
        }
        
        while(true){ 
          user1Stats = combatScreen(user1Stats,cpuStats); //Combat screen
          if(user1Stats[11] == 1){ //Continues the game if the user won
            user1Stats[8] = cpuStats[8]; //Updates the user stage level
            cpuStats[12]=0; //Resets checker for who won
            user1Stats[13]++;
          }
          else if (user1Stats [11]==3){ //Breaks out of loop if the user lost
            break;
          }
          user1Stats = towerAndLevelNumber(user1Stats,cpuStats); //Tower screens after the first
          if(user1Stats[13]==2){
            user1Stats[13]=0;
            powerUpMenu(user1Stats); //Opens the power up menu on every other round
          }
        }
        
        //Gameover information
        gameOverHiScore(user1Stats,cpuStats,highScore);
        avaiableSave=0;
      }
      
      //Multiplayer
      else{
        int attackFirst;      //Determines who attacks first
        int firstMenuCheck=0; //Checks if the first screen was already shown or not
        int turnCheck;        //Current players turn
        int checkWhoWon=0;    //Determines who won the game
        turnCheck=(int)(Math.random()*2+1); //Randomly decides who goes first
        attackFirst = turnCheck;
        user1Stats = statRandomizerMulti(user1Stats);
        user2Stats = statRandomizerMulti(user2Stats);
        if(checker == 0){ //Opens the f console if its not open already
          f = new Console(27,120);
          checker++;
        }
        checkWhoWon = combatMulti(user1Stats,user2Stats,turnCheck,attackFirst,firstMenuCheck); //Game combat that returns winner
        postGameMulti(user1Stats,user2Stats,checkWhoWon); //Game over screen after a winner is determined
      }//Multiplayer
      checker=0;
    }//Restart game
  }//main
  
//Single player methods
  //Checks if there is an available autoSave to open
  public static int checkIfAvaiableSave ()throws IOException{
    int checker=0; //Checker if there is a save or not
    int invalid=0; //Invalid message reports
    try{
      BufferedReader save = new BufferedReader(new FileReader ("userAutoSave.txt")); //Read the users auto save
      BufferedReader cpu = new BufferedReader(new FileReader ("cpuAutoSave.txt")); //Read the cpus auto save
      String strTemp = save.readLine(); //Validates the read text into a int
      int temp = Integer.parseInt(strTemp);
      if(temp==2){ //Does not open auto save
        save.close(); //Closed userAutoSave
        cpu.close(); //Closes cpuAutoSave
        return(checker); //Returns the checker
      }
    }
    catch (FileNotFoundException e){
      return(checker); //Returns the checker
    }
    while(true){
      c.drawImage(texturePack[0],1,1,null);
      c.setCursor(6,7);
      if (invalid!=0){
        invalidInput(); //Invalid input
      }
      String yesOrNo = c.readLine();
      if(yesOrNo.equalsIgnoreCase("y")){
        checker++; //Verify to load save
        break;
      }
      else if (yesOrNo.equalsIgnoreCase("n")){
        PrintWriter save = new PrintWriter (new FileWriter ("userAutoSave.txt")); //Prints to user auto save
        save.println("2"); //Clears the auto save
        save.close(); //Closes userAutoSave
        c.drawImage(texturePack[2],1,1,null);
        c.setCursor(6,7);
        c.readLine();
        break;
      }
      else{
        invalid++; //Invalid input
      }
    }
    return(checker); //Returns the checker
  }//checkIfAvaiableSave
  
  //Loads the auto save file if there is one
  public static int [] loadSave (int inputSave [],int [] cpuStats)throws IOException{
    BufferedReader input = new BufferedReader(new FileReader ("userAutoSave.txt")); //Read the users auto save
    BufferedReader cpu = new BufferedReader(new FileReader ("cpuAutoSave.txt")); //Read the cpus auto save
    String strInputSave = input.readLine(); //Reads first line
    String strCpuSave = cpu.readLine(); //Reads first line
    int i=0; //Counter for loading save with dead enemy
    int j=0; //Counter for loading save with alive enemy
    if (Integer.parseInt(strInputSave) == 1){ //If enemy is dead
      strInputSave = input.readLine();
      while (strInputSave != null){ //Imports file data to user stats
        inputSave[i] = Integer.parseInt(strInputSave);
        strInputSave = input.readLine();
        i++;
      }
    }
    else{ //If enemy is alive
      strInputSave = input.readLine();
      while (strInputSave != null){ //Imports file data to user stats
        inputSave[i] = Integer.parseInt(strInputSave);
        strInputSave = input.readLine();
        i++;
      }
      while (strCpuSave != null){ //Imports file data to cpu stats
        cpuStats[j] = Integer.parseInt(strCpuSave);
        strCpuSave = cpu.readLine();
        j++;
      }
    }
    input.close(); //Closes user auto save
    cpu.close(); //Closes cpu auto save
    return(inputSave); //Returns the users stats
  }//loadSave
  
  //The games title screen
  public static void titleScreen (){
    c.drawImage(texturePack[4],1,1,null);
    c.setCursor(6,7);
    c.readLine();
  }//titleScreen
  
  //Gives the user 4 options in the main menu
  public static int aiPlayHelpQuit (String highScoreMenu, String hiScoreName){
    String nameAndFloor; //String of highscore name and floor
    try{
      nameAndFloor = hiScoreName.concat(" Floor: "); //Concat the high score name to floor
    }
    catch(NullPointerException e){
      nameAndFloor = "N/A";
    }
    int loadNewHelpQuit; //Option that is selected from user
    int invalid=0; //Invalid input
    Font highScoreFont = new Font("Monospaced",Font.BOLD,20);
    while(true){
      try{ 
        c.setFont(highScoreFont); //Set font
        c.setColor(shade3); //Set color for font
        c.drawImage(texturePack[3],1,1,null);
        c.drawString(nameAndFloor+highScoreMenu,275,53); //Display 
        c.setCursor(22,7);
        if(invalid!=0){
          invalidInput(); //Invalid input
        }
        String strLoadNewHelpQuit = c.readLine(); //Validates input
        loadNewHelpQuit = Integer.parseInt(strLoadNewHelpQuit);
        if(loadNewHelpQuit<=4 && loadNewHelpQuit>=1){ //Validates input
          break;
        }
        else{
          invalid++; //Invalid input
        }
      }
      catch(NumberFormatException e){
        invalid++; //Invalid input
      }
    }
    return(loadNewHelpQuit);
  }//aiPlayHelpQuit
  
  //Ask if the user wants to save or load a file
  public static int [] saveOrLoad (int [] tempStats,int [] cpuStats)throws IOException{
    int loadNew;   //Load new game
    int invalid=0; //Invalid
    while(true){
      try{
        c.drawImage(texturePack[5],1,1,null);
        if(invalid==1){ 
          invalidInput(); //Displays invalid input
        }
        c.setCursor(3,7);
        String strLoadNew = c.readLine(); //Validate input as int
        loadNew = Integer.parseInt(strLoadNew);
        if (loadNew==1 ^ loadNew==2){ //Validate input either 1 or 2
          if (loadNew==1){
            tempStats = statRandomizer(tempStats); //New stats
          }
          else{
            tempStats = saveFileRead(tempStats,cpuStats); //Load stats
          }
          break;
        }
        else{
          invalid=1; //Invalid input
        }
      }
      catch(NumberFormatException e){
        invalid=1; //Invalid input
      }
    }
    return (tempStats); //Return the users stats
  }//saveOrLoad
  
  //Gets data from save file and puts it in the array
  public static int [] saveFileRead (int inputSave [],int [] cpuStats)throws IOException{
    while(true){
      int dne=0; //File does not exist
      try{
        c.drawImage(texturePack[20],1,1,null);
        if(dne == 1){ //File does not exist message
          c.drawImage(texturePack[21],35,475,null);
        }
        c.setCursor(6,7);
        String saveName = c.readLine(); //Reads name
        String temp = saveName; //Temp name
        saveName = saveName.concat(".txt"); //User save name
        String cpuSaveName = temp.concat("Cpu.txt"); //CPU save name
        BufferedReader input = new BufferedReader(new FileReader (saveName)); //Reads user save
        String strInputSave = input.readLine();
        int i=0; //Counter for user
        int checkIfCPUAlive = Integer.parseInt(strInputSave);
        if(checkIfCPUAlive == 1){ //Checks what stage the user is at (if the cpu is dead or not)
          strInputSave = input.readLine();
          while (strInputSave != null){ //Copies from user save to user stats
            inputSave[i] = Integer.parseInt(strInputSave);
            strInputSave = input.readLine();
            i++;
          }
          return(inputSave); //Return user stats
        }
        else{
          BufferedReader cpu = new BufferedReader(new FileReader (cpuSaveName)); //Read CPU save
          String strCpuStats = cpu.readLine();
          int j=0; //Counter for user
          int k=0; //Counter for CPU
          strInputSave = input.readLine();
          while (strInputSave != null){ //Copies from user save to user stats
            inputSave[j] = Integer.parseInt(strInputSave);
            strInputSave = input.readLine();
            j++;
          }
          while (strCpuStats != null){ //Copies from CPU save to CPU stats
            cpuStats[k] = Integer.parseInt(strCpuStats);
            strCpuStats = cpu.readLine();
            k++;
          }
          cpuStats[12]=1;
          return(inputSave); //Return user stats
        }
      }
      catch (FileNotFoundException e){
        dne=1; //File does not exist
      }   
    }
  }//saveFileRead
  
  //Tells the user how to play the game or gives a description
  public static void help (){
    int invalid=0; //Invalid input
    while(true){
      try{
        c.drawImage(texturePack[6],1,1,null);
        if(invalid!=0){ //Invalid input
          invalidInput();
        }
        c.setCursor(3,7);
        String strOption = c.readLine(); 
        int option = Integer.parseInt(strOption); //Validates input for int
        if (option==1){ //How to play the game
          c.drawImage(texturePack[7],1,1,null);
          c.setCursor(6,7);
          c.readLine();
          break;
        }
        else if (option==2){ //Game description
          c.drawImage(texturePack[8],1,1,null);
          c.setCursor(6,7);
          c.readLine();
          break;
        }
        else{
          invalid++; //Invalid input
        }
      }
      catch(NumberFormatException e){
        invalid++; //Invalid input
      }
    }
  }//help
  
  //Displays the tower as well as level numbers 
  public static int [] towerAndLevelNumber (int [] towerStats,int [] cpuStats)throws IOException{
    if(towerStats[8] == 0){ //Displays bottom floor if it is their first round
      c.drawImage(texturePack[10],1,1,null);
      c.setCursor(3,44);
      c.readLine();
    }
    else{
      while(true){
        try{
          floorCleared(towerStats); //Displays static screen
          c.drawImage(texturePack[11],1,1,null);
          c.setCursor(9,4);
          String strPlayOrSave = c.readLine(); 
          int playOrSave = Integer.parseInt(strPlayOrSave); //Validates input for int
          if(playOrSave == 1){
            break; 
          }
          else if(playOrSave == 2){
            saveGame(towerStats,cpuStats); //Saves game progress
            break;
          }
          else{ //Validates input either 1 or 2
            invalidInput(); //Invalid input
          }
        }
        catch(NumberFormatException e){
          invalidInput(); //Invalid input
        }
      }
    }
    if(towerStats[8] == 0){ //First floor
      c.drawImage(texturePack[18],1,1,null);
    } 
    else{ //Other floors
      c.drawImage(texturePack[19],1,1,null);
    }
    towerStats[8] = towerStats[8]++; //Adds one to user floor count
    return(towerStats);
  }//towerAndLevelNumber
  
  //Singleplayer gameplay in a new console
  public static int [] combatScreen(int [] userStats,int [] cpuStats)throws IOException{
    if(checker == 0){ //Opens f console if not already open
      f = new Console(27,120);
      checker++;
    }
    if(cpuStats[12]==0){ //Gives CPU their stats if not loaded from a save
      userStats[12] = 5;
      cpuStats [8] = userStats[8];
      cpuStats [8] = cpuStats [8]+1;  
      cpuStats [0] = 5;
      cpuStats [1] = 5;
      cpuStats [2] = 2;
      cpuStats [3] = 2;
      cpuStats [5] = 30;
      cpuStats = statModify(cpuStats);
      cpuStats [10] = (int)(Math.random()*3+1);
    }
    String [] userCpuLog = new String []{"","","","",""}; //Game data log (invlaid input, user healed,...)
    while(true){
      try{
        updateCombat(userStats,cpuStats,userCpuLog); //Update display of user and enemy plus stats
        f.setCursor(26,93);
        autoSave(userStats,cpuStats); //Saves progress of game
        String strAbility = f.readLine(); 
        int ability = Integer.parseInt(strAbility); //Validates input for int
        if (ability<=6 && ability>=1){
          userStats [11] = abilityChosen(ability,userStats,cpuStats,userCpuLog); //Executes ability chosen
          if (userStats [11] == 0){
            //end of round heal from passive regen (make sure to check if they're dead before this point)
            userStats [6] = (userStats [6]) + (userStats [4]); //Heal user
            if(userStats[6]>userStats[5]){ //Resets CHP to THP if CHP is higher than THP
              userStats[6] = userStats[5]; 
            }
            cpuStats [6] = (cpuStats [6]) + (cpuStats [4]); //Heal CPU
            if(cpuStats[6]>cpuStats[5]){ //Resets CHP to THP if CHP is higher than THP
              cpuStats[6] = cpuStats[5]; 
            }
            autoSave(userStats,cpuStats); //Saves progress of game
          }
          else if (userStats[11]==1){
            userStats [9] = 0;//resets block of user
            cpuStats [9] = 0;
            break;
          }
          else if (userStats[11]==2){ //userstats 11 == 2
          }
          else{//userstats 11 == 3
            break;
          }
        }
        else if(ability ==7){ //Quit and save game
          f.close();
          saveGame(userStats,cpuStats); //Saves game progress
        }
        else{
          userCpuLog = importToScreen(2,"N/A",0,userCpuLog); //Displays game data
        }
      }
      catch(NumberFormatException e){
        userCpuLog = importToScreen(2,"N/A",0,userCpuLog); //Displays game data
      }
    }
    return(userStats); //Return user stats
  }//combatScreen
  
  //Modify the cpu stats 
  public static int [] statModify (int [] cpuStats){
    for(int i=0;i<4;i++){ //Adds to AD AP AR MR
      cpuStats[i] = cpuStats[i]+(int)(Math.random()*10+1);
      cpuStats[i] = cpuStats[i] +(int)(cpuStats [8]*2);
    }
    cpuStats[5] = cpuStats[5]+(int)(Math.random()*50+1); //Adds to THP
    cpuStats[5] = cpuStats[5] +cpuStats [8]*2; //Adds to PR
    cpuStats[6] = cpuStats[5]; //Make CHP to THP
    return(cpuStats);
  }//statModify
  
  //Randomizes the bonus stats to add to their base stats 
  public static int [] statRandomizer (int inputStats []){
    for(int i=0;i<4;i++){ //Adds to AD AP AR MR
      inputStats[i] = inputStats[i]+(int)(Math.random()*10+1);
    }
    inputStats[5] = inputStats[5]+(int)(Math.random()*50+1); //Adds to THP
    inputStats[6] = inputStats[5];
    return(inputStats); //Make CHP to THP
  }//statRandomizer
  
  //Updates progress of the game through the stats of the user and CPU and keeps track of logs
  public static void updateCombat(int [] userStats,int [] cpuStats,String [] userCpuLog){
    String [] strUserStats = new String [userStats.length]; //String of user stats
    String [] strCPUStats = new String [cpuStats.length]; //String of CPU stats
    f.drawImage(texturePack[9],1,1,null); 
    f.setFont(statFont); //Sets font
    f.setColor(shade3); //Sets color of font
    for(int i=0;i<userStats.length;i++){ //Converts int user stats to String
      strUserStats[i] = Integer.toString(userStats[i]);
    }
    for(int i=0;i<cpuStats.length;i++){ //Converts int CPU stats to String
      strCPUStats[i] = Integer.toString(cpuStats[i]);
    }
    if(cpuStats[10]==1){ //Random skin 1
      f.drawImage(texturePack[15],480,140,null); 
      f.drawImage(texturePack[27],670,14,null);       
    }
    else if(cpuStats[10]==2){ //Random skin 2
      f.drawImage(texturePack[16],480,140,null);    
      f.drawImage(texturePack[28],670,14,null);       
    }
    else{ //Random skin 3
      f.drawImage(texturePack[17],480,140,null);  
      f.drawImage(texturePack[29],670,14,null);       
    }
    //Display user stats
    f.drawString(strUserStats[0],38,450);
    f.drawString(strUserStats[1],38,482);
    f.drawString(strUserStats[2],135,450);
    f.drawString(strUserStats[3],135,482);
    f.drawString(strUserStats[4],38,516);
    f.drawString(strUserStats[6],135,516);
    f.drawString(strUserStats[12]+"x",540,516);
    //Display CPU stats
    f.drawString(strCPUStats[0],793,28);
    f.drawString(strCPUStats[1],793,60);
    f.drawString(strCPUStats[2],890,28);
    f.drawString(strCPUStats[3],890,60);
    f.drawString(strCPUStats[4],793,94);
    f.drawString(strCPUStats[6],890,94);
    f.drawString(strCPUStats[8],717,94);
    //Game log
    f.drawString(userCpuLog[0],213,458);
    f.drawString(userCpuLog[1],213,508);
    f.drawString(userCpuLog[2],213,450);
    f.drawString(userCpuLog[3],213,465);
    f.drawString(userCpuLog[4],213,458);
    userCpuLog = importToScreen(4,"N/A",0,userCpuLog); //Reset game log
  }//updateCombat
  
  //Displays a screen after a level has been beat to the f console
  public static void floorCleared (int [] userStats){
    String [] strUserStats = new String [userStats.length];
    for(int i=0;i<userStats.length;i++){ //Converts user stats to string
      strUserStats[i] = Integer.toString(userStats[i]);
    }
    if(checker == 0){
      f = new Console(27,120);
      checker++;
      f.drawImage(texturePack[14],1,1,null);
    }
    else{ //Displays users stats
      f.drawImage(texturePack[13],1,1,null);
      f.drawString(strUserStats[0],415,242);
      f.drawString(strUserStats[1],415,274);
      f.drawString(strUserStats[2],513,242);
      f.drawString(strUserStats[3],513,274);
      f.drawString(strUserStats[4],415,308);
      f.drawString(strUserStats[6],513,308);
    }
  }//floorCleared
  
  //Runs the according code to what ability was chosen
  public static int abilityChosen (int ability,int [] userStats,int [] cpuStats,String [] userCpuLog){
    int damage; //Potential damage
    int cpuAttack = (int)(Math.random()*6+1); //Random attack from CPU
    if (userStats[9] ==1){ //Resets block defenses after a round
      userStats[2] = userStats[2]/10;
      userStats[3] = userStats[3]/10; 
      userStats[9] = 2; //Prevents user from blocking again
    }
    switch(ability){ //Ability chosen
      case 1: //Basec attack
        if (mathGameMenu(1,userCpuLog,userStats,cpuStats) != 0){ //If attack hits
        damage = (userStats[0]-cpuStats[2]); //Adjust total damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog = importToScreen(0,"ad damage dealt: ",damage,userCpuLog); //Display damage done
        if(cpuAttack == 6){ //User attack misses from CPU block
        }
        else if(cpuAttack ==5){ //Damage calculator
          cpuStats[6] = cpuStats[6]-(damage)+cpuStats[4];
        }
        else{
          cpuStats[6] = cpuStats[6]-(damage); //Deal damage to CPU
        } 
      }
        userStats [11] = didUserWin(cpuStats); //Checks if user won
        if(userStats [11] ==1){ //Breaks if user won
          break;
        }
        else{
          userStats = damageFromCPU(cpuStats,userStats,cpuAttack,userCpuLog); //Take damage from CPU
          userStats [11] = didCPUWin(userStats); //Checks if CPU won
          break; 
        }
      case 2: //Heavy attack
        if (mathGameMenu(2,userCpuLog,userStats,cpuStats) != 0){ //If attack hits
        damage = (userStats[0]*2-cpuStats[2]); //Adjust total damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog = importToScreen(0,"AD damage dealt ",damage,userCpuLog); //Display damage done
        if(cpuAttack == 6){ //User attack misses from CPU block
        }
        else if(cpuAttack ==5){ //Damage calculator
          cpuStats[6] = cpuStats[6]-(damage)+cpuStats[4];
        }
        else{
          cpuStats[6] = cpuStats[6]-(damage); //Deal damage to CPU
        } 
      }
        userStats [11] = didUserWin(cpuStats);
        if(userStats [11] ==1){ //Breaks if user won
          break;
        }
        else{
          userStats = damageFromCPU(cpuStats,userStats,cpuAttack,userCpuLog); //Take damage from CPU
          userStats [11] = didCPUWin(userStats); //Checks if CPU won
          break; 
        }
      case 3: //Basic magic
        if (mathGameMenu(1,userCpuLog,userStats,cpuStats) != 0){ //If attack hits
        damage = (userStats[1]-cpuStats[3]); //Adjust total damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog = importToScreen(0,"ap damage dealt ",damage,userCpuLog); //Display damage done
        if(cpuAttack == 6){ //User attack misses from CPU block
        }
        else if(cpuAttack ==5){ //Damage calculator
          cpuStats[6] = cpuStats[6]-(damage)+cpuStats[4];
        }
        else{
          cpuStats[6] = cpuStats[6]-(damage); //Deal damage to CPU
        } 
      }
        userStats [11] = didUserWin(cpuStats);
        if(userStats [11] ==1){ //Breaks if user won
          break;
        }
        else{
          userStats = damageFromCPU(cpuStats,userStats,cpuAttack,userCpuLog); //Take damage from CPU
          userStats [11] = didCPUWin(userStats); //Checks if CPU won
          break; 
        }
      case 4: //Heavy magic
        if (mathGameMenu(2,userCpuLog,userStats,cpuStats) != 0){ //If attack hits
        damage = (userStats[1]*2-cpuStats[3]); //Adjust total damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog = importToScreen(0,"AP damage dealt ",damage,userCpuLog); //Display damage done
        if(cpuAttack == 6){ //User attack misses from CPU block
        }
        else if(cpuAttack ==5){ //Damage calculator
          cpuStats[6] = cpuStats[6]-(damage)+cpuStats[4];
        }
        else{
          cpuStats[6] = cpuStats[6]-(damage); //Deal damage to CPU
        } 
      }
        userStats [11] = didUserWin(cpuStats);
        if(userStats [11] ==1){ //Breaks if user won
          break;
        }
        else{
          userStats = damageFromCPU(cpuStats,userStats,cpuAttack,userCpuLog); //Take damage from CPU
          userStats [11] = didCPUWin(userStats); //Checks if CPU won
          break; 
        }
      case 5: //Heal
        userStats[11] = 2; //Skips turn (does not use up a turn)
        if(userStats[12] <=5 && userStats[12]>0){ //Checks potion count
          if(userStats[5] == userStats[6]){ //Checks for max HP
            userCpuLog = importToScreen(0,"Max HP reached.",0,userCpuLog); //Display max HP already reached
            break;
          }
          else{
            userStats[6] = userStats[6]+userStats[4]; //Heal for PR
            if(userStats[6]>userStats[5]){ //Brings it back to THP if CHP exceeds it
              userStats[6] = userStats[5]; 
            }
            userCpuLog = importToScreen(0,"Healed: ",userStats[4],userCpuLog); //Display player got healed
            userStats[12] = userStats[12]-1; //Takes away a potion
            break;
          }
        }
        else{
          userCpuLog = importToScreen(0,"No pots left.",0,userCpuLog); //Display that there are no potions
          break;
        }
      case 6: //Block
        if(userStats [9] == 0){ //Checks if player blocked already
        userStats [11] = 0;
        userStats[2] = userStats[2]*10;
        userStats[3] = userStats[3]*10;
        userStats [9] = 1;
        userCpuLog = importToScreen(0,"Attack blocked.",0,userCpuLog); //Display that player is blocking
        break;
      }
        else{
          userStats [11] = 2;
          userCpuLog = importToScreen(0,"Block is on CD.",0,userCpuLog); //Display that blocking is on cooldown
          break;
        }
    } 
    return(userStats [11]);
  }//abilityChosen
  
  //Displays the created math question according to ability chosen
  public static int mathGameMenu (int basicOrHeavy,String [] userCpuLog,int[]userStats,int[]cpuStats){
    int hitOrMiss=0; //If the attack hit or not
    int x; //Number a
    int y; //Number b
    int answer;           //Answer from a and b
    String strUserAnswer; //User answer as string
    int userAnswer;       //User answer
    if (basicOrHeavy == 1){ //Basic attacks (addition)
      x = (int)(Math.random()*10+1);
      y = (int)(Math.random()*10+1);
      answer = (x+y);
      userCpuLog = importToScreen(4,"N/A",0,userCpuLog); //Resets imports
      updateCombat(userStats,cpuStats,userCpuLog); //Updates screen
      while(true){
        try{
          f.drawString("What is "+x+" + "+y+"?",746,483); //Displays question
          f.setCursor(26,93);
          strUserAnswer = f.readLine(); 
          userAnswer = Integer.parseInt(strUserAnswer); //Validates input 
          if (answer == userAnswer){ //Attack hits
            hitOrMiss++;
          }
          else{ //Attack misses
            userCpuLog = importToScreen(3,"N/A",0,userCpuLog); //Display user missed
          }
          break;
        }
        catch(NumberFormatException e){
          userCpuLog = importToScreen(2,"N/A",0,userCpuLog); //Invalid input
          updateCombat(userStats,cpuStats,userCpuLog); //Updates screen
        }
      }
    }
    else{ //Heavy attack (multiplication)
      x = (int)(Math.random()*10+1);
      y = (int)(Math.random()*10+1);
      answer = (x*y);
      userCpuLog = importToScreen(4,"N/A",0,userCpuLog); //Resets imports
      updateCombat(userStats,cpuStats,userCpuLog); //Updates screen
      while(true){
        try{
          f.drawString("What is "+x+" x "+y+"?",746,483); //Displays question
          f.setCursor(26,93);
          strUserAnswer = f.readLine();
          userAnswer = Integer.parseInt(strUserAnswer); //Validates input
          if (answer == userAnswer){ //Attack hits
            hitOrMiss++;
          }
          else{ //Attack misses
            userCpuLog = importToScreen(3,"N/A",0,userCpuLog); //Display user missed
          }
          break;
        }
        catch(NumberFormatException e){
          userCpuLog = importToScreen(2,"N/A",0,userCpuLog); //Invalid input
          updateCombat(userStats,cpuStats,userCpuLog); //Updates screen
        }
      }
    }
    return(hitOrMiss); //Return if it hit
  }//mathGameMenu
  
  //Imports bothe user and cpu data on what is happening in the game (damage reports and errors)
  public static String [] importToScreen(int caseType,String textReport,int valueReport,String [] userCpuLog){
    String strValueReport = Integer.toString(valueReport);
    if(caseType == 0){ //User data
      if(valueReport!=0){ //Report type calls for int to be concated
        userCpuLog [0] = textReport.concat(strValueReport); //Concats user report with text to int
      }
      else{
        userCpuLog [0] = textReport; //Text report to game log
      }
      return(userCpuLog); //Return game log
    }
    else if (caseType == 1){ //CPU data
      if(valueReport!=0){ //Report type calls for int to be concated
        userCpuLog [1] = textReport.concat(strValueReport); //Concats CPU report with text to int
      }
      else{
        userCpuLog [1] = textReport; //Text report to game log
      }
      return(userCpuLog); //Return game log
    }
    else if(caseType == 2){ //Display invalid input
      userCpuLog [2] = "Invalid input, please";
      userCpuLog [3] = "try again.";
      return(userCpuLog); //Return game log
    }
    else if(caseType ==3){ //User got question wrong
      userCpuLog[4] = "Incorrect answer.";
      return(userCpuLog); //Return game log
    }
    else{ //Clears game log
      for(int i=0;i<userCpuLog.length;i++){
        userCpuLog[i] = "";
      }
      return(userCpuLog); //Return game log
    }
  }//importToScreen
  
  //The CPUs turn to attack the user takes place here
  public static int [] damageFromCPU(int [] cpuStats,int [] userStats,int cpuAbility,String [] userCpuLog){
    int damage; //Potential damage
    if (cpuStats[9] ==1){ //Resets block defenses after a round
      cpuStats[2] = cpuStats[2]/10; 
      cpuStats[3] = cpuStats[3]/10; 
      cpuStats[9] = 2; //Prevents CPU from blocking again
    }
    switch(cpuAbility){ //Ability chosen
      case 1: //Basic attack
        damage = (cpuStats[0]-userStats[2]); //Calculates damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog =  importToScreen(1,"ad damage taken: ",damage,userCpuLog); //Display damage done
        userStats [6] = userStats[6]-(damage); //User take damage from CPU
        break;
      case 2: //Heavy attack
        damage = (cpuStats[0]*2-userStats[2]); //Calculates damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog =  importToScreen(1,"AD damage taken: ",damage,userCpuLog); //Display damage done
        userStats [6] = userStats[6]-(damage); //User take damage from CPU
        break;
      case 3: //Basic magic
        damage = (cpuStats[1]-userStats[3]); //Calculates damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog = importToScreen(1,"ap damage taken: ",damage,userCpuLog); //Display damage done
        userStats [6] = userStats[6]-(damage); //User take damage from CPU
        break;
      case 4: //Heavy magic
        damage = (cpuStats[1]-userStats[3]); //Calculates damage
        if (damage<0){ //Prevents negative damage
          damage = 0;
        }
        userCpuLog = importToScreen(1,"AP damage taken: ",damage,userCpuLog); //Display damage done
        userStats [6] = userStats[6]-(damage); //User take damage from CPU
        break;
      case 5: 
        cpuStats[6] = cpuStats[6]+cpuStats[4]; //Heal CPU
        userCpuLog = importToScreen(1,"CPU healed.",0,userCpuLog); //Display CPU healed
        break;
      case 6: 
        if(cpuStats[9] == 0){ //Checks if CPU blocked already
        cpuStats[2] = cpuStats[2]*10;
        cpuStats[3] = cpuStats[3]*10;
        cpuStats[9] =1;
        userCpuLog = importToScreen(1,"CPU blocked.",0,userCpuLog); //Display that CPU blocked
      }
        else{
          cpuAbility = (int)(Math.random()*5+1); //Invalid ability means that they try attacking again
        }
    }
    return(userStats); //Return user stats
  }//damageFromCPU
  
  //Checks whether if the CPU won or not
  public static int didCPUWin(int [] userStats){
    int winnerOfRound;
    if(userStats[6]<=0){ //If user is dead then say that CPU won
      winnerOfRound = 3; 
    }
    else{ //Round continues
      winnerOfRound =0; 
    }
    return(winnerOfRound); //Return that CPU won
  }//didCPUWin
  
  //Checks whether if the user won or not
  public static int didUserWin(int [] cpuStats){
    int winnerOfRound;
    if(cpuStats[6]<=0){ //If CPU is dead then say that user won
      winnerOfRound = 1; 
    }
    else{ //Round continues
      winnerOfRound =0; 
    }
    return(winnerOfRound); //Return that user won
  }//didUserWin
  
  //Saves the games progress to the requested name text file
  public static void saveGame (int [] userStats,int [] cpuStats)throws IOException{
    PrintWriter closeAuto = new PrintWriter (new FileWriter ("userAutoSave.txt")); //User auto save stats
    c.drawImage(texturePack[12],1,1,null);
    c.setCursor(6,7);
    String saveName = c.readLine(); //Reads first line of user auto save
    closeAuto.println("2"); //Make auto save invalid
    closeAuto.close();      //Close userAutoSave.txt
    saveName = saveName.replaceAll("\\p{Punct}",""); //Removes punctuation from save name
    String temp = saveName; //Takes save name for CPU save file
    saveName = saveName.concat(".txt"); //Makes save file a .txt file
    PrintWriter output = new PrintWriter (new FileWriter (saveName)); //User created save file
    if(cpuStats[6]<=0){ //If the CPU is dead
      cpuStats[11]=1; 
      output.println("1"); //States that CPU is dead
      for(int i=0;i<userStats.length;i++){ //Output user stats to file
        output.println(userStats[i]); 
      }
    }
    else{ //If the CPU is alive
      String cpuSaveName = temp.concat("Cpu.txt"); //Makes save file for CPU into a .txt file
      PrintWriter cpu = new PrintWriter (new FileWriter (cpuSaveName)); //CPU auto save stats
      output.println("0"); //States that CPU is alive
      for(int i=0;i<userStats.length;i++){ //Output user stats to file
        output.println(userStats[i]); 
      }
      for(int i=0;i<cpuStats.length;i++){ //Output CPU stats to file
        cpu.println(cpuStats[i]); 
      }
      cpu.close(); //Close cpuAutoSave.txt
    }
    output.close(); //Close userAutoSave.txt
    System.exit(0);
  }//saveGame
  
  //Prints in game information into the auto save after each move made
  public static void autoSave (int [] userStats, int [] cpuStats)throws IOException{
    PrintWriter output = new PrintWriter (new FileWriter ("userAutoSave.txt")); //User auto save stats
    if(cpuStats[6]<=0){ //If CPU is dead
      cpuStats[11]=1; //User won
      output.println("1"); //Output to save saying CPU is dead
      for(int i=0;i<userStats.length;i++){ //Copies user game data to auto save
        output.println(userStats[i]); 
      }
    }
    else{ //If CPU alive
      PrintWriter cpu = new PrintWriter (new FileWriter ("cpuAutoSave.txt")); //CPU auto save stats
      output.println("0"); //Output to save sating CPU is alive
      for(int i=0;i<userStats.length;i++){ //Copies user game data to auto save
        output.println(userStats[i]); 
      }
      for(int i=0;i<cpuStats.length-1;i++){ //Copies user game data to auto save
        cpu.println(cpuStats[i]); 
      }
      cpu.println(1); //Tells game that CPU stats are from a save
      cpu.close(); //Close cpuAutoSave.txt
    }
    output.close(); //Close userAutoSave.txt
  }//autoSave
  
  //Opens up the power up menu
  public static void powerUpMenu (int [] userStats){
    Image [] textureCards = new Image [1];
    try{ //Validates if all files exist in the texturePack folder
      textureCards = new Image [] {ImageIO.read(new File("texturePack\\plusADSP.png")),ImageIO.read(new File("texturePack\\plusAPSP.png")),ImageIO.read(new File("texturePack\\plusARSP.png")),ImageIO.read(new File("texturePack\\plusMRSP.png")),ImageIO.read(new File("texturePack\\plusPRSP.png")),ImageIO.read(new File("texturePack\\plusTHPSP.png")),ImageIO.read(new File("texturePack\\healFullSP.png"))};
    }
    catch(IOException e){
      for(int i=0;i<textureCards.length;i++){ //Turns all textures to null if there is a missing file
        texturePack[i] = null;
      }
    }
    int power1 = (int)(Math.random()*7+1); //Random first power up
    int power2 = (int)(Math.random()*7+1); //Random second power up
    while(true){ //Rerolls power up if its the same as the first
      if(power2 == power1){
        power2 = (int)(Math.random()*7+1);
      }
      else{
        break; 
      }
    }
    int power3 = (int)(Math.random()*7+1);
    while(true){ //Rerolls power up if it's the same as the first and second
      if(power3 == power2 ^ power3 == power1){
        power3 = (int)(Math.random()*7+1);
      }
      else{
        break; 
      }
    }
    while(true){
      try{
        c.drawImage(texturePack[22],1,1,null);
        //Display card textures
        c.drawImage(textureCards[(power1-1)],124,130,null);
        c.drawImage(textureCards[(power2-1)],378,130,null);
        c.drawImage(textureCards[(power3-1)],633,130,null);
        c.setCursor(24,49);
        String strChoice = c.readLine();
        int choice = Integer.parseInt(strChoice);
        if(choice>=1 && choice<=3){
          int output;
          if(choice == 1){ //Option 1 from screen
            output = power1;
          }
          else if(choice ==2){ //Option 2 from screen
            output = power2;
          }
          else{ //Option 3 from screen
            output = power3;
          }
          switch(output){ //Adds power up chosen to user stats
            case 1: //Bonus attack damage
              userStats [0] = (userStats[0]+10);
              break;
            case 2: //Bonus attack power
              userStats [1] = (userStats[1]+10);
              break;
            case 3: //Bonus armor
              userStats [2] = (userStats[2]+5);
              break;
            case 4: //Bonus magic resistance 
              userStats [3] = (userStats[3]+5);
              break;
            case 5: //Bonus passive regeneration
              userStats [4] = (userStats[4]+2);
              break;
            case 6: //Bonus total health
              userStats [5] = (userStats[5]+10);
              if(userStats[6]<=(userStats[5]-10)){ //Dosn�t let CHP exceed THp
                userStats[6] = (userStats[6]+10);
              }
              break;
            case 7: //Heal to full
              userStats [6] = userStats[5];
              break;
          }
          c.drawImage(texturePack[19],1,1,null);
        }
        else{
          invalidInput(); //Invalid input
        }
      }
      catch(NumberFormatException e){
        invalidInput(); //Invalid input
      }
      break;
    }
  }//powerUpMenu
  
  //Game over screen that shows once the user is defeated
  public static void gameOverHiScore (int [] userStats,int [] cpuStats, String currentHiScore)throws IOException{
    PrintWriter delAutoSave = new PrintWriter (new FileWriter ("userAutoSave.txt")); //Overwrite auto save data
    delAutoSave.println("2"); //Makes auto save invalid
    delAutoSave.close();      //Closes userAutoSave.txt
    f.close(); //Closes f console
    c.setFont(statFont); //Sets font
    c.setColor(shade3);  //Sets color for font
    c.drawImage(texturePack[23],1,1,null);
    c.setCursor(24,48);
    c.readLine();
    PrintWriter output = new PrintWriter (new FileWriter ("Output.txt")); //Output game data
    String userScore = Integer.toString(userStats[8]); //Converts user highscore to a String
    c.drawImage(texturePack[24],1,1,null);
    String [] strUserStats = new String [userStats.length]; //String version of user stats
    for(int i=0;i<userStats.length;i++){ //Converts user stats to string
      strUserStats [i] = Integer.toString(userStats[i]);
    }
    //Display user stats
    c.drawString(strUserStats[0],415,242);
    c.drawString(strUserStats[1],415,274);
    c.drawString(strUserStats[2],513,242);
    c.drawString(strUserStats[3],513,274);
    c.drawString(strUserStats[4],415,308);
    c.drawString(strUserStats[6],513,308);
    try{
      c.drawString(currentHiScore,525,340); //Displays current highscore
    }
    catch(NullPointerException e){
      c.drawString("N/A",525,340); //Displays N/A if there isn't a high score yet
    }
    c.drawString(userScore,500,374);
    c.setCursor(24,50);
    String userName= c.readLine();
    //Outputs game data to Output.txt
    output.println("-------------------------------------");
    output.println("User Attack Damage:\t\t"+userStats[0]);
    output.println("User Attack Power:\t\t"+userStats[1]);
    output.println("User Armor:\t\t\t"+userStats[2]);
    output.println("User Magic Resistance:\t\t"+userStats[3]);
    output.println("User Passive Regeneration:\t"+userStats[4]);
    output.println("User Total Health:\t\t"+userStats[5]);  
    output.println("-------------------------------------");
    output.println("Username:\t\t"+userName);
    output.println("User High Score:\t"+userStats[8]);
    output.println("-------------------------------------");
    output.close(); //Closes Output.txt
    c.drawImage(texturePack[25],1,1,null);
    c.setCursor(4,32);
    c.readLine();
    newHighScore(userStats,currentHiScore,userName); //Checks to see if user beat high score
  }//gameOverHiScore
  
  //Checks if the user was able to beat the current high score then asks for their username
  public static void newHighScore (int [] userStats, String strHighScore,String name)throws IOException{
    int highScore = 0;
    try{
      highScore = Integer.parseInt(strHighScore); //Validates for int
    }
    catch(NumberFormatException e){
    }
    if (userStats [8] >= highScore){ //Checks if user beat current high score
      PrintWriter outputScore = new PrintWriter (new FileWriter ("HighScore.txt"));
      outputScore.println(userStats [8]); //Output new high score
      outputScore.println(name);          //Output name of high score user
      outputScore.close(); //Closes HighScore.txt
      c.setCursor(4,6);
      c.drawImage(texturePack[26],1,1,null);
      c.readLine();
    }
  }//newHighScore
  
//Multiplayer methods
  //Displays who goes first and whose turn it is in the c console
  public static void turnScreensMulti (int firstMenuCheck,int turnCheck,int attackFirst){
    if(firstMenuCheck ==0){ //Checks if the who goes first has been shown already
      if(attackFirst ==1){ //Player 1 attacks first
        c.drawImage(texturePackMulti[2],1,1,null);
      }
      else{ //Player 2 attacks first
        c.drawImage(texturePackMulti[3],1,1,null);
      }
    }
    else{
      if(turnCheck==1){ //Player 1 turn
        c.drawImage(texturePackMulti[4],1,1,null);
      }
      else{ //Player 2 turn
        c.drawImage(texturePackMulti[5],1,1,null);
      }
    }
  }//turnScreensMulti
  
  //Randomizes the bonus stats to add to their base stats 
  public static int [] statRandomizerMulti (int inputStats []){
    inputStats[0] = inputStats[0]+(int)(Math.random()*15+1); //AD
    inputStats[1] = inputStats[1]+(int)(Math.random()*15+1); //AP
    inputStats[2] = inputStats[2]+(int)(Math.random()*10+1); //AR
    inputStats[3] = inputStats[3]+(int)(Math.random()*10+1); //MR
    inputStats[5] = inputStats[5]+(int)(Math.random()*30+1); //THP
    inputStats[6] = inputStats[5];                           //CHP to THP
    return(inputStats);
  }//statRandomizerMulti
  
  //The games combat code for multiplayer
  public static int combatMulti(int [] user1Stats,int [] user2Stats,int turnCheck,int attackFirst,int firstMenuCheck){
    String [] userCpuLog = new String []{"","","","",""};  //Array of error codes and messages that displays (non damage reports)
    while(true){
      try{
        turnScreensMulti(firstMenuCheck,turnCheck,attackFirst);        //Displays turn on c console
        firstMenuCheck=1;                                              //Verify that the first screen has alrady been shown
        updateScreenMulti(user1Stats,user2Stats,turnCheck,userCpuLog); //Updates the f console on the players stats position of player
        f.setCursor(26,93);
        //Validates that input is an integer
        String strAbility = f.readLine();
        int ability = Integer.parseInt(strAbility);
        if (ability<=6 && ability>=1){
          user1Stats [11] = abilityChosenMulti(user1Stats,user2Stats,turnCheck,ability,userCpuLog); //Does action from chosen ability
          if (user1Stats [11] == 0){ //If no one dies then heal PR HP
            //End of round heal from passive regen
            user1Stats [6] = (user1Stats [6]) + (user1Stats [4]); //Heal PR HP
            if(user1Stats[6]>user1Stats[5]){ //If the users HP exceeds more than their total HP then it drops down to their total
              user1Stats[6] = user1Stats[5]; 
            }
            user2Stats [6] = (user2Stats [6]) + (user2Stats [4]); //Heal PR HP
            if(user2Stats[6]>user2Stats[5]){ //If the users HP exceeds more than their total HP then it drops down to their total
              user2Stats[6] = user2Stats[5]; 
            }
            if(turnCheck ==1){ //Changes turns
              turnCheck=2;
            }
            else{//turnCheck == 2  //Changes turns
              turnCheck=1;
            }
          }
          else if (user1Stats[11]==1){ //Returns if user 1 won
            return(user1Stats [11]); 
          }
          else if (user1Stats[11]==2){ //Skips round (nothing happens)(for error and heal msgs)
          }
          else{//userstats 11 == 3     //Returns if user 2 won
            return(user1Stats [11]);
          }
        }
        else{
          userCpuLog = importToScreen(2,"N/A",0,userCpuLog); //Invalid input message
        }
      }
      catch(NumberFormatException e){
        userCpuLog = importToScreen(2,"N/A",0,userCpuLog);   //Invalid input message
      }
    }
  }//combatMulti
  
  //Does action based off of the ability chosen from user
  public static int abilityChosenMulti(int [] user1Stats,int [] user2Stats,int turnCheck,int ability,String [] userCpuLog){
    int damage; //Damage that could be dealt
    if(turnCheck == 1){ //player 1s turn
      if (user1Stats[9] ==1){ //Resets the players defenses after blocking
        user1Stats[2] = user1Stats[2]/10;
        user1Stats[3] = user1Stats[3]/10; 
        user1Stats[9] = 2;
      }
      switch(ability){
        case 1: //Basic attack
          if (mathGameMenuMulti(1,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){
          damage = (user1Stats[0]-user2Stats[2]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"ad damage dealt: ",damage,userCpuLog); //Display damage done
          user2Stats[6] = user2Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did1WinMulti(user2Stats); //Checks if they killed player 2
          break;
        case 2: //Heavy attack
          if (mathGameMenuMulti(2,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){
          damage = (user1Stats[0]-user2Stats[2]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"AD damage dealt: ",damage,userCpuLog); //Display damage done
          user2Stats[6] = user2Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did1WinMulti(user2Stats); //Checks if they killed player 2
          break;
        case 3: //Basic magic
          if (mathGameMenuMulti(1,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){
          damage = (user1Stats[1]-user2Stats[3]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"ap damage dealt: ",damage,userCpuLog); //Display damage done
          user2Stats[6] = user2Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did1WinMulti(user2Stats); //Checks if they killed player 2
          break;
        case 4: //Heavy magic
          if (mathGameMenuMulti(2,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){
          damage = (user1Stats[1]-user2Stats[3]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"AP damage dealt: ",damage,userCpuLog); //Display damage done
          user2Stats[6] = user2Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did1WinMulti(user2Stats); //Checks if they killed player 2
          break;
        case 5: //Heal
          user1Stats[11] = 2; //Skips turn (does not use up a turn)
          if(user1Stats[12] <=5 && user1Stats[12]>0){ //Checks potion count
            if(user1Stats[5] == user1Stats[6]){ //Checks for max HP
              userCpuLog = importToScreen(0,"Max HP reached.",0,userCpuLog); //Display max HP already reached
              break;
            }
            else{
              user1Stats[6] = user1Stats[6]+user1Stats[4]; //Heal for PR
              if(user1Stats[6]>user1Stats[5]){ //Brings it back to THP if CHP exceeds it
                user1Stats[6] = user1Stats[5]; 
              }
              userCpuLog = importToScreen(0,"Healed: ",user1Stats[4],userCpuLog); //Display player got healed
              user1Stats[12] = user1Stats[12]-1; //Takes away a potion
              break;
            }
          }
          else{
            userCpuLog = importToScreen(0,"No pots left.",0,userCpuLog); //Display that there are no potions
            break;
          }
        case 6: //Shield
          if(user1Stats [9] == 0){ //Checks if player blocked already
          user1Stats [11] = 0;
          user1Stats[2] = user1Stats[2]*10;
          user1Stats[3] = user1Stats[3]*10;
          user1Stats [9] = 1;
          userCpuLog = importToScreen(0,"Blocking attack.",0,userCpuLog); //Display that player is blocking
          break;
        }
          else{
            user1Stats [11] = 2;
            userCpuLog = importToScreen(0,"Block is on CD.",0,userCpuLog); //Display that blocking is on cooldown
            break;
          }
      }
    }
    else{ //Player 2s turn
      if (user2Stats[9] ==1){ //Resets the players defenses after blocking
        user2Stats[2] = user2Stats[2]/10;
        user2Stats[3] = user2Stats[3]/10; 
        user2Stats[9] = 2;
      }
      switch(ability){
        case 1: //Basic attack
          if (mathGameMenuMulti(1,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){ 
          damage = (user2Stats[0]-user1Stats[2]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"ad damage dealt: ",damage,userCpuLog); //Display damage done
          user1Stats[6] = user1Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did2WinMulti(user1Stats); //Checks if they killed player 1
          break;
        case 2:
          if (mathGameMenuMulti(2,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){
          damage = (user2Stats[0]-user1Stats[2]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"AD damage dealt: ",damage,userCpuLog); //Display damage done
          user1Stats[6] = user1Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did2WinMulti(user1Stats); //Checks if they killed player 1
          break;
        case 3:
          if (mathGameMenuMulti(1,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){
          damage = (user2Stats[1]-user1Stats[3]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"ap damage dealt: ",damage,userCpuLog); //Display damage done
          user1Stats[6] = user1Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did2WinMulti(user1Stats); //Checks if they killed player 1
          break;
        case 4:
          if (mathGameMenuMulti(2,userCpuLog,user1Stats,user2Stats,turnCheck) != 0){
          damage = (user2Stats[1]-user1Stats[3]); //Adjust total damage
          if (damage<0){ //Prevents negative damage
            damage = 0;
          }
          userCpuLog = importToScreen(0,"AP damage dealt: ",damage,userCpuLog); //Display damage done
          user1Stats[6] = user1Stats[6]-(damage); //Deal damage
        }
          user1Stats [11] = did2WinMulti(user1Stats); //Checks if they killed player 1
          break;
        case 5:
          user1Stats[11] = 2; //Skips turn (dosnt use up a turn)
          if(user2Stats[12] <=5 && user2Stats[12]>0){
            if(user2Stats[5] == user2Stats[6]){
              userCpuLog = importToScreen(0,"Max HP reached.",0,userCpuLog); //Display max HP already reached
              break;
            }
            else{
              user2Stats[6] = user2Stats[6]+user2Stats[4]; //Heal for PR
              if(user2Stats[6]>user2Stats[5]){ //Brings it back to THP if CHP exceeds it
                user2Stats[6] = user2Stats[5]; 
              }
              userCpuLog = importToScreen(0,"Healed: ",user1Stats[4],userCpuLog); //Display player got healed
              user2Stats[12] = user2Stats[12]-1; //Takes away a potion
              break;
            }
          }
          else{
            userCpuLog = importToScreen(0,"No pots left.",0,userCpuLog); //Display that there are no potions
            break;
          }
        case 6: 
          if(user2Stats [9] == 0){ //Checks if player blocked already
          user2Stats [11] = 0;
          user2Stats[2] = user2Stats[2]*10;
          user2Stats[3] = user2Stats[3]*10;
          user2Stats [9] = 1;
          userCpuLog = importToScreen(0,"Blocking attack.",0,userCpuLog); //Display that player is blocking
          break;
        }
          else{
            user1Stats [11] = 2;
            userCpuLog = importToScreen(0,"Block is on CD.",0,userCpuLog); //Display that blocking is on cooldown
            break;
          }
      }
    }
    return(user1Stats [11]);
  }//abilityChosenMulti
  
  //Math game menu for multiplayer
  public static int mathGameMenuMulti (int basicOrHeavy,String [] userCpuLog,int[]user1Stats,int[]user2Stats,int turnCheck){
    int hitOrMiss=0; //If the attack hit or not
    int x; //Number a
    int y; //Number b
    int answer; //Answer from a and b
    String strUserAnswer; //User answer as string
    int userAnswer; //User answer
    if (basicOrHeavy == 1){ //Basic attacks (addition)
      x = (int)(Math.random()*10+1);
      y = (int)(Math.random()*10+1);
      answer = (x+y);
      userCpuLog = importToScreen(4,"N/A",0,userCpuLog); //Resets imports
      updateScreenMulti(user1Stats,user2Stats,turnCheck,userCpuLog); //Updates screen
      while(true){
        try{
          f.drawString("What is "+x+" + "+y+"?",746,483); //Displays question
          f.setCursor(26,93);
          strUserAnswer = f.readLine(); //Validates input
          userAnswer = Integer.parseInt(strUserAnswer);
          if (answer == userAnswer){ //Attack hits
            hitOrMiss++;
          }
          else{ //Attack misses
            userCpuLog = importToScreen(3,"N/A",0,userCpuLog); 
          }
          break;
        }
        catch(NumberFormatException e){
          userCpuLog = importToScreen(2,"N/A",0,userCpuLog); //Invalid input
          updateScreenMulti(user1Stats,user2Stats,turnCheck,userCpuLog); //Updates screen
        }
      }
    }
    else{ //Heavy attack (multiplication)
      x = (int)(Math.random()*10+1);
      y = (int)(Math.random()*10+1);
      answer = (x*y);
      userCpuLog = importToScreen(4,"N/A",0,userCpuLog); //Resets imports
      updateScreenMulti(user1Stats,user2Stats,turnCheck,userCpuLog); //Updates screen
      while(true){
        try{
          f.drawString("What is "+x+" x "+y+"?",746,483); //Displays question
          f.setCursor(26,93);
          strUserAnswer = f.readLine(); //Validates input
          userAnswer = Integer.parseInt(strUserAnswer);
          if (answer == userAnswer){ //Attack hits
            hitOrMiss++;
          }
          else{ //Attack misses
            userCpuLog = importToScreen(3,"N/A",0,userCpuLog); 
          }
          break;
        }
        catch(NumberFormatException e){
          userCpuLog = importToScreen(2,"N/A",0,userCpuLog); //Invalid input
          updateScreenMulti(user1Stats,user2Stats,turnCheck,userCpuLog); //Updates screen
        }
      }
    }
    return(hitOrMiss); //Return if it hit
  }//mathGameMenuMulti
  
  //Checks if player 1 won
  public static int did1WinMulti(int [] user2Stats){
    int winnerOfRound;
    if(user2Stats[6]<=0){
      winnerOfRound = 3; //plater 2 dead
    }
    else{
      winnerOfRound =0; //player 2 not dead
    }
    return(winnerOfRound); //Returns winner
  }//did1WinMulti
  
  //Checks if player 2 won
  public static int did2WinMulti(int [] user1Stats){
    int winnerOfRound;
    if(user1Stats[6]<=0){
      winnerOfRound = 1; //plater 1 dead
    }
    else{
      winnerOfRound =0; //player 1 not dead
    }
    return(winnerOfRound); //Returns winner
  }//did2WinMulti
  
  //Updates the multiplayer screen for the users stats 
  public static void updateScreenMulti(int [] user1Stats,int [] user2Stats,int turnCheck,String [] userCpuLog){
    String [] strUser1Stats = new String [user1Stats.length]; //Turns player 1s stats to a string
    String [] strUser2Stats = new String [user2Stats.length]; //Turns player 2s stats to a string
    f.setFont(statFont); //Set font of game
    f.setColor(shade3); //Set color for font
    for(int i=0;i<user1Stats.length;i++){ //Turns stats to string
      strUser1Stats[i] = Integer.toString(user1Stats[i]);
    }
    for(int i=0;i<user2Stats.length;i++){ //Turns stats to string
      strUser2Stats[i] = Integer.toString(user2Stats[i]);
    }
    if (turnCheck ==1 ){ //User 1 stats display for their turn
      f.drawImage(texturePackMulti[0],1,1,null);
      f.drawString("Player 1's turn.",742,450);
      //User 1 stats
      f.drawString(strUser1Stats[0],38,450);
      f.drawString(strUser1Stats[1],38,482);
      f.drawString(strUser1Stats[2],135,450);
      f.drawString(strUser1Stats[3],135,482);
      f.drawString(strUser1Stats[4],38,516);
      f.drawString(strUser1Stats[6],135,516);
      f.drawString(strUser1Stats[12]+"x",540,516);
      //User 2 stats
      f.drawString(strUser2Stats[0],793,28);
      f.drawString(strUser2Stats[1],793,60);
      f.drawString(strUser2Stats[2],890,28);
      f.drawString(strUser2Stats[3],890,60);
      f.drawString(strUser2Stats[4],793,94);
      f.drawString(strUser2Stats[6],890,94);
      f.drawString(strUser2Stats[12]+"x",711,94);
      //Game log
      f.drawString(userCpuLog[0],213,458);
      f.drawString(userCpuLog[1],213,508);
      f.drawString(userCpuLog[2],213,450);
      f.drawString(userCpuLog[3],213,465);
      f.drawString(userCpuLog[4],213,458);
      userCpuLog = importToScreen(4,"N/A",0,userCpuLog); //Resets game log
    }
    else{ //User 2 stats display for their turn
      f.drawImage(texturePackMulti[1],1,1,null);
      f.drawString("Player 2's turn.",742,450);
      //User 1 stats
      f.drawString(strUser2Stats[0],38,450);
      f.drawString(strUser2Stats[1],38,482);
      f.drawString(strUser2Stats[2],135,450);
      f.drawString(strUser2Stats[3],135,482);
      f.drawString(strUser2Stats[4],38,516);
      f.drawString(strUser2Stats[6],135,516);
      f.drawString(strUser2Stats[12]+"x",540,516);
      //User 2 stats
      f.drawString(strUser1Stats[0],793,28);
      f.drawString(strUser1Stats[1],793,60);
      f.drawString(strUser1Stats[2],890,28);
      f.drawString(strUser1Stats[3],890,60);
      f.drawString(strUser1Stats[4],793,94);
      f.drawString(strUser1Stats[6],890,94);
      f.drawString(strUser1Stats[12]+"x",711,94);
      //Game log
      f.drawString(userCpuLog[0],213,458);
      f.drawString(userCpuLog[1],213,508);
      f.drawString(userCpuLog[2],213,450);
      f.drawString(userCpuLog[3],213,465);
      f.drawString(userCpuLog[4],213,458);
      userCpuLog = importToScreen(4,"N/A",0,userCpuLog); //Resets game log
    }
  }//updateScreenMulti
  
  //Post game screen for multiplayer
  public static void postGameMulti(int [] user1Stats,int [] user2Stats,int checkWhoWon){
    f.close(); //Closes console
    if(checkWhoWon == 1){ //Displays that user 2 won
      c.drawImage(texturePackMulti[7],1,1,null);
    }
    else{ //Displays that user 1 won
      c.drawImage(texturePackMulti[6],1,1,null);
    }
    c.setCursor(4,32);
    c.readLine();
  }//postGameMulti
  
//Other Methods
  //Creates a message at the bottom of the screen saying that there was an invalid input
  public static void invalidInput(){
    c.drawImage(texturePack[1],35,475,null);
  }//invalidInput
}//Class