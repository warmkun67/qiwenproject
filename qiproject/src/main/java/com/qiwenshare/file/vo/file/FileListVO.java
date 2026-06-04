package com.qiwenshare.file.vo.file;

import com.qiwenshare.file.domain.Music;
import lombok.Data;

@Data
public class FileListVO {
    private String fileId;

    private String timeStampName;

    private String fileUrl;

    private Long fileSize;

    private Integer storageType;

    private Integer pointCount;

    private String identifier;

    private String userFileId;

    private Long userId;


    private String fileName;

    private String filePath;

    private String extendName;

    private Integer isDir;

    private String uploadTime;

    private Integer deleteFlag;

    private String deleteTime;

    private String deleteBatchNum;

    private Integer imageWidth;
    private Integer imageHeight;

    private Integer isFavorite;

    /**
     * 文件标签列表，格式：tagId:tagName:tagColor|tagId:tagName:tagColor
     */
    private String tagList;

}
