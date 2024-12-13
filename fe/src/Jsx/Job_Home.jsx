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
import loadinggif from '../images/Evitare loader.gif';
import '../css/Job_Home.css';
import '../css/JobList.css';
import DefaultImage from '../images/logo1.png';
import TokenManager from '../utils/tokenManager';

function Job_Home() {
    const [selectedFilter, setSelectedFilter] = useState('nganh-nghe');
    const [currentValues, setCurrentValues] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [jobPage, setJobPage] = useState(1);
    const [posts, setPosts] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [notification, setNotification] = useState(null); // Trạng thái thông báo
    const [interestedPosts, setInterestedPosts] = useState([]);
    const [isSaving, setIsSaving] = useState(false);



    const [role, setRole] = useState(null);
    const [userId, setUserId] = useState(null);
    const [userInfo, setUserInfo] = useState(null);
    const token = TokenManager.getToken();

    useEffect(() => {
        if (token) {
            setRole(token.role?.toLowerCase());
            setUserId(token.id);
        }
    }, [token]);

    const navigate = useNavigate();
    useEffect(() => {
        const fetchInterestedPosts = async () => {
            if (!userId) return; // Không gọi API nếu chưa có userId

            try {
                const response = await fetch(`http://47.128.243.193:8080/applicants/${userId}/interested-posts`, {
                    headers: {
                        'Authorization': `Bearer ${token?.value}`,
                    },
                });

                if (response.ok) {
                    const result = await response.json();
                    setInterestedPosts(result.data?.map(post => post.id) || []);
                } else {
                    console.error("Failed to fetch interested posts");
                }
            } catch (error) {
                console.error("Error fetching interested posts:", error);
            }
        };

        fetchInterestedPosts();
    }, [userId, token]);

    const handleSave = async (e, id) => {
        e.stopPropagation(); // Prevent triggering the card click

        if (!userId) {
            setNotification('Vui lòng đăng nhập để lưu bài đăng!');
            setTimeout(() => setNotification(null), 3000);
            return;
        }
        else if (role === 'company') {
            setNotification('Bạn phải là ứng viên mới có thể lưu bài đăng!');
            setTimeout(() => setNotification(null), 3000);
            return;
        }

        setIsSaving(true); // Bắt đầu lưu, thay đổi biểu tượng thành GIF loading
        try {
            const response = await fetch('http://47.128.243.193:8080/applicants/interested-posts', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token.value}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    applicantId: userId,
                    jobPostingId: id
                })
            });

            const result = await response.json();

            if (response.ok) {
                setNotification(result.message); // Hiển thị thông báo từ API
                setTimeout(() => setNotification(null), 3000);
            } else {
                setNotification('Đã xảy ra lỗi. Vui lòng thử lại!');
                setTimeout(() => setNotification(null), 3000);
            }
        } catch (error) {
            console.error('Error saving post:', error);
            setNotification('Đã xảy ra lỗi. Vui lòng thử lại!');
            console('Đã xảy ra lỗi!')
            setTimeout(() => setNotification(null), 3000);
        } finally {
            setIsSaving(false); // Kết thúc quá trình lưu, trở lại biểu tượng tim
            setTimeout(() => setNotification(null), 3000);
        }
    };


    useEffect(() => {
        const fetchJobs = async () => {
            setIsLoading(true);
            try {
                const response = await fetch(`http://47.128.243.193:8080/public/jobpostings?page=${jobPage}`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setPosts(data.listResult || []);
                setTotalPages(data.totalPage || 1);
            } catch (error) {
                console.error('Error fetching jobs:', error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchJobs();
    }, [jobPage]);

    const handleJobDetail = (jobId) => {
        if (jobId) {
            navigate(`/JobDetail/${jobId}`);
        } else {
            console.warn("Invalid job ID");
        }
    };

    const paginate = (pageNumber) => {
        setJobPage(pageNumber);
    };

    const handleAllJob = () => {
        navigate(`/alljob`);
    };

    return (
        <div id="jobhome-container">
            {notification && (
                <div className="notification-popup">
                    {notification}
                </div>
            )}

            <div className="header">
                <h1>Việc làm mới nhất</h1>
                <button className="view-all-btn" onClick={handleAllJob}>Xem tất cả</button>
            </div>

            <div className="job-list-container">
                <div className="job-list">
                    {isLoading ? (
                        <div className="loading-container">
                            <img src={loadinggif} alt="Loading..." className="loading-gif" />
                        </div>
                    ) : posts.length > 0 ? (
                        posts.map((job) => (
                            <div
                                key={job.id}
                                className="job-card"
                                onClick={() => handleJobDetail(job.id)}
                            >


                                <div className="job-content">
                                    <img
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
                                                <DollarSign size={16}
                                                    style={{ color: 'green' }}
                                                />
                                                {job.minSalary} - {job.maxSalary} triệu
                                            </span>
                                        </p>
                                        <p className="job-location" title={job.province}>
                                            <span className="icon-and-text">
                                                <MapPin size={16}
                                                    style={{ color: 'blue' }}
                                                />
                                                {job.province}
                                            </span>
                                        </p>
                                    </div>
                                </div>
                                <div
                                    className={`job-heart ${interestedPosts.includes(job.id) ? 'saved' : ''}`}
                                    onClick={(e) => handleSave(e, job.id)}
                                >
                                    <Heart
                                        size={20}
                                        className={`heart-icon ${isSaving ? 'saving' : ''} ${interestedPosts.includes(job.id) ? 'red' : ''}`}
                                    />
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
