import React from 'react';

export type colorType = 'primary' | 'secondary' | 'white';
export type sizeType = 'fit' | 'full';

export interface ButtonProps {
  children: React.ReactNode;
  onClick: () => void;
  disabled: boolean;
  color?: colorType;
  size?: sizeType;
  customClass?: string;
  hasShadow?: boolean;
}
