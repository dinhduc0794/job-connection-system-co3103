import React, { useState } from 'react';
import {
  BusinessOutline,
  LocationOutline,
  PeopleOutline,
  TimeOutline,
  GlobeOutline,
  StarOutline,
  TrendingUpOutline
} from "react-ionicons";
import { useNavigate } from "react-router-dom";
import { companies } from '../data/company';
import Navbar from '../conponents/navbar/navbar';


const AllCompany = () => {
  const [filteredCompanies, setFilteredCompanies] = useState(companies);
  const navigate = useNavigate();

  return (
    <>
      <Navbar/>
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-2xl font-bold mb-6">All Companies</h1>
        <div className="w-full flex items-center justify-between flex-wrap mt-8">
          {filteredCompanies.map((company) => (
            <div
              key={company.id}
              className="md:w-[49%] w-full bg-white mb-5 rounded-lg p-6 border border-gray-200 hover:shadow-lg transition-shadow"
            >
              <div className="flex items-start gap-6 mb-4">
                <img
                  src={company.logo}
                  alt={company.name}
                  className="w-[80px] h-[80px] object-contain"
                />
                <div className="flex-1">
                  <h2 className="text-2xl font-bold text-gray-800 mb-2">
                    {company.name}
                  </h2>
                  <div className="flex items-center gap-2 mb-2">
                    <StarOutline 
                      width="18px"
                      height="18px"
                      color="#555"
                    />
                    <span className="text-sm text-gray-600">
                      Founded in {company.foundedYear}
                    </span>
                  </div>
                  <p className="text-gray-600 text-sm line-clamp-2 mb-4">
                    {company.description}
                  </p>
                </div>
              </div>

              <div className="grid md:grid-cols-2 gap-3">
                <div className="flex items-center gap-2">
                  <LocationOutline
                    width="18px"
                    height="18px"
                    color="#555"
                  />
                  <span className="text-sm text-gray-600">
                    {company.headquarters}
                  </span>
                </div>

                <div className="flex items-center gap-2">
                  <PeopleOutline
                    width="18px"
                    height="18px"
                    color="#555"
                  />
                  <span className="text-sm text-gray-600">
                    {company.employeeCount.toLocaleString()} employees
                  </span>
                </div>

                <div className="flex items-center gap-2">
                  <BusinessOutline
                    width="18px"
                    height="18px"
                    color="#555"
                  />
                  <span className="text-sm text-gray-600">
                    {company.industry}
                  </span>
                </div>

                <div className="flex items-center gap-2">
                  <TrendingUpOutline
                    width="18px"
                    height="18px"
                    color="#555"
                  />
                  <span className="text-sm text-gray-600">
                    {company.growthStage}
                  </span>
                </div>
              </div>

              <div className="flex flex-wrap gap-2 mt-4">
                {company.techStack?.map((tech, index) => (
                  <span
                    key={index}
                    className="px-3 py-1 bg-gray-100 text-gray-700 text-sm rounded-full"
                  >
                    {tech}
                  </span>
                ))}
              </div>

              <div className="flex justify-between items-center mt-6">
                <div className="flex items-center gap-2">
                  <TimeOutline
                    width="18px"
                    height="18px"
                    color="#555"
                  />
                  <span className="text-sm text-gray-600">
                    {company.openPositions} open positions
                  </span>
                </div>

                <button
                  onClick={() => navigate(`/companies/${company.id}`)}
                  className="text-indigo-500 font-semibold hover:text-indigo-600 transition-colors flex items-center gap-2"
                >
                  View Details
                  <GlobeOutline
                    width="18px"
                    height="18px"
                    color="currentColor"
                  />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};

export default AllCompany;