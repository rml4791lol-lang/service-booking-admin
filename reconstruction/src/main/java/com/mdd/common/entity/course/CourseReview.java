/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8bfe\u7a0b\u8bc4\u4ef7\u5b9e\u4f53")
public class CourseReview
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="\u4e3b\u952eID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer courseClassId;
    @ApiModelProperty(value="\u8bfe\u8282\u5e8f\u53f7")
    private Integer lessonNo;
    @ApiModelProperty(value="\u6559\u7ec3ID")
    private Integer instructorId;
    @ApiModelProperty(value="\u6559\u5b66\u8d28\u91cf\u8bc4\u5206(1-5)")
    private Integer ratingTeaching;
    @ApiModelProperty(value="\u8bfe\u5802\u6c1b\u56f4\u8bc4\u5206(1-5)")
    private Integer ratingAtmosphere;
    @ApiModelProperty(value="\u6559\u7ec3\u6001\u5ea6\u8bc4\u5206(1-5)")
    private Integer ratingAttitude;
    @ApiModelProperty(value="\u8bc4\u4ef7\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u8bc4\u4ef7\u56fe\u7247(\u591a\u5f20\u9017\u53f7\u5206\u9694)")
    private String images;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Integer updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Integer deleteTime;
    @ApiModelProperty(value="\u63a8\u8350\u6b64\u8bfe\u7a0b\u6570")
    private Integer recommendCount;
    @ApiModelProperty(value="\u7231\u5fc3\u70b9\u8d5e\u6570")
    private Integer likeCount;
    @ApiModelProperty(value="\u6559\u7ec3\u56de\u590d\u5185\u5bb9")
    private String replyContent;
    @ApiModelProperty(value="\u6559\u7ec3\u56de\u590d\u65f6\u95f4")
    private Integer replyTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getCourseClassId() {
        return this.courseClassId;
    }

    public Integer getLessonNo() {
        return this.lessonNo;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public Integer getRatingTeaching() {
        return this.ratingTeaching;
    }

    public Integer getRatingAtmosphere() {
        return this.ratingAtmosphere;
    }

    public Integer getRatingAttitude() {
        return this.ratingAttitude;
    }

    public String getContent() {
        return this.content;
    }

    public String getImages() {
        return this.images;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public Integer getUpdateTime() {
        return this.updateTime;
    }

    public Integer getDeleteTime() {
        return this.deleteTime;
    }

    public Integer getRecommendCount() {
        return this.recommendCount;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public String getReplyContent() {
        return this.replyContent;
    }

    public Integer getReplyTime() {
        return this.replyTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setRatingTeaching(Integer ratingTeaching) {
        this.ratingTeaching = ratingTeaching;
    }

    public void setRatingAtmosphere(Integer ratingAtmosphere) {
        this.ratingAtmosphere = ratingAtmosphere;
    }

    public void setRatingAttitude(Integer ratingAttitude) {
        this.ratingAttitude = ratingAttitude;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }

    public void setRecommendCount(Integer recommendCount) {
        this.recommendCount = recommendCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setReplyTime(Integer replyTime) {
        this.replyTime = replyTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseReview)) {
            return false;
        }
        CourseReview other = (CourseReview)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$courseClassId = this.getCourseClassId();
        Integer other$courseClassId = other.getCourseClassId();
        if (this$courseClassId == null ? other$courseClassId != null : !((Object)this$courseClassId).equals(other$courseClassId)) {
            return false;
        }
        Integer this$lessonNo = this.getLessonNo();
        Integer other$lessonNo = other.getLessonNo();
        if (this$lessonNo == null ? other$lessonNo != null : !((Object)this$lessonNo).equals(other$lessonNo)) {
            return false;
        }
        Integer this$instructorId = this.getInstructorId();
        Integer other$instructorId = other.getInstructorId();
        if (this$instructorId == null ? other$instructorId != null : !((Object)this$instructorId).equals(other$instructorId)) {
            return false;
        }
        Integer this$ratingTeaching = this.getRatingTeaching();
        Integer other$ratingTeaching = other.getRatingTeaching();
        if (this$ratingTeaching == null ? other$ratingTeaching != null : !((Object)this$ratingTeaching).equals(other$ratingTeaching)) {
            return false;
        }
        Integer this$ratingAtmosphere = this.getRatingAtmosphere();
        Integer other$ratingAtmosphere = other.getRatingAtmosphere();
        if (this$ratingAtmosphere == null ? other$ratingAtmosphere != null : !((Object)this$ratingAtmosphere).equals(other$ratingAtmosphere)) {
            return false;
        }
        Integer this$ratingAttitude = this.getRatingAttitude();
        Integer other$ratingAttitude = other.getRatingAttitude();
        if (this$ratingAttitude == null ? other$ratingAttitude != null : !((Object)this$ratingAttitude).equals(other$ratingAttitude)) {
            return false;
        }
        Integer this$createTime = this.getCreateTime();
        Integer other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Integer this$updateTime = this.getUpdateTime();
        Integer other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Integer this$deleteTime = this.getDeleteTime();
        Integer other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        Integer this$recommendCount = this.getRecommendCount();
        Integer other$recommendCount = other.getRecommendCount();
        if (this$recommendCount == null ? other$recommendCount != null : !((Object)this$recommendCount).equals(other$recommendCount)) {
            return false;
        }
        Integer this$likeCount = this.getLikeCount();
        Integer other$likeCount = other.getLikeCount();
        if (this$likeCount == null ? other$likeCount != null : !((Object)this$likeCount).equals(other$likeCount)) {
            return false;
        }
        Integer this$replyTime = this.getReplyTime();
        Integer other$replyTime = other.getReplyTime();
        if (this$replyTime == null ? other$replyTime != null : !((Object)this$replyTime).equals(other$replyTime)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$images = this.getImages();
        String other$images = other.getImages();
        if (this$images == null ? other$images != null : !this$images.equals(other$images)) {
            return false;
        }
        String this$replyContent = this.getReplyContent();
        String other$replyContent = other.getReplyContent();
        return !(this$replyContent == null ? other$replyContent != null : !this$replyContent.equals(other$replyContent));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseReview;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $courseClassId = this.getCourseClassId();
        result = result * 59 + ($courseClassId == null ? 43 : ((Object)$courseClassId).hashCode());
        Integer $lessonNo = this.getLessonNo();
        result = result * 59 + ($lessonNo == null ? 43 : ((Object)$lessonNo).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $ratingTeaching = this.getRatingTeaching();
        result = result * 59 + ($ratingTeaching == null ? 43 : ((Object)$ratingTeaching).hashCode());
        Integer $ratingAtmosphere = this.getRatingAtmosphere();
        result = result * 59 + ($ratingAtmosphere == null ? 43 : ((Object)$ratingAtmosphere).hashCode());
        Integer $ratingAttitude = this.getRatingAttitude();
        result = result * 59 + ($ratingAttitude == null ? 43 : ((Object)$ratingAttitude).hashCode());
        Integer $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Integer $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Integer $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        Integer $recommendCount = this.getRecommendCount();
        result = result * 59 + ($recommendCount == null ? 43 : ((Object)$recommendCount).hashCode());
        Integer $likeCount = this.getLikeCount();
        result = result * 59 + ($likeCount == null ? 43 : ((Object)$likeCount).hashCode());
        Integer $replyTime = this.getReplyTime();
        result = result * 59 + ($replyTime == null ? 43 : ((Object)$replyTime).hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $images = this.getImages();
        result = result * 59 + ($images == null ? 43 : $images.hashCode());
        String $replyContent = this.getReplyContent();
        result = result * 59 + ($replyContent == null ? 43 : $replyContent.hashCode());
        return result;
    }

    public String toString() {
        return "CourseReview(id=" + this.getId() + ", userId=" + this.getUserId() + ", courseClassId=" + this.getCourseClassId() + ", lessonNo=" + this.getLessonNo() + ", instructorId=" + this.getInstructorId() + ", ratingTeaching=" + this.getRatingTeaching() + ", ratingAtmosphere=" + this.getRatingAtmosphere() + ", ratingAttitude=" + this.getRatingAttitude() + ", content=" + this.getContent() + ", images=" + this.getImages() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ", recommendCount=" + this.getRecommendCount() + ", likeCount=" + this.getLikeCount() + ", replyContent=" + this.getReplyContent() + ", replyTime=" + this.getReplyTime() + ")";
    }
}

