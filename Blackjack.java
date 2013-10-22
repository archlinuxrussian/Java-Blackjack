/**
    Midterm v0.4
*/

import java.util.*;
import java.io.*;

public class Blackjack
{
    public static void main(String[] args)
    {
        System.out.println("Welcome to blackjack!");   //Greeter! :D
        String userWin;            //keeps track of who won
        String playAgain = "y";    //keeps the game running
        Scanner userInput = new Scanner(System.in);

        while(playAgain.equalsIgnoreCase("y"))   //as long as the user wants the game to run
        {
            userWin = playGame();    //Plays the game!
            if(userWin.equals("true"))    //Displays message if the user won
                System.out.println("You won! Do you want to play again? [y/N]");
            else if(userWin.equals("tie"))   //Displays if tie game
                System.out.println("It's a tie! Want to try your luck again? [y/N]");            
            else   //Displays if else
                System.out.println("Sorry, you lost. Wanna play again? [y/N]");
            playAgain = userInput.nextLine();
        }
        System.exit(0);
    }

/**
    This method is for handling the game's mechanics.
    It'll handle both hands and have an array of up to 20 cards per.
    It will also check to see if either user went bust. For the sake of simplicity, if the computer goes bust (low chance),
        it will just report that and the game can be reset.
    @param userWin, returns the status of the game to main method
*/

    public static String playGame()
    {
        int[] computerCards = new int[20];//declare "hand" for computer

        int[] playerCards = new int[20];//Declare the player's hand

        
        boolean playerBust = false, compBust = false;
        String userWin = "false";

        computerCards = genInitCards();             //generate two cards for computer
        computerCards = computerAI(computerCards);  //This hands off the hand to the AI to manipulate
        if(cardSum(computerCards) > 21)             //Checks to see if the computer went bust.
            compBust = true;
        if(compBust == true)                        //If the comp DID bust, then display and done.
        {
            System.out.println("The computer went bust, you win by default!");
            userWin = "true";    //This will be returned
        }
        if(compBust == false)                       //If the comp did not bust, continue
        {
            playerCards = genInitCards();        //Gets the first two cards for the player
            playerCards = playerInterface(playerCards);  //Hands the hand off to the player interface.
            if(cardSum(playerCards) > 21)
                playerBust = true;              //Did the player bust?
            if(playerBust == true)
                System.out.println("Bust.");      //If he/she did, then tell them :O! This programme isn't cruel.
            else if(cardSum(playerCards) > cardSum(computerCards))  //Checks winner
                userWin = "true";
            else if(cardSum(playerCards) == cardSum(computerCards))  //Checks for a tie!
                userWin = "tie";
        }
        System.out.println("The computer's cards were: ");   
        displayCardFace(computerCards);             //Tells the user what the computer's cards were
        System.out.println("Your cards were: ");
        displayCardFace(playerCards);               //Restates what the user's cards were
        
        return userWin;          //Hands the results back to main method!
    }        


    public static int genCard()     //Very simple method to generate a new card! :D
    {
        Random genNewCard = new Random();
        int newCard = genNewCard.nextInt(13) +1;  //Starts cards from element 1, reserving 0 for special aces!
        return newCard;
    }

