'use client';

import { useState } from 'react';
import { StandardButton, TextInput } from '@/shared/ui';
import MonthlyCalendar from '@/shared/ui/calendar/MonthlyCalendar';

function ReservationPage() {
  const [input, setInput] = useState('');
  const [error, setError] = useState(true);
  const [errorText, setErrorText] = useState(
    '*올바른 이메일 형식을 입력해주세요'
  );

  const [date, setDate] = useState(new Date());
  return (
    <div className={'flex flex-col gap-4 w-full px-10 py-10'}>
      <TextInput
        id={'Label'}
        value={input}
        onChange={setInput}
        placeholder="input"
        label={'Label'}
        error={error}
        errorMessage={errorText}
      />
      <div className={'flex gap-2'}>
        <StandardButton
          onClick={() => console.log('button')}
          disabled={false}
          color={'primary'}
          size={'fit'}
          hasShadow={true}
        >
          버튼입니다.
        </StandardButton>
        <StandardButton
          onClick={() => console.log('button')}
          disabled={false}
          color={'secondary'}
          size={'full'}
          hasShadow={true}
        >
          버튼
        </StandardButton>
      </div>

      <MonthlyCalendar selectedDate={date} onClick={value => setDate(value)} />
    </div>
  );
}
export default ReservationPage;
