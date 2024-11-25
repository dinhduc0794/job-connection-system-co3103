import React from 'react';
import loadinggif from '../images/Evitare loader.gif';
import '../css/Loading.css';

const Loading = () => {
    return (
        <div className="loading-page">
            <div className="loading-container">
                <img src={loadinggif} alt="Loading..." className="loading-gif" />
                <p className="loading-text">Đang tải dữ liệu, vui lòng chờ...</p>
            </div>
        </div>
    );
};

export default Loading;
