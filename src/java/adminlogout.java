import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class adminlogout extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate(); // ✅ Session destroy
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Logout</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: #f4f7fa; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
        out.println(".box { background: #fff; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.2); text-align: center; }");
        out.println("h2 { color: #2c3e50; margin-bottom: 20px; }");
        out.println("a { display: inline-block; padding: 10px 20px; text-decoration: none; background: #3498db; color: #fff; border-radius: 5px; transition: 0.3s; }");
        out.println("a:hover { background: #2980b9; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='box'>");
        out.println("<h2>Logged out successfully!</h2>");
        out.println("<a href='adminlogin.html'>Go to Login</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
