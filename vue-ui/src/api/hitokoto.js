import axios from "axios";

export function inside(callback){
    axios.request({
        url: "https://v1.hitokoto.cn",
        method: "GET"
    }).then(res => {
        let data = res.data;
        let from = data.from_who ? data.from_who : data.from;
        callback({content: data.hitokoto, from: from});
    });
}

export function outside(callback){
    axios.request({
        url: "international.v1.hitokoto.cn",
        method: "GET"
    }).then(res => {
        let data = res.data;
        callback({content: data.hitokoto, author: data.from_who});
    });
}