import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ForgetPassword2 extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException 
    {
        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();
        String eid;
        String npass = req.getParameter("n2");
        try {
            HttpSession ses = req.getSession();
            eid = (String) ses.getAttribute("email1");

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            Statement stmt = con.createStatement();
            String q1 = "update users set password='" + npass +
                    "' where email='" + eid + "'";
            int x = stmt.executeUpdate(q1);

            if (x > 0) {
                // ✅ Success Page with CSS
                pw1.println("<html>\n" +
"  <head>\n" +
"    <title>Password Changed</title>\n" +
"    <style>\n" +
"      body {font-family: Arial, sans-serif; background: #f4f4f9; display:flex; justify-content:center; align-items:center; height:100vh;}\n" +
"      .box {background: #fff; padding: 30px; border-radius: 10px; text-align:center; box-shadow: 0px 4px 8px rgba(0,0,0,0.2);}\n" +
"      h2 {color: green;}\n" +
"      a {display:inline-block; margin-top:15px; text-decoration:none; color: white; background:#007BFF; padding:10px 20px; border-radius:5px;}\n" +
"      a:hover {background:#0056b3;}\n" +
"    </style>\n" +
"  </head>\n" +
"  <body>\n" +
"    <div class='box'>\n" +
"      <h2>Password Changed Successfully</h2>\n" +
"      <p>You can now login with your new password.</p>\n" +
"      <a href='login.html'>Go to Login Page</a>\n" +
"    </div>\n" +
"  </body>\n" +
"</html>");
            } else {
                // ❌ Failure Page with CSS
                pw1.println("<html>\n" +
"  <head>\n" +
"    <title>Password Change Failed</title>\n" +
"    <style>\n" +
"      body {font-family: Arial, sans-serif; background: #f8d7da; display:flex; justify-content:center; align-items:center; height:100vh;}\n" +
"      .box {background: #fff; padding: 30px; border-radius: 10px; text-align:center; box-shadow: 0px 4px 8px rgba(0,0,0,0.2);}\n" +
"      h2 {color: red;}\n" +
"      a {display:inline-block; margin-top:15px; text-decoration:none; color: white; background:#007BFF; padding:10px 20px; border-radius:5px;}\n" +
"      a:hover {background:#0056b3;}\n" +
"    </style>\n" +
"  </head>\n" +
"  <body>\n" +
"    <div class='box'>\n" +
"      <h2>Password Change Unsuccessful </h2>\n" +
"      <p>Please try again later.</p>\n" +
"      <a href='ForgetPassword.html'>Retry</a>\n" +
"    </div>\n" +
"  </body>\n" +
"</html>");
            }

            con.close();
        } catch (Exception e) {
            pw1.println(e);
        }
    }
}
