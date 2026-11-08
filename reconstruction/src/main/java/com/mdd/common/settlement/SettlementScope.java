package com.mdd.common.settlement;

import java.io.Serializable;

/**
 * 分账批次的数据范围。
 *
 * <p>type=ALL 表示全量；type=COMMUNITY 表示仅某个社区。社区系统按社区建账时使用后者。</p>
 */
public class SettlementScope implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type = "ALL";
    private Integer id = 0;
    private String name = "";

    public static SettlementScope all() {
        return new SettlementScope("ALL", 0, "全部社区");
    }

    public static SettlementScope community(Integer communityId, String communityName) {
        return new SettlementScope("COMMUNITY", communityId, communityName == null ? "" : communityName);
    }

    public SettlementScope() {
    }

    public SettlementScope(String type, Integer id, String name) {
        this.type = type;
        this.id = id;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
