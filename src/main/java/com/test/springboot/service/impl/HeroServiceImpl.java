package com.test.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.springboot.domain.Hero;
import com.test.springboot.mapper.HeroMapper;
import com.test.springboot.service.HeroService;
import org.springframework.stereotype.Service;

/**
 * @author 肖龙威
 * @date 2022/08/17 13:54
 */
@Service
public class HeroServiceImpl extends ServiceImpl<HeroMapper, Hero> implements HeroService {

}
