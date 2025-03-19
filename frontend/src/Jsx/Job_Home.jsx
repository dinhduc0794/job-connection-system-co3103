import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Heart,
    Building2,
    MapPin,
    Calendar,
    Briefcase,
    Clock,
    DollarSign
  } from "lucide-react";
import loadinggif from '../images/Evitare loader.gif'
import '../css/Job_Home.css';
import '../css/JobList.css';
import DefaultImage from '../images/logo1.png'

function Job_Home() {
    const [selectedFilter, setSelectedFilter] = useState('nganh-nghe');
    const [currentValues, setCurrentValues] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [jobPage, setJobPage] = useState(1);
    const [posts, setPosts] = useState([]);
    const [totalPages, setTotalPages] = useState(0); // Tổng số trang
    const [isLoading, setIsLoading] = useState(true); // Trạng thái tải dữ liệu

    const [savedJobs, setSavedJobs] = useState(() => {
        const saved = localStorage.getItem('savedJobs');
        return saved ? JSON.parse(saved) : [];
    });
    
    const navigate = useNavigate();
    const handleSave = (e, id) => {
        e.stopPropagation(); // Prevent triggering the card click
        setSavedJobs(prev => {
            const isAlreadySaved = prev.includes(id);
            if (isAlreadySaved) {
                return prev.filter(savedId => savedId !== id);
            }
            return [...prev, id];
        });
    };

    useEffect(() => {
        const fetchJobs = async () => {
            setIsLoading(true);
            try {
                const response = await fetch(`/public/jobpostings?page=${jobPage}`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setPosts(data.listResult || []); // Cập nhật danh sách công việc
                setTotalPages(data.totalPage || 1); // Lấy tổng số trang từ API
            } catch (error) {
                console.error('Error fetching jobs:', error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchJobs();
    }, [jobPage]); // Tải lại khi trang thay đổi
    

    const allValues = {
        'nganh-nghe': ['Kinh doanh', 'Phiên dịch', 'Báo chí', 'Viễn thông', 'Game', 'IT'],
        'muc-luong': ['Tất cả', 'Dưới 10 triệu', '10-20 triệu', '20-30 triệu', 'Trên 30 triệu'],
        'dia-diem': ['Tất cả', 'Hà Nội', 'TP Hồ Chí Minh', 'Miền Bắc', 'Miền Nam']
    };
    const valuesPerPage = 4;
    const jobsPerPage = 9;


    const handleJobDetail = (jobId) => {
        if (jobId) {
            navigate(`/JobDetail/${jobId}`);
        } else {
            console.warn("Invalid job ID");
        }
        console.log('Fetching URL:', `/jobpostings/jobId}`);

    };
    

    const handleFilterChange = (event) => {
        const selected = event.target.value;
        setSelectedFilter(selected);
        setCurrentPage(0); // Khi thay đổi filter, chuyển về trang đầu tiên
    };

    const handlePreviousClick = () => {
        if (currentPage > 0) {
            setCurrentPage(currentPage - 1);
        }
    };

    const handleNextClick = () => {
        const maxPage = Math.ceil(allValues[selectedFilter].length / valuesPerPage) - 1;
        if (currentPage < maxPage) {
            setCurrentPage(currentPage + 1);
        }
    };

    useEffect(() => {
        const startIndex = currentPage * valuesPerPage;
        const endIndex = startIndex + valuesPerPage;
        setCurrentValues(allValues[selectedFilter].slice(startIndex, endIndex));
    }, [selectedFilter, currentPage]);

    const indexOfLastJob = jobPage * jobsPerPage;
    const indexOfFirstJob = indexOfLastJob - jobsPerPage;
    // const currentJobs = jobs.slice(indexOfFirstJob, indexOfLastJob);
    // const totalPages = Math.ceil(jobs.length / jobsPerPage);
    // const currentJobs = posts.slice(indexOfFirstJob, indexOfLastJob);
    const currentJobs = posts.length > 0 ? posts : [];
    // const totalPages = Math.ceil(posts.length / jobsPerPage);


    const paginate = (pageNumber) => {
        console.log(`Navigating to page: ${pageNumber}`); // Debug
        setJobPage(pageNumber);
    };
    

    const handleAllJob = () => {
        navigate(`/alljob`);
    }

    return (
        <div id="jobhome-container">
        <div className="header">
            <h1>Việc làm mới nhất</h1>
            <button className="view-all-btn" onClick={handleAllJob}>Xem tất cả</button>
        </div>

        <div className="job-list-container">
            <div className="job-list">
                {isLoading ? ( // Hiển thị khi đang tải
                    <div className="loading-container">
                        <img src={loadinggif} alt="Loading..." className="loading-gif" />
                    </div>
                ) : currentJobs.length > 0 ? ( // Hiển thị danh sách công việc
                    currentJobs.map((job) => (
                        <div
                            key={job.id}
                            className="job-card"
                            onClick={() => handleJobDetail(job.id)}
                        >
                            <div className="job-heart">
                                <Heart size={20} className="heart-icon" />
                            </div>
                            <div className="job-content">
                                <img
                                    // src={job.companyImage ? require(`../images/${job.companyImage}`) : DefaultImage}
                                    src={DefaultImage}
                                    alt={`${job.companyName || "Công ty"} logo`}
                                    className="job-logo"
                                />
                                <div className="job-info">
                                    <h3 className="job-title" title={job.title}>
                                        {job.title?.length > 25 ? job.title.slice(0, 25) + '...' : job.title || "Chưa có tiêu đề"}
                                    </h3>
                                    <p className="job-company" title={job.companyName}>
                                        {job.companyName?.length > 35 ? job.companyName.slice(0, 35) + '...' : job.companyName || "Chưa có công ty"}
                                    </p>
                                    <p className="job-salary" title={job.minSalary}>
                                        <span className="icon-and-text">
                                            <DollarSign size={16} />
                                            {job.minSalary} - {job.maxSalary} triệu
                                        </span>
                                    </p>
                                    <p className="job-location" title={job.province}>
                                        <span className="icon-and-text">
                                            <MapPin size={16} />
                                            {job.province}
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>Không có công việc nào.</p>
                )}
            </div>

             {/* Pagination */}
             <div className="pagination">
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index + 1}
                        onClick={() => paginate(index + 1)}
                        className={jobPage === index + 1 ? 'active' : ''}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>
    </div>
    );
}

export default Job_Home;
