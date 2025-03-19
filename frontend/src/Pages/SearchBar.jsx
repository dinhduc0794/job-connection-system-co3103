import { useState, useEffect } from 'react';

const suggestionsData = [
    // Công nghệ thông tin
    'Frontend Developer',
    'Backend Developer',
    'Fullstack Developer',
    'Mobile Developer',
    'DevOps Engineer',
    'Data Engineer',
    'Data Scientist',
    'Machine Learning Engineer',
    'AI Engineer',
    'Cloud Engineer',
    'System Administrator',
    'Network Engineer',
    'Security Engineer',
    'QA Engineer',
    'Software Architect',
    'Technical Lead',
    'Blockchain Developer',
    'Game Developer',
  
    // Thiết kế & Sáng tạo
    'UI/UX Designer',
    'Graphic Designer',
    'Product Designer',
    'Motion Designer',
    'Visual Designer',
    '3D Artist',
    'Content Creator',
    'Digital Marketing',
    'SEO Specialist',
    'Content Writer',
  
    // Quản lý & Kinh doanh
    'Project Manager',
    'Product Manager',
    'Business Analyst',
    'Sales Manager',
    'Marketing Manager',
    'Account Manager',
    'HR Manager',
    'Operations Manager',
    'Finance Manager',
    'CEO',
    'CTO',
  
    // Tài chính & Kế toán
    'Accountant',
    'Financial Analyst',
    'Tax Consultant',
    'Auditor',
    'Investment Analyst',
    'Risk Analyst',
  
    // Y tế & Chăm sóc sức khỏe
    'Medical Doctor',
    'Nurse',
    'Pharmacist',
    'Dentist',
    'Physical Therapist',
    'Nutritionist',
  
    // Giáo dục
    'Teacher',
    'Professor',
    'Education Consultant',
    'Academic Researcher',
    'Curriculum Developer',
    'Educational Technology Specialist',
  
    // Kỹ thuật & Công nghiệp
    'Mechanical Engineer',
    'Electrical Engineer',
    'Civil Engineer',
    'Chemical Engineer',
    'Industrial Designer',
    'Architecture',
    'Construction Manager',
  
    // Dịch vụ & Khách hàng
    'Customer Service Representative',
    'Customer Success Manager',
    'Technical Support',
    'Sales Representative',
    'Account Executive',
  
    // Logistics & Vận tải
    'Supply Chain Manager',
    'Logistics Coordinator',
    'Warehouse Manager',
    'Import/Export Specialist',
    'Fleet Manager',
  
    // Luật & Tư vấn
    'Lawyer',
    'Legal Consultant',
    'Compliance Officer',
    'Patent Attorney',
    'Corporate Counsel'
  ];
  const searchOptions = [
    { value: 'title', label: 'Tên công việc' },
    { value: 'companyName', label: 'Tên công ty' },
    { value: 'jobType', label: 'Loại công việc' },
    { value: 'schedule', label: 'Lịch làm việc', type: 'enum', options: [
      'FULL_TIME', 'PART_TIME', 'REMOTE', 'HYBRID'
    ]},
    
    { value: 'yoe', label: 'Kinh nghiệm (năm)', type: 'number' },
    { value: 'salary', label: 'Mức lương', type: 'number' },
    { value: 'province', label: 'Tỉnh/Thành phố' },
    { value: 'city', label: 'Quận/Huyện' },
    { value: 'ward', label: 'Phường/Xã' },
    { value: 'companyRating', label: 'Đánh giá công ty', type: 'number' },
    { value: 'allowance', label: 'Phụ cấp', type: 'number' }
  ];

