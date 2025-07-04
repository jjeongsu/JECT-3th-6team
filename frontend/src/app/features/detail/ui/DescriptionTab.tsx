import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { ReviewTab } from "@/app/features/detail/ui/ReviewTab"

export function DescriptionTab() {
  return (
    <TabsContent value="description" className="px-5 py-6">
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">젠틀몬스터 팝업 스토어</h3>
        <p className="text-gray-600 leading-relaxed">
          젠틀몬스터의 특별한 팝업 스토어에서 최신 아이웨어 컬렉션을 만나보세요. 독창적인 디자인과 혁신적인 기술이
          결합된 선글라스와 안경을 체험할 수 있습니다.
        </p>
        <div className="space-y-2">
          <h4 className="font-medium">운영 시간</h4>
          <p className="text-sm text-gray-600">매일 10:00 - 22:00</p>
        </div>
        <div className="space-y-2">
          <h4 className="font-medium">특별 혜택</h4>
          <p className="text-sm text-gray-600">팝업 스토어 한정 제품 및 특가 할인</p>
        </div>
      </div>
    </TabsContent>
  )
}