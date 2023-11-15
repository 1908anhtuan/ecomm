import React, { useState, useEffect } from "react";
import BiddingAdsAPI from "../apis/BiddingAdsApis";
import "../components/BiddingAdsView.css"
import img from "../img/laptop.jpg"
import axios from "axios";

import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link,
    Routes,
    NavLink
  } from "react-router-dom";

  function BiddingAdsView(props) {
    const [biddingAds, setBiddingAds] = useState([]);
    const [filteredBiddingAds, setFilteredBiddingAds] = useState([]);
    useEffect(() => {
      const fetchBiddingAds = async () => {
        try {
          const response = await BiddingAdsAPI.getAll();
          setBiddingAds(response.data.biddingAdvertList); 
          console.log(JSON.stringify(response.data.biddingAdvertList));
        } catch (error) {
          console.log("Error occurred while fetching Bidding ads:", error.message);
        }
      };

      fetchBiddingAds();
    }, [props.selectedCategory]);
  
    useEffect(() => {
        const currentDate = new Date();
        const filteredAds = biddingAds.filter((ad) => {
            const bidExpirationDate = new Date(ad.bidExpirationDate);
          return (
            ad.category.id == props.selectedCategory &&
            ad.verification == "Verified" &&
            ad.adStatus == "Available" &&
            bidExpirationDate > currentDate &&
            ad.ended == false
          );
        });
        setFilteredBiddingAds(filteredAds);
      }, [biddingAds, props.selectedCategory]);
  

    
   
    return (
      <div className="bidding-ads-container">
        {filteredBiddingAds.map((ad) => (
          <div className="bidding-ad-card" key={ad.id}>
            <img src={img} alt="Product Image" />
            <h2>{ad.title}</h2>
            <p>Price: ${ad.price}</p>
            <Link to={`/biddingads/${ad.id}`} className="details-button">
              Go to Details
            </Link>
          </div>
        ))}
      </div>
    );
  }
  
  
  export default BiddingAdsView;