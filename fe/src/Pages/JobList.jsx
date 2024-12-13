import { useNavigate } from 'react-router-dom';
const format = (date, formatString) => {
  const d = new Date(date);
  const day = d.getDate().toString().padStart(2, '0');
  const month = (d.getMonth() + 1).toString().padStart(2, '0');
  const year = d.getFullYear();
  return formatString === 'dd/MM/yyyy' ? `${day}/${month}/${year}` : d.toLocaleDateString();
};
const JobList = ({ jobs, loading, pagination, onPageChange }) => {
    const navigate = useNavigate(); 
    const isHotJob = (updatedAt) => {
      const jobDate = new Date(updatedAt);
      const currentDate = new Date();
      const daysDifference = (currentDate - jobDate) / (1000 * 60 * 60 * 24);
      return daysDifference <= 30;
  };
    const { currentPage, totalPages, totalItems } = pagination;
    const renderPagination = () => {
      // Chỉ render nếu có nhiều hơn 1 trang
      if (totalPages <= 1) return null;
  
      return (
        <div className="mt-6 flex flex-col items-center">
          <div className="flex justify-center items-center gap-2 mb-2">
          <button
              onClick={() => onPageChange('prev')}
              disabled={currentPage === 1}
              className="px-4 py-2 rounded-lg bg-blue-50 text-blue-600 
                        disabled:opacity-50 disabled:cursor-not-allowed
                        hover:bg-blue-100 transition-colors"
            >
              Trước
            </button>
            <span className="px-4 py-2 rounded-lg bg-blue-600 text-white">
            {currentPage}/{totalPages}
          </span>
            <button
              onClick={() => onPageChange('next')}
              disabled={currentPage === totalPages}
              className="px-4 py-2 rounded-lg bg-blue-50 text-blue-600 
                        disabled:opacity-50 disabled:cursor-not-allowed
                        hover:bg-blue-100 transition-colors"
            >
              Sau
            </button>
          </div>
          <div className="text-gray-600 text-sm">
            Tổng số {totalItems} kết quả
          </div>
        </div>
      );
    };
    if (loading) {
      return (
        <div className="space-y-4">
          {[...Array(3)].map((_, i) => (
            <div key={i} className="animate-pulse bg-white p-6 rounded-lg shadow-md">
              <div className="h-4 bg-gray-200 rounded w-3/4 mb-4"></div>
              <div className="h-3 bg-gray-200 rounded w-1/2 mb-2"></div>
              <div className="h-3 bg-gray-200 rounded w-1/3"></div>
            </div>
          ))}
        </div>
      );
    }
  
    if (!jobs || !Array.isArray(jobs) || jobs.length === 0) {
      return (
        <div className="text-center py-8">
          <div className="text-gray-500 mb-4">
            <svg className="mx-auto h-12 w-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M12 12h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 className="text-lg font-medium text-gray-900 mb-2">Không tìm thấy kết quả</h3>
          <p className="text-gray-500">Vui lòng thử lại với từ khóa khác</p>
        </div>
      );
    }
  
    return (
        <div className="space-y-6 max-w-4xl mx-auto">
          {/* Hiển thị số lượng kết quả */}
          <div className="text-gray-600 mb-4 mt-4 text-center">
            Hiển thị {jobs.length}/{totalItems} kết quả
          </div>
          
          {/* Danh sách jobs */}
          {jobs.map(job => (
            <div 
              key={job.id} 
              className="group relative bg-white p-6 rounded-xl shadow-md 
                transition-all duration-300 ease-in-out transform hover:scale-[1.02] hover:shadow-2xl 
                border border-gray-100 hover:border-blue-200 
                hover:bg-gradient-to-br hover:from-blue-50 hover:to-white"
            >
              {isHotJob(job.updatedAt) && (
                <span className="absolute top-2 left-2 bg-red-500 text-white 
                  text-xs font-bold px-2.5 py-1 rounded-full animate-bounce "style={{marginTop:-10,marginLeft:-16}}>
                  HOT
                </span>
              )}
              <div className="flex items-start justify-between">
              <div className="flex-grow min-w-0">
                
              <h3 
                    className="text-2xl font-extrabold bg-gradient-to-r from-indigo-700 to-indigo-900 
                      bg-clip-text text-transparent group-hover:from-blue-600 group-hover:to-blue-800
                      transition-all duration-300 truncate"
                    title={job.title}
                  >
                    {job.title}
                  </h3>
    
    {/* Company name với truncate */}
    <p 
      className="text-lg mt-1 font-semibold bg-gradient-to-r from-purple-600 to-purple-800 
      bg-clip-text text-transparent group-hover:from-purple-500 group-hover:to-purple-700
      transition-all duration-300 truncate"
    title={job.companyName}
  >
      {job.companyName}
    </p>
  </div>
  <div className="relative flex-shrink-0 ml-4">
    <img 
      src={job.companyImage} 
      // alt={job.companyName}
      className="w-16 h-16 object-cover rounded-lg shadow-sm 
        group-hover:shadow-md transition-all duration-300
        transform group-hover:scale-110"
    />
    
    {job.isHot && (
      <span className="absolute -top-2 -right-2 bg-red-500 text-white 
        text-xs font-bold px-2 py-1 rounded-full animate-pulse">
        Hot
      </span>
    )}
  </div>
</div>
    
              <div className="mt-4 grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <p className="text-sm text-gray-600 hover:text-blue-500 
                    transition-colors duration-200 flex items-center">
                    <span className="w-5">💼</span>
                    <span className="ml-2">{job.jobType}</span>
                  </p>
                  <p className="text-sm text-gray-600 hover:text-blue-500 
                    transition-colors duration-200 flex items-center">
                    <span className="w-5">🏢</span>
                    <span className="ml-2">{job.province}, {job.city}, {job.ward}</span>
                  </p>
                  <p className="text-sm text-gray-600 hover:text-blue-500 
                    transition-colors duration-200 flex items-center">
                    <span className="w-5">⭐</span>
                    <span className="ml-2">Kinh nghiệm: {job.yoe === null ? 'Không yêu cầu kinh nghiệm' : `${job.yoe} năm`}</span>
                  </p>
                </div>
                <div className="space-y-2">
                  <p className="text-sm text-gray-600 hover:text-blue-500 
                    transition-colors duration-200 flex items-center">
                    <span className="w-5">💰</span>
                    <span className="ml-2 font-medium text-green-600">
                      {job.minSalary} - {job.maxSalary} triệu
                    </span>
                  </p>
                  <p className="text-sm text-gray-600 hover:text-blue-500 
                    transition-colors duration-200 flex items-center">
                    <span className="w-5">📊</span>
                    <span className="ml-2">{job.level}</span>
                  </p>
                  <p className="text-sm text-gray-600 hover:text-blue-500 
                    transition-colors duration-200 flex items-center">
                    <span className="w-5">⏰</span>
                    <span className="ml-2">{job.schedule}</span>
                  </p>
                </div>
              </div>
    
              {/* Skills với animation */}
              <div className="mt-4">
          <div className="flex flex-wrap gap-2">
            {job.skills && typeof job.skills === 'string' ? (
              job.skills.split(',').map((skill, index) => (
                <span 
                  key={index}
                  className="px-3 py-1 text-sm font-medium rounded-full
                    bg-blue-50 text-blue-600 hover:bg-blue-100
                    transition-all duration-200 hover:scale-105
                    cursor-default"
                >
                  {skill.trim()}
                </span>
              ))
            ) : (
              <span>No skills available</span>
            )}
            {/* Skills với animation */}
            {/* <div className="mt-4">
                        <div className="flex flex-wrap gap-2">
                          {job.skills.split(',').map((skill, index) => (
                            <span 
                              key={index}
                              className="px-3 py-1 text-sm font-medium rounded-full
                                bg-blue-50 text-blue-600 hover:bg-blue-100
                                transition-all duration-200 hover:scale-105
                                cursor-default"
                            >
                              {skill.trim()}
                            </span>
                          ))} */}
                        </div>
                      </div>
            
                      <div className="mt-4 flex justify-between items-center pt-4 border-t border-gray-100">
                        <div className="text-sm text-gray-500">
                          Cập nhật: {new Date(job.updatedAt).toLocaleDateString('vi-VN')}
                        </div>
                        <button 
                  onClick={() => navigate(`http://47.128.243.193:8080/JobDetail/${job.id}`)}
                  className="bg-blue-500 text-white px-6 py-2 rounded-lg
                    hover:bg-blue-600 active:bg-blue-700 
                    transform hover:-translate-y-0.5 active:translate-y-0
                    transition-all duration-200
                    shadow-md hover:shadow-lg
                    flex items-center space-x-2"
                >
                  <span>Xem chi tiết</span>
                  <svg 
                    className="w-4 h-4 transform group-hover:translate-x-1 transition-transform duration-200" 
                    fill="none" 
                    viewBox="0 0 24 24" 
                    stroke="currentColor"
                  >
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </button>
              </div>
    
              {/* Thêm badge cho urgent jobs */}
              {job.isUrgent && (
                <div className="absolute top-4 right-4">
                  <span className="bg-yellow-100 text-yellow-800 text-xs font-semibold 
                    px-2.5 py-0.5 rounded-full">
                    Urgent
                  </span>
                </div>
              )}
            </div>
          ))}
          
          {/* Phân trang */}
          {renderPagination()}
        </div>
      );
    };
  
  export default JobList;