import axios from "axios";
const CATEGORY_BASE_URL = "http://localhost:8080/categories";

const CategoryAPI = {
    getAllCategories: () => axios.get(CATEGORY_BASE_URL)
};

export default CategoryAPI;
