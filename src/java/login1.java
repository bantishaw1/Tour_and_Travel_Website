import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class login1 extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();
        String uid = req.getParameter("l1");
        String pass = req.getParameter("l2");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");
            Statement stmt = con.createStatement();
            String q1 = "select * from users where email='" + uid + "' and password='" + pass + "'";
            ResultSet rs = stmt.executeQuery(q1);

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("userEmail", uid);

                res.sendRedirect("home");
            } else {
                res.sendRedirect("loginfail.html");
            }

            con.close();
        } catch (Exception e) {
            pw1.println(e);
        }
    }
}
