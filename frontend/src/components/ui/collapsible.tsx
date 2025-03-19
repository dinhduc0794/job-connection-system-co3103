import React, { createContext, useContext, useState, ReactNode } from 'react';

interface CollapsibleContextType {
  isOpen: boolean;
  setIsOpen: (open: boolean) => void;
  onOpenChange?: (open: boolean) => void;
}

const CollapsibleContext = createContext<CollapsibleContextType | undefined>(undefined);

interface CollapsibleProps {
  children: ReactNode;
  open?: boolean;
  onOpenChange?: (open: boolean) => void;
}

interface CollapsibleComponentProps {
  children: ReactNode;
  className?: string;
}

export const Collapsible: React.FC<CollapsibleProps> = ({ children, open = false, onOpenChange }) => {
  const [isOpen, setIsOpen] = useState(open);

  return (
    <CollapsibleContext.Provider value={{ isOpen, setIsOpen, onOpenChange }}>
      <div>
        {children}
      </div>
    </CollapsibleContext.Provider>
  );
};

export const CollapsibleTrigger: React.FC<CollapsibleComponentProps> = ({ children, className = '' }) => {
  const context = useContext(CollapsibleContext);
  if (!context) throw new Error('CollapsibleTrigger must be used within Collapsible');

  const { isOpen, setIsOpen, onOpenChange } = context;

  const handleClick = () => {
    setIsOpen(!isOpen);
    onOpenChange?.(!isOpen);
  };

  return (
    <div onClick={handleClick} className={`cursor-pointer ${className}`}>
      {children}
    </div>
  );
};

export const CollapsibleContent: React.FC<CollapsibleComponentProps> = ({ children, className = '' }) => {
  const context = useContext(CollapsibleContext);
  if (!context) throw new Error('CollapsibleContent must be used within Collapsible');

  const { isOpen } = context;

  if (!isOpen) return null;

  return (
    <div className={`overflow-hidden transition-all ${className}`}>
      {children}
    </div>
  );
};