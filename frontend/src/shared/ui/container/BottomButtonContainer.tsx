export default function BottomButtonContainer({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div
      className={
        'w-full h-[122px] fixed bottom-0 px-5 pt-4  rounded-t-[20px] shadow-container z-3'
      }
    >
      <div className={'w-full flex justify-center gap-x-2'}>{children}</div>
    </div>
  );
}
