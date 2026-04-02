import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ForgetPassword1 extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException 
    {
        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();
        String eid;
        String ans = req.getParameter("n1");

        try {
            HttpSession ses = req.getSession();
            eid = (String) ses.getAttribute("email1");

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            Statement stmt = con.createStatement();
            String q1 = "select * from users where email='" + eid +
                        "' and answer='" + ans + "'";
            ResultSet rs = stmt.executeQuery(q1);

            if (rs.next()) {
                pw1.println("<html>");
                pw1.println("<head>");
                pw1.println("<title>New Password</title>");
                pw1.println("<style>");
                pw1.println("body {");
                pw1.println("    font-family: Arial, sans-serif;");
                pw1.println("    background: linear-gradient(135deg, #74ebd5, #9face6);");
                pw1.println("    display: flex;");
                pw1.println("    justify-content: center;");
                pw1.println("    align-items: center;");
                pw1.println("    height: 100vh;");
                pw1.println("    margin: 0;");
                pw1.println("}");
                pw1.println("form {");
                pw1.println("    background: #fff;");
                pw1.println("    padding: 30px;");
                pw1.println("    border-radius: 15px;");
                pw1.println("    width: 350px;");
                pw1.println("    box-shadow: 0 8px 20px rgba(0,0,0,0.2);");
                pw1.println("    text-align: center;");
                pw1.println("    animation: fadeIn 0.8s ease-in-out;");
                pw1.println("}");
                pw1.println("@keyframes fadeIn {");
                pw1.println("    from {opacity: 0; transform: scale(0.9);}");
                pw1.println("    to {opacity: 1; transform: scale(1);}");
                pw1.println("}");
                pw1.println("h2 { color: #333; margin-bottom: 20px; }");
                pw1.println("input[type=text], input[type=password] {");
                pw1.println("    width: 90%; padding: 10px; margin: 10px 0;");
                pw1.println("    border: 1px solid #ccc;");
                pw1.println("    border-radius: 8px;");
                pw1.println("    outline: none;");
                pw1.println("    transition: 0.3s;");
                pw1.println("}");
                pw1.println("input[type=text]:focus, input[type=password]:focus {");
                pw1.println("    border-color: #6a5acd;");
                pw1.println("    box-shadow: 0 0 8px rgba(106,90,205,0.5);");
                pw1.println("}");
                pw1.println("input[type=submit], input[type=reset], button {");
                pw1.println("    width: 45%; padding: 10px; margin: 10px 5px;");
                pw1.println("    border: none; border-radius: 8px;");
                pw1.println("    font-weight: bold; cursor: pointer;");
                pw1.println("    transition: 0.3s;");
                pw1.println("}");
                pw1.println("input[type=submit] { background: #6a5acd; color: #fff; }");
                pw1.println("input[type=submit]:hover { background: #483d8b; transform: scale(1.05);}");
                pw1.println("input[type=reset] { background: #f08080; color: #fff; }");
                pw1.println("input[type=reset]:hover { background: #cd5c5c; transform: scale(1.05);}");
                pw1.println("button { background: linear-gradient(135deg, #808080, #505050); color: #fff; }");
                pw1.println("button:hover { background: linear-gradient(135deg, #505050, #303030); transform: scale(1.05);}");
                pw1.println("</style>");
                pw1.println("</head>");
                pw1.println("<body>");
                pw1.println("<form method='post' action='ForgetPassword2'>");
                pw1.println("<h2>Set New Password</h2>");
                pw1.println("<input type='password' name='n2' placeholder='Enter New Password' required>");
                pw1.println("<br>");
                pw1.println("<input type='submit' value='Submit'>");
                pw1.println("<input type='reset' value='Reset'>");
                pw1.println("<br>");
                // ✅ Back button fixed: takes user to ForgetPassword.html
                pw1.println("<br><button type='button' onclick='history.back()'>Back</button>");
                pw1.println("</form>");
                pw1.println("</body>");
                pw1.println("</html>");
            } else {
                pw1.println("<h3 style='color:red;text-align:center;'>Invalid Answer! Please try again.</h3>");
                pw1.println("<div style='text-align:center;margin-top:20px;'>");
                pw1.println("<button onclick=\"window.location.href='ForgetPassword.html'\" " +
                            "style='padding:10px 20px;border:none;border-radius:8px;background:#6a5acd;color:#fff;cursor:pointer;'>Back</button>");
                pw1.println("</div>");
            }
        } catch (Exception e) {
            pw1.println("<p style='color:red'>Error: " + e + "</p>");
        }
    }
}
