import { RegularText } from "@/app/shared/ui/text/RegularText";
import { Tag } from "@/app/shared/ui/tag/Tag";
import { BadgeExample } from "@/app/shared/ui/badge/example";

export default function Home() {
  return (
      <div>
        hello
        <RegularText>regular text</RegularText>
        <Tag>체험형</Tag>
        <Tag>체험형</Tag>
        <BadgeExample />
      </div>  
  );
}
