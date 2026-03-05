"use client"

import Link from "next/link"
import { useMemo, useState } from "react"
import { keepPreviousData, useQuery } from "@tanstack/react-query"
import { Filter, LayoutGrid, Search, TableIcon } from "lucide-react"

import { SiteHeader } from "@/components/site-header"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Switch } from "@/components/ui/switch"
import { fetchJson } from "@/lib/api"
import { topPitchers } from "@/lib/mock-data"

type ViewMode = "card" | "table"
type TeamFilter = "all" | string

type HitterRow = {
  team: string
  player_name: string
  games: number
  PA: number
  AB: number
  H: number
  HR: number
  RBI: number
  AVG: number
  OBP: number
  SLG: number
  OPS: number
}

type LeaderboardResponse = {
  season: number
  requested_season?: number
  effective_season?: number
  mode?: string
  metric: string
  effective_min_pa: number
  min_pa_policy: string
  team: string | null
  total: number
  limit: number
  offset: number
  rows: HitterRow[]
}

const HITTER_SORT_FIELDS = ["OPS", "AVG", "HR", "RBI", "OBP", "SLG", "H"] as const
const PITCHER_SORT_FIELDS = ["ERA", "W", "SO", "WAR", "WHIP", "K9", "FIP", "SV"] as const
const TEAM_OPTIONS = ["KIA", "LG", "KT", "NC", "SSG", "\uB450\uC0B0", "\uB86F\uB370", "\uC0BC\uC131", "\uD0A4\uC6C0", "\uD55C\uD654"] as const

const DECIMAL_METRICS = new Set(["AVG", "OBP", "SLG", "OPS"])

function formatMetric(value: number, metric: string) {
  if (DECIMAL_METRICS.has(metric)) return Number(value || 0).toFixed(3)
  return String(Math.round(Number(value || 0)))
}

