package com.kyu.gabriel.book.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/testSentinel")
    public @ResponseBody String testSentinel() {
        String resourceName = "testSentinel";
        Entry entry = null;
        String retVal;
        try{
            entry = SphU.entry(resourceName, EntryType.IN);
            retVal = "passed";
        }catch(BlockException e){
            retVal = "blocked";
        }finally {
            if(entry != null){
                entry.exit();
            }
        }
        return retVal;
    }
}
