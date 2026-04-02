import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class addtrains extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // ✅ Back button HTML
        out.println("<a href='trains_admin.html' style='position:absolute; top:20px; right:20px; "
                + "padding:8px 16px; background:#007bff; color:white; text-decoration:none; border-radius:5px; "
                + "font-weight:bold;'>Back to Admin Train Dashboard</a><br><br>");

        String trainId = request.getParameter("train_id");
        String trainName = request.getParameter("train_name");
        String source = request.getParameter("source");
        String destination = request.getParameter("destination");
        String travelDate = request.getParameter("travel_date");
        String price = request.getParameter("price");
        String seats = request.getParameter("seats_available"); // 🆕 seats field

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            // 🔍 Check if train_id already exists
            String checkSql = "SELECT COUNT(*) FROM TRAINS WHERE train_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, trainId);
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                out.println("<h3 style='color:red'>Train ID already exists. Please use a different ID.</h3>");
            } else {
                // ✅ INSERT with seats_available
                String sql = "INSERT INTO trains (train_id, train_name, source, destination, travel_date, price, seats_available) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, trainId);
                ps.setString(2, trainName);
                ps.setString(3, source);
                ps.setString(4, destination);

                java.sql.Date sqlDate = java.sql.Date.valueOf(travelDate); // yyyy-MM-dd format
                ps.setDate(5, sqlDate);

                ps.setDouble(6, Double.parseDouble(price));
                ps.setInt(7, Integer.parseInt(seats)); // 🆕 seats insert

                int i = ps.executeUpdate();
                if (i > 0) {
                    out.println("<h3 style='color:green'>Train Added Successfully!</h3>");
                } else {
                    out.println("<h3 style='color:red'>Failed to Add Train.</h3>");
                }
            }

            con.close();
        } catch(Exception e) {
            out.println("<h3 style='color:red'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
