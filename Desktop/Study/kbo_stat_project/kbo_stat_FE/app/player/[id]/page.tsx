import Link from "next/link"
import { BarChart3 } from "lucide-react"

import { fetchJson } from "@/lib/api"
import { SiteHeader } from "@/components/site-header"
import { PlayerProfile } from "@/components/player-profile"
import { PlayerStatsTable } from "@/components/player-stats-table"
import { PlayerDetailSection } from "@/components/player-detail-section"
import { PredictionSummary } from "@/components/prediction-summary"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { topHitters, topPitchers } from "@/lib/mock-data"
import type { HitterSeason, PlayerBase } from "@/lib/mock-data"

type PlayerRow = {
  season: number
  team: string
  games: number
  PA: number
  AB: number
  H: number
  "2B": number
  "3B": number
  HR: number
  RBI: number
  BB: number
  SO: number
  SB: number
  AVG: number
  OBP: number
  SLG: number
  OPS: number
  WAR?: number
  wRC?: number
  BABIP?: number
}

type MonthlyRow = {
  month: string
  games: number
  PA: number
  AB: number
  H: number
  HR: number
  BB: number
  HBP?: number
  SF?: number
  TB_adj?: number
  AVG: number
  OBP: number
  SLG: number
  OPS: number
}

type PlayerDetailResponse = {
  season: number
  player_name: string
  player_id?: string
  profile?: {
    teams_in_season?: string[]
  }
  season_rows?: PlayerRow[]
  season_by_year?: PlayerRow[]
  monthly_splits?: MonthlyRow[]
  season_aggregate?: {
    OPS?: number
  }
  latest_prediction?: {
    predicted_hr_final?: number
    predicted_ops_final?: number
    predicted_war_final?: number
    predicted_hits_final?: number
    predicted_rbi_final?: number
    golden_glove_probability?: number
    mvp_probability?: number
    confidence_score?: number
    confidence_level?: string
    model_source?: string
    as_of_date?: string
  } | null
}

const TEAM_COLORS: Record<string, string> = {
  KIA: "#EA0029",
  LG: "#C30452",
  KT: "#000000",
  NC: "#315288",
  SSG: "#CE0E2D",
  두산: "#131230",
  롯데: "#041E42",
  삼성: "#074CA1",
  키움: "#820024",
  한화: "#FF6600",
}

function toNumber(value: unknown): number {
  const num = Number(value ?? 0)
  return Number.isFinite(num) ? num : 0
}

function toRate(value: unknown): string {
  return toNumber(value).toFixed(3)
}

function mapSeasonRows(rows: PlayerRow[] = []): HitterSeason[] {
  return rows
    .map((row) => ({
      season: toNumber(row.season),
      team: String(row.team ?? "-"),
      G: toNumber(row.games),
      PA: toNumber(row.PA),
      AB: toNumber(row.AB),
      H: toNumber(row.H),
      "2B": toNumber(row["2B"]),
      "3B": toNumber(row["3B"]),
      HR: toNumber(row.HR),
      RBI: toNumber(row.RBI),
      SB: toNumber(row.SB),
      BB: toNumber(row.BB),
      SO: toNumber(row.SO),
      AVG: toRate(row.AVG),
      OBP: toRate(row.OBP),
      SLG: toRate(row.SLG),
      OPS: toRate(row.OPS),
      WAR: row.WAR !== undefined ? String(row.WAR) : "-",
      wRC: row.wRC !== undefined ? String(row.wRC) : "-",
      BABIP: row.BABIP !== undefined ? toRate(row.BABIP) : "-",
    }))
    .sort((a, b) => a.season - b.season)
}

function buildApiProfile(detail: PlayerDetailResponse): PlayerBase {
  const latest = detail.season_rows?.[0] ?? detail.season_by_year?.[0]
  const teams = detail.profile?.teams_in_season ?? []
  const teamLabel = teams.length > 0 ? teams.join(" / ") : (latest?.team ?? "-")

  return {
    id: `api-${detail.player_name}`,
    name: detail.player_name,
    team: teamLabel,
    teamColor: TEAM_COLORS[latest?.team ?? ""] ?? "#6b7280",
    position: "타자",
    number: 0,
    birthDate: "-",
    age: 0,
    hand: "-",
    height: 0,
    weight: 0,
    salary: "-",
    imageUrl: undefined,
  }
}

