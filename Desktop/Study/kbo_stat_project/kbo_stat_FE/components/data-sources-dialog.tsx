"use client"

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { useLang, tr } from "@/components/lang-context"
import { ScrollArea } from "@/components/ui/scroll-area"

export function DataSourcesDialog({ children }: { children: React.ReactNode }) {
  const { lang } = useLang()

  return (
    <Dialog>
      <DialogTrigger asChild>
        {children}
      </DialogTrigger>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>{tr("home.dataSources", lang)}</DialogTitle>
        </DialogHeader>
        <ScrollArea className="h-auto max-h-[300px] w-full rounded-md border p-4">
          <div className="flex flex-col gap-4 text-sm text-muted-foreground leading-relaxed">
            {lang === "ko" ? (
              <>
                <p>
                  본 사이트(KBO Stats)에서 제공하는 야구 통계 데이터 리소스는 아래와 같은 출처를 기반으로 <b>자체적인 머신러닝 분석 및 가공</b>을 거쳐 제공됩니다.
                </p>
                <ul className="list-disc pl-5 mt-2 flex flex-col gap-1">
                  <li><strong>KBO 공식 홈페이지</strong>: 라이브 중계 경기 일정 및 결과 데이터</li>
                  <li><strong>네이버 스포츠 (KBO)</strong>: 경기 로그 및 세부 선수 관련 스크래핑</li>
                  <li><strong>스탯티즈 (STATIZ)</strong>: 선수 고유 번호 트래킹 및 메타데이터 지원</li>
                  <li><strong>KBReport</strong>: 기타 스플릿 분석 참고 보조 자료</li>
                </ul>
                <p className="text-xs pt-4 border-t mt-2">
                  본 서비스는 비상업적 목적으로 제작되었으며, 분석 및 예측 확률 제공을 위해 원본 데이터를 가공하여 활용하고 있습니다. 모든 원본 기록의 저작권은 해당 데이터 제공처 및 KBO에 있습니다.
                </p>
              </>
            ) : (
              <>
                <p>
                  The baseball statistics and analytics provided on this site (KBO Stats) are based on the following sources, processed through <b>custom machine learning and data aggregation pipelines</b> of this service.
                </p>
                <ul className="list-disc pl-5 mt-2 flex flex-col gap-1">
                  <li><strong>KBO Official Website</strong>: Live game schedules and results</li>
                  <li><strong>Naver Sports (KBO)</strong>: Game logs and detailed player scraping</li>
                  <li><strong>STATIZ</strong>: Player unique ID tracking and metadata support</li>
                  <li><strong>KBReport</strong>: Supplementary reference for detailed splits</li>
                </ul>
                <p className="text-xs pt-4 border-t mt-2">
                  This service is created for non-commercial purposes, processing raw data to provide analytics and predictive probabilities. All raw records and intellectual property belong to their respective data providers and the KBO.
                </p>
              </>
            )}
          </div>
        </ScrollArea>
      </DialogContent>
    </Dialog>
  )
}
