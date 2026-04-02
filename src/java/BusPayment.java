import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/BusPayment")
public class BusPayment extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Parameters fetch karo
        String busId       = request.getParameter("busId");
        String name        = request.getParameter("passenger_name");
        String email       = request.getParameter("passenger_email");
        String phone       = request.getParameter("passenger_phone");
        int pricePerSeat   = safeInt(request.getParameter("pricePerSeat"), 0);
        int numPassengers  = safeInt(request.getParameter("num_passengers"), 1);
        int totalAmount    = pricePerSeat * numPassengers;

        // HTML start
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Bus Payment</title>");
        out.println("<style>");
        out.println("body{font-family:Arial,sans-serif;background:#f5f6f8;margin:0;text-align:center}");
        out.println(".container{width:500px;margin:30px auto;background:#fff;padding:20px;border-radius:10px;box-shadow:0 5px 15px rgba(0,0,0,0.2)}");
        out.println("h1{color:#222}");
        out.println(".summary{background:#e7f3ff;padding:10px;border-radius:8px;margin-bottom:15px}");
        out.println("label{display:block;margin-top:10px;font-weight:bold}");
        out.println("input,select{width:100%;padding:8px;margin-top:5px;border-radius:5px;border:1px solid #ccc}");
        out.println(".btn{margin-top:15px;padding:10px;width:100%;background:#198754;color:#fff;border:none;border-radius:5px;cursor:pointer}");
        out.println(".btn:hover{background:#157347}");
        out.println(".back-btn{margin-top:10px;padding:10px;width:100%;background:#6c757d;color:#fff;border:none;border-radius:5px;cursor:pointer}");
        out.println(".back-btn:hover{background:#5a6268}");
        out.println("</style></head><body>");
        out.println("<div class='container'>");
        out.println("<h1>Complete Your Payment</h1>");

        // Order summary
        out.println("<div class='summary'>");
        out.println("<p><b>Passenger:</b> " + esc(name) + "</p>");
        out.println("<p><b>Bus ID:</b> " + esc(busId) + "</p>");
        out.println("<p><b>Seats:</b> " + numPassengers + "</p>");
        out.println("<p><b>Total Amount:</b> ₹" + String.format("%,d", totalAmount) + "</p>");
        out.println("</div>");

        // Payment form
        out.println("<form action='BusConfirmBooking' method='post'>");
        // Hidden fields
        out.println("<input type='hidden' name='busId' value='" + esc(busId) + "'>");
        out.println("<input type='hidden' name='passenger_name' value='" + esc(name) + "'>");
        out.println("<input type='hidden' name='passenger_email' value='" + esc(email) + "'>");
        out.println("<input type='hidden' name='passenger_phone' value='" + esc(phone) + "'>");
        out.println("<input type='hidden' name='num_passengers' value='" + numPassengers + "'>");
        out.println("<input type='hidden' name='total_amount' value='" + totalAmount + "'>");

        out.println("<label>Payment Method</label>");
        out.println("<select name='paymentMethod' required>");
        out.println("<option value='UPI'>UPI</option>");
        out.println("<option value='CARD'>Card</option>");
        out.println("<option value='NETBANKING'>Net Banking</option>");
        out.println("</select>");

        out.println("<label>Transaction ID / UPI / Card No</label>");
        out.println("<input type='text' name='transaction_id' placeholder='Enter transaction ID' required>");

        out.println("<button type='submit' class='btn'>Pay Now</button>");
        // ✅ Back button
        out.println("<button type='button' class='back-btn' onclick='history.back()'>Back</button>");

        out.println("</form>");
        out.println("</div></body></html>");
    }

    private int safeInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
    }
}
