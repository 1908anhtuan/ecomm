import React, { useState, useEffect, useContext } from "react";
import { useParams, Link } from "react-router-dom";
import BiddingAdsAPI from "../apis/BiddingAdsApis";
import "./BidProductDetailsForm.css";
import img from "../img/laptop.jpg";
import axios from "axios";
import { AuthContext } from "./AuthContext";
import BidPopUpForm from "../components/BidPopUpForm"
import ChatComponent from "./ChatComponent";

function BidProductDetailsForm() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [showBidForm, setShowBidForm] = useState(false);
  const [showChatPopup, setShowChatPopup] = useState(false);

  const handleCloseChatPopup = () => {
    setShowChatPopup(false);
  };
  const handleChat = async () => {
    setShowChatPopup(true);
  };

  const ChatPopup = ({ onClose }) => {
    return (
      <div className="chat-popup">
        <div className="chat-container">
          <ChatComponent receiverIdParam={product.advertiser.id} />
        </div>
        <button className="close-button" onClick={onClose}>
          x
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
      console.log(token);
      setAxiosAuthHeader(token);
      const config = createConfig(token);
    const fetchProductDetails = async () => {
      try {
        const response = await BiddingAdsAPI.getProductWithId(id, config);
        console.log(product);
        setProduct(response.data.biddingAdvert);
        console.log(response.data);
        console.log(sessionStorage.getItem("accessToken"));
      } catch (error) {
        console.log(
          "Error occurred while fetching product details:",
          error.message
        );
      }
    };

    fetchProductDetails();
    const refreshInterval = setInterval(fetchProductDetails, 10000);

    return () => clearInterval(refreshInterval);
  }, []);
  
  if (!product) {
    return <div>Loading...</div>;
  }
  const handleBid = () => {
    setShowBidForm(true);
    console.log("handle bid");
  };
  const bidSubmit = async (bidAmount) => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);
  
    try {
      const userId = sessionStorage.getItem("accountId");
      const requestObject = {
        userId: userId,
        bidAmount: bidAmount
      };
  
      const response = await BiddingAdsAPI.bid(id, requestObject, config);
      console.log("Bid successfully:", response.data.regularAdvert);
      alert("Successfully placed a bid: " + response.data.message);
      handleCloseBidForm(); 
    } catch (error) {
      console.log("Error occurred while placing a bid:", error.message);
      alert("Error occurred while placing a bid: " + error);
    }
  };

  const handleCloseBidForm = () => {
    setShowBidForm(false);
  };
  const handleBuyNow = async() => {
    const token = sessionStorage.getItem("accessToken");
      setAxiosAuthHeader(token);
      const config = createConfig(token);
  
      try {
        
        const userId = sessionStorage.getItem("accountId"); 

        const requestObject = {
        userId: userId,
        };

        const response = await BiddingAdsAPI.buyNowBidding(id, requestObject, config);
        console.log("Ad bought successfully:", response.data.regularAdvert);
        alert("successfully");
      } catch (error) {
        console.log("Error occurred while buying the ad:", error.message);
      }
  };

  return (
    <div className="product-details">
      <h1>Product Details</h1>
      <div className="product-image">
        <img src={img} alt="Product Image" />
      </div>
      <h2 className="product-title">{product.title}</h2>
      <p className="product-price">Price: ${product.binPrice}</p>
      <p className="product-description">
        Description: {product.productDescription}
      </p>
      <p>
        <label>Lowest Bid Amount Allowed:</label>
        <span>{product.lowestBidAmountAllowed}</span>
      </p>
      <p>
        <label>Current Highest Bid:</label>
        <span>{product.currentHighestBid}</span>
      </p>
      <p>
        <label>Initial Bid Price:</label>
        <span>{product.initialBidPrice}</span>
      </p>
      <p>
        <label>Bin Price:</label>
        <span>{product.binPrice}</span>
      </p>
      {isLoggedIn ? (
        <div>
          <button className="bid-button" onClick={handleBid}>
            Place Bid
          </button>
          <button className="buynow-button" onClick={handleBuyNow}>
            Buy Now
          </button>
          <button className="chat-button" onClick={handleChat}>
                Chat
          </button>
          {showBidForm && (
            <BidPopUpForm onClose={handleCloseBidForm} onSubmit={bidSubmit} />
          )}
        </div>
      ) : (
        <Link to="/login" className="signin-link">
          Please sign in
        </Link>
      )}
      {showChatPopup && <ChatPopup onClose={handleCloseChatPopup} />}
    </div>
  );
}

export default BidProductDetailsForm;