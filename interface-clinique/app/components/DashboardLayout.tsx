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
import { usePathname, useRouter } from 'next/navigation';
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
    const [searchQuery, setSearchQuery] = useState('');
    const [showNotifications, setShowNotifications] = useState(false);
    const [showUserMenu, setShowUserMenu] = useState(false);
    const pathname = usePathname();
    const router = useRouter();

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            router.push(`/search?q=${encodeURIComponent(searchQuery)}`);
        }
    };

    const handleSearchKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter') {
            handleSearch(e as any);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Skip Navigation Link for Accessibility */}
            <a
                href="#main-content"
                className="sr-only focus:not-sr-only focus:absolute focus:top-4 focus:left-4 bg-blue-600 text-white px-4 py-2 rounded-lg z-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
                Skip to main content
            </a>

            {/* Sidebar for mobile with animation - UPGRADED */}
            <div className={`fixed inset-0 z-50 lg:hidden transition-opacity duration-300 ${sidebarOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
                }`}>
                <div
                    className="fixed inset-0 bg-gray-900 bg-opacity-75 transition-opacity backdrop-blur-sm"
                    onClick={() => setSidebarOpen(false)}
                    aria-hidden="true"
                />
                <div className={`fixed inset-y-0 left-0 flex w-72 flex-col bg-gradient-to-b from-slate-900 via-slate-800 to-slate-900 shadow-2xl transform transition-transform duration-300 ease-in-out ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'
                    }`}>
                    {/* Logo Section */}
                    <div className="flex h-20 items-center justify-between px-6 border-b border-slate-700/50 bg-slate-900/50 backdrop-blur-xl">
                        <div className="flex items-center space-x-3">
                            <div className="relative w-14 h-14 bg-white rounded-xl shadow-lg flex items-center justify-center p-2">
                                <Image
                                    src="/logo.png"
                                    alt="MedBot Logo"
                                    fill
                                    className="object-contain p-1"
                                />
                            </div>
                            <div>
                                <span className="text-lg font-bold bg-gradient-to-r from-blue-400 via-teal-400 to-blue-400 bg-clip-text text-transparent">
                                    MedBot
                                </span>
                                <p className="text-[10px] text-slate-400 font-semibold tracking-widest">INTELLIGENCE</p>
                            </div>
                        </div>
                        <button
                            onClick={() => setSidebarOpen(false)}
                            className="text-slate-400 hover:text-white transition-colors p-2 hover:bg-slate-800/50 rounded-lg"
                            aria-label="Close sidebar"
                        >
                            <X className="h-6 w-6" />
                        </button>
                    </div>

                    {/* Navigation */}
                    <nav className="flex-1 space-y-1 px-4 py-6">
                        {navigation.map((item) => {
                            const isActive = pathname === item.href;
                            return (
                                <Link
                                    key={item.name}
                                    href={item.href}
                                    className={`group flex items-center px-4 py-3 rounded-xl text-sm font-medium transition-all duration-200 ${isActive
                                        ? 'bg-gradient-to-r from-blue-600 to-teal-600 text-white shadow-lg shadow-blue-500/30'
                                        : 'text-slate-300 hover:bg-slate-800/50 hover:text-white'
                                        }`}
                                    onClick={() => setSidebarOpen(false)}
                                >
                                    <item.icon className={`mr-3 h-5 w-5 transition-transform group-hover:scale-110 ${isActive ? 'text-white' : 'text-slate-400'
                                        }`} />
                                    <span className="flex-1">{item.name}</span>
                                    {isActive && (
                                        <div className="w-2 h-2 rounded-full bg-white animate-pulse"></div>
                                    )}
                                </Link>
                            );
                        })}
                    </nav>
                </div>
            </div>

            {/* Static sidebar for desktop - UPGRADED */}
            <div className="hidden lg:fixed lg:inset-y-0 lg:flex lg:w-72 lg:flex-col">
                <div className="flex flex-col flex-grow bg-gradient-to-b from-slate-900 via-slate-800 to-slate-900 shadow-2xl">
                    {/* Logo Section - Enhanced */}
                    <div className="flex h-20 items-center px-6 border-b border-slate-700/50 bg-slate-900/50 backdrop-blur-xl">
                        <div className="flex items-center space-x-3">
                            <div className="relative w-14 h-14 bg-white rounded-xl shadow-lg flex items-center justify-center p-2">
                                <Image
                                    src="/logo.png"
                                    alt="MedBot Logo"
                                    fill
                                    className="object-contain p-1"
                                />
                            </div>
                            <div>
                                <span className="text-lg font-bold bg-gradient-to-r from-blue-400 via-teal-400 to-blue-400 bg-clip-text text-transparent">
                                    MedBot
                                </span>
                                <p className="text-[10px] text-slate-400 font-semibold tracking-widest">INTELLIGENCE</p>
                            </div>
                        </div>
                    </div>

                    {/* Navigation - Enhanced */}
                    <nav className="flex-1 space-y-1 px-4 py-6">
                        {navigation.map((item) => {
                            const isActive = pathname === item.href;
                            return (
                                <Link
                                    key={item.name}
                                    href={item.href}
                                    className={`group flex items-center px-4 py-3 rounded-xl text-sm font-medium transition-all duration-200 ${isActive
                                        ? 'bg-gradient-to-r from-blue-600 to-teal-600 text-white shadow-lg shadow-blue-500/30'
                                        : 'text-slate-300 hover:bg-slate-800/50 hover:text-white'
                                        }`}
                                >
                                    <item.icon className={`mr-3 h-5 w-5 transition-transform group-hover:scale-110 ${isActive ? 'text-white' : 'text-slate-400'
                                        }`} />
                                    <span className="flex-1">{item.name}</span>
                                    {isActive && (
                                        <div className="w-2 h-2 rounded-full bg-white animate-pulse"></div>
                                    )}
                                </Link>
                            );
                        })}
                    </nav>

                    {/* User Profile - Enhanced */}
                    <div className="border-t border-slate-700/50 p-4 bg-slate-900/50 backdrop-blur-xl">
                        <div className="bg-slate-800/50 rounded-xl p-4 border border-slate-700/50 hover:bg-slate-800/70 transition-all cursor-pointer">
                            <div className="flex items-center space-x-3">
                                <div className="relative">
                                    <div className="w-11 h-11 rounded-xl bg-gradient-to-br from-blue-500 to-teal-500 flex items-center justify-center text-white font-bold shadow-lg text-lg">
                                        DM
                                    </div>
                                    <div className="absolute -bottom-1 -right-1 w-4 h-4 bg-green-500 rounded-full border-2 border-slate-900"></div>
                                </div>
                                <div className="flex-1 min-w-0">
                                    <p className="text-sm font-semibold text-white truncate">Dr. Martinez</p>
                                    <p className="text-xs text-slate-400">Cardiologist</p>
                                </div>
                                <button
                                    className="text-slate-400 hover:text-red-400 transition-colors p-1"
                                    aria-label="Logout"
                                >
                                    <LogOut className="h-4 w-4" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Main content */}
            <div className="lg:pl-72">
                {/* Top navigation - UPGRADED */}
                <div className="sticky top-0 z-40 flex h-16 border-b bg-white/70 backdrop-blur-xl shadow-sm border-gray-200/50">
                    <button
                        type="button"
                        className="px-4 focus:outline-none lg:hidden hover:opacity-80 transition-opacity flex items-center"
                        onClick={() => setSidebarOpen(true)}
                        aria-label="Open sidebar"
                    >
                        <div className="relative w-8 h-8 bg-white rounded-lg shadow-md flex items-center justify-center p-1">
                            <Image
                                src="/logo.png"
                                alt="MedBot Logo"
                                fill
                                className="object-contain p-0.5"
                            />
                        </div>
                    </button>

                    <div className="flex flex-1 justify-between px-4 sm:px-6 lg:px-8">
                        {/* Search Bar - FUNCTIONAL */}
                        <div className="flex flex-1 items-center max-w-2xl">
                            <form onSubmit={handleSearch} className="relative w-full">
                                <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                                    <Search className="h-5 w-5 text-gray-400" />
                                </div>
                                <input
                                    type="text"
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    onKeyPress={handleSearchKeyPress}
                                    placeholder="Search patients, documents, or ask AI..."
                                    className="block w-full pl-10 pr-3 py-2 border border-gray-200 rounded-xl text-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-gray-50/50 hover:bg-white transition-colors"
                                />
                            </form>
                        </div>

                        {/* Right Actions - FUNCTIONAL */}
                        <div className="ml-4 flex items-center space-x-3">
                            {/* Notifications */}
                            <div className="relative">
                                <button
                                    onClick={() => setShowNotifications(!showNotifications)}
                                    className="relative p-2.5 text-gray-400 hover:text-blue-600 transition-colors rounded-xl hover:bg-blue-50 group"
                                    aria-label="View notifications"
                                >
                                    <Bell className="h-5 w-5" />
                                    <span className="absolute top-1.5 right-1.5 flex h-4 w-4">
                                        <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                                        <span className="relative inline-flex rounded-full h-4 w-4 bg-red-500 items-center justify-center text-[10px] font-bold text-white">3</span>
                                    </span>
                                </button>

                                {/* Notifications Dropdown */}
                                {showNotifications && (
                                    <div className="absolute right-0 mt-2 w-80 bg-white rounded-xl shadow-2xl border border-gray-200 z-50 overflow-hidden">
                                        <div className="p-4 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-teal-50">
                                            <h3 className="text-sm font-semibold text-gray-900">Notifications</h3>
                                        </div>
                                        <div className="max-h-96 overflow-y-auto">
                                            {[
                                                { id: 1, title: 'New patient record uploaded', time: '5 min ago', type: 'success', link: '/patients' },
                                                { id: 2, title: 'Document processing complete', time: '1 hour ago', type: 'info', link: '/docs' },
                                                { id: 3, title: 'AI analysis ready for review', time: '2 hours ago', type: 'warning', link: '/qa' },
                                            ].map((notif) => (
                                                <div
                                                    key={notif.id}
                                                    onClick={() => {
                                                        router.push(notif.link);
                                                        setShowNotifications(false);
                                                    }}
                                                    className="p-4 hover:bg-gray-50 border-b border-gray-100 cursor-pointer transition-colors"
                                                >
                                                    <p className="text-sm font-medium text-gray-900">{notif.title}</p>
                                                    <p className="text-xs text-gray-500 mt-1">{notif.time}</p>
                                                </div>
                                            ))}
                                        </div>
                                        <div className="p-3 bg-gray-50 text-center">
                                            <button
                                                onClick={() => {
                                                    router.push('/audit');
                                                    setShowNotifications(false);
                                                }}
                                                className="text-sm text-blue-600 hover:text-blue-700 font-medium"
                                            >
                                                View all notifications
                                            </button>
                                        </div>
                                    </div>
                                )}
                            </div>

                            {/* User Menu */}
                            <div className="relative">
                                <button
                                    onClick={() => setShowUserMenu(!showUserMenu)}
                                    className="p-2.5 text-gray-400 hover:text-blue-600 transition-colors rounded-xl hover:bg-blue-50"
                                    aria-label="User menu"
                                >
                                    <User className="h-5 w-5" />
                                </button>

                                {/* User Menu Dropdown */}
                                {showUserMenu && (
                                    <div className="absolute right-0 mt-2 w-56 bg-white rounded-xl shadow-2xl border border-gray-200 z-50 overflow-hidden">
                                        <div className="p-4 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-teal-50">
                                            <p className="text-sm font-semibold text-gray-900">Dr. Martinez</p>
                                            <p className="text-xs text-gray-500">dr.martinez@medbot.hospital</p>
                                        </div>
                                        <div className="py-2">
                                            <Link
                                                href="/settings"
                                                onClick={() => setShowUserMenu(false)}
                                                className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 transition-colors"
                                            >
                                                <div className="flex items-center">
                                                    <Settings className="h-4 w-4 mr-2" />
                                                    Settings
                                                </div>
                                            </Link>
                                            <Link
                                                href="/patients"
                                                onClick={() => setShowUserMenu(false)}
                                                className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 transition-colors"
                                            >
                                                <div className="flex items-center">
                                                    <Users className="h-4 w-4 mr-2" />
                                                    My Patients
                                                </div>
                                            </Link>
                                            <button
                                                onClick={() => {
                                                    setShowUserMenu(false);
                                                    alert('Logout functionality - Ready for backend integration');
                                                }}
                                                className="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors"
                                            >
                                                <div className="flex items-center">
                                                    <LogOut className="h-4 w-4 mr-2" />
                                                    Logout
                                                </div>
                                            </button>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>

                {/* Page content */}
                <main className="flex-1" id="main-content" role="main">
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
