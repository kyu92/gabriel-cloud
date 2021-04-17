function sliceJWT(token) {
    return token.split(".")[1];
}

export default {
    token: null,
    payload: {},
    saveToken(token){
        localStorage.setItem("token", token);
    },
    getToken(){
        let token = localStorage.getItem("token");
        if (!token){
            return null;
        }
        let info = sliceJWT(token);
        this.payload = JSON.parse(window.atob(info));
        if (this.payload.exp ? this.payload.exp * 1000 < new Date().getTime() : true){
            localStorage.removeItem("token");
            return null;
        }
        return token;
    },
    clearToken(){
        localStorage.removeItem("token");
    },
    init(token){
        this.token = token;
        try {
            let info = sliceJWT(token);
            this.payload = JSON.parse(window.atob(info));
        } catch (e) {
            this.payload = {};
        }
    },
    getClaim(key){
        if (this.payload.hasOwnProperty(key)){
            return this.payload[key];
        } else {
            return null;
        }
    },
    isExpired(){
        return this.payload.exp ? this.payload.exp * 1000 < new Date().getTime() : true;
    }
}