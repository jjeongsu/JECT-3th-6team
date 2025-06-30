import React from 'react';
import { TagProps } from './types';

export function Tag({ children, className = '',...props }: TagProps) {
  return (
    <span
      className={`inline-block mr-1 text-[var(--color-text-gray-color)] font-[var(--font-pretendard)] text-base font-normal leading-[12.584px] ${className}`}
      {...props}
    >
      #{children}
    </span>
  );
}


