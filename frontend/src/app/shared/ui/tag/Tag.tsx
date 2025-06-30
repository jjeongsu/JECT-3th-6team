import React from 'react';
import { TagProps } from './types';

export function Tag({ children, className = '',...props }: TagProps) {
  return (
    <span
      className={`inline-block mr-1 ${className}`}
      style={{
        color: 'var(--color-text-gray-color)',
        fontFamily: 'var(--font-pretendard), Pretendard, sans-serif',
        fontSize: '16px',
        fontStyle: 'normal',
        fontWeight: 400,
        lineHeight: '12.584px',
      }}
      {...props}
    >
      #{children}
    </span>
  );
}


