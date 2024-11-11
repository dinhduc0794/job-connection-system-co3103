import React, { useState } from 'react';
import { jobs } from '../data/jobs';
import ListedJobs from '../conponents/ListedJobs';
import Navbar from '../conponents/navbar/navbar';
import JobHome from '../home/Job_Home/Job_Home';

const AllJob = () => {
  const [savedJobs, setSavedJobs] = useState(() => {
    const saved = localStorage.getItem('savedJobs');
    return saved ? JSON.parse(saved) : [];
  });

  // Thêm state cho phân trang
  const [currentPage, setCurrentPage] = useState(1);
  const jobsPerPage = 6; // Số công việc trên mỗi trang

  // Tính toán số trang
  const totalPages = Math.ceil(jobs.length / jobsPerPage);

  // Lấy công việc cho trang hiện tại
  const indexOfLastJob = currentPage * jobsPerPage;
  const indexOfFirstJob = indexOfLastJob - jobsPerPage;
  const currentJobs = jobs.slice(indexOfFirstJob, indexOfLastJob);

  // Hàm chuyển trang
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <>
      <Navbar />
      <JobHome/>
      <div className="container mx-auto px-4 py-8">      
        <h1 className="text-2xl font-bold mb-6">All Available Jobs</h1>
        <ListedJobs
          jobs={currentJobs}
          savedJobs={savedJobs}
          setSavedJobs={setSavedJobs}
        />
        
        {/* Phân trang */}
        <div className="flex justify-center mt-8">
          <div className="flex gap-2">
            {Array.from({ length: totalPages }, (_, index) => (
              <button
                key={index + 1}
                onClick={() => paginate(index + 1)}
                className={`px-4 py-2 rounded-md ${
                  currentPage === index + 1
                    ? 'bg-indigo-500 text-white'
                    : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                } transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2`}
              >
                {index + 1}
              </button>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default AllJob;