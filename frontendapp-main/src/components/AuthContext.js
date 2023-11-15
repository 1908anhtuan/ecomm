import React, { createContext, useState, useEffect } from "react";
const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    
  })
  useEffect(() => {
    const storedAccessToken = sessionStorage.getItem("accessToken");
    if (storedAccessToken) {
      setIsLoggedIn(true);
    }
  }, []);
  const login = () => {
    setIsLoggedIn(true);
  };

  const logout = () => {
    sessionStorage.removeItem("accessToken");
    setIsLoggedIn(false);
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export { AuthContext, AuthProvider };
