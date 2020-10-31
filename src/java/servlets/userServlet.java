/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataaccess.UserDB;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Role;
import models.User;
import services.UserService;

/**
 *
 * @author 798676
 */
public class userServlet extends HttpServlet {

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
        UserService service = new UserService();
        ArrayList<User> userList;
        ArrayList<Role> roles;

        try {
            userList = service.getAllUsers();
            roles = service.getAllRoles();
            request.setAttribute("userList", userList);
            request.setAttribute("roles", roles);
        } catch (Exception ex) {
            Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
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
        UserService service = new UserService();

        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "Delete":
                    break;
                case "editSelect":
                    break;
                default:

                    String email = request.getParameter("email");
                    Boolean active = Boolean.parseBoolean(request.getParameter("active"));
                    String firstName = request.getParameter("firstname");
                    String lastName = request.getParameter("lastname");
                    String password = request.getParameter("password");
                    int role = Integer.parseInt(request.getParameter("role"));//change role to string in the User class 
                    if (validateFields(email, active, firstName, lastName, password)) {

                    }
                    switch (action) {
                        case "Add": {
                            try {
                                service.addUser(email, active, firstName, lastName, password, role);
                            } catch (Exception ex) {
                                Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
                                User user = new User(email, firstName, lastName, password, role, active);
                                request.setAttribute("newUser", user);
                            }
                        }
                        break;
                        case "Edit":
                            try {
                                service.updateUser(email, active, firstName, lastName, password, role);
                            } catch (Exception ex) {
                                Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
                                User user = new User(email, firstName, lastName, password, role, active);
                                request.setAttribute("editUser", user);
                            }
                            break;
                    }

                    break;
            }
        }

        ArrayList<User> userList = new ArrayList();
        try {
            userList = service.getAllUsers();
        } catch (Exception ex) {
            Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("userList", userList);

        getServletContext().getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);

    }

    private boolean validateFields(String email, Boolean active, String firstName, String lastName, String password) {

        if (email != null && !email.equals("") && firstName != null && !firstName.equals("") && password != null && !password.equals("")) {
            return true;
        }
        return false;

    }

}
