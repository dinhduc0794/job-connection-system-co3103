import { useNavigate } from 'react-router-dom';
const JobList = ({ jobs, loading, page, totalPages, onPageChange }) => {
    const navigate = useNavigate(); 
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
          {jobs.map(job => (
            <div 
              key={job.id} 
              className="group bg-white p-6 rounded-xl shadow-md hover:shadow-xl 
                transform hover:-translate-y-1 transition-all duration-300 ease-in-out
                border border-gray-100 hover:border-blue-200"
            >
              <div className="flex items-start justify-between">
              <div className="flex-grow min-w-0">
                
              <h3 
      className="text-xl font-bold bg-gradient-to-r from-indigo-700 to-indigo-900 
        bg-clip-text text-transparent group-hover:from-blue-600 group-hover:to-blue-800
        transition-all duration-300 truncate"
      title={job.title} // Thêm tooltip khi hover
    >
      {job.title}
    </h3>
    
    {/* Company name với truncate */}
    <p 
      className="text-lg mt-1 font-semibold bg-gradient-to-r from-purple-600 to-purple-800 
        bg-clip-text text-transparent group-hover:from-purple-500 group-hover:to-purple-700
        transition-all duration-300 truncate"
      title={job.companyName} // Thêm tooltip khi hover
    >
      {job.companyName}
    </p>
  </div>
  <div className="relative flex-shrink-0 ml-4">
    <img 
      src={job.companyImage} 
      alt={job.companyName}
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
                    <span className="ml-2">{job.province}, {job.city}</span>
                  </p>
                  <p className="text-sm text-gray-600 hover:text-blue-500 
                    transition-colors duration-200 flex items-center">
                    <span className="w-5">⭐</span>
                    <span className="ml-2">Kinh nghiệm: {job.yoe} năm</span>
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
                  ))}
                </div>
              </div>
    
              <div className="mt-4 flex justify-between items-center pt-4 border-t border-gray-100">
                <div className="text-sm text-gray-500">
                  Cập nhật: {new Date(job.updatedAt).toLocaleDateString('vi-VN')}
                </div>
                <button 
          onClick={() => navigate(`/JobDetail/${job.id}`)}
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
        </div>
      );
    };
  
  export default JobList;