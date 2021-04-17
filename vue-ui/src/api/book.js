import axios from "axios";
import jwt from "@/common/jwt";

const service = axios.create({
    timeout: 3000,
    baseURL: "/BookServiceConsumer",
    headers: {
        "Authorization": `${jwt.getToken()}`,
        "Content-Type": 'application/x-www-form-urlencoded',
    }
});

export default {
    list(){
        return service.get(`/books`);
    },
    remove(uuid){
        let url = `/delete/${uuid}`;
        return service.request({url, method: "POST"});
    },
    clear(){
        return service.post("/clear");
    },
    getUrl(uuid){
        return service.get(`/book/url/${uuid}`);
    },
    getBook: async function(uuid){
        let res = await service.get(`/book/${uuid}`, {
            responseType: "blob",
            timeout: 100000
        });
        return new Blob([res.data], res.headers["Content-Type"]);
    },
    getInfo(uuid){
        let form = new URLSearchParams({uuid});
        return service.post("/info", form);
    },
    saveTxtProgress(uuid, process){
        let progressData = JSON.stringify(process);
        let form = new URLSearchParams({uuid, progressData});
        return service.post("/progress/save", form);
    }
}