package az.mingle.mapper;

import az.mingle.dto.FollowDto;
import az.mingle.entity.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FollowMapper {

    @Mappings({
            @Mapping(source = "follower.userId", target = "followerId"),
            @Mapping(source = "followed.userId", target = "followedId")
    })
    FollowDto toDto(Follow follow);

    @Mappings({
            @Mapping(source = "followerId", target = "follower.userId"),
            @Mapping(source = "followedId", target = "followed.userId")
    })
    Follow toEntity(FollowDto followDto);
}

