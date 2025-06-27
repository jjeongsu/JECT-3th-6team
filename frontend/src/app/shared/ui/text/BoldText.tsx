export function BoldText({ children }: { children: React.ReactNode }) {
  return (
    <p className="text-[28px] font-bold text-[var(--color-text-color)] font-pretendard leading-normal">
      {children}
    </p>
  );
}
