import OnsiteReservationForm from '@/features/reservation/ui/OnsiteReservationForm';
import PageHeader from '@/shared/ui/header/PageHeader';

export default function ReservationOnsitePopupPage() {
  return (
    <div>
      <PageHeader title={'현장 대기'} />
      <OnsiteReservationForm />
    </div>
  );
}
