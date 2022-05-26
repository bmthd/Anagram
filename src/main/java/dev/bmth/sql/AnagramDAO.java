package dev.bmth.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jouge
 * anagram表操作用クラス
 * 
 */
public class AnagramDAO {
  private List<Anagram> list;
  public Connection con;

  public AnagramDAO(Connection con) {
    list = new ArrayList<>();
    this.con = con;
    try {
      this.con.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  
  /**
   * 並び替えた単語を受け取る
   * @param i,k,v
   */
  public void insert(int id, String word, int count) {
    Anagram a = new Anagram();
    a.setId(id);
    a.setWord(word);
    a.setCount(count);
    list.add(a);
    if(list.size()==1000){ //1000行に達したらクエリ実行
      execute(createQuery());
      list.clear(); //outOfMemoryError対策
    }
    
  }
  
  /**
   * 与えられた行数文のSQL文を作成
   * @return sql
   */
  public String createQuery() {
    StringBuilder sql = new StringBuilder();
    sql.append("INSERT INTO anagram (id,word,count) VALUES");
    for(int i=0;i!=list.size();i++) {
      sql.append("(?,?,?)");
        if(i != list.size()-1) {
          sql.append(" ,");
          }else {
            sql.append("ON DUPLICATE KEY UPDATE id=id,word=word,count=count;");
          }
    }
    return sql.toString();
  }
  
  /**
   * SQL文を元に行数分のクエリを実行
   * @param sql
   */
  public void execute(String sql) {
    int[] i=new int[1];
    i[0]=0;
    try(PreparedStatement stmt =con.prepareStatement(sql)){
      list.forEach(a->{
        try {
          stmt.setInt(++i[0], a.getId());
          stmt.setString(++i[0], a.getWord());
          stmt.setInt(++i[0], a.getCount());
        } catch (SQLException e) {
          System.out.println("クエリセットエラー"+e.getMessage());
        }});
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("クエリ実行エラー"+sql+e.getMessage());
    }
  }

  /**
   * IDを指定してそのレコード数を返す
   * @return i
   */
  public int selectCount(int id) {
    int i = 0;
    String sql = "SELECT COUNT(DISTINCT word) FROM anagram where id = ?";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.next();
      i = rs.getInt(1);
    } catch (SQLException e) {
      System.out.println("SELECTエラー：" + e.getMessage());
    }

    return i;
  }

  /**
   * anagram表のid,word,countのリストを返す
   * @param id
   * @return list
   */
  public List<Anagram> setAnagram(int id) {
    List<Anagram> list = new ArrayList<>();
    String sql = "SELECT DISTINCT id,word,count FROM anagram WHERE id = ? ORDER BY count DESC,word ASC";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Anagram a = new Anagram();
        a.setId(rs.getInt("id"));
        a.setWord(rs.getString("word"));
        a.setCount(rs.getInt("count"));
        list.add(a);
      }

    } catch (SQLException e) {
      System.out.println("SELECT anagramエラー：" + e.getMessage());
    }
    return list;
  }
  
  /**
   * idとwordから該当のcountを更新する
   * @param id
   * @param word
   * @param count
   */
  public void update(int id, String word, int count) {
    String sql = "UPDATE anagram SET count = ? WHERE id = ? AND word = ?";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setInt(1, count);
      stmt.setInt(2, id);
      stmt.setString(3, word);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("UPDATEエラー" + e.getMessage());
    }
  }
  
  /**
   * 使わなかったデータを削除する
   * @param id
   */
  public void delete0(int id) {
    String sql = "DELETE FROM anagram WHERE id = ? AND count =0";
    try (PreparedStatement stmt = con.prepareStatement(sql)){
      stmt.setInt(1, id);
      stmt.execute();
      System.out.println("未使用データDELETE完了");
    } catch (SQLException e) {
      System.out.println("DELETEエラー"+e.getMessage());
    }
  }

  /**
   * close用
   */
  public void close() {
    try {
      con.commit();
      con.close();
    } catch (SQLException e) {
      System.out.println("Connection closeエラー" + e.getMessage());
    }
  }
  
  /**
   * テーブルクリア
   * 使用禁止
   */
  @SuppressWarnings("unused")
  private void truncateTable() {
    String sql = "TRUNCATE TABLE bmth.anagrams";
    try (PreparedStatement stmt = con.prepareStatement(sql)){
      stmt.execute();
    } catch (SQLException e) {
      System.out.println("TRUNCATEエラー:" + e.getMessage());
    }
  }


  
}



