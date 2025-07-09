import PageHeader from '@/shared/ui/header/PageHeader';
import { ReservationSummaryView } from '@/features/reservation';

export default async function ReservationCompletePage({
  params,
}: {
  params: Promise<{ popupId: number }>;
}) {
  const { popupId } = await params;
  return (
    <div>
      <PageHeader title={'웨이팅 확정'} />
      <ReservationSummaryView />
    </div>
  );
}
