package dev.bmth.servlet;

import java.io.IOException;
import dev.bmth.main.Main;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ResultServlet
 */
public class ResultServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ResultServlet() {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html; charset=UTF-8");
    request.setCharacterEncoding("UTF-8");
    request.setAttribute("resultHtml", "不正な遷移です。前のページに戻って検索してください");
    getServletConfig().getServletContext().getRequestDispatcher("/jsp/result.jsp").forward(request,
        response);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html; charset=UTF-8");
    request.setCharacterEncoding("UTF-8");
    Main m = new Main();
    String Html = null;
    Html = m.start(request.getParameter("anagram"));
    request.setAttribute("resultHtml", Html);
    getServletConfig().getServletContext().getRequestDispatcher("/jsp/result.jsp").forward(request,
        response);
  }

}
