"use client"

import { User, Ruler, Weight, Banknote } from "lucide-react"

import { Badge } from "@/components/ui/badge"
import { useLang } from "@/components/lang-context"
import { formatPlayerName, formatTeamName } from "@/lib/romanize"
import type { PlayerBase } from "@/lib/mock-data"

export function PlayerProfile({ player }: { player: PlayerBase }) {
  const { lang } = useLang()
  const hasHand = player.hand && player.hand !== "-"
  const hasHeight = Number(player.height) > 0
  const hasWeight = Number(player.weight) > 0
  const hasSalary = player.salary && player.salary !== "-"

  return (
    <div className="rounded-lg border border-border bg-card">
      <div className="flex flex-col gap-4 p-5 sm:flex-row sm:items-start">
        <div className="flex h-24 w-24 shrink-0 items-center justify-center rounded-xl bg-secondary">
          <User className="h-12 w-12 text-muted-foreground" />
        </div>

        <div className="flex-1">
          <div className="flex flex-wrap items-center gap-2">
            <h1 className="text-2xl font-bold text-foreground">{formatPlayerName(player.name, lang)}</h1>
            {player.number > 0 && (
              <Badge variant="outline" className="text-xs font-mono border-border text-muted-foreground">
                #{player.number}
              </Badge>
            )}
            <Badge className="text-xs" style={{ backgroundColor: player.teamColor, color: "#fff" }}>
              {formatTeamName(player.team, lang)}
            </Badge>
          </div>

          <p className="mt-1 text-sm text-muted-foreground">
            {player.position}
            {hasHand ? ` · ${player.hand}` : (
              <span className="ml-1 text-xs text-muted-foreground/60">
                {lang === "ko" ? "(투타 정보 없음)" : "(No handedness data)"}
              </span>
            )}
          </p>

          <div className="mt-3 flex flex-wrap gap-4">
            {hasHeight && (
              <div className="flex items-center gap-1.5 text-xs text-muted-foreground">
                <Ruler className="h-3.5 w-3.5" />
                <span>{player.height}cm</span>
              </div>
            )}
            {hasWeight && (
              <div className="flex items-center gap-1.5 text-xs text-muted-foreground">
                <Weight className="h-3.5 w-3.5" />
                <span>{player.weight}kg</span>
              </div>
            )}
            {hasSalary && (
              <div className="flex items-center gap-1.5 text-xs text-muted-foreground">
                <Banknote className="h-3.5 w-3.5" />
                <span>{player.salary}</span>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
