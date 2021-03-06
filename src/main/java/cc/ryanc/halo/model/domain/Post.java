package cc.ryanc.halo.model.domain;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     文章／页面
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2017/11/14
 */
@Data
@ToString
@Entity
@Table(name = "halo_post")
@EntityListeners(AuditingEntityListener.class)
public class Post implements Serializable {

    private static final long serialVersionUID = -6019684584665869629L;

    /**
     * 文章编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    /**
     * 发表用户 多对一
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 文章标题
     */
    private String postTitle;

    /**
     * 文章类型
     * post  文章
     * page  页面
     * journal 日志
     */
    private String postType = "post";

    /**
     * 文章内容 Markdown格式
     */
    @Lob
    private String postContentMd;

    /**
     * 文章内容 html格式
     */
    @Lob
    @JsonIgnore
    private String postContent;

    /**
     * 文章路径
     */
    @Column(unique = true)
    private String postUrl;

    /**
     * 文章摘要
     */
    private String postSummary;

    /**
     * 文章所属分类
     */
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "halo_posts_categories",
            joinColumns = {@JoinColumn(name = "post_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "cate_id", nullable = false)})
    private List<Category> categories = new ArrayList<>();

    /**
     * 文章所属标签
     */
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "halo_posts_tags",
            joinColumns = {@JoinColumn(name = "post_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", nullable = false)})
    private List<Tag> tags = new ArrayList<>();

    /**
     * 文章的评论
     */
    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 缩略图
     */
    private String postThumbnail;

    /**
     * 发表日期
     */
    private Date postDate;

    /**
     * 最后一次更新时间
     */
    @LastModifiedDate
    private Date postUpdate;

    /**
     * 0 已发布
     * 1 草稿
     * 2 回收站
     */
    private Integer postStatus = 0;

    /**
     * 文章访问量
     */
    private Long postViews = 0L;

    /**
     * 是否允许评论
     */
    private Integer allowComment = 0;

    /**
     * 文章访问密码
     */
    private String postPassword;

    /**
     * 指定渲染模板
     */
    private String customTpl;

    /**
     * Post priority (default is 0)
     */
    private Integer postPriority;

    /**
     * 发布来源 (default is admin)
     */
    private String postSource;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getPostDate() {
        return postDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getPostUpdate() {
        return postUpdate;
    }

    @PrePersist
    public void prePersist() {
        DateTime now = DateUtil.date();

        if (postDate == null) {
            postDate = now;
        }

        if (postUpdate == null) {
            postUpdate = now;
        }

        if (postPriority == null) {
            postPriority = 0;
        }

        if (postSource == null) {
            postSource = "admin";
        }

        postId = null;
    }
}
