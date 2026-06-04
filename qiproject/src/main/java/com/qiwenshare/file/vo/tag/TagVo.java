package com.qiwenshare.file.vo.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "标签Vo", required = true)
public class TagVo {

    @Schema(description = "标签id", example = "1")
    private Long tagId;

    @Schema(description = "用户id", example = "1")
    private String userId;

    @Schema(description = "标签名称", example = "重要文件")
    private String tagName;

    @Schema(description = "标签颜色", example = "#409EFF")
    private String tagColor;

    @Schema(description = "创建时间", example = "2020-10-10 12:21:22")
    private String createTime;
}
