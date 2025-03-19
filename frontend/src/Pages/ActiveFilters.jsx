const ActiveFilters = ({ filters, onRemove }) => {
    return (
      <div className="flex flex-wrap gap-2 mb-4">
        {Object.entries(filters).map(([key, value]) => (
          <div
            key={key}
            className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full flex items-center"
          >
            <span>{key}: {value}</span>
            <button
              onClick={() => onRemove(key)}
              className="ml-2 text-blue-800 hover:text-blue-900"
            >
              ×
            </button>
          </div>
        ))}
      </div>
    );
  };
  
  export default ActiveFilters;