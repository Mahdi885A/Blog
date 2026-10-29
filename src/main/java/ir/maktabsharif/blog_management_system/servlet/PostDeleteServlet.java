package ir.maktabsharif.blog_management_system.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ir.maktabsharif.blog_management_system.service.PostService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/posts/delete")
public class PostDeleteServlet extends HttpServlet {

    private final PostService postService = new PostService();
    private final Gson gson = new Gson();

    @Override
    protected void doDelete(HttpServletRequest request,
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
            String idParameter = request.getParameter("id");

            if (idParameter == null || idParameter.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "Post ID is required"))
                );
                return;
            }

            Long postId = Long.parseLong(idParameter);
            Long userId = (Long) session.getAttribute("userId");

            boolean deleted = postService.delete(postId, userId);

            if (!deleted) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "Post not found"))
                );
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(
                    gson.toJson(Map.of("message", "Post deleted successfully"))
            );

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    gson.toJson(Map.of("error", "Invalid post ID"))
            );

        } catch (SecurityException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(
                    gson.toJson(Map.of("error", e.getMessage()))
            );
        }
    }
}