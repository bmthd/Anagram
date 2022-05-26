package dev.bmth.main;

import java.sql.SQLException;
import dev.bmth.sql.AnagramDAO;
import dev.bmth.sql.DBConnection;
import twitter4j.TwitterException;

/**
 * 他のクラスを操作するクラス
 * @author jouge
 *
 */
public class Main {
  long startTime;
  long interimTime;
  long endTime;

  /**
   * テスト用メソッド
   * @param args
   */
  public static void main(String[] args) {
    Main m = new Main();
      try {
        System.out.println(m.start("テスト"));
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
  }

  /**
   * 検索したいキーワードを引数に結果のHTMLを生成
   * @param s　検索キーワード
   * @return 結果のHTML
   */
  public String start(String s) {
    StringBuilder sb = new StringBuilder();
    SearchTweet tw = new SearchTweet();
    if(s.length()<=1) {
      sb.append("入力された文字が1文字以下です。<br>2文字以上入力してから送信してください。<br>"); //null対策
    }else if(s.length()>10){
      sb.append("入力された文字が長すぎます。<br>10文字以下にして送信してください。<br>"); //処理落ち対策
    }else if(isHTMLTag(s)) {
      sb.append("入力された文字に記号文字が含まれます。<br>漢字、ひらがな、アルファベットなどで入力してください。"); //XSS対策
    }else {
      try {
        DBConnection con = new DBConnection();
        startTime = System.currentTimeMillis();
        AnagramDAO dao = new AnagramDAO(con.getConnection());
        SortAnagram sa = new SortAnagram();
        sa.start(s);
        int id =sa.getId();
        
          if(dao.selectCount(id)>180) {
            sb.append("パターン:"+dao.selectCount(id)+"件<br>");
            sb.append("パターンが多すぎます。180通り以下の組み合わせになる5文字以下の文字でお試しください。"); //TwitterAPI切れ対策 
          }else {
            interimTime = System.currentTimeMillis();
            dao.setAnagram(id).parallelStream().forEach( //並列処理で高速化
              an->{
                try {
                  dao.update(id,an.getWord(),tw.tweetSearch(an.getWord())); //結果セット
                }catch (TwitterException e) {
                  throw new RuntimeException("TwitterException in Ramda"+e.getMessage()); //API切れ判定
                }
              }
              );
          sb.append("\""+s+"\"のアナグラムの可能性があるワードは以下のとおりです。<br>");
          sb.append("パターン:"+dao.selectCount(id)+"件<br>");
          dao.setAnagram(id).forEach(
              an->{if(an.getCount()!=0) 
                sb.append("ワード:" + an.getWord() + " " + "検索結果数: " + an.getCount()+"<br>"); //結果表示
                }
              );
          endTime =System.currentTimeMillis();
          sb.append("処理時間:"+(endTime-startTime)/1000+"秒"+"<br>");
          }
          dao.delete0(id); //ストレージ圧迫対策
          dao.close();
          System.out.println("データベース処理時間:"+(interimTime-startTime)/1000+"秒");
          System.out.println("Twitter処理時間:"+(endTime-interimTime)/1000+"秒");
          
      } catch (SQLException e) {
        sb.append("データベース接続で問題が発生しました。<br>管理者にお問い合わせください。</div><div><a href=mailto:jougennotuki67@gmail.com>管理者にメール</a><br>エラーメッセージ:"+e.getMessage()+"<br>");
        e.printStackTrace();
      }catch (RuntimeException e) {
        try {
          sb.append("Twitter APIを使い切っています。<br>" + tw.countApi());
          e.printStackTrace();
        } catch (TwitterException e1) {
          sb.append("Twitterアクセスで問題が発生しました。<br>Twitterで障害が発生している可能性があります。<br>");
          e1.printStackTrace();
        }
      }
    }
    return sb.toString();
  }
  
  public static boolean isHTMLTag(String str){
    boolean result=false;
     String[] strings =str.split("");
     for(int i=0;i<strings.length;i++) {
       String s = new String(strings[i]);
       switch(s) {
         case "&" :
           result = true;
           break;
         case "<" :
           result = true;
           break;
         case ">" :
           result = true;
           break;
         case "\"" :
           result = true;
         case "\\" :
           result = true;
           break;
       }
     }
      return result;
  }

  
  }
