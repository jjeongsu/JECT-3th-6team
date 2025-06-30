import React from "react"
import { Chip } from "./Chip"

const StarIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
  </svg>
)

const HeartIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
    <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
  </svg>
)

const XIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
    <path d="M18 6L6 18M6 6l12 12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
)

const CheckIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
    <path d="M20 6L9 17l-5-5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
)

export function ChipExample() {
  return (
    <div className="space-y-4 p-4">
      <h2 className="text-lg font-semibold">Chip 컴포넌트 예시</h2>  
      <div className="space-y-2">
        <h3 className="text-sm font-medium">1. 기본 Variants</h3>
        <div className="flex gap-2">
          <Chip variant="white">white</Chip>
          <Chip variant="mainLight">mainLight</Chip>
          <Chip variant="main">main</Chip>
          <Chip variant="disabled">disabled</Chip>
        </div>
      </div>

      <div className="space-y-2">
        <h3 className="text-sm font-medium">3. 아이콘이 우측에 있는 경우</h3>
        <div className="flex gap-2">
          <Chip variant="mainLight" icon={<XIcon />} iconPosition="right">삭제</Chip>
        </div>
      </div>
    </div>
  )
}
