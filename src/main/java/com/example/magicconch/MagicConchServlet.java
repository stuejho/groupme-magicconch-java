package com.example.magicconch;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for the MagicConchServlet class.
 *
 * @author Ethan Ho
 */
@WebServlet("/")
public class MagicConchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public MagicConchServlet() {
        super();
    }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

}