package az.mingle.mapper;

import az.mingle.dto.PostRequest;
import az.mingle.dto.PostResponse;
import az.mingle.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostRequest postRequest);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.username", target = "username")
    PostResponse toDto(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updatePostFromRequest(PostRequest postRequest, @MappingTarget Post post);
}
