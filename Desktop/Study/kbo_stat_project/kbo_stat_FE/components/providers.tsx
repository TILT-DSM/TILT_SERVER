"use client"

import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { useState } from "react"
import { ThemeProvider } from "./theme-provider"
import { LangProvider } from "./lang-context"

export function Providers({ children }: { children: React.ReactNode }) {
  const [queryClient] = useState(() => new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 60 * 1000,
      },
    },
  }))

  return (
    <ThemeProvider attribute="class" defaultTheme="dark">
      <LangProvider>
        <QueryClientProvider client={queryClient}>
          {children}
        </QueryClientProvider>
      </LangProvider>
    </ThemeProvider>
  )
}
