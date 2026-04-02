import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/TrainPayment")
public class TrainPayment extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        String trainId     = req.getParameter("trainId");
        String name        = req.getParameter("passenger_name");
        String email       = req.getParameter("passenger_email");
        String phone       = req.getParameter("passenger_phone");
        int pricePerSeat   = safeInt(req.getParameter("pricePerSeat"), 0);
        int numPassengers  = safeInt(req.getParameter("num_passengers"), 1);
        int total          = pricePerSeat * numPassengers;

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Payment Page</title>");
        out.println("<style>");
        out.println("body{font-family:Arial,sans-serif;background:#f5f6f8;margin:0;text-align:center}");
        out.println("h1{margin:24px 0;color:#222}");
        out.println(".wrap{width:520px;margin:20px auto;background:#fff;border-radius:12px;box-shadow:0 8px 18px rgba(0,0,0,.12);padding:22px;text-align:left}");
        out.println(".row{margin-bottom:14px} label{font-weight:600;color:#333;display:block;margin-bottom:6px}");
        out.println("input,select{width:100%;padding:10px;border:1px solid #dcdcdc;border-radius:8px;box-sizing:border-box}");
        out.println(".summary{background:#f1f7ff;border:1px solid #cde0ff;border-radius:8px;padding:12px;margin-bottom:14px}");
        out.println(".btn{width:100%;padding:12px;border:none;border-radius:8px;background:#198754;color:#fff;font-size:16px;cursor:pointer}");
        out.println(".btn:hover{background:#157347}");
        out.println(".back-btn{width:100%;padding:12px;margin-top:10px;border:none;border-radius:8px;background:#6c757d;color:#fff;font-size:16px;cursor:pointer}");
        out.println(".back-btn:hover{background:#5a6268}");
        out.println("</style></head><body>");
        out.println("<h1>Complete Your Payment</h1>");

        out.println("<div class='wrap'>");

        // order summary
        out.println("<div class='summary'>");
        out.println("<div><b>Passenger:</b> " + esc(name) + "</div>");
        out.println("<div><b>Train ID:</b> " + esc(trainId) + "</div>");
        out.println("<div><b>Seats:</b> " + numPassengers + "</div>");
        out.println("<div><b>Amount:</b> ₹" + String.format("%,d", total) + "</div>");
        out.println("</div>");

        out.println("<form action='TrainConfirmBooking' method='post'>");
        // hidden fields to carry forward
        out.println("<input type='hidden' name='trainId' value='" + esc(trainId) + "'/>");
        out.println("<input type='hidden' name='passenger_name' value='" + esc(name) + "'/>");
        out.println("<input type='hidden' name='passenger_email' value='" + esc(email) + "'/>");
        out.println("<input type='hidden' name='passenger_phone' value='" + esc(phone) + "'/>");
        out.println("<input type='hidden' name='num_passengers' value='" + numPassengers + "'/>");
        out.println("<input type='hidden' name='total_amount' value='" + total + "'/>");

        out.println("<div class='row'><label>Payment Method</label>");
        out.println("<select name='paymentMethod' required>");
        out.println("<option value='UPI'>UPI</option>");
        out.println("<option value='CARD'>Card</option>");
        out.println("<option value='NETBANKING'>Net Banking</option>");
        out.println("</select></div>");

        out.println("<div class='row'><label>Card/UPI/Bank Ref</label>");
        out.println("<input type='text' name='transaction_id' placeholder='example: 98xxxxxx@ybl or 4111111111111111' required></div>");

        out.println("<button class='btn' type='submit'>Pay Now</button>");
        // ✅ Back button
        out.println("<button type='button' class='back-btn' onclick='history.back()'>Back</button>");

        out.println("</form>");

        out.println("</div></body></html>");
    }

    private int safeInt(String s, int def){
        try { return Integer.parseInt(s); } catch(Exception e){ return def; }
    }
    private String esc(String s){
        if(s==null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
    }
}