function isApiNotFound(error: unknown): boolean {
  return error instanceof Error && error.message.includes("API 404")
}


function PitcherStatsTable({ pitcher }: { pitcher: typeof topPitchers[number] }) {
  return (
    <div className="rounded-lg border border-border bg-card">
      <div className="border-b border-border px-4 py-3">
        <h2 className="text-sm font-semibold text-foreground">시즌 기록</h2>
      </div>
      <div className="overflow-x-auto">
        <Table>
          <TableHeader>
            <TableRow className="hover:bg-transparent border-border">
              <TableHead className="text-xs text-muted-foreground">시즌</TableHead>
              <TableHead className="text-xs text-muted-foreground">팀</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">G</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">W</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">L</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">SV</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">HLD</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">IP</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">H</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">ER</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">BB</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">SO</TableHead>
              <TableHead className="text-center text-xs font-semibold text-foreground">ERA</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">WHIP</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">K/9</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">BB/9</TableHead>
              <TableHead className="text-center text-xs text-muted-foreground">FIP</TableHead>
              <TableHead className="text-center text-xs font-semibold text-primary">WAR</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow className="border-border hover:bg-secondary/50">
              <TableCell className="text-sm font-mono font-medium text-foreground">{pitcher.stats.season}</TableCell>
              <TableCell className="text-sm text-muted-foreground">{pitcher.stats.team}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.G}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.W}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.L}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.SV}</TableCell>
              <TableCell className="text-center text-sm font-mono text-muted-foreground">{pitcher.stats.HLD}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.IP}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.H}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.ER}</TableCell>
              <TableCell className="text-center text-sm font-mono text-muted-foreground">{pitcher.stats.BB}</TableCell>
              <TableCell className="text-center text-sm font-mono font-semibold text-kbo-highlight">{pitcher.stats.SO}</TableCell>
              <TableCell className="text-center text-sm font-mono font-semibold text-foreground">{pitcher.stats.ERA}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.WHIP}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.K9}</TableCell>
              <TableCell className="text-center text-sm font-mono text-muted-foreground">{pitcher.stats.BB9}</TableCell>
              <TableCell className="text-center text-sm font-mono text-foreground">{pitcher.stats.FIP}</TableCell>
              <TableCell className="text-center text-sm font-mono font-bold text-primary">{pitcher.stats.WAR}</TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </div>
    </div>
  )
}

