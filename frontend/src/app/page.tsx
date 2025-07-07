import {
  PopupListItemType,
  VisitedPopupListItemType,
} from '@/entities/popup/types/PopupListItem';
import BadgedPopupCard from '@/entities/popup/ui/BadgedPopupCard';
import { Navbar } from '@/widgets';
import BellIcon from '@/assets/icons/Normal/Icon_Bell.svg';
import IconMap from '@/assets/icons/Normal/Icon_map.svg';
import IconCalendar from '@/assets/icons/Normal/Icon_Calendar.svg';
import { SelectButton } from '@/shared/ui';

const popupList: (PopupListItemType | VisitedPopupListItemType)[] = [
  {
    id: 1,
    name: '팝업스토어 서울역점',
    location: {
      address_name: '서울특별시 용산구 청파로 378',
      region_1depth_name: '서울특별시',
      region_2depth_name: '용산구',
      region_3depth_name: '청파동',
      x: 126.9707,
      y: 37.5546,
    },
    rating: {
      averageStar: 4.5,
      reviewCount: 120,
    },
    period: {
      startDate: '2025-07-01',
      endDate: '2025-07-10',
    },
    dDay: 5,
    imageUrl: 'https://picsum.photos/200',
  },
  {
    id: 2,
    name: '성수 핫플 팝업',
    location: {
      address_name: '서울특별시 성동구 연무장길 31',
      region_1depth_name: '서울특별시',
      region_2depth_name: '성동구',
      region_3depth_name: '성수동',
      x: 127.056,
      y: 37.5446,
    },
    rating: {
      averageStar: 4.8,
      reviewCount: 98,
    },
    period: {
      startDate: '2025-07-03',
      endDate: '2025-07-12',
    },
    dDay: 7,
    imageUrl: 'https://picsum.photos/200',
  },
  {
    waitingId: 101,
    popupId: 1,
    popupName: '레트로 감성 팝업',
    popupImageUrl: 'https://picsum.photos/200',
    location: '서울특별시 마포구 서교동 123-45',
    rating: {
      averageStar: 4.7,
      reviewCount: 85,
    },
    period: '2025년 6월 10일 ~ 6월 20일',
    waitingNumber: 12,
    status: 'COMPLETED',
  },
  {
    waitingId: 102,
    popupId: 2,
    popupName: '여름 한정 팝업스토어',
    popupImageUrl: 'https://picsum.photos/200',
    location: '서울특별시 강남구 논현로 508',
    rating: {
      averageStar: 4.5,
      reviewCount: 110,
    },
    period: '2025년 7월 1일 ~ 7월 7일',
    waitingNumber: 8,
    status: 'RESERVED',
  },
  {
    waitingId: 103,
    popupId: 3,
    popupName: '제로 웨이스트 팝업',
    popupImageUrl: 'https://picsum.photos/200',
    location: '경기도 성남시 분당구 정자동 77-1',
    rating: {
      averageStar: 4.3,
      reviewCount: 65,
    },
    period: '2025년 6월 25일 ~ 7월 3일',
    waitingNumber: 5,
    status: 'COMPLETED',
  },
];

export default function Home() {
  return (
    <div className={'w-full bg-main pt-7'}>
      {/*필터링 컨테이너*/}
      <div className={'flex flex-col px-5 gap-y-4 mb-4'}>
        <div className={'flex items-center justify-between'}>
          <span className={'font-gangwon text-[17px] text-white font-regular'}>
            Spot it
          </span>
          <BellIcon fill={'var(--color-main)'} width={28} height={28} />
        </div>
        <div className={'flex items-center gap-x-3'}>
          <SelectButton
            Icon={
              <IconCalendar
                width={22}
                height={22}
                fill={'var(--color-white)'}
                stroke={'var(--color-main)'}
              />
            }
            label={'06.11~06.20'}
          />
          <SelectButton
            Icon={<IconMap width={22} height={22} fill={'var(--color-main)'} />}
            label={'서울'}
          />
        </div>
      </div>

      {/*리스트 컨테이너*/}
      <div className={'flex flex-col px-5 pt-4.5 rounded-[20px] bg-white'}>
        <h2 className={'font-semibold text-xl text-black '}>찾은 팝업</h2>

        {/*팝업 리스트*/}
        <div className={'flex flex-col gap-y-3 mt-4 pb-[90px]'}>
          {popupList.map((popup, index) => (
            <BadgedPopupCard {...popup} key={index} />
          ))}
        </div>
      </div>
      <Navbar />
    </div>
  );
}
