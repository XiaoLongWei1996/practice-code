package com.xlw.mapstruct_demo.mapper;


import com.xlw.mapstruct_demo.entity.Student;
import com.xlw.mapstruct_demo.vo.StudentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author xlw
 * @description:
 * @title: StudentMapper
 * @package com.xlw.mapstruct_demo.mapper
 * @date 2025/3/24 19:28
 */
@Mapper
public interface StudentMapper {

    StudentMapper INSTANCT = Mappers.getMapper(StudentMapper.class);

    @Mappings({
            @Mapping(source = "desc", target = "msg")
    })
    StudentVO toVO(Student student);
}
