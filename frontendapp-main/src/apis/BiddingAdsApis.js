import axios from "axios";
const BIDDING_ADS_BASE_URL = "http://localhost:8080/biddingAds";

const BiddingAdsAPI = {
    getAll: () => axios.get(BIDDING_ADS_BASE_URL),
    getProductWithId: (id) => axios.get(`${BIDDING_ADS_BASE_URL}/${id}`),
    createBiddingAdvert: (request) => axios.post(BIDDING_ADS_BASE_URL, request),
    buyNowBidding: (advertId, request) => axios.post(`${BIDDING_ADS_BASE_URL}/${advertId}/buynow`, request),
    getAllByFilter: (categoryId, verificationStatus, adStatus) => axios.get(BIDDING_ADS_BASE_URL + "/filter", {
        params: {
            categoryId: categoryId,
            verificationStatus: verificationStatus,
            adStatus: adStatus
        }
    }),
    updateAdProperty: (advertId, request) => axios.patch(`${BIDDING_ADS_BASE_URL}/${advertId}/status`, request),
    deleteAdvert: (id) => axios.delete(`${BIDDING_ADS_BASE_URL}/${id}`),
    bid: (advertId, request) => axios.post(`${BIDDING_ADS_BASE_URL}/${advertId}/bid`, request),
    boughtBidAds: (userId) => axios.get(`${BIDDING_ADS_BASE_URL}/history/${userId}`)
};

export default BiddingAdsAPI;