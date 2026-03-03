"use client"

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

type StandingRow = {
  rank: number
  team: string
  wins: number
  losses: number
  draws: number
  win_pct: number | string
  gb: number | string | null
  streak?: string | null
  recent_10?: string | null
}

export function StandingsTable({
  rows,
  asOfDate,
}: {
  rows: StandingRow[]
  asOfDate: string | null
}) {
  return (
    <div className="rounded-lg border border-border bg-card">
      <div className="flex items-center justify-between border-b border-border px-4 py-3">
        <h2 className="text-sm font-semibold text-foreground">2026 Season Standings</h2>
        <span className="font-mono text-xs text-muted-foreground">{asOfDate ?? "-"}</span>
      </div>
      <Table>
        <TableHeader>
          <TableRow className="border-border hover:bg-transparent">
            <TableHead className="w-10 text-center text-xs text-muted-foreground">#</TableHead>
            <TableHead className="text-xs text-muted-foreground">Team</TableHead>
            <TableHead className="text-center text-xs text-muted-foreground">W</TableHead>
            <TableHead className="text-center text-xs text-muted-foreground">D</TableHead>
            <TableHead className="text-center text-xs text-muted-foreground">L</TableHead>
            <TableHead className="text-center text-xs text-muted-foreground">PCT</TableHead>
            <TableHead className="text-center text-xs text-muted-foreground">GB</TableHead>
            <TableHead className="hidden text-center text-xs text-muted-foreground sm:table-cell">Streak</TableHead>
            <TableHead className="hidden text-center text-xs text-muted-foreground md:table-cell">Last10</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {rows.map((team) => (
            <TableRow key={`${team.rank}-${team.team}`} className="border-border transition-colors hover:bg-secondary/50">
              <TableCell className="text-center text-xs font-mono text-muted-foreground">{team.rank}</TableCell>
              <TableCell className="text-sm font-medium text-foreground">{team.team}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{team.wins}</TableCell>
              <TableCell className="text-center text-sm font-mono text-muted-foreground">{team.draws}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{team.losses}</TableCell>
              <TableCell className="text-center text-sm font-mono font-semibold text-foreground">
                {typeof team.win_pct === "number" ? team.win_pct.toFixed(3) : team.win_pct}
              </TableCell>
              <TableCell className="text-center text-sm font-mono text-muted-foreground">{team.gb ?? "-"}</TableCell>
              <TableCell className="hidden text-center text-xs font-mono sm:table-cell">{team.streak ?? "-"}</TableCell>
              <TableCell className="hidden text-center text-xs font-mono text-muted-foreground md:table-cell">{team.recent_10 ?? "-"}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
