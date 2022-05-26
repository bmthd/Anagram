package dev.bmth.sql;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import dev.bmth.main.Main;

class MainTest {

  @AfterAll
  static void tearDownAfterClass() throws Exception {}



  @Test
  void testStart() {
    Main m = new Main();
    assertThat(m.start("tst"),true );
    fail("まだ実装されていません"); // TODO
  }

}
