package xlw.test.satoken.config;

/**
 * @description:
 * @Title: BusinessException
 * @Author xlw
 * @Package xlw.test.satoken.config
 * @Date 2024/3/23 16:46
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
