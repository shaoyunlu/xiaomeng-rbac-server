package com.xm.mapper;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import com.xm.converters.EmptyStringToNullConverter;
import com.xm.converters.PasswordConverter;
import com.xm.dto.RoleDto;
import com.xm.dto.UserDto;
import com.xm.model.Role;
import com.xm.model.User;

@Service
public class UserMapper extends BaseMapper{


    public UserMapper(ModelMapper modelMapper) {
        super(modelMapper); // 调用父类的构造函数
    }

    @Override
    protected void configureModelMapper(ModelMapper modelMapper){

        PasswordConverter passwordConverter  = new PasswordConverter();
        EmptyStringToNullConverter toNullConverter = new EmptyStringToNullConverter();

        modelMapper.createTypeMap(User.class, UserDto.class).addMappings(mapper -> {
            mapper.<Set<RoleDto>>map(src -> {
                // 检查permissions是否为null，如果是则提供一个空的HashSet作为源对象
                Set<Role> roles = src.getRoles() == null ? new HashSet<>() : src.getRoles();
                // 然后进行映射
                return modelMapper.map(roles, new TypeToken<Set<RoleDto>>() {}.getType());
            }, (dest, v) -> dest.setRoleDtos((Set<RoleDto>) v));
        }).addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
                skip(destination.getCheckPassword());
                skip(destination.getOldPassword());
            }
        });

        modelMapper.createTypeMap(UserDto.class, User.class).addMappings(mapper -> {
            mapper.using(passwordConverter).map(source -> source.getPassword(), (dest, v) -> dest.setPassword((String) v));
            mapper.using(toNullConverter).map(UserDto::getEmail, User::setEmail);
        });
    }

    public UserDto convertToDto(User user){
        return modelMapper.map(user ,UserDto.class);
    }

    public User convertToModel(UserDto userDto){
        return modelMapper.map(userDto ,User.class);
    }
}
