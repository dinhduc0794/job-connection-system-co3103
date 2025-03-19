export const handleApiError = (error, defaultMessage) => {
    const errorMessage = error.response?.data?.message || error.message || defaultMessage;
    return {
      success: false,
      error: errorMessage
    };
  };
  
  // utils/axiosInterceptor.js
  