import axios from "axios";

const ACCOUNT_BASE_URL = "http://localhost:8080/accounts";

const AccountAPI = {
  updateAccount: (id, request) => axios.patch(`${ACCOUNT_BASE_URL}/details/${id}`, request),
}
export default AccountAPI;