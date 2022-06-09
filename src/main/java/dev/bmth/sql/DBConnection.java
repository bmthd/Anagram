package dev.bmth.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * データベースのコネクタ取得用クラス
 * 必ず最後にcloseメソッドを呼び出す
 * 
 * @author jouge
 */
public class DBConnection {

  /**   */
  protected Connection con = null;

  /**
   * Connectionを返す
   * 
   * @return Connection
   * @throws SQLException
   */
  public Connection getConnection() throws SQLException {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    con = DriverManager.getConnection(DBUtil.getURL(), DBUtil.getUSER(), DBUtil.getPASSWORD());
    return con;
  }

  /**
   * クローズ用
   */
  public void close() {
    try {
      if (con != null) {
        con.close();
      }
    } catch (SQLException e) {
      System.out.println("DBクローズエラー");
    }
  }

}
