import axios from 'axios';
import { useState } from 'react';
import React from "react";
import "./RegisterForm.css";
import RegisterAPI from "../apis/RegisterApis"
export default function RegisterForm() {
  const [accountName, setAccountName] = useState('');
  const [password, setPassword] = useState('');

  const registerFunction = (e) => {
    e.preventDefault();
    console.log('account registered');
    console.log(accountName);
    console.log(password);
    const credentials = {
      "accountName": accountName,
      "password": password
    }
    RegisterAPI.register(credentials)
      .then(response => {
        console.log("account id: " + response.data.accountId + " added");
      })
      .catch(error => {
        console.log(error.toJSON());
      })
  }

  return (
    <div className="register-container">
      <h2 className="register-heading">Create an Account</h2>
      <form className="register-form" onSubmit={registerFunction}>
        <div className="form-group">
          <label htmlFor="accountName">Username:</label>
          <input type="text" id="accountName" name="accountName" onChange={(e) => setAccountName(e.target.value)} />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input type="password" id="password" name="password" onChange={(e) => setPassword(e.target.value)} />
        </div>
        
        <button className="register-btn">Register</button>
      </form>
    </div>
  );
}