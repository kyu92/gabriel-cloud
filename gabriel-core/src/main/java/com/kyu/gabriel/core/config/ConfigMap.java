package com.kyu.gabriel.core.config;

import com.kyu.gabriel.core.client.ManagerService;
import com.kyu.gabriel.core.model.po.manager.Config;
import com.kyu.gabriel.core.model.po.user.User;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConfigMap extends HashMap<String, String> {

    private static final long serialVersionUID = -5774767621621360442L;
    private ManagerService managerService;
    private final boolean hasBean;

    public ConfigMap(ApplicationContext context){
        hasBean = Arrays.stream(context.getBeanDefinitionNames()).anyMatch(s -> s.contains("ManagerService"));
        if (hasBean) {
            managerService = context.getBean(ManagerService.class);
        }
    }

    public void loadConfig(){
        if (!hasBean){
            return;
        }
        List<Config> configList = managerService.getConfig().getData();
        for (Config config: configList){
            this.put(config.getConfigKey(), config.getConfigValue());
        }
    }

    @Override
    public String get(Object key) {
        this.loadConfig();
        return super.get(key);
    }

    public String getOrigin(String key){
        return super.get(key);
    }

    public void save(User operator){
        if (!hasBean){
            return;
        }
        List<Config> configList = managerService.getConfig().getData();
        List<Config> modify = new ArrayList<>();
        for (Config config: configList){
            String key = config.getConfigKey();
            String origin = config.getConfigValue();
            String mirror = this.getOrigin(key);
//            System.out.println("key: " + key + "\norigin: " + origin + "\nmirror: " + mirror);
            if (!origin.equals(mirror)){
                config.setConfigValue(mirror);
                config.setUpdateBy(operator.getUuid());
                config.setUpdateDate(new Date());
                modify.add(config);
            }
        }
        if (modify.size() <= 0){
            return;
        }
        if(!managerService.updateConfig(modify).isSuccessful()){
            throw new RuntimeException("保存数据失败");
        }
    }
}
