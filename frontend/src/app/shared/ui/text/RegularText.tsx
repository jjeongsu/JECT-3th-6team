export function RegularText({ children }: { children: React.ReactNode }) {
  return (
    <p className="text-[28px] font-normal text-[var(--color-text-color)] font-pretendard leading-normal">
      {children}
    </p>
  );
}   
