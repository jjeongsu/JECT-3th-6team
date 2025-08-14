'use client';

import { useRef, useState } from 'react';
import { MapMarker } from 'react-kakao-maps-sdk';
import { useQuery } from '@tanstack/react-query';

import { KakaoMap } from '@/shared/ui';
import SearchInput from '@/shared/ui/input/SearchInput';
import MyLocationButton from '@/shared/ui/map/MyLocationButton';

import { useFilterContext } from '@/features/filtering/lib/FilterContext';
import KeywordFilterPreview, {
  KeywordChip,
} from '@/features/filtering/ui/KeywordFilterPreview';
import toKeywordChips from '@/features/filtering/lib/makeKeywordChip';
import useSearchMyLocation from '@/features/map/hook/useSearchMyLocation';
import useMapSearch from '@/features/map/hook/useMapSearch';

import { getMapPopupListApi } from '@/entities/map/api';
import getPopupListApi from '@/entities/popup/api/getPopupListApi';
import BadgedPopupCard from '@/entities/popup/ui/BadgedPopupCard';
import { PopupItemType } from '@/entities/popup/types/PopupListItem';

export default function FilterGroupMapContainer() {
  // 기본 위치 (서울숲 4번출구 앞)
  const defaultCenter = { lat: 37.544643, lng: 127.044368 };
  const [center, setCenter] = useState(defaultCenter);
  const [selectedPopupId, setSelectedPopupId] = useState<number | null>(null);
  const [selectedPopupData, setSelectedPopupData] =
    useState<PopupItemType | null>(null);

  const { filter, handleOpen, handleDeleteKeyword } = useFilterContext();
  const { popupType, category } = filter.keyword;
  const mapRef = useRef<kakao.maps.Map>(null);
  const { handleMoveToCurrentLocation } = useSearchMyLocation();
  const { searchValue, isSearchFocused, handleSearchBlur, handleChange } =
    useMapSearch();
  const popupListIconSrc = '/icons/Color/Icon_NormalMinus.svg';
  const selectedPopupIconSrc = '/icons/Color/Icon_Map.svg';

  const keywords: KeywordChip[] = [
    ...toKeywordChips(popupType, 'category'),
    ...toKeywordChips(category, 'category'),
  ];

  const handleMarkerClick = async (popupId: number) => {
    const isCurrentlySelected = selectedPopupId === popupId;

    if (isCurrentlySelected) {
      return;
    }

    // 새로운 마커 선택
    setSelectedPopupId(popupId);
    console.log('선택된 팝업 ID:', popupId);

    try {
      const popupData = await getPopupListApi({ popupId });
      console.log('popupData:', popupData);

      // API 응답에서 첫 번째 팝업 데이터를 selectedPopupData로 설정
      if (popupData.content && popupData.content.length > 0) {
        setSelectedPopupData(popupData.content[0]);
      }
    } catch (error) {
      console.error('팝업 데이터 조회 실패:', error);
      setSelectedPopupData(null);
    }
  };

  // 위치 결정 로직:
  // 1. 로딩 중이면 지도 로딩 상태 유지 (지도를 불러오는 중... 표시)
  // 2. 로딩 완료 후 기본 위치(서울숲역 4번출구) 사용
  // 3. 내위치찾기 버튼 클릭 시 현재 위치 추적 - 이때 권한설정 팝업

  const { data: popupList } = useQuery({
    queryKey: ['mapPopupList', popupType, category],
    queryFn: () =>
      getMapPopupListApi({
        minLatitude: 37.541673,
        maxLatitude: 37.545894,
        minLongitude: 127.041309,
        maxLongitude: 127.047804,
        type: popupType.length > 0 ? popupType.join(',') : undefined,
        category: category.length > 0 ? category.join(',') : undefined,
      }),
  });

  return (
    <div className="w-full h-screen pb-[100px] relative">
      {/* 검색 포커스 시 지도 영역만 오버레이 */}
      {isSearchFocused && (
        <div className="absolute top-0 left-0 right-0 bottom-[100px] z-30 bg-white">
          <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-30 w-[400px] h-[75px] rounded-2xl bg-white shadow-[0_2px_10px_0_rgba(0,0,0,0.05)] backdrop-blur-[5px] p-3">
            <SearchInput
              id={'search-input'}
              value={searchValue}
              onChange={handleChange}
              // onFocus={handleSearchFocus}
              onBlur={handleSearchBlur}
            />
          </div>
        </div>
      )}

      {/* 일반 상태 */}
      {!isSearchFocused && (
        <>
          <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-30 w-[400px] h-[122px] rounded-2xl bg-white/60 shadow-[0_2px_10px_0_rgba(0,0,0,0.05)] backdrop-blur-[5px] p-3 space-y-2.5">
            <SearchInput
              id={'search-input'}
              value={searchValue}
              onChange={handleChange}
              // onFocus={handleSearchFocus}
              onBlur={handleSearchBlur}
            />

            <KeywordFilterPreview
              initialStatus="unselect"
              onClick={() => handleOpen('keyword')}
              keywords={keywords}
              onDelete={({ label, type }) =>
                handleDeleteKeyword(label, type, 'filter')
              }
            />
          </div>

          <KakaoMap
            ref={mapRef}
            center={center}
            level={3}
            className="w-full h-full"
          >
            {popupList?.popupList?.map(popup => (
              <MapMarker
                key={popup.id}
                position={{ lat: popup.latitude, lng: popup.longitude }}
                image={{
                  src:
                    selectedPopupId === popup.id
                      ? selectedPopupIconSrc
                      : popupListIconSrc,
                  size: { width: 32, height: 32 },
                }}
                onClick={() => handleMarkerClick(popup.id)}
              />
            ))}
          </KakaoMap>
        </>
      )}

      <MyLocationButton
        onMoveToCurrentLocation={() =>
          handleMoveToCurrentLocation(mapRef, setCenter)
        }
      />
      {selectedPopupData && (
        <div className="absolute bottom-30 left-0 right-0 z-50">
          <div className="w-full max-w-sm mx-auto transform scale-100">
            <BadgedPopupCard {...selectedPopupData} />
          </div>
        </div>
      )}
    </div>
  );
}
