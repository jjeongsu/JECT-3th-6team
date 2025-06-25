'use client';

import { useState } from 'react';
import { Button, TextInput } from '@/shared/ui';
import MonthlyCalendar from '@/shared/ui/calendar/monthly-calendar/ui';

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
        <Button
          onClick={() => console.log('button')}
          disabled={false}
          color={'primary'}
          size={'fit'}
          hasShadow={true}
        >
          버튼입니다.
        </Button>
        <Button
          onClick={() => console.log('button')}
          disabled={false}
          color={'secondary'}
          size={'full'}
          hasShadow={true}
        >
          버튼
        </Button>
      </div>

      {/*TODO : 캘린더 컴포넌트 캠슐화 다듬기*/}
      <MonthlyCalendar selectedDate={date} onClick={value => setDate(value)} />
    </div>
  );
}
export default ReservationPage;
