import { useState, useEffect } from 'react';

const JobTypeFilter = ({ onSelect }) => {
    const [fields, setFields] = useState([]); // Lưu trữ các lĩnh vực (IT, Marketing,...)
    const [selectedField, setSelectedField] = useState('');
    const [selectedJobType, setSelectedJobType] = useState('');
    const [selectedLevel, setSelectedLevel] = useState('');
    const [selectedSchedule, setSelectedSchedule] = useState('');
    const [selectedSkills, setSelectedSkills] = useState([]);
    const [loading, setLoading] = useState(false);
  
  // Enum cho Schedule
  const SCHEDULE_TYPES = {
    FULLTIME: 'Full-time',
    PARTTIME: 'Part-time',
    INTERNSHIP: 'Internship',
    FREELANCE: 'Freelance',
    CONTRACT: 'Contract'
  };
  // Enum cho Level
  const LEVEL_TYPES = {
    INTERN: 'Thực tập sinh',
    FRESHER: 'Fresher',
    JUNIOR: 'Junior',
    MIDDLE: 'Middle',
    SENIOR: 'Senior',
    LEADER: 'Leader',
    MANAGER: 'Manager'
  };
   
  useEffect(() => {
    const fetchJobTypes = async () => {
      setLoading(true);
      try {
        const response = await fetch('http://localhost:8080/public/fields', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        setFields(data);
      } catch (error) {
        console.error('Error fetching job types:', error);
        
      }
      setLoading(false);
    };

    fetchJobTypes();
  }, []);
  const getJobTypes = () => {
    const selectedFieldData = fields.find(f => f.id === selectedField);
    return selectedFieldData?.jobTypes || [];
  };

  // Lấy danh sách skills dựa trên job type được chọn
  const getSkills = () => {
    const jobTypes = getJobTypes();
    const selectedJobTypeData = jobTypes.find(jt => jt.id === selectedJobType);
    return selectedJobTypeData?.skills || [];
  };
  const handleFieldChange = (e) => {
    const value = e.target.value;
    setSelectedField(value);
    setSelectedJobType(''); // Reset job type
    setSelectedSkills([]); // Reset skills
    onSelect({ 
      type: 'field', 
      value: value,
      displayText: fields.find(f => f.id === parseInt(value))?.name || ''
    });
  };
  const handleJobTypeChange = (e) => {
    const value = e.target.value;
    setSelectedJobType(value);
    setSelectedSkills([]);
    onSelect({ 
      type: 'jobType', 
      value: value,
      displayText: getJobTypes().find(jt => jt.id === parseInt(value))?.name || ''
    });
  };

  const handleLevelChange = (value) => {
    setSelectedLevel(value);
    onSelect({
      type: 'level',
      value: value
    });
  };
  const handleScheduleChange = (value) => {
    setSelectedSchedule(value);
    onSelect({
      type: 'schedule',
      value: value
    });
  };
  const getSelectedJobTypeData = () => {
    return getJobTypes().find(jt => jt.id === selectedJobType);
  };
  if (loading) {
    return (
      <div className="animate-pulse p-4 space-y-4 bg-white rounded-lg shadow-sm">
        <div className="h-4 bg-gray-200 rounded w-3/4"></div>
        <div className="h-8 bg-gray-200 rounded"></div>
        <div className="h-8 bg-gray-200 rounded"></div>
      </div>
    );
  }

  return (
    <div className="bg-white p-4 rounded-lg shadow-sm space-y-4">
      <h3 className="font-semibold text-lg text-gray-800">Bộ lọc công việc</h3>
      
      {/* Job Type Select */}
      <div className="space-y-2">
        <label className="text-sm text-gray-600 block">Lĩnh vực</label>
        <select
          value={selectedField}
          onChange={(e) => {
            setSelectedField(e.target.value);
            setSelectedJobType(''); // Reset job type khi đổi field
            setSelectedSkills([]); // Reset skills
            onSelect({ type: 'field', value: e.target.value });
          }}
          className="w-full p-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
        >
          <option value="">Tất cả lĩnh vực</option>
          {fields.map(field => (
            <option key={field.id} value={field.id}>{field.name}</option>
          ))}
        </select>
      </div>
      {selectedField && (
  <div className="space-y-2">
    <label className="text-sm text-gray-600 block">Loại công việc</label>
    <select
      value={selectedJobType}
      onChange={(e) => {
        setSelectedJobType(e.target.value);
        setSelectedSkills([]);
        onSelect({ type: 'jobType', value: e.target.value });
      }}
      className="w-full p-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
    >
      <option value="">Tất cả loại công việc</option>
      {fields.find(f => f.id === parseInt(selectedField))?.jobTypes?.map(jobType => (
        <option key={jobType.id} value={jobType.id}>
          {jobType.name}
        </option>
      ))}
    </select>
  </div>
)}

      {/* Skills Multi-Select - Chỉ hiện khi đã chọn Job Type */}
      {selectedJobType && (
        <div className="space-y-2">
          <label className="text-sm text-gray-600 block">Kỹ năng</label>
          <div className="space-y-2 max-h-40 overflow-y-auto custom-scrollbar">
            {getSkills().map(skill => (
              <label key={skill.id} className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  checked={selectedSkills.includes(skill.id)}
                  onChange={() => {
                    const newSkills = selectedSkills.includes(skill.id)
                      ? selectedSkills.filter(id => id !== skill.id)
                      : [...selectedSkills, skill.id];
                    setSelectedSkills(newSkills);
                    onSelect({ type: 'skills', value: newSkills });
                  }}
                  className="rounded text-blue-500"
                />
                <span className="text-sm text-gray-700">{skill.name}</span>
              </label>
            ))}
          </div>
        </div>
      )}

      <div className="space-y-2">
        <label className="text-sm text-gray-600 block">Hình thức làm việc</label>
        <select
          value={selectedSchedule}
          onChange={(e) => handleScheduleChange(e.target.value)}
          className="w-full p-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="">Tất cả hình thức</option>
          {Object.entries(SCHEDULE_TYPES).map(([key, value]) => (
            <option key={key} value={key}>
              {value}
            </option>
          ))}
        </select>
      </div>
      {/* Level Select */}
      <div className="space-y-2">
        <label className="text-sm text-gray-600 block">Cấp độ</label>
        <select
          value={selectedLevel}
          onChange={(e) => handleLevelChange(e.target.value)}
          className="w-full p-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="">Tất cả cấp độ</option>
          {Object.entries(LEVEL_TYPES).map(([key, value]) => (
            <option key={key} value={key}>
              {value}
            </option>
          ))}
        </select>
      </div>
       {/* Job Statistics */}
      {selectedJobType && getSelectedJobTypeData()?.jobPostings?.length > 0 && (
        <div className="mt-4 p-3 bg-gray-50 rounded-md">
          <h4 className="text-sm font-medium text-gray-700 mb-2">Thống kê công việc</h4>
          <div className="text-sm text-gray-600 space-y-1">
            <p className="flex justify-between">
              <span>Số lượng công việc:</span>
              <span className="font-medium">{getSelectedJobTypeData().jobPostings.length}</span>
            </p>
            <p className="flex justify-between">
              <span>Mức lương:</span>
              <span className="font-medium">
                {getSelectedJobTypeData().jobPostings[0].minSalary} - 
                {getSelectedJobTypeData().jobPostings[0].maxSalary} triệu
              </span>
            </p>
            <p className="flex justify-between">
              <span>Hình thức:</span>
              <span className="font-medium">
                {SCHEDULE_TYPES[getSelectedJobTypeData().jobPostings[0].schedule]}
              </span>
            </p>
          </div>
        </div>
      )}

      {/* Related Skills Section */}
      {selectedJobType && getSelectedJobTypeData()?.skills?.length > 0 && (
        <div className="mt-4">
          <h4 className="text-sm font-medium text-gray-700 mb-2">Kỹ năng liên quan:</h4>
          <div className="flex flex-wrap gap-2">
            {getSelectedJobTypeData().skills.map(skill => (
              <span
                key={skill.id}
                className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
              >
                {skill.name}
              </span>
            ))}
          </div>
        </div>
      )}

      {/* Job Statistics */}
      <button
        onClick={() => {
          setSelectedField('');
          setSelectedJobType('');
          setSelectedLevel('');
          setSelectedSchedule('');
          setSelectedSkills([]);
          onSelect({ type: 'reset' });
        }}
        className="w-full mt-4 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg
          hover:bg-gray-200 transition-colors duration-200"
      >
        Xóa bộ lọc
      </button>
    </div>
  );
};

export default JobTypeFilter;