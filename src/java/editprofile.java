import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class editprofile extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("userEmail"); // login ke time set kiya hoga
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "system", "manager");

            PreparedStatement ps = con.prepareStatement("UPDATE users SET name=?, phone=? WHERE email=?");
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);

            int i = ps.executeUpdate();
            if(i > 0){
                out.println("<html><body style='font-family:Arial; text-align:center; margin-top:50px;'>");
                out.println("<h3 style='color:green;'>Profile Updated Successfully!</h3>");
                out.println("<form action='home' method='get'>");
                out.println("<button type='submit' style='padding:10px 20px; background:#4CAF50; color:white; border:none; border-radius:6px; cursor:pointer;'>Back to Dashboard</button>");
                out.println("</form>");
                out.println("</body></html>");
            } else {
                out.println("<h3 style='color:red;'>Error updating profile!</h3>");
            }

            con.close();
        } catch(Exception e) {
            e.printStackTrace(out);
        }
    }
}
