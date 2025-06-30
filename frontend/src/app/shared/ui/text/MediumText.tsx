import { TextProps } from './types';

export function MediumText({ 
  children, 
  className = '', 
  onClick,
  ...props 
}: TextProps) {
  return (
    <p 
      className={`text-[28px] font-medium text-[var(--color-text-color)] font-pretendard leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
}   