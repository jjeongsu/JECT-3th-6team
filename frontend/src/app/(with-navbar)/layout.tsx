import { Navbar } from '@/widgets';

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className={'w-full'}>
      {children}
      <Navbar />
    </div>
  );
}
