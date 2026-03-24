"use client"

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useLang } from "@/components/lang-context"
import { formatTeamName } from "@/lib/romanize"
import type { HitterSeason } from "@/lib/mock-data"

export function PlayerStatsTable({ seasons }: { seasons: HitterSeason[] }) {
  const { lang } = useLang()
  const isKo = lang === "ko"

  return (
    <div className="rounded-lg border border-border bg-card">
      <div className="border-b border-border px-4 py-3">
        <h2 className="text-sm font-semibold text-foreground">{isKo ? "시즌별 기록" : "Season Stats"}</h2>
      </div>
      <div className="overflow-x-auto">
        <Table>
          <TableHeader>
            <TableRow className="border-border hover:bg-transparent">
              <TableHead className="sticky left-0 z-10 bg-card text-xs text-muted-foreground">{isKo ? "시즌" : "Season"}</TableHead>
              <TableHead className="sticky left-12 z-10 bg-card text-xs text-muted-foreground">{isKo ? "팀" : "Team"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "경기" : "G"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "타석" : "PA"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "타율" : "AVG"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">OPS</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "안타" : "H"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "홈런" : "HR"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "타점" : "RBI"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "출루" : "OBP"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "장타" : "SLG"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "타수" : "AB"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "2루타" : "2B"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "3루타" : "3B"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "도루" : "SB"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "볼넷" : "BB"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">{isKo ? "삼진" : "SO"}</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">WAR</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">wRC+</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">BABIP</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {seasons.map((season, idx) => (
              <TableRow key={`${season.season}-${season.team}-${idx}`} className="border-border transition-colors hover:bg-secondary/50">
                <TableCell className="sticky left-0 z-10 bg-card text-sm font-mono text-foreground">{season.season}</TableCell>
                <TableCell className="sticky left-12 z-10 bg-card text-sm text-muted-foreground">{formatTeamName(season.team, lang)}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.G}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.PA}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.AVG}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.OPS}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.H}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.HR}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.RBI}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.OBP}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.SLG}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.AB}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season["2B"]}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season["3B"]}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.SB}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.BB}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.SO}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.WAR}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.wRC}</TableCell>
                <TableCell className="text-center text-sm font-mono text-foreground">{season.BABIP}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
