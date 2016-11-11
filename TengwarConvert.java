/*
  Converts Roman letters to Tengwar in English mode. Result is returned 
  in the format used by the Tengwar Annatar font

  Two input methods:
  1) Input file from command line arguements: the file is read in, 
     converted and then printed out to a new file
  2) Input is from console

  Testing: 
  in aenchent tiems the rings of power wer crafted bie the elven-smiths, 
  and sawron, the dark lord, forjed the wun ring, filling it with hiz 
  oewn power so that he culd rul all others!
*/

/* Requires Letter.java */
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class TengwarConvert {
  public static LinkedList<Character> soundList;
  public static LinkedList<Letter> letterList;
  public static char[] charArray;

  public static void main (String[] args) { 
    /* Determine which method of input is being given and call the
     * corresponding method
     */

    soundList = new LinkedList<Character>();
    letterList = new LinkedList<Letter>();

    try {
      if (args.length == 1)
        fileInput(args); 
      else 
        consoleInput(); 
    } 
    catch (FileNotFoundException e) {
      System.out.println("error: file not found");
    }
  }

  public static void fileInput (String[] args) throws FileNotFoundException {
    /* Output is printed to a new file
     * Setup file input and output file Assumes that the file has a 3 letter
     * extension (eg. .txt)
     */

    String outputFileName, currentLine, outputLine, newWord, a0 = args[0];
    String[] wordsArray;
    File inputFile;
    Scanner in;
    PrintWriter writer;
    int i, wordLen;

    outputFileName = a0.substring(0, a0.length() -4) + "_converted.txt";
    inputFile = new File(a0);
    in = new Scanner(a0);
    writer = new PrintWriter(outputFileName);
    System.out.println("\nConverting...");

    /* get input from file */
    while (in.hasNext()) {
      currentLine = in.nextLine();

      if (currentLine.length() != 0) {
        /* break the currentLine into space delimited array of words */
        wordsArray = currentLine.split(" ");
        outputLine = "";

        for (String word : wordsArray) {
          wordLen = word.length();

          if (wordLen != 0) {      

            /* string -> lowercase char[] */
            for (i = 0, charArray = new char[wordLen]; i < wordLen; i++) 
              charArray[i] = Character.toLowerCase(word.charAt(i));

            simplify();       /* char[]     -> letterList */
            sortVowels();     /* letterList -> letterList */
            findSounds();     /* letterList -> soundList  */

            // retrieve output from soundList
            for (i = 0, newWord = ""; i < soundList.size(); i++) 
              newWord += Character.toString(soundList.get(i));

            outputLine += newWord + " ";
            letterList.clear();
            soundList.clear();
          }
        }
        writer.println(outputLine);
      }
    }
    writer.close();
  }

  /* Output is printed to the console */
  public static void consoleInput () {

    int i, wordLen;
    Scanner console;
    String[] wordsArray;
    String currentLine, outputLine, newWord;

    System.out.println("Ready");   
    console = new Scanner(System.in);
    currentLine = console.nextLine();
    System.out.println("Converting...");

    /* Move each word (seperated by a space) into an array of strings,
     * wordsArray */
    if (currentLine.length() != 0) {
      wordsArray = currentLine.split(" ");
      outputLine = "";

      for (String word : wordsArray) {      
        wordLen = word.length();

        if (wordLen != 0) {      

          /* string -> lowercase char[] */
          for (i = 0, charArray = new char[wordLen]; i < wordLen; i++) 
            charArray[i] = Character.toLowerCase(word.charAt(i));

          simplify();       /* char[]     -> letterList */
          sortVowels();     /* letterList -> letterList */
          findSounds();     /* letterList -> soundList  */

          // retrieve output from soundList
          for (i = 0, newWord = ""; i < soundList.size(); i++) 
            newWord += Character.toString(soundList.get(i));

          outputLine += newWord + " ";
          letterList.clear();
          soundList.clear();
        }
      }

      //Print the finished output string to the console
      System.out.println(outputLine);
    }
  }

  /* Searches through the array of characters from input and converts them into
     simplified form: long vowels (ae, ee, ie, oe, ue) are capitalized and
     double letters are capitalized based on their first letter (th, sh, ch,
     wh, ld, ng)

     Special words have the following translations (of : J, the : G, and : K) */
  public static void simplify() {

    int i, lastIndex = letterList.size() -1;
    char cur, nex, fol;

    //Fill letterList with all the characters
    for (char c : charArray)
      letterList.add(new Letter(c));

    //Go through the new letterList and check for words (of, the, and)
    if (letterList.size() == 2) {
      cur = letterList.get(0).getChar();
      nex = letterList.get(1).getChar();

      //of
      if (match(cur, nex, "of")) {
        letterList.clear(); 
        letterList.add(new Letter('J'));
      }   
    }
    else if (letterList.size() == 3) {
      cur = letterList.get(0).getChar();
      nex = letterList.get(1).getChar();
      fol = letterList.get(2).getChar();

      //the
      if (match(cur, nex, fol, "the")) {
        letterList.clear(); 
        letterList.add(new Letter('G'));
      } 
      //and
      if (match(cur, nex, fol, "and")) {
        letterList.clear(); 
        letterList.add(new Letter('K'));
      }
    }

    //Make changes      
    for (i = 0; i < lastIndex; i++) {
      cur = letterList.get(i).getChar();
      nex = letterList.get(i+1).getChar();

      //Repeated consonet case. These add short or long bars indicating a
      //repeated consonet sound
      if (cur == nex) {

        //short width consonet, for single width consonents. Set second
        //repeated consonet Q
        if (contains("cfhkprstwl", cur))
          letterList.set(i+1, new Letter('Q'));

        //long width consonet, for double width consonets. Set second repeated
        //consonet R
        if (contains("bdgjmnv", cur))
          letterList.set(i+1, new Letter('R'));
      }

      //special double letter case  ( sh, ch, wh, ld), remove following letter 
      //and capitalize the first letter
      if ((contains("scw", cur) && match(nex, "h")) || match(cur, nex, "ld") ||
          match(cur, nex, "ng")) {

        letterList.set(i, new Letter(Character.toUpperCase(cur)));
        letterList.remove(i+1);
          }

      //th case. Change t to H, remove h
      else if (match(cur, nex, "th")) {
        letterList.set(i, new Letter('H'));
        letterList.remove(i+1);
      }

      //n or m followed by t, d or b, p. Uppercase the following letter
      else if (match(cur, nex, "nt") || match(cur, nex, "nd") || match(cur,
            nex, "mb") || match(cur, nex, "bp")) {
        letterList.set(i, new Letter(Character.toUpperCase(nex)));
        letterList.remove(i+1);  
      }   

      //long vowel case. Any vowel followed by an e. Uppercase vowel, remove
      //following e
      else if (contains("aeiou", cur) && match(nex, "e")) {
        letterList.set(i, new Letter(Character.toUpperCase(cur)));
        letterList.remove(i+1);
      }
    }

    //s followed by a vowel case, change to downward pointing S
    for (i = 0; i < lastIndex; i++) {
      cur = letterList.get(i).getChar();
      nex = letterList.get(i+1).getChar();

      if (match(cur, "s") && contains("aeiou", nex))
        letterList.set(i, new Letter((char)27) );
    }
  }


  /* Searches through the array of characters and moves vowels 
     one character to the right*/
  public static void sortVowels() {

    //Last sound is s case and preceeding character is not a vowel
    //Replace last s with special s character
    int i, lastIndex = letterList.size() -1;
    boolean foundVowel;
    char secondToLast, cur, nex;
    char[] vowels = {'A', 'a', 'E', 'e', 'I', 'i', 'O', 'o', 'U', 'u'};

    if (lastIndex > 1  && match(letterList.get(lastIndex).getChar(), "s")) {

      secondToLast = letterList.get(lastIndex -1).getChar();

      //Compare secondToLast against all vowels
      foundVowel = false;
      for (char vowel : vowels) 
        if (secondToLast == vowel) 
          foundVowel = true;

      if (foundVowel == false ) 
        letterList.set(lastIndex, new Letter('X') );
    }

    for (i = 0; i < letterList.size()-1; i++) {
      cur = letterList.get(i).getChar();
      nex = letterList.get(i +1).getChar();

      Letter curL = letterList.get(i);
      Letter nexL = letterList.get(i +1);

      //Move short vowel case. Swap nexL and curL in letterList
      if (contains("aeiou", cur) && (! curL.getState() )) {
        letterList.set(i, nexL);
        letterList.set(i+1, curL);
        curL.moved();
      }

      ///Move long vowel case. Swap nexL and curL in letterList
      if (contains("AEIOU", cur) && (! curL.getState() )) {
        letterList.set(i, nexL);
        letterList.set(i+1, curL);
        curL.moved();
      }
    }
  }

  /* Goes through the converted letterList and builds the soundList*/
  public static void findSounds() {
    for (int i = 0; i < letterList.size(); i++) {
      char cur = letterList.get(i).getChar();

      //Special words (the, and, of)
      //--------------------------------------------------------------
      //the
      if ( cur == 'G' ) soundList.add('@');
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

      else if ( cur == '.' ) soundList.add('-');
      else if ( cur == ',' ) soundList.add('=');
      else if ( cur == '!' ) soundList.add((char)193);
      else if ( cur == '?' ) soundList.add((char)192);
      else if ( cur == '(' ) soundList.add((char)140);
      else if ( cur == ')' ) soundList.add((char)156);
      else if ( cur == '-' ) soundList.add((char)194);

      //Repeated Letter Modifiers
      //short width bar
      else if ( cur == 'R' ) soundList.add(':');
      //long width bar
      else if ( cur == 'Q' ) soundList.add(';');


      //Multi letters (th, sh, ch, wh, ld, ng, nt, nd, mp, mb, and following s)
      //--------------------------------------------------------------

      //th
      else if ( cur == 'H' ) soundList.add((char)51);
      //sh
      else if ( cur == 'S' ) soundList.add((char)100);
      //ch
      else if ( cur == 'C' ) soundList.add((char)65);
      //wh
      else if ( cur == 'W' ) soundList.add((char)111);
      //ld
      else if ( cur == 'L' ) soundList.add((char)109);
      //ng
      else if ( cur == 'N' ) soundList.add((char)98 );
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
      else if ( cur == 'B' ) {
        soundList.add((char)119);
        soundList.add('P');
      }
      //last s 
      else if ( cur == 'X' ) soundList.add('_');

      //Consonents
      //--------------------------------------------------------------
      else if (cur == 'b') soundList.add((char)119);
      else if (cur == 'c') soundList.add((char)122);
      else if (cur == 'd') soundList.add((char)50);
      else if (cur == 'f') soundList.add((char)101);
      else if (cur == 'g') soundList.add((char)120);
      else if (cur == 'h') soundList.add((char)57);
      else if (cur == 'j') soundList.add((char)115);
      else if (cur == 'k') soundList.add((char)122);
      else if (cur == 'l') soundList.add((char)106);
      else if (cur == 'm') soundList.add((char)116);
      else if (cur == 'n') soundList.add((char)53);
      else if (cur == 'p') soundList.add((char)113);
      else if ( cur == 'q') {
        soundList.add((char)122);
        soundList.add(i+1, (char)121);
      }
      else if ( cur == 'r') soundList.add((char)54);
      else if ( cur == 't') soundList.add((char)49);

      //S pointed up, or pointed down
      else if ( cur == 's') soundList.add((char)105);
      else if ( cur == (char)27) soundList.add((char)56);

      else if ( cur == 'v') soundList.add((char)114);
      else if ( cur == 'w') soundList.add((char)121);
      else if ( cur == 'x') {
        soundList.add((char)122);
        soundList.add((char)96);
      }
      else if ( cur == 'y') soundList.add((char)104);
      else if ( cur == 'z') soundList.add((char)44);

      //Vowels
      //--------------------------------------------------------------

      //A
      //Short
      else if ( cur == 'a') 
        convertShortVowel(cur, i, letterList, soundList,
            (char)96, 'D', (char)35);
      //Long
      else if (cur == 'A')
        convertLongVowel(cur, i, letterList, soundList,
            (char)126, 'D', (char)35, (char)76, (char)40);

      //E
      //Short
      else if ( cur == 'e') 
        convertShortVowel(cur, i, letterList, soundList,
            (char)96, 'F', (char)36);
      //Long
      else if (cur == 'E')
        convertLongVowel(cur, i, letterList, soundList,
            (char)126, 'F', (char)36, (char)76, (char)40);

      //I
      //Short
      else if ( cur == 'i') 
        convertShortVowel(cur, i, letterList, soundList,
            (char)96, 'G', (char)37);
      //Long
      else if (cur == 'I')
        convertLongVowel(cur, i, letterList, soundList,
            (char)126, 'G', (char)37, (char)76, (char)40);

      //O
      //Short
      else if ( cur == 'o') 
        convertShortVowel(cur, i, letterList, soundList,
            (char)96, 'H', (char)94);
      //Long
      else if (cur == 'O')
        convertLongVowel(cur, i, letterList, soundList,
            (char)126, 'H', (char)94, (char)76, (char)40);

      //U
      //Short
      else if ( cur == 'u') 
        convertShortVowel(cur, i, letterList, soundList, 
            (char)96, 'J', (char)38);
      //Long
      else if (cur == 'U') 
        convertLongVowel(cur, i, letterList, soundList,
            (char)126, 'J', (char)38, (char)76, (char)40);
    }
  }

  private static void convertShortVowel( char cur, int i,
      LinkedList<Letter> letterList, LinkedList<Character> soundList,
      char shortSingleLast1, char shortSingleLast2, char shortNormal){

    //Single or last character, but not because it was moved there       
    if ((letterList.size() == 1) || (letterList.peekLast().getChar() == cur) &&
        (! letterList.peekLast().getState())) {

      soundList.add(shortSingleLast1);
      soundList.add(shortSingleLast2);
        }
    else 
      soundList.add(shortNormal);
  }

  private static void convertLongVowel( char cur, int i,
      LinkedList<Letter> letterList, LinkedList<Character> soundList, 
      char longSingleLast1, char longSingleLast2, char longNormal,
      char longNormalSingle, char longNormalNotSingle){

    //Single or last character, but not because it was moved there       
    if ( (letterList.size() == 1) || (letterList.peekLast().getChar() == cur)
        && (letterList.peekLast().getState() == false) ) {

      soundList.add(longSingleLast1);
      soundList.add(longSingleLast2);
        }
    else {
      soundList.add(longNormal);

      // Stand alone vowel
      if (soundList.size() > 1 && (soundList.get(i-1) == 'j') ) 
        soundList.add(longNormalSingle);
      else 
        soundList.add(longNormalNotSingle);
    }    
  }

  /* helpers */
  public static boolean contains(String base, char value) {
    return base.contains("" + value);
  }

  public static boolean match(char a, String s) {
    return a == s.charAt(0);
  }

  public static boolean match(char a, char b, String s) {
    return (a == s.charAt(0)) && (b == s.charAt(1));
  }

  public static boolean match(char a, char b, char c, String s) {
    return (a == s.charAt(0)) && (b == s.charAt(1)) && (c == s.charAt(2));
  }
}
