package com.xm.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xm.dto.MenuDto;
import com.xm.dto.UserDto;
import com.xm.model.Menu;
import com.xm.model.Permission;
import com.xm.model.Role;
import com.xm.model.User;
import com.xm.repository.MenuRepository;
import com.xm.repository.UserRepository;
import com.xm.util.XmConstants;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Menu> getAllMenus(){
        Sort sort = Sort.by(Sort.Order.asc("order"));
        return menuRepository.findAll(sort);
    }

    public Page<Menu> getMenus(MenuDto menuDto){
        return menuRepository.findAll(PageRequest.of(menuDto.getPageNum() ,menuDto.getPageSize()));
    }

    @Transactional(readOnly = true)
    public List<Menu> getMenusByUser(UserDto userDto){
        List<Menu> allMenus = menuRepository.findAll();
        User sessionUser = userRepository.findById(userDto.getId()).get();
        Set<Long> userMenuIds = sessionUser.getRoles().stream()
                                          .flatMap(role -> role.getPermissions().stream())
                                          .map(Permission::getMenu)
                                          .map(Menu::getId)
                                          .collect(Collectors.toSet());
        // 过滤出用户有权限访问的菜单，同时确保包含所有必要的父节点
        List<Menu> filteredMenus = new ArrayList<>();
        
        boolean hasAdminRole = sessionUser.getRoles().stream().anyMatch(role -> XmConstants.ADMIN_ROLE_NAME_KEY.equals(role.getName()));

        // 判断用户是否有管理员权限
        if (hasAdminRole){
            filteredMenus = allMenus;
        }else{
            for (Menu menu : allMenus) {
                if (userMenuIds.contains(menu.getId())) {
                    addMenuAndParents(menu, filteredMenus, allMenus);
                }
            }
        }
        
        return filteredMenus.stream()
                            .distinct()
                            .sorted(Comparator.comparing(Menu::getParentId).thenComparing(Menu::getOrder))
                            .collect(Collectors.toList());
    }

    private void addMenuAndParents(Menu menu, List<Menu> filteredMenus, List<Menu> allMenus) {
        if (menu != null && !filteredMenus.contains(menu)) {
            filteredMenus.add(menu);
            if (menu.getParentId() != 0) {
                Menu parentMenu = allMenus.stream()
                                          .filter(m -> m.getId().equals(menu.getParentId()))
                                          .findFirst()
                                          .orElse(null);
                addMenuAndParents(parentMenu, filteredMenus, allMenus);
            }
        }
    }

    @Transactional
    public void addMenu(MenuDto menuDto){
        ModelMapper modelMapper = new ModelMapper();
        Menu menu = modelMapper.map(menuDto, Menu.class);
        menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(MenuDto menuDto){

        for (Long id : menuDto.getIds()){
            Menu menu = menuRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
            for (Permission permission : menu.getPermissions()){
                for (Role role : permission.getRoles() ){
                    role.getPermissions().remove(permission);
                }
            }
        }
        menuRepository.deleteAllById(menuDto.getIds());
    }

    @Transactional
    public void updateMenu(MenuDto menuDto){
        Menu menu = menuRepository.findById(menuDto.getId())
                    .orElseThrow(() -> new RuntimeException("Menu not found with id: " + menuDto.getId()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<MenuDto, Menu>() {
            @Override
            protected void configure() {
                skip(destination.getPermissions());
            }
        });
        modelMapper.map(menuDto ,menu);
        menuRepository.save(menu);
    }

    @Transactional
    public void adjustOrder(MenuDto menuDto){
        List<Long> ids = menuDto.getIds();
        List<Integer> orders = menuDto.getOrders();
        List<Menu> menus = menuRepository.findAllById(ids);
        System.out.println(ids);
        Iterator<Integer> orderIterator = orders.iterator();
        Map<Long ,Integer> idToOrderMap = new HashMap<>();
        ids.forEach(id -> idToOrderMap.put(id ,orderIterator.next()));
        menus.forEach(menu->{

            
            Integer order = idToOrderMap.get(menu.getId());

            menu.setOrder(order);
        });
        menuRepository.saveAll(menus);

    }
    
}
