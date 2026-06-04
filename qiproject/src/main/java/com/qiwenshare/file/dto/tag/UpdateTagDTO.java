package com.qiwenshare.file.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(name = "更新标签DTO", required = true)
public class UpdateTagDTO {

    @NotNull(message = "标签id不能为空")
    @Schema(description = "标签id", example = "1")
    private Long tagId;

    @NotBlank(message = "标签名称不能为空")
    @Schema(description = "标签名称", example = "重要文件")
    private String tagName;

    @Schema(description = "标签颜色", example = "#409EFF")
    private String tagColor;
}
