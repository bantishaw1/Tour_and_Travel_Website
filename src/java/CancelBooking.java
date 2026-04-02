import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CancelBooking extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        String type = req.getParameter("type"); // flight/bus/train
        String id = req.getParameter("id");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE","system","manager");

            String table = "";
            if(type.equals("flight")) table = "BOOKINGS";
            else if(type.equals("bus")) table = "BUS_BOOKINGS";
            else if(type.equals("train")) table = "TRAIN_BOOKINGS";

            PreparedStatement ps = con.prepareStatement("DELETE FROM " + table + " WHERE BOOKING_ID=?");
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        res.sendRedirect("MyBookings");
    }
}
