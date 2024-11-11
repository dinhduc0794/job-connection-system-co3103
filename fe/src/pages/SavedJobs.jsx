import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ListedJobs from '../conponents/ListedJobs';
import { jobs } from '../data/jobs';

const SavedJobs = () => {
  const [savedJobIds, setSavedJobIds] = useState(() => {
    const saved = localStorage.getItem('savedJobs');
    return saved ? JSON.parse(saved) : [];
  });

  const { id } = useParams();

  useEffect(() => {
    localStorage.setItem('savedJobs', JSON.stringify(savedJobIds));
  }, [savedJobIds]);

  const savedJobs = jobs.filter(job => savedJobIds.includes(job.id));

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Saved Jobs</h1>
      <ListedJobs
        jobs={savedJobs}
        savedJobs={savedJobIds}
        setSavedJobs={setSavedJobIds}
      />
    </div>
  );
};

export default SavedJobs;