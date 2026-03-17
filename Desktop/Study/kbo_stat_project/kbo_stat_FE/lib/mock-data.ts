// KBO Mock Data for UI demonstration

export interface Team {
  id: string
  name: string
  shortName: string
  wins: number
  losses: number
  draws: number
  pct: string
  gb: string
  streak: string
  last10: string
  color: string
}

export interface PlayerBase {
  id: string
  name: string
  team: string
  teamColor: string
  position: string
  number: number
  birthDate: string
  age: number
  hand: string
  height: number
  weight: number
  salary: string
  imageUrl?: string
}

export interface HitterSeason {
  season: number
  team: string
  G: number
  PA: number
  AB: number
  H: number
  "2B": number
  "3B": number
  HR: number
  RBI: number
  SB: number
  BB: number
  SO: number
  AVG: string
  OBP: string
  SLG: string
  OPS: string
  WAR: string
  wRC: string
  BABIP: string
}

export interface PitcherSeason {
  season: number
  team: string
  G: number
  W: number
  L: number
  SV: number
  HLD: number
  IP: string
  H: number
  ER: number
  BB: number
  SO: number
  ERA: string
  WHIP: string
  K9: string
  BB9: string
  FIP: string
  WAR: string
}

export interface AIPrediction {
  playerId: string
  targetSeason: number
  predictedWAR: string
  confidence: number
  predictedStats: Record<string, string>
  trend: "up" | "down" | "stable"
  summary: string
  riskFactors: string[]
  upside: string[]
}

export const teams: Team[] = [
  { id: "SSG", name: "SSG 랜더스", shortName: "SSG", wins: 52, losses: 38, draws: 2, pct: ".578", gb: "-", streak: "W3", last10: "7-3", color: "#CE0E2D" },
  { id: "KIA", name: "KIA 타이거즈", shortName: "KIA", wins: 50, losses: 39, draws: 3, pct: ".562", gb: "1.5", streak: "W1", last10: "6-4", color: "#EA0029" },
  { id: "LG", name: "LG 트윈스", shortName: "LG", wins: 49, losses: 41, draws: 2, pct: ".544", gb: "3.0", streak: "L2", last10: "5-5", color: "#C30452" },
  { id: "두산", name: "두산 베어스", shortName: "두산", wins: 47, losses: 42, draws: 3, pct: ".528", gb: "4.5", streak: "W2", last10: "6-4", color: "#131230" },
  { id: "KT", name: "KT 위즈", shortName: "KT", wins: 46, losses: 44, draws: 2, pct: ".511", gb: "6.0", streak: "L1", last10: "4-6", color: "#000000" },
  { id: "NC", name: "NC 다이노스", shortName: "NC", wins: 44, losses: 45, draws: 3, pct: ".494", gb: "7.5", streak: "W1", last10: "5-5", color: "#315288" },
  { id: "삼성", name: "삼성 라이온즈", shortName: "삼성", wins: 42, losses: 47, draws: 3, pct: ".472", gb: "9.5", streak: "L3", last10: "3-7", color: "#074CA1" },
  { id: "롯데", name: "롯데 자이언츠", shortName: "롯데", wins: 40, losses: 49, draws: 3, pct: ".449", gb: "11.5", streak: "W1", last10: "4-6", color: "#041E42" },
  { id: "한화", name: "한화 이글스", shortName: "한화", wins: 38, losses: 52, draws: 2, pct: ".422", gb: "14.0", streak: "L4", last10: "2-8", color: "#FF6600" },
  { id: "키움", name: "키움 히어로즈", shortName: "키움", wins: 36, losses: 53, draws: 3, pct: ".404", gb: "15.5", streak: "L1", last10: "3-7", color: "#820024" },
]

