import React, { useState, useEffect, useContext } from "react";
import RegularAdsAPI from "../apis/RegularAdsApis";
import "./RegProductDetailsForm.css";
import img from "../img/laptop.jpg";
import axios from "axios";
import { AuthContext } from "./AuthContext";
import { useParams, Link } from "react-router-dom";
import ChatComponent from "./ChatComponent";

function RegProductDetailsPage() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [showChatPopup, setShowChatPopup] = useState(false);

  const handleCloseChatPopup = () => {
    setShowChatPopup(false);
  };

  const ChatPopup = ({ onClose }) => {
    return (
      <div className="chat-popup">
        <div className="chat-container">
          <ChatComponent receiverIdParam={product.advertiser.id} />
        </div>
        <button className="close-button" onClick={onClose}>
          Close
        </button>
      </div>
    );
  };

  const createConfig = (token) => {
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };

  const setAxiosAuthHeader = (token) => {
    if (token) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    } else {
      delete axios.defaults.headers.common["Authorization"];
    }
  };

  useEffect(() => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);

    const fetchProductDetails = async () => {
      try {
        const response = await RegularAdsAPI.getProductWithId(id, config);
        setProduct(response.data.regularAdvert);
      } catch (error) {
        console.log("Error occurred while fetching product details:", error.message);
      }
    };

    fetchProductDetails();
  }, []);

  if (!product) {
    return <div>Loading...</div>;
  }

  const handleChat = async () => {
    setShowChatPopup(true);
  };

  const handleBuy = async () => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);

    try {
      const userId = sessionStorage.getItem("accountId");
      const requestObject = {
        userId: userId,
      };

      const response = await RegularAdsAPI.buyRegularAdvert(id, requestObject, config);
      console.log("Ad bought successfully:", response.data.regularAdvert);
      alert("Successfully");
    } catch (error) {
      console.log("Error occurred while buying the ad:", error.message);
    }
  };

  return (
    <div className="product-details">
      <h1>Product Details</h1>
      <div className="product-container">
        <div className="product-image">
          <img src={img} alt="Product Image" />
        </div>
        <div className="product-info">
          <h2 className="product-title">{product.title}</h2>
          <p className="product-price">Price: ${product.price}</p>
          <p className="product-description">Description: {product.productDescription}</p>
          {isLoggedIn ? (
            <div className="actions-container">
              <button className="buy-button" onClick={handleBuy}>
                Buy
              </button>
              <button className="buy-button" onClick={handleChat}>
                Chat
              </button>
            </div>
          ) : (
            <Link to="/login" className="signin-link">
              Please sign in
            </Link>
          )}
        </div>
      </div>
      {showChatPopup && <ChatPopup onClose={handleCloseChatPopup} />}
    </div>
  );
}

export default RegProductDetailsPage;