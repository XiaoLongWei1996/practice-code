package xlw.test.satoken.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xlw.test.satoken.entity.User;
import xlw.test.satoken.config.Result;
import xlw.test.satoken.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户表(User)表控制层
 *
 * @author xlw
 * @since 2024-03-23 15:25:17
 */
@Api(tags = "用户表控制层")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @ApiOperation("查询所有数据")
    @GetMapping("selectAll")
    public Result<List<User>> selectAll() {
        return Result.success(userService.list());
    }
    
    /**
     * 分页查询所有数据
     *
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @return {@link Result}<{@link IPage}<{@link User}>>
     */
    @ApiOperation("分页查询所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = true, paramType = "query", dataType = "Integer"),
    })
    @GetMapping("selectAllByPage")
    public Result<IPage<User>> selectAllByPage(Integer currentPage, Integer pageSize) {
        IPage page = Page.of(currentPage, pageSize);
        return Result.success(userService.listByPage(page));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("查询单条数据")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "Integer")
    @GetMapping("selectOne/{id}")
    public Result<User> selectOne(@PathVariable Integer id) {
        System.out.println(StpUtil.getPermissionList());
        return Result.success(userService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @ApiOperation("保存数据")
    @PostMapping("save")
    public Result<Boolean> insert(User user) {
        return Result.success(userService.save(user));
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
	@ApiOperation("修改数据")
    @PutMapping("update")
    public Result<Boolean> update(User user) {
        return Result.success(userService.updateById(user));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping("delete")
    public Result<Boolean> delete(List<Integer> idList) {
        return Result.success(userService.removeByIds(idList));
    }


}

