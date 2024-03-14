package org.xlw.test.shardingspherejdbc_demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xlw.test.shardingspherejdbc_demo.dao.DeptMapper;
import org.xlw.test.shardingspherejdbc_demo.entity.Dept;
import org.xlw.test.shardingspherejdbc_demo.service.DeptService;

/**
 * (Dept0)表服务实现类
 *
 * @author xlw
 * @since 2024-03-14 15:16:17
 */
@Service("deptService")
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}

