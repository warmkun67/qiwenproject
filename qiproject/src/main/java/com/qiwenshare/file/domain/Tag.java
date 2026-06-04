package com.qiwenshare.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "tag")
@Entity
@TableName("tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20)")
    private Long tagId;

    @Column(columnDefinition = "varchar(20) comment '用户id'")
    private String userId;

    @Column(columnDefinition = "varchar(50) comment '标签名称'")
    private String tagName;

    @Column(columnDefinition = "varchar(20) comment '标签颜色'")
    private String tagColor;

    @Column(columnDefinition = "varchar(25) comment '创建时间'")
    private String createTime;
}
