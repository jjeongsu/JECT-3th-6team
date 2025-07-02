
import { RegularText } from "@/app/shared/ui/text/RegularText";
import { TagExample } from "@/app/shared/ui/tag/TagExample";
import { BadgeExample } from "@/app/shared/ui/badge/BadgeExample";
import { ChipExample } from "@/app/shared/ui/chip/ChipExample";
export default function Home() {
  return (
      <div>
        <RegularText>regular text</RegularText>
        <TagExample />
        <BadgeExample />
        <ChipExample />
      </div>  
  );
}
