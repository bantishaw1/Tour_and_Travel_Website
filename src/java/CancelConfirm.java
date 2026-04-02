import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CancelConfirm extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String type = req.getParameter("type");
        String id = req.getParameter("id");

        if(type == null || id == null) {
            res.sendRedirect("MyBookings?msg=Invalid+Request");
            return;
        }

        out.println("<!DOCTYPE html><html><head><title>Confirm Cancellation</title>");
        out.println("<style>");
        out.println("body{font-family:Arial;text-align:center;padding-top:100px;}");
        out.println(".btn{padding:12px 25px;margin:10px;font-size:16px;border:none;border-radius:8px;cursor:pointer}");
        out.println(".yes{background-color:#dc3545;color:#fff;}");
        out.println(".no{background-color:#6c757d;color:#fff;}");
        out.println("</style></head><body>");

        out.println("<h2>Are you sure you want to cancel this booking?</h2>");

        // Yes button → Call CancelBooking
        out.println("<a href='CancelBooking?type=" + type + "&id=" + id + "'><button class='btn yes'>Yes</button></a>");
        // No button → Go back to MyBookings
        out.println("<a href='MyBookings'><button class='btn no'>No</button></a>");

        out.println("</body></html>");
    }
}
