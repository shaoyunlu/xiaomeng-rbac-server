package com.xm.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuDto extends BaseDto {

    private Long id;

    private String name;
    
    private String url;

    private Integer order;

    private Long parentId;

    private String icon;

    private List<Integer> orders;

    private List<Long> ids;

    private List<PermissionDto> permissionDtos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<PermissionDto> getPermissionDtos() {
        return permissionDtos;
    }

    public void setPermissionDtos(List<PermissionDto> permissionDtos) {
        this.permissionDtos = permissionDtos;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public void setOrders(List<Integer> orders) {
        this.orders = orders;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }




}
