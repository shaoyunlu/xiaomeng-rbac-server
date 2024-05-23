package com.xm.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import com.xm.dto.MenuDto;
import com.xm.dto.PermissionDto;
import com.xm.model.Menu;
import com.xm.model.Permission;

@Service
public class MenuMapper extends BaseMapper{

    public MenuMapper(ModelMapper modelMapper){
        super(modelMapper); // 调用父类的构造函数
    }

    @Override
    protected void configureModelMapper(ModelMapper modelMapper){
        modelMapper.createTypeMap(Menu.class, MenuDto.class).addMappings(mapper -> {
            mapper.<List<PermissionDto>>map(src -> {
                // 检查permissions是否为null，如果是则提供一个空的HashSet作为源对象
                List<Permission> roles = src.getPermissions() == null ? new ArrayList<>() : src.getPermissions();
                // 然后进行映射
                return modelMapper.map(roles, new TypeToken<List<PermissionDto>>() {}.getType());
            }, (dest, v) -> dest.setPermissionDtos((List<PermissionDto>) v));
        });
    }
}
