package ir.maktabsharif.blog_management_system.servlet;

import ir.maktabsharif.blog_management_system.dto.UserRegisterDTO;
import ir.maktabsharif.blog_management_system.service.PostService;
import ir.maktabsharif.blog_management_system.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername(name);
        userRegisterDTO.setEmail(email);
        userRegisterDTO.setPassword(password);

        PrintWriter out = response.getWriter();

        try {
            userService.register(userRegisterDTO);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.println("Registration successful");
        }
        catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
        }

    }

}
