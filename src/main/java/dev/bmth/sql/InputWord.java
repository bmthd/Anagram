package dev.bmth.sql;

import java.sql.Timestamp;

/**
 * inputword表を表すクラス
 * 
 * @author jouge
 */
public class InputWord {
  private int id;
  private String word;
  private Timestamp time;

  /**
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id セットする id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return word
   */
  public String getWord() {
    return word;
  }

  /**
   * @param word セットする word
   */
  public void setWord(String word) {
    this.word = word;
  }

  /**
   * @return time
   */
  public Timestamp getTime() {
    return time;
  }

  /**
   * @param time セットする time
   */
  public void setTime(Timestamp time) {
    this.time = time;
  }

}
