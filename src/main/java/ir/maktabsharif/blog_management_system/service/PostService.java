package ir.maktabsharif.blog_management_system.service;

import ir.maktabsharif.blog_management_system.dto.PostCreateDTO;
import ir.maktabsharif.blog_management_system.dto.PostResponseDTO;
import ir.maktabsharif.blog_management_system.model.Post;
import ir.maktabsharif.blog_management_system.model.User;
import ir.maktabsharif.blog_management_system.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

public class PostService {

    private final PostRepository postRepository = new PostRepository();

    public void create(PostCreateDTO dto, User user) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()
                || dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Title and content are required");
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);
    }

    public void update(Long postId, PostCreateDTO dto, Long userId) {
        Post post = postRepository.findById(postId);

        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot edit another user's post");
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()
                || dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Title and content are required");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        postRepository.update(post);
    }

    public boolean delete(Long postId, Long userId) {
        Post post = postRepository.findById(postId);

        if (post == null) {
            return false;
        }

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("You cannot delete another user's post");
        }

        return postRepository.delete(postId);
    }

    public PostResponseDTO findById(Long id) {
        Post post = postRepository.findById(id);

        if (post == null) {
            return null;
        }

        return toDTO(post);
    }

    public List<PostResponseDTO> findAll() {
        return postRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private PostResponseDTO toDTO(Post post) {
        return PostResponseDTO.builder()
                .id(Long.valueOf(post.getId()))
                .title(post.getTitle())
                .content(post.getContent())
                .authorUsername(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
