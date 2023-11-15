import React from "react";
import { useState, useEffect } from 'react';
import RegularAdsView from "./RegularAdsView";
import BiddingAdsView from "./BiddingAdsView";
import CategoryAPI from "../apis/CategoryApis";
import "./AdvertFilteringTools.css";
import axios from "axios";

export default function AdvertFilteringTool(){
    const [fetchedCategories, setFetchedCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState("");
    const [selectedAdvertType, setSelectedAdvertType] = useState("Regular");

    const AdvertType = ["Bidding", "Regular"];
    useEffect(() => {
        const fetchCategories = async () => {
            try{
                const response = await CategoryAPI.getAllCategories();
                setFetchedCategories(response.data.categoryList);
                if(response.data.categoryList.length > 0){
                    setSelectedCategory = response.data.categoryList[1].id;
                }
                console.log(response.data.categoryList);
            }
            catch(error)
            {
                console.log("Failed to fetch categories", error);
            }
        }
        fetchCategories();
    }, [])


    useEffect(() => {
        console.log(selectedCategory);
    })
    const handleTypeChange = (e) => {
        setSelectedAdvertType(e.target.value);
        console.log(e.target.value);
    };

    const handleCategoryChange = (e) => {
        setSelectedCategory(e.target.value);
        console.log(e.target.value);
    }

    const renderFilter = () => {
        return (
          <div className="filteringContainer">
            <div className="combobox">
            <label className="filterLabel">Advert type:</label>
              <select value={selectedAdvertType} onChange={handleTypeChange}>
                {AdvertType.map((adType, index) => (
                  <option key={index} value={adType}>{adType}</option>
                ))}
              </select>
            </div>
            <div className="categoryButtons">
              {fetchedCategories.map((category, index) => (
                <button
                  key={category.id}
                  className={`categoryButton ${Number(selectedCategory) === category.id ? 'active' : ''}`}
                  value={category.id}
                  onClick={handleCategoryChange}
                >
                  {category.name}
                </button>
              ))}
            </div>
          </div>
        );
      };
    const renderAds = () => {
        if (selectedAdvertType === 'Bidding') {
            return <BiddingAdsView selectedCategory={selectedCategory} />;
          } else if (selectedAdvertType === 'Regular') {
            return <RegularAdsView selectedCategory={selectedCategory} />;
          }
      };
    
    return(
        <div>
           {renderFilter()}
           {renderAds()}
        </div>
    );
} 