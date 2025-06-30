
import { RegularText } from "@/app/shared/ui/text/RegularText";
import { TagExample } from "@/app/shared/ui/tag/TagExample";
import { BadgeExample } from "@/app/shared/ui/badge/BadgeExample";

export default function Home() {
  return (
      <div>
        <RegularText>regular text</RegularText>
        <TagExample />
        <BadgeExample />
      </div>  
  );
}
