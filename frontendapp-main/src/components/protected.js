import React, { useContext } from 'react';
import { Navigate } from "react-router-dom";
import { AuthContext } from './AuthContext';
import jwt_decode from 'jwt-decode';

const Protected = ({children,roles }) => {
  
    const { isLoggedIn } = useContext(AuthContext);
    const token = sessionStorage.getItem('accessToken');
    const decodedToken = token ? jwt_decode(token) : null;
    const userRoles = decodedToken && decodedToken.roles ? decodedToken.roles : [];
    console.log(roles);
    if (!token) {
        return <Navigate to="/login" replace/>;
      }
    if(!userRoles.some(role => roles.includes(role))){
      console.log("rolecheck");
        return <Navigate to="/" replace/>
    }
  return children;
};
export default Protected;