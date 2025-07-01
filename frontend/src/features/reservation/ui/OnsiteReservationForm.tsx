'use client';

import {
  BottomButtonContainer,
  NumberInput,
  StandardButton,
  TextInput,
} from '@/shared/ui';
import useForm from '../hook/useForm';
import Image from 'next/image';
import ReloadIcon from '/public/icons/Normal/Icon_Reload.svg';

export default function OnsiteReservationForm() {
  const { formValue, error, handleChange, handleReset } = useForm({
    formType: 'onsite-reservation',
    initialFormValue: {
      name: '',
      headCount: 1,
      email: '',
    },
    initialError: {
      name: '',
      headCount: '',
      email: '',
    },
  });
  const headcountError =
    formValue.headCount >= 6 ? '최대 6명까지 가능합니다' : '';
  return (
    <div>
      <div className={'px-5 flex flex-col gap-y-11.5 mt-4'}>
        <TextInput
          label={'대기자 명'}
          placeholder={'대기자명을 입력하세요'}
          id={'reservation-name'}
          value={formValue.name}
          onChange={(value: string) => handleChange('name', value)}
          errorMessage={error.name}
          error={error.name !== ''}
        />

        <NumberInput
          label={'대기자 수'}
          value={formValue.headCount}
          max={6}
          min={1}
          onChange={(value: number) => handleChange('headCount', value)}
          errorMessage={headcountError} // 여기는 따로 처리
        />

        <TextInput
          label={'대기자 이메일'}
          placeholder={'user@gmail.com'}
          id={'reservation-email'}
          value={formValue.email}
          onChange={(value: string) => handleChange('email', value)}
          errorMessage={error.email}
          error={error.email !== ''}
        />
      </div>

      <BottomButtonContainer>
        <StandardButton
          onClick={handleReset}
          disabled={false}
          size={'fit'}
          color={'white'}
          hasShadow={false}
          customClass={'rounded-lg'}
        >
          <div className={'flex items-center gap-x-2'}>
            <Image
              src={ReloadIcon}
              width={17}
              height={17}
              alt={'reload'}
              objectFit={'cover'}
            />
            <span>초기화</span>
          </div>
        </StandardButton>

        <StandardButton
          onClick={() => console.log('예약!')}
          disabled={false}
          size={'full'}
          color={'primary'}
          hasShadow={false}
        >
          적용하기
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
