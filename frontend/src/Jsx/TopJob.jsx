import React, { useState } from 'react';
import '../css/TopJob.css';

function TopJob() {
  // Dữ liệu mẫu cho các ngành nghề
  const industries = [
    { logo: '📈', name: 'Kinh doanh', count: 100 },
    { logo: '📝', name: 'Phiên dịch', count: 50 },
    { logo: '🎮', name: 'Game', count: 75 },
    { logo: '💻', name: 'IT', count: 120 },
    { logo: '📡', name: 'Bưu chính', count: 30 },
    { logo: '📰', name: 'Báo chí', count: 40 },
    { logo: '📞', name: 'Viễn thông', count: 90 },
    { logo: '💼', name: 'Marketing', count: 110 },
    { logo: '🏢', name: 'Xây dựng', count: 65 },
  ];

  // Xác định trang hiện tại và các ngành nghề hiển thị trên mỗi trang
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 8; // 2 hàng x 4 ô

  // Chia danh sách các ngành nghề thành các trang
  const totalPages = Math.ceil(industries.length / itemsPerPage);
  const startIndex = currentPage * itemsPerPage;
  const currentItems = industries.slice(startIndex, startIndex + itemsPerPage);

  // Xử lý chuyển trang
  const handleNextPage = () => {
    setCurrentPage((prevPage) => (prevPage + 1) % totalPages);
  };

  const handlePreviousPage = () => {
    setCurrentPage((prevPage) => (prevPage - 1 + totalPages) % totalPages);
  };

  return (
    <div id="industry-container">
      {/* Header với tiêu đề và nút chuyển trang */}
      <div className="industry-header">
        <h2>Top Các Ngành Nghề Nổi Bật</h2>
        <div className="pagination-controls">
          <button onClick={handlePreviousPage}>&lt;</button>
          <button onClick={handleNextPage}>&gt;</button>
        </div>
      </div>

      {/* Danh sách ngành nghề */}
      <div className="industry-list">
        {currentItems.map((industry, index) => (
          <div key={index} className="industry-card">
            <div className="industry-logo">{industry.logo}</div>
            <div className="industry-info">
              <h3>{industry.name}</h3>
              <p>{industry.count} công việc</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TopJob;
