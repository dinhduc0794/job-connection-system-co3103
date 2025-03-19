import { useState, useEffect } from 'react';
import { ChevronDownIcon, ChevronUpIcon } from '@heroicons/react/24/outline';
import TokenManager from '../utils/tokenManager';

const SkillsFilter = ({ onSelect }) => {
  const [skills, setSkills] = useState([]);
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const token = TokenManager.getToken();
  useEffect(() => {
    const fetchSkills = async () => {
      setLoading(true);
      try {
        const headers = {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Origin': '*'
        };

        // Chỉ thêm Authorization header nếu có token
        if (token?.value) {
          headers['Authorization'] = `Bearer ${token.value}`;
        }

        const response = await fetch('/public/skills', {
          method: 'GET',
          headers,
          credentials: 'include'
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        setSkills(Array.isArray(data) ? data : []);
      } catch (error) {
        console.error('Error fetching skills:', error);
        setSkills([]);
      }
      setLoading(false);
    };

    fetchSkills();
  }, []);
  const filteredSkills = skills.filter(skill => 
    skill.name.toLowerCase().includes(searchTerm.toLowerCase())
  );
  const handleSkillToggle = (skillName) => {
    const updatedSkills = selectedSkills.includes(skillName)
      ? selectedSkills.filter(skill => skill !== skillName)
      : [...selectedSkills, skillName];
    
    setSelectedSkills(updatedSkills);
    onSelect(updatedSkills);
  };

  if (loading) {
    return <div className="animate-pulse">Đang tải kỹ năng...</div>;
  }

  return (
    <div className="space-y-4 transition-all duration-300 ease-in-out">
      {/* Header với hiệu ứng hover */}
      <div 
        onClick={() => setIsExpanded(!isExpanded)}
        className="flex justify-between items-center p-3 rounded-lg cursor-pointer
          bg-gradient-to-r from-blue-50 to-blue-100/50 hover:from-blue-100 hover:to-blue-200/50
          transition-all duration-300 ease-in-out shadow-sm hover:shadow-md"
      >
        <h3 className="font-semibold text-lg text-blue-900">Kỹ năng</h3>
        <div className="flex items-center space-x-2">
          {selectedSkills.length > 0 && (
            <span className="text-sm bg-blue-500 text-white px-2 py-0.5 rounded-full">
              {selectedSkills.length}
            </span>
          )}
          {isExpanded ? (
            <ChevronUpIcon className="h-5 w-5 text-blue-500" />
          ) : (
            <ChevronDownIcon className="h-5 w-5 text-blue-500" />
          )}
        </div>
      </div>

      {/* Content với animation */}
      <div className={`overflow-hidden transition-all duration-300 ease-in-out
        ${isExpanded ? 'max-h-[500px] opacity-100' : 'max-h-0 opacity-0'}`}>
        
        {/* Search box */}
        {isExpanded && (
          <div className="mb-4">
            <input
              type="text"
              placeholder="Tìm kiếm kỹ năng..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full p-2 border border-gray-200 rounded-lg focus:ring-2 
                focus:ring-blue-500 focus:border-transparent transition-all duration-200"
            />
          </div>
        )}

        {/* Skills list với scrollbar tùy chỉnh */}
        <div className="space-y-2 max-h-60 overflow-y-auto custom-scrollbar pr-2">
          {loading ? (
            <div className="animate-pulse space-y-2">
              {[1,2,3].map(i => (
                <div key={i} className="h-6 bg-gray-200 rounded"></div>
              ))}
            </div>
          ) : (
            filteredSkills.map((skill) => (
              <div 
                key={skill.id} 
                className="flex items-center p-2 rounded-lg hover:bg-gray-50
                  transition-colors duration-200"
              >
                <input
                  type="checkbox"
                  id={`skill-${skill.id}`}
                  checked={selectedSkills.includes(skill.name)}
                  onChange={() => handleSkillToggle(skill.name)}
                  className="w-4 h-4 text-blue-600 rounded border-gray-300 
                    focus:ring-blue-500 transition-colors duration-200"
                />
                <label
                  htmlFor={`skill-${skill.id}`}
                  className="ml-2 text-sm font-medium text-gray-700 cursor-pointer 
                    hover:text-blue-600 transition-colors duration-200"
                >
                  {skill.name}
                </label>
              </div>
            ))
          )}
        </div>

        {/* Selected skills với animation */}
        {selectedSkills.length > 0 && (
          <div className="mt-4 animate-fadeIn">
            <div className="text-sm font-medium text-gray-700 mb-2">
              Kỹ năng đã chọn:
            </div>
            <div className="flex flex-wrap gap-2">
              {selectedSkills.map((skill) => (
                <span
                  key={skill}
                  className="inline-flex items-center px-3 py-1 rounded-full
                    text-sm font-medium bg-blue-100 text-blue-800
                    hover:bg-blue-200 transition-colors duration-200"
                >
                  {skill}
                  <button
                    type="button"
                    onClick={(e) => {
                      e.stopPropagation();
                      handleSkillToggle(skill);
                    }}
                    className="ml-2 hover:text-red-500 transition-colors duration-200"
                  >
                    ×
                  </button>
                </span>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

// Thêm custom scrollbar styles vào global CSS
const styles = `
  .custom-scrollbar::-webkit-scrollbar {
    width: 4px;
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

  @keyframes fadeIn {
    from { opacity: 0; transform: translateY(-10px); }
    to { opacity: 1; transform: translateY(0); }
  }
  .animate-fadeIn {
    animation: fadeIn 0.3s ease-in-out;
  }
`;

export default SkillsFilter;