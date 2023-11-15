import React, { useState, useEffect } from 'react';
import HomeDeliveryAPI from '../apis/HomeDeliveryApis';
import PickUpDeliveryAPI from '../apis/PickUpDeliveryApis';
import './DeliveryComponent.css';
import axios from 'axios';

function DeliveryComponent() {
  const [deliveries, setDeliveries] = useState([]);
  const [deliveryType, setDeliveryType] = useState('Home');
  const [selectedDelivery, setSelectedDelivery] = useState(null);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [deliveryCount, setDeliveryCount] = useState(0);
  const [showAggregatedData, setShowAggregatedData] = useState(false);
  useEffect(() => {
    fetchData();
  }, [deliveryType]);
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
      console.log(token);
    } else {
      delete axios.defaults.headers.common["Authorization"];
    }
  };
  const fetchData = async () => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);
    try {
      let response;
      if (deliveryType === 'Home') {
        response = await HomeDeliveryAPI.getAllHomeDeliveries(config);
      } else if (deliveryType === 'Pickup') {
        response = await PickUpDeliveryAPI.getAllPickUpDeliveries(config);
      }
      console.log("fetched");
      setDeliveries(response.data.deliveries);
    } catch (error) {
      alert(error);
    }
  };


  const handleDeliveryTypeChange = (event) => {
    setDeliveryType(event.target.value);
    setSelectedDelivery(null);
  };

  const showDetailsForm = (delivery) => {
    setSelectedDelivery(delivery);
  };

  const renderTable = () => {
    if (deliveryType === 'Home') {
      return (
        <ul className="responsive-table">
          <li className="table-header">
            <div className="col col-1">Id</div>
            <div className="col col-2">Delivery date</div>
            <div className="col col-3">Start time</div>
            <div className="col col-4">End time</div>
            <div className="col col-5">Delivery status</div>
          </li>
          {deliveries.map((delivery) => (
            <li
              className="table-row"
              key={delivery.id}
              onClick={() => showDetailsForm(delivery)}
            >
              <div className="col col-1" data-label="Id">
                {delivery.id}
              </div>
              <div className="col col-2" data-label="Delivery date">
                {delivery.deliveryDate}
              </div>
              <div className="col col-3" data-label="Start time">
                {delivery.startTime}
              </div>
              <div className="col col-4" data-label="End time">
                {delivery.endTime}
              </div>
              <div className="col col-5" data-label="Status">
                {delivery.deliveryStatus}
              </div>
            </li>
          ))}
        </ul>
      );
    }

    if (deliveryType === 'Pickup') {
      return (
        <ul className="responsive-table">
          <li className="table-header">
            <div className="col col-1">Id</div>
            <div className="col col-2">Delivery date</div>
            <div className="col col-3">Start time</div>
            <div className="col col-4">End time</div>
            <div className="col col-5">Delivery status</div>
          </li>
          {deliveries.map((delivery) => (
            <li
              className="table-row"
              key={delivery.id}
              onClick={() => showDetailsForm(delivery)}
            >
              <div className="col col-1" data-label="Id">
                {delivery.id}
              </div>
              <div className="col col-2" data-label="Delivery date">
                {delivery.deliveryDate}
              </div>
              <div className="col col-3" data-label="Start time">
                {delivery.startTime}
              </div>
              <div className="col col-4" data-label="End time">
                {delivery.endTime}
              </div>
              <div className="col col-5" data-label="Status">
                {delivery.deliveryStatus}
              </div>
            </li>
          ))}
        </ul>
      );
    }

    return null;
  };

  const renderDetailsForm = () => {
    if (selectedDelivery) {
      const handleStatusChange = (event) => {
        const newStatus = event.target.value;
        setSelectedDelivery((prevDelivery) => ({
          ...prevDelivery,
          deliveryStatus: newStatus
        }));
      };
  
      const handleUpdate = async () => {
        const token = sessionStorage.getItem("accessToken");
        setAxiosAuthHeader(token);
        const config = createConfig(token);
        try {
          let response;
          if (deliveryType === 'Home') {
            response = await HomeDeliveryAPI.updateDeliveryStatus(selectedDelivery.id, selectedDelivery, config);
          } else if (deliveryType === 'Pickup') {
            response = await PickUpDeliveryAPI.updateDeliveryStatus(selectedDelivery.id, selectedDelivery, config);
          }
          console.log(response.data); 
          setSelectedDelivery(null);
          fetchData(); 
        } catch (error) {
          alert(error);
        }
      };
  
      const handleClose = () => {
        setSelectedDelivery(null);
        fetchData();
      };
  
      if (deliveryType === 'Home') {
        const {
          id,
          deliveryDate,
          startTime,
          endTime,
          deliveryStatus,
          deliveryDescription,
          postCode,
          city,
          street,
          houseNumber,
          deliveryType,
          timeSlotDetails
        } = selectedDelivery;
  
        return (
          <div className="details-form">
            <h2>Home Delivery Details</h2>
            <p><strong>Id:</strong> {id}</p>
            <p><strong>Delivery Date:</strong> {deliveryDate}</p>
            <p><strong>Start Time:</strong> {startTime}</p>
            <p><strong>End Time:</strong> {endTime}</p>
            <div>
              <label htmlFor="deliveryStatus">Delivery Status:</label>
              <select id="deliveryStatus" value={deliveryStatus} onChange={handleStatusChange}>
                <option value="Undefined">Undefined</option>
                <option value="Pending">Pending</option>
                <option value="Processed">Processed</option>
                <option value="ReadyForDelivery">ReadyForDelivery</option>
                <option value="Delivered">Delivered</option>
              </select>
            </div>
            <p><strong>Delivery Description:</strong> {deliveryDescription}</p>
            <p><strong>Post Code:</strong> {postCode}</p>
            <p><strong>City:</strong> {city}</p>
            <p><strong>Street:</strong> {street}</p>
            <p><strong>House Number:</strong> {houseNumber}</p>
            <p><strong>Delivery Type:</strong> {deliveryType}</p>
            <p><strong>Time Slot Details:</strong> {timeSlotDetails}</p>
            <button onClick={handleUpdate}>Update</button>
            <button onClick={handleClose}>Close</button>
          </div>
        );
      }
  
      if (deliveryType === 'Pickup') {
        const {
          id,
          deliveryDate,
          startTime,
          endTime,
          deliveryStatus,
          pickUpFacility,
          deliveryType,
          timeSlotDetails
        } = selectedDelivery;
  
        const {
          facilityName,
          postCode,
          city,
          openTime,
          closeTime
        } = pickUpFacility;
  
        return (
          <div className="details-form">
            <h2>Pickup Delivery Details</h2>
            <p><strong>Id:</strong> {id}</p>
            <p><strong>Delivery Date:</strong> {deliveryDate}</p>
            <p><strong>Start Time:</strong> {startTime}</p>
            <p><strong>End Time:</strong> {endTime}</p>
            <div>
              <label htmlFor="deliveryStatus">Delivery Status:</label>
              <select id="deliveryStatus" value={deliveryStatus} onChange={handleStatusChange}>
                <option value="Undefined">Undefined</option>
                <option value="Pending">Pending</option>
                <option value="Processed">Processed</option>
                <option value="ReadyForDelivery">ReadyForDelivery</option>
                <option value="Delivered">Delivered</option>
              </select>
            </div>
            <p><strong>Facility Name:</strong> {facilityName}</p>
            <p><strong>Post Code:</strong> {postCode}</p>
            <p><strong>City:</strong> {city}</p>
            <p><strong>Opening Time:</strong> {openTime}</p>
            <p><strong>Closing Time:</strong> {closeTime}</p>
            <p><strong>Delivery Type:</strong> {deliveryType}</p>
            <p><strong>Time Slot Details:</strong> {timeSlotDetails}</p>
            <button onClick={handleUpdate}>Update</button>
            <button onClick={handleClose}>Close</button>
          </div>
        );
      }
    }
  
    return null;
  };
  const handleStartDateChange = (event) => {
    setStartDate(event.target.value);
  };

  const handleEndDateChange = (event) => {
    setEndDate(event.target.value);
  };
  const handleGetCount = async () => {
    const token = sessionStorage.getItem("accessToken");
    setAxiosAuthHeader(token);
    const config = createConfig(token);
    try {
      let response;
      if (deliveryType === 'Home') {
        response = await HomeDeliveryAPI.getCountOfHomeDelivery(startDate, endDate, config);
        console.log(response.data.count);
        console.log(endDate);
        console.log(startDate);
      } else if (deliveryType === 'Pickup') {
        response = await PickUpDeliveryAPI.getCountOfPickUpDelivery(startDate, endDate, config);
        console.log(response.data.count);
        console.log(endDate);
        console.log(startDate);
      }
      const count = response.data.count;
      setDeliveryCount(count);
    } catch (error) {
      alert(error);
    }
  };

  const renderAggregatedForm = () =>{
    if(showAggregatedData){
        return(
            <div>
            <div className="details-form">
            <h2>Select Date Range</h2>
            <div className="date-input-group">
              <label>Start Date:</label>
              <input type="date" value={startDate} onChange={handleStartDateChange} />
            </div>
            <div className="date-input-group">
              <label>End Date:</label>
              <input type="date" value={endDate} onChange={handleEndDateChange} />
            </div>
            <button onClick={handleGetCount}>Get Number of Deliveries</button>
            <button onClick={() => setShowAggregatedData(false)}>close</button>
            {deliveryCount != 0 && <p>Number of Deliveries: {deliveryCount}</p>}
          </div>
          </div>
          );
    }
  }
  return (
    <div className="container">
      <h2>
        <select value={deliveryType} onChange={handleDeliveryTypeChange}>
          <option value="Home">Home Delivery</option>
          <option value="Pickup">Pickup Delivery</option>
        </select>
      </h2>
      <button onClick={()=> setShowAggregatedData(true)}>Show count deliveries</button>
      {renderAggregatedForm()}
      {renderTable()}
      {renderDetailsForm()}
    </div>
  );
}

export default DeliveryComponent;