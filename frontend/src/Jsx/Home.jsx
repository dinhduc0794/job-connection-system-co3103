import React, {useState, useEffect} from "react";
import axios from 'axios';
import Job_Home from "./Job_Home";
import Navbar from "./navbar";
import TopCompany from "./TopCompany";
import TopJob from "./TopJob";
import ListCompany from "./ListCompany";
import Footer from "./Footer";
import AppNavbar from "./AppNavbar";
import CompanyNavbar from "./CompanyNavbar";
import '../css/Home.css'
import TokenManager from "../utils/tokenManager";


function Home(){
    const [role, setRole] = useState(null); // State để lưu role
    const [userId, setUserId] = useState(null); // State để lưu userId
    const [userInfo, setUserInfo] = useState(null);

    
    const token = TokenManager.getToken();

    useEffect(() => {
      if (token) {
        setRole(token.role?.toLowerCase()); // Lấy role từ token
        setUserId(token.id); // Lấy userId từ token
      }
    }, [token]);

    const renderNavbar = () => {
        if (role === 'applicant') {
          return <AppNavbar />;
        } else if (role === 'company') {
          return <CompanyNavbar />;
        } else {
          return <Navbar />;
        }
      };
    return(
        <div id="home-container">

            {renderNavbar()}
            <TopCompany/>
            <Job_Home/>
            <TopJob/>
            <ListCompany/>
            <Footer/>
        </div>
    );
}

export default Home;