/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.asridt.controller;

import ch.asridt.bean.FileRecordBean;
import ch.asridt.record.RecordGroup;
import ch.asridt.record.RecordItem;
import ch.asridt.record.RecordOrder;
import ch.asridt.record.RecordQualifier;
import ch.asridt.record.RecordType;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pvr
 */
@WebServlet(name = "RecordController", urlPatterns = {"/RecordController"})
public class RecordController extends HttpServlet {

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
        // define string that holds the address
        String address;
        // get the real path for directory "fxrecord" using the context
        ServletContext context = getServletContext();
        String realPath = context.getRealPath("fxrecord");
        // create FileRecordModel using an instance of FileRecordBean
        FileRecordBean fmm = new FileRecordBean(realPath);
        // add instance of RecordQualifier
        RecordQualifier mq;
        // get RecordQualifier from session
        HttpSession session = request.getSession();
        if (session.isNew()){
            mq = fmm.getQualifier();
            session.setAttribute("qualifierBean", mq);
        }
        // assign the qualifier from the session attribute
        mq = (RecordQualifier) session.getAttribute("qualifierBean");
        // in case the session timed out, we get it from fmm again
        if (mq == null){
            mq = fmm.getQualifier();
            session.setAttribute("qualifierBean", mq);
        }
        // handle the different actions
        String action = request.getParameter("action");
        if (action == null){
            action = "manager";
        }
        // switch on the action
        switch (action){
            case "manager":
                fmm.loadData(mq);                
                request.setAttribute("fileBean", fmm);
                address = "/WEB-INF/manager.jsp";
                break;
            case "item":
                String itemId = request.getParameter("itemId");
                RecordItem item = fmm.getRecordItem(itemId);
                request.setAttribute("itemBean", item);
                address = "/WEB-INF/record.jsp";
                break;
            case "settings":
                address = "WEB-INF/settings.jsp";
                break;
            case "newSettings":
                String type = request.getParameter("type");
                String sortOrder = request.getParameter("sortOrder");
                // set the recordqualifiers based on type
                if (type != null){
                    switch (type) {
                        case "my":
                            mq.setTypes(RecordType.MY);
                            break;
                        case "fi":
                            mq.setTypes(RecordType.FI);
                            break;
                        default:
                            mq.setTypes(RecordType.MY, RecordType.FI);
                            break;
                    }
                }
                // set sort order
                if (sortOrder != null){
                    mq.setSortOrder(RecordOrder.valueOf(sortOrder));
                }
                // load data with updated mq
                fmm.loadData(mq);
                request.setAttribute("fileBean", fmm);
                address = "/WEB-INF/manager.jsp";
                break;
            default:
                request.setAttribute("error", "Unknown action!");
                address = "/WEB-INF/error.jsp";
                break;
        }
        // forward request via dispatcher to jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
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
        processRequest(request, response);
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
