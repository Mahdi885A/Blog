package ir.maktabsharif.blog_management_system.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ir.maktabsharif.blog_management_system.dto.UserRegisterDTO;
import ir.maktabsharif.blog_management_system.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            UserRegisterDTO dto = gson.fromJson(
                    request.getReader(),
                    UserRegisterDTO.class
            );

            if (dto == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "Request body is required"))
                );
                return;
            }

            userService.register(dto);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(
                    gson.toJson(Map.of("message", "Registration successful"))
            );

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    gson.toJson(Map.of("error", e.getMessage()))
            );

        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    gson.toJson(Map.of("error", "Invalid JSON"))
            );
        }
    }
}

