package com.qiwenshare.file.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "收藏文件DTO", required = true)
public class FavoriteFileDTO {

    @NotBlank(message = "文件id不能为空")
    @Schema(description = "用户文件id", example = "123456789")
    private String userFileId;
}
