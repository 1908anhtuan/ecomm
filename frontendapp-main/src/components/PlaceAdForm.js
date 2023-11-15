import axios from 'axios';
import { useState, useEffect } from 'react';
import CategoryAPI from '../apis/CategoryApis';
import "../components/PlaceAdForm.css";
import { render } from '@testing-library/react';
import img from "../img/laptop.jpg";
import BiddingAdsApis from "../apis/BiddingAdsApis";
import RegularAdsApis from "../apis/RegularAdsApis";
import LoginAPI from '../apis/LoginApis';

export default function PlaceAdForm(){
     
    //user
    const [user, setUser] = useState("");
    //products state
    const [categoryId, setCategoryId] = useState("");
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [adType, setAdType] = useState("Bidding");

    const [fetchedCategories, setFetchedCategories] = useState([]);
    const [step, setStep] = useState(1);
    const [error, setError] = useState();


    const [condition, setCondition] = useState("New");
    //regular
    const [price, setPrice] = useState(0);
    //bidding
    const [initialBidPrice, setInitialBidPrice] = useState("");
    const [binPrice, setBinPrice] = useState("");
    const [bidExpirationDay, setBidExpirationDay] = useState("");
    const [lowestBidAmountAllowed, setLowestBidAmountAllowed] = useState("");

    const conditions = ["New", "Used"];
    const adTypeList = ["Bidding", "Regular"];

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
      const fetchUser = async () => {
        const accountName = sessionStorage.getItem("accountName");
    
        try {
          const response = await LoginAPI.getAccountByAccountName(accountName);
          console.log(JSON.stringify(response.data));
          setUser(response.data.account);
        } catch (error) {
          console.log("Failed to fetch user account", error.response);
        }
      };
    
      fetchUser();
    }, []);


    useEffect(() => {
        const fetchCategories = async () => {
            try{
                const response = await CategoryAPI.getAllCategories();
                setFetchedCategories(response.data.categoryList);
                console.log(response.data.categoryList);
                const initialCategory = response.data.categoryList.find((category) => category.id == 1);
                setCategoryId(initialCategory.id);
            }
            catch(error)
            {
                console.log("Failed to fetch categories", error);
            }
        }
        fetchCategories();
    }, [])
    
    const createRegularAdvert = async () => {
      const token = sessionStorage.getItem("accessToken");
      setAxiosAuthHeader(token);
      const config = createConfig(token);
      const selectedCategory = fetchedCategories.find((category) => category.id == categoryId);
      console.log(JSON.stringify(selectedCategory));
      const request = {
        title: title,
        productDescription: description,
        category: selectedCategory,
        advertiser: user,
        price: price,
        condition: condition
      }
      console.log(JSON.stringify(request));
      try{
        
        const response = await RegularAdsApis.createRegularAdvert(request, config);
        console.log(JSON.stringify(response));
        alert("Successfully");
      }
      catch(error){
        console.error(error);
      }
    }

    const createBiddingAdvert = async () => {
      const token = sessionStorage.getItem("accessToken");
      setAxiosAuthHeader(token);
      const config = createConfig(token);
      const selectedCategory = fetchedCategories.find((category) => category.id == categoryId);
      const request = {
        title: title,
        productDescription: description,
        category: selectedCategory,
        advertiser: user,
        condition: condition,
        lowestBidAmountAllowed: lowestBidAmountAllowed,
        initialBidPrice: initialBidPrice,
        binPrice: binPrice,
        bidExpirationDate: bidExpirationDay
      }
      try{
        
        const response = await BiddingAdsApis.createBiddingAdvert(request, config);
        console.log(JSON.stringify(response));
        alert("Successfully");
      }
      catch(error){
        console.error(error);
      }
    }




    const handleTitleChange = (event) => {
      if (event.target.value === '') {
        setError('Please enter title');
      }else{
        setError('');
      }
      setTitle(event.target.value);
      };
    
    const handleDescriptionChange = (event) => {
      if (event.target.value === '') {
        setError('Please enter description');
      }else{
        setError('');
      }
        
      setDescription(event.target.value);
      console.log(error);
    }
      const handleCategoryChange = (event) => {
        setCategoryId(event.target.value);
      };


      const handleAdTypeChange = (event) => {
        setAdType(event.target.value);
      }

      const handleInitialBidPriceChange = (event) => {
        const inputPrice = event.target.value;
        if (inputPrice.trim() === "") {
          setError("Initial Bid Price is required.");
        } else if (isNaN(inputPrice)) {
          setError("Initial Bid Price must be a valid number.");
        } else if (parseFloat(inputPrice) < 0) {
          setError("Initial Bid Price must be a non-negative value.");
        } else if (inputPrice === "0") {
          setError("Initial Bid Price can't be 0");
        } else {
          setError("");
        }
        setInitialBidPrice(event.target.value);
      };
    
      const handleBinPriceChange = (event) => {
        const inputPrice = event.target.value;
        if (inputPrice.trim() === "") {
          setError("BIN Price is required.");
        } else if (isNaN(inputPrice)) {
          setError("BIN Price must be a valid number.");
        } else if (parseFloat(inputPrice) < 0) {
          setError("BIN Price must be a non-negative value.");
        } else if (inputPrice === "0") {
          setError("BIN Price can't be 0");
        } else {
          setError("");
        }
        setBinPrice(event.target.value);
      };
    
      const handleBidExpirationDayChange = (event) => {
        setBidExpirationDay(event.target.value);
      };
    
      const handleLowestBidAmountAllowedChange = (event) => {
        const inputAmount = event.target.value;
        if (inputAmount.trim() === "") {
          setError("Lowest Bid Amount Allowed is required.");
        } else if (isNaN(inputAmount)) {
          setError("Lowest Bid Amount Allowed must be a valid number.");
        } else if (parseFloat(inputAmount) < 0) {
          setError("Lowest Bid Amount Allowed must be a non-negative value.");
        } else if (inputAmount === "0") {
          setError("Lowest Bid Amount Allowed can't be 0");
        
        } else {
          setError("");
        }
        setLowestBidAmountAllowed(event.target.value);
      }; 

      const handlePriceChange = (event) => {
        const inputPrice = event.target.value;
        if (inputPrice.trim() === "") {
          setError("Price is required.");
        } else if (isNaN(inputPrice)) {
          setError("Price must be a valid number.");
        } else if (parseFloat(inputPrice) < 0) {
          setError("Price must be a non-negative value.");
        } else if(inputPrice == 0) {
          setError("Price can't be 0");
        } else {
          setError("");
        } 
        setPrice(event.target.value);
      }

      const handleConditionChange = (event) =>{
        setCondition(event.target.value);
      }

      const handleSubmitStep1 = (event) => {
        event.preventDefault();
    
  
        setStep(2);
      };
      const handleSubmitStep2 = (event) => {
        event.preventDefault();
    
        if (title.trim() === '') {
          setError('Please enter a title');
          return;
        }

        if (description.trim() === '') {
            setError('Please enter description');
            return;
          }
    
        setStep(3);
        console.log(title + description);
      };
      
      const handleSubmitStep3 = (event) => {
        event.preventDefault();
        setStep(4);
      }

      const handleSubmitStep4 = async (event) => {
        event.preventDefault();
        
        if (adType === "Bidding") {
          if (isNaN(parseFloat(initialBidPrice)) || parseFloat(initialBidPrice) <= 0) {
            alert("Please enter a valid Initial Bid Price.");
            return;
          }
          if (isNaN(parseFloat(binPrice)) || parseFloat(binPrice) <= 0) {
            alert("Please enter a valid BIN Price.");
            return;
          }
          if (!bidExpirationDay) {
            alert("Please select a Bid Expiration Day.");
            return;
          }
          if (isNaN(parseFloat(lowestBidAmountAllowed)) || parseFloat(lowestBidAmountAllowed) <= 0) {
            alert("Please enter a valid Lowest Bid Amount Allowed.");
            return;
          }
        } else {
          if (isNaN(parseFloat(price)) || parseFloat(price) <= 0) {
            alert("Please enter a valid Price.");
            return;
          }
        } 
        

        if(adType == "Bidding"){
          createBiddingAdvert();
        }else{
          createRegularAdvert();
        }
      };


      const renderStep1 = () => {
        return (
            <form className="step1" onSubmit={handleSubmitStep1}>
              <div>
                <label htmlFor="category">Category:</label>
                <select id="category" value={categoryId} onChange={handleCategoryChange}>
                  {fetchedCategories.map((category) => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
              </div>
              <button type="submit">Confirm</button>
            </form>
          );
      };
    
      const renderStep2 = () => {
        console.log(description);
        console.log(title);
        return (
          <form className="step2" onSubmit={handleSubmitStep2}>
            <div>
              <label htmlFor="title">Title:</label>
              <input
                type="text"
                id="title"
                value={title}
                onChange={handleTitleChange}
              />
            </div>
            <div>
              <label htmlFor="description">Description:</label>
              <textarea
                id="description"
                value={description}
                onChange={handleDescriptionChange}
              ></textarea>
            </div>
            <button type="submit">Confirm</button>
          </form>
        );
      };
  

      const renderStep3 = () => {
        return (
          <form className="step3" onSubmit={handleSubmitStep3}>
            <div>
              <label htmlFor="type">Advert type:</label>
              <select id="type" value={adType} onChange={handleAdTypeChange}>
                {adTypeList.map((adType) => (
                  <option key={adType.id} value={adType.id}>
                    {adType}
                  </option>
                ))}
              </select>
            </div>
            <button type="submit">Confirm</button>
          </form>
        );
      };

     


      const renderStep4 = () => {
        return (
          <div className="step4">
            <div className="image-container">
              <img src={img}  alt="Product Image" />
            </div>
            <div className="form-container2">
              {adType === "Bidding" ? renderBidding() : renderRegular()}
            </div>
          </div>
        );
      };
      
      const renderBidding = () => {
        return(
          <div>
            <form  onSubmit={handleSubmitStep4}>
            <div>
                <label htmlFor="initialBidPrice">Initial Bid Price:</label>
                <input type="number" id="initialBidPrice" value={initialBidPrice} onChange={handleInitialBidPriceChange} />
            </div>
            <div>
                <label htmlFor="binPrice">BIN Price:</label>
                <input type="number" id="binPrice" value={binPrice} onChange={handleBinPriceChange} />
            </div>
            <div>
                 <label htmlFor="bidExpirationDay">Bid Expiration Day:</label>
                 <input type="datetime-local" id="bidExpirationDay" value={bidExpirationDay} onChange={handleBidExpirationDayChange} />
            </div>
            <div>
                <label htmlFor="lowestBidAmountAllowed">Lowest Bid Amount Allowed:</label>
                <input type="number" id="lowestBidAmountAllowed" value={lowestBidAmountAllowed} onChange={handleLowestBidAmountAllowedChange} />
            </div>
            <label htmlFor="condition">Condition:</label>
                <select id="condition" value={condition} onChange={handleConditionChange}>
                  {conditions.map((condition) => (
                    <option key={condition.id} value={condition}>
                      {condition}
                    </option>
                  ))}
                </select>
                <div>
                <button type="submit">Confirm</button>
                </div>
            </form>
          </div>
        );
      };

      const renderRegular = () => {
        return(
          <div>
            <form  onSubmit={handleSubmitStep4}>
            <div>
                <label htmlFor="price">Price:</label>
                <input type="number" id="price" value={price} onChange={handlePriceChange} />
            </div>
            <div>
            <label htmlFor="condition">Condition:</label>
                <select id="condition" value={condition} onChange={handleConditionChange}>
                  {conditions.map((condition) => (
                    <option key={condition.id} value={condition}>
                      {condition}
                    </option>
                  ))}
                </select>
            </div>
            
            <button type="submit">Confirm</button>
            </form>
          </div>
        );
      };

      


      const viewRender = () => {
        if (step === 1) {
          return (
            <div className="form-row">
              {renderStep1()}
            </div>
          );
        } else if (step === 2) {
          return (
            <div className="form-row">
              {renderStep1()}
              {renderStep2()}
            </div>
          );
        } else if (step === 3){
          return (
            <div className="form-row">
              {renderStep1()}
              {renderStep2()}
              {renderStep3()}
            </div>
          ) 
        }else if (step === 4){
            return (
              <div>
              <div className="form-row">
              {renderStep1()}
              {renderStep2()}
              {renderStep3()}
            </div>
            <div>
              {renderStep4()}
            </div>
            </div>
            );
          }
      };




    return(
      <div className="form-container">
      {viewRender()}
      {error && <span className="error">{error}</span>}

      </div> 
    );
}