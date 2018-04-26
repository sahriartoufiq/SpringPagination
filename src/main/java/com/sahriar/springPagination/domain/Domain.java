package com.sahriar.springPagination.domain;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private Date created;

    private Date updated;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updated = new Date();
        if (created==null) {
            created = new Date();
        }
    }
}
