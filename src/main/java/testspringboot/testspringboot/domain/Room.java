package testspringboot.testspringboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 肖龙威
 * @date 2022/05/18 15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    private String position;

    private Long price;
}
