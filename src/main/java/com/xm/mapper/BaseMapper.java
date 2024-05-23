package com.xm.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseMapper {

    @Autowired
    protected final ModelMapper modelMapper;
    protected ModelMapper localModelMapper;

    
    public BaseMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.localModelMapper = new ModelMapper();
        configureModelMapper(this.modelMapper );
        configureModelMapper(this.localModelMapper);
    }

    protected abstract void configureModelMapper(ModelMapper modelMapper);

    public ModelMapper getModelMapper(){
        return modelMapper;
    }

    public ModelMapper getLocalModelMapper(){
        return localModelMapper;
    }
}
