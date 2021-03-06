package com.llisovichok.servlets;

import com.llisovichok.models.User;
import com.llisovichok.storages.HibernateStorage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ALEKSANDR KUDIN on 23.03.2017.
 */
public class EditUserServlet extends HttpServlet {

    //final static UserData USER_DATA = UserData.getInstance();

    //private final static JdbcStorage JDBC_STORAGE = JdbcStorage.getINSTANCE();
    final CreateUserServlet createUserServlet = new CreateUserServlet();

    private static final HibernateStorage HIBERNATE_STORAGE = new HibernateStorage();

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Integer id = Integer.valueOf(req.getParameter("id"));
        User hiberUser = createUserServlet.createUser(req);
        //JDBC_STORAGE.edit(id, user);
        HIBERNATE_STORAGE.edit(id, hiberUser);
        resp.sendRedirect(String.format("%s%s", req.getContextPath(), "/user/view/"));
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.setAttribute("user", JDBC_STORAGE.getUser(Integer.valueOf(req.getParameter("id"))));
        req.setAttribute("user", HIBERNATE_STORAGE.getUser(Integer.valueOf(req.getParameter("id"))));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/user/EditUser.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void destroy(){
        super.destroy();
        //JDBC_STORAGE.close();
    }
}
