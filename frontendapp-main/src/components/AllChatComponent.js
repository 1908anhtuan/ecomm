import React, { useEffect, useState } from 'react';
import ChatComponent from './ChatComponent';
import MessageAPI from '../apis/MessageApis';
import './AllChatComponent.css';
import axios from "axios";

const AllChatComponent = () => {
  const [accounts, setAccounts] = useState([]);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const loginUserId = sessionStorage.getItem('accountId');

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
    const getMessagedAccounts = async () => {
      const token = sessionStorage.getItem("accessToken");
      setAxiosAuthHeader(token);
      const config = createConfig(token);
      try {
        const response = await MessageAPI.getMessagedAccounts(loginUserId, config);
        setAccounts(response.data.accounts);
      } catch (error) {
        console.log('Error occurred:', error.message);
      }
    };

    getMessagedAccounts();
  }, [loginUserId]);

  const handleAccountSelection = (accountId) => {
    setSelectedAccount(accountId);
  };

  return (
    <div className="all-chat-component">
      <div className="accounts-bar">
        {accounts.map((account) => (
          <div
            className={`account-item ${selectedAccount === account.id ? 'selected' : ''}`}
            key={account.id}
            onClick={() => handleAccountSelection(account.id)}
          >
            <div className="account-item-rectangular">
              <span className="account-name">{account.accountName}</span>
            </div>
          </div>
        ))}
      </div>
      <div className="chat-container">
        {selectedAccount ? (
          <ChatComponent key={selectedAccount} receiverIdParam={selectedAccount} />
        ) : (
          <div className="no-account-selected">No account selected.</div>
        )}
      </div>
    </div>
  );
};

export default AllChatComponent;