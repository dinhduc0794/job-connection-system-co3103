import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Job_Home.css';
import './JobList.css';

function Job_Home({ onFilterChange }) {
    const [selectedFilter, setSelectedFilter] = useState('nganh-nghe');
    const [selectedValues, setSelectedValues] = useState({
        'nganh-nghe': '',
        'muc-luong': '',
        'dia-diem': ''
    });
    const [currentValues, setCurrentValues] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [jobPage, setJobPage] = useState(1);
    const [filteredJobs, setFilteredJobs] = useState([]);

    const allValues = {
        'nganh-nghe': ['Kinh doanh', 'Phiên dịch', 'Báo chí', 'Viễn thông', 'Game', 'IT'],
        'muc-luong': ['Tất cả', 'Dưới 10 triệu', '10-20 triệu', '20-30 triệu', 'Trên 30 triệu'],
        'dia-diem': ['Tất cả', 'Hà Nội', 'TP Hồ Chí Minh', 'Miền Bắc', 'Miền Nam']
    };

    const jobs = [
        { 
            id: 1, 
            title: 'Quản lý dự án - PM', 
            company: 'Công Ty FSI', 
            description: '20 - 27 triệu',
            category: 'IT',
            salary: '20-30 triệu',
            location: 'Hà Nội'
        },
        { 
            id: 2, 
            title: 'Backend Developer', 
            company: 'Company B', 
            description: 'Thỏa thuận',
            category: 'IT',
            salary: '10-20 triệu',
            location: 'TP Hồ Chí Minh'
        },
        {
            id: 3,
            title: 'Front-end Developer',
            company: 'Startup X',
            description: 'Cơ hội phát triển cao',
            category: 'IT',
            salary: '15-25 triệu',
            location: 'Đà Nẵng'
        },
        {
            id: 4,
            title: 'Designer',
            company: 'Công ty thiết kế Y',
            description: 'Môi trường làm việc sáng tạo',
            category: 'Design',
            salary: '18-28 triệu',
            location: 'Hà Nội'
        },
        {
            id: 5,
            title: 'Data Analyst',
            company: 'Công ty Dữ liệu Z',
            description: 'Phân tích dữ liệu lớn',
            category: 'Data Science',
            salary: '20-30 triệu',
            location: 'TP. Hồ Chí Minh'
        },
        
        {
            id: 6,
            title: 'Marketing Manager',
            company: 'Công ty Thương mại T',
            description: 'Lập kế hoạch marketing',
            category: 'Marketing',
            salary: '25-35 triệu',
            location: 'Hà Nội'
        },
        {
            id: 7,
            title: 'Designer',
            company: 'Công ty thiết kế Y',
            description: 'Môi trường làm việc sáng tạo',
            category: 'Design',
            salary: '18-28 triệu',
            location: 'Hà Nội'
        },
        {
            id: 8,
            title: 'Data Analyst',
            company: 'Công ty Dữ liệu Z',
            description: 'Phân tích dữ liệu lớn',
            category: 'Data Science',
            salary: '20-30 triệu',
            location: 'TP. Hồ Chí Minh'
        },
        
        {
            id: 9,
            title: 'Marketing Manager',
            company: 'Công ty Thương mại T',
            description: 'Lập kế hoạch marketing',
            category: 'Marketing',
            salary: '25-35 triệu',
            location: 'Hà Nội'
        }
        // ... thêm các công việc khác với đầy đủ thông tin lọc
    ];

    const valuesPerPage = 5;
    const jobsPerPage = 9;
    const navigate = useNavigate();

    // Xử lý khi người dùng click vào một giá trị lọc
    const handleValueClick = (value) => {
        setSelectedValues(prev => ({
            ...prev,
            [selectedFilter]: value
        }));
    };

    // Lọc công việc dựa trên các giá trị đã chọn
    const filterJobs = () => {
        let filtered = [...jobs];

        if (selectedValues['nganh-nghe'] && selectedValues['nganh-nghe'] !== 'Tất cả') {
            filtered = filtered.filter(job => job.category === selectedValues['nganh-nghe']);
        }

        if (selectedValues['muc-luong'] && selectedValues['muc-luong'] !== 'Tất cả') {
            filtered = filtered.filter(job => job.salary === selectedValues['muc-luong']);
        }

        if (selectedValues['dia-diem'] && selectedValues['dia-diem'] !== 'Tất cả') {
            filtered = filtered.filter(job => job.location === selectedValues['dia-diem']);
        }

        setFilteredJobs(filtered);
        setJobPage(1); // Reset về trang đầu tiên khi lọc
    };

    useEffect(() => {
        filterJobs();
    }, [selectedValues]);

    useEffect(() => {
        const startIndex = currentPage * valuesPerPage;
        const endIndex = startIndex + valuesPerPage;
        setCurrentValues(allValues[selectedFilter].slice(startIndex, endIndex));
    }, [selectedFilter, currentPage]);

    const handleFilterChange = (event) => {
        const selected = event.target.value;
        setSelectedFilter(selected);
        setCurrentPage(0);
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

    const handleJobDetail = (jobId) => {
        navigate(`/JobDetail/${jobId}`);
    };

    // Tính toán các công việc cho trang hiện tại
    const indexOfLastJob = jobPage * jobsPerPage;
    const indexOfFirstJob = indexOfLastJob - jobsPerPage;
    const currentJobs = filteredJobs.slice(indexOfFirstJob, indexOfLastJob);
    const totalPages = Math.ceil(filteredJobs.length / jobsPerPage);

    const paginate = (pageNumber) => setJobPage(pageNumber);

    return (
        <div>
            <div id="jobhome-container">
                <div className="header">
                    <h1>Việc làm tốt nhất</h1>
                    <button className="view-all-btn" onClick={() => navigate('/all-jobs')}>
                        Xem tất cả
                    </button>
                </div>

                <div className="filter-container">
                    <div className="filter-left">
                        <label>Lọc theo:</label>
                        <select onChange={handleFilterChange} value={selectedFilter}>
                            <option value="nganh-nghe">Ngành nghề</option>
                            <option value="muc-luong">Mức lương</option>
                            <option value="dia-diem">Địa điểm</option>
                        </select>
                    </div>

                    <div className="filter-right">
                        <button className="arrow-btn" onClick={handlePreviousClick}>{"<"}</button>
                        <div className="filter-values">
                            {currentValues.map((value, index) => (
                                <span
                                    key={index}
                                    className={`filter-value ${selectedValues[selectedFilter] === value ? 'active' : ''}`}
                                    onClick={() => handleValueClick(value)}
                                >
                                    {value}
                                </span>
                            ))}
                        </div>
                        <button className="arrow-btn" onClick={handleNextClick}>{">"}</button>
                    </div>
                </div>

                <div className="job-list-container">
                    <div className="job-list">
                        {currentJobs.map((job) => (
                            <div
                                key={job.id}
                                className="job-card"
                                onClick={() => handleJobDetail(job.id)}
                            >
                                <h3>{job.title}</h3>
                                <p><strong>Company:</strong> {job.company}</p>
                                <p className="job-description" title={job.description}>
                                    <strong>Lương: </strong>
                                    {job.description.length > 30 ? job.description.slice(0, 30) + '...' : job.description}
                                </p>
                            </div>
                        ))}
                    </div>

                    {totalPages > 1 && (
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
                    )}
                </div>
            </div>
        </div>
    );
}

export default Job_Home;