/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fei.registration;

import com.fei.model.User;
import com.fei.repo.Mysql;
import com.fei.repo.Redis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Register extends HttpServlet {


    private Redis redis = new Redis();
    private Mysql mysql = new Mysql();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        User user = new User();

        user.setName(request.getParameter("userName"));
        user.setEmail(request.getParameter("userEmail"));
        user.setCountry(request.getParameter("userCountry"));
        System.out.println("Update User :" + user.getName());

        try {
            String fromRedis = redis.Get(user.getName());
            if (fromRedis != null) {
                response.getOutputStream().write(("User with name " + user.getName() + " exist in redis:" + fromRedis).getBytes());
                return;
            }

            User fromDb = mysql.getUser(user.getName());
            if (fromDb != null) {
                response.getOutputStream().write(("User with name " + user.getName() + " exist in db:" + fromDb).getBytes());
                System.out.println("Update redis with data from db :" + user.toString());
                redis.Update(user.getName(), fromDb.toString());
                return;
            }
            mysql.addUser(user);

            redis.Update(user.getName(), user.toString());

            response.getOutputStream().write("Add success".getBytes());

            System.out.println("Update redis with new user :" + user.toString());

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            response.getOutputStream().close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
