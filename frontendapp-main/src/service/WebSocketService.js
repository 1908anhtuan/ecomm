import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';

const useWebSocketService = (userId) => {
  const [stompClient, setStompClient] = useState(null);
  const [messagesReceived, setMessagesReceived] = useState([]);

  useEffect(() => {
    setupStompClient();

    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, []);

  const setupStompClient = () => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    client.onConnect = () => {
      client.subscribe('/topic/publicmessages', (data) => {
        const message = JSON.parse(data.body);
        setMessagesReceived((prevMessages) => [...prevMessages, message]);
      });
      console.log("Establish ws connection");
      client.subscribe(`/user/${userId}/queue/inboxmessages`, (data) => {
        const message = JSON.parse(data.body);
        console.log(message);
        setMessagesReceived((prevMessages) => [...prevMessages, message]);
      });
    };

    client.activate();
    setStompClient(client);
  };

  const sendMessage = async (newMessage) => {
    if (stompClient && stompClient.connected) {
      const payload = {
        sender: userId,
        receiver: newMessage.receiver,
        content: newMessage.content,
      };

      if (payload.receiver) {
        stompClient.publish({
          destination: `/user/${payload.receiver}/queue/inboxmessages`,
          body: JSON.stringify(payload),
        });
      } else {
        stompClient.publish({
          destination: '/topic/publicmessages',
          body: JSON.stringify(payload)
        });
      }
    }
  };

  const clearReceivedMessages = () => {
    setMessagesReceived([]);
  };

  return {
    stompClient,
    messagesReceived,
    sendMessage,
    clearReceivedMessages,
  };
};

export default useWebSocketService;