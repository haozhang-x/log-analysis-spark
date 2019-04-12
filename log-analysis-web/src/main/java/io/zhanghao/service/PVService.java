package io.zhanghao.service;

import io.zhanghao.dataobject.TotalPV;

/**
 * @author 2019/04/12 zhanghao
 */
public interface PVService {
    /**
     * 所有的pv信息
     */
    Iterable<TotalPV> totalPV();
}
