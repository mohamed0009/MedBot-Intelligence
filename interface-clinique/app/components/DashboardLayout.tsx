'use client';

import { useState } from 'react';
import {
    LayoutDashboard,
    FileText,
    MessageSquare,
    Search,
    BarChart3,
    Users,
    Shield,
    Settings,
    Menu,
    X,
    Bell,
    User,
    LogOut,
    Activity
} from 'lucide-react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import Image from 'next/image';

const navigation = [
    { name: 'Dashboard', href: '/', icon: LayoutDashboard },
    { name: 'Documents', href: '/docs', icon: FileText },
    { name: 'Q&A Assistant', href: '/qa', icon: MessageSquare },
    { name: 'Search', href: '/search', icon: Search },
    { name: 'Analytics', href: '/analytics', icon: BarChart3 },
    { name: 'Patients', href: '/patients', icon: Users },
    { name: 'Audit Logs', href: '/audit', icon: Shield },
    { name: 'Settings', href: '/settings', icon: Settings },
];

export default function DashboardLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const pathname = usePathname();

    return (
        <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-teal-50">
            {/* Sidebar for mobile */}
            <div className={`fixed inset-0 z-50 lg:hidden ${sidebarOpen ? '' : 'hidden'}`}>
                <div className="fixed inset-0 bg-gray-600 bg-opacity-75" onClick={() => setSidebarOpen(false)} />
                <div className="fixed inset-y-0 left-0 flex w-64 flex-col bg-white shadow-xl">
                    <div className="flex h-16 items-center justify-between px-6 border-b">
                        <div className="flex items-center space-x-3">
                            <div className="relative w-10 h-10">
                                <Image
                                    src="/logo.png"
                                    alt="MedBot Logo"
                                    fill
                                    className="object-contain"
                                />
                            </div>
                            <div>
                                <span className="text-base font-bold bg-gradient-to-r from-blue-600 to-teal-600 bg-clip-text text-transparent">
                                    MedBot
                                </span>
                                <p className="text-[10px] text-gray-500 font-semibold tracking-wide">INTELLIGENCE</p>
                            </div>
                        </div>
                        <button onClick={() => setSidebarOpen(false)} className="text-gray-500 hover:text-gray-700">
                            <X className="h-6 w-6" />
                        </button>
                    </div>
                    <nav className="flex-1 space-y-1 px-3 py-4">
                        {navigation.map((item) => {
                            const isActive = pathname === item.href;
                            return (
                                <Link
                                    key={item.name}
                                    href={item.href}
                                    className={`flex items-center px-3 py-2 rounded-lg text-sm font-medium transition-all ${isActive
                                            ? 'bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-lg'
                                            : 'text-gray-700 hover:bg-gray-100'
                                        }`}
                                >
                                    <item.icon className={`mr-3 h-5 w-5 ${isActive ? 'text-white' : 'text-gray-500'}`} />
                                    {item.name}
                                </Link>
                            );
                        })}
                    </nav>
                </div>
            </div>

            {/* Static sidebar for desktop */}
            <div className="hidden lg:fixed lg:inset-y-0 lg:flex lg:w-64 lg:flex-col">
                <div className="flex flex-col flex-grow bg-white border-r border-gray-200">
                    <div className="flex h-16 items-center px-6 border-b backdrop-blur-lg bg-white/95">
                        <div className="flex items-center space-x-3">
                            <div className="relative w-10 h-10">
                                <Image
                                    src="/logo.png"
                                    alt="MedBot Logo"
                                    fill
                                    className="object-contain"
                                />
                            </div>
                            <div>
                                <span className="text-base font-bold bg-gradient-to-r from-blue-600 to-teal-600 bg-clip-text text-transparent">
                                    MedBot
                                </span>
                                <p className="text-[10px] text-gray-500 font-semibold tracking-wide">INTELLIGENCE</p>
                            </div>
                        </div>
                    </div>
                    <nav className="flex-1 space-y-1 px-3 py-4">
                        {navigation.map((item) => {
                            const isActive = pathname === item.href;
                            return (
                                <Link
                                    key={item.name}
                                    href={item.href}
                                    className={`flex items-center px-3 py-2.5 rounded-lg text-sm font-medium transition-all ${isActive
                                            ? 'bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-lg transform scale-[1.02]'
                                            : 'text-gray-700 hover:bg-gray-100'
                                        }`}
                                >
                                    <item.icon className={`mr-3 h-5 w-5 ${isActive ? 'text-white' : 'text-gray-500'}`} />
                                    {item.name}
                                </Link>
                            );
                        })}
                    </nav>
                    <div className="border-t p-4 backdrop-blur-lg bg-white/95">
                        <div className="flex items-center space-x-3">
                            <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-500 to-teal-500 flex items-center justify-center text-white font-semibold shadow-md">
                                DM
                            </div>
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-900">Dr. Martinez</p>
                                <p className="text-xs text-gray-500">Cardiologist</p>
                            </div>
                            <button className="text-gray-400 hover:text-red-600 transition-colors">
                                <LogOut className="h-5 w-5" />
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Main content */}
            <div className="lg:pl-64">
                {/* Top navigation */}
                <div className="sticky top-0 z-40 flex h-16 border-b bg-white/80 backdrop-blur-lg shadow-sm">
                    <button
                        type="button"
                        className="px-4 text-gray-500 focus:outline-none lg:hidden"
                        onClick={() => setSidebarOpen(true)}
                    >
                        <Menu className="h-6 w-6" />
                    </button>

                    <div className="flex flex-1 justify-between px-4 sm:px-6 lg:px-8">
                        <div className="flex flex-1 items-center">
                            {/* Search bar could go here */}
                        </div>
                        <div className="ml-4 flex items-center space-x-4">
                            <button className="relative p-2 text-gray-400 hover:text-blue-600 transition-colors rounded-lg hover:bg-blue-50">
                                <Bell className="h-5 w-5" />
                                <span className="absolute top-1 right-1 h-2 w-2 rounded-full bg-red-500"></span>
                            </button>
                            <button className="p-2 text-gray-400 hover:text-blue-600 transition-colors rounded-lg hover:bg-blue-50">
                                <User className="h-5 w-5" />
                            </button>
                        </div>
                    </div>
                </div>

                {/* Page content */}
                <main className="flex-1">
                    <div className="py-6">
                        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                            {children}
                        </div>
                    </div>
                </main>
            </div>
        </div>
    );
}
