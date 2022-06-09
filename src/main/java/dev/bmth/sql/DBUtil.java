package dev.bmth.sql;

import java.util.ResourceBundle;

/**
 * propertiesファイルからデータベース接続情報を取得する
 * 
 * @author jouge
 *
 */
public class DBUtil {

  private static String URL;

  private static String USER;

  private static String PASSWORD;

  static {
    ResourceBundle rb = ResourceBundle.getBundle("db");
    URL = rb.getString("url");
    USER = rb.getString("user");
    PASSWORD = rb.getString("password");
  }

  /**
   * @return uRL
   */
  public static String getURL() {
    return URL;
  }

  /**
   * @return uSER
   */
  public static String getUSER() {
    return USER;
  }

  /**
   * @return pASSWORD
   */
  public static String getPASSWORD() {
    return PASSWORD;
  }

}

