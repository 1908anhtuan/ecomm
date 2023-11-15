import React, { useEffect, useState, useRef } from 'react';
import useWebSocketService from '../service/WebSocketService';
import './ChatComponent.css';
import { useParams } from 'react-router-dom';
import MessageAPI from '../apis/MessageApis';

const renderMessage = (sender, content, isOwnedMessage, id) => (
  <div
    className={`chat-message ${isOwnedMessage ? 'owned-message' : 'other-message'}`}
    key={id}
  >
    <div className={`name ${isOwnedMessage ? 'align-right' : 'align-left'}`}>
      <span style={{ color: isOwnedMessage ? 'blue' : 'red' }}>{sender.accountName}</span>
    </div>
    <div className={`content ${isOwnedMessage ? 'align-right' : 'align-left'}`}>{content}</div>
  </div>
);

const ChatComponent = (receiverIdParam) => {
  const  receiverId  = receiverIdParam.receiverIdParam;
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [updateMessage, setUpdateMessage] = useState(false);
  const loginUserId = sessionStorage.getItem('accountId');
  const webSocketService = useWebSocketService(loginUserId);
  const chatBoxRef = useRef(null);

  useEffect(() => {
    console.log(loginUserId);
    console.log(receiverId);
    const fetchedPreviousMessages = async () => {
      try {
        const response = await MessageAPI.getPreviousMessages(loginUserId, receiverId);
        const sortedMessages = response.data.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
        setMessages(sortedMessages);
        console.log(sortedMessages);
        setUpdateMessage(false);
      } catch (error) {
        console.log('Error occurred while fetching previous messages:', error.message);
      }
    };
    fetchedPreviousMessages();
  }, [updateMessage]);

  useEffect(() => {
    if (webSocketService.messagesReceived.length > 0) {
      const newMessages = [...messages, ...webSocketService.messagesReceived];
      setTimeout(() => {
        updateList();
      }, 30);      webSocketService.clearReceivedMessages();
    }
  }, [webSocketService.messagesReceived]);

  const updateList = () => {
    setUpdateMessage(true);
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);
  const scrollToBottom = () => {
    chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
  };

  const handleSendMessage = async (event) => {
    event.preventDefault();
    const wsMessage = {
      receiver: receiverId,
      content: message,
    };
    webSocketService.sendMessage(wsMessage);
    setTimeout(() => {
      updateList();
    }, 30);
  };

  return (
    <div className="chatcomp">
      <div className="chat-box" ref={chatBoxRef}>
        {messages.map((msg, index) =>
          renderMessage(msg.sender, msg.content, msg.sender.id == loginUserId, index)
        )}
      </div>

      <div className="message-box">
        <input
          type="text"
          placeholder="Type your message..."
          value={message}
          onChange={(event) => setMessage(event.target.value)}
        />
        <button onClick={handleSendMessage}>Send</button>
      </div>
    </div>
  );
};

export default ChatComponent;