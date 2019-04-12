package io.zhanghao.dataobject;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author 2019/04/10 zhanghao
 */
@Entity
@Data
@Table(name = "total_pv")
public class TotalPV {
    @Id
    private Long id;

    @Column(name = "total_pv")
    private String totalPV;

    private Date createTime;

    private Date updateTime;

}
