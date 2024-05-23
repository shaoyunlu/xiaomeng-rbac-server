package com.xm.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.xm.dto.PermissionDto;
import com.xm.model.Permission;

@Service
public class PermissionMapper extends BaseMapper{

    public PermissionMapper(ModelMapper modelMapper){
        super(modelMapper); // 调用父类的构造函数
    }

    @Override
    protected void configureModelMapper(ModelMapper modelMapper){
        modelMapper.createTypeMap(Permission.class, PermissionDto.class).addMappings(mapper -> {
            mapper.map(src -> {
                return src.getMenu().getName();
            },PermissionDto::setMenuName);
        });
    }
}
