function post(url, paramater) {
    let tempForm = document.createElement("form");
    tempForm.action = url;
    //如需打开新窗口，form的target属性要设置为'_blank'
    tempForm.target = "_self";
    tempForm.method = "post";
    tempForm.style.display = "none";
    //添加参数
    for (let item in paramater) {
        if (!paramater.hasOwnProperty(item)){
            return;
        }
        let opt = document.createElement("input");
        opt.name = item;
        opt.value = paramater[item];
        tempForm.appendChild(opt);
    }
    document.body.appendChild(tempForm);
    //提交数据
    tempForm.submit();
}

export default {
    toAdmin(param){
        post("http://admin.kyu92.top/login", param);
    }
}