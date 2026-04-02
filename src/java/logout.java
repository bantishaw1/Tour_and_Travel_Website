import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class logout extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        // ✅ Session destroy
        HttpSession session = req.getSession();
        session.invalidate();

        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        pw.println("<!DOCTYPE html>");
        pw.println("<html><head><title>Logout</title>");
        pw.println("<style>");
        pw.println("body {");
        pw.println("  font-family: 'Arial', sans-serif;");
        pw.println("  display: flex;");
        pw.println("  justify-content: center;");
        pw.println("  align-items: center;");
        pw.println("  height: 100vh;");
        pw.println("  background: linear-gradient(to right, #4facfe, #00f2fe);");
        pw.println("  margin: 0;");
        pw.println("}");
        pw.println(".logout-box {");
        pw.println("  text-align: center;");
        pw.println("  background: rgba(255, 255, 255, 0.9);");
        pw.println("  padding: 50px 40px;");
        pw.println("  border-radius: 20px;");
        pw.println("  box-shadow: 0 10px 30px rgba(0,0,0,0.3);");
        pw.println("}");
        pw.println(".logout-box h2 {");
        pw.println("  color: #0d6efd;");
        pw.println("  margin-bottom: 25px;");
        pw.println("}");
        pw.println(".logout-box a {");
        pw.println("  display: inline-block;");
        pw.println("  padding: 12px 25px;");
        pw.println("  background: #198754;");
        pw.println("  color: #fff;");
        pw.println("  text-decoration: none;");
        pw.println("  border-radius: 10px;");
        pw.println("  font-size: 18px;");
        pw.println("  transition: 0.3s;");
        pw.println("}");
        pw.println(".logout-box a:hover {");
        pw.println("  background: #157347;");
        pw.println("  transform: scale(1.05);");
        pw.println("}");
        pw.println("</style></head><body>");
        
        pw.println("<div class='logout-box'>");
        pw.println("<h2>You have been logged out successfully!</h2>");
        pw.println("<a href='login.html'>Login Again</a>");
        pw.println("</div>");

        pw.println("</body></html>");
    }
}
