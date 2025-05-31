package az.mingle.service;

import az.mingle.dto.PostReactionRequest;
import az.mingle.entity.Post;
import az.mingle.entity.PostReaction;
import az.mingle.entity.User;
import az.mingle.exception.ResourceNotFoundException;
import az.mingle.repository.PostReactionRepository;
import az.mingle.repository.PostRepository;
import az.mingle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {
    private final PostReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void reactToPost(String username, PostReactionRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Optional<PostReaction> existing = reactionRepository.findByUserIdAndPostId(user.getUserId(), post.getPostId());

        if (existing.isPresent()) {
            PostReaction current = existing.get();
            if (current.getType() == request.getType()) {
                // Eyni reaction varsa - sil
                reactionRepository.delete(current);
                return;
            } else {
                // Əvvəl LIKE idisə, indi DISLIKE olacaq (və ya əksi)
                current.setType(request.getType());
                current.setReactedAt(LocalDateTime.now());
                reactionRepository.save(current);
                return;
            }
        }

        // Əgər əvvəlcədən reaction yoxdursa - yeni əlavə et
        PostReaction reaction = new PostReaction();
        reaction.setUser(user);
        reaction.setPost(post);
        reaction.setType(request.getType());
        reaction.setReactedAt(LocalDateTime.now());
        reactionRepository.save(reaction);
    }
}

