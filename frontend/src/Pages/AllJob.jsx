import { useState, useEffect } from 'react';
import axios from 'axios';
import { useSearchParams } from 'react-router-dom';
import './Spinner.css'; 

// Components
import SearchBar from './SearchBar';
import LocationFilter from './LocationFilter';
import JobTypeFilter from './JobTypeFilter';
import SkillsFilter from './SkillsFilter';
import JobList from './JobList';
import ActiveFilters from './ActiveFilters';
import TokenManager from '../utils/tokenManager';
import AppNavbar from '../Jsx/AppNavbar';
import CompanyNavbar from '../Jsx/CompanyNavbar';
import Navbar from '../Jsx/navbar';
import Footer from '../Jsx/Footer';

const AllJob = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 9;
  const token = TokenManager.getToken();
  const getCurrentPageJobs = () => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return jobs.slice(startIndex, endIndex);
  };
  useEffect(() => {
    const fetchJobs = async () => {
      setLoading(true);
      try {
        
        const params = {};
        if (searchParams.has('title')) {
          const title = searchParams.get('title');
          if (title?.trim()) {
            params.title = title.trim();
          }
        }
        
    
        if (params.title) {
          if (typeof params.title !== 'string') {
            console.warn('Invalid title detected:', params.title);
            delete params.title;
          } else {
            params.title = params.title.trim();
            if (params.title === '') delete params.title;
          }
        }
  
        const queryString = new URLSearchParams(params).toString();
        const url = queryString 
          ? `http://localhost:8080/public/jobpostings?${queryString}`
          : 'http://localhost:8080/public/jobpostings';
  
        const response = await axios({
          method: 'GET',
          url: url,
          headers: {
            'Content-Type': 'application/json',
            ...(token?.value && { Authorization: `Bearer ${token.value}` })
          },
          withCredentials: true
        });
  
        if (Array.isArray(response.data)) {
          setJobs(response.data);
          setTotalPages(Math.ceil(response.data.length / 9));
        } else {
          // Nếu không phải mảng, giả sử là object chứa listResult
          setJobs(response.data.listResult || []);
          setTotalPages(response.data.totalPage || 1);
        }
      } catch (error) {
        console.error('Error fetching jobs:', error);
        setJobs([]); // Reset jobs nếu lỗi
        setTotalPages(1);
      }
      setLoading(false);
    };
  
    fetchJobs();
  }, [searchParams, page]);
  const role = token?.role;
  const renderNavbar = () => {
    if (role === 'applicant') {
      return <AppNavbar />;
    } else if (role === 'company') {
      return <CompanyNavbar />;
    } else {
      return <Navbar />;
    }
  };
  const Pagination = () => {
    const totalPages = Math.ceil(jobs.length / itemsPerPage);
  
    return (
      <div className="flex justify-center items-center mt-4 gap-2">
        <button
          onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
          disabled={currentPage === 1}
          className="px-4 py-2 rounded bg-blue-500 text-white disabled:bg-gray-300"
        >
          Trước
        </button>
  
        <span className="mx-4">
          Trang {currentPage} / {totalPages}
        </span>
  
        <button
          onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
          disabled={currentPage === totalPages}
          className="px-4 py-2 rounded bg-blue-500 text-white disabled:bg-gray-300"
        >
          Sau
        </button>
      </div>
    );
  };
  const handleLocationReset = () => {
    const params = new URLSearchParams(searchParams);
    params.delete('provinceId');
    params.delete('cityId');
    params.delete('wardId');
    setSearchParams(params);
  };

  const updateFilters = (key, value) => {
    if (value && typeof value === 'object' && 'type' in value && 'value' in value) {
      const actualValue = value.value.trim();
      if (actualValue) {
        // Xử lý giá trị số
        if (['yoe', 'salary', 'companyRating', 'allowance'].includes(value.type)) {
          const numValue = parseFloat(actualValue);
          if (!isNaN(numValue)) {
            searchParams.set(value.type, numValue);
          }
        } else {
          searchParams.set(value.type, actualValue);
        }
      } else {
        searchParams.delete(value.type);
      }
    } else {
      if (value) {
        searchParams.set(key, value);
      } else {
        searchParams.delete(key);
      }
    }
    setSearchParams(searchParams);
    setPage(1);
  };
  const handleJobTypeFilter = (filterData) => {
    if (typeof filterData === 'string') {
      // Nếu chỉ có jobType
      updateFilters('jobType', filterData);
    } else {
      // Nếu có cả jobType và level
      updateFilters('jobType', filterData.jobType);
      updateFilters('level', filterData.level);
    }
  };
  return (
    <>
      {renderNavbar()}
      
      <div className="min-h-screen bg-gradient-to-b from-blue-50 via-white to-blue-50">
      <div className="container max-w-7xl mx-auto px-4 py-8 mt-20">
        {/* Container cho SearchBar với background và hiệu ứng */}
        
        
        <div 
        className="bg-gradient-to-r from-red-100/50 to-purple-100/50 p-8 rounded-3xl shadow-lg backdrop-blur-sm mb-8"
        style={{
         backgroundImage: 'linear-gradient(45deg, #2d7694 20%, #45a2d6 50%)',
          
        }}
        >
          <h1 className="text-center text-3xl font-bold mb-6 text-white animate-bounce hover:animate-none
  drop-shadow-[0_1.2px_1.2px_rgba(0,0,0,0.8)] 
  hover:bg-gradient-to-r hover:from-white hover:via-blue-200 hover:to-purple-200
  hover:text-transparent hover:bg-clip-text
  transition-all duration-300
  animate-text bg-gradient-to-r from-teal-100 via-white to-blue-100 bg-clip-text">
  Công việc mơ ước của bạn đang ở đây
</h1>
            <SearchBar onSearch={(value) => updateFilters('title', value)} />
          </div>
       
          <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
            {/* Sidebar Filters - Sticky */}
            <aside className="hidden lg:block lg:col-span-1">
            <div className="sticky top-28 overflow-hidden transition-all duration-200">
              <div className="bg-gradient-to-b from-blue-50/80 to-white/80 backdrop-blur-sm rounded-3xl shadow-lg border border-blue-100/50">
                <div className="p-6 max-h-[calc(100vh-150px)] overflow-y-auto custom-scrollbar">
                  <h2 className="text-xl font-semibold mb-6 text-blue-800">Bộ lọc tìm kiếm</h2>
                    <div className="space-y-6">
                      <LocationFilter 
                        onProvinceChange={(value) => updateFilters('province', value)}
                        onCityChange={(value) => updateFilters('city', value)}
                        onWardChange={(value) => updateFilters('ward', value)}
                        onReset={handleLocationReset}
                      />
                      {/* <JobTypeFilter onSelect={handleJobTypeFilter} /> */}
                      <SkillsFilter onSelect={(skills) => updateFilters('skills', skills.join(','))} />
                    </div>
                  </div>
                </div>
              </div>
            </aside>
           
            {/* Main Content */}
            <main className="lg:col-span-3">
            <div className="bg-white/80 backdrop-blur-sm rounded-3xl shadow-lg p-6 mb-6 border border-gray-100">
                <ActiveFilters 
                  filters={Object.fromEntries([...searchParams])}
                  onRemove={(key) => updateFilters(key, null)}
                />
              </div>
              
              <div className="bg-white rounded-xl shadow-sm p-6">
                {loading ? (
                  <div className="flex justify-center items-center h-64">
                    <div className="spinner-border text-blue-500" role="status">
                      <span className="sr-only">Loading...</span>
                    </div>
                  </div>
                ) : (
                  <>
                    <JobList 
                      jobs={jobs}
                      page={page}
                      totalPages={totalPages}
                      onPageChange={setPage}
                    />
                    <Pagination />
                  </>
                )}
              </div>
            </main>
          </div>
        </div>
      </div>
     
      <Footer />
    </>
  );
};
export default AllJob;
