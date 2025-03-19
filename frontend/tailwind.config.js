/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
    "./public/index.html"
  ],
  theme: {
    extend: {
      colors: {
        indigo: {
          500: '#6366fa',
          600: '#4F46E5',
        }
      }
    },
  },
  plugins: [
    require('tailwindcss-motion')
  ],
}