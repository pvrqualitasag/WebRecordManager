/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.asridt.controller;

import ch.asridt.bean.FileRecordBean;
import ch.asridt.record.RecordItem;
import ch.asridt.record.RecordManager;
import ch.asridt.model.UploadData;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author pvr
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig(location = "c:/tmp/RecordManager")
public class UploadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UploadServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // context and path of fxrecord directory
        ServletContext context = getServletContext();
        String realPath = context.getRealPath("fxrecord");
        // get an instance of the record manager
        FileRecordBean fmm = new FileRecordBean(realPath);
        RecordManager mm = fmm.getRecordManager();
        // start to create the response
        // define the address to point to an error by default
        String address = "/WEB-INF/error.jsp";
        // check for multipart form data
        Collection<Part> parts = request.getParts();
        if (parts.isEmpty()){
            request.setAttribute("error", "Not a multipart/form-data request");
        } else {
            for (Part part : parts){
                String name = part.getSubmittedFileName();
                try {
                    int periodIndex = name.lastIndexOf(".");
                    String fileExtension = name.substring(periodIndex);
                    if (fileExtension.equals(".my") || fileExtension.equals(".fi")){
                        String title = name.substring(0, periodIndex);
                        RecordItem item = new RecordItem(title, name, new Date());
                        mm.createRecordItem(item, part.getInputStream());
                        // upload data which will be shown after uploading a file is defined here
                        UploadData data = new UploadData(name, title, fileExtension);
                        request.setAttribute("uploadData", data);
                        address = "/WEB-INF/uploadData.jsp";
                    } else {
                        request.setAttribute("error", fileExtension + " not supported");
                    }
                    part.delete();
                } catch (IOException fe) {
                    request.setAttribute("error", "Exception writing file: " + name + " : " + fe);
                }                               
            }
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
