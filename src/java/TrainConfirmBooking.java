import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.*;

@WebServlet("/TrainConfirmBooking")
public class TrainConfirmBooking extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Request parameters (⚡ names fixed to match Payment servlet)
        String passengerName   = request.getParameter("passenger_name");
        String passengerEmail  = request.getParameter("passenger_email");
        String passengerPhone  = request.getParameter("passenger_phone");
        String trainId        = request.getParameter("trainId");
        String seats           = request.getParameter("num_passengers");
        String amount          = request.getParameter("total_amount");
        String paymentMethod   = request.getParameter("paymentMethod"); 
        String userTxnId       = request.getParameter("transaction_id");

        // Debugging
        System.out.println("DEBUG: trainId=" + trainId + ", payment=" + paymentMethod + ", txn=" + userTxnId);

        // Fake Unique Transaction ID
        String sysTxnId = "TXN-" + (int)(Math.random() * 100000);

        int trainIdInt = safeInt(trainId);
        int seatsInt    = safeInt(seats);
        int amountInt   = safeInt(amount);

        try {
            // DB connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "system", "manager");

            String sql = "INSERT INTO train_bookings " +
                         "(BOOKING_ID, TRAIN_ID, PASSENGER_NAME, PASSENGER_EMAIL, PASSENGER_PHONE, " +
                         "NUM_PASSENGERS, TOTAL_AMOUNT, PAYMENT_STATUS, PAYMENT_METHOD, TRANSACTION_ID, BOOKED_AT) " +
                         "VALUES (BOOKINGS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, trainIdInt);
            ps.setString(2, safeStr(passengerName));
            ps.setString(3, safeStr(passengerEmail));
            ps.setString(4, safeStr(passengerPhone));
            ps.setInt(5, seatsInt);
            ps.setInt(6, amountInt);
            ps.setString(7, "SUCCESS");
            ps.setString(8, safeStr(paymentMethod));
            ps.setString(9, sysTxnId + " / " + safeStr(userTxnId));

            ps.executeUpdate();

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace(out);
        }

        // ✅ HTML Response
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Payment Success</title>");
        out.println("<style>");
        out.println("body {font-family: Arial; background:#f5f6fa; display:flex; justify-content:center; align-items:center; height:100vh;}");
        out.println(".card {background:#fff; padding:30px; border-radius:12px; box-shadow:0 4px 10px rgba(0,0,0,0.1); width:400px;}");
        out.println("h2 {color:green;} p{margin:8px 0;} button{background:#007bff;color:white;border:none;padding:10px 15px;border-radius:5px;cursor:pointer;} button:hover{background:#0056b3;}");
        out.println("</style></head><body>");

        out.println("<div class='card'>");
        out.println("<h2> Payment Successful</h2>");
        out.println("<p><b>System Transaction ID:</b> " + sysTxnId + "</p>");
        out.println("<p><b>User Reference ID:</b> " + safeStr(userTxnId) + "</p>");
        out.println("<p><b>Passenger:</b> " + safeStr(passengerName) + "</p>");
        out.println("<p><b>Email:</b> " + safeStr(passengerEmail) + "</p>");
        out.println("<p><b>Phone:</b> " + safeStr(passengerPhone) + "</p>");
        out.println("<p><b>Train ID:</b> " + trainIdInt + "</p>");
        out.println("<p><b>Seats:</b> " + seatsInt + "</p>");
        out.println("<p><b>Paid Amount:</b> ₹" + amountInt + "</p>");
        out.println("<p><b>Payment Method:</b> " + safeStr(paymentMethod) + "</p>");
        out.println("<br><form action='home'><button type='submit'>Back to Dashboard</button></form>");
        out.println("</div>");

        out.println("</body></html>");
    }

    // ✅ Safe parsers
    private int safeInt(String s) {
        try { return (s != null && !s.isEmpty()) ? Integer.parseInt(s) : 0; }
        catch (Exception e) { return 0; }
    }
    private String safeStr(String s) {
        return (s != null) ? s : "";
    }
}
