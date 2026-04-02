import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class feedback extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String message = req.getParameter("message");

        if (name==null || name.trim().isEmpty() ||
            email==null || email.trim().isEmpty() ||
            message==null || message.trim().isEmpty()) {
            res.sendRedirect("feedback-fail.html");
            return;
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");
                 PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO feedback_table (id, name, email, message, created_at) VALUES (feedback_seq.NEXTVAL, ?, ?, ?, SYSDATE)")) {

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, message);
                int r = ps.executeUpdate();
                if (r > 0) res.sendRedirect("thankyou.html");
                else res.sendRedirect("feedbackfail.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("feedbackfail.html");
        }
    }
}
