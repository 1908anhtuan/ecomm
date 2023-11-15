import axios from "axios";
const DELIVERY_BASE_URL = "http://localhost:8080/pickupdeliveries";

const PickUpDeliveryAPI = {
    getAvailableTimeSlots: (request) => axios.post(DELIVERY_BASE_URL+ "/available", request),
    registerTimeSlot: (request) => axios.post(DELIVERY_BASE_URL, request),
    getCountOfPickUpDelivery: (startDate, endDate) => axios.get(`${DELIVERY_BASE_URL}/count?startDate=${startDate}&endDate=${endDate}`),
    getAllPickUpDeliveries: () => axios.get(`${DELIVERY_BASE_URL}/all`),
    updateDeliveryStatus: (id, request) => axios.put(`${DELIVERY_BASE_URL}/${id}/status`, request)
};

export default PickUpDeliveryAPI;
