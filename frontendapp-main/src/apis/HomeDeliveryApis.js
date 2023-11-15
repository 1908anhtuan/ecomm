import axios from "axios";
const DELIVERY_BASE_URL = "http://localhost:8080/homedeliveries";

const HomeDeliveryAPI = {
    getAvailableTimeSlots: () => axios.get(`${DELIVERY_BASE_URL}/available`),
    registerTimeSlot: (request) => axios.post(DELIVERY_BASE_URL, request),
    getCountOfHomeDelivery: (startDate, endDate) => axios.get(`${DELIVERY_BASE_URL}/count?startDate=${startDate}&endDate=${endDate}`),
    getAllHomeDeliveries: () => axios.get(`${DELIVERY_BASE_URL}/all`),
    updateDeliveryStatus: (id, request) => axios.put(`${DELIVERY_BASE_URL}/${id}/status`, request)
};

export default HomeDeliveryAPI;
