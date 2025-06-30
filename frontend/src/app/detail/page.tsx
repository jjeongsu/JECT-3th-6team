"use client"

import { useState } from "react"
import { ChevronLeft, ChevronRight, MapPin, Star, Clock } from "lucide-react"
import Image from "next/image"

import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Tag } from "../shared/ui/tag/Tag"

export default function ProductDetail() {
  const [currentImageIndex, setCurrentImageIndex] = useState(0)
  const images = ["/images/sunglass.jpg", "/images/sunglass.jpg", "/images/sunglass.jpg"]

  const nextImage = () => {
    setCurrentImageIndex((prev) => (prev + 1) % images.length)
  }

  const prevImage = () => {
    setCurrentImageIndex((prev) => (prev - 1 + images.length) % images.length)
  }

  return (
    
      <div className="p-4">
        {/* Image Carousel */}
        <div className="relative mb-6">
          <div className="aspect-square overflow-hidden rounded-lg">
            <Image
              src={images[currentImageIndex]}
              alt="팝업 스토어 이미지"
              width={400}
              height={400}
              className="w-full h-full object-cover"
            />
          </div>
          
          {/* Navigation Buttons */}
          <Button
            onClick={prevImage}
            className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-white/80 hover:bg-white/90 rounded-full p-2"
            size="sm"
          >
            <ChevronLeft className="w-4 h-4" />
          </Button>
          
          <Button
            onClick={nextImage}
            className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-white/80 hover:bg-white/90 rounded-full p-2"
            size="sm"
          >
            <ChevronRight className="w-4 h-4" />
          </Button>
          
          {/* Image Indicators */}
          <div className="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-1">
            {images.map((_, index) => (
              <div
                key={index}
                className={`w-2 h-2 rounded-full ${
                  index === currentImageIndex ? "bg-white" : "bg-white/50"
                }`}
              />
            ))}
          </div>
        </div>

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
  )
}
