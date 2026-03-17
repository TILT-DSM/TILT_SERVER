import Link from "next/link"

import { fetchJson } from "@/lib/api"
import { SiteHeader } from "@/components/site-header"
import { PlayerProfile } from "@/components/player-profile"
import { PlayerStatsTable } from "@/components/player-stats-table"
import { PlayerDetailSection } from "@/components/player-detail-section"
import { PredictionSummary } from "@/components/prediction-summary"
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
    birth_date?: string | null
    bats_throws?: string | null
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
    id: detail.player_id ?? `api-${detail.player_name}`,
    name: detail.player_name,
    team: teamLabel,
    teamColor: TEAM_COLORS[latest?.team ?? ""] ?? "#6b7280",
    position: "타자",
    number: 0,
    birthDate: detail.profile?.birth_date ?? "-",
    age: 0,
    hand: detail.profile?.bats_throws ?? "-",
    height: 0,
    weight: 0,
    salary: "-",
    imageUrl: undefined,
  }
}

function isApiNotFound(error: unknown): boolean {
  return error instanceof Error && error.message.includes("API 404")
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
            <Link href="/" className="transition-colors hover:text-foreground">Home</Link>
            <span>/</span>
            <Link href="/players" className="transition-colors hover:text-foreground">Players</Link>
            <span>/</span>
            <span className="text-foreground">{decodedId}</span>
          </nav>
          <div className="rounded-lg border border-border bg-card p-6">
            <h1 className="text-lg font-semibold text-foreground">선수 상세 정보가 없습니다</h1>
            <p className="mt-2 text-sm text-muted-foreground">
              "{decodedId}" 선수의 실제 데이터를 찾지 못했습니다. mock 데이터는 더 이상 표시하지 않습니다.
            </p>
          </div>
        </main>
      </div>
    )
  }

  const seasonHistory = mapSeasonRows(detail.season_by_year || detail.season_rows || [])
  const monthlyRows = detail.monthly_splits ?? []
  const availableSeasons = Array.from(
    new Set((detail.season_by_year || detail.season_rows || []).map((row) => Number(row.season))),
  )
    .filter(Boolean)
    .sort((a, b) => b - a)

  if (seasonHistory.length === 0) {
    return (
      <div className="min-h-screen bg-background">
        <SiteHeader />
        <main className="mx-auto max-w-7xl px-4 py-6">
          <div className="rounded-lg border border-border bg-card p-6">
            <h1 className="text-lg font-semibold text-foreground">시즌 기록이 없습니다</h1>
            <p className="mt-2 text-sm text-muted-foreground">
              "{detail.player_name}" 선수의 시즌 기록이 아직 없습니다.
            </p>
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
          <Link href="/" className="transition-colors hover:text-foreground">홈</Link>
          <span>/</span>
          <Link href="/players" className="transition-colors hover:text-foreground">선수</Link>
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
          seasonHistory={seasonHistory.map((seasonRow) => ({
            season: Number(seasonRow.season),
            team: seasonRow.team,
            HR: Number(seasonRow.HR ?? 0),
            AVG: seasonRow.AVG,
            OPS: seasonRow.OPS,
            WAR: seasonRow.WAR,
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
