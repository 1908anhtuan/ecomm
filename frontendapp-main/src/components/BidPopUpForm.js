import React, { useState } from "react";
import "./BidPopUpForm.css";
import axios from "axios";

function BidPopUpForm({ onClose, onSubmit }) {
  const [bidAmount, setBidAmount] = useState("");

  const handleBidSubmit = (event) => {
    event.preventDefault();
    onSubmit(bidAmount);
  };

  return (
    <div className="bid-form-popup-overlay">
      <div className="bid-form-popup">
        <h2>Place a Bid</h2>
        <form onSubmit={handleBidSubmit}>
          <label htmlFor="bidAmount">Bid Amount:</label>
          <input
            type="number"
            id="bidAmount"
            value={bidAmount}
            onChange={(e) => setBidAmount(e.target.value)}
          />
          <button type="submit">Submit Bid</button>
          <button onClick={onClose}>Cancel</button>
        </form>
      </div>
    </div>
  );
}

export default BidPopUpForm;