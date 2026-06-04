package com.qiwenshare.file.controller;

import com.qiwenshare.common.anno.MyLog;
import com.qiwenshare.common.result.RestResult;
import com.qiwenshare.common.util.security.JwtUser;
import com.qiwenshare.common.util.security.SessionUtil;
import com.qiwenshare.file.api.ITagService;
import com.qiwenshare.file.domain.Tag;
import com.qiwenshare.file.dto.tag.CreateTagDTO;
import com.qiwenshare.file.dto.tag.FileTagDTO;
import com.qiwenshare.file.dto.tag.UpdateTagDTO;
import com.qiwenshare.file.vo.tag.TagVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@io.swagger.v3.oas.annotations.tags.Tag(name = "tag", description = "该接口主要是对文件标签进行管理")
@RestController
@Slf4j
@RequestMapping("/tag")
public class TagController {

    @Resource
    ITagService tagService;

    public static final String CURRENT_MODULE = "文件标签接口";

    @Operation(summary = "获取用户所有标签", description = "获取当前用户创建的所有标签列表", tags = {"tag"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<TagVo> getTagList() {
        JwtUser sessionUserBean = SessionUtil.getSession();
        List<Tag> tags = tagService.getUserTags(sessionUserBean.getUserId());
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags) {
            TagVo vo = new TagVo();
            vo.setTagId(tag.getTagId());
            vo.setUserId(tag.getUserId());
            vo.setTagName(tag.getTagName());
            vo.setTagColor(tag.getTagColor());
            vo.setCreateTime(tag.getCreateTime());
            tagVos.add(vo);
        }
        return RestResult.success().dataList(tagVos, tagVos.size());
    }

    @Operation(summary = "创建标签", description = "创建新的文件标签", tags = {"tag"})
    @MyLog(operation = "创建标签", module = CURRENT_MODULE)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<TagVo> createTag(@Valid @RequestBody CreateTagDTO createTagDTO) {
        JwtUser sessionUserBean = SessionUtil.getSession();
        Tag tag = new Tag();
        tag.setUserId(sessionUserBean.getUserId());
        tag.setTagName(createTagDTO.getTagName());
        tag.setTagColor(createTagDTO.getTagColor() != null ? createTagDTO.getTagColor() : "#409EFF");
        tag = tagService.createTag(tag);
        TagVo vo = convertToVo(tag);
        return RestResult.success().data(vo);
    }

    @Operation(summary = "更新标签", description = "修改标签名称或颜色", tags = {"tag"})
    @MyLog(operation = "更新标签", module = CURRENT_MODULE)
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public RestResult<TagVo> updateTag(@Valid @RequestBody UpdateTagDTO updateTagDTO) {
        JwtUser sessionUserBean = SessionUtil.getSession();
        Tag tag = new Tag();
        tag.setTagId(updateTagDTO.getTagId());
        tag.setUserId(sessionUserBean.getUserId());
        tag.setTagName(updateTagDTO.getTagName());
        tag.setTagColor(updateTagDTO.getTagColor());
        tag = tagService.updateTag(tag);
        TagVo vo = convertToVo(tag);
        return RestResult.success().data(vo);
    }

    @Operation(summary = "删除标签", description = "删除标签并解除所有文件关联", tags = {"tag"})
    @MyLog(operation = "删除标签", module = CURRENT_MODULE)
    @RequestMapping(value = "/delete/{tagId}", method = RequestMethod.DELETE)
    @ResponseBody
    public RestResult<String> deleteTag(@PathVariable Long tagId) {
        JwtUser sessionUserBean = SessionUtil.getSession();
        tagService.deleteTag(tagId, sessionUserBean.getUserId());
        return RestResult.success().data("删除成功");
    }

    @Operation(summary = "获取文件的所有标签", description = "获取某个文件被标记的所有标签", tags = {"tag"})
    @RequestMapping(value = "/file/{userFileId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<TagVo> getFileTags(@PathVariable String userFileId) {
        List<Tag> tags = tagService.getTagsByUserFileId(userFileId);
        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags) {
            tagVos.add(convertToVo(tag));
        }
        return RestResult.success().dataList(tagVos, tagVos.size());
    }

    @Operation(summary = "给文件添加标签", description = "给指定文件添加一个标签", tags = {"tag"})
    @MyLog(operation = "给文件添加标签", module = CURRENT_MODULE)
    @RequestMapping(value = "/addToFile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> addTagToFile(@Valid @RequestBody FileTagDTO fileTagDTO) {
        if (fileTagDTO.getTagIds() == null || fileTagDTO.getTagIds().isEmpty()) {
            return RestResult.fail().message("标签id列表不能为空");
        }
        for (Long tagId : fileTagDTO.getTagIds()) {
            tagService.addTagToFile(fileTagDTO.getUserFileId(), tagId);
        }
        return RestResult.success().data("添加成功");
    }

    @Operation(summary = "从文件移除标签", description = "从指定文件移除一个标签", tags = {"tag"})
    @MyLog(operation = "从文件移除标签", module = CURRENT_MODULE)
    @RequestMapping(value = "/removeFromFile", method = RequestMethod.DELETE)
    @ResponseBody
    public RestResult<String> removeTagFromFile(@RequestParam String userFileId, @RequestParam Long tagId) {
        tagService.removeTagFromFile(userFileId, tagId);
        return RestResult.success().data("移除成功");
    }

    @Operation(summary = "设置文件标签", description = "批量设置文件的标签列表（覆盖式）", tags = {"tag"})
    @MyLog(operation = "设置文件标签", module = CURRENT_MODULE)
    @RequestMapping(value = "/setFileTags", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> setFileTags(@Valid @RequestBody FileTagDTO fileTagDTO) {
        tagService.setFileTags(fileTagDTO.getUserFileId(), fileTagDTO.getTagIds());
        return RestResult.success().data("设置成功");
    }

    @Operation(summary = "批量获取文件标签", description = "根据多个文件id批量获取标签", tags = {"tag"})
    @RequestMapping(value = "/batch", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<Map<String, List<TagVo>>> getBatchTags(@RequestParam String userFileIds) {
        String[] fileIdArray = userFileIds.split(",");
        List<String> fileIdList = java.util.Arrays.asList(fileIdArray);
        Map<String, List<Tag>> tagsMap = tagService.getTagsByUserFileIds(fileIdList);
        Map<String, List<TagVo>> result = new java.util.HashMap<>();
        for (Map.Entry<String, List<Tag>> entry : tagsMap.entrySet()) {
            List<TagVo> tagVos = new ArrayList<>();
            for (Tag tag : entry.getValue()) {
                tagVos.add(convertToVo(tag));
            }
            result.put(entry.getKey(), tagVos);
        }
        return RestResult.success().data(result);
    }

    private TagVo convertToVo(Tag tag) {
        TagVo vo = new TagVo();
        vo.setTagId(tag.getTagId());
        vo.setUserId(tag.getUserId());
        vo.setTagName(tag.getTagName());
        vo.setTagColor(tag.getTagColor());
        vo.setCreateTime(tag.getCreateTime());
        return vo;
    }
}
