package com.qiwenshare.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiwenshare.common.util.DateUtil;
import com.qiwenshare.file.api.ITagService;
import com.qiwenshare.file.domain.Tag;
import com.qiwenshare.file.domain.UserFileTag;
import com.qiwenshare.file.mapper.TagMapper;
import com.qiwenshare.file.mapper.UserFileTagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Resource
    TagMapper tagMapper;

    @Resource
    UserFileTagMapper userFileTagMapper;

    @Override
    public List<Tag> getUserTags(String userId) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getUserId, userId);
        queryWrapper.orderByAsc(Tag::getCreateTime);
        return tagMapper.selectList(queryWrapper);
    }

    @Override
    public Tag createTag(Tag tag) {
        tag.setCreateTime(DateUtil.getCurrentTime());
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public Tag updateTag(Tag tag) {
        tagMapper.updateById(tag);
        return tagMapper.selectById(tag.getTagId());
    }

    @Override
    public void deleteTag(Long tagId, String userId) {
        // Verify the tag belongs to the user
        Tag tag = tagMapper.selectById(tagId);
        if (tag == null || !tag.getUserId().equals(userId)) {
            return;
        }
        // Remove all file-tag associations
        LambdaQueryWrapper<UserFileTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFileTag::getTagId, tagId);
        userFileTagMapper.delete(queryWrapper);
        // Delete the tag
        tagMapper.deleteById(tagId);
    }

    @Override
    public List<Tag> getTagsByUserFileId(String userFileId) {
        // Query tag IDs associated with the file
        LambdaQueryWrapper<UserFileTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFileTag::getUserFileId, userFileId);
        List<Long> tagIds = userFileTagMapper.selectList(queryWrapper)
                .stream()
                .map(UserFileTag::getTagId)
                .collect(Collectors.toList());

        if (tagIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        return tagMapper.selectBatchIds(tagIds);
    }

    @Override
    public Map<String, List<Tag>> getTagsByUserFileIds(List<String> userFileIds) {
        if (userFileIds == null || userFileIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        // Batch query all tag associations for given file IDs
        LambdaQueryWrapper<UserFileTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserFileTag::getUserFileId, userFileIds);
        List<UserFileTag> userFileTags = userFileTagMapper.selectList(queryWrapper);

        if (userFileTags.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        // Collect all unique tag IDs
        List<Long> tagIds = userFileTags.stream()
                .map(UserFileTag::getTagId)
                .distinct()
                .collect(Collectors.toList());

        // Batch query all tags
        List<Tag> allTags = tagMapper.selectBatchIds(tagIds);
        Map<Long, Tag> tagMap = allTags.stream()
                .collect(Collectors.toMap(Tag::getTagId, t -> t));

        // Build result: Map<userFileId, List<Tag>>
        Map<String, List<Tag>> result = new java.util.HashMap<>();
        for (UserFileTag uft : userFileTags) {
            Tag tag = tagMap.get(uft.getTagId());
            if (tag != null) {
                result.computeIfAbsent(uft.getUserFileId(), k -> new java.util.ArrayList<>()).add(tag);
            }
        }

        return result;
    }

    @Override
    public void addTagToFile(String userFileId, Long tagId) {
        // Check if already associated
        LambdaQueryWrapper<UserFileTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFileTag::getUserFileId, userFileId);
        queryWrapper.eq(UserFileTag::getTagId, tagId);
        if (userFileTagMapper.selectCount(queryWrapper) > 0) {
            return;
        }
        UserFileTag userFileTag = new UserFileTag();
        userFileTag.setUserFileId(userFileId);
        userFileTag.setTagId(tagId);
        userFileTagMapper.insert(userFileTag);
    }

    @Override
    public void removeTagFromFile(String userFileId, Long tagId) {
        LambdaQueryWrapper<UserFileTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFileTag::getUserFileId, userFileId);
        queryWrapper.eq(UserFileTag::getTagId, tagId);
        userFileTagMapper.delete(queryWrapper);
    }

    @Override
    public void setFileTags(String userFileId, List<Long> tagIds) {
        // Remove all existing tag associations
        LambdaQueryWrapper<UserFileTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFileTag::getUserFileId, userFileId);
        userFileTagMapper.delete(queryWrapper);

        // Add new tag associations
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                UserFileTag userFileTag = new UserFileTag();
                userFileTag.setUserFileId(userFileId);
                userFileTag.setTagId(tagId);
                userFileTagMapper.insert(userFileTag);
            }
        }
    }
}
