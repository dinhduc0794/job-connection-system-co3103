import React from 'react';
import { Outlet } from 'react-router-dom';
import './App.css';
import { AuthProvider } from './Contexts/AuthContext';
import MainNavbar from './Jsx/navbar';
function App() {
  return (
    // <AuthProvider>
    <>
      {/* <MainNavbar /> */}
      <div className="app">
        {/* Add your header, navigation, or any other common components here */}
        <Outlet />
        {/* Add your footer or any other common components here */}
      </div>
     </>
     //</AuthProvider>
  );
}

export default App;