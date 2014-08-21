/*
Converts Roman letters to Tengwar in English mode. Result is returned in the format used by the Tengwar Annatar font

Two input methods:
1) Input file from command line arguements: the file is read in, converted and then printed out to a new file
2) Input is from console

Requires Letter.java class file

Testing: 
in aenchent tiems the rings of power wer crafted bie the elven-smiths, and sawron, the dark lord, forjed the wun ring, filling it with hiz oewn power so that he culd rul all others!
*/

import java.util.Scanner;
import java.io.*;
import java.util.*;

public class TengwarConvert {

   //Global variables
   //---------------------------------------------------------------------------
   public static char[] charArray;
   public static LinkedList<Letter> letterList = new LinkedList<Letter>();
   public static LinkedList<Character> soundList = new LinkedList<Character>();


   /*Main
   Determine which method of input is being given and call the corresponding method
   ---------------------------------------------------------------------------*/
   public static void main (String[] args) {
   
      if (args.length == 1) {
         //INPUT : FILE.txt
         fileInput(args);
      }
      else {
         //INPUT : CONSOLE
         consoleInput();
      }
   }
   
   /* fileInput
   Reads in the given file, and converts each line of the input in turn. Output is printed to a new file
   ---------------------------------------------------------------------------*/
   public static void fileInput (String[] args) {
      try {
      //Setup file input and output file
         String inputFileName = args[0];
         File inputFile = new File(inputFileName);
         Scanner in = new Scanner(inputFile);
      
         PrintWriter writer = new PrintWriter(inputFileName.substring(0, args[0].length() -4) + "_converted.txt");
         System.out.println("Converting...");
      
         //Get input from file
         while (in.hasNext() ) {
            String input = in.nextLine();
         
            if (input.length() != 0) {
            
               //Move each word, defined by being seperated by a space, into an array of Strings called wordsArray[]
               String[] wordsArray = input.split(" ");
               String outputString = "";
            
               //Run operations on each word in wordsArray[]
               for (String word : wordsArray) {
               
                  //Check for empty words
                  if (word.length() != 0) {      
                  
                     //Move each character in the word into an array of chars called charArray[]
                     charArray = new char[word.length()];
                  
                     //Make all letters in the array lowercase
                     for (int i = 0; i < word.length(); i++) {
                        charArray[i] = Character.toLowerCase(word.charAt(i) );
                     }
                  
                     //Run conversions!
                     
                     //charArray to letterList
                     simplify();
                     //letterList to letterList
                     sortVowels();      
                     //letterList to soundList
                     findSounds();
                  
                     //Build the converted word
                     String newWord = "";
                     for (int i = 0; i < soundList.size(); i++) {
                        newWord += Character.toString( soundList.get(i) );
                     }
                  
                     //Add the converted word and a space to the outputString
                     outputString+= newWord;
                     outputString+= " ";
                  
                     //Reset the lists
                     letterList.clear();
                     soundList.clear();
                  }
               }
            
               //Print the finished outputString to the output file
               writer.println(outputString);
            }
         }
      
         writer.close();
      } 
      catch (FileNotFoundException e) {
         System.out.println("File not found");
         System.exit(1);
      }
   }

   //consoleInput
   //---------------------------------------------------------------------------
   public static void consoleInput () {
   
      System.out.println("Ready");   
      Scanner console = new Scanner(System.in);
      String input = console.nextLine();
      System.out.println("Converting...");
   
      //Check for empty input
      if (input.length() != 0) {
      
         //Move each word seperated by a space into an array of strings, wordsArray
         String[] wordsArray = input.split(" ");
         String outputString = "";
      
         //Run operations on each word in array of strings
         for (String word : wordsArray) {      
         
            //Move each character in the input into an array and make them lowercase
            charArray = new char[word.length()];
            for (int i = 0; i < word.length(); i++) {
               charArray[i] = Character.toLowerCase(word.charAt(i) );
            }
         
            //Run conversion operations
            simplify();
            /*
            String testy = "";
            for (int i = 0; i < letterList.size(); i++) {
               testy += letterList.get(i).getChar();
            }
            System.out.println(testy);
            */
            sortVowels();
            /* 
            testy = "";
            for (int i = 0; i < letterList.size(); i++) {
               testy += letterList.get(i).getChar();
            }
            System.out.println(testy);
             */
            findSounds();
         
            //Build the converted word
            String newWord = "";
         
            for (int i = 0; i < soundList.size(); i++) {
               newWord += Character.toString( soundList.get(i) );
            }
         
            //Add the converted word and a space to outputString 
            outputString+= newWord;
            outputString+= " ";
         
            //Reset the lists
            letterList.clear();
            soundList.clear();
         }
      
         //Print the finished output string to the console
         System.out.println(outputString);
      }
   }


