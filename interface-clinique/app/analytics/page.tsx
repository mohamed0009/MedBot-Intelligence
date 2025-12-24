'use client';

import DashboardLayout from '../components/DashboardLayout';
import { BarChart3, TrendingUp, Users, FileText, Activity, PieChart } from 'lucide-react';
import { StatCard } from '../components/ui';

export default function AnalyticsPage() {
    return (
        <DashboardLayout>
            <div className="space-y-6">
                {/* Header - Medical Theme Match */}
                <div className="relative gradient-teal rounded-3xl p-8 text-white shadow-lg overflow-hidden">
                    {/* Animated Background */}
                    <div className="absolute top-0 right-0 -mr-20 -mt-20 w-96 h-96 bg-white/10 rounded-full blur-3xl animate-pulse-soft"></div>
                    <div className="absolute bottom-0 left-0 -ml-20 -mb-20 w-96 h-96 bg-white/5 rounded-full blur-3xl"></div>

                    <div className="relative z-10">
                        <div className="flex items-center space-x-4">
                            <div className="w-16 h-16 bg-white/20 backdrop-blur-lg rounded-2xl flex items-center justify-center border border-white/30 shadow-lg">
                                <BarChart3 className="h-8 w-8 text-white" />
                            </div>
                            <div>
                                <h1 className="text-3xl font-bold mb-1">Analytics Dashboard 📊</h1>
                                <p className="text-white/90 text-base">Insights and metrics about your medical data processing</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Key Metrics */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <StatCard
                        label="Total Documents"
                        value="1,245"
                        change={{ value: "+12%", trend: "up" }}
                        icon={FileText}
                        color="teal"
                    />
                    <StatCard
                        label="Active Patients"
                        value="342"
                        change={{ value: "+5%", trend: "up" }}
                        icon={Users}
                        color="green"
                    />
                    <StatCard
                        label="AI Queries"
                        value="8,902"
                        change={{ value: "+24%", trend: "up" }}
                        icon={Activity}
                        color="blue"
                    />
                    <StatCard
                        label="Avg. Processing Time"
                        value="1.2s"
                        change={{ value: "-15%", trend: "down" }}
                        icon={TrendingUp}
                        color="orange"
                    />
                </div>

                {/* Charts Area */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    {/* Document Processing Volume */}
                    <div className="medical-card p-6">
                        <div className="flex justify-between items-center mb-6">
                            <h3 className="text-lg font-semibold text-gray-900">Document Processing Volume</h3>
                            <select className="text-sm border-gray-200 rounded-md text-gray-500">
                                <option>Last 7 days</option>
                                <option>Last 30 days</option>
                            </select>
                        </div>
                        <div className="h-64 flex items-end justify-between space-x-2 px-2">
                            {[40, 65, 45, 80, 55, 90, 70].map((h, i) => (
                                <div key={i} className="w-full bg-teal-50 rounded-t-lg relative group">
                                    <div
                                        className="absolute bottom-0 left-0 right-0 bg-teal-500 rounded-t-lg transition-all duration-500 group-hover:bg-teal-600"
                                        style={{ height: `${h}%` }}
                                    ></div>
                                    <div className="absolute -top-8 left-1/2 transform -translate-x-1/2 bg-gray-800 text-white text-xs py-1 px-2 rounded opacity-0 group-hover:opacity-100 transition-opacity">
                                        {h} docs
                                    </div>
                                </div>
                            ))}
                        </div>
                        <div className="flex justify-between mt-4 text-xs text-gray-400">
                            <span>Mon</span><span>Tue</span><span>Wed</span><span>Thu</span><span>Fri</span><span>Sat</span><span>Sun</span>
                        </div>
                    </div>

                    {/* Document Types Distribution */}
                    <div className="medical-card p-6">
                        <div className="flex justify-between items-center mb-6">
                            <h3 className="text-lg font-semibold text-gray-900">Document Types</h3>
                            <PieChart className="h-5 w-5 text-gray-400" />
                        </div>
                        <div className="flex items-center justify-center h-64">
                            <div className="relative w-48 h-48 rounded-full border-[16px] border-teal-50 flex items-center justify-center">
                                <div className="absolute inset-0 rounded-full border-[16px] border-teal-500 border-l-transparent border-b-transparent rotate-45"></div>
                                <div className="absolute inset-0 rounded-full border-[16px] border-green-500 border-l-transparent border-t-transparent border-r-transparent -rotate-12"></div>
                                <div className="text-center">
                                    <span className="text-3xl font-bold text-gray-900">1.2k</span>
                                    <p className="text-xs text-gray-500">Total</p>
                                </div>
                            </div>
                        </div>
                        <div className="grid grid-cols-2 gap-4 mt-4">
                            <div className="flex items-center">
                                <div className="w-3 h-3 rounded-full bg-teal-500 mr-2"></div>
                                <span className="text-sm text-gray-600">Clinical Reports (45%)</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-3 h-3 rounded-full bg-green-500 mr-2"></div>
                                <span className="text-sm text-gray-600">Lab Results (30%)</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-3 h-3 rounded-full bg-teal-200 mr-2"></div>
                                <span className="text-sm text-gray-600">Prescriptions (15%)</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-3 h-3 rounded-full bg-gray-200 mr-2"></div>
                                <span className="text-sm text-gray-600">Others (10%)</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </DashboardLayout>
    );
}
