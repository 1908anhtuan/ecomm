import axios from 'axios';
import { useState, useEffect, useCallback } from 'react';
import React from "react";
import "./AdvertVerification.css";
import CategoryAPI from '../apis/CategoryApis';
import Select from 'react-select';
import RegularAdsAPI from '../apis/RegularAdsApis';
import BiddingAdsAPI from '../apis/BiddingAdsApis';
import AdvertFormPopup from '../components/AdvertDetailsPopUp'
import { Prev } from 'react-bootstrap/esm/PageItem';

export default function AdvertVerificationForm() {

    const [fetchedCategories, setFetchedCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [selectedAdType, setSelectedAdType] = useState(null);
    const [selectedAdStatus, setSelectedAdStatus] = useState(null);
    const [selectedVerificationStatus, setSelectedVerificationStatus] = useState(null);
    const [regularAds, setRegularAds] = useState([]);
    const [biddingAds, setBiddingAds] = useState([]);
    const [adTypes, setAdTypes] = useState([]);
    const [adStatuses, setAdStatuses] = useState([]);
    const [adVerificationStatuses, setAdVerificationStatuses] = useState([]);
    const adTypeList = ["Bidding", "Regular"];
    const AdStatusList = ["Available", "Unavailable"];
    const adVerificationStatusList = ["Pending", "Verified", "Unqualified"];
    const [filteredAds, setFilteredAds] = useState([]);
    const [isUpdated, setIsUpdated] = useState(0);

  
    //popup
    const [selectedAdData, setSelectedAdData] = useState(null);
    const [showPopUpStatus, setShowPopUpStatus] = useState(false);
    

   

  
    useEffect(() => {
        setSelectedAdType(adTypes[1]); 
        setSelectedAdStatus(adStatuses[0]); 
        setSelectedVerificationStatus(adVerificationStatuses[0]);
        setSelectedCategory(fetchedCategories[0]); 
      }, [adTypes, adStatuses, adVerificationStatuses, fetchedCategories]);
    
    useEffect(() => {
      console.log("regularAds:", regularAds);
    }, [regularAds]);

    

    
    useEffect(() => {
        const fetchCategories = async () => {
            try{
                const response = await CategoryAPI.getAllCategories();
                setFetchedCategories(response.data.categoryList);
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
        const adTypeOptions = adTypeList.map((type) => ({ value: type, label: type }));
        setAdTypes(adTypeOptions);
    
        const adStatusOptions = AdStatusList.map((status) => ({ value: status, label: status }));
        setAdStatuses(adStatusOptions);
    
        const adVerificationStatusOptions = adVerificationStatusList.map((status) => ({
          value: status,
          label: status,
        }));
        setAdVerificationStatuses(adVerificationStatusOptions);
      }, []);


    const handleCategoryChange = (selectedOption) => {
        setSelectedCategory(selectedOption);
    };
    const handleButtonClick = (jobId) => {
            console.log(`Button clicked for Job Id: ${jobId}`);
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
      
        const fetchFilteredAds = async () => {
          try {
            if (selectedAdType && selectedAdType.value === 'Regular') {
              const response = await RegularAdsAPI.getAllByFilter(
                selectedCategory ? selectedCategory.id : null,
                selectedVerificationStatus ? selectedVerificationStatus.value : null,
                selectedAdStatus ? selectedAdStatus.value : null
              );
              setFilteredAds(response.data.filteredRegularAdList);
              console.log(response.data.filteredRegularAdList);
            } else if (selectedAdType && selectedAdType.value === 'Bidding') {
              const response = await BiddingAdsAPI.getAllByFilter(
                selectedCategory ? selectedCategory.id : null,
                selectedVerificationStatus ? selectedVerificationStatus.value : null,
                selectedAdStatus ? selectedAdStatus.value : null
              );
              setFilteredAds(response.data.filteredBiddingAdList);
              console.log(response.data.filteredBiddingAdList);
            }
          } catch (error) {
            console.log("Error occurred while fetching filtered ads:", error.message);
          }
        };
      
        fetchFilteredAds();
      }, [selectedCategory, selectedVerificationStatus, selectedAdStatus, selectedAdType, isUpdated]);



      const showDetailsForm = (props) => {
        setSelectedAdData(props);
        setShowPopUpStatus(true);

        console.log(props);
      }


      const closeForm = () => {
        setShowPopUpStatus(false);
        setIsUpdated(Prev => Prev +1);
      }

    
      const renderTable = () => {
        if (selectedAdType && selectedAdType.value === 'Regular') {
          return (
            <ul className="responsive-table">
              <li className="table-header">
                <div className="col col-1">Id</div>
                <div className="col col-2">Title</div>
                <div className="col col-3">Description</div>
                <div className="col col-4">Status</div>
              </li>
              {filteredAds.map((ad) => (
                <li className="table-row" key={ad.id} onClick={() => showDetailsForm(ad)}>
                  <div className="col col-1" data-label="Id">{ad.id}</div>
                  <div className="col col-2" data-label="Title">{ad.title}</div>
                  <div className="col col-3" data-label="Description">{ad.productDescription}</div>
                  <div className="col col-4" data-label="Status">{ad.adStatus}</div>
                  <div className="col col-5">
                    {/* Buttons removed */}
                  </div>
                </li>
              ))}
            </ul>
          );
        }
      
        if (selectedAdType && selectedAdType.value === 'Bidding') {
          return (
            <ul className="responsive-table">
              <li className="table-header">
                <div className="col col-1">Id</div>
                <div className="col col-2">Title</div>
                <div className="col col-3">Description</div>
                <div className="col col-4">Status</div>
              </li>
              {filteredAds.map((ad) => (
                <li className="table-row" key={ad.id} onClick={() => showDetailsForm(ad)}>
                  <div className="col col-1" data-label="Id">{ad.id}</div>
                  <div className="col col-2" data-label="Title">{ad.title}</div>
                  <div className="col col-3" data-label="Description">{ad.productDescription}</div>
                  <div className="col col-4" data-label="Status">{ad.adStatus}</div>
                  <div className="col col-5">
                    {}
                  </div>
                </li>
              ))}
            </ul>
          );
        }
      
        return null; 
      };







      const customStyles = {
        control: (provided) => ({
          ...provided,
          fontSize: '18px',
          minHeight: '16px',
          width: '465px'
        }),
      };
  return (
    <div>
    <div className="combobox-container">
        <div className="combobox-wrapper">
          <label htmlFor="adType" className="label">
            Select Ad Type:
          </label>
          <Select
            id="adType"
            options={adTypes}
            value = {selectedAdType}
            placeholder="Select Ad Type"
            styles={customStyles} 
            onChange={(selectedOption) => setSelectedAdType(selectedOption)}

          />
        </div>

        <div className="combobox-wrapper">
          <label htmlFor="category" className="label">Select a category:</label>
          <Select
            id="category"
            value={selectedCategory}
            onChange={handleCategoryChange}
            options={fetchedCategories}
            placeholder="From all categories"
            getOptionLabel={(category) => category.name}
            getOptionValue={(category) => category.id}
            className="custom-select"
            classNamePrefix="custom-select"
            styles={customStyles} 

          />
        </div>

        <div className="combobox-wrapper">
          <label htmlFor="adStatus" className="label">
            Select Ad Status:
          </label>
          <Select
            id="adStatus"
            value={selectedAdStatus}
            options={adStatuses}
            placeholder="Select Ad Status"
            styles={customStyles} 
            onChange={(selectedOption) => setSelectedAdStatus(selectedOption)}

          />
        </div>

        <div className="combobox-wrapper">
          <label htmlFor="adVerificationStatus" className="label">
            Select Verification Status:
          </label>
          <Select
            id="adVerificationStatus"
            value={selectedVerificationStatus}
            onChange={(selectedOption) => setSelectedVerificationStatus(selectedOption)}
            options={adVerificationStatuses}
            placeholder="Select Verification Status"
            styles={customStyles} 

          />
        </div>
      </div>
      <div className="container">
  {renderTable()}
  {showPopUpStatus && <AdvertFormPopup adData={selectedAdData} onClose={closeForm} />}
  </div>
  </div>
  );
}