    public static int[] genInitCards()//Generate two cards
    {
        int[] cards = new int[20];     //Generate array
        cards[1] = genCard();           //Gen card 1
        cards[2] = genCard();           //Gen card 2
                                        //System.out.println(cards[1] + " " + cards[2]);   Was used for debugging purposes! 
        return cards;                  //Pass array
    }

/**
    This method interfaces with the user. It handles all input from the user and displays for him/her.
    @param cards = playerCards in main method, hands them back
*/
    public static int[] playerInterface(int[] cards) 
    {
        Scanner input = new Scanner(System.in);
        String hit = "";
        int i = 3;
        boolean bust = false; //Initialising the variable =3

        System.out.println("Your first two cards are: ");      
        displayCardFace(cards);                       //Shows the first two cards given to the player.
        System.out.println("Do you want another card? [y/N]");
        hit = input.nextLine();            //Asks and gets input for if the user wants another card.
        if(hit.equalsIgnoreCase("y"))      //Whole loop depends on them wanting another card!
        {
            while(hit.equalsIgnoreCase("y") && bust==false)  //feedback loop inside depends on the user not going bust and wanting new card.
            {
                cards[i] = genCard();                             //Generates a new card.
                System.out.println("Your new card is "+cards[i]+".");       //Displays said new card
                if(cardSum(cards) > 21)                          //Checks to see if user went bust.
                    bust = true;
                if(bust == false)                               //As long as user did not go bust, can continue.
                {
                    System.out.println("\nDo you want another? [y/N]");
                    hit = input.nextLine();                                //Gathers input again.
                    i++;
                }
            }
        }
        if(bust==false)  //If the user did NOT go bust, user can change ace from 1 to 11.
        {
            for(int a = 1; a < cards.length; a++)     //Checks all cards for if there is an Ace
            {
            if(cards[a] == 1)                  //Is this card an ace?
                cards = playerAce(cards, a);   //If so, hand off to method to get user advice
            }
        }
        return cards;    //Return the hand to its rightful owner!
    }

/**
    This method is used if there is an ace. It asks the user if they want to convert
        the ace to 11 or keep 1. It operates based on a simple yes or no structure.
*/
    public static int[] playerAce(int[] card, int cardNum)     
    {
        Scanner input = new Scanner(System.in);
        String change = " ";
        System.out.println("Card "+cardNum+" is an Ace, do you want it to be changed to 11? [y/N]");  //Tells user there is an Ace
        change = input.nextLine();           //gets input if they want to change the ace
        if(change.equalsIgnoreCase("y"))     //if input is positive
        {
            card[cardNum] = 0;               //change the card in question to ZERO!
            card[0] = 11;                    //Change reserved Ace spot to ELEVEN!
        }
        return card;                        //returns the hand to the user.
    }

/**
    This is the computerAI. It doesn't have much intelligence, though. 
    This method accepts the computer's hand and manipulates it based on a few rules. 
    Basically it handles changing an Ace from 1 to 11 and adding additional cards. 
    It eventually hands back the hand to the playGame method.
*/
    public static int[] computerAI(int[] cards)
    {
        if(cardSum(cards) < 11 && cards[1] == 1) //If the computer has an ace and the card sum is less than 12, then ace is 11
        {
            cards[0] = 11;
            cards[1] = 0; //This sets Ace's value aside!
        }
        else if(cardSum(cards) < 11 && cards[2] == 1) //Same, though compares to card 2
        {        
            cards[2] = 0;
            cards[0] = 11;//Same. These will be read back as an "Ace" later on.
        }
            
        while(cardSum(cards) < 15) //While the sum of the cards are less than a threshold add another card
        {
            int i = 3;             //Selects which card/element this is going into         
            cards[i] = genCard();  //gets a card
            i++;   //incriments
        }
        return cards;//Returns correct values of the card
    }

    public static int cardSum(int[] cards)    //This method adds up the cards!
    {
        int sum=0;    //Sum initialised
        for(int i = 1; i < cards.length; i++)   //loops through all cards/elements
        {
            if(cards[i] > 10)              //If the card is greater than 10 (is jack, queen, or king)
            {
                sum += 10;                 //Adds 10 instead, the actual value.
            }
            else
                sum += cards[i];           //if not, just add the element value
        }
        sum += cards[0];               //For ace, if ace is 11 and in element[0], then add in that element (11)
        return sum;                         //Separate because it may hold an 11 as an Ace
    }
        
    public static void displayCardFace(int[] card)            //This displays the card's "face", but really displays all cards.
    {
        String name;            //Displays the card via a string variable.
        for(int i = 0; i < card.length; i++)        //Loops through all cards
        {
            if(card[i] < 11 && card[i] > 1)          //if the card is less than 11 (jack) and more than 1 (ace)
                name = Integer.toString(card[i]);    //then translate directly.
            else if(card[i] == 11 && card[1] != 0) //This eliminates the possibility of the ace...may be redundant?
                name = "Jack";                      //Translate to Jack if card is 11
            else if(card[i] == 12)
                name = "Queen";                 //Translate to queen
            else if(card[i] == 13)
                name = "King";                  //etc
            else if(card[i] == 1) 
                name = "Ace";             //If the card is an ace, will display as such...
            else
                name = " ";              //if not, just put in a space for the name
            if(i == 0 && card[0] == 11) //If the counter is at 0 and 
                name = "Ace";    
            if(card[i] > 0)                  //If the element itself is not 0, it also has a card value
                System.out.print(name+"  ");    //Display that "face" value
            if(i == card.length-1)           //If the deck is done, add up the cards and display the sum for those who want it :) 
                System.out.println("= "+cardSum(card));   //Isn't this programme so kind? :P
        }
    }
}
