import React, { useState, useEffect } from 'react';
import './DeliverySelectionForm.css';
import FacilityAPI from '../apis/FacilitiesApis';
import HomeDeliveryAPI from '../apis/HomeDeliveryApis';
import PickUpDeliveryAPI from '../apis/PickUpDeliveryApis';

const DeliverySelectionForm = ({advertId, closeForm}) => {
  const [deliveryType, setDeliveryType] = useState(null);
  const [facilities, setFacilities] = useState([]);
  const [selectedFacility, setSelectedFacility] = useState(null);
  const [timeSlots, setTimeSlots] = useState([]);
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTimeSlot, setSelectedTimeSlot] = useState(null);
  const [postCode, setPostCode] = useState('');
  const [houseNumber, setHouseNumber] = useState('');
  const [street, setStreet] = useState('');
  const [city, setCity] = useState('');
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await FacilityAPI.getFacilities();
        setFacilities(response.data.pickUpFacilityList);
        console.log(JSON.stringify(response.data));
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, [deliveryType]);

  const handleRegister = async () => {
    if(deliveryType === "Pickup"){
      handlePickUpRegister();

    }else{
      handleHomeRegister();
    }
  }

  const handlePickUpRegister = async() => {
    const request = {
      digitalAdvertId: advertId.id,
      timeSlot: selectedTimeSlot,
      facility: selectedFacility
    };
    console.log(JSON.stringify(request));
    try{
      const response = await PickUpDeliveryAPI.registerTimeSlot(request);
      console.log(response.data.message);
      alert(response.data.message);
    }catch(error)
    {
      console.error(error);
    }
  }

  const handleHomeRegister = async() => {
    const request = {
      digitalAdvertId: advertId.id,
      timeSlot: selectedTimeSlot,
      selectedDate: selectedDate,
      address : {
        postCode: postCode,
        city: city,
        street: street,
        houseNumber: houseNumber 
      }
    };
    console.log(JSON.stringify(request));

    try{
      const response = await HomeDeliveryAPI.registerTimeSlot(request);
      alert(response.data.message);
      console.log(JSON.stringify(request));
    }catch(error)
    {
      console.error(error);
    }
  }
  const handleFacilityChange = (event) => {
    const facilityId = event.target.value;
    const selectedFacility = facilities.find((facility) => facility.id == facilityId);
    setSelectedFacility(selectedFacility);
  };

  const handleDateChange = (event) => {
    const selectedDate = event.target.value;
    setSelectedDate(selectedDate);
    console.log(selectedDate);
  };

  const handleTimeSlotClick = (timeSlot) => {
    setSelectedTimeSlot(timeSlot);
  };

  useEffect(() => {
    console.log(JSON.stringify(selectedFacility));
  }, [selectedFacility]);

  const fetchTimeSlots = async () => {
    const request = {
      selectedFacility: selectedFacility,
      selectedDate: selectedDate
    };
    console.log(JSON.stringify(request));
    try {
      const response = await PickUpDeliveryAPI.getAvailableTimeSlots(request);
      setTimeSlots(response.data.timeSlotList);
      console.log(JSON.stringify(response.data));
    } catch (error) {
      console.error(error);
    }
  };

  const fetchTimeSlotsForHomeDelivery = async () => {
    try {
      const response = await HomeDeliveryAPI.getAvailableTimeSlots();
      setTimeSlots(response.data.timeSlotList);
      console.log(JSON.stringify(response.data));
    } catch (error) {
      console.error(error);
    }
  }
  return (
    <div className="popup">
      <div className="delivery-selection-form">
        <h2>Delivery Selection</h2>
        <div className="delivery-type">
          <label>
            <input
              type="radio"
              value="Home"
              checked={deliveryType === 'Home'}
              onChange={() => setDeliveryType('Home')}
            />
            Home Delivery
          </label>
          <label>
            <input
              type="radio"
              value="Pickup"
              checked={deliveryType === 'Pickup'}
              onChange={() => setDeliveryType('Pickup')}
            />
            Pickup
          </label>
        </div>

        {deliveryType === 'Home' && (
  <div className="home-delivery-details">
    <h3>Home Delivery Details</h3>
    <label htmlFor="postCode">Post Code:</label>
    <input type="text" id="postCode" value={postCode} onChange={(e) => setPostCode(e.target.value)} />

    <label htmlFor="houseNumber">House Number:</label>
    <input type="text" id="houseNumber" value={houseNumber} onChange={(e) => setHouseNumber(e.target.value)}/>

    <label htmlFor="street">Street:</label>
    <input type="text" id="street" value={street} onChange={(e) => setStreet(e.target.value)} />

    <label htmlFor="city">City:</label>
    <input type="text" id="city" value={city} onChange={(e) => setCity(e.target.value)}/>

    <label htmlFor="date">Select Date:</label>
    <input className="dateSelection" type="date" id="date" value={selectedDate || ''} onChange={handleDateChange} />

    <button onClick={fetchTimeSlotsForHomeDelivery}>Get Available Time Slots</button>

    {timeSlots.length > 0 && (
      <div className="time-slots">
        <h4>Available Time Slots:</h4>
        <ul>
          {timeSlots.map((slot, index) => (
            <li
              key={`${slot.deliveryDate}-${index}`}
              className={selectedTimeSlot === slot ? 'selected' : ''}
              onClick={() => handleTimeSlotClick(slot)}
            >
              {slot.startTime} - {slot.endTime}
            </li>
          ))}
        </ul>
      </div>
    )}
  </div>
)}


        {deliveryType === 'Pickup' && (
          <div className="pickup-details">
            <h3>Pickup Details</h3>
            <label htmlFor="facility">Select Facility:</label>
            <select id="facility" value={selectedFacility?.id || ''} onChange={handleFacilityChange}>
              <option value="">Select Facility</option>
              {facilities.map((facility) => (
                <option key={facility.id} value={facility.id}>
                  {facility.facilityName}
                </option>
              ))}
            </select>

            <label htmlFor="date">Select Date:</label>
            <input className= "dateSelection" type="date" id="date" value={selectedDate || ''} onChange={handleDateChange} />

            <button onClick={fetchTimeSlots}>Get Available Time Slots</button>

            {/* Display the retrieved time slots */}
            {timeSlots.length > 0 && (
              <div className="time-slots">
                <h4>Available Time Slots:</h4>
                <ul>
                {timeSlots.map((slot, index) => (
                <li
                key={`${slot.deliveryDate}-${index}`}
                className={selectedTimeSlot === slot ? 'selected' : ''}
                onClick={() => handleTimeSlotClick(slot)}
                >
                  {slot.startTime} - {slot.endTime}
                  </li>
                  ))}
                </ul>
              </div>
            )}
          </div>
        )}

        <div className="form-actions">
          <button className="cancel-button" onClick={closeForm}>
            Cancel
          </button>
          <button className="submit-button" onClick={handleRegister}>Register</button>
        </div>
      </div>
    </div>
  );
};

export default DeliverySelectionForm;