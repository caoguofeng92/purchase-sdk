package com.camelot.purchase.convert;

import com.camelot.purchase.domain.UserDomain;
import com.camelot.purchase.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Admin
 * @date 2022/6/8
 */
@Mapper
public interface UserConvert {
    UserConvert INSTANCT = Mappers.getMapper(UserConvert.class);

    UserVO domainToVo(UserDomain domain);

    List<UserVO> domainListToVoList(List<UserDomain> domainList);


    UserDomain voToDomain(UserVO userVO);

    List<UserDomain> voListToDomainList(List<UserVO> userVOList);
}
