import React from "react";
import Job_Home from "./Job_Home";
import Navbar from "./navbar";
import TopCompany from "./TopCompany";
import TopJob from "./TopJob";
import ListCompany from "./ListCompany";
import Footer from "./Footer";
import AppNavbar from "./AppNavbar";
import CompanyNavbar from "./CompanyNavbar";
import '../css/Home.css'

function Home(){
    const role = localStorage.getItem('role');
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