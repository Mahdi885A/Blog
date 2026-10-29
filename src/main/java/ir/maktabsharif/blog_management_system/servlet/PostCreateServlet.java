package ir.maktabsharif.blog_management_system.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ir.maktabsharif.blog_management_system.dto.PostCreateDTO;
import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.service.PostService;
import ir.maktabsharif.blog_management_system.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/posts/create")
public class PostCreateServlet extends HttpServlet {

    private final PostService postService = new PostService();
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request,
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

        try {
            Long userId = (Long) session.getAttribute("userId");
            User author = userService.findById(userId);

            if (author == null) {
                session.invalidate();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "User no longer exists"))
                );
                return;
            }

            PostCreateDTO dto = gson.fromJson(
                    request.getReader(),
                    PostCreateDTO.class
            );

            if (dto == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "Request body is required"))
                );
                return;
            }

            postService.create(dto, author);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(
                    gson.toJson(Map.of("message", "Post created successfully"))
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
