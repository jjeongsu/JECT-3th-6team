export function SemiBoldText({ children }: { children: React.ReactNode }) {
  return (
    <p className="text-[28px] font-semibold text-[var(--color-text-color)] font-pretendard leading-normal">
      {children}
    </p>
  );
} 