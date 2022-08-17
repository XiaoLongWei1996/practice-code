package com.test.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.test.springboot.domain.Hero;

/**
 * @author 肖龙威
 * @date 2022/08/16 17:17
 */
public interface HeroMapper extends BaseMapper<Hero>, MPJBaseMapper<Hero> {
}
