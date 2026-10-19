package ir.maktabsharif.blog_management_system.servlet;

import jakarta.servlet.http.HttpServlet;

import ir.maktabsharif.blog_management_system.dto.UserLoginDTO;
import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail(email);
        dto.setPassword(password);

        PrintWriter out = response.getWriter();

        try {
            User user = userService.login(dto);

            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());

            response.setStatus(HttpServletResponse.SC_OK);
            out.println("Login successful");

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("Invalid email or password");
        }
    }
}


