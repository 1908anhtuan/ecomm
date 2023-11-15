import axios from "axios";

const FACILITIES_BASE_URL = "http://localhost:8080/facilities";

const FacilityAPI = {
  getFacilities: () => axios.get(`${FACILITIES_BASE_URL}`)
}

export default FacilityAPI;