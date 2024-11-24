package controller;

import data.FurnitureDB;
import business.Furniture;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.beans.Customizer;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FurnitureServlet", value = "/furnitureServlet")
public class FurnitureServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String idParam = request.getParameter("furnitureId");
        if (idParam == null || idParam.equals("")) {
            response.sendRedirect(request.getHeader("Referer"));
        }
        else{
            int id = Integer.parseInt(idParam);
            Furniture furniture = FurnitureDB.getFurnitureByID(id);
            List<Furniture> listFurniture = FurnitureDB.getFurnitureInCategory(furniture);
            request.setAttribute("listFurniture", listFurniture);
            request.setAttribute("furniture", furniture);
            context.getRequestDispatcher("/furniture.jsp").forward(request, response);
        }
    }
}