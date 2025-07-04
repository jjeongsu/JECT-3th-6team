import { TextProps } from './types';

export function MediumText({ 
  children, 
  className = '', 
  onClick,
  ...props 
}: TextProps) {
  return (
    <p 
      className={`text-[16px] font-medium text-text-color leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
}   