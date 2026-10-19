package ir.maktabsharif.blog_management_system.servlet;


import ir.maktabsharif.blog_management_system.dto.PostResponseDTO;
import ir.maktabsharif.blog_management_system.service.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/posts")
public class PostListServlet extends HttpServlet {

    private final PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try {
            List<PostResponseDTO> posts = postService.findAll();

            response.setStatus(HttpServletResponse.SC_OK);

            for (PostResponseDTO post : posts) {
                response.getWriter().println(
                        "ID: " + post.getId()
                                + ", Title: " + post.getTitle()
                                + ", Author: " + post.getAuthorUsername()
                );
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Could not retrieve posts");
        }
    }
}
