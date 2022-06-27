package com.hyb.serviceware.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyb
 * @since 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WareLock对象", description="")
public class WareLock implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;

    private String courseId;

    private Integer lockNum;


}
