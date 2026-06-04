package com.qiwenshare.file.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "创建标签DTO", required = true)
public class CreateTagDTO {

    @NotBlank(message = "标签名称不能为空")
    @Schema(description = "标签名称", example = "重要文件")
    private String tagName;

    @Schema(description = "标签颜色", example = "#409EFF")
    private String tagColor;
}