export const topHitters: (PlayerBase & { stats: HitterSeason })[] = [
  {
    id: "h1", name: "이정후", team: "키움", teamColor: "#820024", position: "외야수", number: 51,
    birthDate: "1998-08-20", age: 27, hand: "좌투좌타", height: 185, weight: 90, salary: "9.5억",
    stats: { season: 2026, team: "키움", G: 89, PA: 398, AB: 352, H: 128, "2B": 25, "3B": 2, HR: 12, RBI: 56, SB: 8, BB: 38, SO: 42, AVG: ".364", OBP: ".425", SLG: ".534", OPS: ".959", WAR: "5.2", wRC: "158", BABIP: ".395" }
  },
  {
    id: "h2", name: "강백호", team: "KT", teamColor: "#000000", position: "1루수", number: 50,
    birthDate: "1999-07-29", age: 26, hand: "우투좌타", height: 185, weight: 105, salary: "7.8억",
    stats: { season: 2026, team: "KT", G: 88, PA: 390, AB: 345, H: 112, "2B": 18, "3B": 0, HR: 28, RBI: 78, SB: 1, BB: 40, SO: 88, AVG: ".325", OBP: ".395", SLG: ".610", OPS: "1.005", WAR: "4.8", wRC: "165", BABIP: ".348" }
  },
  {
    id: "h3", name: "오스틴", team: "LG", teamColor: "#C30452", position: "외야수", number: 33,
    birthDate: "1991-06-15", age: 34, hand: "우투우타", height: 188, weight: 100, salary: "12억",
    stats: { season: 2026, team: "LG", G: 85, PA: 375, AB: 330, H: 108, "2B": 22, "3B": 1, HR: 25, RBI: 72, SB: 3, BB: 42, SO: 75, AVG: ".327", OBP: ".408", SLG: ".609", OPS: "1.017", WAR: "4.5", wRC: "162", BABIP: ".352" }
  },
  {
    id: "h4", name: "최정", team: "SSG", teamColor: "#CE0E2D", position: "3루수", number: 14,
    birthDate: "1987-09-29", age: 38, hand: "우투우타", height: 182, weight: 92, salary: "15억",
    stats: { season: 2026, team: "SSG", G: 87, PA: 380, AB: 328, H: 98, "2B": 16, "3B": 0, HR: 22, RBI: 65, SB: 0, BB: 48, SO: 92, AVG: ".299", OBP: ".395", SLG: ".549", OPS: ".944", WAR: "3.9", wRC: "148", BABIP: ".322" }
  },
  {
    id: "h5", name: "김도영", team: "KIA", teamColor: "#EA0029", position: "유격수", number: 5,
    birthDate: "2003-10-02", age: 22, hand: "우투우타", height: 180, weight: 78, salary: "3.5억",
    stats: { season: 2026, team: "KIA", G: 90, PA: 405, AB: 365, H: 118, "2B": 28, "3B": 5, HR: 18, RBI: 62, SB: 22, BB: 32, SO: 68, AVG: ".323", OBP: ".381", SLG: ".548", OPS: ".929", WAR: "5.8", wRC: "152", BABIP: ".358" }
  },
]

export const topPitchers: (PlayerBase & { stats: PitcherSeason })[] = [
  {
    id: "p1", name: "안우진", team: "KIA", teamColor: "#EA0029", position: "투수", number: 29,
    birthDate: "1999-09-26", age: 26, hand: "우투우타", height: 185, weight: 88, salary: "8.2억",
    stats: { season: 2026, team: "KIA", G: 20, W: 12, L: 3, SV: 0, HLD: 0, IP: "138.1", H: 95, ER: 30, BB: 35, SO: 168, ERA: "1.95", WHIP: "0.94", K9: "10.93", BB9: "2.28", FIP: "2.68", WAR: "5.1" }
  },
  {
    id: "p2", name: "쿠에바스", team: "SSG", teamColor: "#CE0E2D", position: "투수", number: 45,
    birthDate: "1991-03-18", age: 35, hand: "좌투좌타", height: 190, weight: 95, salary: "15억",
    stats: { season: 2026, team: "SSG", G: 19, W: 10, L: 4, SV: 0, HLD: 0, IP: "125.0", H: 98, ER: 35, BB: 28, SO: 138, ERA: "2.52", WHIP: "1.01", K9: "9.94", BB9: "2.02", FIP: "3.05", WAR: "3.8" }
  },
  {
    id: "p3", name: "원태인", team: "삼성", teamColor: "#074CA1", position: "투수", number: 17,
    birthDate: "2000-04-02", age: 26, hand: "우투우타", height: 183, weight: 85, salary: "5.5억",
    stats: { season: 2026, team: "삼성", G: 20, W: 9, L: 5, SV: 0, HLD: 0, IP: "132.0", H: 108, ER: 38, BB: 30, SO: 145, ERA: "2.59", WHIP: "1.05", K9: "9.89", BB9: "2.05", FIP: "3.12", WAR: "3.5" }
  },
  {
    id: "p4", name: "고우석", team: "LG", teamColor: "#C30452", position: "투수", number: 38,
    birthDate: "1999-08-06", age: 26, hand: "우투우타", height: 187, weight: 92, salary: "7.0억",
    stats: { season: 2026, team: "LG", G: 42, W: 3, L: 2, SV: 28, HLD: 0, IP: "42.0", H: 25, ER: 8, BB: 12, SO: 58, ERA: "1.71", WHIP: "0.88", K9: "12.43", BB9: "2.57", FIP: "2.15", WAR: "2.8" }
  },
  {
    id: "p5", name: "엔스", team: "KIA", teamColor: "#EA0029", position: "투수", number: 43,
    birthDate: "1993-11-22", age: 32, hand: "좌투좌타", height: 193, weight: 102, salary: "18억",
    stats: { season: 2026, team: "KIA", G: 19, W: 11, L: 3, SV: 0, HLD: 0, IP: "128.2", H: 102, ER: 32, BB: 22, SO: 130, ERA: "2.24", WHIP: "0.96", K9: "9.09", BB9: "1.54", FIP: "2.85", WAR: "4.2" }
  },
]

