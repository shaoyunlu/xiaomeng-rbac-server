package com.xm.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xm.dto.PermissionDto;
import com.xm.dto.RoleDto;
import com.xm.dto.UserDto;
import com.xm.mapper.UserMapper;
import com.xm.model.AjaxResponse;
import com.xm.model.Permission;
import com.xm.model.Role;
import com.xm.model.User;
import com.xm.repository.PermissionRepository;
import com.xm.repository.RoleRepository;
import com.xm.repository.UserRepository;
import com.xm.specification.UserSpecification;
import com.xm.util.XmConstants;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public UserDto login(String name){
        User user = userRepository.findByName(name);
        if (user == null){
            return null;
        }
        ModelMapper modelMapper = userMapper.getModelMapper();
        ModelMapper permissionModelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(user ,UserDto.class);
        userDto.setPassword(user.getPassword());
        // 如何是具有管理员权限，则需要赋上全部的权限
        Optional<RoleDto> optionalDto = userDto.getRoleDtos().stream().filter(tmp -> XmConstants.ADMIN_ROLE_NAME_KEY.equals(tmp.getName())).findAny();
        optionalDto.ifPresent(roleDto -> {
            Set<Permission> permissionList = new HashSet<>(permissionRepository.findAll());
            Set<PermissionDto>permissionDtoList = permissionList.stream().map(permission -> permissionModelMapper.map(permission ,PermissionDto.class)).collect(Collectors.toSet());
            roleDto.setPermissionDtos(permissionDtoList);
        });
        List<PermissionDto> uniquePermissions = userDto.getRoleDtos().stream()
                                                .filter(roleDto -> roleDto.getPermissionDtos() != null)
                                                .flatMap(roleDto ->roleDto.getPermissionDtos().stream())
                                                .distinct()
                                                .collect(Collectors.toList());
        userDto.setPermissionDtos(uniquePermissions);
        return userDto;
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(UserDto userDto){
        Pageable pageable = PageRequest.of(userDto.getPageNum(), userDto.getPageSize());

        if (userDto.getSortDirection() != null && !userDto.getSortDirection().isEmpty() 
                && userDto.getSortBy() != null && !userDto.getSortBy().isEmpty()) {
            Sort sort = "ascending".equalsIgnoreCase(userDto.getSortDirection()) ? 
                Sort.by(userDto.getSortBy()).ascending() : 
                Sort.by(userDto.getSortBy()).descending();
            pageable = PageRequest.of(userDto.getPageNum(), userDto.getPageSize(), sort);
        } else {
            pageable = PageRequest.of(userDto.getPageNum(), userDto.getPageSize());
        }

        Specification<User> spec = Specification.where(UserSpecification.nameOrRealNameContains(userDto.getName()))
                                                        .and(UserSpecification.nameIsNotAdmin());
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    public void addUser(UserDto userDto){
        User user = userMapper.convertToModel(userDto);
        if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()){
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDto.getRoleIds()));
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + userDto.getId()));
        user.getRoles().clear();
        userRepository.save(user);

        userRepository.deleteById(userDto.getId());
    }

    @Transactional
    public void batchDeleteUsers(List<Long> userIds) {
        for (Long userId : userIds) {
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            deleteUser(userDto); // 通过自引用调用，确保@Transactional生效
        }
    }

    @Transactional
    public void updateUser(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.getId()));

        ModelMapper modelMapper = userMapper.getModelMapper();
        modelMapper.map(userDto ,user);

        if (userDto.getRoleIds() != null && !userDto.getRoleIds().isEmpty()){
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userDto.getRoleIds()));
            user.setRoles(roles);
        }

        userRepository.save(user);
    }

    @Transactional
    public AjaxResponse updatePassword(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.getId()));

        if (!BCrypt.checkpw(userDto.getOldPassword() ,user.getPassword())){
            return AjaxResponse.fail("原始密码不正确，请重新输入。");
        }

        user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);
        return AjaxResponse.ok(null);
    }

}
