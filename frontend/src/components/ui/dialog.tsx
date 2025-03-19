import React, { createContext, useContext, useState, ReactNode } from 'react';

interface DialogContextType {
  isOpen: boolean;
  setIsOpen: (open: boolean) => void;
  onOpenChange?: (open: boolean) => void;
}

const DialogContext = createContext<DialogContextType | undefined>(undefined);

interface DialogProps {
  children: ReactNode;
  open?: boolean;
  onOpenChange?: (open: boolean) => void;
}

interface DialogComponentProps {
  children: ReactNode;
  className?: string;
}

export const Dialog: React.FC<DialogProps> = ({ children, open = false, onOpenChange }) => {
  const [isOpen, setIsOpen] = useState(open);
  
  return (
    <DialogContext.Provider value={{ isOpen, setIsOpen, onOpenChange }}>
      {children}
    </DialogContext.Provider>
  );
};

export const DialogTrigger: React.FC<DialogComponentProps> = ({ children, className = '' }) => {
  const context = useContext(DialogContext);
  if (!context) throw new Error('DialogTrigger must be used within Dialog');
  
  const { setIsOpen, onOpenChange } = context;
  
  const handleClick = () => {
    setIsOpen(true);
    onOpenChange?.(true);
  };

  return (
    <div onClick={handleClick} className={className}>
      {children}
    </div>
  );
};

export const DialogContent: React.FC<DialogComponentProps> = ({ children, className = '' }) => {
  const context = useContext(DialogContext);
  if (!context) throw new Error('DialogContent must be used within Dialog');

  const { isOpen, setIsOpen, onOpenChange } = context;

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div 
        className="fixed inset-0 bg-black/50" 
        onClick={() => {
          setIsOpen(false);
          onOpenChange?.(false);
        }} 
      />
      <div className={`bg-white rounded-lg p-4 relative z-50 ${className}`}>
        {children}
      </div>
    </div>
  );
};
export const DialogClose: React.FC<DialogComponentProps> = ({ children, className = '' }) => {
    const context = useContext(DialogContext);
    if (!context) throw new Error('DialogClose must be used within Dialog');
    
    const { setIsOpen, onOpenChange } = context;
    
    const handleClick = () => {
      setIsOpen(false);
      onOpenChange?.(false);
    };
  
    return (
      <div onClick={handleClick} className={className}>
        {children}
      </div>
    );
  };
  
export const DialogHeader: React.FC<DialogComponentProps> = ({ children, className = '' }) => (
  <div className={`mb-4 ${className}`}>
    {children}
  </div>
);

export const DialogTitle: React.FC<DialogComponentProps> = ({ children, className = '' }) => (
  <h2 className={`text-lg font-semibold ${className}`}>
    {children}
  </h2>
);