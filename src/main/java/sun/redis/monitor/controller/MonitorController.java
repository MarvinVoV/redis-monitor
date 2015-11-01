package sun.redis.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.redis.monitor.domain.JsonEntity;
import sun.redis.monitor.service.GatherService;

import java.util.List;

/**
 * Created by yamorn on 2015/10/31.
 */
@Controller
@RequestMapping("/")
public class MonitorController {
    @Autowired
    private GatherService<List<JsonEntity>> gatherService;

    @RequestMapping("/index")
    public String monitor() {
        return "index";
    }

    @RequestMapping(value = "/fetch", produces = {"application/json; charset=utf-8"})
    public
    @ResponseBody
    List<JsonEntity> getInfoData() {
        return gatherService.getDataList();
    }
}
