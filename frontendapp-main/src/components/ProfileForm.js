import React, { useState, useEffect } from 'react';
import './ProfileForm.css'
import AccountAPI from '../apis/AccountApis';
import LoginAPI from '../apis/LoginApis';
import img from '../img/download.png'
import axios from "axios";

function ProfileForm() {
    const [user, setUser] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const [editedUser, setEditedUser] = useState(null);
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
        const getAccount = async () => {
            try {
                const accountName = sessionStorage.getItem("accountName");
                const response = await LoginAPI.getAccountByAccountName(accountName);
                console.log(response.data.account);
                setUser(response.data.account);
            } catch (error) {
                sessionStorage.removeItem("accountId");
                console.log(error);
                alert("get account failed!");
            }
        }
        getAccount();
    }, []);

    const handleAdjustInfo = () => {
        setEditMode(true);
        setEditedUser({ ...user });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        if (name === "balance" || name === "accountName") {
            return; 
        }
        setEditedUser((prevUser) => ({
            ...prevUser,
            [name]: value
        }));
    };

    const handleSaveChanges = () => {
        const token = sessionStorage.getItem("accessToken");
        setAxiosAuthHeader(token);
        const config = createConfig(token);
        const request = {
            "firstName": editedUser.firstName,
            "lastName" : editedUser.lastName,
            "email" : editedUser.email,
            "phoneNumber" : editedUser.phoneNumber
        }
        const updateAccount = async () => {
            try{
                const response = await AccountAPI.updateAccount(user.id, request, config);
                alert(response.data.message);
            }
            catch(error){
                alert(error);
            }
        }
        updateAccount();
        setEditMode(false);
        setUser({ ...editedUser });
    };

    return (
        <div className="user-info">
            <div className="avatar-frame">
                <img src={img} alt="Product Image" />
            </div>
            <div className="user-details">
                {user ? (
                    <>
                        <h2>
                            {editMode ? (
                                <input
                                    type="text"
                                    name="firstName"
                                    value={editedUser.firstName}
                                    onChange={handleInputChange}
                                />
                            ) : (
                                user.firstName
                            )}
                            {' '}
                            {editMode ? (
                                <input
                                    type="text"
                                    name="lastName"
                                    value={editedUser.lastName}
                                    onChange={handleInputChange}
                                />
                            ) : (
                                user.lastName
                            )}
                        </h2>
                        <p>
                            <strong>Account Name:</strong>{' '}
                            {user.accountName}
                        </p>
                        <p>
                            <strong>Email:</strong>{' '}
                            {editMode ? (
                                <input
                                    type="text"
                                    name="email"
                                    value={editedUser.email}
                                    onChange={handleInputChange}
                                />
                            ) : (
                                user.email
                            )}
                        </p>
                        <p>
                            <strong>Phone Number:</strong>{' '}
                            {editMode ? (
                                <input
                                    type="text"
                                    name="phoneNumber"
                                    value={editedUser.phoneNumber}
                                    onChange={handleInputChange}
                                />
                            ) : (
                                user.phoneNumber
                            )}
                        </p>
                        <p>
                            <strong>Balance:</strong>{' '}
                            {user.balance}
                        </p>
                        {editMode ? (
                            <button onClick={handleSaveChanges}>Save Changes</button>
                        ) : (
                            <button onClick={handleAdjustInfo}>Adjust Info</button>
                        )}
                    </>
                ) : (
                    <p className="loading-message">Loading user information...</p>
                )}
            </div>
        </div>
    );
}

export default ProfileForm;