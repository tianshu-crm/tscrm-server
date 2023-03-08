package com.weilango.web.controller.crm;

import com.weilango.common.core.controller.BaseController;
import com.weilango.common.core.page.TableDataInfo;
import com.weilango.crm.domain.CustomerTrack;
import com.weilango.crm.service.ICustomerTrackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
/**
 * 线索轨迹Controller
 * 
 * @author hao
 * @date 2023-01-09
 */
@Api(tags = "线索轨迹管理")
@RestController
@RequestMapping("/crm/customerTrack")
public class CustomerTrackController extends BaseController
{
    @Autowired
    private ICustomerTrackService customerTrackService;

    /**
     * 查询线索轨迹列表
     */
    @ApiOperation("查询线索轨迹列表")
    //@PreAuthorize("@ss.hasPermi('crm:customerTrack:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerTrack customerTrack)
    {
        startPage();
        List<Map<String,Object>> list = customerTrackService.selectCustomerTrackList(customerTrack);
        return getDataTable(list);
    }


}
