package dev.bmth.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * inputword表操作用クラス
 * 
 * @author jouge
 */
public class InputWordDAO {
  private List<InputWord> list;
  public Connection con;

  public InputWordDAO(Connection con) {
    list = new ArrayList<>();
    this.con = con;
    try {
      this.con.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 入力された単語をinputword表に格納する
   * 
   * @param s,t
   */
  public void insert(String s, Timestamp t) {
    String sql = "INSERT INTO inputword (word,time) VALUES(?,?)";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setString(1, s);
      stmt.setTimestamp(2, t);
      stmt.executeUpdate();
      System.out.println("INSERT成功" + s + t);
      con.commit();
    } catch (SQLException e) {
      System.out.println("INSERTエラー：" + e.getMessage());
    }
  }

  /**
   * inputword表のIDを取得する
   * 
   * @param word
   * @param time
   * @return id
   */
  public int selectId(String word, Timestamp time) {
    int id = 0;
    String sql = "SELECT id FROM inputword WHERE word = ? AND time = ?";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setString(1, word);
      stmt.setTimestamp(2, time);
      ResultSet rs = stmt.executeQuery();
      rs.next();
      id = rs.getInt("id");
    } catch (SQLException e) {
      System.out.println("SELECTエラー：" + e.getMessage());
      e.printStackTrace();
    }
    return id;
  }

  /**
   * 過去に入力された単語を新しい順に10件取得しそのリストを返す
   * 
   * @return list InputWord型のArrayList
   */
  public List<InputWord> setInputWord() {
    String sql = "SELECT id word time FROM inputword ORDER BY time DESC FETCH FIRST 10 ROWS ONLY";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        InputWord iw = new InputWord();
        iw.setId(rs.getInt("id"));
        iw.setWord(rs.getString("word"));
        iw.setTime(rs.getTimestamp("time"));
        list.add(iw);
      }
      con.commit();
    } catch (SQLException e) {
      System.out.println("SELECTエラー:" + e.getMessage());
    }
    return list;
  }

}
