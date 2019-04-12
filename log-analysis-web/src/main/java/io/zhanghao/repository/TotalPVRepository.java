package io.zhanghao.repository;

import io.zhanghao.dataobject.TotalPV;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 2019/04/10 zhanghao
 */
@Repository
public interface TotalPVRepository extends CrudRepository<TotalPV,Long> {

}
