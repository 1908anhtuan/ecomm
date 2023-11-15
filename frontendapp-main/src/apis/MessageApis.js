import axios from "axios";

const MESSAGE_BASE_URL = "http://localhost:8080/messages";

const MessageAPI = {
  getPreviousMessages: (senderid, receiverid) => axios.get(`${MESSAGE_BASE_URL}/${senderid}/${receiverid}`),
  getMessagedAccounts: (userId) => axios.get(`${MESSAGE_BASE_URL}/${userId}`)
};

export default MessageAPI;