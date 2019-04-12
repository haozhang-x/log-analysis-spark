package io.zhanghao.controller;

import io.zhanghao.service.PVService;
import io.zhanghao.vo.TotalPVVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 2019/04/11 zhanghao
 */
@RestController
public class PVController {

    private PVService pvService;

    @Autowired
    public PVController(PVService pvService) {
        this.pvService = pvService;
    }

    /**
     * 总的PV
     */
    @GetMapping("/pv/total")
    public List<TotalPVVO> totalPV() {
        List<TotalPVVO> totalPVVOList = new ArrayList<>();
        pvService.totalPV().forEach(
                totalPV -> {
                    TotalPVVO totalPVVO = new TotalPVVO();
                    BeanUtils.copyProperties(totalPV, totalPVVO);
                    totalPVVOList.add(totalPVVO);
                }
        );
        return totalPVVOList;
    }
}
