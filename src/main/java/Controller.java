import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;
import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class Controller extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
	//	String url = request.getParameter("url");
	//	Parser p1 = new Parser(url);
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.print("Worked Get!");
		//request.getRequestDispatcher("/controller").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String url = request.getParameter("url");
		Parser p1 = new Parser(url);
		url += p1.toString(p1.seachLink());
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.print(url);
		//request.getRequestDispatcher("/controller").forward(request, response);
	}
}
