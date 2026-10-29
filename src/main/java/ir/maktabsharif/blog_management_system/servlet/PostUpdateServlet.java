package ir.maktabsharif.blog_management_system.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ir.maktabsharif.blog_management_system.dto.PostCreateDTO;
import ir.maktabsharif.blog_management_system.service.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/posts/update")
public class PostUpdateServlet extends HttpServlet {

    private final PostService postService = new PostService();
    private final Gson gson = new Gson();

    @Override
    protected void doPut(HttpServletRequest request,
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

            postService.update(postId, dto, userId);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(
                    gson.toJson(Map.of("message", "Post updated successfully"))
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