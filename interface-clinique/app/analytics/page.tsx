'use client';

import DashboardLayout from '../components/DashboardLayout';
import { BarChart3, TrendingUp, Users, FileText, Activity, PieChart } from 'lucide-react';

export default function AnalyticsPage() {
    return (
        <DashboardLayout>
            <div className="space-y-6">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Analytics Dashboard</h1>
                    <p className="text-gray-600 mt-1">Insights and metrics about your medical data processing</p>
                </div>

                {/* Key Metrics */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    {[
                        { label: 'Total Documents', value: '1,245', change: '+12%', icon: FileText, color: 'blue' },
                        { label: 'Active Patients', value: '342', change: '+5%', icon: Users, color: 'teal' },
                        { label: 'AI Queries', value: '8,902', change: '+24%', icon: Activity, color: 'purple' },
                        { label: 'Avg. Processing Time', value: '1.2s', change: '-15%', icon: TrendingUp, color: 'green' },
                    ].map((stat, i) => (
                        <div key={i} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                            <div className="flex justify-between items-start">
                                <div>
                                    <p className="text-sm font-medium text-gray-500">{stat.label}</p>
                                    <h3 className="text-2xl font-bold text-gray-900 mt-2">{stat.value}</h3>
                                </div>
                                <div className={`p-3 rounded-lg bg-${stat.color}-50 text-${stat.color}-600`}>
                                    <stat.icon className="h-6 w-6" />
                                </div>
                            </div>
                            <div className="mt-4 flex items-center text-sm">
                                <span className="text-green-600 font-medium">{stat.change}</span>
                                <span className="text-gray-400 ml-2">vs last month</span>
                            </div>
                        </div>
                    ))}
                </div>

                {/* Charts Area */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    {/* Document Processing Volume */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                        <div className="flex justify-between items-center mb-6">
                            <h3 className="text-lg font-semibold text-gray-900">Document Processing Volume</h3>
                            <select className="text-sm border-gray-200 rounded-md text-gray-500">
                                <option>Last 7 days</option>
                                <option>Last 30 days</option>
                            </select>
                        </div>
                        <div className="h-64 flex items-end justify-between space-x-2 px-2">
                            {[40, 65, 45, 80, 55, 90, 70].map((h, i) => (
                                <div key={i} className="w-full bg-blue-100 rounded-t-lg relative group">
                                    <div
                                        className="absolute bottom-0 left-0 right-0 bg-blue-600 rounded-t-lg transition-all duration-500 group-hover:bg-blue-700"
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
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                        <div className="flex justify-between items-center mb-6">
                            <h3 className="text-lg font-semibold text-gray-900">Document Types</h3>
                            <PieChart className="h-5 w-5 text-gray-400" />
                        </div>
                        <div className="flex items-center justify-center h-64">
                            <div className="relative w-48 h-48 rounded-full border-[16px] border-blue-100 flex items-center justify-center">
                                <div className="absolute inset-0 rounded-full border-[16px] border-blue-600 border-l-transparent border-b-transparent rotate-45"></div>
                                <div className="absolute inset-0 rounded-full border-[16px] border-teal-500 border-l-transparent border-t-transparent border-r-transparent -rotate-12"></div>
                                <div className="text-center">
                                    <span className="text-3xl font-bold text-gray-900">1.2k</span>
                                    <p className="text-xs text-gray-500">Total</p>
                                </div>
                            </div>
                        </div>
                        <div className="grid grid-cols-2 gap-4 mt-4">
                            <div className="flex items-center">
                                <div className="w-3 h-3 rounded-full bg-blue-600 mr-2"></div>
                                <span className="text-sm text-gray-600">Clinical Reports (45%)</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-3 h-3 rounded-full bg-teal-500 mr-2"></div>
                                <span className="text-sm text-gray-600">Lab Results (30%)</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-3 h-3 rounded-full bg-blue-100 mr-2"></div>
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
