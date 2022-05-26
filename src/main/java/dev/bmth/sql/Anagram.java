package dev.bmth.sql;


/**
 * @author jouge
 * anagram表を表すクラス
 *
 */
public class Anagram {
  
  private int id;
  
  private String word;
  
  private int count;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }


}
