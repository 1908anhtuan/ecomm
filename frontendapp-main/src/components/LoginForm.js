import React, { useState, useContext, useEffect } from "react";
import "../components/LoginForm.css";
import LoginAPI from "../apis/LoginApis"
import NavbarClient from './navbar';
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";

function LoginForm() {
    const [accountName, setAccountName] = useState("");
    const [password, setPassword] = useState("");
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();
    const handleLogin = async(event) => {
      event.preventDefault();
      const credentials = {
        accountName: accountName,
        password: password
      };
      try {
        const response = await LoginAPI.login(credentials);
        console.log(response.data.accessToken);
        sessionStorage.setItem("accessToken", response.data.accessToken);
        sessionStorage.setItem("accountName", accountName);
        alert("Successfully logged in");
        
        login();
        getAccount();
      } catch (error) {
        sessionStorage.removeItem("accessToken");
        sessionStorage.removeItem("accountName");
        console.log(error);
        alert("Login failed! Check your username and password!");
      }
    };

    
      const getAccount = async () => {
        try {
          const accountName = sessionStorage.getItem("accountName");
          const response = await LoginAPI.getAccountByAccountName(accountName);
          sessionStorage.setItem("accountId", response.data.account.id);
          console.log(response.data.account.id);
          navigate("/"); 
        } catch (error) {
          sessionStorage.removeItem("accountId");
          console.log(error);
          alert("get account failed!");
        }
      }
    
    
    
  
    return (
      <div>
        <div className="login-form-container">
          <form className="login-form">
            <h2 className="login-heading">Log in</h2>
            <div className="form-group">
              <label htmlFor="username">Username:</label>
              <input
                type="text"
                id="username"
                name="username"
                onChange={(event) => setAccountName(event.target.value)}
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password:</label>
              <input
                type="password"
                id="password"
                name="password"
                onChange={(event) => setPassword(event.target.value)}
              />
            </div>
            <button onClick={handleLogin} className="login-btn">
              Log in
            </button>
          </form>
        </div>
      </div>
    );
  }
  
  export default LoginForm;