import React, { useState } from 'react';
import '../css/ListCompany.css';

function ListCompany() {
  const companies = [
    { logo: '🏢', name: 'Company A', industry: 'Tech', jobs: 120 },
    { logo: '🏭', name: 'Company B', industry: 'Manufacturing', jobs: 85 },
    { logo: '🏦', name: 'Company C', industry: 'Finance', jobs: 40 },
    { logo: '💻', name: 'Company D', industry: 'Software', jobs: 70 },
    { logo: '🚀', name: 'Company E', industry: 'Aerospace', jobs: 95 },
    { logo: '🏗️', name: 'Company F', industry: 'Construction', jobs: 110 },
    { logo: '📦', name: 'Company G', industry: 'Logistics', jobs: 60 },
    { logo: '🍔', name: 'Company H', industry: 'Food', jobs: 55 },
    { logo: '📱', name: 'Company I', industry: 'Telecom', jobs: 45 },
    { logo: '✈️', name: 'Company J', industry: 'Aviation', jobs: 130 },
    { logo: '🛒', name: 'Company K', industry: 'Retail', jobs: 90 },
    { logo: '🔬', name: 'Company L', industry: 'Research', jobs: 35 },
  ];

  const itemsPerPage = 9; // Mỗi trang hiển thị 9 công ty
  const totalPages = Math.ceil(companies.length / itemsPerPage);
  const [currentPage, setCurrentPage] = useState(0);

  const startIndex = currentPage * itemsPerPage;
  const currentItems = companies.slice(startIndex, startIndex + itemsPerPage);

  const handleNextPage = () => setCurrentPage((prevPage) => (prevPage + 1) % totalPages);
  const handlePreviousPage = () => setCurrentPage((prevPage) => (prevPage - 1 + totalPages) % totalPages);
  const handlePageClick = (pageIndex) => setCurrentPage(pageIndex);

  return (
    <div>
      <div id="list-company-container">
        {/* Dòng đầu tiên */}
        <div className="header">
          <h2>Các Công Ty Lớn</h2>
          <button className="view-all-btn" onClick={() => alert('Chuyển sang trang khác')}>
            Xem tất cả
          </button>
        </div>

        {/* Flexbox chứa các công ty */}
        <div className="company-flexbox-container">
          <button className="carousel-control left" onClick={handlePreviousPage}>
            &lt;
          </button>

          <div className="company-list">
            {currentItems.map((company, index) => (
              <div className="company-card" key={index}>
              {/* Phần trên: logo + thông tin */}
              <div className="card-top">
                <div className="company-logo">{company.logo}</div>
                <div className="company-info">
                  <h3>{company.name}</h3>
                  <p>{company.industry}</p>
                </div>
              </div>
            
              {/* Phần dưới: số lượng việc làm */}
              <div className="card-bottom">
                <p>Việc làm: {company.jobs}</p>
              </div>
            </div>
            ))}
          </div>

          <button className="carousel-control right" onClick={handleNextPage}>
            &gt;
          </button>
        </div>

        {/* Dấu chấm biểu thị các trang */}
        <div className="pagination-dots">
          {Array.from({ length: totalPages }).map((_, pageIndex) => (
            <span
              key={pageIndex}
              className={`dot ${currentPage === pageIndex ? 'active' : ''}`}
              onClick={() => handlePageClick(pageIndex)}
            ></span>
          ))}
        </div>
      </div>
    </div>
  );
}

export default ListCompany;
