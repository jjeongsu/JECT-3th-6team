import { TextProps } from './types';

export function BoldText({ 
  children, 
  className = '', 
  onClick,
  ...props 
}: TextProps) {
  return (
    <p 
      className={`text-[28px] font-bold text-[var(--color-text-color)] font-pretendard leading-normal ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </p>
  );
}