   //simplify
   //---------------------------------------------------------------------------
   // Searches through the array of characters from input and converts them into
   // simplified form: long vowels (ae, ee, ie, oe, ue) are capitalized and double letters are 
   // capitalized based on their first letter (th, sh, ch, wh, ld, ng)
   public static void simplify() {
   
      //Fill letterList with all the characters
      for (int i = 0; i < charArray.length; i++) {
         Letter newChar = new Letter(charArray[i]);
         letterList.add(newChar);
      }
      //Go through the new letterList and check for words (of, the, and)
      if (letterList.size() == 2) {
         char cur = letterList.get(0).getChar();
         char nex = letterList.get(1).getChar();
      
         //of
         if (cur == 'o' && nex == 'f') {
            Letter newOf = new Letter('J');
            letterList.clear();
            letterList.add(newOf);
         }   
      }
      if (letterList.size() == 3) {
         char cur = letterList.get(0).getChar();
         char nex = letterList.get(1).getChar();
         char fol = letterList.get(2).getChar();
      
         //the
         if (cur == 't' && nex == 'h' && fol == 'e') {
            Letter newThe = new Letter('G');
            letterList.clear();
            letterList.add(newThe);
         } //and
         else if (cur == 'a' && nex == 'n' && fol == 'd') {
            Letter newAnd = new Letter('K');
            letterList.clear();
            letterList.add(newAnd);
         }
      }
   
      //Make changes      
      for (int i = 0; i < letterList.size()-1; i++) {
         char cur = letterList.get(i).getChar();
         char nex = letterList.get(i+1).getChar();
      
         //Repeated consonet case. These add short or long bars indicating a repeated consonet sound
         if (  cur == nex ) {
         
            //short width consonet, for single width consonents. Set second repeated consonet Q
            if (cur == 'c' || cur == 'f' || cur == 'h' || cur == 'k' || cur == 'p' || cur == 'r' || cur == 's' || cur == 't' || cur == 'w' || cur == 'l' ) {
               Letter newShortDouble = new Letter('Q');
               letterList.set(i+1, newShortDouble);
            }
            //long width consonet, for double width consonets. Set second repeated consonet R
            if (cur == 'b' || cur == 'd' || cur == 'g' || cur == 'j' || cur == 'm' || cur == 'n' || cur == 'v' ) {
               Letter newLongDouble = new Letter('R');
               letterList.set(i+1, newLongDouble);
            }
         }
      
         //special double letter case  ( sh, ch, wh, ld), remove following letter and capitalize the first letter
         if ( ( ( cur == 's' || cur == 'c' || cur == 'w') && nex == 'h') || (cur == 'l' && nex == 'd') || (cur == 'n' && nex == 'g') ) {
            Letter newDouble = new Letter(Character.toUpperCase(cur) );
            letterList.set(i, newDouble );
            letterList.remove(i+1);
         }
          //th case. Change t to H, remove h
         else if (  cur == 't' && nex == 'h') {
            Letter newDouble = new Letter('H' );
            letterList.set(i, newDouble );
            letterList.remove(i+1);
         }
          //n or m followed by t, d or b, p. Uppercase the following letter
         else if ( ( cur == 'n'  && ( nex == 't' || nex == 'd') ) || ( cur == 'm' &&  ( nex == 'b' || nex == 'p' ) ) ) {
            Letter newSpecialN = new Letter(Character.toUpperCase(nex) );
            letterList.set(i, newSpecialN);
            letterList.remove(i+1);  
         }   
          //long vowel case. Any vowel followed by an e. Uppercase vowel, remove following e
         else if ( (cur == 'a' || cur == 'e' || cur == 'i' || cur == 'o' || cur == 'u') && nex == 'e') {
            Letter newLong = new Letter(Character.toUpperCase(cur) );
            letterList.set(i, newLong );
            letterList.remove(i+1);
         }
      }
   
      //s followed by a vowel case, change to downward pointing S
      for (int i = 0; i < letterList.size()-1; i++) {
         char cur = letterList.get(i).getChar();
         char nex = letterList.get(i+1).getChar();
      
         if ( cur == 's' && (nex == 'a' || nex == 'e' || nex == 'i' || nex == 'o' || nex == 'u') ) {
            Letter newS = new Letter((char)27 );
            letterList.set(i, newS);
         }  
      }
   }


