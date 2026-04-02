import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class viewfeedback extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws IOException, ServletException {
        
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<html><head><title>Feedbacks</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; }");
        out.println(".back-btn { position:absolute; top:20px; right:20px; padding:8px 16px; background:#007bff; color:white; text-decoration:none; border-radius:5px; font-weight:bold; }");
        out.println(".back-btn:hover { background:#0056b3; }");
        out.println("table { width:80%; margin:60px auto 20px auto; border-collapse:collapse; }");
        out.println("th, td { border:1px solid #ccc; padding:10px; text-align:left; }");
        out.println("th { background:#007BFF; color:white; }");
        out.println("td { background:#f9f9f9; }");
        out.println("h2 { text-align:center; color:#333; margin-top:40px; }");
        out.println("</style></head><body>");

        // Back button
        out.println("<a href='admindashboard.html' class='back-btn'>Back to Dashboard</a>");
        out.println("<h2>User Feedbacks</h2>");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "manager");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT name, email, message FROM feedback_table ORDER BY created_at DESC"
            );

            out.println("<table>");
            out.println("<tr><th>Name</th><th>Email</th><th>Message</th></tr>");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                out.println("<tr>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("message") + "</td>");
                out.println("</tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='3' style='text-align:center;'>No feedback available</td></tr>");
            }
            
            out.println("</table>");

            // ✅ resources close
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("</body></html>");
    }
}
