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

public class Search extends HttpServlet {

    private Redis redis = new Redis();
    private Mysql mysql = new Mysql();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("name");
        System.out.println("Search user with name:" + name);
        try{

            String fromRedis = redis.Get(name);
            if (fromRedis != null) {
                response.getOutputStream().write(("Find (" + fromRedis + ") from redis").getBytes());
                System.out.println("Found (" + fromRedis + ") from db");
                return;
            }

            User fromDb = mysql.getUser(name);
            if (fromDb != null) {
                response.getOutputStream().write(("Find (" + fromDb + ") from db").getBytes());
                System.out.println("Update redis with data from db :" + fromDb.toString());
                redis.Update(fromDb.getName(), fromDb.toString());
                return;
            }

            response.getOutputStream().write(("Not found.").getBytes());
         }
         catch(Exception e){
             System.err.println(e);
         }finally {
             response.getOutputStream().close();
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


}
