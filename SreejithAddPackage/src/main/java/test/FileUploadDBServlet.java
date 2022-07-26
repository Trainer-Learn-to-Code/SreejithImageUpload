package test;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
 
@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class FileUploadDBServlet extends HttpServlet {
     
	/*
	 * // database connection settings private String dbURL =
	 * "jdbc:mysql://192.168.18.245:3306/javadbtest"; private String dbUser =
	 * "javadbtest"; private String dbPass = "GFW5sdwfxt";
	 */
     
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // gets values of text fields
        String packagename = request.getParameter("packagename");
        String packagetype = request.getParameter("packagetype");
		String location =request.getParameter("location"); 
		  String price=request.getParameter("price");
		 
		
		 String feature =request.getParameter("feature");
		 String details =request.getParameter("details");
		 
		 //String packageimage =request.getParameter("packageimage");
		         
         
        InputStream inputStream = null; // input stream of the upload file
         
        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("packageimage");
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
         
        Connection conn = null; // connection to the database
        String message = null;  // message will be sent back to client
         
        try {
            // connects to the database
Class.forName("com.mysql.jdbc.Driver");

conn = DriverManager.getConnection("jdbc:mysql://192.168.18.245:3306/javadbtest","javadbtest","GFW5sdwfxt");
 
            // constructs SQL statement
String sql = "INSERT INTO package (packagename, packagetype, location,price,feature, details, packageimage ) values (?,?,?,?, ?, ?, ?)";

          //  String sql = "INSERT INTO package (packagename, packagetype, location, price, feature, details, packageimage  ) values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, packagename);
            statement.setString(2, packagetype);
            statement.setString(3, location);
            statement.setString(4, price);


		statement.setString(5, feature); 
	statement.setString(6, details);

			/*
			 * statement.setInt(4, price); statement.setString(5, feature);
			 * statement.setString(6, details);
			 */
			// statement.setString(7, packageimage);
		


            
             
            if (inputStream != null) {
                // fetches input stream of the upload file for the blob column
                statement.setBlob(7, inputStream);
            }
 
            // sends the statement to the database server
            int row = statement.executeUpdate();
            if (row > 0) {
                message = "File uploaded and saved into database";
            }
        } catch (Exception ex) {
            message = "ERROR: " + ex.getMessage();
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                // closes the database connection
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            // sets the message in request scope
            request.setAttribute("Message", message);
             
            // forwards to the message page
            getServletContext().getRequestDispatcher("/Message.jsp").forward(request, response);
        }
    }
}