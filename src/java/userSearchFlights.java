import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class userSearchFlights extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();

        String fromCity = req.getParameter("fromCity");
        String toCity = req.getParameter("toCity");
        String travelDateStr = req.getParameter("travelDate");

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'><title>Available Flights</title>");

        // ✅ CSS Added directly inside servlet
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background:#f4f4f4; text-align:center; margin:20px; }");
        out.println("h1 { color:#333; margin-bottom:20px; }");
        out.println("table { width:90%; margin:0 auto; border-collapse:collapse; background:white; box-shadow:0px 0px 10px gray; }");
        out.println("th, td { padding:12px; border:1px solid #ddd; text-align:center; }");
        out.println("th { background:#007BFF; color:white; }");
        out.println("tr:nth-child(even) { background:#f9f9f9; }");
        out.println("tr:hover { background:#f1f1f1; }");
        out.println(".book-btn { background:#28a745; color:white; border:none; padding:8px 15px; border-radius:5px; cursor:pointer; }");
        out.println(".book-btn:hover { background:#218838; }");
        out.println(".back-btn { display:inline-block; margin-top:20px; padding:10px 20px; background:#007BFF; color:white; text-decoration:none; border-radius:5px; }");
        out.println(".back-btn:hover { background:#0056b3; }");
        out.println("</style>");

        out.println("</head><body>");
        out.println("<h1>Available Flights</h1>");

        try {
            java.sql.Date travelDate = java.sql.Date.valueOf(travelDateStr);

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String sql = "SELECT flight_id, flight_name, source, destination, travel_date, price, seats_available " +
                         "FROM flights " +
                         "WHERE LOWER(source) = LOWER(?) " +
                         "AND LOWER(destination) = LOWER(?) " +
                         "AND TRUNC(travel_date) = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fromCity);
            ps.setString(2, toCity);
            ps.setDate(3, travelDate);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Flight ID</th>");
            out.println("<th>Flight Name</th>");
            out.println("<th>Source</th>");
            out.println("<th>Destination</th>");
            out.println("<th>Date</th>");
            out.println("<th>Price</th>");
            out.println("<th>Seats</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");

            while (rs.next()) {
                found = true;
                int flightId = rs.getInt("flight_id");
                String fname = rs.getString("flight_name");
                String src = rs.getString("source");
                String dst = rs.getString("destination");
                Date d = rs.getDate("travel_date");
                int price = rs.getInt("price");
                int seats = rs.getInt("seats_available");

                out.println("<tr>");
                out.println("<td>" + flightId + "</td>");
                out.println("<td>" + escapeHtml(fname) + "</td>");
                out.println("<td>" + escapeHtml(src) + "</td>");
                out.println("<td>" + escapeHtml(dst) + "</td>");
                out.println("<td>" + d + "</td>");
                out.println("<td>&#8377;" + String.format("%,d", price) + "</td>");
                out.println("<td>" + seats + "</td>");

                // Book form
                out.println("<td>");
                out.println("<form action='BookFlight' method='post' style='margin:0;'>");
                out.println("<input type='hidden' name='flightId' value='" + flightId + "'/>");
                out.println("<button type='submit' class='book-btn'>Book</button>");
                out.println("</form>");
                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            if (!found) {
                out.println("<p style='color:red;font-weight:bold;'>No flights found!</p>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("<a href='flights.html' class='back-btn'>Back to Search</a>");
        out.println("</body></html>");
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;")
                .replace(">","&gt;").replace("\"","&quot;");
    }
}