export const playerSeasonHistory: HitterSeason[] = [
  { season: 2022, team: "키움", G: 142, PA: 620, AB: 556, H: 193, "2B": 30, "3B": 4, HR: 23, RBI: 105, SB: 14, BB: 52, SO: 55, AVG: ".349", OBP: ".396", SLG: ".533", OPS: ".929", WAR: "7.0", wRC: "155", BABIP: ".365" },
  { season: 2023, team: "키움", G: 135, PA: 595, AB: 530, H: 170, "2B": 27, "3B": 3, HR: 19, RBI: 85, SB: 10, BB: 55, SO: 60, AVG: ".321", OBP: ".385", SLG: ".494", OPS: ".879", WAR: "5.8", wRC: "142", BABIP: ".340" },
  { season: 2024, team: "키움", G: 140, PA: 610, AB: 545, H: 185, "2B": 32, "3B": 3, HR: 20, RBI: 92, SB: 12, BB: 50, SO: 48, AVG: ".339", OBP: ".402", SLG: ".532", OPS: ".934", WAR: "6.5", wRC: "152", BABIP: ".355" },
  { season: 2025, team: "키움", G: 138, PA: 600, AB: 540, H: 178, "2B": 28, "3B": 2, HR: 18, RBI: 82, SB: 9, BB: 48, SO: 50, AVG: ".330", OBP: ".393", SLG: ".509", OPS: ".902", WAR: "6.0", wRC: "148", BABIP: ".348" },
  { season: 2026, team: "키움", G: 89, PA: 398, AB: 352, H: 128, "2B": 25, "3B": 2, HR: 12, RBI: 56, SB: 8, BB: 38, SO: 42, AVG: ".364", OBP: ".425", SLG: ".534", OPS: ".959", WAR: "5.2", wRC: "158", BABIP: ".395" },
]

export const aiPredictions: AIPrediction[] = [
  {
    playerId: "h1",
    targetSeason: 2026,
    predictedWAR: "6.8",
    confidence: 82,
    predictedStats: {
      AVG: ".342", HR: "21", RBI: "98", OPS: ".935", SB: "14"
    },
    trend: "up",
    summary: "이정후 선수는 현재 BABIP(.395)가 리그 평균 대비 상당히 높은 상태이나, 타구 품질(Hard%)과 주력을 감안하면 .340-.350 수준은 유지 가능할 것으로 예측됩니다. 시즌 풀타임 기준 WAR 6.8을 전망합니다.",
    riskFactors: ["높은 BABIP 회귀 가능성", "하반기 컨디션 관리"],
    upside: ["역대급 컨택 능력", "성장하는 장타력", "안정적 수비"]
  },
  {
    playerId: "h5",
    targetSeason: 2026,
    predictedWAR: "7.5",
    confidence: 78,
    predictedStats: {
      AVG: ".310", HR: "30", RBI: "105", OPS: ".920", SB: "35"
    },
    trend: "up",
    summary: "김도영 선수는 22세의 나이로 이미 리그 상위 퍼포먼스를 보여주고 있습니다. 30-30을 달성할 수 있는 유일한 유격수로, 시즌 완주 시 역대급 시즌이 기대됩니다.",
    riskFactors: ["부상 이력", "시즌 후반 체력 관리", "삼진 증가 추세"],
    upside: ["30-30 달성 가능", "수비 지표 상승 중", "나이 대비 압도적 성숙도"]
  },
  {
    playerId: "p1",
    targetSeason: 2026,
    predictedWAR: "6.5",
    confidence: 85,
    predictedStats: {
      ERA: "2.15", W: "16", SO: "225", WHIP: "0.98", IP: "185.0"
    },
    trend: "up",
    summary: "안우진 선수는 KBO 최고의 투수로서 압도적인 탈삼진 능력과 제구력을 겸비하고 있습니다. FIP 대비 ERA가 낮아 다소 행운이 작용했으나, 투구 품질 자체가 워낙 뛰어나 시즌 ERA 2점대 초반을 유지할 것으로 전망합니다.",
    riskFactors: ["이닝 부담 누적", "MLB 포스팅 가능성"],
    upside: ["KBO 역대급 시즌 가능", "탈삼진 200+ 확실시", "완투 능력"]
  },
]

export const recentGames = [
  { date: "07.15", home: "SSG", away: "LG", homeScore: 5, awayScore: 3, mvp: "최정", stadium: "인천" },
  { date: "07.15", home: "KIA", away: "두산", homeScore: 8, awayScore: 2, mvp: "김도영", stadium: "광주" },
  { date: "07.15", home: "KT", away: "NC", homeScore: 4, awayScore: 4, mvp: "-", stadium: "수원" },
  { date: "07.15", home: "삼성", away: "한화", homeScore: 6, awayScore: 1, mvp: "원태인", stadium: "대구" },
  { date: "07.15", home: "키움", away: "롯데", homeScore: 3, awayScore: 7, mvp: "전준우", stadium: "고척" },
]
