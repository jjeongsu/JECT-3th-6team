export function MediumText({ children }: { children: React.ReactNode }) {
  return (
    <p className="text-[28px] font-medium text-[var(--color-text-color)] font-pretendard leading-normal">
      {children}
    </p>
  );
}   