import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ViewUsers extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws IOException, ServletException {
        
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<html><head><title>Manage Users</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; }");
        out.println(".back-btn { position:absolute; top:20px; right:20px; padding:8px 16px; background:#007bff; color:white; text-decoration:none; border-radius:5px; font-weight:bold; }");
        out.println(".back-btn:hover { background:#0056b3; }");
        out.println("table { width:90%; margin:60px auto 20px auto; border-collapse:collapse; box-shadow:0 0 10px rgba(0,0,0,0.1);} ");
        out.println("th, td { border:1px solid #ccc; padding:10px; text-align:left; }");
        out.println("th { background:#007BFF; color:white; }");
        out.println("td { background:#f9f9f9; }");
        out.println("tr:hover td { background:#eef6ff; }");
        out.println(".delete-btn { background:#dc3545; color:white; border:none; padding:6px 12px; border-radius:4px; cursor:pointer; }");
        out.println(".delete-btn:hover { background:#a71d2a; }");
        out.println("h2 { text-align:center; color:#333; margin-top:40px; }");
        out.println("</style></head><body>");

        out.println("<a href='admindashboard.html' class='back-btn'>Back to Dashboard</a>");
        out.println("<h2>Registered Users</h2>");

        String deleteEmail = req.getParameter("delete");
        if (deleteEmail != null) {
            deleteUser(deleteEmail, out);
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "manager");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT email, name, phone, security_question, answer FROM users ORDER BY email"
            );

            out.println("<table>");
            out.println("<tr><th>Email</th><th>Name</th><th>Phone</th><th>Security Question</th><th>Answer</th><th>Action</th></tr>");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String email = rs.getString("email");
                out.println("<tr>");
                out.println("<td>" + email + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("phone") + "</td>");
                out.println("<td>" + rs.getString("security_question") + "</td>");
                out.println("<td>" + rs.getString("answer") + "</td>");
                out.println("<td><form method='get' style='margin:0;'>"
                        + "<input type='hidden' name='delete' value='" + email + "'>"
                        + "<input type='submit' class='delete-btn' value='Delete'>"
                        + "</form></td>");
                out.println("</tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='6' style='text-align:center;'>No users found</td></tr>");
            }
            
            out.println("</table>");

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("</body></html>");
    }

    private void deleteUser(String email, PrintWriter out) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "manager");

            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE email = ?");
            ps.setString(1, email);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<p style='color:green; text-align:center;'>User " + email + " deleted successfully!</p>");
            } else {
                out.println("<p style='color:orange; text-align:center;'>No user found with email: " + email + "</p>");
            }

            ps.close();
            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error deleting user: " + e.getMessage() + "</p>");
        }
    }
}