export default async function PlayerPage({
  params,
  searchParams,
}: {
  params: Promise<{ id: string }>
  searchParams?: Promise<{ season?: string }> | { season?: string }
}) {
  const [{ id }, qs] = await Promise.all([params, searchParams ? Promise.resolve(searchParams) : Promise.resolve({})])
  const decodedId = decodeURIComponent(id)
  const season = toNumber((qs as { season?: string }).season) || undefined

  // Legacy mock route compatibility: /player/h1, /player/p1
  const mockHitter = topHitters.find((h) => h.id === decodedId)
  const mockPitcher = topPitchers.find((p) => p.id === decodedId)
  if (mockHitter || mockPitcher) {
    const displayPlayer = mockHitter || mockPitcher
    const isHitter = !!mockHitter
    return (
      <div className="min-h-screen bg-background">
        <SiteHeader />
        <main className="mx-auto max-w-7xl px-4 py-6">
          <nav className="mb-4 flex items-center gap-1.5 text-xs text-muted-foreground">
            <Link href="/" className="hover:text-foreground transition-colors">홈</Link>
            <span>/</span>
            <Link href="/players" className="hover:text-foreground transition-colors">선수</Link>
            <span>/</span>
            <span className="text-foreground">{displayPlayer?.name}</span>
          </nav>

          {displayPlayer && <PlayerProfile player={displayPlayer} />}

          <section className="mt-6">
            {isHitter ? (
              <PlayerDetailSection
                playerName={mockHitter!.name}
                playerId={mockHitter!.id}
                seasonHistory={[]}
                monthlyRows={[]}
                selectedSeason={Number(mockHitter!.stats.season)}
                availableSeasons={[Number(mockHitter!.stats.season)]}
              />
            ) : (
              <div className="rounded-lg border border-border bg-card p-8 text-center">
                <BarChart3 className="mx-auto h-10 w-10 text-muted-foreground" />
                <p className="mt-3 text-sm text-muted-foreground">투수 시각화 데이터 준비 중</p>
              </div>
            )}
          </section>

          <section className="mt-6">
            {isHitter ? (
              <PlayerStatsTable seasons={[mockHitter!.stats]} />
            ) : (
              mockPitcher && <PitcherStatsTable pitcher={mockPitcher} />
            )}
          </section>
        </main>
      </div>
    )
  }

  let detail: PlayerDetailResponse | null = null
  let notFound = false
  try {
    detail = await fetchJson<PlayerDetailResponse>(`/players/${encodeURIComponent(decodedId)}`, { season })
  } catch (error) {
    notFound = isApiNotFound(error)
    if (!notFound) throw error
  }

  if (!detail || notFound) {
    return (
      <div className="min-h-screen bg-background">
        <SiteHeader />
        <main className="mx-auto max-w-7xl px-4 py-6">
          <nav className="mb-4 flex items-center gap-1.5 text-xs text-muted-foreground">
            <Link href="/" className="hover:text-foreground transition-colors">홈</Link>
            <span>/</span>
            <Link href="/players" className="hover:text-foreground transition-colors">선수</Link>
            <span>/</span>
            <span className="text-foreground">{decodedId}</span>
          </nav>
          <div className="rounded-lg border border-border bg-card p-6">
            <h1 className="text-lg font-semibold text-foreground">선수 상세 데이터 없음</h1>
            <p className="mt-2 text-sm text-muted-foreground">"{decodedId}" 선수의 상세 데이터를 찾을 수 없습니다.</p>
          </div>
        </main>
      </div>
    )
  }

  const seasonHistory = mapSeasonRows(detail.season_by_year || detail.season_rows || [])
  const monthlyRows = detail.monthly_splits ?? []
  const availableSeasons = Array.from(
    new Set((detail.season_by_year || detail.season_rows || []).map((r) => Number(r.season)))
  ).filter(Boolean).sort((a, b) => b - a)

  if (seasonHistory.length === 0) {
    return (
      <div className="min-h-screen bg-background">
        <SiteHeader />
        <main className="mx-auto max-w-7xl px-4 py-6">
          <div className="rounded-lg border border-border bg-card p-6">
            <h1 className="text-lg font-semibold text-foreground">시즌 데이터 없음</h1>
            <p className="mt-2 text-sm text-muted-foreground">"{decodedId}" 선수의 시즌 기록이 아직 없습니다.</p>
          </div>
        </main>
      </div>
    )
  }

  const profile = buildApiProfile(detail)

  return (
    <div className="min-h-screen bg-background">
      <SiteHeader />
      <main className="mx-auto max-w-7xl px-4 py-6">
        <nav className="mb-4 flex items-center gap-1.5 text-xs text-muted-foreground">
          <Link href="/" className="hover:text-foreground transition-colors">홈</Link>
          <span>/</span>
          <Link href="/players" className="hover:text-foreground transition-colors">선수</Link>
          <span>/</span>
          <span className="text-foreground">{detail.player_name}</span>
        </nav>

        <PlayerProfile player={profile} />

        <section className="mt-6">
          <PredictionSummary prediction={detail.latest_prediction} />
        </section>

        <PlayerDetailSection
          playerName={detail.player_name}
          playerId={detail.player_id ?? detail.player_name}
          seasonHistory={seasonHistory.map((s) => ({
            season: Number(s.season),
            team: s.team,
            HR: Number(s.HR ?? 0),
            AVG: s.AVG,
            OPS: s.OPS,
            WAR: s.WAR,
          }))}
          monthlyRows={monthlyRows}
          selectedSeason={detail.season}
          availableSeasons={availableSeasons.length > 0 ? availableSeasons : [detail.season]}
        />

        <section className="mt-6">
          <PlayerStatsTable seasons={seasonHistory} />
        </section>
      </main>
    </div>
  )
}
