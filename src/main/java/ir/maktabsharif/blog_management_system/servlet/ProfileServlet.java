package ir.maktabsharif.blog_management_system.servlet;

import com.google.gson.Gson;
import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/profile")
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    gson.toJson(Map.of("error", "Please log in first"))
            );
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = userService.findById(userId);

        if (user == null) {
            session.invalidate();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    gson.toJson(Map.of("error", "User not found"))
            );
            return;
        }

        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("id", user.getId());
        profile.put("username", user.getUsername());
        profile.put("email", user.getEmail());

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(profile));
    }
}