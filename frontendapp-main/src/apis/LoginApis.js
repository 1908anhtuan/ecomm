import axios from "axios";

const LOGIN_BASE_URL = "http://localhost:8080/accounts";

const LoginAPI = {
  login: (credentials) => axios.post(`${LOGIN_BASE_URL}/Signin`, credentials),
  getAccountById: (id) => axios.get(`${LOGIN_BASE_URL}/${id}`),
  getAccountByAccountName: (accountName) => axios.get(`${LOGIN_BASE_URL}/accountname?accountName=${accountName}`)};

export default LoginAPI;