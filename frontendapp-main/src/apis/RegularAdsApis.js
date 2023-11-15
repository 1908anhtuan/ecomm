import axios from "axios";
const REGULAR_ADS_BASE_URL = "http://localhost:8080/regularAds";

const RegularAdsAPI = {
    getAll: () => axios.get(REGULAR_ADS_BASE_URL),
    getProductWithId: (id) => axios.get(`${REGULAR_ADS_BASE_URL}/${id}`),
    createRegularAdvert: (request) => axios.post(REGULAR_ADS_BASE_URL, request),
    buyRegularAdvert: (advertId, request) => axios.post(`${REGULAR_ADS_BASE_URL}/${advertId}/buy`, request),
    getAllByFilter: (categoryId, verificationStatus, adStatus) => axios.get(REGULAR_ADS_BASE_URL + "/filter", {
        params: {
            categoryId: categoryId,
            verificationStatus: verificationStatus,
            adStatus: adStatus
        }
    }),
    updateAdProperty: (advertId, request) => axios.patch(`${REGULAR_ADS_BASE_URL}/${advertId}/status`, request),
    deleteAdvert: (id) => axios.delete(`${REGULAR_ADS_BASE_URL}/${id}`),
    boughtRegularAds: (userId) => axios.get(`${REGULAR_ADS_BASE_URL}/history/${userId}`)


};

export default RegularAdsAPI;