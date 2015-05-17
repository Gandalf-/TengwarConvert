/*
Class file for Letter object used in TengwarConvert
 
Data fields:
- symbol: the character of the current letter
- moved: whether the character has been moved already
                 used for the step in which vowels are re-arranged
                 
Methods:  
- getState()
- moved()
- changeSymbol()
- getChar()
*/
 
public class Letter {
 
//Data fields
   private char symbol;
   private boolean moved;
 
//Constructor
   public Letter() {
      this.moved = false;
   }
  
   public Letter(char symbol) {
      this.symbol = symbol;
      this.moved = false;
   }
    
   public Letter(char symbol, boolean moveState) {
      this.symbol = symbol;
      this.moved = moveState;
   }
    
   //Methods
   public boolean getState() {
      return this.moved;
   }
    
   public void moved() {
      this.moved = true;
   }
    
   public void changeSymbol(char newSymbol) {
      this.symbol = newSymbol;
   }
    
   public char getChar() {
      return this.symbol;
   }
    
}