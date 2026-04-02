import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AllFlights extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<html><head><title>All Flights</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; }");
        out.println("table { width: 100%; border-collapse: collapse; margin: 10px auto; }");
        out.println("th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }");
        out.println("th { background-color: #007bff; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println(".delete-btn { background:#dc3545; color:white; border:none; padding:5px 10px; border-radius:4px; cursor:pointer; }");
        out.println(".delete-btn:hover { background:#a71d2a; }");
        out.println("</style>");
        out.println("<script>");
        out.println("function confirmDelete(flightId) {");
        out.println("  if (confirm('Are you sure you want to delete Flight ID: ' + flightId + '?')) {");
        out.println("    window.location.href = 'AllFlights?delete=' + flightId;");
        out.println("  }");
        out.println("}");
        out.println("</script>");
        out.println("</head><body>");

        // ✅ If delete request present
        String deleteFlight = req.getParameter("delete");
        if (deleteFlight != null) {
            deleteFlightById(deleteFlight, out);
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "manager");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT flight_id, flight_name, source, destination, travel_date, price, seats_available FROM flights");

            out.println("<table>");
            out.println("<tr><th>Flight ID</th><th>Name</th><th>Source</th><th>Destination</th><th>Date</th><th>Price</th><th>Seats</th><th>Action</th></tr>");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String id = rs.getString("flight_id");
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + rs.getString("flight_name") + "</td>");
                out.println("<td>" + rs.getString("source") + "</td>");
                out.println("<td>" + rs.getString("destination") + "</td>");
                out.println("<td>" + rs.getDate("travel_date") + "</td>");
                out.println("<td>" + rs.getDouble("price") + "</td>");
                out.println("<td>" + rs.getInt("seats_available") + "</td>");
                out.println("<td><button class='delete-btn' onclick=\"confirmDelete('" + id + "')\">Delete</button></td>");
                out.println("</tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='8'>No flights available</td></tr>");
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

    private void deleteFlightById(String flightId, PrintWriter out) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "manager");

            PreparedStatement ps = con.prepareStatement("DELETE FROM flights WHERE flight_id = ?");
            ps.setString(1, flightId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<p style='color:green; text-align:center;'>Flight " + flightId + " deleted successfully!</p>");
            } else {
                out.println("<p style='color:orange; text-align:center;'>Flight not found: " + flightId + "</p>");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error deleting flight: " + e.getMessage() + "</p>");
        }
    }
}