const SearchBar = ({ onSearch }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchType, setSearchType] = useState('title');
  const [filteredSuggestions, setFilteredSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!searchTerm.trim()) {
      alert('Vui lòng nhập từ khóa tìm kiếm!');
      return;
    }
    
    onSearch({
      type: searchType,
      value: searchTerm.trim()
    });
    setShowSuggestions(false);
  };

  const handleChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);

    if (value.trim()) {
      const filtered = suggestionsData.filter((item) =>
        item.toLowerCase().includes(value.toLowerCase())
      );
      setFilteredSuggestions(filtered);
      setShowSuggestions(true);
    } else {
      setFilteredSuggestions([]);
      setShowSuggestions(false);
    }
  };

  const handleSuggestionClick = (suggestion) => {
    setSearchTerm(suggestion);
    setShowSuggestions(false);
    onSearch({
      type: searchType,
      value: suggestion
    });
  };

  // Đóng bảng gợi ý khi click bên ngoài
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (!event.target.closest('.search-bar-container')) {
        setShowSuggestions(false);
      }
    };
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);
  const renderInput = () => {
    const selectedOption = searchOptions.find(opt => opt.value === searchType);

    if (selectedOption?.type === 'enum') {
      return (
        <select
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full p-5 text-lg rounded-xl border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white"
        >
          <option value="">Chọn {selectedOption.label}</option>
          {selectedOption.options.map(opt => (
            <option key={opt} value={opt}>{opt}</option>
          ))}
        </select>
      );
    }

    if (selectedOption?.type === 'number') {
      return (
        <input
          type="number"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder={`Nhập ${selectedOption.label.toLowerCase()}...`}
          className="w-full p-5 text-lg rounded-xl border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white"
        />
      );
    }
    return (
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="Nhập từ khóa tìm kiếm..."
          className="w-full p-5 text-lg rounded-xl border border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white"
        />
      );
    };

    return (
        <div className="bg-white/80 backdrop-blur-lg p-6 rounded-3xl shadow-2xl hover:shadow-blue-200/50 transition-all duration-300 border border-gray-100">
      <form onSubmit={handleSubmit} className="relative flex flex-col lg:flex-row gap-4">
        {/* Select box với animation */}
        <select
          value={searchType}
          onChange={(e) => {
            setSearchType(e.target.value);
            setSearchTerm('');
          }}
          className="w-full lg:w-44 p-3 text-base rounded-2xl border border-gray-200 
                     focus:ring-2 focus:ring-blue-400 focus:border-transparent 
                     bg-white/90 transition-all duration-200 
                     hover:border-blue-300 cursor-pointer
                     shadow-sm hover:shadow-md"
            >
              <option value="title">Tên công việc</option>
              <option value="companyName">Tên công ty</option>
              <option value="jobType">Loại công việc</option>
              <option value="schedule">Lịch làm việc</option>
              <option value="level">Cấp bậc</option>
              <option value="yoe">Kinh nghiệm (năm)</option>
              <option value="salary">Mức lương</option>
              <option value="province">Tỉnh/Thành phố</option>
              <option value="city">Quận/Huyện</option>
              <option value="ward">Phường/Xã</option>
              <option value="companyRating">Đánh giá công ty</option>
              <option value="allowance">Phụ cấp</option>
            </select>
      
            <div className="flex-1 relative group">
          {searchType === 'schedule' ? (
            <select
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full p-3 text-base rounded-2xl border border-gray-200 
                         focus:ring-2 focus:ring-blue-400 focus:border-transparent 
                         bg-white/90 transition-all duration-200
                         group-hover:border-blue-300
                         shadow-sm group-hover:shadow-md"
                >
                  <option value="">Chọn lịch làm việc</option>
            <option value="FULLTIME">Full-time</option>
            <option value="PARTTIME">Part-time</option>
            <option value="INTERNSHIP">Internship</option>
            <option value="FREELANCE">Freelance</option>
            <option value="CONTRACT">Contract</option>
                </select>
              ) : searchType === 'level' ? (
                <select
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full p-3 text-base rounded-2xl border border-gray-200 
                         focus:ring-2 focus:ring-blue-400 focus:border-transparent 
                         bg-white/90 transition-all duration-200
                         group-hover:border-blue-300
                         shadow-sm group-hover:shadow-md"
                >
                  <option value="">Chọn cấp bậc</option>
            <option value="INTERN">Intern - Thực tập sinh</option>
            <option value="FRESHER">Fresher - Nhân viên mới</option>
            <option value="JUNIOR">Junior - Nhân viên có kinh nghiệm cơ bản</option>
            <option value="SENIOR">Senior - Nhân viên dày dạn kinh nghiệm</option>
            <option value="LEAD">Lead - Trưởng nhóm</option>
            <option value="MANAGER">Manager - Quản lý</option>
            <option value="DIRECTOR">Director - Giám đốc</option>
                </select>
              ) : ['yoe', 'salary', 'companyRating', 'allowance'].includes(searchType) ? (
                <input
                  type="number"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder={`Nhập ${searchOptions.find(opt => opt.value === searchType)?.label.toLowerCase()}...`}
                  className="w-full p-3 text-base rounded-2xl border border-gray-200 
                         focus:ring-2 focus:ring-blue-400 focus:border-transparent 
                         bg-white/90 transition-all duration-200
                         group-hover:border-blue-300
                         shadow-sm group-hover:shadow-md"
                />
              ) : (
                <div className="relative">
                  <input
                    type="text"
                    value={searchTerm}
                    onChange={handleChange}
                    placeholder="Nhập từ khóa tìm kiếm..."
                    className="w-full p-3 text-base rounded-2xl border border-gray-200 
                    focus:ring-2 focus:ring-blue-400 focus:border-transparent 
                    bg-white/90 transition-all duration-200
                    group-hover:border-blue-300
                    shadow-sm group-hover:shadow-md"
                  />
                  {showSuggestions && filteredSuggestions.length > 0 && (
                    <ul className="absolute z-10 w-full bg-white/90 backdrop-blur-sm border 
                    border-gray-200 rounded-xl shadow-xl max-h-60 overflow-y-auto mt-1 
                    animate-slideDown">
                      {filteredSuggestions.map((suggestion, index) => (
                        <li
                          key={index}
                          onClick={() => handleSuggestionClick(suggestion)}
                          className="px-4 py-2 hover:bg-blue-50 cursor-pointer 
                                 transition-colors duration-150"
                        >
                          {suggestion}
                        </li>
                      ))}
                    </ul>
                  )}
                </div>
              )}
            </div>
      
            <button
              type="submit"
              className="w-full lg:w-auto bg-blue-500 text-white px-6 py-3 text-lg rounded-xl hover:bg-blue-600 transition-colors duration-200 flex items-center justify-center"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clipRule="evenodd" />
              </svg>
              <span>Tìm kiếm</span>
            </button>
          </form>
        </div>
      );
    }

export default SearchBar;
