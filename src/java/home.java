import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class home extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ UTF-8 encoding set karo
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // ✅ Check session
        HttpSession session = request.getSession(false);
        String userEmail = (session != null) ? (String) session.getAttribute("userEmail") : null;

        if (userEmail == null) {
            response.sendRedirect("login.html");
            return;
        }

        String name = "";
        String phone = "";

        // ✅ DB Connection with try-with-resources
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");
                 PreparedStatement ps = con.prepareStatement(
                         "SELECT name, phone FROM users WHERE email=?")) {

                ps.setString(1, userEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        name = rs.getString("name");
                        phone = rs.getString("phone");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }

        // ✅ HTML Response
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>User Dashboard</title>");
        out.println("<link rel='stylesheet' href='home.css'>");
        out.println("</head>");
        out.println("<body>");

        // Header + Navbar
        out.println("<header>");
        out.println("<h2>✈ Tour & Travel Dashboard</h2>");
        out.println("<nav>");
        out.println("<a href='userflights.html'>Flights</a>");
        out.println("<a href='userbuses.html'>Bus</a>");
        out.println("<a href='usertrains.html'>Train</a>");
        out.println("<a href='userholidays.html'>Holidays</a>");
        out.println("<a href='MyBookings'>My Bookings</a>");
        out.println("<a href='editProfile.html'>Edit Profile</a>");
        out.println("<a href='logout'>Logout</a>");
        out.println("</nav>");
        out.println("</header>");

        // Welcome + Profile Section
        out.println("<div class='container'>");
        out.println("<div class='welcome'>");
        out.println("<h2 id='welcome-text'>Welcome, <span id='username'>" + name + "</span></h2>");
        out.println("<p>Manage your trips and bookings here.</p>");
        out.println("</div>");

        out.println("<div id='profile-section'>");
        out.println("<h3>👤 My Profile</h3>");
        out.println("<p><b>Name:</b> " + name + "</p>");
        out.println("<p><b>Email:</b> " + userEmail + "</p>");
        out.println("<p><b>Phone:</b> " + phone + "</p>");
        out.println("</div>");
        out.println("</div>");

        // Footer
        out.println("<footer>&copy; 2025 Tour & Travel Management | Designed with ❤️</footer>");

        out.println("</body>");
        out.println("</html>");
    }
}
