package com.smc.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "STOCK_EXCHANGE")
public class StockExchange implements Serializable {

    private static final long serialVersionUID = 1106667047845258942L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CONTACT_ADDRESS", nullable = false)
    private String contactAddress;

    @Column(name = "BRIEF", nullable = false)
    private String brief;

    @Column(name = "REMARKS")
    private String remarks;

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

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
