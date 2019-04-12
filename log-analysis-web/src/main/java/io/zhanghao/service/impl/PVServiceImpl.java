package io.zhanghao.service.impl;

import io.zhanghao.dataobject.TotalPV;
import io.zhanghao.repository.TotalPVRepository;
import io.zhanghao.service.PVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 2019/04/12 zhanghao
 */
@Service
public class PVServiceImpl implements PVService {

    private TotalPVRepository totalPVRepository;

    @Autowired
    public PVServiceImpl(TotalPVRepository totalPVRepository) {
        this.totalPVRepository = totalPVRepository;
    }

    @Override
    public Iterable<TotalPV> totalPV() {
        return totalPVRepository.findAll();
    }
}
