import axios from "axios";
import jwt from "@/common/jwt";

const service = axios.create({
    timeout: 3000,
    baseURL: "/ManagerServiceConsumer",
    headers: {
        "Authorization": `${jwt.getToken()}`,
        "Content-Type": 'application/x-www-form-urlencoded',
    }
});

export default {
    listQA(){
        return service.get("/quest/list");
    },
    putFeedback(title, content, information, contactType){
        let form = new URLSearchParams({title, information, contactType});
        if (content){
            form.append("content", content);
        }
        return service.post("/quest/other/put", form);
    },
    getSystemInfo() {
        return service("/system/info");
    }
}
