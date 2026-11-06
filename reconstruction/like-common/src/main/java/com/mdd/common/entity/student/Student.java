/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.mdd.common.entity.student;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName(value="la_student")
public class Student
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String name;
    private String idCard;
    private Integer gender;
    private String birthday;
    private Integer age;
    private String avatar;
    private String note;
    private Integer isDefault;
    private Long createTime;
    private Long updateTime;
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public Integer getGender() {
        return this.gender;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public Integer getAge() {
        return this.age;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getNote() {
        return this.note;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student other = (Student)o;
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
        Integer this$gender = this.getGender();
        Integer other$gender = other.getGender();
        if (this$gender == null ? other$gender != null : !((Object)this$gender).equals(other$gender)) {
            return false;
        }
        Integer this$isDefault = this.getIsDefault();
        Integer other$isDefault = other.getIsDefault();
        if (this$isDefault == null ? other$isDefault != null : !((Object)this$isDefault).equals(other$isDefault)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$idCard = this.getIdCard();
        String other$idCard = other.getIdCard();
        if (this$idCard == null ? other$idCard != null : !this$idCard.equals(other$idCard)) {
            return false;
        }
        String this$birthday = this.getBirthday();
        String other$birthday = other.getBirthday();
        if (this$birthday == null ? other$birthday != null : !this$birthday.equals(other$birthday)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        return !(this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Student;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $gender = this.getGender();
        result = result * 59 + ($gender == null ? 43 : ((Object)$gender).hashCode());
        Integer $isDefault = this.getIsDefault();
        result = result * 59 + ($isDefault == null ? 43 : ((Object)$isDefault).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $idCard = this.getIdCard();
        result = result * 59 + ($idCard == null ? 43 : $idCard.hashCode());
        String $birthday = this.getBirthday();
        result = result * 59 + ($birthday == null ? 43 : $birthday.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        return result;
    }

    public String toString() {
        return "Student(id=" + this.getId() + ", userId=" + this.getUserId() + ", name=" + this.getName() + ", idCard=" + this.getIdCard() + ", gender=" + this.getGender() + ", birthday=" + this.getBirthday() + ", avatar=" + this.getAvatar() + ", isDefault=" + this.getIsDefault() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

