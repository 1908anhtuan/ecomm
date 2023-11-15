import { Route, Routes, BrowserRouter as Router } from "react-router-dom";
import './App.css';
import NavBar from './components/navbar';
import RegisterPage from './pages/RegisterPage';
import LogInPage from './pages/LoginPage';
import HomePage from "./pages/HomePage";
import RegProductDetailsPage from "./pages/RegProductDetailsPage";
import ProfilePage from "./pages/ProfilePage";
import PlacingAdvertPage from "./pages/PlacingAdvertPage"
import VerificationPage  from "./components/AdvertVerification";
import AllAdsPage from "./pages/AllAdsPage";
import BidProductDetailsPage from "./pages/BidProductDetailsPage";
import BuyHistory from "./components/BuyHistory";
import PrivateChatPage from "./pages/PrivateChatPage"
import AllChatComponent from "./components/AllChatComponent";
import Protected from "./components/protected";
import DeliveryPage from "./pages/DeliveryPage";
import { AuthProvider } from "../src/components/AuthContext";

function App() {
  return (
    <div className="App">
      <AuthProvider>
      <Router>
        <NavBar />
        <div className="background-image">
          <Routes>
         
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LogInPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/alladspage" element={<AllAdsPage />} />
            <Route path="/regularads/:id" element={<RegProductDetailsPage />} />
            <Route path="/biddingads/:id" element={<BidProductDetailsPage />} />
            <Route path="/privatechat/:receiverId" element={<PrivateChatPage />} />
            <Route path="/profile" element={<Protected roles={["Admin","RegularUser"]}><ProfilePage /></Protected>}/>
            <Route path="/verification" element={<Protected roles={["Admin"]}><VerificationPage /></Protected>}/>
            <Route path="/allchat" element={<Protected roles={["Admin","RegularUser"]}><AllChatComponent /></Protected>}/>
            <Route path="/buyhistory" element={<Protected roles={["Admin","RegularUser"]}><BuyHistory /></Protected>}/>
            <Route path="/placeadvert" element={<Protected roles={["Admin","RegularUser"]}><PlacingAdvertPage /></Protected>}/>
            <Route path="/delivery" element={<Protected roles={["Admin"]}><DeliveryPage /></Protected>}/>





          </Routes>
          <footer className="footer">
            <p>&copy; 2023 Online market. All Rights Reserved.</p>
          </footer>
        </div>
      </Router>
      </AuthProvider>
    </div>
  );
}


export default App;
