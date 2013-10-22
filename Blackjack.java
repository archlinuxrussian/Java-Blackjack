/**
    Midterm v0.4
*/

import java.util.*;
import java.io.*;

public class Blackjack
{
    public static void main(String[] args)
    {
        System.out.println("Welcome to blackjack!");
        String userWin;
        String playAgain = "y";
        Scanner userInput = new Scanner(System.in);

        while(playAgain.equalsIgnoreCase("y"))
        {
            userWin = playGame();
            if(userWin.equals("true"))
                System.out.println("You won! Do you want to play again? [y/N]");
            else if(userWin.equals("tie"))
                System.out.println("It's a tie! Want to try your luck again? [y/N]");            
            else
                System.out.println("Sorry, you lost. Wanna play again? [y/N]");
            playAgain = userInput.nextLine();
        }
        System.exit(0);
    }



    public static String playGame()
    {
        int[] computerCards = new int[20];//declare "hand" for computer

        int[] playerCards = new int[20];//Declare the player's hand

        
        boolean playerBust = false, compBust = false;
        String userWin = "false";

        computerCards = genInitCards();//generate two cards for computer
//Note to self: restrict conversion of cards to the sum function!!!! Also, if player or computer wants Ace to be 11, then set ace to element 0.
        computerCards = computerAI(computerCards);
        if(cardSum(computerCards) > 21)
            compBust = true;
        if(compBust == true)
        {
            System.out.println("The computer went bust, you win by default!");
            userWin = "true";
        }
        if(compBust == false)
        {
            playerCards = genInitCards();
            playerCards = playerInterface(playerCards);
            if(cardSum(playerCards) > 21)
                playerBust = true;
            if(playerBust == true)
                System.out.println("Bust.");    
            else if(cardSum(playerCards) > cardSum(computerCards))
                userWin = "true";
            else if(cardSum(playerCards) == cardSum(computerCards))
                userWin = "tie";
        }
        System.out.println("The computer's cards were: ");
        displayCardFace(computerCards);
        System.out.println("Your cards were: ");
        displayCardFace(playerCards);
        

        return userWin;
    }        


    public static int genCard()
    {
        Random genNewCard = new Random();
        int newCard = genNewCard.nextInt(13) +1;
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
            for(int a = 1; a < cards.length; a++)
            {
            if(cards[a] == 1)
                cards = playerAce(cards, a);
            }
        }
        return cards;
    }

    public static int[] playerAce(int[] card, int cardNum)
    {
        Scanner input = new Scanner(System.in);
        String change = " ";
        System.out.println("Card "+cardNum+" is an Ace, do you want it to be changed to 11? [y/N]");
        change = input.nextLine();
        if(change.equalsIgnoreCase("y"))
        {
            card[cardNum] = 0;
            card[0] = 11;
        }
        return card;
    }



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
            int i = 3;            
            cards[i] = genCard(); 
            i++;
        }
        return cards;//Returns correct values of the card
    }

    public static int cardSum(int[] cards)
    {
        int sum=0;
        for(int i = 1; i < cards.length; i++)
        {
            if(cards[i] > 10)
            {
                sum += 10;
            }
            else
                sum += cards[i];
        }
        sum += cards[0];
        return sum; //Separate because it may hold an 11 as an Ace
    }
        

    public static void displayCardFace(int[] card)
    {
        String name;        
        for(int i = 0; i < card.length; i++)        
        {
            if(card[i] < 11 && card[i] > 1)
                name = Integer.toString(card[i]);
            else if(card[i] == 11 && card[1] != 0) //This eliminates the possibility of the ace...may be redundant?
                name = "Jack";
            else if(card[i] == 12)
                name = "Queen";
            else if(card[i] == 13)
                name = "King";
            else if(card[i] == 1) 
                name = "Ace";    
            else
                name = " ";
            if(i == 0 && card[0] == 11) //If the counter is at 0 and 
                name = "Ace";    
            if(card[i] > 0)
                System.out.print(name+"  "); 
            if(i == card.length-1)
                System.out.println("= "+cardSum(card));
        }
    }
}
