const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL

type QueryValue = string | number | boolean | null | undefined

function buildQuery(params?: Record<string, QueryValue>) {
  if (!params) return ""

  const search = new URLSearchParams()
  for (const [key, value] of Object.entries(params)) {
    if (value === null || value === undefined) continue
    search.set(key, String(value))
  }

  const qs = search.toString()
  return qs ? `?${qs}` : ""
}

export async function fetchJson<T>(
  path: string,
  params?: Record<string, QueryValue>,
  init?: RequestInit
): Promise<T> {
  const normalizedPath = path.endsWith('/') ? path : `${path}/`
  const resolvedBaseUrl = resolveApiBaseUrl()
  const url = `${resolvedBaseUrl}${normalizedPath}${buildQuery(params)}`
  const res = await fetch(url, {
    ...init,
    headers: {
      Accept: "application/json",
      ...(init?.headers ?? {}),
    },
    cache: "no-store",
  })

  if (!res.ok) {
    const text = await res.text().catch(() => "")
    throw new Error(`API ${res.status} ${res.statusText}: ${text}`)
  }

  return (await res.json()) as T
}

function resolveApiBaseUrl() {
  // Browser runtime: always hit Next.js proxy first to avoid CORS/host mismatches.
  if (typeof window !== "undefined") {
    return "/api"
  }

  // Server runtime fallback (for SSR and build-time execution).
  return (API_BASE_URL || "http://127.0.0.1:8000/api").replace(/\/$/, "")
}
