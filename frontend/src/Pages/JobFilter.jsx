import React, { useState, useEffect } from 'react';
import { Badge } from '../components/badge';
import { Button } from '../components/Button';
import { Card } from '../components/card';
import { Checkbox } from '../components/checkbox';
import { Slider } from '../components/Slider';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "../components/ui/dropdown-menu";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogClose,
} from "../components/ui/dialog";
import { Filter, ChevronDown, X, Plus } from 'lucide-react';

const SCHEDULE_TYPES = {
  FULLTIME: 'Full-time',
  PARTTIME: 'Part-time',
  INTERNSHIP: 'Internship',
  FREELANCE: 'Freelance',
  CONTRACT: 'Contract'
};

const EXPERIENCE_LEVELS = {
  INTERN: 'Intern',
  FRESHER: 'Fresher',
  JUNIOR: 'Junior',
  SENIOR: 'Senior',
  LEAD: 'Lead',
  MANAGER: 'Manager',
  DIRECTOR: 'Director'
};

const COMMON_SKILLS = {
  'Frontend': ['JavaScript', 'React', 'Angular', 'Vue.js', 'HTML', 'CSS'],
  'Backend': ['Node.js', 'Python', 'Java', 'PHP', 'Ruby'],
  'Database': ['SQL', 'MongoDB', 'PostgreSQL', 'MySQL'],
  'DevOps': ['AWS', 'Docker', 'Kubernetes', 'Jenkins'],
  'Other': ['Git', 'Agile', 'Scrum', 'REST API']
};

