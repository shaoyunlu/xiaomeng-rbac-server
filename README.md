# XiaoMeng-RBAC

本项目是一个功能全面的后台管理系统，支持用户管理、角色管理、菜单管理和权限管理。系统设计支持用户角色一对多关联，角色与权限的绑定，同时权限控制精确到按钮级，适合需要细粒度权限控制的应用场景。

## 功能特性

- **用户管理**：支持用户的增删改查，用户状态管理。
- **角色管理**：角色的创建、修改和删除，以及角色的权限分配。
- **菜单管理**：动态菜单的创建、修改、排序和删除，根据不同角色展示不同菜单。
- **权限管理**：精细的权限控制，包括按钮级别的权限分配，确保系统的安全性。

## 技术栈

- 前端：Vue.js
- UI：xiaomeng-ui [项目地址](https://github.com/shaoyunlu/xiaomeng-ui)
- 后端：Springboot
- 数据库：Mysql

## 在线演示

- 访问在线演示：[演示地址](http://114.116.50.8:3000)
- 用户名：admin
- 密码：admin123456
- 为方便大家体验，请不要随意进行删除或者修改密码操作，谢谢。

## 启动前须知
请将src\main\resources\application.properties.bak 文件的后缀.bak去掉。

## 联系方式

- 作者：[邵云陆]
- 邮箱：[shaoyunlu@126.com]
