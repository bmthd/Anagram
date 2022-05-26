package dev.bmth.main;

import java.sql.SQLException;
import java.sql.Timestamp;
import dev.bmth.sql.AnagramDAO;
import dev.bmth.sql.DBConnection;
import dev.bmth.sql.InputWordDAO;

/**
 *並び替え用の処理を記述したクラス
 * @author jouge
 */
public class SortAnagram {
  /**データベースに格納するTimestamp   */
  private Timestamp time;
  /**データベース側で生成された主キー   */
  private int id;
  
  private AnagramDAO dao;
  private InputWordDAO dao2;
  public DBConnection con;
  
  /**
   * コンストラクタ
   * @throws SQLException
   */
  public SortAnagram() throws SQLException {
    time = new Timestamp(System.currentTimeMillis());
    con = new DBConnection();
    dao =new AnagramDAO(con.getConnection());
    dao2 = new InputWordDAO(con.getConnection());
  }
  
  /**
   * 引数を並び替えてデータベースに配置する
   * @param s 並び替えたい文字列
   * @throws SQLException
   */
  public void start(String s) throws SQLException {
    dao2.insert(s, time);
    id=dao2.selectId(s, time);
    String[] right = s.split("");
    String[] left = new String[0];
    recursive(left, right);
    dao.execute(dao.createQuery());//最後の1回
    dao.con.commit();
    con.close();
  }
  
  /**
   * idのゲッター
   * @return id
   */
  public int getId() {
    return id;
  }
  

  private void recursive(String[] left, String[] right) throws SQLException {
      String[] newLeft = new String[left.length + 1];
      for (int i = 0; i < left.length; i++) {
          newLeft[i] = left[i];
      }

      for (int i = 0; i < right.length; i++) {
          newLeft[newLeft.length - 1] = right[i]; 
          if (right.length == 1) { 
              insertResult(newLeft);
          } else {
              String[] newRight = new String[right.length - 1];
              for (int j = 0; j < i; j++) { 
                  newRight[j] = right[j];
              }
              for (int j = i + 1; j < right.length; j++) { 
                  newRight[j - 1] = right[j];
              }
              recursive(newLeft, newRight);
          }
      }
  }

  private void insertResult(String[] result) throws SQLException {
    StringBuilder sb = new StringBuilder();
      for (int i = 0; i < result.length; i++) { 
        sb.append(result[i]);
      }
      dao.insert(id,sb.toString(),0);          
  }
  

}
