import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class adminlogin extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String adminId = req.getParameter("admin_id");
        String adminPass = req.getParameter("admin_pass");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM admin_table WHERE admin_id=? AND admin_pass=?"
            );
            ps.setString(1, adminId);
            ps.setString(2, adminPass);

            ResultSet rs = ps.executeQuery();

            // 🔹 Common HTML & CSS Start
            out.println("<html><head>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background:#f4f6f9; text-align:center; padding:50px; }");
            out.println(".box { background:#fff; padding:30px; max-width:400px; margin:auto; border-radius:12px; box-shadow:0 4px 10px rgba(0,0,0,0.1);} ");
            out.println("h2 { color:#2c3e50; }");
            out.println("h3 { color:red; }");
            out.println("a { display:inline-block; margin-top:15px; padding:10px 20px; border-radius:8px; text-decoration:none; color:#fff; background:#3498db; transition:0.3s;} ");
            out.println("a:hover { background:#2c3e50; }");
            out.println("</style></head><body>");

            out.println("<div class='box'>");

            if (rs.next()) {
                // ✅ Admin successfully logged in
                out.println("<h2>Welcome, Admin <br>" + adminId + "</h2>");
                out.println("<a href='admindashboard.html'>Go to Dashboard</a>");
            } else {
                // ❌ Wrong credentials
                out.println("<h3>Invalid Admin ID or Password</h3>");
                out.println("<a href='adminlogin.html'>Try Again</a>");
            }

            out.println("</div>");
            out.println("</body></html>");

            con.close();
        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
