package com.lcy.xingchenmall.product.feign;

import com.lcy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("xingchenmall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
     R getSkuHasStock(@RequestBody List<Long> skuIds);
}
