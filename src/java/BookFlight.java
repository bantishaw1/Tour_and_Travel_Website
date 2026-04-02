import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/BookFlight")
public class BookFlight extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String flightId = request.getParameter("flightId");

        // ✅ Get user email from session
        HttpSession session = request.getSession(false);
        String userEmail = (session != null) ? (String) session.getAttribute("userEmail") : "";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "manager");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM flights WHERE flight_id=?");
            ps.setString(1, flightId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int price = rs.getInt("price");
                int seats = rs.getInt("seats_available");

                out.println("<!DOCTYPE html>");
                out.println("<html><head><meta charset='UTF-8'><title>Passenger Details</title>");
                out.println("<style>");
                out.println("body{font-family:Arial,sans-serif;background:#f5f6f8;text-align:center;margin:0}");
                out.println("h2{margin:20px 0;color:#333}");
                out.println(".box{margin:30px auto;padding:20px;width:380px;background:#fff;border-radius:10px;box-shadow:0 6px 16px rgba(0,0,0,.12);text-align:left}");
                out.println("label{display:block;margin:10px 0 6px;font-weight:600;color:#444}");
                out.println("input{width:100%;padding:10px;border:1px solid #dcdcdc;border-radius:8px;box-sizing:border-box;margin-bottom:12px}");
                out.println("input[readonly]{background:#e9ecef;}");
                out.println("button{width:100%;padding:12px;border:none;border-radius:8px;background:#0d6efd;color:#fff;font-size:16px;cursor:pointer}");
                out.println("button:hover{background:#0b5ed7}");
                out.println(".back-btn{background:#6c757d;margin-top:10px}");
                out.println("</style>");
                out.println("</head><body>");
                out.println("<h2>Enter Passenger Details</h2>");

                out.println("<form action='Payment' method='post' class='box'>");
                out.println("<input type='hidden' name='flightId' value='" + rs.getInt("flight_id") + "'/>");
                out.println("<input type='hidden' name='pricePerSeat' value='" + price + "'/>");

                out.println("<label>Passenger Name</label><input type='text' name='passenger_name' required>");
                out.println("<label>Email</label><input type='email' name='passenger_email' value='" + userEmail + "' readonly>");
                out.println("<label>Phone</label><input type='text' name='passenger_phone' required>");
                out.println("<label>Number of Passengers</label><input type='number' name='num_passengers' min='1' max='" + seats + "' required>");

                out.println("<p><b>Price per seat:</b> ₹" + String.format("%,d", price) + "</p>");
                out.println("<button type='submit'>Proceed to Payment</button>");
                out.println("<button type='button' class='back-btn' onclick='history.back()'>Back</button>");

                out.println("</form>");
                out.println("</body></html>");
            } else {
                out.println("<h3 style='color:red;'>Flight not found!</h3>");
            }

            rs.close(); 
            ps.close(); 
            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
}
