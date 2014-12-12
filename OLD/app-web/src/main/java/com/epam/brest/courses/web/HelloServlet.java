package com.epam.brest.courses.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by mentee-42 on 5.11.14.
 */
public class HelloServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String parameter = req.getParameter("name");

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        try {
            out.print("Hello ");
            out.print(parameter);
        } finally {
            out.close();
        }
    }
}
