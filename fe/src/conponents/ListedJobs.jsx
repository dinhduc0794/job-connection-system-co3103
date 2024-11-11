import React from 'react';
import {
  Bookmark,
  BookmarkOutline,
  BusinessOutline,
  LocationOutline,
  NewspaperOutline,
} from "react-ionicons";
import { useNavigate } from "react-router-dom";

const ListedJobs = ({ jobs = [], setSavedJobs = () => {}, savedJobs = [] }) => {
  const navigate = useNavigate();
   
  const handleSave = (id) => {
    setSavedJobs(prev => {
      const isAlreadySaved = prev.includes(id);
      if (isAlreadySaved) {
        return prev.filter(savedId => savedId !== id);
      }
      return [...prev, id];
    });
  };

  if (!Array.isArray(jobs)) {
    return (
      <div className="w-full flex justify-center items-center mt-8">
        <span className="text-gray-500 text-lg">Loading jobs...</span>
      </div>
    );
  }

  if (jobs.length === 0) {
    return (
      <div className="w-full flex justify-center items-center mt-8">
        <span className="text-gray-500 text-lg">No jobs matched your filters!</span>
      </div>
    );
  }

  return (
    <div className="w-full flex items-center justify-between flex-wrap mt-8">
      {jobs.map((job) => (
        <div
          key={job.id}
          className="md:w-[49%] w-full bg-white mb-5 rounded-lg flex items-center justify-between md:p-8 py-8 px-4 border border-gray-200"
        >
          <div className="flex md:flex-row flex-col md:items-center items-start gap-6">
            <img
              src={job.logo}
              alt={job.title}
              className="w-[70px] object-contain"
            />
            <div className="flex flex-col gap-[6px]">
              <span className="font-semibold text-indigo-500 text-[22px]">
                {job.title}
              </span>
              <div className="flex items-center gap-2">
                <BusinessOutline
                  width="18px"
                  height="18px"
                  color="#555"
                />
                <span className="text-[14px] font-medium text-gray-600">
                  {job.company}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <LocationOutline
                  width="18px"
                  height="18px"
                  color="#555"
                />
                <span className="text-[14px] font-medium text-gray-600">
                  {job.workStatus}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <NewspaperOutline
                  width="18px"
                  height="18px"
                  color="#555"
                />
                <span className="text-[14px] font-medium text-gray-600">
                  {job.contractStatus}
                </span>
              </div>
            </div>
          </div>
          <div className="flex flex-col gap-4 self-end">
            <button
              onClick={() => navigate(`/jobs/${job.id}`)}
              className="text-white font-bold text-lg rounded-md bg-indigo-500 hover:bg-indigo-600 transition-colors w-40 h-10"
            >
              Apply
            </button>
            <button
              onClick={() => handleSave(job.id)}
              className={`flex items-center gap-2 cursor-pointer rounded-md justify-center py-1 border ${
                savedJobs.includes(job.id)
                  ? "bg-gray-100 border-indigo-500"
                  : "bg-gray-200 border-gray-200"
              }`}
            >
              {savedJobs.includes(job.id) ? (
                <Bookmark color="#6366fa" />
              ) : (
                <BookmarkOutline color="#6366fa" />
              )}
              <span className="font-medium text-[14.5px] text-gray-600">
                {savedJobs.includes(job.id) ? "Saved!" : "Save"}
              </span>
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default ListedJobs;