export default function PlayersPage() {
  const [search, setSearch] = useState("")
  const [teamFilter, setTeamFilter] = useState<TeamFilter>("all")
  const [season, setSeason] = useState("2025")
  const [viewMode, setViewMode] = useState<ViewMode>("table")
  const [hitterSort, setHitterSort] = useState<string>("OPS")
  const [pitcherSort, setPitcherSort] = useState<string>("ERA")
  const [showRegulation, setShowRegulation] = useState(true)

  const { data: leaderboardData, isLoading, isError, error } = useQuery<LeaderboardResponse>({
    queryKey: ["leaderboard", season, teamFilter, showRegulation, hitterSort],
    queryFn: () =>
      fetchJson("/leaderboard", {
        season,
        team: teamFilter !== "all" ? teamFilter : undefined,
        metric: hitterSort,
        min_pa: showRegulation ? undefined : 0,
        limit: 200,
      }),
    placeholderData: keepPreviousData,
    staleTime: 5 * 60 * 1000,
    refetchOnWindowFocus: false,
  })

  const filteredHitters = useMemo(() => {
    const rows = leaderboardData?.rows ?? []
    if (!search.trim()) return rows
    return rows.filter((row) => row.player_name.includes(search) || row.team.includes(search))
  }, [leaderboardData, search])

  const filteredPitchers = useMemo(() => {
    const filtered = topPitchers.filter((p) => {
      const matchSearch = p.name.includes(search) || p.team.includes(search)
      const matchTeam = teamFilter === "all" || p.team === teamFilter
      return matchSearch && matchTeam
    })

    return [...filtered].sort((a, b) => {
      const aVal = parseFloat(String(a.stats[pitcherSort as keyof typeof a.stats])) || 0
      const bVal = parseFloat(String(b.stats[pitcherSort as keyof typeof b.stats])) || 0
      if (pitcherSort === "ERA" || pitcherSort === "WHIP" || pitcherSort === "FIP") return aVal - bVal
      return bVal - aVal
    })
  }, [search, teamFilter, pitcherSort])

  return (
    <div className="min-h-screen bg-background">
      <SiteHeader />

      <main className="mx-auto max-w-7xl px-4 py-6">
        <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h1 className="text-2xl font-bold text-foreground">Players</h1>
            <p className="mt-1 text-sm text-muted-foreground">KBO 타자/투수 기록을 시즌별로 확인합니다.</p>
            {leaderboardData?.mode === "PRESEASON_FALLBACK" && (
              <p className="mt-1 text-xs text-amber-500">
                요청 시즌 {leaderboardData.requested_season} 데이터가 없어 {leaderboardData.effective_season} 시즌을 표시 중입니다.
              </p>
            )}
          </div>

          <div className="flex items-center gap-3">
            <Select value={season} onValueChange={setSeason}>
              <SelectTrigger className="h-8 w-24 text-xs">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="2026">2026</SelectItem>
                <SelectItem value="2025">2025</SelectItem>
                <SelectItem value="2024">2024</SelectItem>
              </SelectContent>
            </Select>

            <div className="hidden items-center gap-2 sm:flex">
              <Switch checked={showRegulation} onCheckedChange={setShowRegulation} id="regulation" />
              <label htmlFor="regulation" className="cursor-pointer whitespace-nowrap text-xs text-muted-foreground">
                규정타석
              </label>
            </div>

            <div className="flex items-center rounded-md border border-border">
              <button
                onClick={() => setViewMode("table")}
                className={`flex h-8 w-8 items-center justify-center rounded-l-md transition-colors ${
                  viewMode === "table" ? "bg-primary text-primary-foreground" : "text-muted-foreground hover:text-foreground"
                }`}
                aria-label="Table view"
              >
                <TableIcon className="h-3.5 w-3.5" />
              </button>
              <button
                onClick={() => setViewMode("card")}
                className={`flex h-8 w-8 items-center justify-center rounded-r-md transition-colors ${
                  viewMode === "card" ? "bg-primary text-primary-foreground" : "text-muted-foreground hover:text-foreground"
                }`}
                aria-label="Card view"
              >
                <LayoutGrid className="h-3.5 w-3.5" />
              </button>
            </div>
          </div>
        </div>

        <div className="mb-5 flex flex-col gap-3 sm:flex-row sm:items-center">
          <div className="relative max-w-sm flex-1">
            <Search className="absolute top-1/2 left-3 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <input
              type="text"
              placeholder="선수명 또는 팀 검색"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="h-9 w-full rounded-lg border border-border bg-secondary py-0 pr-3 pl-9 text-sm text-foreground placeholder:text-muted-foreground focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
            />
          </div>

          <select
            value={teamFilter}
            onChange={(e) => setTeamFilter(e.target.value)}
            className="h-9 rounded-lg border border-border bg-secondary px-3 text-xs text-foreground focus:border-primary focus:outline-none"
          >
            <option value="all">All Teams</option>
            {TEAM_OPTIONS.map((team) => (
              <option key={team} value={team}>
                {team}
              </option>
            ))}
          </select>
        </div>

        <Tabs defaultValue="hitters">
          <TabsList className="bg-secondary">
            <TabsTrigger value="hitters">Hitters</TabsTrigger>
            <TabsTrigger value="pitchers">Pitchers</TabsTrigger>
          </TabsList>

          <TabsContent value="hitters" className="mt-4">
            <div className="mb-4 flex flex-wrap items-center gap-2">
              <Filter className="h-3.5 w-3.5 shrink-0 text-muted-foreground" />
              {HITTER_SORT_FIELDS.map((field) => (
                <button
                  key={field}
                  onClick={() => setHitterSort(field)}
                  className={`rounded-md px-2.5 py-1 text-xs font-medium transition-colors ${
                    hitterSort === field
                      ? "bg-primary text-primary-foreground"
                      : "bg-secondary text-muted-foreground hover:text-foreground"
                  }`}
                >
                  {field}
                </button>
              ))}
            </div>

            {isLoading ? (
              <div className="py-20 text-center text-sm text-muted-foreground">데이터를 불러오는 중...</div>
            ) : isError ? (
              <div className="rounded-md border border-red-500/30 bg-red-500/10 p-3 text-sm text-red-400">
                선수 데이터 요청 실패: {error instanceof Error ? error.message : "unknown error"}
              </div>
            ) : viewMode === "table" ? (
              <HitterTable hitters={filteredHitters} sortField={hitterSort} season={season} />
            ) : (
              <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
                {filteredHitters.map((h) => (
                  <Link
                    key={`${h.team}-${h.player_name}`}
                    href={`/player/${encodeURIComponent(h.player_name)}?season=${season}`}
                    className="rounded-lg border border-border bg-card p-4 transition-colors hover:border-primary/40"
                  >
                    <p className="text-sm font-semibold text-foreground">{h.player_name}</p>
                    <p className="mt-1 text-xs text-muted-foreground">{h.team}</p>
                    <p className="mt-3 text-xs text-muted-foreground">{hitterSort}</p>
                    <p className="text-lg font-bold text-primary">
                      {formatMetric(Number(h[hitterSort as keyof HitterRow] ?? 0), hitterSort)}
                    </p>
                  </Link>
                ))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="pitchers" className="mt-4">
            <div className="mb-4 flex flex-wrap items-center gap-2">
              <Filter className="h-3.5 w-3.5 shrink-0 text-muted-foreground" />
              {PITCHER_SORT_FIELDS.map((field) => (
                <button
                  key={field}
                  onClick={() => setPitcherSort(field)}
                  className={`rounded-md px-2.5 py-1 text-xs font-medium transition-colors ${
                    pitcherSort === field
                      ? "bg-primary text-primary-foreground"
                      : "bg-secondary text-muted-foreground hover:text-foreground"
                  }`}
                >
                  {field}
                </button>
              ))}
            </div>

            <div className="rounded-lg border border-border bg-card overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow className="border-border hover:bg-transparent">
                    <TableHead className="text-xs">선수</TableHead>
                    <TableHead className="text-xs">팀</TableHead>
                    <TableHead className="text-center text-xs">{pitcherSort}</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredPitchers.map((p) => (
                    <TableRow key={p.id} className="border-border">
                      <TableCell className="text-sm font-medium">{p.name}</TableCell>
                      <TableCell className="text-xs text-muted-foreground">{p.team}</TableCell>
                      <TableCell className="text-center text-sm">{String(p.stats[pitcherSort as keyof typeof p.stats])}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </TabsContent>
        </Tabs>
      </main>
    </div>
  )
}

function HitterTable({ hitters, sortField, season }: { hitters: HitterRow[]; sortField: string; season: string }) {
  return (
    <div className="overflow-x-auto rounded-lg border border-border bg-card">
      <Table>
        <TableHeader>
          <TableRow className="border-border hover:bg-transparent">
            <TableHead className="w-10 text-center text-xs">#</TableHead>
            <TableHead className="text-xs">선수</TableHead>
            <TableHead className="text-xs">팀</TableHead>
            <TableHead className="text-center text-xs">G</TableHead>
            <TableHead className="text-center text-xs">PA</TableHead>
            <TableHead className="text-center text-xs">AVG</TableHead>
            <TableHead className="text-center text-xs">HR</TableHead>
            <TableHead className="text-center text-xs">RBI</TableHead>
            <TableHead className="text-center text-xs">OBP</TableHead>
            <TableHead className="text-center text-xs">SLG</TableHead>
            <TableHead className="text-center text-xs">OPS</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {hitters.map((h, i) => (
            <TableRow key={`${h.team}-${h.player_name}-${i}`} className="border-border">
              <TableCell className="text-center text-xs text-muted-foreground">{i + 1}</TableCell>
              <TableCell className="text-sm font-medium">
                <Link href={`/player/${encodeURIComponent(h.player_name)}?season=${season}`} className="hover:text-primary">
                  {h.player_name}
                </Link>
              </TableCell>
              <TableCell className="text-xs text-muted-foreground">{h.team}</TableCell>
              <TableCell className="text-center text-xs">{h.games}</TableCell>
              <TableCell className="text-center text-xs">{h.PA}</TableCell>
              <TableCell className={`text-center text-xs ${sortField === "AVG" ? "font-semibold text-primary" : ""}`}>
                {Number(h.AVG || 0).toFixed(3)}
              </TableCell>
              <TableCell className={`text-center text-xs ${sortField === "HR" ? "font-semibold text-primary" : ""}`}>
                {h.HR}
              </TableCell>
              <TableCell className={`text-center text-xs ${sortField === "RBI" ? "font-semibold text-primary" : ""}`}>
                {h.RBI}
              </TableCell>
              <TableCell className={`text-center text-xs ${sortField === "OBP" ? "font-semibold text-primary" : ""}`}>
                {Number(h.OBP || 0).toFixed(3)}
              </TableCell>
              <TableCell className={`text-center text-xs ${sortField === "SLG" ? "font-semibold text-primary" : ""}`}>
                {Number(h.SLG || 0).toFixed(3)}
              </TableCell>
              <TableCell className={`text-center text-xs ${sortField === "OPS" ? "font-semibold text-primary" : ""}`}>
                {Number(h.OPS || 0).toFixed(3)}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