const FilterDropdown = ({ title, options, selected, onSelect, badge }) => {
  const [isOpen, setIsOpen] = useState(false);
  return (
    <DropdownMenu open={isOpen} onOpenChange={setIsOpen}>
      <DropdownMenuTrigger asChild>
        <Button 
          variant="outline" 
          className="w-full justify-between hover:border-blue-500 hover:shadow-md transition-all duration-300 group"
        >
          <span className="group-hover:text-blue-600">{title} {badge > 0 && 
            <span className="ml-1 bg-blue-100 text-blue-600 px-2 py-0.5 rounded-full text-sm">
              {badge}
            </span>}
          </span>
          <ChevronDown className="h-4 w-4 ml-2 group-hover:text-blue-500" />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-56 p-2 animate-in slide-in-from-top-2 duration-200">
        <div className="grid gap-2">
          {Object.entries(options).map(([key, label]) => (
            <div key={key} 
              className="flex items-center space-x-2 p-2 hover:bg-blue-50 rounded-md transition-colors duration-200">
              <Checkbox
                id={`option-${key}`}
                checked={selected.includes(key)}
                onCheckedChange={() => onSelect(key)}
                className="border-2 hover:border-blue-500"
              />
              <label 
                htmlFor={`option-${key}`} 
                className="text-sm cursor-pointer hover:text-blue-600 transition-colors duration-200">
                {label}
              </label>
            </div>
          ))}
        </div>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};



const JobFilter = ({ jobs, onFilterUpdate, currentResults }) => {
  const [filters, setFilters] = useState({
    schedule: [],
    level: [],
    minSalary: 0,
    maxSalary: 100,
    yoe: '',
    skills: [],
    isAbove100M: false
  });
   
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [customSkill, setCustomSkill] = useState('');
  const [isSkillsDialogOpen, setIsSkillsDialogOpen] = useState(false);

  const handleScheduleChange = (schedule) => {
    setFilters(prev => ({
      ...prev,
      schedule: prev.schedule.includes(schedule)
        ? prev.schedule.filter(s => s !== schedule)
        : [...prev.schedule, schedule]
    }));
  };
  const handleAbove100MChange = (checked) => {
    setFilters(prev => ({
      ...prev,
      isAbove100M: checked,
      minSalary: checked ? 100 : prev.minSalary,
      maxSalary: checked ? 999 : prev.maxSalary
    }));
  };
  const handleLevelChange = (level) => {
    setFilters(prev => ({
      ...prev,
      level: prev.level.includes(level)
        ? prev.level.filter(l => l !== level)
        : [...prev.level, level]
    }));
  };

  const handleSkillChange = (skill) => {
    setSelectedSkills(prev => {
      const newSkills = prev.includes(skill)
        ? prev.filter(s => s !== skill)
        : [...prev, skill];
      
      // Cập nhật luôn filters.skills
      setFilters(currentFilters => ({
        ...currentFilters,
        skills: newSkills
      }));
      
      return newSkills;
    });
  };
  const isSkillMatching = (jobSkill, selectedSkill) => {
    const normalizedJobSkill = jobSkill.toLowerCase().replace(/[-_\s.]/g, '');
    const normalizedSelectedSkill = selectedSkill.toLowerCase().replace(/[-_\s.]/g, '');
    return normalizedJobSkill.includes(normalizedSelectedSkill) || 
           normalizedSelectedSkill.includes(normalizedJobSkill);
  };
  const addCustomSkill = () => {
    // Chuẩn hóa skill trước khi thêm
    const normalizedSkill = customSkill.trim();
    
    // Kiểm tra các điều kiện
    if (!normalizedSkill) return; // Không thêm skill rỗng
    if (selectedSkills.includes(normalizedSkill)) return; // Không thêm skill trùng
    
    // Thêm skill mới vào state
    setSelectedSkills(prev => {
      const newSkills = [...prev, normalizedSkill];
      
      // Cập nhật luôn vào filters
      setFilters(currentFilters => ({
        ...currentFilters,
        skills: newSkills
      }));
      
      return newSkills;
    });
    
    // Reset input
    setCustomSkill('');
  };
  const removeSkill = (skill) => {
    setSelectedSkills(prev => {
      const newSkills = prev.filter(s => s !== skill);
      
      // Cập nhật luôn filters.skills
      setFilters(currentFilters => ({
        ...currentFilters,
        skills: newSkills
      }));
      
      return newSkills;
    });
  };
  const handleCloseDialog = () => {
    setIsSkillsDialogOpen(false);
  };
  
  
  const handleYoeChange = (e) => {
    const value = e.target.value.replace(/\D/g, '');
    setFilters(prev => ({ ...prev, yoe: value }));
  };

  const resetFilters = () => {
    setFilters({
      schedule: [],
      level: [],
      minSalary: 0,
      maxSalary: 100,
      yoe: '',
      skills: [],
      isAbove100M: false
    });
    setSelectedSkills([]);
    setCustomSkill('');
  };

  // useEffect(() => {
  //   const filteredJobs = jobs.filter(job => {
  //     const matchesSchedule = filters.schedule.length === 0 || 
  //       filters.schedule.includes(job.schedule);
      
  //     const matchesLevel = filters.level.length === 0 || 
  //       filters.level.includes(job.level);
      
  //     const matchesSalary = (!job.minSalary || job.minSalary >= filters.minSalary) &&
  //       (!job.maxSalary || job.maxSalary <= filters.maxSalary);
      
  //     const matchesYoe = !filters.yoe || 
  //       (job.yoe && parseInt(job.yoe) <= parseInt(filters.yoe));
      
  //     const matchesSkills = selectedSkills.length === 0 || 
  //       selectedSkills.every(skill => 
  //         job.skills?.some(jobSkill => 
  //           jobSkill.toLowerCase().includes(skill.toLowerCase())
  //         )
  //       );

  //     return matchesSchedule && matchesLevel && matchesSalary && 
  //            matchesYoe && matchesSkills;
  //   });

  //   onFilterUpdate(filteredJobs);
  // }, [filters, selectedSkills, jobs, onFilterUpdate]);
  useEffect(() => {
    const filteredJobs = jobs.filter(job => {
      const matchesSchedule = filters.schedule.length === 0 || 
        filters.schedule.includes(job.schedule);
      
      const matchesLevel = filters.level.length === 0 || 
        filters.level.includes(job.level);
      
      const matchesSalary = (!job.minSalary || job.minSalary >= filters.minSalary) &&
        (!job.maxSalary || job.maxSalary <= filters.maxSalary);
      
      const matchesYoe = !filters.yoe || 
        (job.yoe && parseInt(job.yoe) <= parseInt(filters.yoe));
      
      // Xử lý skills
      let jobSkillsArray = [];
      if (Array.isArray(job.skills)) {
        jobSkillsArray = job.skills.map(skill => skill.toLowerCase());
      } else if (typeof job.skills === 'string') {
        jobSkillsArray = job.skills.split(',').map(skill => skill.trim().toLowerCase());
      }
  
      const matchesSkills = selectedSkills.length === 0 || 
      selectedSkills.some(selectedSkill => 
        jobSkillsArray.some(jobSkill => isSkillMatching(jobSkill, selectedSkill))
      );

  
      return matchesSchedule && matchesLevel && matchesSalary && 
             matchesYoe && matchesSkills;
    });
  
    onFilterUpdate(filteredJobs);
  }, [filters, selectedSkills, jobs, onFilterUpdate]);
  return (
    <Card className="max-w-[2000px] mx-auto bg-white p-6 shadow-lg hover:shadow-xl transition-shadow duration-300 border-2 border-gray-100">
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center gap-2">
          <Filter className="w-6 h-6 text-blue-600 animate-pulse" />
          <h2 className="text-xl font-semibold bg-gradient-to-r from-blue-600 to-blue-400 bg-clip-text text-transparent">
            Filters
          </h2>
        </div>
        <div className="flex items-center gap-3">
          <span className="text-sm bg-blue-50 text-blue-600 px-3 py-1 rounded-full font-medium">
            {currentResults?.length || 0} results
          </span>
          <Button 
            variant="outline" 
            size="sm"
            onClick={resetFilters}
            className="text-red-500 hover:text-white hover:bg-red-500 border-red-500 hover:border-red-600 transition-all duration-300 hover:scale-105"
          >
            Clear all
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-5 gap-5">
        <FilterDropdown
          title="Schedule"
          options={SCHEDULE_TYPES}
          selected={filters.schedule}
          onSelect={handleScheduleChange}
          badge={filters.schedule.length}
        />

        <FilterDropdown
          title="Experience Level"
          options={EXPERIENCE_LEVELS}
          selected={filters.level}
          onSelect={handleLevelChange}
          badge={filters.level.length}
        />


        

<Dialog 
          open={isSkillsDialogOpen} 
          onOpenChange={setIsSkillsDialogOpen}
        >
          <DialogTrigger asChild>
            <Button 
              variant="outline" 
              className="w-full justify-between hover:border-blue-500 hover:shadow-md transition-all duration-300 group"
            >
              <span className="group-hover:text-blue-600">
                Skills {selectedSkills.length > 0 && 
                  <span className="ml-1 bg-blue-100 text-blue-600 px-2 py-0.5 rounded-full text-sm">
                    {selectedSkills.length}
                  </span>
                }
              </span>
              <ChevronDown className="h-4 w-4 group-hover:text-blue-500" />
            </Button>
          </DialogTrigger>

          <DialogContent 
            className="max-w-4xl max-h-[90vh] overflow-y-auto p-6 animate-in zoom-in-90 duration-300"
          >
            <DialogHeader className="relative mb-4">
              <DialogTitle className="text-2xl font-bold text-center pt-2 bg-gradient-to-r from-blue-600 to-blue-400 bg-clip-text text-transparent">
                Select Skills
              </DialogTitle>
              
              {/* Nút đóng đã được sửa */}
              <DialogClose asChild>
                <Button
                  variant="ghost"
                  className="absolute right-0 top-0 p-2 hover:bg-red-50 rounded-full transition-all duration-300 group"
                  onClick={handleCloseDialog}
                >
                  <X className="h-5 w-5 text-gray-500 group-hover:text-red-500 group-hover:rotate-90 transition-all duration-300" />
                </Button>
              </DialogClose>
            </DialogHeader>

            <div className="space-y-6">
              {/* Selected Skills Area */}
              <div className="relative">
                <div className="flex flex-wrap gap-2 min-h-[50px] p-3 border-2 border-dashed border-gray-200 rounded-lg hover:border-blue-400 transition-colors duration-300 bg-white/50 backdrop-blur-sm">
                  {selectedSkills.length === 0 && (
                    <div className="absolute inset-0 flex items-center justify-center text-gray-400">
                      Select skills or type to add new ones
                    </div>
                  )}
                  {selectedSkills.map((skill) => (
                    <Badge
                      key={skill}
                      variant="secondary"
                      className="flex items-center gap-2 p-2 bg-blue-50 hover:bg-blue-100 transition-all duration-200 hover:scale-105"
                    >
                      {skill}
                      <button
                        onClick={() => removeSkill(skill)}
                        className="ml-1 hover:bg-red-100 rounded-full p-0.5 transition-all duration-200"
                      >
                        <X className="w-3 h-3 text-gray-500 hover:text-red-500 hover:rotate-90 transition-all duration-300" />
                      </button>
                    </Badge>
                  ))}
                </div>
              </div>

              {/* Add New Skill Input */}
              <div className="flex gap-2">
                <input
                  type="text"
                  value={customSkill}
                  onChange={(e) => setCustomSkill(e.target.value)}
                  placeholder="Enter new skill..."
                  className="flex-1 p-3 border-2 rounded-md text-base focus:border-blue-400 focus:ring-blue-400 transition-all duration-300"
                  onKeyDown={(e) => e.key === 'Enter' && addCustomSkill()}
                />
                <Button 
                  onClick={addCustomSkill} 
                  className="px-6 bg-blue-500 hover:bg-blue-600 transition-all duration-300 group hover:scale-105"
                  disabled={!customSkill.trim()}
                >
                  <Plus className="w-5 h-5 mr-2 group-hover:rotate-180 transition-all duration-300" />
                  Add
                </Button>
              </div>

              {/* Common Skills Section */}
              <div className="grid gap-6 mt-6">
                {Object.entries(COMMON_SKILLS).map(([category, skills]) => (
                  <div key={category} className="space-y-3">
                    <h3 className="font-semibold text-lg text-gray-700">
                      {category}
                    </h3>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
                      {skills.map((skill) => (
                        <div 
                          key={skill} 
                          className="flex items-center space-x-2 p-2 hover:bg-blue-50 rounded-md transition-all duration-200"
                        >
                          <Checkbox
                            id={`skill-${skill}`}
                            checked={selectedSkills.includes(skill)}
                            onCheckedChange={() => handleSkillChange(skill)}
                            className="border-2 hover:border-blue-500 data-[state=checked]:bg-blue-500"
                          />
                          <label
                            htmlFor={`skill-${skill}`}
                            className="text-sm cursor-pointer hover:text-blue-600 transition-colors duration-200 select-none"
                          >
                            {skill}
                          </label>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </DialogContent>
        </Dialog>
        <div className="space-y-2">
          <h3 className="text-sm font-medium mb-2">Years of Experience</h3>
          <input
            type="text"
            value={filters.yoe}
            onChange={handleYoeChange}
            placeholder="Enter years of experience"
            className="w-full p-2 border rounded-md"
            maxLength="2"
          />
          <p className="text-xs text-gray-500">Enter a number between 0-20</p>
        </div>
        <div>
<h3 className="text-lg font-semibold mb-3 flex items-center gap-2">
    <span className="text-orange-500 text-xl">💰</span>
    <span>Mức lương (Triệu VNĐ)</span>
</h3>
<div className="mb-4">
        <div className="flex items-center space-x-2">
          <Checkbox
            id="above-100m"
            checked={filters.isAbove100M}
            onCheckedChange={handleAbove100MChange}
          />
          <label htmlFor="above-100m" className="text-sm font-medium">
            Trên 100 triệu
          </label>
        </div>
      </div>
      {!filters.isAbove100M && (
<div>
  
  <Slider
    min={0} 
    max={100}
    step={5}
    value={[filters.minSalary, filters.maxSalary]}
    onValueChange={([min, max]) =>
      setFilters(prev => ({ ...prev, minSalary: min, maxSalary: max }))
    }
    className="[&_.sliderTrack]:h-3 [&_.sliderTrack]:bg-orange-200 [&_.sliderRange]:h-3 [&_.sliderRange]:bg-blue-500 [&_.sliderThumb]:h-6 [&_.sliderThumb]:w-6 [&_.sliderThumb]:bg-white [&_.sliderThumb]:border-3 [&_.sliderThumb]:border-blue-500 [&_.sliderThumb]:hover:border-blue-600 [&_.sliderThumb]:shadow-xl [&_.sliderTrack]:rounded-full [&_.sliderRange]:rounded-full [&_.sliderThumb]:hover:scale-125 [&_.sliderThumb]:transition-all [&_.sliderThumb]:duration-300"
  />

  <div className="flex justify-between mt-4">
    <div className="text-center">
      <div className="bg-orange-200 rounded-xl px-6 py-3 shadow-md">
        <p className="text-sm text-orange-700 font-semibold">Tối thiểu</p>
          <p className="text-lg font-bold text-orange-500">{filters.minSalary}M</p>
        </div>
      </div>
      
      <div className="text-center">
        <div className="bg-orange-100 rounded-lg px-4 py-2">
          <p className="text-xs text-orange-600 font-medium">Max</p>
          <p className="text-lg font-bold text-orange-500">{filters.maxSalary}M</p>
        </div>
      </div>
    </div>
  </div>
   )}
</div>

      </div>
    </Card>
  );
};

export default JobFilter;