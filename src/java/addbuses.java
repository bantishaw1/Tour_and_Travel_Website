import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class addbuses extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        // ✅ Back button HTML
        out.println("<a href='buses_admin.html' style='position:absolute; top:20px; right:20px; "
                + "padding:8px 16px; background:#007bff; color:white; text-decoration:none; border-radius:5px; "
                + "font-weight:bold;'>Back to Admin Bus Dashboard</a><br><br>");

        String busId = request.getParameter("bus_id");
        String busName = request.getParameter("bus_name");
        String source = request.getParameter("source");
        String destination = request.getParameter("destination");
        String travelDate = request.getParameter("travel_date");
        String price = request.getParameter("price");
        String seats = request.getParameter("seats_available"); // 🆕 seats field

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            // 🔍 Check if flight_id already exists
            String checkSql = "SELECT COUNT(*) FROM buses WHERE bus_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, busId);
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                out.println("<h3 style='color:red'>Bus ID already exists. Please use a different ID.</h3>");
            } else {
                // ✅ INSERT with seats_available
                String sql = "INSERT INTO buses(bus_id, bus_name, source, destination, travel_date, price, seats_available) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, busId);
                ps.setString(2, busName);
                ps.setString(3, source);
                ps.setString(4, destination);

                java.sql.Date sqlDate = java.sql.Date.valueOf(travelDate); // yyyy-MM-dd format hona chahiye
                ps.setDate(5, sqlDate);

                ps.setDouble(6, Double.parseDouble(price));
                ps.setInt(7, Integer.parseInt(seats)); // 🆕 seats insert

                int i = ps.executeUpdate();
                if (i > 0) {
                    out.println("<h3 style='color:green'>Bus Added Successfully!</h3>");
                } else {
                    out.println("<h3 style='color:red'>Failed to Add Bus.</h3>");
                }
            }

            con.close();
        } catch(Exception e) {
            out.println("<h3 style='color:red'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
