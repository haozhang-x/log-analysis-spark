package io.zhanghao.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.zhanghao.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * 总PV View Object
 *
 * @author zhanghao
 * @date 2019/04/12
 */
@Data
public class TotalPVVO implements Serializable {

    private static final long serialVersionUID = -5786140609034921677L;

    private Long id;

    private String totalPV;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;
}
