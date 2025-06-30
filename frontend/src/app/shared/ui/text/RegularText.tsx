import { TextProps } from './types';

export function RegularText({ 
  children, 
  className = '', 
  onClick,
  ...props 
}: TextProps) {
  return (
    <p 
      className={`text-[14px] font-normal text-[var(--color-text-color)] font-pretendard leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
}   
