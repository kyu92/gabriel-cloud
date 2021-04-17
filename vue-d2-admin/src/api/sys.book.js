import axios from "axios";
import jwt from "../../../vue-ui/src/common/jwt";

const service = axios.create({
  timeout: 3000,
  baseURL: "/BookServiceConsumer",
  headers: {
    "Authorization": `${jwt.getToken()}`,
    "Content-Type": 'application/x-www-form-urlencoded',
  }
});

export default {


}
