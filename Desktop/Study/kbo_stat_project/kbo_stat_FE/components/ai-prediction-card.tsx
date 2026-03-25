"use client"

import { AlertTriangle, Brain, Minus, TrendingDown, TrendingUp, Zap } from "lucide-react"

import { useLang } from "@/components/lang-context"
import { Progress } from "@/components/ui/progress"
import type { AIPrediction } from "@/lib/mock-data"

export function AIPredictionCard({ prediction }: { prediction: AIPrediction }) {
  const { lang } = useLang()

  const trendIcon =
    prediction.trend === "up" ? (
      <TrendingUp className="h-4 w-4 text-primary" />
    ) : prediction.trend === "down" ? (
      <TrendingDown className="h-4 w-4 text-kbo-danger" />
    ) : (
      <Minus className="h-4 w-4 text-muted-foreground" />
    )

  const trendLabel =
    prediction.trend === "up"
      ? lang === "ko"
        ? "상승세"
        : "Uptrend"
      : prediction.trend === "down"
        ? lang === "ko"
          ? "하락세"
          : "Downtrend"
        : lang === "ko"
          ? "유지"
          : "Stable"

  const trendColor =
    prediction.trend === "up"
      ? "text-primary"
      : prediction.trend === "down"
        ? "text-kbo-danger"
        : "text-muted-foreground"

  return (
    <div className="rounded-lg border border-primary/20 bg-card">
      <div className="flex items-center justify-between border-b border-border px-4 py-3">
        <div className="flex items-center gap-2">
          <div className="flex h-7 w-7 items-center justify-center rounded-md bg-primary/10">
            <Brain className="h-4 w-4 text-primary" />
          </div>
          <div>
            <h3 className="text-sm font-semibold text-foreground">{lang === "ko" ? "AI 성적 예측" : "AI Projection"}</h3>
            <p className="text-xs text-muted-foreground">
              {prediction.targetSeason} {lang === "ko" ? "시즌 전망" : "Season Outlook"}
            </p>
          </div>
        </div>
        <div className="flex items-center gap-1.5">
          {trendIcon}
          <span className={`text-xs font-medium ${trendColor}`}>{trendLabel}</span>
        </div>
      </div>

      <div className="border-b border-border px-4 py-4">
        <div className="flex items-end justify-between">
          <div>
            <p className="text-xs text-muted-foreground">{lang === "ko" ? "예상 WAR" : "Projected WAR"}</p>
            <p className="font-mono text-3xl font-bold text-primary">{prediction.predictedWAR}</p>
          </div>
          <div className="text-right">
            <p className="text-xs text-muted-foreground">{lang === "ko" ? "신뢰도" : "Confidence"}</p>
            <p className="text-lg font-mono font-semibold text-foreground">{prediction.confidence}%</p>
          </div>
        </div>
        <div className="mt-2">
          <Progress value={prediction.confidence} className="h-1.5 bg-secondary" />
        </div>
      </div>

      <div className="border-b border-border px-4 py-3">
        <p className="mb-2 text-xs font-medium text-muted-foreground">{lang === "ko" ? "예상 주요 지표" : "Projected Key Stats"}</p>
        <div className="grid grid-cols-3 gap-3 sm:grid-cols-5">
          {Object.entries(prediction.predictedStats).map(([key, value]) => (
            <div key={key} className="rounded-md bg-secondary/50 px-2 py-1.5 text-center">
              <p className="text-xs text-muted-foreground">{key}</p>
              <p className="text-sm font-mono font-semibold text-foreground">{value}</p>
            </div>
          ))}
        </div>
      </div>

      <div className="border-b border-border px-4 py-3">
        <p className="text-sm leading-relaxed text-muted-foreground">{prediction.summary}</p>
      </div>

      <div className="grid grid-cols-2 divide-x divide-border">
        <div className="px-4 py-3">
          <div className="mb-2 flex items-center gap-1.5">
            <AlertTriangle className="h-3.5 w-3.5 text-kbo-danger" />
            <p className="text-xs font-medium text-kbo-danger">{lang === "ko" ? "리스크" : "Risk"}</p>
          </div>
          <ul className="flex flex-col gap-1">
            {prediction.riskFactors.map((risk, i) => (
              <li key={i} className="text-xs leading-relaxed text-muted-foreground">
                - {risk}
              </li>
            ))}
          </ul>
        </div>
        <div className="px-4 py-3">
          <div className="mb-2 flex items-center gap-1.5">
            <Zap className="h-3.5 w-3.5 text-primary" />
            <p className="text-xs font-medium text-primary">{lang === "ko" ? "상승 요인" : "Upside"}</p>
          </div>
          <ul className="flex flex-col gap-1">
            {prediction.upside.map((item, i) => (
              <li key={i} className="text-xs leading-relaxed text-muted-foreground">
                - {item}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  )
}
