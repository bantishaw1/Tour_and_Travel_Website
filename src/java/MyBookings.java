import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class MyBookings extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        HttpSession session = req.getSession(false);
        if(session == null || session.getAttribute("userEmail") == null) {
            res.sendRedirect("login.html");
            return;
        }

        String email = (String) session.getAttribute("userEmail");

        out.println("<!DOCTYPE html><html><head><title>My Bookings</title>");
        out.println("<link rel='stylesheet' href='mybookings.css'>");
        out.println("<style>");
        // Inline CSS for top-right dashboard button
        out.println(".top-right { position: absolute; top: 20px; right: 20px; }");
        out.println(".dashboard-btn { background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold; }");
        out.println(".dashboard-btn:hover { background-color: #45a049; }");
        out.println("</style>");
        out.println("</head><body>");
        
        // Top-right dashboard button
        out.println("<div class='top-right'><a class='dashboard-btn' href='home'>Dashboard</a></div>");
        
        out.println("<h1>My Bookings</h1>");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE","system","manager");

            // -----------------------
            // Flights
            PreparedStatement psFlight = con.prepareStatement(
                "SELECT b.BOOKING_ID, f.FLIGHT_NAME, b.NUM_PASSENGERS, b.TOTAL_AMOUNT, b.PAYMENT_STATUS, b.BOOKED_AT " +
                "FROM BOOKINGS b JOIN FLIGHTS f ON b.FLIGHT_ID=f.FLIGHT_ID " +
                "WHERE b.PASSENGER_EMAIL=?");
            psFlight.setString(1, email);
            ResultSet rsFlight = psFlight.executeQuery();

            out.println("<h2>Flight Bookings</h2>");
            out.println("<table><tr><th>ID</th><th>Flight</th><th>Passengers</th><th>Amount</th><th>Status</th><th>Booked At</th><th>Action</th></tr>");
            while(rsFlight.next()) {
                int bookingId = rsFlight.getInt("BOOKING_ID");
                String flightName = rsFlight.getString("FLIGHT_NAME");
                int num = rsFlight.getInt("NUM_PASSENGERS");
                int amt = rsFlight.getInt("TOTAL_AMOUNT");
                String status = rsFlight.getString("PAYMENT_STATUS");
                String bookedAt = rsFlight.getDate("BOOKED_AT").toString();

                out.println("<tr>");
                out.println("<td>" + bookingId + "</td>");
                out.println("<td>" + flightName + "</td>");
                out.println("<td>" + num + "</td>");
                out.println("<td>" + amt + "</td>");
                out.println("<td>" + status + "</td>");
                out.println("<td>" + bookedAt + "</td>");
                out.println("<td><a href='CancelConfirm?type=flight&id=" + bookingId + "'>Cancel</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");

            // -----------------------
            // Bus
            PreparedStatement psBus = con.prepareStatement(
                "SELECT b.BOOKING_ID, bs.BUS_NAME, b.NUM_PASSENGERS, b.TOTAL_AMOUNT, b.PAYMENT_STATUS, b.BOOKED_AT " +
                "FROM BUS_BOOKINGS b JOIN BUSES bs ON b.BUS_ID=bs.BUS_ID " +
                "WHERE b.PASSENGER_EMAIL=?");
            psBus.setString(1, email);
            ResultSet rsBus = psBus.executeQuery();

            out.println("<h2>Bus Bookings</h2>");
            out.println("<table><tr><th>ID</th><th>Bus</th><th>Passengers</th><th>Amount</th><th>Status</th><th>Booked At</th><th>Action</th></tr>");
            while(rsBus.next()) {
                int bookingId = rsBus.getInt("BOOKING_ID");
                String busName = rsBus.getString("BUS_NAME");
                int num = rsBus.getInt("NUM_PASSENGERS");
                int amt = rsBus.getInt("TOTAL_AMOUNT");
                String status = rsBus.getString("PAYMENT_STATUS");
                String bookedAt = rsBus.getDate("BOOKED_AT").toString();

                out.println("<tr>");
                out.println("<td>" + bookingId + "</td>");
                out.println("<td>" + busName + "</td>");
                out.println("<td>" + num + "</td>");
                out.println("<td>" + amt + "</td>");
                out.println("<td>" + status + "</td>");
                out.println("<td>" + bookedAt + "</td>");
                out.println("<td><a href='CancelConfirm?type=bus&id=" + bookingId + "'>Cancel</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");

            // -----------------------
            // Train
            PreparedStatement psTrain = con.prepareStatement(
                "SELECT t.BOOKING_ID, tr.TRAIN_NAME, t.NUM_PASSENGERS, t.TOTAL_AMOUNT, t.PAYMENT_STATUS, t.BOOKED_AT " +
                "FROM TRAIN_BOOKINGS t JOIN TRAINS tr ON t.TRAIN_ID=tr.TRAIN_ID " +
                "WHERE t.PASSENGER_EMAIL=?");
            psTrain.setString(1, email);
            ResultSet rsTrain = psTrain.executeQuery();

            out.println("<h2>Train Bookings</h2>");
            out.println("<table><tr><th>ID</th><th>Train</th><th>Passengers</th><th>Amount</th><th>Status</th><th>Booked At</th><th>Action</th></tr>");
            while(rsTrain.next()) {
                int bookingId = rsTrain.getInt("BOOKING_ID");
                String trainName = rsTrain.getString("TRAIN_NAME");
                int num = rsTrain.getInt("NUM_PASSENGERS");
                int amt = rsTrain.getInt("TOTAL_AMOUNT");
                String status = rsTrain.getString("PAYMENT_STATUS");
                String bookedAt = rsTrain.getDate("BOOKED_AT").toString();

                out.println("<tr>");
                out.println("<td>" + bookingId + "</td>");
                out.println("<td>" + trainName + "</td>");
                out.println("<td>" + num + "</td>");
                out.println("<td>" + amt + "</td>");
                out.println("<td>" + status + "</td>");
                out.println("<td>" + bookedAt + "</td>");
                out.println("<td><a href='CancelConfirm?type=train&id=" + bookingId + "'>Cancel</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");

            con.close();
        } catch(Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("</body></html>");
    }
}
