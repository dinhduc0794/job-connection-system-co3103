import { useState, useEffect, useCallback, useMemo } from 'react';
import "./Search.css";

import { ChevronDownIcon, ChevronUpIcon } from '@heroicons/react/24/solid';
import axios from 'axios';
import { useSearchParams } from 'react-router-dom';
import './Spinner.css';


import JobList from './JobList';

import TokenManager from '../utils/tokenManager';
import AppNavbar from '../Jsx/AppNavbar';
import CompanyNavbar from '../Jsx/CompanyNavbar';
import Navbar from '../Jsx/navbar';
import Footer from '../Jsx/Footer';
import { set } from 'react-hook-form';

const AllJob = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);

  // Location States
  const [locations, setLocations] = useState([]);
  const [cities, setCities] = useState([]);
  const [wards, setWards] = useState([]);
  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedWard, setSelectedWard] = useState('');


  const [jobTypes, setJobTypes] = useState([]);
  const [selectedJobType, setSelectedJobType] = useState('');
  const [sortByTime, setSortByTime] = useState('desc');
  const [expandedSections, setExpandedSections] = useState({
    location: false,
    requirements: false,
    compensation: false,
    sort: false,
    company: false,
  });
  const toggleSection = (sectionName) => {
    setExpandedSections(prev => ({
      ...prev,
      [sectionName]: !prev[sectionName]
    }));
  };
  const calculateHeight = () => {
    switch (expandedSections) {
      case 'company': return 120;
      case 'requirements': return 420;
      case 'location': return 420;
      case 'compensation': return 420;
      case 'sort': return 120;
      default: return 220;
    }
  };
  const [searchTitle, setSearchTitle] = useState('');
  const [schedule, setSchedule] = useState('');
  const [experienceLevel, setExperienceLevel] = useState('');
  const [salaryRange, setSalaryRange] = useState('');
  const [yoe, setYoe] = useState('');
  const [companyName, setCompanyName] = useState('');
  const [allowance, setAllowance] = useState('');
  const [minOfApplicants, setMinOfApplicants] = useState('');
  // Skills State
  const [jobTypeSkills, setJobTypeSkills] = useState([]);
  const [skills, setSkills] = useState([]);
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [skillSearchTerm, setSkillSearchTerm] = useState('');

  // Pagination
  const [localPage, setLocalPage] = useState(() => {
    const pageFromUrl = searchParams.get('page');
    return pageFromUrl ? parseInt(pageFromUrl, 10) : 1;
  });
  const [pagination, setPagination] = useState({
    currentPage: 1,
    maxPageItems: 9,
    totalPages: 0,
    totalItems: 0
  });

  const token = TokenManager.getToken();
  const role = token?.role;
  useEffect(() => {
    const fetchJobTypes = async () => {
      try {
        const response = await axios.get('http://47.128.243.193:8080/jobtypes');
        setJobTypes(response.data);
      } catch (error) {
        console.error('Error fetching job types:', error);
      }
    };
    fetchJobTypes();
  }, []);
  useEffect(() => {
    if (selectedJobType) {
      const selectedType = jobTypes.find(type => type.name === selectedJobType);
      if (selectedType && selectedType.skills) {
        setJobTypeSkills(selectedType.skills);
      }
    } else {
      // Nếu không có job type được chọn, hiển thị tất cả các kỹ năng
      setJobTypeSkills(skills);
    }
  }, [selectedJobType, jobTypes, skills]);
  useEffect(() => {
    fetch("http://47.128.243.193:8080/public/locations")
      .then((response) => response.json())
      .then((data) => setLocations(data));
  }, []);

  // Fetch Skills
  useEffect(() => {
    const fetchSkills = async () => {
      try {
        const headers = {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          ...(token?.value && { Authorization: `Bearer ${token.value}` })
        };

        const response = await axios.get('http://47.128.243.193:8080/public/skills', {
          headers,
          withCredentials: true
        });

        setSkills(Array.isArray(response.data) ? response.data : []);
      } catch (error) {
        console.error('Error fetching skills:', error);
        setSkills([]);
      }
    };

    fetchSkills();
  }, []);

  useEffect(() => {
    const province = locations.find((prov) => prov.name === selectedProvince);
    setCities(province ? province.cities : []);
    if (!province) {
      setSelectedCity("");
      setWards([]);
    }
  }, [selectedProvince, locations]);

  useEffect(() => {
    const city = cities.find((city) => city.name === selectedCity);
    setWards(city ? city.wards : []);
    if (!city) {
      setSelectedWard("");
    }
  }, [selectedCity, cities]);

  // Fetch Jobs
  useEffect(() => {
    const fetchJobs = async () => {
      setLoading(true);
      try {
        const params = {
          page: localPage,
          limit: 9
        };

        // Prepare parameters for skills and job type
        ['title', 'schedule', 'level', 'yoe', 'salary', 'allowance', 'sortByTime',
          'companyName', 'province', 'city', 'ward', 'jobType', 'skills'].forEach(filter => {
            const value = searchParams.get(filter);
            if (value?.trim()) {
              params[filter] = filter === 'skills'
                ? value.split(',').map(skill => decodeURIComponent(skill.trim()))
                : filter === 'jobType'
                  ? decodeURIComponent(value.trim())
                  : decodeURIComponent(value.trim());
            }
          });

        const hasActiveFilters = Object.keys(params).some(
          key => key !== 'page' && key !== 'limit' && params[key]
        );

        const queryString = new URLSearchParams(
          Object.fromEntries(
            Object.entries(params).map(([key, value]) =>
              key === 'skills' || key === 'jobType'
                ? [key, Array.isArray(value) ? value.join(',') : value]
                : [key, value]
            )
          )
        ).toString();

        const response = await axios({
          method: 'GET',
          url: hasActiveFilters
            ? `http://47.128.243.193:8080/public/jobpostings?${queryString}`
            : `http://47.128.243.193:8080/public/jobpostings?page=${localPage}&limit=9`,
          headers: {
            'Content-Type': 'application/json',
            ...(token?.value && { Authorization: `Bearer ${token.value}` })
          },
          withCredentials: true
        });

        // Rest of the existing code remains the same
        if (response.data && response.data.listResult) {
          setJobs(response.data.listResult);

          const newTotalPages = response.data.totalPage || 0;
          const newTotalItems = response.data.totalItem || 0;
          const actualCurrentPage = response.data.currentPage || localPage;

          setPagination({
            currentPage: actualCurrentPage,
            maxPageItems: response.data.limit || 9,
            totalPages: newTotalPages,
            totalItems: newTotalItems
          });

          if (actualCurrentPage !== localPage) {
            setLocalPage(actualCurrentPage);
            const params = new URLSearchParams(searchParams);
            params.set('page', actualCurrentPage.toString());
            setSearchParams(params);
          }
        } else {
          setJobs([]);
          setPagination(prev => ({
            ...prev,
            totalPages: 0,
            totalItems: 0
          }));
        }
      } catch (error) {
        console.error('Error fetching jobs:', error);
        setJobs([]);
        setPagination(prev => ({
          ...prev,
          totalPages: 0,
          totalItems: 0
        }));
      }
      setLoading(false);
    };

    fetchJobs();
  }, [localPage, searchParams]);

  const handlePageChange = (direction) => {
    const newPage = direction === 'next'
      ? Math.min(localPage + 1, pagination.totalPages)
      : Math.max(localPage - 1, 1);

    const params = new URLSearchParams(searchParams);
    params.set('page', newPage.toString());
    setSearchParams(params);
    setLocalPage(newPage);
  };

  // Thêm các hàm xóa cho từng trường
  const clearTitle = () => {
    setSearchTitle('');
    const params = new URLSearchParams(searchParams);
    params.delete('title');
    setSearchParams(params);
  };

  const clearCompanyName = () => {
    setCompanyName('');
    const params = new URLSearchParams(searchParams);
    params.delete('companyName');
    setSearchParams(params);
  };

  const handleSearch = () => {
    const params = new URLSearchParams(searchParams);

    // Reset page to 1 when searching
    params.delete('page');

    const handleTextField = (fieldName, value) => {
      if (value == null || value.trim() === '') {
        params.delete(fieldName); // Xóa nếu rỗng hoặc null
      } else {
        params.set(fieldName, value.trim()); // Gán giá trị sau khi trim
      }
    };
    handleTextField('title', searchTitle);
    handleTextField('companyName', companyName);
    const handleNumericField = (fieldName, value) => {
      // Kiểm tra kỹ hơn: 
      // Nếu value là null, undefined, 0, chuỗi rỗng, chỉ toàn khoảng trắng, hoặc không phải số
      if (
        value == null ||
        value === '0' ||
        value === 0 ||
        value.toString().trim() === '' ||
        isNaN(Number(value))
      ) {
        params.delete(fieldName);
      } else {
        // Chuyển đổi sang số để loại bỏ các số 0 đứng đầu
        params.set(fieldName, Number(value).toString());
      }
    };
    // Áp dụng xử lý cho các trường số
    handleNumericField('salary', salaryRange);
    handleNumericField('yoe', yoe);
    handleNumericField('minOfApplicants', minOfApplicants);
    handleNumericField('allowance', allowance);

    // Các xử lý khác như cũ
    if (searchTitle) params.set('title', searchTitle);
    if (schedule) params.set('schedule', schedule);
    if (experienceLevel) params.set('level', experienceLevel);
    if (companyName) params.set('companyName', companyName);

    if (selectedJobType) {
      params.set('jobType', encodeURIComponent(selectedJobType));
    } else {
      params.delete('jobType');
    }

    if (sortByTime) params.set('sortByTime', sortByTime);
    if (selectedProvince) params.set('province', selectedProvince);
    if (selectedCity) params.set('city', selectedCity);
    if (selectedWard) params.set('ward', selectedWard);

    if (selectedSkills.length > 0) {
      params.set('skills', selectedSkills.map(skill => encodeURIComponent(skill)).join(','));
    } else {
      params.delete('skills');
    }

    setSearchParams(params);
  };


  // Handle Clear All Filters
  const handleClearAllFilters = () => {
    setSearchParams({});

    // Reset all filter states
    setSelectedProvince("");
    setSelectedCity("");
    setSelectedWard("");
    setSearchTitle("");
    setSchedule("");
    setExperienceLevel("");
    setSelectedJobType("");
    setMinOfApplicants("");
    setSalaryRange("");
    setYoe("");
    setCompanyName("");
    setAllowance("");
    setSelectedSkills([]);

  };

  // Render Navbar based on role
  const renderNavbar = () => {
    if (role === 'applicant') return <AppNavbar />;
    if (role === 'company') return <CompanyNavbar />;
    return <Navbar />;
  };



  return (
    <>
      {renderNavbar()}


      <div className="h-auto bg-gradient-to-b from-blue-50 via-white to-blue-50">
        <div className="container max-w-7xl mx-auto px-4 py-8 mt-20">
          <div className={`w-full h-[${calculateHeight()}px] rounded-xl bg-gradient-to-br from-thirdColor to-themeColor text-white shadow-2xl p-6 border-l-7 border-blue-700 transition-all duration-500 hover:shadow-3xl hover:scale-105 hover:border-purple-600 from-blue-300 via-blue-400 to-purple-170`}>
            {/* Main Search Bar */}


            <div className="mb-6 relative">
              <div className="relative shadow-lg">
                <input
                  type="text"
                  placeholder="Nhập tên công việc bạn muốn tìm"
                  className="w-full p-4 pl-14 pr-36 text-lg border-0 rounded-full 
      bg-white text-gray-800 
      focus:ring-4 focus:ring-blue-200 
      transition duration-300 ease-in-out
      placeholder-gray-400
      hover:shadow-xl
      transform hover:scale-[1.01]"
                  value={searchTitle}
                  onChange={(e) => setSearchTitle(e.target.value)}
                />

                {/* Icon search với animation */}
                <div className="absolute inset-y-0 left-0 pl-5 flex items-center pointer-events-none">
                  <svg
                    className="h-7 w-7 text-blue-500 transition-transform duration-300 
        group-hover:rotate-12 group-focus:rotate-12"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                    />
                  </svg>
                </div>

                <div className="absolute inset-y-0 right-0 flex items-center pr-2 space-x-2">
                  {/* Filter chip cho title */}
                  {searchTitle && (
                    <div className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm flex items-center">
                      <span>{searchTitle}</span>
                      <button
                        onClick={clearTitle}
                        className="ml-2 text-blue-500 hover:text-blue-700"
                      >
                        ×
                      </button>
                    </div>
                  )}



                </div>
              </div>

              {/* Gợi ý tìm kiếm nhanh */}
              <div className="mt-3 flex flex-wrap gap-2 opacity-70 hover:opacity-100 transition-opacity">
                <span className="text-sm text-red-500">Gợi ý:</span>
                {[
                  'Kỹ sư phần mềm',
                  'Nhà thiết kế đồ họa',
                  'Chuyên viên marketing kỹ thuật số',
                  'Nhân viên kinh doanh',
                  'Quản trị hệ thống',
                  'Chuyên viên SEO',

                  'Quản lý truyền thông xã hội'

                ].map(tag => (
                  <button
                    key={tag}
                    className="bg-gray-100 text-gray-700 px-3 py-1 rounded-full text-xs 
      hover:bg-blue-100 hover:text-blue-800 transition duration-300"
                    onClick={() => setSearchTitle(tag)}
                  >
                    {tag}
                  </button>

                ))}
              </div>
            </div>

            {/* Filter Sections */}
            <div className="grid grid-cols-1 md:grid-cols-5 lg:grid-cols-5 gap-5">
              {/* Location Column */}
              <div className="space-y-2">
                <div
                  className="filter-header cursor-pointer p-2 bg-gray-50 rounded-lg"
                  onClick={() => toggleSection('company')}
                >
                  <div className="flex justify-between items-center">
                    <span className="font-medium text-gray-700 mx-auto">Tên Công Ty</span>
                    {expandedSections.company ? <ChevronUpIcon className="h-5 w-5" /> : <ChevronDownIcon className="h-5 w-5" />}
                  </div>
                </div>

                {expandedSections.company && (
                  <div className="p-2 space-y-2 bg-white rounded-lg">
                    <input
                      type="text"
                      placeholder="Nhập tên công ty"
                      className="w-full p-2 rounded border text-gray-700"
                      value={companyName}
                      onChange={(e) => setCompanyName(e.target.value)}
                    />
                  </div>
                )}
                {companyName && (
                  <div className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm flex items-center">
                    <span>{companyName}</span>
                    <button
                      onClick={clearCompanyName}
                      className="ml-2 text-blue-500 hover:text-blue-700"
                    >
                      ×
                    </button>
                  </div>
                )}
              </div>
              <div className="space-y-2">
                <div
                  className="filter-header cursor-pointer p-2 bg-gray-50 rounded-lg"
                  // onClick={() => setExpandedSection(expandedSection === 'location' ? null : 'location')}
                  onClick={() => toggleSection('location')}
                >
                  <div className="flex justify-between items-center">
                    <span className="font-medium text-gray-700 mx-auto">Địa điểm</span>
                    {expandedSections.location ? <ChevronUpIcon className="h-5 w-5" /> : <ChevronDownIcon className="h-5 w-5" />}
                  </div>
                </div>

                {expandedSections.location && (
                  <div className="p-2 space-y-2 border-blue-200 bg-blue-50 focus:border-blue-500 focus:ring-1 focus:ring-blue-300 transition duration-200 rounded-lg shadow-sm">
                    <select
                      className="w-full p-2 rounded border"
                      value={selectedProvince}
                      onChange={(e) => setSelectedProvince(e.target.value)}
                    >
                      <option value="">Chọn Tỉnh/Thành phố</option>
                      {locations.map((prov) => (
                        <option key={prov.id} value={prov.name}>{prov.name}</option>
                      ))}
                    </select>
                    <label className="block text-sm font-medium text-gray-700">Quận/Huyện</label>
                    <select
                      className="w-full p-2 rounded border"
                      onChange={(e) => setSelectedCity(e.target.value)}
                      value={selectedCity}
                      disabled={!selectedProvince}
                    >
                      <option value="">Chọn Quận/Huyện</option>
                      {cities.map((city) => (
                        <option key={city.id} value={city.name}>
                          {city.name}
                        </option>
                      ))}
                    </select>
                    <label className="block text-sm font-medium text-gray-700">Phường/Xã</label>
                    <select
                      className="w-full p-2 rounded border"
                      onChange={(e) => setSelectedWard(e.target.value)}
                      value={selectedWard}
                      disabled={!selectedCity}
                    >
                      <option value="">Chọn Phường/Xã</option>
                      {wards.map((ward) => (
                        <option key={ward.id} value={ward.name}>
                          {ward.name}
                        </option>
                      ))}
                    </select>
                    {/* City and Ward selects remain the same */}
                  </div>
                )}
              </div>

              {/* Job Type and Characteristics Column */}
              <div className="space-y-2">
                <div
                  className="filter-header cursor-pointer p-2 bg-gray-50 rounded-lg"
                  onClick={() => toggleSection('requirements')}
                >
                  <div className="flex justify-between items-center">
                    <span className="font-medium text-gray-700 mx-auto">Yêu cầu</span>
                    {expandedSections.requirements ? <ChevronUpIcon className="h-5 w-5" /> : <ChevronDownIcon className="h-5 w-5" />}
                  </div>
                </div>

                {expandedSections.requirements && (
                  <div className="p-2 space-y-2 bg-white rounded-lg">
                    <select
                      className="w-full p-2 rounded border"
                      value={experienceLevel}
                      onChange={(e) => setExperienceLevel(e.target.value)}

                    >
                      <option value="">Cấp độ</option>
                      <option value="INTERN">Thực tập sinh</option>
                      <option value="FRESHER">Nhân viên mới</option>
                      <option value="JUNIOR">Nhân viên cơ bản</option>
                      <option value="SENIOR">Nhân viên dày dạn kinh nghiệm</option>
                      <option value="LEAD">Trưởng nhóm</option>
                      <option value="MANAGER">Quản lý</option>
                      <option value="DIRECTOR">Giám đốc</option>
                    </select>
                    <label className="block text-sm font-medium text-gray-700">Số năm kinh nghiệm</label>
                    <input
                      type="number"
                      placeholder="Số năm kinh nghiệm"
                      className="w-full p-2 rounded border text-gray-500"
                      value={yoe}
                      min="0"
                      onChange={(e) => setYoe(Math.max(0, Number(e.target.value)).toString())}
                    />
                    <label className="block text-sm font-medium text-gray-700">Số lượng tuyển tối thiểu</label>
                    <input
                      type="number"
                      placeholder="Số lượng đơn tuyển"
                      className="w-full p-2 rounded border text-gray-500"
                      value={minOfApplicants}
                      min="0"
                      onChange={(e) => setMinOfApplicants(Math.max(0, Number(e.target.value)).toString())}
                    />
                  </div>
                )}
              </div>

              {/* Company and Salary Column */}
              <div className="space-y-2">
                <div
                  className="filter-header cursor-pointer p-2 bg-gray-50 rounded-lg"
                  onClick={() => toggleSection('compensation')}
                >
                  <div className="flex justify-between items-center">
                    <span className="font-medium text-gray-700 mx-auto">Đãi ngộ</span>
                    {expandedSections.compensation ? <ChevronUpIcon className="h-5 w-5" /> : <ChevronDownIcon className="h-5 w-5" />}
                  </div>
                </div>

                {expandedSections.compensation && (
                  <div className="p-2 space-y-2 bg-white rounded-lg">
                    <select
                      className="w-full p-2 rounded border"
                      value={schedule}
                      onChange={(e) => setSchedule(e.target.value)}
                    >
                      <option value="">Hình thức làm việc</option>
                      <option value="FULLTIME">Toàn thời gian</option>
                      <option value="PARTTIME">Bán thời gian</option>
                      <option value="INTERNSHIP">Thực tập sinh</option>
                      <option value="FREELANCE">Làm việc tự do</option>
                      <option value="CONTRACT">Theo hợp đồng</option>
                    </select>


                    <div className="space-y-2">
                      <label className="block text-sm font-medium text-gray-700">Mức lương ($)</label>
                      <input
                        type="number"
                        placeholder="Nhập mức lương"
                        className="w-full p-2 rounded border text-gray-500"
                        value={salaryRange}
                        min="0"
                        onChange={(e) => setSalaryRange(Math.max(0, Number(e.target.value)).toString())}
                      />
                    </div>
                    <label className="block text-sm font-medium text-gray-700">Phụ cấp ($)</label>
                    <input
                      type="number"
                      placeholder="Phụ cấp"
                      className="w-full p-2 rounded border text-gray-500"
                      value={allowance}
                      min="0"
                      onChange={(e) => setAllowance(Math.max(0, Number(e.target.value)).toString())}
                    />
                  </div>
                )}
              </div>




              <div className="space-y-2">
                <div
                  className="filter-header cursor-pointer p-2 bg-gray-50 rounded-lg"
                  onClick={() => toggleSection('sort')}
                >
                  <div className="flex justify-between items-center">
                    <span className="font-medium text-gray-700 mx-auto">Sắp xếp</span>
                    {expandedSections.sort ? <ChevronUpIcon className="h-5 w-5" /> : <ChevronDownIcon className="h-5 w-5" />}
                  </div>
                </div>
                {/* Skills Column */}

                {expandedSections.sort && (
                  <div className="p-2 space-y-2 bg-white rounded-lg">
                    <select
                      className="w-full p-2 rounded border"
                      value={sortByTime}
                      onChange={(e) => setSortByTime(e.target.value)}
                    >
                      <option value="desc">Mới nhất trước</option>
                      <option value="asc">Cũ nhất trước</option>
                    </select>
                  </div>
                )}
              </div>
            </div>

          </div>





          {/* Search and Reset Buttons */}
          {/* Search and Reset Buttons */}
          <div className="flex justify-center space-x-4 mt-6">
            <button
              className="px-8 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-full 
    hover:from-blue-500 hover:to-blue-600 shadow-lg 
    transform transition-transform duration-300 hover:scale-105 focus:outline-none 
    focus:ring-4 focus:ring-blue-300 focus:ring-opacity-50"
              onClick={handleSearch}
            >
              Tìm Kiếm
            </button>

            <button
              className="px-8 py-3 bg-gradient-to-r from-gray-600 to-gray-700 text-white rounded-full 
    hover:from-gray-500 hover:to-gray-600 shadow-lg 
    transform transition-transform duration-300 hover:scale-105 focus:outline-none 
    focus:ring-4 focus:ring-gray-400 focus:ring-opacity-50"
              onClick={handleClearAllFilters}
            >
              Đặt Lại
            </button>
          </div>

        </div>
      </div>





      <div className="container mx-auto px-4">
        <div className="flex flex-col md:flex-row gap-4 items-start"> {/* Thay đổi layout */}
          <div className="w-full md:w-1/4 mt-20"> {/* Sidebar filter */}
            {/* Job Type Section */}
            <div className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden">
              <div className="bg-gray-50 px-4 py-3 border-b border-gray-200">
                <h3 className="font-semibold text-gray-800">Loại công việc</h3>
              </div>
              <div className="p-4">
                <div className="relative">
                  <select
                    className="w-full p-3 rounded-lg border border-gray-300 
                     focus:outline-none focus:ring-2 focus:ring-blue-500 
                     text-gray-700 appearance-none"
                    value={selectedJobType}
                    onChange={(e) => setSelectedJobType(e.target.value)}
                  >
                    <option value="" className="text-gray-500">Tất cả loại công việc</option>
                    {jobTypes.map((type) => (
                      <option key={type.id} value={type.name} className="text-gray-700">
                        {type.name}
                      </option>
                    ))}
                  </select>
                  <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
                    <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                      <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" />
                    </svg>
                  </div>
                </div>
              </div>
            </div>



            <div className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden">
              <div className="bg-gray-50 px-4 py-3 border-b border-gray-200">
                <h3 className="font-semibold text-gray-800">Kỹ năng</h3>
              </div>
              <div className="p-4">
                <div className="relative mb-4">
                  <input
                    type="text"
                    placeholder="Tìm kiếm kỹ năng..."
                    value={skillSearchTerm}
                    onChange={(e) => setSkillSearchTerm(e.target.value)}
                    className="w-full p-3 pl-10 rounded-lg border border-gray-300 
                     focus:outline-none focus:ring-2 focus:ring-blue-500 
                     text-gray-700"
                  />
                  <div className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                    </svg>
                  </div>
                </div>
                <div className="max-h-64 overflow-y-auto space-y-2 custom-scrollbar">
                  {skills
                    .filter(skill => skill.name.toLowerCase().includes(skillSearchTerm.toLowerCase()))
                    .map((skill) => (
                      <div
                        key={skill.id}
                        className="flex items-center hover:bg-gray-50 p-2 rounded-md transition-colors duration-200"
                      >
                        <input
                          type="checkbox"
                          id={`skill-${skill.id}`}
                          checked={selectedSkills.includes(skill.name)}
                          onChange={() => {
                            setSelectedSkills(prev =>
                              prev.includes(skill.name)
                                ? prev.filter(s => s !== skill.name)
                                : [...prev, skill.name]
                            );
                          }}
                          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded mr-3"
                        />
                        <label
                          htmlFor={`skill-${skill.id}`}
                          className="text-sm text-gray-700 cursor-pointer select-none"
                        >
                          {skill.name}
                        </label>
                      </div>
                    ))}
                </div>
              </div>
            </div>
          </div>
          {/* Thêm style cho scrollbar tùy chỉnh (nếu cần) */}
          <style jsx>{`
  .custom-scrollbar::-webkit-scrollbar {
    width: 6px;
  }
  .custom-scrollbar::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 10px;
  }
  .custom-scrollbar::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 10px;
  }
  .custom-scrollbar::-webkit-scrollbar-thumb:hover {
    background: #555;
  }
`}</style>
          {/* Job List Section */}
          <div className="w-full md:flex-1"> {/* Job List */}
            <JobList
              jobs={jobs}
              loading={loading}
              pagination={pagination}
              onPageChange={handlePageChange}
            />


          </div>


        </div>
      </div>
      <Footer />
    </>
  );
}




export default AllJob;