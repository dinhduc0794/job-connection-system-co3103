import React, { createContext, useContext, useRef, useEffect, useState } from 'react';

const DropdownContext = createContext(null);

export const DropdownMenu = ({ children, open, onOpenChange }) => {
  return (
    <DropdownContext.Provider value={{ open, onOpenChange }}>
      <div className="relative inline-block w-full">{children}</div>
    </DropdownContext.Provider>
  );
};

export const DropdownMenuTrigger = ({ children, asChild }) => {
  const { open, onOpenChange } = useContext(DropdownContext);
  const Component = asChild ? 'div' : 'button';

  return (
    <Component
      onClick={() => onOpenChange(!open)}
      className="w-full"
      aria-expanded={open}
    >
      {children}
    </Component>
  );
};

export const DropdownMenuContent = ({ children, className = '' }) => {
  const { open, onOpenChange } = useContext(DropdownContext);
  const ref = useRef(null);
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
    return () => setMounted(false);
  }, []);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (ref.current && !ref.current.contains(event.target)) {
        onOpenChange(false);
      }
    };

    if (open) {
      document.addEventListener('mousedown', handleClickOutside);
      return () => document.removeEventListener('mousedown', handleClickOutside);
    }
  }, [open, onOpenChange]);

  if (!mounted) return null;

  return (
    <div
      ref={ref}
      className={`
        absolute z-50 w-full bg-white border rounded-md shadow-lg 
        transition-all duration-200 ease-in-out origin-top
        ${open 
          ? 'opacity-100 scale-100 translate-y-2' 
          : 'opacity-0 scale-95 -translate-y-2 pointer-events-none'
        }
        ${className}
      `}
      aria-hidden={!open}
    >
      {children}
    </div>
  );
};

export const DropdownMenuItem = ({ children, onSelect }) => {
  const { onOpenChange } = useContext(DropdownContext);

  const handleClick = () => {
    if (onSelect) {
      onSelect();
      onOpenChange(false);
    }
  };

  return (
    <button
      className="w-full px-4 py-2 text-left hover:bg-gray-100 focus:outline-none focus:bg-gray-100 transition-colors duration-150"
      onClick={handleClick}
    >
      {children}
    </button>
  );
};