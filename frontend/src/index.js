import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { BrowserRouter, Routes, Route, RouterProvider, createBrowserRouter } from "react-router-dom";
import reportWebVitals from './reportWebVitals';
import { AuthProvider } from './Contexts/AuthContext';

// Import all components
import Home from './Jsx/Home';
import JobDetail from './Jsx/JobDetail';
import CompanyProfile from './Jsx/CompanyProfile';
import ApplicantProfile from './Jsx/ApplicantProfile';
import AllJob from "./Pages/AllJob";
// import SavedJobs from "./Pages/SavedJobs";

import ErrorBoundary from "./Pages/ErrorBoudary";
import CompanyDetail from './Pages/CompanyDetail';
import AllCompany from './Pages/Allcompany';
import Login from './Pages/Auth/Login';
import Register from './Pages/Auth/Register';
import PrivateRoute from './components/PrivateRoute';
import Dashboard from './Pages/Dashboard';
import Unauthorized from './Pages/Unauthorized';
import RoleBasedRoute from './Pages/RoleBasedRoute';
const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <AuthProvider>
        <App />
      </AuthProvider>
    ),
    errorElement: <ErrorBoundary />,
    children: [
      {
        index: true,
        element: <Home />
      },
      {
        path: "alljob/:id",
        element: <AllJob />
      },
      {
        path: "alljob",
        element: <AllJob />
      },
      {
        path: "allcompany",
        element: <AllCompany />
      },
      {
        path: "allcompany/:id",
        element: <CompanyDetail />
      },
      {
        path: "/unauthorized",
        element: <Unauthorized />
      },
      {
        path: "login",
        element: <Login />
      },
      {
        path: "register/*",
        element: <Register />
      },
      {
        path: "JobDetail/:id",
        element: <JobDetail />
      },
      { 
        element: <RoleBasedRoute allowedRoles={['applicant']} />,        
        children: [
          {
            path: "ApplicantProfile",
            element: <ApplicantProfile />
          },
          {
            path: "ApplicantProfile/:id",
            element: <ApplicantProfile/>
          },
          {
            path: "ApplicantProfile/:tab",
            element: <ApplicantProfile/>
          },
          // {
          //   path: "saved-jobs",
          //   element: <SavedJobs />
          // },
          // {
          //   path: "saved-jobs/:id",
          //   element: <SavedJobs />
          // }
        ]
      },
      {
        element: <RoleBasedRoute allowedRoles={['company']} />,
        children: [
          {
            path: "CompanyProfile",
            element: <CompanyProfile />
          },
          {
            path: "CompanyProfile/:id",
            element: <CompanyProfile />
          },
          {
            path: "CompanyProfile/:tab",
            element: <CompanyProfile />
          }
        ]
      },
      {
        element: <RoleBasedRoute allowedRoles={['admin']} />,
        children: [
          {
            path: "dashboard",
            element: <Dashboard />
          }
        ]
      }
    ]
  }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
 
    <React.StrictMode>
      {/* <BrowserRouter>
        <AuthProvider> */}
        <RouterProvider router={router} />
        {/* </AuthProvider>
      </BrowserRouter> */}
    </React.StrictMode>
  
);