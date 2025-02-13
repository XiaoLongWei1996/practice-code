package com.xlw.test.template_demo.extend.desensitization;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * @description:
 * @Title: DesensitizationSerialize
 * @Author xlw
 * @Package com.invoice.tcc.config
 * @Date 2024/10/30 17:55
 */
@NoArgsConstructor
@AllArgsConstructor
public class DesensitizationSerialize extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 脱敏规则
     */
    private DesensitizationTypeEnum type;

    /**
     * 开始包含
     */
    private Integer startInclude;

    /**
     * 结束包含
     */
    private Integer endInclude;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        switch (type) {
            case CUSTOM_RULE:
                gen.writeString(CharSequenceUtil.hide(value, startInclude, endInclude));
                break;
            case BANK_CARD:
                gen.writeString(DesensitizedUtil.bankCard(value));
                break;
            case ID_CARD:
                gen.writeString(DesensitizedUtil.idCardNum(value, 3, 4));
                break;
            case NAME:
                gen.writeString(DesensitizedUtil.chineseName(value));
                break;
            case MOBILE:
                gen.writeString(DesensitizedUtil.mobilePhone(value));
                break;
            default:
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (Objects.nonNull(property)) {
            if (property.getType().getRawClass().equals(String.class)) {
                //是String
                Desensitization annotation = property.getAnnotation(Desensitization.class);
                if (annotation == null) {
                    annotation = property.getContextAnnotation(Desensitization.class);
                }
                if (annotation != null) {
                    // 创建定义的序列化类的实例并且返回，入参为注解定义的type,开始位置，结束位置。
                    return new DesensitizationSerialize(annotation.type(), annotation.startInclude(), annotation.endInclude());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }
}
