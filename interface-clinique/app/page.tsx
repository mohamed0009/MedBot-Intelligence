'use client';

import { useEffect, useState } from 'react';
import DashboardLayout from './components/DashboardLayout';
import { FileText, CheckCircle, MessageSquare, Clock, TrendingUp, ArrowUpRight, Activity } from 'lucide-react';
import { api } from './utils/api';

export default function Home() {
  const [stats, setStats] = useState({
    totalDocuments: 0,
    totalIndexed: 0,
    totalQueries: 0,
    processingSuccess: 0
  });

  useEffect(() => {
    const fetchStats = async () => {
      const data = await api.getStats();
      setStats(data);
    };
    fetchStats();
  }, []);

  const statCards = [
    {
      name: 'Total Documents',
      value: stats.totalDocuments.toString(),
      change: '+12%',
      changeType: 'positive',
      icon: FileText,
      color: 'from-blue-500 to-blue-600',
    },
    {
      name: 'Indexed Chunks',
      value: stats.totalIndexed.toString(),
      change: '+100%',
      changeType: 'positive',
      icon: CheckCircle,
      color: 'from-green-500 to-green-600',
    },
    {
      name: 'AI Queries',
      value: stats.totalQueries.toString(),
      change: '+18%',
      changeType: 'positive',
      icon: MessageSquare,
      color: 'from-purple-500 to-purple-600',
    },
    {
      name: 'Avg Response Time',
      value: '1.8s',
      change: '-0.5s',
      changeType: 'positive',
      icon: Clock,
      color: 'from-teal-500 to-teal-600',
    },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Welcome Section */}
        <div className="bg-gradient-to-r from-blue-600 via-blue-700 to-teal-600 rounded-2xl p-8 text-white shadow-xl relative overflow-hidden">
          <div className="relative z-10 flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold mb-2">Welcome back, Dr. Martinez! 👋</h1>
              <p className="text-blue-100 text-lg">
                Your AI-powered medical assistant is ready to help you analyze patient data
              </p>
            </div>
            <Activity className="h-16 w-16 text-white/20" />
          </div>
          <div className="absolute top-0 right-0 -mr-16 -mt-16 w-64 h-64 bg-white/10 rounded-full blur-3xl"></div>
          <div className="absolute bottom-0 left-0 -ml-16 -mb-16 w-64 h-64 bg-teal-500/20 rounded-full blur-3xl"></div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {statCards.map((stat) => (
            <div
              key={stat.name}
              className="relative overflow-hidden rounded-xl bg-white p-6 shadow-sm border border-gray-100 hover:shadow-md transition-all duration-300 card-hover"
            >
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">{stat.name}</p>
                  <p className="mt-2 text-3xl font-bold text-gray-900">{stat.value}</p>
                  <div className="mt-2 flex items-center space-x-2">
                    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${stat.changeType === 'positive' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      }`}>
                      <ArrowUpRight className="mr-1 h-3 w-3" />
                      {stat.change}
                    </span>
                  </div>
                </div>
                <div className={`p-3 rounded-xl bg-gradient-to-br ${stat.color} shadow-lg`}>
                  <stat.icon className="h-6 w-6 text-white" />
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Recent Activity & Quick Actions */}
        <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
          {/* Recent Activity */}
          <div className="lg:col-span-2 bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-xl font-bold text-gray-900">Recent Activity</h2>
              <button className="text-sm font-medium text-blue-600 hover:text-blue-700">
                View All
              </button>
            </div>
            <div className="space-y-4">
              {[
                { title: 'Patient PAT001 - Summary Generated', desc: 'Comprehensive medical summary created', time: '5 mins ago', icon: CheckCircle, color: 'text-green-600 bg-green-50' },
                { title: 'Document Uploaded', desc: 'Lab_Results_2024.pdf processing...', time: '12 mins ago', icon: Activity, color: 'text-blue-600 bg-blue-50' },
                { title: 'Q&A Session', desc: '5 questions answered about diabetes', time: '1 hour ago', icon: MessageSquare, color: 'text-purple-600 bg-purple-50' }
              ].map((activity, i) => (
                <div key={i} className="flex items-start space-x-4 p-4 rounded-lg hover:bg-gray-50 transition-colors border border-transparent hover:border-gray-100">
                  <div className={`p-2 rounded-lg ${activity.color}`}>
                    <activity.icon className="h-5 w-5" />
                  </div>
                  <div className="flex-1">
                    <h3 className="text-sm font-semibold text-gray-900">{activity.title}</h3>
                    <p className="text-sm text-gray-600 mt-1">{activity.desc}</p>
                    <p className="text-xs text-gray-400 mt-2">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Quick Actions */}
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <h2 className="text-xl font-bold text-gray-900 mb-6">Quick Actions</h2>
            <div className="space-y-3">
              <button className="w-full flex items-center justify-center space-x-2 bg-gradient-to-r from-blue-600 to-blue-700 text-white px-4 py-3 rounded-lg hover:from-blue-700 hover:to-blue-800 transition-all shadow-md hover:shadow-lg">
                <FileText className="h-5 w-5" />
                <span className="font-medium">Upload Document</span>
              </button>
              <button className="w-full flex items-center justify-center space-x-2 bg-gradient-to-r from-purple-600 to-purple-700 text-white px-4 py-3 rounded-lg hover:from-purple-700 hover:to-purple-800 transition-all shadow-md hover:shadow-lg">
                <MessageSquare className="h-5 w-5" />
                <span className="font-medium">Ask AI Question</span>
              </button>
              <button className="w-full flex items-center justify-center space-x-2 bg-gradient-to-r from-teal-600 to-teal-700 text-white px-4 py-3 rounded-lg hover:from-teal-700 hover:to-teal-800 transition-all shadow-md hover:shadow-lg">
                <TrendingUp className="h-5 w-5" />
                <span className="font-medium">Generate Report</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