   //sortVowels
   //---------------------------------------------------------------------------
   //Searches through the array of characters and moves vowels one character to the right
   public static void sortVowels() {
   
      //Last sound is s case and preceeding character is not a vowel
      //Replace last s with special s character
      if ( letterList.size() > 2  && letterList.get(letterList.size()-1 ).getChar() == 's') {
         char secondToLast = letterList.get(letterList.size()-2).getChar();
         boolean foundVowel = false;
         char[] vowels = {'A', 'a', 'E', 'e', 'I', 'i', 'O', 'o', 'U', 'u'};
         
         //Compare secondToLast against all vowels
         for (char vowel : vowels) {
            if (secondToLast == vowel) {
               foundVowel = true;
             }
         }
      
         if (foundVowel == false ) {
            Letter newLastS = new Letter('X');
            letterList.set(letterList.size()-1, newLastS);
         }
      }
   
      for (int i = 0; i < letterList.size()-1; i++) {
         char cur = letterList.get(i).getChar();
         char nex = letterList.get(i+1).getChar();
      
         Letter curL = letterList.get(i);
         Letter nexL = letterList.get(i+1);
      
         //Move short vowel case. Swap nexL and curL in letterList
         if ( (cur == 'a' || cur == 'e' || cur == 'i' || cur == 'o' || cur == 'u') &&(curL.getState() == false) ) {
            letterList.set(i, nexL);
            letterList.set(i+1, curL);
            curL.moved();
         }
         ///Move long vowel case. Swap nexL and curL in letterList
         if ( (cur == 'A' || cur == 'E' || cur == 'I' || cur == 'O' || cur == 'U') && (curL.getState() == false) ) {
            letterList.set(i, nexL);
            letterList.set(i+1, curL);
            curL.moved();
         }
      }
   
   }

