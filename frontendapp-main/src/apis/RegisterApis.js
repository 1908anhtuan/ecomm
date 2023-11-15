import axios from "axios";
const LOGIN_BASE_URL = "http://localhost:8080/accounts";

const RegisterAPI = {
    register: (credentials) => axios.post(LOGIN_BASE_URL , credentials)
};

export default RegisterAPI;