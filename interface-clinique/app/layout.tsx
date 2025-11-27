import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { Auth0Provider } from '@auth0/auth0-react';

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "MedBot Intelligence - Clinical Interface",
  description: "AI-Powered Medical Document Assistant",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={inter.className}>
        {children}
      </body>
    </html>
  );
}
