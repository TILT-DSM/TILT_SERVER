"use client"

import { Badge } from "@/components/ui/badge"
import { Progress } from "@/components/ui/progress"
import { Brain, TrendingUp, TrendingDown, Minus, AlertTriangle, Zap } from "lucide-react"
import type { AIPrediction } from "@/lib/mock-data"

export function AIPredictionCard({ prediction }: { prediction: AIPrediction }) {
  const trendIcon = prediction.trend === "up"
    ? <TrendingUp className="h-4 w-4 text-primary" />
    : prediction.trend === "down"
    ? <TrendingDown className="h-4 w-4 text-kbo-danger" />
    : <Minus className="h-4 w-4 text-muted-foreground" />

  const trendLabel = prediction.trend === "up" ? "상승세" : prediction.trend === "down" ? "하락세" : "유지"
  const trendColor = prediction.trend === "up" ? "text-primary" : prediction.trend === "down" ? "text-kbo-danger" : "text-muted-foreground"

  return (
    <div className="rounded-lg border border-primary/20 bg-card">
      {/* Header */}
      <div className="flex items-center justify-between border-b border-border px-4 py-3">
        <div className="flex items-center gap-2">
          <div className="flex h-7 w-7 items-center justify-center rounded-md bg-primary/10">
            <Brain className="h-4 w-4 text-primary" />
          </div>
          <div>
            <h3 className="text-sm font-semibold text-foreground">AI 성적 예측</h3>
            <p className="text-xs text-muted-foreground">{prediction.targetSeason} 시즌 전망</p>
          </div>
        </div>
        <div className="flex items-center gap-1.5">
          {trendIcon}
          <span className={`text-xs font-medium ${trendColor}`}>{trendLabel}</span>
        </div>
      </div>

      {/* Predicted WAR */}
      <div className="border-b border-border px-4 py-4">
        <div className="flex items-end justify-between">
          <div>
            <p className="text-xs text-muted-foreground">예상 WAR</p>
            <p className="text-3xl font-bold font-mono text-primary">{prediction.predictedWAR}</p>
          </div>
          <div className="text-right">
            <p className="text-xs text-muted-foreground">신뢰도</p>
            <p className="text-lg font-mono font-semibold text-foreground">{prediction.confidence}%</p>
          </div>
        </div>
        <div className="mt-2">
          <Progress value={prediction.confidence} className="h-1.5 bg-secondary" />
        </div>
      </div>

      {/* Predicted Stats */}
      <div className="border-b border-border px-4 py-3">
        <p className="mb-2 text-xs font-medium text-muted-foreground">예상 주요 지표</p>
        <div className="grid grid-cols-3 gap-3 sm:grid-cols-5">
          {Object.entries(prediction.predictedStats).map(([key, value]) => (
            <div key={key} className="rounded-md bg-secondary/50 px-2 py-1.5 text-center">
              <p className="text-xs text-muted-foreground">{key}</p>
              <p className="text-sm font-mono font-semibold text-foreground">{value}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Summary */}
      <div className="border-b border-border px-4 py-3">
        <p className="text-sm leading-relaxed text-muted-foreground">{prediction.summary}</p>
      </div>

      {/* Risk / Upside */}
      <div className="grid grid-cols-2 divide-x divide-border">
        <div className="px-4 py-3">
          <div className="mb-2 flex items-center gap-1.5">
            <AlertTriangle className="h-3.5 w-3.5 text-kbo-danger" />
            <p className="text-xs font-medium text-kbo-danger">리스크</p>
          </div>
          <ul className="flex flex-col gap-1">
            {prediction.riskFactors.map((r, i) => (
              <li key={i} className="text-xs leading-relaxed text-muted-foreground">- {r}</li>
            ))}
          </ul>
        </div>
        <div className="px-4 py-3">
          <div className="mb-2 flex items-center gap-1.5">
            <Zap className="h-3.5 w-3.5 text-primary" />
            <p className="text-xs font-medium text-primary">상승 요인</p>
          </div>
          <ul className="flex flex-col gap-1">
            {prediction.upside.map((u, i) => (
              <li key={i} className="text-xs leading-relaxed text-muted-foreground">- {u}</li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  )
}
