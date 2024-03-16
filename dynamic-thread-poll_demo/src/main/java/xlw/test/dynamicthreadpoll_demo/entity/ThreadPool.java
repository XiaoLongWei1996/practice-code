package xlw.test.dynamicthreadpoll_demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * (ThreadPool)表实体类
 *
 * @author xlw
 * @since 2024-03-16 23:20:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("thread_pool")
public class ThreadPool {
    
  //id    
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    
  //应用名称    
  @TableField(value = "application_name")
  private String applicationName;
    
  //线程池名称    
  @TableField(value = "thread_pool_name")
  private String threadPoolName;
    
  //核心大小    
  @TableField(value = "core_size")
  private Integer coreSize;
    
  //最大大小    
  @TableField(value = "max_size")
  private Integer maxSize;
    
  //队列大小    
  @TableField(value = "queue_size")
  private Integer queueSize;
    
  //存活时间    
  @TableField(value = "keep_alive_time")
  private Long keepAliveTime;
    
  //队列元素数量    
  @TableField(value = "queue_elements_count")
  private Integer queueElementsCount;
    
  //队列剩余容量    
  @TableField(value = "queue_remaining_capacity")
  private Integer queueRemainingCapacity;
    
  //存活的线程数    
  @TableField(value = "active_count")
  private Integer activeCount;
    
  //任务数    
  @TableField(value = "task_count")
  private Long taskCount;
    
  //完成的任务数    
  @TableField(value = "completed_task_count")
  private Long completedTaskCount;
    
  //巅峰线程数    
  @TableField(value = "largest_pool_size")
  private Integer largestPoolSize;
    
  //负载百分比    
  @TableField(value = "load_pressure")
  private BigDecimal loadPressure;
    
  //创建时间    
  @TableField(value = "create_dt")
  private Date createDt;
    
  //修改时间    
  @TableField(value = "update_dt")
  private Date updateDt;

}
