import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Registration extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html; charset=UTF-8");
        PrintWriter pw1 = res.getWriter();

        // Form fields
        String email = req.getParameter("R1");
        String nm = req.getParameter("R2");
        String phone = req.getParameter("R3");
        String sq = req.getParameter("R4");
        String ans = req.getParameter("R5");
        String pass = req.getParameter("R6");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            // Check if user already exists
            PreparedStatement checkPs = con.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE EMAIL=?");
            checkPs.setString(1, email);
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Email already registered
                pw1.println("<html><head><title>Registration Error</title>");
                pw1.println("<style>");
                pw1.println("body {background: #f8d7da; display: flex; justify-content: center; align-items: center; height: 100vh; font-family: Arial, sans-serif;}");
                pw1.println(".box {background: #fff3f3; padding: 30px; border-radius: 15px; border: 2px solid #f5c2c7; text-align: center; box-shadow: 0 6px 15px rgba(0,0,0,0.1); width: 400px;}");
                pw1.println("h2 {color: #721c24; margin-bottom: 20px;}");
                pw1.println("a {display: inline-block; padding: 10px 20px; background: #721c24; color: white; text-decoration: none; border-radius: 8px; font-weight: bold; transition: 0.3s;}");
                pw1.println("a:hover {background: #501217;}");
                pw1.println("</style></head><body>");
                pw1.println("<div class='box'>");
                pw1.println("<h2>This Email is already registered!</h2>");
                pw1.println("<a href='login.html'>Go to Login</a>");
                pw1.println("</div></body></html>");
            } else {
                // Insert new user
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users (EMAIL, NAME, PHONE, SECURITY_QUESTION, ANSWER, PASSWORD) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, email);
                ps.setString(2, nm);
                ps.setString(3, phone);
                ps.setString(4, sq);
                ps.setString(5, ans);
                ps.setString(6, pass);

                int x = ps.executeUpdate();

                if (x > 0) {
                    // Registration successful
                    pw1.println("<html><head><title>Registration Success</title>");
                    pw1.println("<style>");
                    pw1.println("body {background: linear-gradient(135deg,#74ebd5,#9face6); display:flex; justify-content:center; align-items:center; height:100vh; font-family:Arial,sans-serif;}");
                    pw1.println(".box {background:#fff; padding:30px; border-radius:15px; box-shadow:0 8px 20px rgba(0,0,0,0.2); text-align:center; width:400px;}");
                    pw1.println("h2 {color:#333; margin-bottom:20px;}");
                    pw1.println(".btn {display:inline-block; margin-top:15px; padding:10px 20px; background:#6a5acd; color:#fff; text-decoration:none; border-radius:8px; font-weight:bold; transition:0.3s;}");
                    pw1.println(".btn:hover {background:#483d8b;}");
                    pw1.println("</style></head><body>");
                    pw1.println("<div class='box'>");
                    pw1.println("<h2>Registration Successful</h2>");
                    pw1.println("<p>You have successfully registered.</p>");
                    pw1.println("<a href='login.html' class='btn'>Go to Login Page</a>");
                    pw1.println("</div></body></html>");
                } else {
                    pw1.println("<h3 style='color:red;'>Registration Failed. Please try again.</h3>");
                }
            }

            con.close();
        } catch (Exception e) {
            pw1.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}
