import * as React from "react"
import { Slot } from "@radix-ui/react-slot"
import { cva, type VariantProps } from "class-variance-authority"

import { cn } from "@/lib/utils"

const chipVariants = cva(
  "inline-flex items-start justify-center rounded-full px-3 py-2 text-xs font-medium w-fit whitespace-nowrap shrink-0 gap-2.5 focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden",
  {
    variants: {
      variant: {
        white: "bg-white text-gray80 border border-gray40",
        main: "bg-main text-white",
        mainLight: "bg-white text-main border border-gray40",
        disabled: "bg-gray40 text-gray80",
      },
      iconPosition: {
        left: "flex-row",
        right: "flex-row-reverse",
        none: "",
      },
    },
    defaultVariants: {
      variant: "white",
      iconPosition: "none",
    },
  }
)

export interface ChipProps
  extends React.ComponentProps<"span">,
    VariantProps<typeof chipVariants> {
  asChild?: boolean
  icon?: React.ReactNode
  iconPosition?: "left" | "right" | "none"
}

function Chip({
  className,
  variant,
  iconPosition = "none",
  icon,
  children,
  asChild = false,
  ...props
}: ChipProps) {
  const Comp = asChild ? Slot : "span"

  return (
    <Comp
      data-slot="chip"
      className={cn(chipVariants({ variant, iconPosition }), className)}
      {...props}
    >
      {icon && iconPosition !== "none" && (
        <span className="flex items-center justify-center [&>svg]:size-3 [&>svg]:pointer-events-none">
          {icon}
        </span>
      )}
      <span className="flex flex-col items-start gap-2.5">
        {children}
      </span>
    </Comp>
  )
}

export { Chip, chipVariants }
