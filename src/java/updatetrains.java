import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class updatetrains extends HttpServlet {

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

            // Build dynamic update query
            StringBuilder sql = new StringBuilder("UPDATE TRAINS SET ");
            boolean comma = false;

            if(trainName != null && !trainName.isEmpty()) {
                sql.append("train_name=?");
                comma = true;
            }
            if(source != null && !source.isEmpty()) {
                if(comma) sql.append(", ");
                sql.append("source=?");
                comma = true;
            }
            if(destination != null && !destination.isEmpty()) {
                if(comma) sql.append(", ");
                sql.append("destination=?");
                comma = true;
            }
            if(travelDate != null && !travelDate.isEmpty()) {
                if(comma) sql.append(", ");
                sql.append("travel_date=?");
                comma = true;
            }
            if(price != null && !price.isEmpty()) {
                if(comma) sql.append(", ");
                sql.append("price=?");
                comma = true;
            }
            if(seats != null && !seats.isEmpty()) {   // 🆕 add seats
                if(comma) sql.append(", ");
                sql.append("seats_available=?");
            }

            sql.append(" WHERE train_id=?");

            PreparedStatement ps = con.prepareStatement(sql.toString());

            int idx = 1;
            if(trainName != null && !trainName.isEmpty()) ps.setString(idx++, trainName);
            if(source != null && !source.isEmpty()) ps.setString(idx++, source);
            if(destination != null && !destination.isEmpty()) ps.setString(idx++, destination);
            if(travelDate != null && !travelDate.isEmpty()) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(travelDate);
                ps.setDate(idx++, sqlDate);
            }
            if(price != null && !price.isEmpty()) ps.setDouble(idx++, Double.parseDouble(price));
            if(seats != null && !seats.isEmpty()) ps.setInt(idx++, Integer.parseInt(seats)); // 🆕 set seats

            ps.setString(idx, trainId); // WHERE condition

            int i = ps.executeUpdate();
            if(i > 0) {
                out.println("<h3 style='color:green'>Train Updated Successfully!</h3>");
            } else {
                out.println("<h3 style='color:red'>No Train Found with ID: " + trainId + "</h3>");
            }

            con.close();
        } catch(Exception e) {
            out.println("<h3 style='color:red'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        }
    }
}
