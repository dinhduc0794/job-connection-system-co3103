import logo from './logo.svg';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useState, useEffect } from 'react'
import './App.css';
import Job_Home from './Jsx/Job_Home.jsx';
import Home from './Jsx/Home.jsx';
import JobDetail from './Jsx/JobDetail.jsx';
import CompanyProfile from './Jsx/CompanyProfile.jsx';
import ApplicantProfile from './Jsx/ApplicantProfile.jsx';
import AllCompany from './Pages/Allcompany.jsx';
import AllJob from "./Pages/AllJob";
import SavedJobs from "./Pages/SavedJobs";
import ErrorBoundary from "./Pages/ErrorBoudary";
import Test from './Pages/test.jsx';
import Login from './Authe/Login.jsx';
import PrivateRoute from './Authe/PrivateRoute.jsx';
import Loading from './Jsx/Loading.jsx';
import TestLogin from './Authe/TestLogin.jsx';


function App() {
  return (
    
    <div>

      {/* <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/JobDetail/:id" element={<JobDetail />} />
        <Route path="/CompanyProfile/:tab" element={<CompanyProfile />} />
        <Route path="/ApplicantProfile" element={<ApplicantProfile />} />
        <Route path="/alljob" element={<AllJob />} />
        <Route path="/saved-jobs" element={<SavedJobs />} />
        <Route path="/saved-jobs/:id" element={<SavedJobs />} />
        <Route path="/allcompany" element={<AllCompany />} />
        <Route path="/test" element={<Test />} />
        <Route path="*" element={<ErrorBoundary />} />
      </Routes> */}
      <div>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />

        {/* Các route riêng tư */}
        <Route
          path="/ApplicantProfile"
          element={
            <PrivateRoute role="applicant">
              <ApplicantProfile />
            </PrivateRoute>
          }
        />
        <Route
          path="/CompanyProfile/:tab"
          element={
            <PrivateRoute role="company">
              <CompanyProfile />
            </PrivateRoute>
          }
        />
        <Route
          path="/CompanyProfile"
          element={
            <PrivateRoute role="company">
              <CompanyProfile />
            </PrivateRoute>
          }
        />
        <Route
          path="/admin"
          element={
            <PrivateRoute role="admin">
              <Home />
            </PrivateRoute>
          }
        />

        {/* Các route công khai khác */}
        <Route path="/alljob" element={<AllJob />} />
        <Route path="/saved-jobs" element={<SavedJobs />} />
        <Route path="/allcompany" element={<AllCompany />} />
        <Route path="/test" element={<Test />} />
        <Route path="*" element={<ErrorBoundary />} />
        <Route path="/loading" element={<Loading />} />
        <Route path="/testlogin" element={<TestLogin />} />
      </Routes>
    </div>


    </div>
  );
}

export default App;
