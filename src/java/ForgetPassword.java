import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ForgetPassword extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();
        String uid = req.getParameter("l1");

        try {
            HttpSession ses = req.getSession();
            ses.setAttribute("email1", uid);

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            Statement stmt = con.createStatement();
            String q1 = "select * from users where email='" + uid + "'";
            ResultSet rs = stmt.executeQuery(q1);

            if (rs.next()) {
                pw1.println("<html>");
                pw1.println("<head><title>Security Question</title>");
                pw1.println("<style>");
                pw1.println("body {background: linear-gradient(135deg,#74ebd5,#9face6); "
                        + "display:flex; justify-content:center; align-items:center; height:100vh; font-family:Arial,sans-serif;}");
                pw1.println(".box {background:#fff; padding:30px; border-radius:15px; "
                        + "box-shadow:0 8px 20px rgba(0,0,0,0.2); width:400px; text-align:center;}");
                pw1.println("h2 {color:#333; margin-bottom:20px;}");
                pw1.println("input[type=text] {width:90%; padding:10px; margin:10px 0; border:1px solid #ccc; "
                        + "border-radius:8px; font-size:14px;}");
                pw1.println("input[type=submit], input[type=reset], button {padding:10px 20px; margin:10px; border:none; "
                        + "border-radius:8px; font-weight:bold; cursor:pointer; transition:0.3s;}");
                pw1.println("input[type=submit] {background:#6a5acd; color:#fff;}");
                pw1.println("input[type=submit]:hover {background:#483d8b;}");
                pw1.println("input[type=reset] {background:#f08080; color:#fff;}");
                pw1.println("input[type=reset]:hover {background:#cd5c5c;}");
                pw1.println("button {background:#808080; color:#fff;}");
                pw1.println("button:hover {background:#505050;}");
                pw1.println("</style></head><body>");
                
                pw1.println("<div class='box'>");
                pw1.println("<h2>Security Verification</h2>");
                pw1.println("<form method='post' action='ForgetPassword1'>");
                pw1.println("<p><b>Security Question:</b><br>" + rs.getString(4) + "</p>");
                pw1.println("<input type='text' name='n1' placeholder='Enter your answer'>");
                pw1.println("<br><input type='submit' value='Submit'>");
                pw1.println("<input type='reset' value='Reset'>");
                pw1.println("<br><button type='button' onclick='history.back()'>Back</button>");
                pw1.println("</form>");
                pw1.println("</div></body></html>");
            } else {
                pw1.println("<html><body style='display:flex; justify-content:center; align-items:center; height:100vh; "
                        + "font-family:Arial,sans-serif; background:#f8d7da;'>");
                pw1.println("<div style='background:#fff3f3; padding:20px; border-radius:10px; "
                        + "box-shadow:0 6px 15px rgba(0,0,0,0.2); color:#721c24; text-align:center;'>");
                pw1.println("<h2>Wrong Email ID!</h2>");
                pw1.println("<p>Please enter a valid registered email.</p>");
                pw1.println("<br><button type='button' onclick='history.back()' "
                        + "style='padding:10px 20px; border:none; border-radius:8px; background:#808080; color:#fff; cursor:pointer;'>Back</button>");
                pw1.println("</div></body></html>");
            }
            con.close();
        } catch (Exception e) {
            pw1.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
