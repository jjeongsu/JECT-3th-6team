import { TextProps } from './types';

export function SemiBoldText({ 
  children, 
  className = '', 
  onClick,
  ...props 
}: TextProps) {
  return (
    <p 
      className={`text-[28px] font-semibold text-[var(--color-text-color)] font-pretendard leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
} 