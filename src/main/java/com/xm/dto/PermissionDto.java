package com.xm.dto;

public class PermissionDto extends BaseDto {

    private Long id;

    private String name;

    private Long menuId;

    private String menuIdsStr;

    private String type;

    private String expression;

    private String desc;

    private String menuName;

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

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIdsStr() {
        return menuIdsStr;
    }

    public void setMenuIdsStr(String menuIdsStr) {
        this.menuIdsStr = menuIdsStr;
    }



}
