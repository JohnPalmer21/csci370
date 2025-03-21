package edu.cs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*10, maxFileSize=1024*1024*50, maxRequestSize=1024*1024*100)
public class FileUploadServlet extends HttpServlet {
  
    private static final long serialVersionUID = 205242440643911308L;
    private static final String UPLOAD_DIR = "uploads";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
        System.out.println("Upload File Directory=" + fileSaveDir.getAbsolutePath());
        
        String fileName = "";
        for (Part part : request.getParts()) {
            fileName = getFileName(part);
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            part.write(uploadFilePath + File.separator + fileName);
        }

        String message = "Result";
        String content = new Scanner(new File(uploadFilePath + File.separator + fileName)).useDelimiter("\\Z").next();      
        response.getWriter().write(message + "<BR>" + content);
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }
}