   //findSounds
   //---------------------------------------------------------------------------
   //Goes through the converted letterList and builds the soundList
   public static void findSounds() {
   
      for (int i = 0; i < letterList.size(); i++) {
         char cur = letterList.get(i).getChar();
      
         //Special words (the, and, of)
         //--------------------------------------------------------------
            //the
         if ( cur == 'G' ) {
            soundList.add('@');
         }
          //of
         else if ( cur == 'J') {
            soundList.add('@');
            soundList.add(':');
         }
          //and
         else if ( cur == 'K' ) {
            soundList.add('@');
            soundList.add('P');
         }
         
          //Non-Character Symbols (. , ! ? ( ) )
          //--------------------------------------------------------------
          //period
         else if ( cur == '.' ){
            soundList.add('-');
         }
          //comma
         else if ( cur == ',' ) {
            soundList.add('=');
         }
          //exclamation
         else if ( cur == '!' ) {
            soundList.add((char)193);
         }
          //question
         else if ( cur == '?' ) {
            soundList.add((char)192);
         }
          //open parenthesis
         else if ( cur == '(' ) {
            soundList.add((char)140);
         }
          //close parenthesis
         else if ( cur == ')' ) {
            soundList.add((char)156);
         }
          //dash
         else if ( cur == '-') {
            soundList.add((char)194);
         }
         
          //Repeated Letter Modifiers
          //short width bar
         else if ( cur == 'R' ) {
            soundList.add(':');
         }
          //long width bar
         else if ( cur == 'Q' ) {
            soundList.add(';');
         }
         
          //Multi letters (th, sh, ch, wh, ld, ng, nt, nd, mp, mb, and following s)
          //--------------------------------------------------------------
         
          //th
         else if ( cur == 'H' ) {
            soundList.add((char)51);
         }
          //sh
         else if ( cur == 'S' ) {
            soundList.add((char)100);
         }
          //ch
         else if ( cur == 'C') {
            soundList.add((char)65);
         }
          //wh
         else  if ( cur == 'W') {
            soundList.add((char)111);
         }
          //ld
         else if ( cur == 'L') {
            soundList.add((char)109);
         }
          //ng
         else if ( cur == 'N') {
            soundList.add((char)98 );
         }
          //nt
         else if ( cur == 'T' ) {
            soundList.add((char)49);
            soundList.add('p');
         }
          //nd
         else if ( cur == 'D' ) {
            soundList.add((char)50);
            soundList.add('P');
         }
          //mp
         else if ( cur == 'P' ) {
            soundList.add((char)113);
            soundList.add('p');
         }
          //mb
         else if (cur == 'B' ) {
            soundList.add((char) 119);
            soundList.add('P');
         }
          //last s 
         else if (cur == 'X') {
            soundList.add('_');
         }
         
          //Consonents
          //--------------------------------------------------------------
         else if ( cur == 'b' ) {
            soundList.add((char)119);
         }
         else if ( cur == 'c') {
            soundList.add((char)122);
         }
         else if ( cur == 'd') {
            soundList.add( (char)50);
         }
         else if ( cur == 'f') {
            soundList.add((char)101);
         }
         else if ( cur == 'g') {
            soundList.add((char)120);
         }
         else if ( cur == 'h') {
            soundList.add((char)57);
         }
         else if ( cur == 'j') {
            soundList.add((char)115);
         }
         else if ( cur == 'k') {
            soundList.add((char)122);
         }
         else if ( cur == 'l') {
            soundList.add((char)106);
         }
         else if ( cur == 'm') {
            soundList.add((char)116);
         }
         else if ( cur == 'n') {
            soundList.add((char)53);
         }
         else if ( cur == 'p') {
            soundList.add((char)113);
         }
         else if ( cur == 'q') {
            soundList.add((char)122);
            soundList.add(i+1, (char)121);
         }
         else if ( cur == 'r') {
            soundList.add((char)54);
         }
         else if ( cur == 't') {
            soundList.add((char)49);
         }
          //S pointed up
         else if ( cur == 's') {
            soundList.add((char)105);
         }
          //S pointed down
         else if ( cur == (char)27) {
            soundList.add((char)56);
         }
         else if ( cur == 'v') {
            soundList.add((char)114);
         }
         else if ( cur == 'w') {
            soundList.add((char)121);
         }
         else if ( cur == 'x') {
            soundList.add((char)122);
            soundList.add((char)96);
         }
         else if ( cur == 'y') {
            soundList.add((char)104);
         }
         else if ( cur == 'z') {
            soundList.add((char)44);
         }
         
          //Vowels
          //--------------------------------------------------------------
         
          //A
          //Short
         else if ( cur == 'a') {
            Letter curL = new Letter(cur);
         
               //Check if single or last character, but not because it was moved there
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur)  && (letterList.peekLast().getState() == false) ) {               
               soundList.add((char)96);
               soundList.add('D');
            }
            else {
               soundList.add((char)35);
            }
         }   
          //Long
         else if (cur == 'A') {
            //Single or last character, but not because it was moved there
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur) && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)126);
               soundList.add('D');
            }
            else {
               soundList.add((char)35);
            
               if (soundList.size() > 1 && (soundList.get(i-1) == 'j') ) {
                  soundList.add((char)76);
               }
               else {
                  soundList.add((char)40);    
               }
            }    
         }
         
          //E
          //Short
         else if ( cur == 'e') {
            //Single or last character, but not because it was moved there
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur) && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)96);
               soundList.add('F');
            }
            else {
               soundList.add((char)36);
            }
         }
          //Long
         else if (cur == 'E') {
            //Single or last character, but not because it was moved there
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur) && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)126);
               soundList.add('F');
            }
            else {
               soundList.add((char)36);
            
               if (soundList.size() > 1 && (soundList.get(i-1) == 'j') ) {
                  soundList.add((char)76);
               }
               else {
                  soundList.add((char)40);    
               }
            }         
         }
         
          //I
          //Short
         else if ( cur == 'i') {
            //Single or last character, but not because it was moved there  
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur) && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)96);
               soundList.add('G');
            }
            else {
               soundList.add((char)37);
            }
         }   
          //Long
         else if (cur == 'I') {
            //Single or last character, but not because it was moved there
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur)  && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)126);
               soundList.add('G');
            }
            else {
               soundList.add((char)37);
            
               if (soundList.size() > 1 && (soundList.get(i-1) == 'j') ) {
                  soundList.add((char)76);
               }
               else {
                  soundList.add((char)40);    
               }  
            }    
         }
         
          //O
          //Short
         else if ( cur == 'o') {
            //Single or last character, but not because it was moved there    
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur) && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)96);
               soundList.add('H');
            }
            else {
               soundList.add((char)94);
            }
         }   
          //Long
         else if (cur == 'O') {
            //Single or last character, but not because it was moved there
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur) && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)126);
               soundList.add('H');
            }
            else {
               soundList.add((char)94);
            
               if (soundList.size() > 1 && (soundList.get(i-1) == 'j') ) {
                  soundList.add((char)76);
               }
               else {
                  soundList.add((char)40);    
               } 
            }    
         }
         
          //U
          //Short
         else if ( cur == 'u') {
            //Single or last character, but not because it was moved there       
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur) && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)96);
               soundList.add('J');
            }
            else {
               soundList.add((char)38);
            }
         }   
          //Long
         else if (cur == 'U') {
            //Single or last character, but not because it was moved there
            if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur)  && (letterList.peekLast().getState() == false) ) {
               soundList.add((char)126);
               soundList.add('J');
            }
            else {
               soundList.add((char)38);
            
               if (soundList.size() > 1 && (soundList.get(i-1) == 'j') ) {
                  soundList.add((char)76);
               }
               else {
                  soundList.add((char)40);    
               }   
            }    
         }
      }
   }
   
}//End of file
