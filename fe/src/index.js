import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';

import reportWebVitals from './reportWebVitals';
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import ErrorBoundary from './conponents/ErrorBoudary';
import AllJob from './pages/AllJob';
import SavedJobs from './pages/SavedJobs';
import JobDetail from './pages/JobDetail';
import AllCompany from './pages/Allcompany';

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorBoundary />
  },
  {
    path: "/alljob",
    element: <AllJob />,
    errorElement: <ErrorBoundary />
  },
  {
    path: "/saved-jobs",
    element: <SavedJobs />,
    errorElement: <ErrorBoundary />
  },
  {
    path: "/saved-jobs/:id",
    element: <SavedJobs />,
    errorElement: <ErrorBoundary />
  },
  {
    path: "/jobs/:id",
    element: <JobDetail />,
    errorElement: <ErrorBoundary />
  },
  {
    path: "/allcompany",
    element: <AllCompany/>,
    errorElement: <ErrorBoundary />
  }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

reportWebVitals();