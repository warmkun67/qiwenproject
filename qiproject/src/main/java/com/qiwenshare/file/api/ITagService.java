package com.qiwenshare.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qiwenshare.file.domain.Tag;

import java.util.List;
import java.util.Map;

public interface ITagService extends IService<Tag> {

    List<Tag> getUserTags(String userId);

    Tag createTag(Tag tag);

    Tag updateTag(Tag tag);

    void deleteTag(Long tagId, String userId);

    List<Tag> getTagsByUserFileId(String userFileId);

    Map<String, List<Tag>> getTagsByUserFileIds(List<String> userFileIds);

    void addTagToFile(String userFileId, Long tagId);

    void removeTagFromFile(String userFileId, Long tagId);

    void setFileTags(String userFileId, List<Long> tagIds);
}
