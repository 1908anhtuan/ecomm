import React, { useState, useEffect } from "react";
import "../components/BuyHistory.css";
import img from "../img/laptop.jpg";
import BiddingAdsAPI from "../apis/BiddingAdsApis";
import RegularAdsAPI from "../apis/RegularAdsApis";
import axios from "axios";
import DeliverySelectionForm from "./DeliverySelectionForm";

export default function BuyHistory() {
  const [adType, setAdType] = useState("bidding");
  const [adsList, setAdsList] = useState([]);
  const [selectedAd, setSelectedAd] = useState(null);
  const [showDeliverySelectionForm, setShowDeliverySelectionForm] = useState(false);
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

  const handleDeliverySelection = () => {
    setShowDeliverySelectionForm(true);
  };

  const closeDeliverySelection = () => {
    setShowDeliverySelectionForm(false);
  };

  useEffect(() => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);

    const fetchData = async () => {
      try {
        const userId = sessionStorage.getItem("accountId");
        let response;

        if (adType === "bidding") {
          response = await BiddingAdsAPI.boughtBidAds(userId, config);
          setAdsList(response.data.personalBiddingAdsList);
        } else if (adType === "regular") {
          response = await RegularAdsAPI.boughtRegularAds(userId, config);
          setAdsList(response.data.personalRegularAdsList);
        }

        console.log(JSON.stringify(response.data));
      } catch (error) {
        console.error(error);
      }
    };

    fetchData();
  }, [adType]);

  const handleOptionChange = (event) => {
    setAdType(event.target.value);
    handleHideDetails();
  };

  const handleSeeDetails = (ad) => {
    setSelectedAd(ad);
  };

  const handleHideDetails = () => {
    setSelectedAd(null);
  };

  const renderForm = () => {
    if (selectedAd) {
      if (adType === "bidding") {
        return (
          <div className="detailsForm">
            <h2>Bidding Ad Details</h2>
            <div>
              <label>Title:</label>
              <span>{selectedAd.title}</span>
            </div>
            <div>
              <label>Product Description:</label>
              <span>{selectedAd.productDescription}</span>
            </div>
            <div>
              <label>Seller:</label>
              <span>{selectedAd.advertiser.accountName}</span>
            </div>
            <div>
              <label>Lowest Bid Amount Allowed:</label>
              <span>{selectedAd.lowestBidAmountAllowed}</span>
            </div>
            <div>
              <label>Current Highest Bid:</label>
              <span>{selectedAd.currentHighestBid}</span>
            </div>
            <div>
              <label>Bid Expiration Date:</label>
              <span>{selectedAd.bidExpirationDate}</span>
            </div>
            <div>
              <label>Highest Bidder:</label>
              {selectedAd.highestBidder ? (
                <span>{selectedAd.highestBidder.accountName}</span>
              ) : (
                <span>No bidder yet</span>
              )}
            </div>
            {selectedAd.deliveryType == null ? (
        <button onClick={handleDeliverySelection}>Select Delivery</button>
      ) : (
        <div>
              <label>Delivery status:</label>
              <span>{selectedAd.deliveryType.deliveryStatus}</span>
            </div>
      )}
                  <button onClick={handleHideDetails}>Close</button>

          </div>
        );
      } else if (adType === "regular") {
        return (
          <div className="detailsForm">
            <h2>Regular Ad Details</h2>
            <div>
              <label>Title:</label>
              <span>{selectedAd.title}</span>
            </div>
            <div>
              <label>Product Description:</label>
              <span>{selectedAd.productDescription}</span>
            </div>
            <div>
              <label>Seller:</label>
              <span>{selectedAd.advertiser.accountName}</span>
            </div>
            <div>
              <label>Price:</label>
              <span>{selectedAd.price}</span>
            </div>
           
            <div>
              <label>Condition:</label>
              <span>{selectedAd.condition}</span>
            </div>
            <div>
              <label>Verification:</label>
              <span>{selectedAd.verification}</span>
            </div>
            {selectedAd.buyer && (
              <div>
              <label>Buyer:</label>
              <span>{selectedAd.buyer.accountName}</span>
            </div>
            )}
      {selectedAd.deliveryType == null ? (
        <button onClick={handleDeliverySelection}>Select Delivery</button>
      ) : (
        <div>
              <label>Delivery status:</label>
              <span>{selectedAd.deliveryType.deliveryStatus}</span>
            </div>
      )}
                  <button onClick={handleHideDetails}>Close</button>
          </div>
        );
      }
    }
  };

  return (
    <div className="container2">
      <div className="adList">
        <div>
          <label htmlFor="adType">Choose Ad Type:</label>
          <select id="adType" value={adType} onChange={handleOptionChange}>
            <option value="bidding">Bidding</option>
            <option value="regular">Regular</option>
          </select>
        </div>
        <div className="adContainer">
          {adsList.map((ad, index) => (
            <div className="card" key={index}>
              <div className="imgBx">
                <img src={img} alt="Product Image" />
              </div>
              <div className="contentBx">
                <h2>{ad.title}</h2>
                <p>{ad.description}</p>
                <button onClick={() => handleSeeDetails(ad)}>See Details</button>
              </div>
            </div>
          ))}
        </div>
      </div>
      <div className="detailsContainer">
        {renderForm()}
      </div>
      {showDeliverySelectionForm && (
      <div className="popup">
        <DeliverySelectionForm closeForm={closeDeliverySelection} advertId={selectedAd} />
      </div>
      )}
    </div>  
  );
}