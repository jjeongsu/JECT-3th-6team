"use client"

import React from "react"
import { ChevronRight, MapPin, Star, Clock } from "lucide-react"

import { Badge } from "../../shared/ui/badge/Badge"
import { Tag } from "../../shared/ui/tag/Tag"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { DescriptionTab } from "./DescriptionTab"
import { ReviewTab } from "./ReviewTab"
import { ImageCarousel } from "../features/ui/ImageCarousel"

export default function ProductDetail() {
  const images = ["/images/sunglass.jpg", "/images/sunglass.jpg", "/images/sunglass.jpg"]

  return (
    <div >
      {/* Image Carousel */}
      <ImageCarousel images={images} />

      {/* Main Detail */}
      <div className="p-4"> 
        {/* Badge and Rating */}
        <div className="flex items-center justify-between mb-4">
          <Badge className="bg-orange-500 hover:bg-orange-600 text-white">
            <Clock className="w-3 h-3 mr-1" />
            10일 남음
          </Badge>
          <div className="flex items-center gap-1">
            <Star className="w-4 h-4 fill-orange-400 text-orange-400" />
            <span className="text-sm font-medium">4</span>
            <span className="text-sm text-gray-500">(25개의 리뷰)</span>
          </div>
        </div>

        {/* Title */}
        <p className={`text-[24px] font-normal text-[var(--color-text-color)] font-pretendard leading-normal`}>젠틀몬스터</p>

        {/* Hashtags */}
        <div className="flex flex-wrap gap-2 mb-4">
          <Tag>수도권</Tag>
          <Tag>체험형</Tag>
          <Tag>패션</Tag>
          <Tag>뷰티</Tag>
        </div>

        {/* Location */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <MapPin className="w-4 h-4 text-gray-600" />
            <span className="text-sm text-gray-700">서울, 용산구 한남동 61-2</span>
          </div>
          <ChevronRight className="w-4 h-4 text-gray-400" />
        </div>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="description" className="w-full">
        <TabsList className="grid w-full grid-cols-2 bg-transparent border-b rounded-none h-auto p-0">
          <TabsTrigger
            value="description"
            className="data-[state=active]:bg-transparent data-[state=active]:shadow-none border-0 data-[state=active]:border-0 data-[state=active]:border-b-2 data-[state=active]:border-orange-500 data-[state=active]:text-black rounded-none pb-3 pt-4 text-base font-medium"
          >
            팝업 설명
          </TabsTrigger>
          <TabsTrigger
            value="reviews"
            className="data-[state=active]:bg-transparent data-[state=active]:shadow-none border-0 data-[state=active]:border-0 data-[state=active]:border-b-2 data-[state=active]:border-orange-500 data-[state=active]:text-black rounded-none pb-3 pt-4 text-base font-medium"
          >
            리뷰
          </TabsTrigger>
        </TabsList>

        <DescriptionTab />
        <ReviewTab />
      </Tabs>

    </div>
  )
}
