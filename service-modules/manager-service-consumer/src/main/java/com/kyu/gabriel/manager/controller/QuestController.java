package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.model.po.manager.Answer;
import com.kyu.gabriel.core.model.po.manager.Feedback;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.model.vo.ListFeedbackVo;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quest")
public class QuestController {

    private final ManagerService managerService;

    @Autowired
    public QuestController(ManagerService managerService){
        this.managerService = managerService;
    }

    @Authentication(0)
    @RequestMapping("/add")
    @Logger(value = "添加答疑项", exclude = "content")
    public ResultMap<Void> addQuestAndAnswer(HttpServletRequest request, String title, String content){
        User user = (User) request.getAttribute("user");
        Answer answer = new Answer()
                .setTitle(title)
                .setAnswer(content)
                .setCreator(user.getUuid());
        return managerService.addQA(answer);
    }

    @Authentication(0)
    @Logger(value = "更改答疑项", exclude = "content")
    @RequestMapping("/update/{id}")
    public ResultMap<Void> updateQuestAndAnswer(@PathVariable int id, String title, String content){
        Answer answer = managerService.getQA(id).getData();
        System.out.println(answer);
        if (answer == null){
            return ResultMap.failed(3004, "数据不存在");
        }
        answer.setTitle(title).setAnswer(content);
        return managerService.updateQA(answer);
    }

    @Authentication(0)
    @Logger("删除答疑项")
    @RequestMapping("/delete/{id}")
    public ResultMap<Void> deleteQuestAndAnswer(@PathVariable int id){
        return managerService.deleteQA(id);
    }

    @RequestMapping("/list")
    public ResultMap<List<Answer>> listQuestAndAnswer(){
        return managerService.listQA();
    }

    @RequestMapping("/other/put")
    public ResultMap<Void> putFeedback(String title, String content, String information, int contactType){
        Feedback feedback = new Feedback()
                .setTitle(title)
                .setDescription(content)
                .setInformation(information)
                .setContactType(contactType)
                .setStatus(0);
        return managerService.putFeedback(feedback);
    }

    @RequestMapping("/feedback/list")
    @Authentication(0)
    public ResultMap<Map<String, Object>> listFeedback(String keyword, int page, int limit){
        ListFeedbackVo vo = new ListFeedbackVo(keyword, page, limit);
        return managerService.listFeedback(vo);
    }

    @RequestMapping("/feedback/delete")
    @Authentication(0)
    @Logger("删除用户反馈")
    public ResultMap<Integer> deleteFeedback(@RequestBody int []ids){
        if (ids.length > 0) {
            return managerService.deleteFeedback(Arrays.stream(ids).boxed().collect(Collectors.toList()));
        }
        return ResultMap.failed(5001, "列表为空");
    }

    @RequestMapping("/feedback/status/{statusCode}")
    @Authentication(0)
    @Logger("设置用户反馈状态")
    public ResultMap<Integer> setFeedbackStatus(@RequestBody int []ids, @PathVariable int statusCode){
        if (ids.length > 0) {
            return managerService.setFeedbackStatus(Arrays.stream(ids).boxed().collect(Collectors.toList()), statusCode);
        }
        return ResultMap.failed(5001, "列表为空");
    }
}
