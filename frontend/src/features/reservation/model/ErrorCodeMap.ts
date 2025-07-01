const ERROR_CODE_MAP = {
  INVALID_EMAIL_FORMAT: '이메일 형식을 확인해주세요!', // 이메일
  INVALID_NAME_LENGTH: '2~10글자로 입력해주십시오.', // 이름
  SPECIAL_CHAR_INCLUDED: '특수문자는 포함될 수 없습니다.', // 이름

  NONE: '', // 유효성 검사에 걸리지 않을 경우 빈문자열 (표시되지 않음)
} as const;

export default ERROR_CODE_MAP;
