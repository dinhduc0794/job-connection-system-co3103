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
            setIsLoading(true); // Bắt đầu tải
            try {
                const response = await fetch('/jobpostings');
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setPosts(data);
            } catch (error) {
                console.error('Error fetching jobs:', error);
            } finally {
                setIsLoading(false); // Kết thúc tải
            }
        };
        fetchJobs();
    }, []);

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
    const currentJobs = posts.length > 0 
    ? posts.slice(indexOfFirstJob, indexOfLastJob)
    : [];
    const totalPages = Math.ceil(posts.length / jobsPerPage);


    const paginate = (pageNumber) => setJobPage(pageNumber);

    return (
        <div id="jobhome-container">
        <div className="header">
            <h1>Việc làm tốt nhất</h1>
            <button className="view-all-btn" onClick={() => alert('Chuyển sang trang khác')}>Xem tất cả</button>
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
                                    src={job.companyImage ? require(`../images/${job.companyImage}`) : DefaultImage}
                                    alt={`${job.companyName || "Công ty"} logo`}
                                    className="job-logo"
                                />
                                <div className="job-info">
                                    <h3 className="job-title" title={job.title}>
                                        {job.title?.length > 25 ? job.title.slice(0, 25) + '...' : job.title || "Chưa có tiêu đề"}
                                    </h3>
                                    <p className="job-company" title={job.companyName}>
                                        {job.companyName || 'Không có tên công ty'}
                                    </p>
                                    <p className="job-salary" title={job.minSalary}>
                                        <span className="icon-and-text">
                                            <DollarSign size={16} />
                                            {job.minSalary} - {job.maxSalary}
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





// const jobs = [
//     { id: 1, logo: 'https://icolor.vn/wp-content/uploads/2022/03/logo-vingroup.jpg',
//          title: 'Quản lý dự án - PM', company: 'Công Ty Cổ Phần Đầu Tư Thương Mại Và Phát Triển Công Nghệ FSI', salary: '20 - 27 triệu', location: 'Hà Nội' },
//     { id: 2, logo:'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4Q82eAkv7WH4rOneemtfTuuEcoN0t2z5QWw&s',
//         title: 'Backend Developer', company: 'Company B', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 3, logo:'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAF7GdiqagbkpF49NynmJF5xVC2PKqKjzocg&s',
//         title: 'Full Stack Developer', company: 'Company C', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 4, logo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAHXPluq6GtTRPDIHRv5kJPy86uFjp5sO7hg&s',
//          title: 'Data Scientist', company: 'Company D', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 5, logo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzDdgCcGyJ-lpQfyzNa5SonMe9XWA00odLGg&s',
//          title: 'Mobile Developer', company: 'Company E', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 6, logo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAF7GdiqagbkpF49NynmJF5xVC2PKqKjzocg&s',
//          title: 'DevOps Engineer', company: 'Company F', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 7, logo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4Q82eAkv7WH4rOneemtfTuuEcoN0t2z5QWw&s',
//          title: 'UI/UX Designer', company: 'Company G', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 8, logo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_uTZZ1Ww1dkoVAabEBarS19qoWKbww3BzMw&s',
//          title: 'QA Engineer', company: 'Company H', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 9, logo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNfzY0jYPDNjRaFdyT7cpvSabL8l69GLcULQ&s',
//          title: 'Project Manager', company: 'Company I', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 10, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'Cybersecurity Expert', company: 'Company J', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 11, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'Cloud Architect', company: 'Company K', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 12, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'AI Engineer', company: 'Company L', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 13, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'Blockchain Developer', company: 'Company M', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 14, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'Marketing Manager', company: 'Company N', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 15, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'Product Manager', company: 'Company O', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 16, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'Product Manager', company: 'Company O', salary: 'Thỏa thuận', location: 'Hà Nội'  },
//     { id: 17, logo: 'https://www.saokim.com.vn/blog/wp-content/uploads/2022/04/logo-moi-cua-starbucks.jpg.webp',
//          title: 'Product Manager', company: 'Company O', salary: 'Thỏa thuận', location: 'Hà Nội'  },
// ];

                    {/* {currentJobs.map((job) => (
                        <div
                            key={job.id}
                            className="job-card"
                            onClick={() => handleJobDetail(job.id)}
                        >
                            
                            <div className="job-heart">
                                <Heart size={20} className="heart-icon" />
                            </div>
                            { <button
                                onClick={(e) => handleSave(e, job.id)}
                                className="p-2 hover:bg-gray-100 rounded-full transition-colors"
                            >
                                <Heart
                                className={`w-6 h-6 ${
                                    savedJobs.includes(job.id)
                                    ? "fill-red-500 text-red-500"
                                    : "text-gray-400"
                                }`}
                                />
                            </button> }
                            <div className="job-content">
                                <img
                                    src={job.logo} 
                                    src={require(`../images/${job.logo}`)}
                                    alt={`${job.company} logo`} 
                                    className="job-logo" 
                                />
                                <div className="job-info">
                                    <h3 className='job-title' title={job.title}>
                                        {job.title.length > 25 ? job.title.slice(0, 25) + '...' : job.title}
                                    </h3>
                                    <p className='job-company' title={job.company}> 
                                        {job.company.length > 40 ? job.company.slice(0, 40) + '...' : job.company}
                                    </p>
                                    <p className="job-salary" title={job.salary}>
                                         <DollarSign size={16} />
                                         {job.salary.length > 30 ? job.salary.slice(0, 30) + '...' : job.salary}
                                         <span className="icon-and-text">
                                            <DollarSign size={16} />
                                            {job.salary.length > 30 ? job.salary.slice(0, 30) + '...' : job.salary}
                                        </span>
                                    </p>
                                    <p className="job-location" title={job.location}>
                                        <span className='icon-and-text'>
                                            <MapPin size={16} />
                                            {job.location.length > 25 ? job.location.slice(0, 25) + '...' : job.location}
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))} */}