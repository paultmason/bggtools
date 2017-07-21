/*
import java.io.*;
import java.text.DateFormat;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.text.*;
@WebServlet(name = "uploads",urlPatterns = {"/uploads/*"})
@MultipartConfig
*/
public class Uploads{// extends HttpServlet {
/*

    private static final long serialVersionUID = 2857847752169838915L;
    int BUFFER_LENGTH = 4096;

    private File generateUploadFile( Part part,HttpServletRequest request ) throws IOException,ServletException{
        InputStream is = request.getPart(part.getName()).getInputStream();
        String fileName = getFileName(part);
        FileOutputStream os = new FileOutputStream(System.getenv("OPENSHIFT_DATA_DIR") + fileName);
        byte[] bytes = new byte[BUFFER_LENGTH];
        int read = 0;
        while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
            os.write(bytes, 0, read);
        }
        os.flush();
        is.close();
        os.close();
        return new File(System.getenv("OPENSHIFT_DATA_DIR") + fileName);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();//new PrintWriter(new File("test"));//response.getWriter();
        File updateFile = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        out.println("un"+username );
        for (Part part : request.getParts()) {
            if( part.getName().equalsIgnoreCase("uploadFile")){
                updateFile = generateUploadFile(part, request);
            }

        }
        FileReader fr = new FileReader( updateFile);

        BGGUpload bggUpload = new BGGUpload();
        try {
            bggUpload.processFile(username,password, fr);

            out.println(" result:" + bggUpload.getLogger());
            updateFile.delete();
            FileWriter fw = new FileWriter(System.getenv("OPENSHIFT_DATA_DIR") + "useLog.log",true);
            BufferedWriter loggerFile = new BufferedWriter(fw);
            Format formatter = new SimpleDateFormat("yyyy/mm/dd");
            String s = formatter.format(new java.util.Date());
            loggerFile.append(username+" ran the process:" + s );
            out.println(username + " ran the process:" + s);
            loggerFile.close();
  //          downloadFile(response);
        }catch(Exception  e){
            out.println("error processing file:"+e.getMessage());
        }
    }
/*
    private void downloadFile(HttpServletResponse response) throws Exception{
        String filePath = System.getenv("OPENSHIFT_DATA_DIR") + "useLog.log";
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);

        // if you want to use a relative path to context root:
        String relativePath = getServletContext().getRealPath("");
        System.out.println("relativePath = " + relativePath);

        // obtains ServletContext
        ServletContext context = getServletContext();

        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inStream.close();
        outStream.close();

    }
*/
/*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String filePath = request.getRequestURI();

        File file = new File(System.getenv("OPENSHIFT_DATA_DIR") + filePath.replace("/uploads/",""));
        InputStream input = new FileInputStream(file);

        response.setContentLength((int) file.length());
        response.setContentType(new MimetypesFileTypeMap().getContentType(file));

        OutputStream output = response.getOutputStream();
        byte[] bytes = new byte[BUFFER_LENGTH];
        int read = 0;
        while ((read = input.read(bytes, 0, BUFFER_LENGTH)) != -1) {
            output.write(bytes, 0, read);
            output.flush();
        }

        input.close();
        output.close();
    }
*/
    /*
    private String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }
    */
}