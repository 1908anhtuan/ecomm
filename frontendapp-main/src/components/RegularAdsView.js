import React, { useState, useEffect } from "react";
import RegularAdsAPI from "../apis/RegularAdsApis";
import "../components/RegularAdsView.css"
import img from "../img/laptop.jpg"
import { AuthContext } from "./AuthContext";
import axios from 'axios';

import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link,
    Routes,
    NavLink
  } from "react-router-dom";

  
  function RegularAdsView(props) {
    const [regularAds, setRegularAds] = useState([]);
    const [filteredRegularAds, setFilteredRegularAds] = useState([]);
    
    useEffect(() => {
      const fetchRegularAds = async () => {
        try {
          const response = await RegularAdsAPI.getAll();
          setRegularAds(response.data.regularAdvertList);
          console.log(JSON.stringify(response.data)); 
          console.log("1111111111111111111");
        } catch (error) {
          console.log("Error occurred while fetching regular ads:", error.message);
        }
      };

      fetchRegularAds();
    }, [props.selectedCategory]);
  
    useEffect(() => {
      const filteredAds = regularAds.filter((ad) => {
        return (
          ad.category.id == props.selectedCategory &&
            ad.verification == "Verified" &&
            ad.adStatus == "Available" &&
            ad.sold == false && ad.ended == false
        );
      });
      setFilteredRegularAds(filteredAds);
      console.log("222222222222");
    }, [regularAds, props.selectedCategory]);
  

    
   
    return (
      <div className="regular-ads-container">
        {filteredRegularAds.map((ad) => (
          <div className="regular-ad-card" key={ad.id}>
            <img src={img} alt="Product Image" />
            <h2>{ad.title}</h2>
            <p>Price: ${ad.price}</p>
            <Link to={`/regularads/${ad.id}`} className="details-button">
              Go to Details
            </Link>
          </div>
        ))}
      </div>
    );
  }
  
  
  export default RegularAdsView;