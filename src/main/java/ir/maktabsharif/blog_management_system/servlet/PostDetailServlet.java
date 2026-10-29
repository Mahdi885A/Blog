package ir.maktabsharif.blog_management_system.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import ir.maktabsharif.blog_management_system.dto.PostResponseDTO;
import ir.maktabsharif.blog_management_system.service.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@WebServlet("/post")
public class PostDetailServlet extends HttpServlet {

    private final PostService postService = new PostService();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (com.google.gson.JsonSerializer<LocalDateTime>)
                            (src, type, context) ->
                                    new JsonPrimitive(src.toString()))
            .create();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null || idParameter.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "Post ID is required"))
                );
                return;
            }

            Long id = Long.parseLong(idParameter);
            PostResponseDTO post = postService.findById(id);

            if (post == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(
                        gson.toJson(Map.of("error", "Post not found"))
                );
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(post));

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    gson.toJson(Map.of("error", "Invalid post ID"))
            );
        }
    }
}
