package com.xlw.test.redis_cache_test.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlw.test.redis_cache_test.config.ratelimiter.RateLimiter;
import com.xlw.test.redis_cache_test.entity.Result;
import com.xlw.test.redis_cache_test.entity.Ticket;
import com.xlw.test.redis_cache_test.entity.UserTicket;
import com.xlw.test.redis_cache_test.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 优惠卷表(Ticket)表控制层
 *
 * @author xlw
 * @since 2024-01-22 21:12:01
 */
@Slf4j
@RestController
@RequestMapping("ticket")
public class TicketController {
    /**
     * 服务对象
     */
    @Resource
    private TicketService ticketService;


    @PostMapping("flashSale")
    public Result<Boolean> flashSale(UserTicket ut) {
        return Result.succeed(ticketService.flashSale(ut));
    }

    @RateLimiter(name = "flashSale", limit = 50, timeout = 1000)
    @PostMapping("flashSale2")
    public Result<Boolean> flashSale2(UserTicket ut) {
        return Result.succeed(ticketService.flashSale2(ut));
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("selectAll")
    public Result<List<Ticket>> selectAll() {
        return Result.succeed(ticketService.list());
    }
    
    /**
     * 分页查询所有数据
     *
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @return {@link Result}<{@link IPage}<{@link Ticket}>>
     */
    @GetMapping("selectAllByPage")
    public Result<IPage<Ticket>> selectAllByPage(Integer currentPage, Integer pageSize) {
        IPage page = Page.of(currentPage, pageSize);
        return Result.succeed(ticketService.listByPage(page));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public Result<Ticket> selectOne(@PathVariable Integer id) {
        return Result.succeed(ticketService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param ticket 实体对象
     * @return 新增结果
     */
    @PostMapping("save")
    public Result<Ticket> insert(Ticket ticket) {
        return Result.succeed(ticketService.addTicket(ticket));
    }

    /**
     * 修改数据
     *
     * @param ticket 实体对象
     * @return 修改结果
     */
    @PutMapping("update")
    public Result<Boolean> update(Ticket ticket) {
        return Result.succeed(ticketService.update(ticket));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("delete")
    public Result<Boolean> delete(@RequestParam("idList") List<Integer> idList) {
        return Result.succeed(ticketService.delete(idList));
    }

}

