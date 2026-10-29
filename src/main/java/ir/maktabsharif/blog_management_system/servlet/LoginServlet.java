package ir.maktabsharif.blog_management_system.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ir.maktabsharif.blog_management_system.dto.UserLoginDTO;
import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            UserLoginDTO dto = gson.fromJson(
                    request.getReader(),
                    UserLoginDTO.class
            );

            if (dto == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "Request body is required"))
                );
                return;
            }

            User user = userService.login(dto);

            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(
                    gson.toJson(Map.of("message", "Login successful"))
            );

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    gson.toJson(Map.of("error", "Invalid email or password"))
            );

        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    gson.toJson(Map.of("error", "Invalid JSON"))
            );
        }
    }
}
