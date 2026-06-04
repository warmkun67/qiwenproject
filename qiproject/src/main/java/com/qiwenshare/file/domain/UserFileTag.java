package com.qiwenshare.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "userfile_tag")
@Entity
@TableName("userfile_tag")
public class UserFileTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20)")
    private Long id;

    @Column(columnDefinition = "varchar(20) comment '用户文件id'")
    private String userFileId;

    @Column(columnDefinition = "bigint(20) comment '标签id'")
    private Long tagId;
}
