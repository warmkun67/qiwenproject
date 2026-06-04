package com.qiwenshare.file.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Schema(name = "文件标签DTO", required = true)
public class FileTagDTO {

    @NotBlank(message = "文件id不能为空")
    @Schema(description = "用户文件id", example = "123456789")
    private String userFileId;

    @Schema(description = "标签id列表", example = "[1, 2, 3]")
    private List<Long> tagIds;
}
