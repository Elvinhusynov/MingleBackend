package az.mingle.service;

import az.mingle.dto.PostReactionRequest;

public interface PostReactionService {

    void reactToPost(String username, PostReactionRequest request);
}
