import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AllBuses extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>All Buses Details</title>");
        out.println("<link rel='stylesheet' href='flights_admin.css'>");
        out.println("<style>");
        out.println("table { width:100%; border-collapse: collapse; margin: 20px auto; }");
        out.println("th, td { border: 1px solid #ccc; padding: 10px; text-align: center; }");
        out.println("th { background-color: #007bff; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println(".delete-btn { background:#dc3545; color:white; border:none; padding:5px 10px; border-radius:4px; cursor:pointer; }");
        out.println(".delete-btn:hover { background:#a71d2a; }");
        out.println("</style>");
        out.println("<script>");
        out.println("function confirmDelete(busId) {");
        out.println("  if (confirm('Are you sure you want to delete Bus ID: ' + busId + '?')) {");
        out.println("    window.location.href = 'AllBuses?delete=' + busId;");
        out.println("  }");
        out.println("}");
        out.println("</script>");
        out.println("</head><body>");

        // ✅ Handle delete request
        String deleteBus = req.getParameter("delete");
        if (deleteBus != null) {
            deleteBusById(deleteBus, out);
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM BUSES ORDER BY BUS_ID");

            out.println("<table>");
            out.println("<thead><tr>");
            out.println("<th>Bus ID</th><th>Bus Name</th><th>Source</th><th>Destination</th>");
            out.println("<th>Travel Date</th><th>Price</th><th>Seats Available</th><th>Action</th>");
            out.println("</tr></thead>");
            out.println("<tbody>");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int busId = rs.getInt("BUS_ID");

                out.println("<tr>");
                out.println("<td>" + busId + "</td>");
                out.println("<td>" + rs.getString("BUS_NAME") + "</td>");
                out.println("<td>" + rs.getString("SOURCE") + "</td>");
                out.println("<td>" + rs.getString("DESTINATION") + "</td>");
                out.println("<td>" + rs.getDate("TRAVEL_DATE") + "</td>");
                out.println("<td>" + rs.getInt("PRICE") + "</td>");
                out.println("<td>" + rs.getInt("SEATS_AVAILABLE") + "</td>");
                out.println("<td><button class='delete-btn' onclick=\"confirmDelete('" + busId + "')\">Delete</button></td>");
                out.println("</tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='8'>No buses available</td></tr>");
            }

            out.println("</tbody></table>");
            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("</body></html>");
    }

    // ✅ Helper method to delete bus
    private void deleteBusById(String busId, PrintWriter out) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            PreparedStatement ps = con.prepareStatement("DELETE FROM BUSES WHERE BUS_ID = ?");
            ps.setString(1, busId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<p style='color:green; text-align:center;'>Bus " + busId + " deleted successfully!</p>");
            } else {
                out.println("<p style='color:orange; text-align:center;'>No Bus found with ID: " + busId + "</p>");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red; text-align:center;'>Error deleting bus: " + e.getMessage() + "</p>");
        }
    }
}
