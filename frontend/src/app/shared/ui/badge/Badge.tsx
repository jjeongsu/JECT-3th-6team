import * as React from "react"
import { Slot } from "@radix-ui/react-slot"
import { cva, type VariantProps } from "class-variance-authority"

import { cn } from "@/lib/utils"

export const badgeVariants = cva(
  "inline-flex items-center gap-1 px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 text-white rounded-md",
  {
    variants: {
      variant: {
        main: "bg-main",
        gray: "bg-[var(--color-gray80)]",
      },
    },
    defaultVariants: {
      variant: "main",
    },
  }
)

export interface BadgeProps extends React.ComponentProps<"span">,
  VariantProps<typeof badgeVariants> {
  asChild?: boolean
  icon?: React.ReactNode
  iconPosition?: 'none' | 'left'
}

export function Badge({
  className,
  variant,
  asChild = false,
  icon,
  iconPosition = 'none',
  children,
  ...props
}: BadgeProps) {
  const Comp = asChild ? Slot : "span"

  const renderContent = () => {
    if (iconPosition === 'none' || !icon) {
      return children
    }

    if (iconPosition === 'left') {
      return (
        <>
          {icon}
          {children}
        </>
      )
    }

    return children
  }

  return (
    <Comp
      data-slot="chip"
      className={cn(badgeVariants({ variant }), className)}
      {...props}
    >
      {renderContent()}
    </Comp>
  )
}


