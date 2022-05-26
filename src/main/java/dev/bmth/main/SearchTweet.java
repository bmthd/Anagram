package dev.bmth.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Twitter4Jライブラリを操作するクラス
 * @author jouge
 *
 */
public class SearchTweet {
  Twitter twitter;

  /**
   * コンストラクタ
   */
  public SearchTweet() {
    twitter = new TwitterFactory().getInstance();
  }
  
  /**
   * Twitterを検索した検索結果の件数を返す
   * @param str 検索キーワード
   * @return 検索結果の件数
   */
  public int tweetSearch(String str) throws TwitterException{
  Query query = new Query();
  query.setQuery(str);
  //検索件数
  query.count(100);
  QueryResult result = null;
  result = twitter.search(query);
  System.out.println(str+"を検索中…");
  System.out.println(result.getTweets().size()+"件");
  return result.getTweets().size();
  
  
  }
  
  /**
   * apiリセット時間を取得
   * @return str 結果のHTML
   * @throws TwitterException
   */
  public String countApi() throws TwitterException{
    ResponseList<Status> twitterStatuses= (ResponseList<Status>)twitter.getHomeTimeline();
  RateLimitStatus ratelimit = twitterStatuses.getRateLimitStatus();
  Calendar calendar = Calendar.getInstance();
  calendar.setTime(new Date());
  calendar.add(Calendar.SECOND, ratelimit.getSecondsUntilReset());
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  String str="リセットされる時間"+sdf.format(calendar.getTime())+"<br>";
    return str;

  }
  


}
