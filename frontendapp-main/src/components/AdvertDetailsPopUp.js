import React from "react";
import "./AdvertDetailsPopUp.css";
import { useState, useEffect } from 'react';
import img from '../img/laptop.jpg'
import RegularAdsAPI from '../apis/RegularAdsApis';
import BiddingAdsAPI from '../apis/BiddingAdsApis';
import axios from "axios";

const AdvertFormPopup = ({ adData, onClose }) => {

    const [updatedVerification, setUpdatedVerification] = useState(adData.verification);
    const [updatedAdvertStatus, setUpdatedAdvertStatus] = useState(adData.adStatus);
    const [notification, setNotification] = useState(null);


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
  const handleSubmit = (event) => {
    event.preventDefault();
  };
  useEffect(() => {
    console.log(updatedAdvertStatus + updatedVerification);
  })
  const renderRegularAdvertFields = () => {
    return (
      <div>
        <label>Price:</label>
        <span>{adData.price}</span>
        <label>Buyer:</label>
        <span>{adData.buyer ? adData.buyer.accountName : "Buyer not found"}</span>
      </div>
    );
  };


  const onDelete = async () => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);
    try {
      const API = adData.advertType === "Regular" ? RegularAdsAPI : BiddingAdsAPI;
      const response = await API.deleteAdvert(adData.id, config);

      setNotification({ message: response.data.stringResponse, color: "yellow" });

    } catch (error) {
      console.error("Error deleting ad:", error);
    }
  };
  const onUpdate = async (  ) => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);
    try {
      console.log(adData.advertType);
      const API = adData.advertType === "Regular" ? RegularAdsAPI : BiddingAdsAPI;
  
      const request = {
        verificationStatus: updatedVerification,
        advertStatus: updatedAdvertStatus
      };

      
      const response = await API.updateAdProperty(adData.id, request);
  
      if(response.data.isUpdated ? setNotification({ message: "Updated successfully", color: "green" }) : setNotification({ message: "Update not successful", color: "red" }));
      console.log("Ad property updated successfully:", response.data);
     
     } catch (error) {
      setNotification({ message: "Error updating ad property", color: "red" });
      console.error("Error updating ad property:", error);
    }
  };
  const renderBiddingAdvertFields = () => {
    return (
      <div>
        <label>Lowest Bid Amount Allowed:</label>
        <span>{adData.lowestBidAmountAllowed}</span>
        <label>Current Highest Bid:</label>
        <span>{adData.currentHighestBid}</span>
        <label>Initial Bid Price:</label>
        <span>{adData.initialBidPrice}</span>
        <label>Bin Price:</label>
        <span>{adData.binPrice}</span>
      </div>
    );
  };

  useEffect(() => {
    console.log(JSON.stringify(adData));
  }, []);

  return (
    <div className="popup-container">
    <img src={img} alt="Product Image" />
      <div className="popup-form">
        
        <h2>Advert Form</h2>
        <form onSubmit={handleSubmit}>
        {notification && (
        <div className={`notification ${notification.color}`}>
          {notification.message}
        </div>
        )}
          <div>
            <label>Title:</label>
            <span>{adData.title}</span>
          </div>
          <div>
            <label>Product Description:</label>
            <span>{adData.productDescription}</span>
          </div>
          <div>
            <label>Verification status:</label>
            <select className="advert-status-select" value={adData.verification} onChange={(e) => {setUpdatedVerification(e.target.value);
                                                                  adData.verification = e.target.value }}>
              <option value="Pending">Pending</option>
              <option value="Verified">Verified</option>
              <option value="Unqualified">Unqualified</option>
            </select>
          </div>
          <div>
            <label>Advert status:</label>
            <select className="advert-status-select" value={adData.adStatus} onChange={(e) => {setUpdatedAdvertStatus(e.target.value);
                                                              adData.adStatus = e.target.value}}>
              <option value="Available">Available</option>
              <option value="Unavailable">Unavailable</option>
            </select>
          </div>
          {/* Render additional fields based on advert type */}
          {adData.advertType === "Regular" && renderRegularAdvertFields()}
          {adData.advertType === "Bidding" && renderBiddingAdvertFields()}
          <button onClick={onUpdate}>Update</button>
          <button onClick={onDelete} className="delete-button">Delete</button>
          <button className = "onCloseButton" onClick={onClose}>Close</button>
        </form>
      </div>
    </div>
  );
};

export default AdvertFormPopup